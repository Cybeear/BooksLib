package service;

import dao.*;
import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        var borrow = new Borrow(book, reader);
        Mockito.doReturn(Optional.of(reader)).when(readerDao).findById(anyLong());
        Mockito.doReturn(Optional.of(book)).when(bookDao).findById(anyLong());
        Mockito.doReturn(borrow).when(borrowDao).save(anyLong(), anyLong());
        var returnedBorrow = libraryService.borrowBook("5 / 5");
        assertAll(() -> assertNotNull(returnedBorrow),
                () -> assertEquals(returnedBorrow, borrow),
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
        var reader = new Reader(2L, "test1");
        var book = new Book(1L, "test", "test");
        Mockito.doReturn(Optional.of(reader)).when(readerDao).findById(anyLong());
        Mockito.doReturn(Optional.of(book)).when(bookDao).findById(anyLong());
        assertAll(() -> assertTrue(libraryService.returnBook("2 / 1")),
                () -> verify(readerDao, times(1)).findById(anyLong()),
                () -> verify(bookDao, times(1)).findById(anyLong()),
                () -> verify(borrowDao, times(1)).returnBook(anyLong(), anyLong()));
    }

    @DisplayName("Test return a book method with incorrect data")
    @Test
    void returnBookWithInCorrectData() {
        Mockito.doReturn(Optional.ofNullable(null)).when(readerDao).findById(anyLong());
        Mockito.doReturn(Optional.ofNullable(null)).when(bookDao).findById(anyLong());
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.returnBook("5")),
                () -> assertThrows(IllegalArgumentException.class, () -> libraryService.returnBook("9999 / 9789")),
                () -> verify(readerDao, times(1)).findById(anyLong()),
                () -> verify(bookDao, times(1)).findById(anyLong()),
                () -> verify(borrowDao, never()).returnBook(anyLong(), anyLong()));
    }

    @DisplayName("Test get all borrows by reader id with correct data")
    @Test
    void getAllBorrowedByExistedReaderId() {
        var reader = new Reader(1, "test");
        List<Borrow> borrows = new LinkedList<>() {{
            add(new Borrow(new Book(1, "test", "test"), reader));
            add(new Borrow(new Book(2, "test1", "test1"), reader));
        }};
        Mockito.doReturn(Optional.of(reader)).when(readerDao).findById(anyLong());
        Mockito.doReturn(borrows).when(borrowDao).findAllBorrowedByReaderId(anyLong());
        var returnedBorrows = libraryService.getAllBorrowedByReaderId("1");
        assertAll(() -> assertFalse(returnedBorrows.isEmpty()),
                () -> assertEquals(borrows, returnedBorrows),
                () -> verify(borrowDao, times(1)).findAllBorrowedByReaderId(anyLong()));
    }

    @DisplayName("Test get all borrows by reader id with incorrect data")
    @Test
    void getAllBorrowedByNotExistedReaderId() {
        Mockito.doReturn(Optional.ofNullable(null)).when(readerDao).findById(anyLong());
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.getAllBorrowedByReaderId("999")),
                () -> assertThrows(IllegalArgumentException.class, () -> libraryService.getAllBorrowedByReaderId("test")),
                () -> verify(readerDao, times(1)).findById(anyLong()),
                () -> verify(borrowDao, never()).findAllBorrowedByReaderId(anyLong()));
    }

    @DisplayName("Test get all borrows by book id with correct data")
    @Test
    void getWhoBorrowByExistedBookId() {
        var book = new Book(4, "test3", "test3");
        List<Borrow> borrows = new LinkedList<>() {{
            add(new Borrow(book, new Reader(2, "test1")));
            add(new Borrow(book, new Reader(4, "test3")));
        }};
        Mockito.doReturn(Optional.of(book)).when(bookDao).findById(anyLong());
        Mockito.doReturn(borrows).when(borrowDao).findAllBorrowedByBookId(anyLong());
        var returnedBorrows = libraryService.getWhoBorrowByBookId("4");
        assertAll(() -> assertEquals(2, returnedBorrows.size()),
                () -> assertEquals(borrows, returnedBorrows),
                () -> verify(bookDao, times(1)).findById(anyLong()),
                () -> verify(borrowDao, times(1)).findAllBorrowedByBookId(anyLong()));
    }

    @DisplayName("Test get all borrows by book id with incorrect data")
    @Test
    void getWhoBorrowByNotExistedBookId() {
        Mockito.doReturn(Optional.ofNullable(null)).when(bookDao).findById(anyLong());
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.getWhoBorrowByBookId("999")),
                () -> assertThrows(IllegalArgumentException.class, () -> libraryService.getWhoBorrowByBookId("test")),
                () -> verify(bookDao, times(1)).findById(anyLong()),
                () -> verify(borrowDao, never()).findAllBorrowedByBookId(anyLong()));
    }
}