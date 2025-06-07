import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: null,
    token: null,
    role: null 
  },
  getters: {
    isAuthenticated: state => !!state.token,
    isAdmin: state => state.role === 'admin'
  },
  mutations: {
    setUser(state, payload) {
      state.user = payload.user
      state.token = payload.token
      state.role = payload.role 
    },
    clearAuth(state) {
      state.user = null
      state.token = null
      state.role = null
    }
  },
  actions: {
    login({ commit }, credentials) {
      return new Promise(resolve => {
        setTimeout(() => {
          const user = { name: credentials.username }
          const token = 'mock-token'
          const role = credentials.username === 'admin' ? 'admin' : 'user';  //如果用户名是admin，则是管理员
          const loginTime = Date.now()
          const expireTime = 20 * 60 * 1000 // 20分钟

          commit('setUser', { user, token, role }) 
          localStorage.setItem('token', token)
          localStorage.setItem('user', JSON.stringify(user))
          localStorage.setItem('role', role) 
          localStorage.setItem('loginTime', loginTime.toString())
          localStorage.setItem('expireTime', expireTime.toString())

          resolve()
        }, 500)
      })
    },
    logout({ commit }) {
      commit('clearAuth')
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('role')
      localStorage.removeItem('loginTime')
      localStorage.removeItem('expireTime')
    },
    initializeAuth({ commit }) {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      const role = localStorage.getItem('role')
      const loginTime = parseInt(localStorage.getItem('loginTime'), 10)
      const expireTime = parseInt(localStorage.getItem('expireTime'), 10)
      const now = Date.now()

      if (token && user && loginTime && expireTime && now - loginTime < expireTime) {
        commit('setUser', { user, token, role })
      } else {
        commit('clearAuth')
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        localStorage.removeItem('role')
        localStorage.removeItem('loginTime')
        localStorage.removeItem('expireTime')
      }
    }
  }
})

