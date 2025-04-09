<template>
  <div class="humidity-indicator">
    <el-progress :percentage="normalizedValue" :color="humidityColor" :stroke-width="8" :show-text="false" />
    <span class="humidity-value"> {{ value }}%
      <i v-if="isWarning" class="el-icon-warning warning-icon"></i>
    </span>
  </div>
</template>

<script>
export default {
  name: 'HumidityIndicator',
  props: {
    value: {
      type: [Number, String],
      required: true
    }
  },
  computed: {
    normalizedValue() {
      const humidity = parseFloat(this.value)
      return Math.min(100, Math.max(0, humidity))
    },
    isWarning() {
      const humidity = parseFloat(this.value)
      return humidity > 70 || humidity < 30
    },
    humidityColor() {
      const humidity = parseFloat(this.value)
      if (humidity > 70) return '#ff6b35'
      if (humidity < 30) return '#00d4ff'
      return '#4cb944'
    }
  }
}
</script>

<style scoped>
.humidity-indicator {
  display: flex;
  align-items: center;
}

.humidity-value {
  margin-left: 8px;
  font-family: 'Courier New', monospace;
  font-weight: bold;
}

.warning-icon {
  margin-left: 4px;
  color: #ff6b35;
}

::v-deep .el-progress-bar {
  width: 60px;
}

::v-deep .el-progress-bar__outer {
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
}
</style>