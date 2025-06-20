import apiClient from './apiClient'

export const commentsApi = {
  getMyComment(productId) {
    return apiClient.get(`/comments/my/${productId}`, {
      requiresAuth: true,
    })
  },

  getProductComments(productId, pageNo = 0, pageSize = 10) {
    return apiClient.get(`/comments/products/${productId}`, {
      params: { pageNo, pageSize },
      requiresAuth: false,
    })
  },

  createComment(commentData) {
    return apiClient.post('/comments', commentData, {
      requiresAuth: true,
    })
  },

  updateComment(commentId, commentData) {
    return apiClient.put(`/comments/${commentId}`, commentData, {
      requiresAuth: true,
    })
  },

  deleteComment(commentId) {
    return apiClient.delete(`/comments/${commentId}`, {
      requiresAuth: true,
    })
  },
}
