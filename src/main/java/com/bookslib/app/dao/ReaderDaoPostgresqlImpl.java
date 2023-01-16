package com.bookslib.app.dao;

import com.bookslib.app.entity.Reader;
import com.bookslib.app.exceptions.ReaderDaoException;
import com.bookslib.app.mapper.ReaderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class ReaderDaoPostgresqlImpl implements ReaderDao {
    private JdbcTemplate jdbcTemplate;

    /**
     * @param reader
     * @return
     */
    @Override
    public Reader save(Reader reader) {
        var addNewBookSql = "INSERT INTO reader(name) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(addNewBookSql,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, reader.getName());
                return preparedStatement;
            }, keyHolder);
            reader.setId((Integer) keyHolder.getKeys().get("id"));
            return reader;
        } catch (DataAccessException dataAccessException) {
            log.error("Error save reader! \t {}", reader);
            throw new ReaderDaoException(dataAccessException.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @Override
    public List<Reader> findAll() {
        var findAllBooksSql = "SELECT * FROM reader";
        try {
            return jdbcTemplate.query(findAllBooksSql, new ReaderMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set!");
            return new ArrayList<>();
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
            return Optional.ofNullable(jdbcTemplate.queryForObject(findReaderByIdSql,
                    new ReaderMapper(), readerId));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set! \t reader id - {}", readerId);
            return Optional.empty();
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
            return jdbcTemplate.query(findAllReadersByBookIdSql,
                    new ReaderMapper(), bookId);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.warn("Statement return empty result set! \t book id - {}", bookId);
            return new ArrayList<>();
        }
    }
}
