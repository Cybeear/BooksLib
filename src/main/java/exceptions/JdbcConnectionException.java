package exceptions;

public class JdbcConnectionException extends RuntimeException {
    public JdbcConnectionException(String errorMessage) {
        super(errorMessage);
    }
}
