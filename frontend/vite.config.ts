import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// ==================== 后端配置 ====================
// 🔧 在这里修改后端地址
const BACKEND_URL = 'http://10.189.4.114:8888'
const API_PATH = '/aicommunity'  // 后端 API 路径前缀

// https://vite.dev/config/
export default defineConfig({
  // ==================== 基础路径配置 ====================
  // 所有页面路由都将以 /ai_community 为前缀
  base: '/ai_community',
  
  plugins: [
    vue(),
    // 只在开发环境启用 vue-dev-tools
    ...(process.env.NODE_ENV === 'development' ? [vueDevTools()] : []),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    // ==================== 网络配置 ====================
    // true 或 '0.0.0.0': 允许局域网访问（可通过 IP 访问）
    // 'localhost': 仅本机访问
    host: 'localhost',
    port: 5173,
    strictPort: false, // 如果端口被占用，尝试下一个可用端口
    
    // 启动时自动打开浏览器
    open: false,
    
    // ==================== 代理配置 ====================
    // 当使用代理模式时（request.ts 中 API_BASE_URL = '/api'），
    // 所有 /api 开头的请求会被代理到 BACKEND_URL/aicommunity/api
    proxy: {
      '/api': {
        target: BACKEND_URL,
        changeOrigin: true,
        secure: false,
        // 重写路径：/api/xxx -> /aicommunity/api/xxx
        rewrite: (path) => `${API_PATH}${path}`,
        // 可选：打印代理日志，方便调试
        configure: (proxy, _options) => {
          proxy.on('proxyReq', (proxyReq, req, _res) => {
            console.log(`[Proxy] ${req.method} ${req.url} -> ${BACKEND_URL}${proxyReq.path}`)
          })
          proxy.on('proxyRes', (proxyRes, req, _res) => {
            console.log(`[Proxy] ${req.url} <- ${proxyRes.statusCode}`)
          })
          proxy.on('error', (err, req, _res) => {
            console.error(`[Proxy Error] ${req.url}:`, err.message)
          })
        }
      }
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        // 使用 sass-embedded，如果遇到权限问题可以尝试其他选项
        api: 'modern-compiler',
      },
    },
  },
})
