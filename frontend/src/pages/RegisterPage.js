import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { registerApi } from '../api/api';
import { useAuth } from '../AuthContext';

function RegisterPage() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    registerApi({ username, email, password })
      .then(res => {
        login(res.data); // auto-login after register
        navigate('/');
      })
      .catch(err => {
        const msg = err.response?.data?.error || 'Registration failed';
        setError(msg);
      })
      .finally(() => setLoading(false));
  };

  return (
    <div className="row justify-content-center mt-5">
      <div className="col-md-5">
        <div className="card shadow-sm">
          <div className="card-body p-4">
            <h3 className="mb-4 text-center">Create Account</h3>
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Username</label>
                <input type="text" className="form-control" placeholder="yourname"
                  value={username} onChange={e => setUsername(e.target.value)} required />
              </div>
              <div className="mb-3">
                <label className="form-label">Email</label>
                <input type="email" className="form-control" placeholder="you@example.com"
                  value={email} onChange={e => setEmail(e.target.value)} required />
              </div>
              <div className="mb-3">
                <label className="form-label">Password</label>
                <input type="password" className="form-control" placeholder="at least 6 chars"
                  value={password} onChange={e => setPassword(e.target.value)} minLength={6} required />
              </div>
              <button type="submit" className="btn btn-success w-100" disabled={loading}>
                {loading ? 'Creating account...' : 'Register'}
              </button>
            </form>
            <p className="text-center mt-3 mb-0">
              Already have an account? <Link to="/login">Login</Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default RegisterPage;
