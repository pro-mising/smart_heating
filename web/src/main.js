import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import ElementUI from 'element-ui'
import Notifications from 'vue-notification'
import 'element-ui/lib/theme-chalk/index.css'
import '@/assets/global.css'

Vue.config.productionTip = false

// 初始化登录状态
store.dispatch('initializeAuth')

// axios 请求拦截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

Vue.prototype.$axios = axios

Vue.use(ElementUI)
Vue.use(Notifications)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')