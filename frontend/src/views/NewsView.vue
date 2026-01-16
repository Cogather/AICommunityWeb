<template>
  <div class="news-view">
    <div class="boardStyle">
      <iframe v-if="iframeSrc" :src="iframeSrc" frameborder="0" style="width: 100%; height: 100%"></iframe>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import loginService from '../utils/loginService'

const iframeSrc = ref('')

onMounted(async () => {
  // 确保用户信息已加载
  if (!loginService.userInfo) {
    await loginService.validate()
  }

  const baseUrl = 'https://cogather.coreai.rnd.huawei.com/homepage/news/20251208'

  // 使用平台现在的 userId 获取方式
  const userId = loginService.userInfo?.userId || ''

  const params = new URLSearchParams()
  params.append('noTabs', '')
  if (userId) {
    params.append('userId', userId)
  }

  iframeSrc.value = `${baseUrl}?${params.toString()}`
})
</script>

<style scoped lang="scss">
.news-view {
  width: 100%;
  height: calc(100vh - 60px); /* 减去顶部导航栏高度 */
  padding: 20px;
  box-sizing: border-box;
}

.boardStyle {
  /* 适配原有设计风格 */
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  border-radius: 15px;
  display: flex;
  justify-content: center;
  overflow: hidden;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.18);
}
</style>
