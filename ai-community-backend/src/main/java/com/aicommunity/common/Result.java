package com.aicommunity.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一返回结果类
 *
 * @param <T> 数据类型
 * @author AI Community Team
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功返回
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    /**
     * 成功返回（无数据）
     *
     * @return Result
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    /**
     * 成功返回（自定义消息）
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return Result
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /**
     * 失败返回
     *
     * @param code    错误码
     * @param message 错误消息
     * @return Result
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败返回（400）
     *
     * @param message 错误消息
     * @return Result
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(400, message, null);
    }

    /**
     * 未授权返回（401）
     *
     * @param message 错误消息
     * @return Result
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    /**
     * 无权限返回（403）
     *
     * @param message 错误消息
     * @return Result
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }

    /**
     * 资源不存在返回（404）
     *
     * @param message 错误消息
     * @return Result
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    /**
     * 服务器错误返回（500）
     *
     * @param message 错误消息
     * @return Result
     */
    public static <T> Result<T> serverError(String message) {
        return new Result<>(500, message, null);
    }
}
