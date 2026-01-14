package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 管理后台取消用户获奖请求VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台取消用户获奖请求")
public class AdminCancelAwardRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @ApiModelProperty(value = "荣誉ID", required = true)
    @NotNull(message = "荣誉ID不能为空")
    private Integer honorId;
}
