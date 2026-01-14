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

        <!-- 活动时间 -->
        <el-form-item label="活动时间" prop="date">
          <el-date-picker
            v-model="formData.date"
            type="datetime"
            placeholder="选择活动时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            style="width: 100%"
            :key="datePickerKey"
          />
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
import { createActivity, updateActivity, getTools, getActivityDetail } from '../mock'
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
const datePickerKey = ref(0) // 用于强制重新渲染日期选择器

// 是否为编辑模式
const isEditMode = computed(() => {
  return route.query.edit === 'true' && route.query.id
})

// 所有工具列表（包括扶摇Agent应用）
const allToolsList = ref<any[]>([])

// 加载工具列表
const loadToolsList = async () => {
  try {
    const response = await getTools()
    // getTools() 返回 { list: ToolItem[] }，需要使用 response.list
    const tools = (response.list || []).map((item: any) => ({
      id: item.id,
      name: item.name,
      desc: item.desc || item.description || '',
      logo: item.logo || item.icon || '',
      logoType: 'url' as const,
      color: item.color || '#4096ff',
      link: item.link || `/tools?toolId=${item.id}`
    }))
    
    // 确保扶摇Agent应用不在列表中（避免重复）
    const filteredTools = tools.filter(t => t.id !== -1 && t.name !== '扶摇Agent应用')
    
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
  cover: '',
  content: ''
})

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入活动标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
  toolId: [{ required: true, message: '请选择工具', trigger: 'change' }],
  date: [{ required: true, message: '请选择活动时间', trigger: 'change' }],
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
const handleCoverChange = async (file: any) => {
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
    // 首先尝试从sessionStorage获取（从活动详情页传递过来的数据）
    const editingActivityStr = sessionStorage.getItem('editing_activity')
    if (editingActivityStr) {
      try {
        const editingActivity = JSON.parse(editingActivityStr)
        console.log('从sessionStorage获取活动数据:', editingActivity)
        
        // 验证ID是否匹配
        const editingId = typeof editingActivity.id === 'number' 
          ? editingActivity.id.toString() 
          : String(editingActivity.id)
        
        if (editingId === String(activityId)) {
          // ID匹配，使用sessionStorage中的数据
          console.log('使用sessionStorage中的活动数据')
          
          // 如果sessionStorage中的数据没有toolId，需要从toolName查找
          if (!editingActivity.toolId && editingActivity.toolName) {
            const tool = allToolsList.value.find(t => t.name === editingActivity.toolName)
            if (tool) {
              editingActivity.toolId = tool.id
              console.log('从toolName找到toolId:', tool.id)
            }
          }
          
          fillFormData(editingActivity)
          // 清除sessionStorage
          sessionStorage.removeItem('editing_activity')
          return
        } else {
          console.warn('sessionStorage中的活动ID不匹配，继续从mock API查找')
        }
      } catch (e) {
        console.warn('解析sessionStorage数据失败:', e)
      }
    }
    
    // 从mock API获取活动数据
    const activityIdNum = Number(activityId)
    console.log('从mock API获取活动数据，ID:', activityIdNum)
    
    const activity = await getActivityDetail(activityIdNum)
    
    if (activity) {
      console.log('从mock API找到活动:', activity)
      fillFormData(activity)
    } else {
      console.error('未找到活动')
      ElMessage.error(`活动不存在，ID: ${activityId}`)
    }
  } catch (error: any) {
    console.error('加载活动失败:', error)
    ElMessage.error(error.message || '加载活动失败')
  }
}

// 填充表单数据的辅助函数
const fillFormData = (activity: any) => {
  console.log('填充表单数据，活动:', activity)
  console.log('活动标题:', activity.title)
  console.log('活动工具ID:', activity.toolId, '类型:', typeof activity.toolId)
  console.log('活动时间:', activity.date, '类型:', typeof activity.date)
  console.log('活动封面:', activity.cover ? '有封面' : '无封面')
  console.log('活动内容长度:', activity.content ? activity.content.length : 0)
  
  // 处理工具ID - 确保类型正确
  let toolId = null
  if (activity.toolId !== undefined && activity.toolId !== null) {
    // 如果是数字，直接使用
    if (typeof activity.toolId === 'number') {
      toolId = activity.toolId
    } else {
      // 如果是字符串，尝试转换为数字
      const parsed = Number(activity.toolId)
      toolId = isNaN(parsed) ? null : parsed
    }
  }
  
  // 处理日期 - 确保格式正确（YYYY-MM-DD HH:mm）
  let date = ''
  if (activity.date) {
    console.log('原始日期值:', activity.date, '类型:', typeof activity.date)
    
    // 如果已经是正确格式，直接使用
    if (typeof activity.date === 'string') {
      // 检查是否是 YYYY-MM-DD HH:mm 格式
      if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}$/.test(activity.date)) {
        date = activity.date
        console.log('日期格式正确，直接使用:', date)
      } 
      // 检查是否是 YYYY-MM-DD HH:mm:ss 格式（去掉秒）
      else if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(activity.date)) {
        date = activity.date.substring(0, 16)
        console.log('日期格式（带秒），截取:', date)
      }
      // 处理中文日期格式（如：2024年5月10日 14:00）
      else if (/^\d{4}年\d{1,2}月\d{1,2}日/.test(activity.date)) {
        try {
          // 提取年月日和时分
          const match = activity.date.match(/(\d{4})年(\d{1,2})月(\d{1,2})日\s*(\d{1,2}):(\d{1,2})/)
          if (match) {
            const year = match[1]
            const month = String(match[2]).padStart(2, '0')
            const day = String(match[3]).padStart(2, '0')
            const hours = String(match[4] || '00').padStart(2, '0')
            const minutes = String(match[5] || '00').padStart(2, '0')
            date = `${year}-${month}-${day} ${hours}:${minutes}`
            console.log('中文日期格式转换成功:', date)
          }
        } catch (e) {
          console.warn('中文日期格式转换失败:', e)
        }
      }
      // 尝试转换日期格式
      else {
        try {
          const dateObj = new Date(activity.date)
          if (!isNaN(dateObj.getTime())) {
            // 格式化为 YYYY-MM-DD HH:mm
            const year = dateObj.getFullYear()
            const month = String(dateObj.getMonth() + 1).padStart(2, '0')
            const day = String(dateObj.getDate()).padStart(2, '0')
            const hours = String(dateObj.getHours()).padStart(2, '0')
            const minutes = String(dateObj.getMinutes()).padStart(2, '0')
            date = `${year}-${month}-${day} ${hours}:${minutes}`
            console.log('日期格式转换成功:', date)
          } else {
            console.warn('日期无效:', activity.date)
            date = ''
          }
        } catch (e) {
          console.warn('日期格式转换失败:', e, '原始值:', activity.date)
          date = ''
        }
      }
    } else {
      // 如果不是字符串，尝试转换
      try {
        const dateObj = new Date(activity.date)
        if (!isNaN(dateObj.getTime())) {
          const year = dateObj.getFullYear()
          const month = String(dateObj.getMonth() + 1).padStart(2, '0')
          const day = String(dateObj.getDate()).padStart(2, '0')
          const hours = String(dateObj.getHours()).padStart(2, '0')
          const minutes = String(dateObj.getMinutes()).padStart(2, '0')
          date = `${year}-${month}-${day} ${hours}:${minutes}`
          console.log('日期格式转换成功（非字符串）:', date)
        } else {
          console.warn('日期无效（非字符串）:', activity.date)
          date = ''
        }
      } catch (e) {
        console.warn('日期格式转换失败（非字符串）:', e)
        date = ''
      }
    }
  }
  
  console.log('最终日期值:', date)
  
  // 填充表单数据 - 使用响应式更新
  formData.value.title = activity.title || ''
  formData.value.type = activity.type || 'activity'
  formData.value.toolId = toolId
  formData.value.cover = activity.cover || ''
  formData.value.content = activity.content || ''
  
  // 日期需要特殊处理，确保格式正确并触发响应式更新
  if (date) {
    // 先清空，再设置，确保触发响应式更新
    formData.value.date = ''
    // 强制重新渲染日期选择器
    datePickerKey.value++
    nextTick(() => {
      formData.value.date = date
      console.log('日期已设置（nextTick）:', formData.value.date)
      // 再次强制更新，确保日期选择器显示
      nextTick(() => {
        if (formData.value.date !== date) {
          formData.value.date = date
          datePickerKey.value++
        }
      })
    })
  } else {
    formData.value.date = ''
    console.warn('日期为空，无法设置')
  }
  
  console.log('表单数据已填充:', formData.value)
  console.log('工具ID:', formData.value.toolId, '类型:', typeof formData.value.toolId)
  console.log('日期:', formData.value.date, '类型:', typeof formData.value.date, '长度:', formData.value.date?.length)
  
  // 确保工具选择框能正确显示
  nextTick(() => {
    console.log('工具列表:', allToolsList.value)
    console.log('当前工具ID:', formData.value.toolId)
    const tool = allToolsList.value.find(t => t.id === formData.value.toolId)
    console.log('找到的工具:', tool)
  })
  
  // 设置编辑器内容
  if (editorRef.value) {
    // 等待编辑器完全准备好
    nextTick(() => {
      setTimeout(() => {
        if (editorRef.value) {
          if (activity.content) {
            console.log('设置编辑器内容，长度:', activity.content.length)
            editorRef.value.setHtml(activity.content)
          } else {
            console.warn('活动内容为空')
            editorRef.value.setHtml('')
          }
        } else {
          console.error('编辑器实例不存在')
        }
      }, 200)
    })
  } else {
    console.warn('编辑器还未创建，等待创建...')
    // 如果编辑器还没创建，等待一下再试
    setTimeout(() => {
      if (editorRef.value && activity.content) {
        editorRef.value.setHtml(activity.content)
      }
    }, 1000)
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

    publishing.value = true

    // 判断是创建还是更新
    if (isEditMode.value) {
      // 编辑模式：更新现有活动
      const activityId = Number(route.query.id)
      await updateActivity(activityId, {
        title: formData.value.title,
        toolId: formData.value.toolId || undefined,
        type: formData.value.type,
        date: formData.value.date,
        cover: formData.value.cover,
        content: formData.value.content
      })
    } else {
      // 新建模式：创建新活动
      await createActivity({
        title: formData.value.title,
        toolId: formData.value.toolId || undefined,
        type: formData.value.type,
        date: formData.value.date,
        cover: formData.value.cover,
        content: formData.value.content
      })
    }

    publishing.value = false
    
    ElMessage.success(isEditMode.value ? '活动更新成功！' : '活动发布成功！')
    
    // 跳转到活动详情页或管理页面
    if (isEditMode.value) {
      router.push(`/activity/${route.query.id}`)
    } else {
      router.push('/admin')
    }
  } catch (error: any) {
    console.error('发布活动失败:', error)
    ElMessage.error(error.message || '发布活动失败')
    publishing.value = false
  }
}

// 返回
const handleBack = () => {
  router.back()
}

// 组件挂载时，如果是编辑模式且编辑器还未创建，也尝试加载
onMounted(async () => {
  // 加载工具列表
  await loadToolsList()
  // 如果URL中有toolId参数，自动选中对应工具
  const toolIdParam = route.query.toolId
  if (toolIdParam && !isEditMode.value) {
    const toolId = Number(toolIdParam)
    if (!isNaN(toolId)) {
      // 检查工具是否存在于列表中
      const tool = allToolsList.value.find(t => t.id === toolId)
      if (tool) {
        formData.value.toolId = toolId
        console.log('从URL参数自动选中工具:', tool.name, 'ID:', toolId)
      } else {
        console.warn('URL参数中的toolId不存在于工具列表中:', toolId)
      }
    }
  }
  
  if (isEditMode.value) {
    console.log('组件挂载，编辑模式，准备加载活动数据')
    // 如果编辑器已经创建，直接加载
    if (editorRef.value) {
      nextTick(() => {
        loadActivityForEdit()
      })
    }
    // 否则等待编辑器创建（在 handleEditorCreated 中处理）
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
</style>

