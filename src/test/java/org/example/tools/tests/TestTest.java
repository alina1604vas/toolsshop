package org.example.tools.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTest {

    @Test
    @Tag("smoke")
    void add_shouldReturnFour() {
        int result = 3 + 3;

        System.out.println("2 + 2 = " + result);

        assertEquals(6, result);
    }
}
