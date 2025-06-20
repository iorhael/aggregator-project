import apiClient from './apiClient'

export const usersApi = {
  synchronizeUser() {
    return apiClient.post('users/synchronization', {
      requiresAuth: true,
    })
  },
}
