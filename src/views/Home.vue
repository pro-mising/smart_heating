<template>
  <div class="home">
    <NavBar />
    <router-view></router-view>
    <div class="main-content">
      <HeroSection @button-click="handleButtonClick" />
    </div>
  </div>
</template>

<script>
import NavBar from '../components/NavBar.vue'
import HeroSection from '../components/HeroSection.vue'

export default {
  name: 'App',
  components: {
    NavBar,
    HeroSection
  },
  methods: {
    handleButtonClick(buttonType) {
      console.log(`进入${buttonType}模块`)
      switch (buttonType) {
        case 'monitor':
          this.$router.push('/monitor')
          break
      }
    }
  }
}
</script>

<style>
/* ===== 智能供暖系统全局样式 ===== */
/* 1. 深色主题背景 */
#app {
  background: linear-gradient(135deg,
      #26354a 0%,
      #0c3065 100%);
  min-height: 100vh;
  color: #ccd6f6;
}

/* 2. 主内容区域 */
.main-content {
  padding-top: 100px;
  text-align: center;
}

/* 3. 大标题样式 */
.hero-title {
  color: white !important;
  font-size: 3.2rem;
  font-weight: 700;
  letter-spacing: 1.5px;
  margin-bottom: 20px;
  text-shadow:
    0 0 10px rgba(100, 255, 218, 0.3),
    0 2px 15px rgba(0, 0, 0, 0.5);
}

/* 4. 副标题样式 */
.hero-subtitle {
  color: rgba(255, 255, 255, 0.85) !important;
  font-size: 1.2rem;
  max-width: 700px;
  margin: 0 auto 40px;
  line-height: 1.6;
}

/* 5. 导航栏适配 */
.navbar {
  background-color: rgba(10, 25, 47, 0.95) !important;
  backdrop-filter: blur(5px);
}

/* 6. 操作按钮悬浮效果 */
.action-btn:hover {
  transform: translateY(-5px) scale(1.02);
  box-shadow: 0 8px 25px rgba(0, 118, 255, 0.3) !important;
}

/* 7. 温度计特殊样式 */
.temperature-display {
  background: rgba(16, 42, 87, 0.6);
  border: 1px solid #1f3a68;
  border-radius: 12px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1rem;
    padding: 0 20px;
  }
}
</style>