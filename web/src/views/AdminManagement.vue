<template>
  <div class="admin-management-container">
    <Navbar />
    <h1 class="page-title">
      <i class="el-icon-s-tools"></i> 系统管理中心
    </h1>

    <div class="admin-content-wrapper">
      <el-tabs v-model="activeTab" class="admin-tabs">
        <!-- 用户管理 -->
        <el-tab-pane label="用户管理" name="users">
          <div class="panel-header">
            <h3 class="panel-title">用户列表</h3>
            <div class="panel-actions">
              <button class="action-btn" @click="showAddUserDialog">
                <i class="el-icon-plus"></i> 添加用户
              </button>
              <div class="search-box">
                <i class="el-icon-search"></i>
                <input 
                  type="text" 
                  placeholder="搜索用户..." 
                  class="search-input" 
                  v-model="userSearchQuery"
                >
              </div>
            </div>
          </div>

          <el-table
            :data="filteredUsers"
            class="admin-table"
            border
            v-loading="loading"
          >
            <el-table-column prop="username" label="用户名" width="180" />
            <el-table-column prop="email" label="邮箱" width="220" />
            <el-table-column prop="role" label="角色" width="120">
              <template #default="{row}">
                <el-tag :type="getRoleTagType(row.role)" class="role-tag">{{ row.role }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column prop="status" label="状态" width="120">
              <template #default="{row}">
                <el-switch
                  v-model="row.status"
                  active-value="active"
                  inactive-value="inactive"
                  active-color="#64ffda"
                  inactive-color="#ff6b35"
                  @change="updateUserStatus(row)"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="220">
              <template #default="{row}">
                <button class="edit-btn" @click="editUser(row)">
                  <i class="el-icon-edit"></i> 编辑
                </button>
                <button 
                  class="delete-btn" 
                  @click="confirmDeleteUser(row)"
                  :disabled="row.role === 'admin'"
                >
                  <i class="el-icon-delete"></i> 删除
                </button>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination-wrapper">
            <el-pagination
              :current-page="currentPage"
              :page-sizes="[10, 20, 50]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalUsers"
              @size-change="handleSizeChange"
              @current-change="handlePageChange"
            />
          </div>
        </el-tab-pane>
        
        <!-- 权限管理 -->
        <el-tab-pane label="权限管理" name="permissions">
          <div class="panel-header">
            <h3 class="panel-title">系统权限配置</h3>
            <div class="panel-actions">
              <button class="action-btn" @click="savePermissions">
                <i class="el-icon-check"></i> 保存权限
              </button>
              <button class="action-btn" @click="resetPermissions">
                <i class="el-icon-refresh-left"></i> 重置
              </button>
            </div>
          </div>

          <el-collapse v-model="activePermissions" accordion class="permission-collapse">
            <el-collapse-item 
              v-for="module in permissionModules" 
              :key="module.name"
              :name="module.name"
              :title="module.title"
            >
              <el-checkbox-group v-model="selectedPermissions" class="permission-group">
                <div class="permission-item" v-for="perm in module.permissions" :key="perm">
                  <el-checkbox :label="perm" class="permission-checkbox">{{ perm }}</el-checkbox>
                </div>
              </el-checkbox-group>
            </el-collapse-item>
          </el-collapse>
        </el-tab-pane>
        
        <!-- 系统设置 -->
        <el-tab-pane label="系统设置" name="settings">
          <div class="panel-header">
            <h3 class="panel-title">系统参数配置</h3>
            <div class="panel-actions">
              <button class="action-btn" @click="saveSystemSettings">
                <i class="el-icon-check"></i> 保存设置
              </button>
            </div>
          </div>

          <el-form label-width="180px" label-position="left" class="settings-form">
            <el-form-item label="系统名称">
              <el-input v-model="systemSettings.name" class="settings-input" />
            </el-form-item>
            <el-form-item label="系统LOGO">
              <el-upload
                action=""
                :auto-upload="false"
                :show-file-list="false"
                :on-change="handleLogoChange"
                class="logo-upload"
              >
                <button class="upload-btn">
                  <i class="el-icon-upload"></i> 点击上传
                </button>
                <div class="logo-preview" v-if="logoPreview">
                  <img :src="logoPreview" alt="系统LOGO" />
                </div>
              </el-upload>
            </el-form-item>
            <el-form-item label="数据保留时间">
              <el-select v-model="systemSettings.dataRetention" class="settings-select">
                <el-option label="3个月" value="3" />
                <el-option label="6个月" value="6" />
                <el-option label="1年" value="12" />
                <el-option label="永久保留" value="0" />
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <!-- 添加/编辑用户对话框 -->
    <el-dialog 
      :title="userDialogTitle" 
      :visible.sync="showUserDialog"
      width="600px"
      class="user-dialog"
    >
      <el-form :model="currentUser" label-width="100px" :rules="userRules" ref="userForm" class="dialog-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="currentUser.username" class="dialog-input" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="currentUser.email" class="dialog-input" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="isNewUser">
          <el-input v-model="currentUser.password" type="password" class="dialog-input" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword" v-if="isNewUser">
          <el-input v-model="currentUser.confirmPassword" type="password" class="dialog-input" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="currentUser.role" class="dialog-select">
            <el-option label="管理员" value="admin" />
            <el-option label="普通用户" value="user" />
            <el-option label="操作员" value="operator" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <button class="cancel-btn" @click="showUserDialog = false">取消</button>
        <button class="confirm-btn" @click="submitUserForm">确定</button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
import api from '@/api/index.js'
import { mapState, mapGetters, mapActions } from 'vuex'

export default {
  name: 'AdminManagement',
  components: {
    Navbar
  },
  data() {
    return {
      activeTab: 'users',
      loading: false,
      userSearchQuery: '',
      currentPage: 1,
      pageSize: 10,
      totalUsers: 0,
      users: [],
      activePermissions: [],
      selectedPermissions: [],
      permissionModules: [
        {
          name: 'dashboard',
          title: '仪表盘模块',
          permissions: [
            'dashboard_view',
            'dashboard_export'
          ]
        },
        {
          name: 'monitor',
          title: '监控模块',
          permissions: [
            'monitor_view',
            'monitor_manage',
            'monitor_alert'
          ]
        },
        {
          name: 'system',
          title: '系统模块',
          permissions: [
            'system_settings',
            'system_users',
            'system_permissions'
          ]
        }
      ],
      systemSettings: {
        name: '供暖系统管理平台',
        logo: '',
        dataRetention: '12'
      },
      logoPreview: '',
      showUserDialog: false,
      isNewUser: false,
      currentUser: {
        id: '',
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: 'user',
        status: 'active'
      },
      userRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: this.validateConfirmPassword, trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
     // 使用 mapState 映射 Vuex state
  ...mapState(['user', 'token', 'role']),
  
  // 使用 mapGetters 映射 Vuex getters
  ...mapGetters(['isAuthenticated', 'isAdmin']),

    filteredUsers() {
      return this.users.filter(user => 
        user.username.toLowerCase().includes(this.userSearchQuery.toLowerCase()) ||
        user.email.toLowerCase().includes(this.userSearchQuery.toLowerCase())
      )
    },
    userDialogTitle() {
      return this.isNewUser ? '添加新用户' : '编辑用户信息'
    }
  },
  created() {
    this.fetchUsers()
    this.fetchPermissions()
    this.fetchSystemSettings()
  },
  methods: {
    ...mapActions(['login', 'logout', 'initializeAuth']),

   async fetchUsers() {
    this.loading = true
    try {
      // 检查是否已认证
      if (!this.isAuthenticated || !this.isAdmin) {
        throw new Error('无权限访问')
      }
      
      // 使用真实的API调用
      const response = await api.getUsers(this.token) // 假设api有getUsers方法
      
      this.users = response.data.users // 根据实际API响应结构调整
      this.totalUsers = this.users.length
      } catch (error) {
      console.error('获取用户列表失败:', error)
      this.$message.error('获取用户列表失败: ' + error.message)
      
      // 如果是认证问题，可以尝试重新登录或跳转到登录页
      if (error.message.includes('无权限') || error.message.includes('认证')) {
        this.$router.push('/login')
      }
      } finally {
        this.loading = false
      }
    },
    async fetchPermissions() {
      // 模拟获取权限数据
      this.selectedPermissions = ['dashboard_view', 'monitor_view']
    },
    async fetchSystemSettings() {
      // 模拟获取系统设置
    },
    getRoleTagType(role) {
      switch (role) {
        case 'admin': return 'danger'
        case 'operator': return 'warning'
        default: return ''
      }
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.fetchUsers()
    },
    handlePageChange(val) {
      this.currentPage = val
      this.fetchUsers()
    },
    showAddUserDialog() {
      this.isNewUser = true
      this.currentUser = {
        id: '',
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: 'user',
        status: 'active'
      }
      this.showUserDialog = true
      this.$nextTick(() => {
        this.$refs.userForm.clearValidate()
      })
    },
    editUser(user) {
      this.isNewUser = false
      this.currentUser = { ...user }
      this.showUserDialog = true
    },
    validateConfirmPassword(rule, value, callback) {
      if (value !== this.currentUser.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    },
    submitUserForm() {
      this.$refs.userForm.validate(async valid => {
        if (!valid) return
        
        try {
          this.loading = true
          // 模拟API调用
          await new Promise(resolve => setTimeout(resolve, 800))
          
          if (this.isNewUser) {
            // 添加用户
            const newUser = {
              ...this.currentUser,
              id: Date.now().toString(),
              createdAt: new Date().toISOString()
            }
            this.users.unshift(newUser)
            this.$message.success('用户添加成功')
          } else {
            // 更新用户
            const index = this.users.findIndex(u => u.id === this.currentUser.id)
            if (index !== -1) {
              this.users.splice(index, 1, this.currentUser)
            }
            this.$message.success('用户信息更新成功')
          }
          
          this.showUserDialog = false
        } catch (error) {
          console.error('保存用户失败:', error)
          this.$message.error('保存用户失败')
        } finally {
          this.loading = false
        }
      })
    },
    confirmDeleteUser(user) {
      this.$confirm(`确定要删除用户 "${user.username}" 吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'confirm-dialog'
      }).then(async () => {
        try {
          this.loading = true
          // 模拟API调用
          await new Promise(resolve => setTimeout(resolve, 500))
          
          this.users = this.users.filter(u => u.id !== user.id)
          this.totalUsers = this.users.length
          this.$message.success('用户删除成功')
        } catch (error) {
          console.error('删除用户失败:', error)
          this.$message.error('删除用户失败')
        } finally {
          this.loading = false
        }
      }).catch(() => {})
    },
    updateUserStatus(user) {
      this.$message.success(`用户 ${user.username} 状态已更新`)
    },
    savePermissions() {
      this.$message.success('权限设置已保存')
    },
    resetPermissions() {
      this.selectedPermissions = ['dashboard_view', 'monitor_view']
      this.$message.info('权限设置已重置')
    },
    handleLogoChange(file) {
      const reader = new FileReader()
      reader.onload = () => {
        this.logoPreview = reader.result
      }
      reader.readAsDataURL(file.raw)
    },
    saveSystemSettings() {
      this.$message.success('系统设置已保存')
    }
  }
}
</script>

<style scoped>
.admin-management-container {
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
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-title i {
  margin-right: 15px;
  font-size: 32px;
}

.admin-content-wrapper {
  max-width: 1400px;
  margin: 0 auto;
}

.admin-tabs {
  background-color: #112240;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  overflow: hidden;
}

.admin-tabs >>> .el-tabs__header {
  background-color: #112240;
  padding-left: 20px;
  margin: 0;
  border-bottom: 1px solid #1f3a68;
}

.admin-tabs >>> .el-tabs__nav-wrap::after {
  background-color: #1f3a68;
}

.admin-tabs >>> .el-tabs__item {
  color: #ccd6f6;
  font-weight: 500;
  padding: 0 25px;
}

.admin-tabs >>> .el-tabs__item.is-active {
  color: #64ffda;
}

.admin-tabs >>> .el-tabs__content {
  padding: 20px;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 10px;
}

.panel-title {
  color: #64ffda;
  font-size: 18px;
  margin: 0;
}

.panel-actions {
  display: flex;
  gap: 15px;
  align-items: center;
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

.search-box {
  display: flex;
  align-items: center;
  background-color: #0a192f;
  border: 1px solid #1f3a68;
  border-radius: 4px;
  padding: 5px 10px;
}

.search-box i {
  color: #64ffda;
  margin-right: 8px;
}

.search-box input {
  background: transparent;
  border: none;
  color: #ccd6f6;
  outline: none;
  min-width: 200px;
}

.admin-table {
  width: 100%;
  background-color: #112240;
  color: #ccd6f6;
}

.admin-table >>> .el-table__header-wrapper th {
  background-color: #112240;
  color: #64ffda;
  font-weight: bold;
  border-bottom: 1px solid #1f3a68;
}

.admin-table >>> .el-table__body-wrapper td {
  background-color: #0a192f;
  border-bottom: 1px solid #1f3a68;
  color: #ccd6f6;
}

.admin-table >>> .el-table--striped .el-table__body tr.el-table__row--striped td {
  background-color: rgba(16, 36, 69, 0.5)!important;
}

.admin-table >>> .el-table__body tr:hover > td {
  background-color: rgba(100, 255, 218, 0.1) !important;
}

.role-tag {
  border-radius: 4px;
  font-weight: 500;
}

.edit-btn, .delete-btn {
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

.edit-btn {
  color: #64ffda;
}

.edit-btn:hover {
  background-color: rgba(100, 255, 218, 0.1);
}

.delete-btn {
  color: #ff6b35;
}

.delete-btn:hover {
  background-color: rgba(255, 107, 53, 0.1);
}

.delete-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.pagination-wrapper >>> .el-pagination {
  background-color: #112240;
  padding: 10px;
  border-radius: 4px;
}

.pagination-wrapper >>> .el-pagination button:disabled {
  background-color: #0a192f !important;
  color: #666 !important;
}

.pagination-wrapper >>> .el-pager li {
  background-color: #0a192f;
  color: #ccd6f6;
  border: 1px solid #1f3a68;
}

.pagination-wrapper >>> .el-pager li.active {
  background-color: #0072ff;
  color: white;
}

.pagination-wrapper >>> .el-pagination__total {
  color: #ccd6f6;
}

.permission-collapse {
  border: none;
}

.permission-collapse >>> .el-collapse-item__header {
  background-color: #0a192f;
  color: #64ffda;
  border-bottom: 1px solid #1f3a68;
  padding: 0 20px;
  font-weight: 500;
}

.permission-collapse >>> .el-collapse-item__content {
  padding: 20px;
  background-color: #112240;
  border-bottom: 1px solid #1f3a68;
}

.permission-group {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.permission-item {
  width: calc(33.333% - 10px);
}

.permission-checkbox >>> .el-checkbox__label {
  color: #ccd6f6;
}

.settings-form {
  padding: 20px;
  background-color: #112240;
  border-radius: 8px;
}

.settings-input >>> .el-input__inner {
  background-color: #0a192f;
  border: 1px solid #1f3a68;
  color: #ccd6f6;
}

.settings-select >>> .el-input__inner {
  background-color: #0a192f;
  border: 1px solid #1f3a68;
  color: #ccd6f6;
}

.logo-upload {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.upload-btn {
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
  width: fit-content;
}

.upload-btn:hover {
  background-color: rgba(100, 255, 218, 0.2);
}

.logo-preview {
  margin-top: 10px;
}

.logo-preview img {
  max-width: 200px;
  max-height: 100px;
  border: 1px solid #1f3a68;
  border-radius: 4px;
}

.user-dialog {
  border-radius: 8px;
  overflow: hidden;
}

.user-dialog >>> .el-dialog__header {
  background-color: #112240;
  border-bottom: 1px solid #1f3a68;
  padding: 15px 20px;
}

.user-dialog >>> .el-dialog__title {
  color: #64ffda;
  font-weight: bold;
}

.user-dialog >>> .el-dialog__body {
  background-color: #112240;
  padding: 20px;
}

.user-dialog >>> .el-dialog__footer {
  background-color: #112240;
  border-top: 1px solid #1f3a68;
  padding: 15px 20px;
}

.dialog-form {
  padding: 10px;
}

.dialog-input >>> .el-input__inner {
  background-color: #0a192f;
  border: 1px solid #1f3a68;
  color: #ccd6f6;
}

.dialog-select >>> .el-input__inner {
  background-color: #0a192f;
  border: 1px solid #1f3a68;
  color: #ccd6f6;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.cancel-btn, .confirm-btn {
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.cancel-btn {
  background-color: transparent;
  border: 1px solid #1f3a68;
  color: #ccd6f6;
}

.cancel-btn:hover {
  background-color: rgba(255, 255, 255, 0.05);
}

.confirm-btn {
  background-color: rgba(100, 255, 218, 0.1);
  border: 1px solid rgba(100, 255, 218, 0.3);
  color: #64ffda;
}

.confirm-btn:hover {
  background-color: rgba(100, 255, 218, 0.2);
}

.confirm-dialog {
  background-color: #112240 !important;
  border: 1px solid #1f3a68 !important;
}

.confirm-dialog >>> .el-message-box__title {
  color: #64ffda;
}

.confirm-dialog >>> .el-message-box__content {
  color: #ccd6f6;
}

.confirm-dialog >>> .el-button {
  background-color: #0a192f;
  border: 1px solid #1f3a68;
  color: #ccd6f6;
}

.confirm-dialog >>> .el-button--primary {
  background-color: rgba(100, 255, 218, 0.1);
  border: 1px solid rgba(100, 255, 218, 0.3);
  color: #64ffda;
}

@media (max-width: 768px) {
  .panel-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .panel-actions {
    width: 100%;
    flex-direction: column;
    align-items: flex-start;
  }
  
  .search-box {
    width: 100%;
  }
  
  .permission-item {
    width: 100%;
  }
  
  .user-dialog {
    width: 90% !important;
  }
}
</style>