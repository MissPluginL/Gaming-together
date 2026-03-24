import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Home from '../views/Home.vue'
import GameList from '../views/GameList.vue'
import GameDetail from '../views/GameDetail.vue'
import GameForm from '../views/GameForm.vue'
import UserProfile from '../views/UserProfile.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: true }
  },
  {
    path: '/games',
    name: 'GameList',
    component: GameList,
    meta: { requiresAuth: true }
  },
  {
    path: '/games/:id',
    name: 'GameDetail',
    component: GameDetail,
    meta: { requiresAuth: true }
  },
  {
    path: '/games/add',
    name: 'GameAdd',
    component: GameForm,
    meta: { requiresAuth: true }
  },
  {
    path: '/games/edit/:id',
    name: 'GameEdit',
    component: GameForm,
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'UserProfile',
    component: UserProfile,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && token) {
    next('/home')
  } else {
    next()
  }
})

export default router
