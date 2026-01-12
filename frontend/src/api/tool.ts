// 工具详情相关接口
import request from './request'

// 工具接口
export interface Tool {
  id: number
  name: string
  description?: string
  icon?: string
  link?: string
  featured?: boolean
  banner?: string
}

// 工具Owner响应
export interface ToolOwnerResponse {
  isOwner: boolean
  toolId: number
  toolName?: string
}

// 获取工具详情
export const getToolDetail = async (id: number): Promise<Tool> => {
  return await request.get<Tool>(`/tools/${id}`)
}

// 检查用户是否为工具Owner
export const checkToolOwner = async (id: number): Promise<ToolOwnerResponse> => {
  return await request.get<ToolOwnerResponse>(`/tools/${id}/owner`)
}
