package service;

import dao.*;
import entity.Book;
import entity.Borrow;
import entity.Reader;


/**
 * LibraryService class used to interaction with data and objects in online Library
 */
public class LibraryService {

    private BookDao bookDao;
    private ReaderDao readerDao;
    private BorrowDaoPostgresqlImpl borrowDao;
    private ParserService parserService;

    public LibraryService() {
        this.bookDao = new BookDaoPostgresqlImpl();
        this.readerDao = new ReaderDaoPostgresqlImpl();
        this.borrowDao = new BorrowDaoPostgresqlImpl();
        this.parserService = new ParserService();
    }

    /**
     * Show all books in the list
     */
    public void showAllBooks() {
        bookDao.findAll().forEach(System.out::println);
    }

    /**
     * Show all readers in the list
     */
    public void showAllReaders() {
        readerDao.findAll().forEach(System.out::println);
    }

    /**
     * Add new reader to list
     */
    public void registerReader(String str) {
        if (!str.equals(" ")) System.out.println(readerDao.save(new Reader(str)) + " successful registered!");
        else System.err.println("Error: enter a valid data!");
    }

    /**
     * Add new book to list
     */
    public void addBook(String str) {
        var inputSplit = str.split(" / ");
        if (parserService.checkSize(inputSplit) || inputSplit[0].equals(" ") || inputSplit[1].equals(" ")) {
            System.err.println("Error: Please enter new book name and author separated by '/'. Like this: name / author!");
            return;
        }
        System.out.println(bookDao.save(new Book(inputSplit[0], inputSplit[1])) + " successful added!");
    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * add to borrowList if string not contains any characters other than numbers
     */
    public void borrowABook(String str) {
        var inputSplit = str.split(" / ");
        if (parserService.checkSize(inputSplit)) {
            System.err.println("Error: enter a valid data. Like this: 4 / 2!");
            return;
        }
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            System.err.println("Error: enter a valid data. Like this: 4 / 2!");
            return;
        }
        var bookToBorrow = bookDao.findById(bookId);
        var readerToBorrow = readerDao.findById(readerId);
        if (bookToBorrow.isPresent() && readerToBorrow.isPresent()) {
            var borrow = new Borrow(bookToBorrow.get(), readerToBorrow.get());
            borrowDao.save(bookId, readerId);
            System.out.println(borrow);
        } else System.err.println("Error: data is not exists!");
    }

    /**
     * @param str Function call parser function, show error message
     *            and return to menu if string of arguments contains any characters other than numbers
     *            delete object from borrowList if string not contains any characters other than numbers
     */
    public void returnBorrowedBook(String str) {
        var inputSplit = str.split(" / ");
        if (parserService.checkSize(inputSplit)) {
            System.err.println("Error: enter a valid data. Like this: 1 / 3!");
            return;
        }
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            System.err.println("Error: enter a valid data. Like this: 1 / 3!");
            return;
        }
        var bookToBorrow = bookDao.findById(bookId);
        var readerToBorrow = readerDao.findById(readerId);
        if (bookToBorrow.isPresent() && readerToBorrow.isPresent()) {
            borrowDao.returnBook(bookId, readerId);
            System.out.println(bookToBorrow.get() + " is returned by " + readerToBorrow.get());
        } else System.err.println("Error: data is not exists");
    }

    /**
     * @param str Return to menu if string of arguments contains any symbols other than numbers or
     *            print all borrow objects by reader id
     */
    public void showAllBorrowedByReaderId(String str) {
        var parsed = parserService.parseLong(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data. Enter only digits!");
            return;
        }
        var reader = readerDao.findById(parsed);
        if (reader.isPresent()) {
            var borrows = borrowDao.findAllBorrowedByReaderId(reader.get().getId());
            if (borrows.size() > 0) borrows.forEach(System.out::println);
        } else System.err.println("Error, this reader is not exist!");
    }

    /**
     * @param str Return to menu if string of arguments contains any symbols other than numbers or
     *            print who borrow book by book id
     */
    public void showWhoBorrowByBookId(String str) {
        var parsed = parserService.parseLong(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data. Enter only digits!");
            return;
        }
        var book = bookDao.findById(parsed);
        if (book.isPresent()) {
            var borrows = borrowDao.findAllBorrowedByBookId(book.get().getId());
            if (borrows.size() > 0) borrows.forEach(System.out::println);
            else System.err.println("Error: this book isn`t borrowed!");
        } else System.err.println("Error, this book is not exist!");
    }

    /**
     *
     */
    public void showAllReadersWithTheirBorrows() {
        borrowDao.findAllReadersWithTheirBorrows().forEach(System.out::println);
    }

    /**
     *
     */
    public void showAllBooksWithTheirBorrowers() {
        borrowDao.findAllBooksWithTheirBorrowers().forEach(System.out::println);
    }
}
