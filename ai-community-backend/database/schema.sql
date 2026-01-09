-- AI社区数据库建表语句
-- 数据库：ai_community
-- 字符集：utf8mb4
-- 排序规则：utf8mb4_general_ci

-- 用户表
CREATE TABLE `user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `employee_id` VARCHAR(50) NOT NULL COMMENT '工号',
    `name` VARCHAR(100) NOT NULL COMMENT '姓名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `bio` VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
    `department` VARCHAR(100) DEFAULT NULL COMMENT '部门',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_employee_id` (`employee_id`),
    KEY `idx_name` (`name`),
    KEY `idx_department` (`department`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- 用户角色关联表
CREATE TABLE `user_role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `role` VARCHAR(50) NOT NULL COMMENT '角色：admin-管理员，owner-工具Owner，user-普通用户',
    `tool_id` BIGINT(20) DEFAULT NULL COMMENT '工具ID（当role为owner时必填）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role_tool` (`user_id`, `role`, `tool_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_tool_id` (`tool_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';

-- 帖子表
CREATE TABLE `post` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `summary` VARCHAR(500) DEFAULT NULL COMMENT '内容简介',
    `content` TEXT NOT NULL COMMENT '帖子内容（HTML格式）',
    `author_id` BIGINT(20) NOT NULL COMMENT '作者ID',
    `zone` VARCHAR(50) NOT NULL COMMENT '专区：practices-AI优秀实践，tools-AI工具专区，agent-扶摇Agent应用，empowerment-赋能交流',
    `tool_id` BIGINT(20) DEFAULT NULL COMMENT '工具ID（当zone为tools时必填）',
    `cover` VARCHAR(500) DEFAULT NULL COMMENT '封面图URL',
    `is_featured` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否精选：0-否，1-是',
    `views` INT(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
    `likes` INT(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
    `comments` INT(11) NOT NULL DEFAULT 0 COMMENT '评论数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_author_id` (`author_id`),
    KEY `idx_zone` (`zone`),
    KEY `idx_tool_id` (`tool_id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_is_featured` (`is_featured`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='帖子表';

-- 帖子标签关联表
CREATE TABLE `post_tag` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `post_id` BIGINT(20) NOT NULL COMMENT '帖子ID',
    `tag_name` VARCHAR(100) NOT NULL COMMENT '标签名称',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='帖子标签关联表';

-- 评论表
CREATE TABLE `comment` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `post_id` BIGINT(20) NOT NULL COMMENT '帖子ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '评论用户ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `parent_id` BIGINT(20) DEFAULT NULL COMMENT '父评论ID（回复评论时使用）',
    `reply_to_user_id` BIGINT(20) DEFAULT NULL COMMENT '回复的用户ID',
    `likes` INT(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='评论表';

-- 点赞表
CREATE TABLE `like_record` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `target_type` VARCHAR(50) NOT NULL COMMENT '目标类型：post-帖子，comment-评论',
    `target_id` BIGINT(20) NOT NULL COMMENT '目标ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`),
    KEY `idx_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='点赞记录表';

-- 收藏表
CREATE TABLE `favorite` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `post_id` BIGINT(20) NOT NULL COMMENT '帖子ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_post` (`user_id`, `post_id`),
    KEY `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='收藏表';

-- 活动表
CREATE TABLE `activity` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
    `title` VARCHAR(200) NOT NULL COMMENT '活动标题',
    `tool_id` BIGINT(20) DEFAULT NULL COMMENT '工具ID（-1表示扶摇Agent应用）',
    `type` VARCHAR(50) NOT NULL COMMENT '活动类型：training-培训，competition-竞赛，sharing-分享',
    `date` DATE NOT NULL COMMENT '活动日期',
    `cover` VARCHAR(500) NOT NULL COMMENT '封面图URL',
    `content` TEXT NOT NULL COMMENT '活动内容（HTML格式）',
    `author_id` BIGINT(20) NOT NULL COMMENT '发布者ID',
    `status` VARCHAR(50) NOT NULL DEFAULT 'upcoming' COMMENT '活动状态：upcoming-即将开始，ongoing-进行中，ended-已结束',
    `registered_count` INT(11) NOT NULL DEFAULT 0 COMMENT '已报名人数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_author_id` (`author_id`),
    KEY `idx_tool_id` (`tool_id`),
    KEY `idx_date` (`date`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='活动表';

-- 活动报名表
CREATE TABLE `activity_registration` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `activity_id` BIGINT(20) NOT NULL COMMENT '活动ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='活动报名表';

-- 荣誉表
CREATE TABLE `honor` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '荣誉ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `award_id` BIGINT(20) NOT NULL COMMENT '奖项ID',
    `award_name` VARCHAR(200) NOT NULL COMMENT '奖项名称',
    `award_date` VARCHAR(50) NOT NULL COMMENT '获奖时间（格式：YYYY-MM）',
    `year` VARCHAR(10) NOT NULL COMMENT '年份（格式：YYYY）',
    `category` VARCHAR(50) NOT NULL COMMENT '奖项分类：innovation-技术创新，efficiency-效能提升，practice-最佳实践，community-社区贡献',
    `flowers` INT(11) NOT NULL DEFAULT 0 COMMENT '花朵数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_award_id` (`award_id`),
    KEY `idx_category` (`category`),
    KEY `idx_year` (`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='荣誉表';

-- 荣誉送花记录表
CREATE TABLE `honor_flower` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `honor_id` BIGINT(20) NOT NULL COMMENT '荣誉ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '送花用户ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '送花时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_honor_user` (`honor_id`, `user_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='荣誉送花记录表';

-- 奖项表
CREATE TABLE `award` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '奖项ID',
    `name` VARCHAR(200) NOT NULL COMMENT '奖项名称',
    `desc` VARCHAR(500) DEFAULT NULL COMMENT '奖项描述',
    `image` VARCHAR(500) DEFAULT NULL COMMENT '奖项图片URL',
    `category` VARCHAR(50) NOT NULL COMMENT '奖项分类：innovation-技术创新，efficiency-效能提升，practice-最佳实践，community-社区贡献',
    `rules` TEXT DEFAULT NULL COMMENT '奖项规则详细说明',
    `order` INT(11) NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='奖项表';

-- 工具表
CREATE TABLE `tool` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '工具ID',
    `name` VARCHAR(100) NOT NULL COMMENT '工具名称',
    `desc` VARCHAR(500) DEFAULT NULL COMMENT '工具描述',
    `logo` VARCHAR(500) DEFAULT NULL COMMENT '工具Logo URL',
    `color` VARCHAR(50) DEFAULT NULL COMMENT '工具颜色',
    `link` VARCHAR(200) DEFAULT NULL COMMENT '跳转路由路径',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工具表';

-- 工具Banner表
CREATE TABLE `tool_banner` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Banner ID',
    `tool_id` BIGINT(20) NOT NULL COMMENT '工具ID',
    `image` VARCHAR(500) NOT NULL COMMENT 'Banner图片URL',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '标题',
    `desc` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `order` INT(11) NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_tool_id` (`tool_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工具Banner表';

-- 首页轮播图表
CREATE TABLE `home_carousel` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
    `image` VARCHAR(500) NOT NULL COMMENT '图片URL',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '标题',
    `desc` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `link` VARCHAR(500) DEFAULT NULL COMMENT '链接',
    `show_content` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否显示内容：0-否，1-是',
    `order` INT(11) NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='首页轮播图表';

-- 荣誉殿堂配置表
CREATE TABLE `home_honor_config` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `banner_image` VARCHAR(500) DEFAULT NULL COMMENT 'Banner图片URL',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='荣誉殿堂配置表';

-- 社区头条表
CREATE TABLE `home_news` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '头条ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `image` VARCHAR(500) DEFAULT NULL COMMENT '图片URL',
    `date` VARCHAR(50) DEFAULT NULL COMMENT '日期',
    `link` VARCHAR(500) DEFAULT NULL COMMENT '链接',
    `order` INT(11) NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='社区头条表';

-- 扶摇Agent应用置顶帖子表
CREATE TABLE `agent_featured_post` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `post_id` BIGINT(20) DEFAULT NULL COMMENT '关联的帖子ID（可选）',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `author` VARCHAR(100) DEFAULT NULL COMMENT '作者',
    `author_avatar` VARCHAR(500) DEFAULT NULL COMMENT '作者头像',
    `create_time` VARCHAR(50) DEFAULT NULL COMMENT '创建时间',
    `views` INT(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
    `comments` INT(11) NOT NULL DEFAULT 0 COMMENT '评论数',
    `likes` INT(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
    `cover` VARCHAR(500) NOT NULL COMMENT '封面图URL',
    `link` VARCHAR(500) DEFAULT NULL COMMENT '跳转链接',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='扶摇Agent应用置顶帖子表';

-- 推荐封面表
CREATE TABLE `recommended_cover` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '封面ID',
    `url` VARCHAR(500) NOT NULL COMMENT '封面URL',
    `thumbnail` VARCHAR(500) DEFAULT NULL COMMENT '缩略图URL',
    `order` INT(11) NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='推荐封面表';

-- 消息表
CREATE TABLE `message` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '接收用户ID',
    `type` VARCHAR(50) NOT NULL COMMENT '消息类型：activity_registration-活动报名，post_comment-帖子评论，comment_reply-评论回复，post_like-帖子点赞，award_notification-获奖通知',
    `title` VARCHAR(200) NOT NULL COMMENT '消息标题',
    `content` VARCHAR(500) DEFAULT NULL COMMENT '消息内容',
    `link` VARCHAR(500) DEFAULT NULL COMMENT '跳转链接',
    `from_user_id` BIGINT(20) DEFAULT NULL COMMENT '发送用户ID',
    `read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_read` (`read`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息表';

-- 积分记录表
CREATE TABLE `points_record` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `points` INT(11) NOT NULL COMMENT '积分（正数为增加，负数为减少）',
    `type` VARCHAR(50) NOT NULL COMMENT '积分类型：post-发帖，comment-评论，like_received-被点赞，favorite_received-被收藏，activity-参加活动',
    `target_id` BIGINT(20) DEFAULT NULL COMMENT '关联目标ID（帖子ID、活动ID等）',
    `month` VARCHAR(10) NOT NULL COMMENT '月份（格式：YYYY-MM）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_month` (`month`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='积分记录表';

-- 帖子草稿表
CREATE TABLE `post_draft` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '草稿ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '标题',
    `summary` VARCHAR(500) DEFAULT NULL COMMENT '内容简介',
    `content` TEXT DEFAULT NULL COMMENT '帖子内容',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签（JSON格式）',
    `cover` VARCHAR(500) DEFAULT NULL COMMENT '封面图URL',
    `zone` VARCHAR(50) DEFAULT NULL COMMENT '专区',
    `tool_id` BIGINT(20) DEFAULT NULL COMMENT '工具ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='帖子草稿表';
