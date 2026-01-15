// ==================== 通用类型 ====================

/** API 响应结构 */
export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
}

/** 分页参数 */
export interface PaginationParams {
  page?: number
  pageSize?: number
}

/** 分页响应 */
export interface PaginatedData<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
  totalPages?: number
}

// ==================== 首页类型 ====================

/** 轮播图项 */
export interface CarouselItem {
  id: number
  image: string
  title?: string
  desc?: string
  link?: string
  showContent: boolean
  order?: number
}

/** 荣誉殿堂奖项 */
export interface HonorAward {
  id: number
  name: string
  desc?: string
  image?: string
}

/** 荣誉殿堂数据 */
export interface HonorData {
  bannerImage: string
  awards: HonorAward[]
}

/** 最新获奖者 */
export interface LatestWinner {
  userId: string
  userName: string
  avatar?: string
  awardName: string
}

/** 赋能交流帖子（首页简略版） */
export interface EmpowermentPost {
  id: number | string
  title: string
  tag: string
  tagType?: 'blue' | 'green' | 'orange' | 'red' | 'purple'
  userName?: string
  time: string
  views?: number
}

/** AI 优秀实践帖子（首页简略版） */
export interface PracticePost {
  id: number | string
  title: string
  userName: string
  time: string
  category: 'training' | 'training-battle' | 'user-exchange'
}

/** AI 优秀实践数据 */
export interface PracticesData {
  practices: Record<string, PracticePost[]>
}

/** 新闻资讯项 */
export interface NewsItem {
  id: number
  title: string
  summary?: string
  image?: string
  link?: string
  time: string
  source?: string
}

/** 悬浮工具平台项 */
export interface ToolPlatformItem {
  id: number
  name: string
  desc?: string
  logo?: string
  color?: string
  platformUrl: string
}

/** AI 工具专区项 */
export interface ToolItem {
  id: number
  name: string
  desc?: string
  logo?: string
  color?: string
  link?: string
}

/** 工具专区轮播图 */
export interface ToolBannerItem {
  id: number
  image: string
  title?: string
  desc?: string
  order?: number
}

// ==================== 用户类型 ====================

/** 部门信息 */
export interface DepartmentInfo {
  id: number
  name: string
  level: number
}

/** 用户信息 */
export interface UserProfile {
  userId: string
  userName: string
  employeeId?: string // 保留以兼容，但主要使用 userId
  avatar: string
  bio?: string
  department?: string
  departments?: {
    level1?: DepartmentInfo
    level2?: DepartmentInfo
    level3?: DepartmentInfo
    level4?: DepartmentInfo
    level5?: DepartmentInfo
    level6?: DepartmentInfo
  }
  postsCount: number
  favoritesCount: number
  commentsCount: number
  activitiesCount: number
  flowersCount: number
  points: number
  roles?: string[]
  ownedTools?: Array<{
    toolId: number
    toolName: string
  }>
}

/** 登录参数 */
export interface LoginParams {
  userId: string
  password: string
}

/** 登录响应 */
export interface LoginResponse {
  token: string
  expiresIn: number
  user: UserProfile
}

// ==================== 帖子类型 ====================

/** 帖子基础信息 */
export interface Post {
  id: number | string
  title: string
  description?: string
  summary?: string
  content?: string
  cover?: string
  image?: string
  userId: string
  userName?: string
  authorAvatar?: string
  department?: string
  zone?: 'practices' | 'tools' | 'agent' | 'empowerment'
  toolId?: number
  toolName?: string
  category?: 'guide' | 'excellent'
  tags?: string[]
  views: number
  comments: number
  likes: number
  isLiked?: boolean
  isCollected?: boolean
  isAuthor?: boolean
  canEdit?: boolean
  canDelete?: boolean
  featured?: boolean
  createTime: string
  createTimeDisplay?: string
  updateTime?: string
}

/** 帖子详情 */
export interface PostDetail extends Post {
  content: string
}

// ==================== 评论类型 ====================

/** 回复 */
export interface Reply {
  id: number
  commentId: number
  userId: string
  userName: string
  userAvatar?: string
  content: string
  replyTo?: string
  replyToUserId?: string
  replyToId?: number
  likes: number
  createTime: string
}

/** 评论 */
export interface Comment {
  id: number
  postId: number | string
  postTitle?: string
  userId: string
  userName: string
  userAvatar?: string
  content: string
  likes: number
  isLiked?: boolean
  isAuthor?: boolean
  isMyComment?: boolean
  replyTo?: string
  replyToId?: number
  createTime: string
  updateTime?: string
  replies?: Reply[]
}

// ==================== 活动类型 ====================

/** 活动类型 */
export type ActivityType = 'activity' | 'training' | 'workshop' | 'empowerment'

/** 活动状态 */
export type ActivityStatus = 'upcoming' | 'ongoing' | 'ended'

/** 活动 */
export interface Activity {
  id: number
  toolId: number
  toolName?: string
  type: ActivityType
  title: string
  content?: string
  cover?: string
  date: string
  startTime?: string
  endTime?: string
  location?: string
  meetingLink?: string
  speaker?: string
  speakerTitle?: string
  speakerAvatar?: string
  maxParticipants?: number
  currentParticipants?: number
  status: ActivityStatus
  isJoined?: boolean
  createTime?: string
  userId?: string
  userName?: string
}

// ==================== 标签类型 ====================

/** 标签统计 */
export interface TagStat {
  name: string
  count: number
}

// ==================== 消息类型 ====================

/** 消息类型 */
export type MessageType = 
  | 'activity_registration'
  | 'post_comment'
  | 'comment_reply'
  | 'post_like'
  | 'award_notification'

/** 消息 */
export interface Message {
  id: number
  type: MessageType
  title: string
  content: string
  relatedId?: number
  relatedType?: 'post' | 'activity' | 'award'
  commentId?: number
  replyId?: number
  userId?: string
  userName?: string
  read: boolean
  createdAt: string
  link?: string
}

// ==================== 荣誉类型 ====================

/** 个人荣誉 */
export interface HonorRecord {
  id: number
  userName: string
  department: string
  avatar?: string
  awardName: string
  awardDate: string
  category: 'innovation' | 'efficiency' | 'practice' | 'community'
  year: string
  isMine?: boolean
  flowers: number
  hasGivenFlower: boolean
  achievement?: string
}

/** 团队奖项图片 */
export interface TeamAwardImage {
  id: number
  image: string
  imageType: 'url' | 'upload'
  winnerName: string
  teamField?: string
  story?: string
  flowers: number
  hasGivenFlower: boolean
}

/** 团队奖项 */
export interface TeamAward {
  id: number
  title: string
  year: string
  images: TeamAwardImage[]
}

/** 排行榜用户 */
export interface LeaderboardUser {
  userName: string
  department: string
  avatar?: string
  count: number
  totalFlowers: number
}

// ==================== 帖子草稿类型 ====================

/** 推荐封面 */
export interface RecommendedCover {
  id: number
  url: string
  category?: string
}

/** 草稿数据 */
export interface DraftData {
  title?: string
  content?: string
  zone?: string
  toolId?: number
  tags?: string[]
  cover?: string
}

/** 草稿响应 */
export interface DraftResponse {
  id?: number
  data: DraftData | null
  updateTime?: string
}

// ==================== 报名类型 ====================

/** 活动报名记录 */
export interface Registration {
  id: number
  activityId: number
  userId: string
  userName: string
  userAvatar?: string
  department?: string
  registerTime: string
  status: 'registered' | 'cancelled' | 'attended'
}
