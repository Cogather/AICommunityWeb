package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 赋能交流帖子VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "赋能交流帖子")
public class EmpowermentPostVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private Integer id;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "标签名称")
    private String tag;

    @ApiModelProperty(value = "标签颜色类型")
    private String tagType;

    @ApiModelProperty(value = "作者名称")
    private String author;

    @ApiModelProperty(value = "发布时间（相对时间格式）")
    private String time;
}
