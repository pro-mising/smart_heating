<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="logo">
        <h1>找回密码</h1>
        <p>请输入您的注册邮箱重置密码</p>
      </div>
      
      <form @submit.prevent="handleReset">
        <div class="form-group">
          <label for="email">电子邮箱</label>
          <input 
            type="email" 
            id="email"
            v-model="form.email" 
            class="form-control"
            placeholder="请输入注册邮箱"
            required
          >
        </div>
        
        <div class="form-group">
          <button 
            type="submit" 
            class="btn"
            :disabled="isSubmitting"
          >
            {{ isSubmitting ? '发送中...' : '发送重置邮件' }}
          </button>
        </div>
      </form>
      
      <div class="footer-links">
        <router-link to="/login">返回登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ForgotPasswordPage',
  data() {
    return {
      form: {
        email: ''
      },
      isSubmitting: false
    }
  },
  methods: {
    async handleReset() {
      this.isSubmitting = true
      
      try {
        // 模拟发送过程
        await new Promise(resolve => setTimeout(resolve, 1000))
        alert('重置邮件已发送，请查收邮箱')
        this.$router.push('/login')
      } catch (error) {
        alert('发送失败：' + error.message)
      } finally {
        this.isSubmitting = false
      }
    }
  }
}
</script>

<style scoped>
/* 复用注册页面的相同样式 */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #0a192f;
}

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