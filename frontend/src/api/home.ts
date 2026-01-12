// 首页相关接口
import request from './request'

// 轮播图
export interface CarouselItem {
  id: number
  image: string
  title: string
  desc: string
  link: string
  showContent: boolean
}

// 获取首页轮播图
export const getCarousel = async (): Promise<{ list: CarouselItem[] }> => {
  return request.get<{ list: CarouselItem[] }>('/home/carousel') as Promise<{ list: CarouselItem[] }>
}

// 荣誉殿堂
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
  honorCount: number
}

export interface HonorInfo {
  bannerImage: string
  awards: HonorAward[]
  topUsers: TopUser[]
}

// 获取荣誉殿堂信息
export const getHonor = async (): Promise<{ honor: HonorInfo }> => {
  return request.get<{ honor: HonorInfo }>('/home/honor') as Promise<{ honor: HonorInfo }>
}

// AI工具相关
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

// 获取工具列表
export const getTools = async (): Promise<{ list: ToolItem[] }> => {
  return request.get<{ list: ToolItem[] }>('/tools') as Promise<{ list: ToolItem[] }>
}

// 获取工具Banner列表
export const getToolBanners = async (): Promise<{ list: ToolBannerItem[] }> => {
  return request.get<{ list: ToolBannerItem[] }>('/tools/banners') as Promise<{ list: ToolBannerItem[] }>
}

// AI优秀实践相关
export interface PracticeItem {
  id: number
  title: string
  author: string
  time: string
  category?: 'training' | 'training-battle' | 'user-exchange'
}

export interface PracticesInfo {
  training: PracticeItem[] // 培训赋能
  trainingBattle: PracticeItem[] // AI训战
  userExchange: PracticeItem[] // 用户交流
}

// 获取AI优秀实践内容（三块列表）
export const getPractices = async (): Promise<PracticesInfo> => {
  return request.get<PracticesInfo>('/home/practices') as Promise<PracticesInfo>
}
