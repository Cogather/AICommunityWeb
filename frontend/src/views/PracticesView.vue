<template>
  <div class="practices-view">
    <div class="posts-container">
      <el-row :gutter="24">
        <!-- 左侧：帖子列表 -->
        <el-col :xs="24" :md="18">
          <div class="posts-content">
            <!-- 头部操作栏 -->
            <PostHeader
              :default-sort="sortBy as 'newest' | 'hot' | 'comments' | 'likes'"
              @search="handleSearch"
              @sort="handleSort"
              @post-click="handlePostCreate"
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
                :total="filteredNormalPosts.length"
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
            <!-- 最热帖子（置顶显示） -->
            <div class="sidebar-section hot-posts-section">
              <div class="section-header-hot">
                <h3 class="hot-posts-title">
                  <el-icon class="hot-icon"><Star /></el-icon>
                  最热帖子
                </h3>
              </div>
              <div class="hot-posts-featured">
                <div
                  v-for="(post, index) in topHotPosts"
                  :key="post.id"
                  class="hot-post-featured-item"
                  :class="`rank-${index + 1}`"
                  @click="handlePostClick(post)"
                >
                  <div class="hot-post-featured-rank">
                    <span class="rank-number">{{ index + 1 }}</span>
                    <div class="rank-badge"></div>
                  </div>
                  <div class="hot-post-featured-content">
                    <div class="hot-post-featured-title">{{ post.title }}</div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 分类筛选（培训赋能、AI训战、用户交流） -->
            <div class="sidebar-section">
              <div class="section-header-with-reset">
                <h3>分类筛选</h3>
                <el-button
                  v-if="selectedCategory"
                  text
                  size="small"
                  class="reset-btn"
                  @click="handleResetCategory"
                >
                  <el-icon><Refresh /></el-icon>
                  重置
                </el-button>
              </div>
              <div class="category-filter">
                <div
                  v-for="(config, key) in categoryConfig"
                  :key="key"
                  class="category-item"
                  :class="{ active: selectedCategory === key }"
                  @click="handleCategoryClick(key as string)"
                >
                  <span class="category-icon">{{ config.icon }}</span>
                  <span class="category-name">{{ config.name }}</span>
                </div>
              </div>
            </div>

            <!-- 标签筛选 -->
            <div class="sidebar-section">
              <div class="section-header-with-reset">
                <h3>标签筛选</h3>
                <el-button
                  v-if="selectedTag"
                  text
                  size="small"
                  class="reset-btn"
                  @click="handleResetTag"
                >
                  <el-icon><Refresh /></el-icon>
                  重置
                </el-button>
              </div>
              <TagFilter
                :tags="displayedTags"
                :selected-tag="selectedTag"
                @tag-click="handleTagClick"
              />
            </div>

            <!-- 部门归类 -->
            <div class="sidebar-section">
              <div class="section-header-with-reset">
                <h3>部门归类</h3>
                <el-button
                  v-if="selectedDepartment"
                  text
                  size="small"
                  class="reset-btn"
                  @click="handleResetDepartment"
                >
                  <el-icon><Refresh /></el-icon>
                  重置
                </el-button>
              </div>
              <div class="department-rankings">
                <div
                  v-for="dept in displayedDepartments"
                  :key="dept.id"
                  class="department-item"
                  :class="{ active: selectedDepartment === dept.name }"
                  @click="handleDepartmentClick(dept.name)"
                >
                  <div class="department-info">
                    <div class="department-name">{{ dept.name }}</div>
                    <div class="department-stats">
                      <span class="stat-item">发帖数: {{ dept.postCount }}</span>
                      <span class="stat-item">贡献者: {{ dept.contributorCount }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 热门贡献者 -->
            <div class="sidebar-section">
              <h3>热门贡献者</h3>
              <div class="contributors-list">
                <div
                  v-for="contributor in topContributors"
                  :key="contributor.id"
                  class="contributor-item"
                  :class="{ active: selectedContributor === contributor.name }"
                  @click="handleContributorClick(contributor.name)"
                >
                  <el-avatar :size="40" :src="contributor.avatar" />
                  <span class="contributor-name">{{ contributor.name }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Refresh, Star } from '@element-plus/icons-vue'
import { ROUTES } from '../router/paths'
import PostHeader from '../components/PostHeader.vue'
import PostList from '../components/PostList.vue'
import TagFilter from '../components/TagFilter.vue'
// API 层 - 支持 Mock/Real API 自动切换
import { getPosts, getContributors } from '../api/practices'
import type { Post } from '../api/types'

const router = useRouter()
const route = useRoute()

const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments' | 'likes'>('newest')
const selectedTag = ref<string | null>(null)
const selectedDepartment = ref<string | null>(null)
const selectedContributor = ref<string | null>(null)

// 分类筛选（培训赋能、AI训战、用户交流）
const selectedCategory = ref<string | null>(null)

// 分类配置
const categoryConfig = {
  training: { name: '培训赋能', icon: '📚' },
  'training-battle': { name: 'AI训战', icon: '⚔️' },
  'user-exchange': { name: '用户交流', icon: '💬' }
} as const

// 分页相关
const currentPage = ref(1)
const pageSize = ref(15)

// 所有标签（包含"全部"选项）
const allTags = computed(() => {
  // 获取所有帖子（包括精华帖和普通帖子）
  const allPosts = [...featuredPosts.value, ...posts.value]

  // 根据当前选择的部门过滤帖子
  let filteredPosts = allPosts
  if (selectedDepartment.value) {
    filteredPosts = filteredPosts.filter(post => post.department === selectedDepartment.value)
  }

  // 统计每个标签的数量
  const tagCountMap = new Map<string, number>()
  filteredPosts.forEach(post => {
    if (post.tags && Array.isArray(post.tags)) {
      post.tags.forEach(tag => {
        tagCountMap.set(tag, (tagCountMap.get(tag) || 0) + 1)
      })
    }
  })

  // 构建标签列表，包含"全部"
  const tags: Array<{ name: string; count: number }> = [
    { name: '全部', count: filteredPosts.length }
  ]

  // 添加其他标签
  const tagNames = ['自然语言处理', '计算机视觉', '深度学习', 'AI伦理', '机器学习', '机器人', '数据科学', '生成式AI', 'PyTorch', 'TensorFlow', '项目', 'AI应用', '效率', '自动化', '实践', '已解决', '部署', '活动', 'AI大会']
  tagNames.forEach(tagName => {
    const count = tagCountMap.get(tagName) || 0
    if (count > 0 || !selectedDepartment.value) {
      tags.push({ name: tagName, count })
    }
  })

  return tags
})

// 显示的标签（根据部门过滤后）
const displayedTags = computed(() => {
  return allTags.value
})

// 精华帖（置顶）
const featuredPosts = ref<Post[]>([])

// 普通帖子
const posts = ref<Post[]>([])

// 加载帖子数据
const loadPosts = async () => {
  try {
    const response = await getPosts({
      page: 1,
      pageSize: 100 // 获取足够多的帖子用于前端过滤
    })
    // API 返回 PaginatedData<Post>，需要手动分离精华帖子
    const allPosts = response.data.list
    featuredPosts.value = allPosts.filter((p: Post) => p.featured).map((post: Post) => ({
      ...post,
      author: post.authorName || '',
      description: post.summary || '',
      image: post.cover || '',
      createTime: typeof post.createTime === 'string' ? post.createTime : new Date(post.createTime).toLocaleDateString('zh-CN')
    }))
    posts.value = allPosts.filter((p: Post) => !p.featured).map((post: Post) => ({
      ...post,
      author: post.authorName || '',
      description: post.summary || '',
      image: post.cover || '',
      createTime: typeof post.createTime === 'string' ? post.createTime : new Date(post.createTime).toLocaleDateString('zh-CN')
    }))
  } catch (error) {
    console.error('加载帖子失败:', error)
  }
}

// 部门排名统计（动态计算）
const displayedDepartments = computed(() => {
  // 获取所有帖子（包括精华帖和普通帖子）
  const allPosts = [...featuredPosts.value, ...posts.value]

  // 根据当前选择的标签过滤帖子
  let filteredPosts = allPosts
  if (selectedTag.value && selectedTag.value !== '全部') {
    filteredPosts = filteredPosts.filter(post =>
      post.tags && post.tags.includes(selectedTag.value!)
    )
  }

  // 统计每个部门的发帖数和贡献者
  const deptMap = new Map<string, { postCount: number; contributors: Set<string> }>()

  filteredPosts.forEach(post => {
    if (post.department) {
      if (!deptMap.has(post.department)) {
        deptMap.set(post.department, { postCount: 0, contributors: new Set() })
      }
      const dept = deptMap.get(post.department)!
      dept.postCount++
      if (post.author) {
        dept.contributors.add(post.author)
      }
    }
  })

  // 获取所有部门名称（从所有帖子中提取）
  const allDepts = new Set<string>()
  allPosts.forEach(post => {
    if (post.department) {
      allDepts.add(post.department)
    }
  })

  // 构建部门列表
  const departments = Array.from(allDepts).map((name, index) => {
    const stats = deptMap.get(name) || { postCount: 0, contributors: new Set() }
    return {
      id: index + 1,
      name,
      postCount: stats.postCount,
      contributorCount: stats.contributors.size
    }
  })

  // 按发帖数排序
  return departments.sort((a, b) => b.postCount - a.postCount)
})

// 热门贡献者
const topContributors = ref<Array<{ id: number; name: string; avatar?: string; postCount?: number; department?: string }>>([])

// 加载热门贡献者
const loadContributors = async () => {
  try {
    const response = await getContributors(5)
    topContributors.value = response.data.list
  } catch (error) {
    console.error('加载热门贡献者失败:', error)
  }
}

// 过滤后的普通帖子（不包含精华帖，精华帖始终显示）
const filteredNormalPosts = computed(() => {
  // 只过滤普通帖子
  let result = [...posts.value]

  // 按分类过滤（培训赋能、AI训战、用户交流）
  if (selectedCategory.value) {
    result = result.filter(post => post.category === selectedCategory.value)
  }

  // 按标签过滤（排除"全部"）
  if (selectedTag.value && selectedTag.value !== '全部') {
    result = result.filter(post =>
      post.tags && post.tags.includes(selectedTag.value!)
    )
  }

  // 按部门过滤
  if (selectedDepartment.value) {
    result = result.filter(post => post.department === selectedDepartment.value)
  }

  // 按贡献者过滤
  if (selectedContributor.value) {
    result = result.filter(post => post.author === selectedContributor.value)
  }

  // 搜索过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(post =>
      post.title.toLowerCase().includes(keyword) ||
      (post.author && post.author.toLowerCase().includes(keyword)) ||
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

// 用于统计的过滤后帖子（包含精华帖和普通帖子）
const _filteredPosts = computed(() => {
  return [...featuredPosts.value, ...filteredNormalPosts.value]
})

// 分页后的帖子（只分页普通帖子，精华帖始终显示）
const paginatedPosts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredNormalPosts.value.slice(start, end)
})

// 最热的3个帖子（按浏览量排序，用于右侧分栏顶部展示）
const topHotPosts = computed(() => {
  // 合并所有帖子（包括精华帖和普通帖子）
  const allPosts = [...featuredPosts.value, ...posts.value]
  // 按浏览量降序排序，取前3个
  return allPosts
    .sort((a, b) => (b.views || 0) - (a.views || 0))
    .slice(0, 3)
})



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

// 处理发帖
const handlePostCreate = () => {
  console.log('点击发布帖子，准备跳转到 /post/create')
  router.push(ROUTES.POST_CREATE).catch((err) => {
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

// 处理部门点击
const handleDepartmentClick = (departmentName: string) => {
  if (selectedDepartment.value === departmentName) {
    selectedDepartment.value = null
  } else {
    selectedDepartment.value = departmentName
  }
  currentPage.value = 1 // 重置到第一页
}

// 重置部门过滤
const handleResetDepartment = () => {
  selectedDepartment.value = null
  currentPage.value = 1
}

// 重置标签过滤
const handleResetTag = () => {
  selectedTag.value = null
  currentPage.value = 1
}

// 处理贡献者点击
const handleContributorClick = (contributorName: string) => {
  if (selectedContributor.value === contributorName) {
    selectedContributor.value = null
  } else {
    selectedContributor.value = contributorName
  }
}

// 处理帖子点击
const handlePostClick = (post: { id: number }) => {
  console.log('PracticesView: 处理帖子点击', post)
  if (!post || !post.id) {
    console.error('帖子数据无效:', post)
    return
  }
  // 传递当前页面路径，用于帖子详情页返回
  router.push({
    path: `/post/${post.id}`,
    query: { from: '/practices' }
  }).catch((err) => {
    console.error('路由跳转失败:', err)
  })
}

// 从 URL 读取分类参数
const initCategoryFromRoute = () => {
  const category = route.query.category as string
  if (category && Object.keys(categoryConfig).includes(category)) {
    selectedCategory.value = category
  }
}

// 监听路由变化，更新分类筛选
watch(() => route.query.category, (newCategory) => {
  if (newCategory && Object.keys(categoryConfig).includes(newCategory as string)) {
    selectedCategory.value = newCategory as string
  } else if (!newCategory) {
    selectedCategory.value = null
  }
})

// 处理分类点击
const handleCategoryClick = (category: string) => {
  if (selectedCategory.value === category) {
    selectedCategory.value = null
    // 清除 URL 中的 category 参数
    router.replace({ query: { ...route.query, category: undefined } })
  } else {
    selectedCategory.value = category
    // 更新 URL 中的 category 参数
    router.replace({ query: { ...route.query, category } })
  }
  currentPage.value = 1
}

// 重置分类筛选
const handleResetCategory = () => {
  selectedCategory.value = null
  router.replace({ query: { ...route.query, category: undefined } })
  currentPage.value = 1
}

// 页面加载时获取数据
onMounted(async () => {
  // 从 URL 读取初始分类
  initCategoryFromRoute()

  await Promise.all([
    loadPosts(),
    loadContributors()
  ])
})
</script>

<style scoped lang="scss">
.practices-view {
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

    .section-header-with-reset {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h3 {
        margin: 0;
      }

      .reset-btn {
        color: #909399;
        font-size: 12px;

        &:hover {
          color: #409eff;
        }
      }
    }

    .category-filter {
      display: flex;
      flex-direction: column;
      gap: 8px;

      .category-item {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 12px 16px;
        border-radius: 8px;
        cursor: pointer;
        background: rgba(255, 255, 255, 0.8);
        border: 1px solid rgba(0, 0, 0, 0.05);
        transition: all 0.2s ease;

        &:hover {
          background: rgba(64, 158, 255, 0.08);
          border-color: rgba(64, 158, 255, 0.2);
        }

        &.active {
          background: rgba(64, 158, 255, 0.15);
          border-color: #409eff;

          .category-name {
            color: #409eff;
            font-weight: 600;
          }
        }

        .category-icon {
          font-size: 18px;
        }

        .category-name {
          font-size: 14px;
          color: #333;
        }
      }
    }

    .department-rankings {
      display: flex;
      flex-direction: column;
      gap: 0;

      .department-item {
        padding: 12px;
        border-radius: 8px;
        transition: all 0.2s;
        cursor: pointer;
        border: 1px solid transparent;
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
        margin-bottom: 0;
        position: relative;

        &:last-child {
          border-bottom: none;
        }

        &:hover {
          background: rgba(0, 0, 0, 0.02);
        }

        &.active {
          background: rgba(64, 158, 255, 0.1);
          border-color: #409eff;
        }

        .department-info {
          .department-name {
            font-size: 15px;
            font-weight: 700;
            color: #333;
            margin-bottom: 4px;
          }

          .department-stats {
            display: flex;
            gap: 12px;
            font-size: 12px;
            color: #999;

            .stat-item {
              display: flex;
              align-items: center;
            }
          }
        }
      }
    }

    .contributors-list {
      display: flex;
      flex-direction: column;
      gap: 12px;

      .contributor-item {
        display: flex;
        align-items: center;
        gap: 12px;
        cursor: pointer;
        padding: 8px;
        border-radius: 8px;
        transition: all 0.2s;
        border: 1px solid transparent;

        &:hover {
          background-color: #f5f5f5;
        }

        &.active {
          background-color: #e6f4ff;
          border-color: #409eff;
        }

        .contributor-name {
          font-size: 14px;
          color: #333;
        }
      }
    }

    // 最热帖子区域（置顶显示）
    &.hot-posts-section {
      background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 255, 255, 0.7) 100%);
      border: 2px solid rgba(64, 158, 255, 0.2);
      box-shadow: 0 4px 20px rgba(64, 158, 255, 0.1);
      padding: 16px;

      .section-header-hot {
        margin-bottom: 12px;

        .hot-posts-title {
          display: flex;
          align-items: center;
          gap: 6px;
          margin: 0;
          font-size: 16px;
          font-weight: 700;
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 50%, #4facfe 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;

          .hot-icon {
            font-size: 18px;
            color: #f5576c;
            animation: pulse 2s ease infinite;
          }
        }
      }

      .hot-posts-featured {
        display: flex;
        flex-direction: column;
        gap: 10px;

        .hot-post-featured-item {
          position: relative;
          display: flex;
          align-items: flex-start;
          gap: 10px;
          padding: 10px;
          border-radius: 8px;
          cursor: pointer;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          background: rgba(255, 255, 255, 0.8);
          border: 2px solid transparent;
          overflow: hidden;

          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, transparent, var(--rank-color, #667eea), transparent);
            opacity: 0;
            transition: opacity 0.3s;
          }

          &:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            border-color: var(--rank-color, #667eea);

            &::before {
              opacity: 1;
            }
          }

          &.rank-1 {
            --rank-color: #f5576c;
            background: linear-gradient(135deg, rgba(245, 87, 108, 0.08) 0%, rgba(255, 255, 255, 0.9) 100%);
            border-color: rgba(245, 87, 108, 0.3);

            .hot-post-featured-rank {
              .rank-badge {
                background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                box-shadow: 0 2px 8px rgba(245, 87, 108, 0.3);
              }
            }
          }

          &.rank-2 {
            --rank-color: #4facfe;
            background: linear-gradient(135deg, rgba(79, 172, 254, 0.08) 0%, rgba(255, 255, 255, 0.9) 100%);
            border-color: rgba(79, 172, 254, 0.3);

            .hot-post-featured-rank {
              .rank-badge {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                box-shadow: 0 2px 8px rgba(79, 172, 254, 0.3);
              }
            }
          }

          &.rank-3 {
            --rank-color: #43e97b;
            background: linear-gradient(135deg, rgba(67, 233, 123, 0.08) 0%, rgba(255, 255, 255, 0.9) 100%);
            border-color: rgba(67, 233, 123, 0.3);

            .hot-post-featured-rank {
              .rank-badge {
                background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
                box-shadow: 0 2px 8px rgba(67, 233, 123, 0.3);
              }
            }
          }

          .hot-post-featured-rank {
            position: relative;
            flex-shrink: 0;
            width: 32px;
            height: 32px;
            display: flex;
            align-items: center;
            justify-content: center;

            .rank-number {
              position: relative;
              z-index: 2;
              font-size: 14px;
              font-weight: 800;
              color: #fff;
              text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
            }

            .rank-badge {
              position: absolute;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
              border-radius: 50%;
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            }
          }

          .hot-post-featured-content {
            flex: 1;
            min-width: 0;
            display: flex;
            align-items: center;

            .hot-post-featured-title {
              font-size: 13px;
              font-weight: 600;
              color: #333;
              line-height: 1.4;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              line-clamp: 2;
              -webkit-box-orient: vertical;
              overflow: hidden;
              text-overflow: ellipsis;
            }
          }
        }
      }
    }
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}

@media (max-width: 768px) {
  .posts-header {
    flex-direction: column;
    gap: 16px;

    .header-right {
      flex-wrap: wrap;
      width: 100%;

      .search-input {
        width: 100%;
      }
    }
  }

  .sidebar-section.hot-posts-section {
    padding: 12px;

    .section-header-hot {
      margin-bottom: 10px;

      .hot-posts-title {
        font-size: 14px;

        .hot-icon {
          font-size: 16px;
        }
      }
    }

    .hot-posts-featured {
      gap: 8px;

      .hot-post-featured-item {
        padding: 8px;
        gap: 8px;

        .hot-post-featured-rank {
          width: 28px;
          height: 28px;

          .rank-number {
            font-size: 12px;
          }
        }

        .hot-post-featured-content {
          .hot-post-featured-title {
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>
