<template>
  <div class="message-list-view">
    <div class="message-container">
      <div class="message-header">
        <h2>消息中心</h2>
        <div class="header-actions">
          <el-button text @click="handleMarkAllRead" :disabled="unreadCount === 0">
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
              <span v-if="message.fromUserName" class="message-from">
                来自：{{ message.fromUserName }}
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
import {
  Bell,
  UserFilled,
  ChatDotRound,
  Star,
  Trophy,
  Delete
} from '@element-plus/icons-vue'
import {
  getUserMessages,
  markMessageAsRead,
  markAllMessagesAsRead,
  deleteMessage,
  MessageType,
  type Message
} from '../utils/message'

const router = useRouter()
const loading = ref(false)
const activeTab = ref('all')
const currentUserId = ref(1) // 当前用户ID（实际应该从登录状态获取）

const messages = ref<Message[]>([])

// 过滤后的消息列表
const filteredMessages = computed(() => {
  if (activeTab.value === 'all') {
    return messages.value
  }
  return messages.value.filter(msg => msg.type === activeTab.value)
})

// 未读消息数量
const unreadCount = computed(() => {
  return messages.value.filter(msg => !msg.read).length
})

// 获取消息图标
const getMessageIcon = (type: MessageType) => {
  switch (type) {
    case MessageType.ACTIVITY_REGISTRATION:
      return UserFilled
    case MessageType.POST_COMMENT:
    case MessageType.COMMENT_REPLY:
      return ChatDotRound
    case MessageType.POST_LIKE:
      return Star
    case MessageType.AWARD_NOTIFICATION:
      return Trophy
    default:
      return Bell
  }
}

// 获取消息图标颜色
const getMessageIconColor = (type: MessageType) => {
  switch (type) {
    case MessageType.ACTIVITY_REGISTRATION:
      return '#4096ff'
    case MessageType.POST_COMMENT:
    case MessageType.COMMENT_REPLY:
      return '#67c23a'
    case MessageType.POST_LIKE:
      return '#f56c6c'
    case MessageType.AWARD_NOTIFICATION:
      return '#e6a23c'
    default:
      return '#909399'
  }
}

// 获取消息标签类型
const getMessageTagType = (type: MessageType) => {
  switch (type) {
    case MessageType.ACTIVITY_REGISTRATION:
      return 'primary'
    case MessageType.POST_COMMENT:
    case MessageType.COMMENT_REPLY:
      return 'success'
    case MessageType.POST_LIKE:
      return 'danger'
    case MessageType.AWARD_NOTIFICATION:
      return 'warning'
    default:
      return 'info'
  }
}

// 获取消息类型标签
const getMessageTypeLabel = (type: MessageType) => {
  switch (type) {
    case MessageType.ACTIVITY_REGISTRATION:
      return '活动报名'
    case MessageType.POST_COMMENT:
      return '帖子评论'
    case MessageType.COMMENT_REPLY:
      return '评论回复'
    case MessageType.POST_LIKE:
      return '点赞通知'
    case MessageType.AWARD_NOTIFICATION:
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

// 加载消息
const loadMessages = () => {
  loading.value = true
  try {
    messages.value = getUserMessages(currentUserId.value)
  } catch (error) {
    console.error('加载消息失败:', error)
    ElMessage.error('加载消息失败')
  } finally {
    loading.value = false
  }
}

// 处理消息点击
const handleMessageClick = (message: Message) => {
  // 标记为已读
  if (!message.read) {
    markMessageAsRead(currentUserId.value, message.id)
    message.read = true
  }

  // 根据消息类型跳转
  if (message.relatedId && message.relatedType) {
    if (message.relatedType === 'activity') {
      router.push(`/activity/${message.relatedId}`)
    } else if (message.relatedType === 'post') {
      // 如果是评论回复，需要定位到评论
      if (message.type === MessageType.COMMENT_REPLY) {
        router.push({
          path: `/post/${message.relatedId}`,
          query: { commentId: message.relatedId.toString() }
        })
      } else {
        router.push(`/post/${message.relatedId}`)
      }
    }
  }
}

// 处理标签切换
const handleTabChange = () => {
  // 可以在这里添加其他逻辑
}

// 标记全部为已读
const handleMarkAllRead = () => {
  markAllMessagesAsRead(currentUserId.value)
  messages.value.forEach(msg => {
    msg.read = true
  })
  ElMessage.success('已标记全部为已读')
}

// 删除消息
const handleDeleteMessage = (messageId: number) => {
  deleteMessage(currentUserId.value, messageId)
  messages.value = messages.value.filter(msg => msg.id !== messageId)
  ElMessage.success('消息已删除')
}

// 监听消息更新事件
const handleMessageUpdate = (event: CustomEvent) => {
  if (event.detail.userId === currentUserId.value) {
    loadMessages()
  }
}

onMounted(() => {
  loadMessages()
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
    font-weight: 600;
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
  font-weight: 600;
  color: #303133;
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


