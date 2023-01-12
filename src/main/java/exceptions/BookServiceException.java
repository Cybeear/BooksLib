package exceptions;

public class BookServiceException extends RuntimeException {
    public BookServiceException(String errorMessage) {
        super(errorMessage);
    }

}
