package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Top用户VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "Top用户")
public class TopUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Integer id;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "用户头像URL")
    private String avatar;

    @ApiModelProperty(value = "所属部门")
    private String department;

    @ApiModelProperty(value = "用户等级")
    private Integer level;

    @ApiModelProperty(value = "获得荣誉数量")
    private Integer honorCount;
}
