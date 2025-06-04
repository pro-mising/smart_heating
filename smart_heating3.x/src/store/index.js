import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: null,
    token: null
  },
  getters: {
    isAuthenticated: state => !!state.token
  },
  mutations: {
    setUser(state, payload) {
      state.user = payload.user
      state.token = payload.token
    },
    clearAuth(state) {
      state.user = null
      state.token = null
    }
  },
  actions: {
    login({ commit }, credentials) {
  return new Promise((resolve) => {
    setTimeout(() => {
      commit('setUser', {
        user: { name: credentials.username },
        token: 'mock-token'
      })
      localStorage.setItem('token', 'mock-token')
      resolve()
    }, 500)
  })
},
    logout({ commit }) {
      commit('clearAuth')
      localStorage.removeItem('token')
    },
    initializeAuth({ commit }) {
      const token = localStorage.getItem('token')
      if (token) {
        commit('setUser', {
          user: { name: 'User' }, // 这里应该从API获取用户信息
          token: token
        })
      }
    }
  }
})