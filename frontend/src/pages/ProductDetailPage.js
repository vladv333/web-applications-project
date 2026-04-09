import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getProduct } from '../api/api';
import { useCart } from '../CartContext';

function ProductDetailPage() {
  const { id } = useParams(); // get product id from url /products/3
  const navigate = useNavigate();
  const { addToCart } = useCart();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [added, setAdded] = useState(false);

  useEffect(() => {
    getProduct(id)
      .then(res => setProduct(res.data))
      .catch(() => setError('Product not found.'))
      .finally(() => setLoading(false));
  }, [id]);

  const handleAddToCart = () => {
    addToCart(product);
    setAdded(true);
    setTimeout(() => setAdded(false), 1500);
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border"/></div>;

  if (error) return (
    <div className="mt-4">
      <div className="alert alert-danger">{error}</div>
      <button className="btn btn-secondary" onClick={() => navigate('/')}>← Back to Products</button>
    </div>
  );

  return (
    <div className="mt-2">
      <button className="btn btn-outline-secondary btn-sm mb-4" onClick={() => navigate('/')}>
        ← Back to Products
      </button>

      <div className="row g-5">
        <div className="col-md-5">
          <img
            src={product.imageUrl}
            alt={product.name}
            className="img-fluid rounded shadow-sm w-100"
            style={{ maxHeight: '360px', objectFit: 'cover' }}
          />
        </div>

        <div className="col-md-7">
          <span className="badge bg-secondary mb-2 fs-6">{product.category?.name}</span>
          <h2 className="mb-2">{product.name}</h2>
          <p className="text-muted fs-6 mb-4">{product.description}</p>
          <div className="mb-4">
            <span className="fs-2 fw-bold text-dark">${product.price.toFixed(2)}</span>
          </div>
          <button
            className={`btn btn-lg ${added ? 'btn-success' : 'btn-primary'}`}
            onClick={handleAddToCart}
          >
            {added ? '✓ Added to Cart!' : 'Add to Cart'}
          </button>
        </div>
      </div>
    </div>
  );
}

export default ProductDetailPage;
