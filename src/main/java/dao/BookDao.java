package dao;

import entity.Book;
import service.ConnectionService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDao implements Dao {
    private ConnectionService connectionService = new ConnectionService();

    /**
     * @return list of Book objects
     */
    @Override
    public List<Book> fetchAll() {
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("Select * from \"Book\"");
            var resultSet = statement.executeQuery();
            List<Book> bookList = new ArrayList<>();
            while (resultSet.next()) {
                bookList.add(new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
            if (bookList.size() != 0) return bookList;
            return null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    /**
     * @param id long number, book id
     * @return Book object if exist
     */
    @Override
    public Book fetchById(long id) {
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("Select * from \"Book\" where id = ?");
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            resultSet.next();
            return new Book(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        } catch (SQLException sqlException) {
            return null;
        }
    }

    /**
     * @param obj Book object
     * @return true if operation success
     */
    @Override
    public boolean addNew(Object obj) {
        try (var connection = connectionService.createConnection()) {
            var sql = "INSERT INTO \"Book\"(name, author) VALUES(?, ?)";
            var statement = connection.prepareStatement(sql);
            var book = (Book) obj;
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    /**
     * @param obj Book object
     * @return true if operation success
     */
    @Override
    public boolean deleteRecord(Object obj) {
        try (var connection = connectionService.createConnection()) {
            var sql = "DELETE FROM \"Book\" where id = ?";
            var statement = connection.prepareStatement(sql);
            var book = (Book) obj;
            statement.setLong(1, book.getId());
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }
}
