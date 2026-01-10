package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 奖项实体类
 *
 * @author AI Community Team
 */
@Data
public class Award implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 奖项ID
     */
    private Long id;

    /**
     * 奖项名称
     */
    private String name;

    /**
     * 奖项描述
     */
    private String desc;

    /**
     * 奖项图片URL
     */
    private String image;

    /**
     * 奖项分类：innovation-技术创新，efficiency-效能提升，practice-最佳实践，community-社区贡献
     */
    private String category;

    /**
     * 奖项规则详细说明
     */
    private String rules;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
