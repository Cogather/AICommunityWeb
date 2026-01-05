<template>
  <div class="page-container">
    <div class="global-background">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
    </div>

    <section class="hero-section">
      <HeroCarousel />
    </section>

    <main class="main-content">
      <el-row :gutter="24" class="section-row">
        <el-col :xs="24" :md="16">
          <div class="glass-card honor-section">
            <div class="honor-split-container">
              <!-- 左边：荣誉殿堂 -->
              <div class="honor-hall">
                <div class="hall-banner">
                  <div class="banner-icon">🏆</div>
                  <div class="banner-text">
                    <h4>荣誉殿堂</h4>
                    <p>Hall of Fame</p>
                  </div>
                </div>
                <div class="awards-grid">
                  <div class="award-item" v-for="i in 3" :key="i">
                    <div class="award-icon">🥇</div>
                    <div class="award-info">
                      <span class="award-name">年度最佳贡献奖</span>
                      <span class="award-desc">2026年度</span>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 中间分割线 -->
              <div class="split-line"></div>
              
              <!-- 右边：AI使用达人 -->
              <div class="ai-users">
                <div class="users-header">
                  <h4>AI使用达人</h4>
                  <el-tag effect="dark" round size="small" color="#626aef">TOP</el-tag>
                </div>
                <div class="users-grid">
                  <div class="user-card" v-for="i in 6" :key="i">
                    <el-avatar :size="50" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                    <span class="user-name">User_{{ 100 + i }}</span>
                    <span class="user-level">Lv.{{ 10 - i }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :xs="24" :md="8">
          <div class="glass-card full-height">
            <div class="card-header">
              <h3>
                <el-icon class="header-icon"><Bell /></el-icon>
                社区头条
              </h3>
              <el-tag effect="plain" round size="small">HOT</el-tag>
            </div>
            <ul class="news-list">
              <li v-for="(news, idx) in newsList" :key="idx" class="news-item">
                <span class="news-dot"></span>
                <span class="news-title">{{ news.title }}</span>
                <span class="news-date">{{ news.date }}</span>
              </li>
            </ul>
          </div>
        </el-col>
      </el-row>

      <section class="section-block">
        <div class="section-title-center">
          <h2>AI 工具专区</h2>
          <p>AI TOOL ZONE</p>
        </div>
        
        <el-row :gutter="20">
          <el-col :xs="12" :sm="6" v-for="tool in tools" :key="tool.name" style="margin-bottom: 20px;">
            <div class="tool-card glass-card hover-effect">
              <div class="tool-icon" :style="{ background: tool.color }">
                {{ tool.name[0] }}
              </div>
              <div class="tool-info">
                <h4>{{ tool.name }}</h4>
                <p>{{ tool.desc }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </section>

      <el-row :gutter="24" class="section-row">
        <el-col :xs="24" :md="12">
          <div class="glass-card">
            <div class="card-header">
              <h3>🏠 AI 优秀实践</h3>
              <el-icon><ArrowRight /></el-icon>
            </div>
            <div class="text-list">
              <p v-for="n in 5" :key="n" class="list-row">
                <span class="tag">实践</span>
                大模型在工业设计中的落地应用案例分享...
              </p>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :md="12">
          <div class="glass-card">
            <div class="card-header">
              <h3>🗣️ 赋能交流</h3>
              <el-icon><ChatDotRound /></el-icon>
            </div>
            <div class="text-list">
              <p v-for="n in 5" :key="n" class="list-row">
                <span class="tag blue">讨论</span>
                如何使用 Agent 提升代码开发效率？
              </p>
            </div>
          </div>
        </el-col>
      </el-row>

      <div class="glass-card wide-banner section-row">
        <div class="info-content">
          <h3>AI 领域周报 (2026年1月1日 - 1月7日)</h3>
          <p>
            本周 AI 领域持续升温，OpenAI 发布最新推理模型，国内大模型在长文本处理上取得突破性进展。
            同时，AI 在软件工程中的实际落地效果获得更多量化验证...
          </p>
        </div>
        <el-button round>阅读更多</el-button>
      </div>
    </main>

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
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ArrowRight, ChatDotRound, Bell } from '@element-plus/icons-vue'
import HeroCarousel from '@/components/HeroCarousel.vue'

// Mock Data
const newsList = ref([
  { title: '【大模型专题】多模态模型在医疗影像中的最新应用突破', date: '刚刚' },
  { title: '【社区活动】2026 AI 开发者大会早鸟票开启预售', date: '1小时前' },
  { title: '【开源动态】轻量级 LLM 本地部署最佳实践指南', date: '昨天' },
  { title: '【深度解析】Agent 自主智能体的未来发展趋势', date: '昨天' },
  { title: '【荣誉榜】恭喜 User_9527 获得年度最佳贡献奖', date: '2天前' },
  { title: '【技术分享】如何构建高质量的 RAG 知识库', date: '3天前' },
])

const tools = ref([
  { name: 'TestMate', desc: '自动化测试助手', color: '#36cfc9' },
  { name: 'CodeMate', desc: '智能代码补全', color: '#9254de' },
  { name: 'YunJi', desc: '云端计算集群', color: '#597ef7' },
  { name: 'YunJian', desc: '智能监控平台', color: '#ff9c6e' },
  { name: '扶摇', desc: 'Agent编排引擎', color: '#4096ff' },
  { name: '创绘Agent', desc: 'AI绘画工作流', color: '#ffc53d' },
  { name: 'DT', desc: '数据转换工具', color: '#73d13d' },
  { name: '云图', desc: '知识图谱构建', color: '#f759ab' },
])
</script>

<style scoped lang="scss">
/* -----------------------
  1. 全局布局与背景 
  -----------------------
*/
.page-container {
  min-height: 100vh;
  position: relative;
  font-family: 'PingFang SC', 'Helvetica Neue', Helvetica, 'Hiragino Sans GB', Arial, sans-serif;
  overflow-x: hidden;
  color: #fff;
}

/* 亮色蓝紫流体背景 */
.global-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  /* 基础渐变：偏亮色的蓝紫 */
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
  background-image: linear-gradient(160deg, #6a11cb 0%, #2575fc 100%); /* 更具科技感的亮蓝紫 */

  /* 添加一点流体光斑效果 (简单CSS实现) */
  .blob {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.6;
  }
  .blob-1 {
    top: -10%;
    left: -10%;
    width: 600px;
    height: 600px;
    background: #a18cd1;
  }
  .blob-2 {
    bottom: -10%;
    right: -10%;
    width: 700px;
    height: 700px;
    background: #4facfe;
  }
}

/* 导航栏已移至 App.vue，此处删除 */

/* -----------------------
  3. 主内容区域通用样式
  -----------------------
*/
.hero-section {
  width: 100%;
  /* 轮播图区域 */
}

.main-content {
  width: 100%;
  max-width: 1280px; /* 内容宽度限制 */
  margin: 0 auto;
  padding: 20px;
  padding-top: 10px; /* 减少顶部间距，让内容更靠近轮播图 */
}

/* 间距控制 */
.section-row,
.section-block {
  margin-bottom: 40px;
}

/* 第一个 section-row 减少顶部间距 */
.section-row:first-of-type {
  margin-top: 0;
  padding-top: 0;
}

/* 通用毛玻璃卡片 - 更白更明显 */
.glass-card {
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
  color: #fff;
  transition: transform 0.3s;
}

.full-height {
  height: 100%;
  min-height: 300px;
}

.honor-section {
  min-height: auto;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding-bottom: 10px;

  h3 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .header-icon {
    font-size: 20px;
    color: #409eff;
  }
}


/* -----------------------
  4. 具体的版块内部样式
  -----------------------
*/

/* 荣誉殿堂和AI使用达人 - 左右分割布局 */
.honor-split-container {
  display: flex;
  gap: 20px;
  height: 100%;
}

/* 左边：荣誉殿堂 */
.honor-hall {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.hall-banner {
  background: linear-gradient(135deg, rgba(98, 106, 239, 0.3), rgba(139, 92, 246, 0.3));
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  
  .banner-icon {
    font-size: 32px;
    width: 48px;
    height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 10px;
  }
  
  .banner-text {
    flex: 1;
    h4 {
      margin: 0;
      font-size: 18px;
      font-weight: 700;
    }
    p {
      margin: 4px 0 0;
      font-size: 12px;
      opacity: 0.8;
    }
  }
}

.awards-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.award-item {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  padding: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: all 0.3s;
  
  &:hover {
    background: rgba(255, 255, 255, 0.15);
    transform: translateX(4px);
  }
  
  .award-icon {
    font-size: 24px;
  }
  
  .award-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 4px;
    
    .award-name {
      font-size: 14px;
      font-weight: 600;
    }
    
    .award-desc {
      font-size: 12px;
      opacity: 0.7;
    }
  }
}

/* 中间分割线 */
.split-line {
  width: 1px;
  background: repeating-linear-gradient(
    to bottom,
    rgba(255, 255, 255, 0.3) 0,
    rgba(255, 255, 255, 0.3) 8px,
    transparent 8px,
    transparent 16px
  );
  flex-shrink: 0;
}

/* 右边：AI使用达人 */
.ai-users {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.users-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  h4 {
    margin: 0;
    font-size: 18px;
    font-weight: 700;
  }
}

.users-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.user-card {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  padding: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
  cursor: pointer;
  
  &:hover {
    background: rgba(255, 255, 255, 0.15);
    transform: translateY(-2px);
  }
  
  .user-name {
    font-size: 13px;
    font-weight: 600;
    text-align: center;
  }
  
  .user-level {
    font-size: 11px;
    opacity: 0.7;
    background: rgba(255, 255, 255, 0.1);
    padding: 2px 8px;
    border-radius: 10px;
  }
}

.news-list {
  list-style: none;
  padding: 0;
  margin: 0;

  .news-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px dashed rgba(255, 255, 255, 0.1);
    cursor: pointer;

    &:hover .news-title {
      color: #409eff;
    }

    .news-dot {
      width: 6px;
      height: 6px;
      background: #409eff;
      border-radius: 50%;
      margin-right: 10px;
    }

    .news-title {
      flex: 1;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      margin-right: 10px;
      font-size: 14px;
    }

    .news-date {
      font-size: 12px;
      color: rgba(255, 255, 255, 0.5);
    }
  }
}

/* Section 2: Tools Zone */
.section-title-center {
  text-align: center;
  margin-bottom: 30px;

  h2 {
    font-size: 28px;
    margin: 0;
    text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  }
  p {
    margin: 5px 0 0;
    letter-spacing: 4px;
    opacity: 0.7;
    font-size: 12px;
  }
}

.tool-card {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  cursor: pointer;

  &.hover-effect:hover {
    transform: translateY(-5px);
    background: rgba(255, 255, 255, 0.15);
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  }

  .tool-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    font-size: 20px;
    color: #fff;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  }

  .tool-info h4 {
    margin: 0 0 4px;
    font-size: 16px;
  }
  .tool-info p {
    margin: 0;
    font-size: 12px;
    opacity: 0.6;
  }
}

/* Section 3: AI Park */
.text-list .list-row {
  margin-bottom: 12px;
  font-size: 14px;
  display: flex;
  align-items: center;
  color: rgba(255, 255, 255, 0.9);

  .tag {
    background: rgba(103, 194, 58, 0.2);
    color: #95d475;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 12px;
    margin-right: 8px;
    white-space: nowrap;

    &.blue {
      background: rgba(64, 158, 255, 0.2);
      color: #a0cfff;
    }
  }
}

/* Section 4: AI Info */
.wide-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .info-content {
    h3 {
      margin: 0 0 8px;
      color: #d9ecff;
    }
    p {
      margin: 0;
      font-size: 13px;
      opacity: 0.8;
      max-width: 800px;
    }
  }
}

/* Footer */
.footer-banner {
  margin-top: 60px;
  border-radius: 0; /* 全宽不需要圆角 */
  border-left: none;
  border-right: none;

  .footer-content {
    max-width: 1280px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0;
  }

  .contact-box {
    display: flex;
    align-items: center;
    gap: 15px;
    .qr-placeholder {
      width: 60px;
      height: 60px;
      background: #fff;
      color: #333;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 10px;
      border-radius: 4px;
    }
  }

  .friend-links {
    display: flex;
    align-items: center;
    gap: 10px;
    span {
      font-size: 12px;
      opacity: 0.6;
    }
  }
}
</style>
