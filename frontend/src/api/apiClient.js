import axios from 'axios'
import keycloak from '../services/keycloak'
import router from '@/router'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

// Create axios instance with base configuration
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

apiClient.interceptors.request.use(
  async (config) => {
    // Check if this endpoint requires authentication
    const requiresAuth = config.requiresAuth !== false // Default to true unless explicitly set to false

    if (requiresAuth && keycloak.authenticated) {
      try {
        // Try to update token (refresh if needed)
        await keycloak.updateToken(30) // Refresh if expires in 30 seconds
        config.headers.Authorization = `Bearer ${keycloak.token}`
      } catch (error) {
        console.error('Token refresh failed:', error)
        // Token refresh failed, redirect to login
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

// Response interceptor to handle auth errors
apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config

    // Handle 401 Unauthorized
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      if (keycloak.authenticated) {
        try {
          // Try to refresh token
          await keycloak.updateToken(-1) // Force refresh
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

    // Handle 403 Forbidden (insufficient permissions)
    if (error.response?.status === 403) {
      // You might want to redirect to a forbidden page or show a message
      console.error('Access forbidden - insufficient permissions')
      router.push('/forbidden')
    }

    return Promise.reject(error)
  },
)

export default apiClient
