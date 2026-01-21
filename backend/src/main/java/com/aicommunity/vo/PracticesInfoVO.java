package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    @ApiModelProperty(value = "优秀实践列表")
    Map<String, List<PracticeItemVO>> practices;
}
