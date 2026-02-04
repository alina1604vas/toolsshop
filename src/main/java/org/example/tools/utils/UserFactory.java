package org.example.tools.utils;

import org.example.tools.pageobject.entity.BillingAddress;

public class UserFactory {

    public User create(Type type) {
        return switch (type) {
            case GUEST -> createGuest();
            case REGULAR -> createRegular(null);
        };
    }

    public User createRegularWith(String selectedCountry) {
        return createRegular(selectedCountry);
    }

    private Guest createGuest() {
        return new Guest.Builder()
                .setFirstName(TestData.validFirstName())
                .setLastName(TestData.validLastName())
                .setEmail(TestData.validEmail())
                .setBillingAddress(billingAddressFor(null))
                .build();
    }

    private Customer createRegular(String selectedCountry) {
        return new Customer.Builder()
                .setFirstName(TestData.validFirstName())
                .setLastName(TestData.validLastName())
                .setBirthDate(TestData.validBirthday())
                .setBillingAddress(billingAddressFor(selectedCountry))
                .setPhone(TestData.validPhone())
                .setEmail(TestData.validEmail())
                .setPassword(TestData.validPassword())
                .build();
    }

    private BillingAddress billingAddressFor(String selectedCountry) {
        return TestData.validBillingAddress(selectedCountry);
    }

    public enum Type { GUEST, REGULAR }
}
