package com.aicommunity.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 用户个人资料DTO
 *
 * @author AI Community Team
 */
@Data
public class UserProfileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 工号
     */
    private String employeeId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 部门
     */
    private String department;

    /**
     * 帖子数
     */
    private Integer postsCount;

    /**
     * 收藏数
     */
    private Integer favoritesCount;

    /**
     * 评论数
     */
    private Integer commentsCount;

    /**
     * 活动数
     */
    private Integer activitiesCount;

    /**
     * 花朵数
     */
    private Integer flowersCount;

    /**
     * 积分
     */
    private Integer points;

    /**
     * 用户角色列表
     */
    private List<String> roles;

    /**
     * 拥有的工具列表（如果是工具Owner）
     */
    private List<OwnedTool> ownedTools;

    /**
     * 拥有的工具
     */
    @Data
    public static class OwnedTool implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long toolId;
        private String toolName;
    }
}
