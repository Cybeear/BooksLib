package service;

import dao.*;
import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServiceTest {


    private BookDaoPostgresqlImpl bookDao;
    private ReaderDaoPostgresqlImpl readerDao;
    private BorrowDaoPostgresqlImpl borrowDao;
    private static LibraryService libraryService;

    @BeforeEach
    void setUp() {
        bookDao = spy(BookDaoPostgresqlImpl.class);
        readerDao = spy(ReaderDaoPostgresqlImpl.class);
        borrowDao = spy(BorrowDaoPostgresqlImpl.class);
        libraryService = new LibraryService();
        libraryService.setBookDao(bookDao);
        libraryService.setReaderDao(readerDao);
        libraryService.setBorrowDao(borrowDao);
    }

    @Test
    void registerReaderWithCorrectData() {
        var reader = new Reader("name");
        var returnedReader = libraryService.registerReader("name");
        Mockito.doReturn(reader).when(readerDao).save(returnedReader);
        verify(readerDao, times(1)).save(returnedReader);
        assertAll(() -> assertNotNull(returnedReader),
                () -> assertNotEquals(returnedReader.getId(), reader.getId()),
                () -> assertEquals("name", reader.getName()));
    }

    @Test
    void registerReaderWithInCorrectData() {
        assertThrows(IllegalArgumentException.class, () ->
                Mockito.when(libraryService.registerReader(" ")).thenThrow(IllegalArgumentException.class));
        verify(readerDao, never()).save(any());
    }

    @Test
    void addBookWithCorrectData() {
        var book = new Book("name", "author");
        var returnedBook = libraryService.addBook("name / author");
        Mockito.doReturn(book).when(bookDao).save(returnedBook);
        verify(bookDao, times(1)).save(returnedBook);
        assertAll(() -> assertNotNull(returnedBook),
                () -> assertNotEquals(returnedBook.getId(), book.getId()),
                () -> assertEquals(returnedBook.getName(), book.getName()),
                () -> assertEquals(returnedBook.getAuthor(), book.getAuthor()));
    }

    @Test
    void addBookWithInCorrectData() {
        assertThrows(IllegalArgumentException.class, () ->
                Mockito.when(libraryService.addBook("name")).thenThrow(IllegalArgumentException.class));
        verify(bookDao, never()).save(any());
    }

    @Test
    void borrowBookWithCorrectData() {
        Borrow borrow = new Borrow(new Book(5, "test4", "test4"), new Reader(5, "test4"));
        Mockito.when(libraryService.borrowBook("5 / 5")).thenReturn(borrow);
        assertAll(() -> assertNotNull(borrow),
                () -> assertEquals(new Book(5, "test4", "test4"), borrow.getBook()),
                () -> assertEquals(new Reader(5, "test4"), borrow.getReader()));
    }

    @Test
    void borrowBookWithInCorrectData() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () ->
                        Mockito.when(libraryService.borrowBook("999 / 999")).thenThrow(IllegalArgumentException.class)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        Mockito.when(libraryService.borrowBook("99")).thenThrow(IllegalArgumentException.class)));
        verify(readerDao, times(1)).findById(anyLong());
        verify(bookDao, times(1)).findById(anyLong());
    }

    @Test
    void returnBookWithCorrectData() {
        assertTrue(libraryService.returnBook("2 / 1"));
        verify(borrowDao, times(1)).returnBook(anyLong(), anyLong());
    }

    @Test
    void returnBookWithInCorrectData() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> libraryService.returnBook("5")),
                () -> assertThrows(IllegalArgumentException.class, () -> libraryService.returnBook("9999 / 9789")));
    }

    @Test
    void getAllBorrowedByExistedReaderId() {
        List<Borrow> borrows = new LinkedList<>() {{
            add(new Borrow(new Book(1, "test", "test"), new Reader(1, "test")));
            add(new Borrow(new Book(2, "test1", "test1"), new Reader(1, "test")));
        }};
        var returnedBorrows = libraryService.getAllBorrowedByReaderId("1");
        Mockito.doReturn(returnedBorrows).when(borrowDao).findAllBorrowedByReaderId(anyLong());
        verify(borrowDao, times(1)).findAllBorrowedByReaderId(anyLong());
        assertAll(() -> assertEquals(2, returnedBorrows.size()),
                () -> assertEquals(borrows, returnedBorrows));
    }

    @Test
    void getAllBorrowedByNotExistedReaderId() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () ->
                        Mockito.when(libraryService.getAllBorrowedByReaderId("999"))
                                .thenThrow(IllegalArgumentException.class)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        Mockito.when(libraryService.getAllBorrowedByReaderId("qwer"))
                                .thenThrow(IllegalArgumentException.class))
        );
        verify(readerDao, times(1)).findById(anyLong());
        verify(borrowDao, never()).findAllBorrowedByReaderId(anyLong());
    }

    @Test
    void getWhoBorrowByExistedBookId() {
        List<Borrow> borrows = new LinkedList<>() {{
            add(new Borrow(new Book(4, "test3", "test3"), new Reader(2, "test1")));
            add(new Borrow(new Book(4, "test3", "test3"), new Reader(4, "test3")));
        }};
        var returnedBorrows = libraryService.getWhoBorrowByBookId("4");
        Mockito.doReturn(returnedBorrows).when(borrowDao).findAllBorrowedByBookId(anyLong());
        verify(bookDao, times(1)).findById(anyLong());
        verify(borrowDao, times(1)).findAllBorrowedByBookId(anyLong());
        assertAll(() -> assertEquals(2, returnedBorrows.size()),
                () -> assertEquals(borrows, returnedBorrows));
    }

    @Test
    void getWhoBorrowByNotExistedBookId() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () ->
                        Mockito.when(libraryService.getWhoBorrowByBookId("999"))
                                .thenThrow(IllegalArgumentException.class)),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        Mockito.when(libraryService.getWhoBorrowByBookId("qwer"))
                                .thenThrow(IllegalArgumentException.class))
        );
        verify(bookDao, times(1)).findById(anyLong());
        verify(borrowDao, never()).findAllBorrowedByBookId(anyLong());
    }
}