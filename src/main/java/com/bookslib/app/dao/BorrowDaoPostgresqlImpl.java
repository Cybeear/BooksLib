package com.bookslib.app.dao;

import com.bookslib.app.entity.Book;
import com.bookslib.app.entity.Borrow;
import com.bookslib.app.entity.Reader;
import com.bookslib.app.exceptions.BorrowDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BorrowDaoPostgresqlImpl implements BorrowDao {
    private final JdbcTemplate jdbcTemplate;

    public Borrow mapToBorrow(ResultSet resultSet, int rowNum) {
        var book = new Book();
        var reader = new Reader();
        try {
            book.setId(resultSet.getLong("b_id"));
            book.setName(resultSet.getString("b_name"));
            book.setAuthor(resultSet.getString("b_author"));
        } catch (SQLException sqlException1) {
            book = null;
        }
        try {
            reader.setId(resultSet.getLong("r_id"));
            reader.setName(resultSet.getString("r_name"));
        } catch (SQLException sqlException1) {
            reader = null;
        }
        return new Borrow(book, reader);
    }


    /**
     * @param readerId
     * @param bookId
     * @return
     */
    @Override
    public Optional<Borrow> save(long readerId, long bookId) {
        var addNewBorrowSql = "INSERT INTO borrow(reader_id, book_id) VALUES(?, ?)";
        try {
            jdbcTemplate.update(addNewBorrowSql, readerId, bookId);
            return findBorrowByReaderIdAndBookId(readerId, bookId);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            log.info("Trying to create duplicated data:\t reader id - {} book id - {}", readerId, bookId);
            return Optional.empty();
        } catch (DataAccessException dataAccessException) {
            log.error("Error save borrow!\treader id -{}\tbook id - {}", readerId, bookId);
            throw new BorrowDaoException(dataAccessException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @param readerId
     * @return
     */
    private Optional<Borrow> findBorrowByReaderIdAndBookId(long readerId, long bookId) {
        var findBorrowByReaderIdAndBookIdSql = """
                SELECT
                b.id AS b_id, b.name AS b_name, b.author AS b_author,
                r.id AS r_id, r.name AS r_name
                FROM borrow bor
                    JOIN book b on b.id = bor.book_id
                    JOIN reader r on r.id = bor.reader_id WHERE bor.reader_id = ? AND bor.book_id = ?""";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(findBorrowByReaderIdAndBookIdSql,
                    this::mapToBorrow, readerId, bookId));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.info("Error fetch borrow from DB ResultSet return 0 rows!");
            return Optional.empty();
        } catch (DataAccessException dataAccessException) {
            throw new BorrowDaoException(dataAccessException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @Override
    public List<Borrow> findAll() {
        var findAllBorrowsSql = """
                SELECT
                b.id AS b_id, b.name AS b_name, b.author AS b_author,
                r.id AS r_id, r.name AS r_name
                FROM borrow bor
                    JOIN book b ON b.id = bor.book_id
                    JOIN reader r ON r.id = bor.reader_id""";
        try {
            return jdbcTemplate.query(findAllBorrowsSql, this::mapToBorrow);
        } catch (DataAccessException dataAccessException) {
            throw new BorrowDaoException(dataAccessException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @param readerId
     */
    @Override
    public int returnBook(long readerId, long bookId) {
        var deleteBorrowSql = """
                DELETE FROM borrow
                WHERE reader_id = ? AND book_id = ?""";
        try {
            return jdbcTemplate.update(deleteBorrowSql, readerId, bookId);
        } catch (DataAccessException dataAccessException) {
            throw new BorrowDaoException(dataAccessException.getLocalizedMessage());
        }
    }
}
