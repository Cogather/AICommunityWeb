package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工具帖子列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "工具帖子列表")
public class ToolPostListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子列表")
    private List<ToolPostItemVO> list;

    @ApiModelProperty(value = "总记录数")
    private Long total;

    @ApiModelProperty(value = "当前页码")
    private Integer page;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;
}
