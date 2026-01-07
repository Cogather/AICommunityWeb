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
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { User, Clock, View, ChatDotRound, Calendar, Location, Search } from '@element-plus/icons-vue'
import PostHeader from '../components/PostHeader.vue'
import PostList from '../components/PostList.vue'
import TagFilter from '../components/TagFilter.vue'
import ActivityCarousel from '../components/ActivityCarousel.vue'

const router = useRouter()

// 选中的标签
const selectedTag = ref<string | null>(null)

// 分页
const currentPage = ref(1)
const pageSize = ref(15)

// 搜索关键词和排序
const searchKeyword = ref('')
const sortBy = ref('newest')

// 所有标签
const allTags = ref([
  { name: 'Agent应用', count: 150 },
  { name: '工作流', count: 120 },
  { name: '自动化', count: 95 },
  { name: '智能编排', count: 80 },
  { name: '最佳实践', count: 75 },
  { name: '案例分享', count: 60 },
  { name: '问题解决', count: 50 }
])

// 近期活动和培训
const activities = ref([
  {
    id: 1,
    type: 'training',
    title: '扶摇 Agent 开发训练营',
    desc: '系统学习扶摇 Agent 编排引擎的使用方法和实战技巧，从入门到精通。',
    date: '2024年5月10日',
    location: '线上',
    image: 'https://picsum.photos/600/400?random=30'
  },
  {
    id: 2,
    type: 'activity',
    title: '扶摇 Agent 应用创新大赛',
    desc: '展示你的 Agent 应用创意，赢取丰厚奖品和荣誉。',
    date: '2024年5月15日',
    location: '线上',
    image: 'https://picsum.photos/600/400?random=31'
  },
  {
    id: 3,
    type: 'training',
    title: 'Agent 工作流编排高级培训',
    desc: '深入学习 Agent 工作流编排的高级技巧和最佳实践。',
    date: '2024年5月20日',
    location: '北京',
    image: 'https://picsum.photos/600/400?random=32'
  },
  {
    id: 4,
    type: 'activity',
    title: '扶摇 Agent 技术分享会',
    desc: '与行业专家面对面交流，分享 Agent 应用开发经验。',
    date: '2024年5月25日',
    location: '上海',
    image: 'https://picsum.photos/600/400?random=33'
  }
])

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
    tag: '案例分享',
    image: 'https://picsum.photos/400/300?random=25'
  }
])

// 过滤后的帖子
const filteredPosts = computed(() => {
  let posts = [...allPosts.value]

  // 按标签过滤
  if (selectedTag.value) {
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
    posts.sort((a, b) => b.comments - a.comments)
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
  if (selectedTag.value === tagName) {
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
const handleSort = (sort: 'newest' | 'hot' | 'comments') => {
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
  console.log('点击帖子:', post)
  // 可以跳转到帖子详情页
}

// 处理活动点击
const handleActivityClick = (activity: any) => {
  console.log('点击活动:', activity)
  // 可以跳转到活动详情页
}


// 处理发帖
const handlePostCreate = () => {
  console.log('点击发布帖子，准备跳转到 /post/create')
  router.push('/post/create').catch((err) => {
    console.error('路由跳转失败:', err)
  })
}
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
</style>
