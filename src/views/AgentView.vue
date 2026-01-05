<template>
  <div class="agent-view">
    <div class="page-header">
      <h1>扶摇 Agent 应用</h1>
      <p>基于 Agent 编排引擎的智能应用集合</p>
    </div>
    
    <div class="tools-grid">
      <el-row :gutter="20">
        <el-col 
          :xs="12" 
          :sm="6" 
          :md="getColSpan(agents.length)"
          v-for="agent in agents" 
          :key="agent.id" 
          style="margin-bottom: 20px;"
        >
          <div 
            class="tool-card glass-card hover-effect" 
            @click="handleAgentClick(agent)"
          >
            <div class="tool-logo-wrapper">
              <img 
                v-if="agent.logo" 
                :src="agent.logo" 
                :alt="agent.name" 
                class="tool-logo"
              />
              <div 
                v-else 
                class="tool-icon" 
                :style="{ background: agent.color || '#409eff' }"
              >
                {{ agent.name[0] }}
              </div>
            </div>
            <div class="tool-info">
              <h4>{{ agent.name }}</h4>
              <p>{{ agent.desc }}</p>
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

const agents = ref([
  { 
    id: 1,
    name: '代码生成 Agent', 
    desc: '智能代码生成助手', 
    logo: 'https://picsum.photos/80/80?random=11',
    link: '/agent/code-gen',
    color: '#4096ff' 
  },
  { 
    id: 2,
    name: '文档分析 Agent', 
    desc: '智能文档分析与总结', 
    logo: 'https://picsum.photos/80/80?random=12',
    link: '/agent/doc-analysis',
    color: '#9254de' 
  },
  { 
    id: 3,
    name: '数据分析 Agent', 
    desc: '自动化数据分析工具', 
    logo: 'https://picsum.photos/80/80?random=13',
    link: '/agent/data-analysis',
    color: '#597ef7' 
  },
  { 
    id: 4,
    name: '工作流编排 Agent', 
    desc: '智能工作流编排引擎', 
    logo: 'https://picsum.photos/80/80?random=14',
    link: '/agent/workflow',
    color: '#ff9c6e' 
  },
])

const getColSpan = (count: number) => {
  if (count <= 4) return 24 / count
  return 6
}

const handleAgentClick = (agent: any) => {
  if (agent.link) {
    router.push(agent.link)
  }
}
</script>

<style scoped lang="scss">
.agent-view {
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

