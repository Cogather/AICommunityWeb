package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 新荣誉详情实体类
 * 对应数据库表：t_new_honors_detail
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "新荣誉详情")
public class NewHonorDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID，自增")
    private Integer id;

    @ApiModelProperty(value = "荣誉ID")
    private String honorId;

    @ApiModelProperty(value = "获得年份")
    private String gainedYear;

    @ApiModelProperty(value = "获得月份")
    private String gainedMonth;

    @ApiModelProperty(value = "荣誉用户ID")
    private String honorUserId;

    @ApiModelProperty(value = "成员JSON数据")
    private String membersJson;

    @ApiModelProperty(value = "故事内容")
    private String story;

    @ApiModelProperty(value = "荣誉类型 0：个人 1：团队")
    private Integer honorType;

    @ApiModelProperty(value = "团队名称")
    private String teamName;
}
