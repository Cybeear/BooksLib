package repositories;

import entities.Reader;
import entities.ReaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReaderRepositoryPostgresqlImpl implements ReaderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReaderRepositoryPostgresqlImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * @param reader
     * @return
     */
    @Override
    public Reader save(Reader reader) {
        var addNewBookSql = "INSERT INTO reader(name) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(addNewBookSql,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, reader.getName());
                return preparedStatement;
            }
        }, keyHolder);
        reader.setId((Integer) keyHolder.getKeys().get("id"));
        return reader;
    }

    /**
     * @return
     */
    @Override
    public List<Reader> findAll() {
        var findAllReadersSql = "SELECT * FROM reader";
        return jdbcTemplate.query(findAllReadersSql, new ReaderMapper());
    }

    /**
     * @param readerId
     * @return
     */
    @Override
    public Optional<Reader> findById(long readerId) {
        var findReaderByIdSql = "SELECT * FROM reader WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(findReaderByIdSql,
                new ReaderMapper(),
                readerId));
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
        return jdbcTemplate.query(findAllReadersByBookIdSql, new ReaderMapper(), bookId);
    }
}
