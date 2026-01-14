<template>
  <el-tooltip content="发布帖子" placement="left">
    <el-button
      class="post-fab"
      type="primary"
      size="large"
      @click="goCreate"
    >
      <el-icon class="post-fab__icon"><Edit /></el-icon>
      <span class="post-fab__label">发帖</span>
    </el-button>
  </el-tooltip>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { Edit } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { ROUTES } from '../router/paths'

const router = useRouter()
const route = useRoute()

const goCreate = () => {
  console.log('PostFab: 点击发帖按钮')
  
  // 检查当前是否在编辑帖子页面
  const isEditMode = route.path === ROUTES.POST_CREATE && route.query.edit === 'true' && route.query.id
  
  if (isEditMode) {
    // 如果在编辑模式，先询问是否离开
    ElMessageBox.confirm(
      '确定要离开编辑页面，创建新帖子吗？',
      '提示',
      {
        confirmButtonText: '确定离开',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      // 记录来源信息，用于返回时使用
      const fromEditId = route.query.id
      const fromPath = route.path
      
      // 跳转到新建页面，并传递来源信息
      // 使用 skipLeaveCheck 标记，表示已经询问过用户，跳过 onBeforeRouteLeave 的询问
      router.push({
        path: ROUTES.POST_CREATE,
        query: {
          fromEdit: 'true',
          fromEditId: fromEditId as string,
          fromPath: fromPath,
          skipLeaveCheck: 'true'
        }
      }).catch((err) => {
        console.error('路由跳转失败:', err)
      })
    }).catch(() => {
      // 用户取消，不执行任何操作
    })
  } else {
    // 不在编辑模式，直接跳转
    router.push(ROUTES.POST_CREATE).catch((err) => {
      console.error('路由跳转失败:', err)
    })
  }
}
</script>

<style scoped lang="scss">
.post-fab {
  position: fixed;
  right: 24px;
  bottom: 72px;
  z-index: 1300;
  box-shadow: 0 20px 50px rgba(64, 158, 255, 0.55), 0 0 0 8px rgba(64, 158, 255, 0.16);
  transition: all 0.25s ease, transform 0.25s ease;
  border: none;
  padding: 0 26px;
  height: 60px;
  border-radius: 999px;
  background: linear-gradient(135deg, #6ac4ff 0%, #409eff 50%, #1a74ea 100%);
  font-weight: 700;
  letter-spacing: 0.5px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  backdrop-filter: blur(6px);
  transform: translateY(0);

  &::after {
    content: '';
    position: absolute;
    inset: -10px;
    border-radius: inherit;
    background: radial-gradient(circle at 50% 50%, rgba(64, 158, 255, 0.35), rgba(64, 158, 255, 0));
    z-index: -1;
    opacity: 0.75;
    filter: blur(8px);
    transition: opacity 0.25s ease, transform 0.25s ease;
  }

  &__icon {
    font-size: 18px;
  }

  &__label {
    font-size: 16px;
    letter-spacing: 0.6px;
  }

  &:hover {
    transform: translateY(-4px) scale(1.03);
    box-shadow: 0 24px 58px rgba(64, 158, 255, 0.65), 0 0 0 10px rgba(64, 158, 255, 0.18);

    &::after {
      opacity: 1;
      transform: scale(1.02);
    }
  }
}

@media (max-width: 768px) {
  .post-fab {
    right: 16px;
    bottom: 48px;
    height: 52px;
    padding: 0 18px;

    &__label {
      font-size: 15px;
    }
  }
}
</style>

