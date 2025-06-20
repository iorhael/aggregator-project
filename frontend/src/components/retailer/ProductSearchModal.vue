<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-10 mx-auto p-5 border w-full max-w-4xl shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <!-- Header -->
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-medium text-gray-900">Search Products</h3>
          <button @click="$emit('close')" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              ></path>
            </svg>
          </button>
        </div>

        <div class="mb-6">
          <div class="relative">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg
                class="h-5 w-5 text-gray-400"
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
            </div>
            <input
              v-model="searchQuery"
              @input="onSearchInput"
              type="text"
              placeholder="Search products by name..."
              class="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <p class="text-sm text-gray-500 mt-2">Start typing to search for products</p>
        </div>

        <div v-if="searching" class="flex justify-center items-center py-8">
          <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        </div>

        <div v-else-if="products.length > 0" class="space-y-3 max-h-96 overflow-y-auto">
          <div
            v-for="product in products"
            :key="product.id"
            class="border border-gray-200 rounded-lg p-4 hover:border-blue-300 hover:bg-blue-50 transition-colors cursor-pointer"
            @click="selectProduct(product)"
          >
            <div class="flex items-start space-x-4">
              <div class="w-16 h-16 bg-gray-100 rounded-lg overflow-hidden flex-shrink-0">
                <img
                  :src="product.image_link || placeholderImage"
                  :alt="product.name"
                  @error="handleImageError"
                  class="w-full h-full object-cover"
                />
              </div>

              <div class="flex-1 min-w-0">
                <h4 class="text-sm font-medium text-gray-900 mb-1 line-clamp-2">
                  {{ product.name }}
                </h4>
                <p class="text-sm text-gray-600 mb-2">{{ product.vendor_name }}</p>

                <div class="flex flex-wrap items-center gap-4 text-xs text-gray-500">
                  <div class="flex items-center">
                    <svg
                      class="w-4 h-4 mr-1 text-yellow-400"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path
                        d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                      />
                    </svg>
                    {{ product.average_rating || 'N/A' }}
                  </div>
                  <div>{{ product.offers_count || 0 }} offers</div>
                  <div v-if="product.is_verified" class="text-green-600 font-medium">
                    ✓ Verified
                  </div>
                </div>
              </div>

              <div class="text-right flex-shrink-0">
                <div class="text-lg font-bold text-gray-900">
                  ${{ formatPrice(product.minimal_price) }}
                </div>
                <div class="text-xs text-gray-500">starting from</div>
              </div>
            </div>
          </div>
        </div>

        <div v-else-if="searchQuery && !searching" class="text-center py-8">
          <svg
            class="mx-auto h-12 w-12 text-gray-400 mb-4"
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
          <h3 class="text-lg font-medium text-gray-900 mb-2">No products found</h3>
          <p class="text-gray-600">Try searching with different keywords</p>
        </div>

        <div v-else class="text-center py-8">
          <svg
            class="mx-auto h-12 w-12 text-gray-400 mb-4"
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
          <h3 class="text-lg font-medium text-gray-900 mb-2">Search for Products</h3>
          <p class="text-gray-600">Enter a product name to start searching</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { productsApi } from '@/api'
import defaultProductImage from '@/assets/product-default.png'

export default {
  name: 'ProductSearchModal',
  emits: ['close', 'select'],
  data() {
    return {
      searchQuery: '',
      products: [],
      searching: false,
      debounceTimer: null,
      placeholderImage: defaultProductImage,
    }
  },
  beforeUnmount() {
    if (this.debounceTimer) {
      clearTimeout(this.debounceTimer)
    }
  },
  methods: {
    onSearchInput() {
      if (this.debounceTimer) {
        clearTimeout(this.debounceTimer)
      }

      this.debounceTimer = setTimeout(() => {
        if (this.searchQuery.trim().length >= 2) {
          this.searchProducts()
        } else {
          this.products = []
        }
      }, 500)
    },

    async searchProducts() {
      this.searching = true

      try {
        const response = await productsApi.searchProducts(
          { product_name: this.searchQuery.trim() },
          0,
          15,
        )

        this.products = response.data.content || response.data || []
      } catch (error) {
        console.error('Search failed:', error)
        this.products = []
      } finally {
        this.searching = false
      }
    },

    selectProduct(product) {
      this.$emit('select', product)
    },

    formatPrice(price) {
      return parseFloat(price).toFixed(2)
    },

    handleImageError(event) {
      event.target.src = this.placeholderImage
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
