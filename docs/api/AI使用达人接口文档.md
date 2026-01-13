# AI使用达人页面接口文档

## 概述

AI使用达人页面（荣誉殿堂）需要加载以下数据模块：
1. 个人荣誉列表（荣誉墙/时光轴视图）
2. 团队奖项列表
3. 荣誉影响力排行榜
4. 送花功能
5. 奖项名称列表（筛选用）
6. 部门列表（筛选用）

---

## 1. 获取个人荣誉列表

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/honor/list` |
| 接口描述 | 获取个人荣誉列表，支持分页、筛选和视图切换 |
| 权限要求 | 无需登录（查看"我的"需登录） |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | number | 否 | 当前页码，默认1 |
| pageSize | number | 否 | 每页数量，默认16，可选10/12/20/30/50 |
| scope | string | 否 | 筛选范围：all（全员）/mine（我的），默认all |
| filterType | string | 否 | 筛选类型：award（按奖项）/department（按部门） |
| filterValue | string | 否 | 筛选值（奖项名称或部门名称） |
| keyword | string | 否 | 搜索关键词（匹配用户姓名） |
| view | string | 否 | 视图模式：grid（荣誉墙）/timeline（时光轴） |
| userName | string | 否 | 时光轴模式下，指定用户姓名查看其荣誉历程 |

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "name": "林星辰",
        "department": "架构平台部",
        "avatar": "https://example.com/avatar/linxc.jpg",
        "awardName": "2026年度 AI 技术突破奖",
        "awardDate": "2026-01-05",
        "category": "innovation",
        "year": "2026",
        "isMine": true,
        "flowers": 12,
        "hasGivenFlower": false,
        "achievement": "在AI模型优化领域取得重大突破，成功将模型推理速度提升300%。"
      },
      {
        "id": 2,
        "name": "Sarah",
        "department": "UED 设计中心",
        "avatar": "https://example.com/avatar/sarah.jpg",
        "awardName": "最佳 AI 辅助设计实践",
        "awardDate": "2025-12-20",
        "category": "practice",
        "year": "2025",
        "isMine": false,
        "flowers": 15,
        "hasGivenFlower": false,
        "achievement": "创新性地将AI技术应用于设计工作流程。"
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 16,
    "totalPages": 7
  }
}
```

### 字段说明

**荣誉对象字段：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 荣誉记录ID |
| name | string | 是 | 获奖者姓名 |
| department | string | 是 | 获奖者部门 |
| avatar | string | 否 | 获奖者头像URL |
| awardName | string | 是 | 奖项名称 |
| awardDate | string | 是 | 获奖日期（YYYY-MM-DD格式） |
| category | string | 是 | 奖项类别：innovation（创新）/efficiency（效能）/practice（实践）/community（社区） |
| year | string | 是 | 获奖年份 |
| isMine | boolean | 是 | 是否为当前登录用户的荣誉 |
| flowers | number | 是 | 收到的花朵数 |
| hasGivenFlower | boolean | 是 | 当前用户是否已送花 |
| achievement | string | 否 | 获奖成就描述 |

---

## 2. 获取团队奖项列表

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/honor/team-awards` |
| 接口描述 | 获取团队荣誉奖项列表 |
| 权限要求 | 无需登录 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| year | string | 否 | 按年份筛选 |

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "title": "年度最佳AI创新团队",
        "year": "2026",
        "images": [
          {
            "id": 1,
            "image": "https://example.com/team/ai-team.jpg",
            "imageType": "url",
            "winnerName": "架构平台部AI团队",
            "teamField": "AI技术研发",
            "story": "<p><strong>获奖事迹：</strong></p><p>团队成功研发了多项核心AI技术...</p>",
            "flowers": 15,
            "hasGivenFlower": false
          },
          {
            "id": 2,
            "image": "https://example.com/team/efficiency-team.jpg",
            "imageType": "url",
            "winnerName": "效能工程部",
            "teamField": "工程效能",
            "story": "<p><strong>获奖事迹：</strong></p><p>效能工程部在提升研发效率方面...</p>",
            "flowers": 12,
            "hasGivenFlower": false
          }
        ]
      },
      {
        "id": 2,
        "title": "AI效能提升先锋",
        "year": "2026",
        "images": [
          {
            "id": 3,
            "image": "https://example.com/team/ued-team.jpg",
            "imageType": "url",
            "winnerName": "UED 设计中心",
            "teamField": "设计创新",
            "flowers": 18,
            "hasGivenFlower": false
          }
        ]
      }
    ],
    "years": ["2026", "2025", "2024"]
  }
}
```

### 字段说明

**团队奖项对象：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 奖项ID |
| title | string | 是 | 奖项名称 |
| year | string | 是 | 获奖年份 |
| images | array | 是 | 获奖团队图片列表 |

**团队图片对象：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 图片ID |
| image | string | 是 | 图片URL |
| imageType | string | 是 | 图片类型：url/upload |
| winnerName | string | 是 | 获奖团队名称 |
| teamField | string | 否 | 团队领域 |
| story | string | 否 | 获奖事迹（HTML富文本格式，点击图片展开查看） |
| flowers | number | 是 | 收到的花朵数 |
| hasGivenFlower | boolean | 是 | 当前用户是否已送花 |

### 前端展示说明

团队奖项图片展示采用**一行三个**的网格布局，点击图片可展开下拉抽屉查看该团队的获奖事迹。

---

## 3. 获取荣誉影响力排行榜

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/honor/leaderboard` |
| 接口描述 | 获取荣誉影响力排行榜（按获奖数量和花朵数排序） |
| 权限要求 | 无需登录 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| limit | number | 否 | 返回数量，默认10 |
| scope | string | 否 | 筛选范围：all（全员）/mine（我的），默认all |
| filterType | string | 否 | 筛选类型：award（按奖项）/department（按部门） |
| filterValue | string | 否 | 筛选值 |

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "name": "林星辰",
        "department": "架构平台部",
        "avatar": "https://example.com/avatar/linxc.jpg",
        "count": 5,
        "totalFlowers": 58
      },
      {
        "name": "张伟",
        "department": "效能工程部",
        "avatar": "https://example.com/avatar/zhangwei.jpg",
        "count": 4,
        "totalFlowers": 45
      },
      {
        "name": "Sarah",
        "department": "UED 设计中心",
        "avatar": "https://example.com/avatar/sarah.jpg",
        "count": 3,
        "totalFlowers": 32
      }
    ]
  }
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 用户姓名 |
| department | string | 是 | 所属部门 |
| avatar | string | 否 | 用户头像URL |
| count | number | 是 | 获奖数量（勋章数） |
| totalFlowers | number | 是 | 累计收到的花朵总数 |

---

## 4. 送花

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `POST /api/honor/flower` |
| 接口描述 | 给荣誉记录送花 |
| 权限要求 | 需要登录 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| honorId | number | 是 | 荣誉记录ID |
| type | string | 否 | 荣誉类型：individual（个人）/team（团队），默认individual |

### 请求示例

```json
{
  "honorId": 1,
  "type": "individual"
}
```

### 响应数据

```json
{
  "code": 200,
  "message": "送花成功",
  "data": {
    "flowers": 13,
    "hasGivenFlower": true
  }
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| flowers | number | 是 | 送花后的总花朵数 |
| hasGivenFlower | boolean | 是 | 是否已送花（始终为true） |

### 错误响应

```json
{
  "code": 400,
  "message": "已送过花",
  "data": null
}
```

---

## 5. 获取奖项名称列表

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/honor/awards` |
| 接口描述 | 获取所有奖项名称列表（用于筛选） |
| 权限要求 | 无需登录 |

### 请求参数

无

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      "全部",
      "2026年度 AI 技术突破奖",
      "最佳 AI 辅助设计实践",
      "Copilot 效能提升大师",
      "AI 社区贡献之星",
      "年度最佳AI创新团队"
    ]
  }
}
```

---

## 6. 获取部门列表

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/honor/departments` |
| 接口描述 | 获取所有部门列表（用于筛选） |
| 权限要求 | 无需登录 |

### 请求参数

无

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      "全部",
      "架构平台部",
      "UED 设计中心",
      "效能工程部",
      "开源办公室",
      "数据部",
      "算法部"
    ]
  }
}
```

---

## 7. 获取用户荣誉时光轴

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/honor/timeline` |
| 接口描述 | 获取指定用户的荣誉时光轴数据 |
| 权限要求 | 无需登录 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userName | string | 否 | 用户姓名，不传则返回所有用户的时光轴 |

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "user": {
      "name": "林星辰",
      "avatar": "https://example.com/avatar/linxc.jpg",
      "department": "架构平台部",
      "totalFlowers": 58
    },
    "timeline": [
      {
        "year": "2026",
        "items": [
          {
            "id": 1,
            "name": "林星辰",
            "avatar": "https://example.com/avatar/linxc.jpg",
            "awardName": "2026年度 AI 技术突破奖",
            "awardDate": "2026-01-05",
            "category": "innovation"
          }
        ]
      },
      {
        "year": "2025",
        "items": [
          {
            "id": 5,
            "name": "林星辰",
            "avatar": "https://example.com/avatar/linxc.jpg",
            "awardName": "AI 社区贡献之星",
            "awardDate": "2025-06-15",
            "category": "community"
          }
        ]
      }
    ]
  }
}
```

### 字段说明

**用户信息：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 用户姓名 |
| avatar | string | 否 | 用户头像URL |
| department | string | 否 | 所属部门 |
| totalFlowers | number | 是 | 累计花朵总数 |

**时光轴数据：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| year | string | 是 | 年份 |
| items | array | 是 | 该年度的荣誉记录列表 |

---

## 8. 获取奖项规则说明

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `GET /api/honor/award-rules` |
| 接口描述 | 获取所有奖项的规则说明，包含评选标准和评选周期 |
| 权限要求 | 无需登录 |

### 说明

> ⚠️ **重要**：奖项规则说明的数据来源于**管理后台 → AI使用达人管理 → 个人奖项设置**。
> 
> 管理员在个人奖项设置中添加/编辑的奖项信息（名称、描述、评选标准、评选周期）会直接展示在AI使用达人页面的"奖项规则说明"弹窗中。两者共用同一套数据源。

### 请求参数

无

### 响应数据

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "name": "技术创新奖",
        "description": "表彰在AI技术方案上有重大突破的个人或团队",
        "criteria": [
          "提交创新方案不少于2篇",
          "落地至少1个生产项目",
          "产出技术分享或专利"
        ],
        "cycle": "年度"
      },
      {
        "id": 2,
        "name": "效能提升奖",
        "description": "在工程效能、自动化与质量提升方面贡献突出",
        "criteria": [
          "引入自动化工具并落地",
          "显著降低缺陷率或提升交付速度"
        ],
        "cycle": "季度"
      },
      {
        "id": 3,
        "name": "最佳实践奖",
        "description": "在业务场景中形成可复制的AI最佳实践并推广",
        "criteria": [
          "形成完整案例文档",
          "内部分享不少于2场",
          "被至少一个团队复用"
        ],
        "cycle": "季度"
      },
      {
        "id": 4,
        "name": "社区贡献奖",
        "description": "对社区布道、开源贡献或知识传播有突出表现",
        "criteria": [
          "发布高质量文章/视频",
          "组织或参与社区活动",
          "持续开源贡献"
        ],
        "cycle": "年度"
      }
    ],
    "updateTime": "2026-01-10T10:00:00Z"
  }
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| list | array | 是 | 奖项规则列表 |
| list[].id | number | 是 | 奖项ID |
| list[].name | string | 是 | 奖项名称 |
| list[].description | string | 是 | 奖项简要描述 |
| list[].criteria | array | 是 | 评选标准列表（字符串数组） |
| list[].cycle | string | 是 | 评选周期：年度/季度/月度 |
| updateTime | string | 否 | 最后更新时间（ISO 8601格式） |

---

## 9. 保存单个奖项

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `POST /api/honor/awards` |
| 接口描述 | 添加或更新单个奖项（管理员操作） |
| 权限要求 | 需要管理员权限 |

### 说明

> ⚠️ **重要**：此接口用于管理后台的"个人奖项设置"功能。保存的奖项信息会直接展示在AI使用达人页面的"奖项规则说明"中。

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 否 | 奖项ID，不传则为新增 |
| name | string | 是 | 奖项名称 |
| description | string | 否 | 奖项简要描述 |
| criteria | array | 否 | 评选标准列表（字符串数组） |
| cycle | string | 否 | 评选周期：年度/季度/月度，默认"年度" |

### 请求示例

```json
{
  "name": "技术创新奖",
  "description": "表彰在AI技术方案上有重大突破的个人或团队",
  "criteria": [
    "提交创新方案不少于2篇",
    "落地至少1个生产项目",
    "产出技术分享或专利"
  ],
  "cycle": "年度"
}
```

### 响应数据

```json
{
  "code": 200,
  "message": "保存成功",
  "data": {
    "id": 1,
    "name": "技术创新奖",
    "description": "表彰在AI技术方案上有重大突破的个人或团队",
    "criteria": [
      "提交创新方案不少于2篇",
      "落地至少1个生产项目",
      "产出技术分享或专利"
    ],
    "cycle": "年度"
  }
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 奖项ID |
| name | string | 是 | 奖项名称 |
| description | string | 否 | 奖项简要描述 |
| criteria | array | 否 | 评选标准列表（字符串数组） |
| cycle | string | 否 | 评选周期：年度/季度/月度 |

---

## 10. 删除奖项

### 接口信息

| 项目 | 说明 |
|------|------|
| 接口路径 | `DELETE /api/honor/awards/{id}` |
| 接口描述 | 删除指定奖项（管理员操作） |
| 权限要求 | 需要管理员权限 |

### 请求参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 奖项ID（路径参数） |

### 响应数据

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 错误响应

```json
{
  "code": 400,
  "message": "该奖项已有获奖者，无法删除",
  "data": null
}
```

---

## 接口汇总

| 序号 | 接口路径 | 方法 | 描述 |
|------|----------|------|------|
| 1 | `/api/honor/list` | GET | 获取个人荣誉列表 |
| 2 | `/api/honor/team-awards` | GET | 获取团队奖项列表 |
| 3 | `/api/honor/leaderboard` | GET | 获取荣誉影响力排行榜 |
| 4 | `/api/honor/flower` | POST | 送花 |
| 5 | `/api/honor/awards` | GET | 获取奖项名称列表 |
| 6 | `/api/honor/departments` | GET | 获取部门列表 |
| 7 | `/api/honor/timeline` | GET | 获取用户荣誉时光轴 |
| 8 | `/api/honor/award-rules` | GET | 获取奖项规则说明（返回所有奖项及其描述） |
| 9 | `/api/honor/awards` | POST | 保存单个奖项 |
| 10 | `/api/honor/awards/{id}` | DELETE | 删除奖项 |

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
