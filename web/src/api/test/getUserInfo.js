// src/api/test/getUserInfoTest.js
import store from '../../store/index.js'; // Vuex store，确保路径正确
import axios from 'axios';
const service = axios.create();


// 模拟前端发请求获取用户信息
function getUserInfo() {
    return service.get('http://127.0.0.1:3002/user/info', {
        headers: {
            Authorization: "Bearer " + store.state.token,
        }
    })
}

// 测试命令 node src/api/test/getUserInfo

// 测试逻辑
(async () => {
    try {
        console.log('▶ 正在发送 getUserInfo 请求...');
        const res = await getUserInfo();

        // 输出后端返回内容
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
