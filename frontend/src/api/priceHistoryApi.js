import apiClient from './apiClient'

export const priceHistoryApi = {
  // Get daily price dynamics
  getDailyPriceDynamics(productId, daysOffset) {
    return apiClient.get('/price_histories/day', {
      params: { productId, daysOffset },
      requiresAuth: false,
    })
  },

  // Get monthly price dynamics
  getMonthlyPriceDynamics(productId, monthOffset) {
    return apiClient.get('/price_histories/month', {
      params: { productId, monthOffset },
      requiresAuth: false,
    })
  },
}
