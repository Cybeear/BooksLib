package core;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * LibraryService class used to interaction with data and objects in online Library
 */
public class LibraryService {

    private ArrayList<Book> books;
    private ArrayList<Reader> readers;
    private LinkedList<Borrow> borrows;

    public LibraryService() {
        this.books = new ArrayList<>();
        this.readers = new ArrayList<>();
        this.borrows = new LinkedList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Reader> getReaders() {
        return readers;
    }

    public void setReaders(ArrayList<Reader> readers) {
        this.readers = readers;
    }

    public LinkedList<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(LinkedList<Borrow> borrows) {
        this.borrows = borrows;
    }

    /**
     * @param size Size of arrays
     *             create default data
     */
    public void createData(int size) {
        for (var i = 0; i < size; i++) {
            books.add(new Book("Book" + i, "Author" + i));
            readers.add(new Reader("Name" + i));
        }
    }

    /**
     * Add new reader to list
     */
    public void registerReader(String str) {
        if (!str.equals(" ")) readers.add(new Reader(str));
        else System.out.println("Error: enter a valid data!");
    }

    /**
     * Add new book to list
     */
    public void addBook(String str) {
        var inputSplit = str.split(" / ");
        if (inputSplit.length < 2
                || inputSplit[0].equals(" ")
                || inputSplit[1].equals(" ")) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        books.add(new Book(inputSplit[0], inputSplit[1]));
    }

    /**
     * Show all books in the list
     */
    public void showAllBooks() {
        books.forEach(System.out::println);
    }

    /**
     * Show all readers in the list
     */
    public void showAllReaders() {
        readers.forEach(System.out::println);
    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * add to borrowList if string not contains any characters other than numbers
     */
    public void borrowABook(String str) {
        var inputSplit = str.split(" / ");
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
            if (!checkIfBookBorrowed(bookToBorrow.get())) {
                borrows.add(new Borrow(bookToBorrow.get(),
                        readerToBorrow.get()));
                System.out.println(borrows.getLast().toString());
            } else System.err.println("Error: this book is borrowed!");
        } else System.err.println("Error: data is not exists!");
    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * delete object from borrowList if string not contains any characters other than numbers
     */
    public void returnBorrowedBook(String str) {
        var inputSplit = str.split(" / ");
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
            } else System.err.println("Error: this book is not borrowed!");
        } else System.err.println("Error: data is not exists");
    }

    /**
     * @param book Book object
     * @return boolean true if book is borrowed, use for-each from List class
     */
    private boolean checkIfBookBorrowed(Book book) {
        return borrows.stream().anyMatch(borrow -> borrow.getBook().equals(book));
    }

    /**
     * Return to menu if string of arguments contains any symbols other than numbers or
     * print all borrow objects by reader id
     */
    public void showAllBorrowedByReaderId(String str) {
        var parsed = parser(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        if (checkIfDataExistsByReaderId(parsed)) {
            borrows.stream().filter(borrow -> borrow.getReader().getId() == parsed).forEach(System.out::println);
        } else System.err.println("Error: this user don`t borrow a book!");
    }

    /**
     * Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public void showWhoBorrowByBookId(String str) {
        var parsed = parser(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        if (checkIfDataExistsByBookId(parsed))
            borrows.stream().filter(borrow -> borrow.getBook().getId() == parsed).forEach(borrow -> System.out.println(borrow.getReader()));
        else System.out.println("Error: this book isn`t borrowed!");
    }

    /**
     * @param id integer number, reader id
     * @return true if reader borrow any book
     */
    private boolean checkIfDataExistsByReaderId(int id) {
        return borrows.stream().anyMatch(borrow -> borrow.getReader().getId() == id);
    }

    /**
     * @param id integer number, book id
     * @return true if any reader borrow the book by this id
     */
    private boolean checkIfDataExistsByBookId(int id) {
        return borrows.stream().anyMatch(borrow -> borrow.getBook().getId() == id);
    }

    /**
     * @param str argument string
     * @return integer value -1 if string not contains only digits
     */
    private int parser(String str) {
        return isValidNumber(str) ? Integer.parseInt(str) : -1;
    }

    /**
     * @param str argument string
     * @return boolean true if string contains only digits
     */
    private boolean isValidNumber(String str) {
        return str.matches("^\\d+$");
    }
}
