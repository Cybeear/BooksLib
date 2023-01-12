package exceptions;

public class ReaderRepositoryException extends RuntimeException {
    public ReaderRepositoryException(String errorMessage) {
        super(errorMessage);
    }
}
