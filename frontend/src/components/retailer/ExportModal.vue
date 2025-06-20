<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-20 mx-auto p-5 border w-full max-w-md shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-medium text-gray-900">Export Product Cards</h3>
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

        <div class="space-y-4">
          <p class="text-sm text-gray-600">Choose the export format for your product cards:</p>

          <div class="space-y-3">
            <label class="flex items-center cursor-pointer">
              <input
                v-model="selectedFormat"
                type="radio"
                value="CSV"
                class="text-blue-600 focus:ring-blue-500"
              />
              <span class="ml-3 text-sm font-medium text-gray-700"
                >CSV (Comma Separated Values)</span
              >
            </label>

            <label class="flex items-center cursor-pointer">
              <input
                v-model="selectedFormat"
                type="radio"
                value="JSON"
                class="text-blue-600 focus:ring-blue-500"
              />
              <span class="ml-3 text-sm font-medium text-gray-700"
                >JSON (JavaScript Object Notation)</span
              >
            </label>

            <label class="flex items-center cursor-pointer">
              <input
                v-model="selectedFormat"
                type="radio"
                value="XML"
                class="text-blue-600 focus:ring-blue-500"
              />
              <span class="ml-3 text-sm font-medium text-gray-700"
                >XML (Extensible Markup Language)</span
              >
            </label>
          </div>
        </div>

        <div class="flex justify-end space-x-3 pt-6">
          <button
            @click="$emit('close')"
            class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
          >
            Cancel
          </button>
          <button
            @click="handleExport"
            :disabled="!selectedFormat"
            class="px-4 py-2 text-sm font-medium text-white bg-green-600 border border-transparent rounded-md hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Export {{ selectedFormat }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ExportModal',
  emits: ['close', 'export'],
  data() {
    return {
      selectedFormat: 'CSV',
    }
  },
  methods: {
    handleExport() {
      this.$emit('export', this.selectedFormat)
    },
  },
}
</script>
