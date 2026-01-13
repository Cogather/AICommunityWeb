<template>
  <div class="empowerment-view">
    <div class="posts-container">
      <el-row :gutter="24">
        <!-- 左侧：帖子列表 -->
        <el-col :xs="24" :md="18">
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
        <el-col :xs="24" :md="6">
          <div class="sidebar">
            <!-- 标签筛选 -->
            <div class="sidebar-section">
              <TagFilter
                :tags="displayedTags"
                :selected-tag="selectedTag"
                title="标签筛选"
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
const _isAdmin = ref(false)

const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments' | 'likes'>('newest')
const selectedTag = ref<string | null>(null)

// 分页
const currentPage = ref(1)
const pageSize = ref(15)

// 所有标签（包含"全部"选项）
const displayedTags = computed(() => {
  // 获取所有帖子（包括精华帖和普通帖子）
  const allPosts = [...featuredPosts.value, ...posts.value]
  
  // 统计每个标签的数量
  const tagCountMap = new Map<string, number>()
  allPosts.forEach(post => {
    if (post.tags && Array.isArray(post.tags)) {
      post.tags.forEach(tag => {
        tagCountMap.set(tag, (tagCountMap.get(tag) || 0) + 1)
      })
    }
  })
  
  // 构建标签列表，包含"全部"
  const tags: Array<{ name: string; count: number }> = [
    { name: '全部', count: allPosts.length }
  ]
  
  // 添加其他标签
  const tagNames = ['讨论', '提问', '分享', '经验', '工具', '技巧', '案例', '教程', '最佳实践', '问题解决', 'Agent', 'Prompt', '微调']
  tagNames.forEach(tagName => {
    const count = tagCountMap.get(tagName) || 0
    if (count > 0) {
      tags.push({ name: tagName, count })
    }
  })
  
  return tags
})

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
  // 合并精华帖和普通帖子
  let result = [...featuredPosts.value, ...posts.value]

  // 按标签过滤（排除"全部"）
  if (selectedTag.value && selectedTag.value !== '全部') {
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
const _getTagType = (tag: string) => {
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
const _handlePostCreate = () => {
  console.log('点击发布帖子，准备跳转到 /post/create')
  router.push('/post/create').catch((err) => {
    console.error('路由跳转失败:', err)
  })
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
  console.log('EmpowermentView: 处理帖子点击', post)
  if (!post || !post.id) {
    console.error('帖子数据无效:', post)
    return
  }
  router.push(`/post/${post.id}`).catch((err) => {
    console.error('路由跳转失败:', err)
  })
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
