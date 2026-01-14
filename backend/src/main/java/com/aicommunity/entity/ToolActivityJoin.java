package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工具活动报名实体类
 * 对应数据库表：t_new_tool_activity_join
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "工具活动报名")
public class ToolActivityJoin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "工具ID")
    private Integer toolId;

    @ApiModelProperty(value = "工具活动ID")
    private Integer toolActivityId;

    @ApiModelProperty(value = "报名用户ID")
    private String joinUserId;

    @ApiModelProperty(value = "报名时间")
    private Date joinTime;

    @ApiModelProperty(value = "是否取消：0-否，1-是")
    private Integer canceled;
}
