package org.example.tools.pageobject.entity;

public class BillingAddress {

    private final String streetAddress;
    private final String city;
    private final String state;
    private final String country;
    private final String postCode;

    public BillingAddress(String streetAddress, String city, String state, String country, String postCode) {
        this.streetAddress = streetAddress != null ? streetAddress : "";
        this.city = city != null ? city : "";
        this.state = state != null ? state : "";
        this.country = country != null ? country : "";
        this.postCode = postCode != null ? postCode : "";
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
}
