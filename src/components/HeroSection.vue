<template>
  <div class="hero-section">
    <h1 class="hero-title">智能供暖控制平台</h1>
    <p class="hero-subtitle">基于AI算法的智慧供暖管理系统，实时监控、精准预测、智能控制，提升供暖效率1000%</p>
    
    <div class="action-buttons">
      <ActionButton
        v-for="button in buttons"
        :key="button.type"
        :type="button.type"
        :title="button.title"
        :description="button.description"
        :icon="button.icon"
        @click="handleButtonClick"
      />
    </div>
    
    <div class="tech-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="pulse-dot"></div>
    </div>
  </div>
</template>

<script>
import ActionButton from './ActionButton.vue'

export default {
  name: 'HeroSection',
  components: {
    ActionButton
  },
  data() {
    return {
      buttons: [
        { type: 'monitor', title: '数据监控', description: '实时监测供暖系统运行状态', icon: 'fa-chart-line' },
        { type: 'predict', title: '数据预测', description: '基于AI预测供暖需求变化', icon: 'fa-brain' },
        { type: 'control', title: '控制系统', description: '远程智能调控供暖参数', icon: 'fa-sliders-h' },
        { type: 'user', title: '用户中心', description: '个性化设置与系统管理', icon: 'fa-user-cog' }
      ]
    }
  },
  methods: {
    handleButtonClick(type) {
      this.$emit('button-click', type)
    }
  }
}
</script>

<style scoped>
/*智能供暖主视觉区域*/
.hero-section {
  position: relative;
  padding: 80px 20px;
  margin: 0 auto;
  max-width: 1200px;
  border-radius: 15px;
  overflow: hidden;
  
  background: 
    linear-gradient(135deg, #0a192f 0%, #020c1b 100%),
    repeating-linear-gradient(
      45deg,
      rgba(100, 255, 218, 0.05) 0px,
      rgba(100, 255, 218, 0.05) 1px,
      transparent 1px,
      transparent 10px
    );
  
  /* 边框光效 */
  box-shadow: 
    0 0 0 1px rgba(100, 255, 218, 0.1),
    0 10px 30px rgba(0, 0, 0, 0.5);
}

/* 主标题 */
.hero-title {
  color: white;
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 20px;
  position: relative;
  z-index: 2;
  text-align: center;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
}

/* 副标题 */
.hero-subtitle {
  color: rgba(255, 255, 255, 0.85);
  font-size: 1.2rem;
  max-width: 700px;
  margin: 0 auto 50px;
  line-height: 1.6;
  position: relative;
  z-index: 2;
}

/* 按钮容器 */
.action-buttons {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  max-width: 900px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
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
  0% { transform: scale(1); box-shadow: 0 0 0 0 rgba(100, 255, 218, 0.7); }
  70% { transform: scale(1.2); box-shadow: 0 0 0 15px rgba(100, 255, 218, 0); }
  100% { transform: scale(1); box-shadow: 0 0 0 0 rgba(100, 255, 218, 0); }
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