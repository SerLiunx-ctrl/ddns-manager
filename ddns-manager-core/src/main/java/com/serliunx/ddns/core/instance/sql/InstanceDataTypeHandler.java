package com.serliunx.ddns.core.instance.sql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Deprecated
public class InstanceDataTypeHandler extends BaseTypeHandler<Object> {

    private final JsonMapper jsonMapper;

    public InstanceDataTypeHandler() {
        jsonMapper = new JsonMapper();
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        String jsonString;
        try {
            jsonString = jsonMapper.writeValueAsString(parameter);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ps.setString(i, jsonString == null ? "" : jsonString);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonString = rs.getString(columnName);

        return "";
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return "";
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return "";
    }
}
