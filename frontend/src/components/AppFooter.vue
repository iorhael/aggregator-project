<template>
  <footer class="bg-gray-900 text-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
        <!-- Brand Section -->
        <div class="col-span-1 md:col-span-2">
          <div class="flex items-center space-x-3 mb-4">
            <img
              :src="logoUrl"
              alt="Price Aggregator Logo"
              class="h-8 w-30 object-contain"
              @error="handleLogoError"
            />
          </div>
          <p class="text-gray-300 mb-4 max-w-md">
            Your ultimate destination for finding the best products and deals. Compare prices, read
            reviews, and make informed purchasing decisions.
          </p>
          <div class="flex space-x-4">
            <a
              :href="githubUrl"
              target="_blank"
              rel="noopener noreferrer"
              class="text-gray-300 hover:text-white transition-colors"
              aria-label="GitHub Repository"
            >
              <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
                <path
                  d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"
                />
              </svg>
            </a>
          </div>
        </div>

        <!-- Quick Links -->
        <div>
          <h4 class="text-lg font-semibold mb-4">Quick Links</h4>
          <ul class="space-y-2">
            <li>
              <router-link to="/" class="text-gray-300 hover:text-white transition-colors">
                Home
              </router-link>
            </li>
            <li v-if="hasRetailerRole">
              <router-link to="/retailer" class="text-gray-300 hover:text-white transition-colors">
                Organization Info
              </router-link>
            </li>
            <li>
              <a href="#" class="text-gray-300 hover:text-white transition-colors">Categories</a>
            </li>
            <li>
              <a href="#" class="text-gray-300 hover:text-white transition-colors">About Us</a>
            </li>
          </ul>
        </div>

        <!-- Support -->
        <div>
          <h4 class="text-lg font-semibold mb-4">Support</h4>
          <ul class="space-y-2">
            <li>
              <a href="#" class="text-gray-300 hover:text-white transition-colors">Help Center</a>
            </li>
            <li>
              <a href="#" class="text-gray-300 hover:text-white transition-colors">Contact Us</a>
            </li>
            <li>
              <a href="#" class="text-gray-300 hover:text-white transition-colors"
                >Privacy Policy</a
              >
            </li>
            <li>
              <a href="#" class="text-gray-300 hover:text-white transition-colors"
                >Terms of Service</a
              >
            </li>
          </ul>
        </div>
      </div>

      <!-- Bottom Section -->
      <div
        class="border-t border-gray-800 mt-8 pt-8 flex flex-col md:flex-row justify-between items-center"
      >
        <p class="text-gray-400 text-sm">
          © {{ currentYear }} Price Aggregator. All rights reserved.
        </p>
        <div class="flex items-center space-x-4 mt-4 md:mt-0">
          <span class="text-gray-400 text-sm">Built with ❤️ by</span
          ><a
            :href="githubUrl"
            target="_blank"
            rel="noopener noreferrer"
            class="text-blue-400 hover:text-blue-300 transition-colors text-sm font-medium"
          >
            {{ githubUsername }}
          </a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
import keycloak from '@/services/keycloak'
import Roles from '@/constants/roles'
import logoImage from '@/assets/aggregator-logo.png'

export default {
  name: 'AppFooter',
  props: {
    githubUrl: {
      type: String,
      required: true,
    },
    githubUsername: {
      type: String,
      default: 'https://github.com/iorhael',
    },
  },
  data() {
    return {
      logoUrl: logoImage,
    }
  },
  computed: {
    currentYear() {
      return new Date().getFullYear()
    },
    hasRetailerRole() {
      if (!keycloak.authenticated) return false
      const userRoles = keycloak.tokenParsed?.resource_access?.['aggregator-app']?.roles || []
      return userRoles.includes(Roles.RETAILER)
    },
  },
}
</script>
