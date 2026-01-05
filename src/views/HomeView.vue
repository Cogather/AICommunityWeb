<template>
  <div class="page-container">
    <section class="hero-section">
      <HeroCarousel />
    </section>

    <main class="main-content">
      <!-- AI新闻 -->
      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 新闻</h2>
          <p>AI NEWS</p>
        </div>

      <el-row :gutter="24" class="section-row">
        <el-col :xs="24" :md="16">
          <div class="glass-card honor-section">
            <div class="honor-split-container">
              <!-- 左边：荣誉殿堂 -->
              <div class="honor-hall">
                <!-- 荣誉殿堂 Banner -->
                <div class="honor-banner">
                  <img :src="honorBannerImage" alt="荣誉殿堂" class="banner-image" />
                  <div class="banner-overlay">
                    <div class="banner-content">
                      <h3>荣誉殿堂</h3>
                      <p>Hall of Fame</p>
                    </div>
                  </div>
                </div>
                
                <!-- 奖项列表 - 一行两个，小卡片+小logo -->
                <div class="awards-grid">
                  <div class="award-card-small" v-for="award in awards" :key="award.id">
                    <div class="award-logo-small">
                      <img :src="award.image" :alt="award.name" class="award-logo-img" />
                    </div>
                    <div class="award-content-small">
                      <h4>{{ award.name }}</h4>
                      <p>{{ award.desc }}</p>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 中间分割线 -->
              <div class="split-line"></div>
              
              <!-- 右边：AI使用达人 -->
              <div class="ai-users">
                <div class="users-header">
                  <h4>AI使用达人</h4>
                  <el-tag effect="dark" round size="small" color="#626aef">TOP</el-tag>
                </div>
                <div class="users-grid">
                  <div class="user-card" v-for="i in 6" :key="i">
                    <el-avatar :size="50" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                    <span class="user-name">User_{{ 100 + i }}</span>
                    <span class="user-level">Lv.{{ 10 - i }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :xs="24" :md="8">
          <div class="glass-card full-height">
            <div class="card-header">
              <h3>
                <el-icon class="header-icon"><Bell /></el-icon>
                社区头条
              </h3>
              <div class="header-actions">
                <el-tag effect="plain" round size="small">HOT</el-tag>
                <el-button 
                  text 
                  size="small" 
                  class="more-btn"
                  @click="router.push('/news')"
                >
                  更多
                  <el-icon><ArrowRight /></el-icon>
                </el-button>
              </div>
            </div>
            <!-- 社区头条 - 四个帖子，两两并排 -->
            <div class="news-grid">
              <div class="news-card" v-for="(news, idx) in newsList" :key="idx">
                <div class="news-image-wrapper">
                  <img :src="news.image" :alt="news.title" class="news-image" />
                </div>
                <div class="news-content">
                  <h4 class="news-title">{{ news.title }}</h4>
                  <span class="news-date">{{ news.date }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      </section>

      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 工具专区</h2>
          <p>AI TOOL ZONE</p>
        </div>
        
        <!-- AI工具专区轮播图 Banner -->
        <div class="tool-zone-carousel-wrapper">
          <el-carousel
            :interval="5000"
            height="200px"
            indicator-position="inside"
            :arrow="'hover'"
            class="tool-zone-carousel"
          >
            <el-carousel-item v-for="(banner, index) in toolZoneBanners" :key="index">
              <div class="tool-banner-item">
                <img :src="banner.image" :alt="banner.title" class="banner-image" />
                <div class="banner-overlay">
                  <div class="banner-text">
                    <h3>{{ banner.title }}</h3>
                    <p>{{ banner.desc }}</p>
                  </div>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
        
        <el-row :gutter="20" class="tools-grid">
          <el-col 
            :xs="12" 
            :sm="6" 
            :md="getColSpan(tools.length)"
            v-for="tool in tools" 
            :key="tool.id || tool.name" 
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
      </section>

      <!-- AI乐园 -->
      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 乐园</h2>
          <p>AI PARK</p>
        </div>

      <el-row :gutter="24" class="section-row equal-height">
        <el-col :xs="24" :md="12">
          <div class="glass-card equal-height-card">
            <div class="card-header">
              <h3>🏠 AI 优秀实践</h3>
              <el-button 
                text 
                size="small" 
                class="more-btn"
                @click="router.push('/practices')"
              >
                更多
                <el-icon><ArrowRight /></el-icon>
              </el-button>
            </div>
            <div class="text-list">
              <div v-for="practice in practices" :key="practice.id" class="list-row">
                <div class="practice-content">
                  <h4 class="practice-title">{{ practice.title }}</h4>
                  <div class="practice-meta">
                    <span class="practice-author">{{ practice.author }}</span>
                    <span class="practice-time">{{ practice.time }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :md="12">
          <div class="glass-card equal-height-card">
            <div class="card-header">
              <h3>🗣️ 赋能交流</h3>
              <el-button 
                text 
                size="small" 
                class="more-btn"
                @click="router.push('/empowerment')"
              >
                更多
                <el-icon><ArrowRight /></el-icon>
              </el-button>
            </div>
            <div class="text-list">
              <p v-for="n in 5" :key="n" class="list-row">
                <span class="tag blue">讨论</span>
                如何使用 Agent 提升代码开发效率？
              </p>
            </div>
          </div>
        </el-col>
      </el-row>
      </section>

      <!-- AI资讯 -->
      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 资讯</h2>
          <p>AI INFORMATION</p>
        </div>

      <div class="glass-card wide-banner section-row">
        <div class="info-content">
          <h3>AI 领域周报 (2026年1月1日 - 1月7日)</h3>
          <p>
            本周 AI 领域持续升温，OpenAI 发布最新推理模型，国内大模型在长文本处理上取得突破性进展。
            同时，AI 在软件工程中的实际落地效果获得更多量化验证...
          </p>
        </div>
        <el-button round>阅读更多</el-button>
      </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowRight, ChatDotRound, Bell } from '@element-plus/icons-vue'
import HeroCarousel from '@/components/HeroCarousel.vue'

const router = useRouter()

// AI优秀实践数据
const practices = ref([
  {
    id: 1,
    title: '大模型在工业设计中的落地应用案例分享',
    author: '张工程师',
    time: '2小时前'
  },
  {
    id: 2,
    title: '如何利用AI提升代码质量和开发效率',
    author: '李开发者',
    time: '5小时前'
  },
  {
    id: 3,
    title: 'AI辅助测试的最佳实践与经验总结',
    author: '王测试',
    time: '昨天'
  },
  {
    id: 4,
    title: '多模态模型在医疗影像分析中的应用',
    author: '赵医生',
    time: '2天前'
  },
  {
    id: 5,
    title: '构建企业级AI知识库的完整方案',
    author: '陈架构师',
    time: '3天前'
  }
])

// Mock Data
const newsList = ref([
  { 
    title: '【大模型专题】多模态模型在医疗影像中的最新应用突破', 
    date: '刚刚',
    image: 'https://picsum.photos/300/200?random=20'
  },
  { 
    title: '【社区活动】2026 AI 开发者大会早鸟票开启预售', 
    date: '1小时前',
    image: 'https://picsum.photos/300/200?random=21'
  },
  { 
    title: '【开源动态】轻量级 LLM 本地部署最佳实践指南', 
    date: '昨天',
    image: 'https://picsum.photos/300/200?random=22'
  },
  { 
    title: '【深度解析】Agent 自主智能体的未来发展趋势', 
    date: '昨天',
    image: 'https://picsum.photos/300/200?random=23'
  },
])

// 荣誉殿堂数据
const honorBannerImage = ref('https://picsum.photos/800/300?random=30')

const awards = ref([
  {
    id: 1,
    name: '年度最佳贡献奖',
    desc: '2026年度',
    image: 'https://picsum.photos/200/150?random=31'
  },
  {
    id: 2,
    name: '年度创新奖',
    desc: '2025年度',
    image: 'https://picsum.photos/200/150?random=32'
  },
  {
    id: 3,
    name: '年度最佳实践奖',
    desc: '2024年度',
    image: 'https://picsum.photos/200/150?random=33'
  },
  {
    id: 4,
    name: '社区贡献奖',
    desc: '2023年度',
    image: 'https://picsum.photos/200/150?random=34'
  }
])

// AI工具列表配置
// 可配置字段：id, name, desc, logo(可选), link(路由参数), color(备用，当没有logo时使用)
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

// 根据工具数量计算列宽（一行最多4个）
const getColSpan = (count: number) => {
  if (count <= 4) return 24 / count
  return 6 // 最多4个，每个占6列（24/4=6）
}

// 处理工具点击跳转
const handleToolClick = (tool: any) => {
  if (tool.link) {
    router.push(tool.link)
  }
}

// AI工具专区轮播图数据
const toolZoneBanners = ref([
  {
    title: '最新 AI 工具推荐',
    desc: '探索最新发布的 AI 工具，提升你的工作效率',
    image: 'https://picsum.photos/1200/400?random=10'
  },
  {
    title: '热门工具排行榜',
    desc: '查看最受欢迎的 AI 工具，发现社区精选',
    image: 'https://picsum.photos/1200/400?random=11'
  },
  {
    title: '开发者必备工具',
    desc: '专为开发者打造的 AI 工具集合',
    image: 'https://picsum.photos/1200/400?random=12'
  }
])
</script>

<style scoped lang="scss">
/* -----------------------
  1. 全局布局与背景 
  -----------------------
*/
.page-container {
  min-height: 100vh;
  position: relative;
  font-family: 'PingFang SC', 'Helvetica Neue', Helvetica, 'Hiragino Sans GB', Arial, sans-serif;
  overflow-x: hidden;
  overflow-y: auto;
  color: #fff;
}

/* 背景已移至 App.vue 统一管理 */

/* -----------------------
  3. 主内容区域通用样式
  -----------------------
*/
.hero-section {
  width: 100%;
  /* 轮播图区域 */
}

.main-content {
  width: 100%;
  max-width: 1280px; /* 内容宽度限制 */
  margin: 0 auto;
  padding: 20px;
  padding-top: 0; /* 减少顶部间距，让内容更靠近轮播图 */
}

/* 间距控制 */
.section-row,
.section-block {
  margin-bottom: 40px;
}

/* 确保AI优秀实践和赋能交流同高 */
.equal-height {
  display: flex;
  
  :deep(.el-col) {
    display: flex;
  }
}

.equal-height-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%; /* 占满父元素宽度 */
  
  .text-list {
    flex: 1;
  }
}

/* 第一个 section-block (AI新闻) 往下挪一点，并在轮播图上层 */
.section-block:first-of-type {
  margin-top: 20px; /* 往下挪一点 */
  padding-top: 0;
  position: relative;
  z-index: 10; /* 确保在轮播图上层 */
}

/* 第一个 section-row 减少顶部间距 */
.section-row:first-of-type {
  margin-top: 0;
  padding-top: 0;
}

/* 通用毛玻璃卡片 - 更透亮更立体 */
.glass-card {
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(25px) saturate(200%);
  -webkit-backdrop-filter: blur(25px) saturate(200%);
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: 16px;
  padding: 24px;
  box-shadow: 
    0 8px 32px 0 rgba(31, 38, 135, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.3),
    0 0 0 1px rgba(255, 255, 255, 0.1);
  color: #fff;
  transition: all 0.3s ease;
  position: relative;
  width: 100%; /* 占满父元素宽度 */
  box-sizing: border-box; /* 确保 padding 不会超出宽度 */
  
  /* 添加顶部高光效果，增强立体感 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(
      90deg,
      transparent,
      rgba(255, 255, 255, 0.4),
      transparent
    );
    border-radius: 16px 16px 0 0;
  }
  
  /* 悬停时增强透亮效果 */
  &:hover {
    background: rgba(255, 255, 255, 0.18);
    border-color: rgba(255, 255, 255, 0.35);
    box-shadow: 
      0 12px 40px 0 rgba(31, 38, 135, 0.25),
      inset 0 1px 0 rgba(255, 255, 255, 0.4),
      0 0 0 1px rgba(255, 255, 255, 0.15);
    transform: translateY(-2px);
  }
}

.full-height {
  height: 100%;
  min-height: 300px;
}

.honor-section {
  min-height: auto;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding-bottom: 10px;

  h3 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .header-icon {
    font-size: 20px;
    color: #409eff;
  }
  
  .header-actions {
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .more-btn {
    color: rgba(255, 255, 255, 0.8);
    font-size: 13px;
    padding: 4px 8px;
    transition: all 0.3s ease;
    
    &:hover {
      color: #409eff;
      background: rgba(64, 158, 255, 0.1);
    }
    
    :deep(.el-icon) {
      margin-left: 4px;
      font-size: 12px;
    }
  }
}


/* -----------------------
  4. 具体的版块内部样式
  -----------------------
*/

/* 荣誉殿堂和使用达人 - 左右分割布局 */
.honor-split-container {
  display: flex;
  gap: 20px;
  height: 100%;
}

/* 左边：荣誉殿堂 */
.honor-hall {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 荣誉殿堂 Banner */
.honor-banner {
  position: relative;
  width: 100%;
  height: 240px;
  border-radius: 12px;
  overflow: hidden;
  
  .banner-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .banner-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(
      to bottom,
      rgba(0, 0, 0, 0.3),
      rgba(0, 0, 0, 0.6)
    );
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .banner-content {
    text-align: center;
    color: #fff;
    
    h3 {
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 700;
      text-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
    }
    
    p {
      margin: 0;
      font-size: 13px;
      color: rgba(255, 255, 255, 0.9);
      text-shadow: 0 1px 5px rgba(0, 0, 0, 0.5);
    }
  }
}

/* 奖项网格 - 小卡片+小logo */
.awards-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.award-card-small {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  padding: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: all 0.3s ease;
  
  &:hover {
    background: rgba(255, 255, 255, 0.12);
    border-color: rgba(255, 255, 255, 0.25);
    transform: translateY(-2px);
  }
  
  .award-logo-small {
    flex-shrink: 0;
    width: 40px;
    height: 40px;
    border-radius: 8px;
    overflow: hidden;
    background: rgba(255, 255, 255, 0.1);
    
    .award-logo-img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  
  .award-content-small {
    flex: 1;
    min-width: 0;
    
    h4 {
      margin: 0 0 4px 0;
      font-size: 14px;
      font-weight: 600;
      color: #fff;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    
    p {
      margin: 0;
      font-size: 12px;
      color: rgba(255, 255, 255, 0.7);
    }
  }
}

/* 中间分割线 */
.split-line {
  width: 1px;
  background: repeating-linear-gradient(
    to bottom,
    rgba(255, 255, 255, 0.3) 0,
    rgba(255, 255, 255, 0.3) 8px,
    transparent 8px,
    transparent 16px
  );
  flex-shrink: 0;
}

/* 右边：AI使用达人 */
.ai-users {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.users-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  h4 {
    margin: 0;
    font-size: 18px;
    font-weight: 700;
  }
}

.users-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

/* 中间分割线 */
.split-line {
  width: 1px;
  background: repeating-linear-gradient(
    to bottom,
    rgba(255, 255, 255, 0.3) 0,
    rgba(255, 255, 255, 0.3) 8px,
    transparent 8px,
    transparent 16px
  );
  flex-shrink: 0;
}

/* 右边：AI使用达人 */
.ai-users {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.users-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  h4 {
    margin: 0;
    font-size: 18px;
    font-weight: 700;
  }
}

.users-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.user-card {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  padding: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
  cursor: pointer;
  
  &:hover {
    background: rgba(255, 255, 255, 0.15);
    transform: translateY(-2px);
  }
  
  .user-name {
    font-size: 13px;
    font-weight: 600;
    text-align: center;
  }
  
  .user-level {
    font-size: 11px;
    opacity: 0.7;
    background: rgba(255, 255, 255, 0.1);
    padding: 2px 8px;
    border-radius: 10px;
  }
}

/* 社区头条 - 网格布局，两两并排 */
.news-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.news-card {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
  display: grid;
  grid-template-rows: 1fr 1fr; /* 两行平分高度 */
  height: 180px; /* 整体高度更高 */
  
  &:hover {
    background: rgba(255, 255, 255, 0.12);
    border-color: rgba(255, 255, 255, 0.25);
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
  }
  
  .news-image-wrapper {
    width: 100%;
    height: 100%;
    overflow: hidden;
    
    .news-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }
  }
  
  &:hover .news-image {
    transform: scale(1.05);
  }
  
  .news-content {
    padding: 10px 12px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    
    .news-title {
      margin: 0;
      font-size: 13px;
      font-weight: 600;
      color: #fff;
      display: -webkit-box;
      -webkit-line-clamp: 3; /* 显示更多标题内容 */
      line-clamp: 3;
      -webkit-box-orient: vertical;
      overflow: hidden;
      text-overflow: ellipsis;
      line-height: 1.4;
      flex: 1;
    }
    
    .news-date {
      font-size: 11px;
      color: rgba(255, 255, 255, 0.6);
      margin-top: 6px;
    }
  }
}

/* Section 2: Tools Zone */
.section-title-center {
  text-align: center;
  margin-bottom: 30px;

  h2 {
    font-size: 28px;
    margin: 0;
    text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  }
  p {
    margin: 5px 0 0;
    letter-spacing: 4px;
    opacity: 0.7;
    font-size: 12px;
  }
}

/* AI工具专区轮播图 */
.tool-zone-carousel-wrapper {
  width: 100%;
  margin: 30px 0 0 0; /* 底部margin为0，使工具列表紧挨着轮播图 */
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
}

:deep(.tool-zone-carousel) {
  width: 100%;
  border-radius: 16px;
  
  .el-carousel__container {
    border-radius: 16px;
  }
  
  .el-carousel__arrow {
    background: rgba(255, 255, 255, 0.3);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.4);
    color: #fff;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    transition: all 0.3s ease;
    
    &:hover {
      background: rgba(255, 255, 255, 0.5);
      transform: scale(1.1);
    }
  }
  
  .el-carousel__indicators {
    bottom: 20px !important; /* 指示器在图片底部 */
    left: 50%;
    transform: translateX(-50%);
    
    .el-carousel__indicator {
      margin: 0 4px;
      
      button {
        background-color: rgba(255, 255, 255, 0.5);
        width: 8px;
        height: 8px;
        border-radius: 50%;
        transition: all 0.3s ease;
        border: none;
      }
      
      &.is-active button {
        background-color: #fff;
        width: 24px;
        border-radius: 4px;
      }
    }
  }
}

.tool-banner-item {
  position: relative;
  width: 100%;
  height: 200px;
  border-radius: 16px;
  overflow: hidden;
  
  .banner-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.5s ease;
  }
  
  &:hover .banner-image {
    transform: scale(1.05);
  }
  
  .banner-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
    padding: 30px 40px 50px 40px; /* 底部增加padding，为指示器留出空间 */
    
    .banner-text {
      max-width: 1200px;
      margin: 0 auto;
      
      h3 {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 700;
        color: #fff;
        text-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
      }
      
      p {
        margin: 0;
        font-size: 15px;
        color: rgba(255, 255, 255, 0.95);
        line-height: 1.6;
      }
    }
  }
}

@media (max-width: 768px) {
  .tool-zone-carousel-wrapper {
    margin: 20px 0 0 0; /* 底部margin为0，使工具列表紧挨着轮播图 */
  }
  
  /* 荣誉殿堂响应式 */
  .honor-split-container {
    flex-direction: column;
  }
  
  .awards-grid {
    grid-template-columns: 1fr; /* 小屏幕下一行一个 */
  }
  
  .honor-banner {
    height: 180px;
    
    .banner-content h3 {
      font-size: 20px;
    }
  }
  
  /* 社区头条响应式 */
  .news-grid {
    grid-template-columns: 1fr; /* 小屏幕下一行一个 */
  }
  
  .news-card {
    height: 150px; /* 小屏幕下高度 */
  }
  
  :deep(.tool-zone-carousel) {
    .el-carousel__indicators {
      bottom: 15px !important;
    }
  }
  
  .tool-banner-item {
    height: 160px;
    
    .banner-overlay {
      padding: 20px 20px 45px 20px; /* 底部增加padding，为指示器留出空间 */
      
      .banner-text {
        h3 {
          font-size: 20px;
        }
        
        p {
          font-size: 13px;
        }
      }
    }
  }
}

.tools-grid {
  margin-top: 0; /* 紧挨着轮播图 */
}

.tool-card {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;

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
    min-width: 0; /* 允许文本截断 */
    
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


/* Section 3: AI Park */
.text-list .list-row {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  
  &:last-child {
    margin-bottom: 0;
    padding-bottom: 0;
    border-bottom: none;
  }
  
  &:hover {
    padding-left: 8px;
    
    .practice-title {
      color: #409eff;
    }
  }
  
  .practice-content {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  
  .practice-title {
    font-size: 15px;
    font-weight: 600;
    color: rgba(255, 255, 255, 0.95);
    margin: 0;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    transition: color 0.3s ease;
  }
  
  .practice-meta {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 12px;
    color: rgba(255, 255, 255, 0.6);
  }
  
  .practice-author {
    display: flex;
    align-items: center;
    
    &::before {
      content: '👤';
      margin-right: 4px;
    }
  }
  
  .practice-time {
    display: flex;
    align-items: center;
    
    &::before {
      content: '🕐';
      margin-right: 4px;
    }
  }
}

/* Section 4: AI Info */
.wide-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .info-content {
    h3 {
      margin: 0 0 8px;
      color: #d9ecff;
    }
    p {
      margin: 0;
      font-size: 13px;
      opacity: 0.8;
      max-width: 800px;
    }
  }
}


</style>
