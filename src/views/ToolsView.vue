<template>
  <div class="tools-view">
    <div class="page-header">
      <h1>AI 工具专区</h1>
      <p>发现最新、最强大的 AI 工具，提升你的工作效率和创造力</p>
    </div>
    
    <div class="tools-grid">
      <el-row :gutter="20">
        <el-col 
          :xs="12" 
          :sm="6" 
          :md="getColSpan(tools.length)"
          v-for="tool in tools" 
          :key="tool.id" 
          style="margin-bottom: 20px;"
        >
          <div 
            class="tool-card glass-card hover-effect" 
            @click="handleToolClick(tool)"
          >
            <div class="tool-logo-wrapper">
              <img 
                v-if="tool.logo" 
                :src="tool.logo" 
                :alt="tool.name" 
                class="tool-logo"
              />
              <div 
                v-else 
                class="tool-icon" 
                :style="{ background: tool.color || '#409eff' }"
              >
                {{ tool.name[0] }}
              </div>
            </div>
            <div class="tool-info">
              <h4>{{ tool.name }}</h4>
              <p>{{ tool.desc }}</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const tools = ref([
  { 
    id: 1,
    name: 'TestMate', 
    desc: '自动化测试助手', 
    logo: 'https://picsum.photos/80/80?random=1',
    link: '/tools/testmate',
    color: '#36cfc9' 
  },
  { 
    id: 2,
    name: 'CodeMate', 
    desc: '智能代码补全', 
    logo: 'https://picsum.photos/80/80?random=2',
    link: '/tools/codemate',
    color: '#9254de' 
  },
  { 
    id: 3,
    name: '云集', 
    desc: '云端计算集群', 
    logo: 'https://picsum.photos/80/80?random=3',
    link: '/tools/yunji',
    color: '#597ef7' 
  },
  { 
    id: 4,
    name: '云见', 
    desc: '智能监控平台', 
    logo: 'https://picsum.photos/80/80?random=4',
    link: '/tools/yunjian',
    color: '#ff9c6e' 
  },
  { 
    id: 5,
    name: '扶摇', 
    desc: 'Agent编排引擎', 
    logo: 'https://picsum.photos/80/80?random=5',
    link: '/tools/fuyao',
    color: '#4096ff' 
  },
  { 
    id: 6,
    name: '纠错Agent', 
    desc: '智能代码纠错工具', 
    logo: 'https://picsum.photos/80/80?random=6',
    link: '/tools/correction-agent',
    color: '#ffc53d' 
  },
  { 
    id: 7,
    name: 'DT', 
    desc: '数据转换工具', 
    logo: 'https://picsum.photos/80/80?random=7',
    link: '/tools/dt',
    color: '#73d13d' 
  },
])

const getColSpan = (count: number) => {
  if (count <= 4) return 24 / count
  return 6
}

const handleToolClick = (tool: any) => {
  if (tool.link) {
    router.push(tool.link)
  }
}
</script>

<style scoped lang="scss">
.tools-view {
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

.tools-grid {
  margin-top: 30px;
}

.tool-card {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;

  &.hover-effect:hover {
    transform: translateY(-5px);
    background: rgba(255, 255, 255, 0.15);
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  }

  .tool-logo-wrapper {
    flex-shrink: 0;
    width: 48px;
    height: 48px;
    
    .tool-logo {
      width: 100%;
      height: 100%;
      object-fit: cover;
      border-radius: 12px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
    }
    
    .tool-icon {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
      font-size: 20px;
      color: #fff;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
    }
  }

  .tool-info {
    flex: 1;
    min-width: 0;
    
    h4 {
      margin: 0 0 4px;
      font-size: 16px;
      color: #fff;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    
    p {
      margin: 0;
      font-size: 12px;
      color: rgba(255, 255, 255, 0.7);
      line-height: 1.4;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
  }
}
</style>

