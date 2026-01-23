package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 收藏操作请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "收藏操作请求")
public class CollectActionRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作类型：collect-收藏，uncollect-取消收藏", required = true, example = "collect")
    @NotBlank(message = "操作类型不能为空")
    private String action;
}
