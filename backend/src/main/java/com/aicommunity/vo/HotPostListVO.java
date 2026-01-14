package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 最热帖子列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "最热帖子列表")
public class HotPostListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "最热帖子列表")
    private List<HotPostVO> list;
}
