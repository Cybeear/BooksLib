package com.bookslib.app.exceptions;

public class ParserServiceException extends RuntimeException {
    public ParserServiceException(String errorMessage) {
        super(errorMessage);
    }
}
