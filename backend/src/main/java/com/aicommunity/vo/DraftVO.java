package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 草稿VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "草稿")
public class DraftVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "草稿ID")
    private String draftId;

    @ApiModelProperty(value = "所属专区")
    private String zone;

    @ApiModelProperty(value = "工具ID（可为null）")
    private Integer toolId;

    @ApiModelProperty(value = "草稿标题")
    private String title;

    @ApiModelProperty(value = "草稿摘要")
    private String summary;

    @ApiModelProperty(value = "草稿内容（HTML格式）")
    private String content;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "标签数组")
    private List<String> tags;

    @ApiModelProperty(value = "保存时间（ISO 8601格式）")
    private String savedAt;
}
