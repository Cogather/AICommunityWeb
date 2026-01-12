// 管理平台相关接口
import request from './request'

// ========== 首页管理 ==========

// 轮播图配置（管理后台）
export interface AdminCarouselItem {
  id: number
  image: string
  title: string
  desc: string
  link: string
  showContent: boolean
  order: number
}

// 获取首页轮播图配置
export const getCarouselConfig = async (): Promise<{ list: AdminCarouselItem[] }> => {
  return request.get<{ list: AdminCarouselItem[] }>('/admin/home/carousel') as Promise<{ list: AdminCarouselItem[] }>
}

// 保存首页轮播图配置
export const saveCarouselConfig = async (list: AdminCarouselItem[]): Promise<void> => {
  await request.put('/admin/home/carousel', { list })
}

// 荣誉殿堂Banner配置
export interface HonorBannerConfig {
  bannerImage: string
}

// 获取荣誉殿堂Banner配置
export const getHonorBannerConfig = async (): Promise<HonorBannerConfig> => {
  return request.get<HonorBannerConfig>('/admin/home/honor-banner') as Promise<HonorBannerConfig>
}

// 保存荣誉殿堂Banner配置
export const saveHonorBannerConfig = async (config: HonorBannerConfig): Promise<void> => {
  await request.put('/admin/home/honor-banner', config)
}

// 荣誉殿堂奖项配置
export interface HonorAwardItem {
  id: number
  name: string
  desc: string
  image: string
  order: number
}

// 获取荣誉殿堂奖项配置
export const getHonorAwardsConfig = async (): Promise<{ list: HonorAwardItem[] }> => {
  return request.get<{ list: HonorAwardItem[] }>('/admin/home/honor-awards') as Promise<{ list: HonorAwardItem[] }>
}

// 保存荣誉殿堂奖项配置
export const saveHonorAwardsConfig = async (list: HonorAwardItem[]): Promise<void> => {
  await request.put('/admin/home/honor-awards', { list })
}

// AI工具配置
export interface ToolItem {
  id: number
  name: string
  desc: string
  logo: string
  color: string
  link: string
}

export interface ToolBannerItem {
  id: number
  image: string
  title: string
  desc: string
  order: number
}

// 获取AI工具配置
export const getToolsConfig = async (): Promise<{ list: ToolItem[] }> => {
  return request.get<{ list: ToolItem[] }>('/admin/tools') as Promise<{ list: ToolItem[] }>
}

// 保存AI工具配置
export const saveToolsConfig = async (list: ToolItem[]): Promise<void> => {
  await request.put('/admin/tools', { list })
}

// 工具Banner配置
export const getToolBannersConfig = async (): Promise<{ list: ToolBannerItem[] }> => {
  return request.get<{ list: ToolBannerItem[] }>('/admin/tools/banners') as Promise<{ list: ToolBannerItem[] }>
}

// 保存工具Banner配置
export const saveToolBannersConfig = async (list: ToolBannerItem[]): Promise<void> => {
  await request.put('/admin/tools/banners', { list })
}

// ========== AI使用达人管理 ==========

// 个人奖项配置
export interface PersonalAwardItem {
  id: number
  name: string
  description: string
  order: number
}

// 获取个人奖项配置
export const getPersonalAwardsConfig = async (): Promise<{ list: PersonalAwardItem[] }> => {
  return request.get<{ list: PersonalAwardItem[] }>('/admin/users/awards') as Promise<{ list: PersonalAwardItem[] }>
}

// 保存个人奖项配置
export const savePersonalAwardsConfig = async (list: PersonalAwardItem[]): Promise<void> => {
  await request.put('/admin/users/awards', { list })
}

// 获奖者管理
export interface WinnerItem {
  id: number
  name: string
  category: 'innovation' | 'efficiency' | 'practice' | 'community'
  awardName: string
  awardTime: string // YYYY-MM
}

// 获取获奖者列表
export const getWinnersConfig = async (): Promise<{ list: WinnerItem[] }> => {
  return request.get<{ list: WinnerItem[] }>('/admin/users/winners') as Promise<{ list: WinnerItem[] }>
}

// 保存获奖者列表
export const saveWinnersConfig = async (list: WinnerItem[]): Promise<void> => {
  await request.put('/admin/users/winners', { list })
}

// 团队奖项配置
export interface TeamAwardImageItem {
  id: number
  image: string
  winnerName: string
}

export interface TeamAwardItem {
  id: number
  title: string
  year: number
  images: TeamAwardImageItem[]
  order: number
}

// 获取团队奖项配置
export const getTeamAwardsConfig = async (): Promise<{ list: TeamAwardItem[] }> => {
  return request.get<{ list: TeamAwardItem[] }>('/admin/users/team-awards') as Promise<{ list: TeamAwardItem[] }>
}

// 保存团队奖项配置
export const saveTeamAwardsConfig = async (list: TeamAwardItem[]): Promise<void> => {
  await request.put('/admin/users/team-awards', { list })
}

// 获奖者推荐
export interface RecommendedWinner {
  id: number
  employeeId: string
  name: string
  avatar: string
  department: string
  points: number
  postsCount: number
  commentsCount: number
  activitiesCount: number
  likesReceived: number
  favoritesReceived: number
  hasAwarded: boolean
  honorId?: number
}

// 获取本月积分排行榜（获奖者推荐）
export const getRecommendedWinners = async (month: string, limit: number = 3): Promise<{ list: RecommendedWinner[] }> => {
  return request.get<{ list: RecommendedWinner[] }>('/admin/honors/recommended-winners', {
    params: { month, limit }
  }) as Promise<{ list: RecommendedWinner[] }>
}

// 设置用户获奖
export interface SetAwardParams {
  userId: number
  awardId: number
  awardName: string
  awardDate: string // YYYY-MM
  category: 'innovation' | 'efficiency' | 'practice' | 'community'
  year: string // YYYY
}

export const setUserAward = async (params: SetAwardParams): Promise<{ id: number }> => {
  return request.post<{ id: number }>('/admin/honors', params) as Promise<{ id: number }>
}

// 取消用户获奖
export const cancelUserAward = async (id: number): Promise<void> => {
  await request.delete(`/admin/honors/${id}`)
}

// 获取奖项列表（用于获奖者推荐）
export interface AwardItem {
  id: number
  name: string
  desc: string
  image: string
  category: 'innovation' | 'efficiency' | 'practice' | 'community'
  rules: string
}

export const getAwardsList = async (category?: string): Promise<{ list: AwardItem[] }> => {
  return request.get<{ list: AwardItem[] }>('/awards', {
    params: category ? { category } : {}
  }) as Promise<{ list: AwardItem[] }>
}

// 人员管理
export interface UserItem {
  id: number
  employeeId: string
  name: string
  email: string
  avatar: string
  department: string
  currentRole: 'admin' | 'owner' | 'user'
  ownedTools?: Array<{ toolId: number; toolName: string }>
}

// 搜索用户
export const searchUsers = (params: {
  employeeId?: string
  name?: string
  email?: string
  role?: 'admin' | 'owner' | 'user'
}) => {
  return request.get<{ list: UserItem[] }>('/admin/users/search', { params })
}

// 获取用户列表
export const getUsersList = (params?: {
  role?: 'admin' | 'owner' | 'user'
  toolId?: number
  search?: string
}) => {
  return request.get<{ list: UserItem[]; total: number }>('/admin/users', { params })
}

// 添加用户角色
export const addUserRole = (userId: number, params: {
  role: 'admin' | 'owner'
  toolId?: number
  employeeId?: string
}) => {
  return request.post(`/admin/users/${userId}/role`, params)
}

// 移除用户角色
export const removeUserRole = (userId: number, params: {
  role: 'admin' | 'owner'
  toolId?: number
}) => {
  return request.delete(`/admin/users/${userId}/role`, { data: params })
}

// 上传图片
export const uploadImage = async (file: File): Promise<{ url: string }> => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post<{ url: string }>('/admin/upload/image', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }) as Promise<{ url: string }>
}
