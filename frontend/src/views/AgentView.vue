<template>
  <div class="agent-view">
    <!-- 置顶帖子 -->
    <div class="featured-post" v-if="featuredPost">
      <div class="featured-post-card" @click="handlePostClick(featuredPost)">
        <div class="featured-post-image">
          <img :src="featuredPost.image" :alt="featuredPost.title" />
        </div>
        <div class="featured-post-content">
          <div class="featured-post-header">
            <el-tag type="success" size="small">置顶</el-tag>
            <h2 class="featured-post-title">{{ featuredPost.title }}</h2>
          </div>
          <p class="featured-post-description">{{ featuredPost.description }}</p>
          <div class="featured-post-tags" v-if="featuredPost.tags && featuredPost.tags.length > 0">
            <el-tag
              v-for="tag in featuredPost.tags"
              :key="tag"
              :type="getTagType(tag)"
              size="small"
              class="post-tag"
            >
              {{ tag }}
            </el-tag>
          </div>
          <div class="featured-post-meta">
            <span class="meta-item">
              <el-icon><User /></el-icon>
              {{ featuredPost.author }}
            </span>
            <span class="meta-item">
              <el-icon><Clock /></el-icon>
              {{ formatDate(featuredPost.createTime) }}
            </span>
            <span class="meta-item">
              <el-icon><View /></el-icon>
              {{ featuredPost.views }}
            </span>
            <span class="meta-item">
              <el-icon><ChatDotRound /></el-icon>
              {{ featuredPost.comments }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 左右分栏 -->
    <el-row :gutter="24">
      <!-- 左侧：帖子列表 -->
      <el-col :xs="24" :md="18">
        <div class="posts-section">
          <!-- 头部操作栏 -->
          <PostHeader
            :show-tabs="false"
            :default-sort="sortBy"
            @search="handleSearch"
            @sort="handleSort"
            @post-click="handlePostCreate"
          />

          <!-- 帖子列表 -->
          <PostList
            :posts="paginatedPosts"
            :featured-posts="[]"
            :show-featured-tag="false"
            @post-click="handlePostClick"
          />

          <!-- 分页 -->
          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 15, 20, 30, 50]"
              :total="totalPosts"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </el-col>

      <!-- 右侧：标签选择和活动预告 -->
      <el-col :xs="24" :md="6">
        <!-- 标签选择 -->
        <div class="tags-section">
          <TagFilter
            :tags="allTags"
            :selected-tag="selectedTag"
            title="标签筛选"
            @tag-click="handleTagClick"
          />
        </div>
        
        <!-- 活动轮播 -->
        <div class="activities-section">
          <ActivityCarousel
            :activities="activities"
            title="近期活动与培训"
            @activity-click="handleActivityClick"
          />
        </div>
      </el-col>
    </el-row>

    <!-- 发布活动悬浮按钮（工具Owner和管理员可见） -->
    <el-tooltip 
      v-if="isToolOwner || isAdmin"
      content="发布活动" 
      placement="left"
    >
      <el-button
        class="activity-fab"
        type="success"
        size="large"
        @click="handlePublishActivity"
      >
        <el-icon class="activity-fab__icon"><Plus /></el-icon>
        <span class="activity-fab__label">发布活动</span>
      </el-button>
    </el-tooltip>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, Clock, View, ChatDotRound, Plus } from '@element-plus/icons-vue'
import { ROUTES } from '../router/paths'
import PostHeader from '../components/PostHeader.vue'
import PostList from '../components/PostList.vue'
import TagFilter from '../components/TagFilter.vue'
import ActivityCarousel from '../components/ActivityCarousel.vue'
// API 层 - 支持 Mock/Real API 自动切换
import { getFeaturedPost, getTags, checkOwnerPermission, getPosts, getActivities } from '../api/agent'
import { getCurrentUser } from '../api/user'

const router = useRouter()

// 选中的标签
const selectedTag = ref<string | null>(null)

// 工具Owner权限检查
const isToolOwner = ref(false)
const isAdmin = ref(false) // 是否为管理员
const checkingOwner = ref(false)

// 检查是否为工具Owner或管理员
const checkToolOwnerPermission = async () => {
  checkingOwner.value = true
  try {
    // 获取当前用户信息
    const userResponse = await getCurrentUser()
    isAdmin.value = userResponse.data.roles?.includes('admin') || false
    
    // 检查是否为工具Owner（toolId为-1表示扶摇Agent应用）
    const ownerResponse = await checkOwnerPermission()
    isToolOwner.value = ownerResponse.data.isOwner || false
  } catch (error: unknown) {
    console.error('检查工具Owner权限失败:', error)
    isToolOwner.value = false
    isAdmin.value = false
  } finally {
    checkingOwner.value = false
  }
}

// 发布活动
const handlePublishActivity = () => {
  router.push(`${ROUTES.ACTIVITY_CREATE}?toolId=-1`)
}

// 分页
const currentPage = ref(1)
const pageSize = ref(15)

// 搜索关键词和排序
const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments' | 'likes'>('newest')

// 所有标签（从API获取，这里先使用固定标签，实际应该从getTags API获取）
const allTags = ref<Array<{ name: string; count: number }>>([
  { name: '全部', count: 0 }
])

// 加载标签列表
const loadTags = async () => {
  try {
    const response = await getTags()
    // 构建标签列表，包含"全部"
    const tags: Array<{ name: string; count: number }> = [
      { name: '全部', count: totalPosts.value }
    ]
    response.data.list
      .filter((t) => t.name !== '全部')
      .forEach((tag: { name: string; count?: number }) => {
        tags.push({ name: tag.name, count: tag.count || 0 })
    })
    allTags.value = tags
  } catch (error) {
    console.error('加载标签列表失败:', error)
    // 使用默认标签
    allTags.value = [
      { name: '全部', count: totalPosts.value },
      { name: 'Agent应用', count: 0 },
      { name: '工作流', count: 0 },
      { name: '自动化', count: 0 },
      { name: '智能编排', count: 0 },
      { name: '最佳实践', count: 0 },
      { name: '案例分享', count: 0 },
      { name: '开发指南', count: 0 }
    ]
  }
}

// 近期活动和培训
const activities = ref<Array<{ id: number; type: 'activity' | 'training' | 'workshop'; title: string; desc: string; date: string; location: string; image: string }>>([])


// 加载活动列表
const loadActivities = async () => {
  try {
    const response = await getActivities({
      page: 1,
      pageSize: 10
    })
    activities.value = response.data.list.map((a) => ({
      id: a.id,
      type: (a.type || 'activity') as 'activity' | 'training' | 'workshop',
      title: a.title,
      desc: a.content ? a.content.replace(/<[^>]*>/g, '').substring(0, 100) + '...' : '',
      date: typeof a.date === 'string' ? a.date : new Date(a.date).toLocaleDateString('zh-CN'),
      location: a.location || '',
      image: a.cover || ''
    }))
  } catch (error) {
    console.error('加载活动列表失败:', error)
    activities.value = []
  }
}

// 置顶帖子
const featuredPost = ref<{ id: number; title: string; image?: string; createTime: string; description?: string; tags?: string[]; author?: string; views?: number; comments?: number } | null>(null)


// 加载置顶帖子
const loadFeaturedPost = async () => {
  try {
    const response = await getFeaturedPost()
    if (response.data.post) {
      featuredPost.value = {
        ...response.data.post,
        image: response.data.post.image || response.data.post.cover || '',
        createTime: typeof response.data.post.createTime === 'string' 
          ? response.data.post.createTime 
          : new Date(response.data.post.createTime).toLocaleDateString('zh-CN')
      }
    } else {
      featuredPost.value = null
    }
  } catch (error) {
    console.error('加载置顶帖子失败:', error)
    featuredPost.value = null
  }
}

// 所有帖子
const allPosts = ref<Array<{ id: number; title: string; description: string; author: string; createTime: string; views: number; comments: number; likes: number; tag: string; tags?: string[]; image: string }>>([])

const totalPosts = ref(0)

// 加载帖子列表
const loadPosts = async () => {
  try {
    // 后端 /api/tools/posts 的 sortBy 实现仅支持 newest/hot/comments（见 ToolPostMapper.xml）
    // likes 在后端不会单独处理，这里映射为 hot，保证前端“点赞最多”仍有合理效果。
    const apiSortBy = sortBy.value === 'likes' ? 'hot' : (sortBy.value as 'newest' | 'hot' | 'comments')
    const response = await getPosts({
      tag: selectedTag.value || undefined,
      keyword: searchKeyword.value || undefined,
      sortBy: apiSortBy,
      page: currentPage.value,
      pageSize: pageSize.value
    })
    allPosts.value = response.data.list.map((p) => ({
      id: p.id,
      title: p.title,
      // 后端工具帖子列表返回字段：description/author/cover（见 ToolPostItemVO + ToolPostMapper.xml）
      description: (p as unknown as { description?: string }).description || p.summary || '',
      author: (p as unknown as { author?: string }).author || p.userName || '',
      createTime: typeof p.createTime === 'string' 
        ? p.createTime 
        : new Date(p.createTime).toLocaleDateString('zh-CN'),
      views: p.views,
      comments: p.comments,
      likes: p.likes,
      tag: p.tags?.[0] || '',
      tags: p.tags,
      image: p.cover || ''
    }))
    totalPosts.value = response.data.total
  } catch (error) {
    console.error('加载帖子列表失败:', error)
    allPosts.value = []
    totalPosts.value = 0
  }
}

// 过滤后的帖子（后端已处理搜索、排序、标签过滤，这里直接使用）
const filteredPosts = computed(() => {
  return allPosts.value
})

// 分页后的帖子（后端已分页，直接使用）
const paginatedPosts = computed(() => {
  return filteredPosts.value
})

// 获取标签类型
const getTagType = (tag: string) => {
  const typeMap: Record<string, string> = {
    'Agent应用': 'primary',
    '工作流': 'info',
    '自动化': 'warning',
    '智能编排': 'success',
    '最佳实践': 'success',
    '案例分享': 'info',
    '问题解决': 'warning',
    '开发指南': 'primary'
  }
  return typeMap[tag] || 'info'
}

// 处理标签点击
const handleTagClick = (tagName: string) => {
  if (tagName === '全部') {
    selectedTag.value = null
  } else if (selectedTag.value === tagName) {
    selectedTag.value = null
  } else {
    selectedTag.value = tagName
  }
  currentPage.value = 1
  loadPosts() // 重新加载帖子
}

// 处理搜索
const handleSearch = (keyword: string) => {
  searchKeyword.value = keyword
  currentPage.value = 1
  loadPosts() // 重新加载帖子
}

// 处理排序
const handleSort = (sort: 'newest' | 'hot' | 'comments' | 'likes') => {
  sortBy.value = sort
  currentPage.value = 1
  loadPosts() // 重新加载帖子
}

// 处理分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
  loadPosts() // 重新加载帖子
}

// 处理当前页变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadPosts() // 重新加载帖子
  // 滚动到顶部
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 处理帖子点击
const handlePostClick = (post: { id: number }) => {
  console.log('AgentView: 处理帖子点击', post)
  if (!post || !post.id) {
    console.error('帖子数据无效:', post)
    return
  }
  // 传递当前页面路径，用于帖子详情页返回
  router.push({
    path: `/post/${post.id}`,
    query: { from: '/agent' }
  }).catch((err) => {
    console.error('路由跳转失败:', err)
  })
}

// 处理活动点击
const handleActivityClick = (activity: { id: number }) => {
  // 跳转到活动详情页
  router.push(`/activity/${activity.id}`)
}


// 处理发帖
const handlePostCreate = () => {
  console.log('点击发布帖子，准备跳转到 /post/create')
  router.push(ROUTES.POST_CREATE).catch((err) => {
    console.error('路由跳转失败:', err)
  })
}

// 格式化日期
const formatDate = (date: string | Date) => {
  if (!date) return ''
  const d = new Date(date)
  if (isNaN(d.getTime())) return date.toString()

  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')

  return `${year}-${month}-${day} ${hours}:${minutes}`
}

// 页面加载时检查权限
onMounted(async () => {
  await checkToolOwnerPermission()
  await loadFeaturedPost()
  await loadActivities()
  await loadPosts()
  await loadTags()
})
</script>

<style scoped lang="scss">
.agent-view {
  min-height: 100vh;
  padding: 20px;
  max-width: 1600px;
  margin: 0 auto;
  color: #000;
}

/* 头部操作栏 */
.posts-header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);

  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;
    width: 100%;

    .search-input {
      flex: 1;
      max-width: 300px;
    }
  }
}

/* 置顶帖子 */
.featured-post {
  margin-bottom: 30px;
}

.featured-post-card {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  gap: 24px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  }
}

.featured-post-image {
  flex-shrink: 0;
  width: 400px;
  height: 250px;
  border-radius: 12px;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.featured-post-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.featured-post-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.featured-post-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #000;
  flex: 1;
}

.featured-post-description {
  margin: 0;
  font-size: 15px;
  color: #666;
  line-height: 1.6;
  flex: 1;
}

.featured-post-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.featured-post-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 13px;
  color: #999;

  .meta-item {
    display: flex;
    align-items: center;
    gap: 4px;

    .el-icon {
      font-size: 14px;
    }
  }
}

/* 侧边栏标题 */
.sidebar-title {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 600;
  color: #000;
}

/* 分页 */
.pagination-wrapper {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  padding: 20px 0;
}

/* 标签选择 */
.tags-section {
  margin-bottom: 24px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.tag-item {
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid !important;
  background: transparent !important;
  color: inherit !important;

  &:hover {
    transform: translateY(-2px);
    opacity: 0.8;
  }

  &.tag-item-active {
    font-weight: 600;
  }
}


/* 标签选择 */
.tags-section {
  margin-bottom: 24px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.tag-item {
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    transform: translateY(-2px);
  }
}

/* 帖子列表 */
.posts-section {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.posts-list {
  .post-item {
    display: flex;
    gap: 16px;
    padding: 20px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      background: rgba(0, 0, 0, 0.02);
    }

    &:last-child {
      border-bottom: none;
    }

    .post-image {
      flex-shrink: 0;
      width: 200px;
      height: 120px;
      border-radius: 8px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .post-content {
      flex: 1;
      min-width: 0;

      .post-header {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 8px;

        .post-title {
          margin: 0;
          font-size: 18px;
          font-weight: 600;
          color: #000;
          flex: 1;
        }
      }

      .post-description {
        margin: 0 0 12px 0;
        font-size: 14px;
        color: #666;
        line-height: 1.6;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }

      .post-meta {
        display: flex;
        align-items: center;
        gap: 20px;
        font-size: 13px;
        color: #999;

        .meta-item {
          display: flex;
          align-items: center;
          gap: 4px;

          .el-icon {
            font-size: 14px;
          }
        }
      }
    }
  }
}

.empty-state {
  padding: 60px 20px;
  text-align: center;
}

/* 响应式 */
@media (max-width: 768px) {
  .featured-post-card {
    flex-direction: column;

    .featured-post-image {
      width: 100%;
      height: 200px;
    }
  }

  .post-item {
    flex-direction: column;

    .post-image {
      width: 100%;
      height: 180px;
    }
  }
}

/* 发布活动悬浮按钮 */
.activity-fab {
  position: fixed;
  right: 0; /* 默认只显示一半，所以从右边0开始 */
  bottom: 148px; /* 发帖按钮在72px，按钮高度60px，间距16px，所以是72+60+16=148px */
  z-index: 1301; /* 比发帖按钮稍高，确保在上层 */
  box-shadow: 0 20px 50px rgba(103, 194, 58, 0.55), 0 0 0 8px rgba(103, 194, 58, 0.16);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
  padding: 0 26px;
  height: 60px;
  border-radius: 999px 0 0 999px; /* 左边圆角，右边直角 */
  background: linear-gradient(135deg, #85ce61 0%, #67c23a 50%, #5daf34 100%);
  font-weight: 700;
  letter-spacing: 0.5px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  backdrop-filter: blur(6px);
  transform: translateX(calc(100% - 60px)); /* 默认只显示60px（图标+部分文字） */
  overflow: hidden;
  white-space: nowrap;

  &::after {
    content: '';
    position: absolute;
    inset: -10px;
    border-radius: inherit;
    background: radial-gradient(circle at 50% 50%, rgba(103, 194, 58, 0.35), rgba(103, 194, 58, 0));
    z-index: -1;
    opacity: 0.75;
    filter: blur(8px);
    transition: opacity 0.3s ease, transform 0.3s ease;
  }

  &__icon {
    font-size: 18px;
    flex-shrink: 0;
  }

  &__label {
    font-size: 16px;
    letter-spacing: 0.6px;
    opacity: 0; /* 默认隐藏文字 */
    transition: opacity 0.3s ease;
  }

  &:hover {
    transform: translateX(0); /* 悬浮时完全显示 */
    border-radius: 999px; /* 悬浮时恢复完整圆角 */
    box-shadow: 0 24px 58px rgba(103, 194, 58, 0.65), 0 0 0 10px rgba(103, 194, 58, 0.18);

    &::after {
      opacity: 1;
      transform: scale(1.02);
    }

    .activity-fab__label {
      opacity: 1; /* 悬浮时显示文字 */
    }
  }
}

@media (max-width: 768px) {
  .activity-fab {
    right: 0;
    bottom: 124px; /* 移动端发帖按钮在48px，按钮高度52px，间距24px，所以是48+52+24=124px */
    height: 52px;
    padding: 0 18px;
    transform: translateX(calc(100% - 52px)); /* 移动端默认显示52px */

    &__label {
      font-size: 15px;
    }

    &:hover {
      transform: translateX(0);
    }
  }
}
</style>
