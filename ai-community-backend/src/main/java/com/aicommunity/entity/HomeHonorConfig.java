package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 荣誉殿堂配置实体类
 *
 * @author AI Community Team
 */
@Data
public class HomeHonorConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * Banner图片URL
     */
    private String bannerImage;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
