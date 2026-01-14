package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 保存草稿响应VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "保存草稿响应")
public class DraftSaveResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "草稿ID")
    private String draftId;

    @ApiModelProperty(value = "保存时间（ISO 8601格式）")
    private String savedAt;
}
