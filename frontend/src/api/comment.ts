// 评论相关接口
import request from './request'

// 评论点赞响应
export interface CommentLikeResponse {
  liked: boolean
  likes: number
}

// 评论更新请求
export interface CommentUpdateRequest {
  content: string
}

// 点赞评论
export const likeComment = async (id: number, action: 'like' | 'unlike'): Promise<CommentLikeResponse> => {
  return await request.post<CommentLikeResponse>(`/comments/${id}/like`, null, {
    params: { action }
  })
}

// 更新评论
export const updateComment = async (id: number, data: CommentUpdateRequest): Promise<void> => {
  await request.put(`/comments/${id}`, data)
}

// 删除评论
export const deleteComment = async (id: number): Promise<void> => {
  await request.delete(`/comments/${id}`)
}
