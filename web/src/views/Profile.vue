<template>
  <div>
    <Navbar />
    <div class="container">
      <div class="profile-container">
        <div class="profile-sidebar">
          <div class="avatar">
            <img :src="require('@/assets/3.jpg')" alt="用户头像">
          </div>
          <div class="user-name">{{ username }}</div>
          <div class="user-title">系统管理员</div>

          <div class="stats">
            <div class="stat-item">
              <div class="stat-value">0</div>
              <div class="stat-label">操作记录</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">0</div>
              <div class="stat-label">设备管理</div>
            </div>
          </div>

          <div class="nav-menu">
            <div class="nav-item active">个人资料</div>
            <div class="nav-item">账号安全</div>
            <div class="nav-item">操作记录</div>
            <div class="nav-item">消息通知</div>
            <div class="nav-item"  @click="handleSystemMenuClick">系统管理</div>
          </div>
        </div>

        <div class="profile-content">
          <h2 class="section-title">基本信息</h2>

          <div class="form-group">
            <label>用户名</label>
            <input type="text" class="form-control" v-model="username" readonly>
          </div>

          <div class="form-group">
            <label>真实姓名</label>
            <input type="text" class="form-control" v-model="realName">
          </div>

          <div class="form-group">
            <label>电子邮箱</label>
            <input type="email" class="form-control" v-model="email">
          </div>

          <div class="form-group">
            <label>联系电话</label>
            <input type="tel" class="form-control" v-model="phone">
          </div>

          <div class="form-group">
            <label>所属部门</label>
            <select class="form-control" v-model="department">
              <option>技术研发部</option>
              <option>运维支持部</option>
              <option>项目管理部</option>
            </select>
          </div>

          <button class="btn" @click="saveChanges">保存更改</button>

          <h2 class="section-title" style="margin-top: 40px;">最近活动</h2>

          <div class="activity-list">
            <div class="activity-item" v-for="(activity, index) in activities" :key="index">
              <div class="activity-icon">{{ activity.icon }}</div>
              <div class="activity-content">
                <div class="activity-title">{{ activity.title }}</div>
                <div class="activity-time">{{ activity.time }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'

export default {
  name: 'UserProfile',
  components: {
    Navbar
  },
  data() {
    return {
      username: 'XXX工程师',
      realName: 'XXX',
      email: 'XXX@XXX.com',
      phone: '110',
      department: '技术研发部',
      activities: [
        { icon: '📊', title: '更新了供暖预测算法参数', time: '今天 25:61' },
        { icon: '🔧', title: '维修了第五中学3号供暖设备', time: '昨天 25:62' },
        { icon: '📝', title: '提交了月度能耗分析报告', time: '1998年13月32日' }
      ]
    }
  },
  methods: {
    saveChanges() {
      // 保存更改的逻辑
      alert('用户信息已保存')
    },
    handleSystemMenuClick() {
      if (!this.$store.getters.isAdmin) {
        this.$alert('抱歉，您不具备相应权限', '权限不足', {
          confirmButtonText: '确定'
        })
        return
      }
      this.$router.push('/admin/management')
    }
  }
}
</script>

<style scoped>
/* 保留原有的样式 */
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

.profile-container {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 30px;
}

.profile-sidebar {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 30px;
  border: 1px solid rgba(100, 255, 218, 0.1);
  text-align: center;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin: 0 auto 20px;
  overflow: hidden;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-name {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 5px;
}

.user-title {
  color: #00d4ff;
  font-size: 14px;
  margin-bottom: 20px;
}

.stats {
  display: flex;
  justify-content: space-around;
  margin: 25px 0;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: white;
}

.stat-label {
  font-size: 12px;
  color: rgba(204, 214, 246, 0.7);
}

.nav-menu {
  margin-top: 30px;
}

.nav-item {
  padding: 12px 0;
  border-bottom: 1px solid #1f3a68;
  cursor: pointer;
  transition: all 0.3s;
}

.nav-item:hover {
  color: #00d4ff;
}

.nav-item.active {
  color: #00d4ff;
  font-weight: bold;
}

.profile-content {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 30px;
  border: 1px solid rgba(100, 255, 218, 0.1);
}

.section-title {
  color: #00d4ff;
  font-size: 20px;
  margin-top: 0;
  margin-bottom: 25px;
  padding-bottom: 10px;
  border-bottom: 1px solid #1f3a68;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: rgba(204, 214, 246, 0.8);
}

.form-control {
  width: 100%;
  padding: 12px 15px;
  background: rgba(10, 25, 47, 0.5);
  border: 1px solid #1f3a68;
  border-radius: 6px;
  color: #ccd6f6;
  font-size: 16px;
}

.btn {
  padding: 12px 25px;
  background: linear-gradient(135deg, #00d4ff 0%, #0072ff 100%);
  border: none;
  border-radius: 6px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 212, 255, 0.4);
}

.activity-list {
  margin-top: 20px;
}

.activity-item {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #1f3a68;
}

.activity-icon {
  width: 40px;
  height: 40px;
  background: rgba(0, 212, 255, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  color: #00d4ff;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-weight: bold;
  margin-bottom: 5px;
}

.activity-time {
  color: rgba(204, 214, 246, 0.6);
  font-size: 12px;
}
</style>