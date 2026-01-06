import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/HomeView.vue')
  },
  {
    path: '/practices',
    name: 'Practices',
    component: () => import('../views/PracticesView.vue')
  },
  {
    path: '/users',
    name: 'Users',
    component: () => import('../views/UsersView.vue')
  },
  {
    path: '/tools',
    name: 'Tools',
    component: () => import('../views/ToolsView.vue')
  },
  {
    path: '/agent',
    name: 'Agent',
    component: () => import('../views/AgentView.vue')
  },
  {
    path: '/empowerment',
    name: 'Empowerment',
    component: () => import('../views/EmpowermentView.vue')
  },
  {
    path: '/honor',
    name: 'Honor',
    component: () => import('../views/UsersView.vue')
  },
  {
    path: '/news',
    name: 'News',
    component: () => import('../views/NewsView.vue')
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
