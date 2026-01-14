/**
 * 路由路径配置
 * 所有路由路径都在这里统一管理，方便修改和维护
 * 
 * 注意：路由前缀 /ai_community 在 vite.config.ts 的 base 中配置，
 * Vue Router 会通过 import.meta.env.BASE_URL 自动获取，
 * 所以这里的路由路径不需要包含前缀。
 */

// ==================== 页面路由 ====================
// 路由路径不含前缀，前缀由 Vite base 配置提供
export const ROUTES = {
  // 首页
  HOME: '/home',
  
  // AI优秀实践
  PRACTICES: '/practices',
  
  // AI使用达人
  USERS: '/users',
  
  // AI工具专区
  TOOLS: '/tools',
  
  // 扶摇Agent应用
  AGENT: '/agent',
  
  // 赋能交流
  EMPOWERMENT: '/empowerment',
  
  // AI资讯
  NEWS: '/news',
  
  // 荣誉榜
  HONOR: '/honor',
  
  // 帖子相关
  POST_CREATE: '/post/create',
  POST_DETAIL: (id: number | string) => `/post/${id}`,
  
  // 活动相关
  ACTIVITY_CREATE: '/activity/create',
  ACTIVITY_DETAIL: (id: number | string) => `/activity/${id}`,
  
  // 个人中心
  PROFILE: '/profile',
  
  // 消息中心
  MESSAGES: '/messages',
  
  // 管理后台
  ADMIN: '/admin',
} as const

// ==================== 类型导出 ====================
export type RoutePath = typeof ROUTES[keyof typeof ROUTES]
