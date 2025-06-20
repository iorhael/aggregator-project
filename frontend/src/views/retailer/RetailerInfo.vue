<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gray-50 min-h-screen">
      <div v-if="retailer" class="bg-white border-b border-gray-200">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div
            class="flex flex-col sm:flex-row justify-center items-center py-4 space-y-2 sm:space-y-0 sm:space-x-8"
          >
            <button class="text-blue-600 border-b-2 border-blue-600 pb-2 font-medium">
              Organization Info
            </button>
            <router-link to="/cards" class="text-gray-500 hover:text-gray-700 pb-2">
              Cards Management ({{ cardsCount || 0 }})
            </router-link>
            <router-link to="/stores" class="text-gray-500 hover:text-gray-700 pb-2">
              Stores Management ({{ storesCount || 0 }})
            </router-link>
            <router-link to="/jobs" class="text-gray-500 hover:text-gray-700 pb-2">
              Jobs History
            </router-link>
          </div>
        </div>
      </div>

      <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
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
              <h3 class="text-sm font-medium text-red-800">Error loading retailer information</h3>
              <p class="text-sm text-red-700 mt-1">{{ error }}</p>
              <button
                @click="fetchRetailerInfo"
                class="mt-2 text-sm text-red-600 hover:text-red-500 underline"
              >
                Try again
              </button>
            </div>
          </div>
        </div>

        <div v-else-if="!retailer && !loading" class="text-center py-12">
          <div class="bg-white rounded-lg shadow-md p-8">
            <img
              src="@/assets/organization-logo-default.png"
              alt="Default Organization Logo"
              class="mx-auto h-16 w-16 text-gray-400 mb-4"
            />
            <h3 class="text-xl font-semibold text-gray-900 mb-2">No Organization Found</h3>
            <p class="text-gray-600 mb-6">
              You haven't created your organization profile yet. Create one to start managing your
              products and business information.
            </p>
            <button
              @click="showEditForm = true"
              class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-md font-medium transition-colors"
            >
              Create Organization
            </button>
          </div>
        </div>

        <div v-else-if="retailer" class="space-y-8">
          <div class="bg-white rounded-lg shadow-md p-4 sm:p-8">
            <div
              class="flex flex-col lg:flex-row lg:items-start lg:space-x-8 space-y-6 lg:space-y-0"
            >
              <div class="flex-shrink-0 flex justify-center lg:justify-start">
                <div
                  class="w-32 h-32 sm:w-48 sm:h-48 bg-gray-100 rounded-full overflow-hidden border-4 border-gray-200"
                >
                  <img
                    v-if="retailer.logo_link"
                    :src="retailer.logo_link"
                    :alt="retailer.name + ' logo'"
                    @error="handleLogoError"
                    class="w-full h-full object-cover"
                  />
                  <div v-else class="w-full h-full flex items-center justify-center">
                    <img
                      src="@/assets/organization-logo-default.png"
                      alt="Organization Logo"
                      class="w-16 h-16 sm:w-24 sm:h-24 text-gray-400"
                    />
                  </div>
                </div>
              </div>

              <div class="flex-1 space-y-6">
                <div
                  class="flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-4 sm:space-y-0"
                >
                  <div>
                    <h2 class="text-2xl font-bold text-gray-900">{{ retailer.name }}</h2>
                    <p class="text-gray-600 mt-1">Organization Information</p>
                  </div>
                  <div class="flex flex-col sm:flex-row space-y-2 sm:space-y-0 sm:space-x-3">
                    <button
                      @click="importProducts"
                      class="bg-purple-600 hover:bg-purple-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
                    >
                      Import Products
                    </button>
                    <button
                      @click="exportCards"
                      :disabled="exporting"
                      class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      <span v-if="exporting" class="flex items-center justify-center">
                        <svg
                          class="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                          fill="none"
                          viewBox="0 0 24 24"
                        >
                          <circle
                            class="opacity-25"
                            cx="12"
                            cy="12"
                            r="10"
                            stroke="currentColor"
                            stroke-width="4"
                          ></circle>
                          <path
                            class="opacity-75"
                            fill="currentColor"
                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                          ></path>
                        </svg>
                        Exporting...
                      </span>
                      <span v-else>Export Cards</span>
                    </button>
                    <button
                      @click="showEditForm = true"
                      class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
                    >
                      Edit Profile
                    </button>
                  </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div class="md:col-span-2">
                    <label class="block text-sm font-medium text-gray-500 mb-2">About info:</label>
                    <div class="bg-gray-50 border border-gray-200 rounded-md p-4 min-h-[100px]">
                      <p class="text-gray-900 whitespace-pre-wrap">
                        {{ retailer.description || 'No description provided' }}
                      </p>
                    </div>
                  </div>

                  <div>
                    <label class="block text-sm font-medium text-gray-500 mb-2">Website:</label>
                    <div class="bg-gray-50 border border-gray-200 rounded-md p-3">
                      <a
                        v-if="retailer.website"
                        :href="retailer.website"
                        target="_blank"
                        class="text-blue-600 hover:text-blue-500 break-all"
                      >
                        {{ retailer.website }}
                      </a>
                      <span v-else class="text-gray-500">No website provided</span>
                    </div>
                  </div>

                  <div>
                    <label class="block text-sm font-medium text-gray-500 mb-2">Email:</label>
                    <div class="bg-gray-50 border border-gray-200 rounded-md p-3">
                      <span class="text-gray-900 break-all">{{
                        retailer.email || 'No email provided'
                      }}</span>
                    </div>
                  </div>

                  <div v-if="hasProductFeedConfig" class="md:col-span-2">
                    <label class="block text-sm font-medium text-gray-500 mb-2"
                      >Product Feed Configuration:</label
                    >
                    <div class="bg-gray-50 border border-gray-200 rounded-md p-4 space-y-3">
                      <div>
                        <span class="text-sm font-medium text-gray-700">Feed URL: </span>
                        <a
                          :href="retailer.download_link"
                          target="_blank"
                          class="text-blue-600 hover:text-blue-500 break-all"
                        >
                          {{ retailer.download_link }}
                        </a>
                      </div>
                      <div>
                        <span class="text-sm font-medium text-gray-700">Product Filter: </span>
                        <span
                          :class="
                            retailer.verified_products_only ? 'text-green-600' : 'text-gray-600'
                          "
                        >
                          {{
                            retailer.verified_products_only
                              ? 'Verified Products Only'
                              : 'All Products'
                          }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-md p-6">
            <div
              class="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-6 space-y-4 sm:space-y-0"
            >
              <h3 class="text-lg font-medium text-gray-900">Recent Product Cards</h3>
              <router-link
                to="/cards"
                class="text-blue-600 hover:text-blue-500 text-sm font-medium"
              >
                View All Cards
              </router-link>
            </div>

            <div v-if="cardsLoading" class="flex justify-center py-8">
              <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>

            <div v-else-if="recentCards.length > 0" class="relative">
              <div class="flex space-x-4 overflow-x-auto pb-4">
                <div
                  v-for="card in recentCards"
                  :key="card.product_card_id"
                  class="flex-shrink-0 w-64 bg-gray-50 border border-gray-200 rounded-lg p-4"
                >
                  <h4 class="font-medium text-gray-900 mb-2 line-clamp-2">{{ card.name }}</h4>
                  <p class="text-sm text-gray-600 mb-2">{{ card.vendor_name }}</p>
                  <div class="flex justify-between items-center">
                    <span class="text-lg font-bold text-gray-900"
                      >${{ formatPrice(card.price) }}</span
                    >
                    <div class="text-xs text-gray-500 text-right">
                      <div>Warranty: {{ card.warranty_period }}mo</div>
                      <div>Delivery: {{ card.max_delivery_time }}d</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div v-else class="text-center py-8">
              <p class="text-gray-500">No product cards found</p>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-md p-6 border border-red-200">
            <h3 class="text-lg font-medium text-red-900 mb-4">Danger Zone</h3>
            <div class="bg-red-50 border border-red-200 rounded-md p-4">
              <div
                class="flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-4 sm:space-y-0"
              >
                <div>
                  <h4 class="text-sm font-medium text-red-800">Delete Organization</h4>
                  <p class="text-sm text-red-600 mt-1">
                    Permanently delete your organization and all associated data. This action cannot
                    be undone.
                  </p>
                </div>
                <button
                  @click="confirmDeleteRetailer"
                  class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
                >
                  Delete Organization
                </button>
              </div>
            </div>
          </div>
        </div>

        <ExportModal
          v-if="showExportModal"
          @close="showExportModal = false"
          @export="handleExport"
        />

        <SuccessNotification
          v-if="showSuccessNotification"
          :message="successMessage"
          @close="showSuccessNotification = false"
        />

        <RetailerFormModal
          v-if="showEditForm"
          :is-edit="!!retailer"
          :retailer="retailer"
          @close="showEditForm = false"
          @success="handleFormSuccess"
        />

        <ImportModal
          v-if="showImportModal"
          @close="showImportModal = false"
          @success="handleImportSuccess"
        />

        <DeleteConfirmationModal
          v-if="showDeleteModal"
          :item="retailer"
          item-type="organization"
          :item-name="retailer?.name"
          @close="showDeleteModal = false"
          @confirm="handleDeleteRetailer"
        />
      </div>
    </div>
  </AppLayout>
</template>

<script>
import AppLayout from '@/components/AppLayout.vue'
import RetailerFormModal from '@/components/retailer/RetailerFormModal.vue'
import ExportModal from '@/components/retailer/ExportModal.vue'
import SuccessNotification from '@/components/SuccessNotification.vue'
import DeleteConfirmationModal from '@/components/DeleteConfirmationModal.vue'
import countsMixin from '@/mixins/countsMixin.js'
import ImportModal from '@/components/retailer/ImportModal.vue'
import { retailersApi, productCardsApi, batchApi } from '@/api'

export default {
  name: 'RetailerInfo',
  components: {
    AppLayout,
    RetailerFormModal,
    ExportModal,
    SuccessNotification,
    ImportModal,
    DeleteConfirmationModal,
  },
  mixins: [countsMixin],
  data() {
    return {
      retailer: null,
      recentCards: [],
      loading: false,
      cardsLoading: false,
      exporting: false,
      error: null,
      showEditForm: false,
      showExportModal: false,
      showSuccessNotification: false,
      showDeleteModal: false,
      successMessage: '',
      showImportModal: false,
    }
  },
  computed: {
    hasProductFeedConfig() {
      return this.retailer?.download_link
    },
  },
  mounted() {
    this.fetchRetailerInfo()
  },
  methods: {
    async fetchRetailerInfo() {
      this.loading = true
      this.error = null

      try {
        const response = await retailersApi.getMyRetailer()
        this.retailer = response.data

        await Promise.all([this.fetchCounts(), this.fetchRecentCards()])
      } catch (err) {
        if (err.response?.status === 404) {
          this.retailer = null
        } else {
          this.error = err.response?.data?.message || 'Failed to load retailer information'
        }
      } finally {
        this.loading = false
      }
    },

    async fetchRecentCards() {
      this.cardsLoading = true
      try {
        const response = await productCardsApi.getMyProductCards(0, 10)
        this.recentCards = response.data.content || []
      } catch (err) {
        console.error('Failed to fetch recent cards:', err)
        this.recentCards = []
      } finally {
        this.cardsLoading = false
      }
    },

    exportCards() {
      this.showExportModal = true
    },

    async handleExport(format) {
      this.exporting = true
      this.showExportModal = false

      try {
        await batchApi.exportProductCards(format)

        this.successMessage = `Export job submitted successfully! You will receive the ${format} file via email once processing is complete.`
        this.showSuccessNotification = true

        setTimeout(() => {
          this.showSuccessNotification = false
        }, 5000)
      } catch (error) {
        console.error('Export failed:', error)
        alert('Export failed. Please try again.')
      } finally {
        this.exporting = false
      }
    },

    handleFormSuccess(updatedRetailer) {
      this.retailer = updatedRetailer
      this.showEditForm = false
      this.invalidateCounts()
      this.fetchCounts(true)
      this.fetchRecentCards()
    },

    handleLogoError(event) {
      event.target.style.display = 'none'
    },

    formatPrice(price) {
      return parseFloat(price).toFixed(2)
    },

    importProducts() {
      this.showImportModal = true
    },

    async handleImportSuccess({ file, verifiedProductsOnly }) {
      this.showImportModal = false

      try {
        await batchApi.importProductCards(file, verifiedProductsOnly)

        this.successMessage =
          'Import job submitted successfully! Your products will be processed in the background.'
        this.showSuccessNotification = true

        this.invalidateCounts()
        await Promise.all([this.fetchCounts(true), this.fetchRecentCards()])

        setTimeout(() => {
          this.showSuccessNotification = false
        }, 5000)
      } catch (error) {
        console.error('Import failed:', error)
        alert('Import failed. Please try again.')
      }
    },

    confirmDeleteRetailer() {
      this.showDeleteModal = true
    },

    async handleDeleteRetailer() {
      try {
        await retailersApi.deleteRetailer()

        this.showDeleteModal = false

        this.successMessage = 'Organization deleted successfully! Redirecting...'
        this.showSuccessNotification = true

        this.invalidateCounts()
        setTimeout(() => {
          this.$router.push('/')
        }, 2000)
      } catch (error) {
        console.error('Delete failed:', error)
        alert('Failed to delete organization. Please try again.')
      }
    },
  },
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
