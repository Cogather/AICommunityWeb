package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 设置精华帖子请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "设置精华帖子请求")
public class FeaturedPostSetRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID", required = true, example = "1")
    @NotNull(message = "帖子ID不能为空")
    private Integer postId;

    @ApiModelProperty(value = "true-设为精华，false-取消精华", required = true, example = "true")
    @NotNull(message = "featured字段不能为空")
    private Boolean featured;
}
