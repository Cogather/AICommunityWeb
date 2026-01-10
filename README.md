# AI社区平台

AI社区平台是一个集成了前端和后端的完整项目，提供AI工具分享、实践交流、活动管理等功能的社区平台。

## 项目结构

```
AICommunityWeb/
├── frontend/                # 前端项目 (Vue 3 + TypeScript + Vite)
├── ai-community-backend/   # 后端项目 (Spring Boot 2.7.x + MyBatis + MySQL)
├── API接口需求文档.md       # API接口文档
└── README.md               # 项目说明文档
```

## 技术栈

### 前端
- Vue 3 (Composition API)
- TypeScript
- Vite
- Element Plus
- Vue Router
- Pinia (状态管理)

### 后端
- Spring Boot 2.7.18
- MyBatis
- MySQL 5.7
- JWT (认证)
- Swagger 2 (API文档)

## 快速开始

### 前端开发

```bash
cd frontend
npm install
npm run dev
```

### 后端开发

```bash
cd ai-community-backend
# 配置数据库连接信息
# 修改 src/main/resources/application.yml
mvn clean install
mvn spring-boot:run
```

详细文档请参考：
- 前端：`frontend/README.md`
- 后端：`ai-community-backend/README.md`
- API接口：`API接口需求文档.md`

## 功能模块

- 用户认证与权限管理
- 帖子发布与管理
- 评论与互动
- 活动创建与报名
- 荣誉系统与积分
- 管理后台
- AI工具专区

## 开发规范

- 前端代码遵循 Vue 3 最佳实践
- 后端代码遵循阿里巴巴Java开发手册
- API接口统一返回格式
- 代码提交前进行 lint 检查
