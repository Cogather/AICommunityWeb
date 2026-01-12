// API统一导出
export * from './request'
export * from './users'
export * from './admin'
export * from './practices'
// 单独导出home，避免CarouselItem冲突
export { getCarousel, getHonor, getTools, getToolBanners, getPractices } from './home'
export type { 
  CarouselItem as HomeCarouselItem, 
  HonorAward, 
  TopUser, 
  HonorInfo,
  ToolItem as HomeToolItem,
  ToolBannerItem as HomeToolBannerItem,
  PracticeItem,
  PracticesInfo
} from './home'
