<template>
    <div class="hero-carousel-wrapper">
      <div class="hero-carousel-frame">
        <el-carousel
          :interval="4500"
          type="card"
          height="400px"
          indicator-position="inside"
          :arrow="'always'"
          class="ai-carousel"
        >
          <el-carousel-item v-for="item in slides" :key="item.id" class="ai-carousel-item">

            <div class="slide-card" @click="handleSlideClick(item)" :style="{ cursor: item.link ? 'pointer' : 'default' }">
              <img :src="item.image" :alt="item.title" class="slide-bg" />

              <div class="slide-mask"></div>

              <div class="glass-panel" v-if="item.showContent">
                <div class="panel-content">
                  <h3 class="title">{{ item.title }}</h3>
                  <p class="desc">{{ item.desc }}</p>

                  <el-button
                    class="tech-ghost-btn"
                    round
                    v-if="item.link"
                    @click.stop="handleSlideClick(item)"
                  >
                    立即前往
                    <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                  </el-button>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

    </div>
  </template>

  <script setup lang="ts">
  import { ref, onMounted, onUnmounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { ArrowRight } from '@element-plus/icons-vue';
  // API 层 - 支持 Mock/Real API 自动切换
import { getCarousel } from '@/api/home';

  interface Slide {
    id: number;
    title: string;
    desc: string;
    image: string;
    link: string;
    showContent: boolean;
  }

  const router = useRouter();

  // 默认轮播图数据
  const defaultSlides: Slide[] = [
    {
      id: 1,
      title: "AI 优秀实践",
      desc: "探索大模型在企业级应用中的最佳落地场景，驱动业务数智化转型。",
      image: "https://picsum.photos/1200/600?random=1",
      link: "/practice",
      showContent: true
    },
    {
      id: 2,
      title: "智能算力引擎",
      desc: "基于云原生的弹性算力调度，为 AI Agent 提供源源不断的动力。",
      image: "https://picsum.photos/1200/600?random=2",
      link: "/compute",
      showContent: true
    },
    {
      id: 3,
      title: "多模态创作",
      desc: "打破感官界限，融合视觉、听觉与文本，重塑数字内容生产流。",
      image: "https://picsum.photos/1200/600?random=3",
      link: "/create",
      showContent: true
    },
    {
      id: 4,
      title: "未来社区",
      desc: "连接千万开发者，共享 Prompt 灵感，构建共生共荣的 AI 生态。",
      image: "https://picsum.photos/1200/600?random=4",
      link: "/community",
      showContent: true
    }
  ];

  const slides = ref<Slide[]>(defaultSlides);

  // 从API加载轮播图配置
  const loadCarouselSlides = async () => {
    try {
      const response = await getCarousel();
      if (response.data && response.data.list && response.data.list.length > 0) {
        slides.value = response.data.list.map((item: { id: number; title?: string; desc?: string; image?: string; link?: string; showContent?: boolean }) => ({
          id: item.id,
          title: item.title || '',
          desc: item.desc || '',
          image: item.image || '',
          link: item.link || '/',
          showContent: item.showContent !== false
        })).filter((item: Slide) => item.image);
      }
    } catch (e) {
      console.error('加载轮播图配置失败:', e);
      // 使用默认数据
      slides.value = defaultSlides;
    }
  };

  // 监听配置更新
  const handleConfigUpdate = () => {
    loadCarouselSlides();
  };

  // 处理轮播图点击
  const handleSlideClick = (slide: Slide) => {
    if (slide.link) {
      if (slide.link.startsWith('http')) {
        window.open(slide.link, '_blank');
      } else {
        router.push(slide.link);
      }
    }
  };

  onMounted(() => {
    loadCarouselSlides();
    window.addEventListener('adminConfigUpdated', handleConfigUpdate);
  });

  onUnmounted(() => {
    window.removeEventListener('adminConfigUpdated', handleConfigUpdate);
  });
  </script>

  <style scoped lang="scss">
  /* 全屏宽度容器
    overflow: hidden 防止某些浏览器出现横向滚动条
  */
  .hero-carousel-wrapper {
    width: 100%;
    max-width: 100%;
    position: relative;
    padding: 0; /* 去掉所有padding，与导航栏无缝连接 */
    margin: 0;
    margin-bottom: -24px; /* 减少突兀的视觉占比 */
    box-sizing: border-box;
    z-index: 1; /* 确保在背景上层，但在内容下层 */
  }

  /* 轮播图外层容器：移除边框，只保留基础样式 */
  .hero-carousel-frame {
    width: 100%;
    margin: 0 auto;
    border-radius: 20px;
    background: rgba(255, 255, 255, 0.03);
    overflow: hidden; /* 保证圆角裁切 */
  }

  @media (max-width: 1024px) {
    .hero-carousel-frame {
      width: 92%;
    }
  }

  /* 为轮播图容器 */
  :deep(.el-carousel__container) {
    position: relative;
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
    border-radius: 20px;
    /* 默认状态：稍微透明，带一点模糊，制造景深感 */
    opacity: 0.8;
    filter: blur(0.5px) grayscale(20%);
    transition: all 0.6s cubic-bezier(0.25, 1, 0.5, 1); /* 平滑的贝塞尔曲线 */
    box-sizing: border-box !important;
    /* 调整卡片宽度，确保三张图能在一行显示 */
    width: 55% !important;
    max-width: 55% !important;
  }

  /* 激活卡片保持相同宽度，并确保居中 */
  :deep(.el-carousel__item.is-active) {
    width: 55% !important;
    max-width: 55% !important;
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
    border-radius: 20px;
    overflow: hidden;
    border: 1px solid rgba(255, 255, 255, 0.10);
    box-sizing: border-box;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1); /* 平滑过渡 */
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
    background: rgba(10, 14, 23, 0.52); /* 深蓝黑遮罩 */
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

    /* 确保居中显示 */
    left: 50% !important;
    transform: translateX(-50%) !important;

    .slide-mask {
      background: transparent;
    }

    .slide-bg {
      transform: scale(1.03); /* 轻微放大 */
    }

    /* 激活卡片的边框与阴影：沉稳科技感 */
    .slide-card {
      /* 边框：半透明科技蓝，1px，完美贴合圆角 */
      border: 1px solid rgba(0, 102, 255, 0.2);
      /* 玻璃质感：边框边缘模糊处理 */
      backdrop-filter: blur(4px);
      -webkit-backdrop-filter: blur(4px);
      /* 浅蓝色复合阴影：移除黑色投影，改用柔和扩散阴影 + 紧贴边框的蓝色微光 */
      box-shadow:
        0 20px 50px rgba(0, 102, 255, 0.05), /* 柔和的扩散阴影 */
        0 0 10px rgba(0, 102, 255, 0.1); /* 紧贴边框的蓝色微光 */

      /* Hover 增强效果：边框变电光蓝，增加发光强度 */
      &:hover {
        border-color: rgba(0, 102, 255, 0.8); /* 电光蓝，透明度提升到 0.8 */
        /* 增加边框外侧发光强度，营造'唤醒'交互反馈 */
        box-shadow:
          0 20px 50px rgba(0, 102, 255, 0.08), /* 扩散阴影增强 */
          0 0 20px rgba(0, 102, 255, 0.25), /* 紧贴边框的蓝色微光增强 */
          0 0 30px rgba(0, 102, 255, 0.15); /* 额外的外层光晕 */
      }
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
    bottom: 30px; /* 稍微上移 */
    left: 30px;   /* 稍微右移 */
    right: auto;
    max-width: 420px; /* 宽度增加 */

    background: rgba(255, 255, 255, 0.05);
    backdrop-filter: blur(16px) saturate(180%);
    -webkit-backdrop-filter: blur(16px) saturate(180%);

    border-radius: 16px; /* 圆角恢复适中 */
    border: 1px solid rgba(255, 255, 255, 0.14);
    padding: 24px; /* 内边距增加 */
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);

    opacity: 0;
    transform: translateY(20px);
    transition: all 0.6s ease 0.2s;
  }

  .title {
    margin: 0 0 8px 0;
    font-size: 24px; /* 字体增大 */
    font-weight: 700;
    color: #fff;
    text-shadow: 0 2px 8px rgba(0,0,0,0.6);
  }

  .desc {
    margin: 0 0 16px 0;
    font-size: 14px; /* 字体增大 */
    line-height: 1.6;
    color: rgba(255, 255, 255, 0.72);
    display: -webkit-box;
    -webkit-line-clamp: 3; /* 允许显示更多行 */
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  /* 导航箭头样式 - 始终显示 */
  :deep(.el-carousel__arrow) {
    /* 白色背景，与整体风格保持一致 */
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(12px);
    -webkit-backdrop-filter: blur(12px);
    /* 白色边框带点蓝色点缀 */
    border: 1.5px solid rgba(255, 255, 255, 0.9);
    /* 蓝色点缀：内边框 */
    box-shadow:
      0 0 0 1px rgba(0, 102, 255, 0.15) inset,
      0 4px 12px rgba(0, 102, 255, 0.08);
    color: #0066FF; /* 箭头颜色保持科技蓝 */
    width: 48px;
    height: 48px;
    border-radius: 50%;
    opacity: 1 !important; /* 始终显示 */
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    z-index: 10;

    &:hover {
      background: rgba(255, 255, 255, 1);
      border-color: rgba(255, 255, 255, 1);
      /* 悬停时增强蓝色点缀 */
      box-shadow:
        0 0 0 1px rgba(0, 102, 255, 0.25) inset,
        0 6px 20px rgba(0, 102, 255, 0.15),
        0 0 0 1px rgba(0, 102, 255, 0.2);
      transform: scale(1.08) translateY(-2px);
      color: #0066FF;
    }
  }

  :deep(.el-carousel__arrow--left) {
    left: 20px;
  }

  :deep(.el-carousel__arrow--right) {
    right: 20px;
  }

  /* 指示器微调 - 放在图片上 */
  :deep(.el-carousel__indicators) {
    bottom: 20px !important;
    left: 50%;
    transform: translateX(-50%);

    .el-carousel__indicator {
      margin: 0 4px;

      button {
        background-color: rgba(255, 255, 255, 0.38);
        width: 8px;
        height: 8px;
        border-radius: 50%;
        transition: all 0.3s ease;
        border: none;
      }

      &.is-active button {
        background-color: rgba(0, 242, 255, 0.95);
        width: 24px;
        border-radius: 4px;
      }
    }
  }
  </style>
