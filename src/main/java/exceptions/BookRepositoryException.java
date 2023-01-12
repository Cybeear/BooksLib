package exceptions;

public class BookRepositoryException extends RuntimeException {
    public BookRepositoryException(String errorMessage) {
        super(errorMessage);
    }
}
