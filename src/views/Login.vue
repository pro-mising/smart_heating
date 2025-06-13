<template>
  <div class="login-container">
    <div class="logo">
      <h1>智能供暖控制平台</h1>
      <p>基于AI算法的智慧供暖管理系统</p>
    </div>
    
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">用户名</label>
        <input 
          type="text" 
          id="username" 
          class="form-control" 
          v-model="form.username" 
          placeholder="请输入用户名" 
          required
        >
      </div>
      
      <div class="form-group">
        <label for="password">密码</label>
        <input 
          type="password" 
          id="password" 
          class="form-control" 
          v-model="form.password" 
          placeholder="请输入密码" 
          required
        >
      </div>
      
      <div class="form-group">
        <label for="captcha">验证码</label>
        <input 
          type="text" 
          id="captcha" 
          class="form-control" 
          v-model="form.captcha" 
          placeholder="请输入验证码" 
          required
        >
        <div class="captcha-container">
          <div class="captcha-image" id="captchaImage">{{ captchaText }}</div>
          <button type="button" class="refresh-btn" @click="updateCaptcha">↻</button>
        </div>
      </div>
      
      <div class="form-group">
        <button type="submit" class="btn" :disabled="isLogging">
          {{ isLogging ? '登录中...' : '登录系统' }}
        </button>
      </div>
    </form>
    
    <div class="footer-links">
      <router-link to="/forgot-password">忘记密码?</router-link>
      <router-link to="/register">注册新账号</router-link>
    </div>
  </div>
</template>

<script>
//import api from '@/api/index' 

export default {
  name: 'LoginPage',
  data() {
    return {
      form: {
        username: '',
        password: '',
        captcha: ''
      },
      captchaText: '',
      isLogging: false
    }
  },
  created() {
    this.updateCaptcha()
  },
  methods: {
    generateCaptcha() {
      const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
      let captcha = ''
      for (let i = 0; i < 4; i++) {
        captcha += chars.charAt(Math.floor(Math.random() * chars.length))
      }
      return captcha
    },
    
    updateCaptcha() {
      this.captchaText = this.generateCaptcha()
      this.addCaptchaLines()
    },
    
    addCaptchaLines() {
      const captchaImage = document.getElementById('captchaImage')
      if (captchaImage) {
        captchaImage.style.backgroundImage = `
          url('data:image/svg+xml;utf8,<svg width="100" height="40" xmlns="http://www.w3.org/2000/svg">
            ${Array.from({length: 5}, () => 
              `<path d="M${Math.random()*100},${Math.random()*40} L${Math.random()*100},${Math.random()*40}" 
              stroke="rgba(0,0,0,${0.1 + Math.random()*0.2})" stroke-width="1"/>`
            ).join('')}
          </svg>')
        `
      }
    },
    
  async handleLogin() {
  if (this.form.captcha.toUpperCase() !== this.captchaText) {
    alert('验证码错误！')
    this.updateCaptcha()
    return
  }

  this.isLogging = true

  try {
    // 通过 store 调用 login action，它会处理所有状态和存储
    await this.$store.dispatch('login', { 
      username: this.form.username, 
      password: this.form.password 
    });

    console.log("登录后 Vuex 中的 token：", this.$store.state.token);

    // 跳转
    const redirect = this.$route.query.redirect;
    this.$router.push(redirect ? decodeURIComponent(redirect) : { name: 'home' });

  } catch (error) {
    alert(error.message || '登录失败');
  } finally {
    this.isLogging = false;
  }
}

  }
}
</script>

<style scoped>
.login-container {
  width: 400px;
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(100, 255, 218, 0.1);
  position: relative;
  overflow: hidden;
   position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.login-container::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(0,212,255,0.1) 0%, transparent 70%);
  z-index: -1;
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

.captcha-container {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.captcha-image {
  flex: 1;
  height: 40px;
  background: #0a192f;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Courier New', monospace;
  font-weight: bold;
  letter-spacing: 3px;
  color: #333;
  background-image: url('data:image/svg+xml;utf8,<svg width="100" height="40" xmlns="http://www.w3.org/2000/svg"><path d="M0,0 L100,40 M100,0 L0,40" stroke="rgba(0,0,0,0.1)" stroke-width="1"/></svg>');
  background-size: 20px 20px;
  user-select: none;
}

.refresh-btn {
  width: 40px;
  background: rgba(10, 25, 47, 0.5);
  border: 1px solid #1f3a68;
  border-radius: 6px;
  color: #00d4ff;
  cursor: pointer;
  transition: all 0.3s;
}

.refresh-btn:hover {
  background: rgba(0, 212, 255, 0.1);
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
}

.footer-links {
  margin-top: 25px;
  text-align: center;
  font-size: 14px;
}

.footer-links a {
  color: rgba(204, 214, 246, 0.7);
  text-decoration: none;
  margin: 0 10px;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #00d4ff;
}
</style>