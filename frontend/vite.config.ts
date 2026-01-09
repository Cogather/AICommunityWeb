import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
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
    host: '0.0.0.0', // 监听所有网络接口
    port: 5173,
    strictPort: false, // 如果端口被占用，尝试下一个可用端口
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
