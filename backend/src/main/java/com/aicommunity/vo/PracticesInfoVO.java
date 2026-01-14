package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * AI优秀实践信息VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "AI优秀实践信息")
public class PracticesInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "培训赋能列表")
    private List<PracticeItemVO> training;

    @ApiModelProperty(value = "AI训战列表")
    private List<PracticeItemVO> trainingBattle;

    @ApiModelProperty(value = "用户交流列表")
    private List<PracticeItemVO> userExchange;
}
