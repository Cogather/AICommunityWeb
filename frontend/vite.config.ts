import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// ==================== 后端配置 ====================
// 🔧 在这里修改后端地址
const BACKEND_URL = 'http://10.189.4.114:8888'
const API_PATH = '/aicommunity'  // 后端 API 路径前缀

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  // 加载环境变量
  const env = loadEnv(mode, process.cwd(), '')

  // ==================== 基础路径配置 ====================
  // 所有页面路由都将以 /ai_community 为前缀
  const BASE_URL = '/ai_community'

  return {
    base: BASE_URL,

    plugins: [
      vue(),
      // 只在开发环境启用 vue-dev-tools
      ...(mode === 'development' ? [vueDevTools()] : []),
      // 自定义 HTML 转换插件：用于动态替换标题和 Logo
      {
        name: 'html-transform',
        transformIndexHtml(html) {
          let newHtml = html
          // 替换标题
          if (env.VITE_APP_TITLE) {
            newHtml = newHtml.replace(
              /<title>(.*?)<\/title>/,
              `<title>${env.VITE_APP_TITLE}</title>`
            )
          }
          // 替换 Favicon (Logo)
          if (env.VITE_APP_FAVICON) {
            // 处理路径：如果配置了 base 且 favicon 是绝对路径，需要拼接 base
            let faviconPath = env.VITE_APP_FAVICON
            if (faviconPath.startsWith('/')) {
              // 移除开头的 / 防止双重斜杠（如果 BASE_URL 以 / 结尾）
              // 但这里 BASE_URL = '/ai_community'，没有结尾 /
              // 开发环境如果 base 是 /，则不需要特殊处理（或者拼成 //logo.svg 也是合法的）
              // 简单处理：如果不是开发环境，或者是生产构建，确保路径包含 base

              // 注意：在构建时，base 会生效。
              // 如果 BASE_URL 不是 /，则拼接
              if (BASE_URL !== '/') {
                 faviconPath = `${BASE_URL}${faviconPath}`
              }
            }

            newHtml = newHtml.replace(
              /<link rel="icon" href="(.*?)">/,
              `<link rel="icon" href="${faviconPath}">`
            )
          }
          return newHtml
        }
      }
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

      // ==================== 热更新配置 ====================
      hmr: {
        overlay: true, // 显示错误覆盖层
      },

      // 监听文件变化
      watch: {
        usePolling: true, // 使用轮询方式监听文件变化（解决某些文件系统的兼容问题）
        interval: 100, // 轮询间隔（毫秒）
      },

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
  }
})
