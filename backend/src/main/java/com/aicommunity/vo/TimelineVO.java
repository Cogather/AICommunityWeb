package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 时光轴VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "时光轴")
public class TimelineVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户信息")
    private TimelineUserVO user;

    @ApiModelProperty(value = "时光轴数据")
    private List<TimelineYearVO> timeline;
}
