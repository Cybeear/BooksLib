package com.bookslib.app.exceptions;

public class InternalErrorException extends RuntimeException {
    public InternalErrorException(String message) {
        super(message);
    }
}
