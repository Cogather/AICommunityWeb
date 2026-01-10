package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建荣誉请求DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "创建荣誉请求")
public class HonorCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "奖项ID", required = true)
    @NotNull(message = "奖项ID不能为空")
    private Long awardId;

    @ApiModelProperty(value = "奖项名称", required = true)
    @NotBlank(message = "奖项名称不能为空")
    private String awardName;

    @ApiModelProperty(value = "获奖时间（格式：YYYY-MM）", required = true)
    @NotBlank(message = "获奖时间不能为空")
    private String awardDate;

    @ApiModelProperty(value = "奖项分类", required = true)
    @NotBlank(message = "奖项分类不能为空")
    private String category;

    @ApiModelProperty(value = "年份（格式：YYYY）")
    private String year;
}
