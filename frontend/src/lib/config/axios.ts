import axios, { type AxiosInstance } from 'axios';
import { goto } from '$app/navigation';
import { getToken, removeToken, removeUserInfo } from '@/utils/storage-util';

const apiClient: AxiosInstance = axios.create({
  baseURL: 'https://your.api.base.url',
});

// Request interceptor: add Authorization header
apiClient.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers = config.headers ?? {};
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor: handle 401 globally
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      removeToken();
      removeUserInfo();
      goto('/auth');
    }
    return Promise.reject(error);
  }
);

export default apiClient;
