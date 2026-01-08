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
                  <h3 class="activity-title">{{ activity.title }}</h3>
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
                    <span class="activity-registered-count">
                      <el-icon><User /></el-icon>
                      已报名：{{ activity.registeredCount || 0 }} 人
                    </span>
                  </div>
                  <div class="activity-actions">
                    <el-button
                      type="primary"
                      size="small"
                      @click.stop="handleViewRegistrations(activity)"
                    >
                      <el-icon><View /></el-icon>
                      查看报名详情
                    </el-button>
                    <el-button
                      type="default"
                      size="small"
                      @click.stop="handleActivityClick(activity)"
                    >
                      查看详情
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
            v-for="registration in registrations"
            :key="registration.id"
            class="registration-item"
          >
            <el-avatar :size="40" :src="registration.userAvatar" class="registration-avatar">
              {{ registration.userName?.charAt(0) || 'U' }}
            </el-avatar>
            <div class="registration-info">
              <div class="registration-name">{{ registration.userName }}</div>
              <div class="registration-meta">
                <span v-if="registration.employeeId">工号：{{ registration.employeeId }}</span>
                <span v-if="registration.department">部门：{{ registration.department }}</span>
              </div>
            </div>
            <div class="registration-time">
              {{ registration.registerTime }}
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
import { Document, Star, Calendar, Location, User, View } from '@element-plus/icons-vue'
import { ElMessage, ElDialog, ElMessageBox } from 'element-plus'
import PostList from '../components/PostList.vue'
import HeartIcon from '../components/HeartIcon.vue'
import FlowerIcon from '../components/FlowerIcon.vue'

const router = useRouter()
const route = useRoute()

// 从UsersView获取用户花朵数量的辅助函数（这里需要共享数据或API调用）
// 暂时使用mock数据，实际应该从API或store获取
const getUserFlowersFromHonors = (userName: string): number => {
  // 这里应该调用API或从共享store获取
  // 暂时返回mock数据
  const mockFlowers: Record<string, number> = {
    '林星辰': 20,
    'Sarah': 15,
    '张伟': 55,
    '王强': 18,
    'Emily': 14,
    '赵敏': 9,
    '张三': 34
  }
  return mockFlowers[userName] || 0
}

// 当前标签页
const activeTab = ref('posts')

// 判断是否在查看其他用户
const isViewingOtherUser = computed(() => {
  return !!route.query.user && typeof route.query.user === 'string'
})

// 用户信息
const userInfo = ref({
  name: '张三',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  bio: 'AI技术爱好者，专注于机器学习和深度学习',
  postsCount: 12,
  favoritesCount: 8,
  commentsCount: 45,
  activitiesCount: 3,
  flowersCount: 34
})

// 我发布的帖子
const myPosts = ref([
  {
    id: 1,
    title: '大模型在工业设计中的落地应用案例分享',
    description: '分享如何在实际项目中高效使用大模型提升设计效率。',
    author: '张三',
    createTime: '2024年4月10日',
    views: 1250,
    comments: 45,
    likes: 128,
    tags: ['讨论', 'AI应用'],
    image: 'https://picsum.photos/400/300?random=1'
  },
  {
    id: 2,
    title: '如何利用AI提升代码质量和开发效率',
    description: '介绍AI工具在代码开发中的实际应用经验。',
    author: '张三',
    createTime: '2024年4月8日',
    views: 890,
    comments: 32,
    likes: 95,
    tags: ['分享', '开发'],
    image: 'https://picsum.photos/400/300?random=2'
  }
])

// 我收藏的帖子
const favoritePosts = ref<any[]>([])

// 加载收藏的帖子
const loadFavoritePosts = () => {
  try {
    const currentUserId = 1 // 当前用户ID（实际应该从登录状态获取）
    const favoritesKey = `user_${currentUserId}_favorites`
    const favoritesStr = localStorage.getItem(favoritesKey)

    if (favoritesStr) {
      try {
        const favorites = JSON.parse(favoritesStr)
        // 按收藏时间倒序排列（最新的在前）
        favoritePosts.value = favorites.sort((a: any, b: any) => {
          const timeA = new Date(a.favoriteTime || 0).getTime()
          const timeB = new Date(b.favoriteTime || 0).getTime()
          return timeB - timeA
        })
        // 更新收藏数量
        userInfo.value.favoritesCount = favoritePosts.value.length
      } catch (e) {
        console.warn('解析收藏列表失败:', e)
        favoritePosts.value = []
      }
    } else {
      favoritePosts.value = []
    }
  } catch (error) {
    console.error('加载收藏列表失败:', error)
    favoritePosts.value = []
  }
}

// 监听收藏更新事件
const handleFavoritesUpdate = (event: CustomEvent) => {
  if (event.detail.userId === 1) { // 当前用户ID
    loadFavoritePosts()
  }
}

// 我的评论
const myComments = ref([
  {
    id: 1,
    postId: 5,
    postTitle: '多模态模型在医疗影像分析中的应用',
    userName: '张三',
    userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    content: '这个案例非常实用，感谢分享！',
    createTime: '2024年4月11日 10:30',
    likes: 5
  },
  {
    id: 2,
    postId: 6,
    postTitle: '构建企业级AI知识库的完整方案',
    userName: '张三',
    userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    content: '很有参考价值，我们公司也在做类似的项目。',
    createTime: '2024年4月10日 15:20',
    likes: 3
  }
])

// 我参与的活动
const myActivities = ref<any[]>([])

// 我发布的活动
const myCreatedActivities = ref<any[]>([])

// 加载报名的活动
const loadRegisteredActivities = () => {
  try {
    const currentUserId = 1 // 当前用户ID（实际应该从登录状态获取）
    const registeredKey = `user_${currentUserId}_registered_activities`
    const registeredStr = localStorage.getItem(registeredKey)

    if (registeredStr) {
      try {
        const registered = JSON.parse(registeredStr)
        // 按报名时间倒序排列（最新的在前）
        myActivities.value = registered.sort((a: any, b: any) => {
          const timeA = new Date(a.registerTime || 0).getTime()
          const timeB = new Date(b.registerTime || 0).getTime()
          return timeB - timeA
        })
        // 更新活动数量
        userInfo.value.activitiesCount = myActivities.value.length
      } catch (e) {
        console.warn('解析报名活动列表失败:', e)
        myActivities.value = []
      }
    } else {
      myActivities.value = []
    }
  } catch (error) {
    console.error('加载报名活动列表失败:', error)
    myActivities.value = []
  }
}

// 加载我发布的活动
const loadMyCreatedActivities = async () => {
  try {
    // 这里应该调用API获取用户发布的活动
    // const response = await getUserCreatedActivities(userInfo.value.name)
    // myCreatedActivities.value = response.data
    
    // 模拟数据
    await new Promise(resolve => setTimeout(resolve, 300))
    
    // 从localStorage获取发布的活动（实际应该从API获取）
    const createdKey = `user_${userInfo.value.name}_created_activities`
    const createdStr = localStorage.getItem(createdKey)
    
    if (createdStr) {
      try {
        const created = JSON.parse(createdStr)
        myCreatedActivities.value = created.sort((a: any, b: any) => {
          const timeA = new Date(a.createTime || 0).getTime()
          const timeB = new Date(b.createTime || 0).getTime()
          return timeB - timeA
        })
      } catch (e) {
        console.warn('解析发布活动列表失败:', e)
        myCreatedActivities.value = []
      }
    } else {
      // 模拟数据
      myCreatedActivities.value = [
        {
          id: 1,
          title: 'AI工具使用培训活动',
          description: '分享AI工具的使用技巧和最佳实践',
          content: '本次活动将详细介绍各种AI工具的使用方法...',
          cover: 'https://picsum.photos/400/300?random=10',
          date: '2024-04-15',
          type: 'training',
          status: 'upcoming',
          registeredCount: 15,
          createTime: '2024-04-01 10:00:00'
        },
        {
          id: 2,
          title: 'AI应用创新大赛',
          description: '展示你的AI应用创新项目',
          cover: 'https://picsum.photos/400/300?random=11',
          date: '2024-04-20',
          type: 'competition',
          status: 'upcoming',
          registeredCount: 8,
          createTime: '2024-04-02 14:30:00'
        }
      ]
    }
  } catch (error) {
    console.error('加载发布活动列表失败:', error)
    myCreatedActivities.value = []
  }
}

// 监听活动报名更新事件
const handleActivityRegistered = (event: CustomEvent) => {
  if (event.detail.userId === 1) { // 当前用户ID
    loadRegisteredActivities()
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
  // 这里应该调用API获取用户信息
  // 暂时使用mock数据
  userInfo.value = {
    ...userInfo.value,
    name: userName,
    avatar: getUserAvatar(userName),
    bio: getUserBio(userName),
    flowersCount: getUserFlowersFromHonors(userName)
  }

  // 加载该用户的帖子
  await loadUserPosts(userName)
  // 加载该用户发布的活动（如果是查看其他用户，也加载）
  await loadMyCreatedActivities()
}

// 获取用户头像（mock数据）
const getUserAvatar = (userName: string): string => {
  const avatarMap: Record<string, string> = {
    '张三': 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    '李四': 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    '王五': 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    '赵六': 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  }
  return avatarMap[userName] || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
}

// 获取用户简介（mock数据）
const getUserBio = (userName: string): string => {
  const bioMap: Record<string, string> = {
    '张三': 'AI技术爱好者，专注于机器学习和深度学习',
    '李四': '前端开发工程师，热爱新技术',
    '王五': '后端架构师，专注于系统设计',
    '赵六': '全栈开发者，喜欢分享技术经验'
  }
  return bioMap[userName] || '这个人很懒，什么都没有留下'
}

// 根据用户名加载帖子列表
const loadUserPosts = async (userName: string) => {
  // 这里应该调用API获取该用户的帖子
  // 暂时使用mock数据，根据用户名过滤
  const allPosts = [
    {
      id: 1,
      title: '大模型在工业设计中的落地应用案例分享',
      description: '分享如何在实际项目中高效使用大模型提升设计效率。',
      author: '张三',
      createTime: '2024年4月10日',
      views: 1250,
      comments: 45,
      likes: 128,
      tags: ['讨论', 'AI应用'],
      image: 'https://picsum.photos/400/300?random=1'
    },
    {
      id: 2,
      title: '如何利用AI提升代码质量和开发效率',
      description: '介绍AI工具在代码开发中的实际应用经验。',
      author: '张三',
      createTime: '2024年4月8日',
      views: 890,
      comments: 32,
      likes: 95,
      tags: ['分享', '开发'],
      image: 'https://picsum.photos/400/300?random=2'
    },
    {
      id: 3,
      title: 'AI辅助测试的最佳实践与经验总结',
      description: '分享AI在测试领域的应用经验和最佳实践。',
      author: '李四',
      createTime: '2024年4月9日',
      views: 720,
      comments: 28,
      likes: 78,
      tags: ['测试', '最佳实践'],
      image: 'https://picsum.photos/400/300?random=3'
    },
    {
      id: 4,
      title: '深度学习模型优化技巧分享',
      description: '介绍如何优化深度学习模型的性能和效率。',
      author: '张三',
      createTime: '2024年4月5日',
      views: 1100,
      comments: 38,
      likes: 102,
      tags: ['深度学习', '优化'],
      image: 'https://picsum.photos/400/300?random=4'
    }
  ]

  // 根据用户名过滤帖子
  myPosts.value = allPosts.filter(post => post.author === userName)

  // 更新用户统计信息
  userInfo.value.postsCount = myPosts.value.length
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
  // 这里应该调用API获取当前用户数据
  // const response = await getUserProfile()
  // userInfo.value = response.data.userInfo
  // myPosts.value = response.data.posts
  // ...

  // 如果没有路由参数，使用默认用户数据
  if (!route.query.user) {
    await loadUserPosts(userInfo.value.name)
    loadFavoritePosts() // 加载收藏的帖子
    loadRegisteredActivities() // 加载报名的活动
    await loadMyCreatedActivities() // 加载发布的活动
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
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.activity-item {
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  overflow: hidden;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  }

  .activity-image {
    width: 100%;
    height: 180px;
    overflow: hidden;
    border-radius: 12px 12px 0 0;
    margin: -24px -24px 16px -24px;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .activity-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 12px;

    .activity-title {
      margin: 0;
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }

    .activity-desc {
      margin: 0;
      color: #666;
      font-size: 14px;
      line-height: 1.6;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

  .activity-meta {
    display: flex;
    align-items: center;
    gap: 16px;
    flex-wrap: wrap;
    font-size: 13px;
    color: #999;
    margin-bottom: 12px;

    span {
      display: flex;
      align-items: center;
      gap: 4px;
    }

    .activity-registered-count {
      margin-left: auto;
      color: #409eff;
      font-weight: 500;
    }
  }

  .activity-actions {
    display: flex;
    gap: 12px;
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid rgba(0, 0, 0, 0.05);
  }
}

// 报名详情对话框样式
.registrations-dialog {
  min-height: 200px;
  max-height: 500px;
  overflow-y: auto;

  .registrations-header {
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);

    .total-count {
      font-size: 14px;
      font-weight: 600;
      color: #333;
    }
  }

  .registrations-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .registration-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    background: rgba(250, 250, 250, 0.8);
    border-radius: 8px;
    transition: all 0.2s;

    &:hover {
      background: rgba(255, 255, 255, 0.95);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }

    .registration-avatar {
      flex-shrink: 0;
    }

    .registration-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 4px;

      .registration-name {
        font-size: 14px;
        font-weight: 500;
        color: #333;
      }

      .registration-meta {
        font-size: 12px;
        color: #999;
        display: flex;
        gap: 12px;
      }
    }

    .registration-time {
      font-size: 12px;
      color: #999;
      flex-shrink: 0;
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

