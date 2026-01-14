package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 扶摇Agent应用设置置顶帖子响应VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "扶摇Agent应用设置置顶帖子响应")
public class AgentFeaturedPostSetResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID", example = "1")
    private Integer postId;

    @ApiModelProperty(value = "设置时间（ISO 8601格式）", example = "2026-01-13T16:00:00Z")
    private String setTime;
}
