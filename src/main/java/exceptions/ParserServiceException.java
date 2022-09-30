package exceptions;

public class ParserServiceException extends RuntimeException {
    public ParserServiceException(String errorMessage) {
        super(errorMessage);
    }
}
