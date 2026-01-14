package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 荣誉殿堂信息VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "荣誉殿堂信息")
public class HonorInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Banner图片URL")
    private String bannerImage;

    @ApiModelProperty(value = "奖项列表")
    private List<HonorAwardVO> awards;

    @ApiModelProperty(value = "Top用户列表")
    private List<TopUserVO> topUsers;
}
