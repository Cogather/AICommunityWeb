package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 标签列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "标签列表")
public class TagListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签列表")
    private List<TagVO> list;
}
