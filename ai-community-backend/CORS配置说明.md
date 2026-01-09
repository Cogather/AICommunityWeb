# CORS跨域配置说明

## 配置概述

已为项目添加CORS（Cross-Origin Resource Sharing）跨域配置，解决前端调用后端API时的跨域问题。

## 配置文件

### 1. CORS配置类
- **文件**: `src/main/java/com/aicommunity/config/CorsConfig.java`
- **功能**: 
  - 实现 `WebMvcConfigurer` 接口，配置全局CORS规则
  - 提供 `CorsFilter` Bean，处理OPTIONS预检请求

### 2. 配置文件
- **开发环境**: `application-dev.yml`
- **生产环境**: `application-prod.yml`
- **主配置**: `application.yml`

## 配置项说明

### cors.allow-all
- **类型**: boolean
- **默认值**: true
- **说明**: 
  - `true`: 允许所有源（开发环境推荐）
  - `false`: 只允许配置的源（生产环境推荐）

### cors.allowed-origins
- **类型**: string
- **默认值**: 空
- **说明**: 
  - 允许的前端地址，多个用逗号分隔
  - 例如: `http://localhost:3000,http://localhost:5173`
  - 生产环境示例: `https://aicommunity.example.com`

## 配置示例

### 开发环境配置（application-dev.yml）
```yaml
cors:
  # 开发环境允许所有源
  allow-all: true
  # 也可以配置具体的前端地址
  allowed-origins: http://localhost:3000,http://localhost:5173
```

### 生产环境配置（application-prod.yml）
```yaml
cors:
  # 生产环境不允许所有源
  allow-all: false
  # 配置生产环境的前端地址
  allowed-origins: https://aicommunity.example.com,https://www.aicommunity.example.com
```

### 使用环境变量（生产环境推荐）
```yaml
cors:
  allow-all: false
  allowed-origins: ${CORS_ALLOWED_ORIGINS:}
```

然后在启动时设置环境变量：
```bash
export CORS_ALLOWED_ORIGINS=https://aicommunity.example.com
java -jar ai-community-backend.jar
```

## 配置的CORS规则

### 允许的HTTP方法
- GET
- POST
- PUT
- DELETE
- OPTIONS
- PATCH

### 允许的请求头
- 所有请求头（`*`）

### 暴露的响应头
- `Authorization`: JWT Token
- `Content-Type`: 内容类型
- `X-Total-Count`: 分页总数（如果使用）

### 允许携带凭证
- `true`: 允许携带Cookie、Authorization等凭证信息

### 预检请求缓存时间
- `3600秒`（1小时）

## 使用说明

### 1. 开发环境
开发环境默认允许所有源，无需额外配置即可使用。

### 2. 生产环境
生产环境需要：
1. 设置 `cors.allow-all: false`
2. 配置 `cors.allowed-origins` 为具体的前端地址
3. 建议使用环境变量配置，提高安全性

### 3. 前端调用示例
```javascript
// 使用fetch
fetch('http://localhost:8080/api/posts', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer ' + token,
    'Content-Type': 'application/json'
  },
  credentials: 'include' // 允许携带凭证
})
.then(response => response.json())
.then(data => console.log(data));

// 使用axios
axios.get('http://localhost:8080/api/posts', {
  headers: {
    'Authorization': 'Bearer ' + token
  },
  withCredentials: true // 允许携带凭证
})
.then(response => console.log(response.data));
```

## 安全建议

1. **开发环境**: 可以使用 `allow-all: true` 方便开发调试
2. **生产环境**: 
   - 必须设置 `allow-all: false`
   - 必须配置具体的 `allowed-origins`
   - 建议使用环境变量配置，避免敏感信息泄露
3. **HTTPS**: 生产环境建议使用HTTPS协议
4. **定期审查**: 定期审查允许的源地址，移除不再使用的前端地址

## 常见问题

### Q1: 为什么前端还是报跨域错误？
A: 检查以下几点：
1. 确认后端服务已启动
2. 检查前端请求的URL是否正确
3. 确认 `cors.allow-all` 或 `cors.allowed-origins` 配置正确
4. 检查浏览器控制台的错误信息

### Q2: 预检请求（OPTIONS）失败？
A: 确认 `CorsFilter` Bean已正确配置，检查日志是否有相关错误。

### Q3: 携带凭证的请求失败？
A: 确认：
1. `allowCredentials` 设置为 `true`
2. 前端请求设置了 `credentials: 'include'` 或 `withCredentials: true`
3. 如果使用 `allowedOrigins`（非 `allowedOriginPatterns`），不能使用通配符 `*`

### Q4: 生产环境如何配置？
A: 
1. 创建 `application-prod.yml` 配置文件
2. 设置 `cors.allow-all: false`
3. 配置 `cors.allowed-origins` 为生产环境的前端地址
4. 启动时指定profile: `--spring.profiles.active=prod`

## 测试验证

### 使用curl测试CORS配置
```bash
# 测试OPTIONS预检请求
curl -X OPTIONS http://localhost:8080/api/posts \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: GET" \
  -H "Access-Control-Request-Headers: Authorization" \
  -v

# 应该返回以下响应头：
# Access-Control-Allow-Origin: http://localhost:3000
# Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS,PATCH
# Access-Control-Allow-Headers: *
# Access-Control-Allow-Credentials: true
# Access-Control-Max-Age: 3600
```

---

**配置完成时间**: 2024-01-09
**配置状态**: ✅ 已完成
