package com.bookslib.app.exceptions;

public class ReaderDaoException extends RuntimeException {
    public ReaderDaoException(String errorMessage) {
        super(errorMessage);
    }
}
