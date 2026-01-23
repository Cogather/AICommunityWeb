package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户积分信息VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "用户积分信息")
public class UserPointsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总积分", example = "2500")
    private Integer totalPoints;

    @ApiModelProperty(value = "本月积分", example = "350")
    private Integer monthlyPoints;

    @ApiModelProperty(value = "积分排名", example = "15")
    private Integer ranking;

    @ApiModelProperty(value = "积分变动历史")
    private List<PointsHistoryVO> pointsHistory;

    @ApiModelProperty(value = "积分规则说明")
    private List<PointsRuleVO> pointsRules;
}
