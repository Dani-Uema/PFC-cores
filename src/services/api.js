import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log('🔐 Token adicionado à requisição:', config.url);
    } else {
      console.log('⚠️ Token não encontrado para:', config.url);
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => {
    console.log('✅ Resposta recebida:', response.config.url, response.status);
    return response;
  },
  (error) => {
    console.error('❌ Erro na resposta:', error.config?.url, error.response?.status);
    return Promise.reject(error);
  }
);

export const colorService = {
  searchColors: (query) => api.get(`/colors/search?q=${query}`),
  getColorById: (id) => api.get(`/colors/${id}`),
  getColorComposition: (id) => api.get(`/colors/${id}/pigments`),
  getAllColors: () => api.get('/colors')
};

export const authService = {
  login: (credentials) => api.post('/user/login', credentials),
  register: (userData) => api.post('/user/register', userData),
};

export const historyService = {
  addToHistory: (historyData) => api.post('/history', historyData),
  getSearchHistory: (userId) => api.get(`/history/${userId}`),
  clearHistory: (userId) => api.delete(`/history/${userId}`),
  deleteHistoryItem: (historyId) => api.delete(`/history/item/${historyId}`) 
};

export default api;