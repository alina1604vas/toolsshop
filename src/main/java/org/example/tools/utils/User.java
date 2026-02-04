package org.example.tools.utils;

import org.example.tools.pageobject.entity.BillingAddress;

public abstract class User {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String birthDate;
    private final String phone;
    private final String password;
    private final BillingAddress billingAddress;

    protected User(String firstName, String lastName, String email, String birthDate, String phone, String password, BillingAddress billingAddress) {
        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
        this.email = email != null ? email : "";
        this.birthDate = birthDate != null ? birthDate : "";
        this.phone = phone != null ? phone : "";
        this.password = password != null ? password : "";
        this.billingAddress = billingAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

}
