package com.roc.cloud.core.config.mybatis.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * fastJson转换 <br>
 *
 * @date: 2020/10/23 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class JsonObjectHandler extends BaseTypeHandler<JSONObject> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonStr = rs.getString(columnName);
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSONObject.parseObject(jsonStr);
        }
        return null;
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonStr = rs.getString(columnIndex);
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSONObject.parseObject(jsonStr);
        }
        return null;
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonStr = cs.getString(columnIndex);
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSONObject.parseObject(jsonStr);
        }
        return null;
    }
}
