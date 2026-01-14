package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 封面VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "封面")
public class CoverVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "封面ID")
    private Integer id;

    @ApiModelProperty(value = "封面图片URL")
    private String url;

    @ApiModelProperty(value = "封面名称")
    private String name;
}
