package org.example.tools.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTest {

    @Test
    void add_shouldReturnFour() {
        int result = 2 + 2;

        System.out.println("2 + 2 = " + result);

        assertEquals(4, result);
    }
}
