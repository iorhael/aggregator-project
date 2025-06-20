<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-10 mx-auto p-5 border w-full max-w-2xl shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <div class="flex items-center justify-between mb-6">
          <div>
            <h3 class="text-lg font-medium text-gray-900">Edit Product Card</h3>
            <p class="text-sm text-gray-600 mt-1">{{ card.name }}</p>
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

        <form @submit.prevent="handleSubmit" class="space-y-6">
          <div>
            <label for="description" class="block text-sm font-medium text-gray-700 mb-1">
              Description
            </label>
            <textarea
              id="description"
              v-model="form.description"
              rows="3"
              placeholder="Add a description for this product card..."
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              :class="{ 'border-red-300': errors.description }"
            ></textarea>
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
                min="0"
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
                  required
                  placeholder="12"
                  class="w-full pr-16 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  :class="{ 'border-red-300': errors.warranty }"
                />
                <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                  <span class="text-gray-500 sm:text-sm">months</span>
                </div>
              </div>
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
                  required
                  placeholder="6"
                  class="w-full pr-16 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  :class="{ 'border-red-300': errors.installment_period }"
                />
                <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                  <span class="text-gray-500 sm:text-sm">months</span>
                </div>
              </div>
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
                required
                placeholder="3"
                class="w-full pr-12 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                :class="{ 'border-red-300': errors.max_delivery_time }"
              />
              <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                <span class="text-gray-500 sm:text-sm">days</span>
              </div>
            </div>
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
                Updating...
              </span>
              <span v-else>Update Product Card</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { productCardsApi } from '@/api'

export default {
  name: 'ProductCardEditModal',
  props: {
    card: {
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
    }
  },
  mounted() {
    this.form = {
      description: this.card.description || '',
      price: this.card.price || '',
      warranty: this.card.warranty_period || '',
      installment_period: this.card.installment_period || '',
      max_delivery_time: this.card.max_delivery_time || '',
    }
  },
  methods: {
    validateForm() {
      this.errors = {}

      if (this.form.price && (isNaN(this.form.price) || parseFloat(this.form.price) <= 0)) {
        this.errors.price = 'Price must be a positive number'
      }

      if (this.form.warranty && (isNaN(this.form.warranty) || parseInt(this.form.warranty) <= 0)) {
        this.errors.warranty = 'Warranty period must be a positive number'
      }

      if (
        this.form.installment_period &&
        (isNaN(this.form.installment_period) || parseInt(this.form.installment_period) <= 0)
      ) {
        this.errors.installment_period = 'Installment period must be a positive number'
      }

      if (
        this.form.max_delivery_time &&
        (isNaN(this.form.max_delivery_time) || parseInt(this.form.max_delivery_time) <= 0)
      ) {
        this.errors.max_delivery_time = 'Delivery time must be a positive number'
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
        const updateData = {}

        if (this.form.description !== undefined && this.form.description !== null) {
          updateData.description = this.form.description
        }

        if (this.form.price) {
          updateData.new_price = parseFloat(this.form.price)
        }

        if (this.form.warranty) {
          updateData.warranty = parseInt(this.form.warranty)
        }

        if (this.form.installment_period) {
          updateData.installment_period = parseInt(this.form.installment_period)
        }

        if (this.form.max_delivery_time) {
          updateData.max_delivery_time = parseInt(this.form.max_delivery_time)
        }

        const response = await productCardsApi.updateProductCard(
          this.card.product_card_id,
          updateData,
        )

        const updatedCard = {
          ...this.card,
          description:
            updateData.description !== undefined ? updateData.description : this.card.description,
          price: updateData.new_price || this.card.price,
          warranty_period: updateData.warranty || this.card.warranty_period,
          installment_period: updateData.installment_period || this.card.installment_period,
          max_delivery_time: updateData.max_delivery_time || this.card.max_delivery_time,
        }

        this.$emit('success', updatedCard)
      } catch (error) {
        console.error('Update error:', error)
        this.submitError =
          error.response?.data?.message || 'An error occurred while updating the product card'
      } finally {
        this.submitting = false
      }
    },
  },
}
</script>
