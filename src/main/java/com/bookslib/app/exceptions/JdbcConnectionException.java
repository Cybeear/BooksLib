package com.bookslib.app.exceptions;

public class JdbcConnectionException extends RuntimeException {
    public JdbcConnectionException(String errorMessage) {
        super(errorMessage);
    }
}
