import store from '../../store/index.js'; // Vuex store，确保路径正确
import axios from 'axios';

const service = axios.create();

function updateUserInfo(username, realname, email, phone, department) {
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
}
// 测试命令 node src/api/test/updateUserInfo

// 测试函数
async function runTest() {
    try {
        const response = await updateUserInfo('nst', "倪小婷", '1017815532@qq.com', '13507429302', "技术研发部");
        console.log('✅ 测试成功');
        console.log('返回结果:', response.data);

        if (response.data.error_message === 'success') {
            console.log('✅ error_message 校验通过');
        } else {
            console.error('❌ 返回内容不符合预期');
        }
    } catch (error) {
        console.error('❌ 请求失败:', error.message);
    }
}

runTest();
