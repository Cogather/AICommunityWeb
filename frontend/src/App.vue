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
    <div class="global-background"></div>

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
  
  /* 背景图片 */
  background-image: url('/background.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  
  /* 如果图片较暗，可以添加遮罩层来确保内容可读性 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.1); /* 可选的半透明遮罩，根据需要调整透明度 */
    pointer-events: none;
  }
}


.page-content {
  position: relative;
  z-index: 2;
  min-height: calc(100vh - 200px); /* 减去导航栏和footer的高度 */
}
</style>
