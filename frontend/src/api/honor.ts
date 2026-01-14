/**
 * AI使用达人（荣誉）API
 */

import { get, post, useRealApi, delay, success } from './request'
import type {
  ApiResponse,
  HonorRecord,
  TeamAward,
  LeaderboardUser,
  PaginatedData,
} from './types'

// ==================== 扩展类型 ====================

/** 荣誉列表响应 */
export interface HonorListData extends PaginatedData<HonorRecord> {}

/** 团队奖项列表响应 */
export interface TeamAwardListData {
  list: TeamAward[]
}

/** 排行榜响应 */
export interface LeaderboardData {
  list: LeaderboardUser[]
}

/** 奖项名称列表响应 */
export interface AwardNamesData {
  list: string[]
}

/** 部门列表响应 */
export interface DepartmentNamesData {
  list: string[]
}

/** 时光轴数据 */
export interface TimelineData {
  years: Array<{
    year: string
    records: HonorRecord[]
  }>
}

/** 送花响应 */
export interface FlowerResponse {
  success: boolean
  flowers: number
  hasGivenFlower: boolean
}

// ==================== Mock 数据 ====================

const mockHonorRecords: HonorRecord[] = [
  {
    id: 1,
    name: '林星辰',
    department: '技术部',
    avatar: 'https://picsum.photos/100/100?random=h1',
    awardName: '2026年度 AI 技术突破奖',
    awardDate: '2026-01-01',
    category: 'innovation',
    year: '2026',
    flowers: 128,
    hasGivenFlower: false,
    achievement: '成功研发了新一代AI推理引擎',
  },
  {
    id: 2,
    name: 'Sarah',
    department: '设计部',
    avatar: 'https://picsum.photos/100/100?random=h2',
    awardName: '最佳 AI 辅助设计实践',
    awardDate: '2026-01-05',
    category: 'practice',
    year: '2026',
    flowers: 96,
    hasGivenFlower: false,
  },
]

const mockTeamAwards: TeamAward[] = [
  {
    id: 1,
    title: '年度最佳AI创新团队',
    year: '2026',
    images: [
      {
        id: 1,
        image: 'https://picsum.photos/400/300?random=team1',
        imageType: 'url',
        winnerName: 'AI研发中心',
        teamField: 'AI技术研发',
        story: '<p>AI研发中心团队成功研发了多项核心AI技术</p>',
        flowers: 128,
        hasGivenFlower: false,
      },
    ],
  },
]

const mockLeaderboard: LeaderboardUser[] = [
  { name: '林星辰', department: '技术部', avatar: 'https://picsum.photos/100/100?random=l1', count: 5, totalFlowers: 500 },
  { name: 'Sarah', department: '设计部', avatar: 'https://picsum.photos/100/100?random=l2', count: 4, totalFlowers: 400 },
  { name: '张伟', department: '产品部', avatar: 'https://picsum.photos/100/100?random=l3', count: 3, totalFlowers: 300 },
]

// ==================== Mock API 实现 ====================

const mockGetHonorList = async (
  page = 1,
  pageSize = 15
): Promise<ApiResponse<HonorListData>> => {
  await delay()
  return success({
    list: mockHonorRecords,
    total: mockHonorRecords.length,
    page,
    pageSize,
  })
}

const mockGetTeamAwards = async (): Promise<ApiResponse<TeamAwardListData>> => {
  await delay()
  return success({ list: mockTeamAwards })
}

const mockGetLeaderboard = async (): Promise<ApiResponse<LeaderboardData>> => {
  await delay()
  return success({ list: mockLeaderboard })
}

const mockGiveFlower = async (honorId: number): Promise<ApiResponse<FlowerResponse>> => {
  await delay()
  return success({
    success: true,
    flowers: 129,
    hasGivenFlower: true,
  })
}

const mockGetAwardNames = async (): Promise<ApiResponse<AwardNamesData>> => {
  await delay()
  return success({
    list: ['2026年度 AI 技术突破奖', '最佳 AI 辅助设计实践', 'Copilot 效能提升大师'],
  })
}

const mockGetDepartments = async (): Promise<ApiResponse<DepartmentNamesData>> => {
  await delay()
  return success({
    list: ['技术部', '设计部', '产品部', '运营部'],
  })
}

const mockGetTimeline = async (): Promise<ApiResponse<TimelineData>> => {
  await delay()
  return success({
    years: [
      { year: '2026', records: mockHonorRecords },
    ],
  })
}

// ==================== API 函数 ====================

/**
 * 获取个人荣誉列表
 * GET /api/honor/list
 */
export async function getHonorList(params: {
  page?: number
  pageSize?: number
  scope?: 'all' | 'mine'
  filterType?: 'award' | 'department'
  filterValue?: string
  keyword?: string
  view?: 'grid' | 'timeline'
  userName?: string
} = {}): Promise<ApiResponse<HonorListData>> {
  if (!useRealApi) {
    return mockGetHonorList(params.page, params.pageSize)
  }
  return get<HonorListData>('/honor/list', params)
}

/**
 * 获取团队奖项列表
 * GET /api/honor/team-awards
 */
export async function getTeamAwards(year?: string): Promise<ApiResponse<TeamAwardListData>> {
  if (!useRealApi) {
    return mockGetTeamAwards()
  }
  return get<TeamAwardListData>('/honor/team-awards', year ? { year } : undefined)
}

/**
 * 获取荣誉影响力排行榜
 * GET /api/honor/leaderboard
 */
export async function getLeaderboard(params: {
  limit?: number
  scope?: 'all' | 'mine'
  filterType?: 'award' | 'department'
  filterValue?: string
} = {}): Promise<ApiResponse<LeaderboardData>> {
  if (!useRealApi) {
    return mockGetLeaderboard()
  }
  return get<LeaderboardData>('/honor/leaderboard', params)
}

/**
 * 送花
 * POST /api/honor/flower
 * @param honorId 荣誉记录ID
 * @param type 类型：individual（个人）/ team（团队）
 */
export async function giveFlower(
  honorId: number,
  type: 'individual' | 'team' = 'individual'
): Promise<ApiResponse<FlowerResponse>> {
  if (!useRealApi) {
    return mockGiveFlower(honorId)
  }
  return post<FlowerResponse>('/honor/flower', { honorId, type })
}

/**
 * 获取奖项名称列表
 * GET /api/honor/awards
 */
export async function getAwardNames(): Promise<ApiResponse<AwardNamesData>> {
  if (!useRealApi) {
    return mockGetAwardNames()
  }
  return get<AwardNamesData>('/honor/awards')
}

/**
 * 获取部门列表
 * GET /api/honor/departments
 */
export async function getDepartments(): Promise<ApiResponse<DepartmentNamesData>> {
  if (!useRealApi) {
    return mockGetDepartments()
  }
  return get<DepartmentNamesData>('/honor/departments')
}

/**
 * 获取用户荣誉时光轴
 * GET /api/honor/timeline
 */
export async function getTimeline(userName?: string): Promise<ApiResponse<TimelineData>> {
  if (!useRealApi) {
    return mockGetTimeline()
  }
  return get<TimelineData>('/honor/timeline', userName ? { userName } : undefined)
}

/**
 * 获取最新获奖者列表
 * GET /api/honor/latest-winners
 */
export async function getLatestWinners(limit = 9): Promise<ApiResponse<{ list: HonorRecord[] }>> {
  if (!useRealApi) {
    await delay()
    return success({ list: mockHonorRecords.slice(0, limit) })
  }
  return get<{ list: HonorRecord[] }>('/honor/latest-winners', { limit })
}

// ==================== 导出 ====================

export const honorApi = {
  getHonorList,
  getTeamAwards,
  getLeaderboard,
  giveFlower,
  getAwardNames,
  getDepartments,
  getTimeline,
  getLatestWinners,
}

export default honorApi
