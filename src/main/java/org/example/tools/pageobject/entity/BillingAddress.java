package org.example.tools.pageobject.entity;

public class BillingAddress {

    private final String streetAddress;
    private final String city;
    private final String state;
    private final String country;
    private final String postCode;

    public BillingAddress() {
        this.streetAddress = "";
        this.city = "";
        this.state = "";
        this.country = "";
        this.postCode = "";
    }

    public BillingAddress(String streetAddress, String city, String state, String country, String postCode) {
        this.streetAddress = streetAddress != null ? streetAddress : "";
        this.city = city != null ? city : "";
        this.state = state != null ? state : "";
        this.country = country != null ? country : "";
        this.postCode = postCode != null ? postCode : "";
    }

    private BillingAddress(Builder builder) {
        this.streetAddress = builder.streetAddress != null ? builder.streetAddress : "";
        this.city = builder.city != null ? builder.city : "";
        this.state = builder.state != null ? builder.state : "";
        this.country = builder.country != null ? builder.country : "";
        this.postCode = builder.postCode != null ? builder.postCode : "";
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostCode() {
        return postCode;
    }

    public static class Builder {
        private String streetAddress = "";
        private String city = "";
        private String state = "";
        private String country = "";
        private String postCode = "";

        public Builder setStreetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setPostCode(String postCode) {
            this.postCode = postCode;
            return this;
        }

        public BillingAddress build() {
            return new BillingAddress(this);
        }
    }
}
