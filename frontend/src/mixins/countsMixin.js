import { productCardsApi, storesApi } from '@/api'
import countsCache from '@/utils/countsCache.js'

const countsMixin = {
  data() {
    return {
      cardsCount: 0,
      storesCount: 0,
      countsLoading: false,
    }
  },
  methods: {
    async fetchCounts(force = false) {
      // Check cache first unless forced
      if (!force) {
        const cachedCards = countsCache.get('cardsCount')
        const cachedStores = countsCache.get('storesCount')

        if (cachedCards !== null && cachedStores !== null) {
          this.cardsCount = cachedCards
          this.storesCount = cachedStores
          return
        }
      }

      this.countsLoading = true
      try {
        const [cardsResponse, storesResponse] = await Promise.all([
          productCardsApi.getMyProductCardsCount(),
          storesApi.getMyStoresCount(),
        ])

        this.cardsCount = cardsResponse.data
        this.storesCount = storesResponse.data

        // Cache the results
        countsCache.set('cardsCount', this.cardsCount)
        countsCache.set('storesCount', this.storesCount)
      } catch (err) {
        console.error('Failed to fetch counts:', err)
      } finally {
        this.countsLoading = false
      }
    },

    invalidateCounts() {
      countsCache.invalidateAll()
    },
  },
}

export default countsMixin
