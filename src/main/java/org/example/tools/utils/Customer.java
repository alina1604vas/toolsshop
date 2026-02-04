package org.example.tools.utils;

import org.example.tools.pageobject.entity.BillingAddress;

public class Customer implements User {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final BillingAddress billingAddress;
    private final String birthDate;
    private final String phone;
    private final String password;

    private Customer(Builder builder) {
        this.firstName = builder.firstName != null ? builder.firstName : "";
        this.lastName = builder.lastName != null ? builder.lastName : "";
        this.email = builder.email != null ? builder.email : "";
        this.billingAddress = builder.billingAddress;
        this.birthDate = builder.birthDate != null ? builder.birthDate : "";
        this.phone = builder.phone != null ? builder.phone : "";
        this.password = builder.password != null ? builder.password : "";
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

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder {
        private final String firstName;
        private final String lastName;
        private final String email;
        private final BillingAddress billingAddress;
        private String birthDate = "";
        private String phone = "";
        private String password = "";

        public Builder(String firstName, String lastName, String email, BillingAddress billingAddress) {
            this.firstName = firstName != null ? firstName : "";
            this.lastName = lastName != null ? lastName : "";
            this.email = email != null ? email : "";
            this.billingAddress = billingAddress;
        }

        public Builder setBirthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
