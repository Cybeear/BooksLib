package com.bookslib.app.service;

import com.bookslib.app.dao.ReaderDao;
import com.bookslib.app.entity.Reader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
@Slf4j
public class ReaderService {
    private final ReaderDao readerDao;

    /**
     * Show all readers in the list
     */
    public List<Reader> getAllReaders() {
        return readerDao.findAll();
    }

    public Optional<Reader> getReaderByReaderId(long readerId){
        return readerDao.findById(readerId);
    }
    /**
     * Add new reader to list
     */
    public Reader registerReader(Reader reader) {
        return readerDao.save(reader);
    }

    /**
     * @param bookId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public List<Reader> getWhoBorrowByBookId(long bookId) {
        return readerDao.findAllByBookId(bookId);
    }


}
