package dao;

import entity.Book;
import service.ConnectionServicePostgresImpl;
import service.ConnectionServiceInterface;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoPostgresqlImpl implements BookDao {
    private ConnectionServiceInterface connectionService;

    public BookDaoPostgresqlImpl() {
        this.connectionService = new ConnectionServicePostgresImpl();
    }

    public BookDaoPostgresqlImpl(ConnectionServiceInterface connectionService) {
        this.connectionService = connectionService;
    }

    public ConnectionServiceInterface getConnectionService() {
        return connectionService;
    }

    public void setConnectionService(ConnectionServiceInterface connectionService) {
        this.connectionService = connectionService;
    }

    /**
     * @param book
     * @return
     */
    @Override
    public Book save(Book book) {
        var sql = "INSERT INTO book(name, author) VALUES(?, ?)";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) book.setId(resultSet.getLong(1));
            resultSet.close();
            return book;
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to save book [" + book + "]!\n" +
                    sqlException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @Override
    public List<Book> findAll() {
        var sql = "SELECT * FROM book";
        List<Book> bookList = new ArrayList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bookList.add(new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
            return bookList;
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to find books!\n" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public Optional<Book> findById(long bookId) {
        var sql = "SELECT * FROM book WHERE id = ?";
        Book book = null;
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bookId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
            }
            return Optional.ofNullable(book);
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to find book by Id: "
                    + bookId + "!\n" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public List<Book> findAllByReaderId(long readerId) {
        var sql = "SELECT b.id, b.name, author FROM book b\n" +
                "    LEFT JOIN borrow bor ON b.id = bor.book_id\n" +
                "    LEFT JOIN reader r ON bor.reader_id = r.id\n" +
                "WHERE r.id = ?";
        List<Book> books = new ArrayList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql)) {
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
            throw new RuntimeException("Failed to find books by reader Id: "
                    + readerId + "\n" + sqlException.getLocalizedMessage());
        }
    }


}
