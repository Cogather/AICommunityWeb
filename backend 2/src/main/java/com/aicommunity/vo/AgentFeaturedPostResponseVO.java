package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 扶摇Agent应用置顶帖子响应VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "扶摇Agent应用置顶帖子响应")
public class AgentFeaturedPostResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "置顶帖子对象，无置顶时为null")
    private AgentFeaturedPostVO post;
}
