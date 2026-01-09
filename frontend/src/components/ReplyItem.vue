<template>
  <div class="reply-item" :class="{ 'nested': depth > 0 }">
    <div class="reply-wrapper">
      <div class="avatar-wrapper-small">
        <el-avatar :src="reply.userAvatar" :size="depth > 0 ? 28 : 32">
          {{ reply.userName?.charAt(0) || 'U' }}
        </el-avatar>
      </div>
      <div class="reply-content">
        <div class="reply-header">
          <span class="reply-author">{{ reply.userName }}</span>
          <el-tag
            v-if="isAuthor(reply.userName)"
            type="success"
            size="small"
            class="author-tag-inline"
          >
            楼主
          </el-tag>
          <span v-if="reply.replyTo" class="reply-to">
            回复
            <span class="reply-to-name">{{ reply.replyTo }}</span>
          </span>
          <span class="reply-time">{{ reply.createTime }}</span>
        </div>
        <div class="reply-text">{{ reply.content }}</div>
        <div class="reply-actions">
          <el-button
            type="text"
            size="small"
            @click="handleLike"
          >
            <template #icon>
              <HeartIcon :filled="reply.isLiked" :size="14" :color="reply.isLiked ? '#f56c6c' : '#999'" />
            </template>
            {{ reply.likes || 0 }}
          </el-button>
          <el-button
            v-if="canNest"
            type="text"
            size="small"
            :icon="ChatDotRound"
            @click="handleReplyClick"
          >
            回复
          </el-button>
          <el-button
            v-if="isMyReply"
            type="text"
            size="small"
            :icon="Delete"
            @click="handleDelete"
            :loading="deleting"
            style="color: #f56c6c;"
          >
            删除
          </el-button>
        </div>

        <!-- 回复输入框 -->
        <div class="reply-input" v-if="showReplyInput">
          <el-input
            v-model="replyText"
            type="textarea"
            :rows="2"
            :placeholder="`回复 ${reply.userName}...`"
            maxlength="200"
            show-word-limit
          />
          <div class="reply-actions">
            <el-button
              type="primary"
              size="small"
              @click="handleSubmitReply"
              :loading="replying"
            >
              发表回复
            </el-button>
            <el-button
              size="small"
              @click="handleCancelReply"
            >
              取消
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 递归渲染子回复（楼中楼 - 树形结构） -->
    <div class="nested-replies" v-if="reply.replies && reply.replies.length > 0">
      <ReplyItem
        v-for="childReply in reply.replies"
        :key="childReply.id"
        :reply="childReply"
        :comment="comment"
        :depth="depth + 1"
        :max-depth="maxDepth"
        :post-author-name="postAuthorName"
        :current-user-id="currentUserId"
        :current-user-name="currentUserName"
        @reply="handleChildReply"
        @delete="handleChildDelete"
        @like="handleChildLike"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Delete } from '@element-plus/icons-vue'
import HeartIcon from './HeartIcon.vue'

interface Props {
  reply: any
  comment: any
  depth: number
  maxDepth?: number
  postAuthorName?: string
  currentUserId?: number
  currentUserName?: string
}

const props = withDefaults(defineProps<Props>(), {
  maxDepth: 3,
  postAuthorName: '',
  currentUserId: 1,
  currentUserName: '当前用户'
})

const emit = defineEmits<{
  reply: [comment: any, targetReply: any, replyText: string]
  delete: [comment: any, reply: any]
  like: [reply: any]
}>()

const showReplyInput = ref(false)
const replyText = ref('')
const replying = ref(false)
const deleting = ref(false)

// 判断是否可以嵌套回复
const canNest = computed(() => {
  return props.depth < props.maxDepth
})

// 判断是否是自己的回复
const isMyReply = computed(() => {
  return props.reply.userId === props.currentUserId || props.reply.userName === props.currentUserName
})

// 判断是否是楼主
const isAuthor = (userName: string) => {
  return props.postAuthorName && userName === props.postAuthorName
}

// 点击回复按钮
const handleReplyClick = () => {
  showReplyInput.value = !showReplyInput.value
  if (showReplyInput.value) {
    replyText.value = `@${props.reply.userName} `
  }
}

// 提交回复
const handleSubmitReply = () => {
  if (!replyText.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  emit('reply', props.comment, props.reply, replyText.value)
  replyText.value = ''
  showReplyInput.value = false
}

// 取消回复
const handleCancelReply = () => {
  showReplyInput.value = false
  replyText.value = ''
}

// 删除回复
const handleDelete = () => {
  emit('delete', props.comment, props.reply)
}

// 点赞回复
const handleLike = () => {
  emit('like', props.reply)
}

// 处理子回复事件
const handleChildReply = (comment: any, targetReply: any, replyText: string) => {
  emit('reply', comment, targetReply, replyText)
}

const handleChildDelete = (comment: any, reply: any) => {
  emit('delete', comment, reply)
}

const handleChildLike = (reply: any) => {
  emit('like', reply)
}
</script>

<style scoped lang="scss">
.reply-item {
  margin-bottom: 12px;
  position: relative;

  // 嵌套回复的样式（树形结构）
  &.nested {
    margin-top: 12px;
    padding-left: 32px; // 缩进，形成树形结构
    position: relative;

    // 左侧垂直连接线
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: -12px;
      width: 2px;
      background: #e4e7ed;
    }

    // 水平连接线（指向父回复）
    &::after {
      content: '';
      position: absolute;
      left: 0;
      top: 20px;
      width: 24px;
      height: 2px;
      background: #e4e7ed;
    }

    // 最后一个嵌套回复，连接线不延伸到底部
    &:last-child::before {
      bottom: auto;
      height: 20px;
    }
  }
}

.reply-wrapper {
  display: flex;
  gap: 12px;
  position: relative;
  z-index: 1;
}

.avatar-wrapper-small {
  position: relative;
  display: inline-block;
  flex-shrink: 0;
}

.reply-content {
  flex: 1;
  background: rgba(250, 250, 250, 0.8);
  padding: 10px 14px;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.08);
  transition: all 0.2s;

  &:hover {
    background: rgba(255, 255, 255, 0.95);
    border-color: rgba(64, 158, 255, 0.2);
  }

  .reply-header {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-bottom: 8px;
    font-size: 13px;
    flex-wrap: wrap;

    .reply-author {
      font-weight: 500;
      color: #333;
    }

    .author-tag-inline {
      font-size: 11px;
      padding: 2px 8px;
      font-weight: 600;
      border-radius: 4px;
      margin-left: 4px;
    }

    .reply-to {
      color: #909399;
      font-size: 12px;

      .reply-to-name {
        color: #409eff;
        cursor: pointer;
        font-weight: 500;

        &:hover {
          text-decoration: underline;
        }
      }
    }

    .reply-time {
      color: #909399;
      font-size: 12px;
      margin-left: auto;
    }
  }

  .reply-text {
    font-size: 13px;
    line-height: 1.7;
    color: #555;
    margin-bottom: 10px;
    white-space: pre-wrap;
    word-break: break-word;
  }

  .reply-actions {
    display: flex;
    gap: 12px;
    margin-bottom: 0;
  }

  .reply-input {
    margin-top: 12px;
    padding: 12px;
    background: #fff;
    border-radius: 6px;
    border: 1px solid #e4e7ed;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

    .reply-actions {
      display: flex;
      justify-content: flex-end;
      gap: 8px;
      margin-top: 8px;
    }
  }
}

.nested-replies {
  margin-top: 8px;
  // 嵌套回复容器，子回复会通过 .nested 类自动缩进和显示连接线
}
</style>
