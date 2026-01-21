package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 管理后台设置用户获奖请求VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台设置用户获奖请求")
public class AdminSetAwardRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @ApiModelProperty(value = "奖项ID", required = true)
    @NotNull(message = "奖项ID不能为空")
    private Integer awardId;

    @ApiModelProperty(value = "获奖时间（YYYY-MM格式）", required = true)
    @NotNull(message = "获奖时间不能为空")
    private String awardDate;
}
