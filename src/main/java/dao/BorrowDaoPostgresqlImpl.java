package dao;

import entity.Book;
import entity.Borrow;
import entity.Reader;
import service.ConnectionServicePostgresImpl;
import service.ConnectionServiceInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BorrowDaoPostgresqlImpl implements BorrowDao {
    private final ConnectionServiceInterface connectionService = new ConnectionServicePostgresImpl();

    /**
     * @param bookId
     * @param readerId
     * @return
     */
    @Override
    public Borrow save(long bookId, long readerId) {
        var sql = "INSERT INTO borrow(reader_id, book_id) VALUES(?, ?)";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, readerId);
            statement.setLong(2, bookId);
            statement.executeUpdate();
            return findBorrowByReaderIdAndBookId(readerId, bookId);
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to save borrow reader Id: "
                    + readerId + " and book Id: " + bookId + "!\n" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @param readerId
     * @return
     */
    private Borrow findBorrowByReaderIdAndBookId(long readerId, long bookId) {
        var sql = "SELECT b.id, b.name, b.author, r.id, r.name " +
                "from borrow bor JOIN book b on b.id = bor.book_id " +
                "JOIN reader r on r.id = bor.reader_id WHERE reader_id = ? AND book_id = ?";
        Borrow borrow = null;
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql)) {
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
            return borrow;
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to find borrowed data by reader Id"
                    + readerId + " and book Id: " + bookId + "!\n" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @Override
    public List<Borrow> findAll() {
        var sql = "SELECT b.id, b.name, b.author, r.id, r.name FROM" +
                " borrow bor JOIN book b ON b.id = bor.book_id " +
                "JOIN reader r ON r.id = bor.reader_id";
        List<Borrow> borrowList = new LinkedList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var book = new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                var reader = new Reader(resultSet.getInt(4),
                        resultSet.getString(5));
                borrowList.add(new Borrow(book, reader));
            }
            return borrowList;
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to find borrowed data!\n"
                    + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public List<Borrow> findAllBorrowedByReaderId(long readerId) {
        var sql = "SELECT b.id, b.name, b.author, r.id, r.name FROM" +
                " borrow bor JOIN book b ON b.id = bor.book_id " +
                "JOIN reader r ON r.id = bor.reader_id WHERE r.id = ?";
        List<Borrow> borrows = new LinkedList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, readerId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var book = new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                var reader = new Reader(resultSet.getInt(4),
                        resultSet.getString(5));
                borrows.add(new Borrow(book, reader));
            }
            return borrows;
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to find borrow data by reader Id: "
                    + readerId + "!\n" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public List<Borrow> findAllBorrowedByBookId(long bookId) {
        var sql = "SELECT b.id, b.name, b.author, r.id, r.name FROM" +
                " borrow bor JOIN book b ON b.id = bor.book_id " +
                "JOIN reader r ON r.id = bor.reader_id WHERE b.id = ?";
        List<Borrow> borrows = new LinkedList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bookId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var book = new Book(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                var reader = new Reader(resultSet.getInt(4),
                        resultSet.getString(5));
                borrows.add(new Borrow(book, reader));
            }
            return borrows;
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to find data by book Id: "
                    + bookId + "!\n" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @param readerId
     */
    @Override
    public void returnBook(long bookId, long readerId) {
        var sql = "DELETE FROM borrow " +
                "WHERE reader_id = ? AND book_id = ?";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, readerId);
            statement.setLong(2, bookId);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to return book with book Id:"
                    + bookId + " and reader Id: " + readerId + "!\n" + sqlException.getMessage());
        }
    }

    /**
     * @return 
     */
    public List<Borrow> findAllReadersWithTheirBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        var sql = "SELECT r.id   AS reader_id, r.name AS reader_name, b.id   AS book_id, b.name AS book_name, b.author AS book_author\n" +
                "FROM reader r LEFT JOIN borrow bor on r.id = bor.reader_id LEFT JOIN book b on b.id = bor.book_id\n" +
                "ORDER BY reader_id";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Book book = null;
                Reader reader = new Reader(resultSet.getLong(1),
                        resultSet.getString(2));
                if (resultSet.getLong("book_id") != 0) {
                    book = new Book(resultSet.getLong(3),
                            resultSet.getString(4),
                            resultSet.getString(5));
                }
                borrows.add(new Borrow(book, reader));

            }
            return borrows;
        } catch (SQLException sqlException) {
            throw new RuntimeException("SqlError: " + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    public List<Borrow> findAllBooksWithTheirBorrowers() {
        List<Borrow> borrows = new ArrayList<>();
        var sql = "SELECT r.id   AS reader_id, r.name AS reader_name, b.id   AS book_id, b.name AS book_name, b.author AS book_author\n" +
                "FROM book b LEFT JOIN borrow bor on b.id = bor.book_id LEFT JOIN reader r on r.id = bor.reader_id\n" +
                "ORDER BY book_id";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Book book = new Book(resultSet.getLong(3),
                        resultSet.getString(4),
                        resultSet.getString(5));
                Reader reader = null;
                if (resultSet.getLong("reader_id") != 0) {
                    reader = new Reader(resultSet.getLong(1),
                            resultSet.getString(2));

                }
                borrows.add(new Borrow(book, reader));

            }
            return borrows;
        } catch (SQLException sqlException) {
            throw new RuntimeException("SqlError: " + sqlException.getLocalizedMessage());
        }
    }
}
