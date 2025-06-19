<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <!-- Navigation Subheader -->
    <div v-if="product" class="bg-white border-b border-gray-200">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div
          class="flex flex-col sm:flex-row justify-center items-center py-4 space-y-2 sm:space-y-0 sm:space-x-8"
        >
          <router-link
            :to="{ name: 'ProductDetail', params: { id: product.id } }"
            class="text-gray-500 hover:text-gray-700 pb-2 transition-colors"
          >
            About Product
          </router-link>
          <button class="text-blue-600 border-b-2 border-blue-600 pb-2 font-medium">
            Offers ({{ totalOffers || 0 }})
          </button>
          <router-link
            :to="{ name: 'ProductComments', params: { id: product.id } }"
            class="text-gray-500 hover:text-gray-700 pb-2 transition-colors"
          >
            Comments ({{ product.comments_count || 0 }})
          </router-link>
          <button class="text-gray-500 hover:text-gray-700 pb-2" disabled>Reviews (0)</button>
        </div>
      </div>
    </div>
    <div class="bg-gray-50 min-h-screen">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div class="flex flex-col lg:flex-row gap-8">
          <!-- Filters Sidebar -->
          <div class="lg:w-80 flex-shrink-0">
            <div class="bg-white rounded-lg shadow-md p-6 sticky top-8">
              <h2 class="text-lg font-semibold text-gray-900 mb-6">Filter Offers</h2>

              <!-- Product Info -->
              <div v-if="product" class="mb-6 p-4 bg-blue-50 rounded-lg">
                <h3 class="text-sm font-medium text-blue-900 mb-1">Product</h3>
                <p class="text-sm text-blue-700 font-medium">{{ product.name }}</p>
                <p class="text-xs text-blue-600 mt-1">{{ product.vendor_name }}</p>
              </div>

              <!-- Search Form -->
              <form @submit.prevent="applyFilters" class="space-y-6">
                <!-- Retailer Name -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Store/Retailer</label>
                  <input
                    v-model="filters.retailer_name"
                    type="text"
                    placeholder="Search by store name..."
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>

                <!-- Warranty Period -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2"
                    >Minimum Warranty</label
                  >
                  <div class="relative">
                    <input
                      v-model="filters.warranty"
                      type="number"
                      min="1"
                      max="120"
                      placeholder="12"
                      class="w-full pr-16 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    />
                    <div
                      class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none"
                    >
                      <span class="text-gray-500 sm:text-sm">months</span>
                    </div>
                  </div>
                </div>

                <!-- Installment Period -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2"
                    >Minimum Installment</label
                  >
                  <div class="relative">
                    <input
                      v-model="filters.installment_period"
                      type="number"
                      min="1"
                      max="120"
                      placeholder="6"
                      class="w-full pr-16 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    />
                    <div
                      class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none"
                    >
                      <span class="text-gray-500 sm:text-sm">months</span>
                    </div>
                  </div>
                </div>

                <!-- Max Delivery Time -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2"
                    >Maximum Delivery Time</label
                  >
                  <div class="relative">
                    <input
                      v-model="filters.max_delivery_time"
                      type="number"
                      min="1"
                      max="30"
                      placeholder="5"
                      class="w-full pr-12 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    />
                    <div
                      class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none"
                    >
                      <span class="text-gray-500 sm:text-sm">days</span>
                    </div>
                  </div>
                </div>

                <!-- Action Buttons -->
                <div class="space-y-3 pt-4">
                  <button
                    type="submit"
                    :disabled="searching"
                    class="w-full bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md font-medium transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    <span v-if="searching" class="flex items-center justify-center">
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
                      Searching...
                    </span>
                    <span v-else>Apply Filters</span>
                  </button>
                  <button
                    type="button"
                    @click="clearFilters"
                    class="w-full bg-gray-200 hover:bg-gray-300 text-gray-700 px-4 py-2 rounded-md font-medium transition-colors"
                  >
                    Clear All
                  </button>
                </div>
              </form>
            </div>
          </div>

          <!-- Offers Content -->
          <div class="flex-1">
            <!-- Results Header -->
            <div class="mb-6">
              <h1 class="text-2xl font-bold text-gray-900 mb-2">
                All Offers
                <span v-if="product" class="text-lg font-normal text-gray-600"
                  >for {{ product.name }}</span
                >
              </h1>
              <p class="text-gray-600">
                <span v-if="totalOffers > 0">Found {{ totalOffers }} offers</span>
                <span v-else-if="!loading && !initialLoad">No offers found</span>
                <span v-else>Loading offers...</span>
              </p>
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
                  <h3 class="text-sm font-medium text-red-800">Error loading offers</h3>
                  <p class="text-sm text-red-700 mt-1">{{ error }}</p>
                </div>
              </div>
            </div>

            <!-- Offers Grid -->
            <div
              v-else-if="offers.length > 0"
              class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8"
            >
              <div
                v-for="offer in offers"
                :key="offer.product_card_id"
                class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300 overflow-hidden"
              >
                <div class="p-6">
                  <!-- Header with Price and Store -->
                  <div class="flex justify-between items-start mb-4">
                    <div>
                      <p class="text-2xl font-bold text-gray-900">
                        ${{ formatPrice(offer.price) }}
                      </p>
                      <router-link
                        :to="{ name: 'RetailerInfoPublic', params: { name: offer.retailer_name } }"
                        class="text-blue-600 hover:text-blue-500 font-medium"
                      >
                        {{ offer.retailer_name || 'Unknown Store' }}
                      </router-link>
                    </div>
                  </div>

                  <!-- Description -->
                  <div v-if="offer.description" class="mb-4">
                    <p class="text-sm text-gray-700 line-clamp-3">{{ offer.description }}</p>
                  </div>

                  <!-- Offer Details -->
                  <div class="space-y-3">
                    <div class="space-y-2 text-sm">
                      <div class="flex justify-between">
                        <span class="text-gray-500">Warranty:</span>
                        <span class="font-medium text-gray-900">
                          {{ offer.warranty_period || 'N/A' }}
                          <span v-if="offer.warranty_period">months</span>
                        </span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-500">Delivery:</span>
                        <span class="font-medium text-gray-900">
                          {{ offer.max_delivery_time || 'N/A' }}
                          <span v-if="offer.max_delivery_time">days</span>
                        </span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-500">Installment:</span>
                        <span class="font-medium text-gray-900">
                          {{ offer.installment_period || 'N/A' }}
                          <span v-if="offer.installment_period">months</span>
                        </span>
                      </div>
                    </div>
                  </div>

                  <!-- Action Button -->
                  <div class="mt-4 pt-4 border-t border-gray-200">
                    <router-link
                      :to="{ name: 'RetailerInfoPublic', params: { name: offer.retailer_name } }"
                      class="w-full bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md font-medium transition-colors block text-center"
                    >
                      View Retailer Info
                    </router-link>
                  </div>
                </div>
              </div>
            </div>

            <!-- Empty State -->
            <div v-else-if="!initialLoad" class="text-center py-12">
              <img
                src="@/assets/no-cards.png"
                alt="No Offers Image"
                class="mx-auto h-12 w-12 text-gray-400"
              />
              <h3 class="mt-2 text-sm font-medium text-gray-900">No offers found</h3>
              <p class="mt-1 text-sm text-gray-500">
                Try adjusting your filters or check back later.
              </p>
            </div>

            <!-- Initial State -->
            <div v-else class="text-center py-12">
              <svg
                class="mx-auto h-12 w-12 text-gray-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"
                />
              </svg>
              <h3 class="mt-2 text-sm font-medium text-gray-900">Loading offers...</h3>
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
            <div v-if="offers.length > 0" class="text-center mt-4 text-sm text-gray-600">
              Showing {{ (currentPage - 1) * pageSize + 1 }} to
              {{ Math.min(currentPage * pageSize, totalOffers) }} of {{ totalOffers }} offers
            </div>
          </div>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script>
import AppLayout from '@/components/AppLayout.vue'
import { productsApi } from '@/api'

export default {
  name: 'ProductOffers',
  components: {
    AppLayout,
  },
  data() {
    return {
      product: null,
      filters: {
        retailer_name: '',
        warranty: '',
        installment_period: '',
        max_delivery_time: '',
      },
      offers: [],
      loading: false,
      searching: false,
      error: null,
      currentPage: 1,
      pageSize: 15,
      totalOffers: 0,
      totalPages: 0,
      initialLoad: true,
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
    this.loadInitialData()
  },
  methods: {
    async loadInitialData() {
      const productId = this.$route.params.id
      if (!productId) {
        this.error = 'Product ID is required'
        return
      }

      try {
        // Load product info and initial offers
        const [productResponse] = await Promise.all([productsApi.getProductDetails(productId)])

        this.product = productResponse.data

        // Load all offers without filters
        await this.searchOffers()
      } catch (err) {
        console.error('Failed to load initial data:', err)
        this.error = err.response?.data?.message || 'Failed to load product information'
      }
    },

    async applyFilters() {
      this.currentPage = 1
      await this.searchOffers()
    },

    async searchOffers() {
      const productId = this.$route.params.id
      if (!productId) return

      this.loading = true
      this.searching = true
      this.error = null
      this.initialLoad = false

      try {
        // Build filter criteria
        const filterCriteria = {}

        if (this.filters.retailer_name.trim()) {
          filterCriteria.retailer_name = this.filters.retailer_name.trim()
        }

        if (this.filters.warranty) {
          filterCriteria.warranty = parseInt(this.filters.warranty)
        }

        if (this.filters.installment_period) {
          filterCriteria.installment_period = parseInt(this.filters.installment_period)
        }

        if (this.filters.max_delivery_time) {
          filterCriteria.max_delivery_time = parseInt(this.filters.max_delivery_time)
        }

        const response = await productsApi.searchProductOffers(
          productId,
          filterCriteria,
          this.currentPage - 1,
          this.pageSize,
        )

        // Handle new pagination format
        this.offers = response.data.content || []
        this.totalOffers = response.data.total_elements || 0
        this.totalPages = response.data.total_pages || 0
      } catch (err) {
        console.error('Search error:', err)
        this.error = err.response?.data?.message || 'Failed to search offers'
        this.offers = []
      } finally {
        this.loading = false
        this.searching = false
      }
    },

    clearFilters() {
      this.filters = {
        retailer_name: '',
        warranty: '',
        installment_period: '',
        max_delivery_time: '',
      }
      this.currentPage = 1
      this.searchOffers()
    },

    goToPage(page) {
      if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
        this.currentPage = page
        this.searchOffers()
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    },

    goBackToProduct() {
      this.$router.push({ name: 'ProductDetail', params: { id: this.$route.params.id } })
    },

    formatPrice(price) {
      return parseFloat(price).toFixed(2)
    },
  },
}
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
