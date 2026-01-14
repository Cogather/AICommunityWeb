/**
 * 管理后台 API
 *
 * 支持切换真实 API 和 Mock API
 * 通过 USE_REAL_API 配置项控制
 */

import { get, post, put, del, useRealApi, delay, success } from './request'
import type { ApiResponse } from './types'

// ==================== 类型定义 ====================

/** 轮播图配置项 */
export interface CarouselItem {
  id: number
  image: string
  link?: string
  showContent?: boolean
  title?: string
  desc?: string
  order?: number
}

/** 工具配置项 */
export interface ToolConfigItem {
  id: number
  name: string
  desc?: string
  logo?: string
  color?: string
  link?: string
}

/** 工具Banner配置项 */
export interface ToolBannerItem {
  id: number
  image: string
  title?: string
  desc?: string
  order?: number
}

/** 奖项设置 */
export interface AwardSetting {
  id: number
  name: string
  description?: string
  criteria?: string[]
  cycle?: string
}

/** 奖项名称（下拉选择用） */
export interface AwardName {
  id: number
  name: string
}

/** 获奖者 */
export interface Winner {
  id: number
  name: string
  awardTime?: string
  awardName?: string
}

/** 推荐获奖者 */
export interface RecommendedWinner {
  id: number
  odooId?: string
  employeeId?: string
  name: string
  avatar?: string
  department?: string
  points?: number
  postsCount?: number
  commentsCount?: number
  activitiesCount?: number
  likesReceived?: number
  favoritesReceived?: number
  hasAwarded?: boolean
  honorId?: number | null
}

/** 团队奖项图片 */
export interface TeamAwardImage {
  id: number
  image: string
  winnerName?: string
  teamField?: string
  story?: string
}

/** 团队奖项 */
export interface TeamAward {
  id: number
  title: string
  year: number
  images: TeamAwardImage[]
}

/** 精华帖子 */
export interface FeaturedPost {
  id: number
  title: string
  author?: string
  createTime?: string
  zone?: string
  toolId?: number
}

/** 精选合集配置项 */
export interface CollectionItem {
  id: number
  postId: number
  note?: string
}

/** 用户 */
export interface AdminUser {
  id: number
  name: string
  email?: string
  department?: string
  role?: string
}

/** 活动 */
export interface Activity {
  id: number
  title: string
  type?: string
  date?: string
  location?: string
  cover?: string
  content?: string
  toolId?: number
  status?: string
  participants?: number
  maxParticipants?: number
}

// ==================== Mock 数据 ====================

const mockCarouselList: CarouselItem[] = [
  { id: 1, image: 'https://picsum.photos/1200/400?random=1', link: '/', showContent: true, title: 'AI社区欢迎您', desc: '探索AI技术的无限可能', order: 1 },
  { id: 2, image: 'https://picsum.photos/1200/400?random=2', link: '/practices', showContent: true, title: 'AI优秀实践', desc: '分享您的AI实践经验', order: 2 },
  { id: 3, image: 'https://picsum.photos/1200/400?random=3', link: '/tools', showContent: true, title: '工具专区', desc: '发现强大的AI工具', order: 3 },
]

const mockToolsList: ToolConfigItem[] = [
  { id: 1, name: 'TestMate', desc: '自动化测试助手', logo: '🧪', color: '#36cfc9', link: '/tools?toolId=1' },
  { id: 2, name: 'CodeMate', desc: '智能代码补全', logo: '💻', color: '#9254de', link: '/tools?toolId=2' },
  { id: 3, name: '云集', desc: '云端计算集群', logo: '☁️', color: '#597ef7', link: '/tools?toolId=3' },
  { id: 4, name: '云见', desc: '智能监控平台', logo: '👁️', color: '#ff9c6e', link: '/tools?toolId=4' },
  { id: 5, name: '扶摇', desc: 'Agent编排引擎', logo: '🚀', color: '#4096ff', link: '/tools?toolId=5' },
]

const mockToolBannersList: ToolBannerItem[] = [
  { id: 1, image: 'https://picsum.photos/600/200?random=10', title: '最新 AI 工具推荐', desc: '探索最新发布的 AI 工具' },
  { id: 2, image: 'https://picsum.photos/600/200?random=11', title: '热门工具排行榜', desc: '查看最受欢迎的 AI 工具' },
]

const mockAwardSettings: AwardSetting[] = [
  { id: 1, name: '年度最佳贡献奖', description: '表彰在AI技术方案上有重大突破的个人', criteria: ['提交创新方案不少于2篇', '落地至少1个生产项目'], cycle: '年度' },
  { id: 2, name: 'AI创新突破奖', description: '在AI技术创新方面表现突出', criteria: ['引入自动化工具并落地'], cycle: '季度' },
  { id: 3, name: '效率提升大师', description: '在工程效能提升方面贡献突出', criteria: ['显著提升交付速度'], cycle: '月度' },
]

const mockAwardNames: AwardName[] = [
  { id: 1, name: '年度最佳贡献奖' },
  { id: 2, name: 'AI创新突破奖' },
  { id: 3, name: '效率提升大师' },
  { id: 4, name: '社区贡献奖' },
]

const mockWinners: Winner[] = [
  { id: 1, name: '张工程师', awardTime: '2024-01', awardName: '年度最佳贡献奖' },
  { id: 2, name: '李产品', awardTime: '2024-02', awardName: 'AI创新突破奖' },
]

const mockRecommendedWinners: RecommendedWinner[] = [
  { id: 1, employeeId: 'E001', name: '张三', avatar: 'https://picsum.photos/100/100?random=r1', department: '研发部', points: 1200, postsCount: 15, commentsCount: 42, activitiesCount: 8, likesReceived: 156, favoritesReceived: 45, hasAwarded: false, honorId: null },
  { id: 2, employeeId: 'E002', name: '李四', avatar: 'https://picsum.photos/100/100?random=r2', department: '产品部', points: 980, postsCount: 12, commentsCount: 35, activitiesCount: 6, likesReceived: 120, favoritesReceived: 32, hasAwarded: false, honorId: null },
  { id: 3, employeeId: 'E003', name: '王五', avatar: 'https://picsum.photos/100/100?random=r3', department: '测试部', points: 850, postsCount: 10, commentsCount: 28, activitiesCount: 5, likesReceived: 98, favoritesReceived: 25, hasAwarded: true, honorId: 100 },
]

const mockTeamAwards: TeamAward[] = [
  {
    id: 1,
    title: '年度最佳团队奖',
    year: 2024,
    images: [
      { id: 1, image: 'https://picsum.photos/400/300?random=t1', winnerName: 'AI研发团队', teamField: '人工智能', story: '<p>团队在AI技术应用方面取得了显著成绩...</p>' },
    ]
  },
]

const mockFeaturedPosts = {
  practices: [
    { id: 1, title: 'AI大会2024精彩回顾', author: '张三', createTime: '2024-01-10', zone: 'practices' },
  ],
  empowerment: [
    { id: 2, title: '如何高效使用AI工具', author: '李四', createTime: '2024-01-08', zone: 'empowerment' },
  ],
  agent: [
    { id: 3, title: '扶摇Agent入门指南', author: '王五', createTime: '2024-01-05', zone: 'agent' },
  ],
  otherTools: [] as FeaturedPost[],
}

const mockCollections: CollectionItem[] = [
  { id: 1, postId: 1, note: '顶级AI研究论文' },
  { id: 2, postId: 2, note: '实用工具推荐' },
]

const mockUsersList: AdminUser[] = [
  { id: 1, name: '张三', email: 'zhangsan@example.com', department: '研发部', role: 'admin' },
  { id: 2, name: '李四', email: 'lisi@example.com', department: '产品部', role: 'tool_owner' },
  { id: 3, name: '王五', email: 'wangwu@example.com', department: '测试部', role: 'user' },
]

const mockActivities: Activity[] = [
  { id: 1, title: 'AI技术分享会', type: 'activity', date: '2024-02-15', location: '会议室A', status: 'upcoming', participants: 25, maxParticipants: 50 },
  { id: 2, title: 'Agent开发培训', type: 'training', date: '2024-02-20', location: '培训中心', status: 'upcoming', participants: 18, maxParticipants: 30 },
]

// ==================== Mock API 实现 ====================

// 首页管理
const mockGetCarouselConfig = async (): Promise<ApiResponse<{ list: CarouselItem[] }>> => {
  await delay()
  return success({ list: mockCarouselList })
}

const mockSaveCarouselConfig = async (_list: CarouselItem[]): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

const mockGetHonorBannerConfig = async (): Promise<ApiResponse<{ bannerImage: string }>> => {
  await delay()
  return success({ bannerImage: 'https://picsum.photos/800/300?random=honor' })
}

const mockSaveHonorBannerConfig = async (_bannerImage: string): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

const mockGetToolsConfig = async (): Promise<ApiResponse<{ list: ToolConfigItem[] }>> => {
  await delay()
  return success({ list: mockToolsList })
}

const mockSaveToolsConfig = async (_list: ToolConfigItem[]): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

const mockGetToolBannersConfig = async (): Promise<ApiResponse<{ list: ToolBannerItem[] }>> => {
  await delay()
  return success({ list: mockToolBannersList })
}

const mockSaveToolBannersConfig = async (_list: ToolBannerItem[]): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

// AI使用达人管理
const mockGetHonorAwardsConfig = async (): Promise<ApiResponse<{ list: AwardSetting[] }>> => {
  await delay()
  return success({ list: mockAwardSettings })
}

const mockSavePersonalAwardsConfig = async (_list: AwardSetting[]): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

const mockGetAwardsList = async (): Promise<ApiResponse<{ list: AwardName[] }>> => {
  await delay()
  return success({ list: mockAwardNames })
}

const mockSaveAward = async (_award: AwardSetting): Promise<ApiResponse<{ id: number }>> => {
  await delay()
  return success({ id: Date.now() })
}

const mockDeleteAward = async (_id: number): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

const mockGetWinnersConfig = async (): Promise<ApiResponse<{ list: Winner[] }>> => {
  await delay()
  return success({ list: mockWinners })
}

const mockSaveWinnersConfig = async (_list: Winner[]): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

const mockGetRecommendedWinners = async (_month?: string): Promise<ApiResponse<{ list: RecommendedWinner[] }>> => {
  await delay()
  return success({ list: mockRecommendedWinners })
}

const mockSetUserAward = async (_params: { userId: number; awardId: number; awardDate: string }): Promise<ApiResponse<{ honorId: number }>> => {
  await delay()
  return success({ honorId: Date.now() })
}

const mockCancelUserAward = async (_params: { userId: number; honorId: number }): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

const mockGetTeamAwardsConfig = async (): Promise<ApiResponse<{ list: TeamAward[] }>> => {
  await delay()
  return success({ list: mockTeamAwards })
}

const mockSaveTeamAwardsConfig = async (_list: TeamAward[]): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

// 精华帖子管理
const mockGetAllFeaturedPosts = async (): Promise<ApiResponse<typeof mockFeaturedPosts>> => {
  await delay()
  return success(mockFeaturedPosts)
}

const mockRemoveFeaturedPost = async (_postId: number): Promise<ApiResponse<{ success: boolean }>> => {
  await delay()
  return success({ success: true })
}

const mockGetEmpowermentFeaturedPostsConfig = async (): Promise<ApiResponse<{ list: CollectionItem[] }>> => {
  await delay()
  return success({ list: mockCollections })
}

const mockSaveEmpowermentFeaturedPostsConfig = async (_list: CollectionItem[]): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

const mockGetOtherToolsFeaturedPostsConfig = async (): Promise<ApiResponse<{ list: CollectionItem[] }>> => {
  await delay()
  return success({ list: [] })
}

const mockSaveOtherToolsFeaturedPostsConfig = async (_list: CollectionItem[]): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

// 人员管理
const mockGetUsersList = async (_params?: { keyword?: string; role?: string }): Promise<ApiResponse<{ list: AdminUser[] }>> => {
  await delay()
  return success({ list: mockUsersList })
}

// 活动管理
const mockGetActivities = async (_params?: { page?: number; pageSize?: number }): Promise<ApiResponse<{ list: Activity[]; total: number }>> => {
  await delay()
  return success({ list: mockActivities, total: mockActivities.length })
}

const mockGetActivityDetail = async (id: number): Promise<ApiResponse<Activity>> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id)
  // 确保始终返回有效的 Activity 对象
  const result: Activity = activity || mockActivities[0] || {
    id: id,
    title: '未知活动',
    type: 'activity',
    date: new Date().toISOString().split('T')[0]
  }
  return success(result)
}

const mockCreateActivity = async (_data: Partial<Activity>): Promise<ApiResponse<{ id: number }>> => {
  await delay()
  return success({ id: Date.now() })
}

const mockUpdateActivity = async (_id: number, _data: Partial<Activity>): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

// ==================== API 函数（自动切换真实/Mock） ====================

// ========== 首页管理 ==========

/** 获取轮播图配置 GET /api/admin/carousel */
export async function getCarouselConfig(): Promise<ApiResponse<{ list: CarouselItem[] }>> {
  if (!useRealApi) return mockGetCarouselConfig()
  return get<{ list: CarouselItem[] }>('/admin/carousel')
}

/** 保存轮播图配置 PUT /api/admin/carousel */
export async function saveCarouselConfig(list: CarouselItem[]): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockSaveCarouselConfig(list)
  return put<null>('/admin/carousel', { list })
}

/** 获取荣誉殿堂Banner配置 GET /api/admin/honor/banner */
export async function getHonorBannerConfig(): Promise<ApiResponse<{ bannerImage: string }>> {
  if (!useRealApi) return mockGetHonorBannerConfig()
  return get<{ bannerImage: string }>('/admin/honor/banner')
}

/** 保存荣誉殿堂Banner配置 PUT /api/admin/honor/banner */
export async function saveHonorBannerConfig(bannerImage: string): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockSaveHonorBannerConfig(bannerImage)
  return put<null>('/admin/honor/banner', { bannerImage })
}

/** 获取AI工具配置 GET /api/admin/tools */
export async function getToolsConfig(): Promise<ApiResponse<{ list: ToolConfigItem[] }>> {
  if (!useRealApi) return mockGetToolsConfig()
  return get<{ list: ToolConfigItem[] }>('/admin/tools')
}

/** 保存AI工具配置 PUT /api/admin/tools */
export async function saveToolsConfig(list: ToolConfigItem[]): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockSaveToolsConfig(list)
  return put<null>('/admin/tools', { list })
}

/** 获取AI工具专区Banner配置 GET /api/admin/tool-banners */
export async function getToolBannersConfig(): Promise<ApiResponse<{ list: ToolBannerItem[] }>> {
  if (!useRealApi) return mockGetToolBannersConfig()
  return get<{ list: ToolBannerItem[] }>('/admin/tool-banners')
}

/** 保存AI工具专区Banner配置 PUT /api/admin/tool-banners */
export async function saveToolBannersConfig(list: ToolBannerItem[]): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockSaveToolBannersConfig(list)
  return put<null>('/admin/tool-banners', { list })
}

// ========== AI使用达人管理 ==========

/** 获取奖项设置列表 GET /api/admin/awards */
export async function getHonorAwardsConfig(): Promise<ApiResponse<{ list: AwardSetting[] }>> {
  if (!useRealApi) return mockGetHonorAwardsConfig()
  return get<{ list: AwardSetting[] }>('/admin/awards')
}

/** 保存奖项设置列表 PUT /api/admin/awards */
export async function savePersonalAwardsConfig(list: AwardSetting[]): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockSavePersonalAwardsConfig(list)
  return put<null>('/admin/awards', { list })
}

/** 获取奖项名称列表（下拉选择用） GET /api/admin/awards/names */
export async function getAwardsList(): Promise<ApiResponse<{ list: AwardName[] }>> {
  if (!useRealApi) return mockGetAwardsList()
  return get<{ list: AwardName[] }>('/admin/awards/names')
}

/** 保存单个奖项 (已废弃，请使用 savePersonalAwardsConfig 批量保存) */
export async function saveAward(award: AwardSetting): Promise<ApiResponse<{ id: number }>> {
  if (!useRealApi) return mockSaveAward(award)
  console.warn('saveAward deprecated: use savePersonalAwardsConfig')
  return Promise.reject(new Error('Use savePersonalAwardsConfig'))
}

/** 删除奖项 (已废弃，请使用 savePersonalAwardsConfig 批量保存) */
export async function deleteAward(id: number): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockDeleteAward(id)
  console.warn('deleteAward deprecated: use savePersonalAwardsConfig')
  return Promise.reject(new Error('Use savePersonalAwardsConfig'))
}

/** 获取获奖者列表 GET /api/admin/winners */
export async function getWinnersConfig(): Promise<ApiResponse<{ list: Winner[] }>> {
  if (!useRealApi) return mockGetWinnersConfig()
  return get<{ list: Winner[] }>('/admin/winners')
}

/** 保存获奖者列表 PUT /api/admin/winners */
export async function saveWinnersConfig(list: Winner[]): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockSaveWinnersConfig(list)
  return put<null>('/admin/winners', { list })
}

/** 获取推荐获奖者列表 GET /api/admin/winners/recommended */
export async function getRecommendedWinners(month?: string): Promise<ApiResponse<{ list: RecommendedWinner[] }>> {
  if (!useRealApi) return mockGetRecommendedWinners(month)
  return get<{ list: RecommendedWinner[] }>('/admin/winners/recommended', month ? { month } : undefined)
}

/** 设置用户获奖 POST /api/admin/winners/set-award */
export async function setUserAward(params: { userId: number; awardId: number; awardDate: string }): Promise<ApiResponse<{ honorId: number }>> {
  if (!useRealApi) return mockSetUserAward(params)
  return post<{ honorId: number }>('/admin/winners/set-award', params)
}

/** 取消用户获奖 DELETE /api/admin/winners/cancel-award */
export async function cancelUserAward(params: { userId: number; honorId: number }): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockCancelUserAward(params)
  return del<null>('/admin/winners/cancel-award', params)
}

/** 获取团队奖项列表 GET /api/admin/team-awards */
export async function getTeamAwardsConfig(): Promise<ApiResponse<{ list: TeamAward[] }>> {
  if (!useRealApi) return mockGetTeamAwardsConfig()
  return get<{ list: TeamAward[] }>('/admin/team-awards')
}

/** 保存团队奖项列表 PUT /api/admin/team-awards */
export async function saveTeamAwardsConfig(list: TeamAward[]): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockSaveTeamAwardsConfig(list)
  return put<null>('/admin/team-awards', { list })
}

// ========== 精华帖子管理 ==========

/** 获取所有精华/置顶帖子列表 GET /api/admin/featured-posts/all */
export async function getAllFeaturedPosts(): Promise<ApiResponse<{
  practices: FeaturedPost[]
  empowerment: FeaturedPost[]
  agent: FeaturedPost[]
  otherTools: FeaturedPost[]
}>> {
  if (!useRealApi) return mockGetAllFeaturedPosts()
  return get<{
    practices: FeaturedPost[]
    empowerment: FeaturedPost[]
    agent: FeaturedPost[]
    otherTools: FeaturedPost[]
  }>('/admin/featured-posts/all')
}

/** 移除帖子精华/置顶状态 DELETE /api/admin/featured-posts/{postId} */
export async function removeFeaturedPost(postId: number): Promise<ApiResponse<{ success: boolean }>> {
  if (!useRealApi) return mockRemoveFeaturedPost(postId)
  return del<{ success: boolean }>(`/admin/featured-posts/${postId}`)
}

/** 获取赋能交流精选合集配置 GET /api/admin/empowerment/collections */
export async function getEmpowermentFeaturedPostsConfig(): Promise<ApiResponse<{ list: CollectionItem[] }>> {
  if (!useRealApi) return mockGetEmpowermentFeaturedPostsConfig()
  return get<{ list: CollectionItem[] }>('/admin/empowerment/collections')
}

/** 保存赋能交流精选合集配置 PUT /api/admin/empowerment/collections */
export async function saveEmpowermentFeaturedPostsConfig(list: CollectionItem[]): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockSaveEmpowermentFeaturedPostsConfig(list)
  return put<null>('/admin/empowerment/collections', { list })
}

/** 获取其他工具精华帖子配置 (从所有精华帖子中筛选) */
export async function getOtherToolsFeaturedPostsConfig(): Promise<ApiResponse<{ list: CollectionItem[] }>> {
  if (!useRealApi) return mockGetOtherToolsFeaturedPostsConfig()
  
  // 真实API：调用 getAllFeaturedPosts 并筛选 otherTools
  try {
    const response = await getAllFeaturedPosts()
    const otherTools = response.data?.otherTools || []
    
    // 转换为 CollectionItem 格式
    const list: CollectionItem[] = otherTools.map(post => ({
      id: post.id, // 暂用帖子ID作为ID
      postId: post.id,
      note: post.title // 使用标题作为备注
    }))
    
    return success({ list })
  } catch (e) {
    console.error('获取其他工具精华帖子失败', e)
    return success({ list: [] })
  }
}

/** 保存其他工具精华帖子配置 (暂不支持批量保存，仅返回成功) */
export async function saveOtherToolsFeaturedPostsConfig(list: CollectionItem[]): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockSaveOtherToolsFeaturedPostsConfig(list)
  // 后端暂无批量保存接口，需通过 setFeaturedPost 单个设置
  // 这里暂时静默成功，避免报错
  console.warn('saveOtherToolsFeaturedPostsConfig: 后端暂不支持批量保存其他工具精华帖子')
  return success(null)
}

// ========== 人员管理 ==========

/** 获取用户列表 GET /api/admin/users */
export async function getUsersList(params?: { keyword?: string; role?: string }): Promise<ApiResponse<{ list: AdminUser[] }>> {
  if (!useRealApi) return mockGetUsersList(params)
  return get<{ list: AdminUser[] }>('/admin/users', params)
}

// ========== 活动管理 ==========

/** 获取活动列表 GET /api/activities */
export async function getActivities(params?: { page?: number; pageSize?: number; toolId?: number }): Promise<ApiResponse<{ list: Activity[]; total: number }>> {
  if (!useRealApi) return mockGetActivities(params)
  return get<{ list: Activity[]; total: number }>('/activities', params)
}

/** 获取活动详情 GET /api/activities/{id} */
export async function getActivityDetail(id: number): Promise<ApiResponse<Activity>> {
  if (!useRealApi) return mockGetActivityDetail(id)
  return get<Activity>(`/activities/${id}`)
}

/** 创建活动 POST /api/activities */
export async function createActivity(data: Partial<Activity>): Promise<ApiResponse<{ id: number }>> {
  if (!useRealApi) return mockCreateActivity(data)
  return post<{ id: number }>('/activities', data)
}

/** 更新活动 PUT /api/activities/{id} */
export async function updateActivity(id: number, data: Partial<Activity>): Promise<ApiResponse<null>> {
  if (!useRealApi) return mockUpdateActivity(id, data)
  return put<null>(`/activities/${id}`, data)
}

// ==================== 类型导出（兼容旧代码） ====================

export type { CarouselItem as AdminCarouselItem }
export type Post = FeaturedPost

// ==================== 导出所有 API ====================

export const adminApi = {
  // 首页管理
  getCarouselConfig,
  saveCarouselConfig,
  getHonorBannerConfig,
  saveHonorBannerConfig,
  getToolsConfig,
  saveToolsConfig,
  getToolBannersConfig,
  saveToolBannersConfig,
  // AI使用达人管理
  getHonorAwardsConfig,
  savePersonalAwardsConfig,
  getAwardsList,
  saveAward,
  deleteAward,
  getWinnersConfig,
  saveWinnersConfig,
  getRecommendedWinners,
  setUserAward,
  cancelUserAward,
  getTeamAwardsConfig,
  saveTeamAwardsConfig,
  // 精华帖子管理
  getAllFeaturedPosts,
  removeFeaturedPost,
  getEmpowermentFeaturedPostsConfig,
  saveEmpowermentFeaturedPostsConfig,
  getOtherToolsFeaturedPostsConfig,
  saveOtherToolsFeaturedPostsConfig,
  // 人员管理
  getUsersList,
  // 活动管理
  getActivities,
  getActivityDetail,
  createActivity,
  updateActivity,
}

export default adminApi
