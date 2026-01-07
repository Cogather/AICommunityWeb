<script setup lang="ts">
import { RouterView } from 'vue-router'
import AppNavbar from './components/AppNavbar.vue'
import AppFooter from './components/AppFooter.vue'
import PostFab from './components/PostFab.vue'
</script>

<template>
  <div class="app-shell">
    <!-- 全局背景（所有页面使用） -->
    <div class="global-background">
      <!-- 3D 气泡球体 - 分布在边缘 -->
      <i class="bubble b1"></i>
      <i class="bubble b2"></i>
      <i class="bubble b3"></i>
      <i class="bubble b4"></i>
      <i class="bubble b5"></i>
      <i class="bubble b6"></i>
      <i class="bubble b7"></i>
      
      <!-- 微光点层 -->
      <i class="sparkles"></i>
    </div>

    <!-- 导航栏 -->
    <AppNavbar />

    <!-- 页面内容 -->
    <main class="page-content">
      <RouterView />
      <PostFab />
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
  
  /* 底层：雾化渐变（左下最亮、右上偏紫、右下偏蓝）- 使用指定颜色 */
  background:
    radial-gradient(120% 140% at 18% 95%,
      #D1EBFF 0%,
      #C1E5FF 28%,
      rgba(193, 229, 255, 0) 70%),
    radial-gradient(110% 130% at 85% 10%,
      #C2C7EF 0%,
      #C5CBF0 35%,
      rgba(197, 203, 240, 0) 72%),
    radial-gradient(120% 150% at 92% 85%,
      #B8D5FC 0%,
      #BED9FD 42%,
      rgba(190, 217, 253, 0) 78%),
    linear-gradient(135deg,
      #C1E5FF 0%,
      #C5CBF0 38%,
      #BED9FD 100%);
}

/* 气泡通用：用"凝胶球体渐变 + 镜面高光 + 外缘亮边" */
.bubble {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;

  /* 球体主体：深色核心→亮蓝→淡紫→透明边 */
  background:
    radial-gradient(circle at 30% 28%,
      rgba(241, 246, 253, 0.95) 0%,
      rgba(241, 246, 253, 0.00) 22%),
    radial-gradient(circle at 55% 60%,
      rgba(40, 96, 248, 0.95) 0%,
      rgba(104, 112, 248, 0.70) 32%,
      rgba(144, 112, 248, 0.35) 56%,
      rgba(241, 246, 253, 0.00) 76%),
    radial-gradient(circle at 78% 82%,
      rgba(241, 246, 253, 0.18) 0%,
      rgba(241, 246, 253, 0.00) 58%);

  /* 外缘轻亮边（玻璃感） */
  box-shadow:
    inset 0 0 0 1px rgba(241, 246, 253, 0.35),
    0 10px 20px rgba(40, 96, 248, 0.10);

  /* 轻微虚化，避免"矢量太硬" */
  filter: blur(0.25px);
  opacity: 0.95;

  /* 关键：更像"光学叠加"的透明球体 */
  mix-blend-mode: screen;
}

/* 左侧大蓝球：半出画 */
.b1 {
  width: 140px;
  height: 140px;
  left: -90px;
  top: -20px;
  animation: bubbleFloat1 25s ease-in-out infinite;
}

/* 左侧第二个球：靠上、偏紫蓝 */
.b2 {
  width: 78px;
  height: 78px;
  left: 18px;
  top: -26px;
  opacity: 0.90;
  animation: bubbleFloat2 28s ease-in-out infinite;
}

/* 上中小球：淡紫 */
.b3 {
  width: 58px;
  height: 58px;
  left: 34%;
  top: -18px;
  background:
    radial-gradient(circle at 32% 28%, rgba(241, 246, 253, 0.92) 0%, rgba(241, 246, 253, 0) 20%),
    radial-gradient(circle at 56% 62%, rgba(184, 144, 248, 0.92) 0%, rgba(144, 112, 248, 0.55) 35%, rgba(241, 246, 253, 0) 75%);
  animation: bubbleFloat3 30s ease-in-out infinite;
}

/* 右上紫球 */
.b4 {
  width: 72px;
  height: 72px;
  right: 72px;
  top: -30px;
  background:
    radial-gradient(circle at 32% 28%, rgba(241, 246, 253, 0.92) 0%, rgba(241, 246, 253, 0) 20%),
    radial-gradient(circle at 55% 62%, rgba(184, 144, 248, 0.95) 0%, rgba(144, 112, 248, 0.55) 40%, rgba(241, 246, 253, 0) 78%);
  animation: bubbleFloat4 27s ease-in-out infinite;
}

/* 右上偏中蓝球 */
.b5 {
  width: 64px;
  height: 64px;
  right: 18px;
  top: -10px;
  animation: bubbleFloat5 26s ease-in-out infinite;
}

/* 右侧中部小蓝球 */
.b6 {
  width: 54px;
  height: 54px;
  right: -10px;
  top: 38px;
  opacity: 0.88;
  animation: bubbleFloat6 29s ease-in-out infinite;
}

/* 右下大蓝球：半出画 */
.b7 {
  width: 120px;
  height: 120px;
  right: -42px;
  bottom: -40px;
  background:
    radial-gradient(circle at 30% 28%, rgba(241, 246, 253, 0.95) 0%, rgba(241, 246, 253, 0) 22%),
    radial-gradient(circle at 54% 62%, rgba(40, 96, 248, 0.92) 0%, rgba(104, 168, 248, 0.65) 36%, rgba(241, 246, 253, 0) 78%);
  animation: bubbleFloat7 32s ease-in-out infinite;
}

/* 微光点（星屑/颗粒）：用多重 radial-gradient + 很低透明度 */
.sparkles {
  position: absolute;
  inset: 0;
  pointer-events: none;
  background:
    radial-gradient(circle at 12% 22%, rgba(241, 246, 253, 0.55) 0 1px, rgba(241, 246, 253, 0) 2px),
    radial-gradient(circle at 18% 18%, rgba(241, 246, 253, 0.35) 0 1px, rgba(241, 246, 253, 0) 2px),
    radial-gradient(circle at 40% 14%, rgba(241, 246, 253, 0.35) 0 1px, rgba(241, 246, 253, 0) 2px),
    radial-gradient(circle at 62% 18%, rgba(241, 246, 253, 0.30) 0 1px, rgba(241, 246, 253, 0) 2px),
    radial-gradient(circle at 86% 22%, rgba(241, 246, 253, 0.38) 0 1px, rgba(241, 246, 253, 0) 2px),
    radial-gradient(circle at 92% 30%, rgba(241, 246, 253, 0.28) 0 1px, rgba(241, 246, 253, 0) 2px);
  opacity: 0.65;
  filter: blur(0.2px);
  mix-blend-mode: screen;
  animation: sparklesTwinkle 8s ease-in-out infinite;
}

/* 气泡浮动动画 - 营造景深和空间感 */
@keyframes bubbleFloat1 {
  0%, 100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.95;
  }
  33% {
    transform: translate(20px, 15px) scale(1.02);
    opacity: 0.98;
  }
  66% {
    transform: translate(-15px, 10px) scale(0.98);
    opacity: 0.92;
  }
}

@keyframes bubbleFloat2 {
  0%, 100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.90;
  }
  25% {
    transform: translate(-10px, 12px) scale(1.03);
    opacity: 0.93;
  }
  75% {
    transform: translate(8px, -8px) scale(0.97);
    opacity: 0.87;
  }
}

@keyframes bubbleFloat3 {
  0%, 100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.92;
  }
  50% {
    transform: translate(5px, 10px) scale(1.05);
    opacity: 0.95;
  }
}

@keyframes bubbleFloat4 {
  0%, 100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.95;
  }
  40% {
    transform: translate(-12px, -15px) scale(1.04);
    opacity: 0.98;
  }
  80% {
    transform: translate(8px, -10px) scale(0.96);
    opacity: 0.92;
  }
}

@keyframes bubbleFloat5 {
  0%, 100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.95;
  }
  30% {
    transform: translate(-8px, -10px) scale(1.02);
    opacity: 0.98;
  }
  70% {
    transform: translate(10px, -8px) scale(0.98);
    opacity: 0.92;
  }
}

@keyframes bubbleFloat6 {
  0%, 100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.88;
  }
  35% {
    transform: translate(-8px, 12px) scale(1.03);
    opacity: 0.91;
  }
  70% {
    transform: translate(6px, 8px) scale(0.97);
    opacity: 0.85;
  }
}

@keyframes bubbleFloat7 {
  0%, 100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.95;
  }
  40% {
    transform: translate(-10px, -12px) scale(1.02);
    opacity: 0.98;
  }
  80% {
    transform: translate(8px, -8px) scale(0.98);
    opacity: 0.92;
  }
}

/* 微光点闪烁动画 */
@keyframes sparklesTwinkle {
  0%, 100% {
    opacity: 0.65;
  }
  50% {
    opacity: 0.75;
  }
}

.page-content {
  position: relative;
  z-index: 2;
  min-height: calc(100vh - 200px); /* 减去导航栏和footer的高度 */
}
</style>
