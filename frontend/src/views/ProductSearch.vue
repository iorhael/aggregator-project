<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gray-50 min-h-screen">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div class="flex flex-col lg:flex-row gap-8">
          <!-- Filters Sidebar -->
          <div class="lg:w-80 flex-shrink-0">
            <div class="bg-white rounded-lg shadow-md p-6 sticky top-8">
              <h2 class="text-lg font-semibold text-gray-900 mb-6">Filters</h2>

              <!-- Category Info -->
              <div v-if="selectedCategory" class="mb-6 p-4 bg-blue-50 rounded-lg">
                <h3 class="text-sm font-medium text-blue-900 mb-1">Category</h3>
                <p class="text-sm text-blue-700">{{ selectedCategory.name }}</p>
                <button
                  @click="clearCategory"
                  class="text-xs text-blue-600 hover:text-blue-500 mt-1"
                >
                  Clear category
                </button>
              </div>

              <!-- Search Form -->
              <form @submit.prevent="applyFilters" class="space-y-6">
                <!-- Product Name -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Product Name</label>
                  <input
                    v-model="filters.product_name"
                    type="text"
                    placeholder="Search by product name..."
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>

                <!-- Vendor Name -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Brand/Vendor</label>
                  <input
                    v-model="filters.vendor_name"
                    type="text"
                    placeholder="Search by brand..."
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>

                <!-- Price Range -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Minimum Price</label>
                  <div class="relative">
                    <div
                      class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"
                    >
                      <span class="text-gray-500 sm:text-sm">$</span>
                    </div>
                    <input
                      v-model="filters.min_price"
                      type="number"
                      step="0.01"
                      min="0"
                      placeholder="0.00"
                      class="w-full pl-7 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    />
                  </div>
                </div>

                <!-- Minimum Offers -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Minimum Offers</label>
                  <input
                    v-model="filters.min_offers_count"
                    type="number"
                    min="1"
                    placeholder="1"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>

                <!-- Average Rating -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Minimum Rating</label>
                  <select
                    v-model="filters.average_rating"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  >
                    <option value="">Any rating</option>
                    <option value="1">1+ stars</option>
                    <option value="2">2+ stars</option>
                    <option value="3">3+ stars</option>
                    <option value="4">4+ stars</option>
                    <option value="4.5">4.5+ stars</option>
                  </select>
                </div>

                <!-- Verification Status -->
                <div>
                  <label class="flex items-center cursor-pointer">
                    <input
                      v-model="filters.verification_status"
                      type="checkbox"
                      class="rounded border-gray-300 text-blue-600 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50"
                    />
                    <span class="ml-2 text-sm text-gray-700">Verified products only</span>
                  </label>
                </div>

                <!-- Technical Characteristics -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Technical Characteristics
                  </label>
                  <div class="space-y-3">
                    <div
                      v-for="(characteristic, index) in characteristicsList"
                      :key="index"
                      class="space-y-2"
                    >
                      <div class="flex items-center space-x-2">
                        <input
                          v-model="characteristic.key"
                          type="text"
                          placeholder="Characteristic name (e.g., RAM, CPU)"
                          class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-sm"
                        />
                        <button
                          type="button"
                          @click="removeCharacteristic(index)"
                          class="text-red-600 hover:text-red-500 p-2"
                        >
                          <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                            <path
                              fill-rule="evenodd"
                              d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                              clip-rule="evenodd"
                            />
                          </svg>
                        </button>
                      </div>
                      <input
                        v-model="characteristic.value"
                        type="text"
                        placeholder="Required value (e.g., 8GB, Intel i5)"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-sm ml-0"
                      />
                    </div>
                    <button
                      type="button"
                      @click="addCharacteristic"
                      class="w-full text-left text-sm text-blue-600 hover:text-blue-500 py-2"
                    >
                      + Add characteristic
                    </button>
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

          <!-- Products Content -->
          <div class="flex-1">
            <!-- Results Header -->
            <div class="mb-6">
              <h1 class="text-2xl font-bold text-gray-900 mb-2">
                <span v-if="selectedCategory">{{ selectedCategory.name }}</span>
                <span v-else>Product Search</span>
              </h1>
              <p class="text-gray-600">
                <span v-if="totalProducts > 0">Found {{ totalProducts }} products</span>
                <span v-else-if="!loading && !initialLoad">No products found</span>
                <span v-else>Search for products using the filters</span>
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
                  <h3 class="text-sm font-medium text-red-800">Error loading products</h3>
                  <p class="text-sm text-red-700 mt-1">{{ error }}</p>
                </div>
              </div>
            </div>

            <!-- Products Grid -->
            <div
              v-else-if="products.length > 0"
              class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8"
            >
              <div
                v-for="product in products"
                :key="product.id"
                class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300 overflow-hidden"
              >
                <!-- Product Image -->
                <div class="aspect-square bg-gray-100 relative">
                  <img
                    :src="product.image_link || placeholderImage"
                    :alt="product.name"
                    @error="handleImageError"
                    class="w-full h-full object-cover"
                  />
                  <!-- Verified Badge -->
                  <div v-if="product.is_verified" class="absolute top-2 right-2">
                    <div
                      class="bg-green-500 text-white px-2 py-1 rounded-full text-xs font-medium flex items-center"
                    >
                      <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                        <path
                          fill-rule="evenodd"
                          d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                          clip-rule="evenodd"
                        />
                      </svg>
                      Verified
                    </div>
                  </div>
                </div>

                <!-- Product Info -->
                <div class="p-4">
                  <h3 class="font-semibold text-gray-900 text-sm mb-1 line-clamp-2">
                    {{ product.name }}
                  </h3>
                  <p class="text-gray-600 text-xs mb-2">{{ product.vendor_name }}</p>

                  <!-- Rating -->
                  <div class="flex items-center mb-2">
                    <div class="flex items-center">
                      <svg
                        v-for="star in 5"
                        :key="star"
                        class="w-4 h-4"
                        :class="
                          star <= Math.round(product.average_rating)
                            ? 'text-yellow-400'
                            : 'text-gray-300'
                        "
                        fill="currentColor"
                        viewBox="0 0 20 20"
                      >
                        <path
                          d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                        />
                      </svg>
                    </div>
                    <span class="text-xs text-gray-600 ml-1"
                      >{{ product.average_rating }} ({{ product.comments_count }})</span
                    >
                  </div>

                  <!-- Price and Offers -->
                  <div class="flex justify-between items-center">
                    <div>
                      <span class="text-lg font-bold text-gray-900"
                        >${{ formatPrice(product.minimal_price) }}</span
                      >
                      <p class="text-xs text-gray-500">{{ product.offers_count }} offers</p>
                    </div>
                    <button
                      @click="viewProductDetails(product.id)"
                      class="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded text-xs font-medium transition-colors"
                    >
                      View Details
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Empty State -->
            <div v-else-if="!initialLoad" class="text-center py-12">
              <img
                src="@/assets/no-products.png"
                alt="No Products Image"
                class="mx-auto h-12 w-12 text-gray-400"
              />
              <h3 class="mt-2 text-sm font-medium text-gray-900">No products found</h3>
              <p class="mt-1 text-sm text-gray-500">
                Try adjusting your filters or search criteria.
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
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                />
              </svg>
              <h3 class="mt-2 text-sm font-medium text-gray-900">Start your search</h3>
              <p class="mt-1 text-sm text-gray-500">
                Use the filters on the left to find products.
              </p>
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
            <div v-if="products.length > 0" class="text-center mt-4 text-sm text-gray-600">
              Showing {{ (currentPage - 1) * pageSize + 1 }} to
              {{ Math.min(currentPage * pageSize, totalProducts) }} of {{ totalProducts }} products
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
import productDefaultImage from '@/assets/product-default.png'

export default {
  name: 'ProductSearch',
  components: {
    AppLayout,
  },
  data() {
    return {
      selectedCategory: null,
      filters: {
        product_name: '',
        vendor_name: '',
        category_name: '',
        verification_status: false,
        min_price: '',
        min_offers_count: '',
        average_rating: '',
        characteristics: {},
      },
      characteristicsList: [],
      products: [],
      loading: false,
      searching: false,
      error: null,
      currentPage: 1,
      pageSize: 15,
      totalProducts: 0,
      totalPages: 0,
      placeholderImage: productDefaultImage,
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
  watch: {
    // Watch for route changes
    $route(to, from) {
      this.handleRouteChange()
    },
  },
  mounted() {
    this.handleRouteChange()
  },
  methods: {
    handleRouteChange() {
      // Reset state
      this.selectedCategory = null
      this.filters.category_name = ''

      // Check if category was passed via route params
      if (this.$route.params.categoryId && this.$route.params.categoryName) {
        console.log('Setting category from params:', this.$route.params)
        this.selectedCategory = {
          category_id: this.$route.params.categoryId,
          name: this.$route.params.categoryName,
        }
        this.filters.category_name = this.selectedCategory.name
        this.initialLoad = false
        this.searchProducts()
      }
      // Check for query parameters
      else if (this.$route.query.category) {
        console.log('Setting category from query:', this.$route.query.category)
        this.filters.category_name = this.$route.query.category
        this.initialLoad = false
        this.searchProducts()
      }
    },

    addCharacteristic() {
      this.characteristicsList.push({ key: '', value: '' })
    },

    removeCharacteristic(index) {
      this.characteristicsList.splice(index, 1)
    },

    buildCharacteristics() {
      const characteristics = {}
      this.characteristicsList.forEach((char) => {
        if (char.key.trim() && char.value.trim()) {
          characteristics[char.key.trim()] = char.value.trim()
        }
      })
      return characteristics
    },

    async applyFilters() {
      this.currentPage = 1
      await this.searchProducts()
    },

    async searchProducts() {
      this.loading = true
      this.searching = true
      this.error = null
      this.initialLoad = false

      try {
        // Build search criteria
        const searchCriteria = {}

        if (this.filters.product_name.trim()) {
          searchCriteria.product_name = this.filters.product_name.trim()
        }

        if (this.filters.vendor_name.trim()) {
          searchCriteria.vendor_name = this.filters.vendor_name.trim()
        }

        if (this.filters.category_name.trim()) {
          searchCriteria.category_name = this.filters.category_name.trim()
        }

        if (this.filters.verification_status) {
          searchCriteria.verification_status = true
        }

        if (this.filters.min_price) {
          searchCriteria.min_price = parseFloat(this.filters.min_price)
        }

        if (this.filters.min_offers_count) {
          searchCriteria.min_offers_count = parseInt(this.filters.min_offers_count)
        }

        if (this.filters.average_rating) {
          searchCriteria.average_rating = parseFloat(this.filters.average_rating)
        }

        const characteristics = this.buildCharacteristics()
        if (Object.keys(characteristics).length > 0) {
          searchCriteria.characteristics = characteristics
        }

        console.log('Search criteria:', searchCriteria)

        const response = await productsApi.searchProducts(
          searchCriteria,
          this.currentPage - 1,
          this.pageSize,
        )

        console.log('Search response:', response.data)

        this.products = response.data.content || response.data || []
        this.totalProducts = response.data.total_elements || response.data.length || 0
        this.totalPages =
          response.data.total_pages || Math.ceil(this.totalProducts / this.pageSize) || 0
      } catch (err) {
        console.error('Search error:', err)
        this.error = err.response?.data?.message || 'Failed to search products'
        this.products = []
      } finally {
        this.loading = false
        this.searching = false
      }
    },

    clearFilters() {
      this.filters = {
        product_name: '',
        vendor_name: '',
        category_name: this.selectedCategory ? this.selectedCategory.name : '',
        verification_status: false,
        min_price: '',
        min_offers_count: '',
        average_rating: '',
        characteristics: {},
      }
      this.characteristicsList = []
      this.currentPage = 1
      this.products = []
      this.totalProducts = 0
      this.totalPages = 0
      this.initialLoad = true
    },

    clearCategory() {
      this.selectedCategory = null
      this.filters.category_name = ''
      this.currentPage = 1
      this.products = []
      this.totalProducts = 0
      this.totalPages = 0
      this.initialLoad = true
      this.$router.push({ name: 'ProductSearch' })
    },

    goToPage(page) {
      if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
        this.currentPage = page
        this.searchProducts()
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    },

    formatPrice(price) {
      return parseFloat(price).toFixed(2)
    },

    handleImageError(event) {
      event.target.src = this.placeholderImage
    },

    viewProductDetails(productId) {
      this.$router.push({ name: 'ProductDetail', params: { id: productId } })
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
