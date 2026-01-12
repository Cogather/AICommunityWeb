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
// 后端返回: Result<ToolsConfigResponse>，其中ToolsConfigResponse.list是工具列表
export const getToolsConfig = async (): Promise<{ list: ToolItem[] }> => {
  const result = await request.get<{ list: ToolItem[] }>('/admin/tools') as unknown as { list: ToolItem[] }
  return result || { list: [] }
}

// 保存AI工具配置
// 后端请求: ToolsConfigResponse，其中包含list字段
export const saveToolsConfig = async (list: ToolItem[]): Promise<void> => {
  await request.put('/admin/tools', { list })
}

// 工具Banner配置
// 注意：后端ToolsConfigResponse中的ToolItem包含banners字段
// 工具Banner是工具配置的一部分，通过工具配置接口获取
export const getToolBannersConfig = async (): Promise<{ list: ToolBannerItem[] }> => {
  // 从工具配置中提取所有banners
  const toolsConfig = await request.get<{ list: Array<{ banners?: ToolBannerItem[] }> }>('/admin/tools') as unknown as { list: Array<{ banners?: ToolBannerItem[] }> }
  const allBanners: ToolBannerItem[] = []
  if (toolsConfig?.list && Array.isArray(toolsConfig.list)) {
    toolsConfig.list.forEach(tool => {
      if (tool.banners && Array.isArray(tool.banners)) {
        allBanners.push(...tool.banners)
      }
    })
  }
  return { list: allBanners }
}

// 保存工具Banner配置
// 注意：工具Banner是工具配置的一部分，需要通过保存工具配置来更新
// 这个接口可能需要重新设计，或者通过工具配置接口来保存
export const saveToolBannersConfig = async (list: ToolBannerItem[]): Promise<void> => {
  // 由于banners是工具的一部分，需要先获取工具配置，更新banners，然后保存
  // 这里暂时保留接口，但实际实现可能需要调整
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
// 注意：后端可能还没有实现此接口，需要后端添加
// 后端返回: Result<{ list: PersonalAwardItem[] }>
export const getPersonalAwardsConfig = async (): Promise<{ list: PersonalAwardItem[] }> => {
  try {
    return await request.get<{ list: PersonalAwardItem[] }>('/admin/users/awards') as Promise<{ list: PersonalAwardItem[] }>
  } catch {
    // 如果接口不存在，返回空列表
    return { list: [] }
  }
}

// 保存个人奖项配置
// 注意：后端可能还没有实现此接口，需要后端添加
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
// 注意：后端可能还没有实现此接口，需要后端添加
// 后端返回: Result<{ list: WinnerItem[] }>
export const getWinnersConfig = async (): Promise<{ list: WinnerItem[] }> => {
  try {
    return await request.get<{ list: WinnerItem[] }>('/admin/users/winners') as Promise<{ list: WinnerItem[] }>
  } catch {
    // 如果接口不存在，返回空列表
    return { list: [] }
  }
}

// 保存获奖者列表
// 注意：后端可能还没有实现此接口，需要后端添加
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
// 注意：后端可能还没有实现此接口，需要后端添加
// 后端返回: Result<{ list: TeamAwardItem[] }>
export const getTeamAwardsConfig = async (): Promise<{ list: TeamAwardItem[] }> => {
  try {
    return await request.get<{ list: TeamAwardItem[] }>('/admin/users/team-awards') as Promise<{ list: TeamAwardItem[] }>
  } catch {
    // 如果接口不存在，返回空列表
    return { list: [] }
  }
}

// 保存团队奖项配置
// 注意：后端可能还没有实现此接口，需要后端添加
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
// 后端返回: Result<RecommendedWinnersResponse>，其中包含list和month字段
export const getRecommendedWinners = async (month?: string, limit: number = 3): Promise<{ list: RecommendedWinner[]; month?: string }> => {
  const result = await request.get<{ list: RecommendedWinner[]; month?: string }>('/admin/honors/recommended-winners', {
    params: { month, limit }
  }) as unknown as { list: RecommendedWinner[]; month?: string }
  return result || { list: [] }
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

// 设置用户获奖
// 后端返回: Result<HonorCreateResponse>，其中包含id和message字段
export const setUserAward = async (params: SetAwardParams): Promise<{ id: number; message?: string }> => {
  return request.post<{ id: number; message?: string }>('/admin/honors', params) as Promise<{ id: number; message?: string }>
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
  order?: number // 排序字段
}

// 获取奖项列表
// 后端返回: Result<List<Award>>，需要转换为 { list: AwardItem[] }
export const getAwardsList = async (category?: string): Promise<{ list: AwardItem[] }> => {
  const awards = await request.get<AwardItem[]>('/awards', {
    params: category ? { category } : {}
  }) as unknown as AwardItem[]
  return { list: Array.isArray(awards) ? awards : [] }
}

// 人员管理
// 后端User实体字段：id, employeeId, name, email, avatar, department, bio, createTime, updateTime
// 注意：currentRole和ownedTools可能需要从其他接口或计算得出
export interface UserItem {
  id: number
  employeeId: string
  name: string
  email: string
  avatar: string
  department: string
  bio?: string // 个人简介
  currentRole?: 'admin' | 'owner' | 'user' // 当前角色（可能需要计算）
  roles?: string[] // 用户角色列表（后端可能返回）
  ownedTools?: Array<{ toolId: number; toolName: string }> // 拥有的工具列表
}

// 搜索用户
// 后端返回: Result<List<User>>，需要转换为 { list: UserItem[] }
export const searchUsers = async (params: {
  employeeId?: string
  name?: string
  email?: string
  role?: 'admin' | 'owner' | 'user'
}): Promise<{ list: UserItem[] }> => {
  const users = await request.get<UserItem[]>('/admin/users/search', { params }) as unknown as UserItem[]
  return { list: Array.isArray(users) ? users : [] }
}

// 获取用户列表
// 后端返回: Result<PageResult<User>>，需要转换为 { list: UserItem[]; total: number }
export const getUsersList = async (params?: {
  role?: 'admin' | 'owner' | 'user'
  toolId?: number
  search?: string
  page?: number
  pageSize?: number
}): Promise<{ list: UserItem[]; total: number }> => {
  const result = await request.get<{ list: UserItem[]; total: number }>('/admin/users', { params }) as unknown as { list: UserItem[]; total: number }
  return result || { list: [], total: 0 }
}

// 添加用户角色
// 后端请求: UserRoleRequest { role, toolId?, employeeId? }
// 后端返回: Result<?>
export const addUserRole = async (userId: number, params: {
  role: 'admin' | 'owner'
  toolId?: number
  employeeId?: string
}): Promise<void> => {
  await request.post(`/admin/users/${userId}/role`, params)
}

// 移除用户角色
// 后端请求: UserRoleRequest { role, toolId? } (通过RequestBody传递)
// 后端返回: Result<?>
export const removeUserRole = async (userId: number, params: {
  role: 'admin' | 'owner'
  toolId?: number
}): Promise<void> => {
  await request.delete(`/admin/users/${userId}/role`, { data: params })
}

// 上传图片
// 后端返回: Result<ImageUploadResponse>，其中包含url字段
export const uploadImage = async (file: File): Promise<{ url: string }> => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post<{ url: string }>('/admin/upload/image', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }) as Promise<{ url: string }>
}
