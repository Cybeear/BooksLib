package com.bookslib.app.exceptions;

public class BorrowDaoException extends RuntimeException {
    public BorrowDaoException(String errorMessage) {
        super(errorMessage);
    }
}
