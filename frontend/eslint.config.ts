import { globalIgnores } from 'eslint/config'
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
import pluginVue from 'eslint-plugin-vue'
import pluginVitest from '@vitest/eslint-plugin'
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import pluginCypress from 'eslint-plugin-cypress'
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'

// To allow more languages other than `ts` in `.vue` files, uncomment the following lines:
// import { configureVueProject } from '@vue/eslint-config-typescript'
// configureVueProject({ scriptLangs: ['ts', 'tsx'] })
// More info at https://github.com/vuejs/eslint-config-typescript/#advanced-setup

export default defineConfigWithVueTs(
  {
    name: 'app/files-to-lint',
    files: ['**/*.{ts,mts,tsx,vue}'],
  },

  globalIgnores(['**/dist/**', '**/dist-ssr/**', '**/coverage/**', '**/*.cjs']),

  pluginVue.configs['flat/essential'],
  vueTsConfigs.recommended,

  // 自定义规则
  {
    rules: {
      // 允许使用 any 类型（降级为警告）
      '@typescript-eslint/no-explicit-any': 'warn',
      // 未使用变量以 _ 开头时忽略
      '@typescript-eslint/no-unused-vars': ['error', { 
        argsIgnorePattern: '^_',
        varsIgnorePattern: '^_'
      }],
      // 禁用废弃过滤器检测（TypeScript 联合类型误报）
      'vue/no-deprecated-filter': 'off',
      // 允许单词组件名称
      'vue/multi-word-component-names': 'off',
    },
  },
  
  {
    ...pluginVitest.configs.recommended,
    files: ['src/**/__tests__/*'],
  },
  
  {
    ...pluginCypress.configs.recommended,
    files: [
      'cypress/e2e/**/*.{cy,spec}.{js,ts,jsx,tsx}',
      'cypress/support/**/*.{js,ts,jsx,tsx}'
    ],
  },
  skipFormatting,
)
