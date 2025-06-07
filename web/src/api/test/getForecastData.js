import axios from 'axios';
import store from '../../store/index.js'; // or '../../store/index.js'

// 创建 axios 实例
const service = axios.create();

// 你封装的 getDevices 方法
function getForecastData(address, time) {
    return service.get('http://127.0.0.1:3002/predict/get', {
        params: {
            address: address,
            time: time,
        },
        headers: {
            Authorization: "Bearer " + store.state.token,
        }  //验证方式是 "Bearer " + 用户的token， 注意Bearer 后面有个空格
    });
}

// 测试命令 node src/api/test/getForecastData

// 测试函数
async function runTest() {
    try {
        const response = await getForecastData("富力小学", "未来1小时");
        const { heat_load } = response.data;

        console.log('✅ 请求成功');
        console.log(heat_load);

    } catch (error) {
        console.error('❌ 请求失败:', error.message);
    }
}

runTest();
