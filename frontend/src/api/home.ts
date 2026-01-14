/**
 * 首页 API
 * 
 * 支持切换真实 API 和 Mock API
 * 通过 USE_REAL_API 配置项控制
 */

import { get, useRealApi, delay, success } from './request'
import type {
  ApiResponse,
  CarouselItem,
  HonorData,
  LatestWinner,
  EmpowermentPost,
  PracticesData,
  ToolPlatformItem,
  ToolItem,
  ToolBannerItem,
  NewsItem,
} from './types'

// ==================== Mock 数据 ====================

const mockCarousel: CarouselItem[] = [
  {
    id: 1,
    image: 'https://picsum.photos/1200/400?random=1',
    title: 'AI社区欢迎您',
    desc: '探索AI技术的无限可能',
    link: '/',
    showContent: true,
    order: 1,
  },
  {
    id: 2,
    image: 'https://picsum.photos/1200/400?random=2',
    title: 'AI优秀实践',
    desc: '分享您的AI实践经验',
    link: '/practices',
    showContent: true,
    order: 2,
  },
  {
    id: 3,
    image: 'https://picsum.photos/1200/400?random=3',
    title: '工具专区',
    desc: '发现强大的AI工具',
    link: '/tools',
    showContent: true,
    order: 3,
  },
]

const mockHonorData: HonorData = {
  bannerImage: 'https://picsum.photos/800/300?random=30',
  awards: [
    { id: 1, name: '年度最佳贡献奖', desc: '2026年度', image: 'https://picsum.photos/200/150?random=31' },
    { id: 2, name: 'AI创新突破奖', desc: '2026年度', image: 'https://picsum.photos/200/150?random=32' },
    { id: 3, name: '效率提升大师', desc: '2026年度', image: 'https://picsum.photos/200/150?random=33' },
    { id: 4, name: '社区贡献奖', desc: '2026年度', image: 'https://picsum.photos/200/150?random=34' },
  ],
}

const mockLatestWinners: LatestWinner[] = [
  { id: 1, name: '林星辰', avatar: 'https://picsum.photos/100/100?random=w1', awardName: '2026年度 AI 技术突破奖' },
  { id: 2, name: 'Sarah', avatar: 'https://picsum.photos/100/100?random=w2', awardName: '最佳 AI 辅助设计实践' },
  { id: 3, name: '张伟', avatar: 'https://picsum.photos/100/100?random=w3', awardName: 'Copilot 效能提升大师' },
  { id: 4, name: '李明', avatar: 'https://picsum.photos/100/100?random=w4', awardName: 'AI 社区贡献之星' },
  { id: 5, name: '王芳', avatar: 'https://picsum.photos/100/100?random=w5', awardName: '最佳实践分享奖' },
  { id: 6, name: '陈刚', avatar: 'https://picsum.photos/100/100?random=w6', awardName: '创新应用先锋' },
  { id: 7, name: '刘洋', avatar: 'https://picsum.photos/100/100?random=w7', awardName: '技术分享达人' },
  { id: 8, name: '赵静', avatar: 'https://picsum.photos/100/100?random=w8', awardName: '最具影响力奖' },
  { id: 9, name: '孙浩', avatar: 'https://picsum.photos/100/100?random=w9', awardName: '年度进步奖' },
]

const mockEmpowermentPosts: EmpowermentPost[] = [
  { id: 1, title: '如何使用 Agent 提升代码开发效率？', tag: '讨论', tagType: 'blue', author: '张三', time: '2小时前', views: 328 },
  { id: 2, title: '分享一个提升工作效率的AI工具使用技巧', tag: '分享', tagType: 'green', author: '李四', time: '3小时前', views: 256 },
  { id: 3, title: '关于AI辅助编程的一些疑问', tag: '提问', tagType: 'orange', author: '王五', time: '5小时前', views: 189 },
  { id: 4, title: 'Prompt工程最佳实践经验总结', tag: '经验', tagType: 'purple', author: '赵六', time: '6小时前', views: 412 },
  { id: 5, title: '推荐几个好用的AI工具', tag: '工具', tagType: 'blue', author: '钱七', time: '8小时前', views: 167 },
  { id: 6, title: 'AI助力团队协作效率提升分享', tag: '分享', tagType: 'green', author: '周八', time: '10小时前', views: 203 },
]

const mockPracticesData: PracticesData = {
  training: [
    { id: 1, title: '大模型在工业设计中的落地应用案例分享', author: '张工程师', time: '2小时前', category: 'training' },
    { id: 2, title: 'AI辅助产品设计全流程培训', author: '李产品', time: '5小时前', category: 'training' },
    { id: 3, title: '智能客服系统建设经验分享', author: '王技术', time: '1天前', category: 'training' },
    { id: 4, title: '企业级AI平台架构设计', author: '陈架构', time: '2天前', category: 'training' },
    { id: 5, title: 'AI代码生成工具使用技巧', author: '赵开发', time: '3天前', category: 'training' },
    { id: 6, title: '深度学习模型优化实战指南', author: '孙算法', time: '4天前', category: 'training' },
  ],
  trainingBattle: [
    { id: 7, title: 'AI训战实战案例：智能客服系统优化', author: '刘产品', time: '1小时前', category: 'training-battle' },
    { id: 8, title: '数据分析AI助手开发实战', author: '孙数据', time: '4小时前', category: 'training-battle' },
    { id: 9, title: '智能文档处理系统构建', author: '周前端', time: '8小时前', category: 'training-battle' },
    { id: 10, title: 'AI驱动的自动化测试实践', author: '吴测试', time: '1天前', category: 'training-battle' },
    { id: 11, title: '智能运维平台建设经验', author: '郑运维', time: '2天前', category: 'training-battle' },
    { id: 12, title: 'AI赋能业务流程自动化', author: '钱业务', time: '3天前', category: 'training-battle' },
  ],
  userExchange: [
    { id: 13, title: '如何利用AI提升日常工作效率', author: '用户A', time: '3小时前', category: 'user-exchange' },
    { id: 14, title: '分享我的AI工具使用心得', author: '用户B', time: '6小时前', category: 'user-exchange' },
    { id: 15, title: 'AI在项目管理中的应用探索', author: '用户C', time: '12小时前', category: 'user-exchange' },
    { id: 16, title: '使用AI辅助写作的经验总结', author: '用户D', time: '1天前', category: 'user-exchange' },
    { id: 17, title: 'AI工具选型经验分享', author: '用户E', time: '2天前', category: 'user-exchange' },
    { id: 18, title: 'ChatGPT在研发流程中的最佳实践', author: '用户F', time: '3天前', category: 'user-exchange' },
  ],
}

const mockToolPlatform: ToolPlatformItem[] = [
  { id: 1, name: 'TestMate', desc: '自动化测试助手', logo: '🧪', color: '#36cfc9', platformUrl: 'https://testmate.example.com' },
  { id: 2, name: 'CodeMate', desc: '智能代码补全', logo: '💻', color: '#9254de', platformUrl: 'https://codemate.example.com' },
  { id: 3, name: '云集', desc: '云端计算集群', logo: '☁️', color: '#597ef7', platformUrl: 'https://yunji.example.com' },
  { id: 4, name: '云见', desc: '智能监控平台', logo: '👁️', color: '#ff9c6e', platformUrl: 'https://yunjian.example.com' },
  { id: 5, name: '扶摇', desc: 'Agent编排引擎', logo: '🚀', color: '#4096ff', platformUrl: 'https://fuyao.example.com' },
]

const mockTools: ToolItem[] = [
  { id: 1, name: 'TestMate', desc: '自动化测试助手', logo: '🧪', color: '#36cfc9', link: '/tools?toolId=1' },
  { id: 2, name: 'CodeMate', desc: '智能代码补全', logo: '💻', color: '#9254de', link: '/tools?toolId=2' },
  { id: 3, name: '云集', desc: '云端计算集群', logo: '☁️', color: '#597ef7', link: '/tools?toolId=3' },
  { id: 4, name: '云见', desc: '智能监控平台', logo: '👁️', color: '#ff9c6e', link: '/tools?toolId=4' },
  { id: 5, name: '扶摇', desc: 'Agent编排引擎', logo: '🚀', color: '#4096ff', link: '/tools?toolId=5' },
]

const mockToolBanners: ToolBannerItem[] = [
  { id: 1, image: 'https://picsum.photos/600/200?random=10', title: '最新 AI 工具推荐', desc: '探索最新发布的 AI 工具，提升你的工作效率', order: 1 },
  { id: 2, image: 'https://picsum.photos/600/200?random=11', title: '热门工具排行榜', desc: '查看最受欢迎的 AI 工具，发现社区精选', order: 2 },
  { id: 3, image: 'https://picsum.photos/600/200?random=12', title: '开发者必备工具', desc: '专为开发者打造的 AI 工具集合', order: 3 },
]

// ==================== Mock API 实现 ====================

const mockGetCarousel = async (): Promise<ApiResponse<{ list: CarouselItem[] }>> => {
  await delay()
  return success({ list: mockCarousel })
}

const mockGetHonor = async (): Promise<ApiResponse<{ honor: HonorData }>> => {
  await delay()
  return success({ honor: mockHonorData })
}

const mockGetLatestWinners = async (limit: number = 9): Promise<ApiResponse<{ list: LatestWinner[] }>> => {
  await delay()
  return success({ list: mockLatestWinners.slice(0, limit) })
}

const mockGetEmpowerment = async (limit: number = 6): Promise<ApiResponse<{ list: EmpowermentPost[] }>> => {
  await delay()
  return success({ list: mockEmpowermentPosts.slice(0, limit) })
}

const mockGetPractices = async (_limit: number = 6): Promise<ApiResponse<PracticesData>> => {
  await delay()
  return success(mockPracticesData)
}

const mockGetToolPlatform = async (): Promise<ApiResponse<{ list: ToolPlatformItem[] }>> => {
  await delay()
  return success({ list: mockToolPlatform })
}

const mockGetTools = async (_featured?: boolean): Promise<ApiResponse<{ list: ToolItem[] }>> => {
  await delay()
  return success({ list: mockTools })
}

const mockGetToolBanners = async (_toolId?: number): Promise<ApiResponse<{ list: ToolBannerItem[] }>> => {
  await delay()
  return success({ list: mockToolBanners })
}

// ==================== API 函数（自动切换真实/Mock） ====================

/**
 * 获取首页轮播图
 * GET /api/home/carousel
 */
export async function getCarousel(): Promise<ApiResponse<{ list: CarouselItem[] }>> {
  if (!useRealApi) {
    return mockGetCarousel()
  }
  return get<{ list: CarouselItem[] }>('/home/carousel')
}

/**
 * 获取荣誉殿堂数据
 * GET /api/home/honor
 */
export async function getHonor(): Promise<ApiResponse<{ honor: HonorData }>> {
  if (!useRealApi) {
    return mockGetHonor()
  }
  return get<{ honor: HonorData }>('/home/honor')
}

/**
 * 获取最新获奖者列表（AI使用达人展示）
 * GET /api/home/latest-winners
 * @param limit 返回数量，默认9
 */
export async function getLatestWinners(limit: number = 9): Promise<ApiResponse<{ list: LatestWinner[] }>> {
  if (!useRealApi) {
    return mockGetLatestWinners(limit)
  }
  return get<{ list: LatestWinner[] }>('/home/latest-winners', { limit })
}

/**
 * 获取赋能交流列表
 * GET /api/home/empowerment
 * @param limit 返回数量，默认6
 */
export async function getEmpowerment(limit: number = 6): Promise<ApiResponse<{ list: EmpowermentPost[] }>> {
  if (!useRealApi) {
    return mockGetEmpowerment(limit)
  }
  return get<{ list: EmpowermentPost[] }>('/home/empowerment', { limit })
}

/**
 * 获取AI优秀实践列表
 * GET /api/home/practices
 * @param limit 每个分类返回的数量，默认6
 */
export async function getPractices(limit: number = 6): Promise<ApiResponse<PracticesData>> {
  if (!useRealApi) {
    return mockGetPractices(limit)
  }
  return get<PracticesData>('/home/practices', { limit })
}

/**
 * 获取悬浮工具平台列表
 * GET /api/home/tool-platform
 */
export async function getToolPlatform(): Promise<ApiResponse<{ list: ToolPlatformItem[] }>> {
  if (!useRealApi) {
    return mockGetToolPlatform()
  }
  return get<{ list: ToolPlatformItem[] }>('/home/tool-platform')
}

/**
 * 获取AI工具专区列表
 * GET /api/tools
 * @param featured 是否只返回推荐工具
 */
export async function getTools(featured?: boolean): Promise<ApiResponse<{ list: ToolItem[] }>> {
  if (!useRealApi) {
    return mockGetTools(featured)
  }
  return get<{ list: ToolItem[] }>('/tools', featured !== undefined ? { featured } : undefined)
}

/**
 * 获取工具专区轮播图
 * GET /api/home/tool-banners
 * @param toolId 指定工具ID，返回该工具相关的Banner
 */
export async function getToolBanners(toolId?: number): Promise<ApiResponse<{ list: ToolBannerItem[] }>> {
  if (!useRealApi) {
    return mockGetToolBanners(toolId)
  }
  return get<{ list: ToolBannerItem[] }>('/home/tool-banners', toolId !== undefined ? { toolId } : undefined)
}

// ==================== Mock 新闻数据 ====================

const mockNews: NewsItem[] = [
  { id: 1, title: 'AI 大模型最新进展发布', summary: '探索人工智能前沿技术', time: '2小时前', source: 'AI快讯' },
  { id: 2, title: '公司获得国家级AI创新奖项', summary: '技术实力再获认可', time: '5小时前', source: '公司新闻' },
  { id: 3, title: 'Copilot 3.0 新功能上线', summary: '代码补全效率提升50%', time: '1天前', source: '产品动态' },
  { id: 4, title: 'AI 社区月度活动精彩回顾', summary: '本月共举办12场技术分享', time: '2天前', source: '社区动态' },
  { id: 5, title: '全球AI峰会下周召开', summary: '多位业界专家将发表演讲', time: '3天前', source: '行业资讯' },
]

async function mockGetNews(): Promise<ApiResponse<{ list: NewsItem[] }>> {
  await delay(300)
  return success({ list: mockNews })
}

/**
 * 获取新闻资讯
 * GET /api/home/news
 */
export async function getNews(): Promise<ApiResponse<{ list: NewsItem[] }>> {
  if (!useRealApi) {
    return mockGetNews()
  }
  return get<{ list: NewsItem[] }>('/home/news')
}

// ==================== 导出所有 API ====================

export const homeApi = {
  getCarousel,
  getHonor,
  getLatestWinners,
  getEmpowerment,
  getPractices,
  getToolPlatform,
  getTools,
  getToolBanners,
  getNews,
}

export default homeApi
