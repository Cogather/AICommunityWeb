package com.aicommunity.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果类
 *
 * @param <T> 数据类型
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "统一返回结果")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应码", example = "200")
    private Integer code;

    @ApiModelProperty(value = "响应消息", example = "success")
    private String message;

    @ApiModelProperty(value = "响应数据")
    private T data;

    public Result() {
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    /**
     * 成功返回结果（无数据）
     *
     * @return Result对象
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 错误消息
     * @return Result对象
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败返回结果（默认错误码500）
     *
     * @param message 错误消息
     * @return Result对象
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}
