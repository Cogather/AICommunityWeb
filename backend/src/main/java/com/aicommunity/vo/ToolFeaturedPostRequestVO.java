package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 工具专区设置精华帖子请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "工具专区设置精华帖子请求")
public class ToolFeaturedPostRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工具ID，此接口仅支持toolId=0", required = true, example = "0")
    @NotNull(message = "工具ID不能为空")
    private Integer toolId;

    @ApiModelProperty(value = "帖子ID，设置为null或0表示取消精华", required = true, example = "301")
    @NotNull(message = "帖子ID不能为空")
    private Integer postId;
}
