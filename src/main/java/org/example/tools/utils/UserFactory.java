package org.example.tools.utils;

public class UserFactory {

    public static Customer create(UserType userType) {
        return switch (userType) {
            case GUEST -> guest();
            case REGISTERED -> guest();
            default -> guest();
        };
    }
//??
    private static Customer guest() {
        return new Customer.Builder()
                .setFirstName(TestData.validFirstName())
                .setLastName(TestData.validLastName())
                .setBirthDate(TestData.validBirthday())
                .setBillingAddress(
                        TestData.validBillingAddress(null)
                )
                .setPhone(TestData.validPhone())
                .setEmail(TestData.validEmail())
                .setPassword(TestData.validPassword())
                .build();
    }

    public static Customer randomUser(String selectedCountry) {
        return new Customer.Builder()
                .setFirstName(TestData.validFirstName())
                .setLastName(TestData.validLastName())
                .setBirthDate(TestData.validBirthday())
                .setBillingAddress(
                        TestData.validBillingAddress(selectedCountry)
                )
                .setPhone(TestData.validPhone())
                .setEmail(TestData.validEmail())
                .setPassword(TestData.validPassword())
                .build();
    }
}
