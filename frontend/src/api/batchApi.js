import apiClient from './apiClient'

export const batchApi = {
  importProductCards(file, verifiedProductsOnly = true) {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('verifiedProductsOnly', verifiedProductsOnly)

    return apiClient.post('product_cards_batch/import', formData, {
      requiresAuth: true,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },

  exportProductCards(type = 'CSV') {
    return apiClient.get('product_cards_batch/export', {
      params: { type },
      requiresAuth: true,
    })
  },

  configureAutoUpdate(downloadLink, verifiedProductsOnly) {
    return apiClient.post(
      'product_cards_batch/auto_update',
      {
        download_link: downloadLink,
        verified_products_only: verifiedProductsOnly,
      },
      {
        requiresAuth: true,
      },
    )
  },

  updateAutoUpdate(downloadLink, verifiedProductsOnly) {
    return apiClient.put(
      'product_cards_batch/auto_update',
      {
        download_link: downloadLink,
        verified_products_only: verifiedProductsOnly,
      },
      {
        requiresAuth: true,
      },
    )
  },

  deleteAutoUpdate() {
    return apiClient.delete('product_cards_batch/auto_update', {
      requiresAuth: true,
    })
  },

  getJobExecutions(pageNo = 0, pageSize = 15) {
    return apiClient.get('product_cards_batch/job_executions', {
      params: { pageNo, pageSize },
      requiresAuth: true,
    })
  },
}
