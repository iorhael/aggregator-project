<template>
  <header class="bg-white shadow-sm border-b sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        <div class="flex items-center">
          <router-link to="/" class="flex items-center space-x-3">
            <img :src="logoUrl" alt="Price Aggregator Logo" class="h-8 w-30 object-contain" />
          </router-link>
        </div>

        <nav class="hidden md:flex items-center space-x-8">
          <router-link
            to="/"
            class="text-gray-700 hover:text-gray-900 font-medium transition-colors"
            :class="{ 'text-blue-600 font-semibold': $route.name === 'Home' }"
          >
            Home
          </router-link>

          <router-link
            v-if="hasRetailerRole"
            to="/retailer"
            class="text-gray-700 hover:text-gray-900 font-medium transition-colors"
            :class="{ 'text-blue-600 font-semibold': $route.name === 'Retailer' }"
          >
            Organization Info
          </router-link>

          <router-link
            to="/search"
            class="text-gray-700 hover:text-gray-900 font-medium transition-colors"
          >
            Search
          </router-link>
          <router-link
            to="/about"
            class="text-gray-700 hover:text-gray-900 font-medium transition-colors"
          >
            About
          </router-link>
        </nav>

        <div class="flex items-center space-x-4">
          <div v-if="isAuthenticated" class="flex items-center space-x-4">
            <div class="flex items-center space-x-2" @click="profile">
              <div class="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center">
                <span class="text-white text-sm font-medium">
                  {{ userInitials }}
                </span>
              </div>
              <span class="text-gray-700 font-medium hidden sm:block">
                {{ userName }}
              </span>
            </div>

            <button
              @click="logout"
              class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
            >
              Logout
            </button>
          </div>

          <button
            v-else
            @click="login"
            class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
          >
            Sign In
          </button>

          <button
            @click="toggleMobileMenu"
            class="md:hidden p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100"
          >
            <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
          </button>
        </div>
      </div>

      <div v-if="showMobileMenu" class="md:hidden py-4 border-t">
        <div class="flex flex-col space-y-2">
          <router-link
            to="/"
            class="text-gray-700 hover:text-gray-900 font-medium py-2 px-4 rounded-md transition-colors"
            :class="{ 'bg-blue-50 text-blue-600': $route.name === 'Home' }"
            @click="closeMobileMenu"
          >
            Home
          </router-link>

          <router-link
            v-if="hasRetailerRole"
            to="/retailer"
            class="text-gray-700 hover:text-gray-900 font-medium py-2 px-4 rounded-md transition-colors"
            :class="{ 'bg-blue-50 text-blue-600': $route.name === 'Retailer' }"
            @click="closeMobileMenu"
          >
            Organization Info
          </router-link>

          <a
            href="#"
            class="text-gray-700 hover:text-gray-900 font-medium py-2 px-4 rounded-md transition-colors"
          >
            Categories
          </a>
          <a
            href="#"
            class="text-gray-700 hover:text-gray-900 font-medium py-2 px-4 rounded-md transition-colors"
          >
            About
          </a>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import keycloak from '@/services/keycloak'
import Roles from '@/constants/roles'
import logoImage from '@/assets/aggregator-logo.png'

export default {
  name: 'AppHeader',
  data() {
    return {
      showMobileMenu: false,
      logoUrl: logoImage,
    }
  },
  computed: {
    isAuthenticated() {
      return keycloak.authenticated
    },
    userName() {
      return keycloak.tokenParsed?.preferred_username || keycloak.tokenParsed?.name || 'User'
    },
    userInitials() {
      const name = this.userName
      return name
        .split(' ')
        .map((n) => n[0])
        .join('')
        .toUpperCase()
        .slice(0, 2)
    },
    hasRetailerRole() {
      if (!this.isAuthenticated) return false
      const userRoles = keycloak.tokenParsed?.resource_access?.['aggregator-app']?.roles || []
      return userRoles.includes(Roles.RETAILER)
    },
  },
  methods: {
    login() {
      keycloak.login({ redirectUri: window.location.origin })
    },
    profile() {
      keycloak.accountManagement()
    },
    logout() {
      keycloak.logout()
    },
    toggleMobileMenu() {
      this.showMobileMenu = !this.showMobileMenu
    },
    closeMobileMenu() {
      this.showMobileMenu = false
    },
  },
}
</script>
