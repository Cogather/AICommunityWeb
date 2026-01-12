// 回复相关接口
import request from './request'

// 删除回复
export const deleteReply = async (id: number): Promise<void> => {
  await request.delete(`/replies/${id}`)
}
