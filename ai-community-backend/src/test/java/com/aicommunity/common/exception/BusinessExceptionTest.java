package com.aicommunity.common.exception;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * BusinessException业务异常单元测试
 *
 * @author AI Community Team
 */
public class BusinessExceptionTest {

    /**
     * 测试默认构造函数
     */
    @Test
    public void testDefaultConstructor() {
        // When
        BusinessException exception = new BusinessException("测试异常");

        // Then
        assertNotNull(exception);
        assertEquals("测试异常", exception.getMessage());
        assertEquals(400, exception.getCode().intValue());
    }

    /**
     * 测试带错误码的构造函数
     */
    @Test
    public void testConstructorWithCode() {
        // When
        BusinessException exception = new BusinessException(400, "参数错误");

        // Then
        assertNotNull(exception);
        assertEquals(400, exception.getCode().intValue());
        assertEquals("参数错误", exception.getMessage());
    }

    /**
     * 测试异常继承关系
     */
    @Test
    public void testExceptionInheritance() {
        // When
        BusinessException exception = new BusinessException("测试异常");

        // Then
        assertTrue(exception instanceof RuntimeException);
    }
}
