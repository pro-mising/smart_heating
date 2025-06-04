import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || '/api',
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    return config
  },
  error => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 对响应数据做点什么
    return response.data
  },
  error => {
    // 对响应错误做点什么
    return Promise.reject(error)
  }
)

// API方法
export default {
  // 登录
  login(data) {
    return service.post('/auth/login', data)
  },

  // 获取设备列表
  getDevices() {
    return service.get('/devices')
  },

  // 获取告警列表
  getAlerts(params) {
    return service.get('/alerts', { params })
  },

  // 获取设备详情
  getDeviceDetail(id) {
    return service.get(`/devices/${id}`)
  },

  // 获取告警详情
  getAlertDetail(id) {
    return service.get(`/alerts/${id}`)
  },

  // 更新设备参数
  updateDevice(id, data) {
    return service.put(`/devices/${id}`, data)
  },

  // 获取用户信息
  getUserInfo() {
    return service.get('/user/info')
  },

  // 更新用户信息
  updateUserInfo(data) {
    return service.put('/user/info', data)
  },

  // 获取预测数据
  getForecastData(params) {
    return service.get('/forecast', { params })
  }
}