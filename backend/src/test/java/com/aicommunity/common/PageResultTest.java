package com.aicommunity.common;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

/**
 * PageResult分页结果单元测试
 *
 * @author AI Community Team
 */
public class PageResultTest {

    /**
     * 测试of方法
     */
    @Test
    public void testOf() {
        // Given
        List<String> list = Arrays.asList("item1", "item2", "item3");
        Long total = 100L;
        Integer page = 1;
        Integer pageSize = 10;

        // When
        PageResult<String> result = PageResult.of(list, total, page, pageSize);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getList().size());
        assertEquals(100L, result.getTotal().longValue());
        assertEquals(1, result.getPage().intValue());
        assertEquals(10, result.getPageSize().intValue());
    }

    /**
     * 测试空列表
     */
    @Test
    public void testOfWithEmptyList() {
        // Given
        List<String> list = Arrays.asList();
        Long total = 0L;
        Integer page = 1;
        Integer pageSize = 10;

        // When
        PageResult<String> result = PageResult.of(list, total, page, pageSize);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getList().size());
        assertEquals(0L, result.getTotal().longValue());
    }
}
