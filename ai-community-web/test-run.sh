#!/bin/bash

# 设置 Homebrew 环境
eval "$(/opt/homebrew/bin/brew shellenv)"

# 进入项目目录
cd "$(dirname "$0")"

echo "=== 环境检查 ==="
echo "Node 版本: $(node --version)"
echo "NPM 版本: $(npm --version)"
echo "当前目录: $(pwd)"
echo ""

echo "=== 检查依赖 ==="
if [ ! -d "node_modules" ]; then
    echo "❌ node_modules 不存在，正在安装依赖..."
    npm install
else
    echo "✅ node_modules 存在"
fi
echo ""

echo "=== 尝试运行开发服务器 ==="
npm run dev

