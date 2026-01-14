/*
 Navicat Premium Dump SQL

 Source Server         : LocalHost
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : localhost:3306
 Source Schema         : corecode_ai_community

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 14/01/2026 01:45:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_community_headlines
-- ----------------------------
DROP TABLE IF EXISTS `t_community_headlines`;
CREATE TABLE `t_community_headlines`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头条标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `cover_image` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `detail_link` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详情页链接',
  `publish_date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布日期',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态正常0删除1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_contribution_rules
-- ----------------------------
DROP TABLE IF EXISTS `t_contribution_rules`;
CREATE TABLE `t_contribution_rules`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `activity_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户行为',
  `score_change` int(11) NULL DEFAULT NULL COMMENT '该行为下贡献度增长',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_contribution_transactions
-- ----------------------------
DROP TABLE IF EXISTS `t_contribution_transactions`;
CREATE TABLE `t_contribution_transactions`  (
  `transactions_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id 关联user_info表id',
  `rule_id` int(11) NULL DEFAULT NULL COMMENT '规则id',
  `timestamp` datetime NULL DEFAULT NULL COMMENT '变动时间',
  PRIMARY KEY (`transactions_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_file_opt_log
-- ----------------------------
DROP TABLE IF EXISTS `t_file_opt_log`;
CREATE TABLE `t_file_opt_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人关联user_info表',
  `operate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作A添加D删除',
  `result` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作结果0失败1成功',
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `file_path` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `operate_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_group_join_requests
-- ----------------------------
DROP TABLE IF EXISTS `t_group_join_requests`;
CREATE TABLE `t_group_join_requests`  (
  `request_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申请ID',
  `group_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联兴趣小组表group_id',
  `user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人关联user_info表',
  `applied_at` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核状态：approved/rejected/pending',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请理由',
  PRIMARY KEY (`request_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_homepage_honors
-- ----------------------------
DROP TABLE IF EXISTS `t_homepage_honors`;
CREATE TABLE `t_homepage_honors`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `cover_image` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `detail_link` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详情页链接',
  `publish_date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布日期',
  `honorName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉名称',
  `userName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工号',
  `chnName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `status` int(11) NOT NULL COMMENT '状态正常0删除1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_homepage_promotions
-- ----------------------------
DROP TABLE IF EXISTS `t_homepage_promotions`;
CREATE TABLE `t_homepage_promotions`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `cover_image` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面图片URL地址',
  `detail_link` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详情页链接地址',
  `publish_date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布日期',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态：0-未发布，1-已发布，2-已下线',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_publish_date`(`publish_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首页推广活动表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_honor_banners
-- ----------------------------
DROP TABLE IF EXISTS `t_honor_banners`;
CREATE TABLE `t_honor_banners`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Banner ID',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片URL',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '荣誉Banner表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_honors
-- ----------------------------
DROP TABLE IF EXISTS `t_honors`;
CREATE TABLE `t_honors`  (
  `honor_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉ID',
  `honor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉名称',
  `honor_image` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '奖项图片URL',
  `honor_desc` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '奖项描述（如年度信息）',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`honor_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '荣誉表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_honors_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_honors_detail`;
CREATE TABLE `t_honors_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `honor_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉ID',
  `gained_year` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获得年份',
  `gained_month` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获得月份',
  `honor_user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '荣誉用户ID',
  `members_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '成员JSON数据',
  `story` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '故事内容',
  `honor_type` int(11) NULL DEFAULT NULL COMMENT '荣誉类型',
  `team_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '团队名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_honor_id`(`honor_id`) USING BTREE,
  INDEX `idx_honor_user_id`(`honor_user_id`) USING BTREE,
  INDEX `idx_gained_year_month`(`gained_year`, `gained_month`) USING BTREE,
  INDEX `idx_honor_type`(`honor_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '月度荣誉表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_honors_month
-- ----------------------------
DROP TABLE IF EXISTS `t_honors_month`;
CREATE TABLE `t_honors_month`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `honor_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉ID',
  `gained_year` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获得年份',
  `gained_month` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获得月份',
  `honor_user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '荣誉用户ID',
  `members_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '成员JSON数据',
  `story` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '故事内容',
  `honor_type` int(11) NULL DEFAULT NULL COMMENT '荣誉类型',
  `team_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '团队名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_honor_id`(`honor_id`) USING BTREE,
  INDEX `idx_honor_user_id`(`honor_user_id`) USING BTREE,
  INDEX `idx_gained_year_month`(`gained_year`, `gained_month`) USING BTREE,
  INDEX `idx_honor_type`(`honor_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '月度荣誉表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_interest_groups
-- ----------------------------
DROP TABLE IF EXISTS `t_interest_groups`;
CREATE TABLE `t_interest_groups`  (
  `group_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '小组ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '小组名称',
  `group_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '小组类型',
  `department_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门ID',
  `leader_user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组长用户ID',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`group_id`) USING BTREE,
  INDEX `idx_group_type`(`group_type`) USING BTREE,
  INDEX `idx_department_id`(`department_id`) USING BTREE,
  INDEX `idx_leader_user_id`(`leader_user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '兴趣小组表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_ai_news
-- ----------------------------
DROP TABLE IF EXISTS `t_new_ai_news`;
CREATE TABLE `t_new_ai_news`  (
  `article_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章ID',
  `title_ch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文标题',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章URL',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`article_id`) USING BTREE,
  INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI新闻表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_comment_likes
-- ----------------------------
DROP TABLE IF EXISTS `t_new_comment_likes`;
CREATE TABLE `t_new_comment_likes`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `comment_id` int(11) NOT NULL COMMENT '评论ID',
  `user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '点赞用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_comment_user`(`comment_id`, `user_id`) USING BTREE COMMENT '防止重复点赞',
  INDEX `idx_comment_id`(`comment_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论点赞表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_home_banner_config
-- ----------------------------
DROP TABLE IF EXISTS `t_new_home_banner_config`;
CREATE TABLE `t_new_home_banner_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `banner_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Banner图片URL',
  `banner_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Banner标题',
  `banner_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Banner描述',
  `banner_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Banner跳转URL',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '排序序号',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首页Banner配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_home_tool
-- ----------------------------
DROP TABLE IF EXISTS `t_new_home_tool`;
CREATE TABLE `t_new_home_tool`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工具ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工具名称',
  `desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工具描述',
  `logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Logo URL',
  `color` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `link` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转路由路径',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工具表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_honors
-- ----------------------------
DROP TABLE IF EXISTS `t_new_honors`;
CREATE TABLE `t_new_honors`  (
  `honor_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉ID',
  `honor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉名称',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`honor_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '荣誉表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_honors_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_new_honors_detail`;
CREATE TABLE `t_new_honors_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `honor_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉ID',
  `gained_year` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获得年份',
  `gained_month` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获得月份',
  `honor_user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '荣誉用户ID',
  `members_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '成员JSON数据',
  `story` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '故事内容',
  `honor_type` int(11) NULL DEFAULT NULL COMMENT '荣誉类型',
  `team_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '团队名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_honor_id`(`honor_id`) USING BTREE,
  INDEX `idx_honor_user_id`(`honor_user_id`) USING BTREE,
  INDEX `idx_gained_year_month`(`gained_year`, `gained_month`) USING BTREE,
  INDEX `idx_honor_type`(`honor_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '月度荣誉表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_honors_flowers
-- ----------------------------
DROP TABLE IF EXISTS `t_new_honors_flowers`;
CREATE TABLE `t_new_honors_flowers`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `honor_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉ID',
  `flowers_user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送花用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_honor_id`(`honor_id`) USING BTREE,
  INDEX `idx_honor_user_id`(`flowers_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '月度荣誉表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_post_collected
-- ----------------------------
DROP TABLE IF EXISTS `t_new_post_collected`;
CREATE TABLE `t_new_post_collected`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `post_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子ID',
  `collected_user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收藏帖子的用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_honor_id`(`post_id`) USING BTREE,
  INDEX `idx_honor_user_id`(`collected_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户收藏记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_post_comments
-- ----------------------------
DROP TABLE IF EXISTS `t_new_post_comments`;
CREATE TABLE `t_new_post_comments`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子ID',
  `user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论者用户ID',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评论内容',
  `likes` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态，0-正常，1-已删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post_id`(`post_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_post_likes
-- ----------------------------
DROP TABLE IF EXISTS `t_new_post_likes`;
CREATE TABLE `t_new_post_likes`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `post_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子ID',
  `view_user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '点赞帖子的用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_honor_id`(`post_id`) USING BTREE,
  INDEX `idx_honor_user_id`(`view_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户点赞记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_post_views
-- ----------------------------
DROP TABLE IF EXISTS `t_new_post_views`;
CREATE TABLE `t_new_post_views`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `post_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子ID',
  `view_user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览帖子的用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_honor_id`(`post_id`) USING BTREE,
  INDEX `idx_honor_user_id`(`view_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户浏览记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_posts
-- ----------------------------
DROP TABLE IF EXISTS `t_new_posts`;
CREATE TABLE `t_new_posts`  (
  `post_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子ID',
  `author_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者（关联用户表user_id）',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `front_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '帖子内容',
  `content_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'markdown或richtext，默认richtext',
  `created_at` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '最后更新时间',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帖子链接',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态，0-正常，1-已删除',
  `essence_post` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '0（普通帖子）或1（精华帖）',
  `zone_id` int(11) NULL DEFAULT NULL COMMENT '外键，指向t_new_zone_type表（用于关联帖子的分类。）1：AI优秀实践 2：AI使用达人（团队/个人））3：AI工具专区 4：赋能交流 5：扶摇Agent应用\'',
  `label_id` int(11) NULL DEFAULT NULL COMMENT '外键，指向t_new_post_label表 场景标签id',
  `tool_id` int(11) NULL DEFAULT NULL COMMENT '外键，指向t_new_tool表 工具id',
  `views_nums` int(11) NULL DEFAULT 0 COMMENT '帖子浏览量',
  PRIMARY KEY (`post_id`) USING BTREE,
  INDEX `post_type_index`(`zone_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_posts_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_new_posts_tag`;
CREATE TABLE `t_new_posts_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，序号',
  `label_id` int(11) NULL DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `border_color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_posts_zone_type
-- ----------------------------
DROP TABLE IF EXISTS `t_new_posts_zone_type`;
CREATE TABLE `t_new_posts_zone_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，序号',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帖子分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_tool
-- ----------------------------
DROP TABLE IF EXISTS `t_new_tool`;
CREATE TABLE `t_new_tool`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Banner ID',
  `tool_id` bigint(20) NOT NULL COMMENT '工具ID',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片URL',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `color` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工具主题颜色',
  `owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '责任人 用户表id',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tool_id`(`tool_id`) USING BTREE,
  INDEX `idx_order`(`order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工具表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_new_tool_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_new_tool_activity`;
CREATE TABLE `t_new_tool_activity`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `toolId` int(10) UNSIGNED NOT NULL COMMENT '工具ID',
  `toolName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工具名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '活动内容',
  `cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `date` date NOT NULL COMMENT '活动日期',
  `startTime` time NOT NULL COMMENT '开始时间',
  `endTime` time NOT NULL COMMENT '结束时间',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动地点',
  `meetingLink` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议链接',
  `speaker` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主讲人',
  `speakerTitle` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主讲人头衔',
  `maxParticipants` int(11) NOT NULL COMMENT '最大参与人数',
  `currentParticipants` int(11) NOT NULL COMMENT '当前参与人数',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '新工具活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_new_tool_activity_join
-- ----------------------------
DROP TABLE IF EXISTS `t_new_tool_activity_join`;
CREATE TABLE `t_new_tool_activity_join`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `tool_id` int(10) UNSIGNED NOT NULL COMMENT '工具ID',
  `tool_activity_id` int(11) NOT NULL COMMENT '工具活动id',
  `join_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报名用户',
  `join_time` time NOT NULL COMMENT '报名时间',
  `canceled` tinyint(1) NOT NULL COMMENT '是否取消 0：否 1：是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '新工具活动报名表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_new_tool_banners
-- ----------------------------
DROP TABLE IF EXISTS `t_new_tool_banners`;
CREATE TABLE `t_new_tool_banners`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Banner ID',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片URL',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工具Banner表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_point_rules
-- ----------------------------
DROP TABLE IF EXISTS `t_point_rules`;
CREATE TABLE `t_point_rules`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `score_change` int(11) NOT NULL COMMENT '积分变动值，可正可负',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_point_transactions
-- ----------------------------
DROP TABLE IF EXISTS `t_point_transactions`;
CREATE TABLE `t_point_transactions`  (
  `transaction_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rule_id` int(11) NULL DEFAULT NULL,
  `timestamp` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`transaction_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `rule_id`(`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4732 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_post_associations
-- ----------------------------
DROP TABLE IF EXISTS `t_post_associations`;
CREATE TABLE `t_post_associations`  (
  `association_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `post_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联帖子表post_id',
  `group_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联兴趣小组表group_id',
  `department_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联用户表部门编号',
  PRIMARY KEY (`association_id`) USING BTREE,
  UNIQUE INDEX `idx_postid`(`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_replies
-- ----------------------------
DROP TABLE IF EXISTS `t_replies`;
CREATE TABLE `t_replies`  (
  `reply_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回复ID',
  `post_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联到帖子表post_id',
  `parent_reply_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联到另一个回复ID',
  `author_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联到用户表user_id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '回复内容',
  `created_at` datetime NULL DEFAULT NULL COMMENT '回复时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '回复更新时间',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态，0-正常，1-已删除',
  PRIMARY KEY (`reply_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_reply_feedback
-- ----------------------------
DROP TABLE IF EXISTS `t_reply_feedback`;
CREATE TABLE `t_reply_feedback`  (
  `feedback_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `reply_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联到回复表reply_id',
  `user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联到用户表user_id',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '反馈类型',
  `created_at` datetime NULL DEFAULT NULL COMMENT '反馈时间',
  PRIMARY KEY (`feedback_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_repo_info
-- ----------------------------
DROP TABLE IF EXISTS `t_repo_info`;
CREATE TABLE `t_repo_info`  (
  `repo_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `repo_readme` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `repo_update` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `repo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `repo_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_date` datetime NULL DEFAULT NULL COMMENT '最近刷新update时间',
  `latest_present` datetime NULL DEFAULT NULL COMMENT '最近一次出现在trending的时间',
  PRIMARY KEY (`repo_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_rewards_info
-- ----------------------------
DROP TABLE IF EXISTS `t_rewards_info`;
CREATE TABLE `t_rewards_info`  (
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `points` int(11) NULL DEFAULT 0,
  `updated_point` int(11) NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_trending_repos
-- ----------------------------
DROP TABLE IF EXISTS `t_trending_repos`;
CREATE TABLE `t_trending_repos`  (
  `fetch_date` datetime NOT NULL,
  `trending_repos` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`fetch_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_contributions
-- ----------------------------
DROP TABLE IF EXISTS `t_user_contributions`;
CREATE TABLE `t_user_contributions`  (
  `userid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID, 关联user_info表的userid',
  `total_contributions` int(11) NULL DEFAULT NULL COMMENT '总贡献数',
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info`  (
  `user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工号',
  `chn_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名',
  `author_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `bio` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人简介',
  `department_l1` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '一级部门',
  `department_l1_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '一级部门ID',
  `department_l2` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二级部门',
  `department_l2_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二级部门ID',
  `department_l3` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三级部门',
  `department_l3_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三级部门ID',
  `department_l4` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '四级部门',
  `department_l4_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '四级部门ID',
  `department_l5` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '五级部门',
  `department_l5_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '五级部门ID',
  `department_l6` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '六级部门',
  `department_l6_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '六级部门ID',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
  `level` int(11) NULL DEFAULT 0 COMMENT '用户等级',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核状态',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `idx_user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_messages
-- ----------------------------
DROP TABLE IF EXISTS `t_user_messages`;
CREATE TABLE `t_user_messages`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收消息的用户ID（关联user_info表user_id）',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息类型：activity_registration-活动报名，post_comment-帖子评论，comment_reply-评论回复，post_like-点赞通知，award_notification-奖项通知',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
  `related_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关资源ID（帖子ID、活动ID、奖项ID等）',
  `related_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关资源类型：post-帖子，activity-活动，award-奖项',
  `comment_id` int(11) NULL DEFAULT NULL COMMENT '评论ID（POST_COMMENT和COMMENT_REPLY类型使用，用于定位到具体评论）',
  `reply_id` int(11) NULL DEFAULT NULL COMMENT '回复ID（COMMENT_REPLY类型使用，用于定位到具体回复）',
  `from_user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送消息的用户ID（关联user_info表user_id）',
  `from_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送消息的用户名',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `link` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义跳转链接（可选）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_is_read`(`is_read`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_user_read`(`user_id`, `is_read`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_points
-- ----------------------------
DROP TABLE IF EXISTS `t_user_points`;
CREATE TABLE `t_user_points`  (
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `total_points` int(11) NULL DEFAULT 0 COMMENT '积分',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
