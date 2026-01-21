package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 创建帖子请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "创建帖子请求")
public class PostCreateRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子标题", required = true)
    @NotBlank(message = "帖子标题不能为空")
    private String title;

    @ApiModelProperty(value = "帖子摘要")
    private String summary;

    @ApiModelProperty(value = "帖子内容（HTML格式）", required = true)
    @NotBlank(message = "帖子内容不能为空")
    private String content;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "所属专区：practices/tools/agent/empowerment", required = true)
    @NotBlank(message = "所属专区不能为空")
    private String zone;

    @ApiModelProperty(value = "关联工具ID（zone为tools或agent时需要）")
    private Integer toolId;

    @ApiModelProperty(value = "标签数组")
    private List<String> tags;
}
