package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 评论列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "评论列表")
public class CommentListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论列表")
    private List<CommentVO> list;

    @ApiModelProperty(value = "总记录数")
    private Long total;

    @ApiModelProperty(value = "当前页码")
    private Integer page;

    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;
}
