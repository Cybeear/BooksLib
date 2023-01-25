package com.bookslib.app.api;

import com.bookslib.app.entity.ApiError;
import com.bookslib.app.entity.ApiValidationError;
import com.bookslib.app.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ApiError> handlerNoHandlerFoundException(NoHandlerFoundException ex) {
        var apiError = new ApiError(String.format("It`s invalid URL: %s",
                ex.getRequestURL()));

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var error = "Malformed JSON request";
        var apiError = new ApiError(error, ex);

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ApiError apiError = new ApiError(String.format("Could not find the %s method for URL %s",
                ex.getMethod(),
                ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex) {
        String error = ex.getParameterName() + " parameter is missing";
        var apiError = new ApiError(error, ex);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" - media type is not supported. Supported media types are :");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        var apiError = new ApiError(builder.substring(0, builder.length() - 2), ex);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(apiError);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        var apiError = new ApiError("Request body contains invalid values!");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var apiError = new ApiError("Request body contains invalid values!");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        var apiError = new ApiError(String.format("Request type of argument '%s', you are entered %s",
                ex.getRequiredType(),
                ex.getValue()
                        .getClass()), ex);
        apiError.setSubErrors(
                List.of(new ApiValidationError(ex.getName(),
                        ex.getValue(),
                        ex.getLocalizedMessage())));
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({BorrowDaoException.class})
    public ResponseEntity<ApiError> handleBorrowDaoException(BorrowDaoException borrowDaoException) {
        var apiError = new ApiError(borrowDaoException.getLocalizedMessage());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({BorrowServiceException.class})
    public ResponseEntity<ApiError> handleBorrowServiceException(BorrowServiceException borrowServiceException) {
        var apiError = new ApiError(borrowServiceException.getLocalizedMessage());
        return ResponseEntity.badRequest().body(apiError);
    }


    @ExceptionHandler({BookDaoException.class})
    public ResponseEntity<ApiError> handleBookDaoException(BookDaoException bookDaoException) {
        var apiError = new ApiError(bookDaoException.getLocalizedMessage());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({ReaderDaoException.class})
    public ResponseEntity<ApiError> handleReaderDaoException(ReaderDaoException readerDaoException) {
        var apiError = new ApiError(readerDaoException.getLocalizedMessage());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({InternalErrorException.class})
    public ResponseEntity<ApiError> handleInternalErrorException(InternalErrorException internalErrorException) {
        var apiError = new ApiError("Server internal error!" + internalErrorException.getLocalizedMessage());
        return ResponseEntity.internalServerError().body(apiError);
    }
}
