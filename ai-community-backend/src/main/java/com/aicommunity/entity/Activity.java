package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 活动实体类
 *
 * @author AI Community Team
 */
@Data
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 工具ID（-1表示扶摇Agent应用）
     */
    private Long toolId;

    /**
     * 活动类型：training-培训，competition-竞赛，sharing-分享
     */
    private String type;

    /**
     * 活动日期
     */
    private LocalDate date;

    /**
     * 封面图URL
     */
    private String cover;

    /**
     * 活动内容（HTML格式）
     */
    private String content;

    /**
     * 发布者ID
     */
    private Long authorId;

    /**
     * 活动状态：upcoming-即将开始，ongoing-进行中，ended-已结束
     */
    private String status;

    /**
     * 已报名人数
     */
    private Integer registeredCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
