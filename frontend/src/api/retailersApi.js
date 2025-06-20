import apiClient from './apiClient'

export const retailersApi = {
  getMyRetailer() {
    return apiClient.get('retailers/me', {
      requiresAuth: true,
    })
  },

  getRetailerByName(retailerName) {
    return apiClient.get(`retailers/${retailerName}`, {
      requiresAuth: false,
    })
  },

  createRetailer(retailerData, logoFile = null) {
    const formData = new FormData()

    const retailerBlob = new Blob([JSON.stringify(retailerData)], {
      type: 'application/json',
    })
    formData.append('retailer', retailerBlob)

    if (logoFile) {
      formData.append('image', logoFile)
    }

    return apiClient.post('retailers', formData, {
      requiresAuth: true,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },

  updateRetailer(retailerData = null, logoFile = null) {
    const formData = new FormData()

    if (retailerData) {
      const retailerBlob = new Blob([JSON.stringify(retailerData)], {
        type: 'application/json',
      })
      formData.append('retailer', retailerBlob)
    }

    if (logoFile) {
      formData.append('image', logoFile)
    }

    return apiClient.put('retailers', formData, {
      requiresAuth: true,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },

  deleteRetailer() {
    return apiClient.delete('retailers/me', {
      requiresAuth: true,
    })
  },
}
