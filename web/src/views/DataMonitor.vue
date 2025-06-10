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

        <!-- 加载指示器 -->
        <div v-if="isLoading" class="loading-indicator">
          <i class="el-icon-loading"></i> 数据加载中...
        </div>
  
         <!-- 空状态提示 -->
        <div v-else-if="!isLoading && monitorData.length === 0" class="no-data">
          <i class="el-icon-warning-outline"></i> 没有找到匹配的数据
        </div>

        <table class="data-table">
          <thead>
            <tr>
              <th>设备位置</th>
              <th>采集时间</th>
              <th>外部温度(℃)</th>
              <th>室温(℃)</th>
              <th>风力(级)</th>
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
            <template v-for="item in paginationItems">
              <button 
                v-if="item.type === 'ellipsis'"
                disabled
                class="ellipsis"
                :key="'ellipsis-' + item.value"
              >...</button>
              <button 
                v-else
                @click="goToPage(item.value)"
                :class="{active: currentPage === item.value}"
                :key="item.value"
              >{{ item.value }}</button>
            </template>
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
import { debounce } from 'lodash'

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
    monitorData: [], // 存储所有数据
    totalDataCount: 0, // 总数据量（用于计算总页数）
    isLoading: false, // 防止重复加载
  }
  },
  computed: {
    filteredData() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.monitorData.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.monitorData.length / this.itemsPerPage);
    },
    displayedPages() {
    const total = this.totalPages;
    const current = this.currentPage;
    const range = 5; // 当前页前后显示的数量
    let start = Math.max(1, current - range);
    let end = Math.min(total, current + range);
    
    // 如果总页数超过10，添加省略号
    if (total > 10) {
      if (current > range + 1) {
        start = current - range;
        end = current + range;
      }
      
      if (end > total - 1) {
        end = total;
      }
    }

    const pages = [];
    // 添加第一页
    if (start > 1) {
      pages.push(1);
      if (start > 2) {
        pages.push('...');
      }
    }
    
    // 添加中间页
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    
    // 添加最后一页
    if (end < total) {
      if (end < total - 1) {
        pages.push('...');
      }
      pages.push(total);
    }

    return pages;
  },
  paginationItems() {
    return this.displayedPages.map(page => ({
      type: page === '...' ? 'ellipsis' : 'page',
      value: page
    }));
  }
  },
  created() {
     console.log('组件已创建，调用 fetchData')
    this.fetchAllData(); 
  },
  methods: {
    async fetchAllData() {
    this.isLoading = true;
    try {
      let allData = [];
      let page = 1;
      let hasMoreData = true;
      const maxRequests = 100;

      // 重置数据
      this.monitorData = [];

      // 循环获取至多100页的数据
      while (hasMoreData && page <= maxRequests) {
        const response = await api.getDevices(page, this.searchQuery);

        console.log('接口返回数据:', response)

        if (response.heatdata && response.heatdata.length > 0) {
          allData = [...allData, ...response.heatdata];
          page++;
        } else {
          hasMoreData = false;
        }
      }

      // 转换数据格式
      this.monitorData = allData.map(item => ({
        id: item.id,
        location: item.address,
        time: new Date(item.date),
        outTemp: item.outdoorTemperature,
        roomTemp: item.indoorTemperature,
        windSpeed: parseFloat(item.wind),
        heatLoad: item.heatLoad,
      }));

      this.totalDataCount = allData.length;
    } catch (error) {
      console.error("获取数据失败:", error);
    } finally {
      this.isLoading = false;
    }
  },
 handleSearch: debounce(function() {
      this.currentPage = 1;
      this.fetchAllData();
    }, 500),
  prevPage() {
  if (this.currentPage > 1) {
    this.currentPage--
    this.fetchAllData()
  }
},
nextPage() {
  if (this.currentPage < this.totalPages) {
    this.currentPage++
    this.fetchAllData()
  }
},
goToPage(page) {
  this.currentPage = page
  this.fetchAllData()
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
      this.fetchAllData()
    },
    exportData() {
      console.log('导出数据')
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

  .pagination {
  display: flex;
  align-items: center;
  gap: 10px;
}

.loading-indicator {
  padding: 20px;
  text-align: center;
  color: #666;
  font-size: 16px;
}

.no-data {
  padding: 20px;
  text-align: center;
  color: #999;
  font-size: 16px;
  border: 1px dashed #eee;
  margin: 10px 0;
}

}
</style>