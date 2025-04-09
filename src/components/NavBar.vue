<template>
  <nav class="navbar">
    <div class="logo">
      <div class="logo-icon"></div>
      <div class="logo-text">智能供暖控制平台</div>
    </div>
    <div class="user-info">
      <div class="user-avatar">
        <img :src="require('@/assets/3.jpg')" alt="用户头像">
      </div>
      <div class="username">XXX工程师</div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavBar'
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
  z-index: 100;
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
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
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
  color: #ffffff;
}
</style>