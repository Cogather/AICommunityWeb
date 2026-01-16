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

import loginService from '../utils/loginService'

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

// 路由守卫
router.beforeEach((to, from, next) => {
  loginService.validate().then(valid => {
    if (valid) {
      next()
    } else {
      // validate 内部通常会处理重定向，如果返回 false 可能意味着需要登录但被拦截
      // 这里可以根据需要添加逻辑，例如跳转到登录页（如果 validate 没做的话）
      // 目前 loginService.validate 在未登录时会重定向到 SSO，所以这里很少走到 else
      // 为了安全，可以阻止导航
      // next(false)
      // 但为了兼容性，如果 validate 返回 false 但没跳转，可能是 API 失败？
      next()
    }
  }).catch(() => {
    next()
  })
})

export default router
