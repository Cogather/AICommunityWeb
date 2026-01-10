package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 社区头条实体类
 *
 * @author AI Community Team
 */
@Data
public class HomeNews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 头条ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片URL
     */
    private String image;

    /**
     * 日期
     */
    private String date;

    /**
     * 链接
     */
    private String link;

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
