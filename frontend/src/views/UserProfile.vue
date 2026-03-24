<template>
  <div class="profile-container">
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <h1 class="logo">Gamerr</h1>
        </div>
        <div class="header-right">
          <span class="welcome">欢迎，{{ user?.nickname || user?.username }}</span>
          <el-button @click="handleLogout">退出</el-button>
        </div>
      </el-header>

      <el-container>
        <el-aside width="200px" class="sidebar">
          <el-menu :default-active="activeMenu" router>
            <el-menu-item index="/home">
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/games">
              <span>游戏列表</span>
            </el-menu-item>
            <el-menu-item index="/profile">
              <span>个人中心</span>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <el-main class="main-content">
          <el-card>
            <template #header>
              <span>个人中心</span>
            </template>

            <el-form
              ref="formRef"
              :model="form"
              :rules="rules"
              label-width="100px"
              style="max-width: 600px"
            >
              <el-form-item label="用户名">
                <el-input v-model="form.username" disabled />
              </el-form-item>

              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="form.nickname" placeholder="请输入昵称" />
              </el-form-item>

              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱" />
              </el-form-item>

              <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入手机号" />
              </el-form-item>

              <el-form-item label="头像URL">
                <el-input v-model="form.avatar" placeholder="请输入头像URL" />
              </el-form-item>

              <el-form-item>
                <el-button type="primary" :loading="loading" @click="handleSubmit">
                  保存修改
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <el-card style="margin-top: 20px">
            <template #header>
              <span>我的预约</span>
            </template>

            <el-table :data="reservations" style="width: 100%" v-loading="reservationsLoading">
              <el-table-column prop="id" label="预约ID" width="100" />
              <el-table-column label="预约时间" width="180">
                <template #default="{ row }">
                  {{ formatDate(row.reservationTime) }}
                </template>
              </el-table-column>
              <el-table-column label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getStatusType(row.status)">
                    {{ getStatusText(row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="备注">
                <template #default="{ row }">
                  {{ row.remark || '-' }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120">
                <template #default="{ row }">
                  <el-button
                    type="danger"
                    size="small"
                    :disabled="row.status === 0"
                    @click="handleCancel(row.id)"
                  >
                    取消预约
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { updateUser } from '../utils/userApi'
import { getMyReservations, cancelReservation } from '../utils/reservationApi'

const router = useRouter()
const user = ref(null)
const formRef = ref(null)
const loading = ref(false)
const reservations = ref([])
const reservationsLoading = ref(false)

const form = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: ''
})

const activeMenu = computed(() => router.currentRoute.value.path)

const rules = {
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await updateUser(user.value.id, form)
        user.value = { ...user.value, ...form }
        localStorage.setItem('user', JSON.stringify(user.value))
        ElMessage.success('保存成功')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

const loadReservations = async () => {
  reservationsLoading.value = true
  try {
    const res = await getMyReservations()
    reservations.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    reservationsLoading.value = false
  }
}

const handleCancel = async (id) => {
  try {
    await cancelReservation(id)
    ElMessage.success('取消预约成功')
    loadReservations()
  } catch (error) {
    console.error(error)
  }
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN')
}

const getStatusType = (status) => {
  const types = { 0: 'info', 1: 'success', 2: 'warning' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '已取消', 1: '已预约', 2: '已完成' }
  return texts[status] || '未知'
}

onMounted(() => {
  const storedUser = JSON.parse(localStorage.getItem('user') || '{}')
  user.value = storedUser
  Object.assign(form, storedUser)
  loadReservations()
})
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0 20px;
}

.logo {
  font-size: 24px;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.welcome {
  font-size: 14px;
}

.sidebar {
  background: white;
  min-height: calc(100vh - 60px);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.main-content {
  padding: 20px;
}
</style>
