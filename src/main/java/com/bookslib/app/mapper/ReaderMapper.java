package com.bookslib.app.mapper;

import com.bookslib.app.entity.Reader;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderMapper implements RowMapper<Reader> {

    /**
     * @param resultSet
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public Reader mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Reader(
                resultSet.getLong("id"),
                resultSet.getString("name"));
    }
}