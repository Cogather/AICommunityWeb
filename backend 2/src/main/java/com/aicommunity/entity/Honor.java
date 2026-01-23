package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 荣誉实体类
 * 对应数据库表：t_honors
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "荣誉")
public class Honor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "荣誉ID")
    private String honorId;

    @ApiModelProperty(value = "荣誉名称")
    private String honorName;

    @ApiModelProperty(value = "奖项图片URL")
    private String honorImage;

    @ApiModelProperty(value = "奖项描述（如年度信息）")
    private String honorDesc;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
}
