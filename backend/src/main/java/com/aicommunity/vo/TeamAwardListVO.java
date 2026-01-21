package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import com.aicommunity.entity.HonorBanner;

/**
 * 团队奖项列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "团队奖项列表")
public class TeamAwardListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "首页荣誉殿堂Banner")
    private HonorBanner banner;

    @ApiModelProperty(value = "团队奖项列表")
    private List<TeamAwardVO> list;

    @ApiModelProperty(value = "年份列表")
    private List<String> years;
}
