package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 管理后台所有精华/置顶帖子列表VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台所有精华/置顶帖子列表")
public class AdminFeaturedPostListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "AI优秀实践精华帖子列表")
    private List<AdminFeaturedPostVO> practices;

    @ApiModelProperty(value = "赋能交流精华帖子列表")
    private List<AdminFeaturedPostVO> empowerment;

    @ApiModelProperty(value = "扶摇Agent置顶帖子列表")
    private List<AdminFeaturedPostVO> agent;

    @ApiModelProperty(value = "AI工具专区其他工具精华帖子列表")
    private List<AdminFeaturedPostVO> otherTools;
}
