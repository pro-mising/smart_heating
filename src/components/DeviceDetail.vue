<template>
  <div class="device-detail">
    <div class="detail-header">
      <h3 class="device-title">
        <i class="el-icon-location-outline"></i>
        {{ device.deviceAddress }}
      </h3>
      <el-tag 
        :type="device.status === 'warning' ? 'danger' : 'success'"
        size="small"
      >
        {{ device.status === 'warning' ? '异常' : '正常' }}
      </el-tag>
    </div>
    
    <el-divider></el-divider>
    
    <div class="detail-grid">
      <div class="detail-item">
        <label>设备ID：</label>
        <span>{{ device.id || 'N/A' }}</span>
      </div>
      <div class="detail-item">
        <label>最后上报：</label>
        <span>{{ device.collectTime || 'N/A' }}</span>
      </div>
      <div class="detail-item">
        <label>外部温度：</label>
        <temp-indicator 
          :value="device.outTemp" 
          type="outdoor"
        />
      </div>
      <div class="detail-item">
        <label>室内温度：</label>
        <temp-indicator 
          :value="device.roomTemp" 
          type="indoor"
        />
      </div>
      <div class="detail-item">
        <label>二次供温：</label>
        <temp-indicator 
          :value="device.supplyTemp" 
          type="supply"
          :warning="device.supplyTemp > 75"
        />
      </div>
      <div class="detail-item">
        <label>环境湿度：</label>
        <humidity-indicator :value="device.humidity" />
      </div>
    </div>
    
    <el-divider></el-divider>
    
    <div class="detail-actions">
      <el-button 
        type="primary" 
        size="small"
        @click="handleAdjust"
      >
        <i class="el-icon-edit"></i> 调节参数
      </el-button>
      <el-button 
        type="warning" 
        size="small"
        @click="handleDiagnose"
      >
        <i class="el-icon-search"></i> 故障诊断
      </el-button>
      <el-button 
        type="info" 
        size="small"
        @click="handleHistory"
      >
        <i class="el-icon-time"></i> 历史数据
      </el-button>
    </div>
  </div>
</template>

<script>
import TempIndicator from './TempIndicator.vue'
import HumidityIndicator from './HumidityIndicator.vue'

export default {
  name: 'DeviceDetail',
  components: {
    TempIndicator,
    HumidityIndicator
  },
  props: {
    device: {
      type: Object,
      required: true,
      default: () => ({
        id: '',
        deviceAddress: '未知设备',
        collectTime: 'N/A',
        outTemp: 0,
        roomTemp: 0,
        supplyTemp: 0,
        humidity: 0,
        status: 'normal'
      })
    }
  },
  methods: {
    handleAdjust() {
      this.$emit('adjust', this.device.id)
    },
    handleDiagnose() {
      this.$emit('diagnose', this.device.id)
    },
    handleHistory() {
      this.$emit('history', this.device.id)
    }
  }
}
</script>

<style scoped>
.device-detail {
  padding: 10px;
  background: #0a192f;
  color: #ccd6f6;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.device-title {
  margin: 0;
  color: #64ffda;
  display: flex;
  align-items: center;
}

.device-title i {
  margin-right: 8px;
  font-size: 18px;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px 24px;
  margin: 20px 0;
}

.detail-item {
  display: flex;
  align-items: center;
}

.detail-item label {
  color: #8892b0;
  min-width: 80px;
  text-align: right;
  margin-right: 12px;
}

.detail-item span {
  font-family: 'Courier New', monospace;
}

.detail-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

::v-deep .el-divider {
  background-color: #1f3a68;
}
</style>