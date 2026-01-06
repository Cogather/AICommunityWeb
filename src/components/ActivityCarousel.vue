<template>
  <div class="activities-section">
    <h3 class="section-title">{{ title }}</h3>
    
    <el-carousel
      v-if="activities.length > 0"
      :interval="5000"
      height="400px"
      indicator-position="outside"
      :arrow="'hover'"
      class="activities-carousel"
    >
      <el-carousel-item
        v-for="activity in activities"
        :key="activity.id"
      >
        <div class="activity-card">
          <div class="activity-image">
            <img :src="activity.image" :alt="activity.title" />
            <div class="activity-badge" v-if="activity.type">
              {{ activity.type === 'training' ? '培训' : '活动' }}
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
    <div v-else class="empty-state">
      <el-empty description="暂无近期活动" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Calendar, Location } from '@element-plus/icons-vue'

interface Activity {
  id: number
  type?: 'training' | 'activity' | 'empowerment'
  title: string
  desc: string
  date: string
  location?: string
  image: string
}

interface Props {
  activities: Activity[]
  title?: string
}

const props = withDefaults(defineProps<Props>(), {
  title: '近期活动与培训'
})

const emit = defineEmits<{
  activityClick: [activity: Activity]
}>()

const handleActivityClick = (activity: Activity) => {
  emit('activityClick', activity)
}
</script>

<style scoped lang="scss">
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

.empty-state {
  padding: 60px 20px;
  text-align: center;
}
</style>

