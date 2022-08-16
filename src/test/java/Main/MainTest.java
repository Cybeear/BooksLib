package Main;

import static Main.Main.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {


    @org.junit.jupiter.api.Test
    void booksListCreationTest() {
        var booksList = books(5);
        assertEquals(5, booksList.size());
    }

    @org.junit.jupiter.api.Test
    void booksListGetTest() {
        var booksList = books(5);
        assertEquals(5, booksList.size());
        for (var i = 0; i < booksList.size(); i++){
            assertArrayEquals(new String[]{"test" + i, "author" + i}, booksList.get(i));
        }
    }

    @org.junit.jupiter.api.Test
    void readersListCreationTest() {
        var readersList = readers(5);
        assertEquals(5, readersList.size());
    }

    @org.junit.jupiter.api.Test
    void readersListGetTest() {
        var readersList = readers(5);
        assertEquals(5, readersList.size());
        for (var i = 0; i < readersList.size(); i++){
            assertEquals(new String("reader" + i), readersList.get(i));
        }
    }
}