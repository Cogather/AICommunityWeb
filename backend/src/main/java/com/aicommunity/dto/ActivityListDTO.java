package com.aicommunity.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 活动列表DTO
 *
 * @author AI Community Team
 */
@Data
public class ActivityListDTO implements Serializable {

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
     * 活动描述
     */
    private String description;

    /**
     * 活动状态
     */
    private String status;

    /**
     * 已报名人数
     */
    private Integer registeredCount;
}
