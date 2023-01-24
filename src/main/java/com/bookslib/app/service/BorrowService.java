package com.bookslib.app.service;

import com.bookslib.app.dao.BorrowDao;
import com.bookslib.app.entity.Borrow;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
@Slf4j
public class BorrowService {
    private final BorrowDao borrowDao;

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
    public int returnBook(long readerId, long bookId) {
            return borrowDao.returnBook(bookId, readerId);
    }

    public List<Borrow> getAll() {
        return borrowDao.findAll();
    }
}
