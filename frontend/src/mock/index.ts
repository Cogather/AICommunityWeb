// Mock 数据 - 替代所有 API 调用
// 模拟网络延迟
const delay = (ms: number = 300) => new Promise(resolve => setTimeout(resolve, ms))

// ========== 类型定义 ==========
export interface CarouselItem {
  id: number
  image: string
  title: string
  desc: string
  link: string
  showContent: boolean
}

export interface HonorAward {
  id: number
  name: string
  desc: string
  image: string
}

export interface TopUser {
  id: number
  name: string
  avatar: string
  department: string
  level?: number
  honorCount?: number
}

export interface HonorInfo {
  bannerImage: string
  awards: HonorAward[]
  topUsers: TopUser[]
}

export interface ToolItem {
  id: number
  name: string
  desc: string
  logo: string
  color: string
  link: string
}

export interface ToolBannerItem {
  id: number
  image: string
  title: string
  desc: string
  order: number
}

export interface PracticeItem {
  id: number
  title: string
  author: string
  time: string
  category?: 'training' | 'training-battle' | 'user-exchange'
}

export interface PracticesInfo {
  training: PracticeItem[]
  trainingBattle: PracticeItem[]
  userExchange: PracticeItem[]
}

export interface Post {
  id: number
  title: string
  summary?: string
  description?: string
  content?: string
  authorName?: string
  author?: string
  authorAvatar?: string
  authorId?: number
  createTime: string | Date
  updateTime?: string | Date
  views: number
  comments: number
  likes: number
  tags?: string[]
  tag?: string
  department?: string
  cover?: string
  image?: string
  featured?: boolean
  isFeatured?: boolean
  zone?: 'practices' | 'tools' | 'agent' | 'empowerment'
  toolId?: number
  toolName?: string
  isLiked?: boolean
  isCollected?: boolean
  isAuthor?: boolean
  canEdit?: boolean
  canDelete?: boolean
}

export interface PostsResponse {
  list: Post[]
  total: number
  page: number
  pageSize: number
}

export interface Comment {
  id: number
  postId: number
  userId: number
  userName: string
  userAvatar: string
  content: string
  likes: number
  isLiked?: boolean
  isAuthor?: boolean
  isMyComment?: boolean
  canEdit?: boolean
  canDelete?: boolean
  replies?: Reply[]
  createTime: string | Date
  updateTime?: string | Date
}

export interface Reply {
  id: number
  commentId: number
  userId: number
  userName: string
  userAvatar: string
  replyToUserId?: number
  replyTo?: string
  content: string
  likes: number
  isLiked?: boolean
  replies?: Reply[]
  createTime: string | Date
}

export interface UserProfile {
  id: number
  employeeId?: string
  name: string
  avatar: string
  bio?: string
  department?: string
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

export interface Activity {
  id: number
  title: string
  content: string
  cover?: string
  toolId?: number
  toolName?: string
  type: string
  date: string | Date
  authorId: number
  authorName: string
  authorAvatar?: string
  registeredCount?: number
  status?: string
  isRegistered?: boolean
  isAuthor?: boolean
  canEdit?: boolean
  canDelete?: boolean
  createTime?: string | Date
  updateTime?: string | Date
}

export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

// 消息类型枚举
export enum MessageType {
  ACTIVITY_REGISTRATION = 'activity_registration', // 活动报名
  POST_COMMENT = 'post_comment', // 帖子评论
  COMMENT_REPLY = 'comment_reply', // 评论回复
  POST_LIKE = 'post_like', // 帖子点赞
  AWARD_NOTIFICATION = 'award_notification' // 奖项通知
}

export interface Message {
  id: number
  userId: number
  type: string
  title: string
  content: string
  link?: string
  fromUserId?: number
  fromUserName?: string
  read: boolean
  createTime: string | Date
  createdAt?: string | Date // 兼容字段
}

export interface TeamAward {
  id: number
  title: string
  year: number
  images: Array<{
    id: number
    image: string
    winnerName: string
    teamField?: string
    flowers?: number
    hasGivenFlower?: boolean
  }>
}

// ========== Mock 数据 ==========
const mockCarousel: CarouselItem[] = [
  {
    id: 1,
    image: 'https://picsum.photos/1200/400?random=1',
    title: 'AI社区欢迎您',
    desc: '探索AI技术的无限可能',
    link: '/',
    showContent: true
  },
  {
    id: 2,
    image: 'https://picsum.photos/1200/400?random=2',
    title: 'AI优秀实践',
    desc: '分享您的AI实践经验',
    link: '/practices',
    showContent: true
  },
  {
    id: 3,
    image: 'https://picsum.photos/1200/400?random=3',
    title: '工具专区',
    desc: '发现强大的AI工具',
    link: '/tools',
    showContent: true
  }
]

const mockHonorInfo: HonorInfo = {
  bannerImage: 'https://picsum.photos/800/300?random=30',
  awards: [
    { id: 1, name: '年度最佳贡献奖', desc: '2026年度', image: 'https://picsum.photos/200/150?random=31' },
    { id: 2, name: 'AI创新突破奖', desc: '2026年度', image: 'https://picsum.photos/200/150?random=32' },
    { id: 3, name: '效率提升大师', desc: '2026年度', image: 'https://picsum.photos/200/150?random=33' },
    { id: 4, name: '社区贡献奖', desc: '2026年度', image: 'https://picsum.photos/200/150?random=34' }
  ],
  topUsers: [
    { id: 1, name: '张三', avatar: 'https://picsum.photos/100/100?random=1', department: '技术部', honorCount: 5 },
    { id: 2, name: '李四', avatar: 'https://picsum.photos/100/100?random=2', department: '产品部', honorCount: 4 },
    { id: 3, name: '王五', avatar: 'https://picsum.photos/100/100?random=3', department: '设计部', honorCount: 3 }
  ]
}

const mockTools: ToolItem[] = [
  { id: 1, name: '扶摇Agent', desc: '智能AI助手', logo: '🤖', color: '#409EFF', link: '/agent' },
  { id: 2, name: 'AI工具1', desc: '强大的AI工具', logo: '⚡', color: '#67C23A', link: '/tools/1' },
  { id: 3, name: 'AI工具2', desc: '高效的AI工具', logo: '🎯', color: '#E6A23C', link: '/tools/2' }
]

const mockToolBanners: ToolBannerItem[] = [
  { id: 1, image: 'https://picsum.photos/600/200?random=10', title: '工具Banner 1', desc: '描述1', order: 1 },
  { id: 2, image: 'https://picsum.photos/600/200?random=11', title: '工具Banner 2', desc: '描述2', order: 2 }
]

const mockPractices: PracticesInfo = {
  training: [
    { id: 1, title: 'AI培训课程1', author: '张老师', time: '2026-01-15', category: 'training' },
    { id: 2, title: 'AI培训课程2', author: '李老师', time: '2026-01-20', category: 'training' }
  ],
  trainingBattle: [
    { id: 3, title: 'AI训战项目1', author: '王老师', time: '2026-01-10', category: 'training-battle' },
    { id: 4, title: 'AI训战项目2', author: '赵老师', time: '2026-01-12', category: 'training-battle' }
  ],
  userExchange: [
    { id: 5, title: '用户交流1', author: '用户A', time: '2026-01-08', category: 'user-exchange' },
    { id: 6, title: '用户交流2', author: '用户B', time: '2026-01-09', category: 'user-exchange' }
  ]
}

const mockCurrentUser: UserProfile = {
  id: 1,
  employeeId: 'E001',
  name: '当前用户',
  avatar: 'https://picsum.photos/100/100?random=user',
  bio: '这是一个测试用户',
  department: '技术部',
  postsCount: 10,
  favoritesCount: 5,
  commentsCount: 20,
  activitiesCount: 3,
  flowersCount: 15,
  points: 1000,
  roles: ['admin', 'user'] // 添加admin角色以便显示发布活动按钮
}

const mockPosts: Post[] = [
  {
    id: 1,
    title: 'AI技术实践分享',
    summary: '这是一篇关于AI技术实践的分享文章',
    description: '这是一篇关于AI技术实践的分享文章',
    content: '<p>这是文章内容</p>',
    author: '张三',
    authorName: '张三',
    authorAvatar: 'https://picsum.photos/100/100?random=1',
    authorId: 1,
    createTime: new Date(),
    views: 100,
    comments: 10,
    likes: 20,
    tags: ['AI', '实践'],
    cover: 'https://picsum.photos/800/400?random=1',
    image: 'https://picsum.photos/800/400?random=1',
    featured: false,
    zone: 'practices'
  }
]

const mockComments: Comment[] = [
  {
    id: 1,
    postId: 1,
    userId: 2,
    userName: '李四',
    userAvatar: 'https://picsum.photos/100/100?random=2',
    content: '这是一条评论',
    likes: 5,
    isLiked: false,
    createTime: new Date(),
    replies: [
      {
        id: 1,
        commentId: 1,
        userId: 1,
        userName: '张三',
        userAvatar: 'https://picsum.photos/100/100?random=1',
        content: '感谢您的评论！',
        likes: 0,
        isLiked: false,
        createTime: new Date(),
        replyTo: '李四',
        replyToId: 1
      }
    ]
  }
]

const mockActivities: Activity[] = [
  {
    id: 1,
    title: 'AI技术分享会',
    content: '活动内容',
    cover: 'https://picsum.photos/800/400?random=activity1',
    toolId: 1,
    toolName: 'TestMate',
    type: 'workshop',
    date: new Date(),
    authorId: 1,
    authorName: '张三',
    registeredCount: 10,
    status: 'upcoming',
    isRegistered: false,
    canEdit: true,
    canDelete: true
  }
]

const mockMessages: Message[] = [
  {
    id: 1,
    userId: 1,
    type: 'post_comment',
    title: '新评论',
    content: '您的帖子收到了新评论',
    read: false,
    createTime: new Date()
  }
]

const mockTeamAwards: TeamAward[] = [
  {
    id: 1,
    title: '优秀团队奖',
    year: 2026,
    images: [
      { id: 1, image: 'https://picsum.photos/300/200?random=team1', winnerName: '技术团队', flowers: 10, hasGivenFlower: false }
    ]
  }
]

// ========== Mock API 函数 ==========

// 首页相关
export const getCarousel = async (): Promise<{ list: CarouselItem[] }> => {
  await delay()
  return { list: mockCarousel }
}

export const getHonor = async (): Promise<{ honor: HonorInfo }> => {
  await delay()
  return { honor: mockHonorInfo }
}

export const getTools = async (_featured?: boolean): Promise<{ list: ToolItem[] }> => {
  await delay()
  return { list: mockTools }
}

export const getToolBanners = async (_toolId?: number): Promise<{ list: ToolBannerItem[] }> => {
  await delay()
  return { list: mockToolBanners }
}

export const getPractices = async (): Promise<PracticesInfo> => {
  await delay()
  return mockPractices
}

// 用户相关
export const getCurrentUser = async (): Promise<UserProfile> => {
  await delay()
  return mockCurrentUser
}

export const getUserProfileById = async (userId: number): Promise<UserProfile> => {
  await delay()
  return { ...mockCurrentUser, id: userId }
}

export const getUserProfileByName = async (name: string): Promise<UserProfile> => {
  await delay()
  return { ...mockCurrentUser, name }
}

export const updateUserProfile = async (data: Partial<UserProfile>): Promise<void> => {
  await delay()
  Object.assign(mockCurrentUser, data)
}

export const getUserPoints = async (): Promise<any> => {
  await delay()
  return { total: mockCurrentUser.points, details: [] }
}

export const getUserPosts = async (userId: number, params?: any): Promise<PageResult<Post>> => {
  await delay()
  return { list: mockPosts, total: mockPosts.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getUserFavorites = async (userId: number, params?: any): Promise<PageResult<Post>> => {
  await delay()
  return { list: mockPosts, total: mockPosts.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getUserComments = async (userId: number, params?: any): Promise<PageResult<Comment>> => {
  await delay()
  return { list: mockComments, total: mockComments.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getUserActivities = async (userId: number, params?: any): Promise<PageResult<Activity>> => {
  await delay()
  return { list: mockActivities, total: mockActivities.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getUserCreatedActivities = async (userId: number, params?: any): Promise<PageResult<Activity>> => {
  await delay()
  return { list: mockActivities, total: mockActivities.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

// 帖子相关
export const getPosts = async (params?: any): Promise<PostsResponse> => {
  await delay()
  return { list: mockPosts, total: mockPosts.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getPostDetail = async (id: number): Promise<Post> => {
  await delay()
  const post = mockPosts.find(p => p.id === id) || mockPosts[0]
  // 获取当前用户，判断是否是作者
  const currentUser = await getCurrentUser()
  const isAuthor = post.authorId === currentUser.id
  return { ...post, id, isAuthor }
}

export const createPost = async (data: any): Promise<Post> => {
  await delay()
  const newPost: Post = {
    id: Date.now(),
    ...data,
    author: mockCurrentUser.name,
    authorName: mockCurrentUser.name,
    authorAvatar: mockCurrentUser.avatar,
    authorId: mockCurrentUser.id,
    createTime: new Date(),
    views: 0,
    comments: 0,
    likes: 0
  }
  mockPosts.unshift(newPost)
  return newPost
}

export const updatePost = async (id: number, data: any): Promise<Post> => {
  await delay()
  const post = mockPosts.find(p => p.id === id) || mockPosts[0]
  Object.assign(post, data, { updateTime: new Date() })
  return post
}

export const deletePost = async (id: number): Promise<void> => {
  await delay()
  const index = mockPosts.findIndex(p => p.id === id)
  if (index > -1) mockPosts.splice(index, 1)
}

export const likePost = async (id: number, action: 'like' | 'unlike'): Promise<any> => {
  await delay()
  const post = mockPosts.find(p => p.id === id) || mockPosts[0]
  if (action === 'like') {
    post.likes++
    post.isLiked = true
  } else {
    post.likes = Math.max(0, post.likes - 1)
    post.isLiked = false
  }
  return { liked: post.isLiked, likes: post.likes }
}

export const collectPost = async (id: number, action: 'collect' | 'uncollect'): Promise<any> => {
  await delay()
  const post = mockPosts.find(p => p.id === id) || mockPosts[0]
  post.isCollected = action === 'collect'
  return { collected: post.isCollected }
}

export const getRecommendedCovers = async (): Promise<any[]> => {
  await delay()
  return [
    { id: 1, url: 'https://picsum.photos/800/400?random=cover1' },
    { id: 2, url: 'https://picsum.photos/800/400?random=cover2' }
  ]
}

export const saveDraft = async (_data: any): Promise<any> => {
  await delay()
  return { draftId: 'draft_' + Date.now(), savedAt: new Date().toISOString(), message: '保存成功' }
}

export const getPostComments = async (postId: number, params?: any): Promise<PageResult<Comment>> => {
  await delay()
  return { list: mockComments, total: mockComments.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const createComment = async (postId: number, data: any): Promise<Comment> => {
  await delay()
  const newComment: Comment = {
    id: Date.now(),
    postId,
    userId: mockCurrentUser.id,
    userName: mockCurrentUser.name,
    userAvatar: mockCurrentUser.avatar,
    content: data.content,
    likes: 0,
    createTime: new Date(),
    replies: []
  }
  mockComments.unshift(newComment)
  return newComment
}

// 评论相关
export const likeComment = async (id: number, action: 'like' | 'unlike'): Promise<any> => {
  await delay()
  const comment = mockComments.find(c => c.id === id) || mockComments[0]
  if (action === 'like') {
    comment.likes++
    comment.isLiked = true
  } else {
    comment.likes = Math.max(0, comment.likes - 1)
    comment.isLiked = false
  }
  return { liked: comment.isLiked, likes: comment.likes }
}

export const updateComment = async (id: number, data: any): Promise<void> => {
  await delay()
  const comment = mockComments.find(c => c.id === id) || mockComments[0]
  comment.content = data.content
  comment.updateTime = new Date()
}

export const deleteComment = async (id: number): Promise<void> => {
  await delay()
  const index = mockComments.findIndex(c => c.id === id)
  if (index > -1) mockComments.splice(index, 1)
}

// 回复相关
export const deleteReply = async (_id: number): Promise<void> => {
  await delay()
  // Mock implementation
}

// 活动相关
export const getActivities = async (params?: any): Promise<PageResult<Activity>> => {
  await delay()
  return { list: mockActivities, total: mockActivities.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getActivityDetail = async (id: number): Promise<Activity> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id) || mockActivities[0]
  // 确保返回完整的活动数据，包括工具信息和权限
  return {
    ...activity,
    id,
    toolName: activity.toolName || (activity.toolId ? `工具${activity.toolId}` : ''),
    canEdit: activity.canEdit !== undefined ? activity.canEdit : true,
    canDelete: activity.canDelete !== undefined ? activity.canDelete : true
  }
}

export const createActivity = async (data: any): Promise<Activity> => {
  await delay()
  const newActivity: Activity = {
    id: Date.now(),
    ...data,
    authorId: mockCurrentUser.id,
    authorName: mockCurrentUser.name,
    authorAvatar: mockCurrentUser.avatar,
    registeredCount: 0,
    status: 'upcoming',
    createTime: new Date()
  }
  mockActivities.unshift(newActivity)
  return newActivity
}

export const updateActivity = async (id: number, data: any): Promise<Activity> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id) || mockActivities[0]
  Object.assign(activity, data, { updateTime: new Date() })
  return activity
}

export const deleteActivity = async (id: number): Promise<void> => {
  await delay()
  const index = mockActivities.findIndex(a => a.id === id)
  if (index > -1) mockActivities.splice(index, 1)
}

export const registerActivity = async (id: number): Promise<any> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id) || mockActivities[0]
  activity.isRegistered = true
  activity.registeredCount = (activity.registeredCount || 0) + 1
  return { registered: true, registeredCount: activity.registeredCount }
}

export const cancelRegistration = async (id: number): Promise<any> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id) || mockActivities[0]
  activity.isRegistered = false
  activity.registeredCount = Math.max(0, (activity.registeredCount || 0) - 1)
  return { registered: false, registeredCount: activity.registeredCount }
}

export const getRegistrations = async (id: number, params?: any): Promise<PageResult<any>> => {
  await delay()
  return { list: [], total: 0, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

// 消息相关
export const getMessages = async (params?: any): Promise<PageResult<Message>> => {
  await delay()
  return { list: mockMessages, total: mockMessages.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const markMessageAsRead = async (id: number): Promise<void> => {
  await delay()
  const message = mockMessages.find(m => m.id === id)
  if (message) message.read = true
}

export const markAllMessagesAsRead = async (): Promise<void> => {
  await delay()
  mockMessages.forEach(m => m.read = true)
}

export const deleteMessage = async (id: number): Promise<void> => {
  await delay()
  const index = mockMessages.findIndex(m => m.id === id)
  if (index > -1) mockMessages.splice(index, 1)
}

export const getUnreadMessageCount = async (): Promise<any> => {
  await delay()
  return { count: mockMessages.filter(m => !m.read).length }
}

// 荣誉相关
export const getHonors = async (params?: any): Promise<PageResult<any>> => {
  await delay()
  return { list: [], total: 0, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getHonorInfluence = async (): Promise<any> => {
  await delay()
  return { totalHonors: 0, totalUsers: 0, totalFlowers: 0, categories: [] }
}

export const getTopUsers = async (_params?: any): Promise<PageResult<TopUser>> => {
  await delay()
  return { list: mockHonorInfo.topUsers, total: mockHonorInfo.topUsers.length, page: 1, pageSize: 10 }
}

export const giveFlower = async (_id: number): Promise<any> => {
  await delay()
  return { flowers: 1, hasGivenFlower: true }
}

// 工具相关
export const getToolDetail = async (id: number): Promise<any> => {
  await delay()
  return mockTools.find(t => t.id === id) || mockTools[0]
}

export const checkToolOwner = async (id: number): Promise<any> => {
  await delay()
  // 在mock环境中，默认返回isOwner为true，以便测试发布活动功能
  return { isOwner: true, toolId: id }
}

// Agent相关
export const getFeaturedPost = async (): Promise<any> => {
  await delay()
  return { post: mockPosts[0] || null }
}

// 用户达人相关
export const getTeamAwards = async (_year?: string): Promise<{ list: TeamAward[] }> => {
  await delay()
  return { list: mockTeamAwards }
}

export const getTeamAwardDetail = async (id: number): Promise<TeamAward> => {
  await delay()
  return mockTeamAwards.find(a => a.id === id) || mockTeamAwards[0]
}

// 管理平台相关
export const getCarouselConfig = async (): Promise<{ list: CarouselItem[] }> => {
  await delay()
  return { list: mockCarousel }
}

export const saveCarouselConfig = async (list: CarouselItem[]): Promise<void> => {
  await delay()
  mockCarousel.length = 0
  mockCarousel.push(...list)
}

export const getHonorBannerConfig = async (): Promise<any> => {
  await delay()
  return { bannerImage: mockHonorInfo.bannerImage }
}

export const saveHonorBannerConfig = async (config: any): Promise<void> => {
  await delay()
  mockHonorInfo.bannerImage = config.bannerImage
}

export const getHonorAwardsConfig = async (): Promise<{ list: HonorAward[] }> => {
  await delay()
  return { list: mockHonorInfo.awards }
}

export const saveHonorAwardsConfig = async (list: HonorAward[]): Promise<void> => {
  await delay()
  mockHonorInfo.awards = list
}

export const getToolsConfig = async (): Promise<{ list: ToolItem[] }> => {
  await delay()
  return { list: mockTools }
}

export const saveToolsConfig = async (list: ToolItem[]): Promise<void> => {
  await delay()
  mockTools.length = 0
  mockTools.push(...list)
}

export const getToolBannersConfig = async (): Promise<{ list: ToolBannerItem[] }> => {
  await delay()
  return { list: mockToolBanners }
}

export const saveToolBannersConfig = async (list: ToolBannerItem[]): Promise<void> => {
  await delay()
  mockToolBanners.length = 0
  mockToolBanners.push(...list)
}

export const getPersonalAwardsConfig = async (): Promise<{ list: any[] }> => {
  await delay()
  return { list: [] }
}

export const savePersonalAwardsConfig = async (_list: any[]): Promise<void> => {
  await delay()
}

export const getWinnersConfig = async (): Promise<{ list: any[] }> => {
  await delay()
  return { list: [] }
}

export const saveWinnersConfig = async (_list: any[]): Promise<void> => {
  await delay()
}

export const getTeamAwardsConfig = async (): Promise<{ list: TeamAward[] }> => {
  await delay()
  return { list: mockTeamAwards }
}

export const saveTeamAwardsConfig = async (list: TeamAward[]): Promise<void> => {
  await delay()
  mockTeamAwards.length = 0
  mockTeamAwards.push(...list)
}

export const getRecommendedWinners = async (month?: string, _limit: number = 3): Promise<{ list: any[]; month?: string }> => {
  await delay()
  return { list: [], month }
}

export const setUserAward = async (_params: any): Promise<{ id: number; message?: string }> => {
  await delay()
  return { id: Date.now(), message: '设置成功' }
}

export const cancelUserAward = async (_id: number): Promise<void> => {
  await delay()
}

export const getAwardsList = async (_category?: string): Promise<{ list: any[] }> => {
  await delay()
  return { list: [] }
}

export const searchUsers = async (_params: any): Promise<{ list: any[] }> => {
  await delay()
  return { list: [] }
}

export const getUsersList = async (_params?: any): Promise<{ list: any[]; total: number }> => {
  await delay()
  return { list: [], total: 0 }
}

export const addUserRole = async (_userId: number, _params: any): Promise<void> => {
  await delay()
}

export const removeUserRole = async (_userId: number, _params: any): Promise<void> => {
  await delay()
}

export const uploadImage = async (file: File): Promise<{ url: string }> => {
  await delay()
  return { url: URL.createObjectURL(file) }
}

// 其他
export const getHotPosts = async (_params?: any): Promise<{ list: Post[] }> => {
  await delay()
  return { list: mockPosts.slice(0, 10) }
}

export const getTags = async (_params: any): Promise<{ list: any[] }> => {
  await delay()
  return { list: [] }
}

export const getDepartments = async (_params?: any): Promise<{ list: any[] }> => {
  await delay()
  return { list: [] }
}

export const getTopContributors = async (_params?: any): Promise<{ list: any[] }> => {
  await delay()
  return { list: [] }
}

export const getAwardRules = async (id: number): Promise<any> => {
  await delay()
  return { awardId: id, awardName: '奖项名称', rules: '奖项规则' }
}

export const login = async (_data: any): Promise<any> => {
  await delay()
  return { token: 'mock_token', user: mockCurrentUser }
}

export const logout = async (): Promise<void> => {
  await delay()
}
