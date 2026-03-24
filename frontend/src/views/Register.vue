<template>
  <div class="register-container">
    <div class="register-box">
      <h1 class="title">Gamerr</h1>
      <p class="subtitle">创建您的账号</p>
      
      <el-form ref="registerFormRef" :model="registerForm" :rules="rules" class="register-form">
        <el-form-item prop="username">
          <el-input 
            v-model="registerForm.username" 
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password"
            placeholder="请确认密码"
            prefix-icon="Lock"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="nickname">
          <el-input 
            v-model="registerForm.nickname" 
            placeholder="请输入昵称（可选）"
            prefix-icon="UserFilled"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="email">
          <el-input 
            v-model="registerForm.email" 
            placeholder="请输入邮箱（可选）"
            prefix-icon="Message"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="phone">
          <el-input 
            v-model="registerForm.phone" 
            placeholder="请输入手机号（可选）"
            prefix-icon="Phone"
            size="large"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            :loading="loading" 
            class="register-button"
            @click="handleRegister"
          >
            注 册
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-link">
        已有账号？<router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../utils/userApi'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  email: '',
  phone: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度需在3-20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度需在6-20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await register(registerForm)
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('user', JSON.stringify(res.data))
        ElMessage.success('注册成功')
        router.push('/home')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.title {
  font-size: 32px;
  color: #333;
  text-align: center;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: #999;
  text-align: center;
  margin-bottom: 30px;
}

.register-form {
  margin-top: 20px;
}

.register-button {
  width: 100%;
}

.login-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.login-link a {
  color: #667eea;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
