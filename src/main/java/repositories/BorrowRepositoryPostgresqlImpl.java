package repositories;

import entities.Borrow;
import entities.BorrowMapper;
import exceptions.BorrowDaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class BorrowRepositoryPostgresqlImpl implements BorrowRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BorrowRepositoryPostgresqlImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * @param bookId
     * @param readerId
     * @return
     */
    @Override
    public Optional<Borrow> save(long bookId, long readerId) {
        var addNewBorrowSql = "INSERT INTO borrow(reader_id, book_id) VALUES(?, ?)";
        var changed = jdbcTemplate.update(addNewBorrowSql, readerId, bookId);
        if (changed == 1) {
            return findBorrowByReaderIdAndBookId(readerId, bookId);
        } else throw new BorrowDaoException("by reader Id: "
                + readerId + " and book Id: " + bookId + "!\nThis reader is borrow this book!");
    }

    /**
     * @param bookId
     * @param readerId
     * @return
     */
    private Optional<Borrow> findBorrowByReaderIdAndBookId(long readerId, long bookId) {
        var findBorrowByReaderIdAndBookIdSql = """
                SELECT
                b.id AS bookId, b.name AS bookName, b.author AS bookAuthor,
                r.id AS readerId, r.name AS readerName
                FROM borrow bor
                    JOIN book b on b.id = bor.book_id
                    JOIN reader r on r.id = bor.reader_id WHERE bor.reader_id = ? AND bor.book_id = ?""";
        Borrow borrow = jdbcTemplate.queryForObject(findBorrowByReaderIdAndBookIdSql,
                new BorrowMapper(),
                readerId,
                bookId);
        return Optional.ofNullable(borrow);
    }

    /**
     * @return
     */
    @Override
    public List<Borrow> findAll() {
        var findAllBorrowsSql = """
                SELECT
                b.id AS bookId, b.name AS bookName, b.author AS bookAuthor,
                r.id AS readerId, r.name AS readerName
                FROM borrow bor
                    JOIN book b ON b.id = bor.book_id
                    JOIN reader r ON r.id = bor.reader_id""";
        return jdbcTemplate.query(findAllBorrowsSql, new BorrowMapper());
    }

    /**
     * @param readerId
     * @param bookId
     */
    @Override
    public int returnBook(long readerId, long bookId) {
        var deleteBorrowSql = """
                DELETE FROM borrow
                WHERE reader_id = ? AND book_id = ?""";
        return jdbcTemplate.update(deleteBorrowSql, readerId, bookId);
    }

    /**
     * @return
     */
    public List<Borrow> findAllReadersWithTheirBorrows() {
        var findAllReadersAndTheirBorrowsSql = """
                SELECT
                b.id AS bookId, b.name AS bookName, b.author AS bookAuthor,
                r.id AS readerId, r.name AS readerName
                FROM reader r
                    LEFT JOIN borrow bor on r.id = bor.reader_id
                    LEFT JOIN book b on b.id = bor.book_id
                ORDER BY reader_id""";
        return jdbcTemplate.query(findAllReadersAndTheirBorrowsSql, new BorrowMapper());
    }

    /**
     * @return
     */
    public List<Borrow> findAllBooksWithTheirBorrowers() {
        var findAllBooksAndTheirBorrowersSql = """
                SELECT
                b.id AS book_id, b.name AS book_name, b.author AS book_author,
                r.id AS reader_id, r.name AS reader_name
                FROM book b
                    LEFT JOIN borrow bor on b.id = bor.book_id
                    LEFT JOIN reader r on r.id = bor.reader_id
                ORDER BY book_id""";
        return jdbcTemplate.query(findAllBooksAndTheirBorrowersSql, new BorrowMapper());
    }
}
