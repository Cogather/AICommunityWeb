/**
 * 消息中心 API
 */

import { get, put, del, useRealApi, delay, success } from './request'
import type { ApiResponse, Message, PaginatedData, MessageType } from './types'

// ==================== Mock 数据 ====================

const mockMessages: Message[] = [
  {
    id: 1,
    type: 'post_comment',
    title: '帖子评论通知',
    content: '张三 评论了您的帖子《AI技术实践分享》',
    relatedId: 101,
    relatedType: 'post',
    commentId: 1001,
    fromUserId: 2,
    fromUserName: '张三',
    read: false,
    createdAt: new Date(Date.now() - 3600000).toISOString(),
  },
  {
    id: 2,
    type: 'activity_registration',
    title: '活动报名通知',
    content: '李四 报名参加了您发布的活动《扶摇Agent新手入门培训》',
    relatedId: 1,
    relatedType: 'activity',
    fromUserId: 3,
    fromUserName: '李四',
    read: false,
    createdAt: new Date(Date.now() - 7200000).toISOString(),
  },
  {
    id: 3,
    type: 'award_notification',
    title: '恭喜您获得奖项！',
    content: '恭喜！您在 2026-01 荣获【创新突破】类别的「年度创新贡献奖」奖项',
    relatedId: 10,
    relatedType: 'award',
    fromUserName: '系统通知',
    read: false,
    createdAt: new Date(Date.now() - 86400000).toISOString(),
  },
  {
    id: 4,
    type: 'post_like',
    title: '点赞通知',
    content: '王五 赞了您的帖子《使用扶摇Agent实现智能代码生成》',
    relatedId: 102,
    relatedType: 'post',
    fromUserId: 4,
    fromUserName: '王五',
    read: true,
    createdAt: new Date(Date.now() - 172800000).toISOString(),
  },
  {
    id: 5,
    type: 'comment_reply',
    title: '评论回复通知',
    content: '赵六 回复了您的评论',
    relatedId: 101,
    relatedType: 'post',
    commentId: 1001,
    replyId: 2001,
    fromUserId: 5,
    fromUserName: '赵六',
    read: true,
    createdAt: new Date(Date.now() - 259200000).toISOString(),
  },
]

// ==================== Mock API 实现 ====================

const mockGetMessages = async (
  type?: MessageType,
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Message>>> => {
  await delay()
  let list = mockMessages
  if (type) {
    list = mockMessages.filter(m => m.type === type)
  }
  return success({
    list,
    total: list.length,
    page,
    pageSize,
  })
}

const mockGetUnreadCount = async (): Promise<ApiResponse<{ count: number }>> => {
  await delay()
  const count = mockMessages.filter(m => !m.read).length
  return success({ count })
}

const mockMarkAsRead = async (): Promise<ApiResponse<null>> => {
  await delay()
  return success(null)
}

const mockMarkAllAsRead = async (): Promise<ApiResponse<{ count: number }>> => {
  await delay()
  const count = mockMessages.filter(m => !m.read).length
  return success({ count })
}

const mockDeleteMessage = async (): Promise<ApiResponse<null>> => {
  await delay()
  return success(null, '删除成功')
}

// ==================== API 函数 ====================

/**
 * 获取消息列表
 * GET /api/messages
 */
export async function getMessages(
  type?: MessageType,
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Message>>> {
  if (!useRealApi) {
    return mockGetMessages(type, page, pageSize)
  }
  // 后端接口：/api/messages?type=xx&page=xx&pageSize=xx
  const params: Record<string, any> = { page, pageSize }
  if (type) {
    params.type = type
  }
  
  // 后端返回 PageResult<MessageVO>，需映射为前端 PaginatedData<Message>
  // MessageVO 字段: id, type, title, content, relatedId, relatedType, commentId, replyId, fromUserId, fromUserName, read, createdAt, link
  // Message 字段: id, type, title, content, relatedId, relatedType, commentId, replyId, fromUserId, fromUserName, read, createdAt, link
  // 字段完全匹配，只需注意 PageResult 结构
  
  return get<PaginatedData<Message>>('/messages', params)
}

/**
 * 获取未读消息数量
 * GET /api/messages/unread-count
 */
export async function getUnreadCount(): Promise<ApiResponse<{ count: number }>> {
  if (!useRealApi) {
    return mockGetUnreadCount()
  }
  // 后端返回 { count: number }
  return get<{ count: number }>('/messages/unread-count')
}

/**
 * 标记消息已读
 * PUT /api/messages/:id/read
 */
export async function markAsRead(id: number): Promise<ApiResponse<null>> {
  if (!useRealApi) {
    return mockMarkAsRead()
  }
  return put<null>(`/messages/${id}/read`)
}

/**
 * 标记全部已读
 * PUT /api/messages/read-all
 */
export async function markAllAsRead(): Promise<ApiResponse<{ count: number }>> {
  if (!useRealApi) {
    return mockMarkAllAsRead()
  }
  // 后端返回 { count: number }
  return put<{ count: number }>('/messages/read-all')
}

/**
 * 删除消息
 * DELETE /api/messages/:id
 */
export async function deleteMessage(id: number): Promise<ApiResponse<null>> {
  if (!useRealApi) {
    return mockDeleteMessage()
  }
  return del<null>(`/messages/${id}`)
}

// ==================== 导出 ====================

export const messagesApi = {
  getMessages,
  getUnreadCount,
  markAsRead,
  markAllAsRead,
  deleteMessage,
}

export default messagesApi
