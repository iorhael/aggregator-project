class CountsCache {
  constructor() {
    this.cache = new Map()
    this.cacheTimeout = 5 * 60 * 1000
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

  invalidateAll() {
    this.cache.clear()
  }
}

const countsCache = new CountsCache()

export default countsCache
