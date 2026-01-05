#!/bin/bash

# 设置 Homebrew 环境
eval "$(/opt/homebrew/bin/brew shellenv)"

cd "$(dirname "$0")"

echo "========================================="
echo "项目诊断信息"
echo "========================================="
echo ""

echo "1. 环境检查:"
echo "   Node: $(node --version 2>&1 || echo '❌ 未找到')"
echo "   NPM:  $(npm --version 2>&1 || echo '❌ 未找到')"
echo "   当前目录: $(pwd)"
echo ""

echo "2. 项目文件检查:"
[ -f "package.json" ] && echo "   ✅ package.json 存在" || echo "   ❌ package.json 不存在"
[ -d "node_modules" ] && echo "   ✅ node_modules 存在" || echo "   ❌ node_modules 不存在"
[ -f "vite.config.ts" ] && echo "   ✅ vite.config.ts 存在" || echo "   ❌ vite.config.ts 不存在"
[ -f "src/main.ts" ] && echo "   ✅ src/main.ts 存在" || echo "   ❌ src/main.ts 不存在"
echo ""

echo "3. 依赖检查:"
if [ -d "node_modules" ]; then
    [ -d "node_modules/vue" ] && echo "   ✅ vue 已安装" || echo "   ❌ vue 未安装"
    [ -d "node_modules/element-plus" ] && echo "   ✅ element-plus 已安装" || echo "   ❌ element-plus 未安装"
    [ -d "node_modules/vite" ] && echo "   ✅ vite 已安装" || echo "   ❌ vite 未安装"
else
    echo "   ⚠️  node_modules 不存在，需要运行 npm install"
fi
echo ""

echo "4. 端口检查 (5173 是 Vite 默认端口):"
if lsof -Pi :5173 -sTCP:LISTEN -t >/dev/null 2>&1 ; then
    echo "   ⚠️  端口 5173 已被占用"
    lsof -Pi :5173 -sTCP:LISTEN
else
    echo "   ✅ 端口 5173 可用"
fi
echo ""

echo "5. 尝试类型检查:"
npm run type-check 2>&1 | head -20
echo ""

echo "========================================="
echo "如果以上检查都通过，请尝试运行:"
echo "npm run dev"
echo "========================================="

