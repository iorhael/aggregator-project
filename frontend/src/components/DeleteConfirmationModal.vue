<template>
  <div
    class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
    @click.self="$emit('close')"
  >
    <div class="relative top-20 mx-auto p-5 border w-full max-w-md shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <!-- Header -->
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-medium text-gray-900">{{ title || 'Confirm Deletion' }}</h3>
          <button
            @click="$emit('close')"
            class="text-gray-400 hover:text-gray-600 focus:outline-none"
          >
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

        <!-- Content -->
        <div class="mb-6">
          <div class="flex items-center mb-4">
            <div class="flex-shrink-0">
              <svg
                class="h-10 w-10 text-red-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 16.5c-.77.833.192 2.5 1.732 2.5z"
                />
              </svg>
            </div>
            <div class="ml-4">
              <h4 class="text-lg font-medium text-gray-900">{{ title || `Delete ${itemType}` }}</h4>
              <p class="text-sm text-gray-600 mt-1">This action cannot be undone.</p>
            </div>
          </div>

          <!-- Item details if provided -->
          <div
            v-if="item && itemName"
            class="bg-gray-50 border border-gray-200 rounded-md p-4 mb-4"
          >
            <h5 class="font-medium text-gray-900 mb-2">{{ itemName }}</h5>
            <div v-if="itemType === 'product card'" class="text-sm text-gray-600 space-y-1">
              <div>Vendor: {{ item.vendor_name }}</div>
              <div>Price: ${{ formatPrice(item.price) }}</div>
              <div>Warranty: {{ item.warranty_period }} months</div>
            </div>
            <div v-else-if="itemType === 'store'" class="text-sm text-gray-600 space-y-1">
              <div>Address: {{ item.address }}</div>
              <div>Phone: {{ formatPhone(item.phone) }}</div>
            </div>
          </div>

          <!-- Message -->
          <p class="text-sm text-gray-600">
            {{
              message ||
              `Are you sure you want to delete this ${itemType}? This will permanently remove it from your account.`
            }}
          </p>
        </div>

        <!-- Actions -->
        <div class="flex justify-end space-x-3">
          <button
            @click="$emit('close')"
            type="button"
            class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Cancel
          </button>
          <button
            @click="$emit('confirm')"
            type="button"
            class="px-4 py-2 text-sm font-medium text-white bg-red-600 border border-transparent rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
          >
            {{ confirmText || `Delete ${itemType}` }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DeleteConfirmationModal',
  props: {
    item: {
      type: Object,
      default: null,
    },
    itemType: {
      type: String,
      default: '',
    },
    itemName: {
      type: String,
      default: '',
    },
    title: {
      type: String,
      default: '',
    },
    message: {
      type: String,
      default: '',
    },
    confirmText: {
      type: String,
      default: '',
    },
  },
  emits: ['close', 'confirm'],
  methods: {
    formatPrice(price) {
      if (!price) return '0.00'
      return parseFloat(price).toFixed(2)
    },
    formatPhone(phone) {
      if (!phone) return 'Not provided'

      // Format phone number for display
      if (phone.length === 11) {
        return `+${phone.slice(0, 1)} (${phone.slice(1, 4)}) ${phone.slice(4, 7)}-${phone.slice(7, 9)}-${phone.slice(9)}`
      } else if (phone.length >= 10) {
        return `+${phone.slice(0, -10)} (${phone.slice(-10, -7)}) ${phone.slice(-7, -4)}-${phone.slice(-4, -2)}-${phone.slice(-2)}`
      }

      return phone
    },
  },
}
</script>
