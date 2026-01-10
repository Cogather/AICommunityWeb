package com.aicommunity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 未读消息数量响应DTO
 *
 * @author AI Community Team
 */
@Data
@ApiModel(description = "未读消息数量响应")
public class MessageUnreadCountResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "未读消息数量")
    private Integer count;
}
