import apiClient from './apiClient'

export const productsApi = {
  getProducts(pageNo = 0, pageSize = 15) {
    return apiClient.get(`/products/previews`, {
      params: { pageNo, pageSize },
      requiresAuth: false,
    })
  },

  getProductsByCategory(categoryId, pageNo = 0, pageSize = 15) {
    const categoryParam = categoryId
    return apiClient.get(`/products/previews/${categoryParam}`, {
      params: { pageNo, pageSize },
      requiresAuth: false,
    })
  },

  searchProducts(searchCriteria, pageNo = 0, pageSize = 15) {
    return apiClient.post('/products/search', searchCriteria, {
      params: { pageNo, pageSize },
      requiresAuth: false,
    })
  },

  getProductDetails(productId) {
    return apiClient.get(`/products/${productId}`, {
      requiresAuth: false,
    })
  },

  getProductOffers(productId, pageNo = 0, pageSize = 6) {
    return apiClient.post(
      `/product_cards/search/${productId}`,
      {},
      {
        params: { pageNo, pageSize },
        requiresAuth: false,
      },
    )
  },

  searchProductOffers(productId, filterCriteria = {}, pageNo = 0, pageSize = 15) {
    return apiClient.post(`/product_cards/search/${productId}`, filterCriteria, {
      params: { pageNo, pageSize },
      requiresAuth: false,
    })
  },
}
