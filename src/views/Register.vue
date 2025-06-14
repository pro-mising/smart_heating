<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="logo">
        <h1>用户注册</h1>
        <p>创建您的智能供暖系统账号</p>
      </div>
      
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            type="text" 
            id="username"
            v-model="form.username" 
            class="form-control"
            placeholder="请输入用户名"
            required
          >
        </div>

        <div class="form-group">
          <label for="name">真实姓名</label>
          <input 
            type="text" 
            id="name"
            v-model="form.name" 
            class="form-control"
            placeholder="请输入姓名"
            required
          >
        </div>
        
        <div class="form-group">
          <label for="email">电子邮箱</label>
          <input 
            type="email" 
            id="email"
            v-model="form.email" 
            class="form-control"
            placeholder="请输入邮箱"
            required
          >
        </div>

        <div class="form-group">
          <label for="phone">联系电话</label>
          <input 
            type="phone" 
            id="phone"
            v-model="form.phone" 
            class="form-control"
            placeholder="请输入联系电话"
            required
          >
        </div>

        <div class="form-group">
          <label for="work">所属部门</label>
          <input 
            type="work" 
            id="work"
            v-model="form.work" 
            class="form-control"
            placeholder="请输入所属部门"
            required
          >
        </div>
        
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password"
            v-model="form.password" 
            class="form-control"
            placeholder="请输入密码"
            required
          >
        </div>
        
        <div class="form-group">
          <label for="confirmPassword">确认密码</label>
          <input 
            type="password" 
            id="confirmPassword"
            v-model="form.confirmPassword" 
            class="form-control"
            placeholder="请再次输入密码"
            required
          >
        </div>
        
        <div class="form-group">
          <button 
            type="submit" 
            class="btn"
            :disabled="isRegistering"
          >
            {{ isRegistering ? '注册中...' : '立即注册' }}
          </button>
        </div>
      </form>
      
      <div class="footer-links">
        <router-link to="/login">已有账号？立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RegisterPage',
  data() {
    return {
      form: {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      },
      isRegistering: false
    }
  },
  methods: {
    async handleRegister() {
      if (this.form.password !== this.form.confirmPassword) {
        alert('两次输入的密码不一致！')
        return
      }
      
      this.isRegistering = true
      
      try {
        // 注册过程
        await new Promise(resolve => setTimeout(resolve, 1000))
        alert('注册成功！请登录')
        this.$router.push('/login')
      } catch (error) {
        alert('注册失败：' + error.message)
      } finally {
        this.isRegistering = false
      }
    }
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #0a192f;
}

/*认证卡片设计*/
.auth-card {
  width: 400px;
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(100, 255, 218, 0.1);
}

.logo {
  text-align: center;
  margin-bottom: 30px;
}

.logo h1 {
  background: linear-gradient(90deg, #00d4ff, #0072ff);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  font-size: 28px;
  margin-bottom: 10px;
}

.logo p {
  color: rgba(204, 214, 246, 0.7);
  font-size: 14px;
}

/*表单样式*/
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #00d4ff;
  font-size: 14px;
}

.form-control {
  width: 100%;
  padding: 12px 15px;
  background: rgba(10, 25, 47, 0.5);
  border: 1px solid #1f3a68;
  border-radius: 6px;
  color: #ccd6f6;
  font-size: 16px;
  transition: all 0.3s;
}

.form-control:focus {
  outline: none;
  border-color: #00d4ff;
  box-shadow: 0 0 0 2px rgba(0, 212, 255, 0.2);
}

/*按钮样式*/
.btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #00d4ff 0%, #0072ff 100%);
  border: none;
  border-radius: 6px;
  color: white;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 212, 255, 0.4);
}

.btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/*页脚链接样式*/
.footer-links {
  margin-top: 25px;
  text-align: center;
  font-size: 14px;
}

.footer-links a {
  color: rgba(204, 214, 246, 0.7);
  text-decoration: none;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #00d4ff;
}
</style>