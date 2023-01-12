package repositories;

import entities.Book;
import entities.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryPostgresqlImpl implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepositoryPostgresqlImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * @param book
     * @return
     */
    @Override
    public Book save(Book book) {
        var addNewBookSql = "INSERT INTO book(name, author) VALUES(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(addNewBookSql,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, book.getName());
                preparedStatement.setString(2, book.getAuthor());
                return preparedStatement;
            }
        }, keyHolder);
        book.setId((Integer) keyHolder.getKeys().get("id"));
        return book;
    }

    /**
     * @return
     */
    @Override
    public List<Book> findAll() {
        var findAllBooksSql = "SELECT * FROM book";
        return jdbcTemplate.query(findAllBooksSql, new BookMapper());
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public Optional<Book> findById(long bookId) {
        var findBookByIdSql = "SELECT * FROM book WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(findBookByIdSql, new BookMapper(), bookId));
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public List<Book> findAllByReaderId(long readerId) {
        var findAllBooksByReaderIdSql = """
                SELECT
                book.id,
                book.name,
                book.author
                FROM book
                    LEFT JOIN borrow ON book.id = borrow.book_id
                WHERE borrow.reader_id = ?""";
        return jdbcTemplate.query(findAllBooksByReaderIdSql, new BookMapper(), readerId);
    }
}
