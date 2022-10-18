package exceptions;

public class LibraryServiceException extends RuntimeException {
    public LibraryServiceException(String errorMessage) {
        super(errorMessage);
    }
}
