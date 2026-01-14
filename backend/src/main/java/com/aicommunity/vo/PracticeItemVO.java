package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * AI优秀实践项VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "AI优秀实践项")
public class PracticeItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private Integer id;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "作者名称")
    private String author;

    @ApiModelProperty(value = "发布时间（相对时间格式）")
    private String time;

    @ApiModelProperty(value = "分类：training/training-battle/user-exchange")
    private String category;
}
