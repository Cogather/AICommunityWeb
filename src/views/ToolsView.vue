<template>
  <div class="tools-view">
    <!-- 工具按钮组 -->
    <div class="tools-buttons">
      <div
        v-for="tool in tools"
        :key="tool.id"
        :class="{ 'tool-card-btn': true, 'tool-card-btn-active': selectedToolId === tool.id }"
        @click="selectTool(tool.id)"
      >
        <div class="tool-card-icon">
          <img v-if="tool.logo" :src="tool.logo" :alt="tool.name" />
          <div v-else class="tool-icon-placeholder" :style="{ background: tool.color || '#409eff' }">
            {{ tool.name[0] }}
          </div>
        </div>
        <span class="tool-card-name">{{ tool.name }}</span>
      </div>
      
      <!-- 其他工具按钮 -->
      <div
        :class="{ 'tool-card-btn': true, 'tool-card-btn-active': selectedToolId === 'other', 'tool-card-btn-other': true }"
        @click="selectTool('other')"
      >
        <div class="tool-card-icon">
          <el-icon class="other-icon"><More /></el-icon>
        </div>
        <span class="tool-card-name">其他工具</span>
      </div>
    </div>
    
    <!-- 内容区域 -->
    <div class="content-area" v-if="selectedToolId">
      <el-row :gutter="24">
        <!-- 左侧：帖子列表 -->
        <el-col :xs="24" :md="16">
          <div class="posts-section">
            <!-- 帖子分类标签和发帖按钮 -->
            <div class="post-tabs-header">
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

        <!-- 右侧：赋能/培训活动宣传 或 其他工具的标签/部门分类 -->
        <el-col :xs="24" :md="8">
          <!-- 其他工具：标签和部门分类 -->
          <div v-if="selectedToolId === 'other'" class="sidebar-section">
            <!-- 标签选择 -->
            <div class="sidebar-block">
              <h3 class="sidebar-title">标签选择</h3>
              <div class="tags-list">
                <el-tag
                  v-for="tag in allTags"
                  :key="tag.name"
                  :class="['tag-item', { 'tag-item-active': selectedTag === tag.name }]"
                  :style="getTagStyle(tag.name)"
                  @click="handleTagClick(tag.name)"
                >
                  #{{ tag.name }} ({{ tag.count }})
                </el-tag>
              </div>
            </div>

            <!-- 部门归类 -->
            <div class="sidebar-block">
              <h3 class="sidebar-title">部门归类</h3>
              <div class="department-list">
                <div
                  v-for="dept in departmentRankings"
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
            <h3 class="section-title">近期活动</h3>
            
            <!-- 活动轮播 -->
            <el-carousel
              :interval="5000"
              height="400px"
              indicator-position="outside"
              :arrow="'hover'"
              class="activities-carousel"
            >
              <el-carousel-item
                v-for="activity in currentToolActivities"
                :key="activity.id"
              >
                <div class="activity-card">
                  <div class="activity-image">
                    <img :src="activity.image" :alt="activity.title" />
                    <div class="activity-badge" v-if="activity.type">
                      {{ activity.type === 'training' ? '培训' : '赋能' }}
                    </div>
                  </div>
                  <div class="activity-content">
                    <h4 class="activity-title">{{ activity.title }}</h4>
                    <p class="activity-desc">{{ activity.desc }}</p>
                    <div class="activity-meta">
                      <span class="activity-date">
                        <el-icon><Calendar /></el-icon>
                        {{ activity.date }}
                      </span>
                      <span class="activity-location" v-if="activity.location">
                        <el-icon><Location /></el-icon>
                        {{ activity.location }}
                      </span>
                    </div>
                    <el-button type="primary" class="activity-btn" @click="handleActivityClick(activity)">
                      了解详情
                    </el-button>
                  </div>
                </div>
              </el-carousel-item>
            </el-carousel>

            <!-- 空状态 -->
            <div v-if="currentToolActivities.length === 0" class="empty-state">
              <el-empty description="暂无近期活动" />
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 未选择工具时的提示 -->
    <div v-else class="empty-tool-selection">
      <el-empty description="请选择一个工具查看相关内容" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { User, Clock, View, Calendar, Location, More } from '@element-plus/icons-vue'
import PostHeader from '../components/PostHeader.vue'
import PostList from '../components/PostList.vue'
import TagFilter from '../components/TagFilter.vue'
import ActivityCarousel from '../components/ActivityCarousel.vue'

const router = useRouter()
const route = useRoute()

// 选中的工具ID
const selectedToolId = ref<number | string | null>(null)

// 标签和部门选择
const selectedTag = ref<string | null>(null)
const selectedDepartment = ref<string | null>(null)

// 分页
const currentPage = ref(1)
const pageSize = ref(15)

// 所有标签
const allTags = ref([
  { name: '新手', count: 120 },
  { name: '进阶', count: 95 },
  { name: '最佳实践', count: 80 },
  { name: '技巧', count: 75 },
  { name: '案例', count: 110 },
  { name: '优化', count: 60 },
  { name: '通用', count: 50 }
])

// 部门排名统计
const departmentRankings = ref([
  { id: 1, name: '研发部', postCount: 156, contributorCount: 28 },
  { id: 2, name: '产品部', postCount: 132, contributorCount: 22 },
  { id: 3, name: '技术部', postCount: 98, contributorCount: 18 },
  { id: 4, name: '数据部', postCount: 87, contributorCount: 15 },
  { id: 5, name: '算法部', postCount: 76, contributorCount: 12 },
  { id: 6, name: '运营部', postCount: 65, contributorCount: 10 },
  { id: 7, name: '设计部', postCount: 54, contributorCount: 8 },
  { id: 8, name: '测试部', postCount: 43, contributorCount: 7 }
])

// 页面加载时检查路由参数
onMounted(() => {
  const toolId = route.query.toolId
  if (toolId) {
    const id = Number(toolId)
    if (!isNaN(id) && tools.value.some(t => t.id === id)) {
      selectedToolId.value = id
    } else if (toolId === 'other') {
      selectedToolId.value = 'other'
    }
  } else {
    // 如果没有toolId参数，默认选择第一个工具
    if (tools.value.length > 0 && tools.value[0]) {
      selectedToolId.value = tools.value[0].id
    }
  }
})

// 当前激活的帖子分类
const activePostTab = ref<'guide' | 'excellent'>('guide')

// 工具列表
const tools = ref([
  { 
    id: 1,
    name: 'TestMate', 
    desc: '自动化测试助手', 
    logo: 'https://picsum.photos/80/80?random=1',
    color: '#36cfc9' 
  },
  { 
    id: 2,
    name: 'CodeMate', 
    desc: '智能代码补全', 
    logo: 'https://picsum.photos/80/80?random=2',
    color: '#9254de' 
  },
  { 
    id: 3,
    name: '云集', 
    desc: '云端计算集群', 
    logo: 'https://picsum.photos/80/80?random=3',
    color: '#597ef7' 
  },
  { 
    id: 4,
    name: '云见', 
    desc: '智能监控平台', 
    logo: 'https://picsum.photos/80/80?random=4',
    color: '#ff9c6e' 
  },
  { 
    id: 5,
    name: '扶摇', 
    desc: 'Agent编排引擎', 
    logo: 'https://picsum.photos/80/80?random=5',
    color: '#4096ff' 
  },
  { 
    id: 6,
    name: '纠错Agent', 
    desc: '智能代码纠错工具', 
    logo: 'https://picsum.photos/80/80?random=6',
    color: '#ffc53d' 
  },
  { 
    id: 7,
    name: 'DT', 
    desc: '数据转换工具', 
    logo: 'https://picsum.photos/80/80?random=7',
    color: '#73d13d' 
  },
])

// 所有帖子数据（模拟）
const allPosts = ref([
  // TestMate 相关
  { id: 1, toolId: 1, category: 'guide', title: 'TestMate 快速入门指南', description: '从零开始学习 TestMate 的基本使用方法，快速上手自动化测试。', author: '张工程师', createTime: '2024年4月10日', views: 1250, comments: 45, likes: 128, tag: '新手', image: 'https://picsum.photos/400/300?random=1' },
  { id: 2, toolId: 1, category: 'excellent', title: 'TestMate 在企业级项目中的最佳实践', description: '分享如何在实际项目中高效使用 TestMate 提升测试效率。', author: '李开发者', createTime: '2024年4月8日', views: 890, comments: 32, likes: 95, tag: '最佳实践', image: 'https://picsum.photos/400/300?random=2' },
  { id: 3, toolId: 1, category: 'guide', title: 'TestMate 高级功能详解', description: '深入探讨 TestMate 的高级功能和配置选项。', author: '王测试', createTime: '2024年4月5日', views: 650, comments: 18, likes: 52, tag: '进阶', image: 'https://picsum.photos/400/300?random=3' },
  
  // CodeMate 相关
  { id: 4, toolId: 2, category: 'guide', title: 'CodeMate 代码补全技巧', description: '掌握 CodeMate 的智能代码补全功能，提升编码效率。', author: '赵医生', createTime: '2024年4月12日', views: 720, comments: 28, likes: 78, tag: '技巧', image: 'https://picsum.photos/400/300?random=4' },
  { id: 5, toolId: 2, category: 'excellent', title: 'CodeMate 在大型项目中的应用', description: '介绍 CodeMate 在大型软件开发项目中的实际应用案例。', author: '陈架构师', createTime: '2024年4月9日', views: 520, comments: 15, likes: 42, tag: '案例', image: 'https://picsum.photos/400/300?random=5' },
  
  // 云集相关
  { id: 6, toolId: 3, category: 'guide', title: '云集集群管理入门', description: '学习如何使用云集进行云端计算集群的管理和调度。', author: '刘设计师', createTime: '2024年4月11日', views: 450, comments: 12, likes: 35, tag: '入门', image: 'https://picsum.photos/400/300?random=6' },
  { id: 7, toolId: 3, category: 'excellent', title: '云集性能优化实战', description: '分享云集集群性能优化的实战经验和技巧。', author: '张工程师', createTime: '2024年4月7日', views: 380, comments: 10, likes: 28, tag: '优化', image: 'https://picsum.photos/400/300?random=7' },
  
  // 其他工具
  { id: 8, toolId: 0, category: 'guide', title: 'AI工具使用通用指南', description: '介绍AI工具使用的一般方法和注意事项。', author: '系统管理员', createTime: '2024年4月13日', views: 1200, comments: 58, likes: 156, tag: '通用', image: 'https://picsum.photos/400/300?random=8' },
  { id: 9, toolId: 0, category: 'excellent', title: 'AI工具优秀案例集锦', description: '收集整理各类AI工具的优秀使用案例。', author: '社区编辑', createTime: '2024年4月6日', views: 950, comments: 42, likes: 112, tag: '案例', image: 'https://picsum.photos/400/300?random=9' },
])

// 活动数据（模拟）
const allActivities = ref([
  { id: 1, toolId: 1, type: 'training', title: 'TestMate 培训课程', desc: '深入学习 TestMate 的高级功能和应用场景，提升测试自动化能力。', date: '2024年4月20日', location: '线上', image: 'https://picsum.photos/600/400?random=10' },
  { id: 2, toolId: 1, type: 'empowerment', title: 'TestMate 赋能工作坊', desc: '实战演练 TestMate 在企业项目中的应用，现场答疑解惑。', date: '2024年4月25日', location: '北京', image: 'https://picsum.photos/600/400?random=11' },
  { id: 3, toolId: 2, type: 'training', title: 'CodeMate 开发培训', desc: '掌握 CodeMate 的核心功能，提升代码开发效率和质量。', date: '2024年4月22日', location: '线上', image: 'https://picsum.photos/600/400?random=12' },
  { id: 4, toolId: 3, type: 'empowerment', title: '云集技术分享会', desc: '分享云集集群管理的最佳实践和最新功能。', date: '2024年4月28日', location: '上海', image: 'https://picsum.photos/600/400?random=13' },
  { id: 5, toolId: 5, type: 'training', title: '扶摇 Agent 训练营', desc: '系统学习扶摇 Agent 编排引擎的使用方法和实战技巧。', date: '2024年5月5日', location: '线上', image: 'https://picsum.photos/600/400?random=14' },
])

// 选择工具
const selectTool = (toolId: number | string) => {
  selectedToolId.value = toolId
  activePostTab.value = 'guide' // 重置为操作指导
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

  // 按标签过滤
  if (selectedTag.value) {
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
  let posts = currentToolPosts.value.filter(post => post.category === activePostTab.value)

  // 按标签过滤
  if (selectedTag.value) {
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
      post.author.toLowerCase().includes(keyword) ||
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
  if (selectedToolId.value === 'other' || !selectedToolId.value) {
    return []
  }
  return allActivities.value.filter(activity => activity.toolId === selectedToolId.value)
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
const getTagStyle = (tagName: string) => {
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
const getTagType = (tag: string) => {
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
  router.push(`/post/${post.id}`)
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
const handleSort = (sort: 'newest' | 'hot' | 'comments') => {
  sortBy.value = sort
  currentPage.value = 1 // 重置到第一页
}

// 处理活动点击
const handleActivityClick = (activity: any) => {
  console.log('点击活动:', activity)
  // 可以跳转到活动详情页
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

// 处理部门点击
const handleDepartmentClick = (departmentName: string) => {
  if (selectedDepartment.value === departmentName) {
    selectedDepartment.value = null
  } else {
    selectedDepartment.value = departmentName
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
  gap: 16px;
  justify-content: center;
  margin-bottom: 30px;
  padding: 20px 0;
}

.tool-card-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 20px;
  min-width: 100px;
  border-radius: 16px;
  background: transparent;
  border: 2px solid rgba(0, 0, 0, 0.15);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

  &:hover {
    border-color: rgba(64, 158, 255, 0.5);
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(64, 158, 255, 0.15);
  }

  &.tool-card-btn-active {
    background: transparent;
    border-color: #409eff;
    box-shadow: 0 8px 24px rgba(64, 158, 255, 0.2);

    .tool-card-name {
      color: #409eff;
      font-weight: 600;
    }

    .tool-card-icon {
      transform: scale(1.1);
      border-color: #409eff;
    }
  }

  .tool-card-icon {
    width: 56px;
    height: 56px;
    border-radius: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: transparent;
    border: 2px solid rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .tool-icon-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #333;
      font-size: 24px;
      font-weight: 600;
    }

    .other-icon {
      font-size: 28px;
      color: #666;
    }
  }

  .tool-card-name {
    font-size: 14px;
    font-weight: 500;
    color: #333;
    white-space: nowrap;
    transition: all 0.3s ease;
  }

  &.tool-card-btn-other {
    .tool-card-icon {
      border-color: rgba(0, 0, 0, 0.15);
    }

    &.tool-card-btn-active {
      .tool-card-icon {
        border-color: #909399;
      }

      .tool-card-name {
        color: #909399;
      }
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

/* 活动区域 */
.activities-section {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.section-title {
  margin: 0 0 20px 0;
  font-size: 20px;
  font-weight: 600;
  color: #000;
}

.activities-carousel {
  :deep(.el-carousel__item) {
    padding: 0;
  }
}

.activity-card {
  background: rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  }

  .activity-image {
    position: relative;
    width: 100%;
    height: 200px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .activity-badge {
      position: absolute;
      top: 12px;
      right: 12px;
      padding: 4px 12px;
      background: rgba(64, 158, 255, 0.9);
      color: #fff;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
    }
  }

  .activity-content {
    padding: 16px;

    .activity-title {
      margin: 0 0 8px 0;
      font-size: 16px;
      font-weight: 600;
      color: #000;
    }

    .activity-desc {
      margin: 0 0 12px 0;
      font-size: 13px;
      color: #666;
      line-height: 1.5;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .activity-meta {
      display: flex;
      flex-direction: column;
      gap: 8px;
      margin-bottom: 16px;
      font-size: 12px;
      color: #999;

      .activity-date,
      .activity-location {
        display: flex;
        align-items: center;
        gap: 4px;

        .el-icon {
          font-size: 14px;
        }
      }
    }

    .activity-btn {
      width: 100%;
    }
  }
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
    padding-bottom: 10px;

    .tool-btn {
      flex-shrink: 0;
    }
  }
}
</style>
