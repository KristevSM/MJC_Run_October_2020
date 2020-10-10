package by.kristev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void isAllPositiveNumbers() {
        assertTrue(Utils.isAllPositiveNumbers("12", "3"));
        assertTrue(Utils.isAllPositiveNumbers("1.3", "234324", "435.46"));
        assertFalse(Utils.isAllPositiveNumbers("34", "232423", "-0.999"));
        assertFalse(Utils.isAllPositiveNumbers("34", "-32.6", "1"));
        assertThrows(IllegalArgumentException.class, () -> {
            Utils.isAllPositiveNumbers("qwd12", "12");
        });
    }
}