<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-10 mx-auto p-5 border w-full max-w-2xl shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <!-- Header -->
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-medium text-gray-900">
            {{ isEdit ? 'Edit Organization' : 'Create Organization' }}
          </h3>
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

        <!-- Form -->
        <form @submit.prevent="handleSubmit" class="space-y-6">
          <!-- Logo Upload -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Organization Logo</label>
            <div class="flex items-center space-x-4">
              <!-- Current/Preview Logo -->
              <div class="w-20 h-20 bg-gray-100 rounded-lg overflow-hidden flex-shrink-0">
                <img
                  v-if="logoPreview || (isEdit && retailer?.logo_link)"
                  :src="logoPreview || retailer?.logo_link"
                  alt="Logo preview"
                  class="w-full h-full object-cover"
                />
                <div v-else class="w-full h-full flex items-center justify-center">
                  <svg
                    class="w-8 h-8 text-gray-400"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"
                    />
                  </svg>
                </div>
              </div>

              <!-- Upload Button -->
              <div>
                <input
                  ref="logoInput"
                  type="file"
                  accept="image/png,image/jpeg,image/jpg"
                  @change="handleLogoChange"
                  class="hidden"
                />
                <button
                  type="button"
                  @click="$refs.logoInput.click()"
                  class="bg-white border border-gray-300 rounded-md px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
                >
                  {{ logoFile ? 'Change Logo' : 'Upload Logo' }}
                </button>
                <p class="text-xs text-gray-500 mt-1">PNG or JPEG, max 4MB</p>
              </div>
            </div>
          </div>

          <!-- Organization Name -->
          <div>
            <label for="name" class="block text-sm font-medium text-gray-700 mb-1">
              Organization Name *
            </label>
            <input
              id="name"
              v-model="form.name"
              type="text"
              required
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              :class="{ 'border-red-300': errors.name }"
            />
            <p v-if="errors.name" class="text-red-600 text-xs mt-1">{{ errors.name }}</p>
          </div>

          <!-- Description -->
          <div>
            <label for="description" class="block text-sm font-medium text-gray-700 mb-1">
              Description *
            </label>
            <textarea
              id="description"
              v-model="form.description"
              rows="3"
              required
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              :class="{ 'border-red-300': errors.description }"
            ></textarea>
            <p v-if="errors.description" class="text-red-600 text-xs mt-1">
              {{ errors.description }}
            </p>
          </div>

          <!-- Email -->
          <div>
            <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
              Email *
            </label>
            <input
              id="email"
              v-model="form.email"
              type="email"
              required
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              :class="{ 'border-red-300': errors.email }"
            />
            <p v-if="errors.email" class="text-red-600 text-xs mt-1">{{ errors.email }}</p>
          </div>

          <!-- Website -->
          <div>
            <label for="website" class="block text-sm font-medium text-gray-700 mb-1">
              Website *
            </label>
            <input
              id="website"
              v-model="form.website"
              type="url"
              required
              placeholder="https://example.com"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              :class="{ 'border-red-300': errors.website }"
            />
            <p v-if="errors.website" class="text-red-600 text-xs mt-1">{{ errors.website }}</p>
          </div>

          <!-- Auto Update Toggle -->
          <div class="border-t pt-6">
            <div class="flex items-center justify-between mb-4">
              <label class="flex items-center cursor-pointer">
                <input
                  v-model="enableAutoUpdate"
                  type="checkbox"
                  class="rounded border-gray-300 text-blue-600 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50"
                />
                <span class="ml-2 text-sm font-medium text-gray-700">Enable Auto Update</span>
              </label>
            </div>

            <!-- Auto Update Fields -->
            <div v-if="enableAutoUpdate" class="space-y-4 pl-6 border-l-2 border-blue-200">
              <div>
                <label for="downloadLink" class="block text-sm font-medium text-gray-700 mb-1">
                  Product Feed URL *
                </label>
                <input
                  id="downloadLink"
                  v-model="form.download_link"
                  type="url"
                  required
                  placeholder="https://example.com/feed.xml"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  :class="{ 'border-red-300': errors.download_link }"
                />
                <p v-if="errors.download_link" class="text-red-600 text-xs mt-1">
                  {{ errors.download_link }}
                </p>
              </div>

              <div>
                <label for="verifiedOnly" class="block text-sm font-medium text-gray-700 mb-1">
                  Product Filter
                </label>
                <select
                  id="verifiedOnly"
                  v-model="form.verified_products_only"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                  <option :value="false">All Products</option>
                  <option :value="true">Verified Products Only</option>
                </select>
              </div>

              <div class="bg-blue-50 border border-blue-200 rounded-md p-3">
                <p class="text-blue-800 text-sm">
                  <strong>Note:</strong> This will configure automatic product updates from your
                  feed. The system will periodically fetch and update your product catalog.
                </p>
              </div>
            </div>
          </div>

          <!-- Error Message -->
          <div v-if="submitError" class="bg-red-50 border border-red-200 rounded-md p-3">
            <p class="text-red-600 text-sm">{{ submitError }}</p>
          </div>

          <!-- Form Actions -->
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
                {{ isEdit ? 'Updating...' : 'Creating...' }}
              </span>
              <span v-else>
                {{ isEdit ? 'Update Organization' : 'Create Organization' }}
              </span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { retailersApi, batchApi } from '@/api'

export default {
  name: 'RetailerFormModal',
  props: {
    isEdit: {
      type: Boolean,
      default: false,
    },
    retailer: {
      type: Object,
      default: null,
    },
  },
  emits: ['close', 'success'],
  data() {
    return {
      form: {
        name: '',
        description: '',
        email: '',
        website: '',
        download_link: '',
        verified_products_only: false,
      },
      enableAutoUpdate: false,
      originalAutoUpdateState: false, // Track original state
      logoFile: null,
      logoPreview: null,
      errors: {},
      submitError: null,
      submitting: false,
    }
  },
  mounted() {
    if (this.isEdit && this.retailer) {
      this.form = {
        name: this.retailer.name || '',
        description: this.retailer.description || '',
        email: this.retailer.email || '',
        website: this.retailer.website || '',
        download_link: this.retailer.download_link || '',
        verified_products_only: this.retailer.verified_products_only || false,
      }

      // Enable auto update if there's already a download link
      this.enableAutoUpdate = !!this.retailer.download_link
      this.originalAutoUpdateState = this.enableAutoUpdate
    }
  },
  methods: {
    handleLogoChange(event) {
      const file = event.target.files[0]
      if (file) {
        // Validate file size (4MB max to match backend)
        if (file.size > 4 * 1024 * 1024) {
          alert('File size must be less than 4MB')
          return
        }

        // Validate file type
        if (!['image/png', 'image/jpeg', 'image/jpg'].includes(file.type)) {
          alert('Only PNG and JPEG files are allowed')
          return
        }

        this.logoFile = file

        // Create preview
        const reader = new FileReader()
        reader.onload = (e) => {
          this.logoPreview = e.target.result
        }
        reader.readAsDataURL(file)
      }
    },

    validateForm() {
      this.errors = {}

      if (!this.form.name.trim()) {
        this.errors.name = 'Organization name is required'
      }

      if (!this.form.description.trim()) {
        this.errors.description = 'Description is required'
      }

      if (!this.form.email.trim()) {
        this.errors.email = 'Email is required'
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)) {
        this.errors.email = 'Please enter a valid email address'
      }

      if (!this.form.website.trim()) {
        this.errors.website = 'Website is required'
      } else if (!/^https?:\/\/.+/.test(this.form.website)) {
        this.errors.website = 'Please enter a valid website URL (starting with http:// or https://)'
      }

      // Validate auto update fields if enabled
      if (this.enableAutoUpdate) {
        if (!this.form.download_link.trim()) {
          this.errors.download_link = 'Product feed URL is required when auto update is enabled'
        } else if (!/^https?:\/\/.+/.test(this.form.download_link)) {
          this.errors.download_link = 'Please enter a valid feed URL'
        }
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
        // Prepare retailer data
        const retailerData = {
          name: this.form.name,
          description: this.form.description,
          email: this.form.email,
          website: this.form.website,
        }

        let response

        if (this.isEdit) {
          // Update retailer info
          response = await retailersApi.updateRetailer(retailerData, this.logoFile)
        } else {
          // Create new retailer
          response = await retailersApi.createRetailer(retailerData, this.logoFile)
        }

        // Handle auto update configuration
        if (this.isEdit) {
          // Check if auto update state changed
          if (this.originalAutoUpdateState && !this.enableAutoUpdate) {
            // Auto update was enabled, now disabled - delete configuration
            try {
              await batchApi.deleteAutoUpdate()
              // Clear auto update fields from response
              response.data = {
                ...response.data,
                download_link: null,
                verified_products_only: false,
              }
            } catch (deleteError) {
              console.error('Failed to delete auto update configuration:', deleteError)
            }
          } else if (this.enableAutoUpdate && this.form.download_link) {
            // Auto update is enabled
            try {
              if (this.originalAutoUpdateState) {
                // Update existing configuration
                await batchApi.updateAutoUpdate(
                  this.form.download_link,
                  this.form.verified_products_only,
                )
              } else {
                // Create new configuration
                await batchApi.configureAutoUpdate(
                  this.form.download_link,
                  this.form.verified_products_only,
                )
              }

              // Update response data with auto update info
              response.data = {
                ...response.data,
                download_link: this.form.download_link,
                verified_products_only: this.form.verified_products_only,
              }
            } catch (autoUpdateError) {
              console.error('Auto update configuration failed:', autoUpdateError)
              // Don't fail the whole operation, just log the error
            }
          }
        } else {
          // For new retailers, only configure auto update if enabled
          if (this.enableAutoUpdate && this.form.download_link) {
            try {
              await batchApi.configureAutoUpdate(
                this.form.download_link,
                this.form.verified_products_only,
              )

              // Update response data with auto update info
              response.data = {
                ...response.data,
                download_link: this.form.download_link,
                verified_products_only: this.form.verified_products_only,
              }
            } catch (autoUpdateError) {
              console.error('Auto update configuration failed:', autoUpdateError)
            }
          }
        }

        this.$emit('success', response.data)
      } catch (error) {
        console.error('Submit error:', error)
        this.submitError = error.response?.data?.message || 'An error occurred while saving'
      } finally {
        this.submitting = false
      }
    },
  },
}
</script>
