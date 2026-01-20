<script setup lang="ts">
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import AppNavbar from './components/AppNavbar.vue'
import AppFooter from './components/AppFooter.vue'
import PostFab from './components/PostFab.vue'

const route = useRoute()
const showPostFab = computed(() => route.name !== 'Home')
</script>

<template>
  <div class="app-shell">
    <!-- 全局背景（所有页面使用） -->
    <div class="global-background">
      <!-- 神经网络背景：全屏深度网络网格 -->
      <svg
        class="neural-bg"
        width="100%"
        height="100%"
        viewBox="0 0 1920 1020"
        xmlns="http://www.w3.org/2000/svg"
      >
        <defs>
          <linearGradient id="deepNetGrad" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" style="stop-color:#0066FF;stop-opacity:0.05" />
            <stop offset="50%" style="stop-color:#0066FF;stop-opacity:0.5" />
            <stop offset="100%" style="stop-color:#0066FF;stop-opacity:0.05" />
          </linearGradient>
        </defs>

        <!-- 底色保持为纯白，由 body/background 控制整体浅灰调，这里不过度改动 -->
        <rect width="100%" height="100%" fill="none" />

        <g stroke="url(#deepNetGrad)" stroke-width="0.5" fill="none">
          <path d="M100,50 L200,150 L50,250 L100,50 M200,150 L350,100 L450,200 L200,150 M450,200 L600,150 L750,250 L450,200 M750,250 L900,150 L1050,200 L750,250 M1050,200 L1250,150 L1400,250 L1050,200 M1400,250 L1600,150 L1800,200 L1400,250" />
          <path d="M50,450 L200,350 L350,450 L150,550 L50,450 M350,450 L550,400 L700,550 L350,450 M700,550 L900,450 L1100,550 L700,550 M1100,550 L1350,450 L1550,550 L1100,550 M1550,550 L1750,450 L1900,600 L1550,550" />
          <path d="M100,850 L300,750 L450,900 L200,980 L100,850 M450,900 L650,850 L850,950 L450,900 M850,950 L1100,850 L1300,950 L850,950 M1300,950 L1550,850 L1750,950 L1300,950" />

          <path d="M200,150 L200,350 M450,200 L550,400 M750,250 L900,450 M1050,200 L1100,550 M1400,250 L1550,550 M350,450 L300,750 M700,550 L650,850 M1100,550 L1100,850 M1550,550 L1550,850" />

          <path d="M100,50 L350,450 M750,250 L350,450 M1050,200 L1550,550 M1800,200 L1550,550 M50,450 L300,750 M900,450 L1100,850 M1750,450 L1550,850" stroke-dasharray="2,2" opacity="0.3" />
        </g>

        <g fill="#0066FF">
          <circle cx="200" cy="150" r="4">
            <animate attributeName="opacity" values="0.2;0.9;0.2" dur="2s" repeatCount="indefinite" />
          </circle>
          <circle cx="450" cy="200" r="4.5">
            <animate attributeName="opacity" values="0.2;0.9;0.2" dur="4s" repeatCount="indefinite" />
          </circle>
          <circle cx="750" cy="250" r="4">
            <animate attributeName="opacity" values="0.2;0.9;0.2" dur="2s" repeatCount="indefinite" />
          </circle>
          <circle cx="1100" cy="550" r="5">
            <animate attributeName="opacity" values="0.2;0.9;0.2" dur="4s" repeatCount="indefinite" />
          </circle>
          <circle cx="1550" cy="550" r="4.5">
            <animate attributeName="opacity" values="0.2;0.9;0.2" dur="2s" repeatCount="indefinite" />
          </circle>
          <circle cx="850" cy="950" r="4">
            <animate attributeName="opacity" values="0.2;0.9;0.2" dur="4s" repeatCount="indefinite" />
          </circle>

          <g fill-opacity="0.5">
            <circle cx="100" cy="50" r="2.5" /> <circle cx="50" cy="250" r="2.5" /> <circle cx="350" cy="100" r="2.5" />
            <circle cx="600" cy="150" r="2.5" /> <circle cx="900" cy="150" r="2.5" /> <circle cx="1250" cy="150" r="2.5" />
            <circle cx="1600" cy="150" r="2.5" /> <circle cx="1800" cy="200" r="2.5" /> <circle cx="200" cy="350" r="2.5" />
            <circle cx="50" cy="450" r="2.5" /> <circle cx="350" cy="450" r="2.5" /> <circle cx="150" cy="550" r="2.5" />
            <circle cx="550" cy="400" r="3" /> <circle cx="700" cy="550" r="3" /> <circle cx="900" cy="450" r="2.5" />
            <circle cx="1350" cy="450" r="2.5" /> <circle cx="1750" cy="450" r="2.5" /> <circle cx="1900" cy="600" r="2.5" />
            <circle cx="300" cy="750" r="2.5" /> <circle cx="100" cy="850" r="2.5" /> <circle cx="450" cy="900" r="3" />
            <circle cx="200" cy="980" r="2.5" /> <circle cx="650" cy="850" r="2.5" /> <circle cx="1100" cy="850" r="3" />
            <circle cx="1300" cy="950" r="2.5" /> <circle cx="1550" cy="850" r="2.5" /> <circle cx="1750" cy="950" r="2.5" />
            <circle cx="500" cy="700" r="2" fill-opacity="0.3" /> <circle cx="1300" cy="300" r="2" fill-opacity="0.3" />
            <circle cx="1000" cy="400" r="2" fill-opacity="0.3" /> <circle cx="1600" cy="750" r="2" fill-opacity="0.3" />
            <circle cx="250" cy="250" r="2" fill-opacity="0.3" /> <circle cx="800" cy="600" r="2" fill-opacity="0.3" />
          </g>
        </g>
      </svg>
    </div>

    <!-- 导航栏 -->
    <AppNavbar />

    <!-- 页面内容 -->
    <main class="page-content">
      <RouterView />
      <PostFab v-if="showPostFab" />
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

  /* 最底层：浅冷灰 / 近白背景（明亮通透） */
  background: #f4f7fa;

  /* 顶层：非常淡的蓝色光晕（保持科技感但克制） */
  &::after {
    content: '';
    position: absolute;
    inset: 0;
    pointer-events: none;
    z-index: 2; /* 在神经网络 SVG 之上 */
    background:
      radial-gradient(900px 650px at 50% 20%, rgba(0, 102, 255, 0.06) 0%, rgba(0, 102, 255, 0) 60%),
      radial-gradient(700px 520px at 14% 88%, rgba(88, 86, 214, 0.04) 0%, rgba(88, 86, 214, 0) 62%);
  }
}

/* 神经网络背景层：全屏显示（使用 pattern 平铺） */
.neural-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0.6; /* 轻量科技蓝，不干扰阅读 */
  pointer-events: none;
  z-index: 1; /* 确保在背景之上，但在光晕层之下 */
}


.page-content {
  position: relative;
  z-index: 2;
  min-height: calc(100vh - 200px); /* 减去导航栏和footer的高度 */
}
</style>
