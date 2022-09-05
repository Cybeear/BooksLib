package dao;

import entity.Book;
import service.ConnectionService;
import service.ParserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDao implements Dao {
    private ConnectionService connectionService = new ConnectionService();

    /**
     * @return
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
     * @param id
     * @return
     */
    @Override
    public Book fetchById(int id) {
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("Select * from \"Book\" where id = ?");
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            resultSet.next();
            return new Book(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    /**
     * @param str string of name and author
     * @return true if create new record in database
     */
    @Override
    public boolean addNew(String str) {
        try (var connection = connectionService.createConnection()) {
            var sql = "INSERT INTO \"Book\"(name, author) VALUES(?, ?)";
            var inputSplit = str.split(" / ");
            if (inputSplit.length < 2) return false;
            var statement = connection.prepareStatement(sql);
            statement.setString(1, inputSplit[0]);
            statement.setString(2, inputSplit[1]);
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    /**
     * @return
     */
    @Override
    public boolean deleteRecord(String str) {
        try (var connection = connectionService.createConnection()) {
            var sql = "DELETE FROM \"Book\" where id = ?";
            var parsed = ParserService.parseInt(str);
            if (parsed == -1) return false;
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, parsed);
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }
}
