<template>
  <div class="activity-create-view">
    <div class="create-container">
      <div class="create-header">
        <el-button
          class="back-button"
          text
          :icon="ArrowLeft"
          @click="handleBack"
        >
          返回
        </el-button>
        <h2>{{ isEditMode ? '编辑活动' : '发布活动' }}</h2>
      </div>

      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <!-- 活动标题 -->
        <el-form-item label="活动标题" prop="title">
          <el-input
            v-model="formData.title"
            placeholder="请输入活动标题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <!-- 活动类型 -->
        <el-form-item label="活动类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio label="activity">活动</el-radio>
            <el-radio label="training">培训</el-radio>
            <el-radio label="workshop">工作坊</el-radio>
            <el-radio label="empowerment">赋能</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 选择工具 -->
        <el-form-item label="选择工具" prop="toolId">
          <el-select
            v-model="formData.toolId"
            placeholder="请选择工具"
            style="width: 100%"
          >
            <el-option
              v-for="tool in allToolsList"
              :key="tool.id"
              :label="tool.name"
              :value="tool.id"
            />
          </el-select>
        </el-form-item>

        <!-- 活动日期 -->
        <el-form-item label="活动日期" prop="date">
          <el-date-picker
            v-model="formData.date"
            type="date"
            placeholder="选择活动日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>

        <!-- 活动时间 -->
        <el-form-item label="活动时间" required>
          <div class="time-range-container">
            <el-form-item prop="startTime" style="margin-bottom: 0; flex: 1;">
              <el-time-select
                v-model="formData.startTime"
                start="08:00"
                step="00:15"
                end="22:00"
                placeholder="开始时间"
                style="width: 100%"
              />
            </el-form-item>
            <span class="time-separator">-</span>
            <el-form-item prop="endTime" style="margin-bottom: 0; flex: 1;">
              <el-time-select
                v-model="formData.endTime"
                start="08:00"
                step="00:15"
                end="22:00"
                :min-time="formData.startTime"
                placeholder="结束时间"
                style="width: 100%"
              />
            </el-form-item>
          </div>
        </el-form-item>

        <!-- 活动地点 -->
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="formData.location" placeholder="请输入活动地点（如：会议室名称或'线上'）" />
        </el-form-item>

        <!-- 会议链接 -->
        <el-form-item label="会议链接" prop="meetingLink">
          <el-input v-model="formData.meetingLink" placeholder="请输入线上会议链接（选填）" />
        </el-form-item>

        <!-- 主讲人信息 -->
        <div class="form-row">
          <el-form-item label="主讲人" prop="speaker" style="flex: 1">
            <el-input v-model="formData.speaker" placeholder="请输入主讲人姓名" />
          </el-form-item>
          <el-form-item label="主讲人职称" prop="speakerTitle" style="flex: 1">
            <el-input v-model="formData.speakerTitle" placeholder="请输入主讲人职称" />
          </el-form-item>
        </div>

        <!-- 最大参与人数 -->
        <el-form-item label="最大人数" prop="maxParticipants">
          <el-input-number v-model="formData.maxParticipants" :min="0" :step="10" placeholder="0表示不限制" />
          <span class="form-tip">（0 或不填表示不限制人数）</span>
        </el-form-item>

        <!-- 封面图片 -->
        <el-form-item label="封面图片" prop="cover">
          <el-upload
            class="cover-uploader"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleCoverChange"
            :before-upload="beforeImageUpload"
            accept="image/*"
          >
            <div v-if="formData.cover" class="cover-image-wrapper">
              <img :src="formData.cover" class="cover-image" />
              <div class="cover-delete-overlay">
                <el-button
                  type="danger"
                  :icon="Delete"
                  circle
                  size="small"
                  @click.stop="formData.cover = ''"
                />
              </div>
            </div>
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <span class="upload-tip">建议尺寸 800x400，大小不超过 5MB</span>
        </el-form-item>

        <!-- 活动内容 -->
        <el-form-item label="活动内容" prop="content">
          <div class="editor-wrapper">
            <Toolbar
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="editorMode"
              class="editor-toolbar"
            />
            <Editor
              v-model="formData.content"
              :defaultConfig="editorConfig"
              :mode="editorMode"
              class="editor-content"
              @onCreated="handleEditorCreated"
              @onChange="handleEditorChange"
            />
          </div>
        </el-form-item>
      </el-form>

      <!-- 底部操作按钮 -->
      <div class="form-actions">
        <el-button @click="handleBack">取消</el-button>
        <el-button type="primary" @click="handlePublish" :loading="publishing">
          {{ isEditMode ? '更新活动' : '发布活动' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, shallowRef, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
// API 层 - 支持 Mock/Real API 自动切换
import { createActivity, updateActivity, getActivityDetail } from '../api/activities'
import type { ActivityDetail } from '../api/activities'
import { getTools } from '../api/home'
import {
  Plus,
  Delete,
  ArrowLeft
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const formRef = ref()
const publishing = ref(false)
const editorRef = shallowRef<IDomEditor>()
const editorMode = 'default'

// 是否为编辑模式
const isEditMode = computed(() => {
  return route.query.edit === 'true' && route.query.id
})

// 所有工具列表（包括扶摇Agent应用）
const allToolsList = ref<Array<{ id: number; name: string; desc?: string; logo?: string; logoType?: string; color?: string; link?: string }>>([])


// 加载工具列表
const loadToolsList = async () => {
  try {
    const response = await getTools()
    // getTools() 返回 ApiResponse<{ list: ToolItem[] }>，需要使用 response.data.list
    const tools = (response.data?.list || []).map((item: { id: number; name: string; desc?: string; description?: string; logo?: string; icon?: string; color?: string; link?: string }) => ({
      id: item.id,
      name: item.name,
      desc: item.desc || item.description || '',
      logo: item.logo || item.icon || '',
      logoType: 'url' as const,
      color: item.color || '#4096ff',
      link: item.link || `/tools?toolId=${item.id}`
    }))
    
    // 确保扶摇Agent应用不在列表中（避免重复）
    const filteredTools = tools.filter((t: { id: number; name: string }) => t.id !== -1 && t.name !== '扶摇Agent应用')
    
    // 添加扶摇Agent应用（始终显示在列表最前面）
    filteredTools.unshift({
      id: -1,
      name: '扶摇Agent应用',
      desc: 'Agent编排引擎',
      logo: '',
      logoType: 'url' as const,
      color: '#4096ff',
      link: '/agent'
    })
    
    allToolsList.value = filteredTools
  } catch (error) {
    console.error('加载工具列表失败:', error)
    allToolsList.value = []
  }
}

// 表单数据
const formData = ref({
  title: '',
  type: 'activity' as 'activity' | 'training' | 'workshop' | 'empowerment',
  toolId: null as number | null,
  date: '',
  startTime: '',
  endTime: '',
  location: '',
  meetingLink: '',
  speaker: '',
  speakerTitle: '',
  maxParticipants: 0,
  cover: '',
  content: ''
})

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入活动标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
  toolId: [{ required: true, message: '请选择工具', trigger: 'change' }],
  date: [{ required: true, message: '请选择活动日期', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
  cover: [{ required: true, message: '请上传封面图片', trigger: 'change' }],
  content: [{ required: true, message: '请输入活动内容', trigger: 'blur' }]
}

// 编辑器配置
const toolbarConfig: Partial<IToolbarConfig> = {
  excludeKeys: []
}

const editorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入活动内容...',
  readOnly: false,
  MENU_CONF: {
    uploadImage: {
      maxFileSize: 5 * 1024 * 1024,
      maxNumberOfFiles: 10,
      allowedFileTypes: ['image/*'],
      async customUpload(file: File, insertFn: (url: string, alt?: string, href?: string) => void) {
        const isImage = file.type.startsWith('image/')
        const isLt5M = file.size / 1024 / 1024 < 5

        if (!isImage) {
          ElMessage.error('只能上传图片文件！')
          return
        }
        if (!isLt5M) {
          ElMessage.error('图片大小不能超过 5MB！')
          return
        }

        try {
          const imageUrl = URL.createObjectURL(file)
          insertFn(imageUrl, file.name)
          ElMessage.success('图片插入成功')
        } catch {
          ElMessage.error('图片上传失败')
        }
      }
    }
  }
}

// 图片转Base64
const fileToBase64 = (file: File): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => resolve(reader.result as string)
    reader.onerror = reject
    reader.readAsDataURL(file)
  })
}

// 上传前验证
const beforeImageUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB！')
    return false
  }
  return true
}

// 封面图片选择
const handleCoverChange = async (file: { raw?: File }) => {
  if (!file.raw) return
  try {
    const base64 = await fileToBase64(file.raw)
    formData.value.cover = base64
    ElMessage.success('封面图片已加载')
  } catch (error) {
    console.error('图片处理失败:', error)
    ElMessage.error('图片处理失败')
  }
}

// 编辑器创建
const handleEditorCreated = (editor: IDomEditor) => {
  editorRef.value = editor
  
  // 如果是编辑模式，等待编辑器完全初始化后再加载活动数据
  if (isEditMode.value) {
    nextTick(() => {
      // 延迟加载，确保编辑器完全初始化
      setTimeout(() => {
        loadActivityForEdit()
      }, 500)
    })
  }
}

// 编辑器内容变化
const handleEditorChange = (editor: IDomEditor) => {
  formData.value.content = editor.getHtml()
}

// 加载活动数据用于编辑
const loadActivityForEdit = async () => {
  const activityId = route.query.id
  if (!activityId) {
    console.warn('活动ID不存在，query:', route.query)
    ElMessage.warning('活动ID不存在')
    return
  }

  try {
    // 从API获取活动数据
    const activityIdNum = Number(activityId)
    
    const response = await getActivityDetail(activityIdNum)
    const activity = response.data
    
    if (activity) {
      fillFormData(activity)
    } else {
      console.error('未找到活动')
      ElMessage.error(`活动不存在，ID: ${activityId}`)
    }
  } catch (error: unknown) {
    console.error('加载活动失败:', error)
    ElMessage.error((error as Error).message || '加载活动失败')
  }
}

// 填充表单数据的辅助函数
const fillFormData = (activity: ActivityDetail) => {
  formData.value.title = activity.title || ''
  formData.value.type = (activity.type || 'activity') as 'activity' | 'training' | 'workshop' | 'empowerment'
  formData.value.toolId = activity.toolId || null
  formData.value.cover = activity.cover || ''
  formData.value.content = activity.content || ''
  
  // 填充新增字段
  formData.value.date = activity.date || ''
  formData.value.startTime = activity.startTime || ''
  formData.value.endTime = activity.endTime || ''
  formData.value.location = activity.location || ''
  formData.value.meetingLink = activity.meetingLink || ''
  formData.value.speaker = activity.speaker || ''
  formData.value.speakerTitle = activity.speakerTitle || ''
  formData.value.maxParticipants = activity.maxParticipants || 0
  
  // 设置编辑器内容
  if (editorRef.value) {
    nextTick(() => {
      setTimeout(() => {
        if (editorRef.value && activity.content) {
          editorRef.value.setHtml(activity.content)
        }
      }, 200)
    })
  }
  
  ElMessage.success('活动数据已加载')
}

// 发布活动
const handlePublish = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    
    if (editorRef.value) {
      formData.value.content = editorRef.value.getHtml()
    }
    
    if (!formData.value.content || formData.value.content === '<p><br></p>') {
      ElMessage.error('请输入活动内容')
      return
    }

    if (!formData.value.toolId) {
      ElMessage.error('请选择工具')
      return
    }

    publishing.value = true

    const submitData = {
      title: formData.value.title,
      toolId: formData.value.toolId,
      type: formData.value.type,
      date: formData.value.date,
      startTime: formData.value.startTime,
      endTime: formData.value.endTime,
      location: formData.value.location,
      meetingLink: formData.value.meetingLink,
      speaker: formData.value.speaker,
      speakerTitle: formData.value.speakerTitle,
      maxParticipants: formData.value.maxParticipants || undefined,
      cover: formData.value.cover,
      content: formData.value.content
    }

    // 判断是创建还是更新
    if (isEditMode.value) {
      // 编辑模式：更新现有活动
      const activityId = Number(route.query.id)
      await updateActivity(activityId, submitData)
    } else {
      // 新建模式：创建新活动
      await createActivity(submitData)
    }

    publishing.value = false
    
    ElMessage.success(isEditMode.value ? '活动更新成功！' : '活动发布成功！')
    
    // 跳转到活动详情页或管理页面
    if (isEditMode.value) {
      router.push(`/activity/${route.query.id}`)
    } else {
      // router.push(ROUTES.ADMIN) // 原逻辑
      router.push(`/tools?toolId=${formData.value.toolId}&tab=activities`) // 跳转到对应工具的活动列表
    }
  } catch (error: unknown) {
    console.error('发布活动失败:', error)
    ElMessage.error((error as Error).message || '发布活动失败')
    publishing.value = false
  }
}

// 返回
const handleBack = () => {
  router.back()
}

// 组件挂载时
onMounted(async () => {
  // 加载工具列表
  await loadToolsList()
  
  // 如果URL中有toolId参数，自动选中对应工具
  const toolIdParam = route.query.toolId
  if (toolIdParam && !isEditMode.value) {
    const toolId = Number(toolIdParam)
    if (!isNaN(toolId)) {
      const tool = allToolsList.value.find(t => t.id === toolId)
      if (tool) {
        formData.value.toolId = toolId
      }
    }
  }
  
  if (isEditMode.value) {
    // 等待编辑器创建后再加载数据
    // 注意：数据加载会在 handleEditorCreated 中触发
  }
})

onBeforeUnmount(() => {
  // 清理编辑器
  if (editorRef.value) {
    editorRef.value.destroy()
  }
})
</script>

<style scoped lang="scss">
.activity-create-view {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24px;
}

.create-container {
  max-width: 1200px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  padding: 32px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.create-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 32px;
  
  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
  }
}

.cover-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: all 0.3s;
    width: 200px;
    height: 120px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    &:hover {
      border-color: #4096ff;
    }
  }
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c8c8c;
}

.cover-image-wrapper {
  width: 100%;
  height: 100%;
  position: relative;
  
  .cover-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .cover-delete-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s;
    
    &:hover {
      opacity: 1;
    }
  }
}

.upload-tip {
  display: block;
  margin-top: 8px;
  font-size: 12px;
  color: #8c8c8c;
}

.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.editor-toolbar {
  border-bottom: 1px solid #dcdfe6;
}

.editor-content {
  min-height: 400px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #e4e7ed;
}

.time-range-container {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
}

.time-separator {
  color: #909399;
}

.form-row {
  display: flex;
  gap: 20px;
  
  .el-form-item {
    margin-bottom: 18px;
  }
}

.form-tip {
  margin-left: 10px;
  font-size: 12px;
  color: #909399;
}
</style>
