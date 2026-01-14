/**
 * 扶摇Agent应用 API
 * 
 * 注：扶摇Agent的大部分接口复用工具专区接口，toolId = -1
 */

import { get, put, useRealApi, delay, success } from './request'
import type { ApiResponse, Post, Activity, TagStat, PaginatedData } from './types'
import { toolsApi } from './tools'

// ==================== 常量 ====================

/** 扶摇Agent的工具ID */
export const AGENT_TOOL_ID = -1

// ==================== 扩展类型 ====================

/** 置顶帖子响应 */
export interface PinnedPostResponse {
  post: Post | null
  hasPinned: boolean
}

/** 设置置顶响应 */
export interface SetPinnedResponse {
  success: boolean
  postId: number
  pinned: boolean
}

// ==================== Mock 数据 ====================

const mockPinnedPost: Post = {
  id: 101,
  title: '使用扶摇Agent实现智能代码生成',
  summary: '分享如何使用扶摇Agent编排引擎实现智能代码生成功能，提升开发效率。',
  author: '张工程师',
  authorId: 1,
  createTime: '2026-01-12',
  views: 890,
  comments: 45,
  likes: 98,
  tags: ['Agent应用', '代码生成'],
  zone: 'agent',
  toolId: -1,
  toolName: '扶摇Agent',
  featured: true,
}

const mockAgentPosts: Post[] = [
  {
    id: 102,
    title: '扶摇工作流编排实战案例',
    summary: '通过实际案例展示如何利用扶摇Agent编排复杂的工作流程。',
    author: '李开发者',
    authorId: 2,
    createTime: '2026-01-10',
    views: 720,
    comments: 32,
    likes: 75,
    tags: ['工作流', '实战案例'],
    zone: 'agent',
    toolId: -1,
    toolName: '扶摇Agent',
  },
  {
    id: 103,
    title: 'Agent自动化测试实践',
    summary: '介绍如何使用扶摇Agent进行自动化测试，提高测试效率和覆盖率。',
    author: '王测试',
    authorId: 3,
    createTime: '2026-01-08',
    views: 650,
    comments: 28,
    likes: 62,
    tags: ['自动化', '测试'],
    zone: 'agent',
    toolId: -1,
    toolName: '扶摇Agent',
  },
]

const mockAgentActivities: Activity[] = [
  {
    id: 2,
    toolId: -1,
    toolName: '扶摇Agent',
    type: 'training',
    title: '扶摇Agent实战培训',
    date: '2026-01-25',
    location: 'B区会议室301',
    status: 'upcoming',
    currentParticipants: 25,
    maxParticipants: 30,
  },
  {
    id: 3,
    toolId: -1,
    toolName: '扶摇Agent',
    type: 'workshop',
    title: '智能编排工作坊',
    date: '2026-02-01',
    location: '线上会议',
    meetingLink: 'https://meeting.example.com/456',
    status: 'upcoming',
    currentParticipants: 15,
    maxParticipants: 100,
  },
]

// ==================== Mock API 实现 ====================

const mockGetPinnedPost = async (): Promise<ApiResponse<PinnedPostResponse>> => {
  await delay()
  return success({
    post: mockPinnedPost,
    hasPinned: true,
  })
}

const mockGetPosts = async (
  page = 1,
  pageSize = 15
): Promise<ApiResponse<PaginatedData<Post>>> => {
  await delay()
  return success({
    list: mockAgentPosts,
    total: mockAgentPosts.length,
    page,
    pageSize,
  })
}

const mockGetActivities = async (): Promise<ApiResponse<PaginatedData<Activity>>> => {
  await delay()
  return success({
    list: mockAgentActivities,
    total: mockAgentActivities.length,
    page: 1,
    pageSize: 10,
  })
}

const mockGetTags = async (): Promise<ApiResponse<{ list: TagStat[] }>> => {
  await delay()
  return success({
    list: [
      { name: 'Agent应用', count: 30 },
      { name: '工作流', count: 25 },
      { name: '自动化', count: 20 },
      { name: '代码生成', count: 18 },
      { name: '智能编排', count: 15 },
    ],
  })
}

const mockSetPinnedPost = async (
  postId: number,
  pinned: boolean
): Promise<ApiResponse<SetPinnedResponse>> => {
  await delay()
  return success({
    success: true,
    postId,
    pinned,
  })
}

// ==================== API 函数 ====================

/**
 * 获取置顶帖子
 * GET /api/agent/pinned-post
 */
export async function getPinnedPost(): Promise<ApiResponse<PinnedPostResponse>> {
  if (!useRealApi) {
    return mockGetPinnedPost()
  }
  return get<PinnedPostResponse>('/agent/pinned-post')
}

/**
 * 获取扶摇Agent帖子列表
 * GET /api/tools/posts?toolId=-1
 */
export async function getPosts(params: {
  tag?: string
  keyword?: string
  sortBy?: 'newest' | 'hot' | 'comments'
  page?: number
  pageSize?: number
} = {}): Promise<ApiResponse<PaginatedData<Post>>> {
  if (!useRealApi) {
    return mockGetPosts(params.page, params.pageSize)
  }
  return toolsApi.getToolPosts({
    toolId: AGENT_TOOL_ID,
    ...params,
  })
}

/**
 * 获取扶摇Agent活动列表
 * GET /api/tools/activities?toolId=-1
 */
export async function getActivities(params: {
  status?: 'upcoming' | 'ongoing' | 'ended'
  page?: number
  pageSize?: number
} = {}): Promise<ApiResponse<PaginatedData<Activity>>> {
  if (!useRealApi) {
    return mockGetActivities()
  }
  return toolsApi.getToolActivities({
    toolId: AGENT_TOOL_ID,
    ...params,
  })
}

/**
 * 获取扶摇Agent标签统计
 * GET /api/tools/-1/tags
 */
export async function getTags(): Promise<ApiResponse<{ list: TagStat[] }>> {
  if (!useRealApi) {
    return mockGetTags()
  }
  return toolsApi.getToolTags(AGENT_TOOL_ID)
}

/**
 * 检查扶摇Agent Owner权限
 * GET /api/tools/-1/check-owner
 */
export async function checkOwnerPermission() {
  return toolsApi.checkOwnerPermission(AGENT_TOOL_ID)
}

/**
 * 设置/取消置顶帖子
 * PUT /api/agent/pinned-post
 */
export async function setPinnedPost(
  postId: number,
  pinned: boolean
): Promise<ApiResponse<SetPinnedResponse>> {
  if (!useRealApi) {
    return mockSetPinnedPost(postId, pinned)
  }
  return put<SetPinnedResponse>('/agent/pinned-post', { postId, pinned })
}

// ==================== 导出 ====================

export const agentApi = {
  AGENT_TOOL_ID,
  getPinnedPost,
  getPosts,
  getActivities,
  getTags,
  checkOwnerPermission,
  setPinnedPost,
}

export default agentApi
