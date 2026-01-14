package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "用户信息")
public class UserProfileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID（系统内部ID）", example = "1")
    private String id;

    @ApiModelProperty(value = "工号（用户中心唯一标识）", example = "E001")
    private String employeeId;

    @ApiModelProperty(value = "姓名", example = "张三")
    private String name;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatars/user1.jpg")
    private String avatar;

    @ApiModelProperty(value = "个人简介", example = "AI技术爱好者，专注于智能应用开发")
    private String bio;

    @ApiModelProperty(value = "完整部门路径（用/分隔）", example = "技术部/AI研发中心/智能应用组")
    private String department;

    @ApiModelProperty(value = "多级部门详细信息")
    private UserDepartmentsVO departments;

    @ApiModelProperty(value = "用户角色列表", example = "[\"admin\", \"user\", \"tool_owner\"]")
    private List<String> roles;

    @ApiModelProperty(value = "作为Owner管理的工具列表")
    private List<OwnedToolVO> ownedTools;

    @ApiModelProperty(value = "发布帖子数", example = "15")
    private Integer postsCount;

    @ApiModelProperty(value = "收藏数", example = "32")
    private Integer favoritesCount;

    @ApiModelProperty(value = "评论数", example = "48")
    private Integer commentsCount;

    @ApiModelProperty(value = "参与活动数", example = "5")
    private Integer activitiesCount;

    @ApiModelProperty(value = "获得小红花数", example = "120")
    private Integer flowersCount;

    @ApiModelProperty(value = "积分", example = "2500")
    private Integer points;
}
