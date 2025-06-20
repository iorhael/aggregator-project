<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gray-50 min-h-screen">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div v-if="loading" class="flex justify-center items-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

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
              <h3 class="text-sm font-medium text-red-800">Error loading retailer information</h3>
              <p class="text-sm text-red-700 mt-1">{{ error }}</p>
            </div>
          </div>
        </div>

        <div v-else-if="retailer" class="space-y-8">
          <div class="bg-white rounded-lg shadow-md overflow-hidden">
            <div class="p-6 lg:p-8">
              <div class="flex flex-col lg:flex-row lg:items-start gap-8">
                <div class="flex-shrink-0">
                  <div class="w-32 h-32 bg-gray-100 rounded-lg overflow-hidden">
                    <img
                      :src="retailer.logo_link || placeholderLogo"
                      :alt="retailer.name"
                      @error="handleLogoError"
                      class="w-full h-full object-cover"
                    />
                  </div>
                </div>

                <div class="flex-1 space-y-4">
                  <div>
                    <h1 class="text-3xl font-bold text-gray-900">{{ retailer.name }}</h1>
                    <p v-if="retailer.description" class="text-lg text-gray-600 mt-2">
                      {{ retailer.description }}
                    </p>
                  </div>

                  <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div v-if="retailer.email" class="flex items-center space-x-3">
                      <svg
                        class="w-5 h-5 text-gray-400"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M3 8l7.89 4.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"
                        />
                      </svg>
                      <div>
                        <p class="text-sm font-medium text-gray-900">Email</p>
                        <a
                          :href="`mailto:${retailer.email}`"
                          class="text-sm text-blue-600 hover:text-blue-500"
                        >
                          {{ retailer.email }}
                        </a>
                      </div>
                    </div>

                    <div v-if="retailer.website" class="flex items-center space-x-3">
                      <svg
                        class="w-5 h-5 text-gray-400"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9v-9m0-9v9m0 9c-5 0-9-4-9-9s4-9 9-9"
                        />
                      </svg>
                      <div>
                        <p class="text-sm font-medium text-gray-900">Website</p>
                        <a
                          :href="retailer.website"
                          target="_blank"
                          rel="noopener noreferrer"
                          class="text-sm text-blue-600 hover:text-blue-500"
                        >
                          {{ formatWebsite(retailer.website) }}
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-md p-6">
            <div class="flex justify-between items-center mb-6">
              <h2 class="text-xl font-semibold text-gray-900">Store Locations</h2>
              <span class="text-sm text-gray-500">{{ totalStores }} stores</span>
            </div>

            <div v-if="storesLoading" class="flex justify-center py-8">
              <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>

            <div v-else-if="stores.length > 0" class="space-y-6">
              <div
                v-for="store in stores"
                :key="store.store_id"
                class="border border-gray-200 rounded-lg p-4"
              >
                <div
                  class="flex flex-col lg:flex-row lg:items-start lg:justify-between space-y-4 lg:space-y-0"
                >
                  <div class="flex-1">
                    <div class="flex items-center space-x-3 mb-3">
                      <div
                        class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center"
                      >
                        <img src="@/assets/store-default.png" alt="Store" class="w-8 h-8" />
                      </div>
                      <h3 class="text-lg font-semibold text-gray-900">
                        {{ store.retailer_name }} Store
                      </h3>
                    </div>

                    <div class="space-y-2">
                      <div class="flex items-center text-gray-600">
                        <svg
                          class="w-4 h-4 mr-2 flex-shrink-0"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                          />
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                          />
                        </svg>
                        <span class="text-sm">{{ store.address || 'No address provided' }}</span>
                      </div>
                      <div class="flex items-center text-gray-600">
                        <svg
                          class="w-4 h-4 mr-2 flex-shrink-0"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"
                          />
                        </svg>
                        <span class="text-sm">{{
                          formatPhone(store.phone) || 'No phone provided'
                        }}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-if="store.opening_hours" class="mt-4">
                  <h4 class="text-sm font-medium text-gray-700 mb-3">Opening Hours</h4>

                  <div class="hidden md:block">
                    <div class="grid grid-cols-7 gap-2">
                      <div
                        v-for="(day, dayName) in store.opening_hours"
                        :key="dayName"
                        class="text-center"
                      >
                        <div class="text-xs font-medium text-gray-500 uppercase mb-1">
                          {{ dayName.slice(0, 3) }}
                        </div>
                        <div
                          class="bg-gray-50 rounded-md p-2 min-h-[60px] flex items-center justify-center"
                        >
                          <span class="text-xs text-gray-700 leading-tight">{{
                            formatSchedule(day)
                          }}</span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="md:hidden space-y-2">
                    <div
                      v-for="(day, dayName) in store.opening_hours"
                      :key="dayName"
                      class="flex justify-between items-center py-2 px-3 bg-gray-50 rounded-md"
                    >
                      <span class="text-sm font-medium text-gray-700 capitalize">{{
                        dayName
                      }}</span>
                      <span class="text-sm text-gray-600">{{ formatSchedule(day) }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div
                v-if="totalStorePages > 1"
                class="flex justify-center items-center space-x-2 pt-6"
              >
                <button
                  @click="goToStorePage(currentStorePage - 1)"
                  :disabled="currentStorePage === 1"
                  class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Previous
                </button>

                <div class="flex space-x-1">
                  <button
                    v-for="page in visibleStorePages"
                    :key="page"
                    @click="goToStorePage(page)"
                    :class="[
                      'px-3 py-2 text-sm font-medium rounded-md',
                      page === currentStorePage
                        ? 'bg-blue-600 text-white'
                        : 'text-gray-700 bg-white border border-gray-300 hover:bg-gray-50',
                    ]"
                  >
                    {{ page }}
                  </button>
                </div>

                <button
                  @click="goToStorePage(currentStorePage + 1)"
                  :disabled="currentStorePage === totalStorePages"
                  class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Next
                </button>
              </div>
            </div>

            <div v-else class="text-center py-8">
              <img
                src="@/assets/no-stores.png"
                alt="No Stores"
                class="mx-auto h-12 w-12 text-gray-400 mb-4"
              />
              <p class="text-gray-500">This retailer has no store locations listed.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script>
import AppLayout from '@/components/AppLayout.vue'
import { retailersApi, storesApi } from '@/api'
import organizationLogoDefault from '@/assets/organization-logo-default.png'

export default {
  name: 'RetailerInfoPublic',
  components: {
    AppLayout,
  },
  data() {
    return {
      retailer: null,
      stores: [],
      loading: false,
      storesLoading: false,
      error: null,
      placeholderLogo: organizationLogoDefault,
      currentStorePage: 1,
      totalStorePages: 0,
      totalStores: 0,
      storePageSize: 10,
    }
  },
  computed: {
    visibleStorePages() {
      const pages = []
      const start = Math.max(1, this.currentStorePage - 2)
      const end = Math.min(this.totalStorePages, this.currentStorePage + 2)

      for (let i = start; i <= end; i++) {
        pages.push(i)
      }

      return pages
    },
  },
  mounted() {
    this.fetchRetailerData()
  },
  methods: {
    async fetchRetailerData() {
      const retailerName = this.$route.params.name
      if (!retailerName) {
        this.error = 'Retailer name is required'
        return
      }

      this.loading = true
      this.error = null

      try {
        const response = await retailersApi.getRetailerByName(retailerName)
        this.retailer = response.data

        // Fetch stores
        this.fetchStores()
      } catch (err) {
        console.error('Failed to fetch retailer data:', err)
        this.error = err.response?.data?.message || 'Failed to load retailer information'
      } finally {
        this.loading = false
      }
    },

    async fetchStores() {
      if (!this.retailer?.id) return

      this.storesLoading = true
      try {
        const response = await storesApi.getStoresByRetailerId(
          this.retailer.id,
          this.currentStorePage - 1,
          this.storePageSize,
        )

        this.stores = response.data.content || []
        this.totalStores = response.data.total_elements || 0
        this.totalStorePages = response.data.total_pages || 0
      } catch (err) {
        console.error('Failed to fetch stores:', err)
        this.stores = []
      } finally {
        this.storesLoading = false
      }
    },

    goToStorePage(page) {
      if (page >= 1 && page <= this.totalStorePages && page !== this.currentStorePage) {
        this.currentStorePage = page
        this.fetchStores()
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    },

    handleLogoError(event) {
      event.target.src = this.placeholderLogo
    },

    formatWebsite(website) {
      return website.replace(/^https?:\/\//, '')
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

    formatSchedule(schedule) {
      if (!schedule) return 'Closed'

      // Handle common schedule formats
      if (schedule.toLowerCase().includes('closed')) {
        return 'Closed'
      }

      if (schedule.toLowerCase().includes('24') || schedule.toLowerCase().includes('24/7')) {
        return '24/7'
      }

      // Try to format time ranges
      const timePattern = /(\d{1,2}):?(\d{2})?\s*-\s*(\d{1,2}):?(\d{2})?/
      const match = schedule.match(timePattern)

      if (match) {
        const [, startHour, startMin = '00', endHour, endMin = '00'] = match
        return `${startHour}:${startMin}-${endHour}:${endMin}`
      }

      return schedule
    },
  },
}
</script>
