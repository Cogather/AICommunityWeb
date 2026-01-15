<template>
  <div class="activity-detail-view">
    <div class="detail-container" v-loading="loading">
      <!-- 返回按钮 -->
      <div class="back-button-wrapper">
        <el-button
          :icon="ArrowLeft"
          text
          @click="handleBack"
          class="back-button"
        >
          返回
        </el-button>
      </div>

      <!-- 活动内容区域 -->
      <div class="activity-main" v-if="!loading">
        <!-- 活动头部 -->
        <div class="activity-header">
          <div class="activity-title-section">
            <h1 class="activity-title">{{ activityData.title }}</h1>
          </div>
          
          <div class="activity-info">
            <div class="tool-info" v-if="activityData.toolName">
              <el-tag type="primary" size="large">{{ activityData.toolName }}</el-tag>
              <el-tag type="success" size="large" class="type-tag" v-if="activityData.type">{{ getActivityTypeLabel(activityData.type) }}</el-tag>
            </div>
            
            <!-- 关键信息卡片 -->
            <div class="info-grid">
              <!-- 时间 -->
              <div class="info-card time-card">
                <el-icon class="card-icon"><Calendar /></el-icon>
                <div class="card-content">
                  <div class="card-label">活动时间</div>
                  <div class="card-value">{{ formatDateTime(activityData.date, activityData.startTime, activityData.endTime) }}</div>
                </div>
              </div>
              
              <!-- 地点 -->
              <div class="info-card location-card">
                <el-icon class="card-icon"><Location /></el-icon>
                <div class="card-content">
                  <div class="card-label">活动地点</div>
                  <div class="card-value">
                    {{ activityData.location }}
                    <el-link 
                      v-if="activityData.meetingLink" 
                      :href="activityData.meetingLink" 
                      target="_blank" 
                      type="primary" 
                      class="meeting-link"
                    >
                      (点击进入会议)
                    </el-link>
                  </div>
                </div>
              </div>
              
              <!-- 主讲人 -->
              <div class="info-card speaker-card" v-if="activityData.speaker">
                <el-icon class="card-icon"><User /></el-icon>
                <div class="card-content">
                  <div class="card-label">主讲人</div>
                  <div class="card-value">
                    {{ activityData.speaker }}
                    <span v-if="activityData.speakerTitle" class="speaker-title"> · {{ activityData.speakerTitle }}</span>
                  </div>
                </div>
              </div>

              <!-- 报名人数 -->
              <div class="info-card participants-card">
                <el-icon class="card-icon"><UserFilled /></el-icon>
                <div class="card-content">
                  <div class="card-label">报名人数</div>
    <div class="card-value">
      {{ activityData.currentParticipants }}
      <span v-if="(activityData.maxParticipants || 0) > 0" class="max-participants"> / {{ activityData.maxParticipants }}</span>
    </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 活动封面 -->
        <div class="activity-cover" v-if="activityData.cover">
          <img :src="activityData.cover" :alt="activityData.title" />
        </div>

        <!-- 活动内容 -->
        <div class="activity-content">
          <h3>活动详情</h3>
          <div class="content-html" v-html="activityData.content"></div>
        </div>

        <!-- 操作按钮 -->
        <div class="activity-actions">
          <div class="actions-left">
            <template v-if="!isRegistered">
              <el-button
                v-if="canRegister"
                type="primary"
                size="large"
                @click="handleRegister"
                :loading="registering"
                class="register-button"
              >
                <el-icon><UserFilled /></el-icon>
                报名参加
              </el-button>
              <el-button
                v-else
                type="info"
                size="large"
                disabled
                class="register-button disabled"
              >
                <el-icon><CircleCloseFilled /></el-icon>
                {{ getDisabledReason() }}
              </el-button>
            </template>
            
            <div v-else class="registered-actions">
              <el-button
                type="success"
                size="large"
                disabled
                class="register-button registered"
              >
                <el-icon><Check /></el-icon>
                已报名
              </el-button>
              <el-button
                type="danger"
                size="large"
                plain
                @click="handleCancelRegister"
                :loading="canceling"
                class="cancel-button"
              >
                <el-icon><Close /></el-icon>
                取消报名
              </el-button>
            </div>
          </div>
          <div class="actions-right" v-if="canEditActivity">
            <el-button
              type="warning"
              size="large"
              :icon="Edit"
              @click="handleEdit"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="large"
              :icon="Delete"
              @click="handleDelete"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  ArrowLeft, Calendar, UserFilled, Edit, Delete, Check, Close, 
  Location, User, CircleCloseFilled 
} from '@element-plus/icons-vue'
import { ROUTES } from '../router/paths'
import { ElMessage, ElMessageBox } from 'element-plus'
// API 层 - 支持 Mock/Real API 自动切换
import { getActivityDetail, joinActivity, cancelJoin, deleteActivity } from '../api/activities'
import type { ActivityDetail } from '../api/activities'
import { getCurrentUser } from '../api/user'
import { checkOwnerPermission } from '../api/tools'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const registering = ref(false)
const canceling = ref(false)

// 管理员状态和工具Owner状态
const isAdmin = ref(false)
const isToolOwner = ref(false)
const currentUserId = ref<number | string>(0)

// 活动数据
const activityData = ref<ActivityDetail>({
  id: 0,
  title: '',
  toolName: '',
  toolId: 0,
  type: 'activity',
  date: '',
  cover: '',
  content: '',
  status: 'upcoming',
  isJoined: false,
  currentParticipants: 0,
  maxParticipants: 0
})

// 格式化日期时间
const formatDateTime = (dateStr: string, startTime?: string, endTime?: string) => {
  if (!dateStr) return '待定'
  
  // 假设 dateStr 是 YYYY-MM-DD
  let formattedDate = dateStr
  try {
    const d = new Date(dateStr)
    const year = d.getFullYear()
    const month = d.getMonth() + 1
    const day = d.getDate()
    const weekMap = ['日', '一', '二', '三', '四', '五', '六']
    const week = weekMap[d.getDay()]
    formattedDate = `${year}年${month}月${day}日 (周${week})`
  } catch {
    // 忽略解析错误
  }

  if (startTime && endTime) {
    return `${formattedDate} ${startTime} - ${endTime}`
  } else if (startTime) {
    return `${formattedDate} ${startTime}`
  }
  
  return formattedDate
}

// 获取活动类型标签
const getActivityTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    'activity': '活动',
    'training': '培训',
    'workshop': '工作坊',
    'empowerment': '赋能'
  }
  return map[type] || '活动'
}

// 是否可以报名
const canRegister = computed(() => {
  if (activityData.value.status !== 'upcoming') return false
  if (activityData.value.maxParticipants && 
      activityData.value.maxParticipants > 0 && 
      (activityData.value.currentParticipants || 0) >= activityData.value.maxParticipants) {
    return false
  }
  return true
})

// 获取不可报名原因
const getDisabledReason = () => {
  if (activityData.value.status === 'ongoing') return '活动进行中'
  if (activityData.value.status === 'ended') return '活动已结束'
  if (activityData.value.maxParticipants && 
      activityData.value.maxParticipants > 0 && 
      (activityData.value.currentParticipants || 0) >= activityData.value.maxParticipants) {
    return '名额已满'
  }
  return '无法报名'
}

// 是否有权限编辑/删除
const canEditActivity = computed(() => {
  // 管理员、工具Owner、或者活动的创建者
  return isAdmin.value || isToolOwner.value || (currentUserId.value === activityData.value.userId)
})

// 加载活动数据
const loadActivity = async () => {
  loading.value = true
  try {
    const activityId = route.params.id
    if (!activityId || (Array.isArray(activityId) && activityId.length === 0)) {
      ElMessage.error('活动ID不存在')
      router.push(ROUTES.AGENT)
      return
    }

    const activityIdNum = Number(Array.isArray(activityId) ? activityId[0] : activityId)
    if (isNaN(activityIdNum)) {
      ElMessage.error('活动ID格式错误')
      router.push(ROUTES.AGENT)
      return
    }
    
    const response = await getActivityDetail(activityIdNum)
    activityData.value = response.data
    
    // 检查报名状态
    isRegistered.value = activityData.value.isJoined || false
    
    // 检查工具Owner权限
    if (activityData.value.toolId) {
      try {
        const ownerResponse = await checkOwnerPermission(activityData.value.toolId)
        isToolOwner.value = ownerResponse.data.isOwner || false
      } catch (error) {
        console.error('检查工具Owner权限失败:', error)
        isToolOwner.value = false
      }
    }
  } catch (error: unknown) {
    console.error('加载活动失败:', error)
    ElMessage.error((error as Error).message || '加载活动失败')
    router.push(ROUTES.AGENT)
  } finally {
    loading.value = false
  }
}

// 返回
const handleBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push(ROUTES.AGENT)
  }
}

// 报名状态
const isRegistered = ref(false)

// 报名参加
const handleRegister = async () => {
  if (isRegistered.value) {
    ElMessage.warning('您已经报名参加此活动')
    return
  }
  
  registering.value = true
  try {
    await joinActivity(activityData.value.id)
    // 后端返回 ActivityJoinResponseVO，没有joined字段，成功即为报名成功
    isRegistered.value = true
    // 手动更新人数
    activityData.value.currentParticipants = (activityData.value.currentParticipants || 0) + 1
    
    ElMessage.success('报名成功！')
    
    // 触发活动报名更新事件（用于更新个人中心的活动数量）
    window.dispatchEvent(new CustomEvent('activityRegistered', {
      detail: {
        activityId: activityData.value.id,
        registered: true
      }
    }))
  } catch (error: unknown) {
    console.error('报名失败:', error)
    ElMessage.error((error as Error).message || '报名失败，请稍后重试')
  } finally {
    registering.value = false
  }
}

// 取消报名
const handleCancelRegister = async () => {
  ElMessageBox.confirm('确定要取消报名吗？', '提示', {
    confirmButtonText: '确定取消',
    cancelButtonText: '保持报名',
    type: 'warning'
  }).then(async () => {
    canceling.value = true
    try {
      await cancelJoin(activityData.value.id)
      isRegistered.value = false
      // 手动更新人数
      activityData.value.currentParticipants = Math.max(0, (activityData.value.currentParticipants || 0) - 1)
      
      ElMessage.success('已取消报名')
      
      // 触发活动报名更新事件
      window.dispatchEvent(new CustomEvent('activityRegistered', {
        detail: {
          activityId: activityData.value.id,
          registered: false
        }
      }))
    } catch (error: unknown) {
      console.error('取消报名失败:', error)
      ElMessage.error((error as Error).message || '取消报名失败，请稍后重试')
    } finally {
      canceling.value = false
    }
  }).catch(() => {
    // 用户选择保持报名
  })
}

// 编辑活动
const handleEdit = () => {
  // 跳转到活动创建页面，传递编辑参数
  const activityId = activityData.value.id.toString()
  
  // 将活动数据存储到sessionStorage，以便编辑页面直接使用
  sessionStorage.setItem('editing_activity', JSON.stringify(activityData.value))
  
  router.push({
    path: '/activity/create',
    query: {
      edit: 'true',
      id: activityId
    }
  })
}

// 删除活动
const handleDelete = () => {
  ElMessageBox.confirm('确定要删除这个活动吗？删除后无法恢复。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
    distinguishCancelAndClose: true
  }).then(async () => {
    try {
      await deleteActivity(activityData.value.id)
      ElMessage.success('活动已删除')
      router.push(ROUTES.AGENT)
    } catch (error: unknown) {
      console.error('删除失败:', error)
      ElMessage.error((error as Error).message || '删除失败，请稍后重试')
    }
  }).catch(() => {
    // 用户取消
  })
}

onMounted(async () => {
  try {
    // 获取当前用户信息
    const response = await getCurrentUser()
    const user = response.data
    currentUserId.value = user.id
    isAdmin.value = user.roles?.includes('admin') || false
  } catch (error) {
    console.warn('获取当前用户信息失败:', error)
  }
  
  await loadActivity()
})
</script>

<style scoped lang="scss">
.activity-detail-view {
  min-height: 100vh;
  padding: 20px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
}

.detail-container {
  max-width: 1200px;
  margin: 0 auto;
}

.back-button-wrapper {
  margin-bottom: 20px;
}

.back-button {
  color: #606266;
  padding: 0;

  &:hover {
    color: #409eff;
  }
}

.activity-main {
  background: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.activity-header {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);

  .activity-title-section {
    margin-bottom: 20px;

    .activity-title {
      margin: 0;
      font-size: 32px;
      font-weight: 700;
      color: #333;
      line-height: 1.4;
    }
  }

  .activity-info {
    display: flex;
    flex-direction: column;
    gap: 20px;

    .tool-info {
      display: flex;
      align-items: center;
      gap: 12px;
      
      .type-tag {
        font-weight: 600;
      }
    }
    
    .info-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 16px;
      margin-top: 8px;
    }
    
    .info-card {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 16px;
      border-radius: 12px;
      background: #f8fafc;
      border: 1px solid #e2e8f0;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      }
      
      .card-icon {
        font-size: 24px;
        padding: 10px;
        border-radius: 10px;
        background: #fff;
      }
      
      .card-content {
        flex: 1;
        overflow: hidden;
      }
      
      .card-label {
        font-size: 12px;
        color: #64748b;
        margin-bottom: 4px;
      }
      
      .card-value {
        font-size: 16px;
        font-weight: 600;
        color: #1e293b;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      
      &.time-card {
        background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
        border-color: #bfdbfe;
        .card-icon { color: #3b82f6; }
      }
      
      &.location-card {
        background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
        border-color: #bbf7d0;
        .card-icon { color: #22c55e; }
        .meeting-link { font-size: 12px; margin-left: 4px; }
      }
      
      &.speaker-card {
        background: linear-gradient(135deg, #fff7ed 0%, #ffedd5 100%);
        border-color: #fed7aa;
        .card-icon { color: #f97316; }
        .speaker-title { font-size: 12px; color: #666; font-weight: normal; }
      }
      
      &.participants-card {
        background: linear-gradient(135deg, #fdf4ff 0%, #fae8ff 100%);
        border-color: #f5d0fe;
        .card-icon { color: #d946ef; }
        .max-participants { color: #999; font-size: 14px; font-weight: normal; }
      }
    }
  }
}

.activity-cover {
  margin-bottom: 32px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

  img {
    width: 100%;
    max-height: 500px;
    object-fit: cover;
    display: block;
  }
}

.activity-content {
  margin-bottom: 32px;
  
  h3 {
    font-size: 20px;
    font-weight: 600;
    margin-bottom: 16px;
    padding-left: 12px;
    border-left: 4px solid #409eff;
  }

  .content-html {
    font-size: 16px;
    line-height: 1.8;
    color: #333;
    padding: 24px;
    background: #fff;
    border-radius: 12px;
    border: 1px solid #eef2f6;

    :deep(p) {
      margin: 1em 0;
    }

    :deep(h1), :deep(h2), :deep(h3) {
      margin: 1.5em 0 0.8em;
      font-weight: 600;
    }

    :deep(img) {
      max-width: 100%;
      height: auto;
      border-radius: 8px;
      margin: 1em 0;
    }

    :deep(ul), :deep(ol) {
      padding-left: 2em;
      margin: 1em 0;
    }

    :deep(blockquote) {
      border-left: 4px solid #409eff;
      padding-left: 1em;
      margin: 1em 0;
      color: #666;
      font-style: italic;
      background: #f8fafc;
      padding: 12px 16px;
      border-radius: 0 4px 4px 0;
    }
  }
}

.activity-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 32px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);

  .actions-left {
    flex: 1;
    display: flex;
    justify-content: center;
  }

  .actions-right {
    display: flex;
    gap: 12px;
  }

  .registered-actions {
    display: flex;
    gap: 16px;
    align-items: center;
  }

  .register-button {
    padding: 16px 48px;
    font-size: 18px;
    font-weight: 600;
    transition: all 0.3s ease;
    
    &:hover:not(:disabled) {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
    }
    
    &.registered {
      cursor: default;
    }
    
    &.disabled {
      cursor: not-allowed;
    }
  }

  .cancel-button {
    padding: 16px 32px;
    font-size: 16px;
    font-weight: 500;
  }
}

@media (max-width: 768px) {
  .activity-main {
    padding: 20px;
  }

  .activity-header {
    .activity-title-section {
      .activity-title {
        font-size: 24px;
      }
    }
    
    .info-grid {
      grid-template-columns: 1fr;
    }
  }
  
  .activity-actions {
    flex-direction: column;
    gap: 20px;
    
    .actions-right {
      width: 100%;
      justify-content: center;
    }
  }
}
</style>
