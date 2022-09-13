package dao;

import entity.Book;
import service.ConnectionService;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoJdbcImpl implements BookDao {
    private final ConnectionService connectionService = new ConnectionService();

    /**
     * @param book
     * @return
     */
    @Override
    public Optional<Book> save(Book book) {
        Book newBook = null;
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement("INSERT INTO book(id, name, author) " +
                             "VALUES(Default, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) newBook = new Book(resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
            resultSet.close();
            return Optional.ofNullable(newBook);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.ofNullable(newBook);
        }
    }

    /**
     * @return
     */
    @Override
    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement("SELECT * FROM book");
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bookList.add(new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
            return bookList;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return bookList;
        }
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public Optional<Book> findById(long bookId) {
        Book book = null;
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement("SELECT * FROM book WHERE id = ?")) {
            statement.setLong(1, bookId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) book = new Book(resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
            return Optional.ofNullable(book);
        } catch (SQLException sqlException) {
            return Optional.ofNullable(book);
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public List<Book> findAllByReaderId(long readerId) {
        List<Book> books = new ArrayList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement("SELECT r.id, r.name FROM book b\n" +
                     "    LEFT JOIN borrow bor ON b.id = bor.book_id\n" +
                     "    LEFT JOIN reader r ON bor.reader_id = r.id\n" +
                     "WHERE b.id = ?")) {
            statement.setLong(1, readerId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) books.add(new Book(resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString(3)));
            resultSet.close();
            return books;
        } catch (SQLException sqlException) {
            System.err.println("SqlError: " + sqlException.getMessage());
            return books;
        }
    }


}
