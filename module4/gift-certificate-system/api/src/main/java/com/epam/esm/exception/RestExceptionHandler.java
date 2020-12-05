package com.epam.esm.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal exception", ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(status, "Malformed JSON request", ex);

        return new ResponseEntity(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>(new ApiError(status, "no handler found", ex), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError(status, "method arg not valid", ex);
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("The input parameter is not valid");
        apiError.setDebugMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    protected ResponseEntity<Object> handleGiftCertificateNotFound(GiftCertificateNotFoundException ex,
                                                                      WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex);
        apiError.setMessage("The Gift Certificate not found");
        apiError.setDebugMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    @ExceptionHandler(TagNotFoundException.class)
    protected ResponseEntity<Object> handleTagNotFound(TagNotFoundException ex,
                                                                   WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex);
        apiError.setMessage("The Tag not found");
        apiError.setDebugMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    protected ResponseEntity<Object> handleOrderNotFound(OrderNotFoundException ex,
                                                       WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex);
        apiError.setMessage("The Order not found");
        apiError.setDebugMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex,
                                                         WebRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex);
        apiError.setMessage("The User not found");
        apiError.setDebugMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    @ExceptionHandler(DaoException.class)
    protected ResponseEntity<Object> handleDaoException(DaoException ex,
                                                        WebRequest request) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex);
        apiError.setMessage("DaoException");
        apiError.setDebugMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidInputDataException.class)
    protected ResponseEntity<Object> handleTagNotFound(InvalidInputDataException ex,
                                                       WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex);
        apiError.setMessage("Invalid input data");
        apiError.setDebugMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }


    @ExceptionHandler(JwtAuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(JwtAuthenticationException ex,
                                                       WebRequest request) {
        ApiError apiError = new ApiError(FORBIDDEN, ex);
        apiError.setMessage("Authentication failed");
        apiError.setDebugMessage(ex.getMessage());

        return new ResponseEntity<>(apiError, FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex,
                                                       WebRequest request) {
        ApiError apiError = new ApiError(FORBIDDEN, ex);
        apiError.setMessage("Access denied");
        apiError.setDebugMessage("You don't have access to view the resource");

        return new ResponseEntity<>(apiError, FORBIDDEN);
    }


}
