// src/api/test/getUserInfo.js
const store = require('../../store/index.js'); // 确保是 CommonJS 写法
const axios = require('axios');
const service = axios.create();

function getUserInfo() {
  return service.get('http://127.0.0.1:3002/user/info', {
    headers: {
      Authorization: "Bearer " + store.state.token,
    }
  });
}

(async () => {
  try {
    console.log('▶ 正在发送 getUserInfo 请求...');
    const res = await getUserInfo();

    const { error_message, ...rest } = res.data;

    if (error_message && error_message !== 'success') {
      console.warn('⚠️ 后端返回错误信息:', error_message);
    } else {
      console.log('✅ 请求成功，用户信息如下:');
      console.log(rest);
    }

  } catch (error) {
    console.error('❌ 请求失败:');
    if (error.response) {
      const { status, data } = error.response;
      console.error('状态码:', status);
      console.error('错误信息:', data.error_message || data.message || '未知错误');
    } else {
      console.error('网络错误:', error.message);
    }
  }
})();
