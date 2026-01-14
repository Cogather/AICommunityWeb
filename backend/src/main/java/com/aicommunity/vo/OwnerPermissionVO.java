package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工具Owner权限VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "工具Owner权限")
public class OwnerPermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否为工具Owner")
    private Boolean isOwner;

    @ApiModelProperty(value = "工具ID", example = "-1")
    private Integer toolId;

    @ApiModelProperty(value = "用户拥有的权限列表")
    private List<String> permissions;
}
