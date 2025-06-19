<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-10 mx-auto p-5 border w-full max-w-4xl shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <!-- Header -->
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-medium text-gray-900">Price Dynamics - {{ productName }}</h3>
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

        <!-- Controls -->
        <div class="mb-6 p-4 bg-gray-50 rounded-lg">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <!-- Period Type -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Period Type</label>
              <select
                v-model="periodType"
                @change="onPeriodTypeChange"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              >
                <option value="days">Daily</option>
                <option value="months">Monthly</option>
              </select>
            </div>

            <!-- Period Count -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Number of {{ periodType === 'days' ? 'Days' : 'Months' }}
              </label>
              <input
                v-model="periodCount"
                type="number"
                :min="1"
                :max="periodType === 'days' ? 365 : 24"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              />
            </div>

            <!-- Load Button -->
            <div class="flex items-end">
              <button
                @click="loadPriceData"
                :disabled="loading"
                class="w-full bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md font-medium transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span v-if="loading" class="flex items-center justify-center">
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
                  Loading...
                </span>
                <span v-else>Load Data</span>
              </button>
            </div>
          </div>
        </div>

        <!-- Error State -->
        <div v-if="error" class="bg-red-50 border border-red-200 rounded-md p-4 mb-6">
          <p class="text-red-600 text-sm">{{ error }}</p>
        </div>

        <!-- Chart Container -->
        <div class="bg-white border border-gray-200 rounded-lg p-4">
          <div v-if="loading" class="flex justify-center items-center py-12">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          </div>

          <div v-else-if="!hasData" class="text-center py-12 text-gray-500">
            <svg
              class="mx-auto h-12 w-12 text-gray-400 mb-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"
              />
            </svg>
            <p v-if="dataLoaded">No price data available for the selected period</p>
            <p v-else>Select period and click "Load Data" to view price dynamics</p>
          </div>

          <!-- Always render canvas, but hide it when no data -->
          <div :class="{ hidden: !hasData }">
            <canvas ref="chartCanvas" class="w-full" style="max-height: 400px"></canvas>
          </div>
        </div>

        <!-- Statistics -->
        <div v-if="hasData" class="mt-6 grid grid-cols-2 md:grid-cols-4 gap-4">
          <div class="bg-blue-50 p-3 rounded-lg text-center">
            <p class="text-sm text-blue-600 font-medium">Current Price</p>
            <p class="text-lg font-bold text-blue-900">${{ formatPrice(currentPrice) }}</p>
          </div>
          <div class="bg-green-50 p-3 rounded-lg text-center">
            <p class="text-sm text-green-600 font-medium">Lowest Price</p>
            <p class="text-lg font-bold text-green-900">${{ formatPrice(minPrice) }}</p>
          </div>
          <div class="bg-red-50 p-3 rounded-lg text-center">
            <p class="text-sm text-red-600 font-medium">Highest Price</p>
            <p class="text-lg font-bold text-red-900">${{ formatPrice(maxPrice) }}</p>
          </div>
          <div class="bg-gray-50 p-3 rounded-lg text-center">
            <p class="text-sm text-gray-600 font-medium">Average Price</p>
            <p class="text-lg font-bold text-gray-900">${{ formatPrice(avgPrice) }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { priceHistoryApi } from '@/api'
import Chart from 'chart.js/auto'

export default {
  name: 'PriceDynamicsModal',
  props: {
    productId: {
      type: String,
      required: true,
    },
    productName: {
      type: String,
      required: true,
    },
  },
  emits: ['close'],
  data() {
    return {
      periodType: 'days',
      periodCount: 30,
      loading: false,
      error: null,
      chartData: [],
      chart: null,
      dataLoaded: false,
      // Cache for loaded data
      cache: {
        daily: new Map(),
        monthly: new Map(),
      },
    }
  },
  computed: {
    hasData() {
      return this.chartData.length > 0
    },
    currentPrice() {
      if (!this.hasData) return 0
      return this.chartData[this.chartData.length - 1].price
    },
    minPrice() {
      if (!this.hasData) return 0
      return Math.min(...this.chartData.map((d) => d.price))
    },
    maxPrice() {
      if (!this.hasData) return 0
      return Math.max(...this.chartData.map((d) => d.price))
    },
    avgPrice() {
      if (!this.hasData) return 0
      const sum = this.chartData.reduce((acc, d) => acc + d.price, 0)
      return sum / this.chartData.length
    },
  },
  beforeUnmount() {
    if (this.chart) {
      this.chart.destroy()
    }
  },
  methods: {
    onPeriodTypeChange() {
      // Reset period count based on type
      this.periodCount = this.periodType === 'days' ? 30 : 6
      this.dataLoaded = false
      this.chartData = []
      if (this.chart) {
        this.chart.destroy()
        this.chart = null
      }
    },

    async loadPriceData() {
      this.loading = true
      this.error = null

      try {
        // Check cache first
        const cacheKey = `${this.periodType}-${this.periodCount}`
        const cacheType = this.periodType === 'days' ? 'daily' : 'monthly'

        if (this.cache[cacheType].has(cacheKey)) {
          this.chartData = this.cache[cacheType].get(cacheKey)
          this.dataLoaded = true
          if (this.hasData) {
            // Use setTimeout to ensure DOM is fully updated
            setTimeout(() => {
              this.renderChart()
            }, 100)
          }
          return
        }

        // Load larger dataset for caching
        const loadCount =
          this.periodType === 'days'
            ? Math.max(this.periodCount, 90)
            : Math.max(this.periodCount, 12)

        let response
        if (this.periodType === 'days') {
          response = await priceHistoryApi.getDailyPriceDynamics(this.productId, loadCount)
        } else {
          response = await priceHistoryApi.getMonthlyPriceDynamics(this.productId, loadCount)
        }

        // Transform data
        const fullData = this.transformPriceData(response.data)

        // Cache the full dataset
        this.cache[cacheType].set(`${this.periodType}-${loadCount}`, fullData)

        // Use only requested amount
        this.chartData = fullData.slice(-this.periodCount)

        // Cache the requested subset too
        this.cache[cacheType].set(cacheKey, this.chartData)

        this.dataLoaded = true

        if (this.hasData) {
          // Use setTimeout to ensure DOM is fully updated
          setTimeout(() => {
            this.renderChart()
          }, 100)
        }
      } catch (err) {
        console.error('Failed to load price data:', err)
        this.error = err.response?.data?.message || 'Failed to load price dynamics'
        this.dataLoaded = true
      } finally {
        this.loading = false
      }
    },

    transformPriceData(data) {
      if (!data || typeof data !== 'object') {
        return []
      }

      return Object.entries(data)
        .map(([date, price]) => ({
          date,
          price: parseFloat(price),
        }))
        .sort((a, b) => new Date(a.date) - new Date(b.date))
    },

    renderChart() {
      // Destroy existing chart
      if (this.chart) {
        this.chart.destroy()
        this.chart = null
      }

      // Check if canvas exists and is accessible
      if (!this.$refs.chartCanvas) {
        console.error('Chart canvas not found')
        return
      }

      try {
        const ctx = this.$refs.chartCanvas.getContext('2d')

        if (!ctx) {
          console.error('Could not get canvas context')
          return
        }

        this.chart = new Chart(ctx, {
          type: 'line',
          data: {
            labels: this.chartData.map((d) => this.formatDateLabel(d.date)),
            datasets: [
              {
                label: 'Price ($)',
                data: this.chartData.map((d) => d.price),
                borderColor: 'rgb(59, 130, 246)',
                backgroundColor: 'rgba(59, 130, 246, 0.1)',
                borderWidth: 2,
                fill: true,
                tension: 0.1,
              },
            ],
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                display: false,
              },
              tooltip: {
                callbacks: {
                  label: (context) => `$${this.formatPrice(context.parsed.y)}`,
                },
              },
            },
            scales: {
              x: {
                title: {
                  display: true,
                  text: this.periodType === 'days' ? 'Date' : 'Month',
                },
              },
              y: {
                title: {
                  display: true,
                  text: 'Price ($)',
                },
                ticks: {
                  callback: (value) => `$${this.formatPrice(value)}`,
                },
              },
            },
          },
        })
      } catch (error) {
        console.error('Error creating chart:', error)
        this.error = 'Failed to render chart'
      }
    },

    formatDateLabel(dateStr) {
      const date = new Date(dateStr)
      if (this.periodType === 'days') {
        return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' })
      } else {
        return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short' })
      }
    },

    formatPrice(price) {
      return parseFloat(price).toFixed(2)
    },
  },
}
</script>
