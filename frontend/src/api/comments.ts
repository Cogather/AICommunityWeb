/**
 * 评论 API
 */

import { get, post, put, del, useRealApi, delay, success } from './request'
import type { ApiResponse, Comment, Reply, PaginatedData } from './types'

// ==================== 扩展类型 ====================

/** 创建评论参数 */
export interface CommentCreateParams {
  content: string
}

/** 更新评论参数 */
export interface CommentUpdateParams {
  content: string
}

/** 创建回复参数 */
export interface ReplyCreateParams {
  content: string
  replyToId?: number  // 被回复的回复ID
}

/** 点赞响应 */
export interface LikeResponse {
  isLiked: boolean
  likes: number
}

// ==================== Mock 数据 ====================

const mockComments: Comment[] = [
  {
    id: 1,
    postId: 1,
    userId: 2,
    userName: '李四',
    userAvatar: 'https://picsum.photos/100/100?random=2',
    content: '很棒的分享，学习了！',
    likes: 5,
    isLiked: false,
    createTime: '2026-01-11 10:30:00',
    replies: [
      {
        id: 101,
        commentId: 1,
        userId: 1,
        userName: '张三',
        userAvatar: 'https://picsum.photos/100/100?random=1',
        content: '谢谢支持！',
        replyTo: '李四',
        replyToUserId: 2,
        likes: 1,
        createTime: '2026-01-11 11:00:00',
      },
    ],
  },
  {
    id: 2,
    postId: 1,
    userId: 3,
    userName: '王五',
    userAvatar: 'https://picsum.photos/100/100?random=3',
    content: '请问有没有相关的实战案例？',
    likes: 3,
    isLiked: false,
    createTime: '2026-01-11 14:00:00',
    replies: [],
  },
]

// ==================== Mock API 实现 ====================

const mockGetComments = async (
  _postId: number,
  page: number,
  pageSize: number
): Promise<ApiResponse<PaginatedData<Comment>>> => {
  await delay()
  return success({
    list: mockComments,
    total: mockComments.length,
    page,
    pageSize,
  })
}

const mockCreateComment = async (
  postId: number,
  params: CommentCreateParams
): Promise<ApiResponse<Comment>> => {
  await delay()
  return success({
    id: Date.now(),
    postId,
    userId: 1,
    userName: '当前用户',
    userAvatar: 'https://picsum.photos/100/100?random=user',
    content: params.content,
    likes: 0,
    isLiked: false,
    createTime: new Date().toISOString(),
    replies: [],
  })
}

const mockCreateReply = async (
  commentId: number,
  params: ReplyCreateParams
): Promise<ApiResponse<Reply>> => {
  await delay()
  return success({
    id: Date.now(),
    commentId,
    userId: 1,
    userName: '当前用户',
    userAvatar: 'https://picsum.photos/100/100?random=user',
    content: params.content,
    likes: 0,
    createTime: new Date().toISOString(),
  })
}

const mockUpdateComment = async (
  id: number,
  params: CommentUpdateParams
): Promise<ApiResponse<Comment>> => {
  await delay()
  return success({
    ...mockComments[0],
    id,
    content: params.content,
  })
}

const mockDeleteComment = async (): Promise<ApiResponse<null>> => {
  await delay()
  return success(null, '删除成功')
}

const mockLikeComment = async (isLike: boolean): Promise<ApiResponse<LikeResponse>> => {
  await delay()
  return success({
    isLiked: isLike,
    likes: isLike ? 6 : 5,
  })
}

// ==================== API 函数 ====================

/**
 * 获取帖子评论列表
 * GET /api/posts/:postId/comments
 */
export async function getComments(
  postId: number,
  page = 1,
  pageSize = 15,
  sortBy: 'newest' | 'hot' = 'newest'
): Promise<ApiResponse<PaginatedData<Comment>>> {
  if (!useRealApi) {
    return mockGetComments(postId, page, pageSize)
  }
  return get<PaginatedData<Comment>>(`/posts/${postId}/comments`, { page, pageSize, sortBy })
}

/**
 * 创建评论
 * POST /api/posts/:postId/comments
 */
export async function createComment(
  postId: number,
  params: CommentCreateParams
): Promise<ApiResponse<Comment>> {
  if (!useRealApi) {
    return mockCreateComment(postId, params)
  }
  return post<Comment>(`/posts/${postId}/comments`, params)
}

/**
 * 回复评论
 * POST /api/posts/:postId/comments/:commentId/reply
 */
export async function replyComment(
  postId: number,
  commentId: number,
  params: ReplyCreateParams
): Promise<ApiResponse<Reply>> {
  if (!useRealApi) {
    return mockCreateReply(commentId, params)
  }
  return post<Reply>(`/posts/${postId}/comments/${commentId}/reply`, params)
}

/**
 * 更新评论
 * PUT /api/comments/:id
 */
export async function updateComment(
  id: number,
  params: CommentUpdateParams
): Promise<ApiResponse<Comment>> {
  if (!useRealApi) {
    return mockUpdateComment(id, params)
  }
  return put<Comment>(`/comments/${id}`, params)
}

/**
 * 删除评论
 * DELETE /api/comments/:id
 */
export async function deleteComment(id: number): Promise<ApiResponse<null>> {
  if (!useRealApi) {
    return mockDeleteComment()
  }
  return del<null>(`/comments/${id}`)
}

/**
 * 点赞/取消点赞评论
 * POST /api/comments/:id/like
 */
export async function likeComment(
  id: number,
  action: 'like' | 'unlike'
): Promise<ApiResponse<LikeResponse>> {
  if (!useRealApi) {
    return mockLikeComment(action === 'like')
  }
  return post<LikeResponse>(`/comments/${id}/like`, { action })
}

/**
 * 删除回复
 * DELETE /api/replies/:id
 */
export async function deleteReply(id: number): Promise<ApiResponse<null>> {
  if (!useRealApi) {
    return mockDeleteComment()
  }
  return del<null>(`/replies/${id}`)
}

// ==================== 导出 ====================

export const commentsApi = {
  getComments,
  createComment,
  replyComment,
  updateComment,
  deleteComment,
  likeComment,
  deleteReply,
}

export default commentsApi
