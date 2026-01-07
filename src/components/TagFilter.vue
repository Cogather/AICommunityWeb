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

// 颜色池 - 为不同标签提供不同颜色
const colorPalette = [
  { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },      // 蓝色
  { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },      // 绿色
  { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },      // 橙色
  { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },    // 红色
  { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },    // 灰色
  { bg: 'rgba(156, 39, 176, 0.15)', border: '#9c27b0', text: '#9c27b0' },    // 紫色
  { bg: 'rgba(0, 188, 212, 0.15)', border: '#00bcd4', text: '#00bcd4' },      // 青色
  { bg: 'rgba(255, 152, 0, 0.15)', border: '#ff9800', text: '#ff9800' },      // 深橙色
  { bg: 'rgba(76, 175, 80, 0.15)', border: '#4caf50', text: '#4caf50' },      // 深绿色
  { bg: 'rgba(233, 30, 99, 0.15)', border: '#e91e63', text: '#e91e63' },      // 粉红色
  { bg: 'rgba(63, 81, 181, 0.15)', border: '#3f51b5', text: '#3f51b5' },      // 靛蓝色
  { bg: 'rgba(0, 150, 136, 0.15)', border: '#009688', text: '#009688' },      // 青绿色
]

// 默认颜色映射 - 为常见标签分配特定颜色
const defaultColorMap: Record<string, { bg: string; border: string; text: string }> = {
  '自然语言处理': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  '计算机视觉': { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },
  '深度学习': { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },
  'AI伦理': { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },
  '机器学习': { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },
  '机器人': { bg: 'rgba(156, 39, 176, 0.15)', border: '#9c27b0', text: '#9c27b0' },
  '数据科学': { bg: 'rgba(0, 188, 212, 0.15)', border: '#00bcd4', text: '#00bcd4' },
  '生成式AI': { bg: 'rgba(255, 152, 0, 0.15)', border: '#ff9800', text: '#ff9800' },
  'PyTorch': { bg: 'rgba(76, 175, 80, 0.15)', border: '#4caf50', text: '#4caf50' },
  'TensorFlow': { bg: 'rgba(233, 30, 99, 0.15)', border: '#e91e63', text: '#e91e63' },
  '讨论': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  '提问': { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },
  '分享': { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },
  '经验': { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },
  '工具': { bg: 'rgba(156, 39, 176, 0.15)', border: '#9c27b0', text: '#9c27b0' },
  '技巧': { bg: 'rgba(0, 188, 212, 0.15)', border: '#00bcd4', text: '#00bcd4' },
  '案例': { bg: 'rgba(255, 152, 0, 0.15)', border: '#ff9800', text: '#ff9800' },
  '教程': { bg: 'rgba(76, 175, 80, 0.15)', border: '#4caf50', text: '#4caf50' },
  '最佳实践': { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },
  '问题解决': { bg: 'rgba(233, 30, 99, 0.15)', border: '#e91e63', text: '#e91e63' },
  'Agent应用': { bg: 'rgba(63, 81, 181, 0.15)', border: '#3f51b5', text: '#3f51b5' },
  '工作流': { bg: 'rgba(0, 150, 136, 0.15)', border: '#009688', text: '#009688' },
  '自动化': { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' },
  '智能编排': { bg: 'rgba(245, 108, 108, 0.15)', border: '#f56c6c', text: '#f56c6c' },
  '案例分享': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  '新手': { bg: 'rgba(64, 158, 255, 0.15)', border: '#409eff', text: '#409eff' },
  '进阶': { bg: 'rgba(103, 194, 58, 0.15)', border: '#67c23a', text: '#67c23a' },
  '通用': { bg: 'rgba(144, 147, 153, 0.15)', border: '#909399', text: '#909399' },
  '优化': { bg: 'rgba(230, 162, 60, 0.15)', border: '#e6a23c', text: '#e6a23c' }
}

// 根据标签名称生成哈希值，用于分配颜色
const getTagColorIndex = (tagName: string): number => {
  let hash = 0
  for (let i = 0; i < tagName.length; i++) {
    hash = tagName.charCodeAt(i) + ((hash << 5) - hash)
  }
  return Math.abs(hash) % colorPalette.length
}

// 获取标签颜色样式
const getTagStyle = (tagName: string) => {
  const colorMap = { ...defaultColorMap, ...props.colorMap }
  let colors = colorMap[tagName]
  
  // 如果标签没有预定义颜色，从颜色池中根据名称分配
  if (!colors) {
    const colorIndex = getTagColorIndex(tagName)
    colors = colorPalette[colorIndex] || colorPalette[0]
  }
  
  // 确保 colors 不为 undefined
  const finalColors: { bg: string; border: string; text: string } = (colors || colorPalette[0]) as { bg: string; border: string; text: string }
  const isActive = props.selectedTag === tagName

  return {
    '--tag-bg': isActive ? finalColors.bg.replace('0.15', '0.25') : finalColors.bg,
    '--tag-border': finalColors.border,
    '--tag-color': finalColors.text,
    backgroundColor: isActive ? finalColors.bg.replace('0.15', '0.25') : finalColors.bg,
    borderColor: finalColors.border,
    color: finalColors.text,
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
    font-weight: 500;

    // 使用 :deep() 覆盖 Element Plus 的默认样式
    :deep(.el-tag__content) {
      color: var(--tag-color, inherit) !important;
    }

    &:hover {
      transform: translateY(-2px);
      opacity: 0.8;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    &.tag-item-active {
      font-weight: 600;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }
  }

  // 使用全局样式覆盖 Element Plus 标签样式
  :deep(.el-tag.tag-item) {
    background-color: var(--tag-bg, transparent) !important;
    border-color: var(--tag-border, currentColor) !important;
    color: var(--tag-color, inherit) !important;
  }
}
</style>

