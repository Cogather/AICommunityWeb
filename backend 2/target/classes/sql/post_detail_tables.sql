-- 帖子详情与互动相关数据库表结构
-- 注意：以下表结构需要根据实际数据库情况进行调整

-- 评论表
CREATE TABLE IF NOT EXISTS `t_new_post_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `post_id` varchar(30) NOT NULL COMMENT '帖子ID',
  `user_id` varchar(30) NOT NULL COMMENT '评论者用户ID',
  `content` longtext NOT NULL COMMENT '评论内容',
  `likes` int(11) DEFAULT '0' COMMENT '点赞数',
  `status` varchar(10) NOT NULL DEFAULT '0' COMMENT '状态，0-正常，1-已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_post_id` (`post_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='帖子评论表';

-- 回复表
CREATE TABLE IF NOT EXISTS `t_new_post_replies` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `comment_id` int(11) NOT NULL COMMENT '评论ID',
  `user_id` varchar(30) NOT NULL COMMENT '回复者用户ID',
  `reply_to_user_id` varchar(30) DEFAULT NULL COMMENT '被回复者用户ID',
  `content` longtext NOT NULL COMMENT '回复内容',
  `likes` int(11) DEFAULT '0' COMMENT '点赞数',
  `status` varchar(10) NOT NULL DEFAULT '0' COMMENT '状态，0-正常，1-已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_comment_id` (`comment_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='评论回复表';

-- 评论点赞表
CREATE TABLE IF NOT EXISTS `t_new_comment_likes` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `comment_id` int(11) NOT NULL COMMENT '评论ID',
  `user_id` varchar(30) NOT NULL COMMENT '点赞用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_comment_user` (`comment_id`, `user_id`) USING BTREE,
  KEY `idx_comment_id` (`comment_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='评论点赞记录表';

-- 回复点赞表（如果需要）
CREATE TABLE IF NOT EXISTS `t_new_reply_likes` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `reply_id` int(11) NOT NULL COMMENT '回复ID',
  `user_id` varchar(30) NOT NULL COMMENT '点赞用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_reply_user` (`reply_id`, `user_id`) USING BTREE,
  KEY `idx_reply_id` (`reply_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='回复点赞记录表';

-- 草稿表
CREATE TABLE IF NOT EXISTS `t_new_post_drafts` (
  `draft_id` varchar(50) NOT NULL COMMENT '草稿ID',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `zone` varchar(50) DEFAULT NULL COMMENT '所属专区',
  `tool_id` int(11) DEFAULT NULL COMMENT '工具ID',
  `title` varchar(255) DEFAULT NULL COMMENT '草稿标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '草稿摘要',
  `content` longtext COMMENT '草稿内容',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图URL',
  `tags` text COMMENT '标签（JSON格式存储）',
  `saved_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '保存时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`draft_id`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE,
  KEY `idx_saved_at` (`saved_at`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='帖子草稿表';

-- 推荐封面表（如果需要）
CREATE TABLE IF NOT EXISTS `t_recommended_covers` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `url` varchar(500) NOT NULL COMMENT '封面图片URL',
  `name` varchar(100) DEFAULT NULL COMMENT '封面名称',
  `zone` varchar(50) DEFAULT NULL COMMENT '专区类型（可选）',
  `order` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_zone` (`zone`) USING BTREE,
  KEY `idx_order` (`order`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='推荐封面表';
