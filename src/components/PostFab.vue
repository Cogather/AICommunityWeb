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
import { useRouter } from 'vue-router'
import { Edit } from '@element-plus/icons-vue'

const router = useRouter()

const goCreate = () => {
  router.push('/post/create')
}
</script>

<style scoped lang="scss">
.post-fab {
  position: fixed;
  right: 36px;
  bottom: 48px;
  z-index: 1200;
  box-shadow: 0 18px 40px rgba(64, 158, 255, 0.45), 0 0 0 6px rgba(64, 158, 255, 0.12);
  transition: all 0.25s ease;
  border: none;
  padding: 0 20px;
  height: 52px;
  border-radius: 999px;
  background: linear-gradient(135deg, #5bb0ff 0%, #409eff 55%, #1d7ef7 100%);
  font-weight: 700;
  letter-spacing: 0.5px;

  &__icon {
    font-size: 18px;
    margin-right: 6px;
  }

  &__label {
    font-size: 15px;
  }

  &:hover {
    transform: translateY(-3px) scale(1.02);
    box-shadow: 0 22px 48px rgba(64, 158, 255, 0.55), 0 0 0 8px rgba(64, 158, 255, 0.16);
  }
}

@media (max-width: 768px) {
  .post-fab {
    right: 20px;
    bottom: 28px;
    height: 48px;
    padding: 0 16px;

    &__label {
      font-size: 14px;
    }
  }
}
</style>

