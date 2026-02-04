package org.example.tools.utils;

import net.datafaker.Faker;
import org.example.tools.pageobject.entity.BillingAddress;

import java.text.SimpleDateFormat;

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

    public static String validAccountName() {
        return faker.name().fullName();
    }

    public static String validAccountNumber() {
        return faker.number().digits(10);
    }

    public static String validPhone() {
        return faker.numerify("##########");
    }

    public static String validPassword() {
        return faker.internet().password(8, 12, true, true);
    }

    public static String validBirthday() {
        return new SimpleDateFormat("yyyy-MM-dd").format(faker.date().birthday(18, 65));
    }

    public static BillingAddress validBillingAddress() {
        return validBillingAddress(null);
    }

    public static BillingAddress validBillingAddress(String country) {
        return new BillingAddress(
                faker.address().streetAddress(),
                faker.address().cityName(),
                faker.address().state(),
                country != null && !country.isEmpty() ? country : faker.address().country(),
                faker.address().postcode());
    }

    public static User lastRegisteredUser;
    public static final Credentials realUserCreds = new Credentials();

}

