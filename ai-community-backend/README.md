# AI社区后端项目

## 项目说明

本项目是基于Java 8 + SpringBoot 2.x + MySQL 5.7 + MyBatis + Swagger 2技术栈开发的AI社区后端服务。

## 技术栈

- **Java**: 1.8
- **SpringBoot**: 2.7.18
- **MySQL**: 5.7
- **MyBatis**: 2.3.1
- **Swagger**: 2.9.2
- **PageHelper**: 1.4.6
- **Lombok**: 最新版本

## 项目结构

```
ai-community-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/aicommunity/
│   │   │       ├── Application.java          # 启动类
│   │   │       ├── common/                   # 通用组件
│   │   │       │   ├── Result.java           # 统一返回结果
│   │   │       │   ├── PageQuery.java        # 分页查询参数
│   │   │       │   ├── PageResult.java       # 分页查询结果
│   │   │       │   ├── ErrorCodeEnum.java    # 错误码枚举
│   │   │       │   ├── BusinessException.java # 业务异常
│   │   │       │   └── GlobalExceptionHandler.java # 全局异常处理器
│   │   │       ├── config/                   # 配置类
│   │   │       │   └── SwaggerConfig.java    # Swagger配置
│   │   │       ├── controller/               # 控制器层
│   │   │       │   ├── AuthController.java   # 认证控制器
│   │   │       │   ├── PostController.java   # 帖子控制器
│   │   │       │   ├── ActivityController.java # 活动控制器
│   │   │       │   ├── HonorController.java  # 荣誉控制器
│   │   │       │   ├── UserController.java   # 用户控制器
│   │   │       │   ├── HomeController.java   # 首页控制器
│   │   │       │   ├── ToolController.java   # 工具控制器
│   │   │       │   ├── MessageController.java # 消息控制器
│   │   │       │   └── AdminController.java  # 管理后台控制器
│   │   │       ├── dto/                      # 数据传输对象
│   │   │       │   ├── LoginRequest.java
│   │   │       │   ├── LoginResponse.java
│   │   │       │   ├── PostCreateRequest.java
│   │   │       │   └── ...
│   │   │       ├── entity/                    # 实体类
│   │   │       │   ├── User.java
│   │   │       │   ├── Post.java
│   │   │       │   ├── Comment.java
│   │   │       │   ├── Activity.java
│   │   │       │   └── ...
│   │   │       ├── mapper/                    # Mapper接口
│   │   │       │   ├── UserMapper.java
│   │   │       │   ├── PostMapper.java
│   │   │       │   └── ...
│   │   │       ├── service/                    # 服务接口
│   │   │       │   ├── AuthService.java
│   │   │       │   ├── PostService.java
│   │   │       │   └── ...
│   │   │       └── service/impl/              # 服务实现类
│   │   │           ├── AuthServiceImpl.java
│   │   │           ├── PostServiceImpl.java
│   │   │           └── ...
│   │   └── resources/
│   │       ├── application.yml                # 主配置文件
│   │       ├── application-dev.yml            # 开发环境配置
│   │       ├── mapper/                         # MyBatis XML文件
│   │       │   ├── UserMapper.xml
│   │       │   ├── PostMapper.xml
│   │       │   └── ...
│   │       └── db/
│   │           └── schema.sql                 # 数据库建表语句
│   └── test/                                   # 测试代码
├── pom.xml                                     # Maven配置文件
└── README.md                                   # 项目说明文档
```

## 数据库初始化

1. 创建数据库：
```sql
CREATE DATABASE ai_community DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 执行建表语句：
```bash
mysql -u root -p ai_community < src/main/resources/db/schema.sql
```

## 配置文件说明

### application.yml
主配置文件，包含：
- 数据库连接配置
- MyBatis配置
- PageHelper配置
- Swagger配置
- JWT配置

### application-dev.yml
开发环境配置文件，包含：
- 开发环境数据库连接
- 日志级别配置

## 运行说明

1. **环境要求**：
   - JDK 1.8+
   - Maven 3.6+
   - MySQL 5.7+

2. **启动步骤**：
   ```bash
   # 1. 编译项目
   mvn clean compile
   
   # 2. 运行项目
   mvn spring-boot:run
   
   # 或者打包后运行
   mvn clean package
   java -jar target/ai-community-backend-1.0.0.jar
   ```

3. **访问Swagger文档**：
   ```
   http://localhost:8080/api/swagger-ui.html
   ```

## API接口说明

### 用户认证
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出

### 用户相关
- `GET /api/user/current` - 获取当前用户信息
- `GET /api/user/points/calculate` - 获取用户个人积分详情
- `GET /api/user/{userId}/profile` - 获取用户个人资料
- `PUT /api/user/profile` - 更新用户资料
- `GET /api/user/{userId}/posts` - 获取用户发布的帖子
- `GET /api/user/{userId}/favorites` - 获取用户收藏的帖子
- `GET /api/user/{userId}/comments` - 获取用户评论列表
- `GET /api/user/{userId}/activities` - 获取用户参与的活动
- `GET /api/user/{userId}/activities/created` - 获取用户发布的活动

### 帖子相关
- `GET /api/posts` - 获取帖子列表（支持搜索、排序、筛选）
- `GET /api/posts/{id}` - 获取帖子详情
- `POST /api/posts` - 创建帖子
- `PUT /api/posts/{id}` - 更新帖子
- `DELETE /api/posts/{id}` - 删除帖子
- `POST /api/posts/{id}/like` - 点赞/取消点赞帖子
- `POST /api/posts/{id}/collect` - 收藏/取消收藏帖子
- `GET /api/posts/recommended-covers` - 获取推荐封面列表
- `POST /api/posts/draft` - 保存草稿
- `GET /api/posts/hot` - 获取最热帖子
- `GET /api/posts/{id}/comments` - 获取帖子评论列表
- `POST /api/posts/{id}/comments` - 发表评论

### 评论相关
- `POST /api/comments/{id}/like` - 点赞评论
- `PUT /api/comments/{id}` - 更新评论
- `DELETE /api/comments/{id}` - 删除评论
- `DELETE /api/replies/{id}` - 删除回复

### 活动相关
- `GET /api/activities` - 获取活动列表
- `GET /api/activities/{id}` - 获取活动详情
- `POST /api/activities` - 创建活动
- `PUT /api/activities/{id}` - 更新活动
- `DELETE /api/activities/{id}` - 删除活动
- `POST /api/activities/{id}/register` - 报名活动
- `DELETE /api/activities/{id}/register` - 取消报名
- `GET /api/activities/{id}/registrations` - 获取报名用户列表

### 荣誉相关
- `GET /api/honors` - 获取荣誉列表（支持筛选、查询、分页）
- `GET /api/honors/influence` - 获取荣誉影响力
- `GET /api/honors/top-users` - 获取AI使用达人Top用户
- `POST /api/honors/{id}/flower` - 给荣誉送花

### 工具相关
- `GET /api/tools` - 获取工具列表
- `GET /api/tools/{id}` - 获取工具详情
- `GET /api/tools/{id}/owner` - 检查用户是否为工具Owner
- `GET /api/tags` - 获取标签列表
- `GET /api/departments/stats` - 获取部门统计
- `GET /api/users/top-contributors` - 获取热门贡献者

### 首页相关
- `GET /api/home/carousel` - 获取首页轮播图
- `GET /api/home/honor` - 获取荣誉殿堂和社区头条信息

### 消息相关
- `GET /api/messages` - 获取消息列表
- `PUT /api/messages/{id}/read` - 标记消息为已读
- `PUT /api/messages/read-all` - 全部标记为已读
- `DELETE /api/messages/{id}` - 删除消息
- `GET /api/messages/unread-count` - 获取未读消息数量

### 管理后台
- `GET /api/admin/home/carousel` - 获取首页轮播图配置
- `PUT /api/admin/home/carousel` - 保存首页轮播图配置
- `GET /api/admin/home/honor-banner` - 获取荣誉殿堂Banner配置
- `PUT /api/admin/home/honor-banner` - 保存荣誉殿堂Banner配置
- `GET /api/admin/home/honor-awards` - 获取荣誉殿堂奖项列表配置
- `PUT /api/admin/home/honor-awards` - 保存荣誉殿堂奖项列表配置
- `GET /api/admin/home/news` - 获取社区头条配置
- `PUT /api/admin/home/news` - 保存社区头条配置
- `GET /api/admin/tools` - 获取AI工具配置
- `PUT /api/admin/tools` - 保存AI工具配置
- `GET /api/admin/agent/featured-post` - 获取扶摇Agent应用置顶帖子配置
- `PUT /api/admin/agent/featured-post` - 保存扶摇Agent应用置顶帖子配置
- `GET /api/admin/posts/recommended-covers` - 获取推荐封面配置
- `PUT /api/admin/posts/recommended-covers` - 保存推荐封面配置
- `GET /api/admin/users/search` - 搜索用户
- `GET /api/admin/users` - 获取用户列表
- `POST /api/admin/users/{userId}/role` - 添加用户角色
- `DELETE /api/admin/users/{userId}/role` - 移除用户角色
- `GET /api/admin/honors/recommended-winners` - 获取本月积分排行榜
- `POST /api/admin/honors` - 设置用户获奖
- `DELETE /api/admin/honors/{id}` - 取消用户获奖
- `POST /api/admin/upload/image` - 上传图片

### 奖项相关
- `GET /api/awards` - 获取奖项列表
- `GET /api/awards/{id}/rules` - 获取奖项规则详情

## 代码规范

### 命名规范
- **包命名**：全小写，采用反向域名命名规则（如com.aicommunity.project）
- **类命名**：采用大驼峰（UpperCamelCase），如UserController、OrderService
- **方法命名**：采用小驼峰（lowerCamelCase），如getUserInfo、saveOrder
- **变量命名**：小驼峰，禁止使用拼音+英文混合
- **常量命名**：全大写，下划线分隔，如MAX_PAGE_SIZE

### 注释要求
- 类、方法必须有完整的JavaDoc注释
- 关键业务逻辑必须有行内注释

### 异常处理
- 统一使用BusinessException抛出业务异常
- 全局异常处理器统一处理异常并返回标准化错误码

## 开发注意事项

1. **分页查询**：统一使用PageQuery和PageResult，配合PageHelper实现分页
2. **参数校验**：使用@Valid注解和JSR-303校验注解
3. **Swagger文档**：所有Controller接口需添加@Api、@ApiOperation等注解
4. **事务管理**：Service层方法需要事务的添加@Transactional注解
5. **SQL注入防护**：MyBatis使用#{}防止SQL注入，禁止使用${}
6. **密码加密**：用户密码使用BCrypt等安全加密方式（示例中使用MD5仅为演示）

## 后续开发建议

1. **JWT认证**：完善JWT token生成和验证机制
2. **权限控制**：实现基于角色的权限控制（RBAC）
3. **文件上传**：实现文件上传功能，支持图片、视频等
4. **缓存机制**：集成Redis实现缓存
5. **消息推送**：实现WebSocket实时消息推送
6. **日志系统**：集成日志系统，记录操作日志
7. **单元测试**：编写单元测试和集成测试
8. **API限流**：实现API限流机制，防止接口被恶意调用

## 联系方式

如有问题，请联系开发团队：dev@aicommunity.com
