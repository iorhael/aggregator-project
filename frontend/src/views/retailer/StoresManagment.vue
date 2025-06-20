<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gray-50 min-h-screen">
      <div class="bg-white border-b border-gray-200">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div
            class="flex flex-col sm:flex-row justify-center items-center py-4 space-y-2 sm:space-y-0 sm:space-x-8"
          >
            <router-link to="/retailer" class="text-gray-500 hover:text-gray-700 pb-2">
              Organization Info
            </router-link>
            <router-link to="/cards" class="text-gray-500 hover:text-gray-700 pb-2">
              Cards Management ({{ cardsCount || 0 }})
            </router-link>
            <button class="text-blue-600 border-b-2 border-blue-600 pb-2 font-medium">
              Stores Management ({{ totalStores || 0 }})
            </button>
            <router-link to="/jobs" class="text-gray-500 hover:text-gray-700 pb-2">
              Jobs History
            </router-link>
          </div>
        </div>
      </div>

      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div
          class="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-8 space-y-4 sm:space-y-0"
        >
          <div>
            <h1 class="text-3xl font-bold text-gray-900 mb-2">Stores Management</h1>
            <p class="text-gray-600">
              Manage your store locations, contact information, and opening hours.
            </p>
          </div>
          <button
            @click="createStore"
            class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-md font-medium transition-colors flex items-center"
          >
            <svg class="w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 6v6m0 0v6m0-6h6m-6 0H6"
              />
            </svg>
            Add New Store
          </button>
        </div>

        <div v-if="loading" class="flex justify-center items-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-md p-4 mb-6">
          <div class="flex">
            <div class="flex-shrink-0">
              <svg class="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                <path
                  fill-rule="evenodd"
                  d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                  clip-rule="evenodd"
                />
              </svg>
            </div>
            <div class="ml-3">
              <h3 class="text-sm font-medium text-red-800">Error loading stores</h3>
              <p class="text-sm text-red-700 mt-1">{{ error }}</p>
              <button
                @click="fetchStores"
                class="mt-2 text-sm text-red-600 hover:text-red-500 underline"
              >
                Try again
              </button>
            </div>
          </div>
        </div>

        <div v-else-if="stores.length > 0" class="space-y-6">
          <div
            v-for="store in stores"
            :key="store.store_id"
            class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
          >
            <div class="p-6">
              <div
                class="flex flex-col lg:flex-row lg:items-start lg:justify-between space-y-4 lg:space-y-0 mb-6"
              >
                <div class="flex-1">
                  <div class="flex items-center space-x-3 mb-2">
                    <div
                      class="w-14 h-14 bg-blue-100 rounded-full flex items-center justify-center"
                    >
                      <img
                        src="@/assets/store-default.png"
                        alt="Default Store Image"
                        class="mx-auto h-16 w-16 text-gray-400 mb-4"
                      />
                    </div>
                    <h3 class="text-lg font-semibold text-gray-900">
                      {{ store.retailer_name }} Store
                    </h3>
                  </div>

                  <div class="space-y-2">
                    <div class="flex items-center text-gray-600">
                      <svg
                        class="w-4 h-4 mr-2 flex-shrink-0"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                        />
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                        />
                      </svg>
                      <span class="text-sm">{{ store.address || 'No address provided' }}</span>
                    </div>
                    <div class="flex items-center text-gray-600">
                      <svg
                        class="w-4 h-4 mr-2 flex-shrink-0"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"
                        />
                      </svg>
                      <span class="text-sm">{{
                        formatPhone(store.phone) || 'No phone provided'
                      }}</span>
                    </div>
                  </div>
                </div>

                <div class="flex space-x-2">
                  <button
                    @click="editStore(store)"
                    class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
                  >
                    Edit
                  </button>
                  <button
                    @click="confirmDelete(store)"
                    class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
                  >
                    Delete
                  </button>
                </div>
              </div>

              <div v-if="store.opening_hours">
                <h4 class="text-sm font-medium text-gray-700 mb-3">Opening Hours</h4>

                <div class="hidden md:block">
                  <div class="grid grid-cols-7 gap-2">
                    <div
                      v-for="(day, dayName) in store.opening_hours"
                      :key="dayName"
                      class="text-center"
                    >
                      <div class="text-xs font-medium text-gray-500 uppercase mb-1">
                        {{ dayName.slice(0, 3) }}
                      </div>
                      <div
                        class="bg-gray-50 rounded-md p-2 min-h-[60px] flex items-center justify-center"
                      >
                        <span class="text-xs text-gray-700 leading-tight">{{
                          formatSchedule(day)
                        }}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="md:hidden space-y-2">
                  <div
                    v-for="(day, dayName) in store.opening_hours"
                    :key="dayName"
                    class="flex justify-between items-center py-2 px-3 bg-gray-50 rounded-md"
                  >
                    <span class="text-sm font-medium text-gray-700 capitalize">{{ dayName }}</span>
                    <span class="text-sm text-gray-600">{{ formatSchedule(day) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center py-12">
          <div class="bg-white rounded-lg shadow-md p-8">
            <img
              src="@/assets/no-stores.png"
              alt="Default Store Image"
              class="mx-auto h-16 w-16 text-gray-400 mb-4"
            />
            <h3 class="text-xl font-semibold text-gray-900 mb-2">No Stores Found</h3>
            <p class="text-gray-600 mb-6">
              You don't have any stores yet. Create your first store to get started.
            </p>
            <button
              @click="createStore"
              class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-md font-medium transition-colors"
            >
              Add Your First Store
            </button>
          </div>
        </div>

        <div v-if="totalPages > 1" class="flex justify-center items-center space-x-2 mt-8">
          <button
            @click="goToPage(currentPage - 1)"
            :disabled="currentPage === 1"
            class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Previous
          </button>

          <div class="flex space-x-1">
            <button
              v-for="page in visiblePages"
              :key="page"
              @click="goToPage(page)"
              :class="[
                'px-3 py-2 text-sm font-medium rounded-md',
                page === currentPage
                  ? 'bg-blue-600 text-white'
                  : 'text-gray-700 bg-white border border-gray-300 hover:bg-gray-50',
              ]"
            >
              {{ page }}
            </button>
          </div>

          <button
            @click="goToPage(currentPage + 1)"
            :disabled="currentPage === totalPages"
            class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Next
          </button>
        </div>

        <div v-if="stores.length > 0" class="text-center mt-4 text-sm text-gray-600">
          Showing {{ (currentPage - 1) * pageSize + 1 }} to
          {{ Math.min(currentPage * pageSize, totalStores) }} of {{ totalStores }} stores
        </div>

        <StoreFormModal
          v-if="showStoreModal"
          :is-edit="!!selectedStore"
          :store="selectedStore"
          @close="showStoreModal = false"
          @success="handleStoreSuccess"
        />

        <DeleteConfirmationModal
          v-if="showDeleteModal"
          :item="selectedStore"
          item-type="store"
          :item-name="selectedStore?.retailer_name + ' Store'"
          @close="showDeleteModal = false"
          @confirm="handleDelete"
        />

        <SuccessNotification
          v-if="showSuccessNotification"
          :message="successMessage"
          @close="showSuccessNotification = false"
        />
      </div>
    </div>
  </AppLayout>
</template>

<script>
import AppLayout from '@/components/AppLayout.vue'
import StoreFormModal from '@/components/retailer/StoreFormModal.vue'
import DeleteConfirmationModal from '@/components/DeleteConfirmationModal.vue'
import SuccessNotification from '@/components/SuccessNotification.vue'
import { storesApi } from '@/api'
import countsMixin from '@/mixins/countsMixin.js'

export default {
  name: 'StoresManagement',
  components: {
    AppLayout,
    StoreFormModal,
    DeleteConfirmationModal,
    SuccessNotification,
  },
  mixins: [countsMixin],
  data() {
    return {
      stores: [],
      totalStores: 0,
      totalPages: 0,
      currentPage: 1,
      pageSize: 15,
      loading: false,
      error: null,
      showStoreModal: false,
      showDeleteModal: false,
      showSuccessNotification: false,
      selectedStore: null,
      successMessage: '',
    }
  },
  computed: {
    visiblePages() {
      const pages = []
      const start = Math.max(1, this.currentPage - 2)
      const end = Math.min(this.totalPages, this.currentPage + 2)

      for (let i = start; i <= end; i++) {
        pages.push(i)
      }

      return pages
    },
  },
  mounted() {
    this.fetchStores()
    this.fetchCounts()
  },
  methods: {
    async fetchStores() {
      this.loading = true
      this.error = null

      try {
        const response = await storesApi.getMyStores(this.currentPage - 1, this.pageSize)

        this.stores = response.data.content || []
        this.totalStores = response.data.total_elements || response.data.totalElements || 0
        this.totalPages = response.data.total_pages || response.data.totalPages || 0
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to load stores'
        this.stores = []
      } finally {
        this.loading = false
      }
    },

    goToPage(page) {
      if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
        this.currentPage = page
        this.fetchStores()
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    },

    createStore() {
      this.selectedStore = null
      this.showStoreModal = true
    },

    editStore(store) {
      this.selectedStore = store
      this.showStoreModal = true
    },

    confirmDelete(store) {
      this.selectedStore = store
      this.showDeleteModal = true
    },

    async handleDelete() {
      try {
        await storesApi.deleteStore(this.selectedStore.store_id)

        this.showDeleteModal = false
        this.selectedStore = null

        this.successMessage = 'Store deleted successfully!'
        this.showSuccessNotification = true

        this.invalidateCounts()
        await Promise.all([this.fetchStores(), this.fetchCounts(true)])

        setTimeout(() => {
          this.showSuccessNotification = false
        }, 3000)
      } catch (error) {
        console.error('Delete failed:', error)
        alert('Failed to delete store. Please try again.')
      }
    },

    async handleStoreSuccess(updatedStore) {
      this.showStoreModal = false
      this.selectedStore = null

      this.successMessage = updatedStore.isNew
        ? 'Store created successfully!'
        : 'Store updated successfully!'
      this.showSuccessNotification = true

      this.invalidateCounts()
      await Promise.all([this.fetchStores(), this.fetchCounts(true)])

      setTimeout(() => {
        this.showSuccessNotification = false
      }, 3000)
    },

    formatPhone(phone) {
      if (!phone) return 'Not provided'

      if (phone.length === 11) {
        return `+${phone.slice(0, 1)} (${phone.slice(1, 4)}) ${phone.slice(4, 7)}-${phone.slice(7, 9)}-${phone.slice(9)}`
      } else if (phone.length >= 10) {
        return `+${phone.slice(0, -10)} (${phone.slice(-10, -7)}) ${phone.slice(-7, -4)}-${phone.slice(-4, -2)}-${phone.slice(-2)}`
      }

      return phone
    },

    formatSchedule(schedule) {
      if (!schedule) return 'Closed'

      if (schedule.toLowerCase().includes('closed')) {
        return 'Closed'
      }

      if (schedule.toLowerCase().includes('24') || schedule.toLowerCase().includes('24/7')) {
        return '24/7'
      }

      const timePattern = /(\d{1,2}):?(\d{2})?\s*-\s*(\d{1,2}):?(\d{2})?/
      const match = schedule.match(timePattern)

      if (match) {
        const [, startHour, startMin = '00', endHour, endMin = '00'] = match
        return `${startHour}:${startMin}-${endHour}:${endMin}`
      }

      return schedule
    },
  },
}
</script>
