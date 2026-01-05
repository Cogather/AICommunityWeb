# 故障排除指南

## 常见问题

### 1. 端口被占用错误

**错误信息：**
```
Port 5173 is in use, trying another one...
```
或
```
Error: listen EADDRINUSE: address already in use :::5173
```

**解决方案：**

**方法 1：使用启动脚本（推荐）**
```bash
./start.sh
```
脚本会自动检测并关闭占用端口的进程。

**方法 2：手动关闭占用端口的进程**
```bash
# 查找占用 5173 端口的进程
lsof -i :5173

# 关闭进程（替换 PID 为实际的进程 ID）
kill <PID>
```

**方法 3：使用其他端口**
```bash
npm run dev -- --port 3000
```

### 2. command not found: npm 或 node

**错误信息：**
```
command not found: npm
command not found: node
```

**解决方案：**

确保 Homebrew 环境已加载。如果使用 zsh，运行：
```bash
eval "$(/opt/homebrew/bin/brew shellenv)"
```

或者确保 `~/.zshrc` 文件中包含：
```bash
eval "$(/opt/homebrew/bin/brew shellenv)"
```

然后重新打开终端或运行：
```bash
source ~/.zshrc
```

### 3. 依赖安装失败

**错误信息：**
```
npm ERR! network timeout
npm ERR! SSL certificate problem
```

**解决方案：**

如果是网络问题，可以尝试：
```bash
# 使用国内镜像（如果在中国）
npm config set registry https://registry.npmmirror.com

# 或者使用淘宝镜像
npm config set registry https://registry.npmmirror.com
```

### 4. Element Plus 组件不显示

**检查项：**
1. 确保 `main.ts` 中已导入 Element Plus：
   ```typescript
   import ElementPlus from 'element-plus'
   import 'element-plus/dist/index.css'
   app.use(ElementPlus)
   ```

2. 确保已安装依赖：
   ```bash
   npm install element-plus
   ```

### 5. TypeScript 类型错误

**解决方案：**
```bash
# 运行类型检查查看具体错误
npm run type-check

# 如果只是警告，可以继续开发
# 如果是错误，需要修复代码
```

### 6. 运行诊断脚本

如果遇到其他问题，可以运行诊断脚本：
```bash
./check.sh
```

这会检查：
- 环境配置
- 项目文件完整性
- 依赖安装情况
- 端口占用情况
- 类型检查

## 获取帮助

如果以上方法都无法解决问题，请提供：
1. 完整的错误信息
2. 运行 `./check.sh` 的输出
3. 运行 `npm run dev` 的完整输出

