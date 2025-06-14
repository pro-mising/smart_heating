import Vue from 'vue'
import Vuex from 'vuex'
import api from '../api/index.js'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: '',
    user: null,
    role: ''
  },
  getters: {
    isAuthenticated: state => !!state.token,
    isAdmin: state => state.role === '1'
  },
  mutations: {
    setUser(state, { user, token, role }) {
      state.user = user
      state.token = token
      state.role = role
    },
    clearAuth(state) {
      state.user = null
      state.token = ''
      state.role = ''
    },
    SET_AUTH_TOKEN(state, token) {
      state.token = token
    },
    SET_USER_ROLE(state, role) {
      state.role = role
    }
  },
  actions: {
    async login({ commit }, credentials) {
      try {
        const response = await api.login(credentials.username, credentials.password);
        console.log('登录响应:', response);


        const token = response.token;
        const role = response.flag;
        const user = { username: credentials.username };

        commit('setUser', { user, token, role });

        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
        localStorage.setItem('role', role);
        localStorage.setItem('loginTime', Date.now().toString());
        localStorage.setItem('expireTime', (20 * 60 * 1000).toString());

        return Promise.resolve();
      } catch (error) {
        console.error('登录失败:', error);
        return Promise.reject(error);
      }
    },

    logout({ commit }) {
      commit('clearAuth');
      localStorage.clear();
    },

    // 初始化认证状态（页面刷新时调用）
    initializeAuth({ commit }) {
      const token = localStorage.getItem('token');
      const user = JSON.parse(localStorage.getItem('user'));
      const role = localStorage.getItem('role');
      const loginTime = parseInt(localStorage.getItem('loginTime'), 10);
      const expireTime = parseInt(localStorage.getItem('expireTime'), 10);
      const now = Date.now();

      console.log('初始化登录状态', { token, user, role, loginTime, expireTime, now });

      if (token && user && now - loginTime < expireTime) {
        commit('setUser', { user, token, role });
      } else {
        console.warn('登录信息过期或缺失，清除认证信息');
        commit('clearAuth');
        localStorage.clear();
      }
    }
  }
});
