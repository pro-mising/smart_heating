<template>
  <div>
    <Navbar />
    <div class="container">
      <div class="dashboard">
        <div class="control-panel">
          <h2 class="panel-title">
            <i>⚙️</i> 预测参数设置
          </h2>

          <div class="form-group">
            <label>预测时间范围</label>
            <select class="form-control" v-model="forecastRange">
              <option value="24">未来24小时</option>
              <option value="7">未来7天</option>
              <option value="30">未来30天</option>
            </select>
          </div>

          <div class="form-group">
            <label>天气数据源</label>
            <select class="form-control" v-model="weatherSource">
              <option value="cma">中国气象局</option>
              <option value="accu">AccuWeather</option>
              <option value="weather">Weather.com</option>
            </select>
          </div>

          <div class="form-group">
            <label>历史数据范围</label>
            <select class="form-control" v-model="historyRange">
              <option value="1">最近1年</option>
              <option value="3">最近3年</option>
              <option value="all">全部历史数据</option>
            </select>
          </div>

          <button class="btn" style="width: 100%;" @click="generateReport">生成预测报告</button>
        </div>

        <div class="chart-container">
          <div class="chart-placeholder" v-if="!chartData">
            预测数据可视化图表将在此显示
          </div>
          <div v-else>
            <div class="chart-wrapper">
              <h3>预测趋势图</h3>
              <div class="mock-chart"></div>
            </div>
          </div>

          <div class="data-cards">
            <div class="data-card">
              <h3>预测能耗峰值</h3>
              <div class="data-value">{{ forecastData.peakPower }}kW</div>
              <div class="data-desc">预计出现在{{ forecastData.peakTime }}</div>
            </div>
            <div class="data-card">
              <h3>节能潜力</h3>
              <div class="data-value">{{ forecastData.savingPotential }}%</div>
              <div class="data-desc">相比当前策略可节约</div>
            </div>
            <div class="data-card">
              <h3>预测准确率</h3>
              <div class="data-value">{{ forecastData.accuracy }}%</div>
              <div class="data-desc">基于历史数据验证</div>
            </div>
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
  name: 'ForecastPanel',
  components: {
    Navbar
  },
  data() {
    return {
      forecastRange: '24',
      weatherSource: 'cma',
      historyRange: '1',
      forecastData: {
        peakPower: 0,
        peakTime: '13月61日 25:62',
        savingPotential: 0,
        accuracy: 100
      },
      chartData: null
    }
  },
  methods: {
    async generateReport() {
      try {
        const params = {
          range: this.forecastRange,
          source: this.weatherSource,
          history: this.historyRange
        }
        const response = await api.getForecastData(params)
        this.forecastData = response.data.summary
        this.chartData = response.data.chart
        this.$message.success('预测报告生成成功')
      } catch (error) {
        console.error('生成预测报告失败:', error)
        this.$message.error('生成预测报告失败')
      }
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.dashboard {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 20px;
}

.control-panel {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(100, 255, 218, 0.1);
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
  padding: 10px 15px;
  background: rgba(10, 25, 47, 0.5);
  border: 1px solid #1f3a68;
  border-radius: 6px;
  color: var(--light);
  font-size: 14px;
}

.btn {
  padding: 10px 20px;
  background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
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

.chart-container {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(100, 255, 218, 0.1);
  min-height: 500px;
}

.chart-placeholder {
  background: rgba(10, 25, 47, 0.3);
  border: 2px dashed #1f3a68;
  border-radius: 8px;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: rgba(204, 214, 246, 0.5);
  font-size: 18px;
}

.data-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.data-card {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 8px;
  padding: 20px;
  border: 1px solid rgba(100, 255, 218, 0.1);
}

.data-card h3 {
  color: var(--primary);
  margin-top: 0;
  font-size: 16px;
}

.data-value {
  font-size: 28px;
  font-weight: bold;
  margin: 10px 0;
  color: var(--success);
}

.data-desc {
  color: rgba(204, 214, 246, 0.7);
  font-size: 14px;
}

.mock-chart {
  height: 300px;
  background: rgba(10, 25, 47, 0.3);
  border-radius: 8px;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .dashboard {
    grid-template-columns: 1fr;
  }
  
  .data-cards {
    grid-template-columns: 1fr;
  }
}
</style>