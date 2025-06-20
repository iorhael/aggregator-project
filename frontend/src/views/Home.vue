<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gray-50 min-h-screen">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div class="mb-8">
          <h2 class="text-3xl font-bold text-gray-900 mb-2">
            <span v-if="isSearchMode && searchQuery">Search Results for "{{ searchQuery }}"</span>
            <span v-else>Top Products</span>
          </h2>
          <p class="text-gray-600">
            <span v-if="isSearchMode && searchQuery"
              >Found {{ totalProducts }} products matching your search</span
            >
            <span v-else>Discover the best deals and highest-rated products</span>
          </p>
        </div>

        <div class="mb-8">
          <h3 class="text-lg font-medium text-gray-900 mb-4">Browse by Category</h3>

          <div v-if="categoriesLoading" class="flex justify-center py-8">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          </div>

          <div v-else class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4">
            <button
              v-for="category in categories"
              :key="category.category_id"
              @click="openCategoryTree(category)"
              class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow p-4 text-center group"
            >
              <div
                class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-3 group-hover:bg-blue-200 transition-colors"
              >
                <svg class="w-6 h-6 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                  <path
                    fill-rule="evenodd"
                    d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
                    clip-rule="evenodd"
                  />
                </svg>
              </div>
              <h4
                class="text-sm font-medium text-gray-900 group-hover:text-blue-600 transition-colors"
              >
                {{ category.name }}
              </h4>
            </button>
          </div>
        </div>

        <div class="mb-6">
          <SearchBar :is-searching="isSearching" @search="onSearch" @clear="onClearSearch" />
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
              <h3 class="text-sm font-medium text-red-800">Error loading products</h3>
              <p class="text-sm text-red-700 mt-1">{{ error }}</p>
            </div>
          </div>
        </div>

        <div
          v-else-if="products.length > 0"
          class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6 mb-8"
        >
          <div
            v-for="product in products"
            :key="product.id"
            class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300 overflow-hidden"
          >
            <div class="aspect-square bg-gray-100 relative">
              <img
                :src="product.image_link || placeholderImage"
                :alt="product.name"
                @error="handleImageError"
                class="w-full h-full object-cover"
              />
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

            <div class="p-4">
              <h3 class="font-semibold text-gray-900 text-sm mb-1 line-clamp-2">
                {{ product.name }}
              </h3>
              <p class="text-gray-600 text-xs mb-2">{{ product.vendor_name }}</p>

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
                      d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-.181h3.461a1 1 0 00.951-.69l1.07-3.292z"
                    />
                  </svg>
                </div>
                <span class="text-xs text-gray-600 ml-1"
                  >{{ product.average_rating }} ({{ product.comments_count || 0 }})</span
                >
              </div>

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

        <div v-else class="text-center py-12">
          <img
            src="@/assets/no-products.png"
            alt="No Products Image"
            class="mx-auto h-12 w-12 text-gray-400"
          />
          <h3 class="mt-2 text-sm font-medium text-gray-900">No products found</h3>
          <p class="mt-1 text-sm text-gray-500">
            Try selecting a different category or check back later.
          </p>
        </div>

        <div v-if="totalPages > 1" class="flex justify-center items-center space-x-2">
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

        <div v-if="products.length > 0" class="text-center mt-4 text-sm text-gray-600">
          Showing {{ (currentPage - 1) * pageSize + 1 }} to
          {{ Math.min(currentPage * pageSize, totalProducts) }} of {{ totalProducts }} products
        </div>

        <CategoryTreeModal
          v-if="showCategoryModal"
          :root-category="selectedRootCategory"
          @close="showCategoryModal = false"
          @select="handleCategorySelect"
        />
      </div>
    </div>
  </AppLayout>
</template>

<script>
import SearchBar from '@/components/SearchBar.vue'
import { productsApi, categoriesApi } from '@/api'
import productDefaultImage from '@/assets/product-default.png'
import AppLayout from '@/components/AppLayout.vue'
import CategoryTreeModal from '@/components/CategoryTreeModal.vue'

export default {
  name: 'HomePage',
  components: {
    AppLayout,
    SearchBar,
    CategoryTreeModal,
  },
  data() {
    return {
      products: [],
      categories: [],
      loading: false,
      categoriesLoading: false,
      error: null,
      currentPage: 1,
      pageSize: 15,
      totalProducts: 0,
      totalPages: 0,
      placeholderImage: productDefaultImage,
      searchQuery: '',
      isSearchMode: false,
      isSearching: false,
      showCategoryModal: false,
      selectedRootCategory: null,
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
    this.fetchCategories()
    this.fetchProducts()
  },
  methods: {
    async fetchCategories() {
      this.categoriesLoading = true
      try {
        const response = await categoriesApi.getTopLevelCategories(0, 100)
        this.categories = response.data || []
      } catch (err) {
        console.error('Failed to load categories:', err)
      } finally {
        this.categoriesLoading = false
      }
    },
    openCategoryTree(category) {
      this.selectedRootCategory = category
      this.showCategoryModal = true
    },

    handleCategorySelect(category) {
      this.showCategoryModal = false
      this.$router.push({
        name: 'ProductSearchWithCategory',
        params: {
          categoryId: category.category_id,
          categoryName: category.name,
        },
      })
    },
    async fetchProducts() {
      this.loading = true
      this.error = null

      try {
        let response

        if (this.isSearchMode && this.searchQuery) {
          response = await productsApi.searchProducts(
            { product_name: this.searchQuery },
            this.currentPage - 1,
            this.pageSize,
          )
        } else {
          response = await productsApi.getProducts(this.currentPage - 1, this.pageSize)
        }

        this.products = response.data.content || response.data
        this.totalProducts = response.data.total_elements || response.data.length
        this.totalPages = response.data.total_pages || Math.ceil(this.totalProducts / this.pageSize)
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to load products. Please try again.'
        this.products = []
      } finally {
        this.loading = false
      }
    },

    goToPage(page) {
      if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
        this.currentPage = page
        this.fetchProducts()
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    },

    formatPrice(price) {
      return parseFloat(price).toFixed(2)
    },

    handleImageError(event) {
      event.target.src = this.placeholderImage
    },

    async onSearch(query) {
      this.searchQuery = query
      this.isSearchMode = true
      this.isSearching = true
      this.currentPage = 1

      try {
        await this.fetchProducts()
      } finally {
        this.isSearching = false
      }
    },

    onClearSearch() {
      this.searchQuery = ''
      this.isSearchMode = false
      this.currentPage = 1
      this.fetchProducts()
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
