<template>
  <div class="fixed top-4 right-4 z-50 max-w-sm w-full">
    <div class="bg-green-50 border border-green-200 rounded-md p-4 shadow-lg">
      <div class="flex">
        <div class="flex-shrink-0">
          <svg class="h-5 w-5 text-green-400" viewBox="0 0 20 20" fill="currentColor">
            <path
              fill-rule="evenodd"
              d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
              clip-rule="evenodd"
            />
          </svg>
        </div>
        <div class="ml-3 flex-1">
          <p class="text-sm font-medium text-green-800">Success!</p>
          <p class="text-sm text-green-700 mt-1">{{ message }}</p>
        </div>
        <div class="ml-4 flex-shrink-0">
          <button
            @click="$emit('close')"
            class="bg-green-50 rounded-md inline-flex text-green-400 hover:text-green-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-green-50 focus:ring-green-600"
          >
            <span class="sr-only">Close</span>
            <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path
                fill-rule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clip-rule="evenodd"
              />
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SuccessNotification',
  props: {
    message: {
      type: String,
      required: true,
    },
  },
  emits: ['close'],
}
</script>
