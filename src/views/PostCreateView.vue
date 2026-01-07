<template>
  <div class="post-create-view">
    <div class="create-container">
      <div class="create-header">
        <h2>发布帖子</h2>
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

        <!-- 工具选择（仅AI工具专区显示） -->
        <el-form-item
          v-if="formData.zone === 'tools'"
          label="选择工具"
          prop="toolId"
        >
          <el-select
            v-model="formData.toolId"
            placeholder="请选择工具"
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
          <div class="tags-section">
            <!-- 已选标签显示 -->
            <div class="tags-display">
              <el-tag
                v-for="(tag, index) in formData.tags"
                :key="index"
                :closable="!isPresetTag(tag)"
                @close="removeTag(index)"
                class="tag-item"
                size="small"
                :style="getTagStyle(tag)"
              >
                {{ tag }}
              </el-tag>
            </div>
            <!-- 标签选择输入 -->
            <div class="tags-input-section">
              <el-select
                v-model="selectedTag"
                placeholder="选择已有标签或输入新标签"
                filterable
                allow-create
                default-first-option
                size="small"
                style="width: 100%"
                @change="handleTagAdd"
              >
                <el-option
                  v-for="tag in availableTags"
                  :key="tag"
                  :label="tag"
                  :value="tag"
                />
              </el-select>
            </div>
          </div>
        </el-form-item>

        <!-- 帖子封面 -->
        <el-form-item label="帖子封面" prop="cover">
          <div class="cover-upload-section">
            <div class="cover-options">
              <el-upload
                class="cover-uploader"
                :action="uploadAction"
                :show-file-list="false"
                :on-success="handleCoverSuccess"
                :before-upload="beforeCoverUpload"
                accept="image/*"
              >
                <img v-if="formData.cover" :src="formData.cover" class="cover-image" />
                <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
              </el-upload>
              <el-button
                type="primary"
                plain
                size="small"
                @click="handleRecommendCover"
                :loading="recommending"
                style="margin-left: 12px"
              >
                推荐封面
              </el-button>
            </div>
            <div class="cover-tips">
              <p>支持 JPG、PNG 格式，建议尺寸 800x400，大小不超过 2MB</p>
            </div>
          </div>
        </el-form-item>

        <!-- 内容编辑（富文本编辑器） -->
        <el-form-item label="帖子内容" prop="content">
          <div class="editor-wrapper">
            <QuillEditor
              v-model:content="formData.content"
              contentType="html"
              :options="editorOptions"
              @update:content="handleEditorChange"
            />
            <div class="editor-footer">
              <span class="word-count">字数：{{ wordCount }}</span>
              <span class="auto-save-status" v-if="lastSaveTime">
                自动保存于 {{ lastSaveTime }}
              </span>
            </div>
          </div>
        </el-form-item>
      </el-form>

      <!-- 底部操作按钮 -->
      <div class="form-actions">
        <el-button @click="handleSaveDraft">保存草稿</el-button>
        <el-button type="primary" @click="handlePublish" :loading="publishing">
          发布帖子
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { QuillEditor } from '@vueup/vue-quill'
import 'quill/dist/quill.snow.css'
import {
  Plus,
  Picture as PictureIcon
} from '@element-plus/icons-vue'

const router = useRouter()
const formRef = ref()
const publishing = ref(false)
const recommending = ref(false)
const selectedTag = ref('')
const lastSaveTime = ref('')
let autoSaveTimer: ReturnType<typeof setTimeout> | null = null

// 预设标签（不可删除）
const presetTags = ref<string[]>([])

// 表单数据
const formData = ref({
  zone: '',
  toolId: null as number | null,
  title: '',
  summary: '',
  tags: [] as string[],
  cover: '',
  content: ''
})

// 工具列表（AI工具专区）
const tools = ref([
  { id: 1, name: 'TestMate' },
  { id: 2, name: 'CodeMate' },
  { id: 3, name: '云集' },
  { id: 4, name: '云见' },
  { id: 5, name: '扶摇' },
  { id: 6, name: '纠错Agent' },
  { id: 7, name: 'DT' }
])

// 不同专区的标签
const zoneTags = {
  practices: ['自然语言处理', '计算机视觉', '深度学习', 'AI伦理', '机器学习', '机器人', '数据科学', '生成式AI', 'PyTorch', 'TensorFlow'],
  tools: ['新手', '进阶', '最佳实践', '技巧', '案例', '教程', '优化', '通用'],
  agent: ['Agent应用', '工作流', '自动化', '智能编排', '案例分享'],
  empowerment: ['讨论', '提问', '分享', '经验', '工具', '技巧', '案例', '教程', '最佳实践', '问题解决']
}

// 当前可用的标签
const availableTags = computed(() => {
  if (formData.value.zone === 'tools' && formData.value.toolId) {
    return zoneTags.tools
  } else if (formData.value.zone) {
    return zoneTags[formData.value.zone as keyof typeof zoneTags] || []
  }
  return []
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
      validator: (rule: any, value: any, callback: any) => {
        if (formData.value.zone === 'tools' && !value) {
          callback(new Error('请选择工具'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  title: [{ required: true, message: '请输入帖子标题', trigger: 'blur' }],
  summary: [{ required: true, message: '请输入内容简介', trigger: 'blur' }],
  content: [{ required: true, message: '请输入帖子内容', trigger: 'blur' }]
}

// 字数统计
const wordCount = computed(() => {
  if (!formData.value.content) return 0
  // 移除HTML标签后计算字数
  const text = formData.value.content.replace(/<[^>]*>/g, '').replace(/&nbsp;/g, ' ')
  return text.length
})

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
const handleZoneChange = () => {
  formData.value.toolId = null
  // 保留预设标签，清除自定义标签
  formData.value.tags = formData.value.tags.filter(tag => isPresetTag(tag))
  presetTags.value = []
  selectedTag.value = ''
  handleAutoSave()
}

// 工具切换
const handleToolChange = () => {
  // 保留预设标签，清除自定义标签
  formData.value.tags = formData.value.tags.filter(tag => isPresetTag(tag))
  presetTags.value = []
  selectedTag.value = ''
  handleAutoSave()
}

// 添加标签
const handleTagAdd = (tag: string) => {
  if (tag && !formData.value.tags.includes(tag)) {
    formData.value.tags.push(tag)
    // 如果是已有标签，标记为预设标签
    if (availableTags.value.includes(tag)) {
      presetTags.value.push(tag)
    }
    selectedTag.value = ''
    handleAutoSave()
  }
}

// 移除标签（只能移除自定义标签）
const removeTag = (index: number) => {
  const tag = formData.value.tags[index]
  if (tag && !isPresetTag(tag)) {
    formData.value.tags.splice(index, 1)
    handleAutoSave()
  }
}

// Quill编辑器配置
const editorOptions = {
  theme: 'snow',
  placeholder: '请输入帖子内容...',
  modules: {
    toolbar: [
      [{ 'header': [1, 2, 3, false] }],
      ['bold', 'italic', 'underline', 'strike'],
      [{ 'color': [] }, { 'background': [] }],
      [{ 'list': 'ordered'}, { 'list': 'bullet' }],
      [{ 'align': [] }],
      ['link', 'image'],
      ['clean'],
      ['code-block']
    ]
  }
}

// 编辑器内容变化
const handleEditorChange = () => {
  handleAutoSave()
}

// 封面上传成功
const handleCoverSuccess = (response: any, file: File) => {
  // 这里应该从响应中获取图片URL，暂时使用本地预览
  if (file) {
    formData.value.cover = URL.createObjectURL(file)
    handleAutoSave()
  }
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

// 推荐封面
const handleRecommendCover = async () => {
  recommending.value = true
  try {
    // 这里应该调用后端API获取推荐封面
    // const response = await getRecommendedCover(formData.value.title, formData.value.summary)
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 模拟推荐封面URL
    const recommendedCovers = [
      'https://picsum.photos/800/400?random=1',
      'https://picsum.photos/800/400?random=2',
      'https://picsum.photos/800/400?random=3',
      'https://picsum.photos/800/400?random=4',
      'https://picsum.photos/800/400?random=5'
    ]
    const randomIndex = Math.floor(Math.random() * recommendedCovers.length)
    const randomCover = recommendedCovers[randomIndex]
    
    if (randomCover) {
      formData.value.cover = randomCover
    }
    ElMessage.success('已推荐封面')
    handleAutoSave()
  } catch (error) {
    ElMessage.error('获取推荐封面失败')
  } finally {
    recommending.value = false
  }
}

// 自动保存草稿
const handleAutoSave = () => {
  if (autoSaveTimer) {
    clearTimeout(autoSaveTimer)
  }
  autoSaveTimer = setTimeout(() => {
    const draft = {
      ...formData.value,
      savedAt: new Date().toISOString()
    }
    localStorage.setItem('post_draft', JSON.stringify(draft))
    lastSaveTime.value = new Date().toLocaleTimeString('zh-CN', {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    })
  }, 2000) // 2秒后自动保存
}

// 手动保存草稿
const handleSaveDraft = () => {
  const draft = {
    ...formData.value,
    savedAt: new Date().toISOString()
  }
  localStorage.setItem('post_draft', JSON.stringify(draft))
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

  try {
    await formRef.value.validate()
    
    if (!formData.value.content || wordCount.value === 0) {
      ElMessage.error('请输入帖子内容')
      return
    }

    publishing.value = true

    // 这里应该调用API发布帖子
    // await publishPost(formData.value)
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 清除草稿
    localStorage.removeItem('post_draft')
    
    ElMessage.success('帖子发布成功！')
    
    // 根据专区跳转到对应页面
    const routeMap: Record<string, string> = {
      practices: '/practices',
      tools: '/tools',
      agent: '/agent',
      empowerment: '/empowerment'
    }
    router.push(routeMap[formData.value.zone] || '/practices')
  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败，请检查表单')
  } finally {
    publishing.value = false
  }
}

// 加载草稿
const loadDraft = () => {
  const draftStr = localStorage.getItem('post_draft')
  if (draftStr) {
    try {
      const draft = JSON.parse(draftStr)
      formData.value = { ...draft }
      ElMessage.info('已加载草稿')
    } catch (error) {
      console.error('加载草稿失败:', error)
    }
  }
}

// 上传地址（实际应该配置为真实的上传接口）
const uploadAction = '#'

onMounted(() => {
  loadDraft()
})

onUnmounted(() => {
  if (autoSaveTimer) {
    clearTimeout(autoSaveTimer)
  }
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

  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    color: #333;
  }
}

.tags-section {
  width: 100%;

  .tags-display {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 12px;
    min-height: 32px;
    padding: 8px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    background: #fff;

    .tag-item {
      margin: 0;
    }
  }

  .tags-input-section {
    width: 100%;
  }
}

.cover-upload-section {
  .cover-options {
    display: flex;
    align-items: flex-start;
    gap: 12px;
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

    .cover-image {
      width: 200px;
      height: 120px;
      object-fit: cover;
      display: block;
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

  .cover-tips {
    margin-top: 8px;
    font-size: 12px;
    color: #909399;

    p {
      margin: 0;
    }
  }
}

.editor-wrapper {
  width: 100%;

  :deep(.quill-editor) {
    .ql-container {
      min-height: 400px;
      font-size: 14px;
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
    }

    .ql-editor {
      min-height: 400px;
    }

    .ql-toolbar {
      border-top: 1px solid #dcdfe6;
      border-left: 1px solid #dcdfe6;
      border-right: 1px solid #dcdfe6;
      border-bottom: none;
      border-radius: 4px 4px 0 0;
      background: #fafafa;
    }

    .ql-container {
      border-bottom: 1px solid #dcdfe6;
      border-left: 1px solid #dcdfe6;
      border-right: 1px solid #dcdfe6;
      border-top: none;
      border-radius: 0 0 4px 4px;
    }

    .ql-editor.ql-blank::before {
      color: #c0c4cc;
      font-style: normal;
    }
  }

  .editor-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 8px;
    font-size: 12px;
    color: #909399;

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
