package dao;

import service.ConnectionService;
import service.ParserService;
import entity.Reader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReaderDao implements Dao {
    private ConnectionService connectionService = new ConnectionService();


    /**
     * @return list of Reader objects or null
     */
    @Override
    public List<Reader> fetchAll() {
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
     * @param id integer field
     * @return Reader object or null if not exists
     */
    @Override
    public Reader fetchById(int id) {
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("Select * from \"Reader\" where id = ?");
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            resultSet.next();
            return new Reader(resultSet.getInt(1),
                    resultSet.getString(2));
        } catch (SQLException sqlException) {
            return null;
        }

    }

    /**
     * @param str String field name of Reader
     * @return boolean true if operation successful
     */
    @Override
    public boolean addNew(String str) {
        try (var connection = connectionService.createConnection()) {
            var sql = "INSERT INTO \"Reader\"(name) VALUES(?)";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, str);
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }

    /**
     * @param str string of id, parsed to integer
     * @return boolean if operation successful
     */
    @Override
    public boolean deleteRecord(String str) {
        try (var connection = connectionService.createConnection()) {
            var sql = "DELETE FROM \"Reader\" WHERE id = ?";
            var statement = connection.prepareStatement(sql);
            var parsed = ParserService.parseInt(str);
            if (parsed == -1) return false;
            statement.setInt(1, parsed);
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }
}
