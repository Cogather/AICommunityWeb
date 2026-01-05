<template>
  <footer class="footer-banner glass-card">
    <div class="footer-content">
      <div class="contact-box">
        <div class="qr-placeholder">二维码</div>
        <span>扫码加入 AI 开发者社区</span>
      </div>
      <div class="friend-links">
        <span>友情链接：</span>
        <el-button round size="small" v-for="i in 3" :key="i">云核心产品 {{ i }}</el-button>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
// Footer component
</script>

<style scoped lang="scss">
/* Footer */
.footer-banner {
  margin-top: 60px;
  border-radius: 0;
  border-left: none;
  border-right: none;
  position: relative;
  overflow: hidden;
  margin-bottom: 0;
  
  /* 深色背景 */
  background: #0a0e27;
  
  /* 动态光斑效果 - 左侧圆形光斑 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 30%;
    height: 100%;
    background: 
      radial-gradient(circle at 5% 30%, rgba(64, 158, 255, 0.15) 0%, transparent 30%),
      radial-gradient(circle at 8% 70%, rgba(100, 181, 246, 0.12) 0%, transparent 25%);
    animation: lightMoveLeft 18s ease-in-out infinite;
    pointer-events: none;
    z-index: 0;
  }
  
  /* 动态光斑效果 - 右侧圆形光斑 */
  &::after {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 30%;
    height: 100%;
    background: 
      radial-gradient(circle at 95% 40%, rgba(255, 159, 64, 0.15) 0%, transparent 30%),
      radial-gradient(circle at 92% 80%, rgba(255, 200, 87, 0.12) 0%, transparent 25%);
    animation: lightMoveRight 20s ease-in-out infinite;
    pointer-events: none;
    z-index: 0;
  }

  .footer-content {
    max-width: 1280px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 40px 20px;
    position: relative;
    z-index: 1;
  }

  .contact-box {
    display: flex;
    align-items: center;
    gap: 15px;
    color: #fff;
    
    span {
      color: rgba(255, 255, 255, 0.9);
      font-size: 14px;
      font-weight: 500;
    }
    
    .qr-placeholder {
      width: 60px;
      height: 60px;
      background: rgba(255, 255, 255, 0.1);
      backdrop-filter: blur(10px);
      border: 1px solid rgba(255, 255, 255, 0.2);
      color: rgba(255, 255, 255, 0.8);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 10px;
      border-radius: 8px;
    }
  }

  .friend-links {
    display: flex;
    align-items: center;
    gap: 10px;
    
    span {
      font-size: 12px;
      color: rgba(255, 255, 255, 0.7);
    }
    
    :deep(.el-button) {
      background: rgba(255, 255, 255, 0.1);
      border: 1px solid rgba(255, 255, 255, 0.2);
      color: rgba(255, 255, 255, 0.9);
      
      &:hover {
        background: rgba(255, 255, 255, 0.2);
        border-color: rgba(255, 255, 255, 0.3);
        color: #fff;
      }
    }
  }
}

/* 光斑移动动画 - 左右两侧上下移动 */
@keyframes lightMoveLeft {
  0% {
    transform: translateY(0);
    opacity: 0.8;
  }
  50% {
    transform: translateY(-30px);
    opacity: 1;
  }
  100% {
    transform: translateY(0);
    opacity: 0.8;
  }
}

@keyframes lightMoveRight {
  0% {
    transform: translateY(0);
    opacity: 0.8;
  }
  50% {
    transform: translateY(30px);
    opacity: 1;
  }
  100% {
    transform: translateY(0);
    opacity: 0.8;
  }
}
</style>

