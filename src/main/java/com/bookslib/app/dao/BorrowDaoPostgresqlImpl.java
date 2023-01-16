package com.bookslib.app.dao;

import com.bookslib.app.entity.Borrow;
import com.bookslib.app.mapper.BorrowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class BorrowDaoPostgresqlImpl implements BorrowDao {
    private JdbcTemplate jdbcTemplate;

    /**
     * @param bookId
     * @param readerId
     * @return
     */
    @Override
    public Optional<Borrow> save(long readerId, long bookId) {
        var addNewBorrowSql = "INSERT INTO borrow(reader_id, book_id) VALUES(?, ?)";
        try {
            jdbcTemplate.update(addNewBorrowSql, readerId, bookId);
            return findBorrowByReaderIdAndBookId(readerId, bookId);
        } catch (DataAccessException dataAccessException) {
            log.error("This reader is borrow this book! \t reader id - {}, \t book id - {}", readerId, bookId);
            return Optional.empty();
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
                    new BorrowMapper(), readerId, bookId));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set! \t reader id - {}, \t book id - {}", readerId, bookId);
            return Optional.empty();
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
            return jdbcTemplate.query(findAllBorrowsSql, new BorrowMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set!");
            return new ArrayList<>();
        }
    }

    /**
     * @param bookId
     * @param readerId
     */
    @Override
    public int returnBook(long bookId, long readerId) {
        var deleteBorrowSql = """
                DELETE FROM borrow
                WHERE reader_id = ? AND book_id = ?""";
        try {
            return jdbcTemplate.update(deleteBorrowSql, bookId, readerId);
        } catch (DataAccessException dataAccessException) {
            log.warn("Error deleting borrow from database!\n"
                    + dataAccessException.getLocalizedMessage());
            return -1;
        }
    }

    /**
     * @return
     */
    public List<Borrow> findAllReadersWithTheirBorrows() {
        var findAllReadersAndTheirBorrowsSql = """
                SELECT
                b.id AS b_id, b.name AS b_name, b.author AS b_author,
                r.id AS r_id, r.name AS r_name
                FROM reader r
                    LEFT JOIN borrow bor on r.id = bor.reader_id
                    LEFT JOIN book b on b.id = bor.book_id
                ORDER BY reader_id""";
        try {
            return jdbcTemplate.query(findAllReadersAndTheirBorrowsSql, new BorrowMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set!");
            return new ArrayList<>();
        }
    }

    /**
     * @return
     */
    public List<Borrow> findAllBooksWithTheirBorrowers() {
        var findAllBooksAndTheirBorrowersSql = """
                SELECT
                b.id AS b_id, b.name AS b_name, b.author AS b_author,
                r.id AS r_id, r.name AS r_name
                FROM book b
                    LEFT JOIN borrow bor on b.id = bor.book_id
                    LEFT JOIN reader r on r.id = bor.reader_id
                ORDER BY book_id""";
        try {
            return jdbcTemplate.query(findAllBooksAndTheirBorrowersSql, new BorrowMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set!");
            return new ArrayList<>();
        }
    }
}
