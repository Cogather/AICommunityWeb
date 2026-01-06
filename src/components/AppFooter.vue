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
/* Footer - 按照描述复刻背景样式 */
.footer-banner {
  margin-top: 60px;
  border-radius: 0;
  border-left: none;
  border-right: none;
  position: relative;
  overflow: hidden;
  margin-bottom: 0;
  min-height: 133px; /* 建议高度 */
  
  /* 主渐变背景 - 上略亮、下更暗的深海军蓝 */
  background: linear-gradient(180deg,
    #131825 0%,
    #161C29 40%,
    #080E1C 100%);
  
  /* 动态效果：背景轻微呼吸 */
  animation: backgroundBreath 12s ease-in-out infinite;
}

/* 暗角 + 顶部轻微柔光（让画面更"有层次"） */
.footer-banner::before {
  content: "";
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 0;

  background:
    /* 顶部轻柔冷光（非常淡） */
    radial-gradient(140% 120% at 50% 0%,
      rgba(249, 249, 249, 0.06) 0%,
      rgba(249, 249, 249, 0.00) 55%),
    /* 左右暗角 */
    radial-gradient(120% 160% at 0% 55%,
      rgba(6, 9, 16, 0.70) 0%,
      rgba(6, 9, 16, 0.00) 60%),
    radial-gradient(120% 160% at 100% 55%,
      rgba(7, 10, 17, 0.70) 0%,
      rgba(7, 10, 17, 0.00) 60%),
    /* 底部更暗（加深下沿） */
    radial-gradient(140% 140% at 50% 110%,
      rgba(8, 14, 28, 0.85) 0%,
      rgba(8, 14, 28, 0.00) 60%);

  mix-blend-mode: normal;
  
  /* 动态效果：暗角轻微呼吸 */
  animation: vignetteBreath 8s ease-in-out infinite;
}

/* 右侧散景光带（冷灰为主 + 少量暖棕），整体要"虚、软、克制" */
.footer-banner::after {
  content: "";
  position: absolute;
  inset: -20% -10%; /* 放大一点，避免旋转后露边 */
  pointer-events: none;
  z-index: 0;

  background:
    /* ——右侧冷灰光带（主要）—— */
    radial-gradient(180px 46px at 75% 38%,
      rgba(107, 110, 120, 0.55) 0%,
      rgba(8, 14, 28, 0.00) 72%),
    radial-gradient(190px 50px at 87% 28%,
      rgba(112, 115, 125, 0.50) 0%,
      rgba(8, 14, 28, 0.00) 74%),
    radial-gradient(170px 44px at 98% 22%,
      rgba(127, 130, 138, 0.45) 0%,
      rgba(8, 14, 28, 0.00) 75%),

    /* ——暖棕反光（很少）—— */
    radial-gradient(160px 42px at 73% 20%,
      rgba(119, 106, 90, 0.40) 0%,
      rgba(8, 14, 28, 0.00) 74%),
    radial-gradient(140px 38px at 80% 16%,
      rgba(109, 94, 80, 0.35) 0%,
      rgba(8, 14, 28, 0.00) 74%),

    /* ——下方模糊散景（图里底部也有些虚影）—— */
    radial-gradient(240px 72px at 62% 92%,
      rgba(79, 91, 102, 0.30) 0%,
      rgba(8, 14, 28, 0.00) 78%),

    /* ——极少量镜面高光点（必须非常淡）—— */
    radial-gradient(90px 28px at 92% 14%,
      rgba(249, 249, 249, 0.12) 0%,
      rgba(8, 14, 28, 0.00) 70%);

  /* 关键：虚化 + 轻微旋转，让它变成"斜向光带散景" */
  filter: blur(12px);
  transform: rotate(12deg) scale(1.05);
  opacity: 0.90;

  /* 关键：用 screen/plus-lighter 更像摄影散景叠加 */
  mix-blend-mode: screen;
  
  /* 动态效果：光带缓慢移动和呼吸 */
  animation: bokehFloat 15s ease-in-out infinite;
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

/* 动态效果动画 */
/* 背景呼吸效果 - 轻微亮度变化 */
@keyframes backgroundBreath {
  0%, 100% {
    filter: brightness(1);
  }
  50% {
    filter: brightness(1.03);
  }
}

/* 暗角呼吸效果 - 轻微变化 */
@keyframes vignetteBreath {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.95;
    transform: scale(1.02);
  }
}

/* 散景光带浮动效果 - 缓慢移动和透明度变化 */
@keyframes bokehFloat {
  0%, 100% {
    transform: rotate(12deg) scale(1.05) translate(0, 0);
    opacity: 0.90;
  }
  25% {
    transform: rotate(13deg) scale(1.06) translate(5px, -8px);
    opacity: 0.85;
  }
  50% {
    transform: rotate(11deg) scale(1.04) translate(-3px, 5px);
    opacity: 0.92;
  }
  75% {
    transform: rotate(12.5deg) scale(1.055) translate(3px, -3px);
    opacity: 0.88;
  }
}

</style>

