<template>
  <div class="not-found-view">
    <div class="not-found-container">
      <div class="not-found-content">
        <div class="error-code">404</div>
        <h1 class="error-title">页面未找到</h1>
        <p class="error-description">抱歉，您访问的页面不存在或已被删除</p>
        <div class="error-actions">
          <el-button type="primary" @click="handleGoHome">
            返回首页
          </el-button>
          <el-button @click="handleGoBack">
            返回上一页
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ROUTES } from '../router/paths'

const router = useRouter()

const handleGoHome = () => {
  router.push(ROUTES.HOME)
}

const handleGoBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push(ROUTES.HOME)
  }
}
</script>

<style scoped lang="scss">
.not-found-view {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.not-found-container {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 60px 40px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 600px;
  width: 100%;
}

.not-found-content {
  .error-code {
    font-size: 120px;
    font-weight: 700;
    color: #409eff;
    line-height: 1;
    margin-bottom: 20px;
  }

  .error-title {
    font-size: 32px;
    font-weight: 600;
    color: #333;
    margin: 0 0 16px 0;
  }

  .error-description {
    font-size: 16px;
    color: #666;
    margin: 0 0 32px 0;
  }

  .error-actions {
    display: flex;
    gap: 12px;
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .not-found-container {
    padding: 40px 20px;
  }

  .not-found-content {
    .error-code {
      font-size: 80px;
    }

    .error-title {
      font-size: 24px;
    }

    .error-description {
      font-size: 14px;
    }

    .error-actions {
      flex-direction: column;
    }
  }
}
</style>

