package com.aicommunity.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户角色关联实体类
 *
 * @author AI Community Team
 */
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色：admin-管理员，owner-工具Owner，user-普通用户
     */
    private String role;

    /**
     * 工具ID（当role为owner时必填）
     */
    private Long toolId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
