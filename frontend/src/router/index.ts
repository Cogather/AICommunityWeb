import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { ROUTES } from './paths'

// 重新导出路由配置，方便其他模块使用
export { ROUTES } from './paths'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: ROUTES.HOME
  },
  {
    path: ROUTES.HOME,
    name: 'Home',
    component: () => import('../views/HomeView.vue')
  },
  {
    path: ROUTES.PRACTICES,
    name: 'Practices',
    component: () => import('../views/PracticesView.vue')
  },
  {
    path: ROUTES.USERS,
    name: 'Users',
    component: () => import('../views/UsersView.vue')
  },
  {
    path: ROUTES.TOOLS,
    name: 'Tools',
    component: () => import('../views/ToolsView.vue')
  },
  {
    path: ROUTES.AGENT,
    name: 'Agent',
    component: () => import('../views/AgentView.vue')
  },
  {
    path: ROUTES.EMPOWERMENT,
    name: 'Empowerment',
    component: () => import('../views/EmpowermentView.vue')
  },
  {
    path: ROUTES.NEWS,
    name: 'News',
    component: () => import('../views/NewsView.vue')
  },
  {
    path: ROUTES.HONOR,
    name: 'Honor',
    component: () => import('../views/HonorView.vue')
  },
  {
    path: ROUTES.POST_CREATE,
    name: 'PostCreate',
    component: () => import('../views/PostCreateView.vue')
  },
  {
    path: '/post/:id',
    name: 'PostDetail',
    component: () => import('../views/PostDetailView.vue')
  },
  {
    path: ROUTES.ACTIVITY_CREATE,
    name: 'ActivityCreate',
    component: () => import('../views/ActivityCreateView.vue')
  },
  {
    path: '/activity/:id',
    name: 'ActivityDetail',
    component: () => import('../views/ActivityDetailView.vue')
  },
  {
    path: ROUTES.PROFILE,
    name: 'Profile',
    component: () => import('../views/ProfileView.vue')
  },
  {
    path: ROUTES.MESSAGES,
    name: 'Messages',
    component: () => import('../views/MessageListView.vue')
  },
  {
    path: ROUTES.ADMIN,
    name: 'Admin',
    component: () => import('../views/AdminView.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFoundView.vue')
  }
]

const router = createRouter({
  // 使用 Vite 的 base 配置作为路由基础路径
  // import.meta.env.BASE_URL 会自动获取 vite.config.ts 中的 base 值 (/ai_community/)
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    // 如果有保存的位置（比如浏览器前进后退），则使用保存的位置
    if (savedPosition) {
      return savedPosition
    }
    // 否则滚动到顶部
    return { top: 0 }
  }
})

export default router
