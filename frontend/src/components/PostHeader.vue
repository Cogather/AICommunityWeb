<template>
  <div class="posts-header">
    <div class="header-left">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索作者、关键词"
        class="search-input"
        clearable
        @input="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-select v-model="sortBy" placeholder="按最新排序" style="width: 150px" @change="handleSort">
        <el-option label="按最新排序" value="newest" />
        <el-option label="按热度排序" value="hot" />
        <el-option label="按评论数排序" value="comments" />
        <el-option label="按点赞数排序" value="likes" />
      </el-select>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'

interface Props {
  defaultSort?: 'newest' | 'hot' | 'comments' | 'likes'
}

const props = withDefaults(defineProps<Props>(), {
  defaultSort: 'newest'
})

const emit = defineEmits<{
  search: [keyword: string]
  sort: [sortBy: 'newest' | 'hot' | 'comments' | 'likes']
}>()
const searchKeyword = ref('')
const sortBy = ref<'newest' | 'hot' | 'comments' | 'likes'>(props.defaultSort as 'newest' | 'hot' | 'comments' | 'likes')

const handleSearch = () => {
  emit('search', searchKeyword.value)
}

const handleSort = () => {
  emit('sort', sortBy.value)
}

// 暴露方法供父组件调用
defineExpose({
  searchKeyword,
  sortBy
})
</script>

<style scoped lang="scss">
.posts-header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);

  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;
    width: 100%;

    .search-input {
      flex: 1;
      max-width: 300px;
    }
  }
}

@media (max-width: 768px) {
  .posts-header {
    flex-direction: column;
    gap: 16px;

    .header-left {
      flex-wrap: wrap;
      width: 100%;

      .search-input {
        width: 100%;
        max-width: 100%;
      }
    }
  }
}
</style>

