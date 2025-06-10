<template>
  <nav class="navbar">
    <div class="logo">
      <div class="logo-icon"></div>
      <router-link to="/" class="logo-text">智能供暖控制平台</router-link>
    </div>
    <div class="nav-links">
      <router-link to="/monitor">数据监控</router-link>
      <router-link to="/forecast">数据预测</router-link>
      <router-link to="/control">控制系统</router-link>
      <router-link to="/profile">用户中心</router-link>
    </div>
    <div class="user-info" @click="toggleDropdown">
      <div class="user-avatar">
        <img :src="require('@/assets/3.jpg')" alt="用户头像">
      </div>
      
      <!-- 下拉菜单 -->
      <transition name="dropdown">
        <div class="dropdown-menu" v-show="showDropdown">
          <div class="dropdown-item" @click="logout">
            <i class="el-icon-switch-button"></i> 退出登录
          </div>
        </div>
      </transition>
    </div>
  </nav>
</template>

<script>

export default {
  name: 'AppNavbar',
  data() {
    return {
      username: '',
      showDropdown: false
    }
  },
  mounted() {
    document.addEventListener('click', this.closeDropdown)
  },
  beforeDestroy() {
    document.removeEventListener('click', this.closeDropdown)
  },
  methods: {
    
    toggleDropdown(e) {
      e.stopPropagation() // 阻止事件冒泡
      this.showDropdown = !this.showDropdown
    },
    
    closeDropdown(e) {
      if (!this.$el.contains(e.target)) {
        this.showDropdown = false
      }
    },
    async logout() {
  try {
    await this.$store.dispatch('logout') 
    this.$router.push('/login')
    this.$message.success('已安全退出')
  } catch (error) {
    console.error('退出登录失败:', error)
    this.$message.error('退出登录失败')
  }
}

  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 50px;
  background-color: rgba(10, 25, 47, 0.9);
  position: fixed;
  width: 100%;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.logo {
  display: flex;
  align-items: center;
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #00d4ff 0%, #0072ff 100%);
  border-radius: 8px;
  margin-right: 15px;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  background: linear-gradient(90deg, #00d4ff, #0072ff);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 30px;
}

.nav-links a {
  color: #ccd6f6;
  text-decoration: none;
  font-size: 16px;
  transition: all 0.3s;
  position: relative;
}

.nav-links a:hover {
  color: #00d4ff;
}

.nav-links a.router-link-exact-active {
  color: #00d4ff;
}

.nav-links a.router-link-exact-active::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 100%;
  height: 2px;
  background: #00d4ff;
}

.user-info {
  display: flex;
  align-items: center;
  position: relative;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 20px;
  transition: all 0.3s;
}

.user-info:hover {
  background-color: rgba(100, 255, 218, 0.1);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #112240;
  margin-right: 60px;
  overflow: hidden;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.username {
  font-size: 16px;
  color: white;
}

/* 下拉菜单样式 */
.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: #112240;
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
  min-width: 160px;
  z-index: 1001;
  margin-top: 5px;
  overflow: hidden;
}

.dropdown-item {
  padding: 12px 20px;
  color: #ccd6f6;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: all 0.3s;
}

.dropdown-item i {
  margin-right: 8px;
  font-size: 16px;
}

.dropdown-item:hover {
  background-color: rgba(100, 255, 218, 0.1);
  color: #64ffda;
}

/* 下拉动画 */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.3s;
}

.dropdown-enter,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>