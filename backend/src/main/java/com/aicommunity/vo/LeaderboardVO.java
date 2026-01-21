package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 排行榜VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "排行榜")
public class LeaderboardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "排行榜列表")
    private List<LeaderboardItemVO> list;
}
