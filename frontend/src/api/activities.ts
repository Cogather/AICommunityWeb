/**
 * 活动 API
 */

import { get, post, put, del, useRealApi, delay, success } from './request'
import type { ApiResponse, Activity, PaginatedData, ActivityType, ActivityStatus } from './types'

// ==================== 扩展类型 ====================

/** 活动详情 */
export interface ActivityDetail extends Activity {
  content: string
  registrations?: ActivityRegistration[]
}

/** 活动报名记录 */
export interface ActivityRegistration {
  id: number
  userId: number
  userName: string
  userAvatar?: string
  department?: string
  registerTime: string
}

/** 创建活动参数 */
export interface ActivityCreateParams {
  toolId: number
  type: ActivityType
  title: string
  content: string
  cover?: string
  date: string
  startTime?: string
  endTime?: string
  location?: string
  meetingLink?: string
  maxParticipants?: number
}

/** 更新活动参数 */
export interface ActivityUpdateParams extends Partial<ActivityCreateParams> {}

/** 报名响应 */
export interface JoinResponse {
  isJoined: boolean
  currentParticipants: number
}

// ==================== Mock 数据 ====================

const mockActivities: Activity[] = [
  {
    id: 1,
    toolId: 1,
    toolName: 'TestMate',
    type: 'training',
    title: 'TestMate高级特性培训',
    cover: 'https://picsum.photos/800/400?random=activity1',
    date: '2026-01-20',
    location: '线上腾讯会议',
    meetingLink: 'https://meeting.tencent.com/testmate1',
    speaker: '张测试',
    maxParticipants: 100,
    currentParticipants: 45,
    status: 'upcoming',
    isJoined: false,
  },
  {
    id: 2,
    toolId: -1,
    toolName: '扶摇Agent',
    type: 'training',
    title: '扶摇Agent实战培训',
    cover: 'https://picsum.photos/800/400?random=activity2',
    date: '2026-01-25',
    location: 'B区会议室301',
    maxParticipants: 30,
    currentParticipants: 25,
    status: 'upcoming',
    isJoined: true,
  },
]

// ==================== Mock API 实现 ====================

const mockGetActivityDetail = async (id: number): Promise<ApiResponse<ActivityDetail>> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id) || mockActivities[0]
  return success({
    ...activity,
    content: '<p>活动详细内容...</p>',
  } as ActivityDetail)
}

const mockCreateActivity = async (params: ActivityCreateParams): Promise<ApiResponse<ActivityDetail>> => {
  await delay()
  return success({
    id: Date.now(),
    ...params,
    status: 'upcoming',
    currentParticipants: 0,
    isJoined: false,
    creatorId: 1,
    creatorName: '当前用户',
    createTime: new Date().toISOString(),
  } as ActivityDetail)
}

const mockUpdateActivity = async (
  id: number,
  _params: ActivityUpdateParams
): Promise<ApiResponse<ActivityDetail>> => {
  await delay()
  const activity = mockActivities.find(a => a.id === id) || mockActivities[0]
  return success({
    ...activity,
    content: '<p>更新后的内容</p>',
  } as ActivityDetail)
}

const mockDeleteActivity = async (): Promise<ApiResponse<null>> => {
  await delay()
  return success(null, '删除成功')
}

const mockJoinActivity = async (): Promise<ApiResponse<JoinResponse>> => {
  await delay()
  return success({
    isJoined: true,
    currentParticipants: 46,
  })
}

const mockCancelJoin = async (): Promise<ApiResponse<null>> => {
  await delay()
  return success(null, '取消成功')
}

const mockGetRegistrations = async (
  _activityId: number
): Promise<ApiResponse<PaginatedData<ActivityRegistration>>> => {
  await delay()
  return success({
    list: [
      {
        id: 1,
        userId: 2,
        userName: '李四',
        userAvatar: 'https://picsum.photos/100/100?random=r1',
        department: '技术部',
        registerTime: '2026-01-10 10:00:00',
      },
    ],
    total: 1,
    page: 1,
    pageSize: 100,
  })
}

// ==================== API 函数 ====================

/**
 * 获取活动详情
 * GET /api/tools/activities/:activityId
 */
export async function getActivityDetail(activityId: number): Promise<ApiResponse<ActivityDetail>> {
  if (!useRealApi) {
    return mockGetActivityDetail(activityId)
  }
  return get<ActivityDetail>(`/tools/activities/${activityId}`)
}

/**
 * 创建活动
 * POST /api/tools/activities
 */
export async function createActivity(params: ActivityCreateParams): Promise<ApiResponse<ActivityDetail>> {
  if (!useRealApi) {
    return mockCreateActivity(params)
  }
  return post<ActivityDetail>('/tools/activities', params)
}

/**
 * 更新活动
 * PUT /api/tools/activities/:activityId
 */
export async function updateActivity(
  activityId: number,
  params: ActivityUpdateParams
): Promise<ApiResponse<ActivityDetail>> {
  if (!useRealApi) {
    return mockUpdateActivity(activityId, params)
  }
  return put<ActivityDetail>(`/tools/activities/${activityId}`, params)
}

/**
 * 删除活动
 * DELETE /api/tools/activities/:activityId
 */
export async function deleteActivity(activityId: number): Promise<ApiResponse<null>> {
  if (!useRealApi) {
    return mockDeleteActivity()
  }
  return del<null>(`/tools/activities/${activityId}`)
}

/**
 * 报名活动
 * POST /api/tools/activities/:activityId/join
 */
export async function joinActivity(activityId: number): Promise<ApiResponse<JoinResponse>> {
  if (!useRealApi) {
    return mockJoinActivity()
  }
  return post<JoinResponse>(`/tools/activities/${activityId}/join`)
}

/**
 * 取消报名
 * DELETE /api/tools/activities/:activityId/join
 */
export async function cancelJoin(activityId: number): Promise<ApiResponse<null>> {
  if (!useRealApi) {
    return mockCancelJoin()
  }
  return del<null>(`/tools/activities/${activityId}/join`)
}

/**
 * 获取活动报名列表
 * GET /api/tools/activities/:activityId/registrations
 * 或 GET /api/activities/:activityId/registrations
 */
export async function getActivityRegistrations(
  activityId: number,
  page = 1,
  pageSize = 100
): Promise<ApiResponse<PaginatedData<ActivityRegistration>>> {
  if (!useRealApi) {
    return mockGetRegistrations(activityId)
  }
  return get<PaginatedData<ActivityRegistration>>(`/activities/${activityId}/registrations`, { page, pageSize })
}

// ==================== 导出 ====================

export const activitiesApi = {
  getActivityDetail,
  createActivity,
  updateActivity,
  deleteActivity,
  joinActivity,
  cancelJoin,
  getActivityRegistrations,
}

export default activitiesApi
