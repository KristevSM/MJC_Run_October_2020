package by.kristev.utils;

import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {

    private StringUtils() {
    }

    public static boolean isPositiveNumber(String str) {
        if (NumberUtils.isCreatable(str))
            return (NumberUtils.createDouble(str).doubleValue() > 0.0D);
        throw new IllegalArgumentException("It's not a number: " + str);
    }
}