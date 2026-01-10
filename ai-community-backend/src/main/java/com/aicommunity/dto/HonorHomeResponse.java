package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 荣誉殿堂首页响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "荣誉殿堂首页响应")
public class HonorHomeResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "荣誉殿堂信息")
    private HonorInfo honor;

    @ApiModelProperty(value = "社区头条信息")
    private NewsInfo news;

    @Data
    @ApiModel(description = "荣誉殿堂信息")
    public static class HonorInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "Banner图片URL")
        private String bannerImage;

        @ApiModelProperty(value = "奖项列表")
        private List<AwardItem> awards;

        @ApiModelProperty(value = "AI使用达人Top用户列表")
        private List<TopUser> topUsers;
    }

    @Data
    @ApiModel(description = "奖项项")
    public static class AwardItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "奖项ID")
        private Long id;

        @ApiModelProperty(value = "奖项名称")
        private String name;

        @ApiModelProperty(value = "奖项描述")
        private String desc;

        @ApiModelProperty(value = "奖项图片URL")
        private String image;

        @ApiModelProperty(value = "排序")
        private Integer order;
    }

    @Data
    @ApiModel(description = "Top用户")
    public static class TopUser implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "用户ID")
        private Long id;

        @ApiModelProperty(value = "姓名")
        private String name;

        @ApiModelProperty(value = "头像")
        private String avatar;

        @ApiModelProperty(value = "等级")
        private Integer level;

        @ApiModelProperty(value = "部门")
        private String department;
    }

    @Data
    @ApiModel(description = "社区头条信息")
    public static class NewsInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "头条列表")
        private List<NewsItem> list;
    }

    @Data
    @ApiModel(description = "头条项")
    public static class NewsItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "头条ID")
        private Long id;

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "图片URL")
        private String image;

        @ApiModelProperty(value = "日期")
        private String date;

        @ApiModelProperty(value = "链接")
        private String link;

        @ApiModelProperty(value = "排序")
        private Integer order;
    }
}
