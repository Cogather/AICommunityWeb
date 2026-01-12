// 荣誉相关接口
import request from './request'

// 荣誉接口
export interface Honor {
  id: number
  userId: number
  name: string
  department?: string
  avatar?: string
  awardId: number
  awardName: string
  awardDate: string
  year: string
  category?: string
  flowers: number
  hasGivenFlower?: boolean
  isMine?: boolean
  createTime?: string | Date
}

// Top用户
export interface TopUser {
  id: number
  name: string
  avatar: string
  level: number
  department?: string
}

// 分页响应
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

// 荣誉影响力响应
export interface HonorInfluenceResponse {
  totalHonors: number
  totalUsers: number
  totalFlowers: number
  categories: Array<{
    name: string
    count: number
  }>
}

// 送花响应
export interface HonorFlowerResponse {
  flowers: number
  hasGivenFlower: boolean
}

// 获取荣誉列表
export const getHonors = async (params?: {
  scope?: 'all' | 'mine'
  view?: 'grid' | 'timeline'
  user?: string
  filterType?: 'category' | 'award' | 'department'
  filterValue?: string
  search?: string
  page?: number
  pageSize?: number
}): Promise<PageResult<Honor>> => {
  return await request.get<PageResult<Honor>>('/honors', { params })
}

// 获取荣誉影响力
export const getHonorInfluence = async (): Promise<HonorInfluenceResponse> => {
  return await request.get<HonorInfluenceResponse>('/honors/influence')
}

// 获取AI使用达人Top用户
export const getTopUsers = async (params?: {
  limit?: number
}): Promise<PageResult<TopUser>> => {
  return await request.get<PageResult<TopUser>>('/honors/top-users', { params })
}

// 给荣誉送花
export const giveFlower = async (id: number): Promise<HonorFlowerResponse> => {
  return await request.post<HonorFlowerResponse>(`/honors/${id}/flower`)
}
