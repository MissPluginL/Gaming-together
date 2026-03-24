<template>
  <div class="game-form-container">
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
        <el-card>
          <template #header>
            <span>{{ isEdit ? '编辑游戏' : '新增游戏' }}</span>
          </template>

          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="100px"
            style="max-width: 800px"
          >
            <el-form-item label="游戏名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入游戏名称" />
            </el-form-item>

            <el-form-item label="游戏分类" prop="category">
              <el-select v-model="form.category" placeholder="请选择分类">
                <el-option
                  v-for="category in categories"
                  :key="category"
                  :label="category"
                  :value="category"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="游戏平台" prop="platform">
              <el-input v-model="form.platform" placeholder="请输入游戏平台" />
            </el-form-item>

            <el-form-item label="开发商" prop="developer">
              <el-input v-model="form.developer" placeholder="请输入开发商" />
            </el-form-item>

            <el-form-item label="价格" prop="price">
              <el-input-number
                v-model="form.price"
                :precision="2"
                :step="1"
                :min="0"
                controls-position="right"
              />
            </el-form-item>

            <el-form-item label="发行日期" prop="releaseDate">
              <el-date-picker
                v-model="form.releaseDate"
                type="date"
                placeholder="选择发行日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>

            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="0">未上线</el-radio>
                <el-radio :label="1">预约中</el-radio>
                <el-radio :label="2">已上线</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="封面图片" prop="coverImage">
              <el-input v-model="form.coverImage" placeholder="请输入封面图片URL" />
            </el-form-item>

            <el-form-item label="游戏描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="6"
                placeholder="请输入游戏描述"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleSubmit">
                {{ isEdit ? '保存' : '创建' }}
              </el-button>
              <el-button @click="handleBack">取消</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createGame, updateGame, getGameDetail } from '../utils/gameApi'

const router = useRouter()
const route = useRoute()
const user = ref(null)
const formRef = ref(null)
const loading = ref(false)

const isEdit = computed(() => route.params.id)
const categories = ['动作', '冒险', '角色扮演', '策略', '射击', '休闲', '模拟', '竞技']

const form = reactive({
  name: '',
  category: '',
  platform: '',
  developer: '',
  price: 0,
  releaseDate: '',
  status: 1,
  coverImage: '',
  description: ''
})

const rules = {
  name: [
    { required: true, message: '请输入游戏名称', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  platform: [
    { required: true, message: '请输入游戏平台', trigger: 'blur' }
  ],
  developer: [
    { required: true, message: '请输入开发商', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (isEdit.value) {
          await updateGame(isEdit.value, form)
          ElMessage.success('更新成功')
        } else {
          await createGame(form)
          ElMessage.success('创建成功')
        }
        router.push('/games')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}

const handleBack = () => {
  router.push('/games')
}

const loadGame = async () => {
  if (!isEdit.value) return
  
  try {
    const res = await getGameDetail(isEdit.value)
    Object.assign(form, res.data)
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  user.value = JSON.parse(localStorage.getItem('user') || '{}')
  loadGame()
})
</script>

<style scoped>
.game-form-container {
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
}
</style>
