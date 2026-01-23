<template>
  <div class="tools-view">
    <div class="tools-big-buttons-grid">
      <div
        v-for="tool in tools"
        :key="tool.id"
        class="tool-big-button"
        :class="{ 'tool-big-button-active': selectedToolId === tool.id }"
        :data-tool-type="getToolType(tool.name)"
        :style="{ '--tool-category-color': tool.color || '#0066FF' }"
        @click="selectTool(tool.id)"
      >
        <div class="tool-bg-layer"></div>

        <div class="tool-button-icon">
          <div class="icon-bg-layer"></div>

          <img
            v-if="tool.logo"
            :src="tool.logo"
            :alt="tool.name"
            class="tool-logo"
          />
          <div
            v-else
            class="tool-icon-placeholder"
          >
            {{ tool.name[0] }}
          </div>
        </div>
        <div class="tool-button-content">
          <h4 class="tool-button-name">{{ tool.name }}</h4>
        </div>
      </div>

      <div
        class="tool-big-button tool-big-button-no-icon"
        :class="{ 'tool-big-button-active': selectedToolId === 'other' }"
        data-tool-type="other"
        style="--tool-category-color: #6366F1;"
        @click="selectTool('other')"
      >
        <div class="tool-bg-layer"></div>

        <div class="tool-button-content">
          <h4 class="tool-button-name">其他</h4>
        </div>
      </div>
    </div>

    <div class="content-area" v-if="selectedToolId">
      <el-row :gutter="24">
        <el-col :xs="24" :md="selectedToolId === 'other' ? 18 : 24">
          <div class="posts-section">
            <div v-if="selectedToolId === 'other'">
              <PostHeader
                :default-sort="sortBy as 'newest' | 'hot' | 'comments' | 'likes'"
                @search="handleSearch"
                @sort="handleSort"
                @post-click="handlePostCreate"
              />
            </div>

            <div v-else class="post-tabs-header">
              <div class="post-tabs">
                <el-tag
                  :type="activePostTab === 'guide' ? 'primary' : 'info'"
                  :effect="activePostTab === 'guide' ? 'dark' : 'plain'"
                  size="large"
                  class="post-tab-tag"
                  @click="handleTabChange('guide')"
                >
                  使用指导
                </el-tag>
                <el-tag
                  :type="activePostTab === 'excellent' ? 'primary' : 'info'"
                  :effect="activePostTab === 'excellent' ? 'dark' : 'plain'"
                  size="large"
                  class="post-tab-tag"
                  @click="handleTabChange('excellent')"
                >
                  优秀案例
                </el-tag>
              </div>
            </div>

            <PostList
              :posts="paginatedPosts"
              :featured-posts="selectedToolId === 'other' ? featuredPostsArray : []"
              :show-featured-tag="selectedToolId === 'other'"
              :show-tags="selectedToolId === 'other'"
              @post-click="handlePostClick"
            />

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

        <el-col v-if="selectedToolId === 'other'" :xs="24" :md="6">
          <div class="sidebar-section">
            <div class="sidebar-block">
              <TagFilter
                :tags="displayedTags"
                :selected-tag="selectedTag"
                title="标签筛选"
                @tag-click="handleTagClick"
              />
            </div>

            <div class="sidebar-block">
              <div class="section-header-with-reset">
                <h3 class="sidebar-title">部门归类</h3>
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
              <div class="department-list">
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
          </div>
        </el-col>
      </el-row>
    </div>

    <div v-else class="empty-tool-selection">
      <el-empty description="请选择一个工具查看相关内容" />
    </div>

    <el-tooltip
      v-if="(isToolOwner || isAdmin) && selectedToolId && selectedToolId !== 'other' && typeof selectedToolId === 'number'"
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
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Refresh, Plus } from '@element-plus/icons-vue'
import { ROUTES } from '../router/paths'
import { ElMessage } from 'element-plus'
import PostHeader from '../components/PostHeader.vue'
import PostList from '../components/PostList.vue'
import TagFilter from '../components/TagFilter.vue'
import { getTools, checkOwnerPermission, getToolPosts, getFeaturedPost, getToolTags, getToolDepartments } from '../api/tools'
import { getCurrentUser } from '../api/user'
import type { Post } from '../api/types'

const router = useRouter()
const route = useRoute()

// 选中的工具ID
const selectedToolId = ref<number | string | null>(null)

// 工具Owner权限检查
const isToolOwner = ref(false)
const isAdmin = ref(false)
const checkingOwner = ref(false)

// 标签和部门选择
const selectedTag = ref<string | null>(null)
const selectedDepartment = ref<string | null>(null)

// 精华帖子
const featuredPost = ref<Post | null>(null)
const featuredPostsArray = computed(() => {
  return featuredPost.value ? [featuredPost.value] : []
})

// 分页
const currentPage = ref(1)
const pageSize = ref(15)

// 其他工具统计
const toolTags = ref<Array<{ name: string; count: number }>>([])
const toolDepartments = ref<Array<{ id: number; name: string; postCount: number; contributorCount: number }>>([])

const displayedTags = computed(() => {
  const list = toolTags.value || []
  const hasAll = list.some(t => t.name === '全部')
  return hasAll ? list : [{ name: '全部', count: list.reduce((s, t) => s + (t.count || 0), 0) }, ...list]
})

const displayedDepartments = computed(() => toolDepartments.value || [])

interface ToolInfo {
  id: number
  name: string
  logo?: string
  logoType?: string
  desc?: string
  color?: string
}

const tools = ref<ToolInfo[]>([])

const handleConfigUpdate = async () => {
  tools.value = await loadTools()
}

onMounted(async () => {
  tools.value = await loadTools()
  window.addEventListener('adminConfigUpdated', handleConfigUpdate)

  const toolId = route.query.toolId
  if (toolId) {
    const id = Number(toolId)
    if (!isNaN(id) && tools.value.some((t) => t.id === id)) {
      selectedToolId.value = id
      await checkToolOwner(id)
    } else if (toolId === 'other') {
      selectedToolId.value = 'other'
      isToolOwner.value = false
    }
  } else {
      const pathMatch = route.path.match(/\/tools\/([^/?]+)/)
      if (pathMatch) {
        const toolName = pathMatch[1]
        if (toolName) {
          const matchedTool = tools.value.find((t) => {
            const toolNameFromLink = t.name.toLowerCase().replace(/\s+/g, '-')
            return toolNameFromLink === toolName.toLowerCase() ||
                   t.name.toLowerCase() === toolName.toLowerCase()
          })
          if (matchedTool) {
            selectedToolId.value = matchedTool.id
            await checkToolOwner(matchedTool.id)
          } else {
            if (tools.value.length > 0 && tools.value[0]) {
              selectedToolId.value = tools.value[0].id
              await checkToolOwner(tools.value[0].id)
            }
          }
        }
      } else {
        if (tools.value.length > 0 && tools.value[0]) {
          selectedToolId.value = tools.value[0].id
          await checkToolOwner(tools.value[0].id)
        }
      }
    }
})

watch(selectedToolId, async (newToolId) => {
  if (typeof newToolId === 'number') {
    await checkToolOwner(newToolId)
    await loadPosts(newToolId)
    featuredPost.value = null
  } else {
    isToolOwner.value = false
    await loadOtherToolStats()
    await loadPosts(0)
    await loadFeaturedPost()
  }
})

const activePostTab = ref<'guide' | 'excellent'>('guide')

onUnmounted(() => {
  window.removeEventListener('adminConfigUpdated', handleConfigUpdate)
})

const loadTools = async () => {
  const toolsResponse = await getTools()
  return (toolsResponse.data?.list || []).map((item: { id: number; name: string; desc?: string; logo?: string; link?: string; color?: string }) => ({
    id: item.id,
    name: item.name,
    desc: item.desc || '',
    logo: item.logo || '',
    link: item.link || `/tools?toolId=${item.id}`,
    color: item.color || '#409eff'
  }))
}

type LocalPost = Post & { author?: string; description?: string; image?: string; tag?: string; category?: 'guide' | 'excellent' }

const postList = ref<LocalPost[]>([])
const totalPosts = ref(0)

const loadPosts = async (toolId?: number) => {
  try {
    const isOther = toolId === 0 || selectedToolId.value === 'other'
    const response = await getToolPosts({
      toolId,
      category: !isOther && typeof selectedToolId.value === 'number' ? (activePostTab.value as 'guide' | 'excellent') : undefined,
      tag: isOther && selectedTag.value && selectedTag.value !== '全部' ? selectedTag.value : undefined,
      department: isOther ? (selectedDepartment.value || undefined) : undefined,
      keyword: isOther ? (searchKeyword.value || undefined) : undefined,
      sortBy: isOther ? (sortBy.value as 'newest' | 'hot' | 'comments') : undefined,
      page: currentPage.value,
      pageSize: pageSize.value,
    })
    totalPosts.value = response.data.total || 0

    postList.value = (response.data.list || []).map((post: Post): LocalPost => ({
      ...post,
      author: post.userName || (post as unknown as { author?: string }).author || '',
      description: post.summary || (post as unknown as { description?: string }).description || '',
      image: post.cover || (post as unknown as { image?: string }).image || '',
      createTime: typeof post.createTime === 'string' ? post.createTime : new Date(post.createTime).toLocaleDateString('zh-CN'),
      tag: post.tags && post.tags.length > 0 ? post.tags[0] : '',
      category: post.category === 'guide' || post.category === 'excellent' ? post.category : undefined
    }))
  } catch (error: unknown) {
    console.error('加载帖子列表失败:', error)
    ElMessage.error((error as Error).message || '加载帖子列表失败')
    totalPosts.value = 0
    postList.value = []
  }
}

const loadFeaturedPost = async () => {
  try {
    const response = await getFeaturedPost(0)
    if (response.data.post) {
      featuredPost.value = {
        ...response.data.post,
        image: response.data.post.cover || '',
        createTime: typeof response.data.post.createTime === 'string'
          ? response.data.post.createTime
          : new Date(response.data.post.createTime).toLocaleDateString('zh-CN')
      }
    } else {
      featuredPost.value = null
    }
  } catch (error) {
    console.error('加载精华帖子失败:', error)
    featuredPost.value = null
  }
}

const loadOtherToolStats = async () => {
  if (selectedToolId.value !== 'other') return
  try {
    const [tagsRes, deptsRes] = await Promise.all([
      getToolTags(0, selectedDepartment.value || undefined),
      getToolDepartments(0, selectedTag.value && selectedTag.value !== '全部' ? selectedTag.value : undefined),
    ])
    toolTags.value = tagsRes.data.list || []
    const list = deptsRes.data.list || []
    toolDepartments.value = list.map((d, idx) => ({ id: (d as { id?: number }).id ?? idx + 1, ...d }))
  } catch (e) {
    console.error('加载其他工具统计失败:', e)
    toolTags.value = []
    toolDepartments.value = []
  }
}

const getToolType = (toolName: string): string => {
  const name = toolName.toLowerCase()
  if (name.includes('test') || name.includes('测试')) {
    return 'test'
  }
  if (name.includes('code') || name.includes('代码')) {
    return 'code'
  }
  if (name.includes('云') || name.includes('cloud') || name.includes('算力')) {
    return 'cloud'
  }
  if (name.includes('dt') || name.includes('纠错') || name.includes('转换') || name.includes('效率')) {
    return 'efficiency'
  }
  return 'code'
}

const selectTool = async (toolId: number | string) => {
  selectedToolId.value = toolId
  activePostTab.value = 'guide'
  currentPage.value = 1

  if (typeof toolId === 'number') {
    await checkToolOwner(toolId)
    await loadPosts(toolId)
    featuredPost.value = null
  } else {
    isToolOwner.value = false
    await loadOtherToolStats()
    await loadPosts(0)
    await loadFeaturedPost()
  }
}

const checkToolOwner = async (toolId: number) => {
  if (toolId <= 0) {
    isToolOwner.value = false
    isAdmin.value = false
    return
  }
  checkingOwner.value = true
  try {
    const userResponse = await getCurrentUser()
    const user = userResponse.data
    isAdmin.value = user.roles?.includes('admin') || false
    const ownerResponse = await checkOwnerPermission(toolId)
    isToolOwner.value = ownerResponse.data.isOwner || false
  } catch (error: unknown) {
    console.error('检查工具Owner权限失败:', error)
    isToolOwner.value = false
    isAdmin.value = false
  } finally {
    checkingOwner.value = false
  }
}

const handlePublishActivity = () => {
  if (!selectedToolId.value || typeof selectedToolId.value !== 'number') {
    ElMessage.warning('请先选择一个工具')
    return
  }
  router.push(`/activity/create?toolId=${selectedToolId.value}`)
}

const paginatedPosts = computed(() => postList.value)

const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments'>('newest')

const handleTabChange = (tab: 'guide' | 'excellent') => {
  activePostTab.value = tab
  currentPage.value = 1
  if (typeof selectedToolId.value === 'number') {
    loadPosts(selectedToolId.value)
  }
}

const handleSearch = (keyword: string) => {
  searchKeyword.value = keyword
  currentPage.value = 1
  if (selectedToolId.value === 'other') {
    loadPosts(0)
  }
}

const handleSort = (sort: 'newest' | 'hot' | 'comments' | 'likes') => {
  sortBy.value = sort as 'newest' | 'hot' | 'comments'
  currentPage.value = 1
  if (selectedToolId.value === 'other') {
    loadPosts(0)
  }
}

const handleTagClick = (tagName: string) => {
  if (tagName === '全部') {
    selectedTag.value = null
  } else if (selectedTag.value === tagName) {
    selectedTag.value = null
  } else {
    selectedTag.value = tagName
  }
  currentPage.value = 1
  if (selectedToolId.value === 'other') {
    loadOtherToolStats()
    loadPosts(0)
  }
}

const handleDepartmentClick = (departmentName: string) => {
  if (selectedDepartment.value === departmentName) {
    selectedDepartment.value = null
  } else {
    selectedDepartment.value = departmentName
  }
  currentPage.value = 1
  if (selectedToolId.value === 'other') {
    loadOtherToolStats()
    loadPosts(0)
  }
}

const handleResetDepartment = () => {
  selectedDepartment.value = null
  currentPage.value = 1
  if (selectedToolId.value === 'other') {
    loadOtherToolStats()
    loadPosts(0)
  }
}

const handlePostCreate = () => {
  router.push(ROUTES.POST_CREATE)
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
  if (selectedToolId.value === 'other') {
    loadPosts(0)
  } else if (typeof selectedToolId.value === 'number') {
    loadPosts(selectedToolId.value)
  }
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  window.scrollTo({ top: 0, behavior: 'smooth' })
  if (selectedToolId.value === 'other') {
    loadPosts(0)
  } else if (typeof selectedToolId.value === 'number') {
    loadPosts(selectedToolId.value)
  }
}

const handlePostClick = (post: { id: number | string }) => {
  if (!post || !post.id) return
  router.push({
    path: `/post/${post.id}`,
    query: { from: route.fullPath }
  }).catch((err) => {
    console.error('路由跳转失败:', err)
  })
}
</script>

<style scoped lang="scss">
.tools-view {
  min-height: 100vh;
  padding: 20px;
  max-width: 1600px;
  margin: 0 auto;
  color: #000;
}

/* 核心修改：使用 Grid 布局，但不再固定列数。
  使用 minmax(210px, 1fr) 确保每个按钮最小宽度一致，且自动填充行。
  210px 足够容纳图标(48)+间距(12)+文字，同时比原来的宽度窄。
*/
.tools-big-buttons-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(210px, 1fr));
  gap: 12px;
  margin-bottom: 28px;
}

.tool-big-button {
  display: flex;
  align-items: center;
  gap: 12px;

  /* Grid布局下，宽度由 grid-template-columns 控制，这里只需占满轨道 */
  width: 100%;
  padding: 10px 14px;
  height: 80px;
  border-radius: 14px;
  cursor: pointer;

  /* 更深的背景色 */
  background: rgba(255, 255, 255, 1);
  /* 明显的边框 */
  border: 1px solid rgba(0, 0, 0, 0.1);
  /* 阴影 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);

  position: relative;
  overflow: hidden;
  transition: all 0.25s ease;
  z-index: 1;

  /* 无图标按钮样式 */
  &.tool-big-button-no-icon {
    padding: 10px 14px;
    height: 80px;
    gap: 0;
    justify-content: center;
  }

  /* 背景染色层 */
  .tool-bg-layer {
    position: absolute;
    inset: 0;
    z-index: -1;
    background: var(--tool-category-color);
    opacity: 0.05;
    transition: opacity 0.25s ease;
  }

  &:hover {
    transform: translateY(-3px);
    border-color: rgba(0, 0, 0, 0.15);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);

    .tool-bg-layer {
      opacity: 0.1;
    }

    .tool-button-icon {
      transform: scale(1.05);

      .icon-bg-layer {
        opacity: 0.9;
      }

      .tool-logo {
        filter: brightness(0) invert(1);
      }

      .tool-icon-placeholder {
        color: #fff;
      }
    }

    .tool-button-name {
      color: var(--tool-category-color);
    }
  }

  &.tool-big-button-active {
    background: rgba(255, 255, 255, 1);
    border-color: var(--tool-category-color);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);

    .tool-bg-layer {
      opacity: 0.1;
    }

    .tool-button-name {
      color: var(--tool-category-color);
      font-weight: 700;
    }
  }

  .tool-button-icon {
    flex-shrink: 0;
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    overflow: hidden;
    transition: all 0.25s ease;

    .icon-bg-layer {
      position: absolute;
      inset: 0;
      background: var(--tool-category-color);
      opacity: 0.18;
      transition: opacity 0.25s ease;
      z-index: 0;
    }

    .tool-logo {
      width: 32px;
      height: 32px;
      object-fit: contain;
      border-radius: 6px;
      z-index: 1;
      transition: filter 0.25s;
    }

    .tool-icon-placeholder {
      width: 32px;
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
      font-size: 18px;
      color: var(--tool-category-color);
      background: transparent;
      border-radius: 6px;
      z-index: 1;
      transition: color 0.25s;
    }
  }

  .tool-button-content {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .tool-button-name {
      font-size: 14px;
      font-weight: 600;
      color: #1E293B;
      transition: color 0.25s;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }

  /* 无图标按钮的内容样式 */
  &.tool-big-button-no-icon .tool-button-content {
    flex: none;
    justify-content: center;
  }

  &[data-tool-type="test"] { --tool-category-color: #10B981; }
  &[data-tool-type="code"] { --tool-category-color: #3B82F6; }
  &[data-tool-type="cloud"] { --tool-category-color: #6366F1; }
  &[data-tool-type="efficiency"] { --tool-category-color: #F59E0B; }
  &[data-tool-type="other"] { --tool-category-color: #6366F1; }
}

/* 内容区域 */
.content-area {
  margin-top: 20px;
}

/* 帖子区域 */
.posts-section {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.post-tabs-header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.post-tabs {
  display: flex;
  gap: 12px;
}

.post-tab-tag {
  cursor: pointer;
  padding: 8px 20px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 20px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
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
        font-size: 17px;
        color: #999;

        .meta-item {
          display: flex;
          align-items: center;
          gap: 4px;

          .el-icon {
            font-size: 16px;
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

.empty-tool-selection {
  padding: 100px 20px;
  text-align: center;
}

/* 分页 */
.pagination-wrapper {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  padding: 20px 0;
}

/* 侧边栏（其他工具） */
.sidebar-section {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.sidebar-block {
  margin-bottom: 24px;

  &:last-child {
    margin-bottom: 0;
  }
}

.sidebar-title {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 600;
  color: #000;
}

.section-header-with-reset {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;

  .sidebar-title {
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

.department-list {
  display: flex;
  flex-direction: column;
  gap: 12px;

  .department-item {
    padding: 12px;
    border-radius: 8px;
    transition: all 0.2s;
    cursor: pointer;
    border: 1px solid transparent;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);

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
        font-weight: 600;
        color: #000;
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

/* 响应式 */
@media (max-width: 768px) {
  .post-item {
    flex-direction: column;

    .post-image {
      width: 100%;
      height: 180px;
    }
  }

  /* 在移动端也可以保持 auto-fill 逻辑，或者如果需要可以强制更小尺寸 */
  .tools-big-buttons-grid {
    gap: 10px;
  }

  .tool-big-button {
    padding: 10px 14px;
    height: auto;
    min-height: 70px;

    &.tool-big-button-no-icon {
      padding: 10px 14px;
      min-height: 70px;
      height: auto;
    }
  }
}

/* 发布活动悬浮按钮 */
.activity-fab {
  position: fixed;
  right: 0;
  bottom: 148px;
  z-index: 1301;
  box-shadow: 0 20px 50px rgba(103, 194, 58, 0.55), 0 0 0 8px rgba(103, 194, 58, 0.16);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
  padding: 0 26px;
  height: 60px;
  border-radius: 999px 0 0 999px;
  background: linear-gradient(135deg, #85ce61 0%, #67c23a 50%, #5daf34 100%);
  font-weight: 700;
  letter-spacing: 0.5px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  backdrop-filter: blur(6px);
  transform: translateX(calc(100% - 60px));
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
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  &:hover {
    transform: translateX(0);
    border-radius: 999px;
    box-shadow: 0 24px 58px rgba(103, 194, 58, 0.65), 0 0 0 10px rgba(103, 194, 58, 0.18);

    &::after {
      opacity: 1;
      transform: scale(1.02);
    }

    .activity-fab__label {
      opacity: 1;
    }
  }
}

@media (max-width: 768px) {
  .activity-fab {
    right: 0;
    bottom: 124px;
    height: 52px;
    padding: 0 18px;
    transform: translateX(calc(100% - 52px));

    &__label {
      font-size: 15px;
    }

    &:hover {
      transform: translateX(0);
    }
  }
}
</style>
