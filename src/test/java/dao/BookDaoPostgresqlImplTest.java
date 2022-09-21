package dao;

import entity.Book;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BookDaoPostgresqlImplTest {

    private static BookDaoPostgresqlImpl bookDao;

    @BeforeAll
    static void beforeAll() {
        bookDao = new BookDaoPostgresqlImpl();
    }

    @AfterEach
    static void afterAll() {
        bookDao = new BookDaoPostgresqlImpl();
    }

    @Test
    void saveThenBookNotNull() {
        Book book = new Book("test", "test");
        assertNotNull(bookDao.save(book), "Test creation book passed!");
    }

    @Test
    void saveThenBookNull() {
        assertThrows(NullPointerException.class, () -> bookDao.save(null),
                "Test creation book is null passed!");
    }

    @Test
    void saveThenJdbcConnectionNull() {
        bookDao.setConnectionService(null);
        assertThrows(SQLException.class, () -> bookDao.save(null),
                "Test creation book if JDBC driver is null passed!");

    }

    @Test
    void findAllIfDatabaseNotEmpty() {
        var books = bookDao.findAll();
        assertNotEquals(new ArrayList<Book>(), books);
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
        assertAll(() -> assertFalse(book.isPresent()),
                () -> assertNull(book.get()));
    }


   /* @Test
    void findAllByReaderId() {
        var books = bookDao.findAllByReaderId();

    }*/
}