import axios from 'axios'
import store from '@/store'

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
  //注册
  register(username, realname, password, confirmPassword, email, phone, department) {
    return service.post('http://127.0.0.1:3002/user/register', {
      username: username,
      realname: realname,
      password: password,
      confirmPassword: confirmPassword,
      email: email,
      phone: phone,
      department: department,
    },
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
  },

  // 登录
  login(username, password) {
    return service.post('http://127.0.0.1:3002/user/login', {
        username: username,
        password: password,
    }, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
  },


  // 获取用户信息
  getUserInfo() {
    return service.get('http://127.0.0.1:3002/user/info', {
      headers: {
        Authorization: "Bearer " + store.state.token,
      }
    })
  },


  // 更新用户信息
  updateUserInfo(username, realname, email, phone, department) {
    return service.post('http://127.0.0.1:3002/user/updateuser', {
      username: username,
      realname: realname,
      email: email,
      phone: phone,
      department: department,
    }, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Authorization: "Bearer " + store.state.token,
      }
    });
  },

  //管理员删除普通用户
  removeUser(username) {
    return service.post('http://127.0.0.1:3002/user/admin/removeuser', {
      username: username,
    }, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Authorization: "Bearer " + store.state.token,
      }
    });
  },

  //管理员增加普通用户
  addUser(username, realname, password, phone, email, department) {
    return service.post('http://127.0.0.1:3002/user/admin/adduser', {
      username: username,
      realname: realname,
      password: password,
      phone: phone,
      email: email,
      department: department,
    }, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Authorization: "Bearer " + store.state.token,
      }
    });
  },

  // 获取设备列表
  getDevices(page, address) {
    return service.get('http://127.0.0.1:3002/heatdata/getlist', {
      params: {
        page: page,
        address: address
      },
      headers: {
        Authorization: "Bearer " + store.state.token,
      }  //验证方式是 "Bearer " + 用户的token， 注意Bearer 后面有个空格
    });
  },

  // 获取预测数据  这个输入的time,只能是"未来1小时"，"未来7小时"，"未来24小时"
  getForecastData(address, time) {
    return service.get('http://127.0.0.1:3002/predict/get', {
      params: {
        address: address,
        time: time,
      },
      headers: {
        Authorization: "Bearer " + store.state.token,
      }  //验证方式是 "Bearer " + 用户的token， 注意Bearer 后面有个空格
    });
  },
}