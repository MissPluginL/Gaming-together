<template>
  <div class="game-detail-container">
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <h1 class="logo">Gamerr</h1>
        </div>
        <div class="header-right">
          <span class="welcome">欢迎，{{ user?.nickname || user?.username }}</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </el-header>

      <el-main class="main-content">
        <el-card v-loading="loading">
          <div v-if="game" class="game-detail">
            <el-row :gutter="40">
              <el-col :span="10">
                <el-image
                  :src="game.coverImage || '/placeholder.png'"
                  class="cover-image"
                  fit="cover"
                />
              </el-col>
              <el-col :span="14">
                <div class="game-info">
                  <h1 class="game-title">{{ game.name }}</h1>
                  
                  <div class="info-row">
                    <span class="label">分类：</span>
                    <el-tag>{{ game.category }}</el-tag>
                  </div>
                  
                  <div class="info-row">
                    <span class="label">平台：</span>
                    <span>{{ game.platform }}</span>
                  </div>
                  
                  <div class="info-row">
                    <span class="label">开发商：</span>
                    <span>{{ game.developer }}</span>
                  </div>
                  
                  <div class="info-row">
                    <span class="label">发行日期：</span>
                    <span>{{ formatDate(game.releaseDate) }}</span>
                  </div>
                  
                  <div class="info-row">
                    <span class="label">价格：</span>
                    <span class="price">¥{{ game.price }}</span>
                  </div>
                  
                  <div class="info-row">
                    <span class="label">预约人数：</span>
                    <span class="reservation-count">{{ game.reservationCount }} 人</span>
                  </div>
                  
                  <div class="info-row">
                    <span class="label">状态：</span>
                    <el-tag :type="getStatusType(game.status)">
                      {{ getStatusText(game.status) }}
                    </el-tag>
                  </div>
                  
                  <div class="action-buttons">
                    <el-button 
                      type="primary" 
                      size="large" 
                      :disabled="game.status !== 1"
                      @click="handleReserve"
                    >
                      {{ game.status === 1 ? '立即预约' : '暂不可预约' }}
                    </el-button>
                    <el-button size="large" @click="handleBack">返回列表</el-button>
                  </div>
                </div>
              </el-col>
            </el-row>
            
            <el-divider />
            
            <div class="game-description">
              <h3>游戏介绍</h3>
              <p>{{ game.description || '暂无游戏介绍' }}</p>
            </div>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getGameDetail } from '../utils/gameApi'
import { createReservation } from '../utils/reservationApi'

const router = useRouter()
const route = useRoute()
const user = ref(null)
const game = ref(null)
const loading = ref(false)

const loadGame = async () => {
  loading.value = true
  try {
    const res = await getGameDetail(route.params.id)
    game.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleBack = () => {
  router.push('/games')
}

const handleReserve = async () => {
  try {
    await createReservation(game.value.id)
    ElMessage.success('预约成功！')
    loadGame()
  } catch (error) {
    console.error(error)
  }
}

const formatDate = (date) => {
  if (!date) return '未知'
  return new Date(date).toLocaleDateString('zh-CN')
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
  loadGame()
})
</script>

<style scoped>
.game-detail-container {
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

.main-content {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.game-detail {
  padding: 20px;
}

.cover-image {
  width: 100%;
  height: 400px;
  border-radius: 10px;
}

.game-info {
  padding: 20px 0;
}

.game-title {
  font-size: 32px;
  margin-bottom: 30px;
  color: #333;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  font-size: 16px;
}

.label {
  width: 100px;
  color: #999;
}

.price {
  font-size: 28px;
  color: #f5576c;
  font-weight: bold;
}

.reservation-count {
  color: #667eea;
  font-weight: bold;
}

.action-buttons {
  margin-top: 40px;
  display: flex;
  gap: 15px;
}

.game-description {
  padding: 20px 0;
}

.game-description h3 {
  margin-bottom: 15px;
  color: #333;
}

.game-description p {
  line-height: 1.8;
  color: #666;
  text-indent: 2em;
}
</style>
