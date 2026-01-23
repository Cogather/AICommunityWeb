package com.aicommunity.common;

/**
 * 错误码枚举
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public enum ErrorCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "success"),

    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "请求参数错误"),

    /**
     * 未授权（需要登录）
     */
    UNAUTHORIZED(401, "未授权，请先登录"),

    /**
     * 禁止访问（无权限）
     */
    FORBIDDEN(403, "禁止访问，无权限"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 服务器内部错误
     */
    INTERNAL_ERROR(500, "服务器内部错误"),

    /**
     * 数据库操作失败
     */
    DATABASE_ERROR(501, "数据库操作失败");

    private final Integer code;
    private final String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
