<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gray-50 min-h-screen">
      <div v-if="product" class="bg-white border-b border-gray-200">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div
            class="flex flex-col sm:flex-row justify-center items-center py-4 space-y-2 sm:space-y-0 sm:space-x-8"
          >
            <button class="text-blue-600 border-b-2 border-blue-600 pb-2 font-medium">
              About Product
            </button>
            <router-link
              :to="{ name: 'ProductOffers', params: { id: product.id } }"
              class="text-gray-500 hover:text-gray-700 pb-2 transition-colors"
            >
              Offers ({{ product.offers_count || 0 }})
            </router-link>
            <router-link
              :to="{ name: 'ProductComments', params: { id: product.id } }"
              class="text-gray-500 hover:text-gray-700 pb-2 transition-colors"
            >
              Comments ({{ product.comments_count || 0 }})
            </router-link>
          </div>
        </div>
      </div>

      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
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
              <h3 class="text-sm font-medium text-red-800">Error loading product</h3>
              <p class="text-sm text-red-700 mt-1">{{ error }}</p>
              <button
                @click="fetchProductData"
                class="mt-2 text-sm text-red-600 hover:text-red-500 underline"
              >
                Try again
              </button>
            </div>
          </div>
        </div>

        <div v-else-if="product" class="space-y-8">
          <div class="bg-white rounded-lg shadow-md overflow-hidden">
            <div class="p-6 lg:p-8">
              <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
                <div class="space-y-4">
                  <div class="aspect-square bg-gray-100 rounded-lg overflow-hidden relative">
                    <img
                      :src="product.image_link || placeholderImage"
                      :alt="product.name"
                      @error="handleImageError"
                      class="w-full h-full object-cover"
                    />
                    <div v-if="product.is_verified" class="absolute top-4 right-4">
                      <div
                        class="bg-green-500 text-white px-3 py-1 rounded-full text-sm font-medium flex items-center"
                      >
                        <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
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

                  <div class="text-center lg:text-left space-y-3">
                    <div>
                      <p class="text-2xl font-bold text-gray-900">
                        Price from: ${{ formatPrice(product.minimal_price) }}
                      </p>
                      <p class="text-sm text-gray-600 mt-1">
                        {{ product.offers_count }} offers available
                      </p>
                    </div>

                    <button
                      @click="showPriceDynamics = true"
                      class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md font-medium transition-colors flex items-center justify-center lg:justify-start"
                    >
                      <svg
                        class="w-4 h-4 mr-2"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"
                        />
                      </svg>
                      View Price Dynamics
                    </button>
                  </div>
                </div>

                <div class="space-y-6">
                  <div class="space-y-4">
                    <h1 class="text-2xl lg:text-3xl font-bold text-gray-900">{{ product.name }}</h1>
                    <p class="text-lg text-gray-600">{{ product.vendor_name }}</p>

                    <div
                      class="flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-4 sm:space-y-0"
                    >
                      <div class="flex items-center space-x-2">
                        <div class="flex items-center">
                          <svg
                            v-for="star in 5"
                            :key="star"
                            class="w-5 h-5"
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
                        <span class="text-sm text-gray-600">
                          Average rating: {{ product.average_rating || 'N/A' }} ({{
                            product.comments_count || 0
                          }})
                        </span>
                      </div>

                      <button
                        @click="goToComments"
                        class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-md font-medium transition-colors"
                      >
                        Leave a comment
                      </button>
                    </div>
                  </div>

                  <div class="space-y-2">
                    <h3 class="text-lg font-semibold text-gray-900">Description</h3>
                    <div class="prose prose-sm max-w-none text-gray-700">
                      <p v-if="product.description" class="whitespace-pre-wrap">
                        {{ product.description }}
                      </p>
                      <p v-else class="text-gray-500 italic">No description available</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
            <div class="bg-white rounded-lg shadow-md p-6">
              <h3 class="text-lg font-semibold text-gray-900 mb-4">Technical Characteristics</h3>
              <div v-if="product.technical_characteristics">
                <TechnicalCharacteristics :characteristics="product.technical_characteristics" />
              </div>
              <div v-else class="text-gray-500 italic">No technical characteristics available</div>
            </div>

            <div class="bg-white rounded-lg shadow-md p-6">
              <div class="flex justify-between items-center mb-4">
                <h3 class="text-lg font-semibold text-gray-900">Top Offers</h3>
                <router-link
                  :to="{ name: 'ProductOffers', params: { id: product.id } }"
                  class="text-blue-600 hover:text-blue-500 text-sm font-medium"
                >
                  View All
                </router-link>
              </div>

              <div v-if="offersLoading" class="flex justify-center py-8">
                <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
              </div>

              <div v-else-if="offers.length > 0" class="space-y-4">
                <div
                  v-for="offer in offers"
                  :key="offer.product_card_id"
                  class="border border-gray-200 rounded-lg p-4 hover:border-blue-300 transition-colors"
                >
                  <div class="space-y-3">
                    <div class="flex justify-between items-start">
                      <div>
                        <p class="font-semibold text-gray-900 text-lg">
                          ${{ formatPrice(offer.price) }}
                        </p>
                        <router-link
                          :to="{
                            name: 'RetailerInfoPublic',
                            params: { name: offer.retailer_name },
                          }"
                          class="text-blue-600 hover:text-blue-500 text-sm font-medium"
                        >
                          {{ offer.retailer_name || 'Unknown Store' }}
                        </router-link>
                      </div>
                    </div>

                    <div class="grid grid-cols-1 gap-2 text-xs text-gray-600">
                      <div class="flex justify-between">
                        <span>Warranty:</span>
                        <span>{{ offer.warranty_period || 'N/A' }} months</span>
                      </div>
                      <div class="flex justify-between">
                        <span>Max delivery:</span>
                        <span>{{ offer.max_delivery_time || 'N/A' }} days</span>
                      </div>
                      <div class="flex justify-between">
                        <span>Installment:</span>
                        <span>{{ offer.installment_period || 'N/A' }} months</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div v-else class="text-center py-8">
                <p class="text-gray-500">No offers available</p>
              </div>
            </div>
          </div>
        </div>

        <PriceDynamicsModal
          v-if="showPriceDynamics"
          :product-id="String(product.id || product.product_id)"
          :product-name="product.name"
          @close="showPriceDynamics = false"
        />
      </div>
    </div>
  </AppLayout>
</template>

<script>
import AppLayout from '@/components/AppLayout.vue'
import TechnicalCharacteristics from '@/components/TechnicalCharacteristics.vue'
import PriceDynamicsModal from '@/components/PriceDynamicsModal.vue'
import { productsApi, reviewsApi } from '@/api'
import productDefaultImage from '@/assets/product-default.png'

export default {
  name: 'ProductDetail',
  components: {
    AppLayout,
    TechnicalCharacteristics,
    PriceDynamicsModal,
  },
  data() {
    return {
      product: null,
      offers: [],
      reviewCount: 0,
      loading: false,
      offersLoading: false,
      error: null,
      placeholderImage: productDefaultImage,
      showPriceDynamics: false,
    }
  },
  mounted() {
    this.fetchProductData()
  },
  methods: {
    async fetchProductData() {
      const productId = this.$route.params.id
      if (!productId) {
        this.error = 'Product ID is required'
        return
      }

      this.loading = true
      this.error = null

      try {
        const [productResponse, reviewCountResponse] = await Promise.all([
          productsApi.getProductDetails(productId),
          reviewsApi.getReviewCount(productId).catch(() => ({ data: 0 })),
        ])

        this.product = productResponse.data
        this.reviewCount = reviewCountResponse.data

        this.fetchOffers(productId)
      } catch (err) {
        console.error('Failed to fetch product data:', err)
        this.error = err.response?.data?.message || 'Failed to load product information'
      } finally {
        this.loading = false
      }
    },

    async fetchOffers(productId) {
      this.offersLoading = true
      try {
        const response = await productsApi.getProductOffers(productId, 0, 6)
        this.offers = response.data.content || response.data || []
      } catch (err) {
        console.error('Failed to fetch offers:', err)
        this.offers = []
      } finally {
        this.offersLoading = false
      }
    },

    formatPrice(price) {
      return parseFloat(price).toFixed(2)
    },

    handleImageError(event) {
      event.target.src = this.placeholderImage
    },
    goToComments() {
      this.$router.push({ name: 'ProductComments', params: { id: this.product.id } })
    },
  },
}
</script>

<style scoped>
.prose {
  max-width: none;
}
</style>
