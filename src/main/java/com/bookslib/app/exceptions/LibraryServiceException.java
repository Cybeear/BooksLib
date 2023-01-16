package com.bookslib.app.exceptions;

public class LibraryServiceException extends RuntimeException {
    public LibraryServiceException(String errorMessage) {
        super(errorMessage);
    }
}
