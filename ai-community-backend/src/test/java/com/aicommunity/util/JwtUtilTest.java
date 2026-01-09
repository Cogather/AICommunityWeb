package com.aicommunity.util;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * JWT工具类单元测试
 *
 * @author AI Community Team
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JwtUtil.class})
public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secret = "ai-community-secret-key-2024-this-is-a-very-long-secret-key-for-testing";
    private Long expiration = 86400000L; // 24小时

    @Before
    public void setUp() {
        jwtUtil = new JwtUtil();
        // 使用反射设置私有字段
        Whitebox.setInternalState(jwtUtil, "secret", secret);
        Whitebox.setInternalState(jwtUtil, "expiration", expiration);
    }

    /**
     * 测试生成Token
     */
    @Test
    public void testGenerateToken() {
        // When
        String token = jwtUtil.generateToken(1L, "E001");

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    /**
     * 测试从Token中获取用户ID
     */
    @Test
    public void testGetUserIdFromToken() {
        // Given
        String token = jwtUtil.generateToken(1L, "E001");

        // When
        Long userId = jwtUtil.getUserIdFromToken(token);

        // Then
        assertNotNull(userId);
        assertEquals(1L, userId.longValue());
    }

    /**
     * 测试从Token中获取Claims
     */
    @Test
    public void testGetClaimsFromToken() {
        // Given
        String token = jwtUtil.generateToken(1L, "E001");

        // When
        Claims claims = jwtUtil.getClaimsFromToken(token);

        // Then
        assertNotNull(claims);
        assertEquals(1L, Long.valueOf(claims.get("userId").toString()).longValue());
        assertEquals("E001", claims.get("employeeId").toString());
    }

    /**
     * 测试验证Token - 有效Token
     */
    @Test
    public void testValidateTokenValid() {
        // Given
        String token = jwtUtil.generateToken(1L, "E001");

        // When
        boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    /**
     * 测试验证Token - 无效Token
     */
    @Test
    public void testValidateTokenInvalid() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Then
        assertFalse(isValid);
    }

    /**
     * 测试验证Token - 空Token
     */
    @Test
    public void testValidateTokenEmpty() {
        // Given
        String emptyToken = "";

        // When
        boolean isValid = jwtUtil.validateToken(emptyToken);

        // Then
        assertFalse(isValid);
    }

    /**
     * 测试Token包含正确的声明
     */
    @Test
    public void testTokenContainsCorrectClaims() {
        // Given
        Long userId = 123L;
        String employeeId = "E123";

        // When
        String token = jwtUtil.generateToken(userId, employeeId);
        Claims claims = jwtUtil.getClaimsFromToken(token);

        // Then
        assertEquals(userId, Long.valueOf(claims.get("userId").toString()));
        assertEquals(employeeId, claims.get("employeeId").toString());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }
}
