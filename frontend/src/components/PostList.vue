<template>
  <div class="posts-list">
    <!-- 精华帖（置顶） -->
    <div
      v-for="post in featuredPosts"
      :key="post.id"
      class="post-card featured"
      @click="handlePostClick(post)"
    >
      <!-- 左侧彩条 -->
      <div class="card-accent"></div>

      <!-- 精华标签 -->
      <div class="featured-tag" v-if="showFeaturedTag">
        <el-icon><Star /></el-icon>
        精华
      </div>

      <!-- 主内容区 -->
      <div class="card-main">
        <!-- 标题 - 最突出 -->
        <h3 class="post-title">{{ post.title }}</h3>

        <!-- 描述 - 次突出 -->
        <p class="post-summary" v-if="post.summary" v-html="getSummaryHtml(post.summary)"></p>

        <!-- 封面图（如果有） -->
        <div class="post-cover" v-if="post.image">
          <img :src="post.image" :alt="post.title" />
        </div>

        <!-- 底部信息栏 -->
        <div class="card-meta">
          <!-- 左侧：作者和部门 -->
          <div class="meta-left">
            <span class="author">
              <el-icon><User /></el-icon>
              {{ post.author || '匿名用户' }}
            </span>
            <span class="department" v-if="post.department">
              <el-icon><OfficeBuilding /></el-icon>
              {{ post.department }}
            </span>
            <span class="time">
              {{ formatDate(post.createTime) }}
            </span>
          </div>

          <!-- 右侧：统计数据 -->
          <div class="meta-right">
            <span class="stat">
              <el-icon><View /></el-icon>
              {{ post.views || 0 }}
            </span>
            <span class="stat">
              <el-icon><ChatDotRound /></el-icon>
              {{ post.comments || 0 }}
            </span>
            <span class="stat" v-if="post.likes !== undefined">
              <HeartIcon
                :filled="!!post.isLiked"
                :size="14"
                :color="post.isLiked ? '#f56c6c' : '#f56c6c'"
                :strokeColor="'#f56c6c'"
              />
              {{ post.likes }}
            </span>
          </div>
        </div>

        <!-- 标签（如果有） -->
        <div class="post-tags" v-if="_props.showTags && post.tags && post.tags.length > 0">
          <el-tag
            v-for="tag in post.tags.slice(0, 4)"
            :key="tag"
            size="small"
            class="post-tag"
          >
            {{ tag }}
          </el-tag>
          <span v-if="post.tags.length > 4" class="more-tags">+{{ post.tags.length - 4 }}</span>
        </div>
      </div>
    </div>

    <!-- 普通帖子 -->
    <div
      v-for="post in posts"
      :key="post.id"
      class="post-card"
      :class="{ 'featured': post.featured }"
      @click="handlePostClick(post)"
    >
      <!-- 左侧彩条 -->
      <div class="card-accent"></div>

      <!-- 精华标签 -->
      <div class="featured-tag" v-if="post.featured">
        <el-icon><Star /></el-icon>
        精华
      </div>

      <!-- 主内容区 -->
      <div class="card-main">
        <!-- 标题 - 最突出 -->
        <h3 class="post-title">{{ post.title }}</h3>

        <!-- 描述 - 次突出 -->
        <p class="post-summary" v-if="post.summary" v-html="getSummaryHtml(post.summary)"></p>

        <!-- 封面图（如果有） -->
        <div class="post-cover" v-if="post.image">
          <img :src="post.image" :alt="post.title" />
        </div>

        <!-- 底部信息栏 -->
        <div class="card-meta">
          <!-- 左侧：作者和部门 -->
          <div class="meta-left">
            <span class="author">
              <el-icon><User /></el-icon>
              {{ post.author || '匿名用户' }}
            </span>
            <span class="department" v-if="post.department">
              <el-icon><OfficeBuilding /></el-icon>
              {{ post.department }}
            </span>
            <span class="time">
              {{ formatDate(post.createTime) }}
            </span>
          </div>

          <!-- 右侧：统计数据 -->
          <div class="meta-right">
            <span class="stat">
              <el-icon><View /></el-icon>
              {{ post.views || 0 }}
            </span>
            <span class="stat">
              <el-icon><ChatDotRound /></el-icon>
              {{ post.comments || 0 }}
            </span>
            <span class="stat" v-if="post.likes !== undefined">
              <HeartIcon
                :filled="!!post.isLiked"
                :size="14"
                :color="post.isLiked ? '#f56c6c' : '#f56c6c'"
                :strokeColor="'#f56c6c'"
              />
              {{ post.likes }}
            </span>
          </div>
        </div>

        <!-- 标签（如果有） -->
        <div class="post-tags" v-if="_props.showTags && post.tags && post.tags.length > 0">
          <el-tag
            v-for="tag in post.tags.slice(0, 4)"
            :key="tag"
            size="small"
            class="post-tag"
          >
            {{ tag }}
          </el-tag>
          <span v-if="post.tags.length > 4" class="more-tags">+{{ post.tags.length - 4 }}</span>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="featuredPosts.length === 0 && posts.length === 0" class="empty-state">
      <el-empty description="暂无相关内容" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { marked } from 'marked'
import { User, View, ChatDotRound, Star, OfficeBuilding } from '@element-plus/icons-vue'
import HeartIcon from './HeartIcon.vue'

interface Post {
  id: number | string
  title: string
  summary?: string
  author?: string
  avatar?: string
  department?: string
  createTime: string | Date
  views: number
  comments?: number
  likes?: number
  isLiked?: boolean
  image?: string
  tag?: string
  tags?: string[]
  featured?: boolean
}

interface Props {
  posts: Post[]
  featuredPosts?: Post[]
  showFeaturedTag?: boolean
  showTags?: boolean
}

const _props = withDefaults(defineProps<Props>(), {
  featuredPosts: () => [],
  showFeaturedTag: true,
  showTags: true
})

const emit = defineEmits<{
  postClick: [post: Post]
}>()

const hasMarkdownImage = (value?: string) => {
  if (!value) return false
  const markdownImage = /!\[[^\]]*]\([^)]+\)/i
  const htmlImage = /<img\s[^>]*src\s*=\s*['"][^'"]+['"][^>]*>/i
  return markdownImage.test(value) || htmlImage.test(value)
}

const getSummaryHtml = (value?: string) => {
  if (!value) return ''
  if (hasMarkdownImage(value)) return '…'
  return marked.parseInline(value, { breaks: true })
}

// 处理帖子点击
const handlePostClick = (post: Post) => {
  emit('postClick', post)
}

// 格式化日期
const formatDate = (date: string | Date) => {
  if (!date) return ''
  const d = new Date(date)
  if (isNaN(d.getTime())) return date.toString()

  const now = new Date()
  const diff = now.getTime() - d.getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`

  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${month}-${day}`
}
</script>

<style scoped lang="scss">
.posts-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 卡片基础样式 */
.post-card {
  position: relative;
  display: flex;
  background: #ffffff;
  border-radius: 10px;
  border: 1px solid #ebeef5;
  cursor: pointer;
  transition: all 0.25s ease;
  overflow: hidden;

  &:hover {
    border-color: #c0c4cc;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);

    .card-accent {
      width: 5px;
    }

    .post-title {
      color: #409eff;
    }
  }

  /* 精华帖样式 */
  &.featured {
    background: linear-gradient(to right, #fafcff 0%, #ffffff 100%);
    border-color: #d9ecff;

    .card-accent {
      background: linear-gradient(180deg, #409eff 0%, #67c23a 100%);
    }

    &:hover {
      border-color: #409eff;
      box-shadow: 0 4px 16px rgba(64, 158, 255, 0.15);
    }
  }
}

/* 左侧彩条 */
.card-accent {
  width: 3px;
  flex-shrink: 0;
  background: linear-gradient(180deg, #409eff 0%, #79bbff 100%);
  transition: width 0.25s ease;
}

/* 精华标签 */
.featured-tag {
  position: absolute;
  top: 0;
  right: 20px;
  display: flex;
  align-items: center;
  gap: 3px;
  padding: 4px 10px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  color: #fff;
  font-size: 11px;
  font-weight: 500;
  border-radius: 0 0 6px 6px;
  box-shadow: 0 2px 6px rgba(64, 158, 255, 0.3);

  .el-icon {
    font-size: 12px;
  }
}

/* 主内容区 */
.card-main {
  flex: 1;
  padding: 16px 20px;
  min-width: 0;
}

/* 标题 - 最突出 */
.post-title {
  margin: 0 0 8px 0;
  font-size: 18px;
  font-weight: 400;
  color: #1a1a1a;
  line-height: 1.5;
  transition: color 0.2s;

  /* 限制单行显示，超出省略 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 描述 - 次突出 */
.post-summary {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #5a5a5a;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 封面图 */
.post-cover {
  margin-bottom: 12px;
  border-radius: 6px;
  overflow: hidden;
  max-height: 140px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
  }

  &:hover img {
    transform: scale(1.02);
  }
}

/* 底部信息栏 */
.card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #909399;

  .meta-left {
    display: flex;
    align-items: center;
    gap: 14px;
    flex-wrap: wrap;

    .author, .department, .time {
      display: flex;
      align-items: center;
      gap: 4px;

      .el-icon {
        font-size: 13px;
        color: #c0c4cc;
      }
    }

    .author {
      color: #606266;
      font-weight: 500;
    }

    .department {
      color: #909399;

      /* 部门名加个小背景 */
      background: #f4f4f5;
      padding: 2px 8px;
      border-radius: 4px;
      font-size: 11px;
    }

    .time {
      color: #b0b0b0;
    }
  }

  .meta-right {
    display: flex;
    align-items: center;
    gap: 12px;

    .stat {
      display: flex;
      align-items: center;
      gap: 3px;
      color: #b0b0b0;
      transition: color 0.2s;

      .el-icon {
        font-size: 13px;
      }

      &:hover {
        color: #409eff;
      }
    }
  }
}

/* 标签 */
.post-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px dashed #ebeef5;

  .post-tag {
    background: transparent !important;
    border: 1px solid #dcdfe6 !important;
    color: #909399 !important;
    border-radius: 4px;
    padding: 2px 8px;
    font-size: 11px;
    height: auto;
    line-height: 1.4;
    transition: all 0.2s;

    &:hover {
      border-color: #409eff !important;
      color: #409eff !important;
      background: #ecf5ff !important;
    }
  }

  .more-tags {
    font-size: 11px;
    color: #c0c4cc;
    padding: 2px 6px;
  }
}

/* 空状态 */
.empty-state {
  padding: 60px 20px;
  text-align: center;
}

/* 响应式 */
@media (max-width: 768px) {
  .post-card {
    border-radius: 8px;
  }

  .card-main {
    padding: 12px 14px;
  }

  .post-title {
    font-size: 15px;
  }

  .post-summary {
    font-size: 13px;
    -webkit-line-clamp: 2;
    line-clamp: 2;
  }

  .card-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;

    .meta-left {
      gap: 10px;
    }

    .meta-right {
      width: 100%;
      justify-content: flex-start;
    }
  }

  .featured-tag {
    right: 12px;
    padding: 3px 8px;
    font-size: 10px;
  }

  .post-cover {
    max-height: 120px;
  }
}
</style>
