package org.example.mail.driver;

public class Customer {

    private String firstName = "Alina";
    private String lastName = "Test";
    private String dateOfBirth = "16041992";
    private String street = "Zelena";
    private String postalCode = "12345";
    private String city = "Vinnytsia";
    private String state = "Vinnytsia";
    private String country = "Ukraine";
    private String phone = "123456789010";
    private String email = "alina1604vas@gmail.com";
    private String password = "Dwelon1234!";

    public Customer() {
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
