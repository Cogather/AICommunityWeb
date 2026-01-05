#!/bin/bash

# 设置 Homebrew 环境
eval "$(/opt/homebrew/bin/brew shellenv)"

# 进入项目目录
cd "$(dirname "$0")"

# 检查并清理端口 5173
PORT=5173
PID=$(lsof -ti:$PORT 2>/dev/null)
if [ ! -z "$PID" ]; then
    echo "⚠️  检测到端口 $PORT 被占用 (PID: $PID)，正在关闭..."
    kill $PID 2>/dev/null
    sleep 1
    echo "✅ 端口已释放"
fi

# 运行开发服务器
echo "🚀 启动开发服务器..."
echo ""
echo "═══════════════════════════════════════════════════════"
echo "  📝 开发服务器已启动！"
echo ""
echo "  🌐 访问地址: http://localhost:5173"
echo ""
echo "  💡 提示："
echo "     - 保持此终端窗口打开，服务器会持续运行"
echo "     - 修改代码后，刷新浏览器即可看到更新"
echo "     - 按 Ctrl+C 可停止服务器"
echo ""
echo "  🔥 热更新已启用，代码修改会自动反映到浏览器"
echo "═══════════════════════════════════════════════════════"
echo ""
npm run dev

