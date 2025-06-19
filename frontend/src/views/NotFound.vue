<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-white text-center px-4">
    <h1 class="text-6xl font-bold text-gray-800 mb-4">404</h1>
    <p class="text-xl text-gray-600 mb-6">Page not found</p>
    <router-link
      to="/"
      class="inline-block bg-blue-600 hover:bg-blue-700 text-white font-medium px-6 py-2 rounded-lg transition"
    >
      Go to Home
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'NotFoundPage',
}
</script>
