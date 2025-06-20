import { createApp } from 'vue'
import '@/assets/main.css'
import App from './App.vue'
import router from './router'
import keycloak from './services/keycloak'
import { usersApi } from '@/api'

const app = createApp(App)

keycloak.onAuthSuccess = () => {
  usersApi.synchronizeUser()
}

keycloak.init().then(() => {
  app.use(router)
  app.mount('#app')
})
