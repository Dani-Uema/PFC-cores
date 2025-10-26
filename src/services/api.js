import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/paintlab'; 
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const colorService = {
  searchColors: (query) => api.get(`/colors/search?q=${query}`),
  getColorById: (id) => api.get(`/colors/${id}`),
  getColorComposition: (id) => api.get(`/colors/${id}/composition`),
};

export const authService = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
};

export const historyService = {
  getSearchHistory: () => api.get('/history'),
  addToHistory: (query) => api.post('/history', { query }),
};

export default api;