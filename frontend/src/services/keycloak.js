import Keycloak from 'keycloak-js'

const options = {
  url: import.meta.env.VITE_KC_URL,
  realm: import.meta.env.VITE_KC_REALM,
  clientId: import.meta.env.VITE_KC_CLIENT_ID,
}

const keycloak = new Keycloak(options)

export default keycloak
