package dao;

import entity.Book;
import exceptions.BookDaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookDaoPostgresqlImplTest {

    private BookDao bookDao;

    @BeforeEach
    void beforeEach() {
        bookDao = new BookDaoPostgresqlImpl();
    }

    @Test
    void saveThenBookNotNull() {
        Book book = new Book("test", "test");
        assertNotNull(bookDao.save(book),
                "DAO can not return (book == null) after successful save to DB!");
    }

    @Test
    void saveThenJdbcConnectionNull() {
        bookDao = new BookDaoPostgresqlImpl(null);
        assertThrows(NullPointerException.class, () -> bookDao.save(null),
                "DAO can not throw 'SqlException' in 'BookDaoException' if JDBC driver is null!");
    }

    @Test
    void findAllIfDatabaseNotEmpty() {
        var books = bookDao.findAll();
        assertFalse(books.isEmpty());
    }

    @Test
    void findByExistedId() {
        var book = bookDao.findById(1);
        assertAll(() -> assertTrue(book.isPresent()),
                () -> assertNotNull(book.get()));
    }

    @Test
    void findByNotExistedId() {
        var book = bookDao.findById(1000000);
        assertFalse(book.isPresent());
    }
}