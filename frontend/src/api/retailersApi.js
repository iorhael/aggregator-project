import apiClient from './apiClient'

export const retailersApi = {
  getMyRetailer() {
    return apiClient.get('retailers/me', {
      requiresAuth: true,
    })
  },

  // Get retailer by name (public endpoint)
  getRetailerByName(retailerName) {
    return apiClient.get(`retailers/${retailerName}`, {
      requiresAuth: false,
    })
  },

  // Create new retailer (requires auth)
  createRetailer(retailerData, logoFile = null) {
    const formData = new FormData()

    // Add retailer data as JSON string
    const retailerBlob = new Blob([JSON.stringify(retailerData)], {
      type: 'application/json',
    })
    formData.append('retailer', retailerBlob)

    // Add logo file if provided
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

  // Update retailer info (requires auth)
  updateRetailer(retailerData = null, logoFile = null) {
    const formData = new FormData()

    // Add retailer data as JSON string if provided
    if (retailerData) {
      const retailerBlob = new Blob([JSON.stringify(retailerData)], {
        type: 'application/json',
      })
      formData.append('retailer', retailerBlob)
    }

    // Add logo file if provided
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

  // Delete retailer (requires auth)
  deleteRetailer() {
    return apiClient.delete('retailers/me', {
      requiresAuth: true,
    })
  },
}
