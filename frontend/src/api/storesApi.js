import apiClient from './apiClient'

export const storesApi = {
  getStoresByRetailerId(retailerId, pageNo = 0, pageSize = 15) {
    return apiClient.get(`stores/${retailerId}`, {
      params: { pageNo, pageSize },
      requiresAuth: false,
    })
  },

  getMyStores(page = 0, size = 15) {
    return apiClient.get('stores/my', {
      params: { page, size },
      requiresAuth: true,
    })
  },

  // Get count of retailer's stores (requires auth)
  getMyStoresCount() {
    return apiClient.get('stores/my/count', {
      requiresAuth: true,
    })
  },

  // Create new store (requires auth)
  createStore(storeData) {
    return apiClient.post('stores', storeData, {
      requiresAuth: true,
    })
  },

  // Update store (requires auth)
  updateStore(storeId, storeData) {
    return apiClient.put(`stores/${storeId}`, storeData, {
      requiresAuth: true,
    })
  },

  // Delete store (requires auth)
  deleteStore(storeId) {
    return apiClient.delete(`stores/${storeId}`, {
      requiresAuth: true,
    })
  },
}
