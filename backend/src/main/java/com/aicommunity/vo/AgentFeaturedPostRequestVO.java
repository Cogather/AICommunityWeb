package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 扶摇Agent应用设置置顶帖子请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "扶摇Agent应用设置置顶帖子请求")
public class AgentFeaturedPostRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID，设置为null或0表示取消置顶", required = true, example = "1")
    @NotNull(message = "帖子ID不能为空")
    private Integer postId;
}
