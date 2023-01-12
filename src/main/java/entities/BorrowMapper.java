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

        try {
            book.setId(resultSet.getLong("book_id"));
            book.setName(resultSet.getString("book_name"));
            book.setAuthor(resultSet.getString("book_author"));
        } catch (SQLException sqlException) {
            book = null;
        }
        try {
            reader.setId(resultSet.getLong("reader_id"));
            reader.setName(resultSet.getString("reader_name"));
        } catch (SQLException sqlException) {
            reader = null;
        }

        borrow.setBook(book);
        borrow.setReader(reader);
        return borrow;
    }
}
