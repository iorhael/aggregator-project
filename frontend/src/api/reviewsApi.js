import apiClient from './apiClient'

export const reviewsApi = {
  getReview(reviewId) {
    return apiClient.get(`reviews/${reviewId}`, {
      requiresAuth: false,
    })
  },

  getReviewCount(productId) {
    return apiClient.get(`/reviews/count/${productId}`, {
      requiresAuth: false,
    })
  },

  getProductReviews(productId, pageNo = 0, pageSize = 10) {
    return apiClient.get(`/reviews/products/${productId}`, {
      params: { pageNo, pageSize },
      requiresAuth: false,
    })
  },

  uploadReviewImage(imageFile) {
    const formData = new FormData()
    formData.append('image', imageFile)

    return apiClient.post('/reviews/images', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      requiresAuth: true,
    })
  },

  createReview(reviewData) {
    return apiClient.post('/reviews', reviewData, {
      requiresAuth: true,
    })
  },
}
