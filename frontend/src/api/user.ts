// 用户管理相关接口
import request from './request'
import type { Post } from './practices'

// 用户资料响应
export interface UserProfile {
  id: number
  employeeId?: string
  name: string
  avatar: string
  bio?: string
  department?: string
  postsCount: number
  favoritesCount: number
  commentsCount: number
  activitiesCount: number
  flowersCount: number
  points: number
  roles?: string[]
  ownedTools?: Array<{
    toolId: number
    toolName: string
  }>
}

// 用户积分详情
export interface UserPoints {
  total: number
  details: Array<{
    type: string
    description: string
    points: number
  }>
}

// 用户更新请求
export interface UserUpdateRequest {
  name?: string
  avatar?: string
  bio?: string
  department?: string
}

// 分页响应
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

// 评论接口（从comment.ts导入或定义）
export interface Comment {
  id: number
  postId: number
  userId: number
  userName: string
  userAvatar: string
  content: string
  likes: number
  isLiked?: boolean
  isAuthor?: boolean
  isMyComment?: boolean
  canEdit?: boolean
  canDelete?: boolean
  replies?: any[]
  createTime: string | Date
  updateTime?: string | Date
}

// 活动接口（从activity.ts导入或定义）
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

// 获取当前用户信息
export const getCurrentUser = async (): Promise<UserProfile> => {
  return await request.get<UserProfile>('/user/current')
}

// 获取用户个人积分详情
export const getUserPoints = async (): Promise<UserPoints> => {
  return await request.get<UserPoints>('/user/points/calculate')
}

// 获取用户个人资料（按用户ID）
export const getUserProfileById = async (userId: number): Promise<UserProfile> => {
  return await request.get<UserProfile>(`/user/${userId}/profile`)
}

// 获取用户个人资料（按用户名）
export const getUserProfileByName = async (name: string): Promise<UserProfile> => {
  return await request.get<UserProfile>('/user/profile', { params: { name } })
}

// 更新用户资料
export const updateUserProfile = async (data: UserUpdateRequest): Promise<void> => {
  await request.put('/user/profile', data)
}

// 获取用户发布的帖子
export const getUserPosts = async (userId: number, params?: {
  page?: number
  pageSize?: number
}): Promise<PageResult<Post>> => {
  return await request.get<PageResult<Post>>(`/user/${userId}/posts`, { params })
}

// 获取用户收藏的帖子
export const getUserFavorites = async (userId: number, params?: {
  page?: number
  pageSize?: number
}): Promise<PageResult<Post>> => {
  return await request.get<PageResult<Post>>(`/user/${userId}/favorites`, { params })
}

// 获取用户评论列表
export const getUserComments = async (userId: number, params?: {
  page?: number
  pageSize?: number
}): Promise<PageResult<Comment>> => {
  return await request.get<PageResult<Comment>>(`/user/${userId}/comments`, { params })
}

// 获取用户参与的活动
export const getUserActivities = async (userId: number, params?: {
  page?: number
  pageSize?: number
}): Promise<PageResult<Activity>> => {
  return await request.get<PageResult<Activity>>(`/user/${userId}/activities`, { params })
}

// 获取用户发布的活动
export const getUserCreatedActivities = async (userId: number, params?: {
  status?: string
  page?: number
  pageSize?: number
}): Promise<PageResult<Activity>> => {
  return await request.get<PageResult<Activity>>(`/user/${userId}/activities/created`, { params })
}
