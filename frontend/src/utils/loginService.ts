import { get } from '@/api/request'
import { checkCommunityMembership } from '@/api/user'
import commonMethods from '@/utils/common'

// 缓存 Key
const CACHE_KEY = 'user_info'

// 基础 URL 配置
const NGX_BASE_URL = 'https://coretool.rnd.huawei.com'

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
    console.log('window.location.href', window.location.href);
    window.location.href = 'https://login.huawei.com/login/?redirect='
      + `${encodeURIComponent('http://coretool.rnd.huawei.com/#/home')}?returnUrl=${encodeURIComponent(window.location.href)}`;
  }

  logout() {
    removeCache(CACHE_KEY)
    // 清除 cookie (可选，视具体需求)
    document.cookie = 'userId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'
    document.cookie = 'username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'

    // 登出后通常跳转回当前页或首页，触发 SSO 重新登录流程或显示未登录态
    const params = new URLSearchParams()
    params.append('redirect', window.location.href)
    window.location.href = `https://login.huawei.com/login/?${params.toString()}`
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

  // 完善用户信息：获取社区身份、管理员权限、头像等
  async fetchDetailedUserInfo(userId: string) {
    let communityInfo: any = {};
    let isAdmin = false;

    // 1. 检查社区成员资格 (对应 checkMembership)
    try {
      const memberRes = await checkCommunityMembership(userId);
      console.log('LoginService - checkCommunityMembership response:', memberRes); // Log raw response
      if (memberRes && memberRes.data && Object.keys(memberRes.data).length > 0) {
        communityInfo = memberRes.data;
        // 兼容旧代码，将详细信息（包含 userId）存入 localStorage 的 userMessage
        localStorage.setItem('userMessage', JSON.stringify({ ...memberRes.data, userId }));
      }
    } catch (e) {
      console.warn('获取社区成员信息失败:', e);
    }

    // 2. 检查管理员权限 (对应 fetchAdminFlag)
    try {
      // 假设 getManager 返回管理员列表
      const adminRes = await import('@/api/user').then(m => m.getManager());
      const adminIds = (adminRes.data || []).map((item: any) => item.userName);
      isAdmin = adminIds.includes(userId);
    } catch (e) {
      console.warn('获取管理员权限失败:', e);
    }

    // 3. 构造头像 URL (对应 setAvatarUrl)
    // 格式: https://w3.huawei.com/w3lab/rest/yellowpage/face/{工号}/120
    // 注意：userId 可能是 'x12345' 或 '12345'，需要根据实际情况截取
    const employeeId = userId.startsWith('x') || userId.startsWith('y') ? userId.substring(1) : userId;
    const avatarUrl = `https://w3.huawei.com/w3lab/rest/yellowpage/face/${employeeId}/120`;

    // 4. 更新主缓存
    const currentCache = getCache(CACHE_KEY) || {};
      const detailedUser = {
        ...currentCache,
        ...communityInfo, // 合并 chnName 等字段
        isMember: Object.keys(communityInfo).length > 0,
        isAdmin,
        avatar: avatarUrl, // 设置标准头像
        employeeId: userId, // 确保 employeeId 存在
      };

      console.log('LoginService - Detailed User Info:', detailedUser); // Add logging

      // 兼容旧代码，将详细信息（包含 userId）存入 localStorage 的 userMessage
      localStorage.setItem('userMessage', JSON.stringify(detailedUser));

      setCache(CACHE_KEY, detailedUser);
      return detailedUser;
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

    // 3. SSO 回调验证
    if (query.login_uid && query.login_uid == getCookie('login_uid')) {
      try {
        // 调用外部接口获取用户信息
        // 使用绝对路径调用鉴权服务
        const res = await get<any>(
          `https://corecode-prod.inhuawei.com/developtest/v1/auth/user/${query.login_uid}`
        )

        // 适配返回结构
        const userData = (res as any).data || res;
        console.log('LoginService - SSO User Data:', userData); // Log raw SSO response

        if (userData && (userData.user || userData.uid)) {
           // 写入 Cookie
           document.cookie = `userId=${userData.user};path=/`
           document.cookie = `username=${userData.name};path=/`
           document.cookie = `user_login_time=${new Date().getTime()};path=/`

           // 设置全局变量
           ;(window as any).userId = userData.user
           ;(window as any).userName = userData.name

           // 更新缓存基础信息
           setCache(CACHE_KEY, {
             uid: userData.user,
             userId: userData.user,
             userName: userData.name,
           });

           // 4. 获取详细社区信息 (集成旧平台逻辑)
           // 包含 isAdmin, isMember, chnName, avatarUrl, points 等
           await this.fetchDetailedUserInfo(userData.user);

           // 记录访问日志
           const loginTime = getCookie('user_login_time')
           if (loginTime) {
             const accessTime = commonMethods.timestampToDateTime(parseInt(loginTime))
             await commonMethods.addViewsInfo(accessTime)
           }
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    } else {
      // 4. 未登录状态处理
      const currHref = document.location.href

      // 避免无限重定向：如果已经包含 redirect 参数或正在进行 login_uid 验证，则不跳转
      if (!currHref.includes('?redirect=') && !currHref.includes('login_uid')) {
         // 环境判断：开发环境是否跳转 SSO
         // 判定规则：只在域名包含 huawei 时跳转 SSO (生产环境)
         // 本地环境 (localhost, 127.0.0.1) 和 局域网环境 (IP访问) 均使用 Mock 登录
         const isProduction = window.location.hostname.includes('huawei');

         if (isProduction) {
            console.log('未登录，跳转 SSO')
            window.location.href = 'https://login.huawei.com/login/?'
              + `redirect=${encodeURIComponent('http://coretool.rnd.huawei.com/#/home')}?returnUrl=${encodeURIComponent(currHref)}`;
         } else {
             // 本地开发环境：使用 Mock 数据模拟登录
             console.log('开发环境：使用 Mock 用户登录')
             const mockUser = {
               uid: 'y30022452',
               userId: 'y30022452',
               userName: 'yuanrongqian',
               name: 'yuanrongqian',
               chnName: '袁榕谦',
               isMember: true,
               isAdmin: true,
               avatar: 'https://w3.huawei.com/w3lab/rest/yellowpage/face/30022452/120'
             }

             console.log('LoginService - Mock User:', mockUser); // Add logging

             // 设置全局变量
             ;(window as any).userId = mockUser.userId
             ;(window as any).userName = mockUser.userName

             // 写入 Cookie
             document.cookie = `userId=${mockUser.userId};path=/`
             document.cookie = `username=${mockUser.userName};path=/`

             // 更新缓存
             setCache(CACHE_KEY, mockUser)
             localStorage.setItem('userMessage', JSON.stringify(mockUser))

             return Promise.resolve(true)
         }
      }
    }
    return Promise.resolve(true)
  }
}

export const loginService = new LoginService()

export default loginService
