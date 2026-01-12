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
export const getTeamAwards = async (year?: string): Promise<{ list: TeamAward[] }> => {
  return request.get<{ list: TeamAward[] }>('/team-awards', {
    params: year ? { year } : {}
  }) as Promise<{ list: TeamAward[] }>
}

// 获取团队荣誉详情
export const getTeamAwardDetail = async (id: number): Promise<TeamAward> => {
  return request.get<TeamAward>(`/team-awards/${id}`) as Promise<TeamAward>
}
