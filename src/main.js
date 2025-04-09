import Vue from 'vue'
import App from './App.vue'
import '@fortawesome/fontawesome-free/css/all.min.css'
import '@/assets/styles.css'
import router from './router'
import store from './store' // ⬅️ 引入 Vuex store

Vue.config.productionTip = false

new Vue({
  router,
  store, // ⬅️ 挂载 Vuex store
  render: h => h(App),
}).$mount('#app')
