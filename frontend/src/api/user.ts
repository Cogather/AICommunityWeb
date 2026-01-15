/**
 * 用户信息 API
 */

import { get, put, useRealApi, delay, success } from './request'
import { getCachedUserInfo, removeCachedUserInfo } from '@/utils/storage'
import type {
  ApiResponse,
  UserProfile,
  PaginatedData,
  Post,
  Comment,
  Activity,
} from './types'

// ==================== 扩展类型 ====================

/** 社区成员信息 */
export interface CommunityMemberInfo {
  userId: string
  userName: string
  chnName: string
  // 其他可能返回的字段
  [key: string]: any
}

/** 用户积分信息 */
export interface UserPointsData {
  totalPoints: number
  monthlyPoints: number
  ranking?: number
  history: {
    list: PointsHistory[]
    total: number
    page: number
    pageSize: number
  }
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

const mockCommunityMemberInfo: CommunityMemberInfo = {
  userId: '1',
  userName: 'admin',
  chnName: '管理员',
}

const mockUserPosts: Post[] = [
  {
    id: 1,
    title: '我的第一篇帖子',
    summary: '这是一篇测试帖子',
    author: '当前用户',
    userId: '1',
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

const mockGetRegistrations = async (): Promise<ApiResponse<PaginatedData<any>>> => {
  await delay()
  return success({
    list: [],
    total: 0,
    page: 1,
    pageSize: 20,
  })
}

const mockCheckCommunityMembership = async (_userId: string): Promise<ApiResponse<CommunityMemberInfo>> => {
  await delay()
  return success(mockCommunityMemberInfo)
}

const mockAddCommunity = async (_userId: string): Promise<ApiResponse<any>> => {
  await delay()
  return success({ succeed: true, data: mockCommunityMemberInfo })
}

const mockGetManager = async (): Promise<ApiResponse<{ userName: string }[]>> => {
  await delay()
  return success([{ userName: '1' }]) // Mock admin user ID
}

// ==================== API 函数 ====================

/**
 * 获取当前用户信息
 * GET /api/user/current
 */
export async function getCurrentUser(): Promise<ApiResponse<UserProfile>> {
  // 尝试从缓存获取
  const cachedUser = getCachedUserInfo()
  if (cachedUser && cachedUser.userId) {
    return success(cachedUser)
  }

  if (!useRealApi) {
    return mockGetCurrentUser()
  }

  return get<UserProfile>('/user/current')
}

/**
 * 根据ID获取用户信息
 * GET /api/user/:id
 */
export async function getUserById(id: number | string): Promise<ApiResponse<UserProfile>> {
  if (!useRealApi) {
    return mockGetUserById(Number(id))
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
  const res = await put<null>('/user/current', params)
  if (res.code === 200) {
    removeCachedUserInfo()
  }
  return res
}

/**
 * 获取用户积分信息
 * GET /api/user/points
 */
export async function getUserPoints(page = 1, pageSize = 15): Promise<ApiResponse<UserPointsData>> {
  if (!useRealApi) {
    return mockGetUserPoints()
  }
  // 后端返回 UserPointsVO，需要适配前端 UserPointsData
  const response = await get<any>('/user/points', { page, pageSize })
  if (response.code === 200 && response.data) {
    // 适配逻辑：后端没有 total，暂时用 list.length 或 mock 一个 total
    // UserPointsVO: { totalPoints, monthlyPoints, ranking, pointsHistory: [], pointsRules: [] }
    const vo = response.data
    response.data = {
      totalPoints: vo.totalPoints,
      monthlyPoints: vo.monthlyPoints,
      history: {
        list: vo.pointsHistory || [],
        total: (vo.pointsHistory || []).length, // 后端未返回 total，暂时这样
        page,
        pageSize
      },
      rules: vo.pointsRules || []
    }
  }
  return response
}

/**
 * 获取用户发布的帖子列表
 * GET /api/user/:userId/posts
 */
export async function getUserPosts(
  userId: string | string,
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
  userId: string | string,
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
  userId: string | string,
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
  userId: string | string,
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
  userId: string | string,
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Activity>>> {
  if (!useRealApi) {
    return mockGetUserActivities()
  }
  return get<PaginatedData<Activity>>(`/user/${userId}/created-activities`, { page, pageSize })
}

/**
 * 根据用户名获取用户资料 (已废弃，请使用 getUserById)
 * @deprecated 后端不支持按用户名查询，请使用 ID
 */
export async function getUserByName(_name: string): Promise<ApiResponse<UserProfile>> {
  if (!useRealApi) {
    return mockGetCurrentUser()
  }
  // 临时方案：如果必须用 name，这里暂时无法实现真实调用
  console.warn('getUserByName is deprecated and not supported by backend. Please use getUserById.')
  return Promise.reject(new Error('Backend does not support fetching user by name'))
}

/**
 * 获取活动报名列表
 * GET /api/activities/:activityId/registrations
 */
export async function getRegistrations(
  activityId: number,
  page = 1,
  pageSize = 20
): Promise<ApiResponse<PaginatedData<{ id: number; userId: string; userName: string; userAvatar?: string; department?: string; registerTime: string; status: string }>>> {
  if (!useRealApi) {
    return mockGetRegistrations()
  }
  return get<PaginatedData<{ id: number; userId: string; userName: string; userAvatar?: string; department?: string; registerTime: string; status: string }>>(`/activities/${activityId}/registrations`, { page, pageSize })
}

/**
 * 检查用户是否加入社区 (对应 aiFrontPage.isAddCommunity)
 * GET /home/isAddCommunity
 */
export async function checkCommunityMembership(userId: string): Promise<ApiResponse<CommunityMemberInfo>> {
  if (!useRealApi) {
    return mockCheckCommunityMembership(userId)
  }
  return get<CommunityMemberInfo>(`/home/isAddCommunity`, { userId })
}

/**
 * 用户加入社区 (对应 aiFrontPage.addCommunity)
 * POST /home/addCommunity
 */
export async function addCommunity(userId: string): Promise<ApiResponse<any>> {
  if (!useRealApi) {
    return mockAddCommunity(userId)
  }
  return put<any>(`/home/addCommunity`, { userId })
}

/**
 * 获取管理员列表 (对应 aiFrontPage.getManager)
 * GET /home/getManager
 */
export async function getManager(): Promise<ApiResponse<{ userName: string }[]>> {
  if (!useRealApi) {
    return mockGetManager()
  }
  return get<{ userName: string }[]>('/home/getManager')
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
  getUserByName,
  getRegistrations,
  checkCommunityMembership,
  addCommunity,
  getManager,
}

export default userApi
