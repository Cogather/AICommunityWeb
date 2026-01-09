# AI社区后端项目

## 项目简介

AI社区后端服务，基于SpringBoot 2.7.x + MyBatis + MySQL 5.7 + Swagger 2技术栈开发。

## 技术栈

- **框架**: SpringBoot 2.7.18
- **数据库**: MySQL 5.7
- **ORM**: MyBatis
- **分页**: PageHelper
- **API文档**: Swagger 2 (SpringFox 3.0.0)
- **认证**: JWT
- **Java版本**: 1.8

## 项目结构

```
ai-community-backend/
├── src/main/java/com/aicommunity/
│   ├── common/                    # 通用组件
│   │   ├── Result.java           # 统一返回结果
│   │   ├── PageQuery.java        # 分页查询参数
│   │   ├── PageResult.java       # 分页结果
│   │   ├── exception/            # 异常处理
│   │   └── enums/                # 枚举类
│   ├── entity/                    # 实体类
│   ├── dto/                       # 数据传输对象
│   ├── mapper/                    # MyBatis Mapper接口
│   ├── service/                   # Service接口
│   │   └── impl/                  # Service实现类
│   ├── controller/                # Controller控制器
│   ├── config/                    # 配置类
│   └── util/                      # 工具类
├── src/main/resources/
│   ├── mapper/                    # MyBatis XML映射文件
│   ├── application.yml            # 主配置文件
│   └── application-dev.yml        # 开发环境配置
└── database/
    └── schema.sql                 # 数据库建表语句
```

## 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- IDE: IntelliJ IDEA / Eclipse

## 快速开始

### 1. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE ai_community DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 执行建表语句
source database/schema.sql;
```

### 2. 配置文件

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_community?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 3. 编译运行

```bash
# 编译
mvn clean compile

# 运行
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/ai-community-backend-1.0.0.jar
```

### 4. 访问Swagger文档

启动后访问：http://localhost:8080/api/swagger-ui.html

## 接口文档

详细的接口实现指南请参考：[接口实现指南.md](接口实现指南.md)

接口映射清单请参考：[接口映射清单.md](接口映射清单.md)

## 开发规范

### 1. 代码规范

- 严格遵循《阿里巴巴Java开发手册（最新版）》
- 包命名：全小写，采用反向域名命名规则
- 类命名：大驼峰（UpperCamelCase）
- 方法命名：小驼峰（lowerCamelCase）
- 变量命名：小驼峰，禁止使用拼音+英文混合
- 常量命名：全大写，下划线分隔

### 2. 注释要求

- 类、方法必须有完整的JavaDoc注释
- 关键业务逻辑必须有行内注释

### 3. 异常处理

- 统一使用`BusinessException`抛出业务异常
- 全局异常处理器统一处理异常并返回标准化错误码

### 4. 返回格式

所有接口统一使用`Result<T>`返回：

```java
{
    "code": 200,
    "message": "success",
    "data": {},
    "timestamp": 1234567890
}
```

## 核心功能模块

### 1. 用户认证
- 用户登录/登出
- JWT Token生成和验证
- 用户权限管理

### 2. 帖子管理
- 帖子CRUD
- 帖子点赞/收藏
- 评论管理

### 3. 活动管理
- 活动CRUD
- 活动报名
- 活动统计

### 4. 荣誉系统
- 荣誉记录管理
- 荣誉排行榜
- 获奖者推荐

### 5. 管理后台
- 首页配置管理
- 用户角色管理
- 数据统计

## 数据库设计

数据库设计文档请参考：`database/schema.sql`

主要数据表：
- user: 用户表
- post: 帖子表
- comment: 评论表
- activity: 活动表
- honor: 荣誉表
- award: 奖项表
- tool: 工具表
- message: 消息表
- points_record: 积分记录表

## 积分系统

积分规则：
- 发布帖子: +15 积分
- 参加活动: +10 积分
- 帖子被收藏: +5 积分
- 帖子被点赞: +3 积分
- 发表评论: +1 积分
- 评论被点赞: +1 积分

注意：管理员的所有操作不计算积分。

## 权限说明

### 角色类型
- **user**: 普通用户（默认角色）
- **admin**: 管理员（可以访问管理后台）
- **owner**: 工具Owner（可以发布指定工具的活动）

### 权限控制
- 使用JWT Token进行身份验证
- 通过拦截器验证用户权限
- 管理后台接口需要管理员权限

## 常见问题

### 1. 数据库连接失败
- 检查数据库服务是否启动
- 检查数据库连接配置是否正确
- 检查数据库用户权限

### 2. 端口被占用
- 修改`application.yml`中的`server.port`配置

### 3. Swagger文档无法访问
- 检查Swagger配置是否正确
- 确认`@EnableSwagger2`注解已添加

## 后续开发计划

1. 完善所有接口实现（参考接口实现指南）
2. 添加单元测试
3. 添加Redis缓存支持
4. 添加WebSocket实时推送
5. 添加文件上传功能
6. 性能优化和监控

## 联系方式

如有问题，请联系开发团队。
