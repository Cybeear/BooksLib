package com.bookslib.app.exceptions;

public class BookDaoException extends RuntimeException {
    public BookDaoException(String errorMessage) {
        super(errorMessage);
    }
}
