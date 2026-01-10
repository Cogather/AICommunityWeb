package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

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
     * 活动内容（HTML格式）
     */
    private String content;

    /**
     * 封面图URL
     */
    private String cover;

    /**
     * 工具ID（-1表示扶摇Agent应用）
     */
    private Long toolId;

    /**
     * 工具名称
     */
    private String toolName;

    /**
     * 活动类型：training-培训，competition-竞赛，sharing-分享
     */
    private String type;

    /**
     * 活动日期
     */
    private Date date;

    /**
     * 发布者ID
     */
    private Long authorId;

    /**
     * 发布者姓名
     */
    private String authorName;

    /**
     * 发布者头像
     */
    private String authorAvatar;

    /**
     * 已报名人数
     */
    private Integer registeredCount;

    /**
     * 活动状态：upcoming-即将开始，ongoing-进行中，ended-已结束
     */
    private String status;

    /**
     * 当前用户是否已报名
     */
    private Boolean isRegistered;

    /**
     * 当前用户是否是发布者
     */
    private Boolean isAuthor;

    /**
     * 是否可以编辑
     */
    private Boolean canEdit;

    /**
     * 是否可以删除
     */
    private Boolean canDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
