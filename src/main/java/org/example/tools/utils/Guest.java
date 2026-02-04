package org.example.tools.utils;

import org.example.tools.pageobject.entity.BillingAddress;

public class Guest extends User {

    private Guest(Builder builder) {
        super(
                builder.firstName,
                builder.lastName,
                builder.email,
                "",
                "",
                "",
                builder.billingAddress
        );
    }

    public static class Builder {
        private String firstName = "";
        private String lastName = "";
        private String email = "";
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

        public Builder setBillingAddress(BillingAddress billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public Guest build() {
            return new Guest(this);
        }
    }
}
