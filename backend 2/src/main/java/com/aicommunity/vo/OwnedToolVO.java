package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户拥有的工具VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "用户拥有的工具")
public class OwnedToolVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工具ID", example = "1")
    private Long toolId;

    @ApiModelProperty(value = "工具名称", example = "TestMate")
    private String toolName;
}
