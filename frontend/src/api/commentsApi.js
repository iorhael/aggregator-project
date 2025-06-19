import apiClient from './apiClient'

export const commentsApi = {
  // Get user's comment for a specific product
  getMyComment(productId) {
    return apiClient.get(`/comments/my/${productId}`, {
      requiresAuth: true,
    })
  },

  // Get paginated comments for a product
  getProductComments(productId, pageNo = 0, pageSize = 10) {
    return apiClient.get(`/comments/products/${productId}`, {
      params: { pageNo, pageSize },
      requiresAuth: false,
    })
  },

  // Create a new comment
  createComment(commentData) {
    return apiClient.post('/comments', commentData, {
      requiresAuth: true,
    })
  },

  // Update an existing comment
  updateComment(commentId, commentData) {
    return apiClient.put(`/comments/${commentId}`, commentData, {
      requiresAuth: true,
    })
  },

  // Delete a comment
  deleteComment(commentId) {
    return apiClient.delete(`/comments/${commentId}`, {
      requiresAuth: true,
    })
  },
}
