package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建回复请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "创建回复请求")
public class ReplyCreateRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回复内容", required = true)
    @NotBlank(message = "回复内容不能为空")
    private String content;

    @ApiModelProperty(value = "被回复者用户ID（回复某人时需要）")
    private Integer replyToUserId;

    @ApiModelProperty(value = "被回复的评论/回复ID（用于前端定位展示）")
    private Integer replyToId;
}
