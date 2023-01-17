package com.bookslib.app.dao;

import com.bookslib.app.entity.Book;
import com.bookslib.app.entity.Borrow;
import com.bookslib.app.entity.Reader;
import com.bookslib.app.exceptions.BorrowDaoException;
import com.bookslib.app.service.ConnectionService;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class BorrowDaoPostgresqlImpl implements BorrowDao {
    private final ConnectionService connectionService;

    public BorrowDaoPostgresqlImpl() {
        connectionService = new ConnectionService();
    }

    public BorrowDaoPostgresqlImpl(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    /**
     * @param bookId
     * @param readerId
     * @return
     */
    @Override
    public Optional<Borrow> save(long bookId, long readerId) {
        var addNewBorrowSql = "INSERT INTO borrow(reader_id, book_id) VALUES(?, ?)";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(addNewBorrowSql)) {
            statement.setLong(1, readerId);
            statement.setLong(2, bookId);
            statement.executeUpdate();
            return findBorrowByReaderIdAndBookId(readerId, bookId);
        } catch (SQLException sqlException) {
            throw new BorrowDaoException("by reader Id: "
                    + readerId + " and book Id: " + bookId + "!\nThis reader is borrow this book!" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @param readerId
     * @return
     */
    private Optional<Borrow> findBorrowByReaderIdAndBookId(long readerId, long bookId) {
        var findBorrowByReaderIdAndBookIdSql = """
                SELECT
                b.id, b.name, b.author,
                r.id, r.name
                FROM borrow bor
                    JOIN book b on b.id = bor.book_id
                    JOIN reader r on r.id = bor.reader_id WHERE bor.reader_id = ? AND bor.book_id = ?""";
        Borrow borrow = null;
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(findBorrowByReaderIdAndBookIdSql)) {
            statement.setLong(1, readerId);
            statement.setLong(2, bookId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var book = new Book();
                book.setId(resultSet.getLong(1));
                book.setName(resultSet.getString(2));
                book.setAuthor(resultSet.getString(3));
                var reader = new Reader();
                reader.setId(resultSet.getInt(4));
                reader.setName(resultSet.getString(5));
                borrow = new Borrow(book, reader);
            }
            resultSet.close();
            return Optional.ofNullable(borrow);
        } catch (SQLException sqlException) {
            throw new BorrowDaoException("Failed to find borrowed data by reader Id"
                    + readerId + " and book Id: " + bookId + "!\n" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @Override
    public List<Borrow> findAll() {
        var findAllBorrowsSql = """
                SELECT
                b.id, b.name, b.author,
                r.id, r.name
                FROM borrow bor
                    JOIN book b ON b.id = bor.book_id
                    JOIN reader r ON r.id = bor.reader_id""";
        List<Borrow> borrowList = new LinkedList<>();
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(findAllBorrowsSql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var book = new Book();
                book.setId(resultSet.getLong(1));
                book.setName(resultSet.getString(2));
                book.setAuthor(resultSet.getString(3));
                var reader = new Reader();
                reader.setId(resultSet.getInt(4));
                reader.setName(resultSet.getString(5));
                borrowList.add(new Borrow(book, reader));
            }
            return borrowList;
        } catch (SQLException sqlException) {
            throw new BorrowDaoException("Failed to find borrowed data!\n"
                    + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @param readerId
     */
    @Override
    public int returnBook(long bookId, long readerId) {
        var deleteBorrowSql = """
                DELETE FROM borrow
                WHERE reader_id = ? AND book_id = ?""";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(deleteBorrowSql)) {
            statement.setLong(1, readerId);
            statement.setLong(2, bookId);
            return statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new BorrowDaoException("Failed to return book with book Id:"
                    + bookId + " and reader Id: " + readerId + "!\n" + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    public List<Borrow> findAllReadersWithTheirBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        var findAllReadersAndTheirBorrowsSql = """
                SELECT
                b.id AS b_id, b.name AS b_name, b.author AS b_author,
                r.id AS r_id, r.name AS r_name
                FROM reader r
                    LEFT JOIN borrow bor on r.id = bor.reader_id
                    LEFT JOIN book b on b.id = bor.book_id
                ORDER BY reader_id""";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(findAllReadersAndTheirBorrowsSql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var reader = new Reader();
                var book = new Book();
                reader.setId(resultSet.getLong("r_id"));
                reader.setName(resultSet.getString("r_name"));
                if (resultSet.getLong("b_id") != 0) {
                    book.setId(resultSet.getLong("b_id"));
                    book.setName(resultSet.getString("b_name"));
                    book.setAuthor(resultSet.getString("b_author"));
                }
                borrows.add(new Borrow(book, reader));
            }
            return borrows;
        } catch (SQLException sqlException) {
            throw new BorrowDaoException("SqlError: " + sqlException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    public List<Borrow> findAllBooksWithTheirBorrowers() {
        List<Borrow> borrows = new ArrayList<>();
        var findAllBooksAndTheirBorrowersSql = """
                SELECT
                b.id AS b_id, b.name AS b_name, b.author AS b_author,
                r.id AS r_id, r.name AS r_name
                FROM book b
                    LEFT JOIN borrow bor on b.id = bor.book_id
                    LEFT JOIN reader r on r.id = bor.reader_id
                ORDER BY book_id""";
        try (var connection = connectionService.createConnection();
             var statement = connection.prepareStatement(findAllBooksAndTheirBorrowersSql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var book = new Book();
                book.setId(resultSet.getLong("b_id"));
                book.setName(resultSet.getString("b_name"));
                book.setAuthor(resultSet.getString("b_author"));
                var reader = new Reader();
                if (resultSet.getLong("r_id") != 0) {
                    reader.setId(resultSet.getLong("r_id"));
                    reader.setName(resultSet.getString("r_name"));
                }
                borrows.add(new Borrow(book, reader));
            }
            return borrows;
        } catch (SQLException sqlException) {
            throw new BorrowDaoException("SqlError: " + sqlException.getLocalizedMessage());
        }
    }
}
