/**
 * 帖子 API
 */

import { get, post, put, del, useRealApi, delay, success } from './request'
import type { ApiResponse, Post, PostDetail, TagStat } from './types'

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

/** 点赞操作响应 */
export interface ActionResponse {
  liked: boolean
  count: number
}

/** 收藏操作响应 */
export interface CollectResponse {
  collected: boolean
}

/** 设置精华响应 */
export interface FeaturedResponse {
  featured: boolean
  postId: number
  success: boolean
  message?: string
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
    liked: isLike,
    count: isLike ? 21 : 20,
  })
}

const mockCollectPost = async (isCollect: boolean): Promise<ApiResponse<CollectResponse>> => {
  await delay()
  return success({
    collected: isCollect,
  })
}

const mockSetFeatured = async (id: number, featured: boolean): Promise<ApiResponse<FeaturedResponse>> => {
  await delay()
  return success({
    postId: id,
    featured,
    success: true,
    message: featured ? '已设为精华' : '已取消精华'
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
export async function collectPost(id: number, action: 'collect' | 'uncollect'): Promise<ApiResponse<CollectResponse>> {
  if (!useRealApi) {
    return mockCollectPost(action === 'collect')
  }
  return post<CollectResponse>(`/posts/${id}/collect`, { action })
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

// ==================== Mock 推荐封面和草稿 ====================

const mockRecommendedCovers = [
  { id: 1, url: 'https://picsum.photos/800/400?random=c1', category: 'tech' },
  { id: 2, url: 'https://picsum.photos/800/400?random=c2', category: 'ai' },
  { id: 3, url: 'https://picsum.photos/800/400?random=c3', category: 'general' },
  { id: 4, url: 'https://picsum.photos/800/400?random=c4', category: 'tech' },
  { id: 5, url: 'https://picsum.photos/800/400?random=c5', category: 'ai' },
  { id: 6, url: 'https://picsum.photos/800/400?random=c6', category: 'general' },
]

let mockDraft: { data: PostCreateParams | null; updateTime: string } = {
  data: null,
  updateTime: ''
}

/**
 * 获取推荐封面
 * GET /api/posts/recommended-covers
 */
export async function getRecommendedCovers(params?: {
  zone?: string
  count?: number
}): Promise<ApiResponse<{ list: typeof mockRecommendedCovers }>> {
  if (!useRealApi) {
    await delay(200)
    const count = params?.count || 6
    return success({ list: mockRecommendedCovers.slice(0, count) })
  }
  return get<{ list: typeof mockRecommendedCovers }>('/posts/recommended-covers', params)
}

/** 草稿数据（比 PostCreateParams 更宽松） */
export interface DraftData {
  zone?: string
  toolId?: number | null
  title?: string
  summary?: string
  tags?: string[]
  cover?: string
  content?: string
  savedAt?: string
}

/**
 * 保存草稿
 * POST /api/posts/draft
 */
export async function saveDraft(data: DraftData): Promise<ApiResponse<{ id: number; updateTime: string }>> {
  if (!useRealApi) {
    await delay(200)
    mockDraft = { data: data as PostCreateParams, updateTime: new Date().toISOString() }
    return success({ id: 1, updateTime: mockDraft.updateTime })
  }
  return post<{ id: number; updateTime: string }>('/posts/draft', data)
}

/**
 * 获取草稿
 * GET /api/posts/draft
 */
export async function getDraft(): Promise<ApiResponse<{ data: PostCreateParams | null; updateTime: string }>> {
  if (!useRealApi) {
    await delay(200)
    return success(mockDraft)
  }
  return get<{ data: PostCreateParams | null; updateTime: string }>('/posts/draft')
}

/**
 * 删除草稿
 * DELETE /api/posts/draft
 */
export async function deleteDraft(): Promise<ApiResponse<null>> {
  if (!useRealApi) {
    await delay(200)
    mockDraft = { data: null, updateTime: '' }
    return success(null)
  }
  return del<null>('/posts/draft')
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
  getRecommendedCovers,
  saveDraft,
  getDraft,
  deleteDraft,
}

export default postsApi
