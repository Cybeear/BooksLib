package dao;

import entity.Book;
import entity.Borrow;
import entity.Reader;
import service.ConnectionService;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BorrowBorrowDaoJdbcImpl implements BorrowDao {
    private final ConnectionService connectionService = new ConnectionService();

    /**
     * @param bookId
     * @param readerId
     * @return
     */
    @Override
    public Optional<Borrow> save(long bookId, long readerId) {
        Borrow borrow = null;
        try (var connection = connectionService.createConnection()) {
            var statement =
                    connection.prepareStatement("INSERT INTO Borrow(reader_id, book_id) VALUES(?, ?)");
            statement.setLong(1, readerId);
            statement.setLong(2, bookId);
            statement.executeUpdate();
            statement.close();
            statement =
                    connection.prepareStatement("SELECT b.id, b.name, b.author, r.id, r.name " +
                            "from Borrow bor JOIN Book b on b.id = bor.book_id " +
                            "JOIN Reader r on r.id = bor.reader_id WHERE reader_id = ? AND book_id = ?");
            statement.setLong(1, readerId);
            statement.setLong(2, bookId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                Reader reader = new Reader(resultSet.getInt(4),
                        resultSet.getString(5));
                borrow = new Borrow(book, reader);
            }
            resultSet.close();
            statement.close();
            return Optional.ofNullable(borrow);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.ofNullable(borrow);
        }
    }

    /**
     * @return
     */
    @Override
    public List<Borrow> findAll() {
        List<Borrow> borrowList = new LinkedList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement("SELECT b.id, b.name, b.author, r.id, r.name FROM" +
                     " Borrow bor JOIN Book b ON b.id = bor.book_id " +
                     "JOIN Reader r ON r.id = bor.reader_id");
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                Reader reader = new Reader(resultSet.getInt(4),
                        resultSet.getString(5));
                borrowList.add(new Borrow(book, reader));
            }
            return borrowList;
        } catch (SQLException sqlException) {
            return borrowList;
        }
    }

    /**
     * @param bookId
     * @param readerId
     */
    @Override
    public void returnBook(long bookId, long readerId) {
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement("DELETE FROM Borrow " +
                     "WHERE reader_id = ? AND book_id = ?")) {
            statement.setLong(1, readerId);
            statement.setLong(2, bookId);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("SqlError: " + sqlException.getMessage());
        }
    }


}
