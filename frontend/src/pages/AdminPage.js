import React, { useEffect, useState } from 'react';
import { getProducts, getCategories, createProduct, updateProduct, deleteProduct } from '../api/api';

const EMPTY_FORM = { name: '', description: '', price: '', imageUrl: '', category: null };

function AdminPage() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState(EMPTY_FORM);
  const [editingId, setEditingId] = useState(null); // null = add mode, id = edit mode
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = () => {
    getProducts().then(res => setProducts(res.data));
    getCategories().then(res => setCategories(res.data));
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'categoryId') {
      const cat = categories.find(c => c.id === parseInt(value));
      setForm(prev => ({ ...prev, category: cat || null }));
    } else {
      setForm(prev => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault(); // dont reload page
    setLoading(true);

    const productData = {
      ...form,
      price: parseFloat(form.price), // convert string to number
    };

    const request = editingId
      ? updateProduct(editingId, productData)
      : createProduct(productData);

    request
      .then(() => {
        setMessage(editingId ? 'Product updated!' : 'Product created!');
        setForm(EMPTY_FORM);
        setEditingId(null);
        loadData();
      })
      .catch(() => setMessage('Error saving product.'))
      .finally(() => setLoading(false));
  };

  const handleEdit = (product) => {
    setEditingId(product.id);
    setForm({
      name: product.name,
      description: product.description,
      price: product.price,
      imageUrl: product.imageUrl,
      category: product.category,
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = (id) => {
    if (window.confirm('Delete this product?')) {
      deleteProduct(id).then(() => loadData());
    }
  };

  const handleCancel = () => {
    setForm(EMPTY_FORM);
    setEditingId(null);
    setMessage(null);
  };

  return (
    <div>
      <h2 className="mb-4">Admin Panel</h2>

      <div className="card shadow-sm mb-5">
        <div className="card-body">
          <h5 className="card-title">{editingId ? 'Edit Product' : 'Add New Product'}</h5>
          {message && (
            <div className="alert alert-info py-2" onClick={() => setMessage(null)}>
              {message}
            </div>
          )}
          <form onSubmit={handleSubmit}>
            <div className="row g-3">
              <div className="col-md-6">
                <label className="form-label">Name</label>
                <input name="name" className="form-control" value={form.name}
                  onChange={handleChange} required />
              </div>
              <div className="col-md-6">
                <label className="form-label">Price ($)</label>
                <input name="price" type="number" step="0.01" min="0" className="form-control"
                  value={form.price} onChange={handleChange} required />
              </div>
              <div className="col-12">
                <label className="form-label">Description</label>
                <input name="description" className="form-control" value={form.description}
                  onChange={handleChange} required />
              </div>
              <div className="col-md-6">
                <label className="form-label">Image URL</label>
                <input name="imageUrl" className="form-control" value={form.imageUrl}
                  onChange={handleChange} />
              </div>
              <div className="col-md-6">
                <label className="form-label">Category</label>
                <select name="categoryId" className="form-select"
                  value={form.category?.id || ''} onChange={handleChange} required>
                  <option value="">Select category...</option>
                  {categories.map(cat => (
                    <option key={cat.id} value={cat.id}>{cat.name}</option>
                  ))}
                </select>
              </div>
            </div>
            <div className="mt-3 d-flex gap-2">
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Saving...' : editingId ? 'Update' : 'Add Product'}
              </button>
              {editingId && (
                <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                  Cancel
                </button>
              )}
            </div>
          </form>
        </div>
      </div>

      <h4 className="mb-3">All Products ({products.length})</h4>
      <div className="table-responsive">
        <table className="table table-hover align-middle">
          <thead className="table-dark">
            <tr>
              <th>#</th>
              <th>Name</th>
              <th>Category</th>
              <th>Price</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {products.map(product => (
              <tr key={product.id}>
                <td>{product.id}</td>
                <td>{product.name}</td>
                <td><span className="badge bg-secondary">{product.category?.name}</span></td>
                <td>${product.price.toFixed(2)}</td>
                <td>
                  <button className="btn btn-sm btn-outline-primary me-2"
                    onClick={() => handleEdit(product)}>Edit</button>
                  <button className="btn btn-sm btn-outline-danger"
                    onClick={() => handleDelete(product.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default AdminPage;
