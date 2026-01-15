/**
 * 用户认证 API
 */

import { post, useRealApi, delay, success, error } from './request'
import { setCachedUserInfo, removeCachedUserInfo } from '@/utils/storage'
import type { ApiResponse, UserProfile, LoginParams, LoginResponse } from './types'

// ==================== Mock 数据 ====================

const mockCurrentUser: UserProfile = {
  userId: 'x12345667',
  employeeId: 'E001',
  userName: '当前用户',
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
  ownedTools: [
    { toolId: 1, toolName: 'TestMate' },
  ],
}

// ==================== Mock API 实现 ====================

const mockLogin = async (params: LoginParams): Promise<ApiResponse<LoginResponse>> => {
  await delay(500)

  // 模拟登录验证
  if (params.userId === 'x12345667' && params.password === '123456') {
    return success({
      token: 'mock_token_' + Date.now(),
      expiresIn: 86400,
      user: mockCurrentUser,
    })
  }

  if (params.password !== '123456') {
    return error('密码错误', 40002) as ApiResponse<LoginResponse>
  }

  return error('工号不存在', 40001) as ApiResponse<LoginResponse>
}

const mockLogout = async (): Promise<ApiResponse<null>> => {
  await delay(200)
  return success(null)
}

// ==================== API 函数 ====================

/**
 * 用户登录
 * POST /api/auth/login
 */
export async function login(params: LoginParams): Promise<ApiResponse<LoginResponse>> {
  let res: ApiResponse<LoginResponse>
  if (!useRealApi) {
    res = await mockLogin(params)
  } else {
    res = await post<LoginResponse>('/auth/login', params)
  }

  if (res.code === 200 && res.data && res.data.user) {
    setCachedUserInfo(res.data.user)
  }

  return res
}

/**
 * 用户退出登录
 * POST /api/auth/logout
 */
export async function logout(): Promise<ApiResponse<null>> {
  let res: ApiResponse<null>
  if (!useRealApi) {
    res = await mockLogout()
  } else {
    res = await post<null>('/auth/logout')
  }

  if (res.code === 200) {
    removeCachedUserInfo()
  }

  return res
}

// ==================== 导出 ====================

export const authApi = {
  login,
  logout,
}

export default authApi
