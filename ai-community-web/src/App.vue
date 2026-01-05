<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="app-shell">
    <!-- Navigation Bar with Glassmorphism -->
    <header class="navbar">
      <div class="navbar-container">
        <div class="brand">
          <span class="logo-text">AI Portal</span>
        </div>
        <nav class="nav-menu">
          <RouterLink to="/" class="nav-item">首页</RouterLink>
          <RouterLink to="/practices" class="nav-item">AI优秀实践</RouterLink>
          <RouterLink to="/users" class="nav-item">AI使用达人</RouterLink>
          <RouterLink to="/tools" class="nav-item">AI工具专区</RouterLink>
          <RouterLink to="/agent" class="nav-item">扶摇Agent应用</RouterLink>
          <RouterLink to="/empowerment" class="nav-item">赋能交流</RouterLink>
          <RouterLink to="/honor" class="nav-item">荣誉殿堂</RouterLink>
          <RouterLink to="/news" class="nav-item">AI资讯</RouterLink>
        </nav>
        <div class="nav-actions">
          <el-button text class="login-btn">登录</el-button>
          <el-button type="primary" class="join-btn">加入社区</el-button>
        </div>
      </div>
    </header>

    <main class="page-content">
      <RouterView />
    </main>

    <footer class="app-footer">
      <div class="footer-content">
        <div>© {{ new Date().getFullYear() }} AI Portal. 共建前沿 AI 知识与实践。</div>
        <div class="footer-links">
          <a href="#">隐私</a>
          <a href="#">条款</a>
          <a href="#">联系我们</a>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped lang="scss">
.app-shell {
  min-height: 100vh;
  position: relative;
}

// Glassmorphism for Navbar
.navbar {
  position: sticky;
  top: 0;
  z-index: 1000;
  width: 100%;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.4);
  box-shadow: 0 4px 24px 0 rgba(31, 38, 135, 0.1);
}

.navbar-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 16px 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.brand {
  .logo-text {
    font-size: 24px;
    font-weight: 800;
    color: #ffffff; /* 白色字体 */
  }
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  justify-content: center;
  flex-wrap: wrap;
}

.nav-item {
  padding: 8px 16px;
  color: #ffffff; /* 白色字体 */
  text-decoration: none;
  font-weight: 500;
  font-size: 14px;
  border-radius: 8px;
  transition: all 0.2s ease;
  white-space: nowrap;

  &:hover {
    background: rgba(255, 255, 255, 0.3);
    color: #ffffff; /* 悬停时保持白色 */
  }

  &.router-link-active {
    background: rgba(255, 255, 255, 0.25);
    color: #ffffff; /* 激活时保持白色 */
    font-weight: 600;
    box-shadow: 0 2px 8px rgba(255, 255, 255, 0.2);
  }
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.login-btn {
  color: #ffffff; /* 白色字体 */
  font-weight: 500;

  &:hover {
    color: #ffffff; /* 悬停时保持白色 */
    background: rgba(255, 255, 255, 0.2);
  }
}

.join-btn {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  color: #fff;
  font-weight: 600;

  &:hover {
    background: linear-gradient(135deg, #5568d3, #6a3f91);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
  }
}

.page-content {
  position: relative;
  z-index: 2;
  min-height: 100vh;
}

.app-footer {
  position: relative;
  z-index: 1;
  padding: 32px 20px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(12px);
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  margin-top: 60px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
  color: #1a1a1a;
  font-size: 14px;
}

.footer-links {
  display: flex;
  gap: 20px;

  a {
    color: #4b5563;
    text-decoration: none;
    transition: color 0.2s ease;

    &:hover {
      color: #667eea;
    }
  }
}

// Responsive Design
@media (max-width: 1024px) {
  .navbar-container {
    padding: 12px 20px;
  }

  .nav-menu {
    gap: 4px;
  }

  .nav-item {
    padding: 6px 12px;
    font-size: 13px;
  }
}

@media (max-width: 768px) {
  .navbar-container {
    flex-direction: column;
    gap: 16px;
    padding: 12px 16px;
  }

  .nav-menu {
    width: 100%;
    justify-content: flex-start;
    overflow-x: auto;
    padding-bottom: 8px;

    &::-webkit-scrollbar {
      height: 4px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(102, 126, 234, 0.3);
      border-radius: 2px;
    }
  }

  .nav-actions {
    width: 100%;
    justify-content: flex-end;
  }

  .footer-content {
    flex-direction: column;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .brand .logo-text {
    font-size: 20px;
  }

  .nav-item {
    font-size: 12px;
    padding: 6px 10px;
  }

  .join-btn,
  .login-btn {
    font-size: 13px;
    padding: 8px 16px;
  }
}
</style>
