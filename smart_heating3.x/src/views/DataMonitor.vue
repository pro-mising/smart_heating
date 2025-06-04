<template>
  <div class="data-monitor-container">
    <Navbar />
    <h1 class="page-title">数据监控页面</h1>

    <div class="monitor-content-wrapper">
      <div class="data-nav-panel">
        <div class="search-box">
          <i class="el-icon-search"></i>
          <input type="text" placeholder="搜索设备位置..." class="search-input" v-model="searchQuery">
        </div>

        <div class="location-info-card">
          <h3 class="panel-title">当前位置</h3>
          <div class="location-tags">
            <span class="tag tag-success">通辽市供暖项目</span>
            <span class="tag tag-default">第五中学</span>
          </div>

          <div class="status-indicators">
            <div class="indicator online">
              <i class="el-icon-success"></i>
              <span>{{ onlineCount }}台在线</span>
            </div>
            <div class="indicator warning">
              <i class="el-icon-warning"></i>
              <span>{{ warningCount }}台异常</span>
            </div>
          </div>
        </div>

        <div class="device-tree">
          <div class="tree-node" 
               v-for="location in filteredLocations" 
               :key="location.id"
               @click="selectLocation(location)">
            {{ location.name }}
            <div class="tree-node tree-child" 
                 v-for="device in location.devices" 
                 :key="device.id"
                 @click.stop="selectDevice(device)">
              {{ device.name }}
            </div>
          </div>
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
              <th>设备地址</th>
              <th>采集时间</th>
              <th>外部温度(℃)</th>
              <th>室温(℃)</th>
              <th>湿度(%)</th>
              <th>二次供温(℃)</th>
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
              <td>{{ item.humidity }}</td>
              <td>{{ item.supplyTemp }}</td>
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
      itemsPerPage: 5,
      onlineCount: 0,
      warningCount: 0,
      locations: [
        {
          id: 1,
          name: '第五中学',
          devices: [
            { id: 101, name: '教学楼-主供暖机组' },
            { id: 102, name: '教学楼-备用锅炉' },
            { id: 103, name: '实验楼-供暖系统' }
          ]
        },
        {
          id: 2,
          name: '第三小学',
          devices: [
            { id: 201, name: '主教学楼-供暖机组' }
          ]
        }
      ],
      monitorData: [
        {
          id: 1,
          location: '第五中学-教学楼',
          time: new Date(),
          outTemp: 5,
          roomTemp: 22,
          humidity: 45,
          supplyTemp: 65
        },
        // 更多数据...
      ]
    }
  },
  computed: {
    filteredLocations() {
      if (!this.searchQuery) return this.locations
      return this.locations.filter(loc => 
        loc.name.includes(this.searchQuery) || 
        loc.devices.some(dev => dev.name.includes(this.searchQuery))
      )
    },
    filteredData() {
      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return this.monitorData.slice(start, end)
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
        this.monitorData = response.data
        this.calculateStatusCounts()
      } catch (error) {
        console.error('获取数据失败:', error)
      }
    },
    calculateStatusCounts() {
      this.onlineCount = this.monitorData.filter(item => item.status === 'online').length
      this.warningCount = this.monitorData.filter(item => 
        item.outTemp < 10 || item.roomTemp < 18
      ).length
    },
    formatTime(date) {
      return date.toLocaleTimeString()
    },
    selectLocation(location) {
      // 实现选择位置逻辑
      console.log('选择位置:', location)
    },
    selectDevice(device) {
      // 实现选择设备逻辑
      console.log('选择设备:', device)
    },
    showDetail(item) {
      this.$router.push(`/device/${item.id}`)
    },
    showTrend(item) {
      // 显示趋势图表
      console.log('显示趋势:', item)
    },
    refreshData() {
      this.fetchData()
    },
    exportData() {
      // 导出数据逻辑
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
  display: flex;
  max-width: 1600px;
  margin: 0 auto;
  gap: 20px;
}

.data-nav-panel {
  width: 300px;
  background-color: #112240;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  height: fit-content;
  position: sticky;
  top: 100px;
}

.search-box {
  margin-bottom: 20px;
  position: relative;
}

.search-box input {
  background-color: #0a192f;
  border: 1px solid #1f3a68;
  color: #ccd6f6;
  width: 80%;
  padding: 10px 15px 10px 35px;
  border-radius: 4px;
}

.search-box i {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #64ffda;
}

.panel-title {
  color: #64ffda;
  font-size: 16px;
  margin-bottom: 15px;
}

.location-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
}

.tag {
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 14px;
}

.tag-success {
  background-color: rgba(46, 204, 113, 0.1);
  color: #2ecc71;
  border: 1px solid rgba(46, 204, 113, 0.3);
}

.tag-default {
  background-color: rgba(100, 255, 218, 0.1);
  color: #64ffda;
  border: 1px solid rgba(100, 255, 218, 0.3);
}

.status-indicators {
  display: flex;
  justify-content: space-between;
  margin-bottom: 25px;
}

.indicator {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 4px;
  font-size: 14px;
}

.indicator i {
  margin-right: 6px;
  font-size: 16px;
}

.indicator.online {
  background-color: rgba(46, 204, 113, 0.1);
  color: #2ecc71;
}

.indicator.warning {
  background-color: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
}

.device-tree {
  background: transparent;
  color: #ccd6f6;
}

.tree-node {
  padding: 8px 0;
  cursor: pointer;
}

.tree-node:hover {
  background-color: rgba(100, 255, 218, 0.1);
}

.tree-child {
  padding-left: 20px;
}

.data-display-panel {
  flex: 1;
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
}

.data-table td {
  padding: 12px 15px;
  border-bottom: 1px solid #1f3a68;
  background-color: #0a192f;
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
  justify-content: flex-end;
}

.pagination {
  display: flex;
  gap: 5px;
}

.pagination button {
  background-color: #0a192f;
  color: #ccd6f6;
  border: 1px solid #1f3a68;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
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
  padding: 5px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.trend-btn {
  color: #00d4ff;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .monitor-content-wrapper {
    flex-direction: column;
  }

  .data-nav-panel {
    width: 100%;
    position: static;
    margin-bottom: 20px;
  }
}

@media (max-width: 768px) {
  .data-table {
    display: block;
    overflow-x: auto;
  }
}
</style>