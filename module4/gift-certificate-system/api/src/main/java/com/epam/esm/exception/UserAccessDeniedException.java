package com.epam.esm.exception;

import org.springframework.security.access.AccessDeniedException;

public class UserAccessDeniedException extends AccessDeniedException {
    private static final long serialVersionUID = -109652403422026062L;

    public UserAccessDeniedException(String msg) {
        super(msg);
    }
}
