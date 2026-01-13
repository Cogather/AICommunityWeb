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

// 新闻相关
export const getNews = async (): Promise<{ list: NewsItem[] }> => {
  await delay()
  return { list: mockNews }
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
    filteredPosts = filteredPosts.filter(p => p.tag === params.tag || p.tags?.includes(params.tag))
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

export const getRecommendedCovers = async (params?: { zone?: string; count?: number }): Promise<any[]> => {
  await delay()
  const count = params?.count || 3
  const covers = [
    { id: 1, url: 'https://picsum.photos/800/400?random=cover1', name: '科技蓝' },
    { id: 2, url: 'https://picsum.photos/800/400?random=cover2', name: '简约白' },
    { id: 3, url: 'https://picsum.photos/800/400?random=cover3', name: '渐变紫' },
    { id: 4, url: 'https://picsum.photos/800/400?random=cover4', name: '活力橙' },
    { id: 5, url: 'https://picsum.photos/800/400?random=cover5', name: '清新绿' }
  ]
  return covers.slice(0, count)
}

// 草稿存储（模拟后端存储）
// 草稿双重保存策略：
// - 前端 localStorage: 短时间存储（2秒防抖自动保存）
// - 后端服务器: 长时间存储（每3分钟同步一次）
let mockDraftStorage: any = null

export const saveDraft = async (data: any): Promise<any> => {
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

export const getDraft = async (): Promise<any> => {
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

export const deleteDraft = async (): Promise<any> => {
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
  // 返回完整的权限信息，包括permissions数组
  return {
    isOwner: true,
    toolId: id,
    permissions: ['publish_activity', 'manage_posts', 'set_featured']
  }
}

// Agent相关
export const getFeaturedPost = async (): Promise<any> => {
  await delay()
  // 返回扶摇Agent的置顶帖子
  const featuredPost = mockPosts.find(p => p.zone === 'agent' && (p.featured || p.isFeatured))
  return { post: featuredPost || null }
}

// 获取工具专区精华帖子（仅适用于"其他工具" toolId=0）
export const getToolFeaturedPost = async (toolId: number): Promise<any> => {
  await delay()
  // 只有"其他工具"支持精华帖子
  if (toolId !== 0) {
    return { post: null }
  }
  const featuredPost = mockPosts.find(p => p.toolId === 0 && (p.featured || p.isFeatured))
  return { post: featuredPost || null }
}

// ========== 赋能交流页面 ==========

// 赋能交流精华帖子
const empowermentFeaturedPosts = [
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
const empowermentPosts = [
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
export const getEmpowermentFeaturedPosts = async (): Promise<{ list: any[] }> => {
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
}): Promise<{ list: any[]; total: number; page: number; pageSize: number }> => {
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

// 获取赋能交流精选合集
export const getEmpowermentCollections = async (limit: number = 5): Promise<{ list: any[] }> => {
  await delay()

  const collections = [
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
      const post = empowermentPosts.splice(postIndex, 1)[0]
      post.featured = true
      empowermentFeaturedPosts.push(post)
    }
  } else {
    // 取消精华：将帖子从精华帖子移回普通帖子
    const featuredIndex = empowermentFeaturedPosts.findIndex(p => p.id === params.postId)
    if (featuredIndex !== -1) {
      const post = empowermentFeaturedPosts.splice(featuredIndex, 1)[0]
      post.featured = false
      empowermentPosts.unshift(post)
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

// 赋能交流精华帖子配置
export const getEmpowermentFeaturedPostsConfig = async (): Promise<{ list: Array<{ id: number; url: string; note: string }> }> => {
  await delay()
  return {
    list: empowermentFeaturedPosts.map(p => ({
      id: p.id,
      url: `/post/${p.id}`,
      note: p.title
    }))
  }
}

export const saveEmpowermentFeaturedPostsConfig = async (_list: Array<{ id: number; url: string; note: string }>): Promise<void> => {
  await delay()
  // 在实际场景中，这里会更新精华帖子列表
  console.log('保存赋能交流精华帖子配置:', _list)
}

// AI工具专区其他工具精华帖子配置
export const getOtherToolsFeaturedPostsConfig = async (): Promise<{ list: Array<{ id: number; url: string; note: string }> }> => {
  await delay()
  const otherToolsFeaturedPost = mockPosts.find(p => p.toolId === 0 && (p.featured || p.isFeatured))
  if (otherToolsFeaturedPost) {
    return {
      list: [{
        id: otherToolsFeaturedPost.id,
        url: `/post/${otherToolsFeaturedPost.id}`,
        note: otherToolsFeaturedPost.title || ''
      }]
    }
  }
  return { list: [] }
}

export const saveOtherToolsFeaturedPostsConfig = async (_list: Array<{ id: number; url: string; note: string }>): Promise<void> => {
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

export const getTags = async (params: any): Promise<{ list: Array<{ name: string; count: number }> }> => {
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
