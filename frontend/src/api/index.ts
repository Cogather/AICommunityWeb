/**
 * API 统一入口
 * 
 * 使用方式:
 * 1. 在 request.ts 中设置 DEBUG_MODE_ENABLED = true 使用真实后端
 * 2. 在 request.ts 中设置 DEBUG_MODE_ENABLED = false 使用 mock 数据
 * 
 * 示例:
 * ```typescript
 * // 方式1: 导入整个 API 模块
 * import { homeApi, authApi, userApi } from '@/api'
 * 
 * // 方式2: 使用默认导出
 * import api from '@/api'
 * api.home.getCarousel()
 * ```
 */

// ==================== 配置导出 ====================
export { useRealApi, apiBaseUrl } from './request'

// ==================== 类型导出 ====================
export type {
  ApiResponse,
  PaginationParams,
  PaginatedData,
  CarouselItem,
  HonorAward,
  HonorData,
  LatestWinner,
  EmpowermentPost,
  PracticePost,
  PracticesData,
  ToolPlatformItem,
  ToolItem,
  ToolBannerItem,
  DepartmentInfo,
  UserProfile,
  LoginParams,
  LoginResponse,
  Post,
  PostDetail,
  Reply,
  Comment,
  ActivityType,
  ActivityStatus,
  Activity,
  TagStat,
  MessageType,
  Message,
  HonorRecord,
  TeamAwardImage,
  TeamAward,
  LeaderboardUser,
} from './types'

// ==================== API 模块导出 ====================
export { homeApi } from './home'
export { authApi } from './auth'
export { userApi } from './user'
export { postsApi } from './posts'
export { commentsApi } from './comments'
export { messagesApi } from './messages'
export { honorApi } from './honor'
export { activitiesApi } from './activities'
export { toolsApi } from './tools'
export { empowermentApi } from './empowerment'
export { practicesApi } from './practices'
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
