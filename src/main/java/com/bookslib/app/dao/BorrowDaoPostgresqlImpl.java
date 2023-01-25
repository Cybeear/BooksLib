package com.bookslib.app.dao;

import com.bookslib.app.entity.Borrow;
import com.bookslib.app.exceptions.BorrowDaoException;
import com.bookslib.app.exceptions.InternalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BorrowDaoPostgresqlImpl implements BorrowDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * @param borrow
     * @return
     */
    @Override
    public void save(Borrow borrow) {
        var addNewBorrowSql = "INSERT INTO borrow(reader_id, book_id) VALUES(?, ?)";
        if (jdbcTemplate.update(addNewBorrowSql, borrow.getReader().getId(), borrow.getBook().getId()) != 1) {
            throw new BorrowDaoException("Borrow is not saved, try again later!");
        }
    }

    /**
     * @param borrow
     */
    @Override
    public void returnBook(Borrow borrow) {
        var deleteBorrowSql = """
                DELETE FROM borrow
                WHERE reader_id = ? AND book_id = ?""";
        try {
            if (jdbcTemplate.update(deleteBorrowSql, borrow.getReader().getId(), borrow.getBook().getId()) != 1) {
                throw new BorrowDaoException("Borrow is not deleted, try again later!");
            }
        } catch (DataAccessException dataAccessException) {
            throw new InternalErrorException(dataAccessException.getLocalizedMessage());
        }

    }
}
