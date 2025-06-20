<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-10 mx-auto p-5 border w-full max-w-2xl shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <div class="flex items-center justify-between mb-6">
          <div>
            <h3 class="text-lg font-medium text-gray-900">Create Product Card</h3>
            <p class="text-sm text-gray-600 mt-1">Create a card for the selected product</p>
          </div>
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

        <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
          <div class="flex items-start space-x-4">
            <div class="w-16 h-16 bg-gray-100 rounded-lg overflow-hidden flex-shrink-0">
              <img
                :src="product.image_link || placeholderImage"
                :alt="product.name"
                @error="handleImageError"
                class="w-full h-full object-cover"
              />
            </div>
            <div class="flex-1">
              <h4 class="text-sm font-medium text-gray-900 mb-1">{{ product.name }}</h4>
              <p class="text-sm text-gray-600">{{ product.vendor_name }}</p>
              <div class="flex items-center mt-2 text-xs text-gray-500">
                <svg class="w-4 h-4 mr-1 text-yellow-400" fill="currentColor" viewBox="0 0 20 20">
                  <path
                    d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                  />
                </svg>
                {{ product.average_rating || 'N/A' }} • {{ product.offers_count || 0 }} offers
              </div>
            </div>
          </div>
        </div>

        <form @submit.prevent="handleSubmit" class="space-y-6">
          <div>
            <label for="description" class="block text-sm font-medium text-gray-700 mb-1">
              Description *
            </label>
            <textarea
              id="description"
              v-model="form.description"
              rows="4"
              required
              placeholder="Describe your offer, warranty terms, or any special conditions..."
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              :class="{ 'border-red-300': errors.description }"
            ></textarea>
            <p class="text-xs text-gray-500 mt-1">Minimum 10 characters</p>
            <p v-if="errors.description" class="text-red-600 text-xs mt-1">
              {{ errors.description }}
            </p>
          </div>

          <div>
            <label for="price" class="block text-sm font-medium text-gray-700 mb-1">
              Price *
            </label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <span class="text-gray-500 sm:text-sm">$</span>
              </div>
              <input
                id="price"
                v-model="form.price"
                type="number"
                step="0.01"
                min="0.01"
                required
                placeholder="0.00"
                class="w-full pl-7 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                :class="{ 'border-red-300': errors.price }"
              />
            </div>
            <p v-if="errors.price" class="text-red-600 text-xs mt-1">{{ errors.price }}</p>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label for="warranty" class="block text-sm font-medium text-gray-700 mb-1">
                Warranty Period *
              </label>
              <div class="relative">
                <input
                  id="warranty"
                  v-model="form.warranty"
                  type="number"
                  min="1"
                  max="120"
                  required
                  placeholder="12"
                  class="w-full pr-16 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  :class="{ 'border-red-300': errors.warranty }"
                />
                <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                  <span class="text-gray-500 sm:text-sm">months</span>
                </div>
              </div>
              <p class="text-xs text-gray-500 mt-1">1-120 months</p>
              <p v-if="errors.warranty" class="text-red-600 text-xs mt-1">{{ errors.warranty }}</p>
            </div>

            <div>
              <label for="installment" class="block text-sm font-medium text-gray-700 mb-1">
                Installment Period *
              </label>
              <div class="relative">
                <input
                  id="installment"
                  v-model="form.installment_period"
                  type="number"
                  min="1"
                  max="120"
                  required
                  placeholder="6"
                  class="w-full pr-16 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  :class="{ 'border-red-300': errors.installment_period }"
                />
                <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                  <span class="text-gray-500 sm:text-sm">months</span>
                </div>
              </div>
              <p class="text-xs text-gray-500 mt-1">1-120 months</p>
              <p v-if="errors.installment_period" class="text-red-600 text-xs mt-1">
                {{ errors.installment_period }}
              </p>
            </div>
          </div>

          <div>
            <label for="delivery" class="block text-sm font-medium text-gray-700 mb-1">
              Maximum Delivery Time *
            </label>
            <div class="relative max-w-xs">
              <input
                id="delivery"
                v-model="form.max_delivery_time"
                type="number"
                min="1"
                max="30"
                required
                placeholder="3"
                class="w-full pr-12 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                :class="{ 'border-red-300': errors.max_delivery_time }"
              />
              <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                <span class="text-gray-500 sm:text-sm">days</span>
              </div>
            </div>
            <p class="text-xs text-gray-500 mt-1">1-30 days</p>
            <p v-if="errors.max_delivery_time" class="text-red-600 text-xs mt-1">
              {{ errors.max_delivery_time }}
            </p>
          </div>

          <div v-if="submitError" class="bg-red-50 border border-red-200 rounded-md p-3">
            <p class="text-red-600 text-sm">{{ submitError }}</p>
          </div>

          <div class="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              @click="$emit('close')"
              class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="submitting"
              class="px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <span v-if="submitting" class="flex items-center">
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
                Creating...
              </span>
              <span v-else>Create Product Card</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { productCardsApi } from '@/api'
import defaultProductImage from '@/assets/product-default.png'

export default {
  name: 'ProductCardCreateModal',
  props: {
    product: {
      type: Object,
      required: true,
    },
  },
  emits: ['close', 'success'],
  data() {
    return {
      form: {
        description: '',
        price: '',
        warranty: '',
        installment_period: '',
        max_delivery_time: '',
      },
      errors: {},
      submitError: null,
      submitting: false,
      placeholderImage: defaultProductImage,
    }
  },
  methods: {
    validateForm() {
      this.errors = {}

      if (!this.form.description.trim()) {
        this.errors.description = 'Description is required'
      } else if (this.form.description.trim().length < 10) {
        this.errors.description = 'Description must be at least 10 characters'
      }

      if (!this.form.price) {
        this.errors.price = 'Price is required'
      } else if (isNaN(this.form.price) || parseFloat(this.form.price) <= 0) {
        this.errors.price = 'Price must be a positive number'
      }

      if (!this.form.warranty) {
        this.errors.warranty = 'Warranty period is required'
      } else if (
        isNaN(this.form.warranty) ||
        parseInt(this.form.warranty) < 1 ||
        parseInt(this.form.warranty) > 120
      ) {
        this.errors.warranty = 'Warranty period must be between 1 and 120 months'
      }

      if (!this.form.installment_period) {
        this.errors.installment_period = 'Installment period is required'
      } else if (
        isNaN(this.form.installment_period) ||
        parseInt(this.form.installment_period) < 1 ||
        parseInt(this.form.installment_period) > 120
      ) {
        this.errors.installment_period = 'Installment period must be between 1 and 120 months'
      }

      if (!this.form.max_delivery_time) {
        this.errors.max_delivery_time = 'Delivery time is required'
      } else if (
        isNaN(this.form.max_delivery_time) ||
        parseInt(this.form.max_delivery_time) < 1 ||
        parseInt(this.form.max_delivery_time) > 30
      ) {
        this.errors.max_delivery_time = 'Delivery time must be between 1 and 30 days'
      }

      return Object.keys(this.errors).length === 0
    },

    async handleSubmit() {
      if (!this.validateForm()) {
        return
      }

      this.submitting = true
      this.submitError = null

      try {
        const cardData = {
          product_id: this.product.id,
          description: this.form.description.trim(),
          price: parseFloat(this.form.price),
          warranty: parseInt(this.form.warranty),
          installment_period: parseInt(this.form.installment_period),
          max_delivery_time: parseInt(this.form.max_delivery_time),
        }

        const response = await productCardsApi.createProductCard(cardData)
        this.$emit('success', response.data)
      } catch (error) {
        console.error('Create error:', error)
        this.submitError =
          error.response?.data?.message || 'An error occurred while creating the product card'
      } finally {
        this.submitting = false
      }
    },

    handleImageError(event) {
      event.target.src = this.placeholderImage
    },
  },
}
</script>
