import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api';
const APPOINTMENT_API_URL = 'http://localhost:8082/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const appointmentApi = axios.create({
  baseURL: APPOINTMENT_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

appointmentApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authService = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },
};

export const appointmentService = {
  getAll: () => appointmentApi.get('/appointments'),
  getById: (id) => appointmentApi.get(`/appointments/${id}`),
  getByPatient: (patientId) => appointmentApi.get(`/appointments/patient/${patientId}`),
  getByDoctor: (doctorId) => appointmentApi.get(`/appointments/doctor/${doctorId}`),
  create: (data) => appointmentApi.post('/appointments', data),
  update: (id, data) => appointmentApi.put(`/appointments/${id}`, data),
  cancel: (id) => appointmentApi.put(`/appointments/${id}/cancel`),
  delete: (id) => appointmentApi.delete(`/appointments/${id}`),
};

export default api;
