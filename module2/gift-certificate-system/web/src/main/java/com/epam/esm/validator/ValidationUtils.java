package com.epam.esm.validator;

import com.epam.esm.exception.InvalidInputDataException;

import java.text.MessageFormat;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean checkPaginationData(int page, int pageSize) {
        if (page < 0) {
            throw new InvalidInputDataException(MessageFormat.format("Invalid input data. Page: {0}", page));
        } else if (pageSize <= 0) {
            throw new InvalidInputDataException(MessageFormat.format("Invalid input data. Page size: {0}", pageSize));
        }
        return true;
    }
}
