// Simple cache for counts to avoid frequent API calls
class CountsCache {
  constructor() {
    this.cache = new Map()
    this.cacheTimeout = 5 * 60 * 1000 // 5 minutes
  }

  set(key, value) {
    this.cache.set(key, {
      value,
      timestamp: Date.now(),
    })
  }

  get(key) {
    const cached = this.cache.get(key)
    if (!cached) return null

    // Check if cache is expired
    if (Date.now() - cached.timestamp > this.cacheTimeout) {
      this.cache.delete(key)
      return null
    }

    return cached.value
  }

  invalidate(key) {
    if (key) {
      this.cache.delete(key)
    } else {
      this.cache.clear()
    }
  }

  // Invalidate all counts when data changes
  invalidateAll() {
    this.cache.clear()
  }
}

const countsCache = new CountsCache()

export default countsCache
