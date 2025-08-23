package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class RegistrationPage {

    private final String url = SystemConfig.getBaseUrl() + "/auth/register";

    @FindBy(id = "first_name")
    private WebElement firstName;

    @FindBy(css = "input[placeholder='Your last name *']")
    private WebElement lastName;

    @FindBy(id = "dob")
    private WebElement calendar;

    @FindBy(css = "input[placeholder='Your Street *']")
    private WebElement street;

    @FindBy(xpath = "//input[@formcontrolname='postal_code']")
    private WebElement postCode;

    @FindBy(xpath = "//input[@placeholder='Your City *']")
    private WebElement city;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(xpath = "//select[@data-test='country']")
    private WebElement country;

    @FindBy(css = "div.form-group.mb-3 label[for='phone']+input")
    private WebElement phone;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement buttonRegister;

    public RegistrationPage(WebDriver driver) {
        driver.get(url);
        PageFactory.initElements(driver, this);
    }

    public RegistrationPage setFirstName(String name) {
        firstName.sendKeys(name);
        return this;
    }

    public RegistrationPage setLastName(String surname) {
        lastName.sendKeys(surname);
        return this;
    }

    public RegistrationPage setBirthDate(String date) {
        calendar.click();
        calendar.sendKeys(date);
        return this;
    }

    public RegistrationPage setStreet(String streetName) {
        street.sendKeys(streetName);
        return this;
    }

    public RegistrationPage setPostCode(String postalCode) {
        postCode.sendKeys(postalCode);
        return this;
    }

    public RegistrationPage setCity(String cityName) {
        city.sendKeys(cityName);
        return this;
    }

    public RegistrationPage setState(String stateName) {
        state.sendKeys(stateName);
        return this;
    }

    public RegistrationPage setCountry(String countryName) {
        country.click();
        Select select = new Select(country);
        country.click();
        select.selectByVisibleText(countryName);
        return this;
    }

    public RegistrationPage setPhone(String phoneNumber) {
        phone.sendKeys(phoneNumber);
        return this;
    }

    public RegistrationPage setEmail(String userEmail) {
        email.sendKeys(userEmail);
        return this;
    }

    public RegistrationPage setPassword(String userPassword) {
        password.sendKeys(userPassword);
        return this;
    }

    public RegistrationPage clickRegisterButton() {
        buttonRegister.click();
        return this;
    }
}
