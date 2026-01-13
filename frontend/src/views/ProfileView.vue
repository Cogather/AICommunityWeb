<template>
  <div class="profile-view">
    <div class="profile-container">
      <!-- 用户信息卡片 -->
      <div class="user-info-card glass-card">
        <div class="user-header">
          <el-avatar :size="80" :src="userInfo.avatar" class="user-avatar">
            {{ userInfo.name?.charAt(0) || 'U' }}
          </el-avatar>
          <div class="user-details">
            <h2 class="user-name">{{ userInfo.name }}</h2>
            <p class="user-bio">{{ userInfo.bio || '这个人很懒，什么都没有留下' }}</p>
            <div class="user-stats">
              <div class="stat-item">
                <span class="stat-value">{{ userInfo.postsCount || 0 }}</span>
                <span class="stat-label">发布</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ userInfo.favoritesCount || 0 }}</span>
                <span class="stat-label">收藏</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ userInfo.commentsCount || 0 }}</span>
                <span class="stat-label">评论</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ userInfo.activitiesCount || 0 }}</span>
                <span class="stat-label">活动</span>
              </div>
              <div class="stat-item">
                <FlowerIcon :filled="true" :size="18" color="#f472b6" />
                <span class="stat-value">{{ userInfo.flowersCount || 0 }}</span>
                <span class="stat-label">花朵</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 标签页 -->
      <el-tabs v-model="activeTab" class="profile-tabs">
        <el-tab-pane :label="isViewingOtherUser ? '发布的帖子' : '我发布的帖子'" name="posts">
          <div class="tab-content">
            <PostList
              :posts="myPosts"
              :featured-posts="[]"
              :show-featured-tag="false"
              @post-click="handlePostClick"
            />
            <div v-if="myPosts.length === 0" class="empty-state">
              <el-empty description="还没有发布过帖子" />
            </div>
            <div class="pagination-wrapper" v-if="myPosts.length > 0">
              <el-pagination
                v-model:current-page="postsPage"
                v-model:page-size="postsPageSize"
                :total="myPosts.length"
                layout="total, prev, pager, next"
                @current-change="handlePostsPageChange"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane :label="isViewingOtherUser ? '收藏的帖子' : '我收藏的帖子'" name="favorites">
          <div class="tab-content">
            <PostList
              :posts="favoritePosts"
              :featured-posts="[]"
              :show-featured-tag="false"
              @post-click="handlePostClick"
            />
            <div v-if="favoritePosts.length === 0" class="empty-state">
              <el-empty description="还没有收藏过帖子" />
            </div>
            <div class="pagination-wrapper" v-if="favoritePosts.length > 0">
              <el-pagination
                v-model:current-page="favoritesPage"
                v-model:page-size="favoritesPageSize"
                :total="favoritePosts.length"
                layout="total, prev, pager, next"
                @current-change="handleFavoritesPageChange"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane :label="isViewingOtherUser ? '评论' : '我的评论'" name="comments">
          <div class="tab-content">
            <div class="comments-list">
              <div
                v-for="comment in paginatedComments"
                :key="comment.id"
                class="comment-item glass-card"
              >
                <div class="comment-header">
                  <el-avatar :size="40" :src="comment.userAvatar" class="comment-avatar">
                    {{ comment.userName?.charAt(0) || 'U' }}
                  </el-avatar>
                  <div class="comment-info">
                    <span class="comment-author">{{ comment.userName }}</span>
                    <span class="comment-time">{{ comment.createTime }}</span>
                  </div>
                </div>
                <div class="comment-content">
                  <p class="comment-text">{{ comment.content }}</p>
                  <div class="comment-post-link" @click="handlePostClick({ id: comment.postId })">
                    <el-icon><Document /></el-icon>
                    <span>查看原帖：{{ comment.postTitle }}</span>
                  </div>
                </div>
                <div class="comment-actions">
                  <span class="comment-likes">
                    <HeartIcon :filled="false" :size="16" color="#f56c6c" />
                    {{ comment.likes || 0 }}
                  </span>
                </div>
              </div>
            </div>
            <div v-if="myComments.length === 0" class="empty-state">
              <el-empty description="还没有发表过评论" />
            </div>
            <div class="pagination-wrapper" v-if="myComments.length > 0">
              <el-pagination
                v-model:current-page="commentsPage"
                v-model:page-size="commentsPageSize"
                :total="myComments.length"
                layout="total, prev, pager, next"
                @current-change="handleCommentsPageChange"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane :label="isViewingOtherUser ? '参与的活动' : '我参与的活动'" name="activities">
          <div class="tab-content">
            <div class="activities-list">
              <div
                v-for="activity in paginatedActivities"
                :key="activity.id"
                class="activity-item glass-card"
                @click="handleActivityClick(activity)"
                style="cursor: pointer;"
              >
                <div class="activity-image" v-if="activity.image">
                  <img :src="activity.image" :alt="activity.title" />
                </div>
                <div class="activity-content">
                  <h3 class="activity-title">{{ activity.title }}</h3>
                  <p class="activity-desc">{{ activity.description }}</p>
                  <div class="activity-meta">
                    <span class="activity-date">
                      <el-icon><Calendar /></el-icon>
                      {{ activity.date }}
                    </span>
                    <span class="activity-location" v-if="activity.location">
                      <el-icon><Location /></el-icon>
                      {{ activity.location }}
                    </span>
                    <el-tag :type="activity.status === 'ongoing' ? 'success' : 'info'" size="small">
                      {{ activity.status === 'ongoing' ? '进行中' : '已结束' }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="myActivities.length === 0" class="empty-state">
              <el-empty description="还没有参与过活动" />
            </div>
            <div class="pagination-wrapper" v-if="myActivities.length > 0">
              <el-pagination
                v-model:current-page="activitiesPage"
                v-model:page-size="activitiesPageSize"
                :total="myActivities.length"
                layout="total, prev, pager, next"
                @current-change="handleActivitiesPageChange"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane :label="isViewingOtherUser ? '发布的活动' : '我发布的活动'" name="myActivities">
          <div class="tab-content">
            <div class="activities-list">
              <div
                v-for="activity in paginatedMyActivities"
                :key="activity.id"
                class="activity-item glass-card"
              >
                <div class="activity-image" v-if="activity.cover">
                  <img :src="activity.cover" :alt="activity.title" />
                </div>
                <div class="activity-content">
                  <div class="activity-title-row">
                    <h3 class="activity-title">{{ activity.title }}</h3>
                    <el-button
                      type="default"
                      size="small"
                      @click.stop="handleActivityClick(activity)"
                      class="view-detail-btn"
                    >
                      查看详情
                    </el-button>
                  </div>
                  <p class="activity-desc">{{ activity.description || activity.content?.substring(0, 100) }}</p>
                  <div class="activity-meta">
                    <span class="activity-date">
                      <el-icon><Calendar /></el-icon>
                      {{ activity.date }}
                    </span>
                    <span class="activity-type">
                      <el-tag :type="getActivityTypeTag(activity.type)" size="small">
                        {{ getActivityTypeName(activity.type) }}
                      </el-tag>
                    </span>
                    <el-tag :type="activity.status === 'ongoing' ? 'success' : activity.status === 'upcoming' ? 'warning' : 'info'" size="small">
                      {{ getActivityStatusName(activity.status) }}
                    </el-tag>
                  </div>
                  <div class="activity-actions">
                    <span class="activity-registered-count">
                      <el-icon><User /></el-icon>
                      已报名：{{ activity.registeredCount || 0 }} 人
                    </span>
                    <el-button
                      type="primary"
                      size="small"
                      @click.stop="handleViewRegistrations(activity)"
                    >
                      <el-icon><View /></el-icon>
                      查看报名详情
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="myCreatedActivities.length === 0" class="empty-state">
              <el-empty description="还没有发布过活动" />
            </div>
            <div class="pagination-wrapper" v-if="myCreatedActivities.length > 0">
              <el-pagination
                v-model:current-page="myActivitiesPage"
                v-model:page-size="myActivitiesPageSize"
                :total="myCreatedActivities.length"
                layout="total, prev, pager, next"
                @current-change="handleMyActivitiesPageChange"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 报名详情对话框 -->
    <el-dialog
      v-model="showRegistrationsDialog"
      :title="`${currentActivity?.title || ''} - 报名详情`"
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-loading="loadingRegistrations" class="registrations-dialog">
        <div class="registrations-header">
          <span class="total-count">共 {{ registrations.length }} 人报名</span>
        </div>
        <div class="registrations-list" v-if="registrations.length > 0">
          <div
            v-for="(registration, index) in registrations"
            :key="registration.id"
            class="registration-item"
          >
            <div class="registration-number">{{ index + 1 }}</div>
            <el-avatar :size="48" :src="registration.userAvatar" class="registration-avatar">
              {{ registration.userName?.charAt(0) || 'U' }}
            </el-avatar>
            <div class="registration-info">
              <div class="registration-name">{{ registration.userName }}</div>
              <div class="registration-meta">
                <div class="meta-item" v-if="registration.employeeId">
                  <span class="meta-label">工号：</span>
                  <span class="meta-value">{{ registration.employeeId }}</span>
                </div>
                <div class="meta-item" v-if="registration.department">
                  <span class="meta-label">部门：</span>
                  <span class="meta-value">{{ registration.department }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">报名时间：</span>
                  <span class="meta-value">{{ registration.registerTime }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无报名用户" />
      </div>
      <template #footer>
        <el-button @click="showRegistrationsDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Document, Calendar, Location, User, View } from '@element-plus/icons-vue'
import { ElMessage, ElDialog } from 'element-plus'
import PostList from '../components/PostList.vue'
import HeartIcon from '../components/HeartIcon.vue'
import FlowerIcon from '../components/FlowerIcon.vue'
import {
  getUserProfileByName,
  getCurrentUser,
  getUserPosts,
  getUserFavorites,
  getUserComments,
  getUserActivities,
  getUserCreatedActivities,
  type Post,
  type Comment
} from '../mock'

const router = useRouter()
const route = useRoute()

// 从UsersView获取用户花朵数量的辅助函数（从用户资料中获取）
const _getUserFlowersFromHonors = (_userName: string): number => {
  // 花朵数量已从用户资料API中获取，这里直接返回0（实际应该从userInfo中获取）
  return userInfo.value.flowersCount || 0
}

// 当前标签页
const activeTab = ref('posts')

// 判断是否在查看其他用户
const isViewingOtherUser = computed(() => {
  return !!route.query.user && typeof route.query.user === 'string'
})

// 用户信息
const userInfo = ref({
  id: 0,
  name: '',
  avatar: '',
  bio: '',
  postsCount: 0,
  favoritesCount: 0,
  commentsCount: 0,
  activitiesCount: 0,
  flowersCount: 0
})

// 我发布的帖子
const myPosts = ref<Post[]>([])

// 我收藏的帖子
const favoritePosts = ref<any[]>([])

// 加载收藏的帖子
const loadFavoritePosts = async (userId: number) => {
  try {
    const result = await getUserFavorites(userId, {
      page: 1,
      pageSize: 100 // 获取所有收藏
    })
    // 字段映射
    favoritePosts.value = result.list.map(post => ({
      ...post,
      author: post.author || (post as any).authorName || '',
      description: post.description || (post as any).summary || '',
      image: post.image || post.cover || '',
      createTime: typeof post.createTime === 'string' ? post.createTime : new Date(post.createTime).toLocaleDateString('zh-CN')
    }))
    // 更新收藏数量
    userInfo.value.favoritesCount = result.total || favoritePosts.value.length
  } catch (error: any) {
    console.error('加载收藏列表失败:', error)
    ElMessage.error(error.message || '加载收藏列表失败')
    favoritePosts.value = []
  }
}

// 监听收藏更新事件
const handleFavoritesUpdate = async (_event: CustomEvent) => {
  // 重新加载收藏列表
  if (userInfo.value.id) {
    await loadFavoritePosts(userInfo.value.id)
  }
}

// 我的评论
const myComments = ref<Comment[]>([])

// 加载用户评论列表
const loadUserComments = async (userId: number) => {
  try {
    const result = await getUserComments(userId, {
      page: 1,
      pageSize: 100 // 获取所有评论
    })
    myComments.value = result.list.map(comment => ({
      ...comment,
      createTime: typeof comment.createTime === 'string' ? comment.createTime : new Date(comment.createTime).toLocaleString('zh-CN')
    }))
    // 更新评论数量
    userInfo.value.commentsCount = result.total || myComments.value.length
  } catch (error: any) {
    console.error('加载用户评论失败:', error)
    ElMessage.error(error.message || '加载用户评论失败')
    myComments.value = []
  }
}

// 我参与的活动
const myActivities = ref<any[]>([])

// 我发布的活动
const myCreatedActivities = ref<any[]>([])

// 加载报名的活动
const loadRegisteredActivities = async (userId: number) => {
  try {
    const result = await getUserActivities(userId, {
      page: 1,
      pageSize: 100 // 获取所有活动
    })
    myActivities.value = result.list.map(activity => ({
      ...activity,
      date: typeof activity.date === 'string' ? activity.date : new Date(activity.date).toLocaleString('zh-CN')
    }))
    // 更新活动数量
    userInfo.value.activitiesCount = result.total || myActivities.value.length
  } catch (error: any) {
    console.error('加载报名活动列表失败:', error)
    ElMessage.error(error.message || '加载报名活动列表失败')
    myActivities.value = []
  }
}

// 加载我发布的活动
const loadMyCreatedActivities = async (userId: number) => {
  try {
    const result = await getUserCreatedActivities(userId, {
      page: 1,
      pageSize: 100 // 获取所有活动
    })
    myCreatedActivities.value = result.list.map(activity => ({
      ...activity,
      date: typeof activity.date === 'string' ? activity.date : new Date(activity.date).toLocaleString('zh-CN')
    }))
  } catch (error: any) {
    console.error('加载发布活动列表失败:', error)
    ElMessage.error(error.message || '加载发布活动列表失败')
    myCreatedActivities.value = []
  }
}

// 监听活动报名更新事件
const handleActivityRegistered = async (_event: CustomEvent) => {
  // 重新加载报名的活动
  if (userInfo.value.id) {
    await loadRegisteredActivities(userInfo.value.id)
  }
}

// 分页
const postsPage = ref(1)
const postsPageSize = ref(10)
const favoritesPage = ref(1)
const favoritesPageSize = ref(10)
const commentsPage = ref(1)
const commentsPageSize = ref(10)
const activitiesPage = ref(1)
const activitiesPageSize = ref(10)
const myActivitiesPage = ref(1)
const myActivitiesPageSize = ref(10)

// 分页后的数据
const paginatedComments = computed(() => {
  const start = (commentsPage.value - 1) * commentsPageSize.value
  const end = start + commentsPageSize.value
  return myComments.value.slice(start, end)
})

const paginatedActivities = computed(() => {
  const start = (activitiesPage.value - 1) * activitiesPageSize.value
  const end = start + activitiesPageSize.value
  return myActivities.value.slice(start, end)
})

const paginatedMyActivities = computed(() => {
  const start = (myActivitiesPage.value - 1) * myActivitiesPageSize.value
  const end = start + myActivitiesPageSize.value
  return myCreatedActivities.value.slice(start, end)
})

// 根据用户名加载用户数据
const loadUserProfile = async (userName: string) => {
  try {
    const profile = await getUserProfileByName(userName)
    userInfo.value = {
      id: profile.id,
      name: profile.name,
      avatar: profile.avatar,
      bio: profile.bio || '',
      postsCount: profile.postsCount || 0,
      favoritesCount: profile.favoritesCount || 0,
      commentsCount: profile.commentsCount || 0,
      activitiesCount: profile.activitiesCount || 0,
      flowersCount: profile.flowersCount || 0
    }
    
    // 保存用户ID
    userInfo.value.id = profile.id
    
    // 加载该用户的帖子
    await loadUserPosts(profile.id)
    // 加载该用户的评论
    await loadUserComments(profile.id)
    // 加载该用户收藏的帖子
    await loadFavoritePosts(profile.id)
    // 加载该用户参与的活动
    await loadRegisteredActivities(profile.id)
    // 加载该用户发布的活动
    await loadMyCreatedActivities(profile.id)
  } catch (error: any) {
    console.error('加载用户资料失败:', error)
    ElMessage.error(error.message || '加载用户资料失败')
  }
}

// 根据用户ID加载帖子列表
const loadUserPosts = async (userId: number) => {
  try {
    const result = await getUserPosts(userId, {
      page: 1,
      pageSize: 100 // 获取所有帖子
    })
    // 字段映射
    myPosts.value = result.list.map(post => ({
      ...post,
      author: post.author || (post as any).authorName || '',
      description: post.description || (post as any).summary || '',
      image: post.image || post.cover || '',
      createTime: typeof post.createTime === 'string' ? post.createTime : new Date(post.createTime).toLocaleDateString('zh-CN')
    }))
    // 更新用户统计信息
    userInfo.value.postsCount = result.total || myPosts.value.length
  } catch (error: any) {
    console.error('加载用户帖子失败:', error)
    ElMessage.error(error.message || '加载用户帖子失败')
    myPosts.value = []
  }
}

// 监听路由参数，支持查看其他用户
watch(() => route.query.user, (userName) => {
  if (typeof userName === 'string' && userName) {
    loadUserProfile(userName)
  } else {
    // 如果没有指定用户，加载当前用户的数据
    loadUserData()
  }
}, { immediate: true })

// 处理帖子点击
const handlePostClick = (post: any) => {
  console.log('ProfileView: 处理帖子点击', post)
  if (!post || !post.id) {
    console.error('帖子数据无效:', post)
    return
  }
  router.push(`/post/${post.id}`).catch((err) => {
    console.error('路由跳转失败:', err)
  })
}

// 处理活动点击
const handleActivityClick = (activity: any) => {
  console.log('ProfileView: 处理活动点击', activity)
  if (!activity || !activity.id) {
    console.error('活动数据无效:', activity)
    return
  }
  router.push(`/activity/${activity.id}`).catch((err) => {
    console.error('路由跳转失败:', err)
  })
}

// 分页变化处理
const handlePostsPageChange = (page: number) => {
  postsPage.value = page
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const handleFavoritesPageChange = (page: number) => {
  favoritesPage.value = page
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const handleCommentsPageChange = (page: number) => {
  commentsPage.value = page
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const handleActivitiesPageChange = (page: number) => {
  activitiesPage.value = page
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const handleMyActivitiesPageChange = (page: number) => {
  myActivitiesPage.value = page
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 获取活动类型标签
const getActivityTypeTag = (type: string) => {
  const typeMap: Record<string, string> = {
    'training': 'success',
    'competition': 'warning',
    'sharing': 'info'
  }
  return typeMap[type] || 'info'
}

// 获取活动类型名称
const getActivityTypeName = (type: string) => {
  const nameMap: Record<string, string> = {
    'training': '培训',
    'competition': '竞赛',
    'sharing': '分享'
  }
  return nameMap[type] || type
}

// 获取活动状态名称
const getActivityStatusName = (status: string) => {
  const statusMap: Record<string, string> = {
    'upcoming': '即将开始',
    'ongoing': '进行中',
    'ended': '已结束'
  }
  return statusMap[status] || status
}

// 查看报名详情
const showRegistrationsDialog = ref(false)
const currentActivity = ref<any>(null)
const registrations = ref<any[]>([])
const loadingRegistrations = ref(false)

const handleViewRegistrations = async (activity: any) => {
  currentActivity.value = activity
  showRegistrationsDialog.value = true
  loadingRegistrations.value = true
  
  try {
    // 这里应该调用API获取报名用户列表
    // const response = await getActivityRegistrations(activity.id)
    // registrations.value = response.data.list
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据
    registrations.value = [
      {
        id: 1,
        userId: 2,
        userName: '李四',
        userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        employeeId: 'E001',
        department: '技术部',
        registerTime: '2024-04-05 10:30:00'
      },
      {
        id: 2,
        userId: 3,
        userName: '王五',
        userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        employeeId: 'E002',
        department: '产品部',
        registerTime: '2024-04-05 11:20:00'
      },
      {
        id: 3,
        userId: 4,
        userName: '赵六',
        userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        employeeId: 'E003',
        department: '设计部',
        registerTime: '2024-04-06 09:15:00'
      }
    ]
  } catch (error) {
    console.error('加载报名列表失败:', error)
    ElMessage.error('加载报名列表失败')
    registrations.value = []
  } finally {
    loadingRegistrations.value = false
  }
}

// 加载数据（当前用户）
const loadUserData = async () => {
  try {
    // 获取当前用户信息
    const profile = await getCurrentUser()
    userInfo.value = {
      id: profile.id,
      name: profile.name,
      avatar: profile.avatar,
      bio: profile.bio || '',
      postsCount: profile.postsCount || 0,
      favoritesCount: profile.favoritesCount || 0,
      commentsCount: profile.commentsCount || 0,
      activitiesCount: profile.activitiesCount || 0,
      flowersCount: profile.flowersCount || 0
    }
    
    // 加载当前用户的所有数据
    await Promise.all([
      loadUserPosts(profile.id),
      loadFavoritePosts(profile.id),
      loadUserComments(profile.id),
      loadRegisteredActivities(profile.id),
      loadMyCreatedActivities(profile.id)
    ])
  } catch (error: any) {
    console.error('加载用户数据失败:', error)
    ElMessage.error(error.message || '加载用户数据失败')
  }
}

onMounted(() => {
  // 如果路由中有用户参数，会在 watch 中处理
  // 否则加载当前用户数据
  if (!route.query.user) {
    loadUserData()
  }

  // 监听收藏更新事件
  window.addEventListener('favoritesUpdated', handleFavoritesUpdate as EventListener)
  // 监听活动报名更新事件
  window.addEventListener('activityRegistered', handleActivityRegistered as EventListener)
})

onBeforeUnmount(() => {
  window.removeEventListener('favoritesUpdated', handleFavoritesUpdate as EventListener)
  window.removeEventListener('activityRegistered', handleActivityRegistered as EventListener)
})
</script>

<style scoped lang="scss">
.profile-view {
  min-height: 100vh;
  padding: 20px;
}

.profile-container {
  max-width: 1200px;
  margin: 0 auto;
}

.glass-card {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.user-info-card {
  .user-header {
    display: flex;
    gap: 24px;
    align-items: flex-start;

    .user-avatar {
      flex-shrink: 0;
    }

    .user-details {
      flex: 1;

      .user-name {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 700;
        color: #333;
      }

      .user-bio {
        margin: 0 0 16px 0;
        color: #666;
        font-size: 14px;
      }

      .user-stats {
        display: flex;
        gap: 32px;

        .stat-item {
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 4px;

          .stat-value {
            font-size: 20px;
            font-weight: 700;
            color: #409eff;
          }

          .stat-label {
            font-size: 12px;
            color: #999;
          }
        }
      }
    }
  }
}

.profile-tabs {
  :deep(.el-tabs__header) {
    background: rgba(255, 255, 255, 0.6);
    backdrop-filter: blur(10px);
    border-radius: 16px 16px 0 0;
    padding: 0 24px;
    margin: 0;
    border: 1px solid rgba(0, 0, 0, 0.1);
    border-bottom: none;
  }

  :deep(.el-tabs__content) {
    background: rgba(255, 255, 255, 0.6);
    backdrop-filter: blur(10px);
    border-radius: 0 0 16px 16px;
    padding: 24px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    border-top: none;
  }
}

.tab-content {
  min-height: 400px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.pagination-wrapper {
  margin-top: 24px;
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

// 评论列表样式
.comments-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comment-item {
  padding: 20px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .comment-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;

    .comment-avatar {
      flex-shrink: 0;
    }

    .comment-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 4px;

      .comment-author {
        font-weight: 600;
        color: #333;
        font-size: 14px;
      }

      .comment-time {
        font-size: 12px;
        color: #999;
      }
    }
  }

  .comment-content {
    margin-bottom: 12px;

    .comment-text {
      margin: 0 0 12px 0;
      color: #666;
      line-height: 1.6;
      font-size: 14px;
    }

    .comment-post-link {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      color: #409eff;
      font-size: 13px;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #66b1ff;
      }
    }
  }

  .comment-actions {
    display: flex;
    align-items: center;
    gap: 16px;

    .comment-likes {
      display: flex;
      align-items: center;
      gap: 4px;
      color: #999;
      font-size: 13px;
    }
  }
}

// 活动列表样式
.activities-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
  max-width: 100%;
}

.activity-item {
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  overflow: hidden;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 12px;
  max-width: 100%;
  
  // 活动卡片使用更小的内边距
  &.glass-card {
    padding: 5px;
  }

  &:hover {
    transform: translateY(-6px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
    border-color: rgba(64, 158, 255, 0.2);
  }

  .activity-image {
    width: 100%;
    height: 160px;
    overflow: hidden;
    border-radius: 12px 12px 0 0;
    margin: -5px -5px 8px -5px;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f5f5f5;

    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 60px;
      background: linear-gradient(to top, rgba(0, 0, 0, 0.1), transparent);
      z-index: 1;
    }

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      object-position: center center;
      display: block;
      transition: transform 0.3s ease;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      min-width: 100%;
      min-height: 100%;
    }
  }

  &:hover {
    .activity-image img {
      transform: translate(-50%, -50%) scale(1.05);
    }

    .activity-title-row .activity-title {
      color: #409eff;
    }
  }

  .activity-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 12px;
    padding: 0 2px;

    .activity-title-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
      margin-bottom: 4px;

      .activity-title {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        color: #333;
        line-height: 1.4;
        transition: color 0.2s;
        flex: 1;
        min-width: 0;
      }

      .view-detail-btn {
        flex-shrink: 0;
      }
    }

    .activity-desc {
      margin: 0;
      color: #666;
      font-size: 13px;
      line-height: 1.6;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      min-height: 40px;
    }

    .activity-meta {
      display: flex;
      align-items: center;
      gap: 10px;
      flex-wrap: wrap;
      font-size: 12px;
      color: #666;
      margin-bottom: 6px;

      span {
        display: flex;
        align-items: center;
        gap: 6px;
      }

      .activity-date {
        color: #666;
      }
    }

    .activity-actions {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
      margin-top: 4px;
      padding-top: 16px;
      border-top: 1px solid rgba(0, 0, 0, 0.06);
      min-width: 0;
      width: 100%;

      .activity-registered-count {
        display: flex;
        align-items: center;
        gap: 4px;
        color: #409eff;
        font-weight: 500;
        background: rgba(64, 158, 255, 0.1);
        padding: 4px 10px;
        border-radius: 8px;
        font-size: 12px;
        white-space: nowrap;
        flex-shrink: 0;

        .el-icon {
          font-size: 12px;
        }
      }
    }
  }
}

// 报名详情对话框样式
.registrations-dialog {
  min-height: 200px;
  max-height: 600px;
  overflow-y: auto;

  .registrations-header {
    margin-bottom: 20px;
    padding-bottom: 16px;
    border-bottom: 2px solid rgba(64, 158, 255, 0.1);

    .total-count {
      font-size: 16px;
      font-weight: 600;
      color: #333;
      display: flex;
      align-items: center;
      gap: 8px;

      &::before {
        content: '';
        width: 4px;
        height: 16px;
        background: #409eff;
        border-radius: 2px;
      }
    }
  }

  .registrations-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .registration-item {
    display: flex;
    align-items: flex-start;
    gap: 16px;
    padding: 16px;
    background: #f8f9fa;
    border: 1px solid rgba(0, 0, 0, 0.06);
    border-radius: 12px;
    transition: all 0.3s ease;
    position: relative;

    &:hover {
      background: #fff;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      border-color: rgba(64, 158, 255, 0.2);
      transform: translateY(-2px);
    }

    .registration-number {
      flex-shrink: 0;
      width: 32px;
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #409eff, #66b1ff);
      color: #fff;
      font-weight: 600;
      font-size: 14px;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
    }

    .registration-avatar {
      flex-shrink: 0;
      border: 2px solid rgba(64, 158, 255, 0.2);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .registration-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 12px;
      min-width: 0;

      .registration-name {
        font-size: 16px;
        font-weight: 600;
        color: #333;
        margin-bottom: 4px;
      }

      .registration-meta {
        display: flex;
        flex-direction: column;
        gap: 8px;

        .meta-item {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 13px;
          line-height: 1.6;

          .meta-label {
            color: #666;
            font-weight: 500;
            min-width: 70px;
          }

          .meta-value {
            color: #333;
            font-weight: 400;
            flex: 1;
          }
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .user-info-card .user-header {
    flex-direction: column;
    align-items: center;
    text-align: center;

    .user-details .user-stats {
      justify-content: center;
    }
  }

  .activities-list {
    grid-template-columns: 1fr;
  }
}
</style>

