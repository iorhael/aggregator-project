<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gray-50 min-h-screen">
      <div class="bg-white border-b border-gray-200">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div
            class="flex flex-col sm:flex-row justify-center items-center py-4 space-y-2 sm:space-y-0 sm:space-x-8"
          >
            <router-link to="/retailer" class="text-gray-500 hover:text-gray-700 pb-2">
              Organization Info
            </router-link>
            <router-link to="/cards" class="text-gray-500 hover:text-gray-700 pb-2">
              Cards Management ({{ cardsCount || 0 }})
            </router-link>
            <router-link to="/stores" class="text-gray-500 hover:text-gray-700 pb-2">
              Stores Management ({{ storesCount || 0 }})
            </router-link>
            <button class="text-blue-600 border-b-2 border-blue-600 pb-2 font-medium">
              Jobs History
            </button>
          </div>
        </div>
      </div>

      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div class="mb-8">
          <h1 class="text-3xl font-bold text-gray-900 mb-2">Jobs History</h1>
          <p class="text-gray-600">
            View the history of all background jobs including imports, exports, and auto-updates.
          </p>
        </div>

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
              <h3 class="text-sm font-medium text-red-800">Error loading jobs history</h3>
              <p class="text-sm text-red-700 mt-1">{{ error }}</p>
              <button
                @click="fetchJobs"
                class="mt-2 text-sm text-red-600 hover:text-red-500 underline"
              >
                Try again
              </button>
            </div>
          </div>
        </div>

        <div v-else-if="jobs.length > 0" class="space-y-4">
          <div class="hidden lg:block bg-white shadow-md rounded-lg overflow-hidden">
            <div class="bg-gray-50 px-6 py-3 border-b border-gray-200">
              <div
                class="grid grid-cols-12 gap-4 text-xs font-medium text-gray-500 uppercase tracking-wide"
              >
                <div class="col-span-3">Job Name</div>
                <div class="col-span-2">Status</div>
                <div class="col-span-2">Start Time</div>
                <div class="col-span-2">End Time</div>
                <div class="col-span-1">Duration</div>
                <div class="col-span-2">Exit Status</div>
              </div>
            </div>

            <div class="divide-y divide-gray-200">
              <div
                v-for="job in jobs"
                :key="job.job_name + job.start_time"
                class="px-6 py-4 hover:bg-gray-50 transition-colors"
              >
                <div class="grid grid-cols-12 gap-4 items-center">
                  <div class="col-span-3">
                    <div class="flex items-center">
                      <div
                        class="w-8 h-8 rounded-full flex items-center justify-center mr-3"
                        :class="getJobIconClass(job.job_name)"
                      >
                        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                          <path
                            v-if="job.job_name.includes('import')"
                            d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM6.293 6.707a1 1 0 010-1.414l3-3a1 1 0 011.414 0l3 3a1 1 0 01-1.414 1.414L11 5.414V13a1 1 0 11-2 0V5.414L7.707 6.707a1 1 0 01-1.414 0z"
                          />
                          <path
                            v-else-if="job.job_name.includes('export')"
                            d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm3.293-7.707a1 1 0 011.414 0L9 10.586V3a1 1 0 112 0v7.586l1.293-1.293a1 1 0 111.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z"
                          />
                          <path
                            v-else
                            d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                          />
                        </svg>
                      </div>
                      <div>
                        <h3 class="text-sm font-medium text-gray-900">
                          {{ formatJobName(job.job_name) }}
                        </h3>
                        <p class="text-xs text-gray-500">{{ job.job_name }}</p>
                      </div>
                    </div>
                  </div>

                  <div class="col-span-2">
                    <span
                      class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                      :class="getStatusClass(job.status)"
                    >
                      {{ job.status }}
                    </span>
                  </div>

                  <div class="col-span-2">
                    <p class="text-sm text-gray-900">{{ formatDateTime(job.start_time) }}</p>
                  </div>

                  <div class="col-span-2">
                    <p class="text-sm text-gray-900">
                      {{ job.end_time ? formatDateTime(job.end_time) : '-' }}
                    </p>
                  </div>

                  <div class="col-span-1">
                    <p class="text-sm text-gray-600">
                      {{ calculateDuration(job.start_time, job.end_time) }}
                    </p>
                  </div>

                  <div class="col-span-2">
                    <span class="text-sm text-gray-600">{{ job.exit_status || '-' }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="lg:hidden space-y-4">
            <div
              v-for="job in jobs"
              :key="job.job_name + job.start_time"
              class="bg-white rounded-lg shadow-md p-4"
            >
              <div class="flex items-start justify-between mb-3">
                <div class="flex items-center">
                  <div
                    class="w-10 h-10 rounded-full flex items-center justify-center mr-3"
                    :class="getJobIconClass(job.job_name)"
                  >
                    <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                      <path
                        v-if="job.job_name.includes('import')"
                        d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM6.293 6.707a1 1 0 010-1.414l3-3a1 1 0 011.414 0l3 3a1 1 0 01-1.414 1.414L11 5.414V13a1 1 0 11-2 0V5.414L7.707 6.707a1 1 0 01-1.414 0z"
                      />
                      <path
                        v-else-if="job.job_name.includes('export')"
                        d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm3.293-7.707a1 1 0 011.414 0L9 10.586V3a1 1 0 112 0v7.586l1.293-1.293a1 1 0 111.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z"
                      />
                      <path
                        v-else
                        d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                      />
                    </svg>
                  </div>
                  <div>
                    <h3 class="text-sm font-medium text-gray-900">
                      {{ formatJobName(job.job_name) }}
                    </h3>
                    <p class="text-xs text-gray-500">{{ formatDateTime(job.start_time) }}</p>
                  </div>
                </div>
                <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                  :class="getStatusClass(job.status)"
                >
                  {{ job.status }}
                </span>
              </div>

              <div class="grid grid-cols-2 gap-3 text-sm">
                <div>
                  <span class="text-gray-500">Duration:</span>
                  <span class="ml-1 text-gray-900">{{
                    calculateDuration(job.start_time, job.end_time)
                  }}</span>
                </div>
                <div>
                  <span class="text-gray-500">Exit Status:</span>
                  <span class="ml-1 text-gray-900">{{ job.exit_status || '-' }}</span>
                </div>
                <div v-if="job.end_time" class="col-span-2">
                  <span class="text-gray-500">Completed:</span>
                  <span class="ml-1 text-gray-900">{{ formatDateTime(job.end_time) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center py-12">
          <div class="bg-white rounded-lg shadow-md p-8">
            <img
              src="@/assets/no-jobs.png"
              alt="No Jobs Image"
              class="mx-auto h-16 w-16 text-gray-400 mb-4"
            />
            <h3 class="text-xl font-semibold text-gray-900 mb-2">No Jobs Found</h3>
            <p class="text-gray-600">
              No background jobs have been executed yet. Jobs will appear here when you import
              products, export data, or configure auto-updates.
            </p>
          </div>
        </div>

        <div v-if="totalPages > 1" class="flex justify-center items-center space-x-2 mt-8">
          <button
            @click="goToPage(currentPage - 1)"
            :disabled="currentPage === 1"
            class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Previous
          </button>

          <div class="flex space-x-1">
            <button
              v-for="page in visiblePages"
              :key="page"
              @click="goToPage(page)"
              :class="[
                'px-3 py-2 text-sm font-medium rounded-md',
                page === currentPage
                  ? 'bg-blue-600 text-white'
                  : 'text-gray-700 bg-white border border-gray-300 hover:bg-gray-50',
              ]"
            >
              {{ page }}
            </button>
          </div>

          <button
            @click="goToPage(currentPage + 1)"
            :disabled="currentPage === totalPages"
            class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Next
          </button>
        </div>

        <div v-if="jobs.length > 0" class="text-center mt-4 text-sm text-gray-600">
          Showing {{ (currentPage - 1) * pageSize + 1 }} to
          {{ Math.min(currentPage * pageSize, totalJobs) }} of {{ totalJobs }} jobs
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script>
import AppLayout from '@/components/AppLayout.vue'
import { batchApi } from '@/api'
import countsMixin from '@/mixins/countsMixin.js'

export default {
  name: 'JobsHistory',
  components: {
    AppLayout,
  },
  mixins: [countsMixin],
  data() {
    return {
      jobs: [],
      totalJobs: 0,
      totalPages: 0,
      currentPage: 1,
      pageSize: 15,
      loading: false,
      error: null,
    }
  },
  computed: {
    visiblePages() {
      const pages = []
      const start = Math.max(1, this.currentPage - 2)
      const end = Math.min(this.totalPages, this.currentPage + 2)

      for (let i = start; i <= end; i++) {
        pages.push(i)
      }

      return pages
    },
  },
  mounted() {
    this.fetchJobs()
    this.fetchCounts()
  },
  methods: {
    async fetchJobs() {
      this.loading = true
      this.error = null

      try {
        const response = await batchApi.getJobExecutions(this.currentPage - 1, this.pageSize)

        this.jobs = response.data.content || []
        this.totalJobs = response.data.total_elements || response.data.totalElements || 0
        this.totalPages = response.data.total_pages || response.data.totalPages || 0
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to load jobs history'
        this.jobs = []
      } finally {
        this.loading = false
      }
    },

    goToPage(page) {
      if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
        this.currentPage = page
        this.fetchJobs()
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    },

    formatJobName(jobName) {
      const jobNames = {
        importProductCards: 'Import Product Cards',
        exportProductCards: 'Export Product Cards',
        autoUpdateProductCards: 'Auto Update Product Cards',
        productCardsBatchImport: 'Batch Import Products',
        productCardsBatchExport: 'Batch Export Products',
      }

      return (
        jobNames[jobName] ||
        jobName.replace(/([A-Z])/g, ' $1').replace(/^./, (str) => str.toUpperCase())
      )
    },

    formatDateTime(dateTime) {
      if (!dateTime) return '-'

      const date = new Date(dateTime)
      return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
      })
    },

    calculateDuration(startTime, endTime) {
      if (!startTime) return '-'
      if (!endTime) return 'Running...'

      const start = new Date(startTime)
      const end = new Date(endTime)
      const diffMs = end - start

      if (diffMs < 1000) return '< 1s'
      if (diffMs < 60000) return `${Math.round(diffMs / 1000)}s`
      if (diffMs < 3600000) return `${Math.round(diffMs / 60000)}m`

      const hours = Math.floor(diffMs / 3600000)
      const minutes = Math.round((diffMs % 3600000) / 60000)
      return `${hours}h ${minutes}m`
    },

    getStatusClass(status) {
      const statusClasses = {
        COMPLETED: 'bg-green-100 text-green-800',
        FAILED: 'bg-red-100 text-red-800',
        STARTED: 'bg-blue-100 text-blue-800',
        STARTING: 'bg-yellow-100 text-yellow-800',
        STOPPING: 'bg-orange-100 text-orange-800',
        STOPPED: 'bg-gray-100 text-gray-800',
        ABANDONED: 'bg-purple-100 text-purple-800',
        UNKNOWN: 'bg-gray-100 text-gray-800',
      }

      return statusClasses[status] || 'bg-gray-100 text-gray-800'
    },

    getJobIconClass(jobName) {
      if (jobName.includes('import')) {
        return 'bg-blue-100 text-blue-600'
      } else if (jobName.includes('export')) {
        return 'bg-green-100 text-green-600'
      } else {
        return 'bg-purple-100 text-purple-600'
      }
    },
  },
}
</script>
