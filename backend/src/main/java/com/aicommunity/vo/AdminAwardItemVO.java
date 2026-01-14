package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 管理后台奖项项VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台奖项项")
public class AdminAwardItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "奖项ID")
    private Integer id;

    @ApiModelProperty(value = "奖项名称")
    private String name;

    @ApiModelProperty(value = "奖项简要描述")
    private String description;

    @ApiModelProperty(value = "评选标准列表")
    private List<String> criteria;

    @ApiModelProperty(value = "评选周期：年度/季度/月度")
    private String cycle;
}
