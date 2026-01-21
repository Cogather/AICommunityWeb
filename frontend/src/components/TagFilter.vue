<template>
  <div class="tag-filter">
    <h3 class="filter-title" v-if="title">{{ title }}</h3>
    <div class="tags-container">
      <el-tag
        v-for="tag in tags"
        :key="tag.name"
        :class="['tag-item', { 
          'tag-item-active': tag.name === '全部' 
            ? (selectedTag === null || selectedTag === '全部')
            : selectedTag === tag.name 
        }]"
        :style="getTagStyle(tag.name)"
        @click="handleTagClick(tag.name)"
      >
        <span v-if="tag.name === '全部'">{{ tag.name }} ({{ tag.count }})</span>
        <span v-else>#{{ tag.name }} ({{ tag.count }})</span>
      </el-tag>
    </div>
  </div>
</template>

<script setup lang="ts">
// computed removed - not used

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

const tagBlue = '#2563eb'

// 获取标签颜色样式
const getTagStyle = (tagName: string) => {
  const isActive =
    tagName === '全部'
      ? props.selectedTag === null || props.selectedTag === '全部'
      : props.selectedTag === tagName

  return {
    // 统一：白底、蓝字、蓝边框
    '--tag-bg': '#ffffff',
    '--tag-border': tagBlue,
    '--tag-color': tagBlue,
    backgroundColor: '#ffffff',
    borderColor: tagBlue,
    color: tagBlue,
    borderWidth: isActive ? '2px' : '1.5px',
    borderStyle: 'solid',
    fontWeight: isActive ? '700' : '600',
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
    font-weight: 600;

    // 使用 :deep() 覆盖 Element Plus 的默认样式
    :deep(.el-tag__content) {
      color: var(--tag-color, inherit) !important;
      font-size: inherit;
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
    font-size: 14px;
    padding: 8px 14px;
    border-radius: 999px;
    line-height: 1;
    height: auto;
  }
}
</style>

