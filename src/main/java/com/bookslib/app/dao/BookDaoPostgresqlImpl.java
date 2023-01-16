package com.bookslib.app.dao;

import com.bookslib.app.entity.Book;
import com.bookslib.app.exceptions.BookDaoException;
import com.bookslib.app.mapper.BookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class BookDaoPostgresqlImpl implements BookDao {
    private JdbcTemplate jdbcTemplate;

    /**
     * @param book
     * @return
     */
    @Override
    public Book save(Book book) {
        var addNewBookSql = "INSERT INTO book(name, author) VALUES(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(addNewBookSql,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, book.getName());
                preparedStatement.setString(2, book.getAuthor());
                return preparedStatement;
            }, keyHolder);
            book.setId((Integer) keyHolder.getKeys().get("id"));
            return book;
        } catch (DataAccessException dataAccessException) {
            log.error("Error save book!\t{}", book);
            throw new BookDaoException(dataAccessException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @Override
    public List<Book> findAll() {
        var findAllBooksSql = "SELECT * FROM book";
        try {
            return jdbcTemplate.query(findAllBooksSql, new BookMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set!");
            return new ArrayList<>();
        }
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public Optional<Book> findById(long bookId) {
        var findBookByIdSql = "SELECT * FROM book WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(findBookByIdSql, new BookMapper(), bookId));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set!\tbook id - {}", bookId);
            return Optional.empty();
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public List<Book> findAllByReaderId(long readerId) {
        var findAllBooksByReaderIdSql = """
                SELECT
                b.id,
                b.name,
                b.author
                FROM book b
                    LEFT JOIN borrow bor ON b.id = bor.book_id
                WHERE bor.reader_id = ?""";
        try {
            return jdbcTemplate.query(findAllBooksByReaderIdSql, new BookMapper(), readerId);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set! \treader id - {}", readerId);
            return new ArrayList<>();
        }
    }
}
