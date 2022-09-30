package exceptions;

public class BorrowDaoException extends ReaderDaoException {
    public BorrowDaoException(String errorMessage) {
        super(errorMessage);
    }
}
