package dao;

import entity.Reader;
import service.ConnectionService;

import java.sql.SQLException;
import java.sql.Statement;
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
        Reader newReader = null;
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("INSERT INTO reader(id, name) " +
                            "VALUES(Default, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, reader.getName());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                newReader = new Reader(resultSet.getLong(1), resultSet.getString(2));
                resultSet.close();
                statement.close();
            }
            return Optional.ofNullable(newReader);
        } catch (SQLException sqlException) {
            System.err.println("SqlError: " + sqlException.getMessage());
            return Optional.ofNullable(newReader);
        }
    }

    /**
     * @return
     */
    @Override
    public List<Reader> findAll() {
        List<Reader> readerList = new ArrayList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement("SELECT * FROM reader");
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) readerList.add(new Reader(resultSet.getInt(1),
                    resultSet.getString(2)));
            return readerList;
        } catch (SQLException sqlException) {
            System.err.println("SqlError: " + sqlException.getMessage());
            return readerList;
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public Optional<Reader> findById(long readerId) {
        Reader reader = null;
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement("SELECT * FROM reader WHERE id = ?")) {
            statement.setLong(1, readerId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) reader = new Reader(resultSet.getLong(1), resultSet.getString(2));
            resultSet.close();
            return Optional.ofNullable(reader);
        } catch (SQLException sqlException) {
            System.err.println("SqlError: " + sqlException.getMessage());
            return Optional.ofNullable(reader);
        }
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public List<Reader> findAllByBookId(long bookId) {
        List<Reader> readers = new ArrayList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement("SELECT r.id, r.name FROM reader r\n" +
                     "    LEFT JOIN borrow bor ON r.id = bor.reader_id\n" +
                     "    LEFT JOIN book b ON bor.book_id = b.id\n" +
                     "WHERE b.id = ?")) {
            statement.setLong(1, bookId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) readers.add(new Reader(resultSet.getLong(1),
                    resultSet.getString(2)));
            resultSet.close();
            return readers;
        } catch (SQLException sqlException) {
            System.err.println("SqlError: " + sqlException.getMessage());
            return readers;
        }
    }
}
