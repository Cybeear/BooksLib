package dao;

import entity.Book;
import service.ConnectionService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDaoJdbcImpl implements BookDao {
    private ConnectionService connectionService = new ConnectionService();

    /**
     * @param book
     * @return
     */
    @Override
    public Book save(Book book) {
        try (var connection = connectionService.createConnection()) {
            var sql = "INSERT INTO \"Book\"(name, author) VALUES(?, ?)";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            var resultSet = statement.executeUpdate();
            statement.close();
            return book;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
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
    public Book findById(long bookId) {
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("Select * from \"Book\" where id = ?");
            statement.setLong(1, bookId);
            var resultSet = statement.executeQuery();
            resultSet.next();
            var book = new Book(resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
            statement.close();
            return book;
        } catch (SQLException sqlException) {
            return null;
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
