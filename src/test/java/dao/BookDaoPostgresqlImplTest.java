package dao;

import entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookDaoPostgresqlImplTest {

    private BookDao bookDao;

    @BeforeEach
    void beforeEach() {
        bookDao = new BookDaoPostgresqlImpl();
    }

    @Test
    void saveThenBookNotNull() {
        Book book = new Book("test", "test");
        assertNotNull(bookDao.save(book), "DAO can not return (book == null) after successful save to DB!");
    }

    @Test
    void saveThenBookNull() {
        assertThrows(NullPointerException.class, () -> bookDao.save(null),
                "DAO can not throw exception after receive null to function!");
    }

    @Test
    void saveThenJdbcConnectionNull() {
        bookDao = new BookDaoPostgresqlImpl(null);
        assertThrows(RuntimeException.class, () -> bookDao.save(null),
                "DAO can not throw SqlException in RuntimeException if JDBC driver is null!");
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
        assertAll(() -> assertFalse(book.isPresent()),
                () -> assertTrue(book.isEmpty()));
    }


   /* @Test
    void findAllByReaderId() {
        var books = bookDao.findAllByReaderId();

    }*/
}