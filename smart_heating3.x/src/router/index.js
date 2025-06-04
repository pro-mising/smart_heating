import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import DataMonitor from '../views/DataMonitor.vue'
import Forecast from '../views/Forecast.vue'
import Control from '../views/Control.vue'
import Profile from '../views/Profile.vue'
import AlertDetail from '../views/AlertDetail.vue'
import DeviceDetail from '../views/DeviceDetail.vue'
import store from '../store' 

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: { requiresAuth: true } 
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: { guest: true } 
    },
    {
      path: '/monitor',
      name: 'monitor',
      component: DataMonitor,
      meta: { requiresAuth: true }
    },
    {
      path: '/forecast',
      name: 'forecast',
      component: Forecast,
      meta: { requiresAuth: true }
    },
    {
      path: '/control',
      name: 'control',
      component: Control,
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      name: 'profile',
      component: Profile,
      meta: { requiresAuth: true }
    },
    {
      path: '/alert/:id',
      name: 'alert-detail',
      component: AlertDetail,
      meta: { requiresAuth: true }
    },
    {
      path: '/device/:id',
      name: 'device-detail',
      component: DeviceDetail,
      meta: { requiresAuth: true }
    }
  ]
})

// 全局路由守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = store.getters.isAuthenticated // 从Vuex获取认证状态
  
  // 如果路由需要认证且用户未登录
  if (to.matched.some(record => record.meta.requiresAuth) && !isAuthenticated) {
    next({ name: 'login' }) // 重定向到登录页
  } 
  // 如果用户已登录但尝试访问guest路由（如登录页）
  else if (to.matched.some(record => record.meta.guest) && isAuthenticated) {
    next({ name: 'home' }) // 重定向到首页
  }
  // 其他情况正常放行
  else {
    next()
  }
})

export default router