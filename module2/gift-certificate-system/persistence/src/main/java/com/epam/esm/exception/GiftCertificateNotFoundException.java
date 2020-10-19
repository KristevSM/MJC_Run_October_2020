package com.epam.esm.exception;

public class GiftCertificateNotFoundException extends RuntimeException {
    public GiftCertificateNotFoundException() {
        super();
    }

    public GiftCertificateNotFoundException(String message) {
        super(message);
    }

    public GiftCertificateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
