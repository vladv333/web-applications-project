import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getProducts } from '../api/api';
import { useCart } from '../CartContext';

function ProductsPage() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { addToCart } = useCart();
  const [added, setAdded] = useState({});

  useEffect(() => {
    getProducts()
      .then(res => setProducts(res.data))
      .catch(() => setError('Failed to load products.'))
      .finally(() => setLoading(false));
  }, []);

  const handleAddToCart = (product) => {
    addToCart(product);
    setAdded(prev => ({ ...prev, [product.id]: true }));
    setTimeout(() => setAdded(prev => ({ ...prev, [product.id]: false })), 1000);
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border"/></div>;
  if (error)   return <div className="alert alert-danger">{error}</div>;

  return (
    <div>
      <h2 className="mb-4">Our Products</h2>
      <div className="row row-cols-1 row-cols-md-3 g-4">
        {products.map(product => (
          <div className="col" key={product.id}>
            <div className="card h-100 shadow-sm">
              <Link to={`/products/${product.id}`}>
                <img
                  src={product.imageUrl}
                  className="card-img-top"
                  alt={product.name}
                  style={{ height: '180px', objectFit: 'cover' }}
                />
              </Link>
              <div className="card-body d-flex flex-column">
                <span className="badge bg-secondary mb-2" style={{ width: 'fit-content' }}>
                  {product.category?.name}
                </span>
                <Link to={`/products/${product.id}`} className="text-decoration-none text-dark">
                  <h5 className="card-title">{product.name}</h5>
                </Link>
                <p className="card-text text-muted flex-grow-1">{product.description}</p>
                <div className="d-flex justify-content-between align-items-center mt-3">
                  <span className="fw-bold fs-5">${product.price.toFixed(2)}</span>
                  <button
                    className={`btn ${added[product.id] ? 'btn-success' : 'btn-primary'}`}
                    onClick={() => handleAddToCart(product)}
                  >
                    {added[product.id] ? 'Added!' : 'Add to Cart'}
                  </button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ProductsPage;
