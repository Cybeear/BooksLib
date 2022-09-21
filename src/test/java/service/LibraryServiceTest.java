package service;

import dao.*;
import entity.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

class LibraryServiceTest {

    private static BookDao bookDaoMock;
    private static ReaderDao readerDaoMock;
    private static BorrowDao borrowDaoMock;
    private static LibraryService libraryService;

    @BeforeAll
    static void beforeEach() {
        libraryService = new LibraryService();
        bookDaoMock = mock(BookDaoPostgresqlImpl.class);
        readerDaoMock = mock(ReaderDaoPostgresqlImpl.class);
        borrowDaoMock = mock(BorrowDaoPostgresqlImpl.class);
        libraryService.setBookDao(bookDaoMock);
        libraryService.setReaderDao(readerDaoMock);
        libraryService.setBorrowDao(borrowDaoMock);
    }

    @Test
    void registerReaderWithCorrectData() {
        libraryService.registerReader("test");
        ArgumentCaptor<Reader> captor = ArgumentCaptor.forClass(Reader.class);
        verify(readerDaoMock, times(1)).save(captor.capture());
        Reader createdReader = captor.getValue();
        assertEquals("test", createdReader.getName());
    }

    @Test
    void registerReaderWithInCorrectData() {
        assertThrows(IllegalArgumentException.class, () -> libraryService.registerReader(" "));
        verify(readerDaoMock, never()).save(any());
    }

    @Test
    void addBookWithCorrectData() {
        var book = libraryService.addBook("name / author");
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookDaoMock, times(1)).save(captor.capture());
        Book createdBook = captor.getValue();
        assertAll(() -> assertNotNull(createdBook),
                () -> assertEquals("name", createdBook.getName()),
                () -> assertEquals("author", createdBook.getAuthor()));
    }

    @Test
    void addBookWithInCorrectData() {
        assertThrows(IllegalArgumentException.class, () -> libraryService.addBook("name"));
        verify(bookDaoMock, never()).save(any());
    }

    @Test
    void borrowBookWithCorrectData() {
        libraryService.borrowBook("5 / 5");
        ArgumentCaptor<Borrow> captor = ArgumentCaptor.forClass(Borrow.class);
        verify(borrowDaoMock, atLeast(1)).save(anyLong(), anyLong());
        Borrow createdBorrow = captor.getValue();
        assertNotNull(createdBorrow);
    }

    @Test
    void borrowBookWithInCorrectData() {
        assertThrows(IllegalArgumentException.class, () -> libraryService.borrowBook("5"));
        verify(borrowDaoMock, never()).save(anyLong(),anyLong());
    }

    @Test
    void returnBookWithCorrectData() {
        assertTrue(libraryService.returnBook("1 / 1"));
        ArgumentCaptor<Borrow> captor = ArgumentCaptor.forClass(Borrow.class);
        verify(borrowDaoMock, atLeast(1)).returnBook(anyLong(), anyLong());
        Borrow createdBorrow = captor.getValue();
        assertNotNull(createdBorrow);
    }

    @Test
    void returnBookWithInCorrectData() {
        assertThrows(IllegalArgumentException.class, () -> libraryService.borrowBook("5"));
        verify(borrowDaoMock, never()).returnBook(anyLong(), anyLong());
    }

    @Test
    void getAllBorrowedByExistedReaderId() {
        assertDoesNotThrow(() -> libraryService.getAllBorrowedByReaderId("2").size());
        verify(readerDaoMock, atLeast(1)).findById(anyLong());
        verify(borrowDaoMock, atLeast(1)).findAllBorrowedByReaderId(anyLong());
    }

    @Test
    void getAllBorrowedByNotExistedReaderId() {
        assertThrows(IllegalArgumentException.class, () -> libraryService.getAllBorrowedByReaderId("999"));
        verify(readerDaoMock, never()).findById(anyLong());
        verify(borrowDaoMock, never()).findAllBorrowedByReaderId(anyLong());
    }

    @Test
    void getWhoBorrowByExistedBookId() {
        assertDoesNotThrow(() -> libraryService.getWhoBorrowByBookId("4"));
        verify(bookDaoMock, atLeast(1)).findById(anyLong());
        verify(borrowDaoMock, atLeast(1)).findAllBorrowedByReaderId(anyLong());
    }

    @Test
    void getWhoBorrowByNotExistedBookId() {
        assertThrows(IllegalArgumentException.class, () -> libraryService.getWhoBorrowByBookId("999"));
        verify(bookDaoMock, never()).findById(anyLong());
        verify(borrowDaoMock, never()).findAllBorrowedByReaderId(anyLong());
    }
}