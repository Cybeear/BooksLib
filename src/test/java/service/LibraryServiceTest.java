package service;

import dao.*;
import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServiceTest {


    private BookDaoPostgresqlImpl bookDao;
    private ReaderDaoPostgresqlImpl readerDao;
    private BorrowDaoPostgresqlImpl borrowDao;
    private static LibraryService libraryService;

    @BeforeEach
    void setUp() {
        bookDao = mock(BookDaoPostgresqlImpl.class);
        readerDao = mock(ReaderDaoPostgresqlImpl.class);
        borrowDao = mock(BorrowDaoPostgresqlImpl.class);
        libraryService = new LibraryService();
        libraryService.setBookDao(bookDao);
        libraryService.setReaderDao(readerDao);
        libraryService.setBorrowDao(borrowDao);
    }

    @DisplayName("Test adding new reader with correct data")
    @Test
    void registerReaderWithCorrectData() {
        var reader = new Reader("name");
        Mockito.doReturn(reader).when(readerDao).save(reader);
        var returnedReader = libraryService.registerReader("name");
        assertAll(() -> assertNotNull(returnedReader),
                () -> assertEquals(returnedReader, reader),
                () -> verify(readerDao, times(1)).save(returnedReader));
    }

    @DisplayName("Test adding new reader with incorrect data")
    @Test
    void registerReaderWithInCorrectData() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.registerReader(" ")),
                () -> verify(readerDao, never()).save(any()));
    }

    @DisplayName("Test adding new book with correct data")
    @Test
    void addBookWithCorrectData() {
        var book = new Book("name", "author");
        Mockito.doReturn(book).when(bookDao).save(book);
        var returnedBook = libraryService.addBook("name / author");
        assertAll(() -> assertNotNull(returnedBook),
                () -> assertEquals(returnedBook, book),
                () -> verify(bookDao, times(1)).save(any()));
    }

    @DisplayName("Test adding new book with incorrect data")
    @Test
    void addBookWithInCorrectData() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.addBook("name")),
                () -> verify(bookDao, never()).save(any()));

    }

    @DisplayName("Test borrow a book method with correct data")
    @Test
    void borrowBookWithCorrectData() {
        var reader = new Reader(5, "test4");
        var book = new Book(5, "test4", "test4");
        var borrow = Optional.of(new Borrow(book, reader));
        Mockito.doReturn(Optional.of(reader)).when(readerDao).findById(anyLong());
        Mockito.doReturn(Optional.of(book)).when(bookDao).findById(anyLong());
        Mockito.doReturn(borrow).when(borrowDao).save(anyLong(), anyLong());
        var returnedBorrow = libraryService.borrowBook("5 / 5");
        assertAll(() -> assertNotNull(returnedBorrow),
                () -> assertEquals(returnedBorrow, borrow.get()),
                () -> verify(readerDao, times(1)).findById(anyLong()),
                () -> verify(bookDao, times(1)).findById(anyLong()),
                () -> verify(borrowDao, times(1)).save(anyLong(), anyLong()));
    }

    @DisplayName("Test borrow a book method with incorrect data")
    @Test
    void borrowBookWithInCorrectData() {
        Mockito.doReturn(Optional.ofNullable(null)).when(readerDao).findById(anyLong());
        Mockito.doReturn(Optional.ofNullable(null)).when(bookDao).findById(anyLong());
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.borrowBook("999 / 999")),
                () -> assertThrows(IllegalArgumentException.class, () -> libraryService.borrowBook("99")),
                () -> verify(readerDao, times(1)).findById(anyLong()),
                () -> verify(bookDao, times(1)).findById(anyLong()),
                () -> verify(borrowDao, never()).save(anyLong(), anyLong()));
    }

    @DisplayName("Test return a book method with correct data")
    @Test
    void returnBookWithCorrectData() {
        Mockito.doReturn(1).when(borrowDao).returnBook(anyLong(), anyLong());
        assertAll(() -> assertTrue(libraryService.returnBook("2 / 1")),
                () -> verify(borrowDao, times(1)).returnBook(anyLong(), anyLong()));
    }

    @DisplayName("Test return a book method with incorrect data")
    @Test
    void returnBookWithInCorrectData() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.returnBook("5")),
                () -> assertThrows(IllegalArgumentException.class, () -> libraryService.returnBook("9999/ 9789")),
                () -> verify(borrowDao, never()).returnBook(anyLong(), anyLong()));
    }

    @DisplayName("Test get all borrows by reader id with correct data")
    @Test
    void getAllBorrowedByExistedReaderId() {
        var reader = new Reader(1, "test");
        List<Book> books = new LinkedList<>() {{
            add(new Book(1, "test", "test"));
            add(new Book(2, "test1", "test1"));
        }};
        Mockito.doReturn(books).when(bookDao).findAllByReaderId(anyLong());
        var returnedBooks = libraryService.getAllBorrowedByReaderId("1");
        assertAll(() -> assertFalse(returnedBooks.isEmpty()),
                () -> assertEquals(books, returnedBooks),
                () -> verify(bookDao, times(1)).findAllByReaderId(anyLong()));
    }

    @DisplayName("Test get all borrows by reader id with incorrect data")
    @Test
    void getAllBorrowedByNotExistedReaderId() {
        Mockito.doReturn(new ArrayList<Book>()).when(bookDao).findAllByReaderId(anyLong());
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.getAllBorrowedByReaderId("999")),
                () -> assertThrows(IllegalArgumentException.class, () -> libraryService.getAllBorrowedByReaderId("test")),
                () -> verify(bookDao, times(1)).findAllByReaderId(anyLong()));
    }

    @DisplayName("Test get all borrows by book id with correct data")
    @Test
    void getWhoBorrowByExistedBookId() {
        var book = new Book(4, "test3", "test3");
        List<Reader> readers = new LinkedList<>() {{
            add(new Reader(2, "test1"));
            add(new Reader(4, "test3"));
        }};
        Mockito.doReturn(readers).when(readerDao).findAllByBookId(anyLong());
        var returnedReaders = libraryService.getWhoBorrowByBookId("4");
        assertAll(() -> assertEquals(2, returnedReaders.size()),
                () -> assertEquals(readers, returnedReaders),
                () -> verify(readerDao, times(1)).findAllByBookId(anyLong()));
    }

    @DisplayName("Test get all borrows by book id with incorrect data")
    @Test
    void getWhoBorrowByNotExistedBookId() {
        Mockito.doReturn(new ArrayList<Reader>()).when(readerDao).findAllByBookId(anyLong());
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.getWhoBorrowByBookId("999")),
                () -> assertThrows(IllegalArgumentException.class, () -> libraryService.getWhoBorrowByBookId("test")),
                () -> verify(readerDao, times(1)).findAllByBookId(anyLong()));
    }
}