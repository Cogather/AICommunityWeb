// 奖项相关接口
import request from './request'

// 奖项规则响应
export interface AwardRulesResponse {
  awardId: number
  awardName: string
  category?: string
  description?: string
  rules: string
  requirements?: string[]
  benefits?: string[]
}

// 获取奖项规则详情
export const getAwardRules = async (id: number): Promise<AwardRulesResponse> => {
  return await request.get<AwardRulesResponse>(`/awards/${id}/rules`)
}
