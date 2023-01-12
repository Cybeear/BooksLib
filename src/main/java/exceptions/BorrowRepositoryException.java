package exceptions;

public class BorrowRepositoryException extends ReaderRepositoryException {
    public BorrowRepositoryException(String errorMessage) {
        super(errorMessage);
    }
}
