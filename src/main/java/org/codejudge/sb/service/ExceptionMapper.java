package org.codejudge.sb.service;

import org.codejudge.sb.model.ExceptionCount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExceptionMapper implements RowMapper<ExceptionCount> {

    @Override
    public ExceptionCount mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExceptionCount exceptionCount = new ExceptionCount();
        exceptionCount.setException(rs.getString(2));
        exceptionCount.setCount(rs.getString(1));

        return exceptionCount;
    }
}
