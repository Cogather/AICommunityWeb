import type { ApiResponse } from './types'

// ==================== 配置 ====================

/**
 * API 配置说明
 * 
 * 1. USE_REAL_API: 控制是否使用真实后端 API
 *    - false: 使用 Mock 数据（开发调试，无需后端）
 *    - true:  使用真实后端 API
 * 
 * 2. API_BASE_URL: 后端 API 地址
 *    - 可以直接修改下方的 API_BASE_URL
 *    - 或在 .env.development / .env.production 中配置 VITE_API_BASE_URL
 * 
 * 配置优先级：环境变量 > 默认值
 */

/** 是否使用真实 API（true: 真实后端, false: mock 数据） */
export const USE_REAL_API = import.meta.env.VITE_USE_REAL_API === 'true' || false

/**
 * 后端 API 基础地址
 * 
 * 常用配置：
 * - 本地开发（代理模式）: '/api'
 * - 本地后端调试: 'http://localhost:8888/api'
 * - 远程后端调试: 'http://10.189.4.114:8888/api'
 * - 生产环境: '/api' 或具体域名
 */
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

// ==================== 调试配置（快速切换）====================

/**
 * 🔧 调试模式快速切换
 * 
 * 如果你不想修改环境变量，可以直接修改下面的配置：
 * 
 * 1. 使用 Mock 数据（默认）:
 *    const DEBUG_USE_REAL_API = false
 *    const DEBUG_API_URL = '/api'
 * 
 * 2. 连接本地后端:
 *    const DEBUG_USE_REAL_API = true
 *    const DEBUG_API_URL = 'http://localhost:8888/api'
 * 
 * 3. 连接远程后端（10.189.4.114:8888）:
 *    const DEBUG_USE_REAL_API = true
 *    const DEBUG_API_URL = 'http://10.189.4.114:8888/api'
 */

// ⬇️ 在这里快速切换调试配置 ⬇️
const DEBUG_MODE_ENABLED = true  // 设为 true 启用调试模式配置
const DEBUG_USE_REAL_API = true
const DEBUG_API_URL = 'http://10.189.4.114:8888/aicommunitybe/api'
// ⬆️ 调试配置结束 ⬆️

// 最终使用的配置（调试模式优先）
const FINAL_USE_REAL_API = DEBUG_MODE_ENABLED ? DEBUG_USE_REAL_API : USE_REAL_API
const FINAL_API_URL = DEBUG_MODE_ENABLED ? DEBUG_API_URL : API_BASE_URL

// 控制台输出当前配置（方便调试）
if (import.meta.env.DEV) {
  console.log('[API Config]', {
    debugMode: DEBUG_MODE_ENABLED,
    useRealApi: FINAL_USE_REAL_API,
    apiBaseUrl: FINAL_API_URL,
  })
}

// ==================== 请求方法 ====================

/**
 * 通用请求方法
 */
async function request<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<ApiResponse<T>> {
  const url = `${FINAL_API_URL}${endpoint}`

  const defaultHeaders: HeadersInit = {
    'Content-Type': 'application/json',
  }

  // 如果有 token，添加认证头
  const token = localStorage.getItem('auth_token')
  if (token) {
    ;(defaultHeaders as Record<string, string>)['Authorization'] = `Bearer ${token}`
  }

  const config: RequestInit = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...options.headers,
    },
  }

  try {
    console.log(`[API] ${options.method || 'GET'} ${url}`)
    const response = await fetch(url, config)

    // 检查响应状态
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.message || `HTTP error! status: ${response.status}`)
    }

    const data = await response.json()
    console.log(`[API] Response:`, data)
    return data
  } catch (error) {
    console.error(`[API Error] ${url}:`, error)
    throw error
  }
}

/**
 * GET 请求
 */
export async function get<T>(endpoint: string, params?: Record<string, unknown>): Promise<ApiResponse<T>> {
  let url = endpoint
  if (params) {
    const searchParams = new URLSearchParams()
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        searchParams.append(key, String(value))
      }
    })
    const queryString = searchParams.toString()
    if (queryString) {
      url += `?${queryString}`
    }
  }
  return request<T>(url, { method: 'GET' })
}

/**
 * POST 请求
 */
export async function post<T>(endpoint: string, data?: unknown): Promise<ApiResponse<T>> {
  return request<T>(endpoint, {
    method: 'POST',
    body: data ? JSON.stringify(data) : undefined,
  })
}

/**
 * PUT 请求
 */
export async function put<T>(endpoint: string, data?: unknown): Promise<ApiResponse<T>> {
  return request<T>(endpoint, {
    method: 'PUT',
    body: data ? JSON.stringify(data) : undefined,
  })
}

/**
 * DELETE 请求
 */
export async function del<T>(endpoint: string, data?: unknown): Promise<ApiResponse<T>> {
  return request<T>(endpoint, {
    method: 'DELETE',
    body: data ? JSON.stringify(data) : undefined,
  })
}

// ==================== 辅助函数 ====================

/**
 * 模拟延迟（用于 mock 数据）
 */
export function delay(ms: number = 300): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms))
}

/**
 * 创建成功响应
 */
export function success<T>(data: T, message: string = 'success'): ApiResponse<T> {
  return {
    code: 200,
    message,
    data,
  }
}

/**
 * 创建错误响应
 */
export function error<T = null>(message: string, code: number = 400, data: T | null = null): ApiResponse<T | null> {
  return {
    code,
    message,
    data,
  }
}

// 导出最终配置供其他模块使用
export { FINAL_USE_REAL_API as useRealApi, FINAL_API_URL as apiBaseUrl }
