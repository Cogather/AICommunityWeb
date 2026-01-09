package com.aicommunity.common.enums;

/**
 * 错误码枚举
 *
 * @author AI Community Team
 */
public enum ErrorCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权，请先登录"),

    /**
     * 无权限
     */
    FORBIDDEN(403, "无权限访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 服务器错误
     */
    SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1001, "用户不存在"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1002, "用户名或密码错误"),

    /**
     * 帖子不存在
     */
    POST_NOT_FOUND(2001, "帖子不存在"),

    /**
     * 活动不存在
     */
    ACTIVITY_NOT_FOUND(3001, "活动不存在"),

    /**
     * 荣誉不存在
     */
    HONOR_NOT_FOUND(4001, "荣誉不存在"),

    /**
     * 奖项不存在
     */
    AWARD_NOT_FOUND(4002, "奖项不存在");

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
