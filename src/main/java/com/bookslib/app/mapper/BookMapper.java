package com.bookslib.app.mapper;

import com.bookslib.app.entity.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    /**
     * @param resultSet
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public Book mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Book(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("author"));
    }
}