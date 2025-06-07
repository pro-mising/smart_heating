import axios from 'axios';
import qs from 'qs';

const service = axios.create();

function register(username, realname, password, confirmPassword, email, phone, department) {
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
}
// 测试命令 node src/api/test/register

// 测试函数
async function runTest() {
    try {
        const response = await register('zxy', "赵星宇", 'pzxy', 'pzxy', '3071577715@qq.com', '18937293048', "技术研发部");
        console.log('✅ 测试成功');
        console.log('返回结果:', response.data);

        if (response.data.error_message === 'success') {
            console.log('✅ token 和 error_message 校验通过');
        } else {
            console.error('❌ 返回内容不符合预期');
        }
    } catch (error) {
        console.error('❌ 请求失败:', error.message);
    }
}

runTest();
