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
        book.setId(resultSet.getLong("book.id"));
        book.setName(resultSet.getString("book.name"));
        book.setAuthor(resultSet.getString("book.author"));
        Reader reader = new Reader();
        reader.setId(resultSet.getLong("reader.id"));
        reader.setName(resultSet.getString("reader.name"));
        borrow.setBook(book);
        borrow.setReader(reader);
        return borrow;
    }
}
