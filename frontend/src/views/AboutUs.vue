<template>
  <AppLayout
    logo-path="/src/assets/logo.png"
    github-url="https://github.com/iorhael"
    github-username="iorhael"
  >
    <div class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen">
      <div class="container mx-auto px-4 py-12">
        <div class="max-w-4xl mx-auto">
          <div class="text-center mb-12">
            <h1 class="text-4xl md:text-5xl font-bold text-gray-900 mb-4">About This Project</h1>
            <div class="w-24 h-1 bg-blue-600 mx-auto rounded-full"></div>
          </div>

          <div class="bg-white rounded-2xl shadow-xl overflow-hidden">
            <div
              class="bg-gradient-to-r from-blue-600 to-indigo-700 px-8 py-12 text-white text-center"
            >
              <div class="mb-6">
                <img
                  src="/src/assets/iorhael.png"
                  alt="Author Photo"
                  class="w-32 h-32 md:w-40 md:h-40 rounded-full mx-auto border-4 border-white shadow-lg object-cover"
                />
              </div>
              <h2 class="text-2xl md:text-3xl font-bold mb-2">Graduation Project</h2>
              <p class="text-blue-100 text-lg">4th Year Student • YKSUG University</p>
            </div>

            <div class="px-8 py-12">
              <div class="prose prose-lg max-w-none">
                <h3 class="text-2xl font-bold text-gray-900 mb-6 flex items-center">
                  <svg
                    class="w-8 h-8 text-blue-600 mr-3"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"
                    ></path>
                  </svg>
                  Product Data Aggregation Platform
                </h3>

                <div class="grid md:grid-cols-2 gap-8 mb-8">
                  <div>
                    <p class="text-gray-700 leading-relaxed mb-6">
                      This platform represents a comprehensive solution for modern e-commerce
                      challenges, designed to help consumers to discover and compare products
                      online. The system aggregates product data from multiple retailers, providing
                      users with a unified shopping experience.
                    </p>

                    <p class="text-gray-700 leading-relaxed">
                      Built with cutting-edge technologies, this project demonstrates advanced
                      software engineering principles, real-time data processing, and responsive
                      user interface design.
                    </p>
                  </div>

                  <div class="bg-gray-50 rounded-xl p-6">
                    <h4 class="font-semibold text-gray-900 mb-4">Key Technologies</h4>
                    <div class="flex flex-wrap gap-2">
                      <span class="px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm"
                        >Vue.js</span
                      >
                      <span class="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm"
                        >Spring Boot</span
                      >
                      <span class="px-3 py-1 bg-purple-100 text-purple-800 rounded-full text-sm"
                        >PostgreSQL</span
                      >
                      <span class="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-sm"
                        >MinIO</span
                      >
                      <span class="px-3 py-1 bg-red-100 text-red-800 rounded-full text-sm"
                        >Keycloak</span
                      >
                      <span class="px-3 py-1 bg-indigo-100 text-indigo-800 rounded-full text-sm"
                        >Docker</span
                      >
                    </div>
                  </div>
                </div>

                <div class="grid md:grid-cols-3 gap-6 mb-8">
                  <div class="text-center p-6 bg-blue-50 rounded-xl">
                    <div
                      class="w-12 h-12 bg-blue-600 rounded-lg mx-auto mb-4 flex items-center justify-center"
                    >
                      <svg
                        class="w-6 h-6 text-white"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M19 11H5m14-7H5a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2V6a2 2 0 00-2-2z"
                        ></path>
                      </svg>
                    </div>
                    <h4 class="font-semibold text-gray-900 mb-2">Data Aggregation</h4>
                    <p class="text-gray-600 text-sm">
                      Seamlessly collect product information from multiple retailers
                    </p>
                  </div>

                  <div class="text-center p-6 bg-green-50 rounded-xl">
                    <div
                      class="w-12 h-12 bg-green-600 rounded-lg mx-auto mb-4 flex items-center justify-center"
                    >
                      <svg
                        class="w-6 h-6 text-white"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2H9a2 2 0 01-2-2z"
                        ></path>
                      </svg>
                    </div>
                    <h4 class="font-semibold text-gray-900 mb-2">Smart Comparison</h4>
                    <p class="text-gray-600 text-sm">
                      Advanced filtering and comparison tools to help users make informed purchasing
                      decisions
                    </p>
                  </div>

                  <div class="text-center p-6 bg-purple-50 rounded-xl">
                    <div
                      class="w-12 h-12 bg-purple-600 rounded-lg mx-auto mb-4 flex items-center justify-center"
                    >
                      <svg
                        class="w-6 h-6 text-white"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"
                        ></path>
                      </svg>
                    </div>
                    <h4 class="font-semibold text-gray-900 mb-2">Price Dynamics</h4>
                    <p class="text-gray-600 text-sm">
                      Real-time price tracking and historical analysis to identify the best deals
                    </p>
                  </div>
                </div>

                <div class="bg-gradient-to-r from-indigo-50 to-blue-50 rounded-xl p-8 mb-8">
                  <h4 class="text-xl font-bold text-gray-900 mb-4">Project Objectives</h4>
                  <ul class="space-y-3 text-gray-700">
                    <li class="flex items-start">
                      <svg
                        class="w-5 h-5 text-green-500 mr-3 mt-0.5 flex-shrink-0"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                      >
                        <path
                          fill-rule="evenodd"
                          d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                          clip-rule="evenodd"
                        ></path>
                      </svg>
                      Demonstrate proficiency in full-stack web development using modern frameworks
                    </li>
                    <li class="flex items-start">
                      <svg
                        class="w-5 h-5 text-green-500 mr-3 mt-0.5 flex-shrink-0"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                      >
                        <path
                          fill-rule="evenodd"
                          d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                          clip-rule="evenodd"
                        ></path>
                      </svg>
                      Implement scalable architecture with proper security measures
                    </li>
                    <li class="flex items-start">
                      <svg
                        class="w-5 h-5 text-green-500 mr-3 mt-0.5 flex-shrink-0"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                      >
                        <path
                          fill-rule="evenodd"
                          d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                          clip-rule="evenodd"
                        ></path>
                      </svg>
                      Create an intuitive user experience
                    </li>
                    <li class="flex items-start">
                      <svg
                        class="w-5 h-5 text-green-500 mr-3 mt-0.5 flex-shrink-0"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                      >
                        <path
                          fill-rule="evenodd"
                          d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                          clip-rule="evenodd"
                        ></path>
                      </svg>
                      Apply advanced data processing techniques for real-time analytics
                    </li>
                  </ul>
                </div>

                <!-- Personal Message -->
                <div class="text-center bg-white border-2 border-gray-100 rounded-xl p-8">
                  <h4 class="text-xl font-bold text-gray-900 mb-4">A Personal Note</h4>
                  <p class="text-gray-700 leading-relaxed mb-6">
                    This project represents the culmination of my academic journey and passion for
                    technology. I hope this platform demonstrates not only technical skills but also
                    a deep understanding of user needs and modern software development practices.
                  </p>
                  <p class="text-gray-700 leading-relaxed mb-6">
                    Thank you for taking the time to explore this project. I wish you all the best
                    in your endeavors, and I hope you enjoyed this project.
                  </p>
                  <div class="flex items-center justify-center text-blue-600">
                    <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
                      ></path>
                    </svg>
                    <span class="font-medium">Best wishes from the developer</span>
                  </div>
                </div>
              </div>
            </div>

            <div class="bg-gray-50 px-8 py-8 border-t">
              <div class="text-center">
                <h3 class="text-xl font-bold text-gray-900 mb-4">Get In Touch</h3>
                <div class="flex items-center justify-center text-gray-600 mb-2">
                  <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M3 8l7.89 4.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"
                    ></path>
                  </svg>
                  <a href="mailto:student@yksug.edu" class="hover:text-blue-600 transition-colors">
                    paggregatorteam@gmail.com
                  </a>
                </div>
                <p class="text-sm text-gray-500">
                  Feel free to reach out for any questions or feedback about this project
                </p>
              </div>
            </div>
          </div>

          <!-- Footer Note -->
          <div class="text-center mt-8 text-gray-500">
            <p>&copy; 2025 Price Aggregator • Graduation Project</p>
          </div>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script>
import AppLayout from '@/components/AppLayout.vue'

export default {
  name: 'ContactUs',
  components: {
    AppLayout,
  },
}
</script>
