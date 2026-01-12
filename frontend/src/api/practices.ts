// AI优秀实践页面相关接口
import request from './request'

// ========== 帖子相关 ==========

// 帖子接口（匹配后端Post实体）
export interface Post {
  id: number
  title: string
  summary?: string // 内容简介（后端字段）
  description?: string // 前端使用的描述字段
  content?: string // 帖子内容（HTML格式）
  authorName?: string // 后端字段
  author?: string // 前端使用的作者字段
  authorAvatar?: string // 作者头像
  authorId?: number // 作者ID
  createTime: string | Date // 创建时间
  updateTime?: string | Date // 更新时间
  views: number
  comments: number
  likes: number
  tags?: string[] // 标签列表
  tag?: string // 单个标签（兼容字段）
  department?: string
  cover?: string // 封面图URL
  image?: string // 封面图URL（兼容字段）
  featured?: boolean // 是否精选
  isFeatured?: boolean // 后端字段
  zone?: 'practices' | 'tools' | 'agent' | 'empowerment'
  toolId?: number
  toolName?: string
  isLiked?: boolean // 是否已点赞
  isCollected?: boolean // 是否已收藏
  isAuthor?: boolean // 是否是作者
  canEdit?: boolean // 是否可以编辑
  canDelete?: boolean // 是否可以删除
}

// 帖子列表响应
export interface PostsResponse {
  list: Post[]
  total: number
  page: number
  pageSize: number
}

// 获取帖子列表（带搜索和排序功能）
// 用途：获取AI优秀实践专区的帖子列表，支持多种筛选和排序方式
// 后端返回: Result<PageResult<Post>>
// 参数说明：
//   - zone: 可选，指定专区（"practices"表示AI优秀实践）
//   - toolId: 可选，工具ID（当zone为tools时使用）
//   - search: 可选，搜索关键词（搜索标题、作者、描述）
//   - sort: 可选，排序方式（"newest"最新、"hot"最热、"comments"评论最多、"likes"点赞最多）
//   - tag: 可选，按标签筛选
//   - department: 可选，按部门筛选
//   - author: 可选，按作者筛选
//   - page: 可选，页码（从1开始，默认1）
//   - pageSize: 可选，每页数量（默认15）
export const getPosts = async (params: {
  zone?: 'practices' | 'tools' | 'agent' | 'empowerment'
  toolId?: number
  search?: string
  sort?: 'newest' | 'hot' | 'comments' | 'likes'
  tag?: string
  department?: string
  author?: string
  page?: number
  pageSize?: number
}): Promise<PostsResponse> => {
  const result = await request.get<PostsResponse>('/posts', { params }) as unknown as PostsResponse
  // 转换Post字段：authorName -> author, summary -> description
  if (result.list) {
    result.list = result.list.map(post => ({
      ...post,
      author: post.author || (post as any).authorName || '',
      description: post.description || (post as any).summary || '',
      image: post.image || post.cover || '',
      featured: post.featured !== undefined ? post.featured : (post as any).isFeatured || false
    }))
  }
  return result
}

// ========== 最热帖子 ==========

// 获取最热帖子
// 用途：获取AI优秀实践专区最热门的帖子列表，用于页面右侧"最热帖子"模块展示
// 后端返回: Result<List<Post>>
// 参数说明：
//   - zone: 可选，指定专区（"practices"表示AI优秀实践，不传则返回所有专区）
//   - limit: 可选，返回数量（默认10条）
// 热度计算规则：综合浏览量、点赞数、评论数等因素计算热度值
export const getHotPosts = async (params?: {
  zone?: 'practices' | 'tools' | 'agent' | 'empowerment'
  limit?: number
}): Promise<{ list: Post[] }> => {
  const posts = await request.get<Post[]>('/posts/hot', { params }) as unknown as Post[]
  // 转换Post字段
  const list = (Array.isArray(posts) ? posts : []).map(post => ({
    ...post,
    author: post.author || (post as any).authorName || '',
    description: post.description || (post as any).summary || '',
    image: post.image || post.cover || '',
    featured: post.featured !== undefined ? post.featured : (post as any).isFeatured || false
  }))
  return { list }
}

// ========== 标签相关 ==========

// 标签接口
export interface Tag {
  name: string
  count?: number // 该标签下的帖子数量
  preset?: boolean // 是否为预设标签（不可删除）
}

// 获取所有标签
// 用途：获取AI优秀实践专区的所有标签列表，用于页面右侧"标签筛选"模块
// 后端返回: Result<List<TagInfo>>
// 参数说明：
//   - zone: 必填，指定专区（"practices"表示AI优秀实践）
//   - toolId: 可选，工具ID（当zone为tools时使用）
// 说明：不同专区有不同的标签列表
//   - AI优秀实践专区：返回自然语言处理、计算机视觉、深度学习、AI伦理、机器学习等标签
export const getTags = async (params: {
  zone: 'practices' | 'tools' | 'agent' | 'empowerment'
  toolId?: number
}): Promise<{ list: Tag[] }> => {
  const tags = await request.get<Tag[]>('/tags', { params }) as unknown as Tag[]
  return { list: Array.isArray(tags) ? tags : [] }
}

// ========== 部门相关 ==========

// 部门统计接口
export interface DepartmentStats {
  id: number
  name: string // 部门名称
  postCount: number // 该部门发布的帖子数量
  contributorCount: number // 该部门的贡献者数量
}

// 获取所有部门
// 用途：获取各部门的帖子统计信息，用于页面右侧"部门归类"模块展示
// 后端返回: Result<List<DepartmentStatsResponse>>
// 参数说明：
//   - zone: 可选，指定专区（"practices"表示AI优秀实践，不传则返回所有专区的统计）
// 返回数据：按发帖数从高到低排序的部门列表
export const getDepartments = async (params?: {
  zone?: 'practices' | 'tools' | 'agent' | 'empowerment'
}): Promise<{ list: DepartmentStats[] }> => {
  const departments = await request.get<DepartmentStats[]>('/departments/stats', { params }) as unknown as DepartmentStats[]
  return { list: Array.isArray(departments) ? departments : [] }
}

// ========== 热门贡献者 ==========

// 热门贡献者接口
export interface TopContributor {
  id: number
  name: string // 用户姓名
  avatar: string // 用户头像
  postCount: number // 发布的帖子数量
}

// 获取热门贡献者
// 用途：获取AI优秀实践专区的热门贡献者列表，用于页面右侧"热门贡献者"模块展示
// 后端返回: Result<List<TopContributorResponse>>
// 参数说明：
//   - zone: 可选，指定专区（"practices"表示AI优秀实践，不传则返回所有专区的贡献者）
//   - limit: 可选，返回数量（默认10条）
// 排序规则：按发布的帖子数量从高到低排序
export const getTopContributors = async (params?: {
  zone?: 'practices' | 'tools' | 'agent' | 'empowerment'
  limit?: number
}): Promise<{ list: TopContributor[] }> => {
  const contributors = await request.get<TopContributor[]>('/users/top-contributors', { params }) as unknown as TopContributor[]
  return { list: Array.isArray(contributors) ? contributors : [] }
}
