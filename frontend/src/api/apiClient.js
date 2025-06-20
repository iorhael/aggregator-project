import axios from 'axios'
import keycloak from '../services/keycloak'
import router from '@/router'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

apiClient.interceptors.request.use(
  async (config) => {
    const requiresAuth = config.requiresAuth !== false

    if (requiresAuth && keycloak.authenticated) {
      try {
        await keycloak.updateToken(30)
        config.headers.Authorization = `Bearer ${keycloak.token}`
      } catch (error) {
        console.error('Token refresh failed:', error)
        keycloak.login({ redirectUri: window.location.origin })
        return Promise.reject(error)
      }
    }

    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      if (keycloak.authenticated) {
        try {
          await keycloak.updateToken(-1)
          originalRequest.headers.Authorization = `Bearer ${keycloak.token}`
          return apiClient(originalRequest)
        } catch (refreshError) {
          console.error('Token refresh failed on 401:', refreshError)
          keycloak.login({ redirectUri: window.location.origin })
        }
      } else {
        keycloak.login({ redirectUri: window.location.origin })
      }
    }

    if (error.response?.status === 403) {
      console.error('Access forbidden - insufficient permissions')
      router.push('/forbidden')
    }

    return Promise.reject(error)
  },
)

export default apiClient
