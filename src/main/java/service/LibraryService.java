package service;

import dao.*;
import entity.Book;
import entity.Borrow;
import entity.Reader;
import exceptions.LibraryServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;


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
        if (newReaderName.isBlank()) {
            throw new LibraryServiceException("Reader name can not be an empty string!");
        }
        return readerDao.save(new Reader(newReaderName));
    }

    /**
     * Add new book to list
     */
    public Book addBook(String newBookData) {
        if (
                StringUtils.countMatches(newBookData, "/") != 1
                        || StringUtils.equals(" / ", newBookData)
                        || StringUtils.isBlank(newBookData)) {
            throw new LibraryServiceException("Your input is incorrect, you need to write name and author separated by '/'!");
        }
        var inputSplit = newBookData.split(" / ");
        if (inputSplit.length != 2) {
            throw new LibraryServiceException("You enter empty string!");
        } else if (inputSplit[0].isBlank()) {
            throw new LibraryServiceException("Book name can not be an empty string!");
        } else if (inputSplit[1].isBlank()) {
            throw new LibraryServiceException("Book author can not be an empty string!");
        }
        return bookDao.save(new Book(inputSplit[0], inputSplit[1]));
    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * add to borrowList if string not contains any characters other than numbers
     */
    public Optional<Borrow> borrowBook(String readerAndBookIds) {
        //Можно вынести в отдельную функцию, много дублирующегося кода.
        if (
                StringUtils.countMatches(readerAndBookIds, "/") != 1
                        || StringUtils.equals(" / ", readerAndBookIds)
                        || StringUtils.isBlank(readerAndBookIds)) {
            throw new LibraryServiceException("Your input is incorrect, you need to write name and author separated by '/'!");
        }
        var inputSplit = readerAndBookIds.split(" / ");
        if (inputSplit.length != 2) {
            throw new LibraryServiceException("You enter empty string!");
        } else if (inputSplit[0].isBlank()) {
            throw new LibraryServiceException("Reader id can not be an empty string!");
        } else if (inputSplit[1].isBlank()) {
            throw new LibraryServiceException("Book id can not be an empty string!!");
        }
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        return borrowDao.save(bookId, readerId);
    }

    /**
     * @param readerAndBookIds Function call parser function, show error message
     *                         and return to menu if string of arguments contains any characters other than numbers
     *                         delete object from borrowList if string not contains any characters other than numbers
     * @return true if
     */
    public void returnBook(String readerAndBookIds) {
        if (
                StringUtils.countMatches(readerAndBookIds, "/") != 1
                        || StringUtils.equals(" / ", readerAndBookIds)
                        || StringUtils.isBlank(readerAndBookIds)) {
            throw new LibraryServiceException("Your input is incorrect, you need to write name and author separated by '/'!");
        }
        var inputSplit = readerAndBookIds.split(" / ");
        if (inputSplit.length != 2) {
            throw new LibraryServiceException("You enter empty string!");
        } else if (inputSplit[0].isBlank()) {
            throw new LibraryServiceException("Reader id can not be an empty string!!");
        } else if (inputSplit[1].isBlank()) {
            throw new LibraryServiceException("Book id can not be an empty string!!");
        }
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        if (borrowDao.returnBook(bookId, readerId) == 0) {
            throw new LibraryServiceException("Book or a Reader is not exists!");
        }
    }

    /**
     * @param readerId Return to menu if string of arguments contains any symbols other than numbers or
     *                 print all borrow objects by reader id
     * @return
     */
    public List<Book> getAllBorrowedByReaderId(String readerId) {
        var parsed = parserService.parseLong(readerId);
        var books = bookDao.findAllByReaderId(parsed);
        if (books.isEmpty()) {
            throw new LibraryServiceException("This reader isn`t borrow books!");
        }
        return books;
    }

    /**
     * @param bookId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public List<Reader> getWhoBorrowByBookId(String bookId) {
        if (bookId.isBlank()) {
            throw new LibraryServiceException("You enter a empty string!");
        }
        var parsed = parserService.parseLong(bookId);
        var readers = readerDao.findAllByBookId(parsed);
        if (readers.isEmpty()) {
            throw new LibraryServiceException("This book isn`t borrowed!");
        }
        return readers;
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
