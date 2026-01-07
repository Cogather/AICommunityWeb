import './assets/main.css'
import 'element-plus/dist/index.css'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'

import App from './App.vue'
import router from './router'

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

app.mount('#app')
