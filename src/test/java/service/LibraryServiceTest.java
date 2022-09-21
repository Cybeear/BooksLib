package service;

import dao.*;
import entity.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

class LibraryServiceTest {

    /*private static BookDao bookDaoMock;
    private static ReaderDao readerDaoMock;
    private static BorrowDao borrowDaoMock;*/
    private static LibraryService libraryService;
    private BookDao bookDaoMock = new BookDaoPostgresqlImpl();
    @BeforeAll
    static void beforeAll() {
        libraryService = new LibraryService();
        /*bookDaoMock = mock(BookDaoPostgresqlImpl.class);
        readerDaoMock = mock(ReaderDaoPostgresqlImpl.class);
        borrowDaoMock = mock(BorrowDaoPostgresqlImpl.class);
        libraryService.setBookDao(bookDaoMock);
        libraryService.setReaderDao(readerDaoMock);
        libraryService.setBorrowDao(borrowDaoMock);*/
    }

    /*@DisplayName("")
    @Test
    void registerReaderSuccessful() {
        libraryService.addBook("test");
        ArgumentCaptor<Reader> captor = ArgumentCaptor.forClass(Reader.class);

        verify(readerDaoMock, times(1)).save(captor.capture());
        Reader createdReader = captor.getValue();
        assertAll(
                () -> assertNotEquals(reader.getId(), createdReader.getId()),
                () -> assertEquals(reader.getName(), createdReader.getName()));
    }*/

    /*@DisplayName("")
    @Test
    void registerReaderUnSuccessful() {
        Reader reader = new Reader("name");
        ArgumentCaptor<Reader> captor = ArgumentCaptor.forClass(Reader.class);
        verify(readerDaoMock, times(1)).save(captor.capture());
        Reader createdReader = captor.getValue();
        assertAll(
                () -> assertEquals(reader.getId(), createdReader.getId()),
                () -> assertEquals(reader.getName(), createdReader.getName()));
    }*/

    @Test
    void addBook() {
        Book book = bookDaoMock.save(new Book("name", "author"));
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookDaoMock, times(1)).save(captor.capture());
        Book createdBook = captor.getValue();
        assertAll(() -> assertNotNull(createdBook),
                () -> assertEquals(book.getName(), createdBook.getName()),
                () -> assertEquals(book.getAuthor(), createdBook.getAuthor()));
    }

    @Test
    void borrowABook() {
    }

    @Test
    void returnBorrowedBook() {
    }

    @Test
    void showAllBorrowedByReaderId() {
    }

    @Test
    void showWhoBorrowByBookId() {
    }
}