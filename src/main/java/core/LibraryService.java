package core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * LibraryService class used to interaction with data and objects in online Library
 */
public class LibraryService {

    private static ArrayList<Book> books = new ArrayList<>();
    private static ArrayList<Reader> readers = new ArrayList<>();
    private static LinkedList<Borrow> borrows = new LinkedList<>();
    private static Scanner in = new Scanner(System.in);

    /**
     * @param size Size of arrays
     *             create default data
     */
    public static void createData(int size) {
        for (var i = 0; i < size; i++) {
            books.add(new Book("Book" + i, "Author" + i));
            readers.add(new Reader("Name" + i));
        }
    }

    /**
     * Add new reader to list
     */
    public static void registerReader() {
        System.out.println("Please enter new reader full name!");
        readers.add(new Reader(in.nextLine()));
    }

    /**
     * Add new book to list
     */
    public static void addBook() {
        System.out.println("Please enter new book name and author separated by '/'. Like this: name / author");
        var str = in.nextLine();
        var inputSplit = str.split(" / ");
        if (inputSplit.length < 2) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        books.add(new Book(inputSplit[0], inputSplit[1]));
    }

    /**
     * Show all books in the list
     */
    public static void showAllBooks() {
        books.forEach(System.out::println);
    }

    /**
     * Show all readers in the list
     */
    public static void showAllReaders() {
        readers.forEach(System.out::println);
    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * add to borrowList if string not contains any characters other than numbers
     */
    public static void borrowABook() {
        System.out.println("Please enter reader id and book id to borrow separated by '/'. Like this: 4 / 2");
        var inputSplit = in.nextLine().split(" / ");
        if (inputSplit.length < 2) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var readerId = parser(inputSplit[0]);
        var bookId = parser(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var book = books.stream().filter(_book -> _book.getId() == bookId).findFirst();
        var reader = readers.stream().filter(_reader -> _reader.getId() == readerId).findFirst();
        if (book.isPresent() && reader.isPresent()) {
            if (!checkIfBookBorrowed(book.get())) {
                borrows.add(new Borrow(book.get(),
                        reader.get()));
                System.out.println(borrows.getLast().toString());
            } else System.err.println("Error, this book is borrowed!");
        } else System.err.println("Error");

    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * delete object from borrowList if string not contains any characters other than numbers
     */
    public static void returnBorrowedBook() {
        System.out.println("Please enter reader id and book id to return separated by '/'. Like this: 1 / 3");
        var inputSplit = in.nextLine().split(" / ");
        if (inputSplit.length < 2) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var readerId = parser(inputSplit[0]);
        var bookId = parser(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var bookToBorrow = books.stream().filter(book -> book.getId() == bookId).findFirst();
        var readerToBorrow = readers.stream().filter(reader -> reader.getId() == readerId).findFirst();
        if (bookToBorrow.isPresent() && readerToBorrow.isPresent()) {
            if (checkIfBookBorrowed(bookToBorrow.get())) {
                borrows.remove(new Borrow(bookToBorrow.get(), readerToBorrow.get()));
                System.out.println("Reader: " + readerToBorrow.get()
                        + " return the book: " + bookToBorrow.get());
            } else System.err.println("Error, this book is not borrowed!");
        } else System.err.println("Error");
    }

    /**
     * @param book Book object
     * @return boolean true if book is borrowed, use for-each from List class
     */
    private static boolean checkIfBookBorrowed(Book book) {
        return borrows.stream().anyMatch(borrow -> borrow.getBook().equals(book));
    }

    /**
     * Return to menu if string of arguments contains any symbols other than numbers or
     * print all borrow objects by reader id
     */
    public static void showAllBorrowedByReaderId() {
        System.out.println("Please enter reader id: ");
        var parsed = parser(in.nextLine());
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        if (checkIfDataExistsByReaderId(parsed)) {
            borrows.stream().filter(borrow -> borrow.getReader().getId() == parsed).forEach(System.out::println);
        } else System.out.println("This user don`t borrow a book!");
    }

    /**
     * Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public static void showWhoBorrowByBookId() {
        System.out.println("Please enter book id: ");
        var parsed = parser(in.nextLine());
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        if (checkIfDataExistsByBookId(parsed))
            borrows.stream().filter(borrow -> borrow.getBook().getId() == parsed).forEach(borrow -> System.out.println(borrow.getReader()));
        else System.out.println("This book isn`t borrowed!");
    }

    /**
     * @param id integer number, reader id
     * @return true if reader borrow any book
     */
    private static boolean checkIfDataExistsByReaderId(int id) {
        return borrows.stream().anyMatch(borrow -> borrow.getReader().getId() == id);
    }

    /**
     * @param id integer number, book id
     * @return true if any reader borrow the book by this id
     */
    private static boolean checkIfDataExistsByBookId(int id) {
        return borrows.stream().anyMatch(borrow -> borrow.getBook().getId() == id);
    }

    /**
     * @param str argument string
     * @return integer value
     */
    public static int parser(String str) {
        if (checkData(str)) return -1;
        return Integer.parseInt(str);
    }

    /**
     * @param str argument string
     * @return boolean if string contains any symbols other than numbers
     */
    public static boolean checkData(String str) {
        return str.matches("^[a-zA-Z!@*&%^#+_,.()\\-\\\\A-Za-zА-Яа-яїіІЇ ]+$");
    }
}