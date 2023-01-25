package com.bookslib.app.exceptions;

public class BorrowServiceException extends RuntimeException {
    public BorrowServiceException(String errorMessage) {
        super(errorMessage);
    }
}

