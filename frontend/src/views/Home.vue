<template>
  <div class="home-container">
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <h1 class="logo">Gamerr</h1>
        </div>
        <div class="header-right">
          <span class="welcome">欢迎，{{ user?.nickname || user?.username }}</span>
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              <el-avatar :size="36" :src="user?.avatar" />
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="reservations">我的预约</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
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
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-icon games">
                    <el-icon :size="40"><Gamepad /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ statistics.games }}</div>
                    <div class="stat-label">游戏总数</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-icon reservations">
                    <el-icon :size="40"><Calendar /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ statistics.reservations }}</div>
                    <div class="stat-label">我的预约</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-icon users">
                    <el-icon :size="40"><User /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ statistics.users }}</div>
                    <div class="stat-label">注册用户</div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-card class="recent-games" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>热门游戏</span>
                <el-button type="primary" @click="$router.push('/games')">查看全部</el-button>
              </div>
            </template>
            <el-row :gutter="20">
              <el-col :span="6" v-for="game in hotGames" :key="game.id">
                <el-card :body-style="{ padding: '0px' }" class="game-card" @click="goToDetail(game.id)">
                  <img :src="game.coverImage || '/placeholder.png'" class="game-image" />
                  <div class="game-info">
                    <div class="game-name">{{ game.name }}</div>
                    <div class="game-category">{{ game.category }}</div>
                    <div class="game-reservation">
                      <el-icon><Calendar /></el-icon>
                      {{ game.reservationCount }}人预约
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
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
import { getAllGames, getGameList } from '../utils/gameApi'
import { getMyReservations } from '../utils/reservationApi'

const router = useRouter()
const user = ref(null)
const hotGames = ref([])
const statistics = reactive({
  games: 0,
  reservations: 0,
  users: 1
})

const activeMenu = computed(() => router.currentRoute.value.path)

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'reservations':
      router.push('/games')
      break
    case 'logout':
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      ElMessage.success('已退出登录')
      router.push('/login')
      break
  }
}

const goToDetail = (id) => {
  router.push(`/games/${id}`)
}

onMounted(async () => {
  user.value = JSON.parse(localStorage.getItem('user') || '{}')
  
  try {
    const gamesRes = await getAllGames()
    hotGames.value = gamesRes.data.slice(0, 4)
    statistics.games = gamesRes.data.length
  } catch (error) {
    console.error(error)
  }

  try {
    const reservationsRes = await getMyReservations()
    statistics.reservations = reservationsRes.data.length
  } catch (error) {
    console.error(error)
  }
})
</script>

<style scoped>
.home-container {
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

.user-dropdown {
  cursor: pointer;
}

.sidebar {
  background: white;
  min-height: calc(100vh - 60px);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.main-content {
  padding: 20px;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 70px;
  height: 70px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon.games {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.reservations {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.users {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.recent-games {
  margin-top: 20px;
}

.game-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.game-card:hover {
  transform: translateY(-5px);
}

.game-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.game-info {
  padding: 12px;
}

.game-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.game-category {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.game-reservation {
  font-size: 12px;
  color: #667eea;
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>
