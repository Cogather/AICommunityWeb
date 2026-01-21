# AI社区平台后端服务

## 项目简介

AI社区平台后端服务，基于SpringBoot 2.7.18开发，提供首页数据接口服务。

## 技术栈

- Java 8
- SpringBoot 2.7.18
- MyBatis
- MySQL 5.7
- Swagger 2.9.2
- Lombok
- PageHelper

## 项目结构

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── aicommunity/
│   │   │           ├── AiCommunityApplication.java    # 启动类
│   │   │           ├── common/                          # 通用组件
│   │   │           │   ├── Result.java                  # 统一返回结果
│   │   │           │   ├── PageQuery.java                # 分页查询参数
│   │   │           │   ├── PageResult.java               # 分页查询结果
│   │   │           │   ├── ErrorCodeEnum.java            # 错误码枚举
│   │   │           │   └── exception/                    # 异常处理
│   │   │           ├── config/                           # 配置类
│   │   │           │   ├── SwaggerConfig.java            # Swagger配置
│   │   │           │   └── WebConfig.java                # Web配置
│   │   │           ├── controller/                       # 控制器层
│   │   │           │   ├── HomeController.java           # 首页控制器
│   │   │           │   └── ToolController.java           # 工具控制器
│   │   │           ├── entity/                          # 实体类
│   │   │           ├── mapper/                          # Mapper接口
│   │   │           ├── service/                          # 服务层
│   │   │           └── vo/                              # 视图对象
│   │   └── resources/
│   │       ├── mapper/                                  # MyBatis XML文件
│   │       ├── application.yml                          # 主配置文件
│   │       └── application-dev.yml                      # 开发环境配置
│   └── test/                                            # 测试代码
└── pom.xml                                              # Maven配置
```

## 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+

## 快速开始

### 1. 数据库配置

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_community?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: root
```

### 2. 编译项目

```bash
cd backend
mvn clean install
```

### 3. 运行项目

```bash
mvn spring-boot:run
```

或者使用IDE直接运行 `AiCommunityApplication.java`

### 4. 访问Swagger文档

启动成功后，访问：http://localhost:8080/swagger-ui.html

## API接口说明

### 首页接口

| 接口路径 | 请求方式 | 说明 |
|---------|---------|------|
| `/api/home/carousel` | GET | 获取首页轮播图 |
| `/api/home/honor` | GET | 获取荣誉殿堂数据 |
| `/api/home/empowerment` | GET | 获取赋能交流列表 |
| `/api/home/practices` | GET | 获取AI优秀实践列表 |
| `/api/home/tool-platform` | GET | 获取悬浮工具平台列表 |
| `/api/home/tool-banners` | GET | 获取工具专区轮播图 |

### 工具接口

| 接口路径 | 请求方式 | 说明 |
|---------|---------|------|
| `/api/tools` | GET | 获取工具列表 |

## 数据库表说明

项目使用以下数据库表（表结构不可变）：

- `t_new_home_banner_config` - 首页Banner配置表
- `t_honor_banners` - 荣誉Banner表
- `t_honors` - 荣誉表
- `t_honors_detail` - 荣誉详情表
- `t_new_posts` - 帖子表
- `t_new_home_tool` - 首页工具表
- `t_new_tool` - 工具表
- `t_new_tool_banners` - 工具Banner表

## 注意事项

1. 数据库表结构不可变，请确保数据库中存在对应的表
2. 用户表（t_user）需要根据实际情况调整查询逻辑
3. 帖子分类（label_id）与业务分类的映射关系需要根据实际业务调整
4. 相对时间计算逻辑在 `HomeServiceImpl.calculateRelativeTime()` 方法中

## 开发规范

- 严格遵循《阿里巴巴Java开发手册（最新版）》规范
- 代码缩进为4个空格
- 类、方法必须有完整的JavaDoc注释
- 统一使用Lombok简化代码
- 统一异常处理，使用全局异常处理器
