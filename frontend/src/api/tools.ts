/**
 * AI工具专区 API
 */

import { get, put, useRealApi, delay, success } from './request'
import type {
  ApiResponse,
  ToolItem,
  Post,
  Activity,
  TagStat,
  PaginatedData,
} from './types'

// ==================== 扩展类型 ====================

/** 部门统计 */
export interface DepartmentStat {
  name: string
  postCount: number
  contributorCount: number
}

/** Owner权限检查响应 */
export interface OwnerPermission {
  isOwner: boolean
  // 后端可能返回更多信息
  toolId?: number
  permissions?: string[]
  // 兼容旧字段（有些页面用到）
  isAdmin?: boolean
  canManage?: boolean
}

/** 精华帖子响应 */
export interface FeaturedPostResponse {
  post: Post | null
  hasFeatured: boolean
}

/** 设置精华响应 */
export interface SetFeaturedResponse {
  success: boolean
  postId: number
  featured: boolean
}

// ==================== Mock 数据 ====================

const mockTools: ToolItem[] = [
  { id: 1, name: 'TestMate', desc: '自动化测试助手', logo: '🧪', color: '#36cfc9', link: '/tools?toolId=1' },
  { id: 2, name: 'CodeMate', desc: '智能代码补全', logo: '💻', color: '#9254de', link: '/tools?toolId=2' },
  { id: 3, name: '云集', desc: '云端计算集群', logo: '☁️', color: '#597ef7', link: '/tools?toolId=3' },
  { id: 4, name: '云见', desc: '智能监控平台', logo: '👁️', color: '#ff9c6e', link: '/tools?toolId=4' },
  { id: 5, name: '扶摇', desc: 'Agent编排引擎', logo: '🚀', color: '#4096ff', link: '/tools?toolId=5' },
]

const mockToolPosts: Post[] = [
  {
    id: 201,
    title: 'TestMate快速入门指南',
    summary: '本文详细介绍如何快速上手使用TestMate进行自动化测试',
    author: '张测试',
    authorId: 10,
    createTime: '2026-01-12',
    views: 1580,
    comments: 45,
    likes: 120,
    tags: ['新手', '入门'],
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'guide',
  },
]

const mockActivities: Activity[] = [
  {
    id: 1,
    toolId: 1,
    toolName: 'TestMate',
    type: 'training',
    title: 'TestMate高级特性培训',
    date: '2026-01-20',
    status: 'upcoming',
    currentParticipants: 45,
    maxParticipants: 100,
  },
]

// ==================== Mock API 实现 ====================

const mockGetTools = async (): Promise<ApiResponse<{ list: ToolItem[] }>> => {
  await delay()
  return success({ list: mockTools })
}

const mockGetToolPosts = async (
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Post>>> => {
  await delay()
  return success({
    list: mockToolPosts,
    total: mockToolPosts.length,
    page,
    pageSize,
  })
}

const mockGetToolActivities = async (): Promise<ApiResponse<PaginatedData<Activity>>> => {
  await delay()
  return success({
    list: mockActivities,
    total: mockActivities.length,
    page: 1,
    pageSize: 10,
  })
}

const mockCheckOwner = async (): Promise<ApiResponse<OwnerPermission>> => {
  await delay()
  return success({
    isOwner: true,
    toolId: 1,
    permissions: ['publish_activity', 'manage_posts', 'set_featured'],
    isAdmin: true,
    canManage: true,
  })
}

const mockGetTags = async (): Promise<ApiResponse<{ list: TagStat[] }>> => {
  await delay()
  return success({
    list: [
      { name: '新手', count: 30 },
      { name: '进阶', count: 25 },
      { name: '接口测试', count: 20 },
    ],
  })
}

const mockGetDepartments = async (): Promise<ApiResponse<{ list: DepartmentStat[] }>> => {
  await delay()
  return success({
    list: [
      { name: '测试部', postCount: 50, contributorCount: 10 },
      { name: '研发部', postCount: 40, contributorCount: 8 },
    ],
  })
}

const mockGetFeaturedPost = async (): Promise<ApiResponse<FeaturedPostResponse>> => {
  await delay()
  return success({
    post: null,
    hasFeatured: false,
  })
}

const mockSetFeaturedPost = async (
  postId: number,
  featured: boolean
): Promise<ApiResponse<SetFeaturedResponse>> => {
  await delay()
  return success({
    success: true,
    postId,
    featured,
  })
}

// ==================== API 函数 ====================

/**
 * 获取工具列表
 * GET /api/tools
 */
export async function getTools(featured?: boolean): Promise<ApiResponse<{ list: ToolItem[] }>> {
  if (!useRealApi) {
    return mockGetTools()
  }
  return get<{ list: ToolItem[] }>('/tools', featured !== undefined ? { featured } : undefined)
}

/**
 * 获取工具帖子列表
 * GET /api/tools/posts
 */
export async function getToolPosts(params: {
  toolId?: number
  category?: 'guide' | 'excellent'
  tag?: string
  department?: string
  keyword?: string
  sortBy?: 'newest' | 'hot' | 'comments'
  page?: number
  pageSize?: number
}): Promise<ApiResponse<PaginatedData<Post>>> {
  if (!useRealApi) {
    return mockGetToolPosts(params.page, params.pageSize)
  }
  return get<PaginatedData<Post>>('/tools/posts', params)
}

/**
 * 获取工具活动列表
 * GET /api/tools/activities
 */
export async function getToolActivities(params: {
  toolId?: number
  status?: 'upcoming' | 'ongoing' | 'ended'
  page?: number
  pageSize?: number
}): Promise<ApiResponse<PaginatedData<Activity>>> {
  if (!useRealApi) {
    return mockGetToolActivities()
  }
  return get<PaginatedData<Activity>>('/tools/activities', params)
}

/**
 * 检查工具Owner权限
 * GET /api/tools/:toolId/check-owner
 */
export async function checkOwnerPermission(toolId: number): Promise<ApiResponse<OwnerPermission>> {
  if (!useRealApi) {
    return mockCheckOwner()
  }
  return get<OwnerPermission>(`/tools/${toolId}/check-owner`)
}

/**
 * 获取工具标签统计
 * GET /api/tools/:toolId/tags
 */
export async function getToolTags(
  toolId: number,
  department?: string
): Promise<ApiResponse<{ list: TagStat[] }>> {
  if (!useRealApi) {
    return mockGetTags()
  }
  return get<{ list: TagStat[] }>(`/tools/${toolId}/tags`, department ? { department } : undefined)
}

/**
 * 获取工具部门统计
 * GET /api/tools/:toolId/departments
 */
export async function getToolDepartments(
  toolId: number,
  tag?: string
): Promise<ApiResponse<{ list: DepartmentStat[] }>> {
  if (!useRealApi) {
    return mockGetDepartments()
  }
  return get<{ list: DepartmentStat[] }>(`/tools/${toolId}/departments`, tag ? { tag } : undefined)
}

/**
 * 获取精华帖子（其他工具专有）
 * GET /api/tools/featured-post
 */
export async function getFeaturedPost(toolId: number): Promise<ApiResponse<FeaturedPostResponse>> {
  if (!useRealApi) {
    return mockGetFeaturedPost()
  }
  return get<FeaturedPostResponse>('/tools/featured-post', { toolId })
}

/**
 * 设置精华帖子（其他工具专有）
 * PUT /api/tools/featured-post
 */
export async function setFeaturedPost(
  toolId: number,
  postId: number
): Promise<ApiResponse<SetFeaturedResponse>> {
  if (!useRealApi) {
    return mockSetFeaturedPost(postId, true)
  }
  return put<SetFeaturedResponse>('/tools/featured-post', { toolId, postId })
}

// ==================== 导出 ====================

export const toolsApi = {
  getTools,
  getToolPosts,
  getToolActivities,
  checkOwnerPermission,
  getToolTags,
  getToolDepartments,
  getFeaturedPost,
  setFeaturedPost,
}

export default toolsApi
