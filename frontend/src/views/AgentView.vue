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
              {{ featuredPost.createTime }}
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
      <el-col :xs="24" :md="16">
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
              :total="filteredPosts.length"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </el-col>

      <!-- 右侧：标签选择和活动预告 -->
      <el-col :xs="24" :md="8">
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

    <!-- 发布活动悬浮按钮（工具owner和管理员可见） -->
    <el-tooltip 
      v-if="isAgentOwner || isAdmin"
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
import { User, Clock, View, ChatDotRound, Calendar, Location, Search, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import PostHeader from '../components/PostHeader.vue'
import PostList from '../components/PostList.vue'
import TagFilter from '../components/TagFilter.vue'
import ActivityCarousel from '../components/ActivityCarousel.vue'

const router = useRouter()

// 选中的标签
const selectedTag = ref<string | null>(null)

// 扶摇Agent应用Owner权限检查
const isAgentOwner = ref(false)
const isAdmin = ref(false) // 是否为管理员
const checkingOwner = ref(false)

// 检查是否为扶摇Agent应用Owner或管理员
const checkAgentOwner = async () => {
  checkingOwner.value = true
  try {
    // 模拟API调用：GET /api/tools/-1/owner 和 GET /api/user/current
    // 实际应该调用真实API
    await new Promise(resolve => setTimeout(resolve, 300))
    
    // 模拟数据：从localStorage或用户信息中获取
    // 实际应该从API获取
    const currentUserId = 1 // 当前用户ID（实际应该从登录状态获取）
    
    // 检查是否为管理员
    const savedAdmins = JSON.parse(localStorage.getItem('admin_users_list') || '[]')
    const admin = savedAdmins.find((u: any) => u.id === currentUserId && u.currentRole === 'admin')
    isAdmin.value = !!admin
    
    // 检查是否为扶摇Agent应用Owner
    const savedOwners = JSON.parse(localStorage.getItem('tool_owners') || '[]')
    const isOwner = savedOwners.some((owner: any) => owner.toolId === -1 && owner.userId === currentUserId)
    
    isAgentOwner.value = isOwner
  } catch (error) {
    console.error('检查扶摇Agent应用Owner权限失败:', error)
    isAgentOwner.value = false
    isAdmin.value = false
  } finally {
    checkingOwner.value = false
  }
}

// 发布活动
const handlePublishActivity = () => {
  router.push('/activity/create?toolId=-1')
}

// 分页
const currentPage = ref(1)
const pageSize = ref(15)

// 搜索关键词和排序
const searchKeyword = ref('')
const sortBy = ref('newest')

// 所有标签（包含"全部"选项）
const allTags = computed(() => {
  // 获取所有帖子（包括置顶帖和普通帖子）
  const postsList = featuredPost.value ? [featuredPost.value, ...allPosts.value] : [...allPosts.value]
  
  // 统计每个标签的数量
  const tagCountMap = new Map<string, number>()
  postsList.forEach(post => {
    if (post.tag) {
      tagCountMap.set(post.tag, (tagCountMap.get(post.tag) || 0) + 1)
    }
    if (post.tags && Array.isArray(post.tags)) {
      post.tags.forEach(tag => {
        tagCountMap.set(tag, (tagCountMap.get(tag) || 0) + 1)
      })
    }
  })
  
  // 构建标签列表，包含"全部"
  const tags: Array<{ name: string; count: number }> = [
    { name: '全部', count: postsList.length }
  ]
  
  // 添加其他标签
  const tagNames = ['Agent应用', '工作流', '自动化', '智能编排', '最佳实践', '案例分享', '问题解决', '开发指南']
  tagNames.forEach(tagName => {
    const count = tagCountMap.get(tagName) || 0
    if (count > 0) {
      tags.push({ name: tagName, count })
    }
  })
  
  return tags
})

// 近期活动和培训（从localStorage加载发布的活动）
const activities = computed(() => {
  // 从localStorage加载发布的活动（toolId为-1表示扶摇Agent应用）
  const publishedActivities = JSON.parse(localStorage.getItem('admin_activities') || '[]')
  const publishedForAgent = publishedActivities
    .filter((a: any) => a.toolId === -1)
    .map((a: any) => ({
      id: a.id,
      type: a.type || 'activity',
      title: a.title,
      desc: a.content ? a.content.replace(/<[^>]*>/g, '').substring(0, 100) + '...' : '',
      date: a.date,
      location: '',
      image: a.cover
    }))
  
  // 模拟数据（作为默认数据）
  const mockActivities = [
    {
      id: 1,
      type: 'training' as const,
      title: '扶摇 Agent 开发训练营',
      desc: '系统学习扶摇 Agent 编排引擎的使用方法和实战技巧，从入门到精通。',
      date: '2024年5月10日',
      location: '线上',
      image: 'https://picsum.photos/600/400?random=30'
    },
    {
      id: 2,
      type: 'activity' as const,
      title: '扶摇 Agent 应用创新大赛',
      desc: '展示你的 Agent 应用创意，赢取丰厚奖品和荣誉。',
      date: '2024年5月15日',
      location: '线上',
      image: 'https://picsum.photos/600/400?random=31'
    },
    {
      id: 3,
      type: 'training' as const,
      title: 'Agent 工作流编排高级培训',
      desc: '深入学习 Agent 工作流编排的高级技巧和最佳实践。',
      date: '2024年5月20日',
      location: '北京',
      image: 'https://picsum.photos/600/400?random=32'
    },
    {
      id: 4,
      type: 'activity' as const,
      title: '扶摇 Agent 技术分享会',
      desc: '与行业专家面对面交流，分享 Agent 应用开发经验。',
      date: '2024年5月25日',
      location: '上海',
      image: 'https://picsum.photos/600/400?random=33'
    }
  ]
  
  return [...publishedForAgent, ...mockActivities]
})

// 置顶帖子
const featuredPost = ref({
  id: 0,
  title: '扶摇 Agent 应用开发指南',
  description: '全面介绍如何使用扶摇 Agent 编排引擎开发智能应用，包括基础概念、开发流程、最佳实践和常见问题解答。',
  author: '扶摇团队',
  createTime: '2024年4月15日',
  views: 2500,
  comments: 120,
  tags: ['Agent应用', '最佳实践', '开发指南'],
  image: 'https://picsum.photos/800/400?random=20'
})

// 所有帖子
const allPosts = ref([
  {
    id: 1,
    title: '使用扶摇 Agent 实现智能代码生成',
    description: '分享如何使用扶摇 Agent 编排引擎实现智能代码生成功能，提升开发效率。',
    author: '张工程师',
    createTime: '2024年4月12日',
    views: 890,
    comments: 45,
    likes: 98,
    tag: 'Agent应用',
    image: 'https://picsum.photos/400/300?random=21'
  },
  {
    id: 2,
    title: '扶摇工作流编排实战案例',
    description: '通过实际案例展示如何利用扶摇 Agent 编排复杂的工作流程。',
    author: '李开发者',
    createTime: '2024年4月10日',
    views: 720,
    comments: 32,
    likes: 75,
    tag: '工作流',
    image: 'https://picsum.photos/400/300?random=22'
  },
  {
    id: 3,
    title: 'Agent 自动化测试实践',
    description: '介绍如何使用扶摇 Agent 进行自动化测试，提高测试效率和覆盖率。',
    author: '王测试',
    createTime: '2024年4月8日',
    views: 650,
    comments: 28,
    likes: 62,
    tag: '自动化',
    image: 'https://picsum.photos/400/300?random=23'
  },
  {
    id: 4,
    title: '智能编排最佳实践',
    description: '总结扶摇 Agent 智能编排的最佳实践和注意事项。',
    author: '陈架构师',
    createTime: '2024年4月6日',
    views: 580,
    comments: 25,
    likes: 48,
    tag: '智能编排',
    image: 'https://picsum.photos/400/300?random=24'
  },
  {
    id: 5,
    title: '扶摇 Agent 在企业级应用中的应用',
    description: '分享扶摇 Agent 在企业级应用中的实际应用案例和经验。',
    author: '赵医生',
    createTime: '2024年4月5日',
    views: 520,
    comments: 20,
    likes: 38,
    tag: '案例分享',
    image: 'https://picsum.photos/400/300?random=25'
  }
])

// 过滤后的帖子
const filteredPosts = computed(() => {
  let posts = [...allPosts.value]

  // 按标签过滤（排除"全部"）
  if (selectedTag.value && selectedTag.value !== '全部') {
    posts = posts.filter(post => post.tag === selectedTag.value)
  }

  // 搜索过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    posts = posts.filter(post => 
      post.title.toLowerCase().includes(keyword) ||
      post.author.toLowerCase().includes(keyword) ||
      (post.description && post.description.toLowerCase().includes(keyword))
    )
  }

  // 排序
  if (sortBy.value === 'hot') {
    posts.sort((a, b) => b.views - a.views)
  } else if (sortBy.value === 'comments') {
    posts.sort((a, b) => (b.comments || 0) - (a.comments || 0))
  } else if (sortBy.value === 'likes') {
    posts.sort((a, b) => (b.likes || 0) - (a.likes || 0))
  } else {
    // 按最新排序（这里简化处理，按id倒序）
    posts.sort((a, b) => b.id - a.id)
  }

  return posts
})

// 分页后的帖子
const paginatedPosts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredPosts.value.slice(start, end)
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
    // 点击"全部"时清除标签过滤
    selectedTag.value = null
  } else if (selectedTag.value === tagName) {
    // 再次点击已选中的标签时清除
    selectedTag.value = null
  } else {
    selectedTag.value = tagName
  }
  currentPage.value = 1 // 重置到第一页
}

// 处理搜索
const handleSearch = (keyword: string) => {
  searchKeyword.value = keyword
  currentPage.value = 1 // 重置到第一页
}

// 处理排序
const handleSort = (sort: 'newest' | 'hot' | 'comments' | 'likes') => {
  sortBy.value = sort
  currentPage.value = 1 // 重置到第一页
}

// 处理分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1 // 重置到第一页
}

// 处理当前页变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  // 滚动到顶部
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 处理帖子点击
const handlePostClick = (post: any) => {
  console.log('AgentView: 处理帖子点击', post)
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
  // 跳转到活动详情页
  router.push(`/activity/${activity.id}`)
}


// 处理发帖
const handlePostCreate = () => {
  console.log('点击发布帖子，准备跳转到 /post/create')
  router.push('/post/create').catch((err) => {
    console.error('路由跳转失败:', err)
  })
}

// 页面加载时检查权限
onMounted(() => {
  checkAgentOwner()
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
