package com.aicommunity.common.exception;

import com.aicommunity.common.ErrorCodeEnum;
import lombok.Getter;

/**
 * 业务异常类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = ErrorCodeEnum.INTERNAL_ERROR.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
    }
}
