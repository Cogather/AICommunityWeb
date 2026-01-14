/**
 * 帖子 API
 */

import { get, post, put, del, useRealApi, delay, success } from './request'
import type { ApiResponse, Post, PostDetail, PaginatedData, TagStat } from './types'

// ==================== 扩展类型 ====================

/** 创建帖子参数 */
export interface PostCreateParams {
  title: string
  content: string
  summary?: string
  cover?: string
  zone: 'practices' | 'tools' | 'agent' | 'empowerment'
  toolId?: number
  category?: 'guide' | 'excellent'
  tags?: string[]
}

/** 更新帖子参数 */
export interface PostUpdateParams {
  title?: string
  content?: string
  summary?: string
  cover?: string
  tags?: string[]
}

/** 点赞/收藏操作响应 */
export interface ActionResponse {
  isLiked?: boolean
  isCollected?: boolean
  likes?: number
}

/** 设置精华响应 */
export interface FeaturedResponse {
  featured: boolean
  postId: number
}

// ==================== Mock 数据 ====================

const mockPosts: Post[] = [
  {
    id: 1,
    title: 'AI技术实践分享',
    summary: '这是一篇关于AI技术实践的分享文章',
    content: '<p>这是文章内容</p>',
    author: '张三',
    authorId: 1,
    authorAvatar: 'https://picsum.photos/100/100?random=1',
    createTime: '2026-01-10',
    views: 100,
    comments: 10,
    likes: 20,
    tags: ['AI', '实践'],
    department: '技术部',
    cover: 'https://picsum.photos/800/400?random=1',
    zone: 'practices',
  },
]

// ==================== Mock API 实现 ====================

const mockGetPostDetail = async (id: number): Promise<ApiResponse<PostDetail>> => {
  await delay()
  const mockPost = mockPosts.find(p => p.id === id) || mockPosts[0]
  return success({
    ...mockPost,
    content: '<p>这是帖子的详细内容...</p>',
    isLiked: false,
    isCollected: false,
    isAuthor: true,
    canEdit: true,
    canDelete: true,
  } as PostDetail)
}

const mockCreatePost = async (params: PostCreateParams): Promise<ApiResponse<PostDetail>> => {
  await delay()
  return success({
    id: Date.now(),
    title: params.title,
    content: params.content,
    summary: params.summary,
    cover: params.cover,
    author: '当前用户',
    authorId: 1,
    createTime: new Date().toISOString(),
    views: 0,
    comments: 0,
    likes: 0,
    tags: params.tags,
    zone: params.zone,
    toolId: params.toolId,
    category: params.category,
    isAuthor: true,
    canEdit: true,
    canDelete: true,
  } as PostDetail)
}

const mockUpdatePost = async (id: number, _params: PostUpdateParams): Promise<ApiResponse<PostDetail>> => {
  await delay()
  const mockPost = mockPosts.find(p => p.id === id) || mockPosts[0]
  return success({
    ...mockPost,
    content: '<p>更新后的内容</p>',
  } as PostDetail)
}

const mockDeletePost = async (): Promise<ApiResponse<null>> => {
  await delay()
  return success(null, '删除成功')
}

const mockLikePost = async (isLike: boolean): Promise<ApiResponse<ActionResponse>> => {
  await delay()
  return success({
    isLiked: isLike,
    likes: isLike ? 21 : 20,
  })
}

const mockCollectPost = async (isCollect: boolean): Promise<ApiResponse<ActionResponse>> => {
  await delay()
  return success({
    isCollected: isCollect,
  })
}

const mockSetFeatured = async (id: number, featured: boolean): Promise<ApiResponse<FeaturedResponse>> => {
  await delay()
  return success({
    postId: id,
    featured,
  })
}

const mockGetZoneTags = async (): Promise<ApiResponse<{ list: TagStat[] }>> => {
  await delay()
  return success({
    list: [
      { name: 'AI', count: 50 },
      { name: '实践', count: 30 },
      { name: '工具', count: 25 },
      { name: '效率', count: 20 },
    ],
  })
}

// ==================== API 函数 ====================

/**
 * 获取帖子详情
 * GET /api/posts/:id
 */
export async function getPostDetail(id: number): Promise<ApiResponse<PostDetail>> {
  if (!useRealApi) {
    return mockGetPostDetail(id)
  }
  return get<PostDetail>(`/posts/${id}`)
}

/**
 * 创建帖子
 * POST /api/posts
 */
export async function createPost(params: PostCreateParams): Promise<ApiResponse<PostDetail>> {
  if (!useRealApi) {
    return mockCreatePost(params)
  }
  return post<PostDetail>('/posts', params)
}

/**
 * 更新帖子
 * PUT /api/posts/:id
 */
export async function updatePost(id: number, params: PostUpdateParams): Promise<ApiResponse<PostDetail>> {
  if (!useRealApi) {
    return mockUpdatePost(id, params)
  }
  return put<PostDetail>(`/posts/${id}`, params)
}

/**
 * 删除帖子
 * DELETE /api/posts/:id
 */
export async function deletePost(id: number): Promise<ApiResponse<null>> {
  if (!useRealApi) {
    return mockDeletePost()
  }
  return del<null>(`/posts/${id}`)
}

/**
 * 点赞/取消点赞帖子
 * POST /api/posts/:id/like
 * @param action 'like' | 'unlike'
 */
export async function likePost(id: number, action: 'like' | 'unlike'): Promise<ApiResponse<ActionResponse>> {
  if (!useRealApi) {
    return mockLikePost(action === 'like')
  }
  return post<ActionResponse>(`/posts/${id}/like`, { action })
}

/**
 * 收藏/取消收藏帖子
 * POST /api/posts/:id/collect
 * @param action 'collect' | 'uncollect'
 */
export async function collectPost(id: number, action: 'collect' | 'uncollect'): Promise<ApiResponse<ActionResponse>> {
  if (!useRealApi) {
    return mockCollectPost(action === 'collect')
  }
  return post<ActionResponse>(`/posts/${id}/collect`, { action })
}

/**
 * 设置帖子精华/置顶状态
 * PUT /api/posts/:id/featured
 */
export async function setFeaturedPost(
  id: number,
  featured: boolean,
  zone?: string,
  toolId?: number
): Promise<ApiResponse<FeaturedResponse>> {
  if (!useRealApi) {
    return mockSetFeatured(id, featured)
  }
  return put<FeaturedResponse>(`/posts/${id}/featured`, { featured, zone, toolId })
}

/**
 * 获取区域标签
 * GET /api/zones/:zone/tags
 */
export async function getZoneTags(
  zone: string,
  toolId?: number
): Promise<ApiResponse<{ list: TagStat[] }>> {
  if (!useRealApi) {
    return mockGetZoneTags()
  }
  return get<{ list: TagStat[] }>(`/zones/${zone}/tags`, toolId ? { toolId } : undefined)
}

// ==================== 导出 ====================

export const postsApi = {
  getPostDetail,
  createPost,
  updatePost,
  deletePost,
  likePost,
  collectPost,
  setFeaturedPost,
  getZoneTags,
}

export default postsApi
