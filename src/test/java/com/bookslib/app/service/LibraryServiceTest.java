package com.bookslib.app.service;

import com.bookslib.app.dao.BookDaoPostgresqlImpl;
import com.bookslib.app.dao.BorrowDaoPostgresqlImpl;
import com.bookslib.app.dao.ReaderDaoPostgresqlImpl;
import com.bookslib.app.entity.Book;
import com.bookslib.app.entity.Borrow;
import com.bookslib.app.entity.Reader;
import com.bookslib.app.exceptions.LibraryServiceException;
import com.bookslib.app.exceptions.ParserServiceException;
import com.bookslib.app.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LibraryServiceTest {


    private BookDaoPostgresqlImpl bookDao;
    private ReaderDaoPostgresqlImpl readerDao;
    private BorrowDaoPostgresqlImpl borrowDao;
    private LibraryService libraryService;

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
        assertAll(
                () -> assertNotNull(returnedReader),
                () -> assertEquals(returnedReader, reader),
                () -> verify(readerDao, times(1))
                        .save(returnedReader));
    }

    @DisplayName("Test adding new reader with incorrect data")
    @Test
    void registerReaderWithInCorrectData() {
        assertAll(
                () -> assertThrows(
                        LibraryServiceException.class,
                        () -> libraryService.registerReader(" ")),
                () -> verify(readerDao, never())
                        .save(any()));
    }

    @DisplayName("Test adding new book with correct data")
    @Test
    void addBookWithCorrectData() {
        var book = new Book("name", "author");
        Mockito.doReturn(book).when(bookDao).save(book);
        var returnedBook = libraryService.addBook("name / author");
        assertAll(
                () -> assertNotNull(returnedBook),
                () -> assertEquals(returnedBook, book),
                () -> verify(bookDao, times(1))
                        .save(any()));
    }

    @DisplayName("Test adding new book with incorrect data")
    @Test
    void addBookWithInCorrectInputString() {
        var expectedErrorMessageWhenDoesNotHaveSeparator = "Your input is incorrect, you need to write name and author separated by '/'!";
        var exceptionOfIllegalArguments = assertThrows(
                LibraryServiceException.class,
                () -> libraryService.addBook("name"));
        var exceptionOfEmptyString = assertThrows(
                LibraryServiceException.class,
                () -> libraryService.addBook("       "));
        var expectedErrorMessageWhenStringDoesNotHaveAuthor = "Book author can not be an empty string!";
        var exceptionIfAuthorIsBlank = assertThrows(
                LibraryServiceException.class,
                () -> libraryService.addBook("name /  "));
        var expectedErrorMessageWhenStringDoesNotHaveName = "Book name can not be an empty string!";
        var exceptionIfNameIsBlank = assertThrows(
                LibraryServiceException.class,
                () -> libraryService.addBook(" / author"));
        assertAll(
                () -> assertEquals(
                        exceptionOfIllegalArguments.getMessage(),
                        expectedErrorMessageWhenDoesNotHaveSeparator),
                () -> assertEquals(
                        exceptionOfEmptyString.getMessage(),
                        expectedErrorMessageWhenDoesNotHaveSeparator),
                () -> assertEquals(
                        exceptionIfAuthorIsBlank.getMessage(),
                        expectedErrorMessageWhenStringDoesNotHaveAuthor),
                () -> assertEquals(
                        exceptionIfNameIsBlank.getMessage(),
                        expectedErrorMessageWhenStringDoesNotHaveName),
                () -> verify(bookDao, never())
                        .save(any()));
    }

    @DisplayName("Test adding new book with incorrect data")
    @Test
    void addBookWithInCorrectInput() {
        var expectedErrorMessageIfEmptyBookName = "Book name can not be an empty string!";
        var expectedErrorMessageIfEmptyBookAuthor = "Book author can not be an empty string!";
        var exceptionOfEmptyBookName = assertThrows(
                LibraryServiceException.class,
                () -> libraryService.addBook("  / author"));
        var exceptionOfEmptyBookAuthor = assertThrows(
                LibraryServiceException.class,
                () -> libraryService.addBook("name /  "));
        assertAll(
                () -> assertEquals(
                        expectedErrorMessageIfEmptyBookName,
                        exceptionOfEmptyBookName.getMessage()),
                () -> assertEquals(
                        expectedErrorMessageIfEmptyBookAuthor,
                        exceptionOfEmptyBookAuthor.getMessage()),
                () -> verify(bookDao, never())
                        .save(any())
        );
    }

    @DisplayName("Test borrow a book method with correct data")
    @Test
    void borrowBookWithCorrectData() {
        var reader = new Reader(5, "test4");
        var book = new Book(5, "test4", "test4");
        var borrow = Optional.of(new Borrow(book, reader));
        Mockito.doReturn(borrow).when(borrowDao).save(anyLong(), anyLong());
        var returnedBorrow = libraryService.borrowBook("5 / 5");
        assertAll(
                () -> assertNotNull(returnedBorrow),
                () -> assertEquals(borrow.get(), returnedBorrow.get()),
                () -> verify(borrowDao, times(1))
                        .save(anyLong(), anyLong()));
    }

    @DisplayName("Test borrow a book method with incorrect data")
    @Test
    void borrowBookWithInCorrectData() {
        Mockito.when(borrowDao.save(anyLong(), anyLong())).thenReturn(Optional.ofNullable(null));
        assertAll(
                () -> assertTrue(
                        libraryService.borrowBook("999 / 999").isEmpty()),
                () -> assertThrows(
                        LibraryServiceException.class,
                        () -> libraryService.borrowBook("99")),
                () -> verify(borrowDao, times(1))
                        .save(anyLong(), anyLong()));
    }

    @DisplayName("Test return a book method with correct data")
    @Test
    void returnBookWithCorrectData() {
        Mockito.doReturn(1).when(borrowDao).returnBook(anyLong(), anyLong());
        libraryService.returnBook("1 / 1");
        libraryService.returnBook("9999/ 9789");
        libraryService.returnBook("9999 /  9789");
        assertAll(
                () -> verify(borrowDao, times(3))
                        .returnBook(anyLong(), anyLong()));
    }

    @DisplayName("Test return a book method with incorrect data")
    @Test
    void returnBookWithInCorrectData() {
        assertAll(
                () -> assertThrows(LibraryServiceException.class,
                        () -> libraryService.returnBook("5")),
                () -> verify(borrowDao, never())
                        .returnBook(anyLong(), anyLong()));
    }

    @DisplayName("Test get all borrows by reader id with correct data")
    @Test
    void getAllBorrowedByExistedReaderId() {
        List<Book> books = new LinkedList<>() {{
            add(new Book(1, "test", "test"));
            add(new Book(2, "test1", "test1"));
        }};
        Mockito.doReturn(books).when(bookDao).findAllByReaderId(anyLong());
        var returnedBooks = libraryService.getAllBorrowedByReaderId("1");
        assertAll(
                () -> assertFalse(returnedBooks.isEmpty()),
                () -> assertEquals(books, returnedBooks),
                () -> verify(bookDao, times(1))
                        .findAllByReaderId(anyLong()));
    }

    @DisplayName("Test get all borrows by reader id with incorrect data")
    @Test
    void getAllBorrowedByNotExistedReaderId() {
        Mockito.doReturn(new ArrayList<Book>()).when(bookDao).findAllByReaderId(anyLong());
        assertAll(
                () -> assertDoesNotThrow(() -> libraryService.getAllBorrowedByReaderId("999")),
                () -> assertThrows(ParserServiceException.class, () -> libraryService.getAllBorrowedByReaderId("test")),
                () -> verify(bookDao, times(1)).findAllByReaderId(anyLong()));
    }

    @DisplayName("Test get all borrows by book id with correct data")
    @Test
    void getWhoBorrowByExistedBookId() {
        List<Reader> readers = new LinkedList<>() {{
            add(new Reader(2, "test1"));
            add(new Reader(4, "test3"));
        }};
        Mockito.doReturn(readers).when(readerDao).findAllByBookId(anyLong());
        var returnedReaders = libraryService.getWhoBorrowByBookId("4");
        assertAll(
                () -> assertEquals(2, returnedReaders.size()),
                () -> assertEquals(readers, returnedReaders),
                () -> verify(readerDao, times(1))
                        .findAllByBookId(anyLong()));
    }

    @DisplayName("Test get all borrows by book id with incorrect data")
    @Test
    void getWhoBorrowByNotExistedBookId() {
        Mockito.doReturn(new ArrayList<Reader>()).when(readerDao).findAllByBookId(anyLong());
        assertAll(
                () -> assertDoesNotThrow(() -> libraryService.getWhoBorrowByBookId("999")),
                () -> assertThrows(ParserServiceException.class,
                        () -> libraryService.getWhoBorrowByBookId("test")),
                () -> verify(readerDao, times(1))
                        .findAllByBookId(anyLong()));
    }
}