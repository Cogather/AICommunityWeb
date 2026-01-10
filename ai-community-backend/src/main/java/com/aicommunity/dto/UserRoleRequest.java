package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户角色请求DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "用户角色请求")
public class UserRoleRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色：admin-管理员，owner-工具Owner", required = true)
    @NotBlank(message = "角色不能为空")
    private String role;

    @ApiModelProperty(value = "工具ID（当role为owner时必填）")
    private Long toolId;

    @ApiModelProperty(value = "工号（可选）")
    private String employeeId;
}
