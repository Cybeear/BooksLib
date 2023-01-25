package com.bookslib.app.service;

import com.bookslib.app.dao.BorrowDao;
import com.bookslib.app.entity.Borrow;
import com.bookslib.app.exceptions.BorrowServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
public class BorrowService {
    private final BorrowDao borrowDao;

    /**
     * @param borrow
     */
    public void borrowBook(Borrow borrow) throws BorrowServiceException {
        try {
            borrowDao.save(borrow);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            log.info("Trying to create duplicated data:\t {}", borrow);
            throw new BorrowServiceException("Trying to create duplicated data!");
        }
    }

    /**
     * @param borrow
     */
    public void returnBook(Borrow borrow) {
        borrowDao.returnBook(borrow);
    }
}
