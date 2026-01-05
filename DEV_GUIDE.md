# 开发指南

## 保持开发服务器持续运行

### 方法 1：使用启动脚本（推荐）

在终端中运行：

```bash
cd /Users/lunayuan/Documents/AICommunityWeb/ai-community-web
./start.sh
```

这个脚本会：
- 自动设置环境变量
- 检查并清理端口占用
- 启动开发服务器

**重要**：保持这个终端窗口打开，不要关闭它。服务器会持续运行，支持热更新。

### 方法 2：手动启动

在终端中运行：

```bash
cd /Users/lunayuan/Documents/AICommunityWeb/ai-community-web
npm run dev
```

同样，保持终端窗口打开。

## 热模块替换 (HMR)

Vite 开发服务器支持**热模块替换**，这意味着：

✅ **修改代码后，只需刷新浏览器即可看到更新**
✅ **不需要重启服务器**
✅ **某些情况下甚至不需要刷新，会自动更新**

### 工作流程

1. **启动服务器**（只需一次）
   ```bash
   npm run dev
   ```

2. **在浏览器中打开**
   ```
   http://localhost:5173
   ```

3. **修改代码**
   - 编辑任何 `.vue`、`.ts`、`.scss` 文件
   - 保存文件

4. **查看更新**
   - 浏览器会自动刷新（或显示更新提示）
   - 如果没自动刷新，按 `Cmd+R` (Mac) 或 `F5` (Windows) 手动刷新

### 停止服务器

在运行 `npm run dev` 的终端窗口中：
- 按 `Ctrl + C` 停止服务器

## 常见问题

### Q: 修改代码后没有自动更新？

**A:** 尝试以下方法：
1. 检查终端中是否有错误信息
2. 手动刷新浏览器（`Cmd+R` 或 `F5`）
3. 检查浏览器控制台是否有错误
4. 确认文件已保存

### Q: 端口被占用？

**A:** 运行诊断脚本：
```bash
./check.sh
```

或者手动关闭占用端口的进程：
```bash
lsof -ti:5173 | xargs kill
```

### Q: 服务器意外停止？

**A:** 重新启动：
```bash
npm run dev
```

## 开发技巧

### 1. 使用两个终端窗口

- **终端 1**：运行 `npm run dev`（保持运行）
- **终端 2**：运行其他命令（如 `npm run lint`、`npm run type-check` 等）

### 2. 浏览器开发者工具

打开浏览器开发者工具（`F12` 或 `Cmd+Option+I`）：
- **Console**：查看错误和日志
- **Network**：查看资源加载
- **Elements**：检查 DOM 结构

### 3. 代码检查

在另一个终端运行：
```bash
# 类型检查
npm run type-check

# 代码规范检查
npm run lint

# 格式化代码
npm run format
```

## 生产构建

开发完成后，构建生产版本：

```bash
npm run build
```

构建产物在 `dist/` 目录中。

