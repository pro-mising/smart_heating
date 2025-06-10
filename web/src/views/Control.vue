<template>
  <div>
    <Navbar />
    <div class="container">
      <div class="control-dashboard">
        <div class="device-list">
          <h2 class="panel-title">
            设备列表
          </h2>

          <div class="search-box">
            <input type="text" placeholder="搜索设备..." v-model="searchQuery">
          </div>

          <div class="device-item" 
               v-for="device in filteredDevices" 
               :key="device.id"
               :class="{active: selectedDevice && device.id === selectedDevice.id}"
               @click="selectDevice(device)">
            <div class="device-name">{{ device.name }}</div>
            <div class="device-location">{{ device.location }}</div>
            <div class="device-status">
              <span>{{ device.online ? '在线' : '离线' }}</span>
              <span :class="device.online ? 'status-online' : 'status-offline'">
                {{ device.status }}
              </span>
            </div>
          </div>
        </div>

        <div class="control-panel" v-if="selectedDevice">
          <div class="control-header">
            <div>
              <h2 class="device-title">{{ selectedDevice.name }}</h2>
              <div class="device-id">设备ID: {{ selectedDevice.id }}</div>
            </div>
            <div>
              <button class="btn btn-outline" @click="goToDeviceDetail">设备详情</button>
            </div>
          </div>

          <div class="control-section">
            <h3 class="section-title">
              温度控制
            </h3>

            <div class="control-grid">
              <div class="control-card">
                <div class="control-name">供水温度</div>
                <div class="control-value temp-control">{{ supplyTemp }}°C</div>
                <div class="slider-container">
                  <input type="range" min="50" max="90" v-model="supplyTemp" class="slider">
                  <div class="slider-labels">
                    <span>50°C</span>
                    <span>90°C</span>
                  </div>
                </div>
              </div>

              <div class="control-card">
                <div class="control-name">回水温度</div>
                <div class="control-value temp-control">{{ returnTemp }}°C</div>
                <div class="slider-container">
                  <input type="range" min="30" max="70" v-model="returnTemp" class="slider">
                  <div class="slider-labels">
                    <span>30°C</span>
                    <span>70°C</span>
                  </div>
                </div>
              </div>

              <div class="control-card">
                <div class="control-name">室内温度</div>
                <div class="control-value temp-control">{{ roomTemp }}°C</div>
                <div class="control-actions">
                  <button class="btn btn-outline" @click="adjustRoomTemp(1)">+1°C</button>
                  <button class="btn btn-outline" @click="adjustRoomTemp(-1)">-1°C</button>
                  <button class="btn btn-primary" @click="applyTempSettings">应用</button>
                </div>
              </div>
            </div>
          </div>

          <div class="control-section">
            <h3 class="section-title">
              系统控制
            </h3>

            <div class="control-grid">
              <div class="control-card">
                <div class="control-name">系统压力</div>
                <div class="control-value pressure-control">{{ systemPressure }}MPa</div>
                <div class="mode-selector">
                  <button class="mode-btn" :class="{active: pressureMode === 'auto'}" @click="setPressureMode('auto')">自动</button>
                  <button class="mode-btn" :class="{active: pressureMode === 'manual'}" @click="setPressureMode('manual')">手动</button>
                </div>
              </div>

              <div class="control-card">
                <div class="control-name">水流量</div>
                <div class="control-value flow-control">{{ waterFlow }}m³/h</div>
                <div class="mode-selector">
                  <button class="mode-btn" :class="{active: flowMode === 'auto'}" @click="setFlowMode('auto')">自动</button>
                  <button class="mode-btn" :class="{active: flowMode === 'manual'}" @click="setFlowMode('manual')">手动</button>
                </div>
              </div>

              <div class="control-card">
                <div class="control-name">运行模式</div>
                <div class="mode-selector" style="margin-top: 10px;">
                  <button class="mode-btn" :class="{active: operationMode === 'eco'}" @click="setOperationMode('eco')">节能</button>
                  <button class="mode-btn" :class="{active: operationMode === 'standard'}" @click="setOperationMode('standard')">标准</button>
                  <button class="mode-btn" :class="{active: operationMode === 'power'}" @click="setOperationMode('power')">强力</button>
                </div>
                <div class="control-actions" style="margin-top: 15px;">
                  <button class="btn btn-primary" style="width: 100%;" @click="saveSettings">保存设置</button>
                </div>
              </div>
            </div>
          </div>

          <div class="alert-card" v-if="hasAlerts">
            <div class="alert-icon">⚠️</div>
            <div class="alert-content">
              <div class="alert-title">系统告警</div>
              <div class="alert-desc">{{ alertMessage }}</div>
            </div>
            <button class="btn btn-outline" @click="viewAlertDetails">查看详情</button>
          </div>

          <div class="chart-container">
            设备运行状态趋势图将在此显示
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
import api from '@/api'

export default {
  name: 'ControlPanel',
  components: {
    Navbar
  },
  data() {
    return {
      searchQuery: '',
      selectedDevice: null,
      devices: [
        {
          id: '00000000000',
          name: '富力小学-主供暖机组',
          location: '教学楼A栋地下室',
          online: true,
          status: '运行中'
        },
        {
          id: '00000000001',
          name: '富力小学-备用锅炉',
          location: '实验楼西侧',
          online: true,
          status: '待机'
        },
        {
          id: '00000000002',
          name: '富力小学-供暖机组',
          location: '主教学楼3层',
          online: false,
          status: '检修中'
        },
        {
        id: '00000000003',
        name: '交通小学-主供暖机组',
        location: '综合楼2楼',
        online: true,
        status: '运行中'
        },
        {
        id: '00000000004',
        name: '交通小学-备用锅炉',
        location: '实验楼南侧',
        online: true,
        status: '待机'
        },
        {
        id: '00000000005',
        name: '交通小学-供暖机组',
        location: '教学楼1层',
        online: true,
        status: '运行中'
        }
      ],
      supplyTemp: 72,
      returnTemp: 48,
      roomTemp: 22,
      systemPressure: 0.8,
      waterFlow: 12,
      pressureMode: 'manual',
      flowMode: 'auto',
      operationMode: 'standard',
      hasAlerts: true,
      alertMessage: '检测到某管道压力异常，建议立即检查'
    }
  },
  computed: {
  filteredDevices() {
    if (!this.searchQuery) {
      return this.devices;
    }
    return this.devices.filter(device => {
      return (
        device.name.includes(this.searchQuery) ||
        device.location.includes(this.searchQuery)
      );
    });
  },
},
  created() {
    // 默认选择第一个设备
    if (this.devices.length > 0) {
      this.selectedDevice = this.devices[0]
    }
  },
  methods: {
    selectDevice(device) {
      this.selectedDevice = device
      // 这里可以加载设备的当前设置
    },
    adjustRoomTemp(change) {
      this.roomTemp += change
      this.roomTemp = Math.max(16, Math.min(28, this.roomTemp))
    },
    setPressureMode(mode) {
      this.pressureMode = mode
    },
    setFlowMode(mode) {
      this.flowMode = mode
    },
    setOperationMode(mode) {
      this.operationMode = mode
    },
    async applyTempSettings() {
      try {
        const settings = {
          supplyTemp: this.supplyTemp,
          returnTemp: this.returnTemp,
          roomTemp: this.roomTemp
        }
        await api.updateDevice(this.selectedDevice.id, settings)
        this.$message.success('温度设置已应用')
      } catch (error) {
        console.error('应用温度设置失败:', error)
        this.$message.error('应用温度设置失败')
      }
    },
    async saveSettings() {
      try {
        const settings = {
          pressureMode: this.pressureMode,
          flowMode: this.flowMode,
          operationMode: this.operationMode
        }
        await api.updateDevice(this.selectedDevice.id, settings)
        this.$message.success('系统设置已保存')
      } catch (error) {
        console.error('保存系统设置失败:', error)
        this.$message.error('保存系统设置失败')
      }
    },
    goToDeviceDetail() {
      this.$router.push(`/device/${this.selectedDevice.id}`)
    },
    viewAlertDetails() {
      this.$router.push('/alert/1')
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.control-dashboard {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 25px;
}

.device-list {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(100, 255, 218, 0.1);
  height: fit-content;
}

.panel-title {
  color: var(--primary);
  font-size: 18px;
  margin-top: 0;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.panel-title i {
  margin-right: 10px;
}

.search-box {
  margin-bottom: 20px;
}

.search-box input {
  width: 80%;
  padding: 10px 15px;
  background: rgba(10, 25, 47, 0.5);
  border: 1px solid #1f3a68;
  border-radius: 6px;
  color: var(--light);
  font-size: 14px;
}

.device-item {
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s;
  background: rgba(10, 25, 47, 0.3);
  border-left: 3px solid transparent;
}

.device-item:hover {
  background: rgba(0, 212, 255, 0.1);
}

.device-item.active {
  border-left-color: var(--primary);
  background: rgba(0, 212, 255, 0.15);
}

.device-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.device-location {
  font-size: 12px;
  color: rgba(204, 214, 246, 0.7);
}

.device-status {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
  font-size: 12px;
}

.status-online {
  color: var(--success);
}

.status-offline {
  color: var(--warning);
}

.control-panel {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 25px;
  border: 1px solid rgba(100, 255, 218, 0.1);
}

.control-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.device-title {
  font-size: 22px;
  margin: 0;
}

.device-id {
  color: rgba(204, 214, 246, 0.7);
  font-size: 14px;
}

.control-section {
  margin-bottom: 30px;
}

.section-title {
  color: var(--primary);
  font-size: 16px;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.section-title i {
  margin-right: 10px;
}

.control-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.control-card {
  background: rgba(10, 25, 47, 0.5);
  border-radius: 8px;
  padding: 20px;
  border: 1px solid #1f3a68;
  transition: all 0.3s;
}

.control-card:hover {
  border-color: var(--primary);
}

.control-name {
  font-size: 14px;
  margin-bottom: 15px;
  color: rgba(204, 214, 246, 0.8);
}

.control-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 10px;
}

.temp-control {
  color: var(--primary);
}

.pressure-control {
  color: #a162e8;
}

.flow-control {
  color: #64ffda;
}

.control-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.btn {
  padding: 8px 15px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
  border: none;
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 212, 255, 0.4);
}

.btn-outline {
  background: transparent;
  border: 1px solid var(--primary);
  color: var(--primary);
}

.btn-outline:hover {
  background: rgba(0, 212, 255, 0.1);
}

.slider-container {
  margin-top: 15px;
}

.slider {
  width: 100%;
  height: 6px;
  -webkit-appearance: none;
  background: #1f3a68;
  border-radius: 3px;
  outline: none;
}

.slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 18px;
  height: 18px;
  background: var(--primary);
  border-radius: 50%;
  cursor: pointer;
}

.slider-labels {
  display: flex;
  justify-content: space-between;
  margin-top: 5px;
  font-size: 12px;
  color: rgba(204, 214, 246, 0.7);
}

.mode-selector {
  display: flex;
  border-radius: 6px;
  overflow: hidden;
  margin-top: 15px;
}

.mode-btn {
  flex: 1;
  padding: 10px;
  text-align: center;
  background: rgba(10, 25, 47, 0.5);
  border: none;
  color: rgba(204, 214, 246, 0.7);
  cursor: pointer;
  transition: all 0.3s;
}

.mode-btn.active {
  background: var(--primary);
  color: white;
  font-weight: bold;
}

.alert-card {
  background: rgba(231, 76, 60, 0.1);
  border: 1px solid rgba(231, 76, 60, 0.3);
  border-radius: 8px;
  padding: 20px;
  margin-top: 20px;
  display: flex;
  flex-direction: column; 
  align-items: center; 
  justify-content: space-between;
  text-align: center; 
}

.alert-icon {
  color: var(--warning);
  font-size: 40px; 
  margin-bottom: 15px; 
}

.alert-title {
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 10px; 
}

.alert-desc {
  font-size: 14px;
  color: rgba(204, 214, 246, 0.7);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 20px; 
}

.btn {
  padding: 10px 20px;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}



.chart-container {
  height: 250px;
  background: rgba(10, 25, 47, 0.3);
  border-radius: 8px;
  margin-top: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(204, 214, 246, 0.5);
  border: 1px dashed #1f3a68;
}

@media (max-width: 1024px) {
  .control-dashboard {
    grid-template-columns: 1fr;
  }
  
  .control-grid {
    grid-template-columns: 1fr;
  }
}
</style>