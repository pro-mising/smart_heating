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
            <option value="富力小学">富力小学</option>
            <option value="交通小学">交通小学</option>
            <option value="解放小学">解放小学</option>
            <option value="蒙古族中学">蒙古族中学</option>
            <option value="蒙古族中学(小)">蒙古族中学(小)</option>
            <option value="施介小学">施介小学</option>
            <option value="实验高级中学">实验高级中学</option>
            <option value="实验小学">实验小学</option>
            <option value="实验小学东校区">实验小学东校区</option>
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
              <th>日期</th>
              <th>未来1小时平均热负荷预测值(kW)</th>
              <th>未来7小时平均热负荷预测值(kW)</th>
              <th>未来24小时平均热负荷预测值(kW)</th>
            </tr>
          </thead>
          <tbody>
              <tr v-for="(item, index) in reportData.forecast" :key="index">
              <td>{{ address }}</td>
              <td>{{ item.date }}</td>
              <td>{{ item.hour_one.toFixed(2) }}</td>
              <td>{{ item.hour_seven.toFixed(2) }}</td>
              <td>{{ item.hour_twentyf.toFixed(2) }}</td>
              </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

          <div class="data-cards">
            <div class="data-card">
              <h3>MAE</h3>
              <div class="data-value">{{ modelMetrics.loss.toFixed(4) }}</div>
              <div class="data-desc">平均绝对误差</div>
            </div>
            <div class="data-card">
              <h3>R2（R-Square）</h3>
              <div class="data-value">{{ (modelMetrics.valAccuracy * 100).toFixed(1)}}%</div>
              <div class="data-desc">决定系数</div>
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
      address: '富力小学',
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
  async generateReport() {
  this.showLoadingDialog = true;
  this.progressPercent = 0;
  this.progressStatus = 'success';

  for (const step of this.loadingSteps) {
    await this.updateProgress(step);
  }

  try {
    const response = await api.getForecastData(this.address);
    console.log("接口返回数据：", response);

    const result = response;

    if (!Array.isArray(result) || result.length === 0) {
      throw new Error("返回数据格式不正确");
    }

    // 解析预测数据
   const forecast = result.map(item => ({
  date: item.date || '未知',
  hour_one: item.hourOne ?? 0,
  hour_seven: item.hourSeven ?? 0,
  hour_twentyf: item.hourTwentyfour ?? 0
  }));

    this.reportData = { forecast };

    // 提取模型评估指标，只用第一项的数据
    const first = result[0];
    this.modelMetrics = {
  loss: first.mae ?? 0,
  valAccuracy: first.rtwo ?? 0,
  rmse: first.rmse ?? 0
};

    this.$message.success('预测报告生成成功');
  } catch (error) {
    console.error('生成预测报告失败:', error);
    this.progressStatus = 'exception';
    this.loadingText = '生成报告时出错: ' + error.message;
    this.$message.error('生成预测报告失败');
  } finally {
    setTimeout(() => {
      this.showLoadingDialog = false;
    }, 1000);
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