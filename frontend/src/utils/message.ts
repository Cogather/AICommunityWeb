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
  commentId?: number // 评论ID（用于定位到具体评论，POST_COMMENT和COMMENT_REPLY类型使用）
  replyId?: number // 回复ID（用于定位到具体回复，COMMENT_REPLY类型使用）
  fromUserId?: number // 发送消息的用户ID
  fromUserName?: string // 发送消息的用户名
  read: boolean
  createdAt: string
  link?: string // 自定义跳转链接（可选）
}

// 内存中的消息存储（替代localStorage）
const messagesStore: Map<number, Message[]> = new Map()

// 初始化mock消息数据
const initMockMessages = (userId: number): Message[] => {
  return [
    {
      id: 1,
      type: MessageType.POST_COMMENT,
      title: '帖子评论通知',
      content: '李四 评论了您的帖子《AI大会2024》',
      userId: userId,
      relatedId: 1,           // 帖子ID（对应mockPosts中id=1的帖子）
      relatedType: 'post',
      commentId: 101,         // 评论ID，对应mockComments中id=101的评论
      fromUserId: 2,
      fromUserName: '李四',
      read: false,
      createdAt: new Date(Date.now() - 3600000).toISOString()
    },
    {
      id: 2,
      type: MessageType.ACTIVITY_REGISTRATION,
      title: '活动报名通知',
      content: '李四 报名参加了您发布的活动《扶摇Agent新手入门培训》',
      userId: userId,
      relatedId: 1,           // 活动ID
      relatedType: 'activity',
      fromUserId: 3,
      fromUserName: '李四',
      read: false,
      createdAt: new Date(Date.now() - 7200000).toISOString()
    },
    {
      id: 3,
      type: MessageType.POST_LIKE,
      title: '点赞通知',
      content: '王五 赞了您的帖子《AI大会2024》',
      userId: userId,
      relatedId: 1,           // 帖子ID（对应mockPosts中id=1的帖子）
      relatedType: 'post',
      fromUserId: 4,
      fromUserName: '王五',
      read: true,
      createdAt: new Date(Date.now() - 86400000).toISOString()
    },
    {
      id: 4,
      type: MessageType.COMMENT_REPLY,
      title: '回复通知',
      content: '赵六 回复了您的评论',
      userId: userId,
      relatedId: 1,           // 帖子ID（对应mockPosts中id=1的帖子）
      relatedType: 'post',
      commentId: 101,         // 评论ID，对应mockComments中id=101的评论
      replyId: 1002,          // 回复ID，对应mockComments中id=1002的回复（赵六发的）
      fromUserId: 5,
      fromUserName: '赵六',
      read: false,
      createdAt: new Date(Date.now() - 172800000).toISOString()
    },
    {
      id: 5,
      type: MessageType.COMMENT_REPLY,
      title: '回复通知',
      content: '张三 回复了您的评论',
      userId: userId,
      relatedId: 1,           // 帖子ID
      relatedType: 'post',
      commentId: 102,         // 评论ID，对应mockComments中id=102的评论
      replyId: 1003,          // 回复ID，对应mockComments中id=1003的回复
      fromUserId: 1,
      fromUserName: '张三',
      read: true,
      createdAt: new Date(Date.now() - 259200000).toISOString()
    }
  ]
}

// 获取用户消息列表
export const getUserMessages = (userId: number): Message[] => {
  if (!messagesStore.has(userId)) {
    // 初始化mock数据
    messagesStore.set(userId, initMockMessages(userId))
  }
  const messages = messagesStore.get(userId) || []
  return messages.sort((a: Message, b: Message) =>
    new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
  )
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
  messagesStore.set(message.userId, messages)

  // 触发消息更新事件
  window.dispatchEvent(new CustomEvent('messageUpdated', { detail: { userId: message.userId } }))
}

// 标记消息为已读
export const markMessageAsRead = (userId: number, messageId: number): void => {
  const messages = getUserMessages(userId)
  const message = messages.find(msg => msg.id === messageId)
  if (message) {
    message.read = true
    messagesStore.set(userId, messages)
    window.dispatchEvent(new CustomEvent('messageUpdated', { detail: { userId } }))
  }
}

// 标记所有消息为已读
export const markAllMessagesAsRead = (userId: number): void => {
  const messages = getUserMessages(userId)
  messages.forEach(msg => {
    msg.read = true
  })
  messagesStore.set(userId, messages)
  window.dispatchEvent(new CustomEvent('messageUpdated', { detail: { userId } }))
}

// 删除消息
export const deleteMessage = (userId: number, messageId: number): void => {
  const messages = getUserMessages(userId)
  const filtered = messages.filter(msg => msg.id !== messageId)
  messagesStore.set(userId, filtered)
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

// 发送奖项通知消息（管理平台录入个人奖项时触发）
export const sendAwardNotificationMessage = (
  winnerId: number,        // 获奖者用户ID
  winnerName: string,      // 获奖者姓名
  awardId: number,         // 奖项ID
  awardName: string,       // 奖项名称
  awardCategory: string,   // 奖项分类
  awardDate: string        // 获奖时间（YYYY-MM）
): void => {
  // 获取奖项分类的中文名称
  const categoryNames: Record<string, string> = {
    'innovation': '创新突破',
    'efficiency': '效率提升',
    'practice': '最佳实践',
    'community': '社区贡献'
  }
  const categoryName = categoryNames[awardCategory] || awardCategory

  addMessage({
    type: MessageType.AWARD_NOTIFICATION,
    title: '恭喜您获得奖项！',
    content: `恭喜！您在 ${awardDate} 荣获【${categoryName}】类别的「${awardName}」奖项`,
    userId: winnerId,
    relatedId: awardId,
    relatedType: 'award',
    fromUserName: '系统通知'
  })
}

// 发送帖子评论消息
export const sendPostCommentMessage = (
  postId: number,
  postTitle: string,
  authorId: number,
  commenterId: number,
  commenterName: string,
  commentId: number          // 新增：评论ID，用于定位到具体评论
): void => {
  addMessage({
    type: MessageType.POST_COMMENT,
    title: '帖子评论通知',
    content: `${commenterName} 评论了您的帖子《${postTitle}》`,
    userId: authorId,
    relatedId: postId,
    relatedType: 'post',
    commentId: commentId,    // 评论ID，点击消息跳转到 /post/{postId}#comment-{commentId}
    fromUserId: commenterId,
    fromUserName: commenterName
  })
}

// 发送评论回复消息
export const sendCommentReplyMessage = (
  postId: number,
  commentAuthorId: number,
  replierId: number,
  replierName: string,
  commentId: number,         // 新增：评论ID
  replyId: number            // 新增：回复ID，用于定位到具体回复
): void => {
  addMessage({
    type: MessageType.COMMENT_REPLY,
    title: '评论回复通知',
    content: `${replierName} 回复了您的评论`,
    userId: commentAuthorId,
    relatedId: postId,
    relatedType: 'post',
    commentId: commentId,    // 评论ID
    replyId: replyId,        // 回复ID，点击消息跳转到 /post/{postId}#reply-{replyId}
    fromUserId: replierId,
    fromUserName: replierName
  })
}

// 发送点赞通知消息
export const sendPostLikeMessage = (
  postId: number,
  postTitle: string,
  authorId: number,
  likerId: number,
  likerName: string
): void => {
  addMessage({
    type: MessageType.POST_LIKE,
    title: '点赞通知',
    content: `${likerName} 赞了您的帖子《${postTitle}》`,
    userId: authorId,
    relatedId: postId,
    relatedType: 'post',
    fromUserId: likerId,
    fromUserName: likerName
  })
}

