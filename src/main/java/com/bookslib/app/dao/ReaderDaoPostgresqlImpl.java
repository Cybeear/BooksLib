package com.bookslib.app.dao;

import com.bookslib.app.entity.Reader;
import com.bookslib.app.exceptions.BookDaoException;
import com.bookslib.app.exceptions.ReaderDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReaderDaoPostgresqlImpl implements ReaderDao {
    private final JdbcTemplate jdbcTemplate;

    public Reader mapToReader(ResultSet resultSet, int rowNum) throws SQLException {
        var reader = new Reader();
        reader.setId(resultSet.getLong("id"));
        reader.setName(resultSet.getString("name"));
        return reader;
    }

    /**
     * @param reader
     * @return
     */
    @Override
    public Reader save(Reader reader) {
        var addNewReaderSql = "INSERT INTO reader(name) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(addNewReaderSql,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, reader.getName());
                return preparedStatement;
            }, keyHolder);
            reader.setId((Integer) keyHolder.getKeys().get("id"));
            return reader;
        } catch (NullPointerException nullPointerException) {
            log.error("Error, returned generated keys are null! Reader data: {}", reader);
            throw new ReaderDaoException(nullPointerException.getLocalizedMessage());
        } catch (DataAccessException dataAccessException) {
            log.error("Error save reader!\t{}", reader);
            throw new ReaderDaoException(dataAccessException.getLocalizedMessage());
        }
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public Optional<Reader> findById(long readerId) {
        var findReaderByIdSql = "SELECT * FROM reader WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    findReaderByIdSql,
                    this::mapToReader,
                    readerId));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.info("Error fetch reader from DB ResultSet return 0 rows!");
            return Optional.empty();
        } catch (DataAccessException dataAccessException) {
            log.error("Error fetch reader from DB!\t book id - {}", readerId);
            throw new ReaderDaoException(dataAccessException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @Override
    public List<Reader> findAll() {
        var findAllReadersSql = "SELECT * FROM reader";
        try {
            return jdbcTemplate.query(findAllReadersSql, this::mapToReader);
        } catch (DataAccessException dataAccessException) {
            log.error("Error fetch reader from DB!");
            throw new ReaderDaoException(dataAccessException.getLocalizedMessage());
        }
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public List<Reader> findAllByBookId(long bookId) {
        var findAllReadersByBookIdSql = """
                SELECT
                r.id,
                r.name
                FROM reader r
                    LEFT JOIN borrow bor ON r.id = bor.reader_id
                WHERE bor.book_id = ?""";
        try {
            return jdbcTemplate.query(findAllReadersByBookIdSql, this::mapToReader, bookId);
        } catch (DataAccessException dataAccessException) {
            log.error("Error fetch readers from DB!\t book id - {}", bookId);
            throw new ReaderDaoException(dataAccessException.getLocalizedMessage());
        }
    }
}
