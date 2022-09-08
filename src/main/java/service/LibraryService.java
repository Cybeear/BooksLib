package service;

import dao.BookDao;
import dao.BorrowDao;
import dao.ReaderDao;
import entity.Book;
import entity.Borrow;
import entity.Reader;

/**
 * LibraryService class used to interaction with data and objects in online Library
 */
public class LibraryService {
    BookDao bookDao;
    ReaderDao readerDao;
    BorrowDao borrowDao;
    ParserService parserService;

    {
        bookDao = new BookDao();
        readerDao = new ReaderDao();
        borrowDao = new BorrowDao();
        parserService = new ParserService();
    }

    /**
     * Add new reader to list
     */
    public void registerReader(String str) {
        if (!str.equals(" ")) readerDao.addNew(new Reader(str));
        else System.err.println("Error: enter a valid data!");
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
        bookDao.addNew(new Book(inputSplit[0], inputSplit[1]));
    }

    /**
     * Show all books in the list
     */
    public void showAllBooks() {
        bookDao.fetchAll().forEach(System.out::println);
    }

    /**
     * Show all readers in the list
     */
    public void showAllReaders() {
        readerDao.fetchAll().forEach(System.out::println);
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
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var bookToBorrow = bookDao.fetchById(bookId);
        var readerToBorrow = readerDao.fetchById(readerId);
        if (bookToBorrow != null && readerToBorrow != null) {
            if (!checkIfBookBorrowed(bookToBorrow)) {
                var borrow = new Borrow(bookToBorrow, readerToBorrow);
                borrowDao.addNew(borrow);
                System.out.println(borrow);
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
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var bookToBorrow = bookDao.fetchById(bookId);
        var readerToBorrow = readerDao.fetchById(readerId);
        if (bookToBorrow != null && readerToBorrow != null) {
            if (checkIfBookBorrowed(bookToBorrow)) {
                var borrow = new Borrow(bookToBorrow, readerToBorrow);
                if (borrowDao.deleteRecord(borrow)) System.out.println("Error, try again!");
                else System.out.println("Reader: " + readerToBorrow
                        + " return the book: " + bookToBorrow);
            } else System.err.println("Error: this book is not borrowed!");
        } else System.err.println("Error: data is not exists");
    }

    /**
     * @param book Book object
     * @return boolean true if book is borrowed, use for-each from List class
     */
    private boolean checkIfBookBorrowed(Book book) {
        return borrowDao.fetchById(book.getId()) != null;
    }

    /**
     * Return to menu if string of arguments contains any symbols other than numbers or
     * print all borrow objects by reader id
     */
    public void showAllBorrowedByReaderId(String str) {
        var parsed = parserService.parseLong(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var reader = readerDao.fetchById(parsed);
        if (reader != null) {
            var borrows = borrowDao.fetchAllById(reader.getId());
            if (borrows != null) borrows.forEach(System.out::println);
            else System.err.println("Error: this reader don`t borrow a book!");
        } else System.err.println("Error, this reader is not exist!");
    }

    /**
     * Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public void showWhoBorrowByBookId(String str) {
        var parsed = parserService.parseLong(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var book = bookDao.fetchById(parsed);
        if (book != null) {
            var borrow = borrowDao.fetchById(book.getId());
            if (borrow != null) System.out.println(borrow);
            else System.err.println("Error: this book isn`t borrowed!");
        } else System.err.println("Error, this book is not exist!");
    }

    public void showAllBooksWithTheirBorrowers() {
        bookDao.fetchAll().forEach((book) -> {
            if (checkIfBookBorrowed(book)) {
                System.out.println(borrowDao.fetchById(book.getId()));
            } else System.out.println(book + "\t-\tavailable");
        });
    }

    /**
     * Show all readers in the list
     */
    public void showAllReadersWhitTheirBorrows() {
        readerDao.fetchAll().forEach((reader -> {
            var borrows = borrowDao.fetchAllById(reader.getId());
            if (borrows != null) borrows.forEach(System.out::println);
            else System.out.println(reader + "\t-\tno books borrowed");
        }));
    }
}
