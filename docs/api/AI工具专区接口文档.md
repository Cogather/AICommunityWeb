# AI工具专区页面接口文档

## 概述

AI工具专区页面用于展示公司内部AI工具的使用指导、优秀实践和相关活动培训信息。页面主要功能包括：
- 工具列表展示与切换
- 普通工具：操作指导/优秀使用帖子分类 + 近期活动培训
- 其他工具：帖子列表 + 标签筛选 + 部门归类
- 工具Owner/管理员发布活动功能

---

## 接口列表

### 1. 获取工具列表

获取AI工具专区展示的所有工具信息。此接口同时服务于首页AI工具专区和工具专区页面。

**请求**

```
GET /api/tools
```

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| featured | boolean | 否 | 是否只返回推荐工具 |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "name": "TestMate",
        "desc": "自动化测试助手",
        "logo": "https://example.com/logos/testmate.png",
        "link": "/tools?toolId=1",
        "color": "#36cfc9"
      },
      {
        "id": 2,
        "name": "CodeMate",
        "desc": "智能代码补全",
        "logo": "https://example.com/logos/codemate.png",
        "link": "/tools?toolId=2",
        "color": "#9254de"
      },
      {
        "id": 3,
        "name": "云集",
        "desc": "云端计算集群",
        "logo": "https://example.com/logos/yunji.png",
        "link": "/tools?toolId=3",
        "color": "#597ef7"
      },
      {
        "id": 4,
        "name": "云见",
        "desc": "智能监控平台",
        "logo": "https://example.com/logos/yunjian.png",
        "link": "/tools?toolId=4",
        "color": "#ff9c6e"
      },
      {
        "id": 5,
        "name": "扶摇",
        "desc": "Agent编排引擎",
        "logo": "https://example.com/logos/fuyao.png",
        "link": "/tools?toolId=5",
        "color": "#4096ff"
      }
    ]
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | number | 工具唯一标识 |
| name | string | 工具名称 |
| desc | string | 工具简介描述 |
| logo | string | 工具Logo图片URL |
| link | string | 点击跳转链接，跳转到社区内工具专区页面 |
| color | string | 工具主题颜色（十六进制） |

> **说明**：此接口返回的工具列表用于：
> - 首页 AI 工具专区展示
> - 工具专区页面工具按钮列表
> 
> 点击工具后跳转到 `/tools?toolId={id}` 查看该工具的相关帖子和活动。

---

### 2. 获取工具帖子列表

根据工具ID获取相关帖子列表，支持分类筛选、标签筛选、搜索和排序。

**请求**

```
GET /api/tools/posts
```

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID，0表示"其他工具" |
| category | string | 否 | 帖子分类：guide-操作指导，excellent-优秀使用 |
| tag | string | 否 | 标签筛选 |
| department | string | 否 | 部门筛选 |
| keyword | string | 否 | 搜索关键词 |
| sortBy | string | 否 | 排序方式：newest-最新，hot-最热，comments-评论最多 |
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
        "title": "TestMate 快速入门指南",
        "description": "本文详细介绍如何快速上手使用TestMate进行自动化测试...",
        "cover": "https://example.com/covers/post1.jpg",
        "author": "张三",
        "authorId": 101,
        "authorAvatar": "https://example.com/avatars/user101.jpg",
        "department": "研发部",
        "toolId": 1,
        "toolName": "TestMate",
        "category": "guide",
        "tags": ["新手", "入门"],
        "views": 1580,
        "likes": 89,
        "comments": 23,
        "createTime": "2026-01-10T10:30:00Z",
        "updateTime": "2026-01-10T10:30:00Z"
      }
    ],
    "total": 45,
    "page": 1,
    "pageSize": 15
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | number | 帖子唯一标识 |
| title | string | 帖子标题 |
| description | string | 帖子摘要/描述 |
| cover | string | 封面图URL |
| author | string | 作者名称 |
| authorId | number | 作者用户ID |
| authorAvatar | string | 作者头像URL |
| department | string | 作者所属部门 |
| toolId | number | 关联工具ID，0表示其他工具 |
| toolName | string | 关联工具名称 |
| category | string | 帖子分类：guide/excellent |
| tags | array | 帖子标签数组 |
| views | number | 浏览量 |
| likes | number | 点赞数 |
| comments | number | 评论数 |
| createTime | string | 创建时间（ISO 8601格式） |
| updateTime | string | 更新时间（ISO 8601格式） |

---

### 3. 获取工具活动列表

获取指定工具的近期活动与培训信息。

**请求**

```
GET /api/tools/activities
```

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID |
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
        "toolId": 1,
        "toolName": "TestMate",
        "type": "training",
        "title": "TestMate 高级特性培训",
        "content": "<p>本次培训将深入讲解TestMate的高级功能...</p>",
        "cover": "https://example.com/activities/training1.jpg",
        "date": "2026-01-20",
        "startTime": "14:00",
        "endTime": "16:00",
        "location": "线上腾讯会议",
        "meetingLink": "https://meeting.tencent.com/xxx",
        "speaker": "李四",
        "speakerTitle": "高级测试工程师",
        "maxParticipants": 100,
        "currentParticipants": 45,
        "status": "upcoming",
        "createTime": "2026-01-08T09:00:00Z"
      }
    ],
    "total": 8,
    "page": 1,
    "pageSize": 10
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | number | 活动唯一标识 |
| toolId | number | 关联工具ID |
| toolName | string | 关联工具名称 |
| type | string | 活动类型：activity-活动，training-培训，workshop-工作坊 |
| title | string | 活动标题 |
| content | string | 活动详细内容（HTML格式） |
| cover | string | 活动封面图URL |
| date | string | 活动日期（YYYY-MM-DD） |
| startTime | string | 开始时间（HH:mm） |
| endTime | string | 结束时间（HH:mm） |
| location | string | 活动地点 |
| meetingLink | string | 线上会议链接（可选） |
| speaker | string | 主讲人姓名 |
| speakerTitle | string | 主讲人职称 |
| maxParticipants | number | 最大参与人数 |
| currentParticipants | number | 当前报名人数 |
| status | string | 活动状态：upcoming/ongoing/ended |
| createTime | string | 创建时间（ISO 8601格式） |

---

### 4. 检查工具Owner权限

检查当前用户是否为指定工具的Owner。

**请求**

```
GET /api/tools/{toolId}/check-owner
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "isOwner": true,
    "permissions": ["edit", "publish_activity", "manage_posts"]
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| isOwner | boolean | 是否为工具Owner |
| permissions | array | 用户拥有的权限列表 |

**权限说明**

| 权限 | 说明 |
|-----|------|
| edit | 编辑工具信息 |
| publish_activity | 发布活动/培训 |
| manage_posts | 管理帖子（置顶、删除等） |

---

### 5. 获取标签统计

获取指定工具下帖子的标签统计信息。

**请求**

```
GET /api/tools/{toolId}/tags
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID，0表示"其他工具" |

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
      {
        "name": "全部",
        "count": 156
      },
      {
        "name": "新手",
        "count": 45
      },
      {
        "name": "进阶",
        "count": 32
      },
      {
        "name": "最佳实践",
        "count": 28
      },
      {
        "name": "技巧",
        "count": 21
      },
      {
        "name": "案例",
        "count": 18
      },
      {
        "name": "优化",
        "count": 12
      }
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

### 6. 获取部门统计

获取指定工具下帖子的部门归类统计。

**请求**

```
GET /api/tools/{toolId}/departments
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 工具ID，0表示"其他工具" |

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| tag | string | 否 | 标签筛选，用于联动统计 |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "name": "研发部",
        "postCount": 45,
        "contributorCount": 12
      },
      {
        "id": 2,
        "name": "技术部",
        "postCount": 38,
        "contributorCount": 9
      },
      {
        "id": 3,
        "name": "算法部",
        "postCount": 25,
        "contributorCount": 6
      },
      {
        "id": 4,
        "name": "数据部",
        "postCount": 22,
        "contributorCount": 8
      },
      {
        "id": 5,
        "name": "测试部",
        "postCount": 18,
        "contributorCount": 5
      }
    ]
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | number | 部门ID |
| name | string | 部门名称 |
| postCount | number | 该部门的发帖数 |
| contributorCount | number | 该部门的贡献者数量 |

---

### 7. 发布活动

工具Owner或管理员发布新活动/培训。

**请求**

```
POST /api/tools/activities
```

**请求体**

```json
{
  "toolId": 1,
  "type": "training",
  "title": "TestMate 高级特性培训",
  "content": "<p>本次培训将深入讲解TestMate的高级功能...</p>",
  "cover": "https://example.com/activities/training1.jpg",
  "date": "2026-01-20",
  "startTime": "14:00",
  "endTime": "16:00",
  "location": "线上腾讯会议",
  "meetingLink": "https://meeting.tencent.com/xxx",
  "speaker": "李四",
  "speakerTitle": "高级测试工程师",
  "maxParticipants": 100
}
```

**请求字段说明**

| 字段 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| toolId | number | 是 | 关联工具ID |
| type | string | 是 | 活动类型：activity/training/workshop |
| title | string | 是 | 活动标题 |
| content | string | 是 | 活动详细内容（HTML格式） |
| cover | string | 否 | 活动封面图URL |
| date | string | 是 | 活动日期（YYYY-MM-DD） |
| startTime | string | 是 | 开始时间（HH:mm） |
| endTime | string | 是 | 结束时间（HH:mm） |
| location | string | 是 | 活动地点 |
| meetingLink | string | 否 | 线上会议链接 |
| speaker | string | 否 | 主讲人姓名 |
| speakerTitle | string | 否 | 主讲人职称 |
| maxParticipants | number | 否 | 最大参与人数，不填则不限制 |

**响应**

```json
{
  "code": 200,
  "message": "发布成功",
  "data": {
    "id": 15,
    "toolId": 1,
    "title": "TestMate 高级特性培训",
    "status": "upcoming",
    "createTime": "2026-01-13T15:30:00Z"
  }
}
```

---

### 8. 获取活动详情

获取单个活动的详细信息。

**请求**

```
GET /api/tools/activities/{activityId}
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| activityId | number | 是 | 活动ID |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "toolId": 1,
    "toolName": "TestMate",
    "type": "training",
    "title": "TestMate 高级特性培训",
    "content": "<p>本次培训将深入讲解TestMate的高级功能...</p>",
    "cover": "https://example.com/activities/training1.jpg",
    "date": "2026-01-20",
    "startTime": "14:00",
    "endTime": "16:00",
    "location": "线上腾讯会议",
    "meetingLink": "https://meeting.tencent.com/xxx",
    "speaker": "李四",
    "speakerTitle": "高级测试工程师",
    "speakerAvatar": "https://example.com/avatars/lisi.jpg",
    "maxParticipants": 100,
    "currentParticipants": 45,
    "participants": [
      {
        "userId": 101,
        "userName": "王五",
        "avatar": "https://example.com/avatars/user101.jpg",
        "joinTime": "2026-01-10T09:00:00Z"
      }
    ],
    "status": "upcoming",
    "isJoined": false,
    "createTime": "2026-01-08T09:00:00Z",
    "creatorId": 50,
    "creatorName": "管理员"
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| participants | array | 参与者列表（最多返回前10个） |
| isJoined | boolean | 当前用户是否已报名 |
| creatorId | number | 活动创建者ID |
| creatorName | string | 活动创建者名称 |

---

### 9. 报名活动

用户报名参加活动。

**请求**

```
POST /api/tools/activities/{activityId}/join
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| activityId | number | 是 | 活动ID |

**响应**

```json
{
  "code": 200,
  "message": "报名成功",
  "data": {
    "activityId": 1,
    "userId": 101,
    "joinTime": "2026-01-13T16:00:00Z"
  }
}
```

**错误响应**

```json
{
  "code": 400,
  "message": "活动报名已满",
  "data": null
}
```

---

### 10. 取消报名

用户取消活动报名。

**请求**

```
DELETE /api/tools/activities/{activityId}/join
```

**路径参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| activityId | number | 是 | 活动ID |

**响应**

```json
{
  "code": 200,
  "message": "取消成功",
  "data": null
}
```

---

## 错误码说明

| 错误码 | 说明 |
|-------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或登录已过期 |
| 403 | 无权限（非工具Owner尝试发布活动等） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 数据字典

### 工具分类说明

| 分类 | 说明 |
|-----|------|
| 普通工具 | 有明确toolId的工具，如TestMate、CodeMate等 |
| 其他工具 | toolId为0或"other"，用于收录未分类的AI工具相关内容 |

### 帖子分类说明

| 分类 | 值 | 说明 |
|-----|-----|------|
| 操作指导 | guide | 工具使用教程、操作手册等 |
| 优秀使用 | excellent | 用户分享的优秀使用案例和经验 |

### 活动类型说明

| 类型 | 值 | 说明 |
|-----|-----|------|
| 活动 | activity | 一般性活动，如交流会、分享会等 |
| 培训 | training | 正式的培训课程 |
| 工作坊 | workshop | 动手实践性质的工作坊 |

### 活动状态说明

| 状态 | 值 | 说明 |
|-----|-----|------|
| 即将开始 | upcoming | 活动还未开始 |
| 进行中 | ongoing | 活动正在进行 |
| 已结束 | ended | 活动已结束 |
