import type { ApiResponse } from './types'

// ==================== 环境域名配置 ====================

/** 生产环境接口域名 */
export const PROD_API_BASE = 'https://aicommunity.coreai.rnd.huawei.com/aicommunitybe/api'

/** 测试环境接口域名 */
export const TEST_API_BASE = 'https://corecode-aicommunity-beta.rnd.huawei.com/aicommunitybe/api'

/** 开发调试接口域名 */
export const DEV_API_BASE = 'http://10.189.4.114:8888/aicommunitybe/api'

// ==================== 自动环境检测 ====================

/** 环境类型 */
export type Environment = 'production' | 'test' | 'development'

/**
 * 🔍 自动检测当前环境
 * 
 * 根据当前页面访问的域名自动判断环境：
 * - aicommunity.coreai.rnd.huawei.com → 生产环境
 * - corecode-aicommunity-beta.rnd.huawei.com → 测试环境
 * - localhost / 127.0.0.1 / 其他 → 开发环境
 */
function detectEnvironment(): Environment {
  const hostname = typeof window !== 'undefined' ? window.location.hostname : ''
  
  if (hostname.includes('aicommunity.coreai.rnd.huawei.com')) {
    return 'production'
  }
  
  if (hostname.includes('corecode-aicommunity-beta.rnd.huawei.com')) {
    return 'test'
  }
  
  return 'development'
}

/**
 * 🌐 根据环境自动获取 API 地址
 */
function getApiBaseByEnvironment(env: Environment): string {
  switch (env) {
    case 'production':
      return PROD_API_BASE
    case 'test':
      return TEST_API_BASE
    case 'development':
    default:
      return DEV_API_BASE
  }
}

/** 当前检测到的环境 */
export const currentEnvironment = detectEnvironment()

/** 根据环境自动选择的 API 地址 */
export const AUTO_API_BASE = getApiBaseByEnvironment(currentEnvironment)

// ==================== 配置开关 ====================

/**
 * 🔧 调试模式配置
 * 
 * AUTO_MODE_ENABLED = true  → 自动根据域名检测环境（推荐用于部署）
 * AUTO_MODE_ENABLED = false → 使用下方手动配置的 DEBUG_API_URL（开发调试）
 */

// ⬇️ 配置开关 ⬇️
const AUTO_MODE_ENABLED = true   // true: 自动检测环境, false: 使用手动配置
const DEBUG_USE_REAL_API = true  // 是否使用真实 API（false 则使用 Mock）
const DEBUG_API_URL = DEV_API_BASE  // 手动模式下使用的 API 地址
// ⬆️ 配置结束 ⬆️

// ==================== 最终配置计算 ====================

/** 是否使用真实 API */
export const USE_REAL_API = import.meta.env.VITE_USE_REAL_API === 'true' || DEBUG_USE_REAL_API

/** 最终使用的 API 地址 */
const FINAL_API_URL = AUTO_MODE_ENABLED ? AUTO_API_BASE : DEBUG_API_URL

// 控制台输出当前配置（方便调试）
console.log('[API Config]', {
  autoMode: AUTO_MODE_ENABLED,
  detectedEnv: currentEnvironment,
  hostname: typeof window !== 'undefined' ? window.location.hostname : 'N/A',
  apiBaseUrl: FINAL_API_URL,
  useRealApi: USE_REAL_API,
})

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
export { USE_REAL_API as useRealApi, FINAL_API_URL as apiBaseUrl }
