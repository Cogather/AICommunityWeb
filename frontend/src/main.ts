import './assets/main.css'
import 'element-plus/dist/index.css'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'

import App from './App.vue'
import router from './router'

import loginService from './utils/loginService'

const app = createApp(App)

// 添加全局错误处理
app.config.errorHandler = (err, instance, info) => {
  console.error('Global error:', err, info)
  // 如果是路由相关的错误，尝试重新加载页面
  if (err && typeof err === 'object' && 'message' in err) {
    const message = String(err.message)
    if (message.includes('component') || message.includes('router')) {
      console.warn('Router error detected, this might be a temporary issue')
    }
  }
}

app.use(router)
app.use(ElementPlus)

// 启动前验证登录状态
loginService.validate(true).then(() => {
  app.mount('#app')
}).catch(err => {
  console.error('Login validation failed:', err)
  // 即使验证失败也尝试挂载，由路由守卫处理跳转
  app.mount('#app')
})
