<template>
  <div class="min-h-screen flex flex-col">
    <AppHeader :logo-path="logoPath" />

    <main class="flex-1">
      <slot />
    </main>

    <AppFooter :logo-path="logoPath" :github-url="githubUrl" :github-username="githubUsername" />
  </div>
</template>

<script>
import AppHeader from './AppHeader.vue'
import AppFooter from './AppFooter.vue'

export default {
  name: 'AppLayout',
  components: {
    AppHeader,
    AppFooter,
  },
  props: {
    logoPath: {
      type: String,
      default: '/src/assets/logo.png',
    },
    githubUrl: {
      type: String,
      required: true,
    },
    githubUsername: {
      type: String,
      default: 'Your GitHub',
    },
  },
}
</script>
