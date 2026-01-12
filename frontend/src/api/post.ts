// 帖子详情、创建、编辑、删除、点赞、收藏等相关接口
import request from './request'
import type { Post, PostsResponse } from './practices'

// 评论接口
export interface Comment {
  id: number
  postId: number
  userId: number
  userName: string
  userAvatar: string
  content: string
  likes: number
  isLiked?: boolean
  isAuthor?: boolean
  isMyComment?: boolean
  canEdit?: boolean
  canDelete?: boolean
  replies?: Reply[]
  createTime: string | Date
  updateTime?: string | Date
}

// 回复接口
export interface Reply {
  id: number
  commentId: number
  userId: number
  userName: string
  userAvatar: string
  replyToUserId?: number
  replyTo?: string
  content: string
  likes: number
  isLiked?: boolean
  replies?: Reply[]
  createTime: string | Date
}

// 分页响应
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

// 创建帖子请求
export interface PostCreateRequest {
  title: string
  summary: string
  content: string
  tags?: string[]
  cover?: string
  zone: 'practices' | 'tools' | 'agent' | 'empowerment'
  toolId?: number
}

// 更新帖子请求
export interface PostUpdateRequest {
  title: string
  summary: string
  content: string
  tags?: string[]
  cover?: string
  zone: 'practices' | 'tools' | 'agent' | 'empowerment'
  toolId?: number
}

// 点赞响应
export interface LikeResponse {
  liked: boolean
  likes: number
}

// 收藏响应
export interface CollectResponse {
  collected: boolean
}

// 推荐封面
export interface CoverInfo {
  id: number
  url: string
  thumbnail?: string
}

// 草稿请求
export interface DraftRequest {
  title?: string
  summary?: string
  content?: string
  tags?: string[]
  cover?: string
  zone?: 'practices' | 'tools' | 'agent' | 'empowerment'
  toolId?: number
}

// 草稿响应
export interface DraftResponse {
  draftId: string
  savedAt: string
  message: string
}

// 评论创建请求
export interface CommentCreateRequest {
  content: string
  replyTo?: number
}

// 获取帖子详情
export const getPostDetail = async (id: number): Promise<Post> => {
  const post = await request.get<Post>(`/posts/${id}`)
  // 字段映射
  return {
    ...post,
    author: post.author || (post as any).authorName || '',
    description: post.description || (post as any).summary || '',
    image: post.image || post.cover || '',
    featured: post.featured !== undefined ? post.featured : (post as any).isFeatured || false
  }
}

// 创建帖子
export const createPost = async (data: PostCreateRequest): Promise<Post> => {
  const post = await request.post<Post>('/posts', data)
  return {
    ...post,
    author: post.author || (post as any).authorName || '',
    description: post.description || (post as any).summary || '',
    image: post.image || post.cover || '',
    featured: post.featured !== undefined ? post.featured : (post as any).isFeatured || false
  }
}

// 更新帖子
export const updatePost = async (id: number, data: PostUpdateRequest): Promise<Post> => {
  const post = await request.put<Post>(`/posts/${id}`, data)
  return {
    ...post,
    author: post.author || (post as any).authorName || '',
    description: post.description || (post as any).summary || '',
    image: post.image || post.cover || '',
    featured: post.featured !== undefined ? post.featured : (post as any).isFeatured || false
  }
}

// 删除帖子
export const deletePost = async (id: number): Promise<void> => {
  await request.delete(`/posts/${id}`)
}

// 点赞/取消点赞帖子
export const likePost = async (id: number, action: 'like' | 'unlike'): Promise<LikeResponse> => {
  return await request.post<LikeResponse>(`/posts/${id}/like`, null, {
    params: { action }
  })
}

// 收藏/取消收藏帖子
export const collectPost = async (id: number, action: 'collect' | 'uncollect'): Promise<CollectResponse> => {
  return await request.post<CollectResponse>(`/posts/${id}/collect`, null, {
    params: { action }
  })
}

// 获取推荐封面列表
export const getRecommendedCovers = async (): Promise<CoverInfo[]> => {
  return await request.get<CoverInfo[]>('/posts/recommended-covers')
}

// 保存草稿
export const saveDraft = async (data: DraftRequest): Promise<DraftResponse> => {
  return await request.post<DraftResponse>('/posts/draft', data)
}

// 获取帖子评论列表
export const getPostComments = async (postId: number, params?: {
  page?: number
  pageSize?: number
}): Promise<PageResult<Comment>> => {
  return await request.get<PageResult<Comment>>(`/posts/${postId}/comments`, { params })
}

// 发表评论
export const createComment = async (postId: number, data: CommentCreateRequest): Promise<Comment> => {
  return await request.post<Comment>(`/posts/${postId}/comments`, data)
}
