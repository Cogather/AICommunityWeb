package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 评论点赞响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "评论点赞响应")
public class CommentLikeResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否已点赞")
    private Boolean liked;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;
}
