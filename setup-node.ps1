# Node.js 和 npm 环境配置脚本
# 使用方法：以管理员身份运行 PowerShell，然后执行：.\setup-node.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Node.js 和 npm 环境配置工具" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 检查是否以管理员身份运行
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Host "警告: 建议以管理员身份运行此脚本以获得完整功能" -ForegroundColor Yellow
    Write-Host ""
}

# 1. 检查 Node.js 是否已安装
Write-Host "1. 检查 Node.js 安装状态..." -ForegroundColor Green
try {
    $nodeVersion = node -v 2>$null
    if ($nodeVersion) {
        Write-Host "   ✓ Node.js 已安装: $nodeVersion" -ForegroundColor Green
    }
} catch {
    Write-Host "   ✗ Node.js 未安装或未添加到 PATH" -ForegroundColor Red
}

# 2. 检查 npm 是否可用
Write-Host ""
Write-Host "2. 检查 npm 安装状态..." -ForegroundColor Green
try {
    $npmVersion = npm -v 2>$null
    if ($npmVersion) {
        Write-Host "   ✓ npm 已安装: v$npmVersion" -ForegroundColor Green
    }
} catch {
    Write-Host "   ✗ npm 未安装或未添加到 PATH" -ForegroundColor Red
}

# 3. 检查常见的 Node.js 安装路径
Write-Host ""
Write-Host "3. 检查 Node.js 安装路径..." -ForegroundColor Green
$nodePaths = @(
    "C:\Program Files\nodejs",
    "C:\Program Files (x86)\nodejs",
    "$env:APPDATA\npm",
    "$env:LOCALAPPDATA\Programs\nodejs"
)

$foundNode = $false
foreach ($path in $nodePaths) {
    if (Test-Path $path) {
        Write-Host "   ✓ 找到路径: $path" -ForegroundColor Green
        $foundNode = $true
        
        # 检查 node.exe
        $nodeExe = Join-Path $path "node.exe"
        if (Test-Path $nodeExe) {
            Write-Host "     - node.exe 存在" -ForegroundColor Gray
        }
        
        # 检查 npm
        $npmPath = Join-Path $path "npm.cmd"
        if (Test-Path $npmPath) {
            Write-Host "     - npm.cmd 存在" -ForegroundColor Gray
        }
    }
}

if (-not $foundNode) {
    Write-Host "   ✗ 未找到 Node.js 安装目录" -ForegroundColor Red
}

# 4. 检查环境变量 PATH
Write-Host ""
Write-Host "4. 检查 PATH 环境变量..." -ForegroundColor Green
$envPath = [Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [Environment]::GetEnvironmentVariable("Path", "User")
$pathEntries = $envPath -split ";"

$nodeInPath = $false
foreach ($entry in $pathEntries) {
    if ($entry -like "*nodejs*" -and $entry -ne "") {
        Write-Host "   ✓ PATH 中包含: $entry" -ForegroundColor Green
        $nodeInPath = $true
    }
}

if (-not $nodeInPath) {
    Write-Host "   ✗ PATH 中未找到 Node.js 路径" -ForegroundColor Red
}

# 5. 提供解决方案
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "解决方案" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

try {
    $nodeVersion = node -v 2>$null
    $npmVersion = npm -v 2>$null
    if ($nodeVersion -and $npmVersion) {
        Write-Host "✓ Node.js 和 npm 已正确安装！" -ForegroundColor Green
        Write-Host ""
        Write-Host "下一步操作：" -ForegroundColor Yellow
        Write-Host "1. 在项目目录下运行: npm install" -ForegroundColor White
        Write-Host "2. 安装完成后运行: npm run dev" -ForegroundColor White
    } else {
        Write-Host "需要安装 Node.js：" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "方法 1: 手动安装（推荐）" -ForegroundColor Cyan
        Write-Host "1. 访问: https://nodejs.org/zh-cn" -ForegroundColor White
        Write-Host "2. 下载 LTS 版本（长期支持版）" -ForegroundColor White
        Write-Host "3. 运行安装程序，使用默认选项" -ForegroundColor White
        Write-Host "4. 安装完成后重启终端或电脑" -ForegroundColor White
        Write-Host ""
        Write-Host "方法 2: 使用 Chocolatey（如果已安装）" -ForegroundColor Cyan
        Write-Host "choco install nodejs-lts" -ForegroundColor White
        Write-Host ""
        Write-Host "方法 3: 使用 winget（Windows 10/11）" -ForegroundColor Cyan
        Write-Host "winget install OpenJS.NodeJS.LTS" -ForegroundColor White
    }
} catch {
    Write-Host "需要安装 Node.js，请按照上面的方法进行安装" -ForegroundColor Yellow
}

# 6. 配置 npm 镜像（如果 npm 可用）
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "配置 npm 镜像源（可选）" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

try {
    $npmVersion = npm -v 2>$null
    if ($npmVersion) {
        Write-Host "检测到 npm，是否配置国内镜像源？" -ForegroundColor Yellow
        Write-Host "这将使用淘宝镜像加速下载（推荐在中国大陆使用）" -ForegroundColor Gray
        Write-Host ""
        $response = Read-Host "是否配置？(Y/N)"
        if ($response -eq "Y" -or $response -eq "y") {
            npm config set registry https://registry.npmmirror.com
            npm config set disturl https://npmmirror.com/mirrors/node
            npm config set electron_mirror https://npmmirror.com/mirrors/electron/
            Write-Host ""
            Write-Host "✓ npm 镜像源已配置为淘宝镜像" -ForegroundColor Green
            Write-Host "当前镜像源:" -ForegroundColor Gray
            npm config get registry
        }
    } else {
        Write-Host "npm 不可用，请先安装 Node.js" -ForegroundColor Yellow
    }
} catch {
    Write-Host "无法配置 npm 镜像源，请先安装 Node.js" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "配置完成！" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "按任意键退出..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")






