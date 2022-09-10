package dao;

import entity.Reader;
import service.ConnectionService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ReaderDaoJdbcImpl implements ReaderDao {
    private final ConnectionService connectionService = new ConnectionService();

    /**
     * @param reader
     * @return
     */
    @Override
    public Optional<Reader> save(Reader reader) {
        try (var connection = connectionService.createConnection()) {
            var sql = "INSERT INTO \"Reader\"(name) VALUES(?)";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, reader.getName());
            var resultSet = statement.executeUpdate();
            statement.close();
            statement = connection.prepareStatement("SELECT * FROM \"Reader\" WHERE name = ?");
            var resultSet1 = statement.executeQuery();
            resultSet1.next();
            var newReader = new Reader(resultSet1.getLong(1), resultSet1.getString(2));
            resultSet1.close();
            statement.close();
            return Optional.of(newReader);
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    /**
     * @return
     */
    @Override
    public List<Reader> findAll() {
        var connection = connectionService.createConnection();
        List<Reader> readerList = new ArrayList<>();
        try {
            var statement = connection.prepareStatement("Select * from \"Reader\"");
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                readerList.add(new Reader(resultSet.getInt(1),
                        resultSet.getString(2)));
            }
            resultSet.close();
            connection.close();
            return readerList;
        } catch (SQLException sqlException) {
            return readerList;
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public Optional<Reader> findById(long readerId) {
        try (var connection = connectionService.createConnection()) {
            var statement =
                    connection.prepareStatement("SELECT * FROM \"Reader\" WHERE id = ?");
            statement.setLong(1, readerId);
            var resultSet = statement.executeQuery();
            resultSet.next();
            var reader = new Reader(resultSet.getLong(1), resultSet.getString(2));
            statement.close();
            return Optional.of(reader);
        } catch (SQLException sqlException) {
            return Optional.empty();
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
            return readers;
        }
    }
}
