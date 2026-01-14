package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分历史记录VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "积分历史记录")
public class PointsHistoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录ID")
    private Integer id;

    @ApiModelProperty(value = "积分类型", example = "post_publish")
    private String type;

    @ApiModelProperty(value = "积分变动值", example = "50")
    private Integer points;

    @ApiModelProperty(value = "描述", example = "发布帖子《AI技术实践分享》")
    private String description;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）")
    private String createdAt;
}
