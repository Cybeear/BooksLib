package entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowMapper implements RowMapper<Borrow> {
    /**
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
        var a = resultSet.getMetaData();
        if (resultSet.next()) {
            try {
                book.setId(resultSet.getLong("bookId"));
                book.setName(resultSet.getString("bookName"));
                book.setAuthor(resultSet.getString("bookAuthor"));
            } catch (SQLException sqlException) {
                book = null;
            }
            try {
                reader.setId(resultSet.getLong("readerId"));
                reader.setName(resultSet.getString("readerName"));
            } catch (SQLException sqlException) {
                reader = null;
            }
        }
        borrow.setBook(book);
        borrow.setReader(reader);
        return borrow;
    }
}
