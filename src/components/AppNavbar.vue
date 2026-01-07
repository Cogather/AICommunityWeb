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
        <!-- 未登录状态 -->
        <template v-if="!isLoggedIn">
          <el-button text class="login-btn" @click="handleLogin">登录</el-button>
          <el-button type="primary" class="join-btn">加入社区</el-button>
        </template>
        <!-- 已登录状态 -->
        <template v-else>
          <el-dropdown @command="handleCommand" trigger="hover" placement="bottom-end">
            <div class="user-dropdown-trigger">
              <el-avatar :size="36" :src="userInfo.avatar" class="user-avatar">
                {{ userInfo.name?.charAt(0) || 'U' }}
              </el-avatar>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  <span>个人中心</span>
                </el-dropdown-item>
                <el-dropdown-item command="settings" divided>
                  <el-icon><Setting /></el-icon>
                  <span>设置</span>
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { User, Setting, SwitchButton, ArrowDown } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 登录状态（实际应该从 store 或 API 获取）
const isLoggedIn = ref(true) // 暂时设为 true 用于测试

// 用户信息（实际应该从 store 或 API 获取）
const userInfo = ref({
  name: '张三',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
})

// 处理登录
const handleLogin = () => {
  // 这里应该跳转到登录页面或显示登录对话框
  ElMessage.info('登录功能开发中')
}

// 处理下拉菜单命令
const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      ElMessage.info('设置功能开发中')
      break
    case 'logout':
      // 这里应该调用登出API
      isLoggedIn.value = false
      ElMessage.success('已退出登录')
      break
  }
}
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
    color: #1e40af; /* 深蓝色悬停 */
  }

  &.router-link-active {
    color: #1e3a8a; /* 深蓝色选中，不那么刺眼 */
    font-weight: 700;
    
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 16px;
      right: 16px;
      height: 2px;
      background-color: #1e3a8a; /* 深蓝色下划线 */
      border-radius: 2px 2px 0 0;
    }
  }
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-dropdown-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.3s ease;

  &:hover {
    background: rgba(0, 0, 0, 0.05);
  }

  .user-avatar {
    flex-shrink: 0;
  }

  .dropdown-icon {
    font-size: 12px;
    color: #666;
    transition: transform 0.3s ease;
  }
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}

.login-btn {
  color: #000000; /* 黑色字体 */
  font-weight: 500;

  &:hover {
    color: #000000; /* 悬停时保持黑色 */
    background: rgba(0, 0, 0, 0.05);
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

