package com.bookslib.app.service;

import com.bookslib.app.dao.BookDao;
import com.bookslib.app.dao.BookDaoPostgresqlImpl;
import com.bookslib.app.dao.BorrowDao;
import com.bookslib.app.dao.ReaderDao;
import com.bookslib.app.entity.Book;
import com.bookslib.app.entity.Borrow;
import com.bookslib.app.entity.Reader;
import com.bookslib.app.exceptions.LibraryServiceException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * LibraryService class used to interaction with data and objects in online Library
 */
@Service
@Getter
@Setter
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class LibraryService {

    private BookDao bookDao;
    private ReaderDao readerDao;
    private BorrowDao borrowDao;
    private ParserService parserService;

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
        return readerDao.save(
                new Reader(newReaderName));
    }

    /**
     * Add new book to list
     */
    public Book addBook(String newBookData) {
        /*if (StringUtils.countMatches(newBookData, "/") != 1
                || StringUtils.equals("/", newBookData)
                || StringUtils.isBlank(newBookData)) {
            log.info("Input is incorrect!");
            throw new LibraryServiceException("Your input is incorrect, you need to write name and author separated by '/'!");
        }*/
        var inputSplit = newBookData.split("/");
        if (inputSplit[0].isBlank()) {
            throw new LibraryServiceException("Book name can not be an empty string!");
        } else if (inputSplit[1].isBlank()) {
            throw new LibraryServiceException("Book author can not be an empty string!");
        }
        return bookDao.save(
                new Book(
                        inputSplit[0].trim(),
                        inputSplit[1].trim()));
    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * add to borrowList if string not contains any characters other than numbers
     */
    public Optional<Borrow> borrowBook(String readerAndBookIds) {
        /*if (StringUtils.countMatches(readerAndBookIds, "/") != 1
                || StringUtils.equals("/", readerAndBookIds)
                || StringUtils.isBlank(readerAndBookIds)) {
            log.info("Input is incorrect!");
            throw new LibraryServiceException("Your input is incorrect, you need to write name and author separated by '/'!");
        }*/
        var inputSplit = readerAndBookIds.split("/");
        var readerId = parserService.parseLong(inputSplit[0].trim());
        var bookId = parserService.parseLong(inputSplit[1].trim());
        return borrowDao.save(bookId, readerId);
    }

    /**
     * @param readerAndBookIds Function call parser function, show error message
     *                         and return to menu if string of arguments contains any characters other than numbers
     *                         delete object from borrowList if string not contains any characters other than numbers
     * @return true if
     */
    public void returnBook(String readerAndBookIds) {
        /*if (StringUtils.countMatches(readerAndBookIds, "/") != 1
                || StringUtils.equals("/", readerAndBookIds)
                || StringUtils.isBlank(readerAndBookIds)) {
            log.info("Input is incorrect!");
            throw new LibraryServiceException("Your input is incorrect, you need to write name and author separated by '/'!");
        }*/
        var inputSplit = readerAndBookIds.split("/");
        var readerId = parserService.parseLong(inputSplit[0].trim());
        var bookId = parserService.parseLong(inputSplit[1].trim());
        if (borrowDao.returnBook(bookId, readerId) == 0) {
            log.error("Book or a Reader is not exists!");
            throw new LibraryServiceException("Book or a Reader is not exists!");
        }
    }

    /**
     * @param readerId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print all borrow objects by reader id
     */
    public List<Book> getAllBorrowedByReaderId(String readerId) {
        var parsed = parserService.parseLong(readerId.trim());
        return bookDao.findAllByReaderId(parsed);
    }

    /**
     * @param bookId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public List<Reader> getWhoBorrowByBookId(String bookId) {
        var parsed = parserService.parseLong(bookId.trim());
        return readerDao.findAllByBookId(parsed);
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
