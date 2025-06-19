import apiClient from './apiClient'

export const categoriesApi = {
  getTopLevelCategories(pageNo = 0, pageSize = 100) {
    return apiClient.get('/categories', {
      params: { pageNo, pageSize },
      requiresAuth: false,
    })
  },

  getSubcategories(parentId) {
    return apiClient.get(`/categories/subcategories/${parentId}`, {
      requiresAuth: false,
    })
  },
}
