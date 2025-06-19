import { createApp } from 'vue'
import '@/assets/main.css'
import App from './App.vue'
import router from './router'
import keycloak from './services/keycloak'

const app = createApp(App)

// 'check-sso' causes error message on logout
keycloak.init({ onLoad: 'check-sso' }).then(() => {
  app.use(router)
  app.mount('#app')
})
