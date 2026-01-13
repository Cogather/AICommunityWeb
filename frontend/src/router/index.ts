import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/HomeView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/practices',
    name: 'Practices',
    component: () => import('../views/PracticesView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/users',
    name: 'Users',
    component: () => import('../views/UsersView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/tools',
    name: 'Tools',
    component: () => import('../views/ToolsView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/agent',
    name: 'Agent',
    component: () => import('../views/AgentView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/empowerment',
    name: 'Empowerment',
    component: () => import('../views/EmpowermentView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/award-rules',
    name: 'AwardRules',
    component: () => import('../views/AwardRulesView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/news',
    name: 'News',
    component: () => import('../views/NewsView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/post/create',
    name: 'PostCreate',
    component: () => import('../views/PostCreateView.vue')
  },
  {
    path: '/post/:id',
    name: 'PostDetail',
    component: () => import('../views/PostDetailView.vue')
  },
  {
    path: '/activity/create',
    name: 'ActivityCreate',
    component: () => import('../views/ActivityCreateView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/activity/:id',
    name: 'ActivityDetail',
    component: () => import('../views/ActivityDetailView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/ProfileView.vue')
  },
  {
    path: '/messages',
    name: 'Messages',
    component: () => import('../views/MessageListView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/AdminView.vue').catch(() => import('../views/NotFoundView.vue'))
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFoundView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
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
