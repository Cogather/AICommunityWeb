<template>
  <div class="practices-view">
    <div class="posts-container">
      <el-row :gutter="24">
        <!-- 左侧：帖子列表 -->
        <el-col :xs="24" :md="17">
          <div class="posts-content">
            <!-- 头部操作栏 -->
            <PostHeader
              :show-tabs="true"
              :default-tab="activeTab"
              :default-sort="sortBy"
              @tab-change="handleTabChange"
              @search="handleSearch"
              @sort="handleSort"
              @post-click="handlePostCreate"
            />

            <!-- 帖子列表 -->
            <PostList
              :posts="filteredPosts"
              :featured-posts="featuredPosts"
              :show-featured-tag="true"
              @post-click="handlePostClick"
            />
          </div>
        </el-col>

        <!-- 右侧：标签栏 -->
        <el-col :xs="24" :md="7">
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

            <!-- 部门分类 -->
            <div class="sidebar-section">
              <h3>部门分类</h3>
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

const activeTab = ref<'all' | 'my'>('all')
const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments'>('newest')
const selectedTag = ref<string | null>(null)
const selectedDepartment = ref<string | null>(null)
const selectedContributor = ref<string | null>(null)

// 所有标签
const allTags = ref([
  { name: '自然语言处理', count: 120 },
  { name: '计算机视觉', count: 95 },
  { name: '深度学习', count: 80 },
  { name: 'AI伦理', count: 75 },
  { name: '机器学习', count: 110 },
  { name: '机器人', count: 60 },
  { name: '数据科学', count: 50 },
  { name: '生成式AI', count: 45 },
  { name: 'PyTorch', count: 30 },
  { name: 'TensorFlow', count: 25 }
])

// 精华帖（置顶）
const featuredPosts = ref([
  {
    id: 1,
    title: 'AI大会2024',
    description: '参加活动探索AI的最新趋势和创新。',
    author: 'David Chen',
    createTime: '2024年4月10日',
    views: 1250,
    comments: 45,
    tags: ['活动', 'AI大会'],
    image: 'https://picsum.photos/800/400?random=1',
    featured: true,
    department: '产品部'
  }
])

// 普通帖子
const posts = ref([
  {
    id: 2,
    title: '构建AI应用指南',
    description: '学习如何构建实用的AI应用程序，从概念到部署的完整流程。',
    author: 'Brinit',
    createTime: '2024年4月',
    views: 890,
    comments: 32,
    tags: ['项目', 'AI应用'],
    image: 'https://picsum.photos/400/300?random=2',
    department: '研发部'
  },
  {
    id: 3,
    title: '通过自动化工作流提高效率',
    description: '探索如何使用AI工具自动化重复性任务，提升工作效率。',
    author: 'Iruls',
    createTime: '60分钟前',
    views: 650,
    comments: 18,
    tags: ['效率', '自动化'],
    image: 'https://picsum.photos/400/300?random=3',
    department: '技术部'
  },
  {
    id: 4,
    title: '在真实项目中实施AI',
    description: '学习如何将AI技术应用于解决实际问题，包含来自各个行业的示例。',
    author: 'Emily Zhao',
    createTime: '2024年4月20日',
    views: 520,
    comments: 15,
    tags: ['实践', 'AI应用'],
    department: '数据部'
  },
  {
    id: 5,
    title: '训练机器学习模型的最佳实践',
    description: '发现有效训练AI模型的关键策略，包括数据准备、模型选择和部署。',
    author: 'John Smith',
    createTime: '2024年4月20日',
    views: 720,
    comments: 28,
    tags: ['已解决', '机器学习'],
    department: '算法部'
  },
  {
    id: 6,
    title: '[已解决] 部署AI解决方案的挑战',
    description: '讨论AI部署过程中遇到的常见障碍并分享解决方案。',
    author: 'Sarah Lee',
    createTime: '2024年4月19日',
    views: 450,
    comments: 12,
    tags: ['已解决', '部署'],
    department: '测试部'
  }
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

// 热门贡献者
const topContributors = ref([
  { id: 1, name: '张工程师', avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 2, name: '李开发者', avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 3, name: '王测试', avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 4, name: '赵医生', avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' },
  { id: 5, name: '陈架构师', avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' }
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
      post.author.toLowerCase().includes(keyword) ||
      (post.description && post.description.toLowerCase().includes(keyword))
    )
  }

  // 排序
  if (sortBy.value === 'hot') {
    result.sort((a, b) => b.views - a.views)
  } else if (sortBy.value === 'comments') {
    result.sort((a, b) => b.comments - a.comments)
  } else {
    // 按时间排序（这里简化处理）
    result.sort((a, b) => b.id - a.id)
  }

  return result
})

// 获取标签类型
const getTagType = (tag: string) => {
  const typeMap: Record<string, string> = {
    '精华': 'success',
    '活动': 'success',
    '项目': 'info',
    '效率': 'warning',
    '实践': 'success',
    '已解决': 'success'
  }
  return typeMap[tag] || 'info'
}

// 处理标签切换
const handleTabChange = (tab: 'all' | 'my') => {
  activeTab.value = tab
}

// 处理搜索
const handleSearch = (keyword: string) => {
  searchKeyword.value = keyword
}

// 处理排序
const handleSort = (sort: 'newest' | 'hot' | 'comments') => {
  sortBy.value = sort
}

// 处理发帖
const handlePostCreate = () => {
  console.log('创建新帖子')
  // 可以跳转到发帖页面
}

// 处理标签点击
const handleTagClick = (tagName: string) => {
  if (selectedTag.value === tagName) {
    selectedTag.value = null
  } else {
    selectedTag.value = tagName
  }
}

// 处理部门点击
const handleDepartmentClick = (departmentName: string) => {
  if (selectedDepartment.value === departmentName) {
    selectedDepartment.value = null
  } else {
    selectedDepartment.value = departmentName
  }
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
const handlePostClick = (post: any) => {
  // 可以跳转到帖子详情页
  console.log('点击帖子:', post)
}

// 获取排名样式类
const getRankClass = (index: number) => {
  if (index === 0) return 'rank-gold'
  if (index === 1) return 'rank-silver'
  if (index === 2) return 'rank-bronze'
  return ''
}
</script>

<style scoped lang="scss">
.practices-view {
  min-height: 100vh;
  padding: 20px;
}

.posts-container {
  max-width: 1400px;
  margin: 0 auto;
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


    .department-rankings {
      display: flex;
      flex-direction: column;

      .department-item {
        padding: 12px;
        border-radius: 0;
        transition: all 0.2s;
        cursor: pointer;
        border: none;
        margin-bottom: 0;
        position: relative;

        &:not(:last-child) {
          border-bottom: 1px solid rgba(0, 0, 0, 0.2);
          margin-bottom: 0;
        }

        &:last-child {
          border-bottom: none;
        }

        &:hover {
          background-color: rgba(0, 0, 0, 0.02);
        }

        &.active {
          background-color: rgba(64, 158, 255, 0.1);
          border-left: 3px solid #409eff;
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

    .header-right {
      flex-wrap: wrap;
      width: 100%;

      .search-input {
        width: 100%;
      }
    }
  }
}
</style>
