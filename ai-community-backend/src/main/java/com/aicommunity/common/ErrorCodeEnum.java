package com.aicommunity.common;

import lombok.Getter;

/**
 * 错误码枚举
 *
 * @author AI Community Team
 */
@Getter
public enum ErrorCodeEnum {
    /**
     * 成功
     */
    SUCCESS(200, "success"),

    /**
     * 参数错误
     */
    BAD_REQUEST(400, "请求参数错误"),

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
    INTERNAL_ERROR(500, "服务器内部错误"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1001, "用户名或密码错误"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1002, "用户不存在"),

    /**
     * 帖子不存在
     */
    POST_NOT_FOUND(2001, "帖子不存在"),

    /**
     * 活动不存在
     */
    ACTIVITY_NOT_FOUND(3001, "活动不存在"),

    /**
     * 已报名
     */
    ALREADY_REGISTERED(3002, "您已报名该活动"),

    /**
     * 未报名
     */
    NOT_REGISTERED(3003, "您未报名该活动"),

    /**
     * 荣誉不存在
     */
    HONOR_NOT_FOUND(4001, "荣誉不存在");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
