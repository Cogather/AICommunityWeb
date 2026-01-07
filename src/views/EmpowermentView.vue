<template>
  <div class="empowerment-view">
    <div class="posts-container">
      <el-row :gutter="24">
        <!-- 左侧：帖子列表 -->
        <el-col :xs="24" :md="16">
          <div class="posts-content">
            <!-- 头部操作栏 -->
            <PostHeader
              :default-sort="sortBy"
              @search="handleSearch"
              @sort="handleSort"
            />

            <!-- 帖子列表 -->
            <PostList
              :posts="paginatedPosts"
              :featured-posts="featuredPosts"
              :show-featured-tag="true"
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

        <!-- 右侧：标签栏 -->
        <el-col :xs="24" :md="8">
          <div class="sidebar">
            <!-- 所有标签 -->
            <div class="sidebar-section">
              <TagFilter
                :tags="allTags"
                :selected-tag="selectedTag"
                title="所有标签"
                @tag-click="handleTagClick"
              />
            </div>

            <!-- 精选合集 -->
            <div class="sidebar-section">
              <h3>精选合集</h3>
              <div class="featured-collection">
                <img src="https://picsum.photos/300/200?random=100" alt="精选合集" />
                <h4>顶级AI研究论文</h4>
                <el-button type="primary" size="small">最佳答案</el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import PostHeader from '../components/PostHeader.vue'
import PostList from '../components/PostList.vue'
import TagFilter from '../components/TagFilter.vue'

const router = useRouter()

// 模拟管理员状态，实际应该从用户信息中获取
const isAdmin = ref(false)

const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments' | 'likes'>('newest')
const selectedTag = ref<string | null>(null)

// 分页
const currentPage = ref(1)
const pageSize = ref(15)

// 所有标签
const allTags = ref([
  { name: '讨论', count: 150 },
  { name: '提问', count: 120 },
  { name: '分享', count: 95 },
  { name: '经验', count: 80 },
  { name: '工具', count: 75 },
  { name: '技巧', count: 60 },
  { name: '案例', count: 50 },
  { name: '教程', count: 45 },
  { name: '最佳实践', count: 30 },
  { name: '问题解决', count: 25 }
])

// 精华帖（置顶）
const featuredPosts = ref([
  {
    id: 1,
    title: '如何高效使用Agent提升开发效率',
    description: '分享使用Agent工具在开发过程中的最佳实践和技巧。',
    author: '张工程师',
    createTime: '2024年4月10日',
    views: 1250,
    comments: 45,
    likes: 128,
    tags: ['讨论', 'Agent'],
    image: 'https://picsum.photos/800/400?random=10',
    featured: true
  }
])

// 普通帖子
const posts = ref([
  {
    id: 2,
    title: 'Prompt工程的最佳实践分享',
    description: '如何编写高质量的Prompt，提升AI模型输出效果。',
    author: '李开发者',
    createTime: '2024年4月',
    views: 890,
    comments: 32,
    likes: 75,
    tags: ['分享', 'Prompt'],
    image: 'https://picsum.photos/400/300?random=11'
  },
  {
    id: 3,
    title: '大模型微调 vs 提示工程的选择',
    description: '讨论在不同场景下应该选择微调还是提示工程。',
    author: '王测试',
    createTime: '60分钟前',
    views: 650,
    comments: 18,
    likes: 42,
    tags: ['讨论', '微调'],
    image: 'https://picsum.photos/400/300?random=12'
  },
  {
    id: 4,
    title: 'AI工具链的构建与优化',
    description: '分享如何构建高效的AI工具链，提升团队协作效率。',
    author: '赵医生',
    createTime: '2024年4月20日',
    views: 520,
    comments: 15,
    likes: 28,
    tags: ['工具', '经验'],
    image: 'https://picsum.photos/400/300?random=13'
  },
  {
    id: 5,
    title: '如何解决Agent执行中的常见问题',
    description: '总结Agent使用过程中遇到的问题及解决方案。',
    author: '陈架构师',
    createTime: '2024年4月20日',
    views: 720,
    comments: 28,
    likes: 65,
    tags: ['问题解决', 'Agent'],
    image: 'https://picsum.photos/400/300?random=14'
  },
  {
    id: 6,
    title: 'Prompt模板库分享',
    description: '分享常用的Prompt模板，提高工作效率。',
    author: '刘设计师',
    createTime: '2024年4月19日',
    views: 450,
    comments: 12,
    likes: 19,
    tags: ['分享', 'Prompt'],
    image: 'https://picsum.photos/400/300?random=15'
  }
])

// 过滤后的帖子
const filteredPosts = computed(() => {
  let result = [...posts.value]

  // 按标签过滤
  if (selectedTag.value) {
    result = result.filter(post => 
      post.tags && post.tags.includes(selectedTag.value!)
    )
  }

  // 搜索过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(post => 
      post.title.toLowerCase().includes(keyword) ||
      post.author.toLowerCase().includes(keyword) ||
      (post.description && post.description.toLowerCase().includes(keyword))
    )
  }

  // 排序
  if (sortBy.value === 'hot') {
    result.sort((a, b) => b.views - a.views)
  } else if (sortBy.value === 'comments') {
    result.sort((a, b) => (b.comments || 0) - (a.comments || 0))
  } else if (sortBy.value === 'likes') {
    result.sort((a, b) => (b.likes || 0) - (a.likes || 0))
  } else {
    // 按时间排序（这里简化处理）
    result.sort((a, b) => b.id - a.id)
  }

  return result
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
    '精华': 'success',
    '讨论': 'info',
    '提问': 'warning',
    '分享': 'success',
    '经验': 'info',
    '工具': 'primary',
    '技巧': 'warning',
    '案例': 'success',
    '教程': 'info',
    '最佳实践': 'success',
    '问题解决': 'success',
    'Agent': 'primary',
    'Prompt': 'info',
    '微调': 'warning'
  }
  return typeMap[tag] || 'info'
}

// 处理标签切换
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

// 处理发帖
const handlePostCreate = () => {
  console.log('点击发布帖子，准备跳转到 /post/create')
  router.push('/post/create').catch((err) => {
    console.error('路由跳转失败:', err)
  })
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
  router.push(`/post/${post.id}`)
  console.log('点击帖子:', post)
}
</script>

<style scoped lang="scss">
.empowerment-view {
  min-height: 100vh;
  padding: 20px;
}

.posts-container {
  max-width: 1600px;
  margin: 0 auto;
}

/* 分页 */
.pagination-wrapper {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  padding: 20px 0;
}

.posts-content {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  min-height: 600px;
  color: #333;
  border: 1px solid rgba(0, 0, 0, 0.1);
}


.sidebar {
  .sidebar-section {
    background: rgba(255, 255, 255, 0.6);
    backdrop-filter: blur(10px);
    border-radius: 16px;
    padding: 20px;
    margin-bottom: 20px;
    border: 1px solid rgba(0, 0, 0, 0.1);

    h3 {
      margin: 0 0 16px 0;
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }

    .tags-list {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;

    }

    .featured-collection {
      img {
        width: 100%;
        height: 150px;
        object-fit: cover;
        border-radius: 8px;
        margin-bottom: 12px;
      }

      h4 {
        margin: 0 0 12px 0;
        font-size: 16px;
        font-weight: 600;
        color: #333;
      }
    }
  }
}

@media (max-width: 768px) {
  .posts-header {
    flex-direction: column;
    gap: 16px;

    .header-left {
      flex-wrap: wrap;
      width: 100%;

      .search-input {
        width: 100%;
      }
    }
  }
}
</style>
