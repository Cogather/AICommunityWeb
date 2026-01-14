package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 设置帖子精华/置顶请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "设置帖子精华/置顶请求")
public class PostFeaturedSetRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否设为精华/置顶", required = true, example = "true")
    @NotNull(message = "featured字段不能为空")
    private Boolean featured;

    @ApiModelProperty(value = "所属区域：practices/empowerment/agent/tools", example = "practices")
    private String zone;

    @ApiModelProperty(value = "工具ID（AI工具专区使用，其他工具为0）", example = "0")
    private Integer toolId;
}
