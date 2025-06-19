<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gray-50 min-h-screen">
      <!-- Navigation Subheader -->
      <div v-if="product" class="bg-white border-b border-gray-200">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div
            class="flex flex-col sm:flex-row justify-center items-center py-4 space-y-2 sm:space-y-0 sm:space-x-8"
          >
            <router-link
              :to="{ name: 'ProductDetail', params: { id: product.id } }"
              class="text-gray-500 hover:text-gray-700 pb-2 transition-colors"
            >
              About Product
            </router-link>
            <router-link
              :to="{ name: 'ProductOffers', params: { id: product.id } }"
              class="text-gray-500 hover:text-gray-700 pb-2 transition-colors"
            >
              Offers ({{ product.offers_count || 0 }})
            </router-link>
            <button class="text-blue-600 border-b-2 border-blue-600 pb-2 font-medium">
              Comments ({{ totalComments || 0 }})
            </button>
            <button class="text-gray-500 hover:text-gray-700 pb-2" disabled>
              Reviews ({{ reviewCount || 0 }})
            </button>
          </div>
        </div>
      </div>

      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- Loading State -->
        <div v-if="loading" class="flex justify-center items-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Error State -->
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
              <h3 class="text-sm font-medium text-red-800">Error loading comments</h3>
              <p class="text-sm text-red-700 mt-1">{{ error }}</p>
            </div>
          </div>
        </div>

        <!-- Content -->
        <div v-else class="space-y-6">
          <!-- Product Header -->
          <div v-if="product" class="bg-white rounded-lg shadow-sm p-6">
            <div class="flex items-center space-x-4">
              <img
                :src="product.image_link || placeholderImage"
                :alt="product.name"
                @error="handleImageError"
                class="w-16 h-16 object-cover rounded-lg"
              />
              <div>
                <h1 class="text-xl font-semibold text-gray-900">{{ product.name }}</h1>
                <p class="text-gray-600">{{ product.vendor_name }}</p>
              </div>
            </div>
          </div>

          <!-- User's Comment Section -->
          <div class="bg-white rounded-lg shadow-sm p-6">
            <h2 class="text-lg font-semibold text-gray-900 mb-4">Your Comment</h2>

            <!-- User has comment -->
            <div v-if="userComment" class="border border-gray-200 rounded-lg p-4">
              <div class="flex justify-between items-start mb-3">
                <div class="flex items-center space-x-3">
                  <!-- User Avatar -->
                  <div
                    class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center text-white font-medium"
                  >
                    {{ getInitials(userComment.author_name) }}
                  </div>
                  <div>
                    <span class="font-medium text-gray-900">{{ userComment.author_name }}</span>
                    <div class="flex items-center mt-1">
                      <svg
                        v-for="star in 5"
                        :key="star"
                        class="w-4 h-4"
                        :class="
                          star <= userComment.product_rating ? 'text-yellow-400' : 'text-gray-300'
                        "
                        fill="currentColor"
                        viewBox="0 0 20 20"
                      >
                        <path
                          d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                        />
                      </svg>
                    </div>
                  </div>
                </div>
                <div class="flex space-x-2">
                  <button
                    @click="editComment"
                    class="text-blue-600 hover:text-blue-500 text-sm font-medium"
                  >
                    Edit
                  </button>
                  <button
                    @click="confirmDeleteComment"
                    class="text-red-600 hover:text-red-500 text-sm font-medium"
                  >
                    Delete
                  </button>
                </div>
              </div>
              <p class="text-gray-700 whitespace-pre-wrap">{{ userComment.content }}</p>
              <p class="text-xs text-gray-500 mt-2">
                {{ formatDate(userComment.created_at) }}
                <span v-if="userComment.updated_at !== userComment.created_at">
                  (edited {{ formatDate(userComment.updated_at) }})
                </span>
              </p>
            </div>

            <!-- No user comment -->
            <div v-else class="text-center py-8">
              <p class="text-gray-500 mb-4">You haven't commented on this product yet.</p>
              <button
                @click="addComment"
                class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-md font-medium transition-colors"
              >
                Add Comment
              </button>
            </div>
          </div>

          <!-- All Comments Section -->
          <div class="bg-white rounded-lg shadow-sm p-6">
            <h2 class="text-lg font-semibold text-gray-900 mb-6">All Comments</h2>

            <!-- Comments Loading -->
            <div v-if="commentsLoading" class="flex justify-center py-8">
              <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>

            <!-- Comments List -->
            <div v-else-if="comments.length > 0" class="space-y-6">
              <div
                v-for="comment in comments"
                :key="comment.id"
                class="border border-gray-200 rounded-lg p-4"
              >
                <div class="flex justify-between items-start mb-3">
                  <div class="flex items-center space-x-3">
                    <!-- User Avatar -->
                    <div
                      class="w-10 h-10 bg-gray-600 rounded-full flex items-center justify-center text-white font-medium"
                    >
                      {{ getInitials(comment.author_name) }}
                    </div>
                    <div>
                      <span class="font-medium text-gray-900">{{ comment.author_name }}</span>
                      <div class="flex items-center mt-1">
                        <svg
                          v-for="star in 5"
                          :key="star"
                          class="w-4 h-4"
                          :class="
                            star <= comment.product_rating ? 'text-yellow-400' : 'text-gray-300'
                          "
                          fill="currentColor"
                          viewBox="0 0 20 20"
                        >
                          <path
                            d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                          />
                        </svg>
                      </div>
                    </div>
                  </div>
                  <p class="text-xs text-gray-500">
                    {{ formatDate(comment.created_at) }}
                  </p>
                </div>
                <p class="text-gray-700 whitespace-pre-wrap">{{ comment.content }}</p>
              </div>

              <!-- Pagination -->
              <div v-if="totalPages > 1" class="flex justify-center items-center space-x-2 pt-6">
                <button
                  @click="changePage(currentPage - 1)"
                  :disabled="currentPage === 0"
                  class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Previous
                </button>

                <div class="flex space-x-1">
                  <button
                    v-for="page in visiblePages"
                    :key="page"
                    @click="changePage(page)"
                    :class="[
                      'px-3 py-2 text-sm font-medium rounded-md',
                      page === currentPage
                        ? 'text-blue-600 bg-blue-50 border border-blue-300'
                        : 'text-gray-500 bg-white border border-gray-300 hover:bg-gray-50',
                    ]"
                  >
                    {{ page + 1 }}
                  </button>
                </div>

                <button
                  @click="changePage(currentPage + 1)"
                  :disabled="currentPage >= totalPages - 1"
                  class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Next
                </button>
              </div>
            </div>

            <!-- No Comments -->
            <div v-else class="text-center py-8">
              <p class="text-gray-500">No comments yet. Be the first to comment!</p>
            </div>
          </div>
        </div>

        <!-- Comment Modal -->
        <CommentModal
          v-if="showCommentModal"
          :comment="editingComment"
          :product-id="String(productId)"
          @create="handleCreateComment"
          @update="handleUpdateComment"
          @close="closeCommentModal"
        />

        <!-- Delete Confirmation Modal -->
        <DeleteConfirmationModal
          v-if="showDeleteModal"
          title="Delete Comment"
          message="Are you sure you want to delete your comment? This action cannot be undone."
          @confirm="handleDeleteComment"
          @cancel="showDeleteModal = false"
        />
      </div>
    </div>
  </AppLayout>
</template>

<script>
import AppLayout from '@/components/AppLayout.vue'
import CommentModal from '@/components/CommentModal.vue'
import DeleteConfirmationModal from '@/components/DeleteConfirmationModal.vue'
import { productsApi, commentsApi, reviewsApi } from '@/api'
import productDefaultImage from '@/assets/product-default.png'
import keycloak from '@/services/keycloak'

export default {
  name: 'ProductComments',
  components: {
    AppLayout,
    CommentModal,
    DeleteConfirmationModal,
  },
  data() {
    return {
      product: null,
      userComment: null,
      comments: [],
      reviewCount: 0,
      loading: false,
      commentsLoading: false,
      error: null,
      placeholderImage: productDefaultImage,
      showCommentModal: false,
      showDeleteModal: false,
      editingComment: null,
      currentPage: 0,
      totalPages: 0,
      totalComments: 0,
      pageSize: 10,
    }
  },
  computed: {
    productId() {
      return this.$route.params.id
    },
    visiblePages() {
      const pages = []
      const start = Math.max(0, this.currentPage - 2)
      const end = Math.min(this.totalPages - 1, this.currentPage + 2)

      for (let i = start; i <= end; i++) {
        pages.push(i)
      }

      return pages
    },
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      if (!this.productId) {
        this.error = 'Product ID is required'
        return
      }

      this.loading = true
      this.error = null

      try {
        // Fetch product details and review count
        const [productResponse, reviewCountResponse] = await Promise.all([
          productsApi.getProductDetails(this.productId),
          reviewsApi.getReviewCount(this.productId).catch(() => ({ data: 0 })),
        ])

        this.product = productResponse.data
        this.reviewCount = reviewCountResponse.data

        // Fetch user's comment if authenticated
        if (keycloak.authenticated) {
          this.fetchUserComment()
        }

        // Fetch all comments
        this.fetchComments()
      } catch (err) {
        console.error('Failed to fetch data:', err)
        this.error = err.response?.data?.message || 'Failed to load product information'
      } finally {
        this.loading = false
      }
    },

    async fetchUserComment() {
      try {
        const response = await commentsApi.getMyComment(this.productId)
        this.userComment = response.data
      } catch (err) {
        // User doesn't have a comment yet, which is fine
        if (err.response?.status !== 404) {
          console.error('Failed to fetch user comment:', err)
        }
      }
    },

    async fetchComments() {
      this.commentsLoading = true
      try {
        const response = await commentsApi.getProductComments(
          this.productId,
          this.currentPage,
          this.pageSize,
        )
        const data = response.data

        this.comments = data.content || []
        this.totalPages = data.total_pages || 0
        this.totalComments = data.total_elements || 0
      } catch (err) {
        console.error('Failed to fetch comments:', err)
        this.comments = []
      } finally {
        this.commentsLoading = false
      }
    },

    addComment() {
      if (!keycloak.authenticated) {
        keycloak.login({ redirectUri: window.location.href })
        return
      }

      this.editingComment = null
      this.showCommentModal = true
    },

    editComment() {
      this.editingComment = this.userComment
      this.showCommentModal = true
    },

    confirmDeleteComment() {
      this.showDeleteModal = true
    },

    async handleCreateComment(commentData) {
      try {
        await commentsApi.createComment(commentData)
        await this.fetchUserComment()
        await this.fetchComments()
        this.closeCommentModal()
      } catch (err) {
        console.error('Failed to create comment:', err)
        throw err
      }
    },

    async handleUpdateComment(commentId, commentData) {
      try {
        await commentsApi.updateComment(commentId, commentData)
        await this.fetchUserComment()
        await this.fetchComments()
        this.closeCommentModal()
      } catch (err) {
        console.error('Failed to update comment:', err)
        throw err
      }
    },

    async handleDeleteComment() {
      try {
        await commentsApi.deleteComment(this.userComment.id)
        this.userComment = null
        await this.fetchComments()
        this.showDeleteModal = false
      } catch (err) {
        console.error('Failed to delete comment:', err)
      }
    },

    closeCommentModal() {
      this.showCommentModal = false
      this.editingComment = null
    },

    changePage(page) {
      if (page >= 0 && page < this.totalPages && page !== this.currentPage) {
        this.currentPage = page
        this.fetchComments()
      }
    },

    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    },

    handleImageError(event) {
      event.target.src = this.placeholderImage
    },
    getInitials(name) {
      if (!name) return '?'
      return name
        .split(' ')
        .map((word) => word.charAt(0).toUpperCase())
        .join('')
        .substring(0, 2)
    },
  },
}
</script>
