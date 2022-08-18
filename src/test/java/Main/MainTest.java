package Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    ArrayList<Book> bookArrayList = new ArrayList<>();
    ArrayList<Reader> readerArrayList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (var i = 0; i < 10; i++) {
            bookArrayList.add(new Book(i, "Book" + i, "Author" + i));
            readerArrayList.add(new Reader(i,"Name" + i));
        }
    }

    @Test
    void booksListGetTest() {
        assertEquals(10, bookArrayList.size());
        for (var i = 0; i < 10; i++) {
            assertEquals(new Book(i,"Book" + i, "Author" + i), bookArrayList.get(i));
        }
    }


    @Test
    void readersListGetTest() {
        assertEquals(10, readerArrayList.size());
        for (var i = 0; i < 10; i++) {
            assertEquals(new Reader(i,"Name" + i), readerArrayList.get(i));
        }
    }
}