import apiClient from './apiClient'

export const priceHistoryApi = {
  getDailyPriceDynamics(productId, daysOffset) {
    return apiClient.get('/price_histories/day', {
      params: { productId, daysOffset },
      requiresAuth: false,
    })
  },

  getMonthlyPriceDynamics(productId, monthOffset) {
    return apiClient.get('/price_histories/month', {
      params: { productId, monthOffset },
      requiresAuth: false,
    })
  },
}
