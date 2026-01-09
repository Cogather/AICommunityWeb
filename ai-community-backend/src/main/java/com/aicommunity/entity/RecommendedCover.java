package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推荐封面实体类
 *
 * @author AI Community Team
 */
@Data
public class RecommendedCover implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 封面ID
     */
    private Long id;

    /**
     * 封面URL
     */
    private String url;

    /**
     * 缩略图URL
     */
    private String thumbnail;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
