package Core;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static Core.LibraryService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @BeforeEach
    void setUp() {
        LibraryService.createData(10);
        assertEquals(10, books.size());
        assertEquals(10, readers.size());
    }

    @Test
    void booksListGetTest() {
        for (var i = 0; i < 10; i++) {
            var book = new Book("Book" + i, "Author" + i);
            assertEquals(book, books.get(i));
        }
    }

    @Test
    void readersListGetTest() {
        for (var i = 0; i < 10; i++) {
            assertEquals(new Reader("Name" + i), readers.get(i));
        }
    }

    @Test
    void registerReaderTest() {
        readers.add(new Reader("test"));
        assertEquals(11, readers.size());
        assertEquals("test", readers.get(readers.size() - 1).getName());
    }

    @Test
    void addBookWithValidDataTest() {
        books.add(new Book("testBook", "testAuthor"));
        assertEquals(11, books.size());
        var createdBook = books.get(10);
        assertEquals("testBook", createdBook.getName());
        assertEquals("testAuthor", createdBook.getAuthor());
    }
}