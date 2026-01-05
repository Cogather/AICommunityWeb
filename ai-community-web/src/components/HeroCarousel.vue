<template>
    <div class="hero-carousel-wrapper">
      <el-carousel
        :interval="4000"
        type="card"
        height="420px"
        indicator-position="outside"
        :arrow="'always'"
        class="ai-carousel"
      >
        <el-carousel-item v-for="(item, index) in slides" :key="item.id" class="ai-carousel-item">
          
          <div class="slide-card">
            <img :src="item.image" :alt="item.title" class="slide-bg" />
            
            <div class="slide-mask"></div>
  
            <div class="glass-panel">
              <div class="panel-content">
                <h3 class="title">{{ item.title }}</h3>
                <p class="desc">{{ item.desc }}</p>
                
                <el-button class="gradient-btn" round>
                  立即体验
                  <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                </el-button>
              </div>
            </div>
          </div>
  
        </el-carousel-item>
      </el-carousel>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import { ArrowRight } from '@element-plus/icons-vue';
  
  interface Slide {
    id: number;
    title: string;
    desc: string;
    image: string;
    link: string;
  }
  
  // 模拟数据：建议使用 16:9 或更宽的图片
  const slides = ref<Slide[]>([
    {
      id: 1,
      title: "AI 优秀实践",
      desc: "探索大模型在企业级应用中的最佳落地场景，驱动业务数智化转型。",
      image: "https://picsum.photos/1200/600?random=1", 
      link: "/practice"
    },
    {
      id: 2,
      title: "智能算力引擎",
      desc: "基于云原生的弹性算力调度，为 AI Agent 提供源源不断的动力。",
      image: "https://picsum.photos/1200/600?random=2",
      link: "/compute"
    },
    {
      id: 3,
      title: "多模态创作",
      desc: "打破感官界限，融合视觉、听觉与文本，重塑数字内容生产流。",
      image: "https://picsum.photos/1200/600?random=3",
      link: "/create"
    },
    {
      id: 4,
      title: "未来社区",
      desc: "连接千万开发者，共享 Prompt 灵感，构建共生共荣的 AI 生态。",
      image: "https://picsum.photos/1200/600?random=4",
      link: "/community"
    }
  ]);
  </script>
  
  <style scoped lang="scss">
  /* 全屏宽度容器 
    overflow: hidden 防止某些浏览器出现横向滚动条
  */
  .hero-carousel-wrapper {
    width: 100%;
    max-width: 100%;
    position: relative;
    overflow: hidden; 
    padding: 20px 0 30px 0; /* 减少上边距，靠近导航栏，减少下边距，靠近下方内容 */
    margin: 0;
    box-sizing: border-box;
  }
  
  /* 覆盖 Element Plus 样式 */
  :deep(.el-carousel) {
    width: 100%;
    max-width: 100%;
  }
  
  :deep(.el-carousel__container) {
    /* 限制容器宽度，防止超出 */
    overflow: hidden !important; /* 强制隐藏溢出 */
    width: 100% !important;
    max-width: 100% !important;
    box-sizing: border-box !important;
    position: relative;
    /* 确保容器严格限制在父元素内 */
    left: 0;
    right: 0;
  }
  
  :deep(.el-carousel__track) {
    width: 100%;
    max-width: 100%;
    box-sizing: border-box;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  /* 核心卡片样式 - 一次显示三个图，合适的宽度 */
  :deep(.el-carousel__item) {
    border-radius: 24px;
    /* 默认状态：稍微透明，带一点模糊，制造景深感 */
    opacity: 0.6;
    filter: blur(2px) grayscale(30%);
    transition: all 0.6s cubic-bezier(0.25, 1, 0.5, 1); /* 平滑的贝塞尔曲线 */
    box-sizing: border-box !important;
    /* 调整卡片宽度，确保三张图能在一行显示 */
    width: 60% !important;
    max-width: 60% !important;
  }
  
  /* 激活卡片保持相同宽度，并确保居中 */
  :deep(.el-carousel__item.is-active) {
    width: 60% !important;
    max-width: 60% !important;
    /* 确保激活卡片居中 */
    margin-left: auto !important;
    margin-right: auto !important;
    left: 50% !important;
    transform: translateX(-50%) !important;
    z-index: 10;
  }
  
  /* 激活卡片 - 中间的大图 */
  /* Element Plus card 模式会自动处理激活状态，保持默认样式即可 */
  
  /* 卡片内部容器 */
  .slide-card {
    width: 100%;
    height: 100%;
    position: relative;
    border-radius: 24px;
    overflow: hidden;
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-sizing: border-box;
  }
  
  .slide-bg {
    width: 100%;
    height: 100%;
    object-fit: cover; /* 改为 cover 让图片填满容器，不留空白 */
    transition: transform 0.8s ease;
  }
  
  /* 遮罩：默认深色 */
  .slide-mask {
    position: absolute;
    inset: 0;
    background: rgba(12, 12, 20, 0.5); /* 深蓝黑色遮罩 */
    transition: all 0.6s;
  }
  
  /* -----------------------------------
     激活状态 (Active) - 中间的大图
  ----------------------------------- */
  :deep(.el-carousel__item.is-active) {
    opacity: 1 !important;
    filter: blur(0) grayscale(0) !important; /* 清除模糊 */
    z-index: 20;
    
    /* 移除 mask，让中间图完整显示 */
    mask-image: none !important; 
    -webkit-mask-image: none !important;
  
    /* 强烈的 3D 霓虹投影 */
    box-shadow: 
      0 0 0 1px rgba(255, 255, 255, 0.4), /* 高亮边框 */
      0 0 30px rgba(64, 158, 255, 0.5),   /* 蓝色光晕 */
      0 30px 60px rgba(0, 0, 0, 0.6);     /* 底部深投影 */
  
    /* 确保居中显示 */
    left: 50% !important;
    transform: translateX(-50%) !important;
  
    .slide-mask {
      background: transparent;
    }
  
    .slide-bg {
      transform: scale(1.03); /* 轻微放大 */
    }
  
    /* 显示信息面板 */
    .glass-panel {
      opacity: 1;
      transform: translateY(0);
    }
  }
  
  /* -----------------------------------
     非激活状态的特殊处理 (左右两侧)
     Element Plus 会给左边的加 is-animating (如果在动) 
     或者我们可以通过 CSS 兄弟选择器来微调，但这里统一用默认状态即可。
  ----------------------------------- */
  
  /* -----------------------------------
     毛玻璃信息面板
  ----------------------------------- */
  .glass-panel {
    position: absolute;
    bottom: 40px;
    left: 40px;
    right: 40px; 
    max-width: 460px;
    
    background: rgba(255, 255, 255, 0.08);
    backdrop-filter: blur(16px) saturate(180%);
    -webkit-backdrop-filter: blur(16px) saturate(180%);
    
    border-radius: 20px;
    border: 1px solid rgba(255, 255, 255, 0.3);
    padding: 30px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
    
    opacity: 0;
    transform: translateY(30px);
    transition: all 0.6s ease 0.2s; 
  }
  
  .title {
    margin: 0 0 10px 0;
    font-size: 28px;
    font-weight: 800;
    color: #fff;
    text-shadow: 0 2px 10px rgba(0,0,0,0.5);
  }
  
  .desc {
    margin: 0 0 24px 0;
    font-size: 15px;
    line-height: 1.6;
    color: rgba(255, 255, 255, 0.9);
  }
  
  /* 按钮样式 */
  .gradient-btn {
    background: linear-gradient(135deg, #00C6FF 0%, #0072FF 100%);
    border: none;
    font-size: 14px;
    font-weight: 600;
    padding: 12px 28px;
    height: auto;
    box-shadow: 0 4px 15px rgba(0, 114, 255, 0.4);
    transition: transform 0.2s;
    
    &:hover {
      transform: scale(1.05);
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }
  }
  
  /* 导航箭头样式 - 始终显示 */
  :deep(.el-carousel__arrow) {
    background: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.3);
    color: #fff;
    width: 48px;
    height: 48px;
    border-radius: 50%;
    opacity: 1 !important; /* 始终显示 */
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    z-index: 10;

    &:hover {
      background: rgba(255, 255, 255, 0.3);
      transform: scale(1.1);
      box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
    }
  }

  :deep(.el-carousel__arrow--left) {
    left: 20px;
  }

  :deep(.el-carousel__arrow--right) {
    right: 20px;
  }

  /* 指示器微调 */
  :deep(.el-carousel__indicators--outside) {
    margin-top: 20px;
  }
  :deep(.el-carousel__indicators--outside button) {
    background-color: rgba(255, 255, 255, 0.4);
    height: 4px;
  }
  :deep(.el-carousel__indicator.is-active button) {
    background-color: #00C6FF;
    width: 50px;
  }
  </style>