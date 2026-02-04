package org.example.tools.utils;

import org.example.tools.pageobject.entity.BillingAddress;

public class Guest implements User {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final BillingAddress billingAddress;

    public Guest(String firstName, String lastName, String email, BillingAddress billingAddress) {
        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
        this.email = email != null ? email : "";
        this.billingAddress = billingAddress;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public BillingAddress getBillingAddress() {
        return billingAddress;
    }
}
