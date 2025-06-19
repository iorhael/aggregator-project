import { createRouter, createWebHistory } from 'vue-router'
import keycloak from '@/services/keycloak'
import Roles from '@/constants/roles'
import RetailerInfo from '@/views/retailer/RetailerInfo.vue'
import CardsManagmentPage from '@/views/retailer/CardsManagment.vue'
import StoresManagmentPage from '@/views/retailer/StoresManagment.vue'
import JobsHistoryPage from '@/views/retailer/JobsHistory.vue'
import HomePage from '@/views/Home.vue'
import NotFoundPage from '@/views/NotFound.vue'
import ForbiddenPage from '@/views/Forbidden.vue'
import ProductSearch from '@/views/ProductSearch.vue'
import ProductDetail from '@/views/ProductDetail.vue'
import ProductOffers from '@/views/ProductOffers.vue'
import ProductComments from '@/views/ProductComments.vue'
import RetailerInfoPublic from '@/views/RetailerInfo.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/retailer',
    name: 'Retailer',
    component: RetailerInfo,
    meta: {
      requiresAuth: true,
      availableFor: [Roles.RETAILER],
    },
  },
  {
    path: '/cards',
    name: 'CardsManagement',
    component: CardsManagmentPage,
    meta: {
      requiresAuth: true,
      availableFor: [Roles.RETAILER],
    },
  },
  {
    path: '/stores',
    name: 'StoresManagement',
    component: StoresManagmentPage,
    meta: {
      requiresAuth: true,
      availableFor: [Roles.RETAILER],
    },
  },
  {
    path: '/jobs',
    name: 'JobsHistory',
    component: JobsHistoryPage,
    meta: {
      requiresAuth: true,
      availableFor: [Roles.RETAILER],
    },
  },
  {
    path: '/forbidden',
    name: 'Forbidden',
    component: ForbiddenPage,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFoundPage,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: ProductDetail,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/search',
    name: 'ProductSearch',
    component: ProductSearch,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/search/:categoryId/:categoryName?',
    name: 'ProductSearchWithCategory',
    component: ProductSearch,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/product/:id/offers',
    name: 'ProductOffers',
    component: ProductOffers,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/product/:id/comments',
    name: 'ProductComments',
    component: ProductComments,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/retailer/:name',
    name: 'RetailerInfoPublic',
    component: RetailerInfoPublic,
    meta: {
      requiresAuth: false,
    },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// redirect to retailer page through /retailer causes Uncaught (in promise) {error: 'login_required', error_description: undefined}
router.beforeEach((to, from, next) => {
  if (to.meta?.requiresAuth) {
    const basePath = window.location.origin

    if (!keycloak.authenticated) {
      keycloak.login({ redirectUri: basePath + to.fullPath })
    }

    try {
      keycloak.updateToken(30)
      const availableFor = to.meta.availableFor

      if (Array.isArray(availableFor) && availableFor.length > 0) {
        const userRoles = keycloak.tokenParsed?.resource_access?.['aggregator-app']?.roles || []

        if (!availableFor.some((role) => userRoles?.includes(role))) {
          console.warn('Access denied')
          return next({ path: '/forbidden' })
        }
      }

      return next()
    } catch (err) {
      console.error('Error while updating token', err)
      return next(false)
    }
  } else {
    next()
  }
})

export default router
