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
            <!-- 顶部标题条 -->
            <div class="honor-header-bar">
              <h3 class="header-title">AI使用达人·荣誉殿堂</h3>
              <el-button 
                text 
                size="small" 
                class="more-btn-pill"
                @click="router.push('/users')"
              >
                更多
              </el-button>
            </div>
            
            <div class="honor-split-container" style="padding: 20px;">
              <!-- 左边：荣誉殿堂 -->
              <div class="honor-hall">
                <!-- 荣誉殿堂 Banner -->
                <div class="honor-banner" @click="router.push('/honor')" style="cursor: pointer;">
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
                  class="more-btn-pill"
                  @click="router.push('/news')"
                >
                  更多
                </el-button>
              </div>
            </div>
            <!-- 社区头条 - 四个帖子，两两并排 -->
            <div class="news-grid">
              <div 
                class="news-card" 
                v-for="(news, idx) in newsList" 
                :key="idx"
                @click="handleNewsClick(news)"
              >
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
                class="more-btn-pill"
                @click="router.push('/practices')"
              >
                更多
              </el-button>
            </div>
            <div class="text-list">
              <div 
                v-for="practice in practices" 
                :key="practice.id" 
                class="list-row"
                @click="handlePracticeClick(practice)"
              >
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
                class="more-btn-pill"
                @click="router.push('/empowerment')"
              >
                更多
              </el-button>
            </div>
            <div class="text-list">
              <p 
                v-for="n in 5" 
                :key="n" 
                class="list-row"
                @click="router.push('/empowerment')"
              >
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
        <el-button 
          text 
          size="small" 
          class="more-btn-pill"
          @click="router.push('/news')"
        >
          阅读更多
        </el-button>
      </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowRight, ChatDotRound, Bell } from '@element-plus/icons-vue'
import HeroCarousel from '@/components/HeroCarousel.vue'

const router = useRouter()

// 监听配置更新
const handleConfigUpdate = () => {
  newsList.value = loadNewsList()
  honorBannerImage.value = loadHonorBanner()
  tools.value = loadTools()
}

onMounted(() => {
  window.addEventListener('adminConfigUpdated', handleConfigUpdate)
})

onUnmounted(() => {
  window.removeEventListener('adminConfigUpdated', handleConfigUpdate)
})

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

// Mock Data - 从localStorage读取配置，如果没有则使用默认值
const loadNewsList = () => {
  try {
    const saved = localStorage.getItem('admin_news_config')
    if (saved) {
      const config = JSON.parse(saved)
      return config.map((item: any) => ({
        title: item.title,
        date: item.date,
        image: item.image,
        link: item.link || '/news'
      }))
    }
  } catch (e) {
    console.error('加载头条配置失败:', e)
  }
  return [
    { 
      title: '【大模型专题】多模态模型在医疗影像中的最新应用突破', 
      date: '刚刚',
      image: 'https://picsum.photos/300/200?random=20',
      link: '/news'
    },
    { 
      title: '【社区活动】2026 AI 开发者大会早鸟票开启预售', 
      date: '1小时前',
      image: 'https://picsum.photos/300/200?random=21',
      link: '/news'
    },
    { 
      title: '【开源动态】轻量级 LLM 本地部署最佳实践指南', 
      date: '昨天',
      image: 'https://picsum.photos/300/200?random=22',
      link: '/news'
    },
    { 
      title: '【深度解析】Agent 自主智能体的未来发展趋势', 
      date: '昨天',
      image: 'https://picsum.photos/300/200?random=23',
      link: '/news'
    },
  ]
}

const newsList = ref(loadNewsList())

// 荣誉殿堂数据 - 从localStorage读取配置
const loadHonorBanner = () => {
  try {
    const saved = localStorage.getItem('admin_honor_config')
    if (saved) {
      const config = JSON.parse(saved)
      return config.bannerImage || 'https://picsum.photos/800/300?random=30'
    }
  } catch (e) {
    console.error('加载荣誉殿堂配置失败:', e)
  }
  return 'https://picsum.photos/800/300?random=30'
}

const honorBannerImage = ref(loadHonorBanner())

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

// AI工具列表配置 - 从localStorage读取配置
const loadTools = () => {
  try {
    const saved = localStorage.getItem('admin_tools_config')
    if (saved) {
      const config = JSON.parse(saved)
      return config.map((item: any, index: number) => ({
        id: item.id || index + 1,
        name: item.name,
        desc: item.desc || '',
        logo: item.logo || '',
        link: item.link || `/tools/${item.name.toLowerCase()}`,
        color: item.color || '#409eff'
      }))
    }
  } catch (e) {
    console.error('加载工具配置失败:', e)
  }
  return [
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
  ]
}

const tools = ref(loadTools())

// 根据工具数量计算列宽（一行最多4个）
const getColSpan = (count: number) => {
  if (count <= 4) return 24 / count
  return 6 // 最多4个，每个占6列（24/4=6）
}

// 处理工具点击跳转
const handleToolClick = (tool: any) => {
  // 跳转到工具专区页面，并传递toolId参数
  router.push({
    path: '/tools',
    query: {
      toolId: tool.id
    }
  })
}

// 处理实践点击
const handlePracticeClick = (practice: any) => {
  // 跳转到实践详情或列表页
  router.push('/practices')
}

// 处理头条点击
const handleNewsClick = (news: any) => {
  if (news.link) {
    if (news.link.startsWith('http')) {
      window.open(news.link, '_blank')
    } else {
      router.push(news.link)
    }
  } else {
    router.push('/news')
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
  color: #000000; /* 改为黑色 */
  width: 100%;
  box-sizing: border-box;
  
  /* 小屏设备：确保占满宽度 */
  @media (max-width: 768px) {
    width: 100%;
    max-width: 100vw;
    overflow-x: hidden;
  }
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
  max-width: 1600px; /* 增加内容宽度限制，让模块更宽 */
  margin: 0 auto;
  padding: 20px;
  padding-top: 0; /* 减少顶部间距，让内容更靠近轮播图 */
  
  /* 小屏设备：占满屏幕宽度，减少内边距 */
  @media (max-width: 768px) {
    max-width: 100%;
    padding: 12px;
    padding-top: 0;
  }
  
  /* 中等屏幕：适中的宽度 */
  @media (min-width: 769px) and (max-width: 1024px) {
    max-width: 100%;
    padding: 16px;
    padding-top: 0;
  }
}

/* 间距控制 */
.section-row,
.section-block {
  margin-bottom: 40px;
  width: 100%;
  box-sizing: border-box;
  
  /* 小屏设备：确保占满宽度 */
  @media (max-width: 768px) {
    width: 100%;
    margin-left: 0;
    margin-right: 0;
  }
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
  color: #000000; /* 改为黑色 */
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
  padding: 0; /* 移除内边距，让标题条占满 */
  overflow: hidden; /* 确保圆角正确显示 */
}

/* 顶部标题条 - AI使用达人·荣誉殿堂 */
.honor-header-bar {
  background: #4C85FA; /* 中高饱和度蓝色 */
  position: relative;
  padding: 14px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 16px 16px 0 0; /* 顶部圆角 */
  overflow: hidden;
  
  /* 城市/楼宇线稿纹理 - 使用 SVG 图案 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: 
      /* 城市轮廓线稿纹理 - 模拟楼宇剪影 */
      url("data:image/svg+xml,%3Csvg width='200' height='60' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M0 60 L10 45 L15 50 L25 35 L35 40 L45 30 L55 35 L65 25 L75 30 L85 20 L95 25 L105 15 L115 20 L125 10 L135 15 L145 5 L155 10 L165 0 L175 5 L185 0 L200 0 L200 60 Z' fill='none' stroke='rgba(255,255,255,0.08)' stroke-width='1'/%3E%3C/svg%3E"),
      repeating-linear-gradient(
        90deg,
        transparent,
        transparent 20px,
        rgba(255, 255, 255, 0.02) 20px,
        rgba(255, 255, 255, 0.02) 22px
      );
    background-size: 200px 60px, 40px 40px;
    background-position: 0 100%, 0 0;
    background-repeat: repeat-x, repeat;
    opacity: 0.6;
    pointer-events: none;
  }
  
  /* 标题文字 */
  .header-title {
    position: relative;
    z-index: 1;
    margin: 0;
    font-size: 15px;
    font-weight: 600; /* Semibold */
    color: #ffffff; /* 标题条内保持白色，因为背景是蓝色 */
    letter-spacing: 0.3px;
  }
  
  /* 标题条内的更多按钮 - 特殊定位 */
  .more-btn-pill {
    position: relative;
    z-index: 1;
    border-radius: 999px; /* 完全圆角 pill */
    background: rgba(255, 255, 255, 0.15);
    border: 1px solid rgba(255, 255, 255, 0.3);
    color: #000000; /* 改为黑色 */
    font-size: 13px;
    padding: 6px 16px;
    height: auto;
    font-weight: 500;
    transition: all 0.3s ease;
    backdrop-filter: blur(4px);
    
    &:hover {
      background: rgba(255, 255, 255, 0.25);
      border-color: rgba(255, 255, 255, 0.4);
      transform: translateY(-1px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }
    
    &:active {
      transform: translateY(0);
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1); /* 改为黑色边框，适应浅色背景 */
  padding-bottom: 10px;

  h3 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 8px;
    color: #000000; /* 黑色，只有 section-title-center 的 h2 是深蓝色 */
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
  
  /* 通用胶囊按钮样式 - 适用于所有模块的"更多"按钮 */
  .more-btn-pill {
    border-radius: 999px; /* 完全圆角 pill */
    background: rgba(30, 58, 138, 0.1); /* 深蓝色半透明背景 */
    border: 1px solid rgba(30, 58, 138, 0.3); /* 深蓝色边框 */
    color: #1e3a8a; /* 深蓝色文字 */
    font-size: 13px;
    padding: 6px 16px;
    height: auto;
    font-weight: 500;
    transition: all 0.3s ease;
    backdrop-filter: blur(4px);
    
    &:hover {
      background: rgba(30, 58, 138, 0.2);
      border-color: rgba(30, 58, 138, 0.5);
      transform: translateY(-1px);
      box-shadow: 0 2px 8px rgba(30, 58, 138, 0.2);
      color: #1e40af; /* 稍亮的深蓝色 */
    }
    
    &:active {
      transform: translateY(0);
    }
  }
  
  /* 保留旧的 more-btn 样式以兼容（如果还有使用） */
  .more-btn {
    @extend .more-btn-pill;
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
    color: #000000; /* 改为黑色 */
    
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
      color: #000000; /* 黑色 */
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    
    p {
      margin: 0;
      font-size: 12px;
      color: rgba(0, 0, 0, 0.7); /* 黑色，70% 透明度 */
    }
  }
}

/* 中间分割线 */
.split-line {
  width: 1px;
  background: repeating-linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0.2) 0,
    rgba(0, 0, 0, 0.2) 8px,
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
    color: #000000; /* 黑色 */
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
    rgba(0, 0, 0, 0.2) 0,
    rgba(0, 0, 0, 0.2) 8px,
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
    color: #000000; /* 黑色 */
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
    color: #000000; /* 黑色 */
  }
  
  .user-level {
    font-size: 11px;
    opacity: 0.7;
    background: rgba(0, 0, 0, 0.05);
    color: rgba(0, 0, 0, 0.7); /* 黑色，70% 透明度 */
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
      color: #000000; /* 黑色 */
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
      color: rgba(0, 0, 0, 0.6); /* 黑色，60% 透明度 */
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
    color: #1e3a8a; /* 深蓝色标题 */
    font-weight: 700;
    text-shadow: none; /* 移除阴影，深色背景不需要 */
  }
  p {
    margin: 5px 0 0;
    letter-spacing: 4px;
    opacity: 0.7;
    font-size: 12px;
    color: #000000; /* 黑色 */
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
    color: #000000; /* 改为黑色 */
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
        color: #000000; /* 改为黑色 */
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
  /* 小屏设备：确保所有模块占满宽度 */
  .page-container {
    width: 100%;
    overflow-x: hidden;
  }
  
  .main-content {
    width: 100%;
    max-width: 100%;
    padding: 12px;
    box-sizing: border-box;
  }
  
  .glass-card {
    width: 100%;
    box-sizing: border-box;
  }
  
  .tool-zone-carousel-wrapper {
    margin: 20px 0 0 0; /* 底部margin为0，使工具列表紧挨着轮播图 */
    width: 100%;
  }
  
  /* 荣誉殿堂响应式 */
  .honor-header-bar {
    padding: 12px 16px;
    
    .header-title {
      font-size: 14px;
    }
    
    .more-btn-pill {
      padding: 5px 12px;
      font-size: 12px;
    }
  }
  
  /* 所有模块的胶囊按钮响应式 */
  .more-btn-pill {
    padding: 5px 12px;
    font-size: 12px;
  }
  
  .honor-split-container {
    flex-direction: column;
    width: 100%;
    padding: 16px !important;
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
      color: #000000; /* 改为黑色 */
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
    }
  }

  .tool-info {
    flex: 1;
    min-width: 0; /* 允许文本截断 */
    
    h4 {
      margin: 0 0 4px;
      font-size: 16px;
      color: #000000; /* 黑色 */
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    
    p {
      margin: 0;
      font-size: 12px;
      color: rgba(0, 0, 0, 0.7); /* 黑色，70% 透明度 */
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
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
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
      color: #1e3a8a; /* 深蓝色悬停效果 */
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
    color: #000000; /* 黑色 */
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
    color: rgba(0, 0, 0, 0.6); /* 黑色，60% 透明度 */
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
  padding: 32px 40px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1));
  border: 2px solid rgba(102, 126, 234, 0.2);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(102, 126, 234, 0.05), rgba(118, 75, 162, 0.05));
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(102, 126, 234, 0.2);
    border-color: rgba(102, 126, 234, 0.4);

    &::before {
      opacity: 1;
    }
  }

  .info-content {
    flex: 1;
    position: relative;
    z-index: 1;

    h3 {
      margin: 0 0 16px;
      color: #1e3a8a; /* 深蓝色 */
      font-weight: 800;
      font-size: 24px;
      line-height: 1.4;
      text-shadow: 0 2px 4px rgba(30, 58, 138, 0.1);
      letter-spacing: 0.5px;
    }
    p {
      margin: 0;
      font-size: 16px;
      color: #333; /* 深灰色，更易读 */
      max-width: 900px;
      line-height: 1.8;
      font-weight: 500;
    }
  }

  .more-btn-pill {
    position: relative;
    z-index: 1;
    font-size: 14px;
    font-weight: 600;
    padding: 10px 24px;
    background: linear-gradient(135deg, #667eea, #764ba2);
    color: #fff;
    border: none;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
      background: linear-gradient(135deg, #5568d3, #6a3f91);
    }
  }
}


</style>
