<template>
  <footer class="footer-banner">
    <!--
      【背景图片替换位置】
      请在下方 .footer-banner 的 CSS 中修改 background-image 属性
      例如：background-image: url('@/assets/image/footer_bg.png');
    -->
    <div class="footer-content">
      <div class="contact-box">
        <!-- 二维码区域 - 可替换为真实二维码图片 -->
        <div class="qr-placeholder">
          <img
            src="https://picsum.photos/60/60?random=qr"
            alt="二维码"
            class="qr-image"
          />
        </div>
        <span>扫码加入 AI 开发者社区</span>
      </div>
      <div class="friend-links">
        <span>友情链接：</span>
        <el-button
          v-for="link in friendLinks"
          :key="link.id"
          round
          size="small"
          @click="handleLinkClick(link.url)"
        >
          {{ link.name }}
        </el-button>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
// Footer component with background image

// 友情链接配置
const friendLinks = [
  { id: 1, name: 'CMG-CoGather', url: 'https://cogather.coreai.rnd.huawei.com' },
  { id: 2, name: '华为云', url: 'https://www.huaweicloud.com' },
  { id: 3, name: '昇腾社区', url: 'https://www.hiascend.com' }
]

const handleLinkClick = (url: string) => {
  if (url) {
    window.open(url, '_blank')
  }
}
</script>

<style scoped lang="scss">
/* Footer - 背景图片模式 */
.footer-banner {
  margin-top: 60px;
  position: relative;
  min-height: 150px;
  overflow: hidden;

  /*
    【背景图片配置】
    请将下面的 URL 替换为您自己的图片路径
    例如：background-image: url('@/assets/image/footer_bg.png');
  */
  background-image: url('https://picsum.photos/1920/300?blur=2');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

/* 半透明遮罩层 - 确保文字清晰可读 */
.footer-banner::before {
  content: '';
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.45); /* 黑色半透明遮罩，可调整透明度 */
  z-index: 0;
  pointer-events: none;
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
    color: rgba(255, 255, 255, 0.95);
    font-size: 14px;
    font-weight: 500;
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  }

  .qr-placeholder {
    width: 64px;
    height: 64px;
    background: rgba(255, 255, 255, 0.15);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.3);
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 8px;
    overflow: hidden;

    .qr-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
}

.friend-links {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;

  span {
    font-size: 13px;
    color: rgba(255, 255, 255, 0.85);
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  }

  :deep(.el-button) {
    background: rgba(255, 255, 255, 0.15);
    border: 1px solid rgba(255, 255, 255, 0.3);
    color: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(8px);
    transition: all 0.3s ease;

    &:hover {
      background: rgba(255, 255, 255, 0.25);
      border-color: rgba(255, 255, 255, 0.5);
      color: #fff;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    }
  }
}

/* 响应式布局 */
@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }

  .contact-box {
    flex-direction: column;
  }

  .friend-links {
    justify-content: center;
  }
}
</style>
