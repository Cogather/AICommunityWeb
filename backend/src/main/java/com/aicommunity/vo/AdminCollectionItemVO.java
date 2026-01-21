package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理后台精选合集项VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台精选合集项")
public class AdminCollectionItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置项ID")
    private Integer id;

    @ApiModelProperty(value = "帖子ID")
    private Integer postId;

    @ApiModelProperty(value = "备注信息")
    private String note;
}
