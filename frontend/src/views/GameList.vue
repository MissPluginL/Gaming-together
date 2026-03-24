<template>
  <div class="game-list-container">
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <h1 class="logo">Gamerr</h1>
        </div>
        <div class="header-right">
          <span class="welcome">欢迎，{{ user?.nickname || user?.username }}</span>
          <el-button type="primary" @click="handleAdd">新增游戏</el-button>
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
              <div class="card-header">
                <span>游戏列表</span>
              </div>
            </template>

            <div class="search-bar">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索游戏名称"
                style="width: 200px; margin-right: 10px"
                @keyup.enter="handleSearch"
              />
              <el-select
                v-model="searchCategory"
                placeholder="选择分类"
                style="width: 150px; margin-right: 10px"
                clearable
              >
                <el-option
                  v-for="category in categories"
                  :key="category"
                  :label="category"
                  :value="category"
                />
              </el-select>
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="handleReset">重置</el-button>
            </div>

            <el-table :data="gameList" style="width: 100%; margin-top: 20px" v-loading="loading">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column label="封面" width="120">
                <template #default="{ row }">
                  <el-image
                    :src="row.coverImage || '/placeholder.png'"
                    style="width: 80px; height: 60px"
                    fit="cover"
                  />
                </template>
              </el-table-column>
              <el-table-column prop="name" label="游戏名称" />
              <el-table-column prop="category" label="分类" width="120" />
              <el-table-column prop="platform" label="平台" width="120" />
              <el-table-column prop="developer" label="开发商" />
              <el-table-column prop="price" label="价格" width="100">
                <template #default="{ row }">
                  ¥{{ row.price }}
                </template>
              </el-table-column>
              <el-table-column prop="reservationCount" label="预约人数" width="100" />
              <el-table-column label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getStatusType(row.status)">
                    {{ getStatusText(row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="{ row }">
                  <el-button type="primary" size="small" @click="handleDetail(row.id)">查看</el-button>
                  <el-button type="warning" size="small" @click="handleEdit(row.id)">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <el-pagination
              v-model:current-page="pagination.current"
              v-model:page-size="pagination.size"
              :total="pagination.total"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              style="margin-top: 20px; justify-content: center"
              @size-change="loadGames"
              @current-change="loadGames"
            />
          </el-card>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getGameList, deleteGame } from '../utils/gameApi'

const router = useRouter()
const user = ref(null)
const gameList = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const searchCategory = ref('')
const categories = ['动作', '冒险', '角色扮演', '策略', '射击', '休闲', '模拟', '竞技']

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const activeMenu = computed(() => router.currentRoute.value.path)

const loadGames = async () => {
  loading.value = true
  try {
    const res = await getGameList({
      current: pagination.current,
      size: pagination.size,
      keyword: searchKeyword.value,
      category: searchCategory.value
    })
    gameList.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadGames()
}

const handleReset = () => {
  searchKeyword.value = ''
  searchCategory.value = ''
  handleSearch()
}

const handleAdd = () => {
  router.push('/games/add')
}

const handleDetail = (id) => {
  router.push(`/games/${id}`)
}

const handleEdit = (id) => {
  router.push(`/games/edit/${id}`)
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该游戏吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteGame(id)
      ElMessage.success('删除成功')
      loadGames()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

const getStatusType = (status) => {
  const types = { 0: 'info', 1: 'warning', 2: 'success' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '未上线', 1: '预约中', 2: '已上线' }
  return texts[status] || '未知'
}

onMounted(() => {
  user.value = JSON.parse(localStorage.getItem('user') || '{}')
  loadGames()
})
</script>

<style scoped>
.game-list-container {
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

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.search-bar {
  display: flex;
  align-items: center;
}
</style>
