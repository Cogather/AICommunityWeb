package com.aicommunity.common;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * PageQuery分页查询参数单元测试
 *
 * @author AI Community Team
 */
public class PageQueryTest {

    /**
     * 测试默认构造函数
     */
    @Test
    public void testDefaultConstructor() {
        // When
        PageQuery pageQuery = new PageQuery();

        // Then
        assertNotNull(pageQuery);
        assertEquals(1, pageQuery.getPage().intValue());
        assertEquals(10, pageQuery.getPageSize().intValue());
    }

    /**
     * 测试带参构造函数
     */
    @Test
    public void testConstructorWithParams() {
        // When
        PageQuery pageQuery = new PageQuery(2, 20);

        // Then
        assertNotNull(pageQuery);
        assertEquals(2, pageQuery.getPage().intValue());
        assertEquals(20, pageQuery.getPageSize().intValue());
    }

    /**
     * 测试Setter和Getter
     */
    @Test
    public void testSettersAndGetters() {
        // Given
        PageQuery pageQuery = new PageQuery();

        // When
        pageQuery.setPage(3);
        pageQuery.setPageSize(30);

        // Then
        assertEquals(3, pageQuery.getPage().intValue());
        assertEquals(30, pageQuery.getPageSize().intValue());
    }
}
