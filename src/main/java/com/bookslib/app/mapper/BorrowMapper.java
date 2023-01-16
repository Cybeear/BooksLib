package com.bookslib.app.mapper;

import com.bookslib.app.entity.Book;
import com.bookslib.app.entity.Borrow;
import com.bookslib.app.entity.Reader;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowMapper implements RowMapper<Borrow> {
    /**
     * @param resultSet
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public Borrow mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Book book;
        Reader reader;
        try {
            book = new Book(
                    resultSet.getLong("book_id"),
                    resultSet.getString("book_name"),
                    resultSet.getString("book_author"));
        } catch (SQLException sqlException) {
            book = null;
        }
        try {
            reader = new Reader(resultSet.getLong("reader_id"),
                    resultSet.getString("reader_name"));
        } catch (SQLException sqlException) {
            reader = null;
        }
        return new Borrow(book, reader);
    }
}