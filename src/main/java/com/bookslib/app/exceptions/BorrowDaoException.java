package com.bookslib.app.exceptions;

public class BorrowDaoException extends ReaderDaoException {
    public BorrowDaoException(String errorMessage) {
        super(errorMessage);
    }
}
