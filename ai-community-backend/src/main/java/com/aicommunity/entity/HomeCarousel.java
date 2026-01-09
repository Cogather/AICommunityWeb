package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 首页轮播图实体类
 *
 * @author AI Community Team
 */
@Data
public class HomeCarousel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 轮播图ID
     */
    private Long id;

    /**
     * 图片URL
     */
    private String image;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String desc;

    /**
     * 链接
     */
    private String link;

    /**
     * 是否显示内容：0-否，1-是
     */
    private Boolean showContent;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
