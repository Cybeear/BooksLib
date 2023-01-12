package repositories;

import entities.Borrow;
import entities.BorrowMapper;
import exceptions.BorrowRepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
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
    public Optional<Borrow> save(Long bookId, Long readerId) {
        var addNewBorrowSql = "INSERT INTO borrow(reader_id, book_id) VALUES(?, ?)";
        try {
            var changed = jdbcTemplate.update(addNewBorrowSql, readerId, bookId);
            if (changed == 1) {
                return findBorrowByReaderIdAndBookId(readerId, bookId);
            } else throw new BorrowRepositoryException("by reader Id: "
                    + readerId + " and book Id: " + bookId + "!\nThis reader is borrow this book!");
        } catch (DataAccessException dataAccessException) {
            throw new BorrowRepositoryException("by reader Id: "
                    + readerId + " and book Id: " + bookId + "!\nThis reader is borrow this book!"
                    + dataAccessException.getLocalizedMessage());
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
                b.id AS book_id, b.name AS book_name, b.author AS book_author,
                r.id AS reader_id, r.name AS reader_name
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
                b.id AS book_id, b.name AS book_name, b.author AS book_author,
                r.id AS reader_id, r.name AS reader_name
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
    public int returnBook(Long readerId, Long bookId) {
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
                b.id AS book_id, b.name AS book_name, b.author AS book_author,
                r.id AS reader_id, r.name AS reader_name
                FROM reader r
                    LEFT JOIN borrow bor on r.id = bor.reader_id
                    LEFT JOIN book b on b.id = bor.book_id
                ORDER BY book_id""";
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
                ORDER BY reader_id""";
        return jdbcTemplate.query(findAllBooksAndTheirBorrowersSql, new BorrowMapper());
    }
}
