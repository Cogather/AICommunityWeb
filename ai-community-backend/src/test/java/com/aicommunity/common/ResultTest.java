package com.aicommunity.common;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Result通用响应类单元测试
 *
 * @author AI Community Team
 */
public class ResultTest {

    /**
     * 测试成功响应
     */
    @Test
    public void testSuccess() {
        // When
        Result<String> result = Result.success("test data");

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());
        assertEquals("success", result.getMessage());
        assertEquals("test data", result.getData());
    }

    /**
     * 测试成功响应 - 无数据
     */
    @Test
    public void testSuccessWithoutData() {
        // When
        Result<?> result = Result.success();

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());
        assertEquals("success", result.getMessage());
    }

    /**
     * 测试失败响应
     */
    @Test
    public void testError() {
        // When
        Result<?> result = Result.error(400, "参数错误");

        // Then
        assertNotNull(result);
        assertEquals(400, result.getCode().intValue());
        assertEquals("参数错误", result.getMessage());
    }

    /**
     * 测试失败响应 - 自定义错误码
     */
    @Test
    public void testErrorWithCustomCode() {
        // When
        Result<?> result = Result.error(400, "参数错误");

        // Then
        assertNotNull(result);
        assertEquals(400, result.getCode().intValue());
        assertEquals("参数错误", result.getMessage());
    }

    /**
     * 测试未授权响应
     */
    @Test
    public void testUnauthorized() {
        // When
        Result<?> result = Result.unauthorized("未授权");

        // Then
        assertNotNull(result);
        assertEquals(401, result.getCode().intValue());
        assertEquals("未授权", result.getMessage());
    }

    /**
     * 测试无权限响应
     */
    @Test
    public void testForbidden() {
        // When
        Result<?> result = Result.forbidden("无权限");

        // Then
        assertNotNull(result);
        assertEquals(403, result.getCode().intValue());
        assertEquals("无权限", result.getMessage());
    }
}
