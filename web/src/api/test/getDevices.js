import axios from 'axios';
import store from '../../store/index.js'; // or '../../store/index.js'

// 创建 axios 实例
const service = axios.create();

// 你封装的 getDevices 方法
function getDevices(page, address) {
    return service.get('http://127.0.0.1:3002/heatdata/getlist', {
        params: {
            page: page,
            address: address
        },
        headers: {
            Authorization: "Bearer " + store.state.token,
        }  //验证方式是 "Bearer " + 用户的token， 注意Bearer 后面有个空格
    });
}

// 测试命令 node src/api/test/getDevices

// 测试函数
async function runTest() {
    const page = 1;
    const address = ""; // 可替换为你的真实地址字段值

    try {
        const response = await getDevices(page, address);
        const { heatdata, heatdata_count } = response.data;

        console.log('✅ 请求成功');
        console.log('🔥 heatdata:', heatdata);
        console.log('📊 heatdata_count:', heatdata_count);

        if (Array.isArray(heatdata) && typeof heatdata_count === 'number') {
            console.log('✅ 数据格式正确');
        } else {
            console.error('❌ 返回数据格式不符合预期');
        }
    } catch (error) {
        console.error('❌ 请求失败:', error.message);
    }
}

runTest();
