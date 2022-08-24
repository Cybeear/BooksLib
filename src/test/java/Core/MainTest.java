package Core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static Core.LibraryService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    ArrayList<Book> bookArrayList = new ArrayList<>();
    ArrayList<Reader> readerArrayList = new ArrayList<>();
    LinkedList<Borrow> borrowLinkedList = new LinkedList<>();

    @BeforeEach
    void setUp() {
        for (var i = 0; i < 10; i++) {
            bookArrayList.add(new Book(i, "Book" + i, "Author" + i));
            readerArrayList.add(new Reader(i, "Name" + i));
        }
    }

    @Test
    void booksListGetTest() {
        assertEquals(10, bookArrayList.size());
        for (var i = 0; i < 10; i++) {
            assertEquals(new Book(i, "Book" + i, "Author" + i), bookArrayList.get(i));
        }
    }

    @Test
    void readersListGetTest() {
        assertEquals(10, readerArrayList.size());
        for (var i = 0; i < 10; i++) {
            assertEquals(new Reader(i, "Name" + i), readerArrayList.get(i));
        }
    }

    @Test
    void registerReaderTest() {
        assertEquals(10, readerArrayList.size());
        registerReader(readerArrayList, "test");
        assertEquals(11, readerArrayList.size());
        assertEquals(new Reader(readerArrayList.size() - 1, "test"), readerArrayList.get(readerArrayList.size() - 1));
    }

    @Test
    void addBookWithValidDataTest() {
        assertEquals(10, bookArrayList.size());
        addBook(bookArrayList, "testBook / testAuthor");
        assertEquals(11, bookArrayList.size());
        assertEquals(new Book(bookArrayList.size() - 1, "testBook", "testAuthor"), bookArrayList.get(bookArrayList.size() - 1));
    }

    @Test
    void addBookWithInvalidDataTest() {
        assertEquals(10, bookArrayList.size());
        addBook(bookArrayList, "testBook");
        assertEquals(10, bookArrayList.size());
    }

    @Test
    void borrowABookWithValidDataTest() {
        for (var i = 0; i < 5; i++) {
            borrowLinkedList.add(i, new Borrow(bookArrayList.get(i), readerArrayList.get(2)));
        }
        assertEquals(5, borrowLinkedList.size());
        for (var i = 0; i < 5; i++) {
            assertEquals(new Borrow(bookArrayList.get(i), readerArrayList.get(2)), borrowLinkedList.get(i));
        }
        borrowLinkedList.clear();
    }

    @Test
    void borrowABookWithInvalidBookIdTest() {
        for (var i = 10; i < 15; i++) {
            borrowABook(bookArrayList, readerArrayList, borrowLinkedList, "4 / " + i);
        }
        assertEquals(0, borrowLinkedList.size());
    }

    @Test
    void borrowABookWithInvalidReaderIdTest() {
        for (var i = 10; i < 15; i++) {
            borrowABook(bookArrayList, readerArrayList, borrowLinkedList, i + " / 1");
        }
        assertEquals(0, borrowLinkedList.size());
    }

    @Test
    void returnBorrowedBookWithValidDataTest() {
        for (var i = 0; i < 5; i++) {
            borrowLinkedList.add(i, new Borrow(bookArrayList.get(i), readerArrayList.get(2)));
        }
        returnBorrowedBook(bookArrayList, readerArrayList, borrowLinkedList, "2 / 1");
        assertEquals(4, borrowLinkedList.size());
    }

    @Test
    void returnBorrowedBookWithInvalidBookIdTest() {
        for (var i = 0; i < 5; i++) {
            borrowLinkedList.add(i, new Borrow(bookArrayList.get(i), readerArrayList.get(2)));
        }
        returnBorrowedBook(bookArrayList, readerArrayList, borrowLinkedList, "2 / 10");
        assertEquals(5, borrowLinkedList.size());
    }

    @Test
    void returnBorrowedBookWithInvalidReaderIdTest() {
        for (var i = 0; i < 5; i++) {
            borrowLinkedList.add(i, new Borrow(bookArrayList.get(i), readerArrayList.get(2)));
        }
        returnBorrowedBook(bookArrayList, readerArrayList, borrowLinkedList, "12 / 1");
        assertEquals(5, borrowLinkedList.size());
    }
}