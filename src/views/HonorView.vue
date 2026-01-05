<template>
  <div class="honor-view">
    <div class="page-header">
      <h1>荣誉殿堂</h1>
      <p>Hall of Fame - 表彰社区杰出贡献者</p>
    </div>
    
    <div class="honor-container">
      <el-timeline>
        <el-timeline-item 
          v-for="(award, index) in awards" 
          :key="index"
          :timestamp="award.year"
          placement="top"
          :type="getAwardType(index)"
        >
          <el-card class="award-card">
            <div class="award-header">
              <div class="award-icon">{{ award.icon }}</div>
              <div class="award-info">
                <h3>{{ award.title }}</h3>
                <p>{{ award.desc }}</p>
              </div>
            </div>
            <div class="award-winners">
              <div class="winner-item" v-for="winner in award.winners" :key="winner">
                <el-avatar :size="40" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                <span>{{ winner }}</span>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const awards = ref([
  {
    year: '2026年度',
    icon: '🥇',
    title: '年度最佳贡献奖',
    desc: '表彰在 AI 社区建设中做出卓越贡献的开发者',
    winners: ['User_9527', 'User_1001', 'User_2002']
  },
  {
    year: '2025年度',
    icon: '🥈',
    title: '年度创新奖',
    desc: '表彰在 AI 应用创新方面有突出表现的开发者',
    winners: ['User_3003', 'User_4004']
  },
  {
    year: '2024年度',
    icon: '🥉',
    title: '年度最佳实践奖',
    desc: '表彰在 AI 实践应用中有优秀案例的开发者',
    winners: ['User_5005', 'User_6006', 'User_7007']
  }
])

const getAwardType = (index: number) => {
  const types = ['primary', 'success', 'warning']
  return types[index % types.length]
}
</script>

<style scoped lang="scss">
.honor-view {
  min-height: 100vh;
  padding: 40px 20px;
  max-width: 1000px;
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

.honor-container {
  margin-top: 30px;
}

.award-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  
  .award-header {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 20px;
    
    .award-icon {
      font-size: 48px;
      width: 60px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 12px;
    }
    
    .award-info {
      flex: 1;
      
      h3 {
        color: #fff;
        font-size: 20px;
        margin-bottom: 8px;
      }
      
      p {
        color: rgba(255, 255, 255, 0.7);
        font-size: 14px;
        margin: 0;
      }
    }
  }
  
  .award-winners {
    display: flex;
    gap: 15px;
    flex-wrap: wrap;
    
    .winner-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 8px 12px;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 8px;
      
      span {
        color: rgba(255, 255, 255, 0.9);
        font-size: 14px;
      }
    }
  }
}
</style>

