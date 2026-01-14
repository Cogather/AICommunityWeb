/**
 * AI优秀实践 API
 */

import { get, useRealApi, delay, success } from './request'
import type { ApiResponse, Post, TagStat, PaginatedData } from './types'

// ==================== 扩展类型 ====================

/** 部门排名 */
export interface DepartmentRank {
  name: string
  postCount: number
  contributorCount: number
}

/** 热门贡献者 */
export interface Contributor {
  id: number
  name: string
  avatar?: string
  department?: string
  postCount: number
}

/** 热门帖子 */
export interface HotPost {
  id: number
  title: string
  author: string
  views: number
}

// ==================== Mock 数据 ====================

const mockPosts: Post[] = [
  {
    id: 1,
    title: '大模型在工业设计中的落地应用案例分享',
    summary: '本文详细介绍了大模型在工业设计领域的实际应用案例...',
    author: '张工程师',
    authorId: 1,
    createTime: '2026-01-10',
    views: 1200,
    comments: 50,
    likes: 200,
    tags: ['大模型', '工业设计'],
    zone: 'practices',
  },
  {
    id: 2,
    title: 'AI辅助产品设计全流程培训',
    summary: '分享如何使用AI工具辅助产品设计的完整流程...',
    author: '李产品',
    authorId: 2,
    createTime: '2026-01-11',
    views: 800,
    comments: 30,
    likes: 150,
    tags: ['产品设计', 'AI工具'],
    zone: 'practices',
  },
]

const mockHotPosts: HotPost[] = [
  { id: 1, title: '大模型在工业设计中的落地应用案例分享', author: '张工程师', views: 1200 },
  { id: 2, title: 'AI辅助产品设计全流程培训', author: '李产品', views: 800 },
  { id: 3, title: '智能客服系统建设经验分享', author: '王技术', views: 600 },
]

const mockTags: TagStat[] = [
  { name: '大模型', count: 50 },
  { name: '机器学习', count: 40 },
  { name: 'AI工具', count: 35 },
  { name: '产品设计', count: 30 },
  { name: '效率提升', count: 25 },
]

const mockDepartments: DepartmentRank[] = [
  { name: '技术部', postCount: 100, contributorCount: 20 },
  { name: '产品部', postCount: 80, contributorCount: 15 },
  { name: '设计部', postCount: 60, contributorCount: 12 },
]

const mockContributors: Contributor[] = [
  { id: 1, name: '张工程师', avatar: 'https://picsum.photos/100/100?random=c1', department: '技术部', postCount: 15 },
  { id: 2, name: '李产品', avatar: 'https://picsum.photos/100/100?random=c2', department: '产品部', postCount: 12 },
  { id: 3, name: '王设计', avatar: 'https://picsum.photos/100/100?random=c3', department: '设计部', postCount: 10 },
]

// ==================== Mock API 实现 ====================

const mockGetPosts = async (
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Post>>> => {
  await delay()
  return success({
    list: mockPosts,
    total: mockPosts.length,
    page,
    pageSize,
  })
}

const mockGetHotPosts = async (): Promise<ApiResponse<{ list: HotPost[] }>> => {
  await delay()
  return success({ list: mockHotPosts })
}

const mockGetTags = async (): Promise<ApiResponse<{ list: TagStat[] }>> => {
  await delay()
  return success({ list: mockTags })
}

const mockGetDepartments = async (): Promise<ApiResponse<{ list: DepartmentRank[] }>> => {
  await delay()
  return success({ list: mockDepartments })
}

const mockGetContributors = async (): Promise<ApiResponse<{ list: Contributor[] }>> => {
  await delay()
  return success({ list: mockContributors })
}

// ==================== API 函数 ====================

/**
 * 获取AI优秀实践帖子列表
 * GET /api/practices/posts
 */
export async function getPosts(params: {
  page?: number
  pageSize?: number
  keyword?: string
  tag?: string
  department?: string
  contributor?: string
  sortBy?: 'newest' | 'hot' | 'comments' | 'likes'
} = {}): Promise<ApiResponse<PaginatedData<Post>>> {
  if (!useRealApi) {
    return mockGetPosts(params.page, params.pageSize)
  }
  return get<PaginatedData<Post>>('/practices/posts', params)
}

/**
 * 获取最热帖子Top N
 * GET /api/practices/hot-posts
 */
export async function getHotPosts(limit = 3): Promise<ApiResponse<{ list: HotPost[] }>> {
  if (!useRealApi) {
    return mockGetHotPosts()
  }
  return get<{ list: HotPost[] }>('/practices/hot-posts', { limit })
}

/**
 * 获取标签列表及统计
 * GET /api/practices/tags
 */
export async function getTags(department?: string): Promise<ApiResponse<{ list: TagStat[] }>> {
  if (!useRealApi) {
    return mockGetTags()
  }
  return get<{ list: TagStat[] }>('/practices/tags', department ? { department } : undefined)
}

/**
 * 获取部门排名列表
 * GET /api/practices/departments
 */
export async function getDepartments(tag?: string): Promise<ApiResponse<{ list: DepartmentRank[] }>> {
  if (!useRealApi) {
    return mockGetDepartments()
  }
  return get<{ list: DepartmentRank[] }>('/practices/departments', tag ? { tag } : undefined)
}

/**
 * 获取热门贡献者列表
 * GET /api/practices/contributors
 */
export async function getContributors(limit = 5): Promise<ApiResponse<{ list: Contributor[] }>> {
  if (!useRealApi) {
    return mockGetContributors()
  }
  return get<{ list: Contributor[] }>('/practices/contributors', { limit })
}

// ==================== 导出 ====================

export const practicesApi = {
  getPosts,
  getHotPosts,
  getTags,
  getDepartments,
  getContributors,
}

export default practicesApi
