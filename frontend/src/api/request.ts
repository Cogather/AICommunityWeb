// API请求封装
import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'

// 环境配置
const getBaseURL = (): string => {
  // 优先使用环境变量
  if (import.meta.env.VITE_API_BASE_URL) {
    return import.meta.env.VITE_API_BASE_URL
  }
  
  // 根据环境模式选择不同的baseURL
  const mode = import.meta.env.MODE // 'development' | 'production' | 'test'
  
  // 开发环境：使用本地调试地址
  if (mode === 'development') {
    // 可以通过环境变量覆盖，默认使用本地调试地址
    return import.meta.env.VITE_DEV_API_BASE_URL || 'http://10.189.4.112:8888/aicommunitybe'
  }
  
  // 测试环境
  if (mode === 'test') {
    return import.meta.env.VITE_TEST_API_BASE_URL || '/api'
  }
  
  // 生产环境
  if (mode === 'production') {
    return import.meta.env.VITE_PROD_API_BASE_URL || '/api'
  }
  
  // 默认使用相对路径
  return '/api'
}

// 创建axios实例
const axiosInstance: AxiosInstance = axios.create({
  baseURL: getBaseURL(),
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 后端统一返回格式
interface Result<T> {
  code: number
  message: string
  data: T
  timestamp?: number
}

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response: AxiosResponse<Result<any>>) => {
    const result = response.data
    // 如果后端返回的是Result格式，提取data字段
    if (result && typeof result === 'object' && 'data' in result && 'code' in result) {
      if (result.code === 200) {
        return result.data
      } else {
        // 业务错误，返回错误信息
        return Promise.reject(new Error(result.message || '请求失败'))
      }
    }
    // 如果不是Result格式，直接返回
    return response.data
  },
  (error) => {
    if (error.response) {
      const { status, data } = error.response
      if (status === 401) {
        // 未授权，清除token并跳转到登录页
        localStorage.removeItem('token')
        window.location.href = '/login'
      }
      // 如果后端返回的是Result格式，提取message
      if (data && typeof data === 'object' && 'message' in data) {
        return Promise.reject(new Error(data.message || error.message))
      }
      return Promise.reject(data || error.message)
    }
    return Promise.reject(error.message)
  }
)

// 封装request方法，确保返回类型正确
// 由于响应拦截器已经返回了response.data，所以这里直接返回Promise<T>
const request = {
  get: <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    return axiosInstance.get(url, config) as unknown as Promise<T>
  },
  post: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
    return axiosInstance.post(url, data, config) as unknown as Promise<T>
  },
  put: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
    return axiosInstance.put(url, data, config) as unknown as Promise<T>
  },
  delete: <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    return axiosInstance.delete(url, config) as unknown as Promise<T>
  }
}

export default request
