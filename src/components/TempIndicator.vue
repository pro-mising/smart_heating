<template>
  <div class="temp-indicator">
    <span 
      class="temp-value"
      :class="tempClass"
      :style="tempStyle"
    >
      {{ value }}℃
      <i 
        v-if="warning" 
        class="el-icon-warning warning-icon"
      ></i>
    </span>
    <div 
      class="temp-bar"
      :style="barStyle"
    ></div>
  </div>
</template>

<script>
export default {
  name: 'TempIndicator',
  props: {
    value: {
      type: [Number, String],
      required: true
    },
    type: {
      type: String,
      validator: v => ['outdoor', 'indoor', 'supply'].includes(v),
      default: 'indoor'
    },
    warning: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    normalizedValue() {
      const temp = parseFloat(this.value)
      if (this.type === 'outdoor') {
        // 室外温度范围：-20℃ ~ 40℃
        return Math.min(40, Math.max(-20, temp))
      } else if (this.type === 'supply') {
        // 供温范围：50℃ ~ 90℃
        return Math.min(90, Math.max(50, temp))
      }
      // 室温范围：10℃ ~ 30℃
      return Math.min(30, Math.max(10, temp))
    },
    tempClass() {
      const temp = parseFloat(this.value)
      if (this.type === 'outdoor') {
        return temp < -5 ? 'low' : temp > 30 ? 'high' : ''
      } else if (this.type === 'supply') {
        return temp > 75 ? 'high' : temp < 60 ? 'low' : ''
      }
      return temp < 16 ? 'low' : temp > 24 ? 'high' : ''
    },
    tempStyle() {
      if (this.warning) {
        return { color: '#ff6b35' }
      }
      return {}
    },
    barStyle() {
      let percentage, background
      const temp = this.normalizedValue
      
      if (this.type === 'outdoor') {
        percentage = ((temp + 20) / 60) * 100
        background = 'linear-gradient(90deg, #00d4ff, #ff6b35)'
      } else if (this.type === 'supply') {
        percentage = ((temp - 50) / 40) * 100
        background = 'linear-gradient(90deg, #4cb944, #ff6b35)'
      } else {
        percentage = ((temp - 10) / 20) * 100
        background = 'linear-gradient(90deg, #00d4ff, #ff6b35)'
      }
      
      return {
        width: `${Math.min(100, Math.max(0, percentage))}%`,
        background
      }
    }
  }
}
</script>

<style scoped>
.temp-indicator {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.temp-value {
  font-family: 'Courier New', monospace;
  font-weight: bold;
  margin-bottom: 4px;
}

.temp-value.low {
  color: #00d4ff;
}

.temp-value.high {
  color: #ff6b35;
}

.warning-icon {
  margin-left: 4px;
}

.temp-bar {
  height: 4px;
  border-radius: 2px;
  transition: width 0.5s ease;
}

::v-deep .el-tooltip__popper {
  background: #112240 !important;
  border: 1px solid #1f3a68 !important;
}
</style>