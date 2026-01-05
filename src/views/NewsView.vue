<template>
  <div class="news-view">
    <div class="page-header">
      <h1>AI 资讯</h1>
      <p>最新 AI 领域动态与资讯</p>
    </div>
    
    <div class="news-container">
      <el-tabs v-model="activeTab" class="news-tabs">
        <el-tab-pane label="最新资讯" name="latest">
          <el-timeline>
            <el-timeline-item 
              v-for="(news, index) in latestNews" 
              :key="index"
              :timestamp="news.date"
              placement="top"
            >
              <el-card class="news-card" shadow="hover">
                <h3>{{ news.title }}</h3>
                <p>{{ news.content }}</p>
                <div class="news-footer">
                  <el-tag :type="news.type">{{ news.category }}</el-tag>
                  <el-button text type="primary">阅读全文 →</el-button>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>
        
        <el-tab-pane label="技术动态" name="tech">
          <el-timeline>
            <el-timeline-item 
              v-for="(news, index) in techNews" 
              :key="index"
              :timestamp="news.date"
              placement="top"
            >
              <el-card class="news-card" shadow="hover">
                <h3>{{ news.title }}</h3>
                <p>{{ news.content }}</p>
                <div class="news-footer">
                  <el-tag type="success">{{ news.category }}</el-tag>
                  <el-button text type="primary">阅读全文 →</el-button>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>
        
        <el-tab-pane label="行业报告" name="report">
          <el-timeline>
            <el-timeline-item 
              v-for="(news, index) in reportNews" 
              :key="index"
              :timestamp="news.date"
              placement="top"
            >
              <el-card class="news-card" shadow="hover">
                <h3>{{ news.title }}</h3>
                <p>{{ news.content }}</p>
                <div class="news-footer">
                  <el-tag type="warning">{{ news.category }}</el-tag>
                  <el-button text type="primary">阅读全文 →</el-button>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const activeTab = ref('latest')

const latestNews = ref([
  {
    title: '【大模型专题】多模态模型在医疗影像中的最新应用突破',
    content: '最新研究显示，多模态 AI 模型在医疗影像诊断准确率上取得了显著提升...',
    date: '2026-01-05',
    category: '大模型专题',
    type: 'primary'
  },
  {
    title: '【社区活动】2026 AI 开发者大会早鸟票开启预售',
    content: '2026 AI 开发者大会将于3月举行，早鸟票现已开启预售，限时优惠...',
    date: '2026-01-04',
    category: '社区活动',
    type: 'success'
  },
  {
    title: '【开源动态】轻量级 LLM 本地部署最佳实践指南',
    content: '分享如何在本地环境部署轻量级大语言模型，实现私有化 AI 应用...',
    date: '2026-01-03',
    category: '开源动态',
    type: 'info'
  }
])

const techNews = ref([
  {
    title: '【深度解析】Agent 自主智能体的未来发展趋势',
    content: '探讨 Agent 技术的发展方向，分析自主智能体在未来的应用场景...',
    date: '2026-01-05',
    category: '技术解析'
  },
  {
    title: '【技术分享】如何构建高质量的 RAG 知识库',
    content: '详细介绍 RAG 系统的构建方法，提升知识检索的准确性和效率...',
    date: '2026-01-04',
    category: '技术分享'
  }
])

const reportNews = ref([
  {
    title: 'AI 领域周报 (2026年1月1日 - 1月7日)',
    content: '本周 AI 领域持续升温，OpenAI 发布最新推理模型，国内大模型在长文本处理上取得突破性进展...',
    date: '2026-01-07',
    category: '周报'
  }
])
</script>

<style scoped lang="scss">
.news-view {
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

.news-container {
  margin-top: 30px;
}

.news-tabs {
  :deep(.el-tabs__item) {
    color: rgba(255, 255, 255, 0.7);
    
    &.is-active {
      color: #fff;
    }
  }
  
  :deep(.el-tabs__active-bar) {
    background-color: #667eea;
  }
}

.news-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  
  h3 {
    color: #fff;
    font-size: 18px;
    margin-bottom: 12px;
  }
  
  p {
    color: rgba(255, 255, 255, 0.8);
    font-size: 14px;
    line-height: 1.6;
    margin-bottom: 15px;
  }
  
  .news-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>

