package exceptions;

public class ReaderServiceException extends RuntimeException {
    public ReaderServiceException(String errorMessage) {
        super(errorMessage);
    }

}
