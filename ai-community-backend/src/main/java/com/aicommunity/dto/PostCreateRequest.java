package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建帖子请求DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "创建帖子请求")
public class PostCreateRequest implements Serializable {
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

    @ApiModelProperty(value = "专区：practices-AI优秀实践，tools-AI工具专区，agent-扶摇Agent应用，empowerment-赋能交流", required = true)
    @NotBlank(message = "专区不能为空")
    private String zone;

    @ApiModelProperty(value = "工具ID（当zone为tools时必填）")
    private Long toolId;
}
