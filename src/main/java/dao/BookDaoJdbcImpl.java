package dao;

import entity.Book;
import service.ConnectionService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoJdbcImpl implements BookDao {
    private ConnectionService connectionService = new ConnectionService();

    /**
     * @param book
     * @return
     */
    @Override
    public Optional<Book> save(Book book) {
        try (var connection = connectionService.createConnection()) {
            var statement =
                    connection.prepareStatement("INSERT INTO \"Book\"(name, author) VALUES(?, ?)");
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            var resultSet = statement.executeUpdate();
            statement.close();
            statement = connection.prepareStatement("SELECT * FROM  \"Book\" WHERE name = ? AND author = ?");
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            var resultSet1 = statement.executeQuery();
            resultSet1.next();
            var newBook = new Book(resultSet1.getLong(1),
                    resultSet1.getString(2),
                    resultSet1.getString(3));
            resultSet1.close();
            statement.close();
            return Optional.of(newBook);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * @return
     */
    @Override
    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("Select * from \"Book\"");
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bookList.add(new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
            resultSet.close();
            statement.close();
            if (bookList.size() != 0) return bookList;
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
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("Select * from \"Book\" where id = ?");
            statement.setLong(1, bookId);
            var resultSet = statement.executeQuery();
            resultSet.next();
            var book = new Book(resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
            resultSet.close();
            statement.close();
            return Optional.of(book);
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public List<Book> findAllByReaderId(long readerId) {
        return null;
    }


}
