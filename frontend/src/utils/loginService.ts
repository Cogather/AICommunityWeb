import { get } from '@/api/request'
import commonMethods from '@/utils/common'

// 缓存 Key
const CACHE_KEY = 'user_info'

// 基础 URL 配置
const NGX_BASE_URL = 'https://coretool.rnd.huawei.com' // 假设的 Base URL，根据原代码推断
const LOGIN_URL = 'https://login.huawei.com/login/'

// 辅助函数
const getCache = (key: string) => {
  const val = localStorage.getItem(key)
  return val ? JSON.parse(val) : null
}

const setCache = (key: string, obj: any) => {
  localStorage.setItem(key, JSON.stringify(obj))
}

const removeCache = (key: string) => {
  localStorage.removeItem(key)
}

const getCookie = (cname: string) => {
  const name = `${cname}=`
  const ca = document.cookie.split(';')
  for (let i = 0; i < ca.length; i += 1) {
    const c = ca[i].trim()
    if (c.indexOf(name) === 0) return c.substring(name.length, c.length)
  }
  return ''
}

class LoginService {
  get userInfo() {
    return getCache(CACHE_KEY)
  }

  get loginEndpoint() {
    return `${NGX_BASE_URL}/login/v2/login`
  }

  get logoutEndpoint() {
    return `${NGX_BASE_URL}/login/v2/logout`
  }

  get validateEndpoint() {
    return `${NGX_BASE_URL}/login/v2/validate`
  }

  // 验证用户是否登录，并根据cookice信息生成新老token
  getTokens() {
    return fetch(`${NGX_BASE_URL}/login/v2/tokens`)
  }

  login() {
    console.log('window.location.href', window.location.href)
    const redirectUrl = encodeURIComponent('https://coretool.rnd.huawei.com/#/home')
    const returnUrl = encodeURIComponent(window.location.href)
    window.location.href = `${LOGIN_URL}?redirect=${redirectUrl}?returnUrl=${returnUrl}`
  }

  logout() {
    removeCache(CACHE_KEY)
    const params = new URLSearchParams()
    params.append('redirect', window.location.href)
    window.location.href = `${LOGIN_URL}?${params.toString()}`
  }

  getQueryObject(url: string | null) {
    const curUrl = url === null ? window.location.href : url
    const search = curUrl.substring(curUrl.lastIndexOf('?') + 1)
    const obj: Record<string, string> = {}
    const reg = /([^?&=]+)=([^?&=]*)/g
    search.replace(reg, (rs, $1, $2) => {
      const name = decodeURIComponent($1)
      let val = decodeURIComponent($2)
      val = String(val)
      obj[name] = val
      return rs
    })
    return obj
  }

  async validate(init?: boolean) {
    const cachedUserInfo = getCache(CACHE_KEY)
    const { uid, userId, userName } = cachedUserInfo || {}
    
    // 如果缓存中有用户信息，且不是初始化检查，则认为已登录
    if (uid && userId && userName && !init) {
      return Promise.resolve(true)
    }

    const query = this.getQueryObject(window.location.href)
    
    // 检查 Cookie 中的 userId
    if (getCookie('userId')) {
      (window as any).userId = getCookie('userId');
      (window as any).userName = getCookie('userName');
      return Promise.resolve(true)
    }

    // 检查 URL 参数中的 login_uid 与 Cookie 是否匹配
    if (query.login_uid && query.login_uid == getCookie('login_uid')) {
      try {
        // 调用外部接口获取用户信息
        // 注意：baseURL 设置为空字符串以使用绝对路径
        const res = await get<any>(
          `https://corecode-prod.inhuawei.com/developtest/v1/auth/user/${query.login_uid}`
        )
        
        if (res && (res as any).data) { // 适配直接返回或者 ApiResponse
           const userData = (res as any).data || res; // 兼容不同结构
           // 如果 res.data 存在且有 user 字段，说明结构可能是 { data: { user: ... } }，或者 res 本身就是数据
           // 原代码: if (res.data) { ... res.data.user ... }
           // get<T> 返回的是 ApiResponse<T>，即 { code, data, message }
           // 外部接口可能直接返回数据，request.ts 中处理了 response.json()
           
           // 假设 request.ts 返回的是 data (response.json())
           // 让我们假设 res.data 是用户对象
           const userObj = userData; 

           if (userObj) {
              document.cookie = `userId=${userObj.user};path=/`
              document.cookie = `username=${userObj.name};path=/`
              document.cookie = `user_login_time=${new Date().getTime()};path=/`
              
              ;(window as any).userId = userObj.user
              ;(window as any).userName = userObj.name
              
              setCache(CACHE_KEY, {
                uid: userObj.user,
                userId: userObj.user,
                userName: userObj.name,
              });
              
              (window as any).userLoginTime = getCookie('user_login_time')
              const accessTime = commonMethods.timestampToDateTime(parseInt((window as any).userLoginTime))
              await commonMethods.addViewsInfo(accessTime)
           }
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    } else {
      // 未登录，跳转到登录页
      const url = 'http://coretool.rnd.huawei.com/#/home'
      const currHref = document.location.href
      // 避免无限重定向
      if (!currHref.includes('?redirect=') && !currHref.includes('login_uid')) {
         // 注意：开发环境可能不希望跳转，可以加个判断
         const isProduction = import.meta.env.PROD || window.location.hostname.includes('huawei');
         if (isProduction) {
            window.location.href = `${LOGIN_URL}?redirect=${encodeURIComponent(url)}?returnUrl=${encodeURIComponent(currHref)}`
         } else {
             console.warn('开发环境：未检测到登录信息，但阻止跳转到 SSO')
         }
      }
    }
    return Promise.resolve(true)
  }
}

export const loginService = new LoginService()

export default loginService
