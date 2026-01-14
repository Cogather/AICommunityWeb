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
                :total="totalPosts"
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
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ROUTES } from '../router/paths'
import PostHeader from '../components/PostHeader.vue'
import PostList from '../components/PostList.vue'
import TagFilter from '../components/TagFilter.vue'
import { getEmpowermentFeaturedPosts, getEmpowermentPosts, getEmpowermentTags } from '../mock'

const router = useRouter()

// 模拟管理员状态，实际应该从用户信息中获取
const _isAdmin = ref(false)

const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments' | 'likes'>('newest')
const selectedTag = ref<string | null>(null)

// 分页
const currentPage = ref(1)
const pageSize = ref(15)
const totalPosts = ref(0)

// 精华帖（置顶，不参与分页和筛选）
const featuredPosts = ref<any[]>([])

// 普通帖子
const posts = ref<any[]>([])

// 标签列表
const displayedTags = ref<Array<{ name: string; count: number }>>([])

// 加载精华帖子（只加载一次，不随筛选条件变化）
const loadFeaturedPosts = async () => {
  try {
    const response = await getEmpowermentFeaturedPosts()
    featuredPosts.value = response.list.map(post => ({
      ...post,
      image: post.image || post.cover || '',
      createTime: formatDate(post.createTime)
    }))
  } catch (error) {
    console.error('加载精华帖子失败:', error)
  }
}

// 加载普通帖子（随筛选条件变化重新加载）
const loadPosts = async () => {
  try {
    const response = await getEmpowermentPosts({
      tag: selectedTag.value || undefined,
      keyword: searchKeyword.value || undefined,
      sortBy: sortBy.value,
      page: currentPage.value,
      pageSize: pageSize.value
    })
    posts.value = response.list.map(post => ({
      ...post,
      image: post.image || post.cover || '',
      createTime: formatDate(post.createTime)
    }))
    totalPosts.value = response.total
  } catch (error) {
    console.error('加载帖子列表失败:', error)
  }
}

// 加载标签统计
const loadTags = async () => {
  try {
    const response = await getEmpowermentTags()
    displayedTags.value = response.list
  } catch (error) {
    console.error('加载标签失败:', error)
  }
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  try {
    const date = new Date(dateStr)
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  } catch {
    return dateStr
  }
}

// 页面加载时获取数据
onMounted(async () => {
  await Promise.all([
    loadFeaturedPosts(),
    loadPosts(),
    loadTags()
  ])
})

// 分页后的帖子（现在由API完成分页，这里直接返回posts）
const paginatedPosts = computed(() => {
  return posts.value
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

// 处理搜索
const handleSearch = async (keyword: string) => {
  searchKeyword.value = keyword
  currentPage.value = 1 // 重置到第一页
  await loadPosts()
}

// 处理排序
const handleSort = async (sort: 'newest' | 'hot' | 'comments' | 'likes') => {
  sortBy.value = sort
  currentPage.value = 1 // 重置到第一页
  await loadPosts()
}

// 处理发帖
const _handlePostCreate = () => {
  console.log('点击发布帖子，准备跳转到 /post/create')
  router.push(ROUTES.POST_CREATE).catch((err) => {
    console.error('路由跳转失败:', err)
  })
}

// 处理标签点击
const handleTagClick = async (tagName: string) => {
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
  await loadPosts()
}

// 处理分页大小变化
const handleSizeChange = async (val: number) => {
  pageSize.value = val
  currentPage.value = 1 // 重置到第一页
  await loadPosts()
}

// 处理当前页变化
const handleCurrentChange = async (val: number) => {
  currentPage.value = val
  await loadPosts()
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
  // 传递当前页面路径，用于帖子详情页返回
  router.push({
    path: `/post/${post.id}`,
    query: { from: '/empowerment' }
  }).catch((err) => {
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
