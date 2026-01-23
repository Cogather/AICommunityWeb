package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 专区标签列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "专区标签列表")
public class ZoneTagListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签列表")
    private List<ZoneTagVO> list;
}
