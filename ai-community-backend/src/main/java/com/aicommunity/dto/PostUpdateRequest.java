package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 更新帖子请求DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "更新帖子请求")
public class PostUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子标题", required = true)
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "内容简介", required = true)
    @NotBlank(message = "内容简介不能为空")
    private String summary;

    @ApiModelProperty(value = "帖子内容", required = true)
    @NotBlank(message = "帖子内容不能为空")
    private String content;

    @ApiModelProperty(value = "标签列表")
    private String[] tags;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "专区", required = true)
    @NotBlank(message = "专区不能为空")
    private String zone;

    @ApiModelProperty(value = "工具ID")
    private Long toolId;
}
