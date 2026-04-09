import React from 'react';
import { BrowserRouter, Routes, Route, Link, NavLink, useNavigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './AuthContext';
import { CartProvider, useCart } from './CartContext';
import ProductsPage from './pages/ProductsPage';
import ProductDetailPage from './pages/ProductDetailPage';
import CartPage from './pages/CartPage';
import AdminPage from './pages/AdminPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProfilePage from './pages/ProfilePage';

function Navbar() {
  const { totalCount } = useCart();
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container">
        <Link className="navbar-brand fw-bold" to="/">Vlad's Super Shop</Link>
        <div className="navbar-nav ms-auto d-flex flex-row gap-3 align-items-center">
          <NavLink className="nav-link text-white" to="/">Products</NavLink>
          <NavLink className="nav-link text-white" to="/cart">
            Cart {totalCount > 0 && (
              <span className="badge bg-danger">{totalCount}</span>
            )}
          </NavLink>
          <NavLink className="nav-link text-white" to="/admin">Admin</NavLink>

          {user ? (
            <>
              <NavLink className="nav-link text-white" to="/profile">
                👤 {user.username}
              </NavLink>
              <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>
                Logout
              </button>
            </>
          ) : (
            <NavLink className="btn btn-outline-light btn-sm" to="/login">
              Login
            </NavLink>
          )}
        </div>
      </div>
    </nav>
  );
}

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <BrowserRouter>
          <Navbar />
          <div className="container mt-4">
            <Routes>
              <Route path="/"              element={<ProductsPage />} />
              <Route path="/products/:id" element={<ProductDetailPage />} />
              <Route path="/cart"         element={<CartPage />} />
              <Route path="/admin"        element={<AdminPage />} />
              <Route path="/login"        element={<LoginPage />} />
              <Route path="/register"     element={<RegisterPage />} />
              <Route path="/profile"      element={<ProfilePage />} />
            </Routes>
          </div>
        </BrowserRouter>
      </CartProvider>
    </AuthProvider>
  );
}

export default App;
