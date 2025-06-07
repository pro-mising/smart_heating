<template>
  <div>
    <Navbar />
    <div class="container">
      <router-link to="/control" class="back-btn">
        <i>←</i> 返回控制台
      </router-link>

      <div class="alert-header">
        <h1 class="alert-title" :class="{'alert-critical': alert.level === 'critical', 'alert-warning': alert.level === 'warning'}">
          {{ alert.title }}
        </h1>
        <span class="alert-level" :class="{'level-critical': alert.level === 'critical', 'level-warning': alert.level === 'warning'}">
          <i>⚠️</i> {{ alert.level === 'critical' ? '严重告警' : '警告告警' }}
        </span>
      </div>

      <div class="alert-card">
        <div class="alert-info">
          <div class="info-item">
            <span class="info-label">告警时间</span>
            <span class="info-value">{{ alert.time }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">设备位置</span>
            <span class="info-value">{{ alert.location }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">设备ID</span>
            <span class="info-value">{{ alert.deviceId }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">告警类型</span>
            <span class="info-value">{{ alert.type }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">当前状态</span>
            <span class="info-value">{{ alert.status }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">负责人</span>
            <span class="info-value">{{ alert.responsible }}</span>
          </div>
        </div>

        <div class="alert-content">
          <h3 class="content-title">告警详情描述</h3>
          <p class="content-text">
            {{ alert.description }}
          </p>
        </div>

        <div class="alert-content">
          <h3 class="content-title">影响范围</h3>
          <p class="content-text">
            {{ alert.impact }}
          </p>
        </div>

        <div class="action-log">
          <h3 class="log-title">处理记录</h3>
          <div class="log-item" v-for="(log, index) in alert.logs" :key="index">
            <span class="log-time">{{ log.time }}</span>
            <span class="log-action">{{ log.action }}</span>
            <span class="log-user">{{ log.user }}</span>
          </div>
        </div>

        <div class="action-btns">
          <button class="btn btn-primary" @click="markAsResolved">标记为已处理</button>
          <button class="btn btn-outline">转交其他工程师</button>
          <button class="btn btn-outline">添加处理备注</button>
        </div>
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
      alert: {
        level: 'critical',
        title: '紧急: 3楼东侧管道压力异常',
        time: '2025-13-32 25:61:62',
        location: '富力小学教学楼3楼东侧',
        deviceId: '000000000000',
        type: '压力异常',
        status: '未处理',
        responsible: 'XXX工程师',
        description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        impact: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        logs: [
          { time: '25:61:62', action: '系统检测到压力异常，触发告警', user: '系统自动' },
          { time: '26:61:62', action: '已确认告警，正在前往检查', user: 'XXX工程师' }
        ]
      }
    }
  },
  methods: {
    markAsResolved() {
      this.alert.status = '已处理'
      this.alert.logs.push({
        time: new Date().toLocaleTimeString(),
        action: '已处理告警',
        user: 'XXX工程师'
      })
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

.alert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.alert-title {
  font-size: 24px;
  margin: 0;
}

.alert-critical {
  color: #e74c3c;
}

.alert-warning {
  color: #ff6b35;
}

.alert-level {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: bold;
}

.level-critical {
  background: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
}

.level-warning {
  background: rgba(255, 107, 53, 0.1);
  color: #ff6b35;
}

.alert-card {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 25px;
  border: 1px solid rgba(255, 107, 53, 0.3);
  margin-bottom: 30px;
}

.alert-info {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 25px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-label {
  color: rgba(204, 214, 246, 0.7);
  font-size: 14px;
  margin-bottom: 5px;
}

.info-value {
  font-weight: 500;
}

.alert-content {
  margin-bottom: 25px;
}

.content-title {
  color: #ff6b35;
  font-size: 18px;
  margin-bottom: 15px;
}

.content-text {
  line-height: 1.6;
}

.action-log {
  margin-top: 30px;
}

.log-title {
  color: #00d4ff;
  font-size: 18px;
  margin-bottom: 15px;
}

.log-item {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #1f3a68;
}

.log-time {
  width: 150px;
  color: rgba(204, 214, 246, 0.7);
}

.log-action {
  flex: 1;
}

.log-user {
  width: 120px;
  text-align: right;
}

.action-btns {
  display: flex;
  gap: 15px;
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
</style>