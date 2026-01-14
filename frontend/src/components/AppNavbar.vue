<template>
  <header class="navbar">
    <div class="navbar-container">
      <div class="brand">
        <RouterLink to="/" class="logo-link">
          <span class="logo-text">云核AI使能社区</span>
        </RouterLink>
      </div>
      <nav class="nav-menu">
        <RouterLink to="/" class="nav-item">首页</RouterLink>
        <RouterLink to="/practices" class="nav-item">AI优秀实践</RouterLink>
        <el-dropdown 
          trigger="hover" 
          placement="bottom" 
          @command="handleUsersMenuCommand"
          class="users-dropdown"
        >
          <span class="nav-item users-nav-link" :class="{ 'router-link-active': isUsersPage }">
            <span class="nav-link-text">AI使用达人</span>
            <span class="dropdown-arrow">▼</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu class="users-dropdown-menu">
              <!-- 个人风采和团队荣誉切换 -->
              <div class="dropdown-section">
                <el-dropdown-item 
                  command="awardType:individual"
                  :class="{ active: currentAwardType === 'individual' }"
                >
                  <el-icon><UserFilled /></el-icon>
                  <span>个人风采</span>
                </el-dropdown-item>
                <el-dropdown-item 
                  command="awardType:team"
                  :class="{ active: currentAwardType === 'team' }"
                >
                  <el-icon><Trophy /></el-icon>
                  <span>团队荣誉</span>
                </el-dropdown-item>
              </div>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <RouterLink to="/tools" class="nav-item">AI工具专区</RouterLink>
        <RouterLink to="/agent" class="nav-item">扶摇Agent应用</RouterLink>
        <RouterLink to="/empowerment" class="nav-item">赋能交流</RouterLink>
        <RouterLink to="/news" class="nav-item">AI资讯</RouterLink>
        <RouterLink v-if="isAdmin" to="/admin" class="nav-item admin-link">管理</RouterLink>
      </nav>
      <div class="nav-actions">
        <!-- 未登录状态 -->
        <template v-if="!isLoggedIn">
          <el-button text class="login-btn" @click="handleLogin">登录</el-button>
          <el-button type="primary" class="join-btn">加入社区</el-button>
        </template>
        <!-- 已登录状态 -->
        <template v-else>
          <!-- 消息提示 -->
          <el-badge :value="unreadMessageCount" :hidden="unreadMessageCount === 0" :max="99">
            <el-button text class="message-btn" @click="handleMessageClick">
              <el-icon :size="20"><Bell /></el-icon>
            </el-button>
          </el-badge>
          
          <el-dropdown @command="handleCommand" trigger="hover" placement="bottom-end">
            <div class="user-dropdown-trigger">
              <el-avatar :size="36" :src="userInfo.avatar" class="user-avatar">
                {{ userInfo.name?.charAt(0) || 'U' }}
              </el-avatar>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="user-dropdown-menu">
                <!-- 积分显示区域 -->
                <div class="points-section">
                  <div class="points-header">
                    <el-icon class="points-icon"><Trophy /></el-icon>
                    <span class="points-label">个人积分</span>
                  </div>
                  <div class="points-value">{{ userPoints }}</div>
                  <div class="points-hint">积分高的用户可获得AI使用达人奖项</div>
                </div>
                
                <!-- 积分规则 -->
                <div class="points-rules-section">
                  <div class="rules-title">积分规则</div>
                  <div class="rules-list">
                    <div class="rule-item">
                      <el-icon class="rule-icon"><Document /></el-icon>
                      <span class="rule-text">发布帖子</span>
                      <span class="rule-points">+15</span>
                    </div>
                    <div class="rule-item">
                      <el-icon class="rule-icon"><Calendar /></el-icon>
                      <span class="rule-text">参加活动</span>
                      <span class="rule-points">+10</span>
                    </div>
                    <div class="rule-item">
                      <el-icon class="rule-icon"><Collection /></el-icon>
                      <span class="rule-text">帖子被收藏</span>
                      <span class="rule-points">+5</span>
                    </div>
                    <div class="rule-item">
                      <el-icon class="rule-icon"><Star /></el-icon>
                      <span class="rule-text">帖子被点赞</span>
                      <span class="rule-points">+3</span>
                    </div>
                    <div class="rule-item">
                      <el-icon class="rule-icon"><ChatDotRound /></el-icon>
                      <span class="rule-text">发表评论</span>
                      <span class="rule-points">+1</span>
                    </div>
                    <div class="rule-item">
                      <el-icon class="rule-icon"><Star /></el-icon>
                      <span class="rule-text">评论被点赞</span>
                      <span class="rule-points">+1</span>
                    </div>
                  </div>
                </div>
                
                <el-dropdown-item command="profile" divided>
                  <el-icon><User /></el-icon>
                  <span>个人中心</span>
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
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import { RouterLink, useRouter, useRoute } from 'vue-router'
import { 
  User, 
  SwitchButton, 
  ArrowDown, 
  Trophy,
  Document,
  ChatDotRound,
  Star,
  Collection,
  Bell,
  Calendar,
  UserFilled
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getUnreadMessageCount } from '../utils/message'
import { getTeamAwards } from '../mock'

const router = useRouter()
const route = useRoute()

// 判断是否在AI使用达人页面
const isUsersPage = computed(() => route.path === '/users')

// 团队荣誉相关状态（用于下拉菜单）
const currentAwardType = ref<'individual' | 'team'>('individual')
const teamAwardYears = ref<string[]>([])
const selectedYear = ref<string>('')
const currentTeamAwards = ref<any[]>([])
const activeTeamAwardIndex = ref<number>(0)

// 加载团队荣誉数据（用于下拉菜单）
const loadTeamAwardsForMenu = async () => {
  try {
    const response = await getTeamAwards() as { list: any[] }
    if (response && response.list && response.list.length > 0) {
      const years = Array.from(new Set(response.list.map(a => String(a.year || new Date().getFullYear()))))
        .sort((a, b) => Number(b) - Number(a))
      teamAwardYears.value = years
      if (years.length > 0 && !selectedYear.value && years[0]) {
        selectedYear.value = years[0]
      }
      updateCurrentTeamAwards()
    }
  } catch (e) {
    console.error('加载团队荣誉数据失败:', e)
  }
}

// 更新当前年份的奖项列表（通过事件从UsersView获取）
const updateCurrentTeamAwards = () => {
  // 通过事件从UsersView获取，这里只负责显示
}

// 监听路由变化，加载团队荣誉数据
watch(() => route.path, (_newPath) => {
  loadTeamAwardsForMenu()
}, { immediate: true })

// 处理AI使用达人链接点击（已移除，因为现在RouterLink和dropdown分开）

// 处理下拉菜单命令
const handleUsersMenuCommand = (command: string) => {
  console.log('AppNavbar: 下拉菜单命令', command)
  const [type, value] = command.split(':')
  if (type === 'awardType') {
    currentAwardType.value = value as 'individual' | 'team'
    // 点击下拉菜单项时，跳转到/users页面并发送事件
    if (route.path !== '/users') {
      router.push('/users').then(() => {
        // 等待页面加载完成后再发送事件
        setTimeout(() => {
          console.log('AppNavbar: 发送awardTypeChange事件', value)
          window.dispatchEvent(new CustomEvent('awardTypeChange', { detail: { type: value } }))
        }, 150)
      }).catch((err) => {
        console.error('AppNavbar: 路由跳转失败', err)
      })
    } else {
      // 如果已经在/users页面，直接发送事件
      console.log('AppNavbar: 已在/users页面，直接发送awardTypeChange事件', value)
      // 使用nextTick确保组件已更新
      setTimeout(() => {
        window.dispatchEvent(new CustomEvent('awardTypeChange', { detail: { type: value } }))
      }, 50)
    }
  }
}

// 监听团队荣誉数据更新事件
const handleTeamAwardsUpdate = (event: CustomEvent) => {
  if (event.detail?.awardType) {
    currentAwardType.value = event.detail.awardType
  }
  if (event.detail?.years) {
    teamAwardYears.value = event.detail.years
  }
  if (event.detail?.selectedYear) {
    selectedYear.value = event.detail.selectedYear
  }
  if (event.detail?.currentTeamAwards) {
    currentTeamAwards.value = event.detail.currentTeamAwards
  }
  if (event.detail?.activeTeamAwardIndex !== undefined) {
    activeTeamAwardIndex.value = event.detail.activeTeamAwardIndex
  }
}

onMounted(() => {
  loadTeamAwardsForMenu()
  window.addEventListener('teamAwardsUpdate', handleTeamAwardsUpdate as EventListener)
})

onBeforeUnmount(() => {
  window.removeEventListener('teamAwardsUpdate', handleTeamAwardsUpdate as EventListener)
})

// 登录状态（实际应该从 store 或 API 获取）
const isLoggedIn = ref(true) // 暂时设为 true 用于测试

// 用户信息（实际应该从 store 或 API 获取）
const userInfo = ref({
  name: '张三',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  role: 'admin' // 'admin' | 'user' - 实际应该从 store 或 API 获取
})

// 判断是否为管理员
const isAdmin = computed(() => {
  return userInfo.value.role === 'admin'
})

// 用户积分（实际应该从 store 或 API 获取）
// 这里使用模拟数据，实际应该从后端获取
const userPoints = computed(() => {
  // 模拟计算积分：发帖数*15 + 评论数*1 + 被点赞数*3 + 被收藏数*5 + 参加活动数*10
  // 实际应该从API获取
  const mockData = {
    postsCount: 12,      // 发帖数
    commentsCount: 45,   // 评论数
    likesReceived: 128,   // 帖子被点赞数
    favoritesReceived: 8,  // 帖子被收藏数
    activitiesCount: 5    // 参加活动数
  }
  return mockData.postsCount * 15 + 
         mockData.commentsCount * 1 + 
         mockData.likesReceived * 3 + 
         mockData.favoritesReceived * 5 +
         mockData.activitiesCount * 10
})

// 当前用户ID（实际应该从登录状态获取）
const currentUserId = ref(1)

// 未读消息数量
const unreadMessageCount = ref(0)

// 加载未读消息数量
const loadUnreadCount = () => {
  if (isLoggedIn.value) {
    unreadMessageCount.value = getUnreadMessageCount(currentUserId.value)
  }
}

// 处理消息点击
const handleMessageClick = () => {
  router.push('/messages')
}

// 监听消息更新事件
const handleMessageUpdate = (event: CustomEvent) => {
  if (event.detail.userId === currentUserId.value) {
    loadUnreadCount()
  }
}

onMounted(() => {
  loadUnreadCount()
  window.addEventListener('messageUpdated', handleMessageUpdate as EventListener)
})

onBeforeUnmount(() => {
  window.removeEventListener('messageUpdated', handleMessageUpdate as EventListener)
})

// 处理登录
const handleLogin = () => {
  // 这里应该跳转到登录页面或显示登录对话框
  ElMessage.info('登录功能开发中')
}

// 处理用户下拉菜单命令
const handleCommand = (command: string) => {
  console.log('AppNavbar: 下拉菜单命令', command)
  switch (command) {
    case 'profile':
      console.log('AppNavbar: 跳转到个人中心')
      router.push('/profile').catch((err) => {
        console.error('路由跳转失败:', err)
      })
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
      font-size: 20px; /* 减小字体大小 */
      font-weight: 800;
      color: #000000; /* 改为黑色 */
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
  font-size: 16px; /* 从 14px 增加到 16px */
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

  &.users-nav-link {
    font-weight: 700; /* 加粗字体 */
    color: #333; /* 和其他导航项一样的颜色 */
    
    &.router-link-active {
      font-weight: 700; /* 选中时也保持加粗，不显示下划线 */
      color: #1e3a8a; /* 选中时的颜色 */
      
      &::after {
        display: none; /* 不显示选中框 */
      }
    }
    
    &:hover {
      color: #1e40af; /* 悬停时的颜色 */
    }
  }

  &.admin-link {
    color: #f59e0b;
    font-weight: 700;

    &:hover {
      color: #d97706;
    }

    &.router-link-active {
      color: #d97706;
      
      &::after {
        background-color: #f59e0b;
      }
    }
  }

  &.dropdown-trigger {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    position: relative;

    .nav-link-inner {
      text-decoration: none;
      color: inherit;
      display: inline-block;
    }

    .dropdown-arrow {
      font-size: 10px;
      color: #666;
      transition: transform 0.3s ease;
      margin-left: 4px;
      display: inline-block;
      line-height: 1;
      cursor: pointer;
      pointer-events: auto;
    }

    &:hover .dropdown-arrow {
      transform: rotate(180deg);
      color: #1e40af;
    }
  }
}

.users-dropdown {
  display: inline-block;
  
  :deep(.el-dropdown__caret-button) {
    display: none;
  }

  .users-nav-link {
    display: inline-flex;
    align-items: center;
    gap: 0;
    cursor: default !important; /* 不是pointer，表示不可点击 */
    position: relative;
    outline: none !important;
    border: none !important;
    box-shadow: none !important;
    color: #333 !important;
    font-weight: 700 !important;
    padding: 8px 16px;
    text-decoration: none;
    background: transparent !important;
    user-select: none; /* 禁止选中文字 */

    .nav-link-text {
      color: inherit;
      font-weight: 700;
      pointer-events: none; /* 禁止点击 */
    }

    .dropdown-arrow {
      font-size: 10px;
      color: #666;
      transition: transform 0.3s ease;
      display: inline-block;
      line-height: 1;
      margin-left: 2px;
      pointer-events: none; /* 禁止点击 */
    }

    &:hover {
      color: #1e40af !important;
      background: transparent !important;
      cursor: default !important; /* 悬停时也是default */
      
      .dropdown-arrow {
        transform: rotate(180deg);
        color: #1e40af;
      }
    }

    &:focus,
    &:focus-visible,
    &:active {
      outline: none !important;
      border: none !important;
      box-shadow: none !important;
      background: transparent !important;
      cursor: default !important;
    }

    &.router-link-active {
      color: #1e3a8a !important;
      background: transparent !important;
      
      &::after {
        display: none; /* 不显示选中框 */
      }
    }
  }

  :deep(.el-dropdown) {
    outline: none !important;
    border: none !important;
    box-shadow: none !important;
    background: transparent !important;
    
    &:focus,
    &:focus-visible,
    &:active {
      outline: none !important;
      border: none !important;
      box-shadow: none !important;
      background: transparent !important;
    }
  }
  
  :deep(.el-dropdown__trigger) {
    outline: none !important;
    border: none !important;
    box-shadow: none !important;
    background: transparent !important;
    cursor: default !important;
  }
}

.users-dropdown-menu {
  min-width: 200px;
  max-height: 400px;
  overflow-y: auto;

  .dropdown-section {
    padding: 8px 0;

    .section-title {
      padding: 8px 20px;
      font-size: 12px;
      font-weight: 700;
      color: #909399;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    :deep(.el-dropdown-menu__item) {
      padding: 10px 20px;
      font-size: 14px;
      color: #606266;

      &.active {
        color: #409eff;
        background-color: rgba(64, 158, 255, 0.1);
        font-weight: 600;
      }

      &:hover {
        background-color: rgba(64, 158, 255, 0.05);
      }
    }
  }
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.message-btn {
  padding: 8px;
  font-size: 20px;
  color: #606266;
  transition: color 0.3s;
  
  &:hover {
    color: #4096ff;
  }
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

.user-dropdown-menu {
  :deep(.el-dropdown-menu__item) {
    padding: 12px 20px;
    display: flex;
    align-items: center;
    gap: 8px;
  }
}

.points-section {
  padding: 16px 20px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  min-width: 240px;

  .points-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;

    .points-icon {
      font-size: 18px;
      color: #f59e0b;
    }

    .points-label {
      flex: 1;
      font-size: 14px;
      font-weight: 600;
      color: #333;
    }
  }

  .points-value {
    font-size: 32px;
    font-weight: 800;
    color: #667eea;
    margin-bottom: 4px;
    line-height: 1;
  }

  .points-hint {
    font-size: 12px;
    color: #909399;
    line-height: 1.4;
  }
}

.points-rules-section {
  padding: 12px 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.3);

  .rules-title {
    font-size: 13px;
    font-weight: 600;
    color: #666;
    margin-bottom: 10px;
  }

  .rules-list {
    display: flex;
    flex-direction: column;
    gap: 8px;

    .rule-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 6px 0;
      font-size: 12px;

      .rule-icon {
        font-size: 16px;
        color: #667eea;
        flex-shrink: 0;
      }

      .rule-text {
        flex: 1;
        color: #666;
      }

      .rule-points {
        font-size: 13px;
        font-weight: 700;
        color: #f59e0b;
        flex-shrink: 0;
      }
    }
  }
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
  font-size: 18px; /* 从 16px 增加到 18px */
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
    font-size: 15px; /* 从 13px 增加到 15px */
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
    font-size: 18px; /* 减小字体大小 */
  }

  .nav-item {
    font-size: 14px; /* 从 12px 增加到 14px */
    padding: 6px 10px;
  }

  .join-btn,
  .login-btn {
    font-size: 15px; /* 从 13px 增加到 15px */
    padding: 8px 16px;
  }
}
</style>

