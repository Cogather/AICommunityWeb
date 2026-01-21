package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 赋能交流帖子列表响应VO（带分页）
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "赋能交流帖子列表响应（带分页）")
public class EmpowermentPostListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子列表", required = true)
    private List<EmpowermentPostItemVO> list;

    @ApiModelProperty(value = "总数（不包含精华帖子）", required = true, example = "45")
    private Long total;

    @ApiModelProperty(value = "当前页码", required = true, example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页条数", required = true, example = "15")
    private Integer pageSize;
}
