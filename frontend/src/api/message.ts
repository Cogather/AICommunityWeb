// 消息相关接口
import request from './request'

// 消息类型
export enum MessageType {
  ACTIVITY_REGISTRATION = 'activity_registration', // 活动报名
  POST_COMMENT = 'post_comment', // 帖子评论
  COMMENT_REPLY = 'comment_reply', // 评论回复
  POST_LIKE = 'post_like', // 帖子点赞
  AWARD_NOTIFICATION = 'award_notification' // 奖项通知
}

// 消息接口
export interface Message {
  id: number
  userId: number
  type: string
  title: string
  content: string
  link?: string
  fromUserId?: number
  fromUserName?: string
  read: boolean
  createTime: string | Date
}

// 分页响应
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

// 未读消息数量响应
export interface MessageUnreadCountResponse {
  count: number
}

// 获取消息列表
export const getMessages = async (params?: {
  type?: string
  page?: number
  pageSize?: number
}): Promise<PageResult<Message>> => {
  return await request.get<PageResult<Message>>('/messages', { params })
}

// 标记消息为已读
export const markMessageAsRead = async (id: number): Promise<void> => {
  await request.put(`/messages/${id}/read`)
}

// 全部标记为已读
export const markAllMessagesAsRead = async (): Promise<void> => {
  await request.put('/messages/read-all')
}

// 删除消息
export const deleteMessage = async (id: number): Promise<void> => {
  await request.delete(`/messages/${id}`)
}

// 获取未读消息数量
export const getUnreadMessageCount = async (): Promise<MessageUnreadCountResponse> => {
  return await request.get<MessageUnreadCountResponse>('/messages/unread-count')
}
