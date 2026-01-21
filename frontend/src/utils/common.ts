/**
 * 通用工具方法
 */

export const commonMethods = {
  /**
   * 时间戳转日期时间字符串
   * @param timestamp 时间戳
   * @returns 格式化的日期时间字符串
   */
  timestampToDateTime(timestamp: number): string {
    if (!timestamp) return ''
    const date = new Date(timestamp)
    const Y = date.getFullYear() + '-'
    const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
    const D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' '
    const h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
    const m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':'
    const s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
    return Y + M + D + h + m + s
  },

  /**
   * 记录访问信息 (Mock)
   * @param time 访问时间
   */
  async addViewsInfo(time: string): Promise<void> {
    // console.log('记录用户访问:', time)
    // 实际项目中这里可能调用 API
    return Promise.resolve()
  },

  /**
   * 根据工号获取头像 URL
   * @param id 工号 (如 x123456, 123456)
   * @returns 头像 URL
   */
  getAvatarUrl(id: string | undefined | null): string {
    if (!id) return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

    // 如果已经是完整 URL，直接返回
    if (String(id).startsWith('http')) {
      return String(id)
    }

    // 提取数字部分
    const numericId = String(id).replace(/[^0-9]/g, '')
    if (!numericId) return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

    return `https://w3.huawei.com/w3lab/rest/yellowpage/face/${numericId}/120`
  }
}

export default commonMethods
