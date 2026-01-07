<template>
  <div class="home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用 AI Community Web</span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover">
            <el-icon :size="40" color="#409EFF"><Promotion /></el-icon>
            <h3>Vue 3</h3>
            <p>使用最新的 Vue 3 Composition API</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <el-icon :size="40" color="#67C23A"><Document /></el-icon>
            <h3>TypeScript</h3>
            <p>完整的 TypeScript 类型支持</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <el-icon :size="40" color="#E6A23C"><ElementPlus /></el-icon>
            <h3>Element Plus</h3>
            <p>丰富的 UI 组件库</p>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="demo-card">
      <template #header>
        <span>示例组件</span>
      </template>
      <el-space direction="vertical" size="large" style="width: 100%">
        <el-button type="primary">主要按钮</el-button>
        <el-button type="success">成功按钮</el-button>
        <el-button type="info">信息按钮</el-button>
        <el-button type="warning">警告按钮</el-button>
        <el-button type="danger">危险按钮</el-button>
      </el-space>

      <el-divider />

      <el-form :model="form" label-width="100px" style="max-width: 500px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Promotion, Document, ElementPlus } from '@element-plus/icons-vue'

const form = ref({
  username: '',
  password: ''
})

const handleSubmit = () => {
  ElMessage.success('提交成功！')
  console.log('表单数据:', form.value)
}

const handleReset = () => {
  form.value = {
    username: '',
    password: ''
  }
  ElMessage.info('表单已重置')
}
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.el-col {
  margin-bottom: 20px;
}

.el-card {
  text-align: center;
  padding: 20px;
}

.el-card h3 {
  margin: 10px 0;
  color: #303133;
}

.el-card p {
  color: #909399;
  font-size: 14px;
}

.demo-card {
  margin-top: 20px;
}
</style>
