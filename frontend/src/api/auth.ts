// 用户认证相关接口
import request from './request'

// 登录请求
export interface LoginRequest {
  username: string
  password: string
}

// 登录响应
export interface LoginResponse {
  token: string
  user: {
    id: number
    name: string
    avatar: string
    department: string
    role: string
  }
}

// 用户登录
export const login = async (data: LoginRequest): Promise<LoginResponse> => {
  const response = await request.post<LoginResponse>('/auth/login', data)
  // 保存token到localStorage
  if (response.token) {
    localStorage.setItem('token', response.token)
  }
  return response
}

// 用户登出
export const logout = async (): Promise<void> => {
  await request.post('/auth/logout')
  // 清除token
  localStorage.removeItem('token')
}
