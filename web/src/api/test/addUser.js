import store from '../../store/index.js'; // Vuex store，确保路径正确
import axios from 'axios';

const service = axios.create();

function addUser(username, realname, password, phone, email, department) {
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
}
// 测试命令 node src/api/test/adduser

// 测试函数
async function runTest() {
    try {
        const response = await addUser('zxy', "赵星宇", 'pzxy', '18937293048', '3071577715@qq.com', "技术研发部");
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
