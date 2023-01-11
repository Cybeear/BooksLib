package entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowMapper implements RowMapper<Borrow> {
/**
*
   * @param resultSet
   * @param rowNum
   * @return
 * @throws SQLException
*/
    @Override
    public Borrow mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Borrow borrow = new Borrow();
        Book book = new Book();
        Reader reader = new Reader();

        try{
            book.setId(resultSet.getLong("b.id"));
            book.setName(resultSet.getString("b.name"));
            book.setAuthor(resultSet.getString("b.author"));
        } catch (SQLException sqlException){
            book = null;
        }
        try{
            reader.setId(resultSet.getLong("r.id"));
            reader.setName(resultSet.getString("r.name"));
        } catch (SQLException sqlException){
            reader = null;
        }

        borrow.setBook(book);
        borrow.setReader(reader);
        return borrow;
    }
}
