<template>
  <div class="page-container">
    <section class="hero-section">
      <HeroCarousel />
      <!-- 右侧悬浮工具按钮列表 - 使用 home/tool-platform 接口 -->
      <div class="floating-tools-panel">
        <div class="tools-panel-header">
          <el-icon><Trophy /></el-icon>
          <span>工具平台</span>
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
            <div class="tool-info">
              <span class="tool-name">{{ tool.name }}</span>
              <span class="tool-desc">{{ tool.desc }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <main class="main-content">
      <!-- AI动态 -->
      <section class="section-block ai-dynamic-section">
        <div class="section-title-center">
          <h2>AI 动态</h2>
        </div>

      <el-row :gutter="24" class="section-row">
        <el-col :xs="24" :md="16">
          <div class="glass-card honor-section">
            <!-- 顶部标题条 -->
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

            <div class="honor-split-container" style="padding: 20px;">
              <!-- 左边：荣誉殿堂 -->
              <div class="honor-hall">
                <div class="honor-banner" @click="router.push(ROUTES.USERS)" style="cursor: pointer;">
                  <img
                    v-if="honorBannerImage"
                    :src="honorBannerImage"
                    alt="荣誉殿堂"
                    class="banner-image"
                  />
                  <div v-else class="banner-placeholder">
                    <el-icon><Trophy /></el-icon>
                    <span>荣誉殿堂</span>
                  </div>
                  <div class="banner-overlay">
                    <div class="banner-content">
                      <h3>荣誉殿堂</h3>
                      <p>见证每一个闪耀时刻</p>
                    </div>
                  </div>
                </div>
                <div class="awards-grid">
                  <div
                    v-for="award in honorAwards"
                    :key="award.id"
                    class="honor-ribbon-btn"
                    @click="handleAwardClick(award)"
                  >
                    <div class="ribbon-shape">
                      <span class="ribbon-text">{{ award.name }}</span>
                      <div class="gold-shine"></div>
                    </div>
                    <div class="ribbon-tail-left"></div>
                    <div class="ribbon-tail-right"></div>
                  </div>
                </div>
              </div>

              <!-- 右边：AI使用达人 -->
              <div class="ai-users">
                <div class="users-header">
                  <h4>AI使用达人</h4>
                  <el-tag effect="dark" round size="small" color="#626aef">最新</el-tag>
                </div>
                <div class="users-grid winners-grid">
                  <div 
                    class="winner-card" 
                    v-for="winner in latestWinners" 
                    :key="winner.id"
                    @click="router.push({ path: '/users', query: { keyword: winner.name } })"
                  >
                    <el-avatar 
                      :size="40" 
                      :src="winner.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" 
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
              <h3>🗣️ AI使能站</h3>
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
                v-for="post in empowermentPosts"
                :key="post.id"
                class="empowerment-item"
                @click="router.push(ROUTES.EMPOWERMENT)"
              >
              <el-tooltip
                :content="post.title"
                placement="top"
                :show-after="500"
              >
                <div class="empowerment-title">{{ post.title }}</div>
              </el-tooltip>
                <div class="empowerment-meta">
                  <span class="meta-time">{{ post.time }}</span>
                  <span class="meta-views">
                    <el-icon><View /></el-icon>
                    {{ post.views }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      </section>

      <!-- AI优秀实践 -->
      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 优秀实践</h2>
        </div>

      <div class="practice-unified-container">
        <!-- 代码生成 -->
        <div class="practice-module-card glass-card">
          <div class="practice-header-bar training-header">
            <h3 class="header-title">💻 代码生成</h3>
            <el-button
              text
              size="small"
              class="more-btn-pill header-more-btn"
              @click="router.push({ path: ROUTES.PRACTICES, query: { tag: '代码生成' } })"
            >
              更多
            </el-button>
          </div>
          <div class="practice-list">
            <div
              v-for="practice in practices.training.slice(0, 6)"
              :key="practice.id"
              class="practice-item"
              @click="handlePracticeClick(practice)"
            >
              <el-tooltip
                :content="practice.title"
                placement="top"
                :show-after="500"
              >
                <h4 class="practice-title">{{ practice.title }}</h4>
              </el-tooltip>
              <div class="practice-meta">
                <span class="practice-author">{{ practice.author }}</span>
                <span class="practice-time">{{ practice.time }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 脚本生成 -->
        <div class="practice-module-card glass-card">
          <div class="practice-header-bar battle-header">
            <h3 class="header-title">📜 脚本生成</h3>
            <el-button
              text
              size="small"
              class="more-btn-pill header-more-btn"
              @click="router.push({ path: ROUTES.PRACTICES, query: { tag: '脚本生成' } })"
            >
              更多
            </el-button>
          </div>
          <div class="practice-list">
            <div
              v-for="practice in practices.trainingBattle.slice(0, 6)"
              :key="'train-' + practice.id"
              class="practice-item"
              @click="handlePracticeClick(practice)"
            >
              <el-tooltip
                :content="practice.title"
                placement="top"
                :show-after="500"
              >
                <h4 class="practice-title">{{ practice.title }}</h4>
              </el-tooltip>
              <div class="practice-meta">
                <span class="practice-author">{{ practice.author }}</span>
                <span class="practice-time">{{ practice.time }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 问题处理 -->
        <div class="practice-module-card glass-card">
          <div class="practice-header-bar exchange-header">
            <h3 class="header-title">🔧 问题处理</h3>
            <el-button
              text
              size="small"
              class="more-btn-pill header-more-btn"
              @click="router.push({ path: ROUTES.PRACTICES, query: { tag: '问题处理' } })"
            >
              更多
            </el-button>
          </div>
          <div class="practice-list">
            <div
              v-for="practice in practices.userExchange.slice(0, 6)"
              :key="'exchange-' + practice.id"
              class="practice-item"
              @click="handlePracticeClick(practice)"
            >
              <el-tooltip
                :content="practice.title"
                placement="top"
                :show-after="500"
              >
                <h4 class="practice-title">{{ practice.title }}</h4>
              </el-tooltip>
              <div class="practice-meta">
                <span class="practice-author">{{ practice.author }}</span>
                <span class="practice-time">{{ practice.time }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      </section>

      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 百宝箱</h2>
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
              class="tool-card hover-effect"
              @click="handleToolClick(tool)"
              :style="{
                '--tool-color': tool.color || '#409eff',
                '--tool-bg': hexToRgba(tool.color || '#409eff', 0.06),
                '--tool-bg-hover': hexToRgba(tool.color || '#409eff', 0.12),
                '--tool-border': hexToRgba(tool.color || '#409eff', 0.25),
                '--tool-shadow': hexToRgba(tool.color || '#409eff', 0.25)
              }"
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
                  class="tool-icon-placeholder"
                >
                  {{ tool.name[0] }}
                </div>
              </div>
              <div class="tool-info">
                <h4>{{ tool.name }}</h4>
                <p>{{ tool.desc }}</p>
              </div>
              <div class="tool-action-icon">
                <el-icon><ArrowRight /></el-icon>
              </div>
            </div>
          </el-col>
        </el-row>
      </section>

      <!-- AI资讯 -->
      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 资讯</h2>
        </div>

      <div class="glass-card wide-banner section-row" v-if="newsTop.title">
        <div class="info-content">
          <h3>{{ newsTop.title }}</h3>
          <p>
            {{ newsTop.summary || newsTop.description || '' }}
          </p>
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
import { Trophy, Star, View, ArrowRight } from '@element-plus/icons-vue'
import HeroCarousel from '@/components/HeroCarousel.vue'
// API 层 - 支持 Mock/Real API 自动切换
import { getHonor, getToolPlatform, getTools, getPractices, getToolBanners, getLatestWinners, getEmpowerment, getNews, getAiNews } from '../api/home'
import loginService from '../utils/loginService'
import { addCommunity, getManager } from '../api/user'
import type { LatestWinner } from '../api/types'
import { ROUTES } from '../router/paths'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()

// 用户相关状态
const isMember = ref(false)
const showJoinButton = ref(true)
const isAdmin = ref(false)
const userInfo = ref<any>({})

// 检查用户状态 (合并旧平台 AiTopBar 逻辑)
const checkUserStatus = async () => {
  const cachedUser = loginService.userInfo
  if (cachedUser) {
    userInfo.value = cachedUser
    isMember.value = !!cachedUser.isMember
    showJoinButton.value = !isMember.value
    
    // 检查管理员权限
    try {
      const res = await getManager()
      const adminIds = (res.data || []).map((item: any) => item.userName)
      // 注意：这里假设 userId 是数字或字符串，需保持一致
      isAdmin.value = adminIds.includes(String(cachedUser.userId))
    } catch (e) {
      console.warn('获取管理员列表失败', e)
    }
  }
}

// 加入社区
const handleJoinCommunity = async () => {
  if (!userInfo.value.userId) {
    ElMessage.warning('请先登录')
    loginService.login()
    return
  }
  
  try {
    const res = await addCommunity(userInfo.value.userId)
    if (res && (res.data || (res as any).succeed)) {
      ElMessage.success('已加入社区')
      showJoinButton.value = false
      isMember.value = true
      // 刷新用户信息缓存
      loginService.logout() // 简单处理：登出让用户重新登录刷新，或者手动更新缓存
      loginService.login() 
    }
  } catch (e) {
    console.error('加入社区失败', e)
    ElMessage.error('加入社区失败，请稍后重试')
  }
}

// 辅助函数：十六进制颜色转 RGBA
const hexToRgba = (hex: string, alpha: number) => {
  let r = 0, g = 0, b = 0
  // 处理 #RRGGBB
  if (hex && hex.length === 7) {
    r = parseInt(hex.slice(1, 3), 16)
    g = parseInt(hex.slice(3, 5), 16)
    b = parseInt(hex.slice(5, 7), 16)
  } else if (hex && hex.length === 4) {
    // 处理 #RGB
    r = parseInt(hex.slice(1, 2) + hex.slice(1, 2), 16)
    g = parseInt(hex.slice(2, 3) + hex.slice(2, 3), 16)
    b = parseInt(hex.slice(3, 4) + hex.slice(3, 4), 16)
  } else {
    // 默认颜色 (如果hex无效) - 使用深蓝色 fallback
    return `rgba(64, 158, 255, ${alpha})`
  }
  return `rgba(${r}, ${g}, ${b}, ${alpha})`
}

// 荣誉殿堂配置
const loadHonorConfig = async () => {
  try {
    // 优先从API获取
    const response = await getHonor()
    if (response && response.data && response.data.honor) {
      return {
        bannerImage: response.data.honor.bannerImage || '',
        awards: response.data.honor.awards || []
      }
    }
  } catch (e) {
    console.error('加载荣誉殿堂配置失败:', e)
  }

  // 默认数据
  return {
    bannerImage: 'https://picsum.photos/800/300?random=30',
    awards: [
      { id: 1, name: '年度最佳贡献奖', desc: '2026年度', image: 'https://picsum.photos/200/150?random=31' },
      { id: 2, name: 'AI创新突破奖', desc: '2026年度', image: 'https://picsum.photos/200/150?random=32' },
      { id: 3, name: '效率提升大师', desc: '2026年度', image: 'https://picsum.photos/200/150?random=33' },
      { id: 4, name: '社区贡献奖', desc: '2026年度', image: 'https://picsum.photos/200/150?random=34' }
    ]
  }
}

const honorConfig = ref({ bannerImage: '', awards: [] as Array<{ id: number; name: string; desc?: string; image?: string; year?: string }> })
const honorBannerImage = computed(() => honorConfig.value.bannerImage)
const honorAwards = computed(() => honorConfig.value.awards)

// AI使用达人 - 最新获奖者列表
const latestWinners = ref<LatestWinner[]>([])
const loadLatestWinners = async () => {
  try {
    const response = await getLatestWinners(9)
    latestWinners.value = response.data?.list || []
  } catch (e) {
    console.error('加载最新获奖者失败:', e)
    latestWinners.value = []
  }
}

// 处理荣誉殿堂奖项点击 - 跳转到团队荣誉页面
const handleAwardClick = (award: { id: number; name: string; desc?: string; year?: string }) => {
  // 从 desc 字段提取年份，如 "2026年度" -> "2026"
  let year = award.year
  if (!year && award.desc) {
    const match = award.desc.match(/(\d{4})/)
    if (match) {
      year = match[1]
    }
  }
  // 如果没有年份，使用当前年份
  if (!year) {
    year = new Date().getFullYear().toString()
  }

  // 跳转到 AI使用达人页面，并传递参数
  router.push({
    path: '/users',
    query: {
      type: 'team',           // 切换到团队荣誉
      year: year,             // 选中的年份
      award: award.name       // 选中的奖项名称
    }
  })
}

// 赋能交流数据
interface EmpowermentItem {
  id: number | string
  title: string
  time: string
  views: number
}
const empowermentPosts = ref<EmpowermentItem[]>([])

// 加载赋能交流数据 (home/empowerment)
const loadEmpowermentPosts = async () => {
  try {
    const response = await getEmpowerment(7)
    if (response && response.data && response.data.list) {
      empowermentPosts.value = response.data.list.map((item: { id: number; title: string; time?: string; views?: number }) => ({
        id: item.id,
        title: item.title,
        time: item.time || '刚刚',
        views: item.views || Math.floor(Math.random() * 500) + 50
      }))
    }
  } catch (e) {
    console.error('加载赋能交流数据失败:', e)
    // 使用默认数据
    empowermentPosts.value = [
      { id: 1, title: '如何使用 Agent 提升代码开发效率？', time: '2小时前', views: 328 },
      { id: 2, title: '分享一个提升工作效率的AI工具使用技巧', time: '3小时前', views: 256 },
      { id: 3, title: '关于AI辅助编程的一些疑问', time: '5小时前', views: 189 },
      { id: 4, title: 'Prompt工程最佳实践经验总结', time: '6小时前', views: 412 },
      { id: 5, title: '推荐几个好用的AI工具', time: '8小时前', views: 167 },
      { id: 6, title: 'AI助力团队协作效率提升分享', time: '10小时前', views: 203 },
    ]
  }
}

// 初始化加载所有配置
onMounted(async () => {
  // 验证登录状态
  await loginService.validate()
  await checkUserStatus()
  
  honorConfig.value = await loadHonorConfig()
  await loadLatestWinners()
  await loadToolPlatform() // 加载悬浮工具平台 (/api/home/tool-platform)
  tools.value = await loadTools() // 加载AI工具专区列表 (/api/tools)
  toolZoneBanners.value = await loadToolBanners()
  practices.value = await loadPractices()
  await loadEmpowermentPosts()
  await loadNewsList()
})

// 监听配置更新
const handleConfigUpdate = async () => {
  await loadNewsList()
  await loadToolPlatform() // 加载悬浮工具平台
  tools.value = await loadTools() // 加载AI工具专区列表
  toolZoneBanners.value = await loadToolBanners()
  honorConfig.value = await loadHonorConfig()
  practices.value = await loadPractices()
  await loadEmpowermentPosts()
}

onMounted(() => {
  window.addEventListener('adminConfigUpdated', handleConfigUpdate)
})

onUnmounted(() => {
  window.removeEventListener('adminConfigUpdated', handleConfigUpdate)
})

// AI优秀实践数据
const loadPractices = async () => {
  try {
    // 优先从API获取
    const response = await getPractices()
    if (response && response.data) {
      return {
        training: response.data.training || [],
        trainingBattle: response.data.trainingBattle || [],
        userExchange: response.data.userExchange || []
      }
    }
  } catch (e) {
    console.error('从API加载AI优秀实践失败，使用默认数据:', e)
  }

  // 默认数据
  return {
    training: [
      {
        id: 1,
        title: '大模型在工业设计中的落地应用案例分享',
        author: '张工程师',
        time: '2小时前',
        category: 'training'
      },
      {
        id: 2,
        title: '如何利用AI提升代码质量和开发效率',
        author: '李开发者',
        time: '5小时前',
        category: 'training'
      },
      {
        id: 3,
        title: 'AI辅助测试的最佳实践与经验总结',
        author: '王测试',
        time: '昨天',
        category: 'training'
      },
      {
        id: 4,
        title: '多模态模型在医疗影像分析中的应用',
        author: '赵医生',
        time: '2天前',
        category: 'training'
      },
      {
        id: 5,
        title: '构建企业级AI知识库的完整方案',
        author: '陈架构师',
        time: '3天前',
        category: 'training'
      }
    ],
    trainingBattle: [
      {
        id: 6,
        title: 'AI训战实战案例：智能客服系统优化',
        author: '刘产品',
        time: '1小时前',
        category: 'training-battle'
      },
      {
        id: 7,
        title: '大模型在代码审查中的应用实践',
        author: '周开发',
        time: '3小时前',
        category: 'training-battle'
      }
    ],
    userExchange: []
  }
}

interface PracticePost {
  id: number | string
  title: string
  description?: string
  image?: string
  createTime?: string
  author?: string
  time?: string
}
const practices = ref({
  training: [] as PracticePost[],
  trainingBattle: [] as PracticePost[],
  userExchange: [] as PracticePost[]
})

// 新闻数据
const newsTop = ref<any>({})

const loadNewsList = async () => {
  // 尝试从 LoginService 获取用户信息
  const userInfo = loginService.userInfo
  
  if (!userInfo || !userInfo.userName) {
    // 默认数据
    newsTop.value = {
      title: 'AI 领域周报 (2026年1月1日 - 1月7日)',
      summary: '本周 AI 领域持续升温，OpenAI 发布最新推理模型，国内大模型在长文本处理上取得突破性进展。同时，AI 在软件工程中的实际落地效果获得更多量化验证...',
      link: '/news'
    }
    return
  }

  try {
    const userName = userInfo.userName

    if (userName) {
      getAiNews(userName)
        .then((res: any) => {
          // 适配接口返回
          const data = res.data || res
          const topItem = Array.isArray(data) ? data[0] : data
          newsTop.value = topItem || {}
        })
        .catch((err: any) => {
          console.error('获取AI资讯失败:', err)
        })
    }
  } catch (e) {
    console.error('获取AI资讯失败:', e)
  }
}

// 处理头条点击
const handleNewsClick = (news: { link?: string; url?: string }) => {
  const targetLink = news.link || news.url
  if (targetLink) {
    if (targetLink.startsWith('http')) {
      window.open(targetLink, '_blank')
    } else {
      router.push(targetLink)
    }
  } else {
    router.push(ROUTES.NEWS)
  }
}


// AI工具列表配置 - 从API加载 (/api/tools)
const loadTools = async () => {
  try {
    const response = await getTools()
    if (response && response.data && response.data.list && response.data.list.length > 0) {
      return response.data.list.map((item: { id: number; name: string; desc?: string; logo?: string; link?: string; color?: string }) => ({
        id: item.id,
        name: item.name,
        desc: item.desc || '',
        logo: item.logo || '',
        link: item.link || `/tools?toolId=${item.id}`,
        color: item.color || '#409eff'
      }))
    }
  } catch (e) {
    console.error('加载工具列表失败:', e)
  }

  // 默认数据
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

interface ToolItem {
  id: number
  name: string
  logo?: string
  logoType?: string
  desc?: string
  color?: string
  link?: string
}

// 悬浮工具平台数据 - 从 home/tool-platform 加载
interface ToolPlatformItem {
  id: number
  name: string
  desc?: string
  logo?: string
  color?: string
  platformUrl?: string
}
const toolPlatform = ref<ToolPlatformItem[]>([])

// 加载悬浮工具平台列表 (/api/home/tool-platform)
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

  // 默认数据
  toolPlatform.value = [
    { id: 1, name: 'TestMate', desc: '自动化测试助手', logo: '🧪', color: '#36cfc9', platformUrl: 'https://testmate.example.com' },
    { id: 2, name: 'CodeMate', desc: '智能代码补全', logo: '💻', color: '#9254de', platformUrl: 'https://codemate.example.com' },
    { id: 3, name: '云集', desc: '云端计算集群', logo: '☁️', color: '#597ef7', platformUrl: 'https://yunji.example.com' },
    { id: 4, name: '云见', desc: '智能监控平台', logo: '👁️', color: '#ff9c6e', platformUrl: 'https://yunjian.example.com' },
    { id: 5, name: '扶摇', desc: 'Agent编排引擎', logo: '🚀', color: '#4096ff', platformUrl: 'https://fuyao.example.com' },
  ]
}

// AI工具专区列表 - 从 /api/tools 加载
const tools = ref<ToolItem[]>([])

// AI工具专区Banner配置 - 从API加载
const loadToolBanners = async () => {
  try {
    const response = await getToolBanners()
    if (response && response.data && response.data.list && response.data.list.length > 0) {
      return response.data.list.map((item: { title?: string; desc?: string; image?: string }) => ({
        title: item.title || '',
        desc: item.desc || '',
        image: item.image || ''
      }))
    }
  } catch (e) {
    console.error('加载工具Banner配置失败:', e)
  }
  // 默认数据
  return [
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
  ]
}

// 根据工具数量计算列宽（一行最多4个）
const getColSpan = (count: number) => {
  if (count <= 4) return 24 / count
  return 6 // 最多4个，每个占6列（24/4=6）
}

// 处理工具点击跳转
const handleToolClick = (tool: { id: number; link?: string }) => {
  // 如果配置了link，使用link跳转（link应该包含toolId参数用于过滤）
  if (tool.link) {
    if (tool.link.startsWith('http')) {
      window.open(tool.link, '_blank')
    } else {
      // 解析link，确保包含toolId参数
      try {
        const linkUrl = new URL(tool.link, window.location.origin)
        // 如果link中没有toolId参数，自动添加
        if (!linkUrl.searchParams.has('toolId')) {
          linkUrl.searchParams.set('toolId', String(tool.id))
          router.push(linkUrl.pathname + linkUrl.search)
        } else {
          router.push(tool.link)
        }
      } catch {
        // 如果link不是完整URL，直接使用并添加toolId参数
        const separator = tool.link.includes('?') ? '&' : '?'
        router.push(`${tool.link}${separator}toolId=${tool.id}`)
      }
    }
  } else {
    // 如果没有link，跳转到工具专区，并传递toolId参数
    router.push({
      path: ROUTES.TOOLS,
      query: {
        toolId: String(tool.id)
      }
    })
  }
}

// 处理工具平台点击跳转（悬浮面板 - 使用 platformUrl 跳转到外部平台）
const handleToolPlatformClick = (tool: { id: number; platformUrl?: string }) => {
  // 悬浮工具栏的工具点击后跳转到外部平台
  if (tool.platformUrl) {
    window.open(tool.platformUrl, '_blank')
  } else {
    // 如果没有配置 platformUrl，跳转到工具专区
    router.push({
      path: ROUTES.TOOLS,
      query: {
        toolId: String(tool.id)
      }
    })
  }
}

// 处理实践点击
const handlePracticeClick = (_practice: { id: number | string }) => {
  // 跳转到实践详情或列表页
  router.push(ROUTES.PRACTICES)
}

// AI工具专区轮播图数据
const toolZoneBanners = ref<{ title: string; desc: string; image: string }[]>([])
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
  position: relative;
  min-height: 500px; /* 确保有足够高度，与轮播图高度一致 */
  /* 轮播图区域 */
}

/* 右侧悬浮工具面板 */
.floating-tools-panel {
  position: absolute;
  right: 80px; /* 固定右边距，避免遮挡轮播图右箭头 */
  top: 50%;
  transform: translateY(-50%);
  z-index: 100;

  /* 大屏幕时增加右边距，保持与轮播图的视觉平衡 */
  @media (min-width: 1920px) {
    right: 120px;
  }

  /* 中等屏幕保持固定边距 */
  @media (min-width: 1400px) and (max-width: 1919px) {
    right: 80px;
  }
  width: 220px; /* 减小宽度 */
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.25);
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(255, 255, 255, 0.1) inset,
    0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);

  /* 添加光效背景 */
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background:
      radial-gradient(ellipse at top right, rgba(255, 255, 255, 0.15), transparent 60%),
      linear-gradient(135deg, rgba(255, 255, 255, 0.05) 0%, transparent 50%);
    pointer-events: none;
    border-radius: 20px;
  }

  &:hover {
    background: rgba(255, 255, 255, 0.18);
    border-color: rgba(255, 255, 255, 0.35);
    box-shadow:
      0 12px 48px rgba(0, 0, 0, 0.2),
      0 0 0 1px rgba(255, 255, 255, 0.15) inset,
      0 4px 16px rgba(0, 0, 0, 0.15);
    transform: translateY(-50%) scale(1.02);
  }

  @media (max-width: 1400px) {
    display: none; /* 小屏幕隐藏 */
  }
}

.tools-panel-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px; /* 减小内边距 */
  background: rgba(255, 255, 255, 0.08);
  border-bottom: 1px solid rgba(255, 255, 255, 0.15);
  position: relative;
  z-index: 1;

  .el-icon {
    font-size: 18px; /* 减小图标 */
    color: rgba(255, 255, 255, 0.95);
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
  }

  span {
    font-size: 14px; /* 减小字体 */
    font-weight: 700;
    color: rgba(255, 255, 255, 0.95);
    text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  }
}

.tools-list {
  display: flex;
  flex-direction: column;
  gap: 6px; /* 减小间距 */
  padding: 10px; /* 减小内边距 */
  max-height: 350px; /* 减小最大高度 */
  overflow-y: auto;
  position: relative;
  z-index: 1;

  /* 隐藏滚动条但保持滚动功能 */
  scrollbar-width: thin;
  scrollbar-color: rgba(255, 255, 255, 0.3) transparent;

  &::-webkit-scrollbar {
    width: 4px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.3);
    border-radius: 2px;

    &:hover {
      background: rgba(255, 255, 255, 0.5);
    }
  }
}

.tool-btn {
  display: flex;
  align-items: center;
  gap: 10px; /* 减小间距 */
  padding: 8px 12px; /* 减小内边距 */
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.35); /* 提高透明度，更白 */
  border: 1px solid rgba(255, 255, 255, 0.4);
  position: relative;
  overflow: hidden;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);

  /* 左侧彩色指示条 */
  &::before {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    width: 3px;
    background: var(--tool-color, rgba(255, 255, 255, 0.8));
    transform: scaleY(0);
    transition: transform 0.3s;
    border-radius: 0 2px 2px 0;
  }

  /* 光效 */
  &::after {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
    opacity: 0;
    transition: opacity 0.3s;
    pointer-events: none;
  }

  &:hover {
    background: rgba(255, 255, 255, 0.5); /* 悬停时更白 */
    border-color: rgba(255, 255, 255, 0.6);
    transform: translateX(3px);
    box-shadow:
      0 4px 12px rgba(0, 0, 0, 0.15),
      0 0 0 1px rgba(255, 255, 255, 0.2) inset;

    &::before {
      transform: scaleY(1);
    }

    &::after {
      opacity: 1;
    }

    .tool-icon {
      transform: scale(1.1) rotate(5deg);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    }

    .tool-name {
      color: #1e293b; /* 悬停时保持黑色 */
    }
  }

  &:active {
    transform: translateX(2px);
  }
}

.tool-icon {
  width: 36px; /* 减小图标尺寸 */
  height: 36px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.4); /* 提高透明度，更白 */
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 10px;
  }

  .el-icon {
    font-size: 20px; /* 减小图标字体 */
    color: rgba(255, 255, 255, 0.95);
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
  }
}

.tool-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 3px; /* 减小间距 */
  min-width: 0;
}

.tool-name {
  font-size: 13px; /* 减小字体 */
  font-weight: 700;
  color: #1e293b; /* 改为黑色 */
  transition: all 0.3s;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.tool-desc {
  font-size: 11px; /* 减小字体 */
  color: #64748b; /* 改为深灰色 */
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
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

/* 第一个 section-block (AI动态) 往下挪一点，并在轮播图上层 */
.section-block:first-of-type {
  margin-top: 60px; /* 增加间距，离轮播图远一点 */
  padding-top: 0;
  position: relative;
  z-index: 10; /* 确保在轮播图上层 */
}

/* 第一个 section-row 减少顶部间距 */
.section-row:first-of-type {
  margin-top: 0;
  padding-top: 0;
}

/* 通用毛玻璃卡片 - 带渐变质感的毛玻璃效果 */
.glass-card {
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.85) 0%,
    rgba(230, 240, 255, 0.75) 30%,
    rgba(240, 230, 255, 0.7) 60%,
    rgba(255, 255, 255, 0.8) 100%
  ) !important;
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  padding: 24px;
  box-shadow:
    0 8px 32px 0 rgba(31, 38, 135, 0.12),
    inset 0 2px 0 rgba(255, 255, 255, 0.9),
    inset 0 -2px 4px rgba(200, 210, 230, 0.15),
    0 0 0 1px rgba(255, 255, 255, 0.5);
  color: #000000;
  transition: all 0.3s ease;
  position: relative;
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;

  /* 渐变光晕叠加层 - 更明显的效果 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: 
      radial-gradient(
        ellipse at 0% 0%,
        rgba(100, 150, 255, 0.15) 0%,
        transparent 50%
      ),
      radial-gradient(
        ellipse at 100% 100%,
        rgba(180, 120, 255, 0.12) 0%,
        transparent 50%
      );
    pointer-events: none;
    z-index: 0;
  }

  /* 顶部高光效果，增强立体感 */
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(
      90deg,
      transparent,
      rgba(255, 255, 255, 1),
      transparent
    );
    border-radius: 16px 16px 0 0;
    z-index: 2;
  }

  /* 确保内容在光效之上 */
  > * {
    position: relative;
    z-index: 3;
  }

  /* 悬停时增强透亮效果 */
  &:hover {
    background: rgba(255, 255, 255, 0.65);
    border-color: rgba(255, 255, 255, 0.7);
    box-shadow:
      0 12px 40px 0 rgba(31, 38, 135, 0.25),
      inset 0 1px 0 rgba(255, 255, 255, 0.8),
      0 0 0 1px rgba(255, 255, 255, 0.4);
    transform: translateY(-2px);
  }
}

.full-height {
  height: 100%;
  min-height: 300px;
}

/* AI动态区域 - 两个模块等高固定 */
.ai-dynamic-section {
  .section-row {
    display: flex;
    align-items: stretch;

    > .el-col {
      display: flex;
    }
  }
}

.honor-section {
  min-height: 560px !important;
  height: 560px !important; /* 固定高度，预留8个奖项位置 */
  padding: 0; /* 移除内边距，让标题条占满 */
  overflow: hidden; /* 确保圆角正确显示 */
}

/* 赋能交流区块 */
.empowerment-section {
  min-height: 560px !important;
  height: 560px !important; /* 与荣誉殿堂等高 */
  display: flex;
  flex-direction: column;
  padding: 0;
  overflow: hidden;

  .empowerment-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 14px 20px;
    margin: 0;
    border-bottom: none;
    border-radius: 16px 16px 0 0;
    position: relative;
    overflow: hidden;

    /* 对话气泡暗纹 - 符合交流主题 - 更明显 */
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-image:
        /* 对话气泡图案 - 更明显 */
        url("data:image/svg+xml,%3Csvg width='80' height='50' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M5 10 Q5 4 12 4 L32 4 Q39 4 39 10 L39 24 Q39 30 32 30 L18 30 L10 38 L10 30 Q5 30 5 24 Z' fill='rgba(255,255,255,0.12)' stroke='rgba(255,255,255,0.2)' stroke-width='1'/%3E%3Cpath d='M45 18 Q45 12 52 12 L68 12 Q75 12 75 18 L75 32 Q75 38 68 38 L58 38 L65 46 L60 38 L52 38 Q45 38 45 32 Z' fill='rgba(255,255,255,0.08)' stroke='rgba(255,255,255,0.15)' stroke-width='1'/%3E%3C/svg%3E"),
        /* 点阵连接纹理 - 更明显 */
        url("data:image/svg+xml,%3Csvg width='50' height='50' xmlns='http://www.w3.org/2000/svg'%3E%3Ccircle cx='8' cy='8' r='3' fill='rgba(255,255,255,0.12)'/%3E%3Ccircle cx='42' cy='42' r='3' fill='rgba(255,255,255,0.12)'/%3E%3Cline x1='8' y1='8' x2='42' y2='42' stroke='rgba(255,255,255,0.08)' stroke-width='1' stroke-dasharray='3,3'/%3E%3C/svg%3E");
      background-size: 80px 50px, 50px 50px;
      background-position: 0 0, 25px 0;
      background-repeat: repeat, repeat;
      pointer-events: none;
      z-index: 1;
    }

    /* 右侧斜切装饰 - 更明显 */
    &::after {
      content: '';
      position: absolute;
      top: 0;
      right: 0;
      width: 100px;
      height: 100%;
      background: linear-gradient(
        -65deg,
        rgba(255, 255, 255, 0.2) 0%,
        rgba(255, 255, 255, 0.1) 50%,
        transparent 100%
      );
      pointer-events: none;
      z-index: 1;
    }

    h3 {
      position: relative;
      z-index: 2;
      color: #ffffff;
      font-size: 15px;
      font-weight: 600;
      text-shadow: 0 1px 2px rgba(0, 0, 0, 0.15);
    }

    .more-btn-pill {
      position: relative;
      z-index: 2;
      background: rgba(255, 255, 255, 0.2);
      border: 1px solid rgba(255, 255, 255, 0.35);
      color: #ffffff;

      &:hover {
        background: rgba(255, 255, 255, 0.3);
        border-color: rgba(255, 255, 255, 0.5);
      }
    }
  }

  .empowerment-list {
    flex: 1;
    padding: 16px 20px;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 0;

    .empowerment-item {
      padding: 10px 0;
      border-bottom: 1px solid rgba(0, 0, 0, 0.06);
      cursor: pointer;
      transition: all 0.2s ease;

      &:last-child {
        border-bottom: none;
      }

      &:hover {
        .empowerment-title {
          color: #667eea;
        }
      }

      .empowerment-title {
        font-size: 15px;
        font-weight: 600;
        color: #1a1a2e;
        line-height: 1.4;
        margin-bottom: 6px;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        overflow: hidden;
        transition: color 0.2s ease;
        letter-spacing: 0.3px;
      }

      .empowerment-meta {
        display: flex;
        align-items: center;
        gap: 16px;
        font-size: 12px;
        color: #909399;

        .meta-time {
          display: flex;
          align-items: center;
        }

        .meta-views {
          display: flex;
          align-items: center;
          gap: 4px;

          .el-icon {
            font-size: 14px;
          }
        }
      }
    }
  }
}

/* 顶部标题条 - AI荣誉殿堂 */
.honor-header-bar {
  background: transparent;
  position: relative;
  height: 60px; /* 增加固定高度 */
  display: flex;
  justify-content: space-between;
  align-items: flex-start; /* 顶部对齐，以便制造落差 */
  border-radius: 16px 16px 0 0;
  overflow: hidden;

  /* 左侧蓝色背景 + 斜切分割（从左上到右下） */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 85%;
    height: 100%;
    background: linear-gradient(135deg, #4C85FA 0%, #3a6fd8 100%);
    /* 斜切效果 - 左上到右下 \ 形状 */
    clip-path: polygon(0 0, 88% 0, 100% 100%, 0 100%);
    /* 楼宇暗纹 */
    background-image:
      url("data:image/svg+xml,%3Csvg width='300' height='50' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M0 50 L0 35 L8 35 L8 22 L14 22 L14 35 L22 35 L22 18 L30 18 L30 35 L38 35 L38 12 L42 12 L42 6 L48 6 L48 12 L52 12 L52 35 L62 35 L62 25 L70 25 L70 35 L80 35 L80 15 L86 15 L86 8 L92 8 L92 15 L98 15 L98 35 L108 35 L108 20 L118 20 L118 35 L128 35 L128 14 L134 14 L134 4 L140 4 L140 14 L146 14 L146 35 L156 35 L156 28 L166 28 L166 35 L176 35 L176 18 L184 18 L184 35 L196 35 L196 22 L202 22 L202 10 L208 10 L208 22 L214 22 L214 35 L226 35 L226 26 L236 26 L236 35 L248 35 L248 16 L256 16 L256 35 L268 35 L268 24 L278 24 L278 35 L290 35 L290 20 L300 20 L300 50 Z' fill='rgba(255,255,255,0.18)'/%3E%3C/svg%3E"),
      linear-gradient(135deg, #4C85FA 0%, #3a6fd8 100%);
    background-size: 300px 50px, 100% 100%;
    background-position: 0 100%, 0 0;
    background-repeat: repeat-x, no-repeat;
    pointer-events: none;
    z-index: 1;
  }

  /* 右侧透明区域背景 */
  &::after {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 30%;
    height: 100%;
    background: transparent;
    pointer-events: none;
    z-index: 0;
  }

  /* 标题文字 */
  .header-title {
    position: relative;
    z-index: 2;
    margin: 16px 0 0 24px; /* 定位：保持较高位置 */
    font-size: 15px;
    font-weight: 600;
    color: #ffffff;
    letter-spacing: 0.5px;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.15);
  }

  /* 标题条内的更多按钮 - 灰色样式（在透明区域） */
  .more-btn-pill {
    position: relative;
    z-index: 2;
    border-radius: 999px;
    background: rgba(100, 100, 100, 0.1);
    border: 1px solid rgba(100, 100, 100, 0.4);
    color: #555555;
    font-size: 13px;
    padding: 6px 16px;
    height: auto;
    font-weight: 500;
    transition: all 0.3s ease;
    backdrop-filter: blur(4px);
    margin: 24px 24px 0 0; /* 定位：下移，制造落差 */

    &:hover {
      background: rgba(100, 100, 100, 0.2);
      border-color: rgba(100, 100, 100, 0.6);
      transform: translateY(-1px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
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
  position: relative;

  /* 灰色虚线分割线 */
  &::before {
    content: '';
    position: absolute;
    left: 50%;
    top: 0;
    bottom: 0;
    width: 1px;
    border-left: 1px dashed #d1d5db; /* 灰色虚线 */
    transform: translateX(-50%);
  }
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

  .banner-placeholder {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
    color: #fff;
    gap: 12px;

    .el-icon {
      font-size: 48px;
    }

    span {
      font-size: 20px;
      font-weight: 700;
    }
  }
}

/* 奖项网格 - 金色绶带样式 */
.awards-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px 16px;
  padding-bottom: 10px;
}

.honor-ribbon-btn {
  cursor: pointer;
  position: relative;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  display: flex;
  justify-content: center;
  padding-bottom: 12px;
  margin: 0 8px; /* 左右增加间隙 */
  width: auto; /* 让它自适应变窄 */

  &:hover {
    transform: translateY(-4px) scale(1.03);
    
    .ribbon-shape {
      background: linear-gradient(
        180deg,
        rgba(255, 215, 0, 0.9) 0%,
        rgba(255, 179, 71, 0.9) 50%,
        rgba(255, 140, 0, 0.9) 100%
      );
      box-shadow: 
        0 6px 20px rgba(255, 165, 0, 0.4),
        inset 0 1px 0 rgba(255, 255, 255, 0.4);
    }

    .ribbon-tail-left,
    .ribbon-tail-right {
      background: linear-gradient(180deg, #ff8c00 0%, #cc7000 100%);
    }
  }
}

.ribbon-shape {
  position: relative;
  /* 金色渐变 - 带透明度 */
  background: linear-gradient(
    180deg,
    rgba(255, 224, 102, 0.85) 0%,
    rgba(255, 201, 64, 0.85) 30%,
    rgba(255, 176, 32, 0.85) 70%,
    rgba(255, 149, 0, 0.85) 100%
  );
  border: none;
  padding: 10px 16px; /* 减小内边距，使整体变窄 */
  min-width: 100px; /* 减小最小宽度 */
  text-align: center;
  color: #78350f;
  font-weight: 800;
  font-size: 13px; /* 字体稍微调小 */
  letter-spacing: 0.5px;
  box-shadow: 
    0 2px 4px rgba(0, 0, 0, 0.05),
    0 8px 16px rgba(251, 191, 36, 0.15);
  overflow: hidden; /* 隐藏溢出的流光 */
  width: 100%;
  z-index: 2;
  border-radius: 2px;
}

/* 绶带左尾巴 - 折叠效果 */
.ribbon-tail-left {
  position: absolute;
  top: 10px; /* 向下偏移 */
  left: -12px; /* 向左伸出 */
  width: 24px;
  height: 36px; /* 高度增加 */
  background: linear-gradient(180deg, #d97706 0%, #b45309 100%); /* 深色阴影部分 */
  clip-path: polygon(100% 0, 100% 100%, 0 50%, 0 0); /* 只有折叠三角形部分 */
  z-index: -1; /* 在主体下方 */
  
  /* 添加燕尾飘带伪元素 */
  &::before {
    content: '';
    position: absolute;
    top: 10px; /* 从折叠下方延伸 */
    left: -16px; /* 向外延伸 */
    width: 30px;
    height: 36px;
    background: linear-gradient(180deg, #ff9500 0%, #cc7000 100%);
    clip-path: polygon(0 0, 100% 0, 100% 100%, 0 70%); /* 燕尾形状 */
    z-index: -2;
  }
}

/* 绶带右尾巴 - 折叠效果 */
.ribbon-tail-right {
  position: absolute;
  top: 10px;
  right: -12px;
  width: 24px;
  height: 36px;
  background: linear-gradient(180deg, #d97706 0%, #b45309 100%);
  clip-path: polygon(0 0, 0 100%, 100% 50%, 100% 0);
  z-index: -1;

  /* 添加燕尾飘带伪元素 */
  &::before {
    content: '';
    position: absolute;
    top: 10px;
    right: -16px;
    width: 30px;
    height: 36px;
    background: linear-gradient(180deg, #ff9500 0%, #cc7000 100%);
    clip-path: polygon(0 0, 100% 0, 100% 70%, 0 100%);
    z-index: -2;
  }
}

.ribbon-text {
  position: relative;
  z-index: 1;
  display: block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-shadow: 0 1px 0 rgba(255, 255, 255, 0.3);
}

.gold-shine {
  position: absolute;
  top: 0;
  left: -100%;
  width: 50%;
  height: 100%;
  background: linear-gradient(
    to right,
    transparent,
    rgba(255, 255, 255, 0.7),
    transparent
  );
  transform: skewX(-20deg);
  animation: shine 3s infinite;
}

@keyframes shine {
  0% {
    left: -100%;
  }
  50%, 100% {
    left: 150%;
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

/* AI使用达人 - 最新获奖者网格 */
.winners-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.winner-card {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 8px;
  padding: 10px 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  transition: all 0.3s;
  cursor: pointer;

  &:hover {
    background: rgba(255, 255, 255, 0.15);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .winner-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 2px;
    width: 100%;
    overflow: hidden;
  }

  .winner-name {
    font-size: 13px;
    font-weight: 600;
    color: #1f2937;
    text-align: center;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 100%;
  }

  .winner-award {
    font-size: 10px;
    color: #92400e;
    text-align: center;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 100%;
    background: linear-gradient(135deg, rgba(251, 191, 36, 0.5), rgba(245, 158, 11, 0.4));
    padding: 3px 8px;
    border-radius: 4px;
    font-weight: 500;
    border: 1px solid rgba(251, 191, 36, 0.3);
  }
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
  margin-bottom: 15px; /* 减少标题和模块之间的间距 */

  h2 {
    font-size: 28px;
    margin: 0;
    color: #1e3a8a; /* 深蓝色标题 */
    font-weight: 700;
    text-shadow: none; /* 移除阴影，深色背景不需要 */
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 15px;

    /* 左右小翅膀装饰 */
    &::before,
    &::after {
      content: '';
      display: block;
      width: 40px;
      height: 24px;
      background-size: contain;
      background-repeat: no-repeat;
      background-position: center;
      opacity: 0.8;
    }

    &::before {
      /* 左翅膀 SVG - 抽象流线型 */
      background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 24 24' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M2 10 Q12 0 22 8 Q15 8 18 12 Q12 10 8 14 Q10 14 12 18 Q5 18 2 10 Z' fill='%231e3a8a'/%3E%3C/svg%3E");
    }

    &::after {
      /* 右翅膀 SVG - 翻转 */
      transform: scaleX(-1);
      background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 24 24' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M2 10 Q12 0 22 8 Q15 8 18 12 Q12 10 8 14 Q10 14 12 18 Q5 18 2 10 Z' fill='%231e3a8a'/%3E%3C/svg%3E");
    }
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
  position: relative;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 24px;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  
  /* 动态背景 - 渐变毛玻璃 */
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.85), var(--tool-bg));
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid var(--tool-border);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);

  /* 光效层 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, 
      rgba(255, 255, 255, 0.4) 0%, 
      rgba(255, 255, 255, 0.1) 100%
    );
    z-index: 0;
    opacity: 0.5;
    pointer-events: none;
  }

  /* 装饰圆 */
  &::after {
    content: '';
    position: absolute;
    top: -30px;
    right: -30px;
    width: 100px;
    height: 100px;
    background: var(--tool-color);
    filter: blur(40px);
    opacity: 0.1;
    border-radius: 50%;
    z-index: 0;
    transition: opacity 0.3s, transform 0.3s;
    pointer-events: none;
  }

  > * {
    position: relative;
    z-index: 1;
  }

  &:hover {
    transform: translateY(-4px) scale(1.01);
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), var(--tool-bg-hover));
    box-shadow: 
      0 12px 30px -8px var(--tool-shadow),
      0 4px 10px rgba(0, 0, 0, 0.05);
    border-color: var(--tool-color);

    &::after {
      opacity: 0.2;
      transform: scale(1.2);
    }

    .tool-logo-wrapper {
      transform: scale(1.05) rotate(-3deg);
      box-shadow: 0 8px 16px -4px var(--tool-shadow);
    }

    .tool-action-icon {
      opacity: 1;
      transform: translateX(0);
      color: var(--tool-color);
    }
  }

  .tool-logo-wrapper {
    flex-shrink: 0;
    width: 52px;
    height: 52px;
    transition: all 0.3s ease;
    border-radius: 14px;
    background: #fff;
    padding: 6px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
    display: flex;
    align-items: center;
    justify-content: center;

    .tool-logo {
      width: 100%;
      height: 100%;
      object-fit: contain;
      border-radius: 8px;
    }

    .tool-icon-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 800;
      font-size: 22px;
      color: #fff;
      background: var(--tool-color);
      border-radius: 10px;
    }
  }

  .tool-info {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    gap: 4px;

    h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 700;
      color: #1a1a1a;
      letter-spacing: 0.3px;
    }

    p {
      margin: 0;
      font-size: 13px;
      color: #666;
      line-height: 1.4;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
  }

  .tool-action-icon {
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transform: translateX(-10px);
    transition: all 0.3s ease;
    color: #999;
    
    .el-icon {
      font-size: 18px;
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

/* AI优秀实践 - 三栏布局 */
.practice-unified-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  width: 100%;

  @media (max-width: 992px) {
    grid-template-columns: repeat(2, 1fr);
  }

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
}

/* AI优秀实践 - 单个模块卡片 */
.practice-module-card {
  padding: 0 !important;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  min-height: 380px;
}

/* 通用标题栏样式 */
.practice-header-bar {
  position: relative;
  padding: 14px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  overflow: hidden;
  border-radius: 16px 16px 0 0;

  /* 波浪纹理效果 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image:
      /* 波浪纹理 */
      url("data:image/svg+xml,%3Csvg width='100' height='20' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M0 10 Q25 0 50 10 T100 10' fill='none' stroke='rgba(255,255,255,0.15)' stroke-width='1.5'/%3E%3C/svg%3E"),
      /* 点阵纹理 */
      radial-gradient(circle, rgba(255,255,255,0.1) 1px, transparent 1px);
    background-size: 100px 20px, 8px 8px;
    background-position: 0 50%, 0 0;
    background-repeat: repeat-x, repeat;
    pointer-events: none;
    z-index: 1;
  }

  /* 标题 */
  .header-title {
    position: relative;
    z-index: 2;
    margin: 0;
    font-size: 15px;
    font-weight: 600;
    color: #ffffff;
    letter-spacing: 0.5px;
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
  }

  /* 更多按钮 */
  .header-more-btn {
    position: relative;
    z-index: 2;
    background: rgba(255, 255, 255, 0.25) !important;
    border: 1px solid rgba(255, 255, 255, 0.4) !important;
    color: #ffffff !important;
    border-radius: 20px;
    padding: 4px 14px;
    font-size: 12px;
    transition: all 0.2s ease;

    &:hover {
      background: rgba(255, 255, 255, 0.4) !important;
      transform: translateY(-1px);
    }
  }
}

/* 培训赋能 - 蓝紫渐变 */
.training-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  &::after {
    content: '';
    position: absolute;
    top: -50%;
    right: -20%;
    width: 120px;
    height: 120px;
    background: radial-gradient(circle, rgba(255,255,255,0.2) 0%, transparent 70%);
    border-radius: 50%;
    pointer-events: none;
  }
}

/* AI训战 - 粉红渐变 */
.battle-header {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);

  &::after {
    content: '';
    position: absolute;
    top: -30%;
    left: 10%;
    width: 100px;
    height: 100px;
    background: radial-gradient(circle, rgba(255,255,255,0.15) 0%, transparent 70%);
    border-radius: 50%;
    pointer-events: none;
  }
}

/* 用户交流 - 青蓝渐变 */
.exchange-header {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);

  &::after {
    content: '';
    position: absolute;
    bottom: -40%;
    right: 5%;
    width: 110px;
    height: 110px;
    background: radial-gradient(circle, rgba(255,255,255,0.15) 0%, transparent 70%);
    border-radius: 50%;
    pointer-events: none;
  }
}

/* 列表区域 */
.practice-list {
  flex: 1;
  padding: 16px 20px;
  display: flex;
  flex-direction: column;
  gap: 0;
}

/* 单条帖子 */
.practice-item {
  padding: 12px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.2s ease;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    .practice-title {
      color: #667eea;
    }
  }

  .practice-title {
    font-size: 14px;
    font-weight: 500;
    color: #1a1a2e;
    line-height: 1.5;
    margin: 0 0 6px 0;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    overflow: hidden;
    transition: color 0.2s ease;
  }

  .practice-meta {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 12px;
    color: #909399;

    .practice-author {
      &::before {
        content: '👤';
        margin-right: 4px;
      }
    }

    .practice-time {
      &::before {
        content: '🕐';
        margin-right: 4px;
      }
    }
  }
}

/* Section 4: AI Info */
.wide-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 32px 40px;
  /* 使用发白的毛玻璃质感，覆盖原有的渐变背景 */
  background: rgba(255, 255, 255, 0.55) !important;
  backdrop-filter: blur(25px) saturate(200%);
  -webkit-backdrop-filter: blur(25px) saturate(200%);
  border: 1px solid rgba(255, 255, 255, 0.6) !important;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;

  /* 流动光感效果 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 50%;
    height: 100%;
    background: linear-gradient(
      90deg,
      transparent,
      rgba(255, 255, 255, 0.3),
      rgba(255, 255, 255, 0.5),
      rgba(255, 255, 255, 0.3),
      transparent
    );
    animation: glassShimmer 4s ease-in-out infinite;
    pointer-events: none;
    z-index: 1;
  }

  /* 顶部高光效果 */
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(
      90deg,
      transparent,
      rgba(255, 255, 255, 0.8),
      transparent
    );
    border-radius: 16px 16px 0 0;
    z-index: 2;
  }

  &:hover {
    background: rgba(255, 255, 255, 0.65) !important;
    border-color: rgba(255, 255, 255, 0.7) !important;
    box-shadow:
      0 12px 40px 0 rgba(31, 38, 135, 0.25),
      inset 0 1px 0 rgba(255, 255, 255, 0.8),
      0 0 0 1px rgba(255, 255, 255, 0.4);
    transform: translateY(-2px);
  }

  .info-content {
    flex: 1;
    position: relative;
    z-index: 3; /* 确保内容在光效之上 */

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
    z-index: 3; /* 确保按钮在光效之上 */
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

/* 流动光感动画 */
@keyframes glassShimmer {
  0% {
    left: -50%;
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    left: 150%;
    opacity: 0;
  }
}

</style>
