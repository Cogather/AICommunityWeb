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
  order?: number
  imageType?: 'url' | 'upload'
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
  order?: number
  imageType?: 'url' | 'upload'
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
  category?: 'guide' | 'excellent'  // 帖子分类：操作指导/优秀使用
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
  replyToId?: number  // 被回复的回复ID
  replyTo?: string    // 被回复者名称
  content: string
  likes: number
  isLiked?: boolean
  replies?: Reply[]
  createTime: string | Date
}

// 部门信息接口
export interface DepartmentInfo {
  id: number
  name: string
  level: number  // 1-6级部门
}

export interface UserProfile {
  id: number
  employeeId?: string    // 工号
  name: string           // 姓名
  avatar: string         // 头像URL
  bio?: string           // 个人简介
  department?: string    // 部门名称（向下兼容）
  // 多级部门信息
  departments?: {
    level1?: DepartmentInfo  // 一级部门
    level2?: DepartmentInfo  // 二级部门
    level3?: DepartmentInfo  // 三级部门
    level4?: DepartmentInfo  // 四级部门
    level5?: DepartmentInfo  // 五级部门
    level6?: DepartmentInfo  // 六级部门
  }
  postsCount: number
  favoritesCount: number
  commentsCount: number
  activitiesCount: number
  flowersCount: number
  points: number
  roles?: string[]       // 用户角色：admin-管理员, user-普通用户, tool_owner-工具Owner
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
  toolId?: number | null
  toolName?: string
  type: 'activity' | 'training' | 'workshop'
  date: string | Date
  location?: string
  meetingLink?: string
  authorId: number
  authorName: string
  authorAvatar?: string
  registeredCount?: number
  maxRegistrations?: number
  status?: 'upcoming' | 'ongoing' | 'ended'
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
  relatedId?: number // 相关ID（如帖子ID、活动ID等）
  relatedType?: string // 相关类型（如'post', 'activity', 'comment'等）
  commentId?: number // 评论ID（用于定位到具体评论，POST_COMMENT和COMMENT_REPLY类型使用）
  replyId?: number // 回复ID（用于定位到具体回复，COMMENT_REPLY类型使用）
  fromUserId?: number
  fromUserName?: string
  read: boolean
  createTime: string | Date
  createdAt?: string | Date // 兼容字段（与utils/message.ts一致）
}

export interface TeamAward {
  id: number
  title: string
  year?: number
  images: Array<{
    id: number
    image: string
    winnerName: string
    imageType?: 'url' | 'upload'
    teamField?: string
    story?: string  // 获奖事迹（HTML富文本）
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
  { id: 1, name: 'TestMate', desc: '自动化测试助手', logo: '🧪', color: '#36cfc9', link: '/tools?toolId=1' },
  { id: 2, name: 'CodeMate', desc: '智能代码补全', logo: '💻', color: '#9254de', link: '/tools?toolId=2' },
  { id: 3, name: '云集', desc: '云端计算集群', logo: '☁️', color: '#597ef7', link: '/tools?toolId=3' },
  { id: 4, name: '云见', desc: '智能监控平台', logo: '👁️', color: '#ff9c6e', link: '/tools?toolId=4' },
  { id: 5, name: '扶摇', desc: 'Agent编排引擎', logo: '🚀', color: '#4096ff', link: '/tools?toolId=5' }
  // 注意："其他工具"按钮在ToolsView.vue模板中硬编码，不需要在这里定义
]

const mockToolBanners: ToolBannerItem[] = [
  { id: 1, image: 'https://picsum.photos/600/200?random=10', title: '工具Banner 1', desc: '描述1', order: 1 },
  { id: 2, image: 'https://picsum.photos/600/200?random=11', title: '工具Banner 2', desc: '描述2', order: 2 }
]

// 新闻数据
export interface NewsItem {
  id: number
  title: string
  date: string
  image: string
  link: string
}

const mockNews: NewsItem[] = [
  {
    id: 1,
    title: '【大模型专题】多模态模型在医疗影像中的最新应用突破',
    date: '刚刚',
    image: 'https://picsum.photos/300/200?random=20',
    link: '/news'
  },
  {
    id: 2,
    title: '【社区活动】2026 AI 开发者大会早鸟票开启预售',
    date: '1小时前',
    image: 'https://picsum.photos/300/200?random=21',
    link: '/news'
  },
  {
    id: 3,
    title: '【开源动态】轻量级 LLM 本地部署最佳实践指南',
    date: '昨天',
    image: 'https://picsum.photos/300/200?random=22',
    link: '/news'
  },
  {
    id: 4,
    title: '【深度解析】Agent 自主智能体的未来发展趋势',
    date: '昨天',
    image: 'https://picsum.photos/300/200?random=23',
    link: '/news'
  }
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
  department: '技术部/AI研发中心/智能应用组',  // 完整部门路径
  departments: {
    level1: { id: 100, name: '技术部', level: 1 },
    level2: { id: 110, name: 'AI研发中心', level: 2 },
    level3: { id: 111, name: '智能应用组', level: 3 }
  },
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
    createTime: '2026-01-10',
    updateTime: '2026-01-10',
    views: 100,
    comments: 10,
    likes: 20,
    tags: ['AI', '实践'],
    tag: 'AI',
    department: '技术部',
    cover: 'https://picsum.photos/800/400?random=1',
    image: 'https://picsum.photos/800/400?random=1',
    featured: false,
    zone: 'practices'
  },
  // 扶摇Agent应用帖子
  {
    id: 101,
    title: '使用扶摇Agent实现智能代码生成',
    summary: '分享如何使用扶摇Agent编排引擎实现智能代码生成功能，提升开发效率。',
    description: '分享如何使用扶摇Agent编排引擎实现智能代码生成功能，提升开发效率。',
    content: '<p>本文详细介绍如何利用扶摇Agent的智能编排能力实现代码自动生成...</p>',
    author: '张工程师',
    authorName: '张工程师',
    authorAvatar: 'https://picsum.photos/100/100?random=101',
    authorId: 1,
    createTime: '2026-01-12',
    updateTime: '2026-01-12',
    views: 890,
    comments: 45,
    likes: 98,
    tags: ['Agent应用', '代码生成'],
    tag: 'Agent应用',
    department: '技术部',
    cover: 'https://picsum.photos/800/400?random=21',
    image: 'https://picsum.photos/800/400?random=21',
    featured: true,
    isFeatured: true,
    zone: 'agent',
    toolId: -1,
    toolName: '扶摇Agent'
  },
  {
    id: 102,
    title: '扶摇工作流编排实战案例',
    summary: '通过实际案例展示如何利用扶摇Agent编排复杂的工作流程。',
    description: '通过实际案例展示如何利用扶摇Agent编排复杂的工作流程。',
    content: '<p>工作流编排是扶摇Agent的核心能力之一...</p>',
    author: '李开发者',
    authorName: '李开发者',
    authorAvatar: 'https://picsum.photos/100/100?random=102',
    authorId: 2,
    createTime: '2026-01-10',
    updateTime: '2026-01-10',
    views: 720,
    comments: 32,
    likes: 75,
    tags: ['工作流', '实战案例'],
    tag: '工作流',
    department: '产品部',
    cover: 'https://picsum.photos/800/400?random=22',
    image: 'https://picsum.photos/800/400?random=22',
    featured: false,
    zone: 'agent',
    toolId: -1,
    toolName: '扶摇Agent'
  },
  {
    id: 103,
    title: 'Agent自动化测试实践',
    summary: '介绍如何使用扶摇Agent进行自动化测试，提高测试效率和覆盖率。',
    description: '介绍如何使用扶摇Agent进行自动化测试，提高测试效率和覆盖率。',
    content: '<p>自动化测试是保证代码质量的重要手段...</p>',
    author: '王测试',
    authorName: '王测试',
    authorAvatar: 'https://picsum.photos/100/100?random=103',
    authorId: 3,
    createTime: '2026-01-08',
    updateTime: '2026-01-08',
    views: 650,
    comments: 28,
    likes: 62,
    tags: ['自动化', '测试'],
    tag: '自动化',
    department: '质量部',
    cover: 'https://picsum.photos/800/400?random=23',
    image: 'https://picsum.photos/800/400?random=23',
    featured: false,
    zone: 'agent',
    toolId: -1,
    toolName: '扶摇Agent'
  },
  {
    id: 104,
    title: '智能编排最佳实践',
    summary: '总结扶摇Agent智能编排的最佳实践和注意事项。',
    description: '总结扶摇Agent智能编排的最佳实践和注意事项。',
    content: '<p>智能编排需要遵循一些基本原则...</p>',
    author: '陈架构师',
    authorName: '陈架构师',
    authorAvatar: 'https://picsum.photos/100/100?random=104',
    authorId: 4,
    createTime: '2026-01-06',
    updateTime: '2026-01-06',
    views: 580,
    comments: 25,
    likes: 48,
    tags: ['智能编排', '最佳实践'],
    tag: '智能编排',
    department: '技术部',
    cover: 'https://picsum.photos/800/400?random=24',
    image: 'https://picsum.photos/800/400?random=24',
    featured: false,
    zone: 'agent',
    toolId: -1,
    toolName: '扶摇Agent'
  },
  {
    id: 105,
    title: '扶摇Agent在企业级应用中的应用',
    summary: '分享扶摇Agent在企业级应用中的实际应用案例和经验。',
    description: '分享扶摇Agent在企业级应用中的实际应用案例和经验。',
    content: '<p>企业级应用对稳定性和可扩展性有更高要求...</p>',
    author: '赵医生',
    authorName: '赵医生',
    authorAvatar: 'https://picsum.photos/100/100?random=105',
    authorId: 5,
    createTime: '2026-01-05',
    updateTime: '2026-01-05',
    views: 520,
    comments: 20,
    likes: 38,
    tags: ['案例分享', '企业应用'],
    tag: '案例分享',
    department: '业务部',
    cover: 'https://picsum.photos/800/400?random=25',
    image: 'https://picsum.photos/800/400?random=25',
    featured: false,
    zone: 'agent',
    toolId: -1,
    toolName: '扶摇Agent'
  },
  // TestMate工具帖子 - 操作指导类
  {
    id: 201,
    title: 'TestMate快速入门指南',
    summary: '本文详细介绍如何快速上手使用TestMate进行自动化测试，从安装配置到编写第一个测试用例。',
    description: '本文详细介绍如何快速上手使用TestMate进行自动化测试，从安装配置到编写第一个测试用例。',
    content: '<p>TestMate是一款强大的自动化测试助手...</p><h2>安装配置</h2><p>首先需要安装TestMate插件...</p>',
    author: '张测试',
    authorName: '张测试',
    authorAvatar: 'https://picsum.photos/100/100?random=201',
    authorId: 10,
    createTime: '2026-01-12',
    updateTime: '2026-01-12',
    views: 1580,
    comments: 45,
    likes: 120,
    tags: ['新手', '入门', '配置'],
    tag: '新手',
    department: '测试部',
    cover: 'https://picsum.photos/800/400?random=201',
    image: 'https://picsum.photos/800/400?random=201',
    featured: false,
    isFeatured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'guide'
  },
  {
    id: 202,
    title: 'TestMate接口测试完全指南',
    summary: '深入讲解TestMate的接口测试功能，包括RESTful API测试、GraphQL测试和Mock数据管理。',
    description: '深入讲解TestMate的接口测试功能，包括RESTful API测试、GraphQL测试和Mock数据管理。',
    content: '<p>接口测试是保证系统稳定性的重要环节...</p>',
    author: '李工程师',
    authorName: '李工程师',
    authorAvatar: 'https://picsum.photos/100/100?random=202',
    authorId: 11,
    createTime: '2026-01-11',
    updateTime: '2026-01-11',
    views: 1250,
    comments: 38,
    likes: 95,
    tags: ['接口测试', 'API', '进阶'],
    tag: '进阶',
    department: '研发部',
    cover: 'https://picsum.photos/800/400?random=202',
    image: 'https://picsum.photos/800/400?random=202',
    featured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'guide'
  },
  {
    id: 203,
    title: 'TestMate UI自动化测试教程',
    summary: '学习如何使用TestMate进行UI自动化测试，包括元素定位、交互操作和断言验证。',
    description: '学习如何使用TestMate进行UI自动化测试，包括元素定位、交互操作和断言验证。',
    content: '<p>UI自动化测试可以大幅提升测试效率...</p>',
    author: '王自动化',
    authorName: '王自动化',
    authorAvatar: 'https://picsum.photos/100/100?random=203',
    authorId: 12,
    createTime: '2026-01-10',
    updateTime: '2026-01-10',
    views: 980,
    comments: 28,
    likes: 76,
    tags: ['UI测试', '自动化', '元素定位'],
    tag: '自动化',
    department: '测试部',
    cover: 'https://picsum.photos/800/400?random=203',
    image: 'https://picsum.photos/800/400?random=203',
    featured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'guide'
  },
  {
    id: 204,
    title: 'TestMate性能测试配置详解',
    summary: '详细介绍TestMate的性能测试模块，包括压力测试、负载测试和性能监控配置。',
    description: '详细介绍TestMate的性能测试模块，包括压力测试、负载测试和性能监控配置。',
    content: '<p>性能测试是保证系统可用性的关键...</p>',
    author: '陈性能',
    authorName: '陈性能',
    authorAvatar: 'https://picsum.photos/100/100?random=204',
    authorId: 13,
    createTime: '2026-01-09',
    updateTime: '2026-01-09',
    views: 850,
    comments: 22,
    likes: 65,
    tags: ['性能测试', '压力测试', '监控'],
    tag: '性能测试',
    department: '测试部',
    cover: 'https://picsum.photos/800/400?random=204',
    image: 'https://picsum.photos/800/400?random=204',
    featured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'guide'
  },
  // TestMate工具帖子 - 优秀使用类
  {
    id: 205,
    title: '使用TestMate将回归测试时间缩短80%',
    summary: '分享我们团队如何利用TestMate的智能测试选择功能，将回归测试时间从4小时缩短到45分钟。',
    description: '分享我们团队如何利用TestMate的智能测试选择功能，将回归测试时间从4小时缩短到45分钟。',
    content: '<p>在引入TestMate之前，我们的回归测试需要4个小时...</p>',
    author: '赵效率',
    authorName: '赵效率',
    authorAvatar: 'https://picsum.photos/100/100?random=205',
    authorId: 14,
    createTime: '2026-01-08',
    updateTime: '2026-01-08',
    views: 2150,
    comments: 68,
    likes: 185,
    tags: ['最佳实践', '效率提升', '回归测试'],
    tag: '最佳实践',
    department: '研发部',
    cover: 'https://picsum.photos/800/400?random=205',
    image: 'https://picsum.photos/800/400?random=205',
    featured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'excellent'
  },
  {
    id: 206,
    title: 'TestMate在微服务架构中的实践',
    summary: '探讨如何在微服务架构下使用TestMate进行端到端测试和服务间契约测试。',
    description: '探讨如何在微服务架构下使用TestMate进行端到端测试和服务间契约测试。',
    content: '<p>微服务架构带来了新的测试挑战...</p>',
    author: '钱架构',
    authorName: '钱架构',
    authorAvatar: 'https://picsum.photos/100/100?random=206',
    authorId: 15,
    createTime: '2026-01-07',
    updateTime: '2026-01-07',
    views: 1680,
    comments: 52,
    likes: 145,
    tags: ['微服务', '契约测试', '案例'],
    tag: '案例',
    department: '技术部',
    cover: 'https://picsum.photos/800/400?random=206',
    image: 'https://picsum.photos/800/400?random=206',
    featured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'excellent'
  },
  {
    id: 207,
    title: 'TestMate与CI/CD流水线集成最佳实践',
    summary: '详细介绍如何将TestMate无缝集成到Jenkins、GitLab CI等CI/CD流水线中。',
    description: '详细介绍如何将TestMate无缝集成到Jenkins、GitLab CI等CI/CD流水线中。',
    content: '<p>持续集成和持续部署需要可靠的自动化测试...</p>',
    author: '孙DevOps',
    authorName: '孙DevOps',
    authorAvatar: 'https://picsum.photos/100/100?random=207',
    authorId: 16,
    createTime: '2026-01-06',
    updateTime: '2026-01-06',
    views: 1420,
    comments: 45,
    likes: 125,
    tags: ['CI/CD', 'Jenkins', 'DevOps'],
    tag: 'DevOps',
    department: '运维部',
    cover: 'https://picsum.photos/800/400?random=207',
    image: 'https://picsum.photos/800/400?random=207',
    featured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'excellent'
  },
  {
    id: 208,
    title: '基于TestMate的测试数据管理方案',
    summary: '分享我们如何利用TestMate的数据驱动测试功能，实现测试数据的统一管理和复用。',
    description: '分享我们如何利用TestMate的数据驱动测试功能，实现测试数据的统一管理和复用。',
    content: '<p>测试数据管理是自动化测试的重要一环...</p>',
    author: '周数据',
    authorName: '周数据',
    authorAvatar: 'https://picsum.photos/100/100?random=208',
    authorId: 17,
    createTime: '2026-01-05',
    updateTime: '2026-01-05',
    views: 920,
    comments: 32,
    likes: 88,
    tags: ['数据驱动', '测试数据', '技巧'],
    tag: '技巧',
    department: '测试部',
    cover: 'https://picsum.photos/800/400?random=208',
    image: 'https://picsum.photos/800/400?random=208',
    featured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'excellent'
  },
  {
    id: 209,
    title: 'TestMate助力移动端App测试自动化',
    summary: '分享使用TestMate进行iOS和Android App自动化测试的经验和踩坑记录。',
    description: '分享使用TestMate进行iOS和Android App自动化测试的经验和踩坑记录。',
    content: '<p>移动端测试有其特殊的挑战...</p>',
    author: '吴移动',
    authorName: '吴移动',
    authorAvatar: 'https://picsum.photos/100/100?random=209',
    authorId: 18,
    createTime: '2026-01-04',
    updateTime: '2026-01-04',
    views: 1120,
    comments: 42,
    likes: 98,
    tags: ['移动端', 'App测试', 'iOS', 'Android'],
    tag: '移动端',
    department: '移动开发部',
    cover: 'https://picsum.photos/800/400?random=209',
    image: 'https://picsum.photos/800/400?random=209',
    featured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'excellent'
  },
  {
    id: 210,
    title: 'TestMate测试报告定制与分析',
    summary: '介绍如何定制TestMate的测试报告，以及如何利用报告数据进行测试质量分析。',
    description: '介绍如何定制TestMate的测试报告，以及如何利用报告数据进行测试质量分析。',
    content: '<p>好的测试报告能帮助团队快速定位问题...</p>',
    author: '郑分析',
    authorName: '郑分析',
    authorAvatar: 'https://picsum.photos/100/100?random=210',
    authorId: 19,
    createTime: '2026-01-03',
    updateTime: '2026-01-03',
    views: 780,
    comments: 25,
    likes: 72,
    tags: ['测试报告', '数据分析', '优化'],
    tag: '优化',
    department: '质量部',
    cover: 'https://picsum.photos/800/400?random=210',
    image: 'https://picsum.photos/800/400?random=210',
    featured: false,
    zone: 'tools',
    toolId: 1,
    toolName: 'TestMate',
    category: 'excellent'
  },
  // 其他工具帖子（toolId=0）
  {
    id: 301,
    title: 'AI工具使用效率提升指南',
    summary: '本文汇总了各类AI工具的使用技巧，帮助你全面提升工作效率，涵盖代码、文档、设计等多个领域。',
    description: '本文汇总了各类AI工具的使用技巧，帮助你全面提升工作效率，涵盖代码、文档、设计等多个领域。',
    content: '<p>AI工具已经成为提升工作效率的重要手段...</p>',
    author: '效率达人',
    authorName: '效率达人',
    authorAvatar: 'https://picsum.photos/100/100?random=301',
    authorId: 20,
    createTime: '2026-01-12',
    updateTime: '2026-01-12',
    views: 3580,
    comments: 125,
    likes: 268,
    tags: ['效率提升', '最佳实践', '工具汇总'],
    tag: '效率提升',
    department: '技术部',
    cover: 'https://picsum.photos/800/400?random=301',
    image: 'https://picsum.photos/800/400?random=301',
    featured: true,
    isFeatured: true,
    zone: 'tools',
    toolId: 0,
    toolName: '其他工具'
  },
  {
    id: 302,
    title: 'ChatGPT提示词工程实践',
    summary: '深入讲解如何编写高质量的ChatGPT提示词，让AI更好地理解你的意图并给出准确的回答。',
    description: '深入讲解如何编写高质量的ChatGPT提示词，让AI更好地理解你的意图并给出准确的回答。',
    content: '<p>提示词工程是使用AI的关键技能...</p>',
    author: '提示词专家',
    authorName: '提示词专家',
    authorAvatar: 'https://picsum.photos/100/100?random=302',
    authorId: 21,
    createTime: '2026-01-11',
    updateTime: '2026-01-11',
    views: 2890,
    comments: 98,
    likes: 215,
    tags: ['ChatGPT', '提示词', '进阶'],
    tag: '进阶',
    department: '研发部',
    cover: 'https://picsum.photos/800/400?random=302',
    image: 'https://picsum.photos/800/400?random=302',
    featured: false,
    zone: 'tools',
    toolId: 0,
    toolName: '其他工具'
  },
  {
    id: 303,
    title: 'Midjourney绘图技巧分享',
    summary: '分享使用Midjourney进行AI绘图的技巧和经验，包括提示词编写、风格调整等。',
    description: '分享使用Midjourney进行AI绘图的技巧和经验，包括提示词编写、风格调整等。',
    content: '<p>Midjourney是一款强大的AI绘图工具...</p>',
    author: '设计师小王',
    authorName: '设计师小王',
    authorAvatar: 'https://picsum.photos/100/100?random=303',
    authorId: 22,
    createTime: '2026-01-10',
    updateTime: '2026-01-10',
    views: 1850,
    comments: 65,
    likes: 142,
    tags: ['Midjourney', 'AI绘图', '设计'],
    tag: '设计',
    department: '设计部',
    cover: 'https://picsum.photos/800/400?random=303',
    image: 'https://picsum.photos/800/400?random=303',
    featured: false,
    zone: 'tools',
    toolId: 0,
    toolName: '其他工具'
  },
  {
    id: 304,
    title: 'Copilot代码补全最佳实践',
    summary: '如何高效使用GitHub Copilot进行代码补全，提升编程效率的实践经验分享。',
    description: '如何高效使用GitHub Copilot进行代码补全，提升编程效率的实践经验分享。',
    content: '<p>GitHub Copilot可以显著提升编程效率...</p>',
    author: '码农老张',
    authorName: '码农老张',
    authorAvatar: 'https://picsum.photos/100/100?random=304',
    authorId: 23,
    createTime: '2026-01-09',
    updateTime: '2026-01-09',
    views: 2150,
    comments: 78,
    likes: 165,
    tags: ['Copilot', '代码补全', '最佳实践'],
    tag: '最佳实践',
    department: '研发部',
    cover: 'https://picsum.photos/800/400?random=304',
    image: 'https://picsum.photos/800/400?random=304',
    featured: false,
    zone: 'tools',
    toolId: 0,
    toolName: '其他工具'
  },
  {
    id: 305,
    title: 'Notion AI文档助手使用指南',
    summary: '介绍如何使用Notion AI进行文档撰写、总结和优化，提升文档工作效率。',
    description: '介绍如何使用Notion AI进行文档撰写、总结和优化，提升文档工作效率。',
    content: '<p>Notion AI让文档工作变得更加高效...</p>',
    author: '文档达人',
    authorName: '文档达人',
    authorAvatar: 'https://picsum.photos/100/100?random=305',
    authorId: 24,
    createTime: '2026-01-08',
    updateTime: '2026-01-08',
    views: 1680,
    comments: 52,
    likes: 118,
    tags: ['Notion', '文档', '新手'],
    tag: '新手',
    department: '产品部',
    cover: 'https://picsum.photos/800/400?random=305',
    image: 'https://picsum.photos/800/400?random=305',
    featured: false,
    zone: 'tools',
    toolId: 0,
    toolName: '其他工具'
  },
  {
    id: 306,
    title: 'Claude长文本处理技巧',
    summary: '分享使用Claude处理长文本的技巧，包括文档分析、摘要生成、信息提取等场景。',
    description: '分享使用Claude处理长文本的技巧，包括文档分析、摘要生成、信息提取等场景。',
    content: '<p>Claude在长文本处理方面有独特优势...</p>',
    author: 'AI研究员',
    authorName: 'AI研究员',
    authorAvatar: 'https://picsum.photos/100/100?random=306',
    authorId: 25,
    createTime: '2026-01-07',
    updateTime: '2026-01-07',
    views: 1420,
    comments: 45,
    likes: 96,
    tags: ['Claude', '长文本', '技巧'],
    tag: '技巧',
    department: '算法部',
    cover: 'https://picsum.photos/800/400?random=306',
    image: 'https://picsum.photos/800/400?random=306',
    featured: false,
    zone: 'tools',
    toolId: 0,
    toolName: '其他工具'
  }
]

const mockComments: Comment[] = [
  {
    id: 101,  // 与消息中心的 commentId 对应
    postId: 1,
    userId: 2,
    userName: '李四',
    userAvatar: 'https://picsum.photos/100/100?random=2',
    content: '这是一条很棒的帖子，学到了很多知识！感谢作者的分享。',
    likes: 5,
    isLiked: false,
    createTime: new Date(),
    replies: [
      {
        id: 1001,  // 与消息中心的 replyId 对应
        commentId: 101,
        userId: 1,
        userName: '张三',
        userAvatar: 'https://picsum.photos/100/100?random=1',
        content: '感谢您的评论！欢迎继续交流讨论。',
        likes: 2,
        isLiked: false,
        createTime: new Date(),
        replyTo: '李四',
        replyToId: 2
      },
      {
        id: 1002,
        commentId: 101,
        userId: 5,
        userName: '赵六',
        userAvatar: 'https://picsum.photos/100/100?random=5',
        content: '同意楼上的观点，这个帖子很有价值！',
        likes: 1,
        isLiked: false,
        createTime: new Date(),
        replyTo: '张三',
        replyToId: 1
      }
    ]
  },
  {
    id: 102,
    postId: 1,
    userId: 3,
    userName: '王五',
    userAvatar: 'https://picsum.photos/100/100?random=3',
    content: '请问有没有相关的实战案例分享？想深入学习一下。',
    likes: 3,
    isLiked: false,
    createTime: new Date(Date.now() - 3600000),
    replies: [
      {
        id: 1003,
        commentId: 102,
        userId: 1,
        userName: '张三',
        userAvatar: 'https://picsum.photos/100/100?random=1',
        content: '可以参考我之前发布的另一篇帖子，里面有详细的实战案例。',
        likes: 1,
        isLiked: false,
        createTime: new Date(Date.now() - 3000000),
        replyTo: '王五',
        replyToId: 3
      }
    ]
  }
]

const mockActivities: Activity[] = [
  // TestMate工具活动
  {
    id: 1,
    title: 'TestMate高级特性培训',
    content: '<p>本次培训将深入讲解TestMate的高级功能，包括：</p><ul><li>智能测试选择算法</li><li>分布式测试执行</li><li>测试报告定制</li><li>与CI/CD流水线集成</li></ul>',
    cover: 'https://picsum.photos/800/400?random=activity1',
    toolId: 1,
    toolName: 'TestMate',
    type: 'training',
    date: '2026-01-20',
    location: '线上腾讯会议',
    meetingLink: 'https://meeting.tencent.com/testmate1',
    authorId: 10,
    authorName: '张测试',
    authorAvatar: 'https://picsum.photos/100/100?random=10',
    registeredCount: 45,
    maxRegistrations: 100,
    status: 'upcoming',
    isRegistered: false,
    canEdit: true,
    canDelete: true,
    createTime: '2026-01-10T10:00:00Z'
  },
  {
    id: 5,
    title: 'TestMate接口测试实战工作坊',
    content: '<p>动手实践接口测试，内容包括：</p><ul><li>RESTful API测试编写</li><li>GraphQL接口测试</li><li>Mock数据管理</li><li>测试数据驱动设计</li></ul>',
    cover: 'https://picsum.photos/800/400?random=activity5',
    toolId: 1,
    toolName: 'TestMate',
    type: 'workshop',
    date: '2026-01-22',
    location: 'C区培训室201',
    authorId: 11,
    authorName: '李工程师',
    authorAvatar: 'https://picsum.photos/100/100?random=11',
    registeredCount: 28,
    maxRegistrations: 30,
    status: 'upcoming',
    isRegistered: true,
    canEdit: false,
    canDelete: false,
    createTime: '2026-01-08T14:00:00Z'
  },
  {
    id: 6,
    title: 'TestMate性能测试分享会',
    content: '<p>分享TestMate在性能测试中的应用，包括压力测试、负载测试和性能监控的最佳实践。</p>',
    cover: 'https://picsum.photos/800/400?random=activity6',
    toolId: 1,
    toolName: 'TestMate',
    type: 'activity',
    date: '2026-01-28',
    location: '线上会议',
    meetingLink: 'https://meeting.example.com/perf',
    authorId: 13,
    authorName: '陈性能',
    authorAvatar: 'https://picsum.photos/100/100?random=13',
    registeredCount: 35,
    maxRegistrations: 80,
    status: 'upcoming',
    isRegistered: false,
    canEdit: false,
    canDelete: false,
    createTime: '2026-01-09T11:00:00Z'
  },
  {
    id: 7,
    title: 'TestMate新手入门培训',
    content: '<p>面向初学者的TestMate入门培训，从零开始学习自动化测试。</p><ul><li>TestMate安装配置</li><li>编写第一个测试用例</li><li>断言和验证</li><li>测试组织和执行</li></ul>',
    cover: 'https://picsum.photos/800/400?random=activity7',
    toolId: 1,
    toolName: 'TestMate',
    type: 'training',
    date: '2026-02-05',
    location: 'B区会议室502',
    authorId: 10,
    authorName: '张测试',
    authorAvatar: 'https://picsum.photos/100/100?random=10',
    registeredCount: 20,
    maxRegistrations: 50,
    status: 'upcoming',
    isRegistered: false,
    canEdit: true,
    canDelete: true,
    createTime: '2026-01-12T09:00:00Z'
  },
  // 扶摇Agent活动
  {
    id: 2,
    title: '扶摇Agent实战培训',
    content: '<p>深入学习扶摇Agent的使用技巧和最佳实践。</p>',
    cover: 'https://picsum.photos/800/400?random=activity2',
    toolId: -1,
    toolName: '扶摇Agent',
    type: 'training',
    date: '2026-01-25',
    location: 'B区会议室301',
    authorId: 2,
    authorName: '李四',
    authorAvatar: 'https://picsum.photos/100/100?random=2',
    registeredCount: 25,
    maxRegistrations: 30,
    status: 'upcoming',
    isRegistered: true,
    canEdit: false,
    canDelete: false,
    createTime: '2026-01-08T14:00:00Z'
  },
  {
    id: 3,
    title: '智能编排工作坊',
    content: '<p>学习如何使用扶摇Agent进行智能工作流编排。</p>',
    cover: 'https://picsum.photos/800/400?random=activity3',
    toolId: -1,
    toolName: '扶摇Agent',
    type: 'workshop',
    date: '2026-02-01',
    location: '线上会议',
    meetingLink: 'https://meeting.example.com/456',
    authorId: 1,
    authorName: '张三',
    authorAvatar: 'https://picsum.photos/100/100?random=1',
    registeredCount: 15,
    maxRegistrations: 100,
    status: 'upcoming',
    isRegistered: false,
    canEdit: true,
    canDelete: true,
    createTime: '2026-01-12T09:00:00Z'
  },
  {
    id: 4,
    title: 'AI办公效率提升活动',
    content: '<p>探讨如何利用AI工具提升日常办公效率。</p>',
    cover: 'https://picsum.photos/800/400?random=activity4',
    toolId: -1,
    toolName: '扶摇Agent',
    type: 'activity',
    date: '2026-02-10',
    location: 'A区报告厅',
    authorId: 3,
    authorName: '王五',
    authorAvatar: 'https://picsum.photos/100/100?random=3',
    registeredCount: 50,
    maxRegistrations: 200,
    status: 'upcoming',
    isRegistered: false,
    canEdit: false,
    canDelete: false,
    createTime: '2026-01-11T16:00:00Z'
  }
]

const mockMessages: Message[] = [
  {
    id: 1,
    userId: 1,
    type: 'post_comment',
    title: '帖子评论通知',
    content: '张三 评论了您的帖子《AI技术实践分享》',
    relatedId: 101,           // 帖子ID
    relatedType: 'post',
    commentId: 1001,          // 评论ID，用于定位到具体评论
    fromUserId: 2,
    fromUserName: '张三',
    read: false,
    createTime: new Date(Date.now() - 3600000),
    createdAt: new Date(Date.now() - 3600000).toISOString()
  },
  {
    id: 2,
    userId: 1,
    type: 'activity_registration',
    title: '活动报名通知',
    content: '李四 报名参加了您发布的活动《扶摇Agent新手入门培训》',
    relatedId: 1,             // 活动ID
    relatedType: 'activity',
    fromUserId: 3,
    fromUserName: '李四',
    read: false,
    createTime: new Date(Date.now() - 7200000),
    createdAt: new Date(Date.now() - 7200000).toISOString()
  },
  {
    id: 3,
    userId: 1,
    type: 'award_notification',
    title: '恭喜您获得奖项！',
    content: '恭喜！您在 2026-01 荣获【创新突破】类别的「年度创新贡献奖」奖项',
    relatedId: 10,            // 奖项ID
    relatedType: 'award',
    fromUserName: '系统通知',
    read: false,
    createTime: new Date(Date.now() - 86400000),
    createdAt: new Date(Date.now() - 86400000).toISOString()
  },
  {
    id: 4,
    userId: 1,
    type: 'post_like',
    title: '点赞通知',
    content: '王五 赞了您的帖子《使用扶摇Agent实现智能代码生成》',
    relatedId: 102,           // 帖子ID
    relatedType: 'post',
    fromUserId: 4,
    fromUserName: '王五',
    read: true,
    createTime: new Date(Date.now() - 172800000),
    createdAt: new Date(Date.now() - 172800000).toISOString()
  },
  {
    id: 5,
    userId: 1,
    type: 'comment_reply',
    title: '评论回复通知',
    content: '赵六 回复了您的评论',
    relatedId: 101,           // 帖子ID
    relatedType: 'post',
    commentId: 1001,          // 评论ID
    replyId: 2001,            // 回复ID，用于定位到具体回复
    fromUserId: 5,
    fromUserName: '赵六',
    read: true,
    createTime: new Date(Date.now() - 259200000),
    createdAt: new Date(Date.now() - 259200000).toISOString()
  }
]

const mockTeamAwards: TeamAward[] = [
  {
    id: 1,
    title: '年度最佳AI创新团队',
    year: 2026,
    images: [
      {
        id: 1,
        image: 'https://picsum.photos/400/300?random=team1',
        winnerName: 'AI研发中心',
        teamField: 'AI技术研发',
        story: '<p><strong>获奖事迹：</strong></p><p>AI研发中心团队在过去一年中，成功研发了多项核心AI技术，包括：</p><ul><li>自主研发的大语言模型优化框架，推理效率提升300%</li><li>智能代码审查系统，帮助全公司减少40%的代码缺陷</li><li>跨部门AI能力平台，服务20+业务团队</li></ul><p>团队成员积极参与技术分享，全年完成50+场内部培训。</p>',
        flowers: 128,
        hasGivenFlower: false
      },
      {
        id: 2,
        image: 'https://picsum.photos/400/300?random=team2',
        winnerName: '智能客服团队',
        teamField: '智能服务',
        story: '<p><strong>获奖事迹：</strong></p><p>智能客服团队成功打造了新一代AI客服系统：</p><ul><li>客户问题自动解决率从45%提升至85%</li><li>平均响应时间从30秒缩短至3秒</li><li>用户满意度评分达到4.8/5.0</li></ul><p>该系统已服务超过1000万用户，节省人力成本约60%。</p>',
        flowers: 96,
        hasGivenFlower: false
      },
      {
        id: 3,
        image: 'https://picsum.photos/400/300?random=team3',
        winnerName: '数据智能团队',
        teamField: '大数据分析',
        story: '<p><strong>获奖事迹：</strong></p><p>数据智能团队在数据驱动决策方面取得突破：</p><ul><li>构建企业级数据中台，统一200+数据源</li><li>开发智能预测模型，业务预测准确率达92%</li><li>实现实时数据分析能力，支持秒级决策</li></ul><p>帮助业务团队实现数据化运营，年度创造价值超5000万。</p>',
        flowers: 87,
        hasGivenFlower: false
      }
    ]
  },
  {
    id: 2,
    title: '技术突破贡献奖',
    year: 2026,
    images: [
      {
        id: 4,
        image: 'https://picsum.photos/400/300?random=team4',
        winnerName: '云原生架构组',
        teamField: '基础架构',
        story: '<p><strong>获奖事迹：</strong></p><p>云原生架构组推动公司技术架构全面升级：</p><ul><li>完成核心系统容器化改造，资源利用率提升200%</li><li>搭建统一的服务网格平台，微服务治理效率提升50%</li><li>实现99.99%的系统可用性保障</li></ul>',
        flowers: 75,
        hasGivenFlower: false
      },
      {
        id: 5,
        image: 'https://picsum.photos/400/300?random=team5',
        winnerName: '安全攻防实验室',
        teamField: '信息安全',
        story: '<p><strong>获奖事迹：</strong></p><p>安全攻防实验室保障公司信息安全：</p><ul><li>发现并修复50+高危安全漏洞</li><li>建立AI驱动的威胁检测系统</li><li>全年0重大安全事故</li></ul>',
        flowers: 68,
        hasGivenFlower: false
      },
      {
        id: 6,
        image: 'https://picsum.photos/400/300?random=team6',
        winnerName: '效能工具平台组',
        teamField: '研发效能',
        story: '<p><strong>获奖事迹：</strong></p><p>效能工具平台组提升全公司研发效能：</p><ul><li>打造一站式开发者平台，日活开发者1000+</li><li>研发工具链自动化率达到95%</li><li>平均构建部署时间缩短70%</li></ul>',
        flowers: 62,
        hasGivenFlower: false
      }
    ]
  },
  {
    id: 3,
    title: '年度优秀协作团队',
    year: 2025,
    images: [
      {
        id: 7,
        image: 'https://picsum.photos/400/300?random=team7',
        winnerName: '产品技术融合小组',
        teamField: '产品研发',
        story: '<p><strong>获奖事迹：</strong></p><p>产品技术融合小组实现高效跨部门协作：</p><ul><li>建立产品-技术协作新模式</li><li>项目交付周期缩短40%</li><li>客户满意度提升35%</li></ul>',
        flowers: 55,
        hasGivenFlower: false
      },
      {
        id: 8,
        image: 'https://picsum.photos/400/300?random=team8',
        winnerName: '用户体验设计团队',
        teamField: 'UX设计',
        story: '<p><strong>获奖事迹：</strong></p><p>用户体验设计团队打造极致体验：</p><ul><li>完成30+产品的体验升级</li><li>建立统一设计语言系统</li><li>用户体验评分提升25%</li></ul>',
        flowers: 48,
        hasGivenFlower: false
      },
      {
        id: 9,
        image: 'https://picsum.photos/400/300?random=team9',
        winnerName: '敏捷转型教练组',
        teamField: '组织发展',
        story: '<p><strong>获奖事迹：</strong></p><p>敏捷转型教练组推动组织变革：</p><ul><li>辅导50+团队完成敏捷转型</li><li>建立敏捷教练培养体系</li><li>团队交付效率平均提升60%</li></ul>',
        flowers: 42,
        hasGivenFlower: false
      }
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

// 新闻相关
export const getNews = async (): Promise<{ list: NewsItem[] }> => {
  await delay()
  return { list: mockNews }
}

// 首页赋能交流列表（与赋能交流页面独立）
export interface HomeEmpowermentItem {
  id: number
  title: string
  tag: string
  tagType?: string
  author: string
  time: string
}

export const getHomeEmpowerment = async (limit: number = 5): Promise<{ list: HomeEmpowermentItem[] }> => {
  await delay()
  const items: HomeEmpowermentItem[] = [
    {
      id: 1,
      title: '如何使用 Agent 提升代码开发效率？',
      tag: '讨论',
      tagType: 'blue',
      author: '张三',
      time: '2小时前'
    },
    {
      id: 2,
      title: '分享一个提升工作效率的AI工具使用技巧',
      tag: '分享',
      tagType: 'green',
      author: '李四',
      time: '3小时前'
    },
    {
      id: 3,
      title: '关于AI辅助编程的一些疑问',
      tag: '提问',
      tagType: 'orange',
      author: '王五',
      time: '5小时前'
    },
    {
      id: 4,
      title: 'Prompt工程最佳实践经验总结',
      tag: '经验',
      tagType: 'purple',
      author: '赵六',
      time: '6小时前'
    },
    {
      id: 5,
      title: '推荐几个好用的AI工具',
      tag: '工具',
      tagType: 'blue',
      author: '钱七',
      time: '8小时前'
    }
  ]
  return { list: items.slice(0, limit) }
}

// 悬浮工具平台列表（与AI工具专区独立，点击跳转到外部工具平台）
export interface ToolPlatformItem {
  id: number
  name: string
  desc: string
  logo: string
  color: string
  platformUrl: string  // 外部工具平台链接
}

export const getToolPlatform = async (): Promise<{ list: ToolPlatformItem[] }> => {
  await delay()
  const items: ToolPlatformItem[] = [
    {
      id: 1,
      name: 'TestMate',
      desc: '自动化测试助手',
      logo: '🧪',
      color: '#36cfc9',
      platformUrl: 'https://testmate.example.com'
    },
    {
      id: 2,
      name: 'CodeMate',
      desc: '智能代码补全',
      logo: '💻',
      color: '#9254de',
      platformUrl: 'https://codemate.example.com'
    },
    {
      id: 3,
      name: '云集',
      desc: '云端计算集群',
      logo: '☁️',
      color: '#597ef7',
      platformUrl: 'https://yunji.example.com'
    },
    {
      id: 4,
      name: '云见',
      desc: '智能监控平台',
      logo: '👁️',
      color: '#ff9c6e',
      platformUrl: 'https://yunjian.example.com'
    },
    {
      id: 5,
      name: '扶摇',
      desc: 'Agent编排引擎',
      logo: '🚀',
      color: '#4096ff',
      platformUrl: 'https://fuyao.example.com'
    }
  ]
  return { list: items }
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

// 根据工号获取用户信息
export const getUserByEmployeeId = async (employeeId: string): Promise<UserProfile | null> => {
  await delay()
  // 模拟根据工号查找用户
  if (employeeId === mockCurrentUser.employeeId) {
    return mockCurrentUser
  }
  // 返回模拟数据
  return {
    id: Date.now(),
    employeeId,
    name: `用户${employeeId}`,
    avatar: `https://picsum.photos/100/100?random=${employeeId}`,
    department: '技术部/研发中心',
    departments: {
      level1: { id: 100, name: '技术部', level: 1 },
      level2: { id: 110, name: '研发中心', level: 2 }
    },
    postsCount: Math.floor(Math.random() * 20),
    favoritesCount: Math.floor(Math.random() * 50),
    commentsCount: Math.floor(Math.random() * 100),
    activitiesCount: Math.floor(Math.random() * 10),
    flowersCount: Math.floor(Math.random() * 200),
    points: Math.floor(Math.random() * 5000),
    roles: ['user']
  }
}

export const updateUserProfile = async (data: Partial<UserProfile>): Promise<void> => {
  await delay()
  Object.assign(mockCurrentUser, data)
}

// 通用分页参数接口
interface PaginationParams {
  page?: number
  pageSize?: number
}

// 帖子查询参数接口
interface PostQueryParams extends PaginationParams {
  toolId?: number
  zone?: string
  tag?: string
  keyword?: string
  sortBy?: string
}

// 活动查询参数接口
interface ActivityQueryParams extends PaginationParams {
  toolId?: number
  status?: string
}

// 积分历史记录类型
interface PointsHistoryItem {
  id: number
  type: string
  points: number
  description: string
  createdAt: string
}

// 积分规则类型
interface PointsRule {
  action: string
  points: number
  description: string
}

export const getUserPoints = async (): Promise<{
  totalPoints: number
  monthlyPoints: number
  ranking: number
  pointsHistory: PointsHistoryItem[]
  pointsRules: PointsRule[]
}> => {
  await delay()
  return {
    totalPoints: mockCurrentUser.points,
    monthlyPoints: 350,
    ranking: 15,
    pointsHistory: [
      {
        id: 1,
        type: 'post_publish',
        points: 50,
        description: '发布帖子《AI技术实践分享》',
        createdAt: new Date(Date.now() - 86400000).toISOString()
      },
      {
        id: 2,
        type: 'comment',
        points: 5,
        description: '发表评论',
        createdAt: new Date(Date.now() - 172800000).toISOString()
      },
      {
        id: 3,
        type: 'like_received',
        points: 2,
        description: '帖子获得点赞',
        createdAt: new Date(Date.now() - 259200000).toISOString()
      }
    ],
    pointsRules: [
      { action: 'post_publish', points: 50, description: '发布帖子' },
      { action: 'comment', points: 5, description: '发表评论' },
      { action: 'like_received', points: 2, description: '帖子获得点赞' },
      { action: 'activity_join', points: 10, description: '参与活动' },
      { action: 'flower_received', points: 10, description: '获得小红花' }
    ]
  }
}

export const getUserPosts = async (_userId: number, params?: PaginationParams): Promise<PageResult<Post>> => {
  await delay()
  return { list: mockPosts, total: mockPosts.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getUserFavorites = async (_userId: number, params?: PaginationParams): Promise<PageResult<Post>> => {
  await delay()
  return { list: mockPosts, total: mockPosts.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getUserComments = async (_userId: number, params?: PaginationParams): Promise<PageResult<Comment>> => {
  await delay()
  return { list: mockComments, total: mockComments.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getUserActivities = async (_userId: number, params?: PaginationParams): Promise<PageResult<Activity>> => {
  await delay()
  return { list: mockActivities, total: mockActivities.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getUserCreatedActivities = async (_userId: number, params?: PaginationParams): Promise<PageResult<Activity>> => {
  await delay()
  return { list: mockActivities, total: mockActivities.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

// 帖子相关
export const getPosts = async (params?: PostQueryParams): Promise<PostsResponse> => {
  await delay()
  // 根据toolId或zone过滤帖子
  let filteredPosts = mockPosts
  if (params?.toolId !== undefined) {
    filteredPosts = mockPosts.filter(p => p.toolId === params.toolId)
  } else if (params?.zone) {
    filteredPosts = mockPosts.filter(p => p.zone === params.zone)
  }

  // 对于"其他工具"(toolId=0)，排除精华帖子（精华帖子通过单独接口获取）
  // 精华帖子会通过PostList的featuredPosts属性传入，在列表内置顶显示
  if (params?.toolId === 0) {
    filteredPosts = filteredPosts.filter(p => !p.featured && !p.isFeatured)
  }

  // 根据tag过滤
  if (params?.tag) {
    const tagToFilter = params.tag
    filteredPosts = filteredPosts.filter(p => p.tag === tagToFilter || p.tags?.includes(tagToFilter))
  }
  // 根据keyword搜索
  if (params?.keyword) {
    const keyword = params.keyword.toLowerCase()
    filteredPosts = filteredPosts.filter(p =>
      p.title.toLowerCase().includes(keyword) ||
      p.description?.toLowerCase().includes(keyword) ||
      p.summary?.toLowerCase().includes(keyword)
    )
  }
  // 排序
  if (params?.sortBy) {
    switch (params.sortBy) {
      case 'newest':
        filteredPosts = [...filteredPosts].sort((a, b) =>
          new Date(b.createTime).getTime() - new Date(a.createTime).getTime()
        )
        break
      case 'hot':
        filteredPosts = [...filteredPosts].sort((a, b) => b.views - a.views)
        break
      case 'likes':
        filteredPosts = [...filteredPosts].sort((a, b) => b.likes - a.likes)
        break
      case 'comments':
        filteredPosts = [...filteredPosts].sort((a, b) => b.comments - a.comments)
        break
    }
  }
  return {
    list: filteredPosts,
    total: filteredPosts.length,
    page: params?.page || 1,
    pageSize: params?.pageSize || 15
  }
}

export const getPostDetail = async (id: number): Promise<Post> => {
  await delay()
  const post = mockPosts.find(p => p.id === id) ?? mockPosts[0]!
  // 获取当前用户，判断是否是作者
  const currentUser = await getCurrentUser()
  const isAuthor = post.authorId === currentUser.id
  return { ...post, id, isAuthor }
}

export const createPost = async (data: Partial<Post>): Promise<Post> => {
  await delay()
  const newPost: Post = {
    id: Date.now(),
    title: data.title || '未命名帖子',
    views: 0,
    comments: 0,
    likes: 0,
    createTime: new Date(),
    ...data,
    author: mockCurrentUser.name,
    authorName: mockCurrentUser.name,
    authorAvatar: mockCurrentUser.avatar,
    authorId: mockCurrentUser.id
  }
  mockPosts.unshift(newPost)
  return newPost
}

export const updatePost = async (id: number, data: Partial<Post>): Promise<Post> => {
  await delay()
  const post = mockPosts.find(p => p.id === id) ?? mockPosts[0]!
  Object.assign(post, data, { updateTime: new Date() })
  return post
}

export const deletePost = async (id: number): Promise<void> => {
  await delay()
  const index = mockPosts.findIndex(p => p.id === id)
  if (index > -1) mockPosts.splice(index, 1)
}

// 检查扶摇Agent是否已有置顶帖子
export const checkAgentPinnedPost = async (): Promise<{ hasPinned: boolean; pinnedPostId: number | null; pinnedPostTitle: string | null }> => {
  await delay()
  const pinnedPost = mockPosts.find(p => p.zone === 'agent' && (p.featured || p.isFeatured))
  return {
    hasPinned: !!pinnedPost,
    pinnedPostId: pinnedPost?.id || null,
    pinnedPostTitle: pinnedPost?.title || null
  }
}

// 设置帖子置顶/加精状态
export const setPostFeatured = async (
  postId: number,
  featured: boolean,
  zone?: string,
  toolId?: number | null
): Promise<{ success: boolean; featured: boolean; message?: string }> => {
  await delay()

  // 扶摇Agent应用只能有一个置顶帖子
  if (featured && zone === 'agent') {
    const existingPinned = mockPosts.find(p => p.zone === 'agent' && (p.featured || p.isFeatured) && p.id !== postId)
    if (existingPinned) {
      return {
        success: false,
        featured: false,
        message: `已有置顶帖子「${existingPinned.title}」，请先取消该帖子的置顶后再操作`
      }
    }
  }

  const post = mockPosts.find(p => p.id === postId)
  if (post) {
    post.featured = featured
    post.isFeatured = featured
    console.log(`[Mock API] 帖子 ${postId} ${featured ? '设置' : '取消'}精华/置顶 (zone: ${zone}, toolId: ${toolId})`)
  }
  return { success: true, featured }
}

// 获取所有精华/置顶帖子列表（按区域分组）
export const getAllFeaturedPosts = async (): Promise<{
  practices: Post[]
  empowerment: Post[]
  agent: Post[]
  otherTools: Post[]
}> => {
  await delay()

  // AI优秀实践精华
  const practices = mockPosts.filter(p =>
    (p.zone === 'practices' || (!p.zone && !p.toolId && p.toolId !== 0)) &&
    (p.featured || p.isFeatured)
  )

  // 赋能交流精华
  const empowerment = mockPosts.filter(p =>
    p.zone === 'empowerment' && (p.featured || p.isFeatured)
  )

  // 扶摇Agent置顶
  const agent = mockPosts.filter(p =>
    (p.zone === 'agent' || p.toolId === -1) && (p.featured || p.isFeatured)
  )

  // AI工具专区其他工具精华
  const otherTools = mockPosts.filter(p =>
    p.toolId === 0 && (p.featured || p.isFeatured)
  )

  return { practices, empowerment, agent, otherTools }
}

// 批量移除精华/置顶
export const removeFeaturedPost = async (postId: number): Promise<{ success: boolean }> => {
  await delay()
  const post = mockPosts.find(p => p.id === postId)
  if (post) {
    post.featured = false
    post.isFeatured = false
    console.log(`[Mock API] 移除帖子 ${postId} 的精华/置顶状态`)
  }
  return { success: true }
}

export const likePost = async (id: number, action: 'like' | 'unlike'): Promise<{ liked: boolean; likes: number }> => {
  await delay()
  const post = mockPosts.find(p => p.id === id) ?? mockPosts[0]!
  if (action === 'like') {
    post.likes++
    post.isLiked = true
  } else {
    post.likes = Math.max(0, post.likes - 1)
    post.isLiked = false
  }
  return { liked: post.isLiked ?? false, likes: post.likes }
}

export const collectPost = async (id: number, action: 'collect' | 'uncollect'): Promise<{ collected: boolean }> => {
  await delay()
  const post = mockPosts.find(p => p.id === id) ?? mockPosts[0]!
  post.isCollected = action === 'collect'
  return { collected: post.isCollected ?? false }
}

interface RecommendedCover {
  id: number
  url: string
  name: string
}

export const getRecommendedCovers = async (params?: { zone?: string; count?: number }): Promise<RecommendedCover[]> => {
  await delay()
  const count = params?.count || 3
  const covers: RecommendedCover[] = [
    { id: 1, url: 'https://picsum.photos/800/400?random=cover1', name: '科技蓝' },
    { id: 2, url: 'https://picsum.photos/800/400?random=cover2', name: '简约白' },
    { id: 3, url: 'https://picsum.photos/800/400?random=cover3', name: '渐变紫' },
    { id: 4, url: 'https://picsum.photos/800/400?random=cover4', name: '活力橙' },
    { id: 5, url: 'https://picsum.photos/800/400?random=cover5', name: '清新绿' }
  ]
  return covers.slice(0, count)
}

// 草稿接口类型
interface DraftData {
  draftId?: string
  zone?: string
  toolId?: number | null
  title?: string
  summary?: string
  content?: string
  cover?: string
  tags?: string[]
  savedAt?: string
}

interface DraftResponse {
  code: number
  message: string
  data: DraftData | null
}

// 草稿存储（模拟后端存储）
// 草稿双重保存策略：
// - 前端 localStorage: 短时间存储（2秒防抖自动保存）
// - 后端服务器: 长时间存储（每3分钟同步一次）
let mockDraftStorage: DraftData | null = null

export const saveDraft = async (data: DraftData): Promise<DraftResponse> => {
  await delay()
  // 使用传入的 savedAt 时间（用于前后端草稿时间比较）
  const savedAt = data.savedAt || new Date().toISOString()
  mockDraftStorage = {
    draftId: mockDraftStorage?.draftId || ('draft_' + Date.now()),
    ...data,
    savedAt
  }
  console.log('[Mock API] 草稿已保存到后端:', savedAt)
  return {
    code: 200,
    message: '保存成功',
    data: {
      draftId: mockDraftStorage.draftId,
      savedAt: mockDraftStorage.savedAt
    }
  }
}

export const getDraft = async (): Promise<DraftResponse> => {
  await delay()
  if (mockDraftStorage) {
    console.log('[Mock API] 获取后端草稿:', mockDraftStorage.savedAt)
    return {
      code: 200,
      message: 'success',
      data: mockDraftStorage
    }
  }
  console.log('[Mock API] 后端无草稿')
  return {
    code: 200,
    message: 'success',
    data: null
  }
}

export const deleteDraft = async (): Promise<{ code: number; message: string; data: null }> => {
  await delay()
  mockDraftStorage = null
  console.log('[Mock API] 后端草稿已删除')
  return { code: 200, message: '删除成功', data: null }
}

// 获取专区标签
export const getZoneTags = async (params: { zone: string; toolId?: number | null }): Promise<{ list: Array<{ name: string; count: number }> }> => {
  await delay()
  const { zone, toolId } = params

  // 根据专区和工具返回不同的标签
  if (zone === 'practices') {
    return {
      list: [
        { name: '自然语言处理', count: 15 },
        { name: '计算机视觉', count: 12 },
        { name: '深度学习', count: 18 },
        { name: 'AI伦理', count: 6 },
        { name: '机器学习', count: 14 },
        { name: '机器人', count: 8 },
        { name: '数据科学', count: 10 },
        { name: '生成式AI', count: 20 },
        { name: 'PyTorch', count: 9 },
        { name: 'TensorFlow', count: 7 }
      ]
    }
  }

  if (zone === 'tools') {
    // 非"其他工具"时只返回固定标签
    if (toolId !== null && toolId !== undefined && toolId !== -1) {
      return {
        list: [
          { name: '操作指导', count: 25 },
          { name: '优秀使用', count: 30 }
        ]
      }
    }
    // "其他工具"返回通用标签
    return {
      list: [
        { name: '新手', count: 8 },
        { name: '进阶', count: 12 },
        { name: '最佳实践', count: 15 },
        { name: '技巧', count: 10 },
        { name: '案例', count: 7 },
        { name: '教程', count: 9 },
        { name: '优化', count: 6 },
        { name: '通用', count: 5 }
      ]
    }
  }

  if (zone === 'agent') {
    return {
      list: [
        { name: 'Agent应用', count: 12 },
        { name: '工作流', count: 8 },
        { name: '自动化', count: 15 },
        { name: '智能编排', count: 10 },
        { name: '案例分享', count: 9 },
        { name: '开发指南', count: 5 }
      ]
    }
  }

  if (zone === 'empowerment') {
    return {
      list: [
        { name: '讨论', count: 20 },
        { name: '提问', count: 15 },
        { name: '分享', count: 18 },
        { name: '经验', count: 12 },
        { name: '工具', count: 10 },
        { name: '技巧', count: 14 },
        { name: '案例', count: 8 },
        { name: '教程', count: 6 },
        { name: '最佳实践', count: 9 },
        { name: '问题解决', count: 7 }
      ]
    }
  }

  return { list: [] }
}

export const getPostComments = async (_postId: number, params?: PaginationParams): Promise<PageResult<Comment>> => {
  await delay()
  return { list: mockComments, total: mockComments.length, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const createComment = async (postId: number, data: { content: string }): Promise<Comment> => {
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
export const likeComment = async (id: number, action: 'like' | 'unlike'): Promise<{ liked: boolean; likes: number }> => {
  await delay()
  const comment = mockComments.find(c => c.id === id) ?? mockComments[0]!
  if (action === 'like') {
    comment.likes++
    comment.isLiked = true
  } else {
    comment.likes = Math.max(0, comment.likes - 1)
    comment.isLiked = false
  }
  return { liked: comment.isLiked ?? false, likes: comment.likes }
}

export const updateComment = async (id: number, data: { content: string }): Promise<void> => {
  await delay()
  const comment = mockComments.find(c => c.id === id) ?? mockComments[0]!
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
export const getActivities = async (params?: ActivityQueryParams): Promise<PageResult<Activity>> => {
  await delay()
  // 根据toolId过滤活动
  let filteredActivities = mockActivities
  if (params?.toolId !== undefined) {
    filteredActivities = mockActivities.filter(a => a.toolId === params.toolId)
  }
  // 根据status过滤
  if (params?.status) {
    filteredActivities = filteredActivities.filter(a => a.status === params.status)
  }
  return {
    list: filteredActivities,
    total: filteredActivities.length,
    page: params?.page || 1,
    pageSize: params?.pageSize || 15
  }
}

export const getActivityDetail = async (id: number): Promise<Activity> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id) ?? mockActivities[0]!
  // 确保返回完整的活动数据，包括工具信息和权限
  return {
    ...activity,
    id,
    toolName: activity.toolName || (activity.toolId ? `工具${activity.toolId}` : ''),
    canEdit: activity.canEdit !== undefined ? activity.canEdit : true,
    canDelete: activity.canDelete !== undefined ? activity.canDelete : true
  }
}

export const createActivity = async (data: Partial<Activity>): Promise<Activity> => {
  await delay()
  const newActivity: Activity = {
    id: Date.now(),
    title: data.title || '',
    content: data.content || '',
    type: data.type || 'activity',
    date: data.date || new Date(),
    authorId: mockCurrentUser.id,
    authorName: mockCurrentUser.name,
    authorAvatar: mockCurrentUser.avatar,
    registeredCount: 0,
    status: 'upcoming',
    createTime: new Date(),
    ...data
  }
  mockActivities.unshift(newActivity)
  return newActivity
}

export const updateActivity = async (id: number, data: Partial<Activity>): Promise<Activity> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id) ?? mockActivities[0]!
  Object.assign(activity, data, { updateTime: new Date() })
  return activity
}

export const deleteActivity = async (id: number): Promise<void> => {
  await delay()
  const index = mockActivities.findIndex(a => a.id === id)
  if (index > -1) mockActivities.splice(index, 1)
}

export const registerActivity = async (id: number): Promise<{ registered: boolean; registeredCount: number }> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id) ?? mockActivities[0]!
  activity.isRegistered = true
  activity.registeredCount = (activity.registeredCount || 0) + 1
  return { registered: true, registeredCount: activity.registeredCount }
}

export const cancelRegistration = async (id: number): Promise<{ success: boolean; message: string }> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id)
  if (!activity) {
    throw new Error('活动不存在')
  }
  if (!activity.isRegistered) {
    throw new Error('您尚未报名此活动')
  }
  activity.isRegistered = false
  activity.registeredCount = Math.max(0, (activity.registeredCount || 0) - 1)
  return { success: true, message: '取消成功' }
}

export interface Registration {
  id: number
  activityId?: number
  userId: number
  userName: string
  userAvatar: string
  employeeId?: string
  department?: string
  registerTime: string | Date
}

export const getRegistrations = async (_id: number, params?: { page?: number; pageSize?: number }): Promise<PageResult<Registration>> => {
  await delay()
  // 模拟报名用户数据
  const mockRegistrations: Registration[] = [
    {
      id: 1,
      activityId: _id,
      userId: 2,
      userName: '李四',
      userAvatar: 'https://picsum.photos/100/100?random=2',
      employeeId: 'E002',
      department: '技术部/研发中心',
      registerTime: '2026-01-10 10:30'
    },
    {
      id: 2,
      activityId: _id,
      userId: 3,
      userName: '王五',
      userAvatar: 'https://picsum.photos/100/100?random=3',
      employeeId: 'E003',
      department: '产品部',
      registerTime: '2026-01-10 11:20'
    },
    {
      id: 3,
      activityId: _id,
      userId: 4,
      userName: '赵六',
      userAvatar: 'https://picsum.photos/100/100?random=4',
      employeeId: 'E004',
      department: '设计部/用户体验组',
      registerTime: '2026-01-11 09:15'
    },
    {
      id: 4,
      activityId: _id,
      userId: 5,
      userName: '钱七',
      userAvatar: 'https://picsum.photos/100/100?random=5',
      employeeId: 'E005',
      department: '数据部/BI中心',
      registerTime: '2026-01-11 14:00'
    },
    {
      id: 5,
      activityId: _id,
      userId: 6,
      userName: '孙八',
      userAvatar: 'https://picsum.photos/100/100?random=6',
      employeeId: 'E006',
      department: '运营部',
      registerTime: '2026-01-12 10:45'
    }
  ]
  return { list: mockRegistrations, total: mockRegistrations.length, page: params?.page || 1, pageSize: params?.pageSize || 100 }
}

// 消息相关
export const getMessages = async (params?: PaginationParams): Promise<PageResult<Message>> => {
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

export const getUnreadMessageCount = async (): Promise<{ count: number }> => {
  await delay()
  return { count: mockMessages.filter(m => !m.read).length }
}

// 荣誉类型
interface Honor {
  id: number
  name: string
  description: string
  image: string
  category: string
  awardDate: string
}

interface HonorInfluence {
  totalHonors: number
  totalUsers: number
  totalFlowers: number
  categories: Array<{ name: string; count: number }>
}

interface ToolOwnerCheck {
  isOwner: boolean
  toolId: number
  permissions: string[]
}

interface FeaturedPostResponse {
  post: Post | null
}

// 荣誉相关

// 个人荣誉项类型（与接口文档一致）
export interface HonorListItem {
  id: number
  name: string           // 获奖者姓名
  department: string     // 获奖者部门
  avatar: string         // 获奖者头像URL
  awardName: string      // 奖项名称
  awardDate: string      // 获奖日期（YYYY-MM-DD格式）
  category: string       // 奖项类别：innovation/efficiency/practice/community
  year: string           // 获奖年份
  isMine: boolean        // 是否为当前登录用户的荣誉
  flowers: number        // 收到的花朵数
  hasGivenFlower: boolean// 当前用户是否已送花
  achievement?: string   // 获奖成就描述
}

// Mock 个人荣誉列表数据
const mockHonorList: HonorListItem[] = [
  { id: 1, name: '林星辰', department: '架构平台部', avatar: 'https://i.pravatar.cc/150?img=11', awardName: '2026年度 AI 技术突破奖', awardDate: '2026-01-05', category: 'innovation', year: '2026', isMine: true, flowers: 12, hasGivenFlower: false, achievement: '在AI模型优化领域取得重大突破，成功将模型推理速度提升300%。' },
  { id: 2, name: 'Sarah', department: 'UED 设计中心', avatar: 'https://i.pravatar.cc/150?img=5', awardName: '最佳 AI 辅助设计实践', awardDate: '2025-12-20', category: 'practice', year: '2025', isMine: false, flowers: 15, hasGivenFlower: false, achievement: '创新性地将AI技术应用于设计工作流程。' },
  { id: 3, name: '张伟', department: '效能工程部', avatar: 'https://i.pravatar.cc/150?img=3', awardName: 'Copilot 效能提升大师', awardDate: '2025-11-15', category: 'efficiency', year: '2025', isMine: false, flowers: 20, hasGivenFlower: true, achievement: '深入研究和应用GitHub Copilot等AI编程工具。' },
  { id: 4, name: '王芳', department: '开源办公室', avatar: 'https://i.pravatar.cc/150?img=9', awardName: 'AI 社区贡献之星', awardDate: '2025-10-10', category: 'community', year: '2025', isMine: false, flowers: 18, hasGivenFlower: false, achievement: '在开源社区中持续贡献AI相关项目和文档。' },
  { id: 5, name: '李明', department: '数据部', avatar: 'https://i.pravatar.cc/150?img=12', awardName: '数据智能创新奖', awardDate: '2025-09-20', category: 'innovation', year: '2025', isMine: false, flowers: 22, hasGivenFlower: false, achievement: '开发数据智能分析平台，提升数据处理效率。' },
  { id: 6, name: '陈静', department: '算法部', avatar: 'https://i.pravatar.cc/150?img=16', awardName: 'AI 算法优化专家', awardDate: '2025-08-15', category: 'efficiency', year: '2025', isMine: false, flowers: 25, hasGivenFlower: true, achievement: '在算法优化领域取得显著成果。' },
  { id: 7, name: '周杰', department: '架构平台部', avatar: 'https://i.pravatar.cc/150?img=7', awardName: '年度技术创新奖', awardDate: '2024-12-25', category: 'innovation', year: '2024', isMine: false, flowers: 30, hasGivenFlower: false, achievement: '主导多项核心技术创新项目。' },
  { id: 8, name: '赵敏', department: 'UED 设计中心', avatar: 'https://i.pravatar.cc/150?img=20', awardName: 'AI 设计工具先锋', awardDate: '2024-11-18', category: 'practice', year: '2024', isMine: false, flowers: 16, hasGivenFlower: false, achievement: '推广AI设计工具的应用实践。' },
  { id: 9, name: '孙鹏', department: '效能工程部', avatar: 'https://i.pravatar.cc/150?img=15', awardName: '效能提升贡献奖', awardDate: '2024-10-12', category: 'efficiency', year: '2024', isMine: false, flowers: 14, hasGivenFlower: true, achievement: '持续优化研发流程，提升团队效能。' },
  { id: 10, name: '刘洋', department: '开源办公室', avatar: 'https://i.pravatar.cc/150?img=8', awardName: '开源贡献奖', awardDate: '2024-09-08', category: 'community', year: '2024', isMine: false, flowers: 28, hasGivenFlower: false, achievement: '积极参与开源项目，贡献高质量代码。' },
  { id: 11, name: '黄婷', department: '数据部', avatar: 'https://i.pravatar.cc/150?img=25', awardName: '数据分析之星', awardDate: '2024-08-22', category: 'practice', year: '2024', isMine: false, flowers: 19, hasGivenFlower: false, achievement: '在数据分析领域表现突出。' },
  { id: 12, name: '吴强', department: '算法部', avatar: 'https://i.pravatar.cc/150?img=13', awardName: '机器学习创新奖', awardDate: '2024-07-15', category: 'innovation', year: '2024', isMine: false, flowers: 24, hasGivenFlower: true, achievement: '在机器学习模型创新方面取得突破。' }
]

// 获取个人荣誉列表（与接口文档一致）
export const getHonorList = async (params?: {
  page?: number
  pageSize?: number
  scope?: 'all' | 'mine'
  filterType?: 'award' | 'department'
  filterValue?: string
  keyword?: string
  view?: 'grid' | 'timeline'
  userName?: string
}): Promise<{
  list: HonorListItem[]
  total: number
  page: number
  pageSize: number
  totalPages: number
}> => {
  await delay()
  
  let filtered = [...mockHonorList]
  
  // 按范围筛选
  if (params?.scope === 'mine') {
    filtered = filtered.filter(item => item.isMine)
  }
  
  // 按筛选类型筛选
  if (params?.filterType && params?.filterValue && params.filterValue !== '全部') {
    if (params.filterType === 'award') {
      filtered = filtered.filter(item => item.awardName.includes(params.filterValue!))
    } else if (params.filterType === 'department') {
      filtered = filtered.filter(item => item.department === params.filterValue)
    }
  }
  
  // 按关键词搜索
  if (params?.keyword) {
    const kw = params.keyword.toLowerCase()
    filtered = filtered.filter(item => 
      item.name.toLowerCase().includes(kw) ||
      item.awardName.toLowerCase().includes(kw) ||
      item.department.toLowerCase().includes(kw)
    )
  }
  
  // 时光轴模式下按用户筛选
  if (params?.view === 'timeline' && params?.userName) {
    filtered = filtered.filter(item => item.name === params.userName)
  }
  
  const page = params?.page || 1
  const pageSize = params?.pageSize || 16
  const total = filtered.length
  const totalPages = Math.ceil(total / pageSize)
  const start = (page - 1) * pageSize
  const list = filtered.slice(start, start + pageSize)
  
  return { list, total, page, pageSize, totalPages }
}

export const getHonors = async (params?: PaginationParams): Promise<PageResult<Honor>> => {
  await delay()
  return { list: [], total: 0, page: params?.page || 1, pageSize: params?.pageSize || 15 }
}

export const getHonorInfluence = async (): Promise<HonorInfluence> => {
  await delay()
  return { totalHonors: 0, totalUsers: 0, totalFlowers: 0, categories: [] }
}

export const getTopUsers = async (_params?: PaginationParams): Promise<PageResult<TopUser>> => {
  await delay()
  return { list: mockHonorInfo.topUsers, total: mockHonorInfo.topUsers.length, page: 1, pageSize: 10 }
}

// 获取荣誉影响力排行榜（与接口文档一致）
export const getHonorLeaderboard = async (params?: {
  limit?: number
  scope?: string
  filterType?: string
  filterValue?: string
}): Promise<{
  list: Array<{
    name: string
    department: string
    avatar: string
    count: number
    totalFlowers: number
  }>
}> => {
  await delay()
  return {
    list: [
      {
        name: '林星辰',
        department: '架构平台部',
        avatar: 'https://picsum.photos/100/100?random=leader1',
        count: 5,
        totalFlowers: 58
      },
      {
        name: '张伟',
        department: '效能工程部',
        avatar: 'https://picsum.photos/100/100?random=leader2',
        count: 4,
        totalFlowers: 45
      },
      {
        name: 'Sarah',
        department: 'UED 设计中心',
        avatar: 'https://picsum.photos/100/100?random=leader3',
        count: 3,
        totalFlowers: 32
      }
    ].slice(0, params?.limit || 10)
  }
}

// 获取荣誉时光轴（与接口文档一致）
export const getHonorTimeline = async (userName?: string): Promise<{
  user: {
    name: string
    avatar: string
    department: string
    totalFlowers: number
  } | null
  timeline: Array<{
    year: string
    items: Array<{
      id: number
      name: string
      avatar: string
      awardName: string
      awardDate: string
      category: string
    }>
  }>
}> => {
  await delay()
  
  // 如果指定了用户名，返回该用户的时光轴
  if (userName) {
    return {
      user: {
        name: userName,
        avatar: 'https://picsum.photos/100/100?random=timeline',
        department: '架构平台部',
        totalFlowers: 58
      },
      timeline: [
        {
          year: '2026',
          items: [
            {
              id: 1,
              name: userName,
              avatar: 'https://picsum.photos/100/100?random=timeline',
              awardName: '2026年度 AI 技术突破奖',
              awardDate: '2026-01-05',
              category: 'innovation'
            }
          ]
        },
        {
          year: '2025',
          items: [
            {
              id: 5,
              name: userName,
              avatar: 'https://picsum.photos/100/100?random=timeline',
              awardName: 'AI 社区贡献之星',
              awardDate: '2025-06-15',
              category: 'community'
            }
          ]
        }
      ]
    }
  }
  
  // 返回所有用户的时光轴
  return {
    user: null,
    timeline: [
      {
        year: '2026',
        items: [
          {
            id: 1,
            name: '林星辰',
            avatar: 'https://picsum.photos/100/100?random=t1',
            awardName: '2026年度 AI 技术突破奖',
            awardDate: '2026-01-05',
            category: 'innovation'
          },
          {
            id: 2,
            name: 'Sarah',
            avatar: 'https://picsum.photos/100/100?random=t2',
            awardName: '最佳 AI 辅助设计实践',
            awardDate: '2025-12-20',
            category: 'practice'
          }
        ]
      }
    ]
  }
}

// 获取荣誉部门列表（用于筛选）
export const getHonorDepartments = async (): Promise<{ list: string[] }> => {
  await delay()
  return {
    list: [
      '全部',
      '架构平台部',
      'UED 设计中心',
      '效能工程部',
      '开源办公室',
      '数据部',
      '算法部'
    ]
  }
}

export const giveFlower = async (_id: number): Promise<{ flowers: number; hasGivenFlower: boolean }> => {
  await delay()
  return { flowers: 1, hasGivenFlower: true }
}

// 工具相关
export const getToolDetail = async (id: number): Promise<ToolItem | undefined> => {
  await delay()
  return mockTools.find(t => t.id === id) || mockTools[0]
}

// 获取工具专区部门统计（与接口文档一致）
export const getToolDepartments = async (toolId: number, tag?: string): Promise<{
  list: Array<{
    id: number
    name: string
    postCount: number
    contributorCount: number
  }>
}> => {
  await delay()
  
  // 模拟根据toolId和tag过滤后的部门统计
  const departments = [
    { id: 1, name: '研发部', postCount: 45, contributorCount: 12 },
    { id: 2, name: '技术部', postCount: 38, contributorCount: 9 },
    { id: 3, name: '算法部', postCount: 25, contributorCount: 6 },
    { id: 4, name: '数据部', postCount: 22, contributorCount: 8 },
    { id: 5, name: '测试部', postCount: 18, contributorCount: 5 }
  ]
  
  console.log(`[Mock API] 获取工具专区部门统计 (toolId: ${toolId}, tag: ${tag})`)
  return { list: departments }
}

export const checkToolOwner = async (id: number): Promise<ToolOwnerCheck> => {
  await delay()
  // 在mock环境中，默认返回isOwner为true，以便测试发布活动功能
  // 返回完整的权限信息，包括permissions数组
  return {
    isOwner: true,
    toolId: id,
    permissions: ['publish_activity', 'manage_posts', 'set_featured']
  }
}

// Agent相关
export const getFeaturedPost = async (): Promise<FeaturedPostResponse> => {
  await delay()
  // 返回扶摇Agent的置顶帖子
  const featuredPost = mockPosts.find(p => p.zone === 'agent' && (p.featured || p.isFeatured))
  return { post: featuredPost || null }
}

// 获取工具专区精华帖子（仅适用于"其他工具" toolId=0）
export const getToolFeaturedPost = async (toolId: number): Promise<FeaturedPostResponse> => {
  await delay()
  // 只有"其他工具"支持精华帖子
  if (toolId !== 0) {
    return { post: null }
  }
  const featuredPost = mockPosts.find(p => p.toolId === 0 && (p.featured || p.isFeatured))
  return { post: featuredPost || null }
}

// ========== 赋能交流页面 ==========

// 赋能交流帖子类型
interface EmpowermentPost {
  id: number
  title: string
  description: string
  author: string
  authorId: number
  authorAvatar: string
  createTime: string
  views: number
  comments: number
  likes: number
  tags: string[]
  image: string
  cover?: string
  featured?: boolean
}

// 赋能交流精华帖子
const empowermentFeaturedPosts: EmpowermentPost[] = [
  {
    id: 1001,
    title: '如何高效使用Agent提升开发效率',
    description: '分享使用Agent工具在开发过程中的最佳实践和技巧。',
    author: '张工程师',
    authorId: 101,
    authorAvatar: 'https://picsum.photos/100/100?random=1001',
    createTime: '2026-01-10T10:30:00Z',
    views: 1250,
    comments: 45,
    likes: 128,
    tags: ['讨论', 'Agent'],
    image: 'https://picsum.photos/800/400?random=1001',
    cover: 'https://picsum.photos/800/400?random=1001',
    featured: true
  }
]

// 赋能交流普通帖子
const empowermentPosts: EmpowermentPost[] = [
  {
    id: 1002,
    title: 'Prompt工程的最佳实践分享',
    description: '如何编写高质量的Prompt，提升AI模型输出效果。',
    author: '李开发者',
    authorId: 102,
    authorAvatar: 'https://picsum.photos/100/100?random=1002',
    createTime: '2026-01-12T14:30:00Z',
    views: 890,
    comments: 32,
    likes: 75,
    tags: ['分享', 'Prompt'],
    image: 'https://picsum.photos/400/300?random=1002'
  },
  {
    id: 1003,
    title: '大模型微调 vs 提示工程的选择',
    description: '讨论在不同场景下应该选择微调还是提示工程。',
    author: '王测试',
    authorId: 103,
    authorAvatar: 'https://picsum.photos/100/100?random=1003',
    createTime: '2026-01-11T09:00:00Z',
    views: 650,
    comments: 18,
    likes: 42,
    tags: ['讨论', '微调'],
    image: 'https://picsum.photos/400/300?random=1003'
  },
  {
    id: 1004,
    title: 'AI工具链的构建与优化',
    description: '分享如何构建高效的AI工具链，提升团队协作效率。',
    author: '赵医生',
    authorId: 104,
    authorAvatar: 'https://picsum.photos/100/100?random=1004',
    createTime: '2026-01-10T15:00:00Z',
    views: 520,
    comments: 15,
    likes: 28,
    tags: ['工具', '经验'],
    image: 'https://picsum.photos/400/300?random=1004'
  },
  {
    id: 1005,
    title: '如何解决Agent执行中的常见问题',
    description: '总结Agent使用过程中遇到的问题及解决方案。',
    author: '陈架构师',
    authorId: 105,
    authorAvatar: 'https://picsum.photos/100/100?random=1005',
    createTime: '2026-01-09T11:00:00Z',
    views: 720,
    comments: 28,
    likes: 65,
    tags: ['问题解决', 'Agent'],
    image: 'https://picsum.photos/400/300?random=1005'
  },
  {
    id: 1006,
    title: 'Prompt模板库分享',
    description: '分享常用的Prompt模板，提高工作效率。',
    author: '刘设计师',
    authorId: 106,
    authorAvatar: 'https://picsum.photos/100/100?random=1006',
    createTime: '2026-01-08T16:00:00Z',
    views: 450,
    comments: 12,
    likes: 19,
    tags: ['分享', 'Prompt'],
    image: 'https://picsum.photos/400/300?random=1006'
  }
]

// 获取赋能交流精华帖子（不参与分页和筛选）
export const getEmpowermentFeaturedPosts = async (): Promise<{ list: EmpowermentPost[] }> => {
  await delay()
  return { list: empowermentFeaturedPosts }
}

// 获取赋能交流普通帖子列表
export const getEmpowermentPosts = async (params?: {
  tag?: string
  keyword?: string
  sortBy?: string
  page?: number
  pageSize?: number
}): Promise<{ list: EmpowermentPost[]; total: number; page: number; pageSize: number }> => {
  await delay()

  let result = [...empowermentPosts]

  // 标签筛选
  if (params?.tag && params.tag !== '全部') {
    result = result.filter(post => post.tags && post.tags.includes(params.tag!))
  }

  // 搜索
  if (params?.keyword) {
    const keyword = params.keyword.toLowerCase()
    result = result.filter(post =>
      post.title.toLowerCase().includes(keyword) ||
      post.author.toLowerCase().includes(keyword) ||
      post.description.toLowerCase().includes(keyword)
    )
  }

  // 排序
  if (params?.sortBy === 'hot') {
    result.sort((a, b) => b.views - a.views)
  } else if (params?.sortBy === 'comments') {
    result.sort((a, b) => b.comments - a.comments)
  } else if (params?.sortBy === 'likes') {
    result.sort((a, b) => b.likes - a.likes)
  } else {
    // 默认按时间排序
    result.sort((a, b) => new Date(b.createTime).getTime() - new Date(a.createTime).getTime())
  }

  const total = result.length
  const page = params?.page || 1
  const pageSize = params?.pageSize || 15
  const start = (page - 1) * pageSize
  const end = start + pageSize

  return {
    list: result.slice(start, end),
    total,
    page,
    pageSize
  }
}

// 获取赋能交流标签统计
export const getEmpowermentTags = async (): Promise<{ list: Array<{ name: string; count: number }> }> => {
  await delay()

  // 合并精华帖和普通帖子统计标签
  const allPosts = [...empowermentFeaturedPosts, ...empowermentPosts]
  const tagCountMap = new Map<string, number>()

  allPosts.forEach(post => {
    if (post.tags) {
      post.tags.forEach(tag => {
        tagCountMap.set(tag, (tagCountMap.get(tag) || 0) + 1)
      })
    }
  })

  const tags: Array<{ name: string; count: number }> = [
    { name: '全部', count: allPosts.length }
  ]

  tagCountMap.forEach((count, name) => {
    tags.push({ name, count })
  })

  // 按数量排序（全部除外）
  tags.sort((a, b) => {
    if (a.name === '全部') return -1
    if (b.name === '全部') return 1
    return b.count - a.count
  })

  return { list: tags }
}

// 精选合集类型
interface Collection {
  id: number
  title: string
  description: string
  cover: string
  postCount: number
  viewCount: number
}

// 获取赋能交流精选合集
export const getEmpowermentCollections = async (limit: number = 5): Promise<{ list: Collection[] }> => {
  await delay()

  const collections: Collection[] = [
    {
      id: 1,
      title: '顶级AI研究论文',
      description: '精选AI领域最具影响力的研究论文合集',
      cover: 'https://picsum.photos/300/200?random=100',
      postCount: 25,
      viewCount: 8500
    },
    {
      id: 2,
      title: 'Prompt工程精华',
      description: 'Prompt编写技巧和最佳实践合集',
      cover: 'https://picsum.photos/300/200?random=101',
      postCount: 18,
      viewCount: 6200
    }
  ]

  return { list: collections.slice(0, limit) }
}

// 设置赋能交流精华帖子
export const setEmpowermentFeaturedPost = async (params: { postId: number; featured: boolean }): Promise<{ postId: number; featured: boolean; setTime: string }> => {
  await delay()

  if (params.featured) {
    // 设为精华：将帖子从普通帖子移到精华帖子
    const postIndex = empowermentPosts.findIndex(p => p.id === params.postId)
    if (postIndex !== -1) {
      const [post] = empowermentPosts.splice(postIndex, 1)
      if (post) {
        post.featured = true
        empowermentFeaturedPosts.push(post)
      }
    }
  } else {
    // 取消精华：将帖子从精华帖子移回普通帖子
    const featuredIndex = empowermentFeaturedPosts.findIndex(p => p.id === params.postId)
    if (featuredIndex !== -1) {
      const [post] = empowermentFeaturedPosts.splice(featuredIndex, 1)
      if (post) {
        post.featured = false
        empowermentPosts.unshift(post)
      }
    }
  }

  return {
    postId: params.postId,
    featured: params.featured,
    setTime: new Date().toISOString()
  }
}

// 设置AI工具专区其他工具精华帖子
export const setOtherToolsFeaturedPost = async (params: { toolId: number; postId: number | null }): Promise<{ postId: number | null; setTime: string }> => {
  await delay()

  // toolId=0 表示"其他工具"
  if (params.toolId !== 0) {
    throw new Error('此接口仅支持toolId=0的"其他工具"')
  }

  // 更新帖子的精华状态
  const allOtherToolsPosts = mockPosts.filter(p => p.toolId === 0)

  // 先取消之前的精华
  allOtherToolsPosts.forEach(p => {
    p.featured = false
    p.isFeatured = false
  })

  // 设置新的精华帖子
  if (params.postId) {
    const post = mockPosts.find(p => p.id === params.postId && p.toolId === 0)
    if (post) {
      post.featured = true
      post.isFeatured = true
    }
  }

  return {
    postId: params.postId,
    setTime: new Date().toISOString()
  }
}

// 用户达人相关
export const getTeamAwards = async (_year?: string): Promise<{ list: TeamAward[] }> => {
  await delay()
  return { list: mockTeamAwards }
}

export const getTeamAwardDetail = async (id: number): Promise<TeamAward> => {
  await delay()
  return mockTeamAwards.find(a => a.id === id) ?? mockTeamAwards[0]!
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

export const getHonorBannerConfig = async (): Promise<{ bannerImage: string; bannerImageType?: 'url' | 'upload' }> => {
  await delay()
  return { bannerImage: mockHonorInfo.bannerImage, bannerImageType: 'url' }
}

export const saveHonorBannerConfig = async (config: { bannerImage: string }): Promise<void> => {
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

// 个人奖项配置类型
// 个人奖项配置类型（兼容管理后台的 AwardItem）
interface PersonalAwardConfig {
  id: number
  name: string
  description?: string
  category?: string
  criteria?: string[]
  cycle?: string
  year?: number
  saving?: boolean
  saved?: boolean
}

// 获奖者配置类型（兼容管理后台的 WinnerItem）
interface WinnerConfig {
  id: number
  name?: string
  awardTime?: string
  awardName?: string
  userId?: number
  userName?: string
  awardId?: number
  year?: number
}

export const getPersonalAwardsConfig = async (): Promise<{ list: PersonalAwardConfig[] }> => {
  await delay()
  return { list: [] }
}

export const savePersonalAwardsConfig = async (_list: PersonalAwardConfig[]): Promise<void> => {
  await delay()
}

export const getWinnersConfig = async (): Promise<{ list: WinnerConfig[] }> => {
  await delay()
  return { list: [] }
}

export const saveWinnersConfig = async (_list: WinnerConfig[]): Promise<void> => {
  await delay()
}

// 赋能交流精华帖子配置
export const getEmpowermentFeaturedPostsConfig = async (): Promise<{ list: Array<{ id: number; postId: number; note: string }> }> => {
  await delay()
  return {
    list: empowermentFeaturedPosts.map(p => ({
      id: p.id,
      postId: p.id,
      note: p.title
    }))
  }
}

export const saveEmpowermentFeaturedPostsConfig = async (_list: Array<{ id: number; postId: number | null; note: string }>): Promise<void> => {
  await delay()
  // 在实际场景中，这里会更新精华帖子列表
  console.log('保存赋能交流精华帖子配置:', _list)
}

// AI工具专区其他工具精华帖子配置
export const getOtherToolsFeaturedPostsConfig = async (): Promise<{ list: Array<{ id: number; postId: number; note: string }> }> => {
  await delay()
  const otherToolsFeaturedPost = mockPosts.find(p => p.toolId === 0 && (p.featured || p.isFeatured))
  if (otherToolsFeaturedPost) {
    return {
      list: [{
        id: otherToolsFeaturedPost.id,
        postId: otherToolsFeaturedPost.id,
        note: otherToolsFeaturedPost.title || ''
      }]
    }
  }
  return { list: [] }
}

export const saveOtherToolsFeaturedPostsConfig = async (_list: Array<{ id: number; postId: number | null; note: string }>): Promise<void> => {
  await delay()
  // 在实际场景中，这里会更新精华帖子列表
  console.log('保存AI工具专区其他工具精华帖子配置:', _list)
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

// 推荐获奖者类型
// 推荐获奖者类型（兼容 AdminView 中的 RecommendedWinner）
export interface RecommendedWinner {
  id: number
  employeeId: string
  name: string
  avatar: string
  department: string
  points: number
  postsCount: number
  commentsCount: number
  activitiesCount: number
  likesReceived: number
  favoritesReceived: number
  hasAwarded: boolean
  honorId?: number
}

// 设置奖项参数类型
interface SetUserAwardParams {
  userId: number
  awardId: number
  awardName?: string
  awardDate?: string
  category?: string
  year?: number
  reason?: string
}

// 搜索用户参数类型
interface SearchUsersParams {
  keyword?: string
  department?: string
  role?: 'admin' | 'tool_owner' | 'user'
  page?: number
  pageSize?: number
}

// 用户列表项类型
interface UserListItem {
  id: number
  name: string
  email: string
  avatar: string
  department: string
  employeeId: string
  currentRole: 'user' | 'admin' | 'tool_owner'
}

// 角色参数类型
interface RoleParams {
  role: string
  toolId?: number
}

// 奖项列表项类型（管理后台设置，同时用于奖项规则说明展示）
export interface AwardListItem {
  id: number
  name: string
  description?: string    // 奖项简要描述
  category?: string       // 奖项类别
  criteria?: string[]     // 评选标准列表
  cycle?: string          // 评选周期：年度/季度/月度
}

export const getRecommendedWinners = async (month?: string, _limit: number = 3): Promise<{ list: RecommendedWinner[]; month?: string }> => {
  await delay()
  return { list: [], month }
}

export const setUserAward = async (_params: SetUserAwardParams): Promise<{ id: number; message?: string }> => {
  await delay()
  return { id: Date.now(), message: '设置成功' }
}

export const cancelUserAward = async (_id: number): Promise<void> => {
  await delay()
}

// Mock 奖项列表数据（管理后台设置，同时用于奖项规则说明展示）
const mockAwardsList: AwardListItem[] = [
  {
    id: 1,
    name: '技术创新奖',
    description: '表彰在AI技术方案上有重大突破的个人或团队',
    criteria: ['提交创新方案不少于2篇', '落地至少1个生产项目', '产出技术分享或专利'],
    cycle: '年度'
  },
  {
    id: 2,
    name: '效能提升奖',
    description: '在工程效能、自动化与质量提升方面贡献突出',
    criteria: ['引入自动化工具并落地', '显著降低缺陷率或提升交付速度'],
    cycle: '季度'
  },
  {
    id: 3,
    name: '最佳实践奖',
    description: '在业务场景中形成可复制的AI最佳实践并推广',
    criteria: ['形成完整案例文档', '内部分享不少于2场', '被至少一个团队复用'],
    cycle: '季度'
  },
  {
    id: 4,
    name: '社区贡献奖',
    description: '对社区布道、开源贡献或知识传播有突出表现',
    criteria: ['发布高质量文章/视频', '组织或参与社区活动', '持续开源贡献'],
    cycle: '年度'
  }
]

export const getAwardsList = async (_category?: string): Promise<{ list: AwardListItem[] }> => {
  await delay()
  return { list: mockAwardsList }
}

// 获取奖项规则说明（直接从管理后台的奖项设置中读取）
// 注意：AwardRuleDetail 类型与 AwardListItem 相同，直接复用
export type AwardRuleDetail = AwardListItem

export const getAwardRules = async (): Promise<{
  list: AwardListItem[]
  updateTime: string
}> => {
  await delay()
  // 直接返回管理后台设置的奖项列表
  return {
    list: mockAwardsList,
    updateTime: new Date().toISOString()
  }
}

// 保存单个奖项（新增或更新，支持评选标准和周期）
export const saveAward = async (award: {
  id?: number
  name: string
  description?: string
  criteria?: string[]
  cycle?: string
}): Promise<AwardListItem> => {
  await delay()
  if (award.id !== undefined) {
    // 更新现有奖项
    const existing = mockAwardsList.find(a => a.id === award.id)
    if (existing) {
      const updated: AwardListItem = {
        id: award.id,
        name: award.name,
        description: award.description,
        criteria: award.criteria || existing.criteria || [],
        cycle: award.cycle || existing.cycle || '年度'
      }
      const index = mockAwardsList.indexOf(existing)
      mockAwardsList[index] = updated
      return updated
    }
  }
  // 新增奖项
  const newAward: AwardListItem = {
    id: Date.now(),
    name: award.name,
    description: award.description || '',
    criteria: award.criteria || [],
    cycle: award.cycle || '年度'
  }
  mockAwardsList.push(newAward)
  return newAward
}

// 删除奖项
export const deleteAward = async (id: number): Promise<{ success: boolean; message?: string }> => {
  await delay()
  const index = mockAwardsList.findIndex(a => a.id === id)
  if (index !== -1) {
    mockAwardsList.splice(index, 1)
    return { success: true }
  }
  return { success: false, message: '奖项不存在' }
}

export const searchUsers = async (_params: SearchUsersParams): Promise<{ list: UserListItem[] }> => {
  await delay()
  return { list: [] }
}

// Mock 用户列表数据
const mockUsersList: UserListItem[] = [
  {
    id: 1,
    name: '张三',
    email: 'zhangsan@example.com',
    avatar: 'https://picsum.photos/100/100?random=1',
    department: '研发部',
    employeeId: 'E001',
    currentRole: 'admin'
  },
  {
    id: 2,
    name: '李四',
    email: 'lisi@example.com',
    avatar: 'https://picsum.photos/100/100?random=2',
    department: '产品部',
    employeeId: 'E002',
    currentRole: 'tool_owner'
  },
  {
    id: 3,
    name: '王五',
    email: 'wangwu@example.com',
    avatar: 'https://picsum.photos/100/100?random=3',
    department: '技术部',
    employeeId: 'E003',
    currentRole: 'user'
  },
  {
    id: 4,
    name: '赵六',
    email: 'zhaoliu@example.com',
    avatar: 'https://picsum.photos/100/100?random=4',
    department: '数据部',
    employeeId: 'E004',
    currentRole: 'user'
  },
  {
    id: 5,
    name: '孙七',
    email: 'sunqi@example.com',
    avatar: 'https://picsum.photos/100/100?random=5',
    department: '运营部',
    employeeId: 'E005',
    currentRole: 'user'
  }
]

export const getUsersList = async (params?: SearchUsersParams): Promise<{ list: UserListItem[]; total: number }> => {
  await delay()
  let list = mockUsersList

  // 关键词搜索
  if (params?.keyword) {
    const keyword = params.keyword.toLowerCase()
    list = list.filter(u =>
      u.name.toLowerCase().includes(keyword) ||
      u.email.toLowerCase().includes(keyword)
    )
  }

  // 角色筛选
  if (params?.role) {
    list = list.filter(u => u.currentRole === params.role)
  }

  return { list, total: list.length }
}

export const addUserRole = async (_userId: number, _params: RoleParams): Promise<void> => {
  await delay()
}

export const removeUserRole = async (_userId: number, _params: RoleParams): Promise<void> => {
  await delay()
}

export const uploadImage = async (file: File): Promise<{ url: string }> => {
  await delay()
  return { url: URL.createObjectURL(file) }
}

// 其他
export const getHotPosts = async (_params?: PaginationParams): Promise<{ list: Post[] }> => {
  await delay()
  return { list: mockPosts.slice(0, 10) }
}

export const getTags = async (params: { toolId?: number; zone?: string }): Promise<{ list: Array<{ name: string; count: number }> }> => {
  await delay()
  // 根据toolId返回不同的标签
  if (params?.toolId === -1) {
    // 扶摇Agent应用标签
    return {
      list: [
        { name: 'Agent应用', count: 12 },
        { name: '工作流', count: 8 },
        { name: '自动化', count: 15 },
        { name: '智能编排', count: 10 },
        { name: '最佳实践', count: 6 },
        { name: '案例分享', count: 9 },
        { name: '开发指南', count: 5 }
      ]
    }
  }
  if (params?.toolId === 1) {
    // TestMate工具标签
    return {
      list: [
        { name: '新手', count: 15 },
        { name: '进阶', count: 12 },
        { name: '自动化', count: 18 },
        { name: '性能测试', count: 8 },
        { name: '接口测试', count: 10 },
        { name: '最佳实践', count: 14 },
        { name: '案例', count: 9 },
        { name: 'DevOps', count: 6 },
        { name: '技巧', count: 11 },
        { name: '移动端', count: 5 },
        { name: '优化', count: 7 }
      ]
    }
  }
  if (params?.toolId === 0) {
    // 其他工具标签
    return {
      list: [
        { name: '效率提升', count: 18 },
        { name: '最佳实践', count: 15 },
        { name: '进阶', count: 12 },
        { name: '新手', count: 10 },
        { name: '技巧', count: 14 },
        { name: '设计', count: 8 },
        { name: 'ChatGPT', count: 9 },
        { name: 'Copilot', count: 6 },
        { name: '文档', count: 5 }
      ]
    }
  }
  // 默认标签
  return {
    list: [
      { name: '使用技巧', count: 20 },
      { name: '问题解决', count: 15 },
      { name: '经验分享', count: 12 }
    ]
  }
}

// 部门类型
interface Department {
  id: number
  name: string
  code: string
}

// 贡献者类型
interface Contributor {
  id: number
  name: string
  avatar: string
  department: string
  postsCount: number
  likesCount: number
}

// 奖项规则类型
interface AwardRules {
  awardId: number
  awardName: string
  rules: string
}

// 登录参数类型
interface LoginParams {
  employeeId: string  // 工号（与接口文档一致）
  password: string
}

// 登录响应类型
interface LoginResponse {
  token: string
  expiresIn?: number  // 令牌有效期（秒）
  user: UserProfile
}

export const getDepartments = async (_params?: PaginationParams): Promise<{ list: Department[] }> => {
  await delay()
  return { list: [] }
}

export const getTopContributors = async (_params?: PaginationParams): Promise<{ list: Contributor[] }> => {
  await delay()
  return { list: [] }
}

export const getAwardRulesById = async (id: number): Promise<AwardRules> => {
  await delay()
  return { awardId: id, awardName: '奖项名称', rules: '奖项规则' }
}

export const login = async (_data: LoginParams): Promise<LoginResponse> => {
  await delay()
  return { token: 'mock_token', user: mockCurrentUser }
}

export const logout = async (): Promise<void> => {
  await delay()
}

// ========== AI优秀实践页面专用 API ==========

// AI优秀实践帖子列表查询参数
interface PracticesPostsParams {
  page?: number
  pageSize?: number
  keyword?: string
  tag?: string
  department?: string
  contributor?: string
  sortBy?: 'newest' | 'hot' | 'comments' | 'likes'
}

// 获取AI优秀实践帖子列表（含精华帖子）
export const getPracticePosts = async (params?: PracticesPostsParams): Promise<{
  list: Post[]
  featuredPosts: Post[]
  total: number
  page: number
  pageSize: number
  totalPages: number
}> => {
  await delay()

  // 获取所有 practices zone 的帖子
  const allPracticesPosts = mockPosts.filter(p => p.zone === 'practices' || !p.zone || (!p.toolId && p.toolId !== 0))

  // 精华帖子
  const featuredPosts = allPracticesPosts.filter(p => p.featured || p.isFeatured)

  // 普通帖子
  let normalPosts = allPracticesPosts.filter(p => !p.featured && !p.isFeatured)

  // 标签筛选
  if (params?.tag && params.tag !== '全部') {
    normalPosts = normalPosts.filter(p => p.tags?.includes(params.tag!) || p.tag === params.tag)
  }

  // 部门筛选
  if (params?.department) {
    normalPosts = normalPosts.filter(p => p.department === params.department)
  }

  // 贡献者筛选
  if (params?.contributor) {
    normalPosts = normalPosts.filter(p => p.author === params.contributor || p.authorName === params.contributor)
  }

  // 搜索
  if (params?.keyword) {
    const keyword = params.keyword.toLowerCase()
    normalPosts = normalPosts.filter(p =>
      p.title.toLowerCase().includes(keyword) ||
      (p.author && p.author.toLowerCase().includes(keyword)) ||
      (p.authorName && p.authorName.toLowerCase().includes(keyword)) ||
      (p.description && p.description.toLowerCase().includes(keyword))
    )
  }

  // 排序
  if (params?.sortBy === 'hot') {
    normalPosts.sort((a, b) => (b.views || 0) - (a.views || 0))
  } else if (params?.sortBy === 'comments') {
    normalPosts.sort((a, b) => (b.comments || 0) - (a.comments || 0))
  } else if (params?.sortBy === 'likes') {
    normalPosts.sort((a, b) => (b.likes || 0) - (a.likes || 0))
  } else {
    // 默认按时间排序（最新）
    normalPosts.sort((a, b) => b.id - a.id)
  }

  const page = params?.page || 1
  const pageSize = params?.pageSize || 15
  const total = normalPosts.length
  const totalPages = Math.ceil(total / pageSize)

  // 分页
  const start = (page - 1) * pageSize
  const paginatedPosts = normalPosts.slice(start, start + pageSize)

  return {
    list: paginatedPosts,
    featuredPosts,
    total,
    page,
    pageSize,
    totalPages
  }
}

// 获取最热帖子Top N
export const getPracticeHotPosts = async (limit: number = 3): Promise<{ list: Array<{ id: number; title: string; views: number; rank: number }> }> => {
  await delay()

  // 获取所有 practices zone 的帖子
  const practicesPosts = mockPosts.filter(p => p.zone === 'practices' || !p.zone || (!p.toolId && p.toolId !== 0))

  // 按浏览量排序
  const sortedPosts = [...practicesPosts].sort((a, b) => (b.views || 0) - (a.views || 0))

  // 取前N个
  const hotPosts = sortedPosts.slice(0, limit).map((post, index) => ({
    id: post.id,
    title: post.title,
    views: post.views || 0,
    rank: index + 1
  }))

  return { list: hotPosts }
}

// 获取标签统计列表
export const getPracticeTags = async (department?: string): Promise<{ list: Array<{ name: string; count: number }> }> => {
  await delay()

  // 获取所有 practices zone 的帖子
  let allPosts = mockPosts.filter(p => p.zone === 'practices' || !p.zone || (!p.toolId && p.toolId !== 0))

  // 按部门过滤
  if (department) {
    allPosts = allPosts.filter(p => p.department === department)
  }

  // 统计标签
  const tagCountMap = new Map<string, number>()
  allPosts.forEach(post => {
    if (post.tags) {
      post.tags.forEach(tag => {
        tagCountMap.set(tag, (tagCountMap.get(tag) || 0) + 1)
      })
    }
    if (post.tag && !post.tags?.includes(post.tag)) {
      tagCountMap.set(post.tag, (tagCountMap.get(post.tag) || 0) + 1)
    }
  })

  const tags: Array<{ name: string; count: number }> = [
    { name: '全部', count: allPosts.length }
  ]

  tagCountMap.forEach((count, name) => {
    tags.push({ name, count })
  })

  // 按数量排序
  tags.sort((a, b) => {
    if (a.name === '全部') return -1
    if (b.name === '全部') return 1
    return b.count - a.count
  })

  return { list: tags }
}

// 获取部门排名列表
export const getPracticeDepartments = async (tag?: string): Promise<{ list: Array<{ id: number; name: string; postCount: number; contributorCount: number }> }> => {
  await delay()

  // 获取所有 practices zone 的帖子
  let allPosts = mockPosts.filter(p => p.zone === 'practices' || !p.zone || (!p.toolId && p.toolId !== 0))

  // 按标签过滤
  if (tag && tag !== '全部') {
    allPosts = allPosts.filter(p => p.tags?.includes(tag) || p.tag === tag)
  }

  // 统计部门
  const deptMap = new Map<string, { postCount: number; contributors: Set<string> }>()

  allPosts.forEach(post => {
    const dept = post.department || '未分类'
    if (!deptMap.has(dept)) {
      deptMap.set(dept, { postCount: 0, contributors: new Set() })
    }
    const deptStats = deptMap.get(dept)!
    deptStats.postCount++
    if (post.author || post.authorName) {
      deptStats.contributors.add(post.author || post.authorName || '')
    }
  })

  const departments: Array<{ id: number; name: string; postCount: number; contributorCount: number }> = []
  let id = 1
  deptMap.forEach((stats, name) => {
    departments.push({
      id: id++,
      name,
      postCount: stats.postCount,
      contributorCount: stats.contributors.size
    })
  })

  // 按发帖数排序
  departments.sort((a, b) => b.postCount - a.postCount)

  return { list: departments }
}

// 获取热门贡献者列表
export const getPracticeContributors = async (limit: number = 5): Promise<{ list: Array<{ id: number; name: string; avatar: string; postCount: number; department: string }> }> => {
  await delay()

  // 获取所有 practices zone 的帖子
  const allPosts = mockPosts.filter(p => p.zone === 'practices' || !p.zone || (!p.toolId && p.toolId !== 0))

  // 统计贡献者
  const contributorMap = new Map<string, { postCount: number; avatar: string; department: string }>()

  allPosts.forEach(post => {
    const name = post.author || post.authorName || '匿名'
    if (!contributorMap.has(name)) {
      contributorMap.set(name, {
        postCount: 0,
        avatar: post.authorAvatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        department: post.department || '未知部门'
      })
    }
    contributorMap.get(name)!.postCount++
  })

  const contributors: Array<{ id: number; name: string; avatar: string; postCount: number; department: string }> = []
  let id = 1
  contributorMap.forEach((stats, name) => {
    contributors.push({
      id: id++,
      name,
      avatar: stats.avatar,
      postCount: stats.postCount,
      department: stats.department
    })
  })

  // 按发帖数排序并取前N个
  contributors.sort((a, b) => b.postCount - a.postCount)

  return { list: contributors.slice(0, limit) }
}
