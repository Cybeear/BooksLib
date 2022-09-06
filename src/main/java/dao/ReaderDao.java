package dao;

import entity.Reader;
import service.ConnectionService;

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
     * @return Reader object if exist
     */
    @Override
    public Reader fetchById(long id) {
        try (var connection = connectionService.createConnection()) {
            var statement = connection.prepareStatement("Select * from \"Reader\" where id = ?");
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            resultSet.next();
            return new Reader(resultSet.getInt(1),
                    resultSet.getString(2));
        } catch (SQLException sqlException) {
            return null;
        }

    }

    /**
     * @param obj Reader object
     * @return boolean true if operation success
     */
    @Override
    public boolean addNew(Object obj) {
        try (var connection = connectionService.createConnection()) {
            var sql = "INSERT INTO \"Reader\"(name) VALUES(?)";
            var statement = connection.prepareStatement(sql);
            var reader = (Reader) obj;
            statement.setString(1, reader.getName());
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }

    /**
     * @param obj Reader object
     * @return boolean if operation success
     */
    @Override
    public boolean deleteRecord(Object obj) {
        try (var connection = connectionService.createConnection()) {
            var sql = "DELETE FROM \"Reader\" WHERE id = ?";
            var statement = connection.prepareStatement(sql);
            var reader = (Reader) obj;
            statement.setLong(1, reader.getId());
            var resultSet = statement.executeUpdate();
            return resultSet == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }
}
