package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 荣誉实体类
 *
 * @author AI Community Team
 */
@Data
public class Honor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 荣誉ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 部门
     */
    private String department;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 奖项ID
     */
    private Long awardId;

    /**
     * 奖项名称
     */
    private String awardName;

    /**
     * 获奖时间（格式：YYYY-MM）
     */
    private String awardDate;

    /**
     * 年份（格式：YYYY）
     */
    private String year;

    /**
     * 奖项分类：innovation-技术创新，efficiency-效能提升，practice-最佳实践，community-社区贡献
     */
    private String category;

    /**
     * 花朵数
     */
    private Integer flowers;

    /**
     * 是否已送花
     */
    private Boolean hasGivenFlower;

    /**
     * 是否是我的荣誉
     */
    private Boolean isMine;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * Top用户内部类
     */
    @Data
    public static class TopUser implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long id;
        private String name;
        private String avatar;
        private Integer level;
        private String department;
    }
}
