package com.epam.esm.exception;

/**
 * @author Sergei Kristev
 */
public class InvalidInputDataException extends RuntimeException {
    public InvalidInputDataException() {
        super();
    }

    public InvalidInputDataException(String message) {
        super(message);
    }

    public InvalidInputDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
