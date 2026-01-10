package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 更新评论请求DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "更新评论请求")
public class CommentUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论内容", required = true)
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
