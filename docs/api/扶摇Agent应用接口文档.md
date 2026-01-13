# 扶摇Agent应用页面接口文档

## 概述

扶摇Agent应用页面用于展示扶摇Agent相关的使用指导、实践案例和活动培训信息。页面主要功能包括：
- 置顶帖子展示
- 帖子列表（支持搜索、排序、标签筛选）
- 标签筛选
- 近期活动与培训轮播
- 工具Owner/管理员发布活动功能

> **说明**：扶摇Agent应用使用 `toolId = -1` 作为标识，与AI工具专区共用统一的工具Owner权限体系。

---

## 响应格式约定

### 标准响应结构

所有接口返回统一的响应结构：

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

> **前端处理说明**：建议在 axios 拦截器中统一解包 `response.data.data`，使业务代码可以直接使用数据对象。
> 
> 本文档中的响应示例均展示 `data` 字段内的内容，实际 HTTP 响应会包含外层的 `code` 和 `message`。

---

## 接口概览

| 接口 | 说明 | 是否与AI工具专区共用 |
|-----|------|-------------------|
| 获取当前用户信息 | 获取用户角色信息 | ✅ 共用 |
| 检查工具Owner权限 | 检查是否为工具Owner | ✅ 共用（toolId=-1） |
| 获取置顶/精选帖子 | 获取Agent应用置顶帖子 | ❌ 专有 |
| 获取帖子列表 | 获取帖子列表 | ✅ 共用（toolId=-1） |
| 获取标签列表 | 获取标签统计 | ✅ 共用（toolId=-1） |
| 获取活动列表 | 获取活动列表 | ✅ 共用（toolId=-1） |
| 发布活动 | 工具Owner/管理员发布活动 | ✅ 共用 |

> **toolId说明**：`-1`=扶摇Agent，`0`=其他工具，`>0`=具体工具ID

---

## 接口详情

### 1. 获取当前用户信息（共用）

获取当前登录用户的基本信息和角色，用于判断是否为管理员。

**请求**

```
GET /api/user/current
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "employeeId": "E001",
    "name": "张三",
    "avatar": "https://example.com/avatars/user1.jpg",
    "department": "技术部",
    "roles": ["user", "admin"],
    "ownedTools": [
      { "toolId": -1, "toolName": "扶摇Agent" },
      { "toolId": 1, "toolName": "TestMate" }
    ]
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | number | 用户ID |
| employeeId | string | 员工工号 |
| name | string | 用户名称 |
| avatar | string | 头像URL |
| department | string | 所属部门 |
| roles | array | 用户角色列表，包含admin表示管理员 |
| ownedTools | array | 用户拥有的工具Owner权限列表 |

---

### 2. 检查工具Owner权限（共用）

检查当前用户是否为指定工具的Owner。此接口适用于所有工具，包括扶摇Agent应用（toolId=-1）。

**请求**

```
GET /api/tools/{toolId}/check-owner
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID（-1=扶摇Agent，0=其他工具，>0=具体工具） |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "isOwner": true,
    "toolId": -1,
    "permissions": ["publish_activity", "manage_posts", "set_featured"]
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| isOwner | boolean | 是否为工具Owner |
| toolId | number | 工具ID |
| permissions | array | 用户拥有的权限列表 |

**权限说明**

| 权限 | 说明 |
|-----|------|
| publish_activity | 发布活动/培训 |
| manage_posts | 管理帖子（删除等） |
| set_featured | 设置置顶帖子 |

> **说明**：工具Owner权限适用于AI工具专区的所有工具和扶摇Agent应用，不需要区分角色。

---

### 3. 获取置顶/精选帖子（Agent专有）

获取扶摇Agent应用页面的置顶/精选帖子。

> **精华帖子特性**：
> - 精华帖子在**帖子列表外**单独展示（大图卡片样式）
> - 精华帖子不参与分页、搜索、标签筛选和排序
> - 前端需单独请求精华帖子，与普通帖子列表分开处理
>
> **与AI工具专区（其他工具）的区别**：
> - 扶摇Agent：精华帖子在帖子列表外单独展示（大图卡片样式）
> - AI工具专区（其他工具）：精华帖子在帖子列表内置顶显示

**请求**

```
GET /api/agent/featured-post
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "post": {
      "id": 1,
      "title": "扶摇Agent智能编排最佳实践",
      "description": "本文详细介绍如何利用扶摇Agent进行智能工作流编排...",
      "image": "https://example.com/covers/featured1.jpg",
      "cover": "https://example.com/covers/featured1.jpg",
      "author": "张三",
      "authorId": 101,
      "authorAvatar": "https://example.com/avatars/user101.jpg",
      "tags": ["Agent应用", "智能编排", "最佳实践"],
      "views": 2580,
      "comments": 89,
      "likes": 156,
      "featured": true,
      "createTime": "2026-01-10T10:30:00Z"
    }
  }
}
```

**响应字段说明**

| 字段 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| post | object\|null | ✅ | 置顶帖子对象，无置顶时为null |
| post.id | number | ✅ | 帖子ID |
| post.title | string | ✅ | 帖子标题 |
| post.description | string | ✅ | 帖子摘要 |
| post.image | string | ❌ | 帖子封面图（优先使用） |
| post.cover | string | ❌ | 帖子封面图（备用字段，与image二选一） |
| post.author | string | ✅ | 作者名称 |
| post.authorId | number | ❌ | 作者ID |
| post.authorAvatar | string | ❌ | 作者头像 |
| post.tags | array | ❌ | 标签列表（数组） |
| post.views | number | ✅ | 浏览量 |
| post.comments | number | ✅ | 评论数 |
| post.likes | number | ❌ | 点赞数 |
| post.featured | boolean | ✅ | 是否为精华帖子（始终为true） |
| post.createTime | string | ✅ | 创建时间（ISO 8601格式或日期字符串） |

> **前端兼容说明**：
> - 封面图：前端使用 `post.image || post.cover` 兼容两个字段
> - 日期：前端会判断类型并格式化 `typeof createTime === 'string' ? createTime : new Date(createTime).toLocaleDateString('zh-CN')`
> - 精华帖子通过单独接口获取，不随筛选条件重新请求

---

### 4. 获取帖子列表（共用）

获取帖子列表，支持按工具、分类、标签、部门筛选，以及搜索和排序。

**请求**

```
GET /api/tools/posts
```

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID（-1=扶摇Agent，0=其他工具，>0=具体工具） |
| category | string | 否 | 帖子分类：guide-操作指导，excellent-优秀使用 |
| tag | string | 否 | 标签筛选 |
| department | string | 否 | 部门筛选 |
| keyword | string | 否 | 搜索关键词 |
| sortBy | string | 否 | 排序方式：newest-最新，hot-最热，comments-评论最多，likes-点赞最多 |
| page | number | 否 | 页码，默认1 |
| pageSize | number | 否 | 每页条数，默认15 |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "title": "使用扶摇Agent实现智能代码生成",
        "description": "分享如何使用扶摇Agent编排引擎实现智能代码生成功能...",
        "cover": "https://example.com/covers/post1.jpg",
        "author": "张工程师",
        "authorId": 101,
        "authorAvatar": "https://example.com/avatars/user101.jpg",
        "department": "研发部",
        "toolId": -1,
        "toolName": "扶摇Agent",
        "category": "excellent",
        "tags": ["Agent应用", "代码生成"],
        "views": 890,
        "comments": 45,
        "likes": 98,
        "createTime": "2026-01-12T14:30:00Z",
        "updateTime": "2026-01-12T14:30:00Z"
      }
    ],
    "total": 28,
    "page": 1,
    "pageSize": 15
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| list | array | 帖子列表 |
| total | number | 总数 |
| page | number | 当前页码 |
| pageSize | number | 每页条数 |

**帖子对象字段说明**

| 字段 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| id | number | ✅ | 帖子ID |
| title | string | ✅ | 帖子标题 |
| description | string | ✅ | 帖子摘要 |
| cover | string | ❌ | 封面图URL |
| author | string | ✅ | 作者名称 |
| authorId | number | ✅ | 作者用户ID |
| authorAvatar | string | ❌ | 作者头像URL |
| department | string | ❌ | 作者所属部门 |
| toolId | number | ✅ | 关联工具ID（-1=扶摇Agent） |
| toolName | string | ❌ | 关联工具名称 |
| category | string | ❌ | 帖子分类：guide/excellent |
| tags | array | ❌ | 帖子标签数组 |
| views | number | ✅ | 浏览量 |
| comments | number | ✅ | 评论数 |
| likes | number | ✅ | 点赞数 |
| createTime | string | ✅ | 创建时间（ISO 8601格式） |
| updateTime | string | ❌ | 更新时间（ISO 8601格式） |

> **与AI工具专区共用**：此接口与AI工具专区完全共用，通过 `toolId` 区分数据范围。

---

### 5. 获取标签列表（共用）

获取指定工具下帖子的标签统计信息。

**请求**

```
GET /api/tools/{toolId}/tags
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID（-1=扶摇Agent，0=其他工具，>0=具体工具） |

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| department | string | 否 | 部门筛选，用于联动统计 |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      { "name": "全部", "count": 28 },
      { "name": "Agent应用", "count": 12 },
      { "name": "工作流", "count": 8 },
      { "name": "自动化", "count": 6 },
      { "name": "智能编排", "count": 5 },
      { "name": "最佳实践", "count": 4 },
      { "name": "案例分享", "count": 3 },
      { "name": "开发指南", "count": 2 }
    ]
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| name | string | 标签名称，"全部"为特殊标签表示总数 |
| count | number | 该标签下的帖子数量 |

---

### 6. 获取活动列表（共用）

获取指定工具的近期活动与培训列表。

**请求**

```
GET /api/tools/activities
```

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID（-1=扶摇Agent，0=其他工具，>0=具体工具） |
| status | string | 否 | 活动状态：upcoming-即将开始，ongoing-进行中，ended-已结束 |
| page | number | 否 | 页码，默认1 |
| pageSize | number | 否 | 每页条数，默认10 |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "toolId": -1,
        "toolName": "扶摇Agent",
        "type": "training",
        "title": "扶摇Agent高级编排培训",
        "content": "<p>本次培训将深入讲解扶摇Agent的高级编排功能...</p>",
        "cover": "https://example.com/activities/training1.jpg",
        "date": "2026-01-20",
        "startTime": "14:00",
        "endTime": "16:00",
        "location": "线上腾讯会议",
        "meetingLink": "https://meeting.tencent.com/xxx",
        "speaker": "李四",
        "speakerTitle": "高级架构师",
        "maxParticipants": 100,
        "currentParticipants": 45,
        "status": "upcoming",
        "createTime": "2026-01-08T09:00:00Z"
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 10
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | number | 活动ID |
| toolId | number | 关联工具ID（-1=扶摇Agent） |
| toolName | string | 关联工具名称 |
| type | string | 活动类型：activity/training/workshop |
| title | string | 活动标题 |
| content | string | 活动详情（HTML格式） |
| cover | string | 封面图URL |
| date | string | 活动日期（YYYY-MM-DD） |
| startTime | string | 开始时间（HH:mm） |
| endTime | string | 结束时间（HH:mm） |
| location | string | 活动地点 |
| meetingLink | string | 线上会议链接（可选） |
| speaker | string | 主讲人 |
| speakerTitle | string | 主讲人职称 |
| maxParticipants | number | 最大参与人数 |
| currentParticipants | number | 当前报名人数 |
| status | string | 活动状态 |
| createTime | string | 创建时间（ISO 8601格式） |

> **与AI工具专区共用**：此接口与AI工具专区完全共用，通过 `toolId` 区分数据范围。
> 
> **前端数据转换说明**：
> 
> `ActivityCarousel` 组件需要以下字段格式，前端需进行数据转换：
> 
> ```typescript
> // 将API返回的活动数据转换为组件所需格式
> activities.value = result.list.map((a) => ({
>   id: a.id,
>   type: a.type || 'activity',
>   title: a.title,
>   desc: a.content ? a.content.replace(/<[^>]*>/g, '').substring(0, 100) + '...' : '',
>   date: typeof a.date === 'string' ? a.date : new Date(a.date).toLocaleDateString('zh-CN'),
>   location: a.location || '',
>   image: a.cover || ''
> }))
> ```
> 
> | API字段 | 组件字段 | 转换说明 |
> |--------|---------|---------|
> | content | desc | 去除HTML标签，截取前100字符 |
> | cover | image | 直接映射 |
> | date | date | 格式化为本地日期字符串 |

---

### 7. 发布活动（共用）

Agent应用Owner或管理员发布新活动/培训。

**请求**

```
POST /api/tools/activities
```

**请求体**

```json
{
  "toolId": -1,
  "type": "training",
  "title": "扶摇Agent高级编排培训",
  "content": "<p>本次培训将深入讲解扶摇Agent的高级编排功能...</p>",
  "cover": "https://example.com/activities/training1.jpg",
  "date": "2026-01-20",
  "startTime": "14:00",
  "endTime": "16:00",
  "location": "线上腾讯会议",
  "meetingLink": "https://meeting.tencent.com/xxx",
  "speaker": "李四",
  "speakerTitle": "高级架构师",
  "maxParticipants": 100
}
```

**请求字段说明**

| 字段 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID，扶摇Agent应用使用 -1 |
| type | string | 是 | 活动类型：activity/training/workshop |
| title | string | 是 | 活动标题 |
| content | string | 是 | 活动详情（HTML格式） |
| cover | string | 否 | 封面图URL |
| date | string | 是 | 活动日期（YYYY-MM-DD） |
| startTime | string | 是 | 开始时间（HH:mm） |
| endTime | string | 是 | 结束时间（HH:mm） |
| location | string | 是 | 活动地点 |
| meetingLink | string | 否 | 线上会议链接 |
| speaker | string | 否 | 主讲人 |
| speakerTitle | string | 否 | 主讲人职称 |
| maxParticipants | number | 否 | 最大参与人数 |

**响应**

```json
{
  "code": 200,
  "message": "发布成功",
  "data": {
    "id": 15,
    "toolId": -1,
    "title": "扶摇Agent高级编排培训",
    "status": "upcoming",
    "createTime": "2026-01-13T15:30:00Z"
  }
}
```

---

### 8. 设置置顶帖子（Agent专有）

Owner或管理员设置/取消置顶帖子。

**请求**

```
PUT /api/agent/featured-post
```

**请求体**

```json
{
  "postId": 1
}
```

**请求字段说明**

| 字段 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| postId | number | 是 | 帖子ID，设置为null或0表示取消置顶 |

**响应**

```json
{
  "code": 200,
  "message": "设置成功",
  "data": {
    "postId": 1,
    "setTime": "2026-01-13T16:00:00Z"
  }
}
```

---

## 共用接口说明

以下接口与AI工具专区完全共用，通过 `toolId` 区分数据范围：

| toolId值 | 说明 |
|---------|------|
| -1 | 扶摇Agent应用 |
| 0 | 其他工具 |
| >0 | 具体工具（如TestMate=1，CodeMate=2等） |

### 完全共用的接口

| 接口 | 路径 | 说明 |
|-----|------|------|
| 检查Owner权限 | `GET /api/tools/{toolId}/check-owner` | 统一权限检查 |
| 获取帖子列表 | `GET /api/tools/posts` | 统一帖子列表接口 |
| 获取标签统计 | `GET /api/tools/{toolId}/tags` | 统一标签统计接口 |
| 获取活动列表 | `GET /api/tools/activities` | 统一活动列表接口 |
| 发布活动 | `POST /api/tools/activities` | 统一发布活动接口 |
| 获取活动详情 | `GET /api/tools/activities/{activityId}` | 获取单个活动详情 |
| 报名活动 | `POST /api/tools/activities/{activityId}/join` | 用户报名活动 |
| 取消报名 | `DELETE /api/tools/activities/{activityId}/join` | 用户取消报名 |
| 编辑活动 | `PUT /api/tools/activities/{activityId}` | Owner/管理员编辑活动 |
| 删除活动 | `DELETE /api/tools/activities/{activityId}` | Owner/管理员删除活动 |

### 专有接口差异

| 功能 | 扶摇Agent | AI工具专区（其他工具） |
|-----|-----------|---------------------|
| 精华帖子 | `GET/PUT /api/agent/featured-post` | `GET/PUT /api/tools/featured-post?toolId=0` |
| 部门统计 | 不适用 | `GET /api/tools/{toolId}/departments` |

> 详细接口说明请参考 [AI工具专区接口文档](./AI工具专区接口文档.md)

---

## 错误码说明

| 错误码 | 说明 |
|-------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或登录已过期 |
| 403 | 无权限（非Owner尝试发布活动/设置置顶等） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 数据字典

### toolId 说明

| 值 | 说明 |
|-----|------|
| -1 | 扶摇Agent应用 |
| 0 | 其他工具（AI工具专区） |
| >0 | 具体工具ID（AI工具专区） |

### 帖子标签（Agent应用常用）

| 标签 | 说明 |
|-----|------|
| Agent应用 | 扶摇Agent应用相关 |
| 工作流 | 工作流编排相关 |
| 自动化 | 自动化实践相关 |
| 智能编排 | 智能编排功能相关 |
| 最佳实践 | 最佳实践总结 |
| 案例分享 | 实际案例分享 |
| 开发指南 | 开发指导文档 |
| 问题解决 | 问题排查与解决 |

### 活动类型说明

| 类型 | 值 | 说明 |
|-----|-----|------|
| 活动 | activity | 一般性活动，如交流会、分享会等 |
| 培训 | training | 正式的培训课程 |
| 工作坊 | workshop | 动手实践性质的工作坊 |

> **与AI工具专区共用**：活动类型定义与AI工具专区完全一致。

### 活动状态说明

| 状态 | 值 | 说明 |
|-----|-----|------|
| 即将开始 | upcoming | 活动还未开始 |
| 进行中 | ongoing | 活动正在进行 |
| 已结束 | ended | 活动已结束 |

---

## 前端调用示例

### 页面初始化流程

```typescript
// 1. 检查用户权限
const user = await getCurrentUser()
const isAdmin = user.roles?.includes('admin') || false

// 2. 检查工具Owner权限（toolId=-1 表示扶摇Agent）
const ownerResponse = await checkToolOwner(-1)
const isToolOwner = ownerResponse.isOwner || false

// 3. 加载置顶帖子
const featuredResponse = await getFeaturedPost()
const featuredPost = featuredResponse.post

// 4. 加载活动列表
const activitiesResponse = await getActivities({ toolId: -1, page: 1, pageSize: 10 })
const activities = activitiesResponse.list

// 5. 加载帖子列表
const postsResponse = await getPosts({ toolId: -1, page: 1, pageSize: 15 })
const posts = postsResponse.list

// 6. 加载标签列表
const tagsResponse = await getTags({ toolId: -1 })
const tags = tagsResponse.list
```

### 发布活动按钮显示条件

```typescript
// 工具Owner或管理员可以发布活动
const canPublishActivity = isToolOwner || isAdmin
```

### 标签筛选

```typescript
const handleTagClick = (tagName: string) => {
  const tag = tagName === '全部' ? null : tagName
  loadPosts({ toolId: -1, tag, page: 1, pageSize: 15 })
}
```

---

## 附录：前端TypeScript类型定义

以下是前端组件使用的核心类型定义，供后端参考：

### 帖子类型（PostList组件）

```typescript
interface Post {
  id: number
  title: string
  description?: string
  author?: string
  createTime: string | Date
  views: number
  comments?: number
  likes?: number
  isLiked?: boolean
  image?: string       // 封面图
  tag?: string         // 单个主标签
  tags?: string[]      // 标签数组
  featured?: boolean   // 是否精华
}
```

### 活动类型（ActivityCarousel组件）

```typescript
interface Activity {
  id: number
  type?: 'activity' | 'training' | 'workshop'
  title: string
  desc: string         // 描述（从content转换）
  date: string         // 日期字符串
  location?: string
  image?: string       // 封面图（从cover转换）
  cover?: string
}
```

### 标签类型（TagFilter组件）

```typescript
interface Tag {
  name: string
  count: number
}
```

### 用户类型

```typescript
interface UserProfile {
  id: number
  employeeId?: string
  name: string
  avatar: string
  department?: string
  roles?: string[]     // 角色列表，包含'admin'表示管理员
  ownedTools?: Array<{
    toolId: number
    toolName: string
  }>
}
```
