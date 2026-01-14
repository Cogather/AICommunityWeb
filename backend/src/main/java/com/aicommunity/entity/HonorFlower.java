package com.aicommunity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 荣誉送花实体类
 * 对应数据库表：t_new_honors_flowers
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "荣誉送花")
public class HonorFlower implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID，自增")
    private Integer id;

    @ApiModelProperty(value = "荣誉ID")
    private String honorId;

    @ApiModelProperty(value = "送花用户ID")
    private String flowersUserId;
}
