import { get } from '@/api/request'
import { checkCommunityMembership } from '@/api/user'
import commonMethods from '@/utils/common'

// 缓存 Key
const CACHE_KEY = 'user_info'

// 基础 URL 配置
// 注意：原代码中的 NGX_BASE_URL 是 https://coretool.rnd.huawei.com
// 这里我们将其调整为当前应用的 API 地址，或者如果后端有统一的 Auth 服务，应该配置为 Auth 服务地址
// 鉴于这是一个移植模块，我们暂时保留结构，但 login/logout 主要依赖 SSO 跳转
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

  // 获取当前应用的主页地址，用于 SSO 回调
  get appHomeUrl() {
    // 自动获取当前协议+域名+端口
    return window.location.origin
  }

  // 以下 Endpoint 保留用于兼容，但实际逻辑主要依赖 login/validate 方法
  get loginEndpoint() {
    return '/login/v2/login'
  }

  get logoutEndpoint() {
    return '/login/v2/logout'
  }

  get validateEndpoint() {
    return '/login/v2/validate'
  }

  // 验证用户是否登录，并根据cookice信息生成新老token
  getTokens() {
    return fetch('/login/v2/tokens')
  }

  login() {
    console.log('Login: redirecting to SSO', window.location.href)
    // 构造回调地址：当前应用的首页
    const redirectUrl = encodeURIComponent(this.appHomeUrl)
    // 构造返回地址：当前页面的完整 URL (用于登录后跳回具体页面)
    const returnUrl = encodeURIComponent(window.location.href)
    
    // 跳转到 SSO 登录页
    // 格式：https://login.huawei.com/login/?redirect={应用首页}?returnUrl={当前页}
    window.location.href = `${LOGIN_URL}?redirect=${redirectUrl}?returnUrl=${returnUrl}`
  }

  logout() {
    removeCache(CACHE_KEY)
    // 清除 cookie (可选，视具体需求)
    document.cookie = 'userId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'
    document.cookie = 'username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'
    
    const params = new URLSearchParams()
    // 登出后通常跳转回当前页或首页，触发 SSO 重新登录流程或显示未登录态
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
    
    // 1. 如果缓存中有用户信息，且不是初始化检查，则认为已登录
    if (uid && userId && userName && !init) {
      return Promise.resolve(true)
    }

    const query = this.getQueryObject(window.location.href)
    
    // 2. 检查 Cookie 中的 userId (兼容旧逻辑)
    if (getCookie('userId')) {
      (window as any).userId = getCookie('userId');
      (window as any).userName = getCookie('userName');
      
      // 确保缓存同步
      if (!cachedUserInfo) {
        setCache(CACHE_KEY, {
          uid: getCookie('userId'),
          userId: getCookie('userId'),
          userName: getCookie('userName'),
        })
      }
      return Promise.resolve(true)
    }

    // 3. SSO 回调验证：检查 URL 参数中的 login_uid 与 Cookie 中的 login_uid 是否匹配
    // 注意：SSO 登录成功后会写 login_uid cookie 并重定向回带有 login_uid 参数的 URL
    if (query.login_uid && query.login_uid == getCookie('login_uid')) {
      try {
        // 调用外部接口获取用户信息
        // 使用绝对路径调用鉴权服务
        const res = await get<any>(
          `https://corecode-prod.inhuawei.com/developtest/v1/auth/user/${query.login_uid}`
        )
        
        // 适配返回结构：可能是 res.data (ApiResponse) 或直接是数据
        const userData = (res as any).data || res;

        if (userData && (userData.user || userData.uid)) {
           const uid = userData.user || userData.uid
           const name = userData.name || userData.userName

           // 写入 Cookie
           document.cookie = `userId=${uid};path=/`
           document.cookie = `username=${encodeURIComponent(name)};path=/`
           document.cookie = `user_login_time=${new Date().getTime()};path=/`
           
           // 设置全局变量 (兼容旧代码)
           ;(window as any).userId = uid
           ;(window as any).userName = name
           
           // 4. 检查社区成员资格并获取详细信息 (集成旧平台逻辑)
           let communityInfo = {};
           try {
             // 这里调用 application backend
             const memberRes = await checkCommunityMembership(String(uid))
             if (memberRes && memberRes.data && Object.keys(memberRes.data).length > 0) {
                communityInfo = memberRes.data;
             }
           } catch (e) {
             console.warn('获取社区成员信息失败:', e)
           }

           // 更新缓存
           setCache(CACHE_KEY, {
             uid: uid,
             userId: uid,
             userName: name,
             ...userData,
             ...communityInfo, // 合并社区信息 (如 chnName)
             isMember: Object.keys(communityInfo).length > 0 // 标记是否为成员
           });
           
           // 记录访问日志
           const loginTime = getCookie('user_login_time')
           if (loginTime) {
             const accessTime = commonMethods.timestampToDateTime(parseInt(loginTime))
             await commonMethods.addViewsInfo(accessTime)
           }
           
           // 登录成功后，通常建议清除 URL 中的敏感参数，但这取决于具体需求
           // window.history.replaceState({}, document.title, window.location.pathname);
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    } else {
      // 4. 未登录状态处理
      const appHome = this.appHomeUrl
      const currHref = document.location.href
      
      // 避免无限重定向：如果已经包含 redirect 参数或正在进行 login_uid 验证，则不跳转
      if (!currHref.includes('?redirect=') && !currHref.includes('login_uid')) {
         // 环境判断：开发环境是否跳转 SSO
         const isProduction = import.meta.env.PROD || window.location.hostname.includes('huawei');
         
         // 只有在非本地环境或明确配置了 SSO 时才跳转，避免开发时死循环
         // 这里假设 .huawei.com 域名下都启用 SSO
         if (isProduction || window.location.hostname.includes('10.')) {
            console.log('未登录，跳转 SSO')
            window.location.href = `${LOGIN_URL}?redirect=${encodeURIComponent(appHome)}?returnUrl=${encodeURIComponent(currHref)}`
         } else {
             console.warn('开发环境：未检测到登录信息。请手动设置 localStorage user_info 或 cookie userId 以模拟登录。')
         }
      }
    }
    return Promise.resolve(true)
  }
}

export const loginService = new LoginService()

export default loginService
