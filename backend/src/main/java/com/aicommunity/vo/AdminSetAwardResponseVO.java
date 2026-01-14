package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理后台设置用户获奖响应VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台设置用户获奖响应")
public class AdminSetAwardResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "荣誉ID")
    private Integer honorId;
}
