package by.kristev.utils;

import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {

    private StringUtils() {
    }

    public static boolean isPositiveNumber(String str) {
        if (NumberUtils.isCreatable(str))
            return (NumberUtils.createDouble(str) > 0.0D);
        else {
            throw new IllegalArgumentException("It's not a number: " + str);
        }
    }
}