package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 保存单个奖项请求VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "保存单个奖项请求")
public class AwardSaveRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "奖项ID，不传则为新增")
    private Integer id;

    @ApiModelProperty(value = "奖项名称", required = true)
    @NotBlank(message = "奖项名称不能为空")
    private String name;

    @ApiModelProperty(value = "奖项简要描述")
    private String description;

    @ApiModelProperty(value = "评选标准列表（字符串数组）")
    private List<String> criteria;

    @ApiModelProperty(value = "评选周期：年度/季度/月度，默认\"年度\"")
    private String cycle;
}
