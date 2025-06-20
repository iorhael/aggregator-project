<template>
  <div class="relative">
    <div class="relative">
      <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
        <svg class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
          />
        </svg>
      </div>
      <input
        v-model="searchQuery"
        @input="onSearchInput"
        @keyup.enter="onSearchSubmit"
        type="text"
        placeholder="Search products by name..."
        class="block w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-sm"
        :class="{ 'pr-20': isSearching }"
      />

      <div v-if="isSearching" class="absolute inset-y-0 right-0 pr-3 flex items-center">
        <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-600"></div>
      </div>

      <button
        v-else-if="searchQuery"
        @click="clearSearch"
        class="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-400 hover:text-gray-600"
        type="button"
      >
        <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M6 18L18 6M6 6l12 12"
          />
        </svg>
      </button>
    </div>

    <div
      v-if="showSuggestions && recentSearches.length > 0"
      class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg"
    >
      <div class="py-1">
        <div class="px-3 py-2 text-xs font-medium text-gray-500 uppercase tracking-wide">
          Recent Searches
        </div>
        <button
          v-for="(search, index) in recentSearches"
          :key="index"
          @click="selectRecentSearch(search)"
          class="w-full text-left px-3 py-2 text-sm text-gray-700 hover:bg-gray-100 flex items-center"
        >
          <svg
            class="h-4 w-4 text-gray-400 mr-2"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          {{ search }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  props: {
    isSearching: {
      type: Boolean,
      default: false,
    },
    debounceMs: {
      type: Number,
      default: 500,
    },
  },
  emits: ['search', 'clear'],
  data() {
    return {
      searchQuery: '',
      showSuggestions: false,
      recentSearches: [],
      debounceTimer: null,
    }
  },
  mounted() {
    this.loadRecentSearches()
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside)
    if (this.debounceTimer) {
      clearTimeout(this.debounceTimer)
    }
  },
  methods: {
    onSearchInput() {
      if (this.debounceTimer) {
        clearTimeout(this.debounceTimer)
      }

      this.showSuggestions = this.searchQuery.length > 0

      this.debounceTimer = setTimeout(() => {
        if (this.searchQuery.trim()) {
          this.performSearch()
        } else {
          this.clearSearch()
        }
      }, this.debounceMs)
    },

    onSearchSubmit() {
      if (this.searchQuery.trim()) {
        this.performSearch()
        this.showSuggestions = false
      }
    },

    performSearch() {
      const query = this.searchQuery.trim()
      if (query) {
        this.addToRecentSearches(query)
        this.$emit('search', query)
        this.showSuggestions = false
      }
    },

    clearSearch() {
      this.searchQuery = ''
      this.showSuggestions = false
      this.$emit('clear')
    },

    selectRecentSearch(search) {
      this.searchQuery = search
      this.performSearch()
    },

    addToRecentSearches(search) {
      this.recentSearches = this.recentSearches.filter((s) => s !== search)
      this.recentSearches.unshift(search)
      this.recentSearches = this.recentSearches.slice(0, 5)
      localStorage.setItem('recentSearches', JSON.stringify(this.recentSearches))
    },

    loadRecentSearches() {
      try {
        const saved = localStorage.getItem('recentSearches')
        if (saved) {
          this.recentSearches = JSON.parse(saved)
        }
      } catch (error) {
        console.error('Error loading recent searches:', error)
      }
    },

    handleClickOutside(event) {
      if (!this.$el.contains(event.target)) {
        this.showSuggestions = false
      }
    },
  },
}
</script>
