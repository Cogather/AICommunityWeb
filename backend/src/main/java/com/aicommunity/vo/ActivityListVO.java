package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 活动列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "活动列表")
public class ActivityListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动列表")
    private List<ActivityItemVO> list;

    @ApiModelProperty(value = "总记录数")
    private Long total;

    @ApiModelProperty(value = "当前页码")
    private Integer page;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;
}
