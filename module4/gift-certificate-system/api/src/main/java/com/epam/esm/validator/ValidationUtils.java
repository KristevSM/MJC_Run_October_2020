package com.epam.esm.validator;

import com.epam.esm.exception.InvalidInputDataException;
import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.Arrays;

@UtilityClass
public final class ValidationUtils {

    public static boolean checkPaginationData(int page, int pageSize) {
        if (page <= 0) {
            throw new InvalidInputDataException(MessageFormat.format("Invalid input data. Page: {0}", page));
        } else if (pageSize <= 0) {
            throw new InvalidInputDataException(MessageFormat.format("Invalid input data. Page size: {0}", pageSize));
        }
        return true;
    }

    public static boolean checkId(Long... ids) {
        for (Long id : ids)
            if (id == null) {
                throw new InvalidInputDataException("Missing value for the userId parameter");
            } else if (id <= 0) {
                throw new InvalidInputDataException("The given id must not be negative or zero");
            }
        return true;
    }

}
