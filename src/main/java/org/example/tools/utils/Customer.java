package org.example.tools.utils;

import org.example.tools.pageobject.entity.BillingAddress;

public class Customer extends User {

    private Customer(Builder builder) {
        super(
                builder.firstName,
                builder.lastName,
                builder.email,
                builder.birthDate,
                builder.phone,
                builder.password,
                builder.billingAddress
        );
    }

    public static class Builder {
        private String firstName = "";
        private String lastName = "";
        private String email = "";
        private String birthDate = "";
        private String phone = "";
        private String password = "";
        private BillingAddress billingAddress;

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
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

        public Builder setBillingAddress(BillingAddress billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
