# AI优秀实践页面接口文档

## 概述

AI优秀实践页面需要加载以下数据模块：
1. 帖子列表（包含精华帖和普通帖子）
2. 最热帖子列表（右侧边栏Top 3）
3. 标签列表
4. 部门排名列表
5. 热门贡献者列表

---

## 1. 获取帖子列表

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/practices/posts` |
| 接口描述 | 获取AI优秀实践帖子列表，支持分页、搜索、筛选和排序 |
| 权限要求 | 无需登录 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | number | 否 | 当前页码，默认1 |
| pageSize | number | 否 | 每页数量，默认15，可选10/15/20/30/50 |
| keyword | string | 否 | 搜索关键词（匹配标题、作者、描述） |
| tag | string | 否 | 按标签筛选 |
| department | string | 否 | 按部门筛选 |
| contributor | string | 否 | 按贡献者筛选 |
| sortBy | string | 否 | 排序方式：newest（最新）/hot（最热）/comments（评论数）/likes（点赞数），默认newest |

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "title": "AI大会2024",
        "description": "参加活动探索AI的最新趋势和创新。",
        "author": "David Chen",
        "authorId": 101,
        "authorAvatar": "https://example.com/avatar/david.jpg",
        "createTime": "2024-04-10T10:30:00Z",
        "createTimeDisplay": "2024年4月10日",
        "views": 1250,
        "comments": 45,
        "likes": 128,
        "tags": ["活动", "AI大会"],
        "image": "https://example.com/images/post1.jpg",
        "featured": true,
        "department": "产品部"
      },
      {
        "id": 2,
        "title": "构建AI应用指南",
        "description": "学习如何构建实用的AI应用程序，从概念到部署的完整流程。",
        "author": "Brinit",
        "authorId": 102,
        "authorAvatar": "https://example.com/avatar/brinit.jpg",
        "createTime": "2024-04-08T14:20:00Z",
        "createTimeDisplay": "2024年4月",
        "views": 890,
        "comments": 32,
        "likes": 56,
        "tags": ["项目", "AI应用"],
        "image": "https://example.com/images/post2.jpg",
        "featured": false,
        "department": "研发部"
      }
    ],
    "featuredPosts": [
      {
        "id": 1,
        "title": "AI大会2024",
        "description": "参加活动探索AI的最新趋势和创新。",
        "author": "David Chen",
        "authorId": 101,
        "createTime": "2024-04-10T10:30:00Z",
        "createTimeDisplay": "2024年4月10日",
        "views": 1250,
        "comments": 45,
        "likes": 128,
        "tags": ["活动", "AI大会"],
        "image": "https://example.com/images/post1.jpg",
        "featured": true,
        "department": "产品部"
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 15,
    "totalPages": 7
  }
}
```

### 字段说明

**帖子对象字段：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 帖子ID |
| title | string | 是 | 帖子标题 |
| description | string | 否 | 帖子描述/摘要 |
| author | string | 是 | 作者名称 |
| authorId | number | 否 | 作者ID |
| authorAvatar | string | 否 | 作者头像URL |
| createTime | string | 是 | 创建时间（ISO 8601格式） |
| createTimeDisplay | string | 是 | 显示用的时间格式（如"2024年4月10日"或"60分钟前"） |
| views | number | 是 | 浏览量 |
| comments | number | 是 | 评论数 |
| likes | number | 是 | 点赞数 |
| tags | array | 否 | 标签数组 |
| image | string | 否 | 封面图片URL |
| featured | boolean | 是 | 是否为精华帖 |
| department | string | 否 | 所属部门 |

**分页信息：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| total | number | 是 | 总记录数 |
| page | number | 是 | 当前页码 |
| pageSize | number | 是 | 每页数量 |
| totalPages | number | 是 | 总页数 |

---

## 2. 获取最热帖子列表

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/practices/hot-posts` |
| 接口描述 | 获取最热帖子Top N（按浏览量排序，用于右侧边栏展示） |
| 权限要求 | 无需登录 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| limit | number | 否 | 返回数量，默认3 |

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "title": "AI大会2024",
        "views": 1250,
        "rank": 1
      },
      {
        "id": 2,
        "title": "构建AI应用指南",
        "views": 890,
        "rank": 2
      },
      {
        "id": 5,
        "title": "训练机器学习模型的最佳实践",
        "views": 720,
        "rank": 3
      }
    ]
  }
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 帖子ID |
| title | string | 是 | 帖子标题 |
| views | number | 是 | 浏览量 |
| rank | number | 是 | 排名（1-N） |

---

## 3. 获取标签列表

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/practices/tags` |
| 接口描述 | 获取所有可用标签及其帖子统计数量 |
| 权限要求 | 无需登录 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| department | string | 否 | 按部门过滤标签统计 |

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      { "name": "全部", "count": 50 },
      { "name": "自然语言处理", "count": 12 },
      { "name": "计算机视觉", "count": 8 },
      { "name": "深度学习", "count": 15 },
      { "name": "AI伦理", "count": 3 },
      { "name": "机器学习", "count": 20 },
      { "name": "机器人", "count": 5 },
      { "name": "数据科学", "count": 10 },
      { "name": "生成式AI", "count": 7 },
      { "name": "PyTorch", "count": 6 },
      { "name": "TensorFlow", "count": 4 },
      { "name": "项目", "count": 8 },
      { "name": "AI应用", "count": 12 },
      { "name": "效率", "count": 5 },
      { "name": "自动化", "count": 6 },
      { "name": "实践", "count": 9 },
      { "name": "已解决", "count": 11 },
      { "name": "部署", "count": 4 },
      { "name": "活动", "count": 2 },
      { "name": "AI大会", "count": 1 }
    ]
  }
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 标签名称 |
| count | number | 是 | 该标签下的帖子数量 |

---

## 4. 获取部门排名列表

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/practices/departments` |
| 接口描述 | 获取部门排名列表，包含发帖数和贡献者统计 |
| 权限要求 | 无需登录 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tag | string | 否 | 按标签过滤部门统计 |

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "name": "研发部",
        "postCount": 25,
        "contributorCount": 12
      },
      {
        "id": 2,
        "name": "产品部",
        "postCount": 18,
        "contributorCount": 8
      },
      {
        "id": 3,
        "name": "技术部",
        "postCount": 15,
        "contributorCount": 6
      },
      {
        "id": 4,
        "name": "数据部",
        "postCount": 12,
        "contributorCount": 5
      },
      {
        "id": 5,
        "name": "算法部",
        "postCount": 10,
        "contributorCount": 4
      },
      {
        "id": 6,
        "name": "测试部",
        "postCount": 8,
        "contributorCount": 3
      }
    ]
  }
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 部门ID |
| name | string | 是 | 部门名称 |
| postCount | number | 是 | 该部门的发帖数 |
| contributorCount | number | 是 | 该部门的贡献者数量 |

---

## 5. 获取热门贡献者列表

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/practices/contributors` |
| 接口描述 | 获取热门贡献者列表（按发帖数/贡献度排序） |
| 权限要求 | 无需登录 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| limit | number | 否 | 返回数量，默认5 |

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "name": "张工程师",
        "avatar": "https://example.com/avatar/zhang.png",
        "postCount": 15,
        "department": "研发部"
      },
      {
        "id": 2,
        "name": "李开发者",
        "avatar": "https://example.com/avatar/li.png",
        "postCount": 12,
        "department": "技术部"
      },
      {
        "id": 3,
        "name": "王测试",
        "avatar": "https://example.com/avatar/wang.png",
        "postCount": 10,
        "department": "测试部"
      },
      {
        "id": 4,
        "name": "赵医生",
        "avatar": "https://example.com/avatar/zhao.png",
        "postCount": 8,
        "department": "产品部"
      },
      {
        "id": 5,
        "name": "陈架构师",
        "avatar": "https://example.com/avatar/chen.png",
        "postCount": 6,
        "department": "算法部"
      }
    ]
  }
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 用户ID |
| name | string | 是 | 用户名称 |
| avatar | string | 否 | 用户头像URL |
| postCount | number | 否 | 发帖数量 |
| department | string | 否 | 所属部门 |

---

## 接口汇总

| 序号 | 接口路径 | 方法 | 描述 |
|------|----------|------|------|
| 1 | `/api/practices/posts` | GET | 获取帖子列表（支持分页、搜索、筛选、排序） |
| 2 | `/api/practices/hot-posts` | GET | 获取最热帖子Top N |
| 3 | `/api/practices/tags` | GET | 获取标签列表及统计 |
| 4 | `/api/practices/departments` | GET | 获取部门排名列表 |
| 5 | `/api/practices/contributors` | GET | 获取热门贡献者列表 |

---

## 通用响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 错误响应

```json
{
  "code": 500,
  "message": "服务器内部错误",
  "data": null
}
```

### 常见错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（需要登录） |
| 403 | 禁止访问（无权限） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
