package org.example.tools.utils;

import org.example.tools.pageobject.entity.BillingAddress;

public class Guest implements User {

    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private BillingAddress billingAddress;

    public String getFirstName() {
        return firstName;
    }

    public Guest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Guest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Guest setEmail(String email) {
        this.email = email;
        return this;
    }


    public Guest setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

}
