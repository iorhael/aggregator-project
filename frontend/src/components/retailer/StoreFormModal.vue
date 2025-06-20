<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-10 mx-auto p-5 border w-full max-w-4xl shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-medium text-gray-900">
            {{ isEdit ? 'Edit Store' : 'Create New Store' }}
          </h3>
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

        <form @submit.prevent="handleSubmit" class="space-y-6">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="md:col-span-2">
              <label for="address" class="block text-sm font-medium text-gray-700 mb-1">
                Store Address *
              </label>
              <input
                id="address"
                v-model="form.address"
                type="text"
                required
                placeholder="123 Main Street, City, State"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                :class="{ 'border-red-300': errors.address }"
              />
              <p v-if="errors.address" class="text-red-600 text-xs mt-1">{{ errors.address }}</p>
            </div>

            <div>
              <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">
                Phone Number *
              </label>
              <input
                id="phone"
                v-model="form.phone"
                type="tel"
                required
                placeholder="12345678901"
                maxlength="15"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                :class="{ 'border-red-300': errors.phone }"
                @input="validatePhone"
              />
              <p class="text-xs text-gray-500 mt-1">Numbers only, 11-15 digits</p>
              <p v-if="errors.phone" class="text-red-600 text-xs mt-1">{{ errors.phone }}</p>
            </div>
          </div>

          <div>
            <h4 class="text-lg font-medium text-gray-900 mb-4">Opening Hours</h4>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div v-for="day in daysOfWeek" :key="day" class="space-y-2">
                <label :for="day" class="block text-sm font-medium text-gray-700 capitalize">
                  {{ day }} *
                </label>
                <div class="space-y-2">
                  <div class="flex space-x-2">
                    <button
                      type="button"
                      @click="setDaySchedule(day, '09:00-18:00')"
                      class="text-xs px-2 py-1 bg-gray-100 hover:bg-gray-200 rounded transition-colors"
                    >
                      9-18
                    </button>
                    <button
                      type="button"
                      @click="setDaySchedule(day, '10:00-22:00')"
                      class="text-xs px-2 py-1 bg-gray-100 hover:bg-gray-200 rounded transition-colors"
                    >
                      10-22
                    </button>
                    <button
                      type="button"
                      @click="setDaySchedule(day, 'Closed')"
                      class="text-xs px-2 py-1 bg-gray-100 hover:bg-gray-200 rounded transition-colors"
                    >
                      Closed
                    </button>
                  </div>

                  <input
                    :id="day"
                    v-model="form.opening_hours[day]"
                    type="text"
                    required
                    placeholder="09:00-18:00 or Closed"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-sm"
                    :class="{ 'border-red-300': errors[`opening_hours.${day}`] }"
                  />
                  <p v-if="errors[`opening_hours.${day}`]" class="text-red-600 text-xs">
                    {{ errors[`opening_hours.${day}`] }}
                  </p>
                </div>
              </div>
            </div>

            <div class="mt-4 p-4 bg-gray-50 rounded-md">
              <h5 class="text-sm font-medium text-gray-700 mb-2">Quick Fill Options:</h5>
              <div class="flex flex-wrap gap-2">
                <button
                  type="button"
                  @click="setAllDays('09:00-18:00')"
                  class="text-sm px-3 py-1 bg-blue-100 hover:bg-blue-200 text-blue-700 rounded transition-colors"
                >
                  All Days 9-18
                </button>
                <button
                  type="button"
                  @click="setWeekdaysWeekends('09:00-18:00', '10:00-16:00')"
                  class="text-sm px-3 py-1 bg-green-100 hover:bg-green-200 text-green-700 rounded transition-colors"
                >
                  Weekdays 9-18, Weekends 10-16
                </button>
                <button
                  type="button"
                  @click="setWeekdaysWeekends('10:00-22:00', 'Closed')"
                  class="text-sm px-3 py-1 bg-purple-100 hover:bg-purple-200 text-purple-700 rounded transition-colors"
                >
                  Weekdays 10-22, Weekends Closed
                </button>
              </div>
            </div>
          </div>

          <div v-if="submitError" class="bg-red-50 border border-red-200 rounded-md p-3">
            <p class="text-red-600 text-sm">{{ submitError }}</p>
          </div>

          <div class="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              @click="$emit('close')"
              class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="submitting"
              class="px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <span v-if="submitting" class="flex items-center">
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
                {{ isEdit ? 'Updating...' : 'Creating...' }}
              </span>
              <span v-else>
                {{ isEdit ? 'Update Store' : 'Create Store' }}
              </span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { storesApi } from '@/api'

export default {
  name: 'StoreFormModal',
  props: {
    isEdit: {
      type: Boolean,
      default: false,
    },
    store: {
      type: Object,
      default: null,
    },
  },
  emits: ['close', 'success'],
  data() {
    return {
      form: {
        address: '',
        phone: '',
        opening_hours: {
          monday: '',
          tuesday: '',
          wednesday: '',
          thursday: '',
          friday: '',
          saturday: '',
          sunday: '',
        },
      },
      daysOfWeek: ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'],
      errors: {},
      submitError: null,
      submitting: false,
    }
  },
  mounted() {
    if (this.isEdit && this.store) {
      this.form = {
        address: this.store.address || '',
        phone: this.store.phone || '',
        opening_hours: {
          monday: this.store.opening_hours?.monday || '',
          tuesday: this.store.opening_hours?.tuesday || '',
          wednesday: this.store.opening_hours?.wednesday || '',
          thursday: this.store.opening_hours?.thursday || '',
          friday: this.store.opening_hours?.friday || '',
          saturday: this.store.opening_hours?.saturday || '',
          sunday: this.store.opening_hours?.sunday || '',
        },
      }
    }
  },
  methods: {
    validatePhone() {
      this.form.phone = this.form.phone.replace(/\D/g, '')
    },

    setDaySchedule(day, schedule) {
      this.form.opening_hours[day] = schedule
    },

    setAllDays(schedule) {
      this.daysOfWeek.forEach((day) => {
        this.form.opening_hours[day] = schedule
      })
    },

    setWeekdaysWeekends(weekdaySchedule, weekendSchedule) {
      const weekdays = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday']
      const weekends = ['saturday', 'sunday']

      weekdays.forEach((day) => {
        this.form.opening_hours[day] = weekdaySchedule
      })

      weekends.forEach((day) => {
        this.form.opening_hours[day] = weekendSchedule
      })
    },

    validateForm() {
      this.errors = {}

      if (!this.form.address.trim()) {
        this.errors.address = 'Address is required'
      }

      if (!this.form.phone.trim()) {
        this.errors.phone = 'Phone number is required'
      } else if (!/^\d{11,15}$/.test(this.form.phone)) {
        this.errors.phone = 'Phone number must be 11-15 digits'
      }

      this.daysOfWeek.forEach((day) => {
        if (!this.form.opening_hours[day].trim()) {
          this.errors[`opening_hours.${day}`] = 'Required'
        }
      })

      return Object.keys(this.errors).length === 0
    },

    async handleSubmit() {
      if (!this.validateForm()) {
        return
      }

      this.submitting = true
      this.submitError = null

      try {
        let response

        if (this.isEdit) {
          response = await storesApi.updateStore(this.store.store_id, this.form)
        } else {
          response = await storesApi.createStore(this.form)
        }

        this.$emit('success', { ...response.data, isNew: !this.isEdit })
      } catch (error) {
        console.error('Submit error:', error)
        this.submitError =
          error.response?.data?.message || 'An error occurred while saving the store'
      } finally {
        this.submitting = false
      }
    },
  },
}
</script>
