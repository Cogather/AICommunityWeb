<template>
  <div class="message-list-view">
    <div class="message-container">
      <div class="message-header">
        <h2>消息中心</h2>
        <div class="header-actions">
          <el-button text @click="handleMarkAllRead" :disabled="unreadCount === 0 && computedUnreadCount === 0">
            全部标记为已读
          </el-button>
        </div>
      </div>

      <div class="message-tabs">
        <el-tabs v-model="activeTab" @tab-change="handleTabChange">
          <el-tab-pane label="全部" name="all" />
          <el-tab-pane label="活动报名" name="activity_registration" />
          <el-tab-pane label="帖子评论" name="post_comment" />
          <el-tab-pane label="评论回复" name="comment_reply" />
          <el-tab-pane label="点赞通知" name="post_like" />
          <el-tab-pane label="奖项通知" name="award_notification" />
        </el-tabs>
      </div>

      <div class="message-list" v-loading="loading">
        <div v-if="filteredMessages.length === 0" class="empty-state">
          <el-empty description="暂无消息" />
        </div>

        <div
          v-for="message in filteredMessages"
          :key="message.id"
          class="message-item"
          :class="{ 'message-unread': !message.read }"
          @click="handleMessageClick(message)"
        >
          <div class="message-icon">
            <el-icon :size="24" :color="getMessageIconColor(message.type)">
              <component :is="getMessageIcon(message.type)" />
            </el-icon>
          </div>
          <div class="message-content">
            <div class="message-title-row">
              <h3 class="message-title">{{ message.title }}</h3>
              <el-tag :type="getMessageTagType(message.type)" size="small">
                {{ getMessageTypeLabel(message.type) }}
              </el-tag>
            </div>
            <p class="message-text">{{ message.content }}</p>
            <div class="message-meta">
              <span class="message-time">{{ formatTime(message.createdAt) }}</span>
              <span v-if="message.userName" class="message-from">
                来自：{{ message.userName }}
              </span>
            </div>
          </div>
          <div class="message-actions">
            <el-button
              text
              :icon="Delete"
              @click.stop="handleDeleteMessage(message.id)"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ROUTES } from '../router/paths'
import {
  Bell,
  UserFilled,
  ChatDotRound,
  Star,
  Trophy,
  Delete
} from '@element-plus/icons-vue'
// API 层 - 支持 Mock/Real API 自动切换
import { getCurrentUser } from '../api/user'
import {
  getMessages as fetchMessages,
  markAsRead as markMessageRead,
  markAllAsRead as markAllMessagesRead,
  deleteMessage as removeMessageApi,
  getUnreadCount as fetchUnreadCount,
} from '../api/messages'
import type { Message, MessageType } from '../api/types'

const router = useRouter()
const loading = ref(false)
const activeTab = ref('all')
const currentUserId = ref<number | string>(0)

const messages = ref<Message[]>([])

// 过滤后的消息列表
// 后端接口已支持类型筛选，这里主要用于前端切换tab时的逻辑
// 如果是all，直接展示列表（后端默认all）
// 如果是特定类型，前端需重新请求（或本地过滤，取决于策略）
// 鉴于已对接真实API，建议切换tab时重新请求后端
const filteredMessages = computed(() => {
  return messages.value
})

// 未读消息数量
const unreadCount = ref(0)

// 计算未读数量（作为备用）
const computedUnreadCount = computed(() => {
  return messages.value.filter(msg => !msg.read).length
})

// 加载未读消息数量
const loadUnreadCount = async () => {
  try {
    const res = await fetchUnreadCount()
    unreadCount.value = res.data.count
  } catch (error) {
    console.error('加载未读消息数量失败:', error)
    // 如果API失败，使用本地计算
    unreadCount.value = messages.value.filter(msg => !msg.read).length
  }
}

// 加载消息
const loadMessages = async () => {
  loading.value = true
  try {
    const type = activeTab.value === 'all' ? undefined : (activeTab.value as MessageType)
    const res = await fetchMessages(type)
    messages.value = res.data.list
  } catch (error) {
    console.error('加载消息失败:', error)
    ElMessage.error('加载消息失败')
  } finally {
    loading.value = false
  }
}

// 获取消息图标
const getMessageIcon = (type: MessageType) => {
  switch (type) {
    case 'activity_registration':
      return UserFilled
    case 'post_comment':
    case 'comment_reply':
      return ChatDotRound
    case 'post_like':
      return Star
    case 'award_notification':
      return Trophy
    default:
      return Bell
  }
}

// 获取消息图标颜色
const getMessageIconColor = (type: MessageType) => {
  switch (type) {
    case 'activity_registration':
      return '#4096ff'
    case 'post_comment':
    case 'comment_reply':
      return '#67c23a'
    case 'post_like':
      return '#f56c6c'
    case 'award_notification':
      return '#e6a23c'
    default:
      return '#909399'
  }
}

// 获取消息标签类型
const getMessageTagType = (type: MessageType) => {
  switch (type) {
    case 'activity_registration':
      return 'primary'
    case 'post_comment':
    case 'comment_reply':
      return 'success'
    case 'post_like':
      return 'danger'
    case 'award_notification':
      return 'warning'
    default:
      return 'info'
  }
}

// 获取消息类型标签
const getMessageTypeLabel = (type: MessageType) => {
  switch (type) {
    case 'activity_registration':
      return '活动报名'
    case 'post_comment':
      return '帖子评论'
    case 'comment_reply':
      return '评论回复'
    case 'post_like':
      return '点赞通知'
    case 'award_notification':
      return '奖项通知'
    default:
      return '其他'
  }
}

// 格式化时间
const formatTime = (timeStr: string) => {
  const time = new Date(timeStr)
  const now = new Date()
  const diff = now.getTime() - time.getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return time.toLocaleDateString()
}

// 处理消息点击
const handleMessageClick = async (message: Message) => {
  // 标记为已读
  if (!message.read) {
    await handleMarkAsRead(message)
  }

  // 根据消息类型跳转
  if (message.link) {
    router.push(message.link)
  } else if (message.type === 'activity_registration') {
    // 活动报名消息，跳转到活动详情
    router.push(`/activity/${message.relatedId || message.id}`)
  } else if (message.type === 'post_comment') {
    // 帖子评论通知，跳转到帖子详情并定位到具体评论
    const postId = message.relatedId || message.id
    if (message.commentId) {
      router.push(`/post/${postId}#comment-${message.commentId}`)
    } else {
      router.push(`/post/${postId}`)
    }
  } else if (message.type === 'comment_reply') {
    // 评论回复通知，跳转到帖子详情并定位到具体回复
    const postId = message.relatedId || message.id
    if (message.replyId) {
      router.push(`/post/${postId}#reply-${message.replyId}`)
    } else if (message.commentId) {
      router.push(`/post/${postId}#comment-${message.commentId}`)
    } else {
      router.push(`/post/${postId}`)
    }
  } else if (message.type === 'post_like') {
    // 点赞消息，跳转到帖子详情
    router.push(`/post/${message.relatedId || message.id}`)
  } else if (message.type === 'award_notification') {
    // 奖项通知，跳转到个人荣誉页面
    router.push(`${ROUTES.PROFILE}?tab=honors`)
  }
}

// 处理标签切换
const handleTabChange = () => {
  // 重新加载消息列表
  loadMessages()
}

// 标记单条消息为已读
const handleMarkAsRead = async (message: Message) => {
  try {
    await markMessageRead(message.id)
    message.read = true
    // 更新未读数
    loadUnreadCount()
  } catch (error: unknown) {
    console.error('标记为已读失败:', error)
  }
}

// 标记全部为已读
const handleMarkAllRead = async () => {
  try {
    await markAllMessagesRead()
    messages.value.forEach(msg => {
      msg.read = true
    })
    unreadCount.value = 0
    ElMessage.success('已标记全部为已读')
  } catch (error: unknown) {
    console.error('标记全部为已读失败:', error)
    ElMessage.error((error as Error).message || '操作失败')
  }
}

// 删除消息
const handleDeleteMessage = async (messageId: number) => {
  try {
    await removeMessageApi(messageId)
    messages.value = messages.value.filter(msg => msg.id !== messageId)
    // 更新未读数
    loadUnreadCount()
    ElMessage.success('消息已删除')
  } catch (error: unknown) {
    console.error('删除消息失败:', error)
    ElMessage.error((error as Error).message || '删除失败')
  }
}

// 监听消息更新事件
const handleMessageUpdate = () => {
  loadMessages()
  loadUnreadCount()
}

onMounted(async () => {
  try {
    // 获取当前用户ID
    const response = await getCurrentUser()
    currentUserId.value = response.data.id
  } catch (error) {
    console.warn('获取当前用户信息失败:', error)
  }
  
  await loadMessages()
  await loadUnreadCount()
  window.addEventListener('messageUpdated', handleMessageUpdate as EventListener)
})

import { onBeforeUnmount } from 'vue'
onBeforeUnmount(() => {
  window.removeEventListener('messageUpdated', handleMessageUpdate as EventListener)
})
</script>

<style scoped lang="scss">
.message-list-view {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24px;
}

.message-container {
  max-width: 1000px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  h2 {
    margin: 0;
    font-size: 20px;
    font-weight: 700;
    color: #1a1a1a;
  }
}

.message-tabs {
  margin-bottom: 24px;
}

.message-list {
  min-height: 400px;
}

.message-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  border-bottom: 1px solid #e4e7ed;
  cursor: pointer;
  transition: background 0.3s;

  &:hover {
    background: #f5f7fa;
  }

  &.message-unread {
    background: #f0f9ff;
    border-left: 3px solid #4096ff;
  }
}

.message-icon {
  flex-shrink: 0;
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.message-title {
  margin: 0;
  font-size: 16px;
  font-weight: 700;
  color: #000000;
}

.message-text {
  margin: 8px 0;
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
}

.message-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

.message-actions {
  flex-shrink: 0;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}
</style>
