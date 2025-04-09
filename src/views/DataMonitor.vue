<template>
  <div class="data-monitor-standalone">
    <!-- 独立导航栏 -->
    <h1>数据监控页面</h1>
    <p>如果你看到这个页面，说明 DataMonitor 已正确加载。</p>
    <nav class="standalone-navbar">
      <div class="logo">
        <div class="logo-icon"></div>
        <div class="logo-text">智能供暖数据监控系统</div>
      </div>
      <div class="nav-controls">
        <el-button type="text" icon="el-icon-back" @click="handleBack" v-if="!isStandalone">返回主控台</el-button>
        <span class="last-update">最后更新: {{ lastUpdateTime }}</span>
      </div>
    </nav>

    <!-- 主内容区 -->
    <div class="monitor-content-wrapper">
      <!-- 数据导航侧边栏 -->
      <div class="data-nav-panel">
        <div class="search-box">
          <el-input placeholder="搜索设备位置..." prefix-icon="el-icon-search" v-model="searchQuery" clearable
            @clear="handleSearchClear"></el-input>
        </div>

        <div class="location-info-card">
          <h3 class="panel-title">当前位置</h3>
          <div class="location-tags">
            <el-tag type="success" size="medium">通辽市供暖项目</el-tag>
            <el-tag size="medium" class="building-tag">第五中学</el-tag>
          </div>

          <div class="status-indicators">
            <div class="indicator online">
              <i class="el-icon-success"></i>
              <span>24台在线</span>
            </div>
            <div class="indicator warning">
              <i class="el-icon-warning"></i>
              <span>2台异常</span>
            </div>
          </div>
        </div>

        <!-- 设备树形导航 -->
        <div class="device-tree">
          <el-tree :data="deviceTree" :props="treeProps" node-key="id" default-expand-all highlight-current
            @node-click="handleNodeClick"></el-tree>
        </div>
      </div>

      <!-- 数据表格区 -->
      <div class="data-display-panel">
        <div class="table-header">
          <h3 class="table-title">实时监测数据</h3>
          <div class="table-actions">
            <el-button size="small" icon="el-icon-refresh" @click="refreshData">刷新</el-button>
            <el-button size="small" icon="el-icon-download" @click="exportData">导出</el-button>
          </div>
        </div>

        <el-table :data="filteredData" style="width: 100%" height="calc(100vh - 220px)" v-loading="loading"
          :header-cell-style="tableHeaderStyle" :row-style="tableRowStyle" @sort-change="handleSortChange">
          <!-- 表格列定义 -->
          <el-table-column prop="deviceAddress" label="设备地址" width="200" sortable>
            <template #default="{ row }">
              <div class="device-cell">
                <i class="el-icon-location-outline"></i>
                <span>{{ row.deviceAddress }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="collectTime" label="采集时间" width="180" sortable></el-table-column>

          <el-table-column prop="outTemp" label="外部温度(℃)" width="140" sortable>
            <template #default="{ row }">
              <temp-indicator :value="row.outTemp" type="outdoor" />
            </template>
          </el-table-column>

          <el-table-column prop="roomTemp" label="室温(℃)" width="140" sortable>
            <template #default="{ row }">
              <temp-indicator :value="row.roomTemp" type="indoor" />
            </template>
          </el-table-column>

          <el-table-column prop="humidity" label="湿度(%)" width="120" sortable>
            <template #default="{ row }">
              <humidity-indicator :value="row.humidity" />
            </template>
          </el-table-column>

          <el-table-column prop="supplyTemp" label="二次供温(℃)" width="160" sortable>
            <template #default="{ row }">
              <temp-indicator :value="row.supplyTemp" type="supply" :warning="row.supplyTemp > 75" />
            </template>
          </el-table-column>

          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button size="mini" type="text" @click="showDetail(row)" class="detail-btn">
                <i class="el-icon-document"></i> 详情
              </el-button>
              <el-button size="mini" type="text" @click="showTrend(row)" class="trend-btn">
                <i class="el-icon-data-line"></i> 趋势
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-wrapper">
          <el-pagination background layout="total, prev, pager, next, jumper" :total="totalCount" :page-size="pageSize"
            :current-page="currentPage" @current-change="handlePageChange"></el-pagination>
        </div>
      </div>
    </div>

    <!-- 详情对话框 -->
    <el-dialog title="设备详情" :visible.sync="detailVisible" width="60%" custom-class="device-detail-dialog">
      <device-detail :device="currentDevice" />
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'; // 确保正确导入
import TempIndicator from '@/components/TempIndicator.vue'
import HumidityIndicator from '@/components/HumidityIndicator.vue'
import DeviceDetail from '@/components/DeviceDetail.vue'

export default {
  name: 'DataMonitor',
  components: {
    TempIndicator,
    HumidityIndicator,
    DeviceDetail
  },
  data() {
    return {
      searchQuery: '',
      currentPage: 1,
      pageSize: 10,
      totalCount: 0,
      loading: false,
      detailVisible: false,
      currentDevice: null,
      deviceTree: [
        {
          id: 'school',
          label: '第五中学',
          children: [
            { id: 'buildingA', label: '教学楼A' },
            { id: 'buildingB', label: '教学楼B' },
            { id: 'lab', label: '实验楼' },
            { id: 'gym', label: '体育馆' },
            { id: 'canteen', label: '食堂' }
          ]
        }
      ],
      treeProps: {
        children: 'children',
        label: 'label'
      },
      tableData: [],
      sortParams: {}
    }
  },
  computed: {
    ...mapState({
      lastUpdateTime: state => state.lastUpdateTime // 映射 Vuex 状态中的 lastUpdateTime
    }),
    isStandalone() {
      return window.self === window.top
    },
    filteredData() {
      return this.tableData
        .filter(item =>
          item.deviceAddress.includes(this.searchQuery) ||
          item.outTemp.toString().includes(this.searchQuery) ||
          item.roomTemp.toString().includes(this.searchQuery)
        )
        .slice(
          (this.currentPage - 1) * this.pageSize,
          this.currentPage * this.pageSize
        )
    },
    tableHeaderStyle() {
      return {
        backgroundColor: '#112240',
        color: '#fff',
        borderBottom: '1px solid #1f3a68'
      }
    },
    tableRowStyle() {
      return {
        backgroundColor: '#0a192f',
        color: '#ccd6f6'
      }
    }
  },
  created() {
    this.initData()
    this.startAutoRefresh()
  },
  beforeDestroy() {
    this.stopAutoRefresh()
  },
  methods: {
    async initData() {
      this.loading = true
      try {
        // 模拟API请求
        await new Promise(resolve => setTimeout(resolve, 800))
        this.tableData = this.generateMockData()
        this.totalCount = this.tableData.length
      } finally {
        this.loading = false
      }
    },
    generateMockData() {
      const locations = ['教学楼A', '教学楼B', '实验楼', '体育馆', '食堂']
      const data = []
      for (let i = 0; i < 50; i++) {
        data.push({
          id: `device-${i}`,
          deviceAddress: `第五中学-${locations[i % 5]}-${i + 1}`,
          collectTime: this.formatTime(new Date()),
          outTemp: this.randomTemp(-15, 5),
          roomTemp: this.randomTemp(18, 24),
          humidity: this.randomTemp(30, 60),
          supplyTemp: this.randomTemp(65, 80),
          status: Math.random() > 0.9 ? 'warning' : 'normal'
        })
      }
      return data
    },
    randomTemp(min, max) {
      return (Math.random() * (max - min) + min).toFixed(1)
    },
    formatTime(date) {
      return date.toLocaleTimeString('zh-CN', {
        hour12: false,
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },
    handleBack() {
      if (this.$route.query.from) {
        this.$router.go(-1)
      } else {
        this.$router.push('/')
      }
    },
    refreshData() {
      this.initData()
      this.$message.success('数据已刷新')
    },
    exportData() {
      // 实际项目中实现导出逻辑
      this.$message.info('导出功能开发中')
    },
    handleSearchClear() {
      this.currentPage = 1
    },
    handleNodeClick(data) {
      this.searchQuery = data.label
      this.currentPage = 1
    },
    handlePageChange(page) {
      this.currentPage = page
    },
    handleSortChange({ prop, order }) {
      this.sortParams = { prop, order }
      // 实际项目中应该发送排序参数到API
    },
    showDetail(row) {
      this.currentDevice = row
      this.detailVisible = true
    },
    showTrend(row) {
      this.$router.push({
        path: '/trend',
        query: { deviceId: row.id }
      })
    },
    startAutoRefresh() {
      this.refreshTimer = setInterval(() => {
        this.refreshData()
      }, 30000) // 30秒自动刷新
    },
    stopAutoRefresh() {
      clearInterval(this.refreshTimer)
    }
  }
}
</script>


<style lang="scss" scoped>
.data-monitor-standalone {
  background-color: #0a192f;
  min-height: 100vh;
  color: #ccd6f6;
}

.standalone-navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 30px;
  background-color: rgba(16, 42, 87, 0.9);
  position: fixed;
  width: 100%;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);

  .logo {
    display: flex;
    align-items: center;

    .logo-icon {
      width: 36px;
      height: 36px;
      background: linear-gradient(135deg, #00d4ff 0%, #0072ff 100%);
      border-radius: 6px;
      margin-right: 12px;
    }

    .logo-text {
      font-size: 18px;
      font-weight: bold;
      background: linear-gradient(90deg, #00d4ff, #0072ff);
      background-clip: text;
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }

  .nav-controls {
    display: flex;
    align-items: center;

    .last-update {
      margin-left: 20px;
      font-size: 14px;
      color: #8892b0;
    }
  }
}

.monitor-content-wrapper {
  display: flex;
  padding-top: 80px;
  max-width: 1600px;
  margin: 0 auto;
  min-height: calc(100vh - 80px);
}

.data-nav-panel {
  width: 300px;
  padding: 20px;
  background-color: #112240;
  border-radius: 8px;
  margin-right: 20px;
  height: fit-content;
  position: sticky;
  top: 90px;

  .search-box {
    margin-bottom: 20px;
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

    .building-tag {
      background-color: rgba(100, 255, 218, 0.1);
      border-color: rgba(100, 255, 218, 0.3);
      color: #64ffda;
    }
  }

  .status-indicators {
    display: flex;
    justify-content: space-between;
    margin-bottom: 25px;

    .indicator {
      display: flex;
      align-items: center;
      padding: 8px 12px;
      border-radius: 4px;
      font-size: 14px;

      i {
        margin-right: 6px;
        font-size: 16px;
      }

      &.online {
        background-color: rgba(46, 204, 113, 0.1);
        color: #2ecc71;
      }

      &.warning {
        background-color: rgba(231, 76, 60, 0.1);
        color: #e74c3c;
      }
    }
  }

  .device-tree {
    ::v-deep .el-tree {
      background: transparent;
      color: #ccd6f6;

      .el-tree-node__content:hover {
        background-color: rgba(100, 255, 218, 0.1);
      }

      .el-tree-node:focus>.el-tree-node__content {
        background-color: rgba(0, 210, 255, 0.2);
      }

      .el-tree-node__content {
        height: 36px;
      }
    }
  }
}

.data-display-panel {
  flex: 1;
  background-color: #112240;
  padding: 20px;
  border-radius: 8px;

  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .table-title {
      color: #64ffda;
      font-size: 18px;
      margin: 0;
    }
  }

  .device-cell {
    display: flex;
    align-items: center;

    i {
      margin-right: 8px;
      color: #0072ff;
    }
  }

  .pagination-wrapper {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.detail-btn {
  color: #64ffda !important;
  padding: 5px !important;
}

.trend-btn {
  color: #00d4ff !important;
  padding: 5px !important;
}

::v-deep .device-detail-dialog {
  background: #0a192f;
  border: 1px solid #1f3a68;

  .el-dialog__header {
    background: #112240;
    border-bottom: 1px solid #1f3a68;

    .el-dialog__title {
      color: #64ffda;
    }
  }

  .el-dialog__body {
    padding: 20px;
    background: #0a192f;
    color: #ccd6f6;
  }
}
</style>