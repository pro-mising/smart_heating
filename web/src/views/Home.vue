<template>
  <div class="home-page">
    <Navbar />
    <div class="main-content">
      <div class="hero-section">
        <h1 class="hero-title">智能供暖控制平台</h1>
        <p class="hero-subtitle">基于AI算法的智慧供暖管理系统，实时监控、精准预测、智能控制，提升供暖效率1000%</p>

        <div class="action-buttons">
          <div class="action-btn btn-monitor" @click="$router.push('/monitor')">
            <i class="fas fa-chart-line"></i>
            <h3>数据监控</h3>
            <p>实时监测供暖系统运行状态</p>
          </div>

          <div class="action-btn btn-predict" @click="$router.push('/forecast')">
            <i class="fas fa-brain"></i>
            <h3>数据预测</h3>
            <p>基于AI预测供暖需求变化</p>
          </div>

          <div class="action-btn btn-control" @click="$router.push('/control')">
            <i class="fas fa-sliders-h"></i>
            <h3>控制系统</h3>
            <p>远程智能调控供暖参数</p>
          </div>

          <div class="action-btn btn-user" @click="$router.push('/profile')">
            <i class="fas fa-user-cog"></i>
            <h3>用户中心</h3>
            <p>个性化设置与系统管理</p>
          </div>
        </div>

        <div class="tech-decoration">
          <div class="circle circle-1"></div>
          <div class="circle circle-2"></div>
          <div class="pulse-dot"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'

export default {
  name: 'HomeView',
  components: {
    Navbar
  }
}
</script>

<style scoped>
body {
  margin: 0;
  padding: 0;
  font-family: 'Arial', sans-serif;
  background: linear-gradient(135deg, #26354a 0%, #0c3065 100%);
  color: #ccd6f6;
  min-height: 100vh;
}

.main-content {
  padding-top: 100px;
  text-align: center;
}

.hero-section {
  position: relative;
  padding: 80px 20px;
  margin: 0 auto;
  max-width: 1200px;
  border-radius: 15px;
  overflow: hidden;
  background:
    linear-gradient(135deg, #0a192f 0%, #020c1b 100%),
    repeating-linear-gradient(45deg,
      rgba(100, 255, 218, 0.05) 0px,
      rgba(100, 255, 218, 0.05) 1px,
      transparent 1px,
      transparent 10px);
  box-shadow:
    0 0 0 1px rgba(100, 255, 218, 0.1),
    0 10px 30px rgba(0, 0, 0, 0.5);
}

.hero-title {
  color: white;
  font-size: 3.2rem;
  font-weight: 700;
  letter-spacing: 1.5px;
  margin-bottom: 20px;
  text-shadow:
    0 0 10px rgba(100, 255, 218, 0.3),
    0 2px 15px rgba(0, 0, 0, 0.5);
  position: relative;
  z-index: 2;
}

.hero-subtitle {
  color: rgba(255, 255, 255, 0.85);
  font-size: 1.2rem;
  max-width: 700px;
  margin: 0 auto 50px;
  line-height: 1.6;
  position: relative;
  z-index: 2;
}

.action-buttons {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  max-width: 900px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
}

.action-btn {
  padding: 30px 20px;
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
  position: relative;
  overflow: hidden;
  z-index: 1;
}

.action-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0) 100%);
  z-index: -1;
}

.action-btn:hover {
  transform: translateY(-5px) scale(1.02);
  box-shadow: 0 8px 25px rgba(0, 118, 255, 0.3);
}

.action-btn i {
  font-size: 40px;
  margin-bottom: 25px;
  display: block;
  color: white !important;
}

.action-btn h3 {
  font-size: 22px;
  margin-bottom: 10px;
  color: white !important;
}

.action-btn p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85) !important;
}

.btn-monitor {
  background: linear-gradient(135deg, #00d4ff 0%, #0072ff 100%);
}

.btn-predict {
  background: linear-gradient(135deg, #6e45e2 0%, #88d3ce 100%);
}

.btn-control {
  background: linear-gradient(135deg, #ff758c 0%, #ff7eb3 100%);
}

.btn-user {
  background: linear-gradient(135deg, #a8ff78 0%, #78ffd6 100%);
}

.tech-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(100, 255, 218, 0.1);
  border: 1px solid rgba(100, 255, 218, 0.2);
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -100px;
}

.circle-2 {
  width: 150px;
  height: 150px;
  bottom: -50px;
  left: -50px;
}

.pulse-dot {
  position: absolute;
  width: 10px;
  height: 10px;
  background: #64ffda;
  border-radius: 50%;
  top: 30%;
  left: 20%;
  animation: pulse 3s infinite;
  box-shadow: 0 0 0 0 rgba(100, 255, 218, 0.7);
}

@keyframes pulse {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(100, 255, 218, 0.7);
  }

  70% {
    transform: scale(1.2);
    box-shadow: 0 0 0 15px rgba(100, 255, 218, 0);
  }

  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(100, 255, 218, 0);
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 2.2rem;
  }

  .hero-subtitle {
    font-size: 1rem;
    padding: 0 20px;
  }

  .action-buttons {
    grid-template-columns: 1fr;
  }
}
</style>