<template>
  <div class="posts-list">
    <!-- 精华帖（置顶） -->
    <div
      v-for="post in featuredPosts"
      :key="post.id"
      class="post-item featured"
      @click="handlePostClick(post)"
    >
      <div class="post-image" v-if="post.image">
        <img :src="post.image" :alt="post.title" />
      </div>
      <div class="post-content">
        <div class="post-header">
          <el-tag type="success" size="small" v-if="showFeaturedTag">精华</el-tag>
          <h3 class="post-title">{{ post.title }}</h3>
          <el-tag v-if="post.tag" :type="getTagType(post.tag)" size="small">
            {{ post.tag }}
          </el-tag>
        </div>
        <p class="post-description" v-if="post.description">{{ post.description }}</p>
        <div class="post-tags" v-if="post.tags && post.tags.length > 0">
          <el-tag
            v-for="tag in post.tags"
            :key="tag"
            :type="getTagType(tag)"
            size="small"
            class="post-tag"
          >
            {{ tag }}
          </el-tag>
        </div>
        <div class="post-meta">
          <span class="meta-item">
            <el-icon><User /></el-icon>
            {{ post.author }}
          </span>
          <span class="meta-item">
            <el-icon><Clock /></el-icon>
            {{ post.createTime }}
          </span>
          <span class="meta-item">
            <el-icon><View /></el-icon>
            {{ post.views }}
          </span>
          <span class="meta-item" v-if="post.comments !== undefined">
            <el-icon><ChatDotRound /></el-icon>
            {{ post.comments }}
          </span>
          <span class="meta-item" v-if="post.likes !== undefined">
            <HeartIcon
              :filled="!!post.isLiked"
              :size="16"
              :color="post.isLiked ? '#f56c6c' : '#f56c6c'"
              :strokeColor="'#f56c6c'"
            />
            {{ post.likes }}
          </span>
        </div>
      </div>
    </div>

    <!-- 普通帖子 -->
    <div
      v-for="post in posts"
      :key="post.id"
      class="post-item"
      @click="handlePostClick(post)"
    >
      <div class="post-image" v-if="post.image">
        <img :src="post.image" :alt="post.title" />
      </div>
      <div class="post-content">
        <div class="post-header">
          <h3 class="post-title">{{ post.title }}</h3>
          <el-tag v-if="post.tag" :type="getTagType(post.tag)" size="small">
            {{ post.tag }}
          </el-tag>
        </div>
        <p class="post-description" v-if="post.description">{{ post.description }}</p>
        <div class="post-tags" v-if="post.tags && post.tags.length > 0">
          <el-tag
            v-for="tag in post.tags"
            :key="tag"
            :type="getTagType(tag)"
            size="small"
            class="post-tag"
          >
            {{ tag }}
          </el-tag>
        </div>
        <div class="post-meta">
          <span class="meta-item">
            <el-icon><User /></el-icon>
            {{ post.author }}
          </span>
          <span class="meta-item">
            <el-icon><Clock /></el-icon>
            {{ post.createTime }}
          </span>
          <span class="meta-item">
            <el-icon><View /></el-icon>
            {{ post.views }}
          </span>
          <span class="meta-item" v-if="post.comments !== undefined">
            <el-icon><ChatDotRound /></el-icon>
            {{ post.comments }}
          </span>
          <span class="meta-item" v-if="post.likes !== undefined">
            <HeartIcon
              :filled="!!post.isLiked"
              :size="16"
              :color="post.isLiked ? '#f56c6c' : '#f56c6c'"
              :strokeColor="'#f56c6c'"
            />
            {{ post.likes }}
          </span>
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
import { User, Clock, View, ChatDotRound, Star } from '@element-plus/icons-vue'
import HeartIcon from './HeartIcon.vue'

interface Post {
  id: number
  title: string
  description?: string
  author: string
  createTime: string
  views: number
  comments?: number
  likes?: number
  isLiked?: boolean
  image?: string
  tag?: string
  tags?: string[]
}

interface Props {
  posts: Post[]
  featuredPosts?: Post[]
  showFeaturedTag?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  featuredPosts: () => [],
  showFeaturedTag: true
})

const emit = defineEmits<{
  postClick: [post: Post]
}>()

// 获取标签类型
const getTagType = (tag: string) => {
  const typeMap: Record<string, string> = {
    '精华': 'success',
    '活动': 'success',
    '项目': 'info',
    '效率': 'warning',
    '实践': 'success',
    '已解决': 'success',
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
    'Agent应用': 'primary',
    '工作流': 'info',
    '自动化': 'warning',
    '智能编排': 'success',
    '案例分享': 'info',
    '开发指南': 'primary'
  }
  return typeMap[tag] || 'info'
}

// 处理帖子点击
const handlePostClick = (post: Post) => {
  emit('postClick', post)
}
</script>

<style scoped lang="scss">
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

    &.featured {
      background-color: rgba(240, 249, 255, 0.5);
      border-left: 4px solid #409eff;
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

      .post-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        margin-bottom: 12px;

        .post-tag {
          cursor: pointer;
          transition: all 0.2s;

          &:hover {
            transform: translateY(-2px);
          }
        }
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

          .heart-icon {
            flex-shrink: 0;
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

/* 响应式 */
@media (max-width: 768px) {
  .post-item {
    flex-direction: column;

    .post-image {
      width: 100%;
      height: 180px;
    }
  }
}
</style>

