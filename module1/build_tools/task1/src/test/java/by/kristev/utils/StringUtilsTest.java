package by.kristev.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void isPositiveNumber() {
        assertTrue(StringUtils.isPositiveNumber("12"));
        assertTrue(StringUtils.isPositiveNumber("1.3"));
        assertFalse(StringUtils.isPositiveNumber("-0.999"));
        assertFalse(StringUtils.isPositiveNumber("-1"));
        assertThrows(IllegalArgumentException.class, () -> {
            StringUtils.isPositiveNumber("qwd12");
        });
    }
}