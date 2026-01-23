package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录请求VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "登录请求参数")
public class LoginRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工号", required = true, example = "E001")
    @NotBlank(message = "工号不能为空")
    private String employeeId;

    @ApiModelProperty(value = "密码", required = true, example = "******")
    @NotBlank(message = "密码不能为空")
    private String password;
}
