<template>
  <div class="page-container">
    <section class="hero-section">
      <HeroCarousel />
      <div class="floating-tools-panel">
        <div class="tools-panel-header">
          <el-icon><Trophy /></el-icon>
          <span>AI工具</span>
        </div>
        <div class="tools-list">
          <div
            v-for="tool in toolPlatform"
            :key="tool.id"
            class="tool-btn"
            :style="{ '--tool-color': tool.color }"
            @click="handleToolPlatformClick(tool)"
          >
            <div class="tool-icon">
              <span v-if="tool.logo && !tool.logo.startsWith('http')" class="emoji-logo">{{ tool.logo }}</span>
              <img v-else-if="tool.logo" :src="tool.logo" :alt="tool.name" />
              <el-icon v-else><Star /></el-icon>
            </div>
            <span class="tool-name">{{ tool.name }}</span>
          </div>
        </div>
      </div>
    </section>

    <main class="main-content">
      <section class="section-block ai-dynamic-section">
        <div class="section-title-center">
          <h2>AI 动态</h2>
        </div>

        <!-- 用 gap 替代 el-row gutter（避免负 margin 导致宽度与下方模块不对齐） -->
        <el-row :gutter="0" class="section-row ai-dynamic-row">
          <el-col :xs="24" :md="16">
            <div class="glass-card honor-section">
              <div class="honor-header-bar">
                <h3 class="header-title">AI荣誉殿堂</h3>
                <el-button
                  text
                  size="small"
                  class="more-btn-pill"
                  @click="router.push(ROUTES.USERS)"
                >
                  更多
                </el-button>
              </div>

              <div class="honor-content-container">
                <div class="winners-three-columns">
                  <div class="winners-column">
                    <div
                      v-for="winner in latestWinners.slice(0, 6)"
                      :key="winner.id"
                      class="winner-card-item"
                      @click="router.push({ path: '/users', query: { view: 'timeline', user: winner.name } })"
                    >
                      <el-avatar
                        :size="44"
                        :src="winner.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'"
                        class="winner-avatar"
                      />
                      <div class="winner-info">
                        <span class="winner-name">{{ winner.name }}</span>
                        <span class="winner-award">{{ winner.awardName }}</span>
                      </div>
                    </div>
                  </div>

                  <div class="winners-column">
                    <div
                      v-for="winner in latestWinners.slice(6, 12)"
                      :key="winner.id"
                      class="winner-card-item"
                      @click="router.push({ path: '/users', query: { view: 'timeline', user: winner.name } })"
                    >
                      <el-avatar
                        :size="44"
                        :src="winner.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'"
                        class="winner-avatar"
                      />
                      <div class="winner-info">
                        <span class="winner-name">{{ winner.name }}</span>
                        <span class="winner-award">{{ winner.awardName }}</span>
                      </div>
                    </div>
                  </div>

                  <div class="winners-column">
                    <div
                      v-for="winner in latestWinners.slice(12, 18)"
                      :key="winner.id"
                      class="winner-card-item"
                      @click="router.push({ path: '/users', query: { view: 'timeline', user: winner.name } })"
                    >
                      <el-avatar
                        :size="44"
                        :src="winner.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'"
                        class="winner-avatar"
                      />
                      <div class="winner-info">
                        <span class="winner-name">{{ winner.name }}</span>
                        <span class="winner-award">{{ winner.awardName }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-col>

          <el-col :xs="24" :md="8">
            <div class="glass-card empowerment-section">
              <div class="card-header empowerment-header">
                <h3 class="empowerment-title-text">🗣️ AI使能站</h3>
                <el-button
                  text
                  size="small"
                  class="more-btn-pill"
                  @click="router.push(ROUTES.EMPOWERMENT)"
                >
                  更多
                </el-button>
              </div>
              <div class="empowerment-list">
                <div
                  v-for="(post, index) in empowermentPosts"
                  :key="post.id"
                  class="empowerment-item"
                  @click="router.push(`/post/${post.id}`)"
                >
                  <div class="rank-number" :class="`rank-${index + 1}`">
                    {{ index + 1 }}
                  </div>
                  <div class="empowerment-content">
                    <el-tooltip
                      :content="post.title"
                      placement="top"
                      :show-after="500"
                    >
                      <div class="empowerment-title">{{ post.title }}</div>
                    </el-tooltip>
                    <div class="empowerment-meta">
                      <span class="meta-department">{{ post.department }}</span>
                      <span class="meta-views">
                        <el-icon><View /></el-icon>
                        {{ post.views }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </section>

      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 优秀实践</h2>
        </div>

        <div class="practice-combined-card glass-card">
          <div class="practice-three-columns">
            <div class="practice-column">
              <div class="practice-column-header">
                <h3 class="column-title">💻 代码生成</h3>
                <el-button
                  text
                  size="small"
                  class="more-btn-pill"
                  @click="router.push({ path: ROUTES.PRACTICES, query: { tag: '代码生成' } })"
                >
                  更多
                </el-button>
              </div>
              <div class="practice-column-list">
                <div
                  v-for="practice in practices.training.slice(0, 6)"
                  :key="practice.id"
                  class="practice-item"
                  @click="handlePracticeClick(practice)"
                >
                  <h4 class="practice-title">{{ practice.title }}</h4>
                  <div class="practice-meta">
                    <span class="practice-author">{{ practice.author }}</span>
                    <span class="practice-department">{{ practice.department }}</span>
                    <span class="practice-time">{{ practice.time }}</span>
                  </div>
                </div>
              </div>
            </div>

            <div class="practice-column">
              <div class="practice-column-header">
                <h3 class="column-title">📜 脚本生成</h3>
                <el-button
                  text
                  size="small"
                  class="more-btn-pill"
                  @click="router.push({ path: ROUTES.PRACTICES, query: { tag: '脚本生成' } })"
                >
                  更多
                </el-button>
              </div>
              <div class="practice-column-list">
                <div
                  v-for="practice in practices.trainingBattle.slice(0, 6)"
                  :key="'train-' + practice.id"
                  class="practice-item"
                  @click="handlePracticeClick(practice)"
                >
                  <h4 class="practice-title">{{ practice.title }}</h4>
                  <div class="practice-meta">
                    <span class="practice-author">{{ practice.author }}</span>
                    <span class="practice-department">{{ practice.department }}</span>
                    <span class="practice-time">{{ practice.time }}</span>
                  </div>
                </div>
              </div>
            </div>

            <div class="practice-column">
              <div class="practice-column-header">
                <h3 class="column-title">🔧 问题处理</h3>
                <el-button
                  text
                  size="small"
                  class="more-btn-pill"
                  @click="router.push({ path: ROUTES.PRACTICES, query: { tag: '问题处理' } })"
                >
                  更多
                </el-button>
              </div>
              <div class="practice-column-list">
                <div
                  v-for="practice in practices.userExchange.slice(0, 6)"
                  :key="'exchange-' + practice.id"
                  class="practice-item"
                  @click="handlePracticeClick(practice)"
                >
                  <h4 class="practice-title">{{ practice.title }}</h4>
                  <div class="practice-meta">
                    <span class="practice-author">{{ practice.author }}</span>
                    <span class="practice-department">{{ practice.department }}</span>
                    <span class="practice-time">{{ practice.time }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section class="section-block tool-zone-section">
        <div class="section-title-center">
          <h2>AI 百宝箱</h2>
        </div>

        <div class="tools-big-buttons-grid">
          <div
            v-for="tool in tools"
            :key="tool.id || tool.name"
            class="tool-big-button"
            :data-tool-type="getToolType(tool.name)"
            :style="{ '--tool-category-color': tool.color || '#0066FF' }"
            @click="handleToolClick(tool)"
          >
            <div class="tool-button-icon">
              <img
                v-if="tool.logo"
                :src="tool.logo"
                :alt="tool.name"
                class="tool-logo"
              />
              <div
                v-else
                class="tool-icon-placeholder"
              >
                {{ tool.name[0] }}
              </div>
            </div>
            <div class="tool-button-content">
              <h4 class="tool-button-name">{{ tool.name }}</h4>
              <p class="tool-button-desc">{{ tool.desc }}</p>
            </div>
            <div class="tool-button-arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </section>

      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 资讯</h2>
        </div>

        <div class="glass-card wide-banner section-row" v-if="newsTop.title">
          <div class="info-content">
            <h3>{{ newsTop.title }}</h3>
            <div class="news-content-text" v-html="renderedOverview"></div>
          </div>
          <el-button
            text
            size="small"
            class="more-btn-pill"
            @click="handleNewsClick(newsTop)"
          >
            阅读更多
          </el-button>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { marked } from 'marked'
import { Trophy, Star, View, ArrowRight } from '@element-plus/icons-vue'
import HeroCarousel from '@/components/HeroCarousel.vue'
import { getHonor, getToolPlatform, getTools, getPractices, getToolBanners, getLatestWinners, getEmpowerment, getNews, getAiNews } from '../api/home'
import loginService from '../utils/loginService'
import { addCommunity, getManager } from '../api/user'
import type { LatestWinner } from '../api/types'
import { ROUTES } from '../router/paths'
import { ElMessage, ElMessageBox } from 'element-plus'
import commonMethods from '@/utils/common'

const router = useRouter()
const route = useRoute()

// 用户相关状态
const isMember = ref(false)
const showJoinButton = ref(true)
const isAdmin = ref(false)
const userInfo = ref<any>({})

const checkUserStatus = async () => {
  const cachedUser = loginService.userInfo
  if (cachedUser) {
    userInfo.value = cachedUser
    isMember.value = !!cachedUser.isMember
    showJoinButton.value = !isMember.value

    try {
      const res = await getManager()
      const adminIds = (res.data || []).map((item: any) => item.userName)
      isAdmin.value = adminIds.includes(String(cachedUser.userId))
    } catch (e) {
      console.warn('获取管理员列表失败', e)
    }
  }
}

// 悬浮工具平台
interface ToolPlatformItem {
  id: number
  name: string
  desc?: string
  logo?: string
  color?: string
  platformUrl?: string
}
const toolPlatform = ref<ToolPlatformItem[]>([])

const loadToolPlatform = async () => {
  try {
    const response = await getToolPlatform()
    if (response && response.data && response.data.list && response.data.list.length > 0) {
      toolPlatform.value = response.data.list
      return
    }
  } catch (e) {
    console.error('加载工具平台列表失败:', e)
  }
  toolPlatform.value = [
    { id: 1, name: 'TestMate', desc: '自动化测试助手', logo: '🧪', color: '#36cfc9', platformUrl: 'https://testmate.example.com' },
    { id: 2, name: 'CodeMate', desc: '智能代码补全', logo: '💻', color: '#9254de', platformUrl: 'https://codemate.example.com' },
    { id: 3, name: '云集', desc: '云端计算集群', logo: '☁️', color: '#597ef7', platformUrl: 'https://yunji.example.com' },
    { id: 4, name: '云见', desc: '智能监控平台', logo: '👁️', color: '#ff9c6e', platformUrl: 'https://yunjian.example.com' },
    { id: 5, name: '扶摇', desc: 'Agent编排引擎', logo: '🚀', color: '#4096ff', platformUrl: 'https://fuyao.example.com' },
  ]
}

const handleToolPlatformClick = (tool: { id: number; platformUrl?: string }) => {
  if (tool.platformUrl) {
    window.open(tool.platformUrl, '_blank')
  } else {
    router.push({
      path: ROUTES.TOOLS,
      query: { toolId: String(tool.id) }
    })
  }
}

// 荣誉殿堂
const latestWinners = ref<LatestWinner[]>([])
const loadLatestWinners = async () => {
  try {
    const response = await getLatestWinners(18)
    if (response.data?.list && response.data.list.length > 0) {
      latestWinners.value = response.data.list.map((winner: LatestWinner) => ({
        ...winner,
        avatar: commonMethods.getAvatarUrl(winner.avatar || winner.userId || winner.id)
      }))
    } else {
      const mockList = []
      for(let i=1; i<=18; i++) {
         mockList.push({
            id: i,
            name: `示例用户${i}`,
            awardName: ['AI创新先锋', '效率提升达人', '最佳实践奖'][i % 3],
            avatar: ''
         })
      }
      latestWinners.value = mockList.map(w => ({ ...w, avatar: commonMethods.getAvatarUrl(w.avatar || String(w.id)) }))
    }
  } catch (e) {
    console.error('加载最新获奖者失败:', e)
  }
}

// 赋能交流
interface EmpowermentItem {
  id: number | string
  title: string
  department: string
  views: number
}
const empowermentPosts = ref<EmpowermentItem[]>([])

const loadEmpowermentPosts = async () => {
  try {
    const response = await getEmpowerment(7)
    if (response && response.data && response.data.list) {
      empowermentPosts.value = response.data.list.map((item: any) => ({
        id: item.id,
        title: item.title,
        department: item.department || '未知部门',
        views: item.views || Math.floor(Math.random() * 500) + 50
      }))
    }
  } catch (e) {
    console.error('加载赋能交流数据失败:', e)
    empowermentPosts.value = [
      { id: 1, title: '如何使用 Agent 提升代码开发效率？', department: '研发一部', views: 328 },
      { id: 2, title: '分享一个提升工作效率的AI工具使用技巧', department: '产品部', views: 256 },
      { id: 3, title: '关于AI辅助编程的一些疑问', department: '测试部', views: 189 },
      { id: 4, title: 'Prompt工程最佳实践经验总结', department: '研发二部', views: 412 },
      { id: 5, title: '推荐几个好用的AI工具', department: '设计部', views: 167 },
      { id: 6, title: 'AI助力团队协作效率提升分享', department: '运营部', views: 203 },
    ]
  }
}

// AI优秀实践
const practices = ref({
  training: [] as any[],
  trainingBattle: [] as any[],
  userExchange: [] as any[]
})

const loadPractices = async () => {
  try {
    const response = await getPractices()
    if (response && response.data) {
      const data = response.data.practices || {}
      return {
        training: data['代码生成'] || [],
        trainingBattle: data['脚本生成'] || [],
        userExchange: data['问题处理'] || []
      }
    }
  } catch (e) {
    console.error('API失败:', e)
  }
  return {
    training: [
      { id: 1, title: '大模型在工业设计中的落地应用案例分享', author: '张工程师', department: '研发一部', time: '2小时前' },
      { id: 2, title: '如何利用AI提升代码质量和开发效率', author: '李开发者', department: '研发二部', time: '5小时前' },
      { id: 3, title: 'AI辅助测试的最佳实践与经验总结', author: '王测试', department: '测试部', time: '昨天' },
      { id: 4, title: '多模态模型在医疗影像分析中的应用', author: '赵医生', department: '产品部', time: '2天前' },
      { id: 5, title: '构建企业级AI知识库的完整方案', author: '陈架构师', department: '架构组', time: '3天前' }
    ],
    trainingBattle: [
      { id: 6, title: 'AI训战实战案例：智能客服系统优化', author: '刘产品', department: '产品部', time: '1小时前' },
      { id: 7, title: '大模型在代码审查中的应用实践', author: '周开发', department: '研发三部', time: '3小时前' }
    ],
    userExchange: []
  }
}

// 新闻
const newsTop = ref<any>({})
const renderedOverview = computed(() => {
  const content = newsTop.value.overview || newsTop.value.summary || newsTop.value.description || ''
  if (!content) return ''
  try {
    const result = marked.parse(String(content), { breaks: true })
    return typeof result === 'string' ? result : content
  } catch (e) {
    return content
  }
})

const loadNewsList = async () => {
  const userInfo = loginService.userInfo
  if (!userInfo || !userInfo.userName) {
    newsTop.value = {
      title: 'AI 领域周报 (2026年1月1日 - 1月7日)',
      summary: '本周 AI 领域持续升温...',
      link: '/news'
    }
    return
  }
  try {
    const userName = userInfo.userName
    if (userName) {
      const userId = userInfo.userId || userInfo.employeeId || userName;
      getAiNews(userId).then((res: any) => {
          const data = res.data || res
          const topItem = Array.isArray(data) ? data[0] : data
          newsTop.value = topItem || {}
        }).catch((err: any) => console.error(err))
    }
  } catch (e) {
    console.error(e)
  }
}

const handleNewsClick = (news: { link?: string; url?: string }) => {
  const targetLink = news.link || news.url
  if (targetLink) {
    if (targetLink.startsWith('http')) window.open(targetLink, '_blank')
    else router.push(targetLink)
  } else {
    router.push(ROUTES.NEWS)
  }
}

// Tools
interface ToolItem {
  id: number
  name: string
  logo?: string
  desc?: string
  color?: string
  link?: string
}
const tools = ref<ToolItem[]>([])
const loadTools = async () => {
  try {
    const response = await getTools()
    if (response && response.data && response.data.list) {
      return response.data.list.map((item: any) => ({
        id: item.id,
        name: item.name,
        desc: item.desc || '',
        logo: item.logo || '',
        link: item.link || `/tools?toolId=${item.id}`,
        color: item.color || '#409eff'
      }))
    }
  } catch (e) { console.error(e) }
  return [
    { id: 1, name: 'TestMate', desc: '自动化测试助手', logo: 'https://picsum.photos/80/80?random=1', link: '/tools/testmate', color: '#36cfc9' },
    { id: 2, name: 'CodeMate', desc: '智能代码补全', logo: 'https://picsum.photos/80/80?random=2', link: '/tools/codemate', color: '#9254de' },
    { id: 3, name: '云集', desc: '云端计算集群', logo: 'https://picsum.photos/80/80?random=3', link: '/tools/yunji', color: '#597ef7' },
    { id: 4, name: '云见', desc: '智能监控平台', logo: 'https://picsum.photos/80/80?random=4', link: '/tools/yunjian', color: '#ff9c6e' },
    { id: 5, name: '扶摇', desc: 'Agent编排引擎', logo: 'https://picsum.photos/80/80?random=5', link: '/tools/fuyao', color: '#4096ff' },
    { id: 6, name: '纠错Agent', desc: '智能代码纠错工具', logo: 'https://picsum.photos/80/80?random=6', link: '/tools/correction-agent', color: '#ffc53d' },
    { id: 7, name: 'DT', desc: '数据转换工具', logo: 'https://picsum.photos/80/80?random=7', link: '/tools/dt', color: '#73d13d' },
  ]
}

const getToolType = (toolName: string): string => {
  const name = toolName.toLowerCase()
  if (name.includes('test') || name.includes('测试')) return 'test'
  if (name.includes('code') || name.includes('代码')) return 'code'
  if (name.includes('云') || name.includes('cloud') || name.includes('算力')) return 'cloud'
  if (name.includes('dt') || name.includes('纠错') || name.includes('转换') || name.includes('效率')) return 'efficiency'
  return 'code'
}

const handleToolClick = (tool: { id: number; link?: string }) => {
  if (tool.link) {
    if (tool.link.startsWith('http')) {
      window.open(tool.link, '_blank')
    } else {
      try {
        const linkUrl = new URL(tool.link, window.location.origin)
        if (!linkUrl.searchParams.has('toolId')) {
          linkUrl.searchParams.set('toolId', String(tool.id))
          router.push(linkUrl.pathname + linkUrl.search)
        } else {
          router.push(tool.link)
        }
      } catch {
        const separator = tool.link.includes('?') ? '&' : '?'
        router.push(`${tool.link}${separator}toolId=${tool.id}`)
      }
    }
  } else {
    router.push({ path: ROUTES.TOOLS, query: { toolId: String(tool.id) } })
  }
}

const handlePracticeClick = (practice: { id: number | string }) => {
  router.push(`/post/${practice.id}`)
}

onMounted(async () => {
  await loginService.validate()
  await checkUserStatus()
  await loadLatestWinners()
  await loadToolPlatform()
  tools.value = await loadTools()
  practices.value = await loadPractices()
  await loadEmpowermentPosts()
  await loadNewsList()
})

const handleConfigUpdate = async () => {
  await loadNewsList()
  await loadToolPlatform()
  tools.value = await loadTools()
  practices.value = await loadPractices()
  await loadEmpowermentPosts()
}
onMounted(() => window.addEventListener('adminConfigUpdated', handleConfigUpdate))
onUnmounted(() => window.removeEventListener('adminConfigUpdated', handleConfigUpdate))
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
  color: rgba(226, 232, 240, 0.9);
  width: 100%;
  box-sizing: border-box;

  @media (max-width: 768px) {
    width: 100%;
    max-width: 100vw;
    overflow-x: hidden;
  }
}

.main-content {
  width: 100%;
  max-width: 1600px;
  margin: 0 auto;
  padding: 20px;
  padding-top: 0;

  @media (max-width: 768px) {
    max-width: 100%;
    padding: 12px;
    padding-top: 0;
  }
  @media (min-width: 769px) and (max-width: 1024px) {
    max-width: 100%;
    padding: 16px;
    padding-top: 0;
  }
}

.hero-section {
  width: 100%;
  position: relative;
  min-height: 400px;
}

/* 间距控制 */
.section-row,
.section-block {
  margin-bottom: 40px;
  width: 100%;
  box-sizing: border-box;

  @media (max-width: 768px) {
    width: 100%;
    margin-left: 0;
    margin-right: 0;
  }
}

.section-block:first-of-type {
  margin-top: 60px;
  padding-top: 0;
  position: relative;
  z-index: 10;
}
.section-row:first-of-type {
  margin-top: 0;
  padding-top: 0;
}

/* -----------------------
   2. 通用组件样式
   -----------------------
*/
.glass-card {
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1.5px solid rgba(0, 102, 255, 0.2) !important;
  border-radius: 16px;
  box-shadow:
    0 0 0 1px rgba(255, 255, 255, 0.6) inset,
    0 10px 25px -5px rgba(0, 102, 255, 0.1),
    0 8px 10px -6px rgba(0, 0, 0, 0.05) !important;
  padding: 28px !important;
  color: rgba(51, 65, 85, 0.88);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;

  &:hover {
    background: rgba(255, 255, 255, 0.98) !important;
    border-color: rgba(0, 102, 255, 0.4) !important;
    box-shadow:
      0 0 0 1px rgba(255, 255, 255, 0.8) inset,
      0 15px 35px -8px rgba(0, 102, 255, 0.15),
      0 12px 15px -8px rgba(0, 0, 0, 0.08),
      0 0 0 1px rgba(0, 102, 255, 0.15) inset !important;
    transform: translateY(-3px);
  }
}

/* -----------------------
   3. AI动态区域 (修复布局)
   -----------------------
*/
.ai-dynamic-section {
  .section-row {
    display: flex;
    align-items: stretch;
    height: 480px;
    width: 100%;
    box-sizing: border-box;
  }

  /* 使用 flex 布局替代 el-row 的栅格系统，避免 gap + 百分比宽度超出 */
  .ai-dynamic-row {
    display: flex !important;
    flex-wrap: nowrap !important;
    gap: 24px;
    > .el-col {
      display: flex;
      height: 100%;
      min-width: 0;
      flex-shrink: 0;
    }
    /* 左侧荣誉殿堂占 2/3 宽度减去 gap 的一半 */
    > .el-col:first-child {
      flex: 0 0 calc(66.666% - 12px);
      max-width: calc(66.666% - 12px);
    }
    /* 右侧使能站占 1/3 宽度减去 gap 的一半 */
    > .el-col:last-child {
      flex: 0 0 calc(33.333% - 12px);
      max-width: calc(33.333% - 12px);
    }
  }

  @media (max-width: 768px) {
    .section-row {
      height: auto;
    }
    .ai-dynamic-row {
      flex-wrap: wrap !important;
      > .el-col:first-child,
      > .el-col:last-child {
        flex: 0 0 100%;
        max-width: 100%;
      }
    }
  }
}

/* ================== AI荣誉殿堂 (新版全宽三列布局) ================== */
.honor-section {
  min-height: 480px !important;
  height: 480px !important;
  width: 100% !important;
  padding: 0 !important;
  overflow: hidden;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.honor-header-bar {
  background: transparent;
  position: relative;
  flex-shrink: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 24px;
  border-bottom: 1px solid rgba(0, 102, 255, 0.08);
  overflow: visible;

  /* 蓝色背景装饰 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 85%;
    height: 100%;
    background: linear-gradient(135deg, #4C85FA 0%, #3a6fd8 100%);
    clip-path: polygon(0 0, 88% 0, 100% 100%, 0 100%);
    pointer-events: none;
    z-index: 1;
  }

  /* 恢复楼宇暗纹 SVG */
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 85%;
    height: 100%;
    background-image:
      url("data:image/svg+xml,%3Csvg width='300' height='50' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M0 50 L0 35 L8 35 L8 22 L14 22 L14 35 L22 35 L22 18 L30 18 L30 35 L38 35 L38 12 L42 12 L42 6 L48 6 L48 12 L52 12 L52 35 L62 35 L62 25 L70 25 L70 35 L80 35 L80 15 L86 15 L86 8 L92 8 L92 15 L98 15 L98 35 L108 35 L108 20 L118 20 L118 35 L128 35 L128 14 L134 14 L134 4 L140 4 L140 14 L146 14 L146 35 L156 35 L156 28 L166 28 L166 35 L176 35 L176 18 L184 18 L184 35 L196 35 L196 22 L202 22 L202 10 L208 10 L208 22 L214 22 L214 35 L226 35 L226 26 L236 26 L236 35 L248 35 L248 16 L256 16 L256 35 L268 35 L268 24 L278 24 L278 35 L290 35 L290 20 L300 20 L300 50 Z' fill='rgba(255,255,255,0.18)'/%3E%3C/svg%3E"),
      linear-gradient(to right, transparent 0%, transparent 50%, rgba(255,255,255,0.15) 50%, rgba(255,255,255,0.15) 100%);
    background-size: 50% 50px, 100% 2px;
    background-position: 0 100%, 0 calc(100% - 8px);
    background-repeat: no-repeat, no-repeat;
    clip-path: polygon(0 0, 88% 0, 100% 100%, 0 100%);
    pointer-events: none;
    z-index: 2;
  }

  .header-title {
    position: relative;
    z-index: 2;
    font-size: 15px;
    color: #ffffff;
    letter-spacing: 0.5px;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.15);
    font-weight: 600;
  }

  .more-btn-pill {
    border-radius: 12px;
    background: transparent;
    border: 1px solid #0066FF;
    color: #0066FF;
    font-size: 13px;
    padding: 6px 16px;
    height: auto;
    font-weight: 500;
    margin: 0;
    transition: all 0.3s ease;
    &:hover {
      background: rgba(0, 102, 255, 0.08);
      border-color: #0066FF;
      transform: translateY(-1px);
    }
  }
}

.honor-content-container {
  flex: 1;
  width: 100%;
  padding: 8px 0 !important;
  overflow: hidden;
  box-sizing: border-box;
  min-height: 0;
}

.winners-three-columns {
  display: flex;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  gap: 0;
}

.winners-column {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
  /* 减小左右内边距，让内容更靠近左侧 */
  padding: 0 8px;
  height: 100%;
  position: relative;
  overflow: hidden;
  justify-content: flex-start;

  &:not(:last-child) {
    border-right: 1px dashed rgba(0, 102, 255, 0.2);
  }
}

/* 获奖者卡片项 */
.winner-card-item {
  display: flex;
  align-items: center;
  /* 调整 gap，控制内部元素紧凑度 */
  gap: 10px;
  padding: 8px 10px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  flex-shrink: 0;
  flex: 0 0 auto;

  /* 添加半透明背景和阴影，让按钮分开 */
  background: rgba(255, 255, 255, 0.5);
  box-shadow: 0 2px 5px rgba(0, 102, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.5);

  /* 确保占满整行 */
  width: 100%;
  box-sizing: border-box;

  &:hover {
    background: rgba(255, 255, 255, 0.9);
    border-color: rgba(0, 102, 255, 0.2);
    transform: translateX(2px);
    box-shadow: 0 4px 8px rgba(0, 102, 255, 0.1);

    .winner-avatar {
      transform: scale(1.08);
    }
    .winner-name {
      color: #0066FF;
    }
  }
}

.winner-avatar {
  flex-shrink: 0;
  border: 2px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  background: #fff;
}

.winner-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
  white-space: nowrap;

  /* 让内容更饱满地分布（可选：如果希望左右撑开，可用 space-between） */
  /* justify-content: space-between; */
}

.winner-name {
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
  letter-spacing: 0.5px;
}

.winner-award {
  font-size: 15px;
  font-weight: 500;
  color: #B45309;
  background: linear-gradient(135deg, rgba(254, 243, 199, 0.8), rgba(253, 230, 138, 0.6));
  padding: 2px 10px;
  border-radius: 6px;
  border: 1px solid rgba(251, 191, 36, 0.3);
  text-overflow: ellipsis;
  overflow: hidden;
  max-width: 140px;
}

@media (max-width: 768px) {
  .winners-three-columns {
    flex-direction: column;
    height: auto;
    gap: 16px;
  }
  .winners-column {
    padding: 0;
    border-right: none !important;
    gap: 10px;
  }
  .honor-section {
    height: auto !important;
    min-height: 500px !important;
  }
}

/* ================== AI使能站 ================== */
.empowerment-section {
  min-height: 480px !important;
  height: 480px !important;
  width: 100% !important;
  display: flex;
  flex-direction: column;
  padding: 0 !important;
  overflow: hidden;
  position: relative;
  flex-shrink: 0;
  background: linear-gradient(180deg, rgba(255, 255, 255, 1) 0%, rgba(240, 247, 255, 0.5) 100%) !important;
  border-top: 1.5px solid #3B82F6 !important;
  border-right: 1.5px solid #E2E8F0 !important;
  border-bottom: 1.5px solid #E2E8F0 !important;
  border-left: 1.5px solid #E2E8F0 !important;
  border-radius: 15px;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 60px;
    height: 4px;
    background: linear-gradient(90deg, #3B82F6 0%, #60A5FA 100%);
    border-radius: 0 0 2px 0;
    z-index: 10;
    pointer-events: none;
  }

  .empowerment-header {
    background: transparent;
    padding: 18px 24px;
    margin: 0;
    border-bottom: 1px solid rgba(0, 102, 255, 0.08);
    border-radius: 15px 15px 0 0;
    position: relative;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .empowerment-title-text {
      color: #1A2B4B;
      font-size: 16px;
      font-weight: 400 !important;
      margin: 0;
      letter-spacing: 0.5px;
    }

    .more-btn-pill {
      background: transparent;
      border: 1px solid #0066FF;
      color: #0066FF;
      border-radius: 12px;
      padding: 6px 16px;
      font-size: 13px;
      font-weight: 500;
      transition: all 0.3s ease;
      &:hover {
        background: rgba(0, 102, 255, 0.08);
        border-color: #0066FF;
        transform: translateY(-1px);
      }
    }
  }

  .empowerment-list {
    flex: 1;
    padding: 20px 24px;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 0;

    .empowerment-item {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 14px 0;
      border-bottom: 0.5px solid rgba(0, 102, 255, 0.08);
      cursor: pointer;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      position: relative;
      overflow: hidden;

      &:last-child { border-bottom: none; }

      &::before {
        content: '';
        position: absolute;
        left: -100%;
        top: 0;
        bottom: 0;
        width: 100%;
        background: linear-gradient(90deg, transparent 0%, rgba(0, 102, 255, 0.08) 50%, transparent 100%);
        transition: left 0.5s cubic-bezier(0.4, 0, 0.2, 1);
        pointer-events: none;
        z-index: 0;
      }

      &:hover {
        &::before { left: 100%; }
        .rank-number { transform: scale(1.1); }
        .empowerment-title { color: #0066FF; }
      }

      .rank-number {
        flex-shrink: 0;
        width: 20px;
        height: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 11px;
        font-weight: 700;
        color: #3B82F6;
        transition: transform 0.3s ease;
        position: relative;
        z-index: 1;
        clip-path: polygon(50% 0%, 100% 50%, 50% 100%, 0% 50%);
        background: rgba(59, 130, 246, 0.1);
        border: 1px solid rgba(59, 130, 246, 0.3);
      }
      .rank-1 { background: rgba(26, 43, 75, 0.15); border-color: rgba(26, 43, 75, 0.4); color: #1A2B4B; }
      .rank-2 { background: rgba(30, 64, 175, 0.15); border-color: rgba(30, 64, 175, 0.4); color: #1E40AF; }
      .rank-3 { background: rgba(59, 130, 246, 0.15); border-color: rgba(59, 130, 246, 0.4); color: #3B82F6; }

      .empowerment-content {
        flex: 1;
        position: relative;
        z-index: 1;
      }
      .empowerment-title {
        font-size: 16px;
        font-weight: 400; /* 不加粗 */
        color: #1A2B4B;
        margin-bottom: 8px;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        overflow: hidden;
        transition: color 0.3s ease;
      }
      .empowerment-meta {
        display: flex;
        align-items: center;
        gap: 16px;
        font-size: 13px; /* 13号字 */
        color: #94A3B8;
        .meta-department {
          display: flex;
          align-items: center;
          &::before {
            content: '🏢';
            margin-right: 4px;
            font-size: 12px;
          }
        }
        .meta-views {
          display: flex;
          align-items: center;
          gap: 4px;
        }
      }
    }
  }
}

/* -----------------------
   4. 右侧悬浮工具面板 (变窄，只显示logo和名称)
   -----------------------
*/
.floating-tools-panel {
  position: absolute;
  right: calc(100% / 2 + 20px + 40px + 10px);
  top: 50%;
  transform: translateY(-50%);
  z-index: 100;
  width: 120px; /* 变窄：从180px改为120px */
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.25);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);

  @media (min-width: 1920px) { right: 120px; }
  @media (min-width: 1400px) and (max-width: 1919px) { right: 80px; }
  @media (max-width: 1400px) { display: none; }

  &:hover {
    background: rgba(255, 255, 255, 0.18);
    transform: translateY(-50%) scale(1.02);
  }

  .tools-panel-header {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 4px;
    padding: 8px 10px;
    background: rgba(255, 255, 255, 0.08);
    border-bottom: 1px solid rgba(255, 255, 255, 0.15);
    span { font-size: 12px; font-weight: 700; color: rgba(255, 255, 255, 0.95); }
    .el-icon { font-size: 14px; color: rgba(255, 255, 255, 0.95); }
  }

  .tools-list {
    display: flex;
    flex-direction: column;
    gap: 4px;
    padding: 6px;
    max-height: 320px;
    overflow-y: auto;
    &::-webkit-scrollbar { width: 3px; }
    &::-webkit-scrollbar-thumb { background: rgba(255, 255, 255, 0.3); border-radius: 2px; }
  }

  .tool-btn {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 6px 8px;
    border-radius: 8px;
    cursor: pointer;
    background: rgba(255, 255, 255, 0.35);
    border: 1px solid rgba(255, 255, 255, 0.4);
    position: relative;
    overflow: hidden;
    backdrop-filter: blur(10px);
    transition: all 0.3s;

    &::before {
      content: '';
      position: absolute;
      left: 0; top: 0; bottom: 0; width: 2px;
      background: var(--tool-color, rgba(255, 255, 255, 0.8));
      transform: scaleY(0);
      transition: transform 0.3s;
    }

    &:hover {
      background: rgba(255, 255, 255, 0.5);
      transform: translateX(2px);
      &::before { transform: scaleY(1); }
      .tool-icon { transform: scale(1.1); }
    }

    .tool-icon {
      width: 24px; height: 24px;
      border-radius: 6px;
      background: rgba(255, 255, 255, 0.4);
      display: flex; align-items: center; justify-content: center;
      flex-shrink: 0;
      transition: all 0.3s;
      img { width: 100%; height: 100%; object-fit: cover; border-radius: 6px; }
      .emoji-logo { font-size: 14px; }
    }

    .tool-name {
      font-size: 11px;
      font-weight: 600;
      color: #1e293b;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      flex: 1;
      min-width: 0;
    }
  }
}

/* -----------------------
   5. AI优秀实践 (减少padding)
   -----------------------
*/
.practice-combined-card {
  padding: 0 !important;
  overflow: hidden;
  height: 480px;
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1.5px solid rgba(0, 102, 255, 0.15) !important;
  border-radius: 16px;
}

.practice-three-columns {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  height: 100%;
  @media (max-width: 992px) { grid-template-columns: repeat(2, 1fr); height: auto; }
  @media (max-width: 768px) { grid-template-columns: 1fr; height: auto; }
}

.practice-column {
  display: flex; flex-direction: column; height: 100%; position: relative;
  &:not(:last-child) { border-right: 1px dashed #D1D5DB; }
  @media (max-width: 992px) {
    &:nth-child(2) { border-right: none; }
    &:nth-child(3) { border-right: none; border-top: 1px dashed #D1D5DB; }
  }
  @media (max-width: 768px) {
    &:not(:last-child) { border-right: none; border-bottom: 1px dashed #D1D5DB; }
  }
}

.practice-column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 16px;
  border-bottom: 1px solid rgba(0, 102, 255, 0.08);
  background: rgba(248, 250, 252, 0.5);
  position: relative;

  /* 小蓝线装饰 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 50px;
    height: 3px;
    background: linear-gradient(90deg, #3B82F6 0%, #60A5FA 100%);
    border-radius: 0 0 2px 0;
    z-index: 10;
    pointer-events: none;
  }

  .column-title { font-size: 15px; font-weight: 600; color: #1A2B4B; margin: 0; }

  .more-btn-pill {
    border-radius: 12px;
    background: transparent;
    border: 1px solid #0066FF;
    color: #0066FF;
    font-size: 13px;
    padding: 6px 16px;
    height: auto;
    font-weight: 500;
    margin: 0;
    transition: all 0.3s ease;
    &:hover {
      background: rgba(0, 102, 255, 0.08);
      border-color: #0066FF;
      transform: translateY(-1px);
    }
  }
}

.practice-column-list {
  flex: 1;
  padding: 4px 12px; /* 减少 padding: 从 8px 16px 改为 4px 12px */
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.practice-item {
  padding: 6px 0 6px 12px; /* 大幅减少 padding: 从 10px 0 10px 16px 改为 6px 0 6px 12px */
  position: relative;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.2s ease;
  flex: 1; display: flex; flex-direction: column; justify-content: center; min-height: 0;

  &:last-child { border-bottom: none; }
  &::before {
    content: ''; position: absolute; left: 0; top: 50%; transform: translateY(-50%);
    width: 4px; height: 4px; background: #3B82F6; /* 稍微缩小标记 */
    clip-path: polygon(50% 0%, 100% 50%, 50% 100%, 0% 50%);
    opacity: 0.6; transition: all 0.2s ease;
  }
  &:hover {
    background: rgba(0, 102, 255, 0.03);
    &::before { opacity: 1; transform: translateY(-50%) scale(1.2); background: #0066FF; }
    .practice-title { color: #0066FF; }
  }
  .practice-title {
    font-size: 15px;
    font-weight: 400; /* 不加粗 */
    color: #334155;
    margin: 0 0 2px 0; /* 减少底部间距 */
    display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden;
  }
  .practice-meta {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 13px; /* 13号字 */
    color: #94A3B8;
    .practice-author::before { content: '👤'; margin-right: 2px; font-size: 10px; }
    .practice-department::before { content: '🏢'; margin-right: 2px; font-size: 10px; }
    .practice-time::before { content: '🕐'; margin-right: 2px; font-size: 10px; }
  }
}

/* -----------------------
   6. AI工具专区 (统一padding)
   -----------------------
*/
.section-title-center {
  text-align: center; margin-bottom: 15px; padding-bottom: 10px; position: relative;
  h2 {
    font-size: 28px; margin: 0; color: #1A2B4B; font-weight: 600; letter-spacing: 2px;
    display: flex; align-items: center; justify-content: center; gap: 15px;
    &::before, &::after {
      content: ''; display: block; width: 40px; height: 24px;
      background-size: contain; background-repeat: no-repeat; background-position: center;
      opacity: 0.9; filter: drop-shadow(0 0 2px rgba(0, 102, 255, 0.3));
    }
    &::before { background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 24 24' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M2 10 Q12 0 22 8 Q15 8 18 12 Q12 10 8 14 Q10 14 12 18 Q5 18 2 10 Z' fill='%230066FF'/%3E%3C/svg%3E"); }
    &::after { transform: scaleX(-1); background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 24 24' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M2 10 Q12 0 22 8 Q15 8 18 12 Q12 10 8 14 Q10 14 12 18 Q5 18 2 10 Z' fill='%230066FF'/%3E%3C/svg%3E"); }
  }
}

.tool-zone-section {
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 24px 0 !important; /* 只保留上下padding，与其他模块对齐 */
  margin: 24px 0;
  width: 100%;

  .section-title-center {
    padding: 0 24px; /* 标题区域单独加padding */
  }
}

.tools-big-buttons-grid {
  display: grid; grid-template-columns: repeat(4, 1fr); gap: 24px; margin-top: 24px;
  padding: 0 24px; /* 工具网格单独加padding */
  @media (max-width: 1200px) { grid-template-columns: repeat(3, 1fr); }
  @media (max-width: 900px) { grid-template-columns: repeat(2, 1fr); gap: 16px; }
  @media (max-width: 560px) { grid-template-columns: 1fr; gap: 12px; }
}

.tool-big-button {
  display: flex; align-items: center; gap: 20px;
  padding: 28px 32px;
  height: 150px;
  border-radius: 20px;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px);
  border: 2px solid rgba(0, 102, 255, 0.15);
  box-shadow: 0 4px 20px rgba(0, 102, 255, 0.08), inset 0 1px 0 rgba(255, 255, 255, 0.8);
  position: relative; overflow: hidden;
  transition: all 0.35s;

  &::before {
    content: ''; position: absolute; left: 0; top: 16px; bottom: 16px; width: 4px;
    border-radius: 0 4px 4px 0;
    background: var(--tool-category-color, #0066FF);
    transition: all 0.3s ease;
  }

  &:hover {
    background: rgba(255, 255, 255, 0.98);
    border-color: var(--tool-category-color, #0066FF);
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(0, 102, 255, 0.15);
    &::before { width: 5px; top: 12px; bottom: 12px; }
    .tool-button-icon { transform: scale(1.08); }
    .tool-button-arrow { opacity: 1; transform: translateX(0); color: var(--tool-category-color, #0066FF); }
    .tool-button-name { color: var(--tool-category-color, #0066FF); }
  }

  .tool-button-icon {
    flex-shrink: 0; width: 64px; height: 64px; border-radius: 16px;
    display: flex; align-items: center; justify-content: center;
    background: linear-gradient(135deg, rgba(0, 102, 255, 0.1), rgba(0, 102, 255, 0.05));
    border: 1px solid rgba(0, 102, 255, 0.1);
    transition: all 0.3s;
    .tool-logo { width: 44px; height: 44px; object-fit: contain; border-radius: 10px; }
    .tool-icon-placeholder {
      width: 44px; height: 44px; display: flex; align-items: center; justify-content: center;
      font-weight: 800; font-size: 22px; color: #fff;
      background: var(--tool-category-color, #0066FF); border-radius: 10px;
    }
  }

  .tool-button-content {
    flex: 1; min-width: 0; display: flex; flex-direction: column; gap: 6px;
    .tool-button-name { font-size: 18px; font-weight: 700; color: #1E293B; }
    .tool-button-desc { font-size: 14px; color: #64748B; display: -webkit-box; -webkit-line-clamp: 2; line-clamp: 2; overflow: hidden; -webkit-box-orient: vertical; }
  }

  .tool-button-arrow {
    flex-shrink: 0; width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;
    opacity: 0; transform: translateX(-8px); transition: all 0.3s; color: #94A3B8;
    .el-icon { font-size: 20px; font-weight: 600; }
  }

  &[data-tool-type="test"] { --tool-category-color: #10B981; }
  &[data-tool-type="code"] { --tool-category-color: #3B82F6; }
  &[data-tool-type="cloud"] { --tool-category-color: #6366F1; }
  &[data-tool-type="efficiency"] { --tool-category-color: #F59E0B; }
}

/* -----------------------
   7. AI资讯
   -----------------------
*/
.wide-banner {
  display: flex; align-items: flex-start; justify-content: space-between; gap: 32px;
  padding: 32px 40px;
  background: rgba(255, 255, 255, 0.55) !important;
  backdrop-filter: blur(25px) saturate(200%);
  border: 1px solid rgba(255, 255, 255, 0.6) !important;
  min-height: 200px; max-height: 300px; overflow: hidden;
  transition: all 0.3s ease;

  &:hover {
    background: rgba(255, 255, 255, 0.65) !important;
    transform: translateY(-2px);
  }

  .info-content {
    flex: 1; min-width: 0; position: relative; z-index: 3;
    h3 { margin: 0 0 16px; color: #1e3a8a; font-weight: 800; font-size: 24px; }
    .news-content-text {
      margin: 0; font-size: 16px; color: #333; line-height: 1.8;
      :deep(p) { margin: 0 0 8px 0; }
    }
  }

  .more-btn-pill {
    position: relative; z-index: 3; font-size: 14px; font-weight: 600;
    padding: 10px 24px; background: transparent; color: rgba(255, 255, 255, 0.92);
    border: 1px solid rgba(0, 242, 255, 0.55);
    transition: all 0.3s ease;
    flex-shrink: 0;
    &:hover { background: rgba(0, 242, 255, 0.06); transform: translateY(-2px); }
  }
}
</style>
