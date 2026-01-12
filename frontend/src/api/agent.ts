// 扶摇Agent应用相关接口
import request from './request'
import type { Post } from './practices'

// Agent置顶帖子响应
export interface AgentFeaturedPostResponse {
  post: Post | null
  message?: string
}

// 获取扶摇Agent应用置顶帖子
export const getFeaturedPost = async (): Promise<AgentFeaturedPostResponse> => {
  const response = await request.get<AgentFeaturedPostResponse>('/agent/featured-post')
  // 如果返回的post存在，进行字段映射
  if (response.post) {
    response.post = {
      ...response.post,
      author: response.post.author || (response.post as any).authorName || '',
      description: response.post.description || (response.post as any).summary || '',
      image: response.post.image || response.post.cover || '',
      featured: response.post.featured !== undefined ? response.post.featured : (response.post as any).isFeatured || false
    }
  }
  return response
}
