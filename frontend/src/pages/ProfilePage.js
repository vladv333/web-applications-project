import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import { updateUser, getUserOrders, changePassword } from '../api/api';

function ProfilePage() {
  const { user, login, logout } = useAuth();
  const navigate = useNavigate();

  // profile fields
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [saveMsg, setSaveMsg] = useState(null);
  const [saveError, setSaveError] = useState(null);
  const [saving, setSaving] = useState(false);

  // password fields
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [passMsg, setPassMsg] = useState(null);
  const [passError, setPassError] = useState(null);
  const [changingPass, setChangingPass] = useState(false);

  // orders
  const [orders, setOrders] = useState([]);
  const [ordersLoading, setOrdersLoading] = useState(true);

  useEffect(() => {
    if (!user) {
      navigate('/login');
      return;
    }
    setUsername(user.username);
    setEmail(user.email);

    getUserOrders(user.id)
        .then(res => setOrders(res.data))
        .catch(() => setOrders([]))
        .finally(() => setOrdersLoading(false));
  }, [user, navigate]);

  const handleSave = (e) => {
    e.preventDefault();
    setSaving(true);
    setSaveMsg(null);
    setSaveError(null);

    updateUser(user.id, { username, email })
        .then(res => {
          login(res.data);
          setSaveMsg('Profile updated!');
        })
        .catch(err => {
          const msg = err.response?.data?.error || 'Failed to update profile';
          setSaveError(msg);
        })
        .finally(() => setSaving(false));
  };

  const handleChangePassword = (e) => {
    e.preventDefault();
    setPassMsg(null);
    setPassError(null);

    // check new passwords match before sending to backend
    if (newPassword !== confirmPassword) {
      setPassError('New passwords do not match');
      return;
    }

    if (newPassword.length < 6) {
      setPassError('Password must be at least 6 characters');
      return;
    }

    setChangingPass(true);

    changePassword(user.id, { currentPassword, newPassword })
        .then(() => {
          setPassMsg('Password changed!');
          // clear the fields
          setCurrentPassword('');
          setNewPassword('');
          setConfirmPassword('');
        })
        .catch(err => {
          const msg = err.response?.data?.error || 'Failed to change password';
          setPassError(msg);
        })
        .finally(() => setChangingPass(false));
  };

  if (!user) return null;

  return (
      <div className="row g-4 mt-1">

        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">


              <hr />


              <h6 className="mb-3">Edit Profile</h6>
              {saveMsg   && <div className="alert alert-success py-2">{saveMsg}</div>}
              {saveError && <div className="alert alert-danger  py-2">{saveError}</div>}
              <form onSubmit={handleSave}>
                <div className="mb-3">
                  <label className="form-label">Username</label>
                  <input type="text" className="form-control" value={username}
                         onChange={e => setUsername(e.target.value)} required />
                </div>
                <div className="mb-3">
                  <label className="form-label">Email</label>
                  <input type="email" className="form-control" value={email}
                         onChange={e => setEmail(e.target.value)} required />
                </div>
                <button type="submit" className="btn btn-primary w-100" disabled={saving}>
                  {saving ? 'Saving...' : 'Save Changes'}
                </button>
              </form>

              <hr />

              <h6 className="mb-3">Change Password</h6>
              {passMsg   && <div className="alert alert-success py-2">{passMsg}</div>}
              {passError && <div className="alert alert-danger  py-2">{passError}</div>}
              <form onSubmit={handleChangePassword}>
                <div className="mb-3">
                  <label className="form-label">Current Password</label>
                  <input type="password" className="form-control"
                         value={currentPassword}
                         onChange={e => setCurrentPassword(e.target.value)}
                         required />
                </div>
                <div className="mb-3">
                  <label className="form-label">New Password</label>
                  <input type="password" className="form-control"
                         value={newPassword}
                         onChange={e => setNewPassword(e.target.value)}
                         required />
                </div>
                <div className="mb-3">
                  <label className="form-label">Confirm New Password</label>
                  <input type="password" className="form-control"
                         value={confirmPassword}
                         onChange={e => setConfirmPassword(e.target.value)}
                         required />
                </div>
                <button type="submit" className="btn btn-warning w-100" disabled={changingPass}>
                  {changingPass ? 'Changing...' : 'Change Password'}
                </button>
              </form>

              <hr />

              <button
                  className="btn btn-outline-danger w-100"
                  onClick={() => { logout(); navigate('/'); }}
              >
                Logout
              </button>
            </div>
          </div>
        </div>

        <div className="col-md-8">
          <h4 className="mb-3">Order History</h4>

          {ordersLoading && <div className="spinner-border" />}

          {!ordersLoading && orders.length === 0 && (
              <div className="text-center text-muted mt-5">
                <p className="fs-5">No orders yet</p>
                <a href="/" className="btn btn-primary">Start Shopping</a>
              </div>
          )}

          {orders.map(order => (
              <div className="card mb-3 shadow-sm" key={order.id}>
                <div className="card-header d-flex justify-content-between align-items-center">
                  <span className="fw-bold">Order #{order.id}</span>
                  <span className="text-muted" style={{ fontSize: 13 }}>
                {new Date(order.createdAt).toLocaleDateString('en-GB', {
                  day: '2-digit', month: 'short', year: 'numeric',
                  hour: '2-digit', minute: '2-digit'
                })}
              </span>
                </div>
                <div className="card-body">
                  {order.items?.map(item => (
                      <div key={item.id} className="d-flex justify-content-between mb-1">
                        <span>{item.product?.name} × {item.quantity}</span>
                        <span className="text-muted">${item.price?.toFixed(2)}</span>
                      </div>
                  ))}
                  <hr className="my-2" />
                  <div className="d-flex justify-content-between fw-bold">
                    <span>Total</span>
                    <span>${order.totalPrice?.toFixed(2)}</span>
                  </div>
                </div>
              </div>
          ))}
        </div>

      </div>
  );
}

export default ProfilePage;