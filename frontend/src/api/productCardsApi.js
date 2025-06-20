import apiClient from './apiClient'

export const productCardsApi = {
  getMyProductCards(page = 0, size = 10) {
    return apiClient.get('product_cards/my', {
      params: { page, size },
      requiresAuth: true,
    })
  },

  getMyProductCardsCount() {
    return apiClient.get('product_cards/my/count', {
      requiresAuth: true,
    })
  },

  createProductCard(cardData) {
    return apiClient.post('product_cards', cardData, {
      requiresAuth: true,
    })
  },

  updateProductCard(cardId, updateData) {
    return apiClient.put(`product_cards/${cardId}`, updateData, {
      requiresAuth: true,
    })
  },

  deleteProductCard(cardId) {
    return apiClient.delete(`product_cards/${cardId}`, {
      requiresAuth: true,
    })
  },
}
