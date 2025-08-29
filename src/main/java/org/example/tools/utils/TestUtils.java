package org.example.tools.utils;

import java.util.Random;

public class TestUtils {

    public static int getRandomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min should not be greater than max");
        }
        return new Random().nextInt(max - min + 1) + min;
    }

}
