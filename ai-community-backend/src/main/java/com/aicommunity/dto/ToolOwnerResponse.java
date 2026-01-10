package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工具Owner响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "工具Owner响应")
public class ToolOwnerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否是Owner")
    private Boolean isOwner;

    @ApiModelProperty(value = "工具ID")
    private Long toolId;

    @ApiModelProperty(value = "工具名称")
    private String toolName;
}
