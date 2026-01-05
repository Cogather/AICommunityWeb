<template>
  <div class="users-view">
    <div class="page-header">
      <h1>AI 使用达人</h1>
      <p>发现社区中最活跃的 AI 使用者</p>
    </div>
    
    <div class="users-container">
      <el-row :gutter="24">
        <el-col :xs="12" :sm="8" :md="6" v-for="user in users" :key="user.id" style="margin-bottom: 24px;">
          <el-card class="user-card" shadow="hover">
            <div class="user-avatar-wrapper">
              <el-avatar :size="80" :src="user.avatar" />
              <el-tag class="level-tag" :type="getLevelType(user.level)">Lv.{{ user.level }}</el-tag>
            </div>
            <h3>{{ user.name }}</h3>
            <p class="user-title">{{ user.title }}</p>
            <div class="user-stats">
              <div class="stat-item">
                <span class="stat-value">{{ user.contributions }}</span>
                <span class="stat-label">贡献</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ user.followers }}</span>
                <span class="stat-label">关注</span>
              </div>
            </div>
            <el-button type="primary" size="small" style="width: 100%; margin-top: 15px;">关注</el-button>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const users = ref([
  { id: 1, name: 'User_9527', title: 'AI 工具专家', level: 10, contributions: 128, followers: 256, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 2, name: 'User_1001', title: 'Prompt 工程师', level: 9, contributions: 95, followers: 189, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 3, name: 'User_2002', title: 'Agent 开发者', level: 8, contributions: 76, followers: 142, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 4, name: 'User_3003', title: 'AI 实践者', level: 7, contributions: 64, followers: 98, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 5, name: 'User_4004', title: '大模型研究者', level: 9, contributions: 112, followers: 203, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 6, name: 'User_5005', title: 'AI 工具创作者', level: 8, contributions: 88, followers: 167, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 7, name: 'User_6006', title: '社区贡献者', level: 6, contributions: 52, followers: 89, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 8, name: 'User_7007', title: 'AI 应用专家', level: 7, contributions: 71, followers: 125, avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
])

const getLevelType = (level: number) => {
  if (level >= 9) return 'danger'
  if (level >= 7) return 'warning'
  return 'success'
}
</script>

<style scoped lang="scss">
.users-view {
  min-height: 100vh;
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  
  h1 {
    font-size: 32px;
    color: #fff;
    margin-bottom: 10px;
  }
  
  p {
    font-size: 16px;
    color: rgba(255, 255, 255, 0.7);
  }
}

.users-container {
  margin-top: 30px;
}

.user-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  text-align: center;
  
  .user-avatar-wrapper {
    position: relative;
    display: inline-block;
    margin-bottom: 15px;
    
    .level-tag {
      position: absolute;
      bottom: -5px;
      right: -5px;
    }
  }
  
  h3 {
    color: #fff;
    font-size: 18px;
    margin: 15px 0 5px;
  }
  
  .user-title {
    color: rgba(255, 255, 255, 0.7);
    font-size: 14px;
    margin-bottom: 15px;
  }
  
  .user-stats {
    display: flex;
    justify-content: space-around;
    margin: 15px 0;
    
    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .stat-value {
        color: #fff;
        font-size: 20px;
        font-weight: bold;
        margin-bottom: 5px;
      }
      
      .stat-label {
        color: rgba(255, 255, 255, 0.6);
        font-size: 12px;
      }
    }
  }
}
</style>

