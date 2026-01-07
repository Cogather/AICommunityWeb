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
        <el-tab-pane label="我发布的帖子" name="posts">
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

        <el-tab-pane label="我收藏的帖子" name="favorites">
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

        <el-tab-pane label="我的评论" name="comments">
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

        <el-tab-pane label="我参与的活动" name="activities">
          <div class="tab-content">
            <div class="activities-list">
              <div
                v-for="activity in paginatedActivities"
                :key="activity.id"
                class="activity-item glass-card"
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
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Document, Star, Calendar, Location } from '@element-plus/icons-vue'
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
const favoritePosts = ref([
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
  }
])

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
const myActivities = ref([
  {
    id: 1,
    title: '2026 AI 开发者大会',
    description: '深入学习 AI 技术的最新发展和应用场景，与行业专家面对面交流。',
    date: '2026年3月15日',
    location: '北京',
    image: 'https://picsum.photos/600/400?random=10',
    status: 'ongoing'
  },
  {
    id: 2,
    title: 'AI工具链构建工作坊',
    description: '实战演练 AI 工具链的构建方法，提升团队协作效率。',
    date: '2026年2月20日',
    location: '上海',
    image: 'https://picsum.photos/600/400?random=11',
    status: 'ended'
  }
])

// 分页
const postsPage = ref(1)
const postsPageSize = ref(10)
const favoritesPage = ref(1)
const favoritesPageSize = ref(10)
const commentsPage = ref(1)
const commentsPageSize = ref(10)
const activitiesPage = ref(1)
const activitiesPageSize = ref(10)

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

// 监听路由参数，支持查看其他用户
watch(() => route.query.user, (userName) => {
  if (typeof userName === 'string' && userName) {
    // 这里应该调用API获取用户信息
    // 暂时使用mock数据
    userInfo.value = {
      ...userInfo.value,
      name: userName,
      flowersCount: getUserFlowersFromHonors(userName)
    }
  }
}, { immediate: true })

// 处理帖子点击
const handlePostClick = (post: any) => {
  router.push(`/post/${post.id}`)
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

// 加载数据
const loadUserData = async () => {
  // 这里应该调用API获取用户数据
  // const response = await getUserProfile()
  // userInfo.value = response.data.userInfo
  // myPosts.value = response.data.posts
  // ...
}

onMounted(() => {
  loadUserData()
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

      span {
        display: flex;
        align-items: center;
        gap: 4px;
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

