package org.example.tools.utils;

import net.datafaker.Faker;

public class TestData {
    private static final Faker faker = new Faker();

    public static String validFirstName() {
        return faker.name().firstName();
    }

    public static String validLastName() {
        return faker.name().lastName();
    }

    public static String validEmail() {
        return faker.internet().emailAddress();
    }

    public static String validAddress() {
        return faker.address().fullAddress();
    }

    public static String validCity() {
        return faker.address().cityName();
    }

    public static String validState() {
        return faker.address().state();
    }

    public static String validCountry() {
        return faker.address().country();
    }
    public static String validPostCode() {
        return faker.address().postcode();
    }
}
