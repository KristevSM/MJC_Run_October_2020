package com.epam.esm.validator;

import com.epam.esm.exception.InvalidInputDataException;
import lombok.experimental.UtilityClass;

import java.text.MessageFormat;

@UtilityClass
public final class ValidationUtils {

    public static boolean checkPaginationData(Long page, Long pageSize) {
        if (page <= 0) {
            throw new InvalidInputDataException(MessageFormat.format("Invalid input data. Page: {0}", page));
        } else if (pageSize <= 0) {
            throw new InvalidInputDataException(MessageFormat.format("Invalid input data. Page size: {0}", pageSize));
        }
        return true;
    }
}
