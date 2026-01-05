<template>
  <header class="navbar">
    <div class="navbar-container">
      <div class="brand">
        <RouterLink to="/" class="logo-link">
          <span class="logo-text">AI Portal</span>
        </RouterLink>
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
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<style scoped lang="scss">
// Glassmorphism for Navbar
.navbar {
  position: sticky;
  top: 0;
  z-index: 1000;
  width: 100%;
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 
    0 4px 24px 0 rgba(31, 38, 135, 0.15),
    0 1px 0 rgba(255, 255, 255, 0.6) inset,
    0 -1px 0 rgba(0, 0, 0, 0.1) inset;
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
  .logo-link {
    text-decoration: none;
    
    .logo-text {
      font-size: 24px;
      font-weight: 800;
      color: #ffffff;
    }
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
  color: #333;
  text-decoration: none;
  font-weight: 600;
  font-size: 14px;
  border-radius: 8px;
  transition: all 0.2s ease;
  white-space: nowrap;
  position: relative;

  &:hover {
    color: #409eff;
  }

  &.router-link-active {
    color: #409eff;
    font-weight: 700;
    
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 16px;
      right: 16px;
      height: 2px;
      background-color: #409eff;
      border-radius: 2px 2px 0 0;
    }
  }
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.login-btn {
  color: #ffffff;
  font-weight: 500;

  &:hover {
    color: #ffffff;
    background: rgba(255, 255, 255, 0.2);
  }
}

.join-btn {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  color: #fff;
  font-weight: 700;
  font-size: 16px;
  padding: 12px 28px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.5);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
    transition: left 0.5s;
  }

  &:hover {
    background: linear-gradient(135deg, #5568d3, #6a3f91);
    transform: translateY(-3px);
    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
    
    &::before {
      left: 100%;
    }
  }

  &:active {
    transform: translateY(-1px);
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

