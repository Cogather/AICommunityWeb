# AI Community Web

Vue 3 + TypeScript + Element Plus 项目

## 技术栈

- Vue 3 (Composition API)
- TypeScript
- Vue Router
- Pinia (状态管理)
- Element Plus (UI 组件库)
- Vite (构建工具)
- Vitest (单元测试)
- Cypress (E2E 测试)
- ESLint + Prettier (代码规范)

## 环境要求

- Node.js >= 20.19.0 或 >= 22.12.0
- npm

## 安装依赖

```bash
npm install
```

## 开发

```bash
npm run dev
```

如果遇到 `command not found: npm` 或 `command not found: node` 错误，请先执行：

```bash
eval "$(/opt/homebrew/bin/brew shellenv)"
```

或者将以下内容添加到你的 `~/.zshrc` 文件中（如果使用 zsh）：

```bash
eval "$(/opt/homebrew/bin/brew shellenv)"
```

然后重新打开终端或执行：

```bash
source ~/.zshrc
```

## 构建

```bash
npm run build
```

## 预览构建结果

```bash
npm run preview
```

## 代码检查

```bash
npm run lint
```

## 格式化代码

```bash
npm run format
```

## 类型检查

```bash
npm run type-check
```

## 运行测试

### 单元测试

```bash
npm run test:unit
```

### E2E 测试

```bash
npm run test:e2e
```

## 项目结构

```
ai-community-web/
├── src/
│   ├── assets/          # 静态资源
│   ├── components/      # 组件
│   ├── router/          # 路由配置
│   ├── stores/          # Pinia 状态管理
│   ├── views/           # 页面视图
│   ├── App.vue          # 根组件
│   └── main.ts          # 入口文件
├── public/              # 公共资源
└── package.json         # 项目配置
```
