<template>
  <div class="data-monitor-container">
    <Navbar />
    <h1 class="page-title">数据监控页面</h1>

    <div class="monitor-content-wrapper">
      <div class="search-container">
        <div class="search-box">
          <i class="el-icon-search"></i>
          <input type="text" placeholder="搜索设备位置..." class="search-input" v-model="searchQuery">
          <button class="search-btn" @click="handleSearch">
            <i class="el-icon-search"></i> 搜索
          </button>
        </div>
      </div>

      <div class="data-display-panel">
        <div class="panel-header">
          <h3 class="panel-title">实时监测数据</h3>
          <div class="panel-actions">
            <button class="action-btn" @click="refreshData">
              <i class="el-icon-refresh"></i> 刷新
            </button>
            <button class="action-btn" @click="exportData">
              <i class="el-icon-download"></i> 导出
            </button>
          </div>
        </div>

        <table class="data-table">
          <thead>
            <tr>
              <th>设备位置</th>
              <th>采集时间</th>
              <th>外部温度(℃)</th>
              <th>室温(℃)</th>
              <th>风力(m/s)</th>
              <th>热负荷(kW)</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in filteredData" :key="item.id">
              <td>
                <div class="device-cell">
                  <i class="el-icon-location-outline"></i>
                  <span>{{ item.location }}</span>
                </div>
              </td>
              <td>{{ formatTime(item.time) }}</td>
              <td :class="{'warning-text': item.outTemp < 10}">{{ item.outTemp }}</td>
              <td :class="{'warning-text': item.roomTemp < 18}">{{ item.roomTemp }}</td>
              <td>{{ item.windSpeed }}</td>
              <td>{{ item.heatLoad }}</td>
              <td>
                <button class="detail-btn" @click="showDetail(item)">
                  <i class="el-icon-document"></i> 详情
                </button>
                <button class="trend-btn" @click="showTrend(item)">
                  <i class="el-icon-data-line"></i> 趋势
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="pagination-wrapper">
          <div class="pagination">
            <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
            <button v-for="page in totalPages" 
                    :key="page" 
                    @click="goToPage(page)"
                    :class="{active: currentPage === page}">
              {{ page }}
            </button>
            <button @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
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
  name: 'DataMonitor',
  components: {
    Navbar
  },
  data() {
    return {
      searchQuery: '',
      currentPage: 1,
      itemsPerPage: 10,
      monitorData: [
        {
          id: 1,
          location: '第五中学-教学楼',
          time: new Date(),
          outTemp: 5,
          roomTemp: 22,
          windSpeed: 3.2,
          heatLoad: 45.6
        },
        {
          id: 2,
          location: '第三小学-主楼',
          time: new Date(),
          outTemp: 7,
          roomTemp: 20,
          windSpeed: 2.8,
          heatLoad: 38.2
        },
        {
          id: 3,
          location: '市政府办公楼',
          time: new Date(),
          outTemp: 6,
          roomTemp: 21,
          windSpeed: 4.1,
          heatLoad: 52.3
        },
        {
          id: 4,
          location: '人民医院-住院部',
          time: new Date(),
          outTemp: 4,
          roomTemp: 23,
          windSpeed: 3.5,
          heatLoad: 48.7
        },
        {
          id: 5,
          location: '购物中心-主供暖',
          time: new Date(),
          outTemp: 8,
          roomTemp: 19,
          windSpeed: 2.3,
          heatLoad: 42.1
        }
      ]
    }
  },
  computed: {
    filteredData() {
      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return this.monitorData
        .filter(item => item.location.includes(this.searchQuery))
        .slice(start, end)
    },
    totalPages() {
      return Math.ceil(this.monitorData.length / this.itemsPerPage)
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const response = await api.getDevices()
        this.monitorData = response.data || this.monitorData
      } catch (error) {
        console.error('获取数据失败:', error)
      }
    },
    handleSearch() {
      this.currentPage = 1
    },
    formatTime(date) {
      return date.toLocaleTimeString()
    },
    showDetail(item) {
      this.$router.push(`/device/${item.id}`)
    },
    showTrend(item) {
      console.log('显示趋势:', item)
    },
    refreshData() {
      this.fetchData()
    },
    exportData() {
      console.log('导出数据')
    },
    prevPage() {
      if (this.currentPage > 1) this.currentPage--
    },
    nextPage() {
      if (this.currentPage < this.totalPages) this.currentPage++
    },
    goToPage(page) {
      this.currentPage = page
    }
  }
}
</script>

<style scoped>
.data-monitor-container {
  background-color: #0a192f;
  min-height: 100vh;
  color: #ccd6f6;
  padding: 20px;
  padding-top: 100px;
}

.page-title {
  color: #64ffda;
  text-align: center;
  margin-bottom: 30px;
  font-size: 28px;
  font-weight: bold;
}

.monitor-content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
}

.search-container {
  margin-bottom: 20px;
  background-color: #112240;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-box input {
  background-color: #0a192f;
  border: 1px solid #1f3a68;
  color: #ccd6f6;
  flex: 1;
  padding: 10px 15px;
  border-radius: 4px;
  font-size: 14px;
}

.search-box i {
  color: #64ffda;
}

.search-btn {
  background-color: rgba(100, 255, 218, 0.1);
  border: 1px solid rgba(100, 255, 218, 0.3);
  color: #64ffda;
  padding: 10px 15px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
}

.search-btn:hover {
  background-color: rgba(100, 255, 218, 0.2);
}

.data-display-panel {
  background-color: #112240;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.panel-title {
  color: #64ffda;
  font-size: 18px;
  margin: 0;
}

.panel-actions {
  display: flex;
  gap: 10px;
}

.action-btn {
  background-color: rgba(100, 255, 218, 0.1);
  border: 1px solid rgba(100, 255, 218, 0.3);
  color: #64ffda;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
}

.action-btn:hover {
  background-color: rgba(100, 255, 218, 0.2);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.data-table th {
  background-color: #112240;
  color: #64ffda;
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #1f3a68;
  font-weight: bold;
  font-size: 14px;
}

.data-table td {
  padding: 12px 15px;
  border-bottom: 1px solid #1f3a68;
  background-color: #0a192f;
  font-size: 14px;
}

.warning-text {
  color: #ff6b35;
  font-weight: bold;
}

.device-cell {
  display: flex;
  align-items: center;
}

.device-cell i {
  margin-right: 8px;
  color: #0072ff;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.pagination {
  display: flex;
  gap: 5px;
}

.pagination button {
  background-color: #0a192f;
  color: #ccd6f6;
  border: 1px solid #1f3a68;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.pagination button:hover:not(:disabled) {
  background-color: #1f3a68;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination .active {
  background-color: #0072ff;
  color: white;
}

.detail-btn,
.trend-btn {
  color: #64ffda;
  background: none;
  border: none;
  padding: 5px 8px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  transition: all 0.3s;
  border-radius: 4px;
}

.detail-btn:hover {
  background-color: rgba(100, 255, 218, 0.1);
}

.trend-btn {
  color: #00d4ff;
}

.trend-btn:hover {
  background-color: rgba(0, 212, 255, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-box {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-btn {
    width: 100%;
    justify-content: center;
  }
  
  .data-table {
    display: block;
    overflow-x: auto;
  }
  
  .panel-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .panel-actions {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>