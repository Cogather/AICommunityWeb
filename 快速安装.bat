@echo off
chcp 65001 >nul
echo ========================================
echo Node.js 和 npm 快速安装检查工具
echo ========================================
echo.

echo 正在检查 Node.js...
where node >nul 2>&1
if %errorlevel% == 0 (
    echo [✓] Node.js 已安装
    node -v
) else (
    echo [✗] Node.js 未安装
    echo.
    echo 请访问以下网址下载安装：
    echo https://nodejs.org/zh-cn
    echo.
    echo 或使用 winget 安装（Windows 10/11）：
    echo winget install OpenJS.NodeJS.LTS
    echo.
    pause
    exit /b 1
)

echo.
echo 正在检查 npm...
where npm >nul 2>&1
if %errorlevel% == 0 (
    echo [✓] npm 已安装
    npm -v
) else (
    echo [✗] npm 未安装
    echo 请重新安装 Node.js（npm 会随 Node.js 一起安装）
    pause
    exit /b 1
)

echo.
echo ========================================
echo 检查完成！
echo ========================================
echo.
echo 下一步操作：
echo 1. 配置 npm 镜像源（可选，推荐）：
echo    npm config set registry https://registry.npmmirror.com
echo.
echo 2. 安装项目依赖：
echo    npm install
echo.
echo 3. 启动开发服务器：
echo    npm run dev
echo.
pause







