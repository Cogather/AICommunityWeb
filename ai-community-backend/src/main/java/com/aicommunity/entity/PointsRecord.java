package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积分记录实体类
 *
 * @author AI Community Team
 */
@Data
public class PointsRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 积分（正数为增加，负数为减少）
     */
    private Integer points;

    /**
     * 积分类型：post-发帖，comment-评论，like_received-被点赞，favorite_received-被收藏，activity-参加活动
     */
    private String type;

    /**
     * 关联目标ID（帖子ID、活动ID等）
     */
    private Long targetId;

    /**
     * 月份（格式：YYYY-MM）
     */
    private String month;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
