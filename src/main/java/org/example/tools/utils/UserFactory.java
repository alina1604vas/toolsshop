package org.example.tools.utils;

import org.example.tools.pageobject.entity.BillingAddress;

public class UserFactory {

    public Guest createGuest() {
        return new Guest(
                TestData.validFirstName(),
                TestData.validLastName(),
                TestData.validEmail(),
                billingAddressFor(null)
        );
    }

    public Customer createCustomer(String selectedCountry) {
        Customer.Builder builder = new Customer.Builder(
                TestData.validFirstName(),
                TestData.validLastName(),
                TestData.validEmail(),
                billingAddressFor(selectedCountry)
        );
        builder.setBirthDate(TestData.validBirthday())
                .setPhone(TestData.validPhone())
                .setPassword(TestData.validPassword());
        return builder.build();
    }

    private BillingAddress billingAddressFor(String selectedCountry) {
        return TestData.validBillingAddress(selectedCountry);
    }
}
