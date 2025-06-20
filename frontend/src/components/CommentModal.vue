<template>
  <div
    class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
    @click="closeModal"
  >
    <div
      class="relative top-20 mx-auto p-5 border w-full max-w-md shadow-lg rounded-md bg-white"
      @click.stop
    >
      <div class="mt-3">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-medium text-gray-900">
            {{ isEditing ? 'Edit Comment' : 'Add Comment' }}
          </h3>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
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

        <form @submit.prevent="submitComment" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Rating</label>
            <div class="flex items-center space-x-1">
              <button
                v-for="star in 5"
                :key="star"
                type="button"
                @click="setRating(star)"
                class="focus:outline-none"
              >
                <svg
                  class="w-8 h-8 transition-colors"
                  :class="star <= rating ? 'text-yellow-400' : 'text-gray-300'"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                >
                  <path
                    d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                  />
                </svg>
              </button>
            </div>
            <p v-if="errors.rating" class="mt-1 text-sm text-red-600">{{ errors.rating }}</p>
          </div>

          <div>
            <label for="content" class="block text-sm font-medium text-gray-700 mb-2"
              >Comment</label
            >
            <textarea
              id="content"
              v-model="content"
              rows="4"
              class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              placeholder="Share your thoughts about this product..."
              :class="{ 'border-red-500': errors.content }"
            ></textarea>
            <p v-if="errors.content" class="mt-1 text-sm text-red-600">{{ errors.content }}</p>
          </div>

          <div class="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              @click="closeModal"
              class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-md transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="loading"
              class="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-md transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <span v-if="loading" class="flex items-center">
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
                {{ isEditing ? 'Updating...' : 'Adding...' }}
              </span>
              <span v-else>
                {{ isEditing ? 'Update Comment' : 'Add Comment' }}
              </span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CommentModal',
  props: {
    comment: {
      type: Object,
      default: null,
    },
    productId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      rating: 5,
      content: '',
      loading: false,
      errors: {},
    }
  },
  computed: {
    isEditing() {
      return !!this.comment
    },
  },
  mounted() {
    if (this.comment) {
      this.rating = this.comment.product_rating
      this.content = this.comment.content
    }
  },
  methods: {
    setRating(star) {
      this.rating = star
      if (this.errors.rating) {
        delete this.errors.rating
      }
    },

    validateForm() {
      this.errors = {}

      if (!this.rating || this.rating < 1 || this.rating > 5) {
        this.errors.rating = 'Please select a rating'
      }

      if (!this.content.trim()) {
        this.errors.content = 'Please enter a comment'
      }

      return Object.keys(this.errors).length === 0
    },

    async submitComment() {
      if (!this.validateForm()) {
        return
      }

      this.loading = true

      try {
        const commentData = {
          content: this.content.trim(),
          product_rating: this.rating,
        }

        if (this.isEditing) {
          await this.$emit('update', this.comment.id, commentData)
        } else {
          commentData.product_id = this.productId
          await this.$emit('create', commentData)
        }

        this.closeModal()
      } catch (error) {
        console.error('Error submitting comment:', error)
      } finally {
        this.loading = false
      }
    },

    closeModal() {
      this.$emit('close')
    },
  },
}
</script>
