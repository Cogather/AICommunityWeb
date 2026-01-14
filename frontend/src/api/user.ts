/**
 * 用户信息 API
 */

import { get, put, useRealApi, delay, success } from './request'
import type {
  ApiResponse,
  UserProfile,
  PaginatedData,
  Post,
  Comment,
  Activity,
} from './types'

// ==================== 扩展类型 ====================

/** 用户积分信息 */
export interface UserPointsData {
  totalPoints: number
  monthlyPoints: number
  history: PaginatedData<PointsHistory>
  rules: PointsRule[]
}

export interface PointsHistory {
  id: number
  type: string
  points: number
  description: string
  createTime: string
}

export interface PointsRule {
  action: string
  points: number
  description: string
}

/** 用户更新参数 */
export interface UserUpdateParams {
  avatar?: string
  bio?: string
}

// ==================== Mock 数据 ====================

const mockCurrentUser: UserProfile = {
  id: 1,
  employeeId: 'E001',
  name: '当前用户',
  avatar: 'https://picsum.photos/100/100?random=user',
  bio: '这是一个测试用户',
  department: '技术部/AI研发中心/智能应用组',
  departments: {
    level1: { id: 100, name: '技术部', level: 1 },
    level2: { id: 110, name: 'AI研发中心', level: 2 },
    level3: { id: 111, name: '智能应用组', level: 3 },
  },
  postsCount: 10,
  favoritesCount: 5,
  commentsCount: 20,
  activitiesCount: 3,
  flowersCount: 15,
  points: 1000,
  roles: ['admin', 'user'],
  ownedTools: [{ toolId: 1, toolName: 'TestMate' }],
}

const mockUserPosts: Post[] = [
  {
    id: 1,
    title: '我的第一篇帖子',
    summary: '这是一篇测试帖子',
    author: '当前用户',
    authorId: 1,
    views: 100,
    comments: 10,
    likes: 20,
    createTime: '2026-01-10',
    zone: 'practices',
  },
]

// ==================== Mock API 实现 ====================

const mockGetCurrentUser = async (): Promise<ApiResponse<UserProfile>> => {
  await delay()
  return success(mockCurrentUser)
}

const mockGetUserById = async (id: number): Promise<ApiResponse<UserProfile>> => {
  await delay()
  return success({ ...mockCurrentUser, id, name: `用户${id}` })
}

const mockUpdateUser = async (_params: UserUpdateParams): Promise<ApiResponse<null>> => {
  await delay()
  return success(null, '更新成功')
}

const mockGetUserPoints = async (): Promise<ApiResponse<UserPointsData>> => {
  await delay()
  return success({
    totalPoints: 1000,
    monthlyPoints: 150,
    history: {
      list: [
        { id: 1, type: 'post', points: 10, description: '发布帖子', createTime: '2026-01-10' },
        { id: 2, type: 'comment', points: 2, description: '发表评论', createTime: '2026-01-09' },
      ],
      total: 2,
      page: 1,
      pageSize: 15,
    },
    rules: [
      { action: 'post', points: 10, description: '发布帖子' },
      { action: 'comment', points: 2, description: '发表评论' },
      { action: 'like', points: 1, description: '获得点赞' },
    ],
  })
}

const mockGetUserPosts = async (): Promise<ApiResponse<PaginatedData<Post>>> => {
  await delay()
  return success({
    list: mockUserPosts,
    total: 1,
    page: 1,
    pageSize: 15,
  })
}

const mockGetUserFavorites = async (): Promise<ApiResponse<PaginatedData<Post>>> => {
  await delay()
  return success({
    list: [],
    total: 0,
    page: 1,
    pageSize: 15,
  })
}

const mockGetUserComments = async (): Promise<ApiResponse<PaginatedData<Comment>>> => {
  await delay()
  return success({
    list: [],
    total: 0,
    page: 1,
    pageSize: 15,
  })
}

const mockGetUserActivities = async (): Promise<ApiResponse<PaginatedData<Activity>>> => {
  await delay()
  return success({
    list: [],
    total: 0,
    page: 1,
    pageSize: 15,
  })
}

// ==================== API 函数 ====================

/**
 * 获取当前用户信息
 * GET /api/user/current
 */
export async function getCurrentUser(): Promise<ApiResponse<UserProfile>> {
  if (!useRealApi) {
    return mockGetCurrentUser()
  }
  return get<UserProfile>('/user/current')
}

/**
 * 根据ID获取用户信息
 * GET /api/user/:id
 */
export async function getUserById(id: number): Promise<ApiResponse<UserProfile>> {
  if (!useRealApi) {
    return mockGetUserById(id)
  }
  return get<UserProfile>(`/user/${id}`)
}

/**
 * 根据工号获取用户信息
 * GET /api/user/by-employee-id/:employeeId
 */
export async function getUserByEmployeeId(employeeId: string): Promise<ApiResponse<UserProfile>> {
  if (!useRealApi) {
    return mockGetUserById(1)
  }
  return get<UserProfile>(`/user/by-employee-id/${employeeId}`)
}

/**
 * 更新当前用户信息
 * PUT /api/user/current
 */
export async function updateCurrentUser(params: UserUpdateParams): Promise<ApiResponse<null>> {
  if (!useRealApi) {
    return mockUpdateUser(params)
  }
  return put<null>('/user/current', params)
}

/**
 * 获取用户积分信息
 * GET /api/user/points
 */
export async function getUserPoints(page = 1, pageSize = 15): Promise<ApiResponse<UserPointsData>> {
  if (!useRealApi) {
    return mockGetUserPoints()
  }
  return get<UserPointsData>('/user/points', { page, pageSize })
}

/**
 * 获取用户发布的帖子列表
 * GET /api/user/:userId/posts
 */
export async function getUserPosts(
  userId: number,
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Post>>> {
  if (!useRealApi) {
    return mockGetUserPosts()
  }
  return get<PaginatedData<Post>>(`/user/${userId}/posts`, { page, pageSize })
}

/**
 * 获取用户收藏的帖子列表
 * GET /api/user/:userId/favorites
 */
export async function getUserFavorites(
  userId: number,
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Post>>> {
  if (!useRealApi) {
    return mockGetUserFavorites()
  }
  return get<PaginatedData<Post>>(`/user/${userId}/favorites`, { page, pageSize })
}

/**
 * 获取用户评论列表
 * GET /api/user/:userId/comments
 */
export async function getUserComments(
  userId: number,
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Comment>>> {
  if (!useRealApi) {
    return mockGetUserComments()
  }
  return get<PaginatedData<Comment>>(`/user/${userId}/comments`, { page, pageSize })
}

/**
 * 获取用户参与的活动列表
 * GET /api/user/:userId/activities
 */
export async function getUserActivities(
  userId: number,
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Activity>>> {
  if (!useRealApi) {
    return mockGetUserActivities()
  }
  return get<PaginatedData<Activity>>(`/user/${userId}/activities`, { page, pageSize })
}

/**
 * 获取用户发布的活动列表
 * GET /api/user/:userId/created-activities
 */
export async function getUserCreatedActivities(
  userId: number,
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Activity>>> {
  if (!useRealApi) {
    return mockGetUserActivities()
  }
  return get<PaginatedData<Activity>>(`/user/${userId}/created-activities`, { page, pageSize })
}

// ==================== 导出 ====================

export const userApi = {
  getCurrentUser,
  getUserById,
  getUserByEmployeeId,
  updateCurrentUser,
  getUserPoints,
  getUserPosts,
  getUserFavorites,
  getUserComments,
  getUserActivities,
  getUserCreatedActivities,
}

export default userApi
