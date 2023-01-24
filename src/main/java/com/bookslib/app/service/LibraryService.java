package com.bookslib.app.service;

import com.bookslib.app.dao.BookDao;
import com.bookslib.app.dao.BorrowDao;
import com.bookslib.app.dao.ReaderDao;
import com.bookslib.app.entity.Borrow;
import com.bookslib.app.entity.Reader;
import com.bookslib.app.exceptions.LibraryServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * LibraryService class used to interaction with data and objects in online Library
 */
@Service
@Data
@Slf4j
public class LibraryService {
    private final BookDao bookDao;
    private final ReaderDao readerDao;
    private final BorrowDao borrowDao;


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
        var reader = new Reader();
        reader.setName(newReaderName);
        return readerDao.save(reader);
    }

    /**
     * @param readerId
     * @param bookId
     * @return
     */
    public Optional<Borrow> borrowBook(long readerId, long bookId) {
        return borrowDao.save(bookId, readerId);
    }

    /**
     * @param readerId
     * @param bookId
     * @return
     */
    public void returnBook(long readerId, long bookId) {
        if (borrowDao.returnBook(bookId, readerId) == 0) {
            log.error("Book or a Reader is not exists!");
            throw new LibraryServiceException("Book or a Reader is not exists!");
        }
    }


    /**
     * @param bookId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public List<Reader> getWhoBorrowByBookId(long bookId) {
        return readerDao.findAllByBookId(bookId);
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
