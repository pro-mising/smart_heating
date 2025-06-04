<template>
  <div>
    <Navbar />
    <div class="container">
      <router-link to="/control" class="back-btn">
        <i>←</i> 返回控制台
      </router-link>

      <div class="device-header">
        <h1 class="device-title">{{ device.name }}</h1>
        <span class="device-status" :class="{ 'offline': !device.online }">
          <i>●</i> {{ device.online ? '运行正常' : '离线' }}
        </span>
      </div>

      <div class="detail-grid">
        <div class="detail-card">
          <h2 class="card-title">
            <i>📌</i> 基本信息
          </h2>
          <div class="info-item" v-for="(value, key) in device.basicInfo" :key="key">
            <div class="info-label">{{ key }}</div>
            <div class="info-value">{{ value }}</div>
          </div>
        </div>

        <div class="detail-card">
          <h2 class="card-title">
            <i>⚙️</i> 技术参数
          </h2>
          <div class="info-item" v-for="(value, key) in device.techParams" :key="key">
            <div class="info-label">{{ key }}</div>
            <div class="info-value">{{ value }}</div>
          </div>
        </div>
      </div>

      <div class="detail-card">
        <h2 class="card-title">
          <i>📊</i> 运行状态趋势
        </h2>
        <div class="chart-container">
          温度与压力变化趋势图将在此显示
        </div>
      </div>

      <div class="action-btns">
        <button class="btn btn-primary">调整参数</button>
        <button class="btn btn-outline">维护记录</button>
        <button class="btn btn-outline">操作日志</button>
        <button class="btn btn-warning" @click="forceRestart">强制重启</button>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'

export default {
  components: {
    Navbar
  },
  data() {
    return {
      device: {
        name: '第五中学-主供暖机组',
        online: true,
        basicInfo: {
          '设备ID': '000000000000000',
          '安装位置': '教学楼机房',
          '设备型号': '1111111111',
          '序列号': '22222222222222',
          '安装日期': '2029年13月32日',
          '最后维护': '2029年15月33日'
        },
        techParams: {
          '额定功率': '0kW',
          '工作压力': '0MPa',
          '供水温度': '0°C',
          '回水温度': '0°C',
          '水流量': '0m³/h',
          '运行时长': '0小时'
        }
      }
    }
  },
  methods: {
    forceRestart() {
      if (confirm('确定要强制重启设备吗？')) {
        alert('设备重启命令已发送')
      }
    }
  }
}
</script>

<style scoped>
body {
  background-color: #0a192f;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #ccd6f6;
  margin: 0;
  padding-top: 90px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  color: #00d4ff;
  margin-bottom: 20px;
  text-decoration: none;
  font-size: 14px;
}

.back-btn i {
  margin-right: 5px;
}

.device-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.device-title {
  font-size: 24px;
  margin: 0;
  color: #00d4ff;
}

.device-status {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  background: rgba(76, 185, 68, 0.1);
  border-radius: 20px;
  color: #4cb944;
  font-size: 14px;
}

.device-status.offline {
  background: rgba(255, 107, 53, 0.1);
  color: #ff6b35;
}

.device-status i {
  margin-right: 5px;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
  margin-bottom: 30px;
}

.detail-card {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 25px;
  border: 1px solid rgba(100, 255, 218, 0.1);
}

.card-title {
  color: #00d4ff;
  font-size: 18px;
  margin-top: 0;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.card-title i {
  margin-right: 10px;
}

.info-item {
  display: flex;
  margin-bottom: 15px;
}

.info-label {
  width: 120px;
  color: rgba(204, 214, 246, 0.7);
}

.info-value {
  flex: 1;
  font-weight: 500;
}

.chart-container {
  height: 300px;
  background: rgba(10, 25, 47, 0.3);
  border-radius: 8px;
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(204, 214, 246, 0.5);
  border: 1px dashed #1f3a68;
}

.action-btns {
  display: flex;
  gap: 15px;
  margin-top: 30px;
}

.btn {
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: linear-gradient(135deg, #00d4ff 0%, #0072ff 100%);
  border: none;
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 212, 255, 0.4);
}

.btn-outline {
  background: transparent;
  border: 1px solid #00d4ff;
  color: #00d4ff;
}

.btn-outline:hover {
  background: rgba(0, 212, 255, 0.1);
}

.btn-warning {
  background: rgba(255, 107, 53, 0.1);
  border: 1px solid #ff6b35;
  color: #ff6b35;
}

.btn-warning:hover {
  background: rgba(255, 107, 53, 0.2);
}
</style>