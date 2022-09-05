package dao;

import entity.*;
import service.ConnectionService;
import service.ParserService;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BorrowDao implements Dao {
    private static final BookDao bookDao = new BookDao();
    private static final ReaderDao readerDao = new ReaderDao();
    private ConnectionService connectionService = new ConnectionService();

    /**
     * @return
     */
    @Override
    public List fetchAll() {
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

    public List<Borrow> fetchAllById(String str) {
        try (var connection = connectionService.createConnection()) {
            var sql = "SELECT b.id, b.name, b.author, r.id, r.name FROM \"Borrow\" bor " +
                    "LEFT JOIN \"Book\" b ON bor.book_id = B.id " +
                    "JOIN \"Reader\" r ON bor.reader_id = r.id " +
                    "WHERE r.id = ?";
            var statement = connection.prepareStatement(sql);
            var parsed = ParserService.parseInt(str);
            if (parsed == -1) return null;
            statement.setInt(1, parsed);
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
            return null;
        }
    }

    @Override
    public Borrow fetchById(int id) {
        try (var connection = connectionService.createConnection()) {
            var sql = "SELECT b.id, b.name, b.author, r.id, r.name FROM \"Borrow\" bor " +
                    "LEFT JOIN \"Book\" b ON bor.book_id = B.id " +
                    "JOIN \"Reader\" r ON bor.reader_id = r.id " +
                    "WHERE b.id = ?";
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
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

    @Override
    public boolean addNew(String str) {
        try (var connection = connectionService.createConnection()) {
            var sql = "INSERT INTO \"Borrow\"(reader_id, book_id) VALUES(?, ?)";
            var statement = connection.prepareStatement(sql);
            var inputSplit = str.split(" / ");
            if (inputSplit.length < 2) return false;
            int[] parsed = {ParserService.parseInt(inputSplit[0]), ParserService.parseInt(inputSplit[1])};
            if (parsed[0] == -1 && parsed[1] == -1) return false;
            var reader = readerDao.fetchById(parsed[0]);
            var book = bookDao.fetchById(parsed[1]);
            if (reader == null || book == null) return false;
            statement.setInt(1, parsed[0]);
            statement.setInt(2, parsed[1]);
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }

    @Override
    public boolean deleteRecord(String str) {
        try (var connection = connectionService.createConnection()) {
            var sql = "DELETE FROM \"Borrow\" WHERE reader_id = ? AND book_id = ?";
            var statement = connection.prepareStatement(sql);
            var inputSplit = str.split(" / ");
            int[] parsed = {ParserService.parseInt(inputSplit[0]), ParserService.parseInt(inputSplit[1])};
            if (parsed[0] == -1 && parsed[1] == -1) return false;
            var reader = readerDao.fetchById(parsed[0]);
            var book = bookDao.fetchById(parsed[1]);
            if (reader != null || book != null) {
                statement.setInt(1, parsed[0]);
                statement.setInt(2, parsed[1]);
            }
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }
}
