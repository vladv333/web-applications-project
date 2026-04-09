import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api';

export const getProducts = () => axios.get(`${BASE_URL}/products`);
export const getProduct = (id) => axios.get(`${BASE_URL}/products/${id}`);
export const createProduct = (product) => axios.post(`${BASE_URL}/products`, product);
export const updateProduct = (id, product) => axios.put(`${BASE_URL}/products/${id}`, product);
export const deleteProduct = (id) => axios.delete(`${BASE_URL}/products/${id}`);

export const getCategories = () => axios.get(`${BASE_URL}/categories`);

export const createOrder = (order) => axios.post(`${BASE_URL}/orders`, order);
export const getUserOrders = (userId) => axios.get(`${BASE_URL}/orders/user/${userId}`);

export const registerApi = (data) => axios.post(`${BASE_URL}/auth/register`, data);
export const loginApi    = (data) => axios.post(`${BASE_URL}/auth/login`, data);

export const getUser    = (id)       => axios.get(`${BASE_URL}/users/${id}`);
export const updateUser = (id, data) => axios.put(`${BASE_URL}/users/${id}`, data);

export const fetchCart        = (userId)            => axios.get(`${BASE_URL}/cart/${userId}`);
export const addToDbCart      = (userId, item)      => axios.post(`${BASE_URL}/cart/${userId}/items`, item);
export const removeFromDbCart = (userId, productId) => axios.delete(`${BASE_URL}/cart/${userId}/items/${productId}`);
export const clearDbCart      = (userId)            => axios.delete(`${BASE_URL}/cart/${userId}`);

export const changePassword = (id, data) => axios.put(`${BASE_URL}/users/${id}/password`, data);