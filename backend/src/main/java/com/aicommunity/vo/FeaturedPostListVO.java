package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 精华帖子列表响应VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "精华帖子列表响应")
public class FeaturedPostListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "精华帖子列表", required = true)
    private List<FeaturedPostVO> list;
}
