<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-10 mx-auto p-5 border w-full max-w-4xl shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <!-- Header -->
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-medium text-gray-900">Select Category</h3>
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

        <!-- Loading State -->
        <div v-if="loading" class="flex justify-center items-center py-12">
          <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        </div>

        <!-- Error State -->
        <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-md p-4">
          <p class="text-red-600 text-sm">{{ error }}</p>
          <button
            @click="fetchCategoryTree"
            class="mt-2 text-sm text-red-600 hover:text-red-500 underline"
          >
            Try again
          </button>
        </div>

        <!-- Category Tree -->
        <div v-else class="max-h-96 overflow-y-auto">
          <div class="space-y-2">
            <CategoryTreeNode
              v-for="category in categories"
              :key="category.category_id"
              :category="category"
              :level="0"
              @select="handleCategorySelect"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { categoriesApi } from '@/api'
import CategoryTreeNode from './CategoryTreeNode.vue'

export default {
  name: 'CategoryTreeModal',
  components: {
    CategoryTreeNode,
  },
  props: {
    rootCategory: {
      type: Object,
      default: null,
    },
  },
  emits: ['close', 'select'],
  data() {
    return {
      categories: [],
      loading: false,
      error: null,
    }
  },
  mounted() {
    this.fetchCategoryTree()
  },
  methods: {
    async fetchCategoryTree() {
      this.loading = true
      this.error = null

      try {
        if (this.rootCategory) {
          // Load subcategories for the selected root category
          const response = await categoriesApi.getSubcategories(this.rootCategory.category_id)
          // The response should be the category with its children
          this.categories = [response.data]
        } else {
          // Load top level categories
          const response = await categoriesApi.getTopLevelCategories(0, 100)
          this.categories = response.data || []
        }
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to load categories'
      } finally {
        this.loading = false
      }
    },

    handleCategorySelect(category) {
      this.$emit('select', category)
    },
  },
}
</script>
