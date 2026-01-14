/**
 * 赋能交流 API
 */

import { get, put, useRealApi, delay, success } from './request'
import type { ApiResponse, Post, TagStat, PaginatedData } from './types'

// ==================== 扩展类型 ====================

/** 精选合集 */
export interface Collection {
  id: number
  title: string
  description?: string
  cover?: string
  postCount: number
}

/** 设置精华响应 */
export interface SetFeaturedResponse {
  success: boolean
  postId: number
  featured: boolean
}

// ==================== Mock 数据 ====================

const mockFeaturedPosts: Post[] = [
  {
    id: 1,
    title: '如何使用 Agent 提升代码开发效率？',
    summary: '本文详细介绍了如何利用Agent工具提升日常开发效率...',
    author: '张三',
    authorId: 1,
    createTime: '2026-01-10',
    views: 500,
    comments: 30,
    likes: 100,
    tags: ['讨论', 'Agent'],
    zone: 'empowerment',
    featured: true,
  },
]

const mockPosts: Post[] = [
  {
    id: 2,
    title: '分享一个提升工作效率的AI工具使用技巧',
    summary: '今天分享一个非常实用的AI工具使用技巧...',
    author: '李四',
    authorId: 2,
    createTime: '2026-01-11',
    views: 300,
    comments: 20,
    likes: 50,
    tags: ['分享'],
    zone: 'empowerment',
  },
  {
    id: 3,
    title: '关于AI辅助编程的一些疑问',
    summary: '最近在使用AI辅助编程时遇到了一些问题...',
    author: '王五',
    authorId: 3,
    createTime: '2026-01-12',
    views: 200,
    comments: 15,
    likes: 30,
    tags: ['提问'],
    zone: 'empowerment',
  },
]

const mockCollections: Collection[] = [
  { id: 1, title: 'AI入门必读', description: '适合新手的AI学习资源合集', postCount: 10 },
  { id: 2, title: 'Prompt工程精选', description: '最佳Prompt编写技巧', postCount: 8 },
  { id: 3, title: '效率提升指南', description: '使用AI工具提升工作效率', postCount: 15 },
]

// ==================== Mock API 实现 ====================

const mockGetFeaturedPosts = async (): Promise<ApiResponse<{ list: Post[] }>> => {
  await delay()
  return success({ list: mockFeaturedPosts })
}

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

const mockGetTags = async (): Promise<ApiResponse<{ list: TagStat[] }>> => {
  await delay()
  return success({
    list: [
      { name: '讨论', count: 50 },
      { name: '分享', count: 40 },
      { name: '提问', count: 30 },
      { name: '经验', count: 25 },
      { name: '工具', count: 20 },
    ],
  })
}

const mockGetCollections = async (): Promise<ApiResponse<{ list: Collection[] }>> => {
  await delay()
  return success({ list: mockCollections })
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
 * 获取精华帖子列表
 * GET /api/empowerment/featured-posts
 */
export async function getFeaturedPosts(): Promise<ApiResponse<{ list: Post[] }>> {
  if (!useRealApi) {
    return mockGetFeaturedPosts()
  }
  return get<{ list: Post[] }>('/empowerment/featured-posts')
}

/**
 * 获取帖子列表
 * GET /api/empowerment/posts
 */
export async function getPosts(params: {
  tag?: string
  keyword?: string
  sortBy?: 'newest' | 'hot' | 'comments' | 'likes'
  page?: number
  pageSize?: number
} = {}): Promise<ApiResponse<PaginatedData<Post>>> {
  if (!useRealApi) {
    return mockGetPosts(params.page, params.pageSize)
  }
  return get<PaginatedData<Post>>('/empowerment/posts', params)
}

/**
 * 获取标签统计
 * GET /api/empowerment/tags
 */
export async function getTags(): Promise<ApiResponse<{ list: TagStat[] }>> {
  if (!useRealApi) {
    return mockGetTags()
  }
  return get<{ list: TagStat[] }>('/empowerment/tags')
}

/**
 * 获取精选合集
 * GET /api/empowerment/collections
 */
export async function getCollections(limit = 5): Promise<ApiResponse<{ list: Collection[] }>> {
  if (!useRealApi) {
    return mockGetCollections()
  }
  return get<{ list: Collection[] }>('/empowerment/collections', { limit })
}

/**
 * 设置/取消精华帖子
 * PUT /api/empowerment/featured-posts
 */
export async function setFeaturedPost(
  postId: number,
  featured: boolean
): Promise<ApiResponse<SetFeaturedResponse>> {
  if (!useRealApi) {
    return mockSetFeaturedPost(postId, featured)
  }
  return put<SetFeaturedResponse>('/empowerment/featured-posts', { postId, featured })
}

// ==================== 导出 ====================

export const empowermentApi = {
  getFeaturedPosts,
  getPosts,
  getTags,
  getCollections,
  setFeaturedPost,
}

export default empowermentApi
