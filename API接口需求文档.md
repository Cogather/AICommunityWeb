# AI社区Web前端接口需求文档

## 重要说明：数据来源分类

### 管理后台配置数据（静态配置）
以下数据由管理员在管理后台配置，前端通过接口读取显示：

1. **首页轮播图** - 管理员在后台配置轮播图内容、图片、链接等
2. **荣誉殿堂** - 管理员在后台配置Banner图片和奖项列表
   - **注意**: AI使用达人（Top用户）由系统根据用户荣誉数量动态计算，无需配置
3. **社区头条** - 管理员在后台配置头条内容
4. **AI工具配置** - 管理员在后台配置：
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

## 目录
1. [用户认证与个人信息](#用户认证与个人信息)
2. [首页](#首页)
3. [帖子相关](#帖子相关)
4. [活动相关](#活动相关)
5. [荣誉系统](#荣誉系统)
6. [消息系统](#消息系统)
7. [工具专区](#工具专区)
8. [管理后台](#管理后台)
9. [数据交互流程](#数据交互流程)
10. [积分系统](#积分系统)
11. [用户角色与权限](#用户角色与权限)

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

### 3. 获取当前用户信息
- **接口**: `GET /api/user/current`
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
    "points": "number",
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

### 4. 获取用户个人资料
- **接口**: `GET /api/user/:userId/profile`
- **查询参数**: `userId` (可选，不传则获取当前用户)
- **响应数据**: 同"获取当前用户信息"
- **使用页面**: `ProfileView.vue`

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
> 1. **管理后台配置数据**：轮播图、荣誉殿堂、工具配置、社区头条 - 这些数据由管理员在后台配置，前端通过接口读取
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

### 2. 获取荣誉殿堂和社区头条信息（管理后台配置）
- **接口**: `GET /api/home/honor`
- **说明**: 一次性获取荣誉殿堂模块和社区头条的所有数据，包括：
  - 荣誉殿堂Banner图片
  - 奖项列表
  - AI使用达人（Top用户）
  - 社区头条列表
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
    },
    "news": {
      "list": [
        {
          "id": "number",
          "title": "string",
          "image": "string",
          "date": "string",
          "link": "string",
          "order": "number"
        }
      ]
    }
  }
  ```
- **使用页面**: `HomeView.vue`
- **配置页面**: 
  - `AdminView.vue` (首页管理-荣誉殿堂Banner配置)
  - `AdminView.vue` (首页管理-荣誉殿堂奖项配置)
  - `AdminView.vue` (首页管理-社区头条配置)
- **数据说明**:
  - `honor.bannerImage`: 荣誉殿堂Banner图片URL（管理后台配置）
  - `honor.awards`: 奖项列表（管理后台配置）
  - `honor.topUsers`: AI使用达人Top用户列表（动态计算，根据用户荣誉数量排序）
  - `news.list`: 社区头条列表（管理后台配置，默认返回前4条）

### 3. 获取AI工具列表（管理后台配置）
- **接口**: `GET /api/tools`
- **查询参数**: `featured` (boolean, 是否只获取推荐工具)
- **说明**: 工具列表、工具路由、工具Banner都由管理后台配置
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

### 4. 获取AI优秀实践帖子列表（接口动态数据）
- **接口**: `GET /api/posts`
- **查询参数**: 
  - `zone`: "practices"
  - `limit`: number (默认6)
  - `sort`: "newest" | "hot"
- **说明**: 动态获取AI优秀实践专区的帖子列表
- **响应数据**: 见"帖子相关-获取帖子列表"
- **使用页面**: `HomeView.vue` (AI优秀实践模块)

### 5. 获取赋能交流帖子列表（接口动态数据）
- **接口**: `GET /api/posts`
- **查询参数**: 
  - `zone`: "empowerment"
  - `limit`: number (默认6)
  - `sort`: "newest" | "hot"
- **说明**: 动态获取赋能交流专区的帖子列表
- **响应数据**: 见"帖子相关-获取帖子列表"
- **使用页面**: `HomeView.vue` (赋能交流模块)

### 6. 获取扶摇Agent应用置顶帖子（管理后台配置）
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

### 1. 获取帖子列表
- **接口**: `GET /api/posts`
- **查询参数**:
  - `zone`: "practices" | "tools" | "agent" | "empowerment" | "other"
  - `toolId`: number (工具ID，可选)
  - `tag`: string (标签，可选)
  - `department`: string (部门，可选)
  - `author`: string (作者，可选)
  - `sort`: "newest" | "hot" | "comments" | "likes"
  - `page`: number
  - `pageSize`: number
  - `search`: string (搜索关键词)
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

### 2. 获取帖子详情
- **接口**: `GET /api/posts/:id`
- **响应数据**:
  ```json
  {
    "id": "number",
    "title": "string",
    "content": "string",
    "authorName": "string",
    "authorAvatar": "string",
    "createTime": "string",
    "views": "number",
    "likes": "number",
    "tags": ["string"],
    "cover": "string",
    "isLiked": "boolean",
    "isCollected": "boolean",
    "isAuthor": "boolean",
    "canEdit": "boolean",
    "zone": "string",
    "toolId": "number",
    "toolName": "string"
  }
  ```
- **使用页面**: `PostDetailView.vue`

### 3. 创建帖子
- **接口**: `POST /api/posts`
- **请求参数**:
  ```json
  {
    "title": "string",
    "summary": "string", // 内容简介
    "content": "string",
    "tags": ["string"],
    "cover": "string",
    "zone": "practices" | "tools" | "agent" | "empowerment",
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
- **使用页面**: `PostCreateView.vue`
- **数据依赖**:
  - 工具列表: 通过 `GET /api/tools` 获取（当zone为tools时）
  - 标签列表: 通过 `GET /api/tags?zone=xxx&toolId=xxx` 获取
  - 推荐封面: 通过 `GET /api/posts/recommended-covers` 获取

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

### 4. 更新帖子
- **接口**: `PUT /api/posts/:id`
- **请求参数**: 同"创建帖子"
- **使用页面**: `PostCreateView.vue`, `PostDetailView.vue`

### 5. 删除帖子
- **接口**: `DELETE /api/posts/:id`
- **使用页面**: `PostDetailView.vue`

### 6. 点赞/取消点赞帖子
- **接口**: `POST /api/posts/:id/like`
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
- **使用页面**: `PostDetailView.vue`

### 7. 收藏/取消收藏帖子
- **接口**: `POST /api/posts/:id/collect`
- **请求参数**:
  ```json
  {
    "action": "collect" | "uncollect"
  }
  ```
- **响应数据**:
  ```json
  {
    "collected": "boolean"
  }
  ```
- **使用页面**: `PostDetailView.vue`, `ProfileView.vue`

### 8. 获取帖子评论列表
- **接口**: `GET /api/posts/:id/comments`
- **查询参数**: `page`, `pageSize`
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "userName": "string",
        "userAvatar": "string",
        "content": "string",
        "createTime": "string",
        "likes": "number",
        "isLiked": "boolean",
        "isAuthor": "boolean",
        "replies": [
          {
            "id": "number",
            "userName": "string",
            "userAvatar": "string",
            "content": "string",
            "createTime": "string",
            "replyTo": "string"
          }
        ]
      }
    ],
    "total": "number"
  }
  ```
- **使用页面**: `PostDetailView.vue`

### 9. 发表评论
- **接口**: `POST /api/posts/:id/comments`
- **请求参数**:
  ```json
  {
    "content": "string",
    "replyTo": "number" // 可选，回复的评论ID
  }
  ```
- **响应数据**: 评论对象
- **使用页面**: `PostDetailView.vue`

### 10. 点赞评论
- **接口**: `POST /api/comments/:id/like`
- **请求参数**:
  ```json
  {
    "action": "like" | "unlike"
  }
  ```
- **使用页面**: `PostDetailView.vue`

### 11. 获取用户发布的帖子
- **接口**: `GET /api/user/:userId/posts`
- **查询参数**: `page`, `pageSize`
- **响应数据**: 同"获取帖子列表"
- **使用页面**: `ProfileView.vue`

### 12. 获取用户收藏的帖子
- **接口**: `GET /api/user/:userId/favorites`
- **查询参数**: `page`, `pageSize`
- **响应数据**: 同"获取帖子列表"
- **使用页面**: `ProfileView.vue`

### 13. 获取用户评论列表
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

### 14. 获取标签列表
- **接口**: `GET /api/tags`
- **查询参数**: 
  - `zone`: "practices" | "tools" | "agent" | "empowerment" (可选，按专区获取标签)
  - `toolId`: number (可选，当zone为tools时，指定工具ID获取该工具的标签)
- **说明**: 
  - 不同专区有不同的标签列表
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

### 15. 获取部门统计
- **接口**: `GET /api/departments/stats`
- **查询参数**: `zone` (可选)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "postCount": "number",
        "contributorCount": "number"
      }
    ]
  }
  ```
- **使用页面**: `PracticesView.vue`, `ToolsView.vue`

### 16. 获取热门贡献者
- **接口**: `GET /api/users/top-contributors`
- **查询参数**: `zone` (可选), `limit` (默认10)
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "avatar": "string",
        "postCount": "number"
      }
    ]
  }
  ```
- **使用页面**: `PracticesView.vue`

### 17. 获取热门帖子
- **接口**: `GET /api/posts/hot`
- **查询参数**: `zone` (可选), `limit` (默认10)
- **响应数据**: 同"获取帖子列表"
- **使用页面**: `PracticesView.vue`

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

### 2. 获取活动详情
- **接口**: `GET /api/activities/:id`
- **响应数据**:
  ```json
  {
    "id": "number",
    "title": "string",
    "toolId": "number",
    "toolName": "string",
    "type": "string",
    "date": "string",
    "cover": "string",
    "content": "string",
    "authorId": "number",
    "status": "string",
    "isRegistered": "boolean",
    "registeredCount": "number"
  }
  ```
- **使用页面**: `ActivityDetailView.vue`

### 3. 创建活动
- **接口**: `POST /api/activities`
- **请求参数**:
  ```json
  {
    "title": "string",
    "toolId": "number", // 工具ID（从 GET /api/tools 获取，可选，-1表示扶摇Agent应用）
    "type": "training" | "competition" | "sharing",
    "date": "string",
    "cover": "string",
    "content": "string"
  }
  ```
- **使用页面**: `ActivityCreateView.vue`
- **数据依赖**:
  - 工具列表: 通过 `GET /api/tools` 获取（用于选择活动关联的工具）

### 4. 更新活动
- **接口**: `PUT /api/activities/:id`
- **请求参数**: 同"创建活动"
- **使用页面**: `ActivityCreateView.vue`, `ActivityDetailView.vue`

### 5. 删除活动
- **接口**: `DELETE /api/activities/:id`
- **使用页面**: `ActivityDetailView.vue`

### 6. 报名活动
- **接口**: `POST /api/activities/:id/register`
- **响应数据**:
  ```json
  {
    "success": "boolean",
    "message": "string"
  }
  ```
- **使用页面**: `ActivityDetailView.vue`
- **积分规则**: 报名成功后，用户积分+10（管理员除外）

### 6.1. 检查用户是否为工具Owner
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
- **查询参数**: `page`, `pageSize`
- **响应数据**: 同"获取活动列表"
- **使用页面**: `ProfileView.vue`

---

## 荣誉系统

> **重要说明**: 荣誉系统包括：
> - 用户荣誉记录（获奖记录）
> - 荣誉排行榜
> - 获奖者推荐（管理员功能）

### 1. 获取荣誉列表
- **接口**: `GET /api/honors`
- **查询参数**:
  - `scope`: "all" | "mine"
  - `view`: "grid" | "timeline"
  - `user`: string (用户名，查看特定用户的时光轴)
  - `filterType`: "category" | "award" | "department"
  - `filterValue`: string (筛选值)
  - `search`: string (搜索用户名)
  - `page`: number
  - `pageSize`: number
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
- **使用页面**: `UsersView.vue`

### 2. 获取荣誉排行榜
- **接口**: `GET /api/honors/leaderboard`
- **查询参数**: `limit` (默认10)
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

### 4. 获取奖项列表
- **接口**: `GET /api/awards`
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "name": "string",
        "desc": "string",
        "image": "string",
        "category": "string",
        "rules": "string"
      }
    ]
  }
  ```
- **使用页面**: `HomeView.vue`, `AwardRulesView.vue`

### 5. 获取奖项规则详情
- **接口**: `GET /api/awards/:id/rules`
- **查询参数**: `award` (奖项名称，可选)
- **响应数据**:
  ```json
  {
    "id": "number",
    "name": "string",
    "rules": "string",
    "category": "string",
    "image": "string"
  }
  ```
- **使用页面**: `AwardRulesView.vue`


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

## 工具专区

### 1. 获取工具详情
- **接口**: `GET /api/tools/:id`
- **说明**: 工具信息（包括Banner）由管理后台配置，这里读取配置数据
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

### 2. 获取工具相关活动
- **接口**: `GET /api/tools/:id/activities`
- **查询参数**: `limit` (默认3)
- **说明**: 动态获取该工具相关的活动列表
- **响应数据**: 同"活动相关-获取活动列表"
- **使用页面**: `ToolsView.vue`

### 3. 获取工具列表（用于筛选和选择）
- **接口**: `GET /api/tools`
- **说明**: 
  - 用于工具专区页面的工具筛选按钮（除了"其他工具"按钮）
  - 用于发帖页面选择工具类别
  - 用于发布活动页面选择工具
- **响应数据**: 见"首页-获取AI工具列表"
- **使用页面**: 
  - `ToolsView.vue` - 工具筛选按钮
  - `PostCreateView.vue` - 工具类别选择
  - `ActivityCreateView.vue` - 工具选择
- **注意**: 
  - 工具专区页面会显示所有工具按钮（来自此接口）+ "其他工具"按钮（前端固定）
  - 发帖和发布活动页面会显示所有工具选项供选择

---

## 管理后台

> **重要说明**: 管理后台用于配置首页的静态内容，包括轮播图、荣誉殿堂、工具配置、社区头条等。这些配置数据会被首页读取并显示。

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

### 7. 获取社区头条配置
- **接口**: `GET /api/admin/home/news`
- **响应数据**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "title": "string",
        "image": "string",
        "date": "string",
        "link": "string",
        "order": "number"
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (首页管理-社区头条配置)

### 8. 保存社区头条配置
- **接口**: `PUT /api/admin/home/news`
- **请求参数**:
  ```json
  {
    "list": [
      {
        "id": "number",
        "title": "string",
        "image": "string",
        "date": "string",
        "link": "string",
        "order": "number"
      }
    ]
  }
  ```
- **说明**: 保存后，首页通过 `GET /api/home/honor` 统一读取（与荣誉殿堂数据一起返回）
- **使用页面**: `AdminView.vue` (首页管理-社区头条配置)

### 9. 获取AI工具配置
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

### 12. 保存扶摇Agent应用置顶帖子配置
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

### 13. 获取推荐封面配置
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

### 14. 保存推荐封面配置
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

### 15. 搜索用户（通过工号）
- **接口**: `GET /api/admin/users/search`
- **查询参数**:
  - `employeeId`: string (工号)
  - `name`: string (姓名，可选)
  - `email`: string (邮箱，可选)
- **说明**: 通过工号搜索用户，用于人员管理添加用户
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
        "avatar": "string"
      }
    ]
  }
  ```
- **使用页面**: `AdminView.vue` (管理后台-人员管理)

### 16. 获取用户列表
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

### 17. 添加管理员
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

### 18. 添加工具Owner
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

### 19. 移除用户角色
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

### 20. 获取本月积分排行榜（获奖者推荐）
- **接口**: `GET /api/admin/honors/recommended-winners`
- **查询参数**: 
  - `month`: string (可选，格式：YYYY-MM，默认当前月份)
  - `limit`: number (默认3，推荐Top 3用户)
- **说明**: 获取本月积分靠前的用户，用于管理员推荐评奖
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

### 21. 设置用户获奖
- **接口**: `POST /api/admin/honors`
- **请求参数**:
  ```json
  {
    "userId": "number",
    "awardId": "number", // 奖项ID（从奖项列表中选择）
    "awardName": "string", // 奖项名称
    "awardDate": "string", // 获奖日期，格式：YYYY-MM-DD
    "category": "innovation" | "efficiency" | "practice" | "community",
    "year": "string" // 年份，格式：YYYY
  }
  ```
- **响应数据**:
  ```json
  {
    "id": "number",
    "message": "string"
  }
  ```
- **使用页面**: `AdminView.vue` (管理后台-AI使用达人管理-获奖者推荐)
- **说明**: 
  - 管理员可以为推荐的用户设置获奖记录
  - 设置后，该用户会出现在荣誉墙和时光轴中
  - 可以设置是否评奖（hasAwarded字段）

### 22. 取消用户获奖
- **接口**: `DELETE /api/admin/honors/:id`
- **说明**: 删除已设置的获奖记录
- **使用页面**: `AdminView.vue` (管理后台-AI使用达人管理-获奖者推荐)

### 23. 上传图片
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
  ├── 获取荣誉殿堂和社区头条 (GET /api/home/honor) ← 管理后台配置 + 动态数据
  │   ├── 荣誉殿堂Banner图片
  │   ├── 奖项列表
  │   ├── AI使用达人Top用户（动态计算）
  │   └── 社区头条列表
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
  ├── 配置社区头条 (PUT /api/admin/home/news)
  │   └── 首页统一读取 (GET /api/home/honor) - 同时返回荣誉殿堂和社区头条数据
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
  → 后端计算积分（发布帖子+20，管理员除外）
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

1. **发布帖子**: +20 积分
2. **发表评论**: +1 积分
3. **帖子被点赞**: +3 积分
4. **帖子被收藏**: +5 积分
5. **参加活动**: +10 积分

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
- 发布帖子成功后自动+20积分
- 参加活动成功后自动+10积分
- 发表评论成功后自动+1积分
- 帖子被点赞时自动+3积分
- 帖子被收藏时自动+5积分

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
   - 发布活动按钮只在工具Owner访问对应工具页面时显示（悬浮按钮形式）
   - 管理后台的人员管理需要管理员权限
   - 积分计算时排除管理员用户
   - 工具Owner可以通过工号搜索添加

7. **发布活动按钮位置**:
   - 从管理后台移除发布活动按钮
   - 在AI工具专区页面（ToolsView）和扶摇Agent应用页面（AgentView）以悬浮按钮形式显示
   - 只有当前工具Owner可以看到并点击该按钮
   - 按钮位置：页面右下角（类似PostFab组件的位置）
