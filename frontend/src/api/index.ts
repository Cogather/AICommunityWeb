/**
 * API 统一入口
 * 
 * 使用方式:
 * 1. 在 request.ts 中设置 USE_REAL_API = true 使用真实后端
 * 2. 在 request.ts 中设置 USE_REAL_API = false 使用 mock 数据
 * 
 * 示例:
 * ```typescript
 * // 方式1: 直接导入函数
 * import { getCarousel, getHonor } from '@/api/home'
 * 
 * // 方式2: 导入整个 API 模块
 * import { homeApi, authApi, userApi } from '@/api'
 * 
 * // 方式3: 使用默认导出
 * import api from '@/api'
 * api.home.getCarousel()
 * ```
 */

// ==================== 配置导出 ====================
export { USE_REAL_API, API_BASE_URL } from './request'

// ==================== 类型导出 ====================
export * from './types'

// ==================== 首页 API ====================
export * from './home'
export { homeApi } from './home'

// ==================== 用户认证 API ====================
export * from './auth'
export { authApi } from './auth'

// ==================== 用户信息 API ====================
export * from './user'
export { userApi } from './user'

// ==================== 帖子 API ====================
export * from './posts'
export { postsApi } from './posts'

// ==================== 评论 API ====================
export * from './comments'
export { commentsApi } from './comments'

// ==================== 消息中心 API ====================
export * from './messages'
export { messagesApi } from './messages'

// ==================== AI使用达人（荣誉）API ====================
export * from './honor'
export { honorApi } from './honor'

// ==================== 活动 API ====================
export * from './activities'
export { activitiesApi } from './activities'

// ==================== AI工具专区 API ====================
export * from './tools'
export { toolsApi } from './tools'

// ==================== 赋能交流 API ====================
export * from './empowerment'
export { empowermentApi } from './empowerment'

// ==================== AI优秀实践 API ====================
export * from './practices'
export { practicesApi } from './practices'

// ==================== 扶摇Agent应用 API ====================
export * from './agent'
export { agentApi } from './agent'

// ==================== 默认导出（API 对象集合）====================
import { homeApi } from './home'
import { authApi } from './auth'
import { userApi } from './user'
import { postsApi } from './posts'
import { commentsApi } from './comments'
import { messagesApi } from './messages'
import { honorApi } from './honor'
import { activitiesApi } from './activities'
import { toolsApi } from './tools'
import { empowermentApi } from './empowerment'
import { practicesApi } from './practices'
import { agentApi } from './agent'

const api = {
  home: homeApi,
  auth: authApi,
  user: userApi,
  posts: postsApi,
  comments: commentsApi,
  messages: messagesApi,
  honor: honorApi,
  activities: activitiesApi,
  tools: toolsApi,
  empowerment: empowermentApi,
  practices: practicesApi,
  agent: agentApi,
}

export default api
