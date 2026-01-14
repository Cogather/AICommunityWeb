package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 送花请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "送花请求")
public class FlowerRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "荣誉记录ID", required = true)
    @NotNull(message = "荣誉记录ID不能为空")
    private Integer honorId;

    @ApiModelProperty(value = "荣誉类型：individual（个人）/team（团队），默认individual")
    private String type;
}
