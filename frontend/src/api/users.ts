// AI使用达人页面相关接口
import request from './request'

// 团队荣誉相关接口
export interface TeamAwardImage {
  id: number
  image: string
  winnerName: string // 团队名称
  teamField?: string // 团队领域
  flowers?: number // 花朵数
  hasGivenFlower?: boolean // 是否已送花
}

export interface TeamAward {
  id: number
  title: string
  year: number
  images: TeamAwardImage[]
}

// 获取团队荣誉列表
// 注意：后端可能还没有实现此接口，需要后端添加TeamAwardController
// 后端返回: Result<{ list: TeamAward[] }>
export const getTeamAwards = async (year?: string): Promise<{ list: TeamAward[] }> => {
  try {
    const result = await request.get<{ list: TeamAward[] }>('/team-awards', {
      params: year ? { year } : {}
    }) as unknown as { list: TeamAward[] }
    return result || { list: [] }
  } catch {
    // 如果接口不存在，返回空列表
    return { list: [] }
  }
}

// 获取团队荣誉详情
// 注意：后端可能还没有实现此接口，需要后端添加
// 后端返回: Result<TeamAward>
export const getTeamAwardDetail = async (id: number): Promise<TeamAward> => {
  try {
    return await request.get<TeamAward>(`/team-awards/${id}`) as Promise<TeamAward>
  } catch {
    // 如果接口不存在，返回空对象
    return { id: 0, title: '', year: new Date().getFullYear(), images: [] }
  }
}
