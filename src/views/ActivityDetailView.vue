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
            <div class="tool-info">
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
              :type="isRegistered ? 'success' : 'primary'"
              size="large"
              @click="handleRegister"
              :loading="registering"
              :disabled="isRegistered"
              class="register-button"
            >
              <el-icon><UserFilled /></el-icon>
              {{ isRegistered ? '已报名' : '报名参加' }}
            </el-button>
          </div>
          <div class="actions-right" v-if="isAdmin">
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
import { ArrowLeft, Calendar, UserFilled, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const registering = ref(false)

// 模拟管理员状态（实际应该从用户信息中获取）
const isAdmin = ref(true)

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
    
    // 这里应该调用API获取活动数据
    // const response = await getActivity(activityId)
    
    // 模拟数据
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 从localStorage获取活动数据（实际应该从API获取）
    const activities = JSON.parse(localStorage.getItem('admin_activities') || '[]')
    const activity = activities.find((a: any) => a.id === Number(activityId))
    
      if (activity) {
        activityData.value = {
          id: activity.id,
          title: activity.title || '活动标题',
          toolName: activity.toolName || '工具名称',
          toolId: activity.toolId, // 保存toolId以便编辑时使用
          date: activity.date || '',
          cover: activity.cover || '',
          content: activity.content || '',
          authorId: activity.authorId || 1
        }
      } else {
        // 如果没有找到，尝试从模拟数据中查找（兼容旧数据）
        const mockActivities = [
          {
            id: 1,
            title: '扶摇 Agent 开发训练营',
            toolName: '扶摇Agent应用',
            date: '2024年5月10日 14:00',
            cover: 'https://picsum.photos/800/400?random=30',
            content: '<p>系统学习扶摇 Agent 编排引擎的使用方法和实战技巧，从入门到精通。</p>'
          }
        ]
        const mockActivity = mockActivities.find(a => a.id === Number(activityId))
        
        if (mockActivity) {
          activityData.value = mockActivity
        } else {
          ElMessage.error('活动不存在')
          router.push('/agent')
        }
      }
  } catch (error) {
    console.error('加载活动失败:', error)
    ElMessage.error('加载活动失败')
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

// 检查是否已报名
const checkIfRegistered = () => {
  try {
    const currentUserId = 1 // 当前用户ID（实际应该从登录状态获取）
    const registeredKey = `user_${currentUserId}_registered_activities`
    const registeredStr = localStorage.getItem(registeredKey)
    
    if (registeredStr) {
      try {
        const registered = JSON.parse(registeredStr)
        isRegistered.value = registered.some((act: any) => act.id === activityData.value.id)
      } catch (e) {
        console.warn('检查报名状态失败:', e)
      }
    }
  } catch (error) {
    console.error('检查报名状态失败:', error)
  }
}

// 报名参加
const handleRegister = async () => {
  if (isRegistered.value) {
    ElMessage.warning('您已经报名参加此活动')
    return
  }
  
  registering.value = true
  try {
    // 这里应该调用API报名
    // await registerActivity(activityData.value.id)
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 保存到localStorage
    const currentUserId = 1 // 当前用户ID（实际应该从登录状态获取）
    const registeredKey = `user_${currentUserId}_registered_activities`
    
    // 从localStorage读取已报名的活动列表
    const registeredStr = localStorage.getItem(registeredKey)
    let registered: any[] = []
    
    if (registeredStr) {
      try {
        registered = JSON.parse(registeredStr)
      } catch (e) {
        console.warn('解析报名列表失败:', e)
        registered = []
      }
    }
    
    // 检查是否已存在
    const exists = registered.some((act: any) => act.id === activityData.value.id)
    if (!exists) {
      // 添加活动信息
      const activityToRegister = {
        id: activityData.value.id,
        title: activityData.value.title,
        description: activityData.value.content?.replace(/<[^>]*>/g, '').substring(0, 200) || '',
        date: activityData.value.date,
        location: '',
        image: activityData.value.cover || '',
        toolName: activityData.value.toolName || '',
        registerTime: new Date().toISOString(), // 报名时间
        status: 'registered' // 报名状态
      }
      
      registered.unshift(activityToRegister) // 添加到开头
      isRegistered.value = true
      
      // 保存到localStorage
      localStorage.setItem(registeredKey, JSON.stringify(registered))
      
      // 触发活动报名更新事件
      window.dispatchEvent(new CustomEvent('activityRegistered', {
        detail: { userId: currentUserId, activities: registered }
      }))
    }
    
    // 发送消息给活动发布者
    const authorId = activityData.value.authorId // 活动发布者ID
    const currentUserName = '当前用户' // 当前用户名（实际应该从登录状态获取）
    
    // 导入消息工具函数
    const { sendActivityRegistrationMessage } = await import('../utils/message')
    sendActivityRegistrationMessage(
      activityData.value.id,
      activityData.value.title,
      authorId,
      currentUserId,
      currentUserName
    )
    
    ElMessage.success('报名成功！')
  } catch (error) {
    console.error('报名失败:', error)
    ElMessage.error('报名失败，请稍后重试')
  } finally {
    registering.value = false
  }
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
      // 这里应该调用API删除活动
      // await deleteActivity(activityData.value.id)
      
      // 从localStorage删除
      const activities = JSON.parse(localStorage.getItem('admin_activities') || '[]')
      const filtered = activities.filter((a: any) => a.id !== activityData.value.id)
      localStorage.setItem('admin_activities', JSON.stringify(filtered))
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      ElMessage.success('活动已删除')
      router.push('/agent')
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }).catch(() => {
    // 用户取消
  })
}

onMounted(async () => {
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

  .register-button {
    padding: 16px 48px;
    font-size: 18px;
    font-weight: 600;
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

