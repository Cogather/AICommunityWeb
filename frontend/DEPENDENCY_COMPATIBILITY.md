# 依赖兼容性检查报告

## 问题总结

检查发现以下依赖包不兼容 Node.js 18.20.1：

1. **@vitejs/plugin-vue@6.0.3**
   - 要求：`^20.19.0 || >=22.12.0`
   - 状态：❌ 不兼容 Node.js 18.20.1

2. **unplugin-utils@0.3.1**
   - 要求：`>=20.19.0`
   - 状态：❌ 不兼容 Node.js 18.20.1

3. **vite@6.4.1**
   - 要求：`^18.0.0 || ^20.0.0 || >=22.0.0`
   - 状态：⚠️ 理论上兼容，但配合的插件不兼容

## 已执行的修复

### 1. 降级 @vitejs/plugin-vue
- **从**: `^6.0.2` → **到**: `^5.2.1`
- **原因**: v6.x 要求 Node.js >= 20.19.0，v5.x 兼容 Node.js 18

### 2. 降级 vite
- **从**: `^6.0.0` → **到**: `^5.4.11`
- **原因**: 确保与 @vitejs/plugin-vue 5.x 版本兼容

### 3. 降级 vite-plugin-vue-devtools
- **从**: `^8.0.5` → **到**: `^7.0.7`
- **原因**: v8.x 可能依赖不兼容的 unplugin-utils，v7.x 应该使用兼容版本

### 4. 添加 overrides 锁定 unplugin-utils
- **添加**: `"unplugin-utils": "^0.2.0"`
- **原因**: 强制所有依赖使用兼容 Node.js 18 的 unplugin-utils 版本

### 5. 更新 engines 字段
- **从**: `"node": ">=18.0.0"`
- **到**: `"node": ">=18.0.0 <18.20.2"`
- **原因**: 明确限制 Node.js 版本在 18.20.1 以下

## 下一步操作

1. **删除旧的依赖文件**：
   ```bash
   rm -rf node_modules package-lock.json
   ```

2. **重新安装依赖**：
   ```bash
   npm install
   ```

3. **验证安装**：
   ```bash
   npm list unplugin-utils @vitejs/plugin-vue vite
   ```

4. **检查 Node.js 版本要求**：
   ```bash
   node check-node-versions.cjs
   node check-unplugin-utils.cjs
   ```

## 兼容性验证

安装完成后，请验证以下包的 Node.js 版本要求：

- ✅ `@vitejs/plugin-vue@5.x` - 应该兼容 Node.js 18
- ✅ `vite@5.x` - 应该兼容 Node.js 18
- ✅ `unplugin-utils@0.2.x` - 应该兼容 Node.js 18

## 注意事项

1. **npm overrides** 功能需要 npm >= 8.3.0
2. 如果使用 yarn，需要使用 `resolutions` 字段替代 `overrides`
3. 如果使用 pnpm，需要使用 `pnpm.overrides` 字段

## 参考链接

- [Vite 5.x 文档](https://vitejs.dev/)
- [@vitejs/plugin-vue 版本历史](https://www.npmjs.com/package/@vitejs/plugin-vue?activeTab=versions)
- [unplugin-utils 版本历史](https://www.npmjs.com/package/unplugin-utils?activeTab=versions)
