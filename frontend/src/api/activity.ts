// 活动相关接口
import request from './request'

// 活动接口
export interface Activity {
  id: number
  title: string
  content: string
  cover?: string
  toolId?: number
  toolName?: string
  type: string
  date: string | Date
  authorId: number
  authorName: string
  authorAvatar?: string
  registeredCount?: number
  status?: string
  isRegistered?: boolean
  isAuthor?: boolean
  canEdit?: boolean
  canDelete?: boolean
  createTime?: string | Date
  updateTime?: string | Date
}

// 分页响应
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

// 创建活动请求
export interface ActivityCreateRequest {
  title: string
  toolId?: number
  type: string
  date: string
  cover: string
  content: string
}

// 更新活动请求
export interface ActivityUpdateRequest {
  title: string
  toolId?: number
  type: string
  date: string
  cover: string
  content: string
}

// 活动报名响应
export interface ActivityRegistrationResponse {
  registered: boolean
  registeredCount: number
  registrationUser?: {
    id: number
    name: string
    avatar: string
    registeredAt: string
  }
}

// 获取活动列表
export const getActivities = async (params?: {
  toolId?: number
  type?: string
  status?: string
  page?: number
  pageSize?: number
}): Promise<PageResult<Activity>> => {
  return await request.get<PageResult<Activity>>('/activities', { params })
}

// 获取活动详情
export const getActivityDetail = async (id: number): Promise<Activity> => {
  return await request.get<Activity>(`/activities/${id}`)
}

// 创建活动
export const createActivity = async (data: ActivityCreateRequest): Promise<Activity> => {
  return await request.post<Activity>('/activities', data)
}

// 更新活动
export const updateActivity = async (id: number, data: ActivityUpdateRequest): Promise<Activity> => {
  return await request.put<Activity>(`/activities/${id}`, data)
}

// 删除活动
export const deleteActivity = async (id: number): Promise<void> => {
  await request.delete(`/activities/${id}`)
}

// 报名活动
export const registerActivity = async (id: number): Promise<ActivityRegistrationResponse> => {
  return await request.post<ActivityRegistrationResponse>(`/activities/${id}/register`)
}

// 取消报名
export const cancelRegistration = async (id: number): Promise<ActivityRegistrationResponse> => {
  return await request.delete<ActivityRegistrationResponse>(`/activities/${id}/register`)
}

// 获取报名用户列表
export const getRegistrations = async (id: number, params?: {
  page?: number
  pageSize?: number
}): Promise<PageResult<ActivityRegistrationResponse['registrationUser']>> => {
  return await request.get<PageResult<ActivityRegistrationResponse['registrationUser']>>(`/activities/${id}/registrations`, { params })
}
