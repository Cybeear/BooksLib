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

@Repository
public class BookRepositoryPostgresqlImpl implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepositoryPostgresqlImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
/*private final ConnectionService connectionService;

    public BookRepositoryPostgresqlImpl() {
        this.connectionService = new ConnectionService();
    }

    public BookRepositoryPostgresqlImpl(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }*/

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

        book.setId(keyHolder.getKey().longValue());
        return book;

        /*try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(addNewBookSql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                book.setId(resultSet.getLong(1));
            }
            resultSet.close();
            return book;
        } catch (SQLException sqlException) {
            throw new BookDaoException("[" + book + "]!\n"
                    + sqlException.getLocalizedMessage());
        }*/
    }

    /**
     * @return
     */
    @Override
    public List<Book> findAll() {
        var findAllBooksSql = "SELECT * FROM book";

        return jdbcTemplate.query(findAllBooksSql, new BookMapper());

        /*try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(findAllBooksSql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bookList.add(new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
            return bookList;
        } catch (SQLException sqlException) {
            throw new BookDaoException("Failed to find books!\n" + sqlException.getLocalizedMessage());
        }*/
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public Book findById(long bookId) {
        var findBookByIdSql = "SELECT * FROM book WHERE id = ?";

        return jdbcTemplate.queryForObject(findBookByIdSql, new BookMapper(), bookId);

        /*Book book = null;
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(findBookByIdSql)) {
            statement.setLong(1, bookId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
            }
            return Optional.ofNullable(book);
        } catch (SQLException sqlException) {
            throw new BookDaoException("Failed to find book by Id: "
                    + bookId + "!\n" + sqlException.getLocalizedMessage());
        }*/
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

        return jdbcTemplate.query(findAllBooksByReaderIdSql,  new BookMapper(), readerId);

        /*List<Book> books = new ArrayList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(findAllBooksByReaderIdSql)) {
            statement.setLong(1, readerId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                books.add(new Book(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
            resultSet.close();
            return books;
        } catch (SQLException sqlException) {
            throw new BookDaoException("Failed to find books by reader Id: "
                    + readerId + "\n" + sqlException.getLocalizedMessage());
        }*/
    }
}
