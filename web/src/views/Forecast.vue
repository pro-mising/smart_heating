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
              <option value="1">未来1小时</option>
              <option value="12">未来7小时</option>
              <option value="24">未来24小时</option>
            </select>
          </div>

          <div class="form-group">
            <label>天气数据源</label>
            <select class="form-control" v-model="weatherSource">
              <option value="enterprise">企业报表</option>
              <option value="cma">中国气象局</option>
              <option value="accu">AccuWeather</option>
              <option value="weather">Weather.com</option>
            </select>
          </div>

          <div class="form-group">
            <label>历史数据范围</label>
            <select class="form-control" v-model="historyRange">
              <option value="all">全部历史数据</option>
              <option value="1">最近1年</option>
              <option value="3">最近3年</option>   
            </select>
          </div>

          <div class="form-group">
            <label>选择地址</label>
            <select class="form-control" v-model="address">
              <option value="fuli">富力小学</option>
              <option value="jiaotong">交通小学</option>
              <option value="jiefang">解放小学</option>
              <option value="menggu">蒙古族中学</option>
              <option value="shijie">施介小学</option>
              <option value="shigao">实验高级中学</option>
              <option value="shixiao">实验小学</option>
            </select>
          </div>

          <button class="btn" style="width: 100%;" @click="generateReport">生成预测报告</button>
        </div>

        <div class="data-container">
          <div class="data-placeholder" v-if="!reportData">
            预测数据报告将在此显示
          </div>
          <div v-else>
    <div class="report-section">
      <h3>预测结果数据表</h3>
      <div class="data-table-wrapper">
        <table class="data-table">
          <thead>
            <tr>
              <th>地址</th>
              <th>时间点</th>
              <th>热负荷预测值(kW)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in reportData.forecast" :key="index">
              <td>{{ getAddressName(address) }}</td>
              <td>{{ item.time }}</td>
              <td>{{ item.value.toFixed(2) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

          <div class="data-cards">
            <div class="data-card">
              <h3>模型损失值</h3>
              <div class="data-value">{{ modelMetrics.loss.toFixed(4) }}</div>
              <div class="data-desc">训练集平均损失</div>
            </div>
            <div class="data-card">
              <h3>验证集准确率</h3>
              <div class="data-value">{{ (modelMetrics.valAccuracy * 100).toFixed(1)}}%</div>
              <div class="data-desc">交叉验证结果</div>
            </div>
            <div class="data-card">
              <h3>RMSE</h3>
              <div class="data-value">{{ modelMetrics.rmse.toFixed(2)}}</div>
              <div class="data-desc">均方根误差</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 生成报告弹窗 -->
    <el-dialog
      title="正在生成预测报告"
      :visible.sync="showLoadingDialog"
      width="30%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <div class="loading-content">
        <el-progress :percentage="progressPercent" :status="progressStatus"></el-progress>
        <p class="loading-text">{{ loadingText }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
//import api from '@/api'

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
      address: 'fuli',
      modelMetrics: {
        loss: 0.0,
        valAccuracy: 0.0,
        rmse: 0.0
      },
      reportData: null,
      showLoadingDialog: false,
      progressPercent: 0,
      progressStatus: 'success',
      loadingText: '正在初始化预测模型...',
      loadingSteps: [
        { percent: 20, text: '正在加载历史数据...' },
        { percent: 40, text: '正在预处理数据...' },
        { percent: 60, text: '正在训练预测模型...' },
        { percent: 80, text: '正在生成预测结果...' },
        { percent: 100, text: '正在生成数据报告...' }
      ]
    }
  },
  methods: {
    getAddressName(value) {
      const addresses = {
        fuli: '富力小学',
        jiaotong: '交通小学',
        jiefang: '解放小学',
        menggu: '蒙古族中学',
        shijie: '施介小学',
        shigao: '实验高级中学',
        shixiao: '实验小学'
      };
      return addresses[value] || value;
    },
    
    async generateReport() {
      this.showLoadingDialog = true
      this.progressPercent = 0
      this.progressStatus = 'success'
      
      // 模拟进度更新
      for (const step of this.loadingSteps) {
        await this.updateProgress(step)
      }
      
      try {
        // 模拟API返回数据
        await new Promise(resolve => setTimeout(resolve, 500))
        
        // 生成模拟预测数据
        const forecast = []
        const now = new Date()
        const hours = parseInt(this.forecastRange)
        
        for (let i = 1; i <= hours; i++) {
          const time = new Date(now.getTime() + i * 60 * 60 * 1000)
          const value = 50 + Math.random() * 50
          forecast.push({
            time: time.toLocaleTimeString(),
            value: value,
            lower: value * (0.9 + Math.random() * 0.05),
            upper: value * (1.1 + Math.random() * 0.05),
            accuracy: 0.8 + Math.random() * 0.15
          })
        }
        
        // 计算统计摘要
        const values = forecast.map(item => item.value)
        const sum = values.reduce((a, b) => a + b, 0)
        const mean = sum / values.length
        const sorted = [...values].sort((a, b) => a - b)
        const median = sorted[Math.floor(sorted.length / 2)]
        const min = Math.min(...values)
        const max = Math.max(...values)
        const std = Math.sqrt(values.map(x => Math.pow(x - mean, 2)).reduce((a, b) => a + b) / values.length)
        const cv = std / mean
        
        this.reportData = {
          forecast: forecast,
          summary: {
            mean,
            median,
            min,
            max,
            std,
            cv
          }
        }
        
        this.modelMetrics = {
          loss: Math.random() * 0.5,
          valAccuracy: 0.85 + Math.random() * 0.1,
          rmse: 2.5 + Math.random() * 1.5
        }
        
        this.$message.success('预测报告生成成功')
      } catch (error) {
        console.error('生成预测报告失败:', error)
        this.progressStatus = 'exception'
        this.loadingText = '生成报告时出错: ' + error.message
        this.$message.error('生成预测报告失败')
      } finally {
        setTimeout(() => {
          this.showLoadingDialog = false
        }, 1000)
      }
    },
    
    async updateProgress(step) {
      return new Promise(resolve => {
        setTimeout(() => {
          this.progressPercent = step.percent
          this.loadingText = step.text
          resolve()
        }, 800)
      })
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
  color: #64ffda;
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
  color: #ccd6f6;
  font-size: 14px;
}

.btn {
  padding: 10px 20px;
  background: linear-gradient(135deg, #64ffda 0%, #00d4ff 100%);
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

.data-container {
  background: rgba(16, 36, 69, 0.8);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(100, 255, 218, 0.1);
  min-height: 500px;
}

.data-placeholder {
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

.data-table-wrapper {
  max-height: 300px;
  overflow-y: auto;
  margin-top: 15px;
  background: rgba(10, 25, 47, 0.3);
  border-radius: 8px;
  padding: 10px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  color: #ccd6f6;
}

.data-table th, .data-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #1f3a68;
}

.data-table th {
  background: rgba(16, 36, 69, 0.8);
  color: #64ffda;
  position: sticky;
  top: 0;
}

.data-table tr:hover {
  background: rgba(100, 255, 218, 0.1);
}

.summary-section {
  margin-top: 30px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  margin-top: 15px;
}

.summary-item {
  background: rgba(10, 25, 47, 0.3);
  border-radius: 6px;
  padding: 15px;
  border: 1px solid #1f3a68;
}

.summary-label {
  display: block;
  color: rgba(204, 214, 246, 0.7);
  font-size: 14px;
  margin-bottom: 5px;
}

.summary-value {
  display: block;
  color: #64ffda;
  font-size: 18px;
  font-weight: bold;
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
  color: #64ffda;
  margin-top: 0;
  font-size: 16px;
}

.data-value {
  font-size: 28px;
  font-weight: bold;
  margin: 10px 0;
  color: #64ffda;
}

.data-desc {
  color: rgba(204, 214, 246, 0.7);
  font-size: 14px;
}

.loading-content {
  text-align: center;
}

.loading-text {
  margin-top: 15px;
  color: #606266;
}

@media (max-width: 768px) {
  .dashboard {
    grid-template-columns: 1fr;
  }
  
  .data-cards, .summary-grid {
    grid-template-columns: 1fr;
  }
  
  .data-table-wrapper {
    overflow-x: auto;
  }
}
</style>