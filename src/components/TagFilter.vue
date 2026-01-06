<template>
  <div class="tag-filter">
    <h3 class="filter-title" v-if="title">{{ title }}</h3>
    <div class="tags-container">
      <el-tag
        v-for="tag in tags"
        :key="tag.name"
        :class="['tag-item', { 'tag-item-active': selectedTag === tag.name }]"
        :style="getTagStyle(tag.name)"
        @click="handleTagClick(tag.name)"
      >
        #{{ tag.name }} ({{ tag.count }})
      </el-tag>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Tag {
  name: string
  count: number
}

interface Props {
  tags: Tag[]
  selectedTag?: string | null
  title?: string
  colorMap?: Record<string, { bg: string; border: string; text: string }>
}

const props = withDefaults(defineProps<Props>(), {
  selectedTag: null,
  title: '',
  colorMap: () => ({})
})

const emit = defineEmits<{
  tagClick: [tagName: string]
}>()

// 默认颜色映射
const defaultColorMap: Record<string, { bg: string; border: string; text: string }> = {
  '自然语言处理': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  '计算机视觉': { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },
  '深度学习': { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },
  'AI伦理': { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },
  '机器学习': { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },
  '机器人': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  '数据科学': { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },
  '生成式AI': { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },
  'PyTorch': { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },
  'TensorFlow': { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },
  '讨论': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  '提问': { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },
  '分享': { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },
  '经验': { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },
  '工具': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  '技巧': { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },
  '案例': { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },
  '教程': { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },
  '最佳实践': { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },
  '问题解决': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  'Agent应用': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  '工作流': { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },
  '自动化': { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },
  '智能编排': { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },
  '案例分享': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' }
}

// 获取标签颜色样式
const getTagStyle = (tagName: string) => {
  const colorMap = { ...defaultColorMap, ...props.colorMap }
  const colors = colorMap[tagName] || { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' }
  const isActive = props.selectedTag === tagName

  return {
    backgroundColor: isActive ? colors.bg.replace('0.15', '0.25') : colors.bg,
    borderColor: colors.border,
    color: colors.text,
    borderWidth: '1px',
    borderStyle: 'solid'
  }
}

// 处理标签点击
const handleTagClick = (tagName: string) => {
  emit('tagClick', tagName)
}
</script>

<style scoped lang="scss">
.tag-filter {
  .filter-title {
    margin: 0 0 16px 0;
    font-size: 18px;
    font-weight: 600;
    color: #000;
  }

  .tags-container {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
  }

  .tag-item {
    cursor: pointer;
    transition: all 0.2s;
    border: 1px solid !important;
    background: transparent !important;
    color: inherit !important;

    &:hover {
      transform: translateY(-2px);
      opacity: 0.8;
    }

    &.tag-item-active {
      font-weight: 600;
    }
  }
}
</style>

