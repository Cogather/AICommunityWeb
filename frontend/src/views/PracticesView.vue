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
                  @click="handlePostClick(post)"
                >
                  <!-- 排名数字 - 冷色系渐变 -->
                  <div class="rank-number" :class="`rank-${index + 1}`">
                    {{ index + 1 }}
                  </div>
                  <div class="hot-post-featured-content">
                    <div class="hot-post-featured-title">{{ post.title }}</div>
                  </div>
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
                  <div class="contributor-info">
                    <div class="contributor-name">{{ contributor.name }}</div>
                    <div class="contributor-department">
                      {{ contributor.department || '—' }}
                    </div>
                  </div>
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
import { getPosts, getContributors, getHotPosts, getTags, getDepartments } from '../api/practices'
import type { Post } from '../api/types'
import commonMethods from '@/utils/common'

const router = useRouter()
const route = useRoute()

const extractAvatarId = (avatar: string | number | undefined): string => {
  if (!avatar) return ''
  const str = String(avatar)
  // 匹配末尾的数字部分
  const match = str.match(/(\d+)$/)
  return match ? match[1] : str
}
const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments' | 'likes'>('newest')
const selectedTag = ref<string | null>(null)
const selectedDepartment = ref<string | null>(null)
const selectedContributor = ref<string | null>(null)

// 首页"AI优秀实践"三个入口实际按标签筛选（但底层数据可能来自 category 字段）
const specialTagToCategory: Record<string, string> = {
  代码生成: 'training',
  脚本生成: 'training-battle',
  问题处理: 'user-exchange',
}

// 分页相关
const currentPage = ref(1)
const pageSize = ref(15)
const totalPosts = ref(0)

// 标签列表及统计（右侧标签筛选）- 完全由接口返回
const tagsFromApi = ref<Array<{ name: string; count: number }>>([])

// 显示的标签 - 完全使用接口返回的数据
const displayedTags = computed(() => {
  // 完全使用后端返回的标签数据，不再硬编码任何标签
  if (tagsFromApi.value.length > 0) {
    return tagsFromApi.value
  }

  // 兜底：如果接口没有返回数据，从本地帖子中统计
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

  // 构建标签列表（不再添加固定的"全部"标签）
  const tags: Array<{ name: string; count: number }> = []

  tagCountMap.forEach((count, name) => {
    tags.push({ name, count })
  })

  // 按数量排序
  return tags.sort((a, b) => b.count - a.count)
})

// 精华帖（置顶）
const featuredPosts = ref<Post[]>([])

// 普通帖子
const posts = ref<Post[]>([])

// 加载帖子数据
const loadPosts = async () => {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchKeyword.value || undefined,
      tag: selectedTag.value || undefined,
      department: selectedDepartment.value || undefined,
      contributor: selectedContributor.value || undefined,
      sortBy: sortBy.value
    }

    const response = await getPosts(params)

    // API 返回 PaginatedData<Post>
    const allPosts = response.data.list

    // 后端分页模式下，featuredPosts 暂不从此处分离（除非有专门接口）
    // 这里假设返回的列表就是当前页需要展示的
    // 为了兼容 PostList 组件的展示，我们将所有帖子放入 posts
    // 如果需要置顶精华帖，建议后端单独提供接口或在第一页返回
    featuredPosts.value = []

    posts.value = allPosts.map((post: Post) => ({
      ...post,
      author: post.author || post.userName || '',
      description: post.summary || '',
      image: post.cover || '',
      createTime: typeof post.createTime === 'string' ? post.createTime : new Date(post.createTime).toLocaleDateString('zh-CN')
    }))

    totalPosts.value = response.data.total
  } catch (error) {
    console.error('加载帖子失败:', error)
  }
}

// 部门排名统计（动态计算）
const displayedDepartments = computed(() => {
  // 优先使用后端接口返回的 departments
  if (departmentsFromApi.value.length > 0) return departmentsFromApi.value

  // 获取所有帖子（包括精华帖和普通帖子）
  const allPosts = [...featuredPosts.value, ...posts.value]

  // 根据当前选择的标签过滤帖子
  let filteredPosts = allPosts
  if (selectedTag.value) {
    const category = specialTagToCategory[selectedTag.value]
    filteredPosts = category
      ? filteredPosts.filter(p => p.category === category)
      : filteredPosts.filter(p => p.tags && p.tags.includes(selectedTag.value!))
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

  // 构建部门列表（只保留出现过的部门）
  const departments = Array.from(deptMap.entries()).map(([name, stats], index) => ({
    id: index + 1,
    name,
    postCount: stats.postCount,
    contributorCount: stats.contributors.size,
  }))

  // 按发帖数排序
  return departments.sort((a, b) => b.postCount - a.postCount)
})

// 热门贡献者
const topContributors = ref<Array<{ id: number; name: string; avatar?: string; postCount?: number; department?: string }>>([])

// 加载热门贡献者
const loadContributors = async () => {
  try {
    const response = await getContributors(5)
    // 处理 avatar
    topContributors.value = response.data.list.map((contributor: { id: number; name: string; avatar?: string; postCount?: number; department?: string }) => ({
      ...contributor,
      avatar: commonMethods.getAvatarUrl(extractAvatarId(contributor.avatar))
    }))
  } catch (error) {
    console.error('加载热门贡献者失败:', error)
  }
}

// 最热帖子（右侧置顶）
const hotPosts = ref<Array<{ id: number | string; title: string; views?: number }>>([])
const loadHotPosts = async () => {
  try {
    const response = await getHotPosts(3)
    hotPosts.value = response.data.list
  } catch (error) {
    console.error('加载最热帖子失败:', error)
  }
}

// 加载标签列表
const loadTags = async () => {
  try {
    const response = await getTags(selectedDepartment.value || undefined)
    tagsFromApi.value = response.data.list || []
  } catch (error) {
    console.error('加载标签列表失败:', error)
  }
}

// 部门排名列表（右侧部门归类）
const departmentsFromApi = ref<Array<{ id: number; name: string; postCount: number; contributorCount: number }>>([])
const loadDepartments = async () => {
  try {
    // 如果选中的标签在特殊标签映射中，则不传 tag 参数
    const tagParam =
      selectedTag.value && !specialTagToCategory[selectedTag.value]
        ? selectedTag.value
        : undefined

    const response = await getDepartments(tagParam)
    departmentsFromApi.value = (response.data.list || []).map((d: { name: string; postCount: number; contributorCount: number }, idx: number) => ({ id: idx + 1, ...d }))
  } catch (error) {
    console.error('加载部门排名失败:', error)
  }
}

// 过滤后的普通帖子（不包含精华帖，精华帖始终显示）
const filteredNormalPosts = computed(() => {
  return posts.value
})

// 用于统计的过滤后帖子（包含精华帖和普通帖子）
const _filteredPosts = computed(() => {
  return posts.value
})

// 分页后的帖子（只分页普通帖子，精华帖始终显示）
const paginatedPosts = computed(() => {
  return posts.value
})

// 最热的3个帖子（按浏览量排序，用于右侧分栏顶部展示）
const topHotPosts = computed(() => {
  // 优先使用后端接口返回的 hot-posts
  if (hotPosts.value.length > 0) return hotPosts.value
  // 兜底：本地计算
  const allPosts = [...featuredPosts.value, ...posts.value]
  return allPosts.sort((a, b) => (b.views || 0) - (a.views || 0)).slice(0, 3)
})



// 处理搜索
const handleSearch = (keyword: string) => {
  searchKeyword.value = keyword
  currentPage.value = 1 // 重置到第一页
  loadPosts()
}

// 处理排序
const handleSort = (sort: 'newest' | 'hot' | 'comments' | 'likes') => {
  sortBy.value = sort
  currentPage.value = 1 // 重置到第一页
  loadPosts()
}

// 处理分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1 // 重置到第一页
  loadPosts()
}

// 处理当前页变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadPosts()
  // 滚动到顶部
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 处理发帖
const handlePostCreate = () => {
  router.push(ROUTES.POST_CREATE).catch((err) => {
    console.error('路由跳转失败:', err)
  })
}

// 处理标签点击
const handleTagClick = (tagName: string) => {
  if (selectedTag.value === tagName) {
    // 再次点击已选中的标签时清除
    selectedTag.value = null
    router.replace({ query: { ...route.query, tag: undefined } })
  } else {
    selectedTag.value = tagName
    router.replace({ query: { ...route.query, tag: tagName } })
  }
  currentPage.value = 1 // 重置到第一页
  loadPosts()
}

// 处理部门点击
const handleDepartmentClick = (departmentName: string) => {
  if (selectedDepartment.value === departmentName) {
    selectedDepartment.value = null
  } else {
    selectedDepartment.value = departmentName
  }
  currentPage.value = 1 // 重置到第一页
  loadPosts()
}

// 重置部门过滤
const handleResetDepartment = () => {
  selectedDepartment.value = null
  currentPage.value = 1
  loadPosts()
}

// 重置标签过滤
const handleResetTag = () => {
  selectedTag.value = null
  router.replace({ query: { ...route.query, tag: undefined } })
  currentPage.value = 1
  loadPosts()
}

// 处理贡献者点击
const handleContributorClick = (contributorName: string) => {
  if (selectedContributor.value === contributorName) {
    selectedContributor.value = null
  } else {
    selectedContributor.value = contributorName
  }
  loadPosts()
}

// 处理帖子点击
const handlePostClick = (post: { id: number | string }) => {
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

// 从 URL 读取标签参数（支持首页跳转：?tag=培训赋能/AI训战/用户交流）
const initTagFromRoute = () => {
  const tag = route.query.tag as string
  selectedTag.value = tag || null
}

// 监听路由变化，更新标签筛选
watch(() => route.query.tag, (newTag) => {
  selectedTag.value = (newTag as string) || null
})

// 部门变化时：重新拉取标签统计（后端支持 department 参数）
watch(selectedDepartment, async () => {
  await loadTags()
})

// 标签变化时：重新拉取部门排名（后端可能按 tag 过滤）
watch(selectedTag, async () => {
  await loadDepartments()
})

// 页面加载时获取数据
onMounted(async () => {
  // 从 URL 读取初始标签
  initTagFromRoute()

  await Promise.all([
    loadPosts(),
    loadContributors(),
    loadHotPosts(),
    loadTags(),
    loadDepartments(),
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
    gap: 14px;
    cursor: pointer;

    /* 放大整体点击区域 */
    padding: 12px 12px;
    border-radius: 10px;
    transition: all 0.2s;
    border: 1px solid transparent;

    &:hover {
      background-color: #f5f5f5;
    }

    &.active {
      background-color: #e6f4ff;
      border-color: #409eff;
    }

    /* 放大头像（Element Plus Avatar） */
    :deep(.el-avatar) {
      width: 52px !important;
      height: 52px !important;
      flex: 0 0 52px;
    }

    .contributor-info {
      /* 关键：同一行显示姓名+部门 */
      display: flex;
      flex-direction: row;
      align-items: center;
      gap: 10px;

      /* 允许右侧文字截断，不把布局撑爆 */
      min-width: 0;
      flex: 1;
    }

    .contributor-name {
      font-size: 16px;          /* 放大 */
      font-weight: 700;
      color: #333;
      line-height: 1.2;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      max-width: 50%;
    }

    .contributor-department {
      font-size: 14px;          /* 放大 */
      color: #606266;
      line-height: 1.2;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;

      /* 让部门优先被截断，姓名更"稳" */
      min-width: 0;
      flex: 1;
      .contributor-department::before {
  content: '·';
  margin: 0 6px 0 0;
  color: #C0C4CC;
}
    }
  }
}


    // 最热帖子区域（置顶显示）- 白蓝色密集神经网络风格
    &.hot-posts-section {
      /* 玻璃拟态材质：悬浮在底层神经网络背景之上 */
      background: rgba(255, 255, 255, 0.8) !important;
      backdrop-filter: blur(12px);
      -webkit-backdrop-filter: blur(12px);

      /* 边框精密化 */
      border: 1px solid rgba(0, 102, 255, 0.1);
      border-radius: 15px;
      padding: 16px;

      .section-header-hot {
        margin-bottom: 12px;
        padding-bottom: 12px;
        border-bottom: 1px solid rgba(0, 102, 255, 0.08);

        .hot-posts-title {
          display: flex;
          align-items: center;
          gap: 6px;
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #1A2B4B; /* 深海蓝，去掉彩色边框感 */
          letter-spacing: 0.5px;

          .hot-icon {
            font-size: 18px;
            color: #0066FF;
          }
        }
      }

      .hot-posts-featured {
        display: flex;
        flex-direction: column;
        gap: 0;

        .hot-post-featured-item {
          position: relative;
          display: flex;
          align-items: center;
          gap: 16px;
          padding: 14px 0;
          cursor: pointer;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          border-bottom: 0.5px solid rgba(0, 102, 255, 0.08);
          overflow: hidden;

          &:last-child {
            border-bottom: none;
          }

          /* 交互优化：悬停时蓝色渐变流光（左到右） */
          &::before {
            content: '';
            position: absolute;
            left: -100%;
            top: 0;
            bottom: 0;
            width: 100%;
            background: linear-gradient(
              90deg,
              transparent 0%,
              rgba(0, 102, 255, 0.08) 50%,
              transparent 100%
            );
            transition: left 0.5s cubic-bezier(0.4, 0, 0.2, 1);
            pointer-events: none;
            z-index: 0;
          }

          &:hover {
            &::before {
              left: 100%; /* 流光从左到右 */
            }

            .rank-number {
              transform: scale(1.1); /* 排名数字略微放大 */
            }

            .hot-post-featured-title {
              color: #0066FF;
            }
          }

          /* 排名数字 - 降噪处理：冷色系渐变 */
          .rank-number {
            flex-shrink: 0;
            width: 32px;
            height: 32px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            font-weight: 700;
            color: #ffffff;
            transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            position: relative;
            z-index: 1;
          }

          /* 排名1-3：深红、中红、浅红 */
          .rank-1 {
            background: linear-gradient(135deg, #DC2626 0%, #991B1B 100%);
            box-shadow: 0 2px 8px rgba(220, 38, 38, 0.3);
          }

          .rank-2 {
            background: linear-gradient(135deg, #EF4444 0%, #DC2626 100%);
            box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
          }

          .rank-3 {
            background: linear-gradient(135deg, #F87171 0%, #EF4444 100%);
            box-shadow: 0 2px 8px rgba(248, 113, 113, 0.3);
          }

          .hot-post-featured-content {
            flex: 1;
            min-width: 0;
            position: relative;
            z-index: 1;

            .hot-post-featured-title {
              font-size: 13px;
              font-weight: 600;
              color: #1A2B4B; /* 深海蓝，去掉彩色边框感 */
              line-height: 1.4;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              line-clamp: 2;
              -webkit-box-orient: vertical;
              overflow: hidden;
              text-overflow: ellipsis;
              transition: color 0.3s ease;
              letter-spacing: 0.2px;
            }
          }
        }
      }
    }
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
      padding-bottom: 10px;

      .hot-posts-title {
        font-size: 14px;

        .hot-icon {
          font-size: 16px;
        }
      }
    }

    .hot-posts-featured {
      .hot-post-featured-item {
        padding: 12px 0;
        gap: 12px;

        .rank-number {
          width: 28px;
          height: 28px;
          font-size: 12px;
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
