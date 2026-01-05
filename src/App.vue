<script setup lang="ts">
import { RouterView } from 'vue-router'
import AppNavbar from './components/AppNavbar.vue'
import AppFooter from './components/AppFooter.vue'
</script>

<template>
  <div class="app-shell">
    <!-- 全局背景（所有页面使用） -->
    <div class="global-background">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
    </div>

    <!-- 导航栏 -->
    <AppNavbar />

    <!-- 页面内容 -->
    <main class="page-content">
      <RouterView />
    </main>

    <!-- Footer -->
    <AppFooter />
  </div>
</template>

<style scoped lang="scss">
.app-shell {
  min-height: 100vh;
  position: relative;
}

/* 全局背景（所有页面使用） */
.global-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  overflow: hidden;
  
  /* 动态渐变背景 */
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
  background-size: 200% 200%;
  animation: gradientShift 15s ease infinite;
  
  /* 多层渐变叠加 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(160deg, #6a11cb 0%, #2575fc 100%);
    background-size: 200% 200%;
    animation: gradientShift 20s ease infinite reverse;
    opacity: 0.8;
  }

  .blob {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.6;
  }
  
  .blob-1 {
    top: -10%;
    left: -10%;
    width: 600px;
    height: 600px;
    background: #a18cd1;
    animation: blobMove1 20s ease-in-out infinite;
  }
  
  .blob-2 {
    bottom: -10%;
    right: -10%;
    width: 700px;
    height: 700px;
    background: #4facfe;
    animation: blobMove2 25s ease-in-out infinite;
  }
}

/* 背景渐变移动动画 */
@keyframes gradientShift {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

/* 光斑移动动画 */
@keyframes blobMove1 {
  0% {
    transform: translate(0, 0) scale(1);
    opacity: 0.6;
  }
  25% {
    transform: translate(50px, 80px) scale(1.1);
    opacity: 0.7;
  }
  50% {
    transform: translate(100px, 50px) scale(0.9);
    opacity: 0.5;
  }
  75% {
    transform: translate(30px, 100px) scale(1.05);
    opacity: 0.65;
  }
  100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.6;
  }
}

@keyframes blobMove2 {
  0% {
    transform: translate(0, 0) scale(1);
    opacity: 0.6;
  }
  33% {
    transform: translate(-80px, -50px) scale(1.15);
    opacity: 0.7;
  }
  66% {
    transform: translate(-50px, -100px) scale(0.85);
    opacity: 0.5;
  }
  100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.6;
  }
}

.page-content {
  position: relative;
  z-index: 2;
  min-height: calc(100vh - 200px); /* 减去导航栏和footer的高度 */
}
</style>
