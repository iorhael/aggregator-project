<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gray-50 min-h-screen">
      <!-- Navigation Subheader -->
      <div class="bg-white border-b border-gray-200">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div
            class="flex flex-col sm:flex-row justify-center items-center py-4 space-y-2 sm:space-y-0 sm:space-x-8"
          >
            <router-link to="/retailer" class="text-gray-500 hover:text-gray-700 pb-2">
              Organization Info
            </router-link>
            <button class="text-blue-600 border-b-2 border-blue-600 pb-2 font-medium">
              Cards Management ({{ totalCards || 0 }})
            </button>
            <router-link to="/stores" class="text-gray-500 hover:text-gray-700 pb-2">
              Stores Management ({{ storesCount || 0 }})
            </router-link>
            <router-link to="/jobs" class="text-gray-500 hover:text-gray-700 pb-2">
              Jobs History
            </router-link>
          </div>
        </div>
      </div>

      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- Page Header -->
        <div
          class="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-8 space-y-4 sm:space-y-0"
        >
          <div>
            <h1 class="text-3xl font-bold text-gray-900 mb-2">Product Cards Management</h1>
            <p class="text-gray-600">
              Manage your product cards, update pricing, warranty, and delivery information.
            </p>
          </div>
          <button
            @click="createProductCard"
            class="bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded-md font-medium transition-colors flex items-center"
          >
            <svg class="w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 6v6m0 0v6m0-6h6m-6 0H6"
              />
            </svg>
            Create Product Card
          </button>
        </div>

        <!-- Loading State -->
        <div v-if="loading" class="flex justify-center items-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Error State -->
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
              <h3 class="text-sm font-medium text-red-800">Error loading product cards</h3>
              <p class="text-sm text-red-700 mt-1">{{ error }}</p>
              <button
                @click="fetchProductCards"
                class="mt-2 text-sm text-red-600 hover:text-red-500 underline"
              >
                Try again
              </button>
            </div>
          </div>
        </div>

        <!-- Product Cards List -->
        <div v-else-if="productCards.length > 0">
          <!-- Desktop Table -->
          <div class="hidden lg:block bg-white shadow-md rounded-lg overflow-hidden">
            <!-- Table Header -->
            <div class="bg-gray-50 px-6 py-3 border-b border-gray-200">
              <div
                class="grid grid-cols-12 gap-4 text-xs font-medium text-gray-500 uppercase tracking-wide"
              >
                <div class="col-span-3">Product</div>
                <div class="col-span-2">Vendor</div>
                <div class="col-span-1">Price</div>
                <div class="col-span-1">Warranty</div>
                <div class="col-span-1">Installment</div>
                <div class="col-span-1">Delivery</div>
                <div class="col-span-3">Actions</div>
              </div>
            </div>

            <!-- Table Body -->
            <div class="divide-y divide-gray-200">
              <div
                v-for="card in productCards"
                :key="card.product_card_id"
                class="px-6 py-4 hover:bg-gray-50 transition-colors"
              >
                <div class="grid grid-cols-12 gap-4 items-center">
                  <!-- Product Name -->
                  <div class="col-span-3">
                    <h3 class="text-sm font-medium text-gray-900 line-clamp-2">{{ card.name }}</h3>
                  </div>

                  <!-- Vendor -->
                  <div class="col-span-2">
                    <p class="text-sm text-gray-600">{{ card.vendor_name || 'Not defined' }}</p>
                  </div>

                  <!-- Price -->
                  <div class="col-span-1">
                    <p class="text-sm font-semibold text-gray-900">
                      {{ card.price ? '$' + formatPrice(card.price) : 'Not defined' }}
                    </p>
                  </div>

                  <!-- Warranty -->
                  <div class="col-span-1">
                    <p class="text-sm text-gray-600">
                      {{ card.warranty_period ? card.warranty_period + ' mo' : 'Not defined' }}
                    </p>
                  </div>

                  <!-- Installment -->
                  <div class="col-span-1">
                    <p class="text-sm text-gray-600">
                      {{
                        card.installment_period ? card.installment_period + ' mo' : 'Not defined'
                      }}
                    </p>
                  </div>

                  <!-- Delivery -->
                  <div class="col-span-1">
                    <p class="text-sm text-gray-600">
                      {{
                        card.max_delivery_time ? card.max_delivery_time + ' days' : 'Not defined'
                      }}
                    </p>
                  </div>

                  <!-- Actions -->
                  <div class="col-span-3">
                    <div class="flex space-x-2">
                      <button
                        @click="editCard(card)"
                        class="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded text-xs font-medium transition-colors"
                      >
                        Edit
                      </button>
                      <button
                        @click="confirmDelete(card)"
                        class="bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded text-xs font-medium transition-colors"
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Mobile Cards -->
          <div class="lg:hidden space-y-4">
            <div
              v-for="card in productCards"
              :key="card.product_card_id"
              class="bg-white rounded-lg shadow-md p-4"
            >
              <div class="space-y-3">
                <!-- Product Name and Vendor -->
                <div>
                  <h3 class="text-sm font-medium text-gray-900 mb-1">{{ card.name }}</h3>
                  <p class="text-xs text-gray-600">{{ card.vendor_name || 'Not defined' }}</p>
                </div>

                <!-- Details Grid -->
                <div class="grid grid-cols-2 gap-3 text-sm">
                  <div>
                    <span class="text-gray-500">Price:</span>
                    <span class="ml-1 font-semibold text-gray-900">
                      {{ card.price ? '$' + formatPrice(card.price) : 'Not defined' }}
                    </span>
                  </div>
                  <div>
                    <span class="text-gray-500">Warranty:</span>
                    <span class="ml-1 text-gray-600">
                      {{ card.warranty_period ? card.warranty_period + ' mo' : 'Not defined' }}
                    </span>
                  </div>
                  <div>
                    <span class="text-gray-500">Installment:</span>
                    <span class="ml-1 text-gray-600">
                      {{
                        card.installment_period ? card.installment_period + ' mo' : 'Not defined'
                      }}
                    </span>
                  </div>
                  <div>
                    <span class="text-gray-500">Delivery:</span>
                    <span class="ml-1 text-gray-600">
                      {{
                        card.max_delivery_time ? card.max_delivery_time + ' days' : 'Not defined'
                      }}
                    </span>
                  </div>
                </div>

                <!-- Actions -->
                <div class="flex space-x-2 pt-2 border-t border-gray-200">
                  <button
                    @click="editCard(card)"
                    class="flex-1 bg-blue-600 hover:bg-blue-700 text-white px-3 py-2 rounded text-sm font-medium transition-colors"
                  >
                    Edit
                  </button>
                  <button
                    @click="confirmDelete(card)"
                    class="flex-1 bg-red-600 hover:bg-red-700 text-white px-3 py-2 rounded text-sm font-medium transition-colors"
                  >
                    Delete
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Empty State -->
        <div v-else class="text-center py-12">
          <div class="bg-white rounded-lg shadow-md p-8">
            <img
              src="@/assets/no-cards.png"
              alt="No Cards Image"
              class="mx-auto h-16 w-16 text-gray-400 mb-4"
            />
            <h3 class="text-xl font-semibold text-gray-900 mb-2">No Product Cards Found</h3>
            <p class="text-gray-600 mb-6">
              You don't have any product cards yet. They will appear here once you configure your
              product feed.
            </p>
          </div>
        </div>

        <!-- Pagination -->
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

        <!-- Results Info -->
        <div v-if="productCards.length > 0" class="text-center mt-4 text-sm text-gray-600">
          Showing {{ (currentPage - 1) * pageSize + 1 }} to
          {{ Math.min(currentPage * pageSize, totalCards) }} of {{ totalCards }} product cards
        </div>

        <!-- Edit Modal -->
        <ProductCardEditModal
          v-if="showEditModal"
          :card="selectedCard"
          @close="showEditModal = false"
          @success="handleEditSuccess"
        />

        <!-- Delete Confirmation Modal -->
        <DeleteConfirmationModal
          v-if="showDeleteModal"
          :item="selectedCard"
          item-type="product card"
          :item-name="selectedCard?.name"
          @close="showDeleteModal = false"
          @confirm="handleDelete"
        />

        <!-- Success Notification -->
        <SuccessNotification
          v-if="showSuccessNotification"
          :message="successMessage"
          @close="showSuccessNotification = false"
        />

        <!-- Product Search Modal -->
        <ProductSearchModal
          v-if="showSearchModal"
          @close="showSearchModal = false"
          @select="handleProductSelect"
        />

        <!-- Product Card Create Modal -->
        <ProductCardCreateModal
          v-if="showCreateModal"
          :product="selectedProduct"
          @close="showCreateModal = false"
          @success="handleCreateSuccess"
        />
      </div>
    </div>
  </AppLayout>
</template>

<script>
import AppLayout from '@/components/AppLayout.vue'
import ProductCardEditModal from '@/components/retailer/ProductCardEditModal.vue'
import DeleteConfirmationModal from '@/components/DeleteConfirmationModal.vue'
import SuccessNotification from '@/components/SuccessNotification.vue'
import countsMixin from '@/mixins/countsMixin.js'
import ProductSearchModal from '@/components/retailer/ProductSearchModal.vue'
import ProductCardCreateModal from '@/components/retailer/ProductCardCreateModal.vue'
import { productCardsApi } from '@/api'

export default {
  name: 'CardsManagement',
  components: {
    AppLayout,
    ProductCardEditModal,
    DeleteConfirmationModal,
    SuccessNotification,
    ProductSearchModal,
    ProductCardCreateModal,
  },
  mixins: [countsMixin],
  data() {
    return {
      productCards: [],
      totalCards: 0,
      totalPages: 0,
      currentPage: 1,
      pageSize: 15,
      loading: false,
      error: null,
      showEditModal: false,
      showDeleteModal: false,
      showSuccessNotification: false,
      selectedCard: null,
      successMessage: '',
      showSearchModal: false,
      showCreateModal: false,
      selectedProduct: null,
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
    this.fetchProductCards()
    this.fetchCounts()
  },
  methods: {
    async fetchProductCards() {
      this.loading = true
      this.error = null

      try {
        const response = await productCardsApi.getMyProductCards(
          this.currentPage - 1,
          this.pageSize,
        )

        // Handle paginated response structure
        this.productCards = response.data.content || []
        this.totalCards = response.data.total_elements || response.data.totalElements || 0
        this.totalPages = response.data.total_pages || response.data.totalPages || 0
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to load product cards'
        this.productCards = []
      } finally {
        this.loading = false
      }
    },

    goToPage(page) {
      if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
        this.currentPage = page
        this.fetchProductCards()
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    },

    editCard(card) {
      this.selectedCard = card
      this.showEditModal = true
    },

    confirmDelete(card) {
      this.selectedCard = card
      this.showDeleteModal = true
    },

    async handleDelete() {
      try {
        await productCardsApi.deleteProductCard(this.selectedCard.product_card_id)

        this.showDeleteModal = false
        this.selectedCard = null

        // Show success message
        this.successMessage = 'Product card deleted successfully!'
        this.showSuccessNotification = true

        // Invalidate cache and refresh
        this.invalidateCounts()
        await Promise.all([this.fetchProductCards(), this.fetchCounts(true)])

        // Auto-hide notification
        setTimeout(() => {
          this.showSuccessNotification = false
        }, 3000)
      } catch (error) {
        console.error('Delete failed:', error)
        alert('Failed to delete product card. Please try again.')
      }
    },

    async handleEditSuccess(updatedCard) {
      this.showEditModal = false
      this.selectedCard = null

      // Show success message
      this.successMessage = 'Product card updated successfully!'
      this.showSuccessNotification = true

      // Update the card in the list
      const index = this.productCards.findIndex(
        (card) => card.product_card_id === updatedCard.product_card_id,
      )
      if (index !== -1) {
        this.productCards[index] = updatedCard
      }

      // Auto-hide notification
      setTimeout(() => {
        this.showSuccessNotification = false
      }, 3000)
    },

    formatPrice(price) {
      return parseFloat(price).toFixed(2)
    },

    createProductCard() {
      this.showSearchModal = true
    },

    handleProductSelect(product) {
      this.selectedProduct = product
      this.showSearchModal = false
      this.showCreateModal = true
    },

    async handleCreateSuccess(newCard) {
      this.showCreateModal = false
      this.selectedProduct = null

      // Show success message
      this.successMessage = 'Product card created successfully!'
      this.showSuccessNotification = true

      // Invalidate cache and refresh
      this.invalidateCounts()
      await Promise.all([this.fetchProductCards(), this.fetchCounts(true)])

      // Auto-hide notification
      setTimeout(() => {
        this.showSuccessNotification = false
      }, 3000)
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
