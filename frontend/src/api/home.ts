// 首页相关接口
import request from './request'

// 轮播图
export interface CarouselItem {
  id: number
  image: string
  title: string
  desc: string
  link: string
  showContent: boolean
}

// 获取首页轮播图
// 后端返回: Result<CarouselResponse>，其中 CarouselResponse.list 是轮播图列表
export const getCarousel = async (): Promise<{ list: CarouselItem[] }> => {
  return request.get<{ list: CarouselItem[] }>('/home/carousel') as Promise<{ list: CarouselItem[] }>
}

// 荣誉殿堂
export interface HonorAward {
  id: number
  name: string
  desc: string
  image: string
}

export interface TopUser {
  id: number
  name: string
  avatar: string
  department: string
  level?: number // 等级（后端返回level，前端用honorCount显示）
  honorCount?: number // 前端计算或从其他地方获取
}

export interface HonorInfo {
  bannerImage: string
  awards: HonorAward[]
  topUsers: TopUser[]
}

// 获取荣誉殿堂信息
// 后端返回: Result<HonorHomeResponse>，其中包含honor和news字段
// 前端只需要honor字段，news字段已移除
export const getHonor = async (): Promise<{ honor: HonorInfo }> => {
  const result = await request.get<{ honor: HonorInfo; news?: any }>('/home/honor') as unknown as { honor: HonorInfo; news?: any }
  // 转换TopUser字段：level -> honorCount（如果honorCount不存在）
  if (result.honor?.topUsers) {
    result.honor.topUsers = result.honor.topUsers.map(user => ({
      ...user,
      honorCount: user.honorCount || user.level || 0
    }))
  }
  return { honor: result.honor }
}

// AI工具相关
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

// 获取工具列表
// 后端返回: Result<List<Tool>>，需要转换为 { list: ToolItem[] }
export const getTools = async (featured?: boolean): Promise<{ list: ToolItem[] }> => {
  const tools = await request.get<ToolItem[]>('/tools', {
    params: featured !== undefined ? { featured } : {}
  }) as unknown as ToolItem[]
  return { list: Array.isArray(tools) ? tools : [] }
}

// 获取工具Banner列表
// 注意：后端Tool实体包含banners字段，需要从工具详情或工具配置中获取
// 如果指定了toolId，从工具详情获取banners；否则从所有工具中收集banners
export const getToolBanners = async (toolId?: number): Promise<{ list: ToolBannerItem[] }> => {
  if (toolId) {
    // 从指定工具详情获取banners
    const tool = await request.get<{ banners?: Array<{ id: number; image: string; title: string; desc: string; order: number }> }>(`/tools/${toolId}`) as unknown as { banners?: ToolBannerItem[] }
    return { list: tool?.banners || [] }
  }
  // 否则从所有工具中收集banners（用于首页展示）
  try {
    const tools = await request.get<Array<{ banners?: ToolBannerItem[] }>>('/tools') as unknown as Array<{ banners?: ToolBannerItem[] }>
    const allBanners: ToolBannerItem[] = []
    if (Array.isArray(tools)) {
      tools.forEach(tool => {
        if (tool.banners && Array.isArray(tool.banners)) {
          allBanners.push(...tool.banners)
        }
      })
    }
    // 按order排序
    allBanners.sort((a, b) => (a.order || 0) - (b.order || 0))
    return { list: allBanners }
  } catch {
    return { list: [] }
  }
}

// AI优秀实践相关
export interface PracticeItem {
  id: number
  title: string
  author: string
  time: string
  category?: 'training' | 'training-battle' | 'user-exchange'
}

export interface PracticesInfo {
  training: PracticeItem[] // 培训赋能
  trainingBattle: PracticeItem[] // AI训战
  userExchange: PracticeItem[] // 用户交流
}

// 获取AI优秀实践内容（三块列表）
// 注意：后端可能没有单独的/home/practices接口
// 如果后端没有该接口，需要通过帖子接口获取，按category分类
export const getPractices = async (): Promise<PracticesInfo> => {
  try {
    // 尝试直接获取
    return await request.get<PracticesInfo>('/home/practices') as Promise<PracticesInfo>
  } catch {
    // 如果接口不存在，通过帖子接口获取
    // 这里返回空数据，实际应该调用帖子接口按category分类获取
    return {
      training: [],
      trainingBattle: [],
      userExchange: []
    }
  }
}
