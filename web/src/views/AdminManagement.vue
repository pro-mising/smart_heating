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
            </div>
          </div>

          <el-table
            :data="filteredUsers"
            class="admin-table"
            border
            v-loading="loading"
          >
            <!-- 你可以根据需要自定义列 -->
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="username" label="用户名"/>
            <el-table-column prop="realname" label="真实姓名"/>
            <el-table-column prop="email" label="邮箱"/>
            <el-table-column prop="phone" label="电话"/>
            <el-table-column prop="department" label="部门"/>
            <el-table-column prop="roleLabel" label="权限" width="120" />
            <el-table-column label="操作" width="180">
               <template slot-scope="{ row }">
                  <el-button size="mini" type="primary" @click="editUser(row)">编辑</el-button>
                  <el-button size="mini" type="danger" @click="confirmDeleteUser(row)">删除</el-button>
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
        <el-form-item label="真实姓名" prop="realname">
          <el-input v-model="currentUser.realname" class="dialog-input" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="currentUser.email" class="dialog-input" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="currentUser.phone" class="dialog-input" />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="currentUser.department" class="dialog-input" />
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
      showUserDialog: false,
      isNewUser: false,
      currentUser: {
        id: '',
        username: '',
        realname: '',
        email: '',
        phone: ''
      },
      addUserDialogVisible: false,
      newUser: {
      username: '',
      realname: '',
      phone: '',
      email: '',
      department: '',
       },
      userRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        realname: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapState(['user', 'token', 'role']),
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
  },
  methods: {
    ...mapActions(['login', 'logout', 'initializeAuth']),

    async fetchUsers() {
  this.loading = true
  try {
    if (!this.isAuthenticated || !this.isAdmin) {
      throw new Error('无权限访问')
    }
    const response = await api.getUserList()

    console.log('接口返回数据:', response)
    console.log('完整返回数据结构:', JSON.stringify(response, null, 2))

    const userList = Array.isArray(response) ? response : []
    this.users = userList.map(user => ({
      id: user.id,
      username: user.username,
      realname: user.realname,
      email: user.email || '',  
      phone: user.phone || '',
      department: user.department || '',
      flag: user.flag || '0',
      roleLabel: user.flag === '1' ? '管理员' : '普通用户'
    }))

    this.totalUsers = this.users.length
  } catch (error) {
    console.error('获取用户列表失败:', error)
    this.$message.error('获取用户列表失败: ' + error.message)
    if (error.message.includes('无权限') || error.message.includes('认证')) {
      this.$router.push('/login')
    }
  } finally {
    this.loading = false
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
        realname: '',
        email: '',
        phone: ''
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

    async submitUserForm() {
    this.$refs.userForm.validate(async valid => {
    if (!valid) return
    try {
      this.loading = true
      if (this.isNewUser) {
        // 添加默认密码和确保部门不为空
        const response = await api.addUser(
          this.currentUser.username,
          this.currentUser.realname,
          '123456', // 默认密码
          this.currentUser.email,
          this.currentUser.phone,
          this.currentUser.department || '默认部门'
        )
        
        console.log('Add user response:', response)
        
        // 错误信息
         if (response.error_message && response.error_message !== 'success') {
          throw new Error(response.error_message)
        }
        
        const newUser = {
          ...this.currentUser,
          id: response.id || response.userId, 
          flag: '0',
          roleLabel: '普通用户'
        }
        
       this.users.unshift(newUser)
        this.totalUsers = this.users.length
        this.$message.success('用户添加成功')
      } else {
        console.log("登录后 Vuex 中的 token：", this.$store.state.token);
        
        const response = await api.adminUpdateUserInfo(
      this.currentUser.username,
      this.currentUser.realname,
      this.currentUser.email,
      this.currentUser.phone,
      this.currentUser.department
    )

    // 错误日志
    console.log('更新响应:', JSON.stringify(response, null, 2))
    
    if (response.error_message && response.error_message !== 'success') {
      throw new Error(response.error_message)
    }
    
    //成功处理
        if (response.error_message === 'success') {
          this.$message.success('用户信息更新成功')
        } else {
          this.$message.success('用户信息更新成功')
        }
      }
      
      this.showUserDialog = false
      this.fetchUsers()

    } catch (error) {
      console.error('保存用户失败:', error)
      if (error.message === 'success') {
        this.$message.success('操作成功')
        this.showUserDialog = false
        this.fetchUsers()
      } else {
        this.$message.error('保存用户失败: ' + error.message)
      }
    } finally {
      this.loading = false
    }
  })
},

  resetAddUserForm() {
    this.newUser = {
      username: '',
      realname: '',
      phone: '',
      email: '',
      department: '',
    }
  },

    async confirmDeleteUser(user) {
  this.$confirm(`确定要删除用户 "${user.username}" 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      this.loading = true
      const response = await api.removeUser(user.username)
      
      // 检查响应结果
      if (response.error_message && response.error_message !== 'success') {
        throw new Error(response.error_message)
      }
      
      // 从本地列表中移除
      this.users = this.users.filter(u => u.id !== user.id)
      this.totalUsers = this.users.length
      
      // 特殊处理"success"消息
      if (response.error_message === 'success') {
        this.$message.success('用户删除成功')
      } else {
        this.$message.success('用户删除成功')
      }
    } catch (error) {
      console.error('删除用户失败:', error)
      this.$message.error('删除用户失败: ' + 
        (error.response?.data?.message || error.message))
    } finally {
      this.loading = false
    }
  }).catch(() => {
    this.$message.info('已取消删除')
  })
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