package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {

    private LibraryService service;


    @BeforeEach
    void setUp() {
        service = new LibraryService();
        service.createData(10);
        assertEquals(10, service.getBooks().size());
        assertEquals(10, service.getReaders().size());
    }

    @Test
    void registerReaderWithValidDataTest() {
        service.registerReader("TestReader");
        assertEquals(11, service.getReaders().size());
    }

    @Test
    void registerReaderInWithValidDataTest() {
        service.registerReader(" ");
        assertEquals(10, service.getReaders().size());
    }

    @Test
    void addBookWithValidDataTest() {
        service.addBook("TestBook / TestAuthor");
        assertEquals(11, service.getBooks().size());
    }

    @Test
    void addBookWithInValidDataTest() {
        service.addBook("TestBook");
        assertEquals(10, service.getBooks().size());
    }

    @Test
    void borrowABookWithInValidDataTest() {
        service.borrowABook("1002 / test");
        assertEquals(0, service.getBorrows().size());
        service.borrowABook(" ");
        assertEquals(0, service.getBorrows().size());
        service.borrowABook("test");
        assertEquals(0, service.getBorrows().size());
    }

    @Test
    void returnBorrowedBookWithValidDataTest() {
        service.borrowABook("1062 / 1062");
        service.borrowABook("1062 / 1063");
        service.borrowABook("1062 / 1064");
        service.returnBorrowedBook("1062 / 1063");
        assertEquals(2, service.getBorrows().size());
    }

    @Test
    void returnBorrowedBookWithInValidDataTest() {
        service.returnBorrowedBook("1002 / 1030");
        assertEquals(0, service.getBorrows().size());
        service.returnBorrowedBook("test");
        assertEquals(0, service.getBorrows().size());
        service.returnBorrowedBook("test / test1");
        assertEquals(0, service.getBorrows().size());
    }
}