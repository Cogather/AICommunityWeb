<template>
  <div class="tools-view">
    <!-- 工具按钮组 -->
    <div class="tools-buttons">
      <div
        v-for="tool in tools"
        :key="tool.id"
        :class="['tool-card-btn', { 'tool-card-btn-active': selectedToolId === tool.id }]"
        :style="{ '--tool-color': tool.color || '#409eff' }"
        @click="selectTool(tool.id)"
      >
        <span class="tool-card-name">{{ tool.name }}</span>
      </div>

      <!-- 其他工具按钮 -->
      <div
        :class="['tool-card-btn', 'tool-card-btn-other', { 'tool-card-btn-active': selectedToolId === 'other' }]"
        style="--tool-color: #8b95a5;"
        @click="selectTool('other')"
      >
        <span class="tool-card-name">其他工具</span>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area" v-if="selectedToolId">
      <el-row :gutter="24">
        <!-- 左侧：帖子列表 -->
        <el-col :xs="24" :md="18">
          <div class="posts-section">
            <!-- 其他工具：搜索框和排序 -->
            <div v-if="selectedToolId === 'other'">
              <PostHeader
                :default-sort="sortBy as 'newest' | 'hot' | 'comments' | 'likes'"
                @search="handleSearch"
                @sort="handleSort"
                @post-click="handlePostCreate"
              />
            </div>

            <!-- 普通工具：帖子分类标签 -->
            <div v-else class="post-tabs-header">
              <div class="post-tabs">
                <el-tag
                  :type="activePostTab === 'guide' ? 'primary' : 'info'"
                  :effect="activePostTab === 'guide' ? 'dark' : 'plain'"
                  size="large"
                  class="post-tab-tag"
                  @click="handleTabChange('guide')"
                >
                  操作指导
                </el-tag>
                <el-tag
                  :type="activePostTab === 'excellent' ? 'primary' : 'info'"
                  :effect="activePostTab === 'excellent' ? 'dark' : 'plain'"
                  size="large"
                  class="post-tab-tag"
                  @click="handleTabChange('excellent')"
                >
                  优秀使用
                </el-tag>
              </div>
            </div>

            <!-- 帖子列表 -->
            <PostList
              :posts="paginatedPosts"
              :featured-posts="selectedToolId === 'other' ? featuredPostsArray : []"
              :show-featured-tag="selectedToolId === 'other'"
              :show-tags="selectedToolId === 'other'"
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

        <!-- 右侧：赋能/培训活动宣传 或 其他工具的标签/部门分类 -->
        <el-col :xs="24" :md="6">
          <!-- 其他工具：标签和部门分类 -->
          <div v-if="selectedToolId === 'other'" class="sidebar-section">
            <!-- 标签筛选 -->
            <div class="sidebar-block">
              <TagFilter
                :tags="displayedTags"
                :selected-tag="selectedTag"
                title="标签筛选"
                @tag-click="handleTagClick"
              />
            </div>

            <!-- 部门归类 -->
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

          <!-- 普通工具：活动宣传 -->
          <div v-else class="activities-section">
            <ActivityCarousel
              :activities="currentToolActivities"
              title="近期活动与培训"
              @activity-click="handleActivityClick"
            />
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 未选择工具时的提示 -->
    <div v-else class="empty-tool-selection">
      <el-empty description="请选择一个工具查看相关内容" />
    </div>

    <!-- 发布活动悬浮按钮（工具owner和管理员可见） -->
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
import { ElMessage } from 'element-plus'
import PostHeader from '../components/PostHeader.vue'
import PostList from '../components/PostList.vue'
import TagFilter from '../components/TagFilter.vue'
import ActivityCarousel from '../components/ActivityCarousel.vue'
import { checkToolOwner as checkToolOwnerAPI, getCurrentUser, getActivities, getPosts, getToolFeaturedPost, type Post } from '../mock'

const router = useRouter()
const route = useRoute()

// 选中的工具ID
const selectedToolId = ref<number | string | null>(null)

// 工具Owner权限检查
const isToolOwner = ref(false)
const isAdmin = ref(false) // 是否为管理员
const checkingOwner = ref(false)

// 标签和部门选择
const selectedTag = ref<string | null>(null)
const selectedDepartment = ref<string | null>(null)

// 精华帖子（仅"其他工具"使用）
const featuredPost = ref<any>(null)

// 将单个精华帖子转换为数组（供PostList组件使用）
const featuredPostsArray = computed(() => {
  return featuredPost.value ? [featuredPost.value] : []
})

// 分页
const currentPage = ref(1)
const pageSize = ref(15)

// 所有标签（动态计算，包含"全部"选项）
const displayedTags = computed(() => {
  // 获取所有帖子（其他工具的帖子）
  const allPosts = currentToolPosts.value

  // 根据当前选择的部门过滤帖子
  const filteredPosts = allPosts
  if (selectedDepartment.value) {
    // 这里需要从帖子数据中获取部门信息，暂时简化处理
    // filteredPosts = filteredPosts.filter(post => post.department === selectedDepartment.value)
  }

  // 统计每个标签的数量
  const tagCountMap = new Map<string, number>()
  filteredPosts.forEach(post => {
    if (post.tag) {
      tagCountMap.set(post.tag, (tagCountMap.get(post.tag) || 0) + 1)
    }
  })

  // 构建标签列表，包含"全部"
  const tags: Array<{ name: string; count: number }> = [
    { name: '全部', count: filteredPosts.length }
  ]

  // 添加其他标签
  const tagNames = ['新手', '进阶', '最佳实践', '技巧', '案例', '优化', '通用', '入门']
  tagNames.forEach(tagName => {
    const count = tagCountMap.get(tagName) || 0
    if (count > 0 || !selectedDepartment.value) {
      tags.push({ name: tagName, count })
    }
  })

  return tags
})

// 部门排名统计（动态计算）
const displayedDepartments = computed(() => {
  // 获取所有帖子（其他工具的帖子）
  const allPosts = currentToolPosts.value

  // 根据当前选择的标签过滤帖子
  let filteredPosts = allPosts
  if (selectedTag.value && selectedTag.value !== '全部') {
    filteredPosts = filteredPosts.filter(post => post.tag === selectedTag.value)
  }

  // 统计每个部门的发帖数和贡献者
  const deptMap = new Map<string, { postCount: number; contributors: Set<string> }>()

  filteredPosts.forEach(post => {
    // 这里需要从帖子数据中获取部门信息，暂时简化处理
    // 假设所有帖子都属于某个部门，这里用模拟数据
    const deptName = '研发部' // 实际应该从post.department获取
    if (!deptMap.has(deptName)) {
      deptMap.set(deptName, { postCount: 0, contributors: new Set() })
    }
    const dept = deptMap.get(deptName)!
    dept.postCount++
    if (post.author) {
      dept.contributors.add(post.author)
    }
  })

  // 获取所有部门名称（从所有帖子中提取，这里简化处理）
  const allDepts = new Set<string>(['研发部', '产品部', '技术部', '数据部', '算法部', '运营部', '设计部', '测试部'])

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

const tools = ref<any[]>([])

// 监听配置更新
const handleConfigUpdate = async () => {
  tools.value = await loadTools()
}

// 页面加载时检查路由参数
onMounted(async () => {
  // 加载工具列表
  tools.value = await loadTools()

  // 加载所有活动
  await loadActivities()
  // 监听配置更新
  window.addEventListener('adminConfigUpdated', handleConfigUpdate)

  // 检查路由参数（优先从query参数获取toolId）
  const toolId = route.query.toolId
  if (toolId) {
    const id = Number(toolId)
    if (!isNaN(id) && tools.value.some((t: any) => t.id === id)) {
      selectedToolId.value = id
      // 检查是否为工具Owner
      await checkToolOwner(id)
    } else if (toolId === 'other') {
      selectedToolId.value = 'other'
      isToolOwner.value = false
    }
  } else {
    // 如果没有toolId参数，尝试从路径中匹配工具名称
      const pathMatch = route.path.match(/\/tools\/([^/?]+)/)
      if (pathMatch) {
        const toolName = pathMatch[1]
        if (toolName) {
          const matchedTool = tools.value.find((t: any) => {
            // 尝试匹配工具名称（不区分大小写）
            const toolNameFromLink = t.name.toLowerCase().replace(/\s+/g, '-')
            return toolNameFromLink === toolName.toLowerCase() ||
                   t.name.toLowerCase() === toolName.toLowerCase()
          })
          if (matchedTool) {
            selectedToolId.value = matchedTool.id
            await checkToolOwner(matchedTool.id)
          } else {
            // 如果路径匹配失败，默认选择第一个工具
            if (tools.value.length > 0 && tools.value[0]) {
              selectedToolId.value = tools.value[0].id
              await checkToolOwner(tools.value[0].id)
            }
          }
        }
      } else {
        // 如果没有toolId参数和路径匹配，默认选择第一个工具
        if (tools.value.length > 0 && tools.value[0]) {
          selectedToolId.value = tools.value[0].id
          // 检查是否为工具Owner
          await checkToolOwner(tools.value[0].id)
        }
      }
    }
})

// 监听selectedToolId变化，自动检查权限并加载数据
watch(selectedToolId, async (newToolId) => {
  if (typeof newToolId === 'number') {
    await checkToolOwner(newToolId)
    await loadPosts(newToolId)
    await loadActivities(newToolId)
  } else {
    isToolOwner.value = false
    await loadPosts(0) // 加载"其他工具"的帖子
    await loadActivities(0)
  }
})

// 当前激活的帖子分类
const activePostTab = ref<'guide' | 'excellent'>('guide')

onUnmounted(() => {
  window.removeEventListener('adminConfigUpdated', handleConfigUpdate)
})

// 工具列表 - 从API加载（与首页共用 /api/tools 接口）
const loadTools = async () => {
  try {
    const { getTools } = await import('../mock')
    const toolsResponse = await getTools()
    return toolsResponse.list.map((item: any) => ({
      id: item.id,
      name: item.name,
      desc: item.desc || '',
      logo: item.logo || '',
      link: item.link || `/tools?toolId=${item.id}`,
      color: item.color || '#409eff'
    }))
  } catch (e) {
    console.error('加载工具配置失败:', e)
    return [
    {
      id: 1,
      name: 'TestMate',
      desc: '自动化测试助手',
      logo: 'https://picsum.photos/80/80?random=1',
      link: '/tools?toolId=1',
      color: '#36cfc9'
    },
    {
      id: 2,
      name: 'CodeMate',
      desc: '智能代码补全',
      logo: 'https://picsum.photos/80/80?random=2',
      link: '/tools?toolId=2',
      color: '#9254de'
    },
    {
      id: 3,
      name: '云集',
      desc: '云端计算集群',
      logo: 'https://picsum.photos/80/80?random=3',
      link: '/tools?toolId=3',
      color: '#597ef7'
    },
    {
      id: 4,
      name: '云见',
      desc: '智能监控平台',
      logo: 'https://picsum.photos/80/80?random=4',
      link: '/tools?toolId=4',
      color: '#ff9c6e'
    },
    {
      id: 5,
      name: '扶摇',
      desc: 'Agent编排引擎',
      logo: 'https://picsum.photos/80/80?random=5',
      link: '/tools?toolId=5',
      color: '#4096ff'
    },
    {
      id: 6,
      name: '纠错Agent',
      desc: '智能代码纠错工具',
      logo: 'https://picsum.photos/80/80?random=6',
      link: '/tools?toolId=6',
      color: '#ffc53d'
    },
    {
      id: 7,
      name: 'DT',
      desc: '数据转换工具',
      logo: 'https://picsum.photos/80/80?random=7',
      link: '/tools?toolId=7',
      color: '#73d13d'
    }
  ]
  }
}

// 所有帖子数据
const allPosts = ref<Post[]>([])

// 加载帖子列表
const loadPosts = async (toolId?: number) => {
  try {
    const result = await getPosts({
      zone: 'tools',
      toolId: toolId !== undefined ? toolId : undefined,  // 正确处理 toolId=0 的情况
      page: 1,
      pageSize: 100 // 获取所有帖子
    })
    // 字段映射
    allPosts.value = result.list.map(post => ({
      ...post,
      author: post.author || (post as any).authorName || '',
      description: post.description || (post as any).summary || '',
      image: post.image || post.cover || '',
      createTime: typeof post.createTime === 'string' ? post.createTime : new Date(post.createTime).toLocaleDateString('zh-CN'),
      tag: post.tags && post.tags.length > 0 ? post.tags[0] : '',
      category: (post as any).category || 'guide' // 保留category字段用于分类
    }))
  } catch (error: any) {
    console.error('加载帖子列表失败:', error)
    ElMessage.error(error.message || '加载帖子列表失败')
    allPosts.value = []
  }
}

// 加载精华帖子（仅"其他工具"使用）
const loadFeaturedPost = async () => {
  try {
    const response = await getToolFeaturedPost(0)
    if (response.post) {
      featuredPost.value = {
        ...response.post,
        image: response.post.image || response.post.cover || '',
        createTime: typeof response.post.createTime === 'string' 
          ? response.post.createTime 
          : new Date(response.post.createTime).toLocaleDateString('zh-CN')
      }
    } else {
      featuredPost.value = null
    }
  } catch (error) {
    console.error('加载精华帖子失败:', error)
    featuredPost.value = null
  }
}

// 活动数据
const allActivities = ref<any[]>([])

// 加载活动列表
const loadActivities = async (toolId?: number) => {
  try {
    const result = await getActivities({
      toolId: toolId || undefined,
      page: 1,
      pageSize: 100 // 获取所有活动
    })
    allActivities.value = result.list.map((a: any) => ({
      id: a.id,
      toolId: a.toolId,
      type: a.type || 'activity',
      title: a.title,
      desc: a.content ? a.content.replace(/<[^>]*>/g, '').substring(0, 100) + '...' : '',
      date: typeof a.date === 'string' ? a.date : new Date(a.date).toLocaleDateString('zh-CN'),
      location: '',
      image: a.cover || ''
    }))
  } catch (error) {
    console.error('加载活动列表失败:', error)
    allActivities.value = []
  }
}

// 选择工具
const selectTool = async (toolId: number | string) => {
  selectedToolId.value = toolId
  activePostTab.value = 'guide' // 重置为操作指导

  // 检查是否为工具Owner（仅对普通工具检查，不包括"其他工具"）
  if (typeof toolId === 'number') {
    await checkToolOwner(toolId)
    await loadPosts(toolId)
    await loadActivities(toolId)
    featuredPost.value = null // 普通工具没有精华帖子
  } else {
    // "其他工具"
    isToolOwner.value = false
    await loadPosts(0)
    await loadActivities(0)
    await loadFeaturedPost() // 加载精华帖子
  }
}

// 检查是否为工具Owner或管理员
const checkToolOwner = async (toolId: number) => {
  if (toolId <= 0) {
    isToolOwner.value = false
    isAdmin.value = false
    return
  }

  checkingOwner.value = true
  try {
    // 获取当前用户信息
    const user = await getCurrentUser()
    isAdmin.value = user.roles?.includes('admin') || false

    // 检查是否为工具Owner
    const ownerResponse = await checkToolOwnerAPI(toolId)
    isToolOwner.value = ownerResponse.isOwner || false
  } catch (error: any) {
    console.error('检查工具Owner权限失败:', error)
    isToolOwner.value = false
    isAdmin.value = false
  } finally {
    checkingOwner.value = false
  }
}

// 发布活动
const handlePublishActivity = () => {
  if (!selectedToolId.value || typeof selectedToolId.value !== 'number') {
    ElMessage.warning('请先选择一个工具')
    return
  }

  router.push(`/activity/create?toolId=${selectedToolId.value}`)
}

// 当前工具的帖子
const currentToolPosts = computed(() => {
  let posts = []

  if (selectedToolId.value === 'other') {
    posts = allPosts.value.filter(post => post.toolId === 0)
  } else if (selectedToolId.value) {
    posts = allPosts.value.filter(post => post.toolId === selectedToolId.value)
  } else {
    return []
  }

  // 按标签过滤（排除"全部"）
  if (selectedTag.value && selectedTag.value !== '全部') {
    posts = posts.filter(post => post.tag === selectedTag.value)
  }

  // 按部门过滤（这里简化处理，实际应该从帖子数据中获取部门信息）
  // if (selectedDepartment.value) {
  //   posts = posts.filter(post => post.department === selectedDepartment.value)
  // }

  return posts
})

// 过滤后的帖子（根据分类）
const filteredPosts = computed(() => {
  let posts = currentToolPosts.value

  // 如果不是"其他"工具，按分类过滤
  if (selectedToolId.value !== 'other') {
    posts = posts.filter(post => (post as any).category === activePostTab.value)
  }

  // 按标签过滤（排除"全部"）
  if (selectedTag.value && selectedTag.value !== '全部') {
    posts = posts.filter(post => post.tag === selectedTag.value)
  }

  // 按部门过滤（这里简化处理，实际应该从帖子数据中获取部门信息）
  // if (selectedDepartment.value) {
  //   posts = posts.filter(post => post.department === selectedDepartment.value)
  // }

  // 搜索过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    posts = posts.filter(post =>
      post.title.toLowerCase().includes(keyword) ||
      (post.author?.toLowerCase().includes(keyword) ?? false) ||
      (post.description && post.description.toLowerCase().includes(keyword))
    )
  }

  // 排序
  if (sortBy.value === 'hot') {
    posts.sort((a, b) => b.views - a.views)
  } else if (sortBy.value === 'comments') {
    // 如果帖子没有comments属性，使用views作为替代
    posts.sort((a, b) => (b.views || 0) - (a.views || 0))
  } else {
    // 按时间排序（这里简化处理）
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

// 当前工具的活动
const currentToolActivities = computed(() => {
  return allActivities.value
})

// 颜色池 - 为不同标签提供不同颜色
const colorPalette = [
  { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },      // 蓝色
  { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },      // 绿色
  { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },      // 橙色
  { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },    // 红色
  { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },    // 灰色
  { bg: 'rgba(156, 39, 176, 0.15)', border: '#9c27b0', text: '#9c27b0' },      // 紫色
  { bg: 'rgba(0, 188, 212, 0.15)', border: '#00bcd4', text: '#00bcd4' },       // 青色
  { bg: 'rgba(255, 152, 0, 0.15)', border: '#ff9800', text: '#ff9800' },       // 深橙色
]

// 根据标签名称生成哈希值，用于分配颜色
const getTagColorIndex = (tagName: string): number => {
  let hash = 0
  for (let i = 0; i < tagName.length; i++) {
    hash = tagName.charCodeAt(i) + ((hash << 5) - hash)
  }
  return Math.abs(hash) % colorPalette.length
}

// 获取标签样式
const _getTagStyle = (tagName: string) => {
  const colorIndex = getTagColorIndex(tagName)
  const colors = colorPalette[colorIndex] || colorPalette[0]
  const finalColors: { bg: string; border: string; text: string } = (colors || colorPalette[0]) as { bg: string; border: string; text: string }
  const isActive = selectedTag.value === tagName

  return {
    '--tag-bg': isActive ? finalColors.bg.replace('0.15', '0.25') : finalColors.bg,
    '--tag-border': finalColors.border,
    '--tag-color': finalColors.text,
    backgroundColor: isActive ? finalColors.bg.replace('0.15', '0.25') : finalColors.bg,
    borderColor: finalColors.border,
    color: finalColors.text,
    borderWidth: '1px',
    borderStyle: 'solid'
  }
}

// 获取标签类型（保留用于帖子中的标签）
const _getTagType = (tag: string) => {
  const typeMap: Record<string, string> = {
    '新手': 'info',
    '进阶': 'warning',
    '最佳实践': 'success',
    '技巧': 'primary',
    '案例': 'success',
    '入门': 'info',
    '优化': 'warning',
    '通用': 'info'
  }
  return typeMap[tag] || 'info'
}

// 处理帖子点击
const handlePostClick = (post: any) => {
  console.log('ToolsView: 处理帖子点击', post)
  if (!post || !post.id) {
    console.error('帖子数据无效:', post)
    return
  }
  router.push(`/post/${post.id}`).catch((err) => {
    console.error('路由跳转失败:', err)
  })
}

// 搜索关键词和排序
const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments'>('newest')

// 处理标签切换
const handleTabChange = (tab: 'guide' | 'excellent') => {
  activePostTab.value = tab
  currentPage.value = 1 // 重置到第一页
}

// 处理搜索
const handleSearch = (keyword: string) => {
  searchKeyword.value = keyword
  currentPage.value = 1 // 重置到第一页
}

// 处理排序
const handleSort = (sort: 'newest' | 'hot' | 'comments' | 'likes') => {
  sortBy.value = sort as 'newest' | 'hot' | 'comments'
  currentPage.value = 1 // 重置到第一页
}

// 处理活动点击
const handleActivityClick = (activity: any) => {
  // 跳转到活动详情页
  router.push(`/activity/${activity.id}`)
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

// 处理发帖
const handlePostCreate = () => {
  router.push('/post/create')
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
</script>

<style scoped lang="scss">
.tools-view {
  min-height: 100vh;
  padding: 20px;
  max-width: 1600px;
  margin: 0 auto;
  color: #000;
}

/* 工具按钮组 */
.tools-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  justify-content: center;
  margin-bottom: 28px;
  padding: 20px 16px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.15) 0%, rgba(255, 255, 255, 0.08) 100%);
  backdrop-filter: blur(12px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.25);
}

.tool-card-btn {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px 24px;
  min-width: 100px;
  border-radius: 14px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);

  // 拟态玻璃背景 - 使用工具主题色渐变
  background: linear-gradient(
    135deg,
    color-mix(in srgb, var(--tool-color) 15%, rgba(255, 255, 255, 0.85)) 0%,
    color-mix(in srgb, var(--tool-color) 8%, rgba(255, 255, 255, 0.7)) 50%,
    color-mix(in srgb, var(--tool-color) 12%, rgba(255, 255, 255, 0.75)) 100%
  );
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);

  // 玻璃边框
  border: 1px solid color-mix(in srgb, var(--tool-color) 20%, rgba(255, 255, 255, 0.5));

  // 拟态阴影 - 凸起效果
  box-shadow:
    inset 0 1px 1px rgba(255, 255, 255, 0.7),
    inset 0 -1px 2px color-mix(in srgb, var(--tool-color) 8%, transparent),
    0 4px 12px color-mix(in srgb, var(--tool-color) 15%, rgba(0, 0, 0, 0.08)),
    0 1px 3px rgba(0, 0, 0, 0.06);

  // 工具名称
  .tool-card-name {
    position: relative;
    z-index: 2;
    font-size: 14px;
    font-weight: 600;
    color: color-mix(in srgb, var(--tool-color) 70%, #1e293b);
    white-space: nowrap;
    transition: all 0.3s ease;
    text-shadow: 0 1px 0 rgba(255, 255, 255, 0.6);
    letter-spacing: 0.3px;
  }

  // Hover 效果
  &:hover {
    transform: translateY(-3px) scale(1.02);
    background: linear-gradient(
      135deg,
      color-mix(in srgb, var(--tool-color) 22%, rgba(255, 255, 255, 0.9)) 0%,
      color-mix(in srgb, var(--tool-color) 12%, rgba(255, 255, 255, 0.8)) 50%,
      color-mix(in srgb, var(--tool-color) 18%, rgba(255, 255, 255, 0.85)) 100%
    );
    border-color: color-mix(in srgb, var(--tool-color) 35%, rgba(255, 255, 255, 0.6));
    box-shadow:
      inset 0 1px 2px rgba(255, 255, 255, 0.8),
      inset 0 -1px 2px color-mix(in srgb, var(--tool-color) 10%, transparent),
      0 8px 20px color-mix(in srgb, var(--tool-color) 25%, rgba(0, 0, 0, 0.12)),
      0 2px 6px rgba(0, 0, 0, 0.08);

    .tool-card-name {
      color: color-mix(in srgb, var(--tool-color) 85%, #000);
    }
  }

  // Active 选中状态
  &.tool-card-btn-active {
    transform: translateY(-2px);
    background: linear-gradient(
      135deg,
      color-mix(in srgb, var(--tool-color) 30%, rgba(255, 255, 255, 0.95)) 0%,
      color-mix(in srgb, var(--tool-color) 18%, rgba(255, 255, 255, 0.85)) 50%,
      color-mix(in srgb, var(--tool-color) 25%, rgba(255, 255, 255, 0.9)) 100%
    );
    border-color: color-mix(in srgb, var(--tool-color) 50%, rgba(255, 255, 255, 0.4));
    box-shadow:
      inset 0 2px 3px rgba(255, 255, 255, 0.9),
      inset 0 -2px 4px color-mix(in srgb, var(--tool-color) 15%, transparent),
      0 6px 18px color-mix(in srgb, var(--tool-color) 35%, rgba(0, 0, 0, 0.15)),
      0 0 0 3px color-mix(in srgb, var(--tool-color) 20%, transparent);

    .tool-card-name {
      color: color-mix(in srgb, var(--tool-color) 90%, #000);
      font-weight: 700;
      text-shadow: 
        0 1px 0 rgba(255, 255, 255, 0.8),
        0 0 8px color-mix(in srgb, var(--tool-color) 30%, transparent);
    }
  }
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

/* ActivityCarousel 组件已有自己的样式，不需要额外定义 */

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

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;

  .tag-item {
    cursor: pointer;
    transition: all 0.2s;
    font-weight: 500;

    &:hover {
      transform: translateY(-2px);
      opacity: 0.8;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    &.tag-item-active {
      font-weight: 600;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }
  }

  // 使用全局样式覆盖 Element Plus 标签样式
  :deep(.el-tag.tag-item) {
    background-color: var(--tag-bg, transparent) !important;
    border-color: var(--tag-border, currentColor) !important;
    color: var(--tag-color, inherit) !important;
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

  .tools-buttons {
    justify-content: flex-start;
    overflow-x: auto;
    padding: 14px 12px;
    gap: 10px;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      height: 4px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(0, 0, 0, 0.15);
      border-radius: 4px;
    }

    .tool-card-btn {
      flex-shrink: 0;
      min-width: 80px;
      padding: 10px 18px;

      .tool-card-name {
        font-size: 13px;
      }
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
