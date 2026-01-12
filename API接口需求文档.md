# AI社区Web前端接口需求文档

## 重要说明：数据来源分类

### 管理后台配置数据（静态配置）
以下数据由管理员在管理后台配置，前端通过接口读取显示：

1. **首页轮播图** - 管理员在后台配置轮播图内容、图片、链接等
2. **荣誉殿堂** - 管理员在后台配置Banner图片和奖项列表
   - **注意**: AI使用达人（Top用户）由系统根据用户荣誉数量动态计算，无需配置
3. **AI工具配置** - 管理员在后台配置：
   - 工具列表（工具名称、描述、Logo、颜色）
   - 工具跳转路由路径
   - 工具Banner图
5. **扶摇Agent应用置顶帖子** - 管理员在后台配置置顶帖子的内容、封面图等
6. **推荐封面** - 管理员在后台配置发帖页面的推荐封面图片列表
7. **标签列表** - 不同专区有不同的标签列表，由后端返回（不是管理后台配置）

**数据流向**：
```
管理后台配置 → PUT /api/admin/xxx → 数据库存储
首页读取 → GET /api/home/xxx → 显示配置内容
```

### 接口动态数据（动态内容）
以下数据通过接口动态获取，实时更新：

1. **AI优秀实践帖子列表** - 通过 `GET /api/posts?zone=practices` 动态获取
2. **赋能交流帖子列表** - 通过 `GET /api/posts?zone=empowerment` 动态获取
3. **用户发布的帖子、评论、收藏等** - 实时从数据库获取
4. **活动列表** - 实时从数据库获取
5. **荣誉记录** - 实时从数据库获取
6. **AI使用达人（Top用户）** - 根据用户荣誉数量动态计算排序
7. **标签列表** - 根据专区和工具动态返回对应的标签列表
8. **本月积分排行榜** - 用于获奖者推荐，根据本月积分动态计算（排除管理员）

**数据流向**：
```
用户操作 → POST/PUT/DELETE → 数据库更新
前端读取 → GET → 实时显示最新数据
```

---

## 快速参考：核心接口列表

### 用户相关接口
1. **用户登录** - `POST /api/auth/login`
2. **用户登出** - `POST /api/auth/logout`
3. **用户个人积分** - `GET /api/user/points/calculate`
4. **用户个人中心** - `GET /api/user/current`

### 首页相关接口
1. **首页轮播图** - `GET /api/home/carousel`
2. **AI使用达人（荣誉殿堂）** - `GET /api/home/honor` (包含topUsers)
3. **AI工具列表** - `GET /api/tools`
4. **AI工具Banner列表** - `GET /api/tools/banners`
5. **AI优秀实践内容（三块列表）** - `GET /api/home/practices`
6. **AI优秀实践帖子列表** - `GET /api/posts?zone=practices`
7. **赋能交流帖子列表** - `GET /api/posts?zone=empowerment`

### AI优秀实践页面接口
1. **帖子列表（含搜索排序）** - `GET /api/posts?zone=practices&search=xxx&sort=xxx`
2. **标签列表** - `GET /api/tags?zone=practices`
3. **部门列表** - `GET /api/departments/stats?zone=practices`
4. **热门贡献者** - `GET /api/users/top-contributors?zone=practices`
5. **最热帖子** - `GET /api/posts/hot?zone=practices`

### AI使用达人页面接口
1. **荣誉列表（按类别筛选、查询、分页）** - `GET /api/honors?filterType=xxx&filterValue=xxx&search=xxx&page=xxx`
2. **荣誉影响力** - `GET /api/honors/influence`
3. **我的荣誉** - `GET /api/honors?scope=mine&page=xxx`
4. **荣誉时光轴** - `GET /api/honors?view=timeline&page=xxx`
5. **个人用户时光轴** - `GET /api/honors?view=timeline&user=xxx&page=xxx`
6. **奖项规则** - `GET /api/awards/:id/rules`
7. **团队荣誉列表** - `GET /api/team-awards`
8. **团队荣誉详情** - `GET /api/team-awards/:id`

### AI工具专区页面接口
1. **获取工具列表** - `GET /api/tools`
2. **获取帖子列表（分页）** - `GET /api/posts?zone=tools&toolId=xxx&page=xxx`
3. **获取活动列表** - `GET /api/activities?toolId=xxx`
4. **其他工具标签列表** - `GET /api/tags?zone=tools&toolId=-1`
5. **其他工具帖子列表（分页）** - `GET /api/posts?zone=tools&toolId=-1&page=xxx`
6. **部门列表（可过滤）** - `GET /api/departments/stats?zone=tools`

### 扶摇Agent应用页面接口
1. **获取帖子列表（分页）** - `GET /api/posts?zone=agent&page=xxx`
2. **获取标签列表（可过滤）** - `GET /api/tags?zone=agent`
3. **获取活动列表** - `GET /api/activities?toolId=-1`

### 赋能交流页面接口
1. **获取帖子列表（分页）** - `GET /api/posts?zone=empowerment&page=xxx`
2. **获取标签列表（可过滤）** - `GET /api/tags?zone=empowerment`
3. **获取精选内容** - `GET /api/posts?zone=empowerment&isFeatured=true`

### 发布帖子页面接口
1. **获取工具选择列表** - `GET /api/tools` (当选择AI工具专区时)
2. **获取标签列表** - `GET /api/tags?zone=xxx&toolId=xxx` (根据专区和工具获取)
3. **获取推荐封面** - `GET /api/posts/recommended-covers`
4. **保存草稿** - `POST /api/posts/draft`
5. **发布帖子** - `POST /api/posts`

### 个人中心相关接口
1. **获取用户个人资料** - `GET /api/user/:userId/profile` 或 `GET /api/user/profile?name=xxx`
2. **获取用户发布的帖子** - `GET /api/user/:userId/posts`
3. **获取用户收藏的帖子** - `GET /api/user/:userId/favorites`
4. **获取用户评论列表** - `GET /api/user/:userId/comments`
5. **获取用户参与的活动** - `GET /api/user/:userId/activities`
6. **获取用户发布的活动** - `GET /api/user/:userId/activities/created`
7. **获取活动报名详情** - `GET /api/activities/:id/registrations` (用于查看报名用户列表)

### 帖子详情页面接口
1. **获取帖子详情** - `GET /api/posts/:id`
2. **点赞/取消点赞** - `POST /api/posts/:id/like`
3. **收藏/取消收藏** - `POST /api/posts/:id/collect`
4. **更新帖子** - `PUT /api/posts/:id`
5. **删除帖子** - `DELETE /api/posts/:id`
6. **获取评论列表** - `GET /api/posts/:id/comments`
7. **发表评论** - `POST /api/posts/:id/comments`
8. **点赞评论** - `POST /api/comments/:id/like`
9. **更新评论** - `PUT /api/comments/:id`
10. **删除评论** - `DELETE /api/comments/:id`
11. **删除回复** - `DELETE /api/replies/:id` 或 `DELETE /api/comments/:commentId/replies/:id`

### 活动详情页面接口
1. **获取活动详情** - `GET /api/activities/:id`
2. **报名活动** - `POST /api/activities/:id/register`
3. **取消报名** - `DELETE /api/activities/:id/register`
4. **获取报名用户列表** - `GET /api/activities/:id/registrations`
5. **更新活动** - `PUT /api/activities/:id`
6. **删除活动** - `DELETE /api/activities/:id`

### 发布活动页面接口
1. **获取工具选择列表** - `GET /api/tools`
2. **发布活动** - `POST /api/activities`

### 管理后台接口
1. **保存各个页面配置** - `PUT /api/admin/xxx` (轮播图、荣誉殿堂、工具配置等)
2. **获取本月积分排行榜** - `GET /api/admin/honors/recommended-winners`
3. **设置用户获奖** - `POST /api/admin/honors`
4. **获取奖项列表** - `GET /api/awards?category=xxx` (用于获奖者推荐)
5. **搜索管理员** - `GET /api/admin/users/search?name=xxx&role=admin`
6. **通过工号搜索人员** - `GET /api/admin/users/search?employeeId=xxx`

---

## 目录
1. [用户认证与个人信息](#用户认证与个人信息)
   - [用户登录](#1-用户登录)
   - [用户登出](#2-用户登出)
   - [用户个人积分](#用户个人积分接口)
   - [用户个人中心](#3-获取当前用户信息)
2. [首页](#首页)
   - [首页轮播图](#1-获取首页轮播图管理后台配置)
   - [AI使用达人（荣誉殿堂）](#2-获取荣誉殿堂信息管理后台配置)
   - [AI工具专区（Banner和工具列表）](#3-获取ai工具列表管理后台配置)
   - [AI优秀实践](#4-获取ai优秀实践帖子列表接口动态数据)
   - [赋能交流](#5-获取赋能交流帖子列表接口动态数据)
3. [AI优秀实践页面](#帖子相关)
   - [帖子列表（含搜索排序）](#1-获取帖子列表)
   - [标签列表](#18-获取标签列表)
   - [部门列表](#19-获取部门统计)
   - [热门贡献者](#20-获取热门贡献者)
   - [最热帖子](#21-获取最热帖子)
4. [帖子相关](#帖子相关)
   - [获取帖子列表](#1-获取帖子列表ai优秀实践页面核心接口)
   - [获取帖子详情](#2-获取帖子详情帖子详情页面)
   - [创建帖子](#3-创建帖子发布帖子页面)
   - [更新帖子](#4-更新帖子编辑帖子内容)
   - [删除帖子](#5-删除帖子删除帖子内容)
   - [点赞/取消点赞](#6-点赞取消点赞帖子帖子详情页面)
   - [收藏/取消收藏](#7-收藏取消收藏帖子帖子详情页面)
   - [获取评论列表](#8-获取帖子评论列表评论区接口)
   - [发表评论](#9-发表评论评论区接口)
   - [回复评论](#10-回复评论回复接口)
   - [点赞评论](#11-点赞评论)
   - [更新评论](#12-更新评论评论的更新接口)
   - [删除评论](#13-删除评论评论的删除接口)
   - [删除回复](#14-删除回复回复的删除接口)
   - [获取用户发布的帖子](#15-获取用户发布的帖子)
   - [获取用户收藏的帖子](#16-获取用户收藏的帖子)
   - [获取用户评论列表](#17-获取用户评论列表)
5. [活动相关](#活动相关)
   - [获取活动列表](#1-获取活动列表)
   - [获取活动详情](#2-获取活动详情活动详情页面)
   - [创建活动](#3-创建活动发布活动接口)
   - [更新活动](#4-更新活动编辑接口)
   - [删除活动](#5-删除活动删除接口)
   - [报名活动](#6-报名活动报名状态更新接口)
   - [取消报名](#61-取消报名报名状态更新接口)
   - [获取报名用户列表](#62-获取活动已报名用户列表该活动已报名用户名字列表的接口)
   - [获取用户参与的活动](#8-获取用户参与的活动)
   - [获取用户发布的活动](#9-获取用户发布的活动我发布的活动)
6. [荣誉系统](#荣誉系统)
7. [消息系统](#消息系统)
8. [工具专区](#工具专区)
9. [管理后台](#管理后台)
10. [数据交互流程](#数据交互流程)
11. [积分系统](#积分系统)
12. [用户角色与权限](#用户角色与权限)

---

## 用户认证与个人信息

### 1. 用户登录
- **接口**: `POST /api/auth/login`
- **请求参数**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **响应数据**:
  ```json
  {
    "token": "string",
    "user": {
      "id": "number",
      "name": "string",
      "avatar": "string",
      "department": "string",
      "role": "string" // "admin" | "user"
    }
  }
  ```
- **使用页面**: `AppNavbar.vue`

### 2. 用户登出
- **接口**: `POST /api/auth/logout`
- **使用页面**: `AppNavbar.vue`

### 3. 获取当前用户信息（用户个人中心）
- **接口**: `GET /api/user/current`
- **说明**: 获取当前登录用户的完整信息，包括个人资料、统计数据、积分等
- **响应数据**:
  ```json
  {
    "id": "number",
    "employeeId": "string", // 工号
    "name": "string",
    "avatar": "string",
    "bio": "string",
    "department": "string",
    "postsCount": "number",
    "favoritesCount": "number",
    "commentsCount": "number",
    "activitiesCount": "number",
    "flowersCount": "number",
    "points": "number", // 用户个人积分
    "roles": ["admin" | "owner" | "user"], // 用户角色列表
    "ownedTools": [ // 如果是工具Owner，显示拥有的工具列表
      {
        "toolId": "number",
        "toolName": "string"
      }
    ]
  }
  ```
- **使用页面**: `AppNavbar.vue`, `ProfileView.vue`

### 3.1. 获取用户个人积分详情
- **接口**: `GET /api/user/points/calculate`
- **说明**: 获取用户个人积分的详细计算信息，包括积分总数和各项积分明细
- **响应数据**:
  ```json
  {
    "points": "number", // 总积分
    "breakdown": {
      "posts": "number", // 发帖获得的积分
      "comments": "number", // 评论获得的积分
      "likesReceived": "number", // 被点赞获得的积分
      "favoritesReceived": "number", // 被收藏获得的积分
      "activities": "number" // 参加活动获得的积分
    }
  }
  ```
- **使用页面**: `AppNavbar.vue`, `ProfileView.vue`
- **积分规则**: 详见[积分系统](#积分系统)章节

### 4. 获取用户个人资料（根据用户名或用户ID）
- **接口**: `GET /api/user/:userId/profile` 或 `GET /api/user/profile?name=xxx`
- **说明**: 获取指定用户的个人资料信息，用于个人中心页面（支持查看其他用户）
- **路径参数**:
  - `userId`: number (用户ID，可选)
- **查询参数**: 
  - `userId`: number (用户ID，可选，与路径参数二选一)
  - `name`: string (用户名，可选，与userId二选一)
- **响应数据**: 同"获取当前用户信息"
  ```json
  {
    "id": "number",
    "employeeId": "string",
    "name": "string",
    "avatar": "string",
    "bio": "string",
    "department": "string",
    "postsCount": "number",
    "favoritesCount": "number",
    "commentsCount": "number",
    "activitiesCount": "number",
    "flowersCount": "number",
    "points": "number"
  }
  ```
- **使用页面**: `ProfileView.vue` (个人中心页面，支持查看其他用户)

### 5. 更新用户资料
- **接口**: `PUT /api/user/profile`
- **请求参数**:
  ```json
  {
    "bio": "string",
    "avatar": "string"
  }
  ```
- **使用页面**: `ProfileView.vue`

---

## 首页

> **重要说明**: 首页数据分为两类：
> 1. **管理后台配置数据**：轮播图、荣誉殿堂、工具配置 - 这些数据由管理员在后台配置，前端通过接口读取
> 2. **接口动态数据**：AI优秀实践、赋能交流的帖子列表 - 这些数据通过接口动态获取

### 1. 获取首页轮播图（管理后台配置）
- **接口**: `GET /api/home/carousel`
- **说明**: 数据由管理后台配置，首页读取显示
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "image": "string",
        "title": "string",
        "desc": "string",
        "link": "string",
        "showContent": "boolean",
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `HomeView.vue` (HeroCarousel组件)
- **配置页面**: `AdminView.vue` (首页管理-轮播图配置)

### 2. 获取荣誉殿堂信息（管理后台配置）
- **接口**: `GET /api/home/honor`
- **说明**: 获取荣誉殿堂模块的所有数据，包括：
  - 荣誉殿堂Banner图片
  - 奖项列表
  - AI使用达人（Top用户）
- **响应数据**:
  ```json
  {
    "honor": {
      "bannerImage": "string",
      "awards": [
        {
          "id": "number",
          "name": "string",
          "desc": "string",
          "image": "string",
          "order": "number"
        }
      ],
      "topUsers": [
        {
          "id": "number",
          "name": "string",
          "avatar": "string",
          "level": "number",
          "department": "string"
        }
      ]
    }
  }
  ```
- **使用页面**: `HomeView.vue`
- **配置页面**: 
  - `AdminView.vue` (首页管理-荣誉殿堂Banner配置)
  - `AdminView.vue` (首页管理-荣誉殿堂奖项配置)
- **数据说明**:
  - `honor.bannerImage`: 荣誉殿堂Banner图片URL（管理后台配置）
  - `honor.awards`: 奖项列表（管理后台配置）
  - `honor.topUsers`: AI使用达人Top用户列表（动态计算，根据用户荣誉数量排序）

### 3. 获取AI工具列表（管理后台配置）
- **接口**: `GET /api/tools`
- **查询参数**: `featured` (boolean, 可选，是否只获取推荐工具)
- **说明**: 工具列表、工具路由都由管理后台配置
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "desc": "string",
        "logo": "string",
        "color": "string",
        "link": "string" // 跳转路由，如 "/tools?toolId=1" 或 "/tools/testmate?toolId=1"，用于跳转到工具专区并过滤显示对应工具的内容
      }
    ]
  }
  ```
- **使用页面**: `HomeView.vue` (首页-工具列表), `ToolsView.vue` (工具专区页面)
- **数据说明**:
  - `link`: 跳转路由路径，点击工具按钮后会跳转到此路径。如果link中包含`toolId`参数，工具专区页面会自动选中对应的工具并过滤显示该工具的内容。如果link不包含`toolId`参数，系统会自动添加`toolId`参数。
  - 例如：`/tools?toolId=1` 或 `/tools/testmate?toolId=1` 都会跳转到工具专区并自动选中ID为1的工具

### 3.1. 获取AI工具Banner列表（管理后台配置）
- **接口**: `GET /api/tools/banners`
- **说明**: 获取AI工具专区的Banner轮播图列表，由管理后台配置
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "image": "string",
        "title": "string",
        "desc": "string",
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `HomeView.vue` (首页-AI工具专区Banner)

### 4. 获取AI优秀实践内容（首页三块列表）
- **接口**: `GET /api/home/practices`
- **说明**: 获取首页AI优秀实践模块的三块列表内容（培训赋能、AI训战、用户交流）
- **响应数据**:
  ```json
  {
    "training": [
      {
        "id": "number",
        "title": "string",
        "author": "string",
        "time": "string",
        "category": "training"
      }
    ],
    "trainingBattle": [
      {
        "id": "number",
        "title": "string",
        "author": "string",
        "time": "string",
        "category": "training-battle"
      }
    ],
    "userExchange": [
      {
        "id": "number",
        "title": "string",
        "author": "string",
        "time": "string",
        "category": "user-exchange"
      }
    ]
  }
  ```
- **使用页面**: `HomeView.vue` (首页-AI优秀实践模块)
- **数据说明**:
  - `training`: 培训赋能列表（默认返回前5条）
  - `trainingBattle`: AI训战列表（默认返回前5条）
  - `userExchange`: 用户交流列表（默认返回前5条）

### 5. 获取AI工具列表（管理后台配置）
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "desc": "string",
        "logo": "string",
        "color": "string",
        "link": "string", // 跳转路由，由管理后台配置
        "banners": [ // 工具Banner图，由管理后台配置
          {
            "id": "number",
            "image": "string",
            "title": "string",
            "desc": "string",
            "order": "number"
          }
        ]
      }
    ]
  }
  ```
- **使用页面**: 
  - `HomeView.vue` - 首页工具专区展示
  - `ToolsView.vue` - 工具筛选按钮（除了"其他工具"按钮，其他工具按钮都来自此接口）
  - `PostCreateView.vue` - 发帖页面选择工具类别时的工具列表
  - `ActivityCreateView.vue` - 发布活动页面选择工具时的工具列表
- **配置页面**: `AdminView.vue` (首页管理-AI工具配置)
- **注意**: 
  - 工具专区页面的工具筛选按钮（除了"其他工具"）都来自此接口返回的工具列表
  - "其他工具"是前端固定的，不在工具配置中

### 5. 获取AI优秀实践帖子列表（接口动态数据）
- **接口**: `GET /api/posts`
- **查询参数**: 
  - `zone`: "practices"
  - `limit`: number (默认6)
  - `sort`: "newest" | "hot"
- **说明**: 动态获取AI优秀实践专区的帖子列表
- **响应数据**: 见"帖子相关-获取帖子列表"
- **使用页面**: `PracticesView.vue` (AI优秀实践页面)

### 6. 获取赋能交流帖子列表（接口动态数据）

### 7. 获取赋能交流帖子列表（接口动态数据）
- **接口**: `GET /api/posts`
- **查询参数**: 
  - `zone`: "empowerment"
  - `limit`: number (默认6)
  - `sort`: "newest" | "hot"
- **说明**: 动态获取赋能交流专区的帖子列表
- **响应数据**: 见"帖子相关-获取帖子列表"
- **使用页面**: `HomeView.vue` (赋能交流模块)

### 8. 获取扶摇Agent应用置顶帖子（管理后台配置）
- **接口**: `GET /api/agent/featured-post`
- **说明**: 获取扶摇Agent应用页面的置顶帖子，由管理后台配置
- **响应数据**:
  ```json
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "author": "string",
    "authorAvatar": "string",
    "createTime": "string",
    "views": "number",
    "comments": "number",
    "likes": "number",
    "tags": ["string"],
    "cover": "string", // 封面图URL，必填
    "image": "string", // 封面图URL（兼容字段，与cover相同）
    "link": "string" // 可选，跳转链接（如果不提供则跳转到 /post/:id）
  }
  ```
- **使用页面**: `AgentView.vue`
- **配置页面**: `AdminView.vue` (管理后台-扶摇Agent应用置顶帖子配置)

---

## 帖子相关

> **重要说明**: AI优秀实践页面（PracticesView）需要以下接口支持：
> - 帖子列表接口（支持搜索、排序、筛选）
> - 标签列表接口
> - 部门统计接口
> - 热门贡献者接口
> - 最热帖子接口

### 1. 获取帖子列表（AI优秀实践页面核心接口）
- **接口**: `GET /api/posts`
- **说明**: 支持搜索、排序、筛选功能的帖子列表接口，用于AI优秀实践页面展示帖子
- **查询参数**:
  - `zone`: "practices" | "tools" | "agent" | "empowerment" | "other" (必填，指定专区)
  - `toolId`: number (工具ID，可选，当zone为tools时使用)
  - `tag`: string (标签，可选，筛选指定标签的帖子)
  - `department`: string (部门，可选，筛选指定部门的帖子)
  - `author`: string (作者，可选，筛选指定作者的帖子)
  - `sort`: "newest" | "hot" | "comments" | "likes" (排序方式，默认"newest")
    - `newest`: 最新发布
    - `hot`: 最热门（综合浏览量、点赞数、评论数）
    - `comments`: 评论数最多
    - `likes`: 点赞数最多
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认10，最大100)
  - `search`: string (搜索关键词，可选，支持标题和内容搜索)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "title": "string",
        "description": "string",
        "author": "string",
        "authorAvatar": "string",
        "createTime": "string",
        "views": "number",
        "comments": "number",
        "likes": "number",
        "tags": ["string"],
        "image": "string", // 封面图URL
        "cover": "string", // 封面图URL（与image相同，兼容字段）
        "isFeatured": "boolean",
        "zone": "string",
        "toolId": "number",
        "toolName": "string"
      }
    ],
    "total": "number",
    "page": "number",
    "pageSize": "number"
  }
  ```
- **使用页面**: `PracticesView.vue`, `ToolsView.vue`, `AgentView.vue`, `EmpowermentView.vue`, `HomeView.vue`
- **注意**: 所有帖子列表接口返回的数据都包含封面图字段（`cover` 或 `image`）

### 2. 获取帖子详情（帖子详情页面）
- **接口**: `GET /api/posts/:id`
- **说明**: 获取指定帖子的详细信息，用于帖子详情页面展示
- **路径参数**:
  - `id`: number (帖子ID)
- **响应数据**:
  ```json
  {
    "id": "number",
    "title": "string",
    "summary": "string", // 内容简介
    "content": "string", // 帖子内容（HTML格式）
    "authorName": "string",
    "authorAvatar": "string",
    "authorId": "number", // 作者ID
    "createTime": "string",
    "updateTime": "string", // 更新时间
    "views": "number",
    "likes": "number",
    "comments": "number", // 评论总数
    "tags": ["string"],
    "cover": "string", // 封面图URL
    "isLiked": "boolean", // 当前用户是否已点赞
    "isCollected": "boolean", // 当前用户是否已收藏
    "isAuthor": "boolean", // 当前用户是否是作者
    "canEdit": "boolean", // 当前用户是否可以编辑（作者或管理员）
    "canDelete": "boolean", // 当前用户是否可以删除（作者或管理员）
    "zone": "practices" | "tools" | "agent" | "empowerment",
    "toolId": "number",
    "toolName": "string"
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面)

### 3. 创建帖子（发布帖子页面）
- **接口**: `POST /api/posts`
- **说明**: 发布新帖子，用于发布帖子页面
- **请求参数**:
  ```json
  {
    "title": "string", // 帖子标题
    "summary": "string", // 内容简介（必填）
    "content": "string", // 帖子内容
    "tags": ["string"], // 标签列表
    "cover": "string", // 封面图URL
    "zone": "practices" | "tools" | "agent" | "empowerment", // 专区
    "toolId": "number" // 当zone为tools时必填（从 GET /api/tools 获取）
  }
  ```
- **响应数据**:
  ```json
  {
    "id": "number",
    "message": "string"
  }
  ```
- **使用页面**: `PostCreateView.vue` (发布帖子页面)
- **数据依赖**:
  - 工具列表: 通过 `GET /api/tools` 获取（当选择AI工具专区时）
  - 标签列表: 通过 `GET /api/tags?zone=xxx&toolId=xxx` 获取（根据专区和工具选择）
  - 推荐封面: 通过 `GET /api/posts/recommended-covers` 获取

### 3.0. 获取工具选择列表（发布帖子页面）
- **接口**: `GET /api/tools`
- **说明**: 当用户在发布帖子页面选择"AI工具专区"时，获取工具选择列表
- **查询参数**: 无
- **响应数据**: 同"首页-获取AI工具列表"
- **使用页面**: `PostCreateView.vue` (发布帖子页面-工具选择)
- **注意**: 
  - 只有当用户选择"AI工具专区"（zone=tools）时才需要调用此接口
  - 工具列表用于下拉选择框，用户必须选择一个工具（必填）

### 3.0.1. 获取标签列表（发布帖子页面）
- **接口**: `GET /api/tags?zone=xxx&toolId=xxx`
- **说明**: 根据用户选择的专区和工具，获取对应的已有标签列表，用于发布帖子页面
- **查询参数**:
  - `zone`: "practices" | "tools" | "agent" | "empowerment" (必填，根据用户选择的专区)
  - `toolId`: number (可选，当zone为tools时，根据用户选择的工具ID获取标签)
- **响应数据**: 同"获取标签列表"
- **使用页面**: `PostCreateView.vue` (发布帖子页面-标签选择)
- **标签获取规则**:
  - AI优秀实践（zone=practices）: 返回自然语言处理、计算机视觉等标签
  - AI工具专区（zone=tools）:
    - 如果选择了具体工具（toolId不为-1）: 返回固定标签（"操作指导"、"优秀使用"）
    - 如果选择了"其他工具"（toolId=-1）: 返回工具专区的通用标签
  - 扶摇Agent应用（zone=agent）: 返回Agent应用、工作流等标签
  - 赋能交流（zone=empowerment）: 返回讨论、提问、分享等标签

### 3.0.2. 保存草稿（发布帖子页面）
- **接口**: `POST /api/posts/draft`
- **说明**: 保存帖子草稿，用于发布帖子页面的自动保存功能
- **请求参数**:
  ```json
  {
    "title": "string", // 帖子标题（可选）
    "summary": "string", // 内容简介（可选）
    "content": "string", // 帖子内容（可选）
    "tags": ["string"], // 标签列表（可选）
    "cover": "string", // 封面图URL（可选）
    "zone": "practices" | "tools" | "agent" | "empowerment", // 专区（可选）
    "toolId": "number" // 工具ID（可选，当zone为tools时）
  }
  ```
- **响应数据**:
  ```json
  {
    "draftId": "string", // 草稿ID
    "savedAt": "string", // 保存时间
    "message": "string"
  }
  ```
- **使用页面**: `PostCreateView.vue` (发布帖子页面-自动保存草稿)
- **说明**: 
  - 前端可以定期（如每30秒）自动调用此接口保存草稿
  - 草稿保存后，用户下次进入发布页面时可以恢复草稿内容
  - 草稿ID可以存储在localStorage中，用于恢复草稿

### 3.1. 获取推荐封面列表
- **接口**: `GET /api/posts/recommended-covers`
- **说明**: 获取发帖页面的推荐封面图片列表
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "url": "string",
        "thumbnail": "string" // 缩略图URL（可选）
      }
    ]
  }
  ```
- **使用页面**: `PostCreateView.vue`

### 4. 更新帖子（编辑帖子内容）
- **接口**: `PUT /api/posts/:id`
- **说明**: 更新帖子内容，用于帖子详情页面的编辑功能
- **路径参数**:
  - `id`: number (帖子ID)
- **请求参数**: 同"创建帖子"
  ```json
  {
    "title": "string",
    "summary": "string",
    "content": "string",
    "tags": ["string"],
    "cover": "string",
    "zone": "practices" | "tools" | "agent" | "empowerment",
    "toolId": "number"
  }
  ```
- **响应数据**:
  ```json
  {
    "id": "number",
    "message": "string"
  }
  ```
- **使用页面**: `PostCreateView.vue` (编辑模式), `PostDetailView.vue` (帖子详情页面-编辑)
- **权限说明**: 只有帖子作者或管理员可以编辑帖子

### 5. 删除帖子（删除帖子内容）
- **接口**: `DELETE /api/posts/:id`
- **说明**: 删除帖子，用于帖子详情页面的删除功能
- **路径参数**:
  - `id`: number (帖子ID)
- **响应数据**:
  ```json
  {
    "success": "boolean",
    "message": "string"
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面-删除)
- **权限说明**: 只有帖子作者或管理员可以删除帖子
- **注意**: 删除帖子时，该帖子的所有评论和回复会一并删除

### 6. 点赞/取消点赞帖子（帖子详情页面）
- **接口**: `POST /api/posts/:id/like`
- **说明**: 更新帖子的点赞状态，用于帖子详情页面
- **路径参数**:
  - `id`: number (帖子ID)
- **请求参数**:
  ```json
  {
    "action": "like" | "unlike"
  }
  ```
- **响应数据**:
  ```json
  {
    "liked": "boolean", // 当前点赞状态
    "likes": "number" // 更新后的点赞总数
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面)
- **积分规则**: 帖子被点赞时，作者积分+3（管理员除外）

### 7. 收藏/取消收藏帖子（帖子详情页面）
- **接口**: `POST /api/posts/:id/collect`
- **说明**: 更新帖子的收藏状态，用于帖子详情页面
- **路径参数**:
  - `id`: number (帖子ID)
- **请求参数**:
  ```json
  {
    "action": "collect" | "uncollect"
  }
  ```
- **响应数据**:
  ```json
  {
    "collected": "boolean" // 当前收藏状态
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面), `ProfileView.vue` (个人中心)
- **积分规则**: 帖子被收藏时，作者积分+5（管理员除外）

### 8. 获取帖子评论列表（评论区接口）
- **接口**: `GET /api/posts/:id/comments`
- **说明**: 获取指定帖子的评论列表，用于帖子详情页面的评论区
- **路径参数**:
  - `id`: number (帖子ID)
- **查询参数**: 
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认10，最大100)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "userId": "number",
        "userName": "string",
        "userAvatar": "string",
        "content": "string",
        "createTime": "string",
        "updateTime": "string", // 更新时间
        "likes": "number",
        "isLiked": "boolean", // 当前用户是否已点赞
        "isAuthor": "boolean", // 是否是帖子作者
        "isMyComment": "boolean", // 是否是当前用户的评论
        "canEdit": "boolean", // 是否可以编辑
        "canDelete": "boolean", // 是否可以删除
        "replies": [
          {
            "id": "number",
            "userId": "number",
            "userName": "string",
            "userAvatar": "string",
            "content": "string",
            "createTime": "string",
            "replyTo": "string", // 回复的用户名
            "replyToId": "number", // 回复的用户ID
            "likes": "number",
            "isLiked": "boolean"
          }
        ]
      }
    ],
    "total": "number"
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面-评论区)

### 9. 发表评论（评论区接口）
- **接口**: `POST /api/posts/:id/comments`
- **说明**: 在指定帖子下发表评论，用于帖子详情页面的评论区
- **路径参数**:
  - `id`: number (帖子ID)
- **请求参数**:
  ```json
  {
    "content": "string", // 评论内容（必填）
    "replyTo": "number" // 可选，回复的评论ID（如果是回复评论则必填）
  }
  ```
- **响应数据**: 
  ```json
  {
    "id": "number",
    "userId": "number",
    "userName": "string",
    "userAvatar": "string",
    "content": "string",
    "createTime": "string",
    "likes": 0,
    "isLiked": false,
    "replies": []
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面-发表评论)
- **积分规则**: 发表评论成功后，用户积分+1（管理员除外）

### 10. 回复评论（回复接口）
- **接口**: `POST /api/posts/:id/comments`
- **说明**: 回复指定评论，用于帖子详情页面的回复功能
- **路径参数**:
  - `id`: number (帖子ID)
- **请求参数**:
  ```json
  {
    "content": "string", // 回复内容（必填）
    "replyTo": "number" // 必填，回复的评论ID
  }
  ```
- **响应数据**: 同"发表评论"
- **使用页面**: `PostDetailView.vue` (帖子详情页面-回复评论)
- **说明**: 回复会作为评论的子回复显示在评论的replies数组中

### 11. 点赞评论
- **接口**: `POST /api/comments/:id/like`
- **说明**: 点赞或取消点赞评论，用于帖子详情页面
- **路径参数**:
  - `id`: number (评论ID)
- **请求参数**:
  ```json
  {
    "action": "like" | "unlike"
  }
  ```
- **响应数据**:
  ```json
  {
    "liked": "boolean",
    "likes": "number"
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面)
- **积分规则**: 评论被点赞时，评论作者积分+1（管理员除外）

### 12. 更新评论（评论的更新接口）
- **接口**: `PUT /api/comments/:id`
- **说明**: 更新评论内容，用于帖子详情页面编辑评论
- **路径参数**:
  - `id`: number (评论ID)
- **请求参数**:
  ```json
  {
    "content": "string" // 更新后的评论内容
  }
  ```
- **响应数据**:
  ```json
  {
    "id": "number",
    "content": "string",
    "updateTime": "string",
    "message": "string"
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面-编辑评论)
- **权限说明**: 只有评论作者可以更新自己的评论

### 13. 删除评论（评论的删除接口）
- **接口**: `DELETE /api/comments/:id`
- **说明**: 删除评论，用于帖子详情页面删除评论功能
- **路径参数**:
  - `id`: number (评论ID)
- **响应数据**:
  ```json
  {
    "success": "boolean",
    "message": "string"
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面-删除评论)
- **权限说明**: 
  - 只有评论作者可以删除自己的评论
  - 管理员可以删除任何评论
- **注意**: 
  - 删除评论时，该评论下的所有回复会一并删除
  - 删除后无法恢复

### 14. 删除回复（回复的删除接口）
- **接口**: `DELETE /api/replies/:id` 或 `DELETE /api/comments/:commentId/replies/:id`
- **说明**: 删除回复，用于帖子详情页面删除回复功能
- **路径参数**:
  - `id`: number (回复ID)
  - `commentId`: number (评论ID，如果使用第二种路径格式)
- **响应数据**:
  ```json
  {
    "success": "boolean",
    "message": "string"
  }
  ```
- **使用页面**: `PostDetailView.vue` (帖子详情页面-删除回复)
- **权限说明**: 
  - 只有回复作者可以删除自己的回复
  - 管理员可以删除任何回复
- **注意**: 
  - 删除回复只删除该条回复，不影响其他回复
  - 删除后无法恢复

### 12. 获取用户发布的帖子
- **接口**: `GET /api/user/:userId/posts`
- **查询参数**: `page`, `pageSize`
- **响应数据**: 同"获取帖子列表"
- **使用页面**: `ProfileView.vue`

### 13. 获取用户收藏的帖子
- **接口**: `GET /api/user/:userId/favorites`
- **查询参数**: `page`, `pageSize`
- **响应数据**: 同"获取帖子列表"
- **使用页面**: `ProfileView.vue`

### 14. 获取用户评论列表
- **接口**: `GET /api/user/:userId/comments`
- **查询参数**: `page`, `pageSize`
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "postId": "number",
        "postTitle": "string",
        "content": "string",
        "createTime": "string",
        "likes": "number"
      }
    ],
    "total": "number"
  }
  ```
- **使用页面**: `ProfileView.vue`

### 18. 获取标签列表（AI优秀实践页面使用）
- **接口**: `GET /api/tags`
- **说明**: 获取指定专区的标签列表，用于AI优秀实践页面的标签筛选功能
- **查询参数**: 
  - `zone`: "practices" | "tools" | "agent" | "empowerment" (必填，按专区获取标签)
  - `toolId`: number (可选，当zone为tools时，指定工具ID获取该工具的标签)
- **说明**: 
  - 不同专区有不同的标签列表
  - AI优秀实践专区（zone=practices）: 返回自然语言处理、计算机视觉、深度学习、AI伦理、机器学习等标签
  - AI工具专区：如果指定toolId且不为"其他"，返回固定标签（"操作指导"、"优秀使用"）
  - AI工具专区：如果toolId为"其他"或不指定，返回工具专区的通用标签
  - 其他专区：返回对应专区的标签列表
- **响应数据**:
  ```json
  {
    "list": [
      {
        "name": "string",
        "count": "number", // 可选，该标签下的帖子数量
        "preset": "boolean" // 可选，是否为预设标签（不可删除）
      }
    ]
  }
  ```
- **使用页面**: 
  - `PracticesView.vue` - 标签筛选
  - `ToolsView.vue` - 标签筛选（其他工具时）
  - `PostList.vue` - 标签展示
  - `PostCreateView.vue` - 发帖页面选择标签（必用接口）
- **标签分类说明**:
  - **AI优秀实践 (practices)**: 自然语言处理、计算机视觉、深度学习、AI伦理、机器学习等
  - **AI工具专区 (tools)**: 
    - 特定工具（toolId指定且不为"其他"）: 操作指导、优秀使用（固定标签）
    - 其他工具或通用: 新手、进阶、最佳实践、技巧、案例、教程等
  - **扶摇Agent应用 (agent)**: Agent应用、工作流、自动化、智能编排、案例分享等
  - **赋能交流 (empowerment)**: 讨论、提问、分享、经验、工具、技巧、案例、教程等

### 19. 获取部门统计（AI优秀实践页面使用）
- **接口**: `GET /api/departments/stats`
- **说明**: 获取各部门的帖子统计信息，用于AI优秀实践页面的部门筛选功能
- **查询参数**: 
  - `zone`: "practices" | "tools" | "agent" | "empowerment" (可选，指定专区，不传则返回所有专区的统计)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string", // 部门名称
        "postCount": "number", // 该部门发布的帖子数量
        "contributorCount": "number" // 该部门的贡献者数量
      }
    ]
  }
  ```
- **使用页面**: `PracticesView.vue` (AI优秀实践页面), `ToolsView.vue` (工具专区页面)

### 20. 获取热门贡献者（AI优秀实践页面使用）
- **接口**: `GET /api/users/top-contributors`
- **说明**: 获取指定专区的热门贡献者列表，用于AI优秀实践页面展示
- **查询参数**: 
  - `zone`: "practices" | "tools" | "agent" | "empowerment" (可选，指定专区，不传则返回所有专区的贡献者)
  - `limit`: number (可选，返回数量，默认10)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string", // 用户姓名
        "avatar": "string", // 用户头像
        "postCount": "number" // 发布的帖子数量
      }
    ]
  }
  ```
- **使用页面**: `PracticesView.vue` (AI优秀实践页面)
- **排序规则**: 按发布的帖子数量从高到低排序

### 21. 获取最热帖子（AI优秀实践页面使用）
- **接口**: `GET /api/posts/hot`
- **说明**: 获取指定专区的最热门帖子列表，用于AI优秀实践页面展示
- **查询参数**: 
  - `zone`: "practices" | "tools" | "agent" | "empowerment" (可选，指定专区，不传则返回所有专区的热门帖子)
  - `limit`: number (可选，返回数量，默认10)
- **响应数据**: 同"获取帖子列表"，返回的帖子按热度排序
- **使用页面**: `PracticesView.vue` (AI优秀实践页面)
- **热度计算规则**: 综合浏览量、点赞数、评论数等因素计算热度值

### 22. 获取扶摇Agent应用帖子列表（分页）
- **接口**: `GET /api/posts?zone=agent`
- **说明**: 获取扶摇Agent应用专区的帖子列表，支持分页、搜索、排序、筛选
- **查询参数**:
  - `zone`: "agent" (必填)
  - `tag`: string (可选，标签筛选)
  - `department`: string (可选，部门筛选)
  - `sort`: "newest" | "hot" | "comments" | "likes" (可选，排序方式)
  - `search`: string (可选，搜索关键词)
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认10，最大100)
- **响应数据**: 同"获取帖子列表"
- **使用页面**: `AgentView.vue` (扶摇Agent应用页面)

### 23. 获取扶摇Agent应用标签列表（可过滤）
- **接口**: `GET /api/tags?zone=agent`
- **说明**: 获取扶摇Agent应用专区的标签列表，支持过滤
- **查询参数**:
  - `zone`: "agent" (必填)
- **响应数据**: 同"获取标签列表"
- **使用页面**: `AgentView.vue` (扶摇Agent应用页面-标签筛选)

### 24. 获取扶摇Agent应用活动列表
- **接口**: `GET /api/activities?toolId=-1`
- **说明**: 获取扶摇Agent应用相关的活动列表（toolId=-1表示扶摇Agent应用）
- **查询参数**:
  - `toolId`: -1 (必填，表示扶摇Agent应用)
  - `type`: "training" | "competition" | "sharing" (可选，活动类型)
  - `status`: "upcoming" | "ongoing" | "ended" (可选，活动状态)
- **响应数据**: 同"活动相关-获取活动列表"
- **使用页面**: `AgentView.vue` (扶摇Agent应用页面)

### 25. 获取赋能交流帖子列表（分页）
- **接口**: `GET /api/posts?zone=empowerment`
- **说明**: 获取赋能交流专区的帖子列表，支持分页、搜索、排序、筛选
- **查询参数**:
  - `zone`: "empowerment" (必填)
  - `tag`: string (可选，标签筛选)
  - `department`: string (可选，部门筛选)
  - `sort`: "newest" | "hot" | "comments" | "likes" (可选，排序方式)
  - `search`: string (可选，搜索关键词)
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认10，最大100)
- **响应数据**: 同"获取帖子列表"
- **使用页面**: `EmpowermentView.vue` (赋能交流页面)

### 26. 获取赋能交流标签列表（可过滤）
- **接口**: `GET /api/tags?zone=empowerment`
- **说明**: 获取赋能交流专区的标签列表，支持过滤
- **查询参数**:
  - `zone`: "empowerment" (必填)
- **响应数据**: 同"获取标签列表"
- **使用页面**: `EmpowermentView.vue` (赋能交流页面-标签筛选)

### 27. 获取赋能交流精选内容
- **接口**: `GET /api/posts?zone=empowerment&isFeatured=true`
- **说明**: 获取赋能交流专区的精选帖子列表
- **查询参数**:
  - `zone`: "empowerment" (必填)
  - `isFeatured`: true (必填，筛选精选内容)
  - `limit`: number (可选，返回数量，默认10)
- **响应数据**: 同"获取帖子列表"，只返回精选帖子
- **使用页面**: `EmpowermentView.vue` (赋能交流页面-精选内容)

---

## 活动相关

### 1. 获取活动列表
- **接口**: `GET /api/activities`
- **查询参数**:
  - `toolId`: number (可选)
  - `type`: "training" | "competition" | "sharing" (可选)
  - `status`: "upcoming" | "ongoing" | "ended" (可选)
  - `page`: number
  - `pageSize`: number
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "title": "string",
        "toolId": "number",
        "toolName": "string",
        "type": "string",
        "date": "string",
        "cover": "string",
        "description": "string",
        "status": "string",
        "registeredCount": "number"
      }
    ],
    "total": "number"
  }
  ```
- **使用页面**: `ToolsView.vue`, `ActivityCarousel.vue`, `ProfileView.vue`

### 2. 获取活动详情（活动详情页面）
- **接口**: `GET /api/activities/:id`
- **说明**: 获取指定活动的详细信息，用于活动详情页面展示
- **路径参数**:
  - `id`: number (活动ID)
- **响应数据**:
  ```json
  {
    "id": "number",
    "title": "string",
    "toolId": "number",
    "toolName": "string",
    "type": "training" | "competition" | "sharing",
    "date": "string", // 活动日期
    "cover": "string", // 封面图URL
    "content": "string", // 活动内容（HTML格式）
    "authorId": "number", // 发布者ID
    "authorName": "string", // 发布者姓名
    "authorAvatar": "string", // 发布者头像
    "createTime": "string", // 创建时间
    "updateTime": "string", // 更新时间
    "status": "upcoming" | "ongoing" | "ended", // 活动状态
    "isRegistered": "boolean", // 当前用户是否已报名
    "registeredCount": "number", // 已报名人数
    "isAuthor": "boolean", // 当前用户是否是发布者
    "canEdit": "boolean", // 当前用户是否可以编辑（发布者或管理员）
    "canDelete": "boolean" // 当前用户是否可以删除（发布者或管理员）
  }
  ```
- **使用页面**: `ActivityDetailView.vue` (活动详情页面)

### 3. 创建活动（发布活动接口）
- **接口**: `POST /api/activities`
- **说明**: 发布新活动，用于发布活动页面
- **请求参数**:
  ```json
  {
    "title": "string", // 活动标题（必填）
    "toolId": "number", // 工具ID（从 GET /api/tools 获取，可选，-1表示扶摇Agent应用）
    "type": "training" | "competition" | "sharing", // 活动类型（必填）
    "date": "string", // 活动日期（必填，格式：YYYY-MM-DD）
    "cover": "string", // 封面图URL（必填）
    "content": "string" // 活动内容（必填，HTML格式）
  }
  ```
- **响应数据**:
  ```json
  {
    "id": "number",
    "message": "string"
  }
  ```
- **使用页面**: `ActivityCreateView.vue` (发布活动页面)
- **数据依赖**:
  - 工具列表: 通过 `GET /api/tools` 获取（用于选择活动关联的工具）

### 3.0. 获取可选择的工具下拉列表（发布活动页面）
- **接口**: `GET /api/tools`
- **说明**: 获取所有工具列表，用于发布活动页面的工具下拉选择
- **查询参数**: 无
- **响应数据**: 同"首页-获取AI工具列表"
- **使用页面**: `ActivityCreateView.vue` (发布活动页面-工具选择)
- **注意**: 
  - 工具列表用于下拉选择框
  - toolId为-1表示"扶摇Agent应用"

### 4. 更新活动（编辑接口）
- **接口**: `PUT /api/activities/:id`
- **说明**: 更新活动信息，用于活动详情页面的编辑功能
- **路径参数**:
  - `id`: number (活动ID)
- **请求参数**: 同"创建活动"
  ```json
  {
    "title": "string",
    "toolId": "number",
    "type": "training" | "competition" | "sharing",
    "date": "string",
    "cover": "string",
    "content": "string"
  }
  ```
- **响应数据**:
  ```json
  {
    "id": "number",
    "message": "string"
  }
  ```
- **使用页面**: `ActivityCreateView.vue` (编辑模式), `ActivityDetailView.vue` (活动详情页面-编辑)
- **权限说明**: 只有活动发布者或管理员可以编辑活动

### 5. 删除活动（删除接口）
- **接口**: `DELETE /api/activities/:id`
- **说明**: 删除活动，用于活动详情页面的删除功能
- **路径参数**:
  - `id`: number (活动ID)
- **响应数据**:
  ```json
  {
    "success": "boolean",
    "message": "string"
  }
  ```
- **使用页面**: `ActivityDetailView.vue` (活动详情页面-删除)
- **权限说明**: 只有活动发布者或管理员可以删除活动
- **注意**: 删除活动时，该活动的所有报名记录会一并删除

### 6. 报名活动（报名状态更新接口）
- **接口**: `POST /api/activities/:id/register`
- **说明**: 报名参加活动，用于活动详情页面的报名功能
- **路径参数**:
  - `id`: number (活动ID)
- **响应数据**:
  ```json
  {
    "success": "boolean",
    "message": "string",
    "isRegistered": true, // 更新后的报名状态
    "registeredCount": "number" // 更新后的报名人数
  }
  ```
- **使用页面**: `ActivityDetailView.vue` (活动详情页面-报名)
- **积分规则**: 报名成功后，用户积分+10（管理员除外）

### 6.1. 取消报名（报名状态更新接口）
- **接口**: `DELETE /api/activities/:id/register`
- **说明**: 取消活动报名，用于活动详情页面取消报名功能
- **路径参数**:
  - `id`: number (活动ID)
- **响应数据**:
  ```json
  {
    "success": "boolean",
    "message": "string",
    "isRegistered": false, // 更新后的报名状态
    "registeredCount": "number" // 更新后的报名人数
  }
  ```
- **使用页面**: `ActivityDetailView.vue` (活动详情页面-取消报名)

### 6.2. 获取活动已报名用户列表（该活动已报名用户名字列表的接口）
- **接口**: `GET /api/activities/:id/registrations`
- **说明**: 获取指定活动的已报名用户列表，用于活动详情页面展示
- **路径参数**:
  - `id`: number (活动ID)
- **查询参数**:
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认20，最大100)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number", // 报名记录ID
        "userId": "number", // 用户ID
        "userName": "string", // 用户姓名
        "userAvatar": "string", // 用户头像
        "employeeId": "string", // 工号
        "department": "string", // 部门
        "registerTime": "string" // 报名时间
      }
    ],
    "total": "number" // 总报名人数
  }
  ```
- **使用页面**: `ActivityDetailView.vue` (活动详情页面-已报名用户列表)

### 6.3. 检查用户是否为工具Owner
- **接口**: `GET /api/tools/:id/owner`
- **说明**: 检查当前用户是否为指定工具的Owner，用于控制发布活动按钮的显示
- **响应数据**:
  ```json
  {
    "isOwner": "boolean",
    "toolId": "number",
    "toolName": "string"
  }
  ```
- **使用页面**: `ToolsView.vue`, `AgentView.vue`
- **权限说明**: 
  - 只有工具Owner可以在工具专区页面和扶摇Agent应用页面看到发布活动按钮
  - 发布活动按钮以悬浮按钮形式显示在页面右下角

### 7. 取消报名
- **接口**: `DELETE /api/activities/:id/register`
- **使用页面**: `ActivityDetailView.vue`

### 8. 获取用户参与的活动
- **接口**: `GET /api/user/:userId/activities`
- **说明**: 获取指定用户参与（已报名）的活动列表，用于个人中心"我参与的活动"标签页
- **路径参数**:
  - `userId`: number (用户ID)
- **查询参数**: 
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认10，最大100)
- **响应数据**: 同"获取活动列表"
- **使用页面**: `ProfileView.vue` (个人中心-我参与的活动)

### 9. 获取用户发布的活动（我发布的活动）
- **接口**: `GET /api/user/:userId/activities/created`
- **说明**: 获取指定用户发布的活动列表，用于个人中心"我发布的活动"标签页
- **路径参数**:
  - `userId`: number (用户ID)
- **查询参数**: 
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认10，最大100)
  - `status`: "upcoming" | "ongoing" | "ended" (可选，按状态筛选)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "title": "string",
        "description": "string",
        "content": "string",
        "cover": "string",
        "date": "string",
        "type": "training" | "competition" | "sharing",
        "status": "upcoming" | "ongoing" | "ended",
        "registeredCount": "number", // 已报名人数
        "createTime": "string",
        "toolId": "number",
        "toolName": "string"
      }
    ],
    "total": "number"
  }
  ```
- **使用页面**: `ProfileView.vue` (个人中心-我发布的活动)

---

## 荣誉系统（AI使用达人页面）

> **重要说明**: 荣誉系统包括：
> - 用户荣誉记录（获奖记录）
> - 荣誉排行榜
> - 获奖者推荐（管理员功能）
> - **所有接口都支持分页功能**

### 1. 获取荣誉列表（按类别筛选、查询、分页）
- **接口**: `GET /api/honors`
- **说明**: AI使用达人页面的核心接口，支持多种筛选、查询和分页功能
- **查询参数**:
  - `scope`: "all" | "mine" (可选，默认"all")
    - `all`: 获取所有荣誉记录
    - `mine`: 获取当前用户的荣誉记录（我的荣誉）
  - `view`: "grid" | "timeline" (可选，默认"grid")
    - `grid`: 荣誉墙视图
    - `timeline`: 时光轴视图
  - `user`: string (可选，用户名，查看特定用户的时光轴)
  - `filterType`: "category" | "award" | "department" (可选，筛选类型)
    - `category`: 按类别筛选（技术创新、效能提升、最佳实践、社区贡献）
    - `award`: 按奖项名称筛选
    - `department`: 按部门筛选
  - `filterValue`: string (可选，筛选值，配合filterType使用)
  - `search`: string (可选，搜索用户名)
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认16，最大100)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "department": "string",
        "avatar": "string",
        "awardName": "string",
        "awardDate": "string",
        "category": "innovation" | "efficiency" | "practice" | "community",
        "year": "string",
        "isMine": "boolean",
        "flowers": "number",
        "hasGivenFlower": "boolean"
      }
    ],
    "total": "number"
  }
  ```
- **使用页面**: `UsersView.vue` (AI使用达人页面)
- **筛选说明**:
  - 按类别筛选：`filterType=category&filterValue=innovation` (技术创新)
  - 按奖项筛选：`filterType=award&filterValue=2026年度 AI 技术突破奖`
  - 按部门筛选：`filterType=department&filterValue=架构平台部`
  - 查询功能：`search=张三` (搜索用户名)

### 1.1. 获取我的荣誉（分页）
- **接口**: `GET /api/honors?scope=mine`
- **说明**: 获取当前登录用户的荣誉记录，支持分页
- **查询参数**:
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认16)
- **响应数据**: 同"获取荣誉列表"
- **使用页面**: `UsersView.vue` (AI使用达人页面-我的荣誉)

### 1.2. 获取荣誉时光轴（分页）
- **接口**: `GET /api/honors?view=timeline`
- **说明**: 获取所有荣誉记录的时光轴视图，按年份和日期分组，支持分页
- **查询参数**:
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认20)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "year": "string", // 年份
        "items": [
          {
            "id": "number",
            "name": "string",
            "department": "string",
            "avatar": "string",
            "awardName": "string",
            "awardDate": "string",
            "category": "innovation" | "efficiency" | "practice" | "community",
            "flowers": "number"
          }
        ]
      }
    ],
    "total": "number"
  }
  ```
- **使用页面**: `UsersView.vue` (AI使用达人页面-时光轴视图)

### 1.3. 获取个人用户时光轴（分页）
- **接口**: `GET /api/honors?view=timeline&user=xxx`
- **说明**: 获取指定用户的荣誉时光轴，点击用户头像时使用，支持分页
- **查询参数**:
  - `user`: string (必填，用户名)
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认20)
- **响应数据**: 同"获取荣誉时光轴"，但只包含指定用户的记录
- **使用页面**: `UsersView.vue` (AI使用达人页面-点击用户查看个人时光轴)

### 2. 获取荣誉影响力
- **接口**: `GET /api/honors/influence`
- **说明**: 获取荣誉影响力统计数据，用于展示荣誉的总体影响力
- **查询参数**: 无
- **响应数据**:
  ```json
  {
    "totalHonors": "number", // 总荣誉数
    "totalUsers": "number", // 获奖用户总数
    "totalFlowers": "number", // 总花朵数
    "categories": [
      {
        "category": "innovation" | "efficiency" | "practice" | "community",
        "count": "number",
        "label": "string" // 类别名称（技术创新、效能提升等）
      }
    ],
    "topDepartments": [
      {
        "name": "string",
        "count": "number"
      }
    ]
  }
  ```
- **使用页面**: `UsersView.vue` (AI使用达人页面)

### 3. 获取荣誉排行榜（分页）
- **接口**: `GET /api/honors/leaderboard`
- **说明**: 获取荣誉排行榜，按获奖数和花朵数排序，支持分页
- **查询参数**: 
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认10)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "name": "string",
        "department": "string",
        "avatar": "string",
        "count": "number",
        "totalFlowers": "number"
      }
    ]
  }
  ```
- **使用页面**: `UsersView.vue`
- **说明**: 排行榜按获奖数优先排序，获奖数相同时按总花朵数排序

### 2.1. 获取AI使用达人Top用户（用于首页展示）
- **接口**: `GET /api/honors/top-users`
- **查询参数**: `limit` (默认6)
- **说明**: 用于首页荣誉殿堂模块展示的AI使用达人，根据用户荣誉数量动态计算
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "avatar": "string",
        "level": "number", // 根据荣誉数量计算的等级
        "department": "string"
      }
    ]
  }
  ```
- **使用页面**: `HomeView.vue` (通过 `GET /api/home/honor` 接口返回)
- **计算逻辑**: 
  - 根据用户获得的荣誉数量计算等级
  - 荣誉数量越多，等级越高
  - 默认返回Top 6名用户

### 3. 给荣誉送花
- **接口**: `POST /api/honors/:id/flower`
- **响应数据**:
  ```json
  {
    "flowers": "number",
    "hasGivenFlower": "boolean"
  }
  ```
- **使用页面**: `UsersView.vue`

### 4. 获取奖项规则详情
- **接口**: `GET /api/awards/:id/rules`
- **说明**: 获取指定奖项的详细规则信息，用于AI使用达人页面展示奖项规则
- **查询参数**: 
  - `id`: number (奖项ID，路径参数)
  - `award`: string (奖项名称，可选，作为备用查询参数)
- **响应数据**:
  ```json
  {
    "id": "number",
    "name": "string", // 奖项名称
    "rules": "string", // 奖项规则详细说明
    "category": "innovation" | "efficiency" | "practice" | "community",
    "image": "string", // 奖项图片
    "desc": "string" // 奖项描述
  }
  ```
- **使用页面**: `UsersView.vue` (AI使用达人页面), `AwardRulesView.vue` (奖项规则详情页)

### 5. 获取奖项列表
- **接口**: `GET /api/awards`
- **查询参数**:
  - `category`: string (可选，筛选奖项分类: "innovation" | "efficiency" | "practice" | "community")
- **说明**: 
  - 如果不传category参数，返回所有已配置的奖项
  - 如果传入category参数，只返回该分类下的奖项
  - 奖项数据来自管理后台配置的奖项列表（奖项管理页面）
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "desc": "string",
        "image": "string",
        "category": "innovation" | "efficiency" | "practice" | "community",
        "rules": "string"
      }
    ]
  }
  ```
- **使用页面**: `HomeView.vue`, `AwardRulesView.vue`, `AdminView.vue` (获奖者推荐-设置评奖)

### 6. 获取团队荣誉列表
- **接口**: `GET /api/team-awards`
- **查询参数**:
  - `year`: string (可选，筛选年份，格式：YYYY，如 "2026")
- **说明**: 获取团队荣誉列表，支持按年份筛选
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "title": "string",
        "year": "number",
        "images": [
          {
            "id": "number",
            "image": "string",
            "winnerName": "string"
          }
        ]
      }
    ]
  }
  ```
- **使用页面**: `UsersView.vue` (AI使用达人页面-团队荣誉)

### 7. 获取团队荣誉详情
- **接口**: `GET /api/team-awards/:id`
- **路径参数**:
  - `id`: number (团队奖项ID)
- **响应数据**:
  ```json
  {
    "id": "number",
    "title": "string",
    "year": "number",
    "images": [
      {
        "id": "number",
        "image": "string",
        "winnerName": "string"
      }
    ]
  }
  ```
- **使用页面**: `UsersView.vue` (AI使用达人页面-团队荣誉)



---

## 消息系统

### 1. 获取消息列表
- **接口**: `GET /api/messages`
- **查询参数**:
  - `type`: "all" | "activity_registration" | "post_comment" | "comment_reply" | "post_like" | "award_notification"
  - `page`: number
  - `pageSize`: number
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "type": "string",
        "title": "string",
        "content": "string",
        "read": "boolean",
        "createdAt": "string",
        "fromUserName": "string",
        "link": "string"
      }
    ],
    "total": "number",
    "unreadCount": "number"
  }
  ```
- **使用页面**: `MessageListView.vue`

### 2. 标记消息为已读
- **接口**: `PUT /api/messages/:id/read`
- **使用页面**: `MessageListView.vue`

### 3. 全部标记为已读
- **接口**: `PUT /api/messages/read-all`
- **使用页面**: `MessageListView.vue`

### 4. 删除消息
- **接口**: `DELETE /api/messages/:id`
- **使用页面**: `MessageListView.vue`

### 5. 获取未读消息数量
- **接口**: `GET /api/messages/unread-count`
- **响应数据**:
  ```json
  {
    "count": "number"
  }
  ```
- **使用页面**: `AppNavbar.vue`

---

## 工具专区（AI工具专区页面）

> **重要说明**: AI工具专区页面包括：
> - 工具列表展示
> - 工具对应的帖子列表（支持分页）
> - 工具对应的活动列表
> - 其他工具的标签和帖子列表（支持分页）
> - 部门列表（支持过滤）

### 1. 获取工具列表（AI工具专区页面）
- **接口**: `GET /api/tools`
- **说明**: 获取所有工具列表，用于AI工具专区页面的工具筛选按钮（除了"其他工具"按钮）
- **查询参数**: 
  - `featured`: boolean (可选，是否只获取推荐工具)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "desc": "string",
        "logo": "string",
        "color": "string",
        "link": "string", // 跳转路由
        "banners": [ // 工具Banner，由管理后台配置
          {
            "id": "number",
            "image": "string",
            "title": "string",
            "desc": "string",
            "order": "number"
          }
        ]
      }
    ]
  }
  ```
- **使用页面**: `ToolsView.vue` (AI工具专区页面)
- **注意**: 
  - 工具专区页面会显示所有工具按钮（来自此接口）+ "其他工具"按钮（前端固定）
  - 工具ID为-1表示"其他工具"

### 2. 获取工具详情
- **接口**: `GET /api/tools/:id`
- **说明**: 获取指定工具的详细信息（包括Banner），由管理后台配置
- **响应数据**:
  ```json
  {
    "id": "number",
    "name": "string",
    "desc": "string",
    "logo": "string",
    "color": "string",
    "link": "string", // 跳转路由
    "banners": [ // 工具Banner，由管理后台配置
      {
        "id": "number",
        "image": "string",
        "title": "string",
        "desc": "string",
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `ToolsView.vue`
- **配置页面**: `AdminView.vue` (首页管理-AI工具配置)

### 3. 获取工具对应的帖子列表（分页）
- **接口**: `GET /api/posts?zone=tools&toolId=xxx`
- **说明**: 获取指定工具对应的帖子列表，支持分页、搜索、排序、筛选
- **查询参数**:
  - `zone`: "tools" (必填)
  - `toolId`: number (必填，工具ID，-1表示"其他工具")
  - `tag`: string (可选，标签筛选)
  - `department`: string (可选，部门筛选)
  - `sort`: "newest" | "hot" | "comments" | "likes" (可选，排序方式)
  - `search`: string (可选，搜索关键词)
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认10，最大100)
- **响应数据**: 同"帖子相关-获取帖子列表"
- **使用页面**: `ToolsView.vue` (AI工具专区页面)

### 4. 获取工具对应的活动列表
- **接口**: `GET /api/activities?toolId=xxx`
- **说明**: 获取指定工具相关的活动列表
- **查询参数**:
  - `toolId`: number (必填，工具ID，-1表示扶摇Agent应用)
  - `type`: "training" | "competition" | "sharing" (可选，活动类型)
  - `status`: "upcoming" | "ongoing" | "ended" (可选，活动状态)
  - `limit`: number (可选，返回数量，默认3)
- **响应数据**: 同"活动相关-获取活动列表"
- **使用页面**: `ToolsView.vue` (AI工具专区页面)

### 5. 获取其他工具的所有标签（可过滤）
- **接口**: `GET /api/tags?zone=tools&toolId=-1`
- **说明**: 获取"其他工具"的标签列表，支持过滤
- **查询参数**:
  - `zone`: "tools" (必填)
  - `toolId`: -1 (必填，表示"其他工具")
- **响应数据**: 同"帖子相关-获取标签列表"
- **使用页面**: `ToolsView.vue` (AI工具专区页面-其他工具筛选)

### 6. 获取其他工具的帖子列表（分页）
- **接口**: `GET /api/posts?zone=tools&toolId=-1`
- **说明**: 获取"其他工具"的帖子列表，支持分页、搜索、排序、筛选
- **查询参数**:
  - `zone`: "tools" (必填)
  - `toolId`: -1 (必填，表示"其他工具")
  - `tag`: string (可选，标签筛选)
  - `department`: string (可选，部门筛选)
  - `sort`: "newest" | "hot" | "comments" | "likes" (可选，排序方式)
  - `search`: string (可选，搜索关键词)
  - `page`: number (页码，从1开始，默认1)
  - `pageSize`: number (每页数量，默认10，最大100)
- **响应数据**: 同"帖子相关-获取帖子列表"
- **使用页面**: `ToolsView.vue` (AI工具专区页面-其他工具)

### 7. 获取部门列表（可过滤）
- **接口**: `GET /api/departments/stats?zone=tools`
- **说明**: 获取工具专区的部门统计信息，支持按专区过滤
- **查询参数**:
  - `zone`: "tools" (可选，指定专区，不传则返回所有专区的统计)
- **响应数据**: 同"帖子相关-获取部门统计"
- **使用页面**: `ToolsView.vue` (AI工具专区页面-部门筛选)

---

## 管理后台

> **重要说明**: 管理后台用于配置首页的静态内容，包括轮播图、荣誉殿堂、工具配置等。这些配置数据会被首页读取并显示。
>
> **保存各个页面配置接口**：管理后台的所有配置接口都使用 `PUT` 方法保存配置，包括：
> - 首页轮播图配置
> - 荣誉殿堂Banner配置
> - 荣誉殿堂奖项配置
> - AI工具配置
> - 扶摇Agent应用置顶帖子配置
> - 推荐封面配置

### 1. 获取首页轮播图配置
- **接口**: `GET /api/admin/home/carousel`
- **说明**: 获取当前配置的轮播图列表，用于管理后台编辑
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "image": "string",
        "title": "string",
        "desc": "string",
        "link": "string",
        "showContent": "boolean",
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (首页管理-轮播图配置)

### 2. 保存首页轮播图配置
- **接口**: `PUT /api/admin/home/carousel`
- **请求参数**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "image": "string",
        "title": "string",
        "desc": "string",
        "link": "string",
        "showContent": "boolean",
        "order": "number"
      }
    ]
  }
  ```
- **说明**: 保存轮播图配置后，首页通过 `GET /api/home/carousel` 读取
- **使用页面**: `AdminView.vue` (首页管理-轮播图配置)

### 3. 获取荣誉殿堂Banner配置
- **接口**: `GET /api/admin/home/honor-banner`
- **响应数据**:
  ```json
  {
    "bannerImage": "string"
  }
  ```
- **使用页面**: `AdminView.vue` (首页管理-荣誉殿堂Banner配置)

### 4. 保存荣誉殿堂Banner配置
- **接口**: `PUT /api/admin/home/honor-banner`
- **请求参数**:
  ```json
  {
    "bannerImage": "string"
  }
  ```
- **说明**: 保存后，首页通过 `GET /api/home/honor` 读取
- **使用页面**: `AdminView.vue` (首页管理-荣誉殿堂Banner配置)

### 5. 获取荣誉殿堂奖项列表配置
- **接口**: `GET /api/admin/home/honor-awards`
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "desc": "string",
        "image": "string",
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (首页管理-荣誉殿堂奖项配置)

### 6. 保存荣誉殿堂奖项列表配置
- **接口**: `PUT /api/admin/home/honor-awards`
- **请求参数**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "desc": "string",
        "image": "string",
        "order": "number"
      }
    ]
  }
  ```
- **说明**: 保存后，首页通过 `GET /api/home/honor` 读取
- **使用页面**: `AdminView.vue` (首页管理-荣誉殿堂奖项配置)
- **注意**: AI使用达人（topUsers）不需要在后台配置，由系统根据用户荣誉数量动态计算

### 7. 获取AI工具配置
- **接口**: `GET /api/admin/tools`
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "desc": "string",
        "logo": "string",
        "color": "string",
        "link": "string", // 跳转路由路径
        "banners": [
          {
            "id": "number",
            "image": "string",
            "title": "string",
            "desc": "string",
            "order": "number"
          }
        ]
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (首页管理-AI工具配置)

### 10. 保存AI工具配置
- **接口**: `PUT /api/admin/tools`
- **请求参数**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "desc": "string",
        "logo": "string",
        "color": "string",
        "link": "string", // 跳转路由路径，如 "/tools/testmate"
        "banners": [
          {
            "id": "number",
            "image": "string",
            "title": "string",
            "desc": "string",
            "order": "number"
          }
        ]
      }
    ]
  }
  ```
- **说明**: 
  - 保存工具列表、跳转路由、工具Banner配置
  - 保存后，首页通过 `GET /api/tools` 读取
  - 工具专区页面通过 `GET /api/tools` 读取工具列表和Banner
- **使用页面**: `AdminView.vue` (首页管理-AI工具配置)

### 11. 获取扶摇Agent应用置顶帖子配置
- **接口**: `GET /api/admin/agent/featured-post`
- **说明**: 获取当前配置的置顶帖子，用于管理后台编辑
- **响应数据**:
  ```json
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "author": "string",
    "authorAvatar": "string",
    "createTime": "string",
    "views": "number",
    "comments": "number",
    "likes": "number",
    "tags": ["string"],
    "cover": "string", // 封面图URL，必填
    "link": "string" // 可选，跳转链接
  }
  ```
- **使用页面**: `AdminView.vue` (管理后台-扶摇Agent应用置顶帖子配置)

### 11. 保存扶摇Agent应用置顶帖子配置
- **接口**: `PUT /api/admin/agent/featured-post`
- **请求参数**:
  ```json
  {
    "id": "number", // 可选，如果提供则关联到现有帖子，否则创建新配置
    "title": "string",
    "description": "string",
    "author": "string",
    "authorAvatar": "string",
    "createTime": "string",
    "views": "number",
    "comments": "number",
    "likes": "number",
    "tags": ["string"],
    "cover": "string", // 封面图URL，必填
    "link": "string" // 可选，跳转链接（如果不提供则使用 /post/:id）
  }
  ```
- **说明**: 
  - 保存置顶帖子配置后，扶摇Agent应用页面通过 `GET /api/agent/featured-post` 读取
  - 封面图（cover）字段必填，用于页面展示
  - 如果提供id字段，可以关联到已存在的帖子，此时会同步更新帖子的封面图等信息
- **使用页面**: `AdminView.vue` (管理后台-扶摇Agent应用置顶帖子配置)

### 12. 获取推荐封面配置
- **接口**: `GET /api/admin/posts/recommended-covers`
- **说明**: 获取当前配置的推荐封面列表，用于管理后台编辑
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "url": "string",
        "thumbnail": "string",
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (管理后台-推荐封面配置)

### 13. 保存推荐封面配置
- **接口**: `PUT /api/admin/posts/recommended-covers`
- **请求参数**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "url": "string",
        "thumbnail": "string",
        "order": "number"
      }
    ]
  }
  ```
- **说明**: 保存推荐封面配置后，发帖页面通过 `GET /api/posts/recommended-covers` 读取
- **使用页面**: `AdminView.vue` (管理后台-推荐封面配置)

### 14. 搜索用户（通过工号搜索人员的接口）
- **接口**: `GET /api/admin/users/search`
- **说明**: 通过工号、姓名或邮箱搜索用户，用于人员管理添加用户
- **查询参数**:
  - `employeeId`: string (工号，可选)
  - `name`: string (姓名，可选)
  - `email`: string (邮箱，可选)
- **说明**: 
  - 至少需要提供一个查询参数
  - 支持模糊搜索
  - 用于人员管理添加用户
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "employeeId": "string", // 工号
        "name": "string", // 姓名
        "email": "string", // 邮箱
        "department": "string", // 部门
        "avatar": "string" // 头像
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (管理后台-人员管理)

### 14.1. 搜索管理员（搜索管理员姓名的接口）
- **接口**: `GET /api/admin/users/search`
- **说明**: 搜索管理员用户，用于管理后台搜索管理员
- **查询参数**:
  - `name`: string (管理员姓名，支持模糊搜索)
  - `role`: "admin" (必填，筛选管理员角色)
  - `employeeId`: string (工号，可选)
- **响应数据**: 同"搜索用户"
- **使用页面**: `AdminView.vue` (管理后台-人员管理-搜索管理员)

### 15. 获取用户列表
- **接口**: `GET /api/admin/users`
- **查询参数**:
  - `role`: "admin" | "owner" | "user" (可选，筛选角色)
  - `toolId`: number (可选，筛选指定工具的Owner)
  - `search`: string (可选，搜索关键词，支持姓名、邮箱、工号)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "employeeId": "string", // 工号
        "name": "string",
        "email": "string",
        "department": "string",
        "avatar": "string",
        "roles": ["admin" | "owner" | "user"], // 用户角色列表
        "ownedTools": [ // 如果是工具Owner，显示拥有的工具列表
          {
            "toolId": "number",
            "toolName": "string"
          }
        ]
      }
    ],
    "total": "number"
  }
  ```
- **使用页面**: `AdminView.vue` (管理后台-人员管理)
- **说明**: 
  - 用于管理后台的人员管理页面
  - 可以筛选不同角色的用户
  - 可以查看工具Owner及其拥有的工具列表

### 16. 添加管理员
- **接口**: `POST /api/admin/users/:userId/role`
- **请求参数**:
  ```json
  {
    "role": "admin",
    "employeeId": "string" // 可选，如果通过工号搜索添加
  }
  ```
- **说明**: 将用户设置为管理员角色
- **使用页面**: `AdminView.vue` (管理后台-人员管理)

### 17. 添加工具Owner
- **接口**: `POST /api/admin/users/:userId/role`
- **请求参数**:
  ```json
  {
    "role": "owner",
    "toolId": "number", // 工具ID
    "employeeId": "string" // 可选，如果通过工号搜索添加
  }
  ```
- **说明**: 将用户设置为指定工具的Owner角色
- **响应数据**:
  ```json
  {
    "success": "boolean",
    "message": "string"
  }
  ```
- **使用页面**: `AdminView.vue` (管理后台-人员管理)

### 18. 移除用户角色
- **接口**: `DELETE /api/admin/users/:userId/role`
- **请求参数**:
  ```json
  {
    "role": "admin" | "owner",
    "toolId": "number" // 当role为owner时必填
  }
  ```
- **说明**: 移除用户的指定角色（管理员或工具Owner）
- **使用页面**: `AdminView.vue` (管理后台-人员管理)

### 19. 获取本月积分排行榜（获奖者推荐接口）
- **接口**: `GET /api/admin/honors/recommended-winners`
- **说明**: 获取本月积分靠前的用户，用于管理员推荐评奖
- **查询参数**: 
  - `month`: string (可选，格式：YYYY-MM，默认当前月份)
  - `limit`: number (默认3，推荐Top 3用户)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "employeeId": "string", // 工号
        "name": "string",
        "avatar": "string",
        "department": "string",
        "points": "number", // 本月积分
        "postsCount": "number", // 本月发帖数
        "commentsCount": "number", // 本月评论数
        "activitiesCount": "number", // 本月参加活动数
        "likesReceived": "number", // 本月被点赞数
        "favoritesReceived": "number", // 本月被收藏数
        "hasAwarded": "boolean" // 是否已评奖
      }
    ],
    "month": "string" // 当前查询的月份，格式：YYYY-MM
  }
  ```
- **使用页面**: `AdminView.vue` (管理后台-AI使用达人管理-获奖者推荐)
- **计算说明**: 
  - 只计算本月（当前月份）的积分
  - 排除管理员用户的积分
  - 按积分从高到低排序，返回Top 3

### 20. 设置用户获奖（获奖者推荐接口）
- **接口**: `POST /api/admin/honors`
- **说明**: 为推荐的用户设置获奖记录，用于管理后台获奖者推荐功能
- **请求参数**:
  ```json
  {
    "userId": "number", // 用户ID（必填）
    "awardId": "number", // 奖项ID（从奖项列表中选择，必填）
    "awardName": "string", // 奖项名称（自动从奖项ID获取）
    "awardDate": "string", // 获奖时间，格式：YYYY-MM（年月，必填）
    "category": "innovation" | "efficiency" | "practice" | "community", // 奖项分类（必填）
    "year": "string" // 年份，格式：YYYY（从awardDate中提取）
  }
  ```
- **响应数据**:
  ```json
  {
    "id": "number", // 荣誉记录ID
    "message": "string"
  }
  ```
- **使用页面**: `AdminView.vue` (管理后台-AI使用达人管理-获奖者推荐)
- **说明**: 
  - 管理员可以为推荐的用户设置获奖记录
  - 设置后，该用户会出现在荣誉墙和时光轴中
  - 设置评奖流程：
    1. 先选择奖项分类（innovation/efficiency/practice/community）
    2. 根据分类从接口获取奖项列表（GET /api/awards?category=xxx）
    3. 选择奖项名称（只能选择该分类下已配置的奖项）
    4. 选择获奖时间（年月格式：YYYY-MM）
    5. 提交设置（系统自动从获奖时间中提取年份）
  - 奖项名称必须从管理后台配置的奖项列表中选择，只有配置了的奖项才能被选择
  - 获奖时间使用年月格式，系统会自动提取年份用于展示和统计

### 20.1. 通过已添加的奖项获取获奖者可选奖项（通过已添加的奖项获取获奖者可选奖项的接口）
- **接口**: `GET /api/awards?category=xxx`
- **说明**: 根据奖项分类获取可选奖项列表，用于获奖者推荐时选择奖项
- **查询参数**:
  - `category`: "innovation" | "efficiency" | "practice" | "community" (必填，奖项分类)
- **响应数据**: 同"获取奖项列表"
- **使用页面**: `AdminView.vue` (管理后台-AI使用达人管理-获奖者推荐-选择奖项)
- **说明**: 
  - 只返回指定分类下已配置的奖项
  - 用于获奖者推荐时，管理员只能从已配置的奖项中选择

### 21. 取消用户获奖
- **接口**: `DELETE /api/admin/honors/:id`
- **说明**: 删除已设置的获奖记录
- **使用页面**: `AdminView.vue` (管理后台-AI使用达人管理-获奖者推荐)

### 22. 获取个人奖项配置
- **接口**: `GET /api/admin/users/awards`
- **说明**: 获取个人奖项配置列表，用于管理后台编辑
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "description": "string",
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (AI使用达人管理-个人奖项设置-奖项设置)

### 23. 保存个人奖项配置
- **接口**: `PUT /api/admin/users/awards`
- **请求参数**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "description": "string",
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (AI使用达人管理-个人奖项设置-奖项设置)

### 24. 获取获奖者列表
- **接口**: `GET /api/admin/users/winners`
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "category": "innovation" | "efficiency" | "practice" | "community",
        "awardName": "string",
        "awardTime": "string" // YYYY-MM
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (AI使用达人管理-个人奖项设置-获奖者管理)

### 25. 保存获奖者列表
- **接口**: `PUT /api/admin/users/winners`
- **请求参数**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "category": "innovation" | "efficiency" | "practice" | "community",
        "awardName": "string",
        "awardTime": "string" // YYYY-MM
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (AI使用达人管理-个人奖项设置-获奖者管理)

### 26. 获取团队奖项配置
- **接口**: `GET /api/admin/users/team-awards`
- **说明**: 获取团队奖项配置列表，用于管理后台编辑
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "title": "string",
        "year": "number",
        "images": [
          {
            "id": "number",
            "image": "string",
            "winnerName": "string"
          }
        ],
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (AI使用达人管理-团队奖项设置)

### 27. 保存团队奖项配置
- **接口**: `PUT /api/admin/users/team-awards`
- **请求参数**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "title": "string",
        "year": "number",
        "images": [
          {
            "id": "number",
            "image": "string",
            "winnerName": "string"
          }
        ],
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (AI使用达人管理-团队奖项设置)

### 28. 上传图片
- **接口**: `POST /api/admin/upload/image`
- **请求类型**: `multipart/form-data`
- **请求参数**: `file` (File)
- **响应数据**:
  ```json
  {
    "url": "string"
  }
  ```
- **使用页面**: `AdminView.vue`, `PostCreateView.vue`, `ActivityCreateView.vue`

---

## 数据交互流程

### 1. 首页数据加载流程
```
首页加载
  ├── 获取轮播图配置 (GET /api/home/carousel) ← 管理后台配置
  ├── 获取荣誉殿堂 (GET /api/home/honor) ← 管理后台配置 + 动态数据
  │   ├── 荣誉殿堂Banner图片
  │   ├── 奖项列表
  │   └── AI使用达人Top用户（动态计算）
  ├── 获取工具列表 (GET /api/tools) ← 管理后台配置
  │   ├── 工具列表（名称、描述、Logo、颜色、路由）
  │   └── 工具Banner图
  ├── 获取AI优秀实践帖子 (GET /api/posts?zone=practices) ← 接口动态数据
  └── 获取赋能交流帖子 (GET /api/posts?zone=empowerment) ← 接口动态数据

工具专区页面加载
  ├── 获取工具列表 (GET /api/tools) ← 管理后台配置
  │   └── 用于显示工具筛选按钮（除了"其他工具"）
  ├── 获取工具详情 (GET /api/tools/:id) ← 管理后台配置（选中工具时）
  │   └── 获取工具Banner图
  ├── 检查是否为工具Owner (GET /api/tools/:id/owner) ← 权限检查
  │   └── 如果是Owner，显示发布活动悬浮按钮
  └── 获取工具相关帖子 (GET /api/posts?zone=tools&toolId=xxx) ← 接口动态数据

扶摇Agent应用页面加载
  ├── 获取置顶帖子 (GET /api/agent/featured-post) ← 管理后台配置
  ├── 检查是否为扶摇Agent应用Owner (GET /api/tools/-1/owner) ← 权限检查
  │   └── 如果是Owner，显示发布活动悬浮按钮
  └── 获取帖子列表 (GET /api/posts?zone=agent) ← 接口动态数据

发帖页面加载
  ├── 获取工具列表 (GET /api/tools) ← 管理后台配置
  │   └── 当选择"AI工具专区"时，显示工具选择下拉框
  ├── 获取标签列表 (GET /api/tags?zone=xxx&toolId=xxx) ← 接口动态数据
  │   └── 根据选择的专区和工具，返回对应的标签列表
  └── 获取推荐封面列表 (GET /api/posts/recommended-covers) ← 管理后台配置

发布活动页面加载
  ├── 获取工具列表 (GET /api/tools) ← 管理后台配置
  │   └── 显示工具选择下拉框
  └── 如果URL中有toolId参数，自动选中对应工具
```

### 2. 管理后台配置流程
```
管理员在后台配置
  ├── 配置首页轮播图 (PUT /api/admin/home/carousel)
  │   └── 首页读取 (GET /api/home/carousel)
  ├── 配置荣誉殿堂 (PUT /api/admin/home/honor-banner, PUT /api/admin/home/honor-awards)
  ├── 配置AI工具 (PUT /api/admin/tools)
  │   ├── 配置工具列表、路由、Banner
  │   ├── 工具列表用于：
  │   │   ├── 首页工具专区展示
  │   │   ├── 工具专区页面筛选按钮（除了"其他工具"）
  │   │   ├── 发帖页面工具类别选择
  │   │   └── 发布活动页面工具选择
  │   └── 首页和工具页面读取 (GET /api/tools)
  ├── 配置扶摇Agent应用置顶帖子 (PUT /api/admin/agent/featured-post)
  │   └── 扶摇Agent应用页面读取 (GET /api/agent/featured-post) - 返回包含封面图
  └── 配置推荐封面 (PUT /api/admin/posts/recommended-covers)
      └── 发帖页面读取 (GET /api/posts/recommended-covers)

AI使用达人管理流程
  ├── 获取本月积分排行榜 (GET /api/admin/honors/recommended-winners)
  │   └── 推荐本月积分Top 3用户（排除管理员）
  ├── 管理员查看推荐用户信息
  │   ├── 用户基本信息
  │   ├── 本月积分明细
  │   └── 是否已评奖状态
  └── 管理员设置评奖 (POST /api/admin/honors)
      └── 设置后用户出现在荣誉墙和时光轴中
```

### 3. 用户登录流程
```
用户输入账号密码 
  → POST /api/auth/login 
  → 返回token和用户信息 
  → 存储token到localStorage 
  → 更新全局用户状态 
  → 跳转到首页
```

### 4. 帖子发布流程
```
用户在PostCreateView填写表单 
  → 获取推荐封面列表 (GET /api/posts/recommended-covers)
  → 获取工具列表 (GET /api/tools) - 如果选择AI工具专区
  → 获取标签列表 (GET /api/tags?zone=xxx&toolId=xxx)
  → 上传封面图片 (POST /api/admin/upload/image) 
  → 提交帖子 (POST /api/posts) 
  → 后端计算积分（发布帖子+15，管理员除外）
  → 返回帖子ID 
  → 跳转到帖子详情页或返回列表页
```

### 5. 评论交互流程
```
用户在PostDetailView查看帖子 
  → GET /api/posts/:id 获取帖子详情 
  → GET /api/posts/:id/comments 获取评论列表 
  → 用户发表评论 (POST /api/posts/:id/comments) 
  → 后端创建评论并发送消息通知 
  → 前端刷新评论列表 
  → 如果被评论用户在线，通过WebSocket推送消息
```

### 6. 活动报名流程
```
用户在ActivityDetailView查看活动 
  → GET /api/activities/:id 获取活动详情 
  → 用户点击报名 (POST /api/activities/:id/register) 
  → 后端创建报名记录并发送消息通知 
  → 后端计算积分（参加活动+10，管理员除外）
  → 更新活动详情中的isRegistered状态 
  → 如果活动发布者在线，通过WebSocket推送消息
```

### 6.1. 发布活动流程（工具Owner）
```
工具Owner在ToolsView或AgentView页面
  → 检查是否为工具Owner (GET /api/tools/:id/owner)
  → 如果isOwner为true，显示发布活动悬浮按钮
  → 点击发布活动按钮
  → 跳转到活动创建页面 (/activity/create?toolId=xxx)
  → 获取工具列表 (GET /api/tools) - 用于选择工具
  → 填写活动信息并提交 (POST /api/activities)
  → 返回活动ID
  → 跳转到活动详情页
```

### 6.2. 获奖者推荐流程（管理员）
```
管理员在管理后台-AI使用达人管理页面
  → 获取本月积分排行榜 (GET /api/admin/honors/recommended-winners)
  │   └── 返回本月积分Top 3用户（排除管理员）
  → 管理员查看推荐用户信息
  │   ├── 用户基本信息（姓名、部门、工号）
  │   ├── 本月积分
  │   ├── 本月发帖数、评论数、参加活动数
  │   └── 是否已评奖状态
  → 管理员选择是否给推荐用户评奖
  │   ├── 如果选择评奖：
  │   │   ├── 选择奖项 (从奖项列表中选择)
  │   │   ├── 设置获奖日期和年份
  │   │   └── 提交评奖 (POST /api/admin/honors)
  │   └── 如果取消评奖：
  │       └── 删除获奖记录 (DELETE /api/admin/honors/:id)
  → 评奖成功后，用户出现在荣誉墙和时光轴中
```

### 7. 荣誉送花流程
```
用户在UsersView查看荣誉墙 
  → GET /api/honors 获取荣誉列表 
  → 用户点击送花 (POST /api/honors/:id/flower) 
  → 后端更新花朵数 
  → 返回更新后的花朵数 
  → 前端更新UI显示
```

### 8. 消息通知流程
```
后端触发事件 (评论、点赞、报名等) 
  → 创建消息记录 
  → 通过WebSocket推送消息给用户 
  → 前端AppNavbar接收消息 
  → 更新未读消息数量 
  → 用户点击消息图标 
  → 跳转到MessageListView 
  → GET /api/messages 获取消息列表 
  → 用户点击消息 
  → PUT /api/messages/:id/read 标记已读 
  → 跳转到相关页面
```

### 9. 数据同步机制

#### 实时更新
- **WebSocket连接**: 用于实时推送消息、点赞数变化等
- **连接地址**: `ws://your-domain/api/ws`
- **消息格式**:
  ```json
  {
    "type": "message" | "like" | "comment" | "activity",
    "data": {}
  }
  ```

#### 轮询更新
- **未读消息数**: 每30秒轮询一次 `GET /api/messages/unread-count`
- **帖子统计数据**: 在PostDetailView中每60秒刷新一次

#### 本地存储
- **草稿保存**: 帖子/活动创建时自动保存到localStorage
- **用户偏好**: 标签筛选、排序方式等保存到localStorage
- **收藏列表**: 从localStorage读取（实际应从API获取）

### 10. 数据依赖关系

```
用户信息
  ├── 影响: 帖子作者信息、评论作者信息、活动报名状态
  └── 依赖: 无

帖子
  ├── 影响: 评论、点赞、收藏、消息通知
  └── 依赖: 用户信息、标签、工具信息

活动
  ├── 影响: 报名记录、消息通知
  └── 依赖: 用户信息、工具信息

荣誉
  ├── 影响: 排行榜、用户花朵数
  └── 依赖: 用户信息、奖项信息

消息
  ├── 影响: 未读消息数
  └── 依赖: 用户信息、帖子、活动、荣誉
```

### 11. 缓存策略

#### 前端缓存
- **首页数据**: 缓存5分钟
- **工具列表**: 缓存10分钟
- **标签列表**: 缓存30分钟
- **部门统计**: 缓存10分钟

#### 后端缓存建议
- **热门帖子**: Redis缓存，5分钟过期
- **排行榜数据**: Redis缓存，10分钟过期
- **用户信息**: Redis缓存，30分钟过期

---

## 接口规范说明

### 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2024-01-01T00:00:00Z"
}
```

### 错误码规范
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未授权，需要登录
- `403`: 无权限
- `404`: 资源不存在
- `500`: 服务器错误

### 分页参数
- `page`: 页码，从1开始
- `pageSize`: 每页数量，默认10，最大100

### 时间格式
- 统一使用 ISO 8601 格式: `YYYY-MM-DDTHH:mm:ssZ`
- 显示时前端转换为本地时间格式

---

## 积分系统

### 积分规则
用户通过以下行为获得积分（管理员除外）：

1. **发布帖子**: +15 积分
2. **参加活动**: +10 积分
3. **帖子被收藏**: +5 积分
4. **帖子被点赞**: +3 积分
5. **发表评论**: +1 积分
6. **评论被点赞**: +1 积分

### 积分计算接口
- **接口**: `GET /api/user/points/calculate`
- **说明**: 计算用户当前积分（用于显示和验证）
- **响应数据**:
  ```json
  {
    "points": "number",
    "breakdown": {
      "posts": "number", // 发帖获得的积分
      "comments": "number", // 评论获得的积分
      "likesReceived": "number", // 被点赞获得的积分
      "favoritesReceived": "number", // 被收藏获得的积分
      "activities": "number" // 参加活动获得的积分
    }
  }
  ```
- **使用页面**: `AppNavbar.vue`, `ProfileView.vue`

### 积分更新时机
- 发布帖子成功后自动+15积分
- 参加活动成功后自动+10积分
- 发表评论成功后自动+1积分
- 帖子被点赞时自动+3积分
- 帖子被收藏时自动+5积分
- 评论被点赞时自动+1积分

### 权限说明
- **管理员**: 管理员的所有操作不计算积分
- **工具Owner**: 工具Owner的积分计算规则与普通用户相同

### 月度积分统计
- 系统按月统计用户积分
- 每月1日重置月度积分统计
- 用于获奖者推荐功能，推荐本月积分Top 3用户

---

## 用户角色与权限

### 角色类型
1. **user** - 普通用户（默认角色）
2. **admin** - 管理员（可以访问管理后台，配置所有内容）
3. **owner** - 工具Owner（可以发布指定工具的活动）

### 权限说明

#### 管理员 (admin)
- 可以访问管理后台
- 可以配置首页内容、荣誉殿堂、工具配置等
- 可以管理用户角色（添加/移除管理员、工具Owner）
- 所有操作不计算积分

#### 工具Owner (owner)
- 可以在指定工具的工具专区页面和扶摇Agent应用页面看到发布活动按钮
- 可以发布该工具相关的活动
- 积分计算规则与普通用户相同
- 一个用户可以是多个工具的Owner

#### 普通用户 (user)
- 可以发布帖子、评论、参加活动等
- 所有操作正常计算积分

---

## 注意事项

1. **认证**: 除公开接口外，所有接口都需要在Header中携带token
   ```
   Authorization: Bearer <token>
   ```

2. **文件上传**: 图片上传需要先调用上传接口获取URL，再使用URL创建帖子/活动

3. **实时性**: 点赞、评论等操作建议使用WebSocket实时推送，避免频繁轮询

4. **性能优化**: 
   - 列表接口支持分页，避免一次性加载大量数据
   - 图片使用CDN加速
   - 使用懒加载优化长列表

5. **数据一致性**: 
   - 点赞、收藏等操作需要检查重复操作
   - 删除操作需要检查权限和关联数据

6. **权限控制**:
   - 发布活动按钮在工具Owner或管理员访问对应工具页面时显示（悬浮按钮形式）
   - 管理后台的人员管理需要管理员权限
   - 积分计算时排除管理员用户
   - 工具Owner可以通过工号搜索添加

7. **发布活动按钮位置**:
   - 从管理后台移除发布活动按钮
   - 在AI工具专区页面（ToolsView）和扶摇Agent应用页面（AgentView）以悬浮按钮形式显示
   - 当前工具Owner或管理员可以看到并点击该按钮
   - 按钮位置：页面右下角（类似PostFab组件的位置）

8. **获奖时间格式**:
   - 设置评奖和获奖者管理中的获奖时间统一使用年月格式（YYYY-MM）
   - 系统会自动从获奖时间中提取年份用于展示和统计
