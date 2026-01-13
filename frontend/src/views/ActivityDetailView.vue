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
            </div>
            <!-- 突出显示活动时间 -->
            <div class="activity-time-highlight">
              <el-icon class="time-icon"><Calendar /></el-icon>
              <span class="time-label">活动时间：</span>
              <span class="time-value">{{ activityData.date }}</span>
            </div>
          </div>
        </div>

        <!-- 活动封面 -->
        <div class="activity-cover" v-if="activityData.cover">
          <img :src="activityData.cover" :alt="activityData.title" />
        </div>

        <!-- 活动内容 -->
        <div class="activity-content">
          <div class="content-html" v-html="activityData.content"></div>
        </div>

        <!-- 操作按钮 -->
        <div class="activity-actions">
          <div class="actions-left">
            <el-button
              v-if="!isRegistered"
              type="primary"
              size="large"
              @click="handleRegister"
              :loading="registering"
              class="register-button"
            >
              <el-icon><UserFilled /></el-icon>
              报名参加
            </el-button>
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
          <div class="actions-right" v-if="isAdmin || isToolOwner">
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
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Calendar, UserFilled, Edit, Delete, Check, Close } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getActivityDetail, registerActivity, cancelRegistration, deleteActivity, getCurrentUser, checkToolOwner } from '../mock'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const registering = ref(false)
const canceling = ref(false)

// 管理员状态和工具Owner状态
const isAdmin = ref(false)
const isToolOwner = ref(false)

// 活动数据
const activityData = ref({
  id: 0,
  title: '',
  toolName: '',
  toolId: null as number | null, // 工具ID，用于编辑
  date: '',
  cover: '',
  content: '',
  authorId: 1 // 活动发布者ID
})

// 加载活动数据
const loadActivity = async () => {
  loading.value = true
  try {
    const activityId = route.params.id
    if (!activityId || (Array.isArray(activityId) && activityId.length === 0)) {
      ElMessage.error('活动ID不存在')
      router.push('/agent')
      return
    }

    const activityIdNum = Number(Array.isArray(activityId) ? activityId[0] : activityId)
    if (isNaN(activityIdNum)) {
      ElMessage.error('活动ID格式错误')
      router.push('/agent')
      return
    }
    
    const activity = await getActivityDetail(activityIdNum)
    
    // 格式化日期
    const formatDate = (date: string | Date) => {
      if (!date) return ''
      const d = typeof date === 'string' ? new Date(date) : date
      return d.toLocaleString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
    
    activityData.value = {
      id: activity.id,
      title: activity.title,
      toolName: activity.toolName || '',
      toolId: activity.toolId || null,
      date: formatDate(activity.date),
      cover: activity.cover || '',
      content: activity.content || '',
      authorId: activity.authorId
    }
    
    // 检查报名状态
    isRegistered.value = activity.isRegistered || false
    
    // 检查管理员权限和工具Owner权限
    if (activity.toolId) {
      try {
        const ownerResponse = await checkToolOwner(activity.toolId)
        isToolOwner.value = ownerResponse.isOwner || false
      } catch (error) {
        console.error('检查工具Owner权限失败:', error)
        isToolOwner.value = false
      }
    }
    
    // 检查管理员权限
    isAdmin.value = activity.canDelete || activity.canEdit || false
  } catch (error: any) {
    console.error('加载活动失败:', error)
    ElMessage.error(error.message || '加载活动失败')
    router.push('/agent')
  } finally {
    loading.value = false
  }
}

// 返回
const handleBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/agent')
  }
}

// 报名状态
const isRegistered = ref(false)

// 检查是否已报名（已在loadActivity中从API获取）
const checkIfRegistered = () => {
  // 报名状态已从API返回的isRegistered字段中获取
}

// 报名参加
const handleRegister = async () => {
  if (isRegistered.value) {
    ElMessage.warning('您已经报名参加此活动')
    return
  }
  
  registering.value = true
  try {
    const response = await registerActivity(activityData.value.id)
    isRegistered.value = response.registered
    ElMessage.success('报名成功！')
    
    // 触发活动报名更新事件（用于更新个人中心的活动数量）
    window.dispatchEvent(new CustomEvent('activityRegistered', {
      detail: {
        activityId: activityData.value.id,
        registered: response.registered
      }
    }))
  } catch (error: any) {
    console.error('报名失败:', error)
    ElMessage.error(error.message || '报名失败，请稍后重试')
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
      await cancelRegistration(activityData.value.id)
      isRegistered.value = false
      ElMessage.success('已取消报名')
      
      // 触发活动报名更新事件
      window.dispatchEvent(new CustomEvent('activityRegistered', {
        detail: {
          activityId: activityData.value.id,
          registered: false
        }
      }))
    } catch (error: any) {
      console.error('取消报名失败:', error)
      ElMessage.error(error.message || '取消报名失败，请稍后重试')
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
  // 确保ID是数字类型，然后转为字符串
  const activityId = typeof activityData.value.id === 'number' 
    ? activityData.value.id.toString() 
    : String(activityData.value.id)
  
  console.log('跳转到编辑页面，活动ID:', activityId)
  console.log('当前活动数据:', activityData.value)
  
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
      router.push('/agent')
    } catch (error: any) {
      console.error('删除失败:', error)
      ElMessage.error(error.message || '删除失败，请稍后重试')
    }
  }).catch(() => {
    // 用户取消
  })
}

onMounted(async () => {
  try {
    // 获取当前用户信息（用于判断管理员权限）
    const user = await getCurrentUser()
    isAdmin.value = user.roles?.includes('admin') || false
  } catch (error) {
    console.warn('获取当前用户信息失败:', error)
  }
  
  await loadActivity()
  checkIfRegistered() // 检查报名状态
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
    gap: 16px;

    .tool-info {
      display: flex;
      align-items: center;
    }

    .activity-time-highlight {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 16px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 12px;
      color: #fff;
      font-size: 18px;
      font-weight: 600;

      .time-icon {
        font-size: 24px;
      }

      .time-label {
        font-weight: 500;
      }

      .time-value {
        font-weight: 700;
        font-size: 20px;
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
    height: auto;
    display: block;
  }
}

.activity-content {
  margin-bottom: 32px;

  .content-html {
    font-size: 16px;
    line-height: 1.8;
    color: #333;

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
    
    &.registered {
      cursor: default;
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

    .activity-info {
      .activity-time-highlight {
        font-size: 16px;

        .time-value {
          font-size: 18px;
        }
      }
    }
  }
}
</style>

