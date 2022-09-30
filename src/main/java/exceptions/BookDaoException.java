package exceptions;

public class BookDaoException extends RuntimeException {
    public BookDaoException(String errorMessage) {
        super(errorMessage);
    }
}
