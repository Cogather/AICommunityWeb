package com.aicommunity.util;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * String数组类型处理器
 * 用于MyBatis处理String[]类型与数据库VARCHAR类型的转换
 *
 * @author AI Community Team
 */
@MappedTypes(String[].class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class StringArrayTypeHandler extends BaseTypeHandler<String[]> {

    private static final String DELIMITER = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.length == 0) {
            ps.setString(i, null);
        } else {
            ps.setString(i, String.join(DELIMITER, parameter));
        }
    }

    @Override
    public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return parseStringArray(value);
    }

    @Override
    public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return parseStringArray(value);
    }

    @Override
    public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return parseStringArray(value);
    }

    private String[] parseStringArray(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new String[0];
        }
        return value.split(DELIMITER);
    }
}
