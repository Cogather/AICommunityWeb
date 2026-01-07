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
  type: MessageType
  title: string
  content: string
  userId: number // 接收消息的用户ID
  relatedId?: number // 相关ID（如帖子ID、活动ID等）
  relatedType?: string // 相关类型（如'post', 'activity', 'comment'等）
  fromUserId?: number // 发送消息的用户ID
  fromUserName?: string // 发送消息的用户名
  read: boolean
  createdAt: string
}

// 获取用户消息列表
export const getUserMessages = (userId: number): Message[] => {
  const messagesStr = localStorage.getItem(`user_messages_${userId}`)
  if (!messagesStr) return []
  try {
    const messages = JSON.parse(messagesStr)
    return messages.sort((a: Message, b: Message) => 
      new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
    )
  } catch {
    return []
  }
}

// 获取未读消息数量
export const getUnreadMessageCount = (userId: number): number => {
  const messages = getUserMessages(userId)
  return messages.filter(msg => !msg.read).length
}

// 添加消息
export const addMessage = (message: Omit<Message, 'id' | 'read' | 'createdAt'>): void => {
  const messages = getUserMessages(message.userId)
  const newMessage: Message = {
    ...message,
    id: Date.now(),
    read: false,
    createdAt: new Date().toISOString()
  }
  messages.unshift(newMessage)
  localStorage.setItem(`user_messages_${message.userId}`, JSON.stringify(messages))
  
  // 触发消息更新事件
  window.dispatchEvent(new CustomEvent('messageUpdated', { detail: { userId: message.userId } }))
}

// 标记消息为已读
export const markMessageAsRead = (userId: number, messageId: number): void => {
  const messages = getUserMessages(userId)
  const message = messages.find(msg => msg.id === messageId)
  if (message) {
    message.read = true
    localStorage.setItem(`user_messages_${userId}`, JSON.stringify(messages))
    window.dispatchEvent(new CustomEvent('messageUpdated', { detail: { userId } }))
  }
}

// 标记所有消息为已读
export const markAllMessagesAsRead = (userId: number): void => {
  const messages = getUserMessages(userId)
  messages.forEach(msg => {
    msg.read = true
  })
  localStorage.setItem(`user_messages_${userId}`, JSON.stringify(messages))
  window.dispatchEvent(new CustomEvent('messageUpdated', { detail: { userId } }))
}

// 删除消息
export const deleteMessage = (userId: number, messageId: number): void => {
  const messages = getUserMessages(userId)
  const filtered = messages.filter(msg => msg.id !== messageId)
  localStorage.setItem(`user_messages_${userId}`, JSON.stringify(filtered))
  window.dispatchEvent(new CustomEvent('messageUpdated', { detail: { userId } }))
}

// 发送活动报名消息
export const sendActivityRegistrationMessage = (
  activityId: number,
  activityTitle: string,
  authorId: number,
  registrantId: number,
  registrantName: string
): void => {
  addMessage({
    type: MessageType.ACTIVITY_REGISTRATION,
    title: '活动报名通知',
    content: `${registrantName} 报名参加了您发布的活动《${activityTitle}》`,
    userId: authorId,
    relatedId: activityId,
    relatedType: 'activity',
    fromUserId: registrantId,
    fromUserName: registrantName
  })
}

