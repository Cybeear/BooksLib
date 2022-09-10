package dao;

import entity.Reader;
import service.ConnectionService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReaderDaoJdbcImpl implements ReaderDao {
    private final ConnectionService connectionService = new ConnectionService();

    /**
     * @param reader
     * @return
     */
    @Override
    public Reader save(Reader reader) {
        try (var connection = connectionService.createConnection()) {
            var sql = "INSERT INTO \"Reader\"(name) VALUES(?)";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, reader.getName());
            var resultSet = statement.executeUpdate();
            return reader;
        } catch (SQLException sqlException) {
            return null;
        }
    }

    /**
     * @return
     */
    @Override
    public List<Reader> findAll() {
        var connection = connectionService.createConnection();
        try {
            var statement = connection.prepareStatement("Select * from \"Reader\"");
            var resultSet = statement.executeQuery();
            List<Reader> readerList = new ArrayList<>();
            while (resultSet.next()) {
                readerList.add(new Reader(resultSet.getInt(1),
                        resultSet.getString(2)));
            }
            connection.close();
            if (readerList.size() != 0) {
                return readerList;
            } else return null;
        } catch (
                SQLException sqlException) {
            return null;
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public Reader findById(long readerId) {
        try (var connection = connectionService.createConnection()) {
            var statement =
                    connection.prepareStatement("SELECT * FROM \"Reader\" WHERE id = ?");
            statement.setLong(1, readerId);
            var resultSet = statement.executeQuery();
            resultSet.next();
            var reader = new Reader(resultSet.getLong(1), resultSet.getString(2));
            statement.close();
            return reader;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public List<Reader> findAllByBookId(long bookId) {
        List<Reader> readers = new ArrayList<>();
        try (var connection = connectionService.createConnection()) {
            var statement =
                    connection.prepareStatement("SELECT r.id, r.name FROM \"Reader\" r\n" +
                            "    LEFT JOIN \"Borrow\" bor on r.id = bor.reader_id\n" +
                            "    LEFT JOIN \"Book\" b on bor.book_id = b.id\n" +
                            "WHERE b.id = ?");
            statement.setLong(1, bookId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) readers.add(new Reader(resultSet.getLong(1), resultSet.getString(2)));
            statement.close();
            return readers;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return readers;
        }
    }
}
