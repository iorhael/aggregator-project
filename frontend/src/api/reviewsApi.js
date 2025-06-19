import apiClient from './apiClient'

export const reviewsApi = {
  // Get review count for a product
  getReviewCount(productId) {
    return apiClient.get(`/reviews/count/${productId}`, {
      requiresAuth: false,
    })
  },
}
