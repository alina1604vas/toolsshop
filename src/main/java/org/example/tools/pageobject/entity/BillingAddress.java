package org.example.tools.pageobject.entity;

public class BillingAddress {

    private final String country;
    private final String postCode;
    private final String houseNumber;
    private final String streetAddress;
    private final String city;
    private final String state;

    public BillingAddress(String country,
                          String postCode,
                          String houseNumber,
                          String streetAddress,
                          String city,
                          String state) {
        this.country       = country       != null ? country       : "";
        this.postCode      = postCode      != null ? postCode      : "";
        this.houseNumber   = houseNumber   != null ? houseNumber   : "";
        this.streetAddress = streetAddress != null ? streetAddress : "";
        this.city          = city          != null ? city          : "";
        this.state         = state         != null ? state         : "";
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getHouseNumber() {
        return houseNumber;
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
