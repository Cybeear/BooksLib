package dao;

import entity.Book;
import entity.Borrow;
import entity.Reader;
import service.ConnectionService;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BorrowDao implements Dao {
    private final ConnectionService connectionService = new ConnectionService();

    /**
     * @return list of Borrow objects
     */
    @Override
    public List<Borrow> fetchAll() {
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("Select b.id, b.name, b.author, r.id, r.name " +
                    "from \"Borrow\" JOIN \"Book\" b on b.id = \"Borrow\".book_id " +
                    "JOIN \"Reader\" r on r.id = \"Borrow\".reader_id");
            var resultSet = statement.executeQuery();
            List<Borrow> borrowList = new LinkedList<>();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                Reader reader = new Reader(resultSet.getInt(4),
                        resultSet.getString(5));
                borrowList.add(new Borrow(book, reader));
            }
            if (borrowList.size() != 0) {
                return borrowList;
            } else return null;
        } catch (
                SQLException sqlException) {
            return null;
        }
    }

    /**
     * @param id long number, reader id
     * @return list of Borrow objects
     */
    public List<Borrow> fetchAllById(long id) {
        try (var connection = connectionService.createConnection()) {
            var sql = "SELECT b.id, b.name, b.author, r.id, r.name FROM \"Borrow\" bor " +
                    "LEFT JOIN \"Book\" b ON bor.book_id = B.id " +
                    "JOIN \"Reader\" r ON bor.reader_id = r.id " +
                    "WHERE r.id = ?";
            var statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            List<Borrow> borrowList = new LinkedList<>();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                Reader reader = new Reader(resultSet.getInt(4), resultSet.getString(5));
                borrowList.add(new Borrow(book, reader));
            }
            if (borrowList.size() != 0) return borrowList;
            else return null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    /**
     * @param id long number, book id
     * @return Borrow object if exist
     */
    @Override
    public Borrow fetchById(long id) {
        try (var connection = connectionService.createConnection()) {
            var sql = "SELECT b.id, b.name, b.author, r.id, r.name FROM \"Borrow\" bor " +
                    "LEFT JOIN \"Book\" b ON bor.book_id = B.id " +
                    "JOIN \"Reader\" r ON bor.reader_id = r.id " +
                    "WHERE b.id = ?";
            var statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            resultSet.next();
            Book book = new Book(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
            Reader reader = new Reader(resultSet.getInt(4), resultSet.getString(5));
            return new Borrow(book, reader);
        } catch (SQLException sqlException) {
            return null;
        }
    }

    /**
     * @param obj Borrow object
     * @return true if operation success
     */
    @Override
    public boolean addNew(Object obj) {
        try (var connection = connectionService.createConnection()) {
            var sql = "INSERT INTO \"Borrow\"(reader_id, book_id) VALUES(?, ?)";
            var statement = connection.prepareStatement(sql);
            var borrow = (Borrow) obj;
            var reader = borrow.getReader();
            var book = borrow.getBook();
            if (reader == null || book == null) return false;
            statement.setLong(1, reader.getId());
            statement.setLong(2, book.getId());
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    /**
     * @param obj Borrow object
     * @return true if operation success
     */
    @Override
    public boolean deleteRecord(Object obj) {
        try (var connection = connectionService.createConnection()) {
            var sql = "DELETE FROM \"Borrow\" WHERE reader_id = ? AND book_id = ?";
            var statement = connection.prepareStatement(sql);
            var borrow = (Borrow) obj;
            var reader = borrow.getReader();
            var book = borrow.getBook();
            if (reader != null || book != null) {
                statement.setLong(1, reader.getId());
                statement.setLong(2, book.getId());
            }
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }
}
