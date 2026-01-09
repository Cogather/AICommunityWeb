<template>
  <div class="honor-view">
    <div class="iframe-container">
      <iframe
        src=""
        frameborder="0"
        class="honor-iframe"
        title="荣誉殿堂"
      ></iframe>
    </div>
  </div>
</template>

<script setup lang="ts">
// 荣誉殿堂页面 - iframe嵌套
</script>

<style scoped lang="scss">
.honor-view {
  width: 100%;
  height: calc(100vh - 120px); // 减去导航栏和footer的高度
  min-height: 600px;
  padding: 20px;
  max-width: 1600px;
  margin: 0 auto;
}

.iframe-container {
  width: 100%;
  height: 100%;
  border-radius: 16px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.honor-iframe {
  width: 100%;
  height: 100%;
  border: none;
  display: block;
}

@media (max-width: 768px) {
  .honor-view {
    padding: 10px;
    height: calc(100vh - 100px);
  }
}
</style>
