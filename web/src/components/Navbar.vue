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
    <div class="user-info">
      <div class="user-avatar">
        <img :src="require('@/assets/3.jpg')" alt="用户头像">
      </div>
      <div class="username">{{ username }}</div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'AppNavbar',
  data() {
    return {
      username: 'XXX工程师'
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
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #112240;
  margin-right: 10px;
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
</style>