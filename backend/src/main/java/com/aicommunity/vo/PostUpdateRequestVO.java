package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新帖子请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "更新帖子请求")
public class PostUpdateRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "帖子摘要")
    private String summary;

    @ApiModelProperty(value = "帖子内容（HTML格式）")
    private String content;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "标签数组")
    private List<String> tags;
}
