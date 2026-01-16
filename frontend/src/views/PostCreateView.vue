<template>
  <div class="post-create-view">
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
        <h2>{{ isEditMode ? '编辑帖子' : '发布帖子' }}</h2>
      </div>

      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <!-- 专区选择 -->
        <el-form-item label="选择专区" prop="zone">
          <el-select
            v-model="formData.zone"
            placeholder="请选择专区"
            style="width: 100%"
            @change="handleZoneChange"
          >
            <el-option label="AI优秀实践" value="practices" />
            <el-option label="AI工具专区" value="tools" />
            <el-option label="扶摇Agent应用" value="agent" />
            <el-option label="赋能交流" value="empowerment" />
          </el-select>
        </el-form-item>

        <!-- 工具选择（仅AI工具专区显示，必填） -->
        <el-form-item
          v-if="formData.zone === 'tools'"
          label="选择工具"
          prop="toolId"
          :required="true"
        >
          <el-select
            v-model="formData.toolId"
            placeholder="请选择工具（必填）"
            style="width: 100%"
            @change="handleToolChange"
          >
            <el-option
              v-for="tool in tools"
              :key="tool.id"
              :label="tool.name"
              :value="tool.id"
            />
          </el-select>
        </el-form-item>

        <!-- 标题 -->
        <el-form-item label="帖子标题" prop="title">
          <el-input
            v-model="formData.title"
            placeholder="请输入帖子标题"
            maxlength="100"
            show-word-limit
            @input="handleAutoSave"
          />
        </el-form-item>

        <!-- 内容简介（必填） -->
        <el-form-item label="内容简介" prop="summary">
          <el-input
            v-model="formData.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入内容简介"
            maxlength="200"
            show-word-limit
            @input="handleAutoSave"
          />
        </el-form-item>

        <!-- 标签选择 -->
        <el-form-item label="标签" prop="tags">
          <div class="tags-container">
            <!-- 已选标签 -->
            <div class="tags-section" v-if="formData.tags.length > 0">
              <div class="tags-label">已选标签：</div>
              <div class="tags-list">
              <el-tag
                  v-for="tag in formData.tags"
                  :key="tag"
                :closable="!isPresetTag(tag)"
                  :disable-transitions="false"
                size="small"
                :style="getTagStyle(tag)"
                  @close="handleTagClose(tag)"
              >
                {{ tag }}
              </el-tag>
            </div>
            </div>
            
            <!-- 可选标签 -->
            <div class="tags-section" v-if="availableTags.length > 0">
              <div class="tags-label">可选标签：</div>
              <div class="tags-list">
                <el-tag
                  v-for="tag in availableTags"
                  :key="tag"
                  :class="{ 'tag-selected': formData.tags.includes(tag) }"
                  size="small"
                  :style="getTagStyle(tag)"
                  @click="handleTagSelect(tag)"
                >
                  {{ tag }}
                </el-tag>
              </div>
            </div>
            
            <!-- 新增标签（仅当允许自定义时显示） -->
            <div class="tags-section" v-if="canAddCustomTag">
              <el-input
                v-if="tagInputVisible"
                ref="tagInputRef"
                v-model="tagInputValue"
                class="tag-input"
                size="small"
                placeholder="输入新标签"
                @keyup.enter="handleTagInputConfirm"
                @blur="handleTagInputConfirm"
              />
              <el-button
                v-else
                class="button-new-tag"
                size="small"
                @click="showTagInput"
              >
                + 新增标签
              </el-button>
            </div>
          </div>
        </el-form-item>

        <!-- 帖子封面 -->
        <el-form-item label="帖子封面" prop="cover">
          <div class="cover-upload-section">
            <div class="cover-row">
            <div class="cover-options">
              <el-upload
                class="cover-uploader"
                :action="uploadAction"
                :show-file-list="false"
                :on-success="handleCoverSuccess"
                :before-upload="beforeCoverUpload"
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
                        @click.stop="handleCoverDelete"
                      />
                    </div>
                  </div>
                  <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
                </el-upload>
              </div>
              
              <!-- 推荐封面 -->
              <div class="recommended-covers">
                <span class="recommended-covers-title">推荐封面</span>
                <div class="recommended-covers-list">
                  <div
                    v-for="(cover, index) in recommendedCovers"
                    :key="index"
                    class="recommended-cover-item"
                    :class="{ 'cover-selected': formData.cover === cover }"
                    @click="handleSelectRecommendedCover(cover)"
                  >
                    <img :src="cover" alt="推荐封面" />
                    <div class="cover-overlay">
                      <el-icon class="cover-check-icon"><Check /></el-icon>
            </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="cover-tips">
              <p>支持 JPG、PNG 格式，建议尺寸 800x400，大小不超过 2MB</p>
            </div>
          </div>
        </el-form-item>

        <!-- 内容编辑（富文本编辑器） -->
        <el-form-item label="帖子内容" prop="content">
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
            <div class="editor-footer">
              <span class="word-count">字数：{{ wordCount }}</span>
              <span class="auto-save-status" v-if="!isEditMode && lastSaveTime">
                自动保存于 {{ lastSaveTime }}
              </span>
            </div>
          </div>
        </el-form-item>
      </el-form>

      <!-- 底部操作按钮 -->
      <div class="form-actions">
        <el-button v-if="!isEditMode" @click="handleSaveDraft">保存草稿</el-button>
        <el-button type="primary" @click="handlePublish" :loading="publishing">
          {{ isEditMode ? '更新帖子' : '发布帖子' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, nextTick, watch, shallowRef } from 'vue'
import { useRouter, useRoute, onBeforeRouteLeave } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ROUTES } from '../router/paths'
// API 层 - 支持 Mock/Real API 自动切换
import { createPost, updatePost, getPostDetail, getZoneTags, getRecommendedCovers, saveDraft, getDraft, deleteDraft } from '../api/posts'
import { getTools } from '../api/home'

// localStorage 草稿存储 key
const DRAFT_STORAGE_KEY = 'post_draft'

interface StoredDraft {
  draftId?: string
  zone?: string
  toolId?: number | null
  title?: string
  summary?: string
  tags?: string[]
  cover?: string
  content?: string
  savedAt?: string
}

// 获取存储的草稿
const getStoredDraft = (): StoredDraft | null => {
  try {
    const stored = localStorage.getItem(DRAFT_STORAGE_KEY)
    return stored ? JSON.parse(stored) : null
  } catch {
    return null
  }
}

// 保存草稿到 localStorage
const setStoredDraft = (draft: StoredDraft) => {
  try {
    localStorage.setItem(DRAFT_STORAGE_KEY, JSON.stringify(draft))
  } catch (e) {
    console.error('保存草稿到localStorage失败:', e)
  }
}

// 清除存储的草稿
const clearStoredDraft = () => {
  try {
    localStorage.removeItem(DRAFT_STORAGE_KEY)
  } catch (e) {
    console.error('清除草稿失败:', e)
  }
}
import type { InputInstance } from 'element-plus'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import {
  Plus,
  Delete,
  Check,
  ArrowLeft
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const formRef = ref()
const tagInputRef = ref<InputInstance>()
const publishing = ref(false)
const _recommending = ref(false)
const postLoading = ref(false)
const tagInputVisible = ref(false)
const tagInputValue = ref('')
const lastSaveTime = ref('')
const editorRef = shallowRef<IDomEditor>()
const editorMode = 'default'
let autoSaveTimer: ReturnType<typeof setTimeout> | null = null
let backendSyncTimer: ReturnType<typeof setInterval> | null = null
const BACKEND_SYNC_INTERVAL = 3 * 60 * 1000 // 3分钟同步一次到后端
let lastBackendSyncTime = 0 // 上次同步到后端的时间戳

// 是否为编辑模式
const isEditMode = computed(() => {
  return route.query.edit === 'true' && route.query.id
})

// 是否从编辑页面跳转过来（用于新建帖子）
const _isFromEditPage = computed(() => {
  return route.query.fromEdit === 'true'
})

// 标记是否已经检查过草稿（避免重复检查）
const hasCheckedDraft = ref(false)

// 推荐封面列表
const recommendedCovers = ref<string[]>([])

// 加载推荐封面
const loadRecommendedCovers = async () => {
  try {
    const response = await getRecommendedCovers()
    recommendedCovers.value = response.data.list.map((cover: { url: string }) => cover.url)
  } catch (error) {
    console.error('加载推荐封面失败:', error)
    // 使用默认封面
    recommendedCovers.value = [
      'https://picsum.photos/800/400?random=1',
      'https://picsum.photos/800/400?random=2',
      'https://picsum.photos/800/400?random=3'
    ]
  }
}

// 预设标签（不可删除）
const presetTags = ref<string[]>([])

// 表单数据
const formData = ref({
  zone: '' as '' | 'practices' | 'tools' | 'agent' | 'empowerment',
  toolId: null as number | null,
  title: '',
  summary: '',
  tags: [] as string[],
  cover: '',
  content: ''
})

// 清空表单数据
const resetFormData = () => {
  formData.value = {
    zone: '',
    toolId: null,
    title: '',
    summary: '',
    tags: [],
    cover: '',
    content: ''
  }
  presetTags.value = []
  lastSaveTime.value = ''
  if (editorRef.value) {
    editorRef.value.setHtml('')
  }
  updateWordCount()
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 工具列表（AI工具专区）- 从API加载
const loadTools = async () => {
  try {
    const response = await getTools()
    // getTools() 返回 ApiResponse<{ list: ToolItem[] }>，需要使用 response.data.list
    const tools = (response.data?.list || []).map((item: { id: number; name: string }) => ({
      id: item.id,
      name: item.name
    }))
    // 添加"其他工具"选项（toolId=0）
    tools.push({ id: 0, name: '其他工具' })
    return tools
  } catch (e) {
    console.error('加载工具配置失败:', e)
    return [
      { id: 1, name: 'TestMate' },
      { id: 2, name: 'CodeMate' },
      { id: 3, name: '云集' },
      { id: 4, name: '云见' },
      { id: 5, name: '扶摇' },
      { id: 6, name: '纠错Agent' },
      { id: 7, name: 'DT' },
      { id: 0, name: '其他工具' }
    ]
  }
}

const tools = ref<Array<{ id: number; name: string }>>([])

// 监听配置更新
const handleConfigUpdate = async () => {
  tools.value = await loadTools()
}

// 监听配置更新（在现有的onMounted中添加）

const handleBack = () => {
  // 如果是从编辑页面跳转过来的，返回到编辑页面的上级页面
  const fromEdit = route.query.fromEdit === 'true'
  const fromEditId = route.query.fromEditId
  const fromPath = route.query.fromPath as string
  
  if (fromEdit && fromEditId) {
    // 从编辑页面跳转过来的，返回到帖子详情页（编辑页面的上级页面）
    router.push(`/post/${fromEditId}`).catch(() => {
      // 如果跳转失败，尝试返回到来源路径的上级页面
      if (fromPath) {
        // 从 /post/create?edit=true&id=xxx 可以推断出应该返回到 /post/xxx
        router.push(`/post/${fromEditId}`).catch(() => {
          router.push(ROUTES.PRACTICES)
        })
      } else {
        router.push(ROUTES.PRACTICES)
      }
    })
  } else if (window.history.length > 1) {
    router.back()
  } else {
    router.push(ROUTES.PRACTICES)
  }
}

// AI工具专区的固定标签（非"其他"工具时只能选择这两个）
const toolFixedTags = ['使用指导', '优秀案例']

// 从后端获取的专区标签
const fetchedZoneTags = ref<string[]>([])

// 加载专区标签
const loadZoneTags = async () => {
  if (!formData.value.zone) {
    fetchedZoneTags.value = []
    return
  }
  
  try {
    const response = await getZoneTags(formData.value.zone, formData.value.toolId ?? undefined)
    fetchedZoneTags.value = response.data.list.map((t: { name: string }) => t.name)
  } catch (error) {
    console.error('加载专区标签失败:', error)
    // 使用默认标签作为降级方案
    const defaultTags: Record<string, string[]> = {
      practices: ['自然语言处理', '计算机视觉', '深度学习', 'AI伦理', '机器学习', '机器人', '数据科学', '生成式AI', 'PyTorch', 'TensorFlow'],
      tools: ['新手', '进阶', '最佳实践', '技巧', '案例', '教程', '优化', '通用'],
      agent: ['Agent应用', '工作流', '自动化', '智能编排', '案例分享'],
      empowerment: ['讨论', '提问', '分享', '经验', '工具', '技巧', '案例', '教程', '最佳实践', '问题解决']
    }
    fetchedZoneTags.value = defaultTags[formData.value.zone] || []
  }
}

// 当前可用的标签
const availableTags = computed(() => {
  if (formData.value.zone === 'tools') {
    // 如果选择了AI工具专区
    if (formData.value.toolId !== null && formData.value.toolId !== -1) {
      // 如果选择了非"其他"工具，只显示固定标签
      return toolFixedTags
    } else if (formData.value.toolId === -1) {
      // 如果选择了"其他"工具，显示从后端获取的标签
      return fetchedZoneTags.value.length > 0 ? fetchedZoneTags.value : ['新手', '进阶', '最佳实践', '技巧', '案例', '教程', '优化', '通用']
    }
    // 如果还没选择工具，不显示标签
    return []
  }
  
  // 其他专区显示从后端获取的标签
  if (formData.value.zone) {
    return fetchedZoneTags.value
  }
  return []
})

// 是否允许自定义标签
const canAddCustomTag = computed(() => {
  // 如果选择了AI工具专区且选择了非"其他"工具，不允许自定义
  if (formData.value.zone === 'tools' && formData.value.toolId !== null && formData.value.toolId !== -1) {
    return false
  }
  return true
})

// 判断是否为预设标签
const isPresetTag = (tag: string) => {
  return presetTags.value.includes(tag)
}

// 表单验证规则
const rules = {
  zone: [{ required: true, message: '请选择专区', trigger: 'change' }],
  toolId: [
    {
      validator: (_rule: unknown, value: number | null, callback: (error?: Error) => void) => {
        if (formData.value.zone === 'tools') {
          if (!value && value !== 0) {
            callback(new Error('请选择工具'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: ['change', 'blur']
    }
  ],
  title: [{ required: true, message: '请输入帖子标题', trigger: 'blur' }],
  summary: [{ required: true, message: '请输入内容简介', trigger: 'blur' }],
  content: [{ required: true, message: '请输入帖子内容', trigger: 'blur' }]
}

// wangeditor 工具栏配置
const toolbarConfig: Partial<IToolbarConfig> = {
  excludeKeys: []
}

// wangeditor 编辑器配置
const editorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入帖子内容...',
  // 启用粘贴功能，支持带格式、表格、图片
  readOnly: false,
  MENU_CONF: {
    // 图片上传配置
    uploadImage: {
      maxFileSize: 5 * 1024 * 1024, // 5MB
      maxNumberOfFiles: 10,
      allowedFileTypes: ['image/*'],
      // 自定义上传
      async customUpload(file: File, insertFn: (url: string, alt?: string, href?: string) => void) {
        // 验证文件类型和大小
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
          // 这里应该调用后端API上传图片
          // const imageUrl = await uploadImage(file)
          
          // 临时使用本地预览（实际应该使用上传后的URL）
          const imageUrl = URL.createObjectURL(file)
          insertFn(imageUrl, file.name)
          ElMessage.success('图片插入成功')
        } catch {
          ElMessage.error('图片上传失败')
        }
      }
    },
    // 视频上传配置
    uploadVideo: {
      maxFileSize: 50 * 1024 * 1024, // 50MB
      allowedFileTypes: ['video/*'],
      // 自定义上传
      async customUpload(file: File, insertFn: (url: string, poster?: string) => void) {
        // 验证文件类型和大小
        const isVideo = file.type.startsWith('video/')
        const isLt50M = file.size / 1024 / 1024 < 50

        if (!isVideo) {
          ElMessage.error('只能上传视频文件！')
          return
        }
        if (!isLt50M) {
          ElMessage.error('视频大小不能超过 50MB！')
          return
        }

        try {
          // 这里应该调用后端API上传视频
          // const videoUrl = await uploadVideo(file)
          
          // 临时使用本地预览（实际应该使用上传后的URL）
          const videoUrl = URL.createObjectURL(file)
          insertFn(videoUrl)
          ElMessage.success('视频插入成功')
        } catch {
          ElMessage.error('视频上传失败')
        }
      }
    },
    // 代码块配置
    codeSelectLang: {
      codeLangs: [
        { text: 'CSS', value: 'css' },
        { text: 'HTML', value: 'html' },
        { text: 'XML', value: 'xml' },
        { text: 'JavaScript', value: 'javascript' },
        { text: 'TypeScript', value: 'typescript' },
        { text: 'Java', value: 'java' },
        { text: 'Python', value: 'python' },
        { text: 'Go', value: 'go' },
        { text: 'C', value: 'c' },
        { text: 'C#', value: 'csharp' },
        { text: 'C++', value: 'cpp' },
        { text: 'PHP', value: 'php' },
        { text: 'Ruby', value: 'ruby' },
        { text: 'Swift', value: 'swift' },
        { text: 'Kotlin', value: 'kotlin' },
        { text: 'Rust', value: 'rust' },
        { text: 'SQL', value: 'sql' },
        { text: 'JSON', value: 'json' },
        { text: 'Markdown', value: 'markdown' },
        { text: 'Bash', value: 'bash' },
        { text: 'Shell', value: 'shell' }
      ]
    },
    // 表格配置
    insertTable: {
      maxRow: 20,
      maxCol: 10
    }
  }
}

// 编辑器创建回调
const handleEditorCreated = (editor: IDomEditor) => {
  editorRef.value = editor
  
  // 添加粘贴事件监听，支持带格式、表格、图片的粘贴
  const editorDom = editor.getEditableContainer()
  if (editorDom) {
    editorDom.addEventListener('paste', ((event: ClipboardEvent) => {
      handleEditorPaste(editor, event)
      // 粘贴后更新字数
      setTimeout(() => {
        updateWordCount()
      }, 100)
    }) as EventListener)
    
    // 监听输入事件，实时更新字数
    editorDom.addEventListener('input', () => {
      updateWordCount()
    })
    
    // 监听键盘事件（包括删除、退格等）
    editorDom.addEventListener('keyup', () => {
      updateWordCount()
    })
    
    // 监听删除事件
    editorDom.addEventListener('keydown', ((e: KeyboardEvent) => {
      if (e.key === 'Delete' || e.key === 'Backspace') {
        setTimeout(() => {
          updateWordCount()
        }, 10)
      }
    }) as EventListener)
  }
  
  // 如果有草稿内容，设置到编辑器
  nextTick(() => {
    if (formData.value.content) {
      editor.setHtml(formData.value.content)
      updateWordCount()
    }
  })
  
  // 初始化字数统计
  updateWordCount()
}

// 字数统计（响应式）
const wordCount = ref(0)

// 更新字数统计
const updateWordCount = () => {
  if (editorRef.value) {
    try {
      const text = editorRef.value.getText()
      // 计算所有字符数（包括空格）
      wordCount.value = text.length
    } catch (error) {
      console.error('字数统计失败:', error)
      // 降级方案：从 HTML 内容提取
      if (formData.value.content) {
        const tempDiv = document.createElement('div')
        tempDiv.innerHTML = formData.value.content
        const text = tempDiv.textContent || tempDiv.innerText || ''
        wordCount.value = text.length
      } else {
        wordCount.value = 0
      }
    }
  } else if (formData.value.content) {
    // 如果编辑器未初始化，从 HTML 内容中提取文本
    const tempDiv = document.createElement('div')
    tempDiv.innerHTML = formData.value.content
    const text = tempDiv.textContent || tempDiv.innerText || ''
    wordCount.value = text.length
  } else {
    wordCount.value = 0
  }
}

// 监听编辑器内容变化，实时更新字数
watch(() => formData.value.content, () => {
  nextTick(() => {
    updateWordCount()
  })
}, { deep: true })

// 编辑器内容变化回调
const handleEditorChange = (editor: IDomEditor) => {
  formData.value.content = editor.getHtml()
  updateWordCount()
  handleAutoSave()
}

// 粘贴事件处理（支持带格式、表格、图片）
const handleEditorPaste = (editor: IDomEditor, event: ClipboardEvent) => {
  const clipboardData = event.clipboardData
  if (!clipboardData) return

  // 检查是否有图片文件
  const items = Array.from(clipboardData.items)
  const imageItem = items.find(item => item.type.startsWith('image/'))
  
  if (imageItem) {
    // 处理粘贴的图片
    event.preventDefault()
    const file = imageItem.getAsFile()
    if (file) {
      // 验证文件大小
      const isLt5M = file.size / 1024 / 1024 < 5
      if (!isLt5M) {
        ElMessage.error('图片大小不能超过 5MB！')
        return
      }
      
      // 插入图片到编辑器
      const imageUrl = URL.createObjectURL(file)
      editor.dangerouslyInsertHtml(`<img src="${imageUrl}" alt="粘贴的图片" style="max-width: 100%;" />`)
      ElMessage.success('图片已粘贴')
    }
    return
  }

  // wangeditor 会自动处理 HTML 和纯文本的粘贴
  // 包括表格、样式等，不需要额外处理
}

// 标签颜色样式
const getTagStyle = (tag: string) => {
  const colorPalette = [
    { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
    { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },
    { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },
    { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },
    { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },
    { bg: 'rgba(156, 39, 176, 0.15)', border: '#9c27b0', text: '#9c27b0' }
  ]
  const hash = tag.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0)
  const index = hash % colorPalette.length
  const colors = colorPalette[index] ?? colorPalette[0]!
  return {
    backgroundColor: colors.bg,
    borderColor: colors.border,
    color: colors.text
  }
}

// 专区切换
const handleZoneChange = async () => {
  formData.value.toolId = null
  // 保留预设标签，清除自定义标签
  formData.value.tags = formData.value.tags.filter(tag => isPresetTag(tag))
  presetTags.value = []
  
  // 加载新专区的标签
  await loadZoneTags()
  
  // 如果切换到AI工具专区，需要验证工具选择
  if (formData.value.zone === 'tools') {
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('toolId')
      }
    })
  }
  
  handleAutoSave()
}

// 工具切换
const handleToolChange = async () => {
  // 如果选择了非"其他"工具，只保留固定标签（操作指导、优秀使用）
  if (formData.value.toolId !== null && formData.value.toolId !== -1) {
    formData.value.tags = formData.value.tags.filter(tag => toolFixedTags.includes(tag))
    presetTags.value = formData.value.tags.filter(tag => toolFixedTags.includes(tag))
  } else {
    // 如果选择了"其他"或清空了工具选择，保留预设标签，清除自定义标签
    formData.value.tags = formData.value.tags.filter(tag => isPresetTag(tag))
    presetTags.value = []
  }
  
  // 加载工具对应的标签
  await loadZoneTags()
  
  handleAutoSave()
}

// 选择标签（从可选标签中点击）
const handleTagSelect = (tag: string) => {
  if (!formData.value.tags.includes(tag)) {
    formData.value.tags.push(tag)
    // 标记为预设标签
    if (!presetTags.value.includes(tag)) {
      presetTags.value.push(tag)
    }
    handleAutoSave()
  }
}

// 显示标签输入框
const showTagInput = () => {
  tagInputVisible.value = true
  nextTick(() => {
    tagInputRef.value?.input?.focus()
  })
}

// 确认添加标签
const handleTagInputConfirm = () => {
  // 如果不允许自定义标签，直接返回
  if (!canAddCustomTag.value) {
    tagInputVisible.value = false
    tagInputValue.value = ''
    return
  }
  
  if (tagInputValue.value) {
    const tag = tagInputValue.value.trim()
    if (tag && !formData.value.tags.includes(tag)) {
      formData.value.tags.push(tag)
      // 如果是已有标签，标记为预设标签
      if (availableTags.value.includes(tag)) {
        if (!presetTags.value.includes(tag)) {
          presetTags.value.push(tag)
        }
      }
      handleAutoSave()
    }
  }
  tagInputVisible.value = false
  tagInputValue.value = ''
}

// 移除标签（只能移除自定义标签）
const handleTagClose = (tag: string) => {
  if (!isPresetTag(tag)) {
    const index = formData.value.tags.indexOf(tag)
    if (index > -1) {
      formData.value.tags.splice(index, 1)
      handleAutoSave()
    }
  }
}


// 封面上传成功
const handleCoverSuccess = (_response: unknown, file: File) => {
  // 这里应该从响应中获取图片URL，暂时使用本地预览
  if (file) {
    formData.value.cover = URL.createObjectURL(file)
    handleAutoSave()
  }
}

// 删除封面
const handleCoverDelete = () => {
  formData.value.cover = ''
  handleAutoSave()
}

// 封面上传前验证
const beforeCoverUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB！')
    return false
  }
  return true
}

// 选择推荐封面
const handleSelectRecommendedCover = (coverUrl: string) => {
  formData.value.cover = coverUrl
  handleAutoSave()
  ElMessage.success('已选择推荐封面')
}

// 自动保存草稿到本地（仅在新建模式下）- 短时间保存
const handleAutoSave = () => {
  // 编辑模式下不保存草稿
  if (isEditMode.value) {
    return
  }
  
  if (autoSaveTimer) {
    clearTimeout(autoSaveTimer)
  }
  autoSaveTimer = setTimeout(() => {
    const draft = {
      ...formData.value,
      savedAt: new Date().toISOString()
    }
    // 只保存到 localStorage（短时间存储）
    setStoredDraft(draft)
    lastSaveTime.value = new Date().toLocaleTimeString('zh-CN', {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    })
  }, 2000) // 2秒后自动保存到本地
}

// 同步草稿到后端（长时间存储）- 每3分钟执行一次
const syncDraftToBackend = async () => {
  // 编辑模式下不同步草稿
  if (isEditMode.value) {
    return
  }
  
  // 检查是否有内容需要保存
  const hasDraftContent = formData.value.title || 
                          formData.value.summary || 
                          (formData.value.content && formData.value.content !== '<p><br></p>')
  
  if (!hasDraftContent) {
    return
  }
  
  const now = Date.now()
  // 如果距离上次同步不足3分钟，跳过
  if (now - lastBackendSyncTime < BACKEND_SYNC_INTERVAL) {
    return
  }
  
  const draft = {
    ...formData.value,
    savedAt: new Date().toISOString()
  }
  
  try {
    await saveDraft(draft)
    lastBackendSyncTime = now
    console.log('草稿已同步到后端:', new Date().toLocaleTimeString())
  } catch (e) {
    console.error('同步草稿到后端失败:', e)
  }
}

// 启动后端同步定时器
const startBackendSyncTimer = () => {
  if (backendSyncTimer) {
    clearInterval(backendSyncTimer)
  }
  // 每3分钟同步一次到后端
  backendSyncTimer = setInterval(() => {
    syncDraftToBackend()
  }, BACKEND_SYNC_INTERVAL)
}

// 停止后端同步定时器
const stopBackendSyncTimer = () => {
  if (backendSyncTimer) {
    clearInterval(backendSyncTimer)
    backendSyncTimer = null
  }
}

// 手动保存草稿（仅在新建模式下）- 同时保存到本地和后端
const handleSaveDraft = async () => {
  // 编辑模式下不保存草稿
  if (isEditMode.value) {
    return
  }
  
  const draft = {
    ...formData.value,
    savedAt: new Date().toISOString()
  }
  // 保存到 localStorage
  setStoredDraft(draft)
  // 同时保存到后端
  try {
    await saveDraft(draft)
    lastBackendSyncTime = Date.now()
  } catch (e) {
    console.error('保存草稿到后端失败:', e)
  }
  lastSaveTime.value = new Date().toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
  ElMessage.success('草稿已保存')
}

// 发布帖子
const handlePublish = async () => {
  if (!formRef.value) return

  let result: { data?: { id?: number } } | null = null
  
  try {
    // 如果选择了AI工具专区，确保工具已选择
    if (formData.value.zone === 'tools' && (formData.value.toolId === null || formData.value.toolId === undefined)) {
      ElMessage.error('请选择工具')
      // 验证工具选择字段
      formRef.value.validateField('toolId')
      return
    }
    
    await formRef.value.validate()
    
    // 获取编辑器内容
    if (editorRef.value) {
      formData.value.content = editorRef.value.getHtml()
    }
    
    if (!formData.value.content || wordCount.value === 0) {
      ElMessage.error('请输入帖子内容')
      return
    }

    publishing.value = true

    try {
      // 判断是创建还是更新
      const postId = route.query.id ? Number(route.query.id) : null
      const zone = formData.value.zone || undefined
      if (postId && route.query.edit === 'true') {
        // 更新帖子
        // 如果路由参数中有 authorId，则在更新时带上（参考旧代码逻辑）
        // 注意：旧代码 updatePost 并没有 authorId 参数在 params 中，但这里我们可以保留逻辑
        // 不过 api/posts.ts 中的 updatePost 签名 params 是 PostUpdateParams，目前没有 authorId
        // 如果后端不需要 authorId，这里可以忽略。
        // 但为了完整性，如果 params 支持，可以添加。目前 PostUpdateParams 不支持。
        // 所以我们只更新支持的字段。
        
        result = await updatePost(postId, {
          title: formData.value.title,
          summary: formData.value.summary,
          content: formData.value.content,
          tags: formData.value.tags,
          cover: formData.value.cover,
        })
        ElMessage.success('帖子更新成功')
      } else {
        // 创建帖子
        if (!zone) {
          ElMessage.error('请选择专区')
          publishing.value = false
          return
        }
        
        // 获取当前用户信息
        let authorId = '';
        try {
          const userMessageStr = localStorage.getItem('userMessage');
          if (userMessageStr) {
            const userMessage = JSON.parse(userMessageStr);
            authorId = userMessage.userId;
          }
        } catch (e) {
          console.error('获取用户信息失败', e);
        }

        result = await createPost({
          title: formData.value.title,
          summary: formData.value.summary,
          content: formData.value.content,
          tags: formData.value.tags,
          cover: formData.value.cover,
          zone: zone as 'practices' | 'tools' | 'agent' | 'empowerment',
          toolId: formData.value.toolId || undefined,
          authorId: authorId || undefined
        })
        ElMessage.success('帖子发布成功')
      }

      // 清除草稿（仅在新建模式下）
      if (!isEditMode.value) {
        clearStoredDraft()
        try {
          await deleteDraft()
        } catch (e) {
          console.error('删除后端草稿失败:', e)
        }
      }
    } catch (error: unknown) {
      console.error('发布失败:', error)
      ElMessage.error((error as Error).message || '发布失败，请稍后重试')
      publishing.value = false
      return
    }
    
    publishing.value = false
    
    // 获取新创建的帖子ID（编辑模式下使用原ID，新建模式下使用返回的ID）
    const newPostId = isEditMode.value ? route.query.id : result?.data?.id
    
    // 发布成功后询问用户
    ElMessageBox({
      title: '提示',
      message: '帖子发布成功！',
      type: 'success',
      showCancelButton: true,
      confirmButtonText: '查看帖子',
      cancelButtonText: '返回上一页',
      distinguishCancelAndClose: true
    }).then(() => {
      // 用户选择查看帖子
      if (newPostId) {
        router.push(`/post/${newPostId}`)
      } else {
        // 如果没有帖子ID，跳转到对应专区
        const routeMap: Record<string, string> = {
          practices: '/practices',
          tools: '/tools',
          agent: '/agent',
          empowerment: '/empowerment'
        }
        router.push(routeMap[formData.value.zone] || '/practices')
      }
    }).catch((action) => {
      if (action === 'cancel') {
        // 用户选择返回上一页
        if (window.history.length > 2) {
          router.back()
        } else {
          // 如果没有足够的历史记录，跳转到对应专区
          const routeMap: Record<string, string> = {
            practices: '/practices',
            tools: '/tools',
            agent: '/agent',
            empowerment: '/empowerment'
          }
          router.push(routeMap[formData.value.zone] || '/practices')
        }
      }
      // 用户关闭对话框，留在当前页，清空表单准备继续发帖
      formData.value = {
        zone: 'practices',
        toolId: null,
        title: '',
        summary: '',
        tags: [],
        cover: '',
        content: ''
      }
      if (editorRef.value) {
        editorRef.value.setHtml('')
      }
      updateWordCount()
      // 清除表单验证状态
      if (formRef.value) {
        formRef.value.clearValidate()
      }
    })
  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败，请检查表单')
    publishing.value = false
  }
}

// 检查并加载草稿（比较后端和本地草稿的时间，使用最新的）
const checkAndLoadDraft = async () => {
  let backendDraft: StoredDraft | null = null
  let localDraft: StoredDraft | null = null
  
  // 1. 尝试从后端获取草稿
  try {
    const backendResult = await getDraft()
    // getDraft 返回的是 ApiResponse<DraftData | null>
    // backendResult.data 就是 DraftData 对象
    if (backendResult && backendResult.data) {
      backendDraft = backendResult.data
    }
  } catch (error) {
    console.error('从后端获取草稿失败:', error)
  }
  
  // 2. 从 localStorage 获取草稿
  localDraft = getStoredDraft()
  
  // 3. 比较时间，选择最新的草稿
  let draftData = null
  let draftSource = '' // 'backend' | 'local'
  
  if (backendDraft && localDraft) {
    // 两者都有，比较时间
    const backendTime = backendDraft.savedAt ? new Date(backendDraft.savedAt).getTime() : 0
    const localTime = localDraft.savedAt ? new Date(localDraft.savedAt).getTime() : 0
    if (backendTime >= localTime) {
      draftData = backendDraft
      draftSource = 'backend'
    } else {
      draftData = localDraft
      draftSource = 'local'
    }
  } else if (backendDraft) {
    draftData = backendDraft
    draftSource = 'backend'
  } else if (localDraft) {
    draftData = localDraft
    draftSource = 'local'
  }
  
  if (draftData) {
    try {
      // 检查草稿是否有内容
      const hasDraftContent = draftData.title || 
                              draftData.summary || 
                              (draftData.content && draftData.content !== '<p><br></p>')
      
      if (hasDraftContent) {
        // 格式化保存时间用于显示
        const savedTimeStr = draftData.savedAt 
          ? new Date(draftData.savedAt).toLocaleString('zh-CN')
          : '未知时间'
        
        // 询问用户是否继续编辑草稿
        ElMessageBox.confirm(
          `检测到您有未完成的草稿（保存于 ${savedTimeStr}），是否继续编辑？`,
          '草稿提示',
          {
            confirmButtonText: '继续编辑',
            cancelButtonText: '重新开始',
            type: 'info',
            distinguishCancelAndClose: true
          }
        ).then(() => {
          // 用户选择继续编辑
          loadDraft(draftData)
          // 如果草稿来自本地，启动后立即同步到后端
          if (draftSource === 'local') {
            syncDraftToBackend()
          }
        }).catch(async (action) => {
          // 用户选择重新开始
          if (action === 'cancel') {
            // 清除草稿（后端和本地都清除）
            clearStoredDraft()
            try {
              await deleteDraft()
            } catch (e) {
              console.error('删除后端草稿失败:', e)
            }
            // 清空表单
            formData.value = {
              zone: 'practices',
              toolId: null,
              title: '',
              summary: '',
              tags: [],
              cover: '',
              content: ''
            }
            // 清空编辑器内容
            if (editorRef.value) {
              editorRef.value.setHtml('')
            }
            updateWordCount()
            // 清除表单验证状态
            if (formRef.value) {
              formRef.value.clearValidate()
            }
            ElMessage.info('已清除草稿，开始新的编辑')
          }
        })
      }
    } catch (error) {
      console.error('检查草稿失败:', error)
      // 如果解析失败，清除无效的草稿
      clearStoredDraft()
    }
  }
}

// 加载草稿内容
const loadDraft = (draft?: StoredDraft) => {
  let draftData = draft
  if (!draftData) {
    // 从 localStorage 读取草稿
    const storedDraft = getStoredDraft()
    if (!storedDraft) return
    draftData = storedDraft
  }
  
  formData.value = {
    zone: (draftData.zone || '') as '' | 'practices' | 'tools' | 'agent' | 'empowerment',
    toolId: draftData.toolId ?? null,
    title: draftData.title || '',
    summary: draftData.summary || '',
    tags: draftData.tags || [],
    cover: draftData.cover || '',
    content: draftData.content || ''
  }
  // 如果编辑器已创建，设置内容
  if (editorRef.value && draftData.content) {
    editorRef.value.setHtml(draftData.content)
    updateWordCount()
  } else if (draftData.content) {
    // 如果编辑器未创建，先更新字数（从 HTML 提取）
    updateWordCount()
  }
  ElMessage.success('已加载草稿')
}

// 上传地址（实际应该配置为真实的上传接口）
const uploadAction = '#'

// 检查是否有未保存的内容
const hasUnsavedContent = computed(() => {
  return formData.value.title || 
         formData.value.summary || 
         (formData.value.content && formData.value.content !== '<p><br></p>')
})

// 路由离开前确认
onBeforeRouteLeave((to, from, next) => {
  // 如果跳转目标带有 skipLeaveCheck 标记，说明已经询问过用户，直接放行
  if (to.query.skipLeaveCheck === 'true') {
    next()
    return
  }
  
  // 如果是编辑模式，跳转到新建页面时询问（无论是否有未保存内容）
  if (isEditMode.value && to.path === '/post/create' && !to.query.edit) {
    ElMessageBox.confirm(
      '确定要离开编辑页面，创建新帖子吗？',
      '提示',
      {
        confirmButtonText: '确定离开',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      // 确认离开，跳转到新建页面时会自动清空内容（通过 watch 监听）
      next()
    }).catch(() => {
      next(false)
    })
    return
  }
  
  // 编辑模式下，离开到其他页面时，如果有未保存内容，提示用户
  if (isEditMode.value && hasUnsavedContent.value && to.path !== '/post/create') {
    ElMessageBox.confirm(
      '您有未保存的修改，确定要离开吗？',
      '提示',
      {
        confirmButtonText: '确定离开',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      next()
    }).catch(() => {
      next(false)
    })
    return
  }
  
  // 新建模式下，如果有未保存内容，提示用户（草稿已自动保存）
  if (!isEditMode.value && hasUnsavedContent.value) {
    ElMessageBox.confirm(
      '您有未保存的内容，草稿已自动保存。确定要离开吗？',
      '提示',
      {
        confirmButtonText: '确定离开',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      next()
    }).catch(() => {
      next(false)
    })
  } else {
    next()
  }
})

// 加载帖子数据用于编辑
const loadPostForEdit = async () => {
  const postId = route.query.id
  if (!postId) return

  try {
    postLoading.value = true
    const response = await getPostDetail(Number(postId))
    const post = response.data
    
    // 填充表单数据
    formData.value = {
      zone: post.zone || 'practices',
      toolId: post.toolId || null,
      title: post.title,
      summary: post.summary || '',
      tags: post.tags || [],
      cover: post.cover || '',
      content: post.content || ''
    }
    
    // 标记所有标签为预设标签
    presetTags.value = [...(post.tags || [])]
    
    // 设置编辑器内容
    if (editorRef.value) {
      editorRef.value.setHtml(post.content || '')
      updateWordCount()
    }
    
  } catch (error) {
    console.error('加载帖子失败:', error)
    ElMessage.error('加载帖子失败')
  } finally {
    postLoading.value = false
  }
}

// 监听路由变化，处理编辑模式和新建模式的切换
watch(() => [route.query.edit, route.query.id], ([newEdit, newId], [oldEdit, oldId]) => {
  // 跳过首次加载（oldEdit 和 oldId 都是 undefined）
  if (oldEdit === undefined && oldId === undefined) {
    return
  }
  
  // 如果从编辑模式切换到新建模式，清空所有内容并检查草稿
  if (oldEdit === 'true' && (newEdit !== 'true' || !newEdit)) {
    resetFormData()
    // 重置草稿检查标记
    hasCheckedDraft.value = false
    // 启动后端同步定时器
    startBackendSyncTimer()
    // 延迟检查草稿，确保编辑器已创建
    nextTick(() => {
      setTimeout(() => {
        if (!hasCheckedDraft.value) {
          hasCheckedDraft.value = true
          checkAndLoadDraft()
        }
      }, 500)
    })
  }
  // 如果从新建模式切换到编辑模式，清空内容并加载帖子
  else if ((oldEdit !== 'true' || !oldEdit) && newEdit === 'true' && newId) {
    // 停止后端同步定时器（编辑模式不需要草稿）
    stopBackendSyncTimer()
    resetFormData()
    nextTick(() => {
      setTimeout(() => {
        loadPostForEdit()
      }, 500)
    })
  }
}, { immediate: false })

onMounted(async () => {
  // 加载工具列表
  tools.value = await loadTools()
  // 加载推荐封面
  await loadRecommendedCovers()
  
  // 监听配置更新
  window.addEventListener('adminConfigUpdated', handleConfigUpdate)
  
  // 如果是编辑模式，先清空内容，然后加载帖子数据（不检查草稿）
  if (isEditMode.value) {
    resetFormData()
    nextTick(() => {
      setTimeout(() => {
        loadPostForEdit()
      }, 500)
    })
  } else {
    // 新建模式：先清空内容，然后检查草稿
    resetFormData()
    
    // 启动后端同步定时器（每3分钟同步一次草稿到后端）
    startBackendSyncTimer()
    
    // 延迟检查草稿，确保编辑器已创建
    // 注意：如果是从编辑页面跳转过来的（组件复用），watch 会处理草稿检查
    // 这里只处理首次进入新建页面的情况
    nextTick(() => {
      setTimeout(() => {
        if (!hasCheckedDraft.value) {
          hasCheckedDraft.value = true
          checkAndLoadDraft()
        }
      }, 500)
    })
  }
})

onBeforeUnmount(() => {
  // 清除本地自动保存定时器
  if (autoSaveTimer) {
    clearTimeout(autoSaveTimer)
  }
  // 停止后端同步定时器
  stopBackendSyncTimer()
  
  // 如果是新建模式且有未保存内容，离开前同步一次到后端
  if (!isEditMode.value) {
    const hasDraftContent = formData.value.title || 
                            formData.value.summary || 
                            (formData.value.content && formData.value.content !== '<p><br></p>')
    if (hasDraftContent) {
      // 异步同步到后端（不等待结果）
      saveDraft({
        ...formData.value,
        savedAt: new Date().toISOString()
      }).catch(e => console.error('离开前同步草稿失败:', e))
    }
  }
  
  if (editorRef.value) {
    editorRef.value.destroy()
  }
  window.removeEventListener('adminConfigUpdated', handleConfigUpdate)
})
</script>

<style scoped lang="scss">
.post-create-view {
  min-height: 100vh;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.create-container {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.create-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 12px;

  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    color: #333;
  }

  .back-button {
    color: #606266;
    padding: 0;

    &:hover {
      color: #409eff;
    }
  }
}

.tags-container {
  width: 100%;
}

.tags-section {
  margin-bottom: 12px;
  
  &:last-child {
    margin-bottom: 0;
  }

  .tags-label {
    font-size: 12px;
    color: #909399;
    margin-bottom: 8px;
  }

  .tags-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    align-items: center;

    .el-tag {
      cursor: pointer;
      transition: all 0.3s;

      &.tag-selected {
        opacity: 0.5;
        cursor: not-allowed;
      }

      &:not(.tag-selected):hover {
        transform: scale(1.05);
      }
    }
  }

  .tag-input {
    width: 150px;
  }

  .button-new-tag {
    height: 24px;
    line-height: 24px;
    padding: 0 12px;
    font-size: 12px;
  }
}

.cover-upload-section {
  .cover-row {
    display: flex;
    align-items: flex-start;
    gap: 24px;
    flex-wrap: wrap;
  }

  .cover-options {
    flex-shrink: 0;
  }

  .cover-uploader {
    :deep(.el-upload) {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: all 0.3s;

      &:hover {
        border-color: #409eff;
      }
    }

    .cover-image-wrapper {
      position: relative;
      width: 200px;
      height: 120px;
      overflow: hidden;
      border-radius: 6px;

      .cover-image {
        width: 100%;
        height: 100%;
      object-fit: cover;
      display: block;
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

    .cover-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 200px;
      height: 120px;
      line-height: 120px;
      text-align: center;
    }
  }

  .recommended-covers {
    flex: 1;
    min-width: 0;
    display: flex;
    align-items: center;
    gap: 12px;

    .recommended-covers-title {
      font-size: 14px;
      color: #333;
      font-weight: 500;
      white-space: nowrap;
      flex-shrink: 0;
    }

    .recommended-covers-list {
      display: flex;
      gap: 12px;
      flex-wrap: nowrap;
      align-items: center;
    }

    .recommended-cover-item {
      position: relative;
      width: 150px;
      height: 90px;
      flex-shrink: 0;
      border-radius: 6px;
      overflow: hidden;
      cursor: pointer;
      border: 2px solid transparent;
      transition: all 0.3s;

      &:hover {
        border-color: #409eff;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
      }

      &.cover-selected {
        border-color: #67c23a;
        border-width: 3px;
      }

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: block;
      }

      .cover-overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.4);
        display: flex;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: opacity 0.3s;

        .cover-check-icon {
          font-size: 24px;
          color: #fff;
        }
      }

      &:hover .cover-overlay {
        opacity: 1;
      }

      &.cover-selected .cover-overlay {
        opacity: 1;
        background: rgba(103, 194, 58, 0.3);
      }
    }
  }

  .cover-tips {
    margin-top: 12px;
    font-size: 12px;
    color: #909399;

    p {
      margin: 0;
    }
  }
}

.editor-wrapper {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;

  .editor-toolbar {
    border-bottom: 1px solid #e4e7ed;
  }

  .editor-content {
    height: 500px !important;
    overflow-y: auto;

    :deep(.w-e-text-container) {
      height: 500px !important;
    }

    :deep(.w-e-text-placeholder) {
      color: #c0c4cc;
    }

    :deep(.w-e-text) {
      padding: 16px;
      font-size: 14px;
      line-height: 1.6;
      color: #333;
      min-height: 468px;

      p {
        margin: 0.5em 0;
      }

      h1, h2, h3, h4, h5, h6 {
        margin: 1em 0 0.5em;
        font-weight: 600;
      }

      h1 { font-size: 2em; }
      h2 { font-size: 1.5em; }
      h3 { font-size: 1.25em; }

      ul, ol {
        padding-left: 1.5em;
        margin: 0.5em 0;
      }

      blockquote {
        border-left: 4px solid #ddd;
        padding-left: 1em;
        margin: 1em 0;
        color: #666;
      }

      code {
        background: #f4f4f4;
        padding: 2px 4px;
        border-radius: 3px;
        font-family: monospace;
      }

      pre {
        background: #f4f4f4;
        padding: 1em;
        border-radius: 4px;
        overflow-x: auto;
        margin: 1em 0;

        code {
          background: none;
          padding: 0;
        }
      }

      img {
        max-width: 100%;
        height: auto;
        border-radius: 4px;
        margin: 1em 0;
      }

      table {
        border-collapse: collapse;
        width: 100%;
        margin: 1em 0;

        td, th {
          border: 1px solid #ddd;
          padding: 8px;
          min-width: 100px;
        }

        th {
          background: #f5f5f5;
          font-weight: 600;
        }
      }

      a {
        color: #409eff;
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }

  .editor-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 16px;
    border-top: 1px solid #e4e7ed;
    font-size: 12px;
    color: #909399;
    background: #fafafa;

    .word-count {
      font-weight: 500;
    }

    .auto-save-status {
      color: #67c23a;
    }
  }
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
}

:deep(.el-input__wrapper) {
  background: #fff;
}

:deep(.el-textarea__inner) {
  background: #fff;
}
</style>

