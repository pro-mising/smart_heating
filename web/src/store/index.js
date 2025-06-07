import Vue from 'vue'
import Vuex from 'vuex'
import api from '@/api/index.js'

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
  async login({ commit }, credentials) {
    try {
      // 真实的 API 请求
      const response = await api.login(credentials.username, credentials.password);
      
      // 假设 API 返回的 response 包含 user, token 和 role
      const user = response.user;
      const token = response.token;
      const role = response.role;

      const loginTime = Date.now();
      const expireTime = 20 * 60 * 1000; // 20分钟

      commit('setUser', { user, token, role });

      // 将 token 和用户信息存储到 localStorage
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(user));
      localStorage.setItem('role', role);
      localStorage.setItem('loginTime', loginTime.toString());
      localStorage.setItem('expireTime', expireTime.toString());

      return Promise.resolve();
    } catch (error) {
      console.error('登录失败:', error);
      return Promise.reject(error);
    }
  },
  // 退出登录
  logout({ commit }) {
    commit('clearAuth');
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('role');
    localStorage.removeItem('loginTime');
    localStorage.removeItem('expireTime');
  },
  // 初始化认证（检查 localStorage 中的登录信息）
  initializeAuth({ commit }) {
    const token = localStorage.getItem('token');
    const user = JSON.parse(localStorage.getItem('user'));
    const role = localStorage.getItem('role');
    const loginTime = parseInt(localStorage.getItem('loginTime'), 10);
    const expireTime = parseInt(localStorage.getItem('expireTime'), 10);
    const now = Date.now();

    if (token && user && loginTime && expireTime && now - loginTime < expireTime) {
      commit('setUser', { user, token, role });
    } else {
      commit('clearAuth');
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('role');
      localStorage.removeItem('loginTime');
      localStorage.removeItem('expireTime');
    }
  }
}

})

