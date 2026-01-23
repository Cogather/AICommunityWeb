<template>
  <div class="hero-carousel-wrapper">
    <div class="coverflow-container">
      <!-- 自定义 Cover Flow 实现 -->
      <div class="coverflow-track">
        <div
          v-for="(item, index) in displaySlides"
          :key="item.id + '-' + index"
          class="coverflow-item"
          :class="getItemClass(index)"
          :style="getItemStyle(index)"
          @click="handleItemClick(index, item)"
        >
          <div class="slide-card">
            <img :src="item.image" :alt="item.title" class="slide-bg" />
            <div class="slide-mask"></div>

            <!-- 只在激活卡片显示内容 -->
            <div class="glass-panel" v-if="item.showContent && index === centerIndex">
              <div class="panel-content">
                <h3 class="title">{{ item.title }}</h3>
                <el-button
                  class="gradient-btn"
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
        </div>
      </div>

      <!-- 导航箭头 -->
      <button class="coverflow-arrow coverflow-arrow-left" @click="prev">
        <el-icon><ArrowLeft /></el-icon>
      </button>
      <button class="coverflow-arrow coverflow-arrow-right" @click="next">
        <el-icon><ArrowRight /></el-icon>
      </button>

      <!-- 指示器 -->
      <div class="coverflow-indicators">
        <span
          v-for="(_, index) in slides"
          :key="index"
          class="indicator-dot"
          :class="{ active: index === currentIndex }"
          @click="goTo(index)"
        ></span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowRight, ArrowLeft } from '@element-plus/icons-vue';
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
const currentIndex = ref(0);
const isTransitioning = ref(false);

// 自动播放
let autoPlayTimer: ReturnType<typeof setInterval> | null = null;
const autoPlayInterval = 4000;

// 计算显示的 5 张卡片（循环）
const displaySlides = computed(() => {
  const total = slides.value.length;
  if (total === 0) return [];

  const result: Slide[] = [];
  // 显示 5 张：当前索引前 2 张 + 当前 + 后 2 张
  for (let i = -2; i <= 2; i++) {
    const index = (currentIndex.value + i + total) % total;
    result.push(slides.value[index]);
  }
  return result;
});

// 中心卡片在 displaySlides 中的索引
const centerIndex = 2;

// 获取卡片的 CSS 类
const getItemClass = (index: number) => {
  const offset = index - centerIndex; // -2, -1, 0, 1, 2
  return {
    'is-active': offset === 0,
    'is-near-left': offset === -1,
    'is-near-right': offset === 1,
    'is-far-left': offset === -2,
    'is-far-right': offset === 2,
  };
};

// 获取卡片的内联样式
const getItemStyle = (index: number) => {
  const offset = index - centerIndex; // -2, -1, 0, 1, 2

  // 紧密堆叠配置 - 像扇子一样展开，延伸到屏幕边缘
  const config = {
    // 缩放比例：中间最大，两侧依次缩小
    scale: [0.5, 0.7, 1, 0.7, 0.5],
    // X 轴偏移（像素值）- 增大数值让卡片更靠近屏幕边缘
    translateX: [-620, -320, 0, 320, 620],
    // Y 轴旋转角度：两侧向内倾斜
    rotateY: [50, 30, 0, -30, -50],
    // translateZ：制造前后层次感
    translateZ: [-200, -80, 0, -80, -200],
  };

  const i = offset + 2; // 转换为 0-4 索引

  // 注意：z-index 通过 CSS 类控制，不在这里设置，避免内联样式覆盖
  return {
    transform: `
      translateX(${config.translateX[i]}px)
      translateZ(${config.translateZ[i]}px)
      scale(${config.scale[i]})
      rotateY(${config.rotateY[i]}deg)
    `,
  };
};

// 导航方法
const next = () => {
  if (isTransitioning.value) return;
  isTransitioning.value = true;
  currentIndex.value = (currentIndex.value + 1) % slides.value.length;
  setTimeout(() => { isTransitioning.value = false; }, 500);
  resetAutoPlay();
};

const prev = () => {
  if (isTransitioning.value) return;
  isTransitioning.value = true;
  currentIndex.value = (currentIndex.value - 1 + slides.value.length) % slides.value.length;
  setTimeout(() => { isTransitioning.value = false; }, 500);
  resetAutoPlay();
};

const goTo = (index: number) => {
  if (isTransitioning.value || index === currentIndex.value) return;
  isTransitioning.value = true;
  currentIndex.value = index;
  setTimeout(() => { isTransitioning.value = false; }, 500);
  resetAutoPlay();
};

// 点击卡片
const handleItemClick = (index: number, item: Slide) => {
  const offset = index - centerIndex;
  if (offset === 0) {
    // 点击中间的卡片，跳转
    handleSlideClick(item);
  } else if (offset < 0) {
    // 点击左侧，向前
    prev();
  } else {
    // 点击右侧，向后
    next();
  }
};

// 处理轮播图点击跳转
const handleSlideClick = (slide: Slide) => {
  if (slide.link) {
    if (slide.link.startsWith('http')) {
      window.open(slide.link, '_blank');
    } else {
      router.push(slide.link);
    }
  }
};

// 自动播放控制
const startAutoPlay = () => {
  stopAutoPlay();
  autoPlayTimer = setInterval(next, autoPlayInterval);
};

const stopAutoPlay = () => {
  if (autoPlayTimer) {
    clearInterval(autoPlayTimer);
    autoPlayTimer = null;
  }
};

const resetAutoPlay = () => {
  startAutoPlay();
};

// 从 API 加载轮播图配置
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
    slides.value = defaultSlides;
  }
};

// 监听配置更新
const handleConfigUpdate = () => {
  loadCarouselSlides();
};

onMounted(() => {
  loadCarouselSlides();
  startAutoPlay();
  window.addEventListener('adminConfigUpdated', handleConfigUpdate);
});

onUnmounted(() => {
  stopAutoPlay();
  window.removeEventListener('adminConfigUpdated', handleConfigUpdate);
});
</script>

<style scoped lang="scss">
/* ===================================
   Cover Flow 轮播图 - 紧凑层叠效果
   =================================== */

.hero-carousel-wrapper {
  width: 100%;
  position: relative;
  overflow: hidden;
  /* 无间距，紧贴上下 */
  padding: 0;
  margin: 0;
  box-sizing: border-box;

  /* 透明背景 - 直接铺在页面上 */
  background: transparent;
}

.coverflow-container {
  position: relative;
  width: 100%;
  /* 全屏宽度，不限制 max-width */
  max-width: none;
  /* 容器高度等于卡片高度，消除上下间距 */
  height: 380px;
  margin: 0 auto;
  perspective: 1800px;
  perspective-origin: 50% 50%;
}

/* ===================================
   轨道与卡片
   =================================== */

.coverflow-track {
  position: relative;
  width: 100%;
  height: 100%;
  transform-style: preserve-3d;
}

.coverflow-item {
  position: absolute;
  left: 50%;
  top: 0;
  width: 650px;
  height: 380px;
  margin-left: -325px; /* 宽度的一半，确保居中 */
  margin-top: 0;

  /* 平滑过渡 */
  transition: all 0.6s cubic-bezier(0.34, 1.56, 0.64, 1);
  transform-style: preserve-3d;
  cursor: pointer;

  /* 默认最低层级 */
  z-index: 1 !important;

  will-change: transform, filter, z-index;
}

/* ===================================
   强制 Z-Index 层级控制 (关键修复)
   =================================== */

/* 最外侧卡片 - 最低层级 */
.coverflow-item.is-far-left,
.coverflow-item.is-far-right {
  z-index: 10 !important;
}

/* 紧邻两侧卡片 - 中等层级 */
.coverflow-item.is-near-left,
.coverflow-item.is-near-right {
  z-index: 50 !important;
}

/* 激活卡片 - 最高层级，永远在最顶层 */
.coverflow-item.is-active {
  z-index: 200 !important;
}

/* ===================================
   卡片内容
   =================================== */

.slide-card {
  width: 100%;
  height: 100%;
  position: relative;
  border-radius: 20px;
  overflow: hidden;
  box-shadow:
    0 25px 50px -12px rgba(0, 0, 0, 0.4),
    0 0 0 1px rgba(255, 255, 255, 0.15);
  transform-style: preserve-3d;
  backface-visibility: hidden;
}

.slide-bg {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

/* 遮罩层 - 非激活状态加深 */
.slide-mask {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  transition: all 0.5s ease;
  pointer-events: none;
}

/* ===================================
   激活状态 (中间卡片) - 最突出
   =================================== */

.coverflow-item.is-active {
  cursor: default;

  .slide-card {
    box-shadow:
      0 40px 80px -20px rgba(0, 0, 0, 0.5),
      0 0 0 2px rgba(255, 255, 255, 0.25),
      0 0 60px rgba(59, 130, 246, 0.2);
  }

  .slide-bg {
    transform: scale(1.02);
  }

  /* 激活卡片几乎无遮罩，最亮 */
  .slide-mask {
    background: linear-gradient(
      180deg,
      transparent 50%,
      rgba(0, 0, 0, 0.3) 100%
    );
  }

  .glass-panel {
    opacity: 1;
    transform: translateY(0);
  }
}

/* ===================================
   紧邻两侧卡片 - 中等暗度
   =================================== */

.coverflow-item.is-near-left,
.coverflow-item.is-near-right {
  .slide-card {
    box-shadow:
      0 20px 40px -10px rgba(0, 0, 0, 0.35),
      0 0 0 1px rgba(255, 255, 255, 0.1);
  }

  .slide-mask {
    background: rgba(0, 0, 0, 0.45);
  }
}

/* ===================================
   最外侧卡片 - 最暗
   =================================== */

.coverflow-item.is-far-left,
.coverflow-item.is-far-right {
  .slide-card {
    box-shadow:
      0 15px 30px -8px rgba(0, 0, 0, 0.25),
      0 0 0 1px rgba(255, 255, 255, 0.05);
  }

  .slide-mask {
    background: rgba(0, 0, 0, 0.6);
  }
}

/* ===================================
   毛玻璃信息面板
   =================================== */

.glass-panel {
  position: absolute;
  bottom: 28px;
  left: 28px;
  right: auto;
  max-width: 400px;

  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);

  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.25);
  padding: 22px 28px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);

  opacity: 0;
  transform: translateY(16px);
  transition: all 0.5s ease 0.15s;
}

.title {
  margin: 0 0 14px 0;
  font-size: 24px;
  font-weight: 700;
  color: #fff;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
  letter-spacing: 0.5px;
}

/* 渐变按钮 */
.gradient-btn {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border: none;
  font-size: 14px;
  font-weight: 600;
  padding: 10px 22px;
  height: auto;
  box-shadow: 0 4px 14px rgba(59, 130, 246, 0.4);
  transition: all 0.3s ease;
  color: #fff !important;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(59, 130, 246, 0.5);
    background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 100%);
  }

  :deep(.el-icon) {
    color: #fff !important;
  }
}

/* ===================================
   导航箭头
   =================================== */

.coverflow-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 300; /* 高于所有卡片 */

  width: 56px;
  height: 56px;
  border-radius: 50%;
  border: none;

  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.25);

  color: #fff;
  font-size: 22px;
  cursor: pointer;

  display: flex;
  align-items: center;
  justify-content: center;

  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);

  &:hover {
    background: rgba(255, 255, 255, 0.25);
    transform: translateY(-50%) scale(1.1);
    box-shadow: 0 6px 28px rgba(0, 0, 0, 0.25);
  }

  &:active {
    transform: translateY(-50%) scale(0.95);
  }
}

.coverflow-arrow-left {
  left: 20px;
}

.coverflow-arrow-right {
  right: 20px;
}

/* ===================================
   指示器
   =================================== */

.coverflow-indicators {
  position: absolute;
  /* 放在激活卡片的底部内侧 */
  bottom: 15px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
  z-index: 300;
  /* 半透明背景 */
  background: rgba(0, 0, 0, 0.3);
  padding: 6px 12px;
  border-radius: 20px;
  backdrop-filter: blur(8px);
}

.indicator-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    background: rgba(255, 255, 255, 0.8);
  }

  &.active {
    background: #fff;
    width: 18px;
    border-radius: 3px;
  }
}

/* ===================================
   响应式适配
   =================================== */

@media (max-width: 1600px) {
  .coverflow-container {
    height: 340px;
  }

  .coverflow-item {
    width: 580px;
    height: 340px;
    margin-left: -290px;
    margin-top: 0;
  }
}

@media (max-width: 1400px) {
  .coverflow-container {
    height: 310px;
  }

  .coverflow-item {
    width: 520px;
    height: 310px;
    margin-left: -260px;
    margin-top: 0;
  }
}

@media (max-width: 1200px) {
  .coverflow-container {
    height: 276px;
  }

  .coverflow-item {
    width: 460px;
    height: 276px;
    margin-left: -230px;
    margin-top: 0;
  }

  .coverflow-arrow-left {
    left: 16px;
  }

  .coverflow-arrow-right {
    right: 16px;
  }

  .coverflow-indicators {
    bottom: 12px;
  }
}

@media (max-width: 900px) {
  .coverflow-container {
    height: 228px;
  }

  .coverflow-item {
    width: 380px;
    height: 228px;
    margin-left: -190px;
    margin-top: 0;
  }

  .glass-panel {
    padding: 16px 20px;
    max-width: 300px;
    bottom: 20px;
    left: 20px;
  }

  .title {
    font-size: 18px;
    margin-bottom: 10px;
  }

  .coverflow-arrow {
    width: 44px;
    height: 44px;
    font-size: 18px;
  }

  .coverflow-indicators {
    bottom: 10px;
  }

  /* 隐藏最外侧卡片 */
  .coverflow-item.is-far-left,
  .coverflow-item.is-far-right {
    opacity: 0;
    pointer-events: none;
  }
}

@media (max-width: 600px) {
  .hero-carousel-wrapper {
    padding: 0;
  }

  .coverflow-container {
    height: 180px;
  }

  .coverflow-item {
    width: 300px;
    height: 180px;
    margin-left: -150px;
    margin-top: 0;
  }

  .coverflow-arrow {
    width: 38px;
    height: 38px;
    font-size: 16px;
  }

  .coverflow-arrow-left {
    left: 8px;
  }

  .coverflow-arrow-right {
    right: 8px;
  }

  .glass-panel {
    bottom: 10px;
    left: 10px;
    padding: 10px 14px;
    max-width: 200px;
  }

  .title {
    font-size: 14px;
    margin-bottom: 8px;
  }

  .gradient-btn {
    font-size: 12px;
    padding: 6px 14px;
  }

  .coverflow-indicators {
    bottom: 8px;
    padding: 4px 10px;
    gap: 6px;
  }

  .indicator-dot {
    width: 5px;
    height: 5px;

    &.active {
      width: 14px;
    }
  }
}
</style>
