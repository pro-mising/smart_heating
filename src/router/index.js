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
import AdminManagement from '../views/AdminManagement.vue'

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
      path: '/admin/management',
      name: 'admin-management',
      component: AdminManagement,
      meta: { requiresAdmin: true } // 添加管理员权限的标识
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
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue'),
      meta: { guest: true } // 仅未登录可访问
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: () => import('../views/ForgotPassword.vue'),
      meta: { guest: true }
    }
  ]
})

// 全局路由守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = store.getters.isAuthenticated;
  const isAdmin = store.getters.isAdmin;

  if (to.name === 'login') {
    if (isAuthenticated) {
      return next({ name: 'home' });
    }
    return next();
  }

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      return next({
        name: 'login',
        query: { redirect: to.fullPath }
      });
    }

    if (to.matched.some(record => record.meta.requiresAdmin) && !isAdmin) {
      return next({ name: 'home' });
    }
  }

  // 默认放行
  next();
});

export default router