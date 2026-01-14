package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 最新获奖者列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "最新获奖者列表")
public class LatestWinnerListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "获奖者列表", required = true)
    private List<LatestWinnerVO> list;
}
