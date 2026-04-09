import React, { useState } from 'react';
import { useCart } from '../CartContext';
import { useAuth } from '../AuthContext';
import { createOrder } from '../api/api';

function CartPage() {
  const { cartItems, removeFromCart, clearCart } = useCart();
  const { user } = useAuth();

  // if logged in - prefill name with username, otherwise empty
  const [customerName, setCustomerName] = useState(user?.username || '');
  const [orderSuccess, setOrderSuccess] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const total = cartItems.reduce((sum, item) => sum + item.product.price * item.quantity, 0);

  const handleCheckout = () => {
    if (!customerName.trim()) {
      setError('Please enter your name.');
      return;
    }
    setLoading(true);
    setError(null);

    const orderRequest = {
      customerName,
      items: cartItems.map(item => ({
        productId: item.product.id,
        quantity: item.quantity,
      })),
    };

    createOrder(orderRequest)
      .then(() => {
        clearCart();
        setOrderSuccess(true);
      })
      .catch(() => setError('Failed to place order. Try again.'))
      .finally(() => setLoading(false));
  };

  if (orderSuccess) {
    return (
      <div className="text-center mt-5">
        <div className="display-1">🎉</div>
        <h2 className="mt-3">Order placed successfully!</h2>
        <p className="text-muted">Thank you, {customerName}!</p>
        <a href="/" className="btn btn-primary mt-3">Continue Shopping</a>
      </div>
    );
  }

  if (cartItems.length === 0) {
    return (
      <div className="text-center mt-5">
        <div className="display-1">🛒</div>
        <h4 className="mt-3">Your cart is empty</h4>
        <a href="/" className="btn btn-primary mt-3">Go Shopping</a>
      </div>
    );
  }

  return (
    <div className="row">
      <div className="col-md-8">
        <h2 className="mb-4">Your Cart</h2>
        {cartItems.map(item => (
          <div className="card mb-3" key={item.product.id}>
            <div className="card-body d-flex justify-content-between align-items-center">
              <div>
                <h5 className="mb-1">{item.product.name}</h5>
                <small className="text-muted">
                  ${item.product.price.toFixed(2)} × {item.quantity}
                </small>
              </div>
              <div className="d-flex align-items-center gap-3">
                <span className="fw-bold fs-5">
                  ${(item.product.price * item.quantity).toFixed(2)}
                </span>
                <button
                  className="btn btn-outline-danger btn-sm"
                  onClick={() => removeFromCart(item.product.id)}
                >
                  Remove
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="col-md-4">
        <div className="card shadow-sm">
          <div className="card-body">
            <h4 className="card-title">Order Summary</h4>
            <hr />
            <div className="d-flex justify-content-between fs-5 fw-bold mb-3">
              <span>Total:</span>
              <span>${total.toFixed(2)}</span>
            </div>
            <input
              type="text"
              className="form-control mb-3"
              placeholder="Your name"
              value={customerName}
              onChange={e => setCustomerName(e.target.value)}
              // if logged in - field is readonly, name is always the username
              readOnly={!!user}
            />
            {user && (
              <small className="text-muted d-block mb-3">
                Ordering as <strong>{user.username}</strong>
              </small>
            )}
            {error && <div className="alert alert-danger py-2">{error}</div>}
            <button
              className="btn btn-success w-100"
              onClick={handleCheckout}
              disabled={loading}
            >
              {loading ? 'Placing order...' : 'Place Order'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CartPage;
