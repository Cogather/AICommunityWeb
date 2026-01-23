package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 发布活动响应VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "发布活动响应")
public class ActivityCreateResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动ID")
    private Integer id;

    @ApiModelProperty(value = "工具ID")
    private Integer toolId;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动状态")
    private String status;

    @ApiModelProperty(value = "创建时间（ISO 8601格式）")
    private String createTime;
}
