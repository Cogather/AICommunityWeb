package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理后台奖项名称VO（下拉选择用）
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台奖项名称")
public class AdminAwardNameVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "奖项ID")
    private Integer id;

    @ApiModelProperty(value = "奖项名称")
    private String name;
}
