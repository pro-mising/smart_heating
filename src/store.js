// src/store.js
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        lastUpdateTime: null
    },
    mutations: {
        setLastUpdateTime(state, payload) {
            state.lastUpdateTime = payload
        }
    },
    actions: {
        updateLastUpdateTime({ commit }, payload) {
            commit('setLastUpdateTime', payload)
        }
    }
})

export default store
