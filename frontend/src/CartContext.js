import React, { createContext, useContext, useState, useEffect } from 'react';
import { useAuth } from './AuthContext';
import { fetchCart, addToDbCart, removeFromDbCart, clearDbCart } from './api/api';

const CartContext = createContext();

export function CartProvider({ children }) {
  const { user } = useAuth();

  // load guest cart from localStorage on startup
  const [localItems, setLocalItemsw] = useState(() => {
    try {
      const saved = localStorage.getItem('guestCart');
      return saved ? JSON.parse(saved) : [];
    } catch {
      return [];
    }
  });

  const [dbCart, setDbCart] = useState(null);

  // whenever localItems changes - save to localStorage
  useEffect(() => {
    localStorage.setItem('guestCart', JSON.stringify(localItems));
  }, [localItems]);

  // when user logs in or out, reload cart from db
  useEffect(() => {
    if (user) {
      fetchCart(user.id)
          .then(res => setDbCart(res.data))
          .catch(() => setDbCart(null));
    } else {
      setDbCart(null);
    }
  }, [user]);

  // pick the right cart depending on auth status
  const cartItems = user
      ? (dbCart?.items || []).map(item => ({
        product: item.product,
        quantity: item.quantity,
      }))
      : localItems;

  const addToCart = async (product) => {
    if (user) {
      const res = await addToDbCart(user.id, { productId: product.id, quantity: 1 });
      setDbCart(res.data);
    } else {
      setLocalItems(prev => {
        const existing = prev.find(i => i.product.id === product.id);
        if (existing) {
          return prev.map(i =>
              i.product.id === product.id ? { ...i, quantity: i.quantity + 1 } : i
          );
        }
        return [...prev, { product, quantity: 1 }];
      });
    }
  };

  const removeFromCart = async (productId) => {
    if (user) {
      const res = await removeFromDbCart(user.id, productId);
      setDbCart(res.data);
    } else {
      setLocalItems(prev => prev.filter(i => i.product.id !== productId));
    }
  };

  const clearCart = async () => {
    if (user) {
      const res = await clearDbCart(user.id);
      setDbCart(res.data);
    } else {
      setLocalItems([]);
      localStorage.removeItem('guestCart'); // clean up localStorage too
    }
  };

  const totalCount = cartItems.reduce((sum, item) => sum + item.quantity, 0);

  return (
      <CartContext.Provider value={{ cartItems, addToCart, removeFromCart, clearCart, totalCount }}>
        {children}
      </CartContext.Provider>
  );
}

export const useCart = () => useContext(CartContext);