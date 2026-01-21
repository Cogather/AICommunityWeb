package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子收藏VO（用于Mapper查询结果）
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "帖子收藏信息")
public class PostCollectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    private String postId;

    @ApiModelProperty(value = "收藏时间")
    private Date createTime;
}
