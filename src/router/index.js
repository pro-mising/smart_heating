import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/views/Home.vue'
import DataMonitor from '@/views/DataMonitor.vue'
import NotFound from '../views/NotFound.vue'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: '/',
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: { title: '智能供暖控制平台' }
    },
    {
      path: '/monitor',
      name: 'monitor',
      component: DataMonitor,
      meta: { title: '数据监控' }
    },
    {
      path: '/404',
      name: 'notfound',
      component: NotFound,
      meta: { title: '页面未找到' }
    },
    {
      path: '*',
      redirect: '/404'
    }
  ]
})

// 添加路由守卫调试
router.beforeEach((to, from, next) => {
  console.log(`[路由] 从 ${from.path} 跳转到 ${to.path}`)
  document.title = to.meta.title || '智能供暖系统'
  next();
})

export default router