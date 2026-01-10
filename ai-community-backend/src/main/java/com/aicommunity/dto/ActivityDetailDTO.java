package com.aicommunity.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 活动详情DTO
 *
 * @author AI Community Team
 */
@Data
public class ActivityDetailDTO implements Serializable {

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
     * 工具ID
     */
    private Long toolId;

    /**
     * 工具名称
     */
    private String toolName;

    /**
     * 活动类型
     */
    private String type;

    /**
     * 活动日期
     */
    private String date;

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
     * 发布者姓名
     */
    private String authorName;

    /**
     * 发布者头像
     */
    private String authorAvatar;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 活动状态
     */
    private String status;

    /**
     * 当前用户是否已报名
     */
    private Boolean isRegistered;

    /**
     * 已报名人数
     */
    private Integer registeredCount;

    /**
     * 当前用户是否是发布者
     */
    private Boolean isAuthor;

    /**
     * 当前用户是否可以编辑（发布者或管理员）
     */
    private Boolean canEdit;

    /**
     * 当前用户是否可以删除（发布者或管理员）
     */
    private Boolean canDelete;
}
