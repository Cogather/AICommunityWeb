package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户资料响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "用户资料响应")
public class UserProfileResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "工号")
    private String employeeId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "个人简介")
    private String bio;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "发帖数")
    private Integer postsCount;

    @ApiModelProperty(value = "收藏数")
    private Integer favoritesCount;

    @ApiModelProperty(value = "评论数")
    private Integer commentsCount;

    @ApiModelProperty(value = "活动数")
    private Integer activitiesCount;

    @ApiModelProperty(value = "花朵数")
    private Integer flowersCount;

    @ApiModelProperty(value = "积分")
    private Integer points;

    @ApiModelProperty(value = "用户角色列表")
    private List<String> roles;

    @ApiModelProperty(value = "拥有的工具列表")
    private List<OwnedTool> ownedTools;

    @Data
    @ApiModel(description = "拥有的工具")
    public static class OwnedTool implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "工具ID")
        private Long toolId;

        @ApiModelProperty(value = "工具名称")
        private String toolName;
    }
}
