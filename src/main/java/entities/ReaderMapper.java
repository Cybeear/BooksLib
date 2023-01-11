package entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderMapper implements RowMapper<Reader> {
/**
*
   * @param resultSet
   * @param rowNum
   * @return
 * @throws SQLException
*/
    @Override
    public Reader mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Reader reader = new Reader();
        reader.setId(resultSet.getLong("id"));
        reader.setName(resultSet.getString("name"));
        return reader;
    }
}
