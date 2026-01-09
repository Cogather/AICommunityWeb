<template>
  <div class="post-detail-view">
    <div class="detail-container" v-loading="loading">
      <!-- 返回按钮 -->
      <div class="back-button-wrapper">
        <el-button
          :icon="ArrowLeft"
          text
          @click="handleBack"
          class="back-button"
        >
          返回
        </el-button>
      </div>

      <!-- 帖子内容区域 -->
      <div class="post-main" v-if="!loading">
        <!-- 帖子头部 -->
        <div class="post-header">
          <div class="post-title-section">
            <h1 class="post-title">{{ postData.title }}</h1>
          </div>
          
          <div class="post-info">
            <div class="author-info">
              <div class="avatar-wrapper" @click="handleAuthorClick">
                <el-avatar :src="postData.authorAvatar" :size="40" class="author-avatar-clickable">
                  {{ postData.authorName?.charAt(0) || 'U' }}
                </el-avatar>
                <el-tag v-if="postData.isAuthor" type="success" size="small" class="author-badge">
                  楼主
                </el-tag>
              </div>
              <div class="author-details">
                <span class="author-name author-name-clickable" @click="handleAuthorClick">{{ postData.authorName }}</span>
                <span class="post-time">{{ postData.createTime }}</span>
              </div>
            </div>
            <div class="post-stats">
              <span class="stat-item">
                <el-icon><View /></el-icon>
                <span>阅读 {{ postData.views }}</span>
              </span>
              <span class="stat-item">
                <el-icon><ChatDotRound /></el-icon>
                <span>评论 {{ commentCount }}</span>
              </span>
              <span class="stat-item">
                <HeartIcon :filled="false" :size="16" color="#f56c6c" />
                <span>点赞 {{ postData.likes }}</span>
              </span>
            </div>
          </div>
        </div>

        <!-- 帖子封面 -->
        <div class="post-cover" v-if="postData.cover">
          <img :src="postData.cover" :alt="postData.title" />
        </div>

        <!-- 帖子内容 -->
        <div class="post-content">
          <div class="content-html" v-html="postData.content"></div>
        </div>

        <!-- 帖子标签 -->
        <div class="post-tags-section" v-if="postData.tags && postData.tags.length > 0">
          <el-tag
            v-for="tag in postData.tags"
            :key="tag"
            :type="getTagType(tag)"
            size="small"
            class="post-tag"
          >
            {{ tag }}
          </el-tag>
        </div>

        <!-- 操作按钮 -->
        <div class="post-actions">
          <div class="actions-left">
            <el-button
              :type="postData.isLiked ? 'primary' : 'default'"
              @click="handleLike"
              :loading="liking"
            >
              <template #icon>
                <HeartIcon :filled="postData.isLiked" :size="16" :color="postData.isLiked ? '#fff' : '#f56c6c'" />
              </template>
              {{ postData.isLiked ? '已点赞' : '点赞' }} ({{ postData.likes }})
            </el-button>
            <el-button
              type="default"
              :icon="Share"
              @click="handleShare"
            >
              分享
            </el-button>
            <el-button
              :type="isCollected ? 'warning' : 'default'"
              @click="handleCollect"
            >
              <template #icon>
                <el-icon><Star /></el-icon>
              </template>
              {{ isCollected ? '已收藏' : '收藏' }}
            </el-button>
          </div>
          <div class="actions-right" v-if="canEditPost">
            <el-button
              type="warning"
              :icon="Edit"
              @click="handleEdit"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              :icon="Delete"
              @click="handleDelete"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>

      <!-- 评论区域 -->
      <div class="comments-section">
        <div class="comments-header">
          <h2>评论 ({{ commentCount }})</h2>
        </div>

        <!-- 评论输入框 -->
        <div class="comment-input-section">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="4"
            placeholder="写下你的评论..."
            maxlength="500"
            show-word-limit
          />
          <div class="comment-actions">
            <el-button type="primary" @click="handleSubmitComment" :loading="submitting">
              发表评论
            </el-button>
            <el-button @click="newComment = ''">取消</el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list" v-if="comments.length > 0">
          <div
            v-for="comment in comments"
            :key="comment.id"
            class="comment-item"
          >
            <div class="comment-main">
              <div class="avatar-wrapper">
                <el-avatar :src="comment.userAvatar" :size="40">
                  {{ comment.userName?.charAt(0) || 'U' }}
                </el-avatar>
              </div>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="comment-author">{{ comment.userName }}</span>
                  <el-tag v-if="isAuthor(comment.userName)" type="success" size="small" class="author-tag-inline">
                    楼主
                  </el-tag>
                  <span class="comment-time">{{ comment.createTime }}</span>
                </div>
                <div class="comment-text">{{ comment.content }}</div>
                <div class="comment-actions">
                  <el-button
                    type="text"
                    size="small"
                    @click="handleCommentLike(comment)"
                  >
                    <template #icon>
                      <HeartIcon :filled="comment.isLiked" :size="14" :color="comment.isLiked ? '#f56c6c' : '#999'" />
                    </template>
                    {{ comment.likes || 0 }}
                  </el-button>
                  <el-button
                    type="text"
                    size="small"
                    :icon="ChatDotRound"
                    @click="handleReplyClick(comment)"
                  >
                    回复
                  </el-button>
                  <!-- 删除按钮：只有评论作者本人可以删除，删除评论会同时删除该评论下的所有回复 -->
                  <el-button
                    v-if="isMyComment(comment)"
                    type="text"
                    size="small"
                    :icon="Delete"
                    @click="handleDeleteComment(comment)"
                    :loading="deletingComment"
                    style="color: #f56c6c;"
                    title="删除评论（将同时删除该评论下的所有回复）"
                  >
                    删除
                  </el-button>
                </div>

                <!-- 回复输入框（展开时显示） -->
                <div class="reply-input" v-if="comment.showReplyInput">
                  <el-input
                    v-model="comment.replyText"
                    type="textarea"
                    :rows="2"
                    placeholder="写下你的回复..."
                    maxlength="200"
                    show-word-limit
                  />
                  <div class="reply-actions">
                    <el-button
                      type="primary"
                      size="small"
                      @click="handleSubmitReply(comment)"
                      :loading="replying"
                    >
                      发表回复
                    </el-button>
                    <el-button
                      size="small"
                      @click="handleCancelReply(comment)"
                    >
                      取消
                    </el-button>
                  </div>
                </div>

                <!-- 回复列表（扁平化结构，所有回复同级） -->
                <div class="replies-list" v-if="comment.replies && comment.replies.length > 0">
                  <div
                    v-for="reply in comment.replies"
                    :key="reply.id"
                    class="reply-item-flat"
                  >
                    <div class="avatar-wrapper-small">
                      <el-avatar :src="reply.userAvatar" :size="32">
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
                          @click="handleReplyLike(reply)"
                        >
                          <template #icon>
                            <HeartIcon :filled="reply.isLiked" :size="14" :color="reply.isLiked ? '#f56c6c' : '#999'" />
                          </template>
                          {{ reply.likes || 0 }}
                        </el-button>
                        <el-button
                          type="text"
                          size="small"
                          :icon="ChatDotRound"
                          @click="handleReplyToReplyClick(comment, reply)"
                        >
                          回复
                        </el-button>
                        <!-- 删除按钮：只有回复作者本人可以删除，只删除该条回复 -->
                        <el-button
                          v-if="isMyReply(reply)"
                          type="text"
                          size="small"
                          :icon="Delete"
                          @click="handleDeleteReply(comment, reply)"
                          :loading="deletingComment"
                          style="color: #f56c6c;"
                          title="删除回复"
                        >
                          删除
                        </el-button>
                      </div>
                      <!-- 回复输入框 -->
                      <div class="reply-input" v-if="reply.showReplyInput">
                        <el-input
                          v-model="reply.replyText"
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
                            @click="handleSubmitReplyToReply(comment, reply)"
                            :loading="replying"
                          >
                            发表回复
                          </el-button>
                          <el-button
                            size="small"
                            @click="handleCancelReplyToReply(reply)"
                          >
                            取消
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <el-empty v-else description="暂无评论，快来发表第一条评论吧！" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  View,
  ChatDotRound,
  Star,
  Share,
  ArrowLeft,
  Edit,
  Delete
} from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import HeartIcon from '../components/HeartIcon.vue'

// 路由
const route = useRoute()
const router = useRouter()

// 帖子数据
const postData = ref({
  id: 1,
  title: '这是一个示例帖子标题',
  content: '<p>这是帖子的内容，支持富文本格式。</p><p>可以包含<strong>加粗</strong>、<em>斜体</em>等格式。</p>',
  cover: '',
  authorName: '张三',
  authorAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  createTime: '2024-01-07 10:30:00',
  views: 1234,
  likes: 56,
  isLiked: false,
  isAuthor: true,
  tags: ['自然语言处理', '深度学习', '最佳实践']
})

// 评论相关
const comments = ref<any[]>([])
const newComment = ref('')
const submitting = ref(false)
const replying = ref(false)
const liking = ref(false)
const deletingComment = ref(false)
const loading = ref(true)
const commentCount = computed(() => {
  // 计算所有评论和回复的总数（扁平化结构，直接计算 replies 长度）
  let total = comments.value.length
  comments.value.forEach(comment => {
    if (comment.replies && comment.replies.length > 0) {
      total += comment.replies.length
    }
  })
  return total
})

// 当前用户信息（实际应该从登录状态获取）
const currentUser = ref({
  id: 1, // 当前用户ID
  name: '当前用户' // 当前用户名
})

// 当前用户ID和用户名（用于评论和回复）
const currentUserId = computed(() => currentUser.value.id)
const currentUserName = computed(() => currentUser.value.name)

// 判断是否是当前用户
const isCurrentUser = (userName: string) => {
  return userName === currentUser.value.name
}

// 判断是否是自己的评论
const isMyComment = (comment: any) => {
  // 通过 userId 或 userName 判断是否是当前用户发布的评论
  if (comment.userId && currentUserId.value) {
    return comment.userId === currentUserId.value
  }
  if (comment.userName && currentUserName.value) {
    return comment.userName === currentUserName.value
  }
  return false
}

// 收藏状态
const isCollected = ref(false)

// 模拟管理员状态（实际应该从用户信息中获取）
const isAdmin = ref(false)

// 是否可以编辑帖子（作者或管理员）
const canEditPost = computed(() => {
  return postData.value.isAuthor || isAdmin.value
})

// 标签类型映射
const getTagType = (tag: string) => {
  const typeMap: Record<string, string> = {
    '自然语言处理': 'success',
    '深度学习': 'warning',
    '最佳实践': 'info',
    '机器学习': 'success',
    '计算机视觉': 'warning',
    '数据科学': 'info'
  }
  return typeMap[tag] || 'info'
}

// 判断是否是楼主
const isAuthor = (userName: string) => {
  return userName === postData.value.authorName
}

// 点击作者头像或姓名，跳转到用户信息中心
const handleAuthorClick = () => {
  router.push({
    path: '/profile',
    query: {
      user: postData.value.authorName
    }
  })
}

// 返回上一页
const handleBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/practices')
  }
}

// 加载帖子详情
const loadPostDetail = async () => {
  const postId = route.params.id
  if (!postId || (Array.isArray(postId) && postId.length === 0)) {
    ElMessage.error('帖子ID不存在')
    router.push('/practices')
    return
  }

  // 确保 postId 是字符串
  const postIdStr = Array.isArray(postId) ? postId[0] : postId
  const postIdNum = Number(postIdStr)
  
  if (isNaN(postIdNum)) {
    ElMessage.error('帖子ID格式错误')
    router.push('/practices')
    return
  }

  loading.value = true
  try {
    // 这里应该调用API获取帖子详情
    // const response = await getPostDetail(postIdNum)
    // postData.value = response.data
    
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据
    postData.value = {
      ...postData.value,
      id: postIdNum,
      title: `帖子标题 ${postIdStr}`,
      views: Math.floor(Math.random() * 5000) + 1000,
      likes: Math.floor(Math.random() * 200) + 10
    }
  } catch (error) {
    console.error('加载帖子详情失败:', error)
    ElMessage.error('加载帖子详情失败')
    router.push('/practices')
  } finally {
    loading.value = false
  }
}

// 加载评论列表
const loadComments = async () => {
  try {
    // 这里应该调用API获取评论列表
    // const response = await getComments(postData.value.id)
    // comments.value = response.data
    
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 300))
    
    // 模拟数据（包含嵌套回复结构）
    comments.value = [
      {
        id: 1,
        userId: 2,
        userName: '李四',
        userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        content: '这是一个很好的帖子，学到了很多！',
        createTime: '2024-01-07 11:00:00',
        likes: 5,
        isLiked: false,
        replies: [
          {
            id: 1,
            userId: 3,
            userName: '王五',
            userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
            content: '我也觉得很有用！',
            createTime: '2024-01-07 11:30:00',
            likes: 2,
            isLiked: false,
            replyTo: '李四',
            replyToId: 1,
            replies: [
              {
                id: 3,
                userId: 1,
                userName: postData.value.authorName,
                userAvatar: postData.value.authorAvatar,
                content: '感谢支持！',
                createTime: '2024-01-07 11:32:00',
                likes: 1,
                isLiked: false,
                replyTo: '王五',
                replyToId: 1,
                replies: []
              }
            ]
          },
          {
            id: 2,
            userId: 1,
            userName: postData.value.authorName,
            userAvatar: postData.value.authorAvatar,
            content: '谢谢支持！',
            createTime: '2024-01-07 11:35:00',
            likes: 3,
            isLiked: false,
            replyTo: '李四',
            replyToId: 1,
            replies: []
          }
        ],
        showReplyInput: false,
        replyText: ''
      },
      {
        id: 2,
        userId: 4,
        userName: '赵六',
        userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        content: '感谢分享，期待更多内容！',
        createTime: '2024-01-07 12:00:00',
        likes: 3,
        isLiked: false,
        replies: [],
        showReplyInput: false,
        replyText: ''
      }
    ]
  } catch (error) {
    console.error('加载评论失败:', error)
    ElMessage.error('加载评论失败')
  }
}

// 点赞帖子
const handleLike = async () => {
  if (liking.value) return
  
  liking.value = true
  try {
    // 这里应该调用API点赞
    // await likePost(postData.value.id)
    
    postData.value.isLiked = !postData.value.isLiked
    postData.value.likes += postData.value.isLiked ? 1 : -1
    
    ElMessage.success(postData.value.isLiked ? '点赞成功' : '取消点赞')
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('操作失败')
  } finally {
    liking.value = false
  }
}

// 分享
const handleShare = () => {
  // 复制链接到剪贴板
  const url = window.location.href
  navigator.clipboard.writeText(url).then(() => {
    ElMessage.success('链接已复制到剪贴板')
  }).catch(() => {
    ElMessage.error('复制失败')
  })
}

// 收藏/取消收藏
const handleCollect = () => {
  try {
    const currentUserId = 1 // 当前用户ID（实际应该从登录状态获取）
    const favoritesKey = `user_${currentUserId}_favorites`
    
    // 从localStorage读取收藏列表
    const favoritesStr = localStorage.getItem(favoritesKey)
    let favorites: any[] = []
    
    if (favoritesStr) {
      try {
        favorites = JSON.parse(favoritesStr)
      } catch (e) {
        console.warn('解析收藏列表失败:', e)
        favorites = []
      }
    }
    
    if (isCollected.value) {
      // 取消收藏：从列表中移除
      favorites = favorites.filter((fav: any) => fav.id !== postData.value.id)
      isCollected.value = false
      ElMessage.success('已取消收藏')
    } else {
      // 添加收藏：将帖子信息添加到列表
      const postToFavorite = {
        id: postData.value.id,
        title: postData.value.title,
        description: postData.value.content?.replace(/<[^>]*>/g, '').substring(0, 100) || '',
        author: postData.value.authorName,
        createTime: postData.value.createTime,
        views: postData.value.views,
        comments: commentCount.value,
        likes: postData.value.likes,
        tags: postData.value.tags || [],
        image: postData.value.cover || '',
        favoriteTime: new Date().toISOString() // 收藏时间
      }
      
      // 检查是否已存在
      const exists = favorites.some((fav: any) => fav.id === postData.value.id)
      if (!exists) {
        favorites.unshift(postToFavorite) // 添加到开头
        isCollected.value = true
        ElMessage.success('收藏成功')
      } else {
        ElMessage.warning('该帖子已在收藏列表中')
      }
    }
    
    // 保存到localStorage
    localStorage.setItem(favoritesKey, JSON.stringify(favorites))
    
    // 触发收藏更新事件（用于更新个人中心的收藏数量）
    window.dispatchEvent(new CustomEvent('favoritesUpdated', {
      detail: { userId: currentUserId, favorites }
    }))
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('操作失败，请稍后重试')
  }
}

// 检查是否已收藏
const checkIfCollected = () => {
  try {
    const currentUserId = 1 // 当前用户ID（实际应该从登录状态获取）
    const favoritesKey = `user_${currentUserId}_favorites`
    const favoritesStr = localStorage.getItem(favoritesKey)
    
    if (favoritesStr) {
      try {
        const favorites = JSON.parse(favoritesStr)
        isCollected.value = favorites.some((fav: any) => fav.id === postData.value.id)
      } catch (e) {
        console.warn('检查收藏状态失败:', e)
      }
    }
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 编辑帖子
const handleEdit = () => {
  router.push({
    path: '/post/create',
    query: {
      id: postData.value.id,
      edit: 'true'
    }
  })
}

// 删除帖子
const handleDelete = () => {
  ElMessageBox.confirm('确定要删除这个帖子吗？删除后无法恢复。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
    distinguishCancelAndClose: true
  }).then(async () => {
    try {
      // 这里应该调用API删除帖子
      // await deletePost(postData.value.id)
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      ElMessage.success('帖子已删除')
      router.push('/practices')
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }).catch(() => {
    // 用户取消
  })
}

// 提交评论
const handleSubmitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  submitting.value = true
  try {
    // 这里应该调用API提交评论
    // await submitComment(postData.value.id, newComment.value)
    
    const comment = {
      id: Date.now(),
      userId: currentUserId.value,
      userName: currentUserName.value,
      userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      content: newComment.value,
      createTime: new Date().toLocaleString('zh-CN'),
      likes: 0,
      isLiked: false,
      replies: [],
      showReplyInput: false,
      replyText: ''
    }
    
    comments.value.unshift(comment)
    newComment.value = ''
    ElMessage.success('评论发表成功')
  } catch (error) {
    console.error('发表评论失败:', error)
    ElMessage.error('发表评论失败')
  } finally {
    submitting.value = false
  }
}

// 点赞评论
const handleCommentLike = async (comment: any) => {
  try {
    // 这里应该调用API点赞评论
    // await likeComment(comment.id)
    
    comment.isLiked = !comment.isLiked
    comment.likes = (comment.likes || 0) + (comment.isLiked ? 1 : -1)
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('操作失败')
  }
}

// 点击回复按钮（回复评论）
const handleReplyClick = (comment: any) => {
  comment.showReplyInput = !comment.showReplyInput
  if (comment.showReplyInput) {
    comment.replyText = ''
  }
}

// 点击回复按钮（回复回复）
const handleReplyToReplyClick = (comment: any, reply: any) => {
  // 确保 reply 对象有 showReplyInput 属性
  if (!reply.hasOwnProperty('showReplyInput')) {
    reply.showReplyInput = false
  }
  reply.showReplyInput = !reply.showReplyInput
  if (reply.showReplyInput) {
    reply.replyText = `@${reply.userName} `
  }
}

// 提交回复（回复回复）
const handleSubmitReplyToReply = async (comment: any, targetReply: any) => {
  if (!targetReply.replyText?.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  replying.value = true
  try {
    const newReply = {
      id: Date.now(),
      userId: currentUserId.value,
      userName: currentUserName.value,
      userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      content: targetReply.replyText.replace(/^@\w+\s*/, ''),
      createTime: new Date().toLocaleString('zh-CN'),
      likes: 0,
      isLiked: false,
      replyTo: targetReply.userName,
      replyToId: targetReply.id,
    }
    
    if (!comment.replies) {
      comment.replies = []
    }
    // 找到目标回复的位置，在其后插入
    const targetIndex = comment.replies.findIndex((r: any) => r.id === targetReply.id)
    if (targetIndex !== -1) {
      comment.replies.splice(targetIndex + 1, 0, newReply)
    } else {
      comment.replies.push(newReply)
    }
    
    targetReply.replyText = ''
    targetReply.showReplyInput = false
    ElMessage.success('回复发表成功')
  } catch (error) {
    console.error('发表回复失败:', error)
    ElMessage.error('发表回复失败')
  } finally {
    replying.value = false
  }
}

// 取消回复（回复回复）
const handleCancelReplyToReply = (reply: any) => {
  reply.showReplyInput = false
  reply.replyText = ''
}

// 提交回复（回复评论）
const handleSubmitReply = async (comment: any) => {
  if (!comment.replyText?.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  replying.value = true
  try {
    // 这里应该调用API提交回复
    // await submitReply(comment.id, comment.replyText)
    
    const reply = {
      id: Date.now(),
      userId: currentUserId.value,
      userName: currentUserName.value,
      userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      content: comment.replyText.replace(/^@\w+\s*/, ''), // 移除 @用户名 前缀
      createTime: new Date().toLocaleString('zh-CN'),
      likes: 0,
      isLiked: false,
      replyTo: comment.userName, // 回复的目标用户名
      replyToId: comment.id, // 回复的目标评论ID
      // 不再有 replies 数组，所有回复都是同级
    }
    
    if (!comment.replies) {
      comment.replies = []
    }
    comment.replies.push(reply)
    comment.replyText = ''
    comment.showReplyInput = false
    
    ElMessage.success('回复发表成功')
  } catch (error) {
    console.error('发表回复失败:', error)
    ElMessage.error('发表回复失败')
  } finally {
    replying.value = false
  }
}

// 取消回复
const handleCancelReply = (comment: any) => {
  comment.showReplyInput = false
  comment.replyText = ''
}


// 点赞回复
const handleReplyLike = async (reply: any) => {
  try {
    // 这里应该调用API点赞回复
    // await likeReply(reply.id)
    
    reply.isLiked = !reply.isLiked
    reply.likes = (reply.likes || 0) + (reply.isLiked ? 1 : -1)
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('操作失败')
  }
}

// 判断是否是自己的回复
const isMyReply = (reply: any) => {
  // 通过 userId 或 userName 判断是否是当前用户发布的回复
  if (reply.userId && currentUserId.value) {
    return reply.userId === currentUserId.value
  }
  if (reply.userName && currentUserName.value) {
    return reply.userName === currentUserName.value
  }
  return false
}

// 删除评论（会删除该评论及其下所有回复）
const handleDeleteComment = (comment: any) => {
  const totalReplies = comment.replies?.length || 0
  const confirmMessage = totalReplies > 0
    ? `确定要删除这条评论吗？删除后该评论下的 ${totalReplies} 条回复也会一并删除，且无法恢复。`
    : '确定要删除这条评论吗？删除后无法恢复。'
  
  ElMessageBox.confirm(
    confirmMessage,
    '确认删除',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
      distinguishCancelAndClose: true
    }
  ).then(async () => {
    deletingComment.value = true
    try {
      // 这里应该调用API删除评论
      // await deleteComment(comment.id)
      // 注意：后端应该自动删除该评论下的所有回复（包括嵌套回复）
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 从评论列表中移除该评论（包括其所有回复）
      // 由于是扁平化结构，删除评论时会自动删除其下的所有回复
      const index = comments.value.findIndex(c => c.id === comment.id)
      if (index !== -1) {
        comments.value.splice(index, 1)
        ElMessage.success(totalReplies > 0 ? `评论及 ${totalReplies} 条回复已删除` : '评论已删除')
      } else {
        ElMessage.warning('未找到要删除的评论')
      }
    } catch (error) {
      console.error('删除评论失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    } finally {
      deletingComment.value = false
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 删除回复（只删除该条回复，不影响其他回复）
const handleDeleteReply = (comment: any, reply: any) => {
  ElMessageBox.confirm(
    '确定要删除这条回复吗？删除后无法恢复。',
    '确认删除',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
      distinguishCancelAndClose: true
    }
  ).then(async () => {
    deletingComment.value = true
    try {
      // 这里应该调用API删除回复
      // await deleteReply(reply.id)
      // 注意：只删除该条回复，不会删除其他回复
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 从评论的 replies 数组中删除该条回复（扁平化结构，只删除单条回复）
      if (comment.replies && Array.isArray(comment.replies)) {
        const index = comment.replies.findIndex((r: any) => r.id === reply.id)
        if (index !== -1) {
          comment.replies.splice(index, 1)
          ElMessage.success('回复已删除')
        } else {
          ElMessage.warning('未找到要删除的回复')
        }
      } else {
        ElMessage.warning('回复列表不存在')
      }
    } catch (error) {
      console.error('删除回复失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    } finally {
      deletingComment.value = false
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 初始化
onMounted(async () => {
  await loadPostDetail()
  if (postData.value.id) {
    await loadComments()
    checkIfCollected() // 检查收藏状态
  }
})
</script>

<style scoped lang="scss">
.post-detail-view {
  min-height: 100vh;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.detail-container {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  position: relative;
}

.back-button-wrapper {
  margin-bottom: 20px;
  
  .back-button {
    font-size: 14px;
    color: #666;
    
    &:hover {
      color: #409eff;
    }
  }
}

.post-main {
  margin-bottom: 32px;
}

.post-header {
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);

  .post-title-section {
    margin-bottom: 16px;

    .post-title {
      font-size: 28px;
      font-weight: 600;
      color: #333;
      margin: 0 0 12px 0;
      line-height: 1.4;
    }

    .post-meta-top {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      align-items: center;

      .author-tag {
        font-weight: 600;
      }

      .post-tag {
        cursor: default;
      }
    }
  }

  .post-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 16px;

    .author-info {
      display: flex;
      align-items: center;
      gap: 12px;

      .avatar-wrapper {
        position: relative;
        display: inline-block;
        cursor: pointer;
        transition: transform 0.2s;

        &:hover {
          transform: scale(1.05);
        }

        .author-avatar-clickable {
          cursor: pointer;
          transition: all 0.2s;
          
          &:hover {
            box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
          }
        }

        .author-badge {
          position: absolute;
          bottom: -4px;
          right: -4px;
          font-size: 10px;
          padding: 2px 6px;
          border-radius: 8px;
          font-weight: 600;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
          z-index: 1;
        }
      }

      .author-details {
        display: flex;
        flex-direction: column;
        gap: 4px;

        .author-name {
          font-size: 16px;
          font-weight: 500;
          color: #333;
          
          &.author-name-clickable {
            cursor: pointer;
            transition: color 0.2s;
            
            &:hover {
              color: #409eff;
            }
          }
        }

        .post-time {
          font-size: 12px;
          color: #909399;
        }
      }
    }

    .post-stats {
      display: flex;
      gap: 20px;
      align-items: center;

      .stat-item {
        display: flex;
        align-items: center;
        gap: 4px;

        .heart-icon {
          flex-shrink: 0;
        }
        font-size: 14px;
        color: #666;

        .el-icon {
          font-size: 16px;
        }
      }
    }
  }
}

.post-cover {
  margin-bottom: 24px;
  border-radius: 8px;
  overflow: hidden;

  img {
    width: 100%;
    height: auto;
    display: block;
  }
}

.post-content {
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  min-height: 200px;
}

.post-tags-section {
  margin-bottom: 24px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;

  .post-tag {
    cursor: default;
  }
}

.content-html {
  font-size: 16px;
  line-height: 1.8;
  color: #333;

  :deep(p) {
    margin: 1em 0;
  }

  :deep(h1), :deep(h2), :deep(h3) {
    margin: 1.5em 0 1em;
    font-weight: 600;
  }

  :deep(img) {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 1em 0;
  }

  :deep(table) {
    width: 100%;
    border-collapse: collapse;
    margin: 1em 0;

    td, th {
      border: 1px solid #ddd;
      padding: 8px;
    }

    th {
      background: #f5f5f5;
      font-weight: 600;
    }
  }

  :deep(blockquote) {
    border-left: 4px solid #409eff;
    padding-left: 1em;
    margin: 1em 0;
    color: #666;
  }

  :deep(code) {
    background: #f4f4f4;
    padding: 2px 4px;
    border-radius: 3px;
    font-family: monospace;
  }

  :deep(pre) {
    background: #f4f4f4;
    padding: 1em;
    border-radius: 4px;
    overflow-x: auto;
    margin: 1em 0;

    code {
      background: none;
      padding: 0;
    }
  }
}

.post-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);

  .actions-left {
    display: flex;
    gap: 12px;
  }

  .actions-right {
    display: flex;
    gap: 12px;
  }
}

.comments-section {
  margin-top: 32px;
  padding-top: 32px;
  border-top: 2px solid rgba(0, 0, 0, 0.1);

  .comments-header {
    margin-bottom: 20px;

    h2 {
      font-size: 20px;
      font-weight: 600;
      color: #333;
      margin: 0;
    }
  }

  .comment-input-section {
    margin-bottom: 32px;
    padding: 16px;
    background: #fafafa;
    border-radius: 8px;

    .comment-actions {
      display: flex;
      justify-content: flex-end;
      gap: 12px;
      margin-top: 12px;
    }
  }

  .comments-list {
    .comment-item {
      margin-bottom: 24px;
      padding-bottom: 24px;
      border-bottom: 1px solid rgba(0, 0, 0, 0.05);

      &:last-child {
        border-bottom: none;
        margin-bottom: 0;
        padding-bottom: 0;
      }

      .comment-main {
        display: flex;
        gap: 12px;

        .avatar-wrapper {
          position: relative;
          display: inline-block;
          flex-shrink: 0;

          .author-badge {
            position: absolute;
            bottom: -4px;
            right: -4px;
            font-size: 10px;
            padding: 2px 6px;
            border-radius: 8px;
            font-weight: 600;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            z-index: 1;
          }
        }

        .comment-content {
          flex: 1;

          .comment-header {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 8px;
            flex-wrap: wrap;

            .comment-author {
              font-size: 14px;
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

            .comment-time {
              font-size: 12px;
              color: #909399;
            }
          }

          .comment-text {
            font-size: 14px;
            line-height: 1.6;
            color: #666;
            margin-bottom: 12px;
            white-space: pre-wrap;
            word-break: break-word;
          }

          .comment-actions {
            display: flex;
            gap: 16px;
            margin-bottom: 12px;
          }

          .reply-input {
            margin-top: 12px;
            padding: 12px;
            background: #fff;
            border-radius: 4px;
            border: 1px solid #e4e7ed;

            .reply-actions {
              display: flex;
              justify-content: flex-end;
              gap: 8px;
              margin-top: 8px;
            }
          }

            .replies-list {
            margin-top: 16px;
            
            .reply-item-flat {
              display: flex;
              gap: 12px;
              margin-bottom: 16px;
              padding-left: 24px; // 统一缩进
              position: relative;
              
              &:last-child {
                margin-bottom: 0;
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
            }

            .reply-item {
              display: flex;
              gap: 12px;
              margin-bottom: 16px;
              padding-bottom: 16px;
              border-bottom: 1px solid rgba(0, 0, 0, 0.05);

              &:last-child {
                border-bottom: none;
                margin-bottom: 0;
                padding-bottom: 0;
              }

              .avatar-wrapper-small {
                position: relative;
                display: inline-block;
                flex-shrink: 0;
              }

              .reply-content {
                flex: 1;

                .reply-header {
                  display: flex;
                  align-items: center;
                  gap: 6px;
                  margin-bottom: 6px;
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

                    .reply-to-name {
                      color: #409eff;
                      cursor: pointer;

                      &:hover {
                        text-decoration: underline;
                      }
                    }
                  }

                  .reply-time {
                    color: #909399;
                    font-size: 12px;
                  }
                }

                .reply-text {
                  font-size: 13px;
                  line-height: 1.6;
                  color: #666;
                  margin-bottom: 8px;
                  white-space: pre-wrap;
                  word-break: break-word;
                }

                .reply-actions {
                  display: flex;
                  gap: 12px;
                }
              }
            }
          }
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .post-detail-view {
    padding: 12px;
  }

  .detail-container {
    padding: 16px;
  }

  .post-header {
    .post-info {
      flex-direction: column;
      align-items: flex-start;
    }
  }

  .post-actions {
    flex-wrap: wrap;
  }
}
</style>

