package service;

import dao.*;
import entity.Book;
import entity.Borrow;
import entity.Reader;

import java.util.List;


/**
 * LibraryService class used to interaction with data and objects in online Library
 */
public class LibraryService {

    private BookDao bookDao;
    private ReaderDao readerDao;
    private BorrowDao borrowDao;
    private ParserService parserService;

    public LibraryService(BookDao bookDao, ReaderDao readerDao, BorrowDao borrowDao, ParserService parserService) {
        this.bookDao = bookDao;
        this.readerDao = readerDao;
        this.borrowDao = borrowDao;
        this.parserService = parserService;
    }

    public LibraryService() {
        this.bookDao = new BookDaoPostgresqlImpl();
        this.readerDao = new ReaderDaoPostgresqlImpl();
        this.borrowDao = new BorrowDaoPostgresqlImpl();
        this.parserService = new ParserService();
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public ReaderDao getReaderDao() {
        return readerDao;
    }

    public void setReaderDao(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    public BorrowDao getBorrowDao() {
        return borrowDao;
    }

    public void setBorrowDao(BorrowDao borrowDao) {
        this.borrowDao = borrowDao;
    }

    public ParserService getParserService() {
        return parserService;
    }

    public void setParserService(ParserService parserService) {
        this.parserService = parserService;
    }

    /**
     * Show all books in the list
     */
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    /**
     * Show all readers in the list
     */
    public List<Reader> getAllReaders() {
        return readerDao.findAll();
    }

    /**
     * Add new reader to list
     */
    public Reader registerReader(String newReaderName) {
        if (!parserService.checkString(newReaderName)) {
            return readerDao.save(new Reader(newReaderName));
        } else {
            throw new IllegalArgumentException("Failed to create new reader: reader name can not be an empty string!");
        }
    }

    /**
     * Add new book to list
     */
    public Book addBook(String newBookData) {
        var inputSplit = newBookData.split(" / ");
        if (parserService.checkSize(inputSplit) || parserService.checkString(inputSplit[0])
                || parserService.checkString(inputSplit[1])) {
            throw new IllegalArgumentException("Failed to create new book: your input is incorrect or an empty string!");
        } else {
            return bookDao.save(new Book(inputSplit[0], inputSplit[1]));
        }
    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * add to borrowList if string not contains any characters other than numbers
     */
    public Borrow borrowBook(String readerAndBookIds) {
        //Можно вынести в отдельную переменную, много дублирующегося кода.
        var inputSplit = readerAndBookIds.split(" / ");
        if (parserService.checkSize(inputSplit)) {
            throw new IllegalArgumentException("Error: enter a valid data of two arguments. Like this: 4 / 2!");
        }
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            throw new IllegalArgumentException("Error: enter only digits!");
        }
        var bookToBorrow = bookDao.findById(bookId);
        var readerToBorrow = readerDao.findById(readerId);
        if (bookToBorrow.isPresent() && readerToBorrow.isPresent()) {
            return borrowDao.save(bookId, readerId);
        } else {
            throw new IllegalArgumentException("Error: Book or a Reader is not exists!");
        }
    }

    /**
     * @param readerAndBookIds Function call parser function, show error message
     *                         and return to menu if string of arguments contains any characters other than numbers
     *                         delete object from borrowList if string not contains any characters other than numbers
     * @return
     */
    public boolean returnBook(String readerAndBookIds) {
        var inputSplit = readerAndBookIds.split(" / ");
        if (parserService.checkSize(inputSplit)) {
            throw new IllegalArgumentException("Error: enter a valid data. Like this: 1 / 3!");
        }
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            return false;
        }
        var bookToBorrow = bookDao.findById(bookId);
        var readerToBorrow = readerDao.findById(readerId);
        if (bookToBorrow.isPresent() && readerToBorrow.isPresent()) {
            borrowDao.returnBook(bookId, readerId);
            return true;
        } else {
            throw new IllegalArgumentException("Error: Book or a Reader is not exists!");
        }
    }

    /**
     * @param readerId Return to menu if string of arguments contains any symbols other than numbers or
     *                 print all borrow objects by reader id
     * @return
     */
    public List<Borrow> getAllBorrowedByReaderId(String readerId) {
        var parsed = parserService.parseLong(readerId);
        if (parsed == -1) {
            throw new IllegalArgumentException("Error: enter a valid data. Enter only digits!");
        }
        var reader = readerDao.findById(parsed);
        if (reader.isPresent()) {
            return borrowDao.findAllBorrowedByReaderId(reader.get().getId());
        } else {
            throw new IllegalArgumentException("Error, this reader is not exist!");
        }
    }

    /**
     * @param bookId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public List<Borrow> getWhoBorrowByBookId(String bookId) {
        var parsed = parserService.parseLong(bookId);
        if (parsed == -1) {
            throw new IllegalArgumentException("Error: enter a valid data. Enter only digits!");
        }
        var book = bookDao.findById(parsed);
        if (book.isPresent()) {
            return borrowDao.findAllBorrowedByBookId(book.get().getId());
        } else {
            throw new IllegalArgumentException("Error, this book is not exist!");
        }
    }

    /**
     *
     */
    public List<Borrow> getAllReadersWithTheirBorrows() {
        return borrowDao.findAllReadersWithTheirBorrows();
    }

    /**
     *
     */
    public List<Borrow> getAllBooksWithTheirBorrowers() {
        return borrowDao.findAllBooksWithTheirBorrowers();
    }
}
