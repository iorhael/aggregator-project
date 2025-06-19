<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gray-50 text-center px-4">
    <h1 class="text-5xl font-bold text-red-600 mb-4">403</h1>
    <h2 class="text-2xl font-semibold text-gray-800 mb-2">Access Denied</h2>
    <p class="text-gray-600 mb-6">You do not have permission to view this page.</p>
    <router-link
      to="/"
      class="inline-block bg-blue-600 hover:bg-blue-700 text-white font-semibold px-6 py-2 rounded-lg transition"
    >
      Go to Home
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'ForbiddenPage',
}
</script>
