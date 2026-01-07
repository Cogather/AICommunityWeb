<template>
  <div class="docs">
    <el-card>
      <template #header>
        <span>文档中心</span>
      </template>
      <el-timeline>
        <el-timeline-item timestamp="2024-01-01" placement="top">
          <el-card>
            <h4>项目初始化</h4>
            <p>使用 Vite + Vue 3 + TypeScript 创建项目</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="2024-01-02" placement="top">
          <el-card>
            <h4>集成 Element Plus</h4>
            <p>配置 Element Plus UI 组件库</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="2024-01-03" placement="top">
          <el-card>
            <h4>路由配置</h4>
            <p>使用 Vue Router 配置页面路由</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup lang="ts">
// 文档页面
</script>

<style scoped>
.docs {
  max-width: 800px;
  margin: 0 auto;
}
</style>






