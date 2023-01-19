package com.bookslib.app.dao;

import com.bookslib.app.entity.Book;
import com.bookslib.app.exceptions.BookDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookDaoPostgresqlImpl implements BookDao {
    private final JdbcTemplate jdbcTemplate;

    public Book mapToBook(ResultSet resultSet, int rowNum) throws SQLException {
        var book = new Book();
        try {
            book.setId(resultSet.getLong("id"));
            book.setName(resultSet.getString("name"));
            book.setAuthor(resultSet.getString("author"));
            return book;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return book;
        }
    }

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
            return jdbcTemplate.query(findAllBooksSql, this::mapToBook);
        } catch (DataAccessException dataAccessException) {
            log.error("Error fetch books from DB!");
            throw new BookDaoException(dataAccessException.getLocalizedMessage());
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
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    findBookByIdSql,
                    this::mapToBook,
                    bookId));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.info("Error fetch book from DB ResultSet return 0 rows!");
            return Optional.empty();
        } catch (DataAccessException dataAccessException) {
            log.error("Error fetch book from DB!\t book id - {}", bookId);
            throw new BookDaoException(dataAccessException.getLocalizedMessage());
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
            return jdbcTemplate.query(findAllBooksByReaderIdSql, this::mapToBook, readerId);
        } catch (DataAccessException dataAccessException) {
            log.error("Error fetch books from DB!\t reader id - {}", readerId);
            throw new BookDaoException(dataAccessException.getLocalizedMessage());
        }
    }
}
