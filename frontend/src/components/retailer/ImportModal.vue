<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-20 mx-auto p-5 border w-full max-w-md shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-medium text-gray-900">Import Product Cards</h3>
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

        <form @submit.prevent="handleImport" class="space-y-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2"> Select File * </label>
            <div
              class="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center hover:border-gray-400 transition-colors"
            >
              <input
                ref="fileInput"
                type="file"
                accept=".csv,.xml,.json"
                @change="handleFileChange"
                class="hidden"
                required
              />

              <div v-if="!selectedFile" @click="$refs.fileInput.click()" class="cursor-pointer">
                <svg
                  class="mx-auto h-12 w-12 text-gray-400"
                  stroke="currentColor"
                  fill="none"
                  viewBox="0 0 48 48"
                >
                  <path
                    d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
                <p class="mt-2 text-sm text-gray-600">
                  <span class="font-medium text-blue-600 hover:text-blue-500">Click to upload</span>
                  or drag and drop
                </p>
                <p class="text-xs text-gray-500">CSV, XML, or JSON files only</p>
              </div>

              <div v-else class="space-y-2">
                <div class="flex items-center justify-center space-x-2">
                  <svg
                    class="h-8 w-8 text-green-500"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                    />
                  </svg>
                  <span class="text-sm font-medium text-gray-900">{{ selectedFile.name }}</span>
                </div>
                <p class="text-xs text-gray-500">{{ formatFileSize(selectedFile.size) }}</p>
                <button
                  type="button"
                  @click="clearFile"
                  class="text-sm text-red-600 hover:text-red-500"
                >
                  Remove file
                </button>
              </div>
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2"> Product Filter </label>
            <div class="space-y-2">
              <label class="flex items-center cursor-pointer">
                <input
                  v-model="verifiedProductsOnly"
                  type="radio"
                  :value="true"
                  class="text-blue-600 focus:ring-blue-500"
                />
                <span class="ml-2 text-sm text-gray-700">Verified Products Only</span>
              </label>
              <label class="flex items-center cursor-pointer">
                <input
                  v-model="verifiedProductsOnly"
                  type="radio"
                  :value="false"
                  class="text-blue-600 focus:ring-blue-500"
                />
                <span class="ml-2 text-sm text-gray-700">All Products</span>
              </label>
            </div>
          </div>

          <div class="bg-blue-50 border border-blue-200 rounded-md p-3">
            <p class="text-blue-800 text-sm">
              <strong>Note:</strong> The import process will run in the background. You'll be
              notified once the import is complete.
            </p>
          </div>

          <div v-if="error" class="bg-red-50 border border-red-200 rounded-md p-3">
            <p class="text-red-600 text-sm">{{ error }}</p>
          </div>

          <div class="flex justify-end space-x-3">
            <button
              type="button"
              @click="$emit('close')"
              class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="!selectedFile || importing"
              class="px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <span v-if="importing" class="flex items-center">
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
                Importing...
              </span>
              <span v-else>Import Products</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { batchApi } from '@/api'

export default {
  name: 'ImportModal',
  emits: ['close', 'success'],
  data() {
    return {
      selectedFile: null,
      verifiedProductsOnly: true,
      importing: false,
      error: null,
    }
  },
  methods: {
    handleFileChange(event) {
      const file = event.target.files[0]
      if (file) {
        const allowedTypes = ['text/csv', 'application/xml', 'text/xml', 'application/json']
        const fileExtension = file.name.split('.').pop().toLowerCase()
        const allowedExtensions = ['csv', 'xml', 'json']

        if (!allowedTypes.includes(file.type) && !allowedExtensions.includes(fileExtension)) {
          this.error = 'Only CSV, XML, and JSON files are allowed'
          return
        }

        this.selectedFile = file
        this.error = null
      }
    },

    clearFile() {
      this.selectedFile = null
      this.$refs.fileInput.value = ''
    },

    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    },

    async handleImport() {
      if (!this.selectedFile) {
        this.error = 'Please select a file to import'
        return
      }

      this.importing = true
      this.error = null

      try {
        await batchApi.importProductCards(this.selectedFile, this.verifiedProductsOnly)
        this.$emit('success', {
          file: this.selectedFile,
          verifiedProductsOnly: this.verifiedProductsOnly,
        })
      } catch (error) {
        console.error('Import failed:', error)
        this.error = error.response?.data?.message || 'Import failed. Please try again.'
      } finally {
        this.importing = false
      }
    },
  },
}
</script>
