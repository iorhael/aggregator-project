<template>
  <div>
    <div
      class="flex items-center justify-between p-3 hover:bg-gray-50 rounded-md cursor-pointer transition-colors"
      :class="{ 'bg-blue-50': isExpanded }"
      @click="handleClick"
    >
      <div class="flex items-center space-x-3" :style="{ paddingLeft: level * 20 + 'px' }">
        <button
          v-if="hasChildren"
          @click.stop="toggleExpanded"
          class="text-gray-400 hover:text-gray-600"
        >
          <svg
            class="w-4 h-4 transition-transform"
            :class="{ 'rotate-90': isExpanded }"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M9 5l7 7-7 7"
            />
          </svg>
        </button>
        <div v-else class="w-4"></div>

        <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
          <svg class="w-4 h-4 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
            <path
              fill-rule="evenodd"
              d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
              clip-rule="evenodd"
            />
          </svg>
        </div>

        <span class="text-sm font-medium text-gray-900">{{ category.name }}</span>
      </div>

      <button
        @click.stop="selectCategory"
        class="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded text-xs font-medium transition-colors"
      >
        Select
      </button>
    </div>

    <div v-if="isExpanded && hasChildren" class="mt-1">
      <CategoryTreeNode
        v-for="child in category.child_categories"
        :key="child.category_id"
        :category="child"
        :level="level + 1"
        @select="$emit('select', $event)"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'CategoryTreeNode',
  props: {
    category: {
      type: Object,
      required: true,
    },
    level: {
      type: Number,
      default: 0,
    },
  },
  emits: ['select'],
  data() {
    return {
      isExpanded: false,
    }
  },
  computed: {
    hasChildren() {
      return this.category.child_categories && this.category.child_categories.length > 0
    },
  },
  methods: {
    toggleExpanded() {
      if (this.hasChildren) {
        this.isExpanded = !this.isExpanded
      }
    },

    handleClick() {
      if (this.hasChildren) {
        this.toggleExpanded()
      } else {
        this.selectCategory()
      }
    },

    selectCategory() {
      this.$emit('select', this.category)
    },
  },
}
</script>
