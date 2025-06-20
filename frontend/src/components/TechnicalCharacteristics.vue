<template>
  <div class="space-y-4">
    <div v-for="(section, sectionKey) in characteristics" :key="sectionKey" class="space-y-2">
      <h4 class="text-sm font-semibold text-gray-900 capitalize border-b border-gray-200 pb-1">
        {{ formatSectionName(sectionKey) }}
      </h4>

      <div class="space-y-1 pl-2">
        <div v-if="isObject(section)">
          <div
            v-for="(value, key) in section"
            :key="key"
            class="flex justify-between items-start py-1"
          >
            <span class="text-sm text-gray-600 capitalize flex-1">{{ formatKey(key) }}:</span>
            <span class="text-sm text-gray-900 flex-1 text-right">{{ formatValue(value) }}</span>
          </div>
        </div>
        <div v-else class="flex justify-between items-start py-1">
          <span class="text-sm text-gray-600 capitalize flex-1"
            >{{ formatSectionName(sectionKey) }}:</span
          >
          <span class="text-sm text-gray-900 flex-1 text-right">{{ formatValue(section) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TechnicalCharacteristics',
  props: {
    characteristics: {
      type: [Object, String],
      default: () => ({}),
    },
  },
  computed: {
    parsedCharacteristics() {
      if (typeof this.characteristics === 'string') {
        try {
          return JSON.parse(this.characteristics)
        } catch (e) {
          return {}
        }
      }
      return this.characteristics || {}
    },
  },
  methods: {
    isObject(value) {
      return value !== null && typeof value === 'object' && !Array.isArray(value)
    },

    formatSectionName(key) {
      return key
        .replace(/_/g, ' ')
        .replace(/([A-Z])/g, ' $1')
        .trim()
    },

    formatKey(key) {
      return key
        .replace(/_/g, ' ')
        .replace(/([A-Z])/g, ' $1')
        .trim()
    },

    formatValue(value) {
      if (Array.isArray(value)) {
        return value.join(', ')
      }
      if (typeof value === 'boolean') {
        return value ? 'Yes' : 'No'
      }
      return String(value)
    },
  },
}
</script>
