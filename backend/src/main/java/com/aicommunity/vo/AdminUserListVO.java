package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 管理后台用户列表VO
 *
 * @author AI Community Team
 * @date 2026-01-14
 */
@Data
@ApiModel(description = "管理后台用户列表")
public class AdminUserListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户列表")
    private List<AdminUserItemVO> list;
}
