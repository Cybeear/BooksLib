package dao;

import entity.Reader;
import service.ConnectionServicePostgresImpl;
import service.ConnectionServiceInterface;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDaoPostgresqlImpl implements ReaderDao {
    private final ConnectionServiceInterface connectionService = new ConnectionServicePostgresImpl();

    /**
     * @param reader
     * @return
     */
    @Override
    public Reader save(Reader reader) {
        var sql = "INSERT INTO reader(name) VALUES(?)";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, reader.getName());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                reader.setId(resultSet.getLong(1));
            }
            resultSet.close();
            return reader;
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to save reader [" + reader + "]!\n" +
                    sqlException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @Override
    public List<Reader> findAll() {
        var sql = "SELECT * FROM reader";
        List<Reader> readerList = new ArrayList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                readerList.add(new Reader(resultSet.getInt(1),
                        resultSet.getString(2)));
            }
            return readerList;
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to find books!\n"
                    + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public Optional<Reader> findById(long readerId) {
        var sql = "SELECT * FROM reader WHERE id = ?";
        Reader reader = null;
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, readerId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                reader = new Reader(resultSet.getLong(1), resultSet.getString(2));
            }
            resultSet.close();
            return Optional.ofNullable(reader);
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to find reader by Id: "
                    + readerId + "!\n" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public List<Reader> findAllByBookId(long bookId) {
        var sql = "SELECT r.id, r.name FROM reader r\n" +
                "    LEFT JOIN borrow bor ON r.id = bor.reader_id\n" +
                "    LEFT JOIN book b ON bor.book_id = b.id\n" +
                "WHERE b.id = ?";
        List<Reader> readers = new ArrayList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bookId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                readers.add(new Reader(resultSet.getLong(1),
                        resultSet.getString(2)));
            }
            resultSet.close();
            return readers;
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to find readers by book Id: "
                    + bookId + "!\n" + sqlException.getLocalizedMessage());
        }
    }
}
