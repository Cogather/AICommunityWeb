/**
 * 本地存储工具
 */

const USER_INFO_KEY = 'user_info'

export const getCachedUserInfo = () => {
  const val = localStorage.getItem(USER_INFO_KEY)
  return val ? JSON.parse(val) : null
}

export const setCachedUserInfo = (obj: any) => {
  localStorage.setItem(USER_INFO_KEY, JSON.stringify(obj))
}

export const removeCachedUserInfo = () => {
  localStorage.removeItem(USER_INFO_KEY)
}

export default {
  getCachedUserInfo,
  setCachedUserInfo,
  removeCachedUserInfo
}
