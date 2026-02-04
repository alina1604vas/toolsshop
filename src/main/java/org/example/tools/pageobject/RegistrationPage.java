package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.utils.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class RegistrationPage {

    private final WebDriver driver;
    private final String url = SystemConfig.getBaseUrl() + "auth/register";

    @FindBy(css = "input[data-test='first-name']")
    private WebElement firstName;

    @FindBy(css = "input[placeholder='Your last name *']")
    private WebElement lastName;

    @FindBy(id = "dob")
    private WebElement calendar;

    @FindBy(css = "input[placeholder='Your address *']")
    private WebElement address;

    @FindBy(xpath = "//input[@formcontrolname='postcode']")
    private WebElement postCode;

    @FindBy(xpath = "//input[@placeholder='Your city *']")
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

    @FindBy(xpath = "//h1[@data-test='page-title']")
    private WebElement accountHeader;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public RegistrationPage open() {
        driver.get(url);
        return this;
    }

    public RegistrationPage setFirstName(String name) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[data-test='first-name']")));
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

    public RegistrationPage setAddress(String address) {
        this.address.sendKeys(address);
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

    public void registerUser(User user) {
        this.setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setBirthDate(user.getBirthDate())
                .setAddress(user.getBillingAddress().getStreetAddress())
                .setPostCode(user.getBillingAddress().getPostCode())
                .setCity(user.getBillingAddress().getCity())
                .setState(user.getBillingAddress().getState())
                .setPhone(user.getPhone())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .clickRegisterButton();
    }

    public boolean isRegistrationSuccessful() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //TODO: move to constants
        return wait.until(ExpectedConditions.urlContains("/auth/login"));
    }

    public List<WebElement> getAvailableCountries() {
        Select select = new Select(country);
        return select.getOptions()
                .stream()
                .filter(option -> !option.getText().equals("Select your country *"))
                .toList();
    }

    public String chooseRandomCountry(List<WebElement> dropdownOptions) {
        Random random = new Random();
        int randomIndex = random.nextInt(dropdownOptions.size());
        WebElement selectedOption = dropdownOptions.get(randomIndex);
        new Select(country).selectByVisibleText(selectedOption.getText());
        return selectedOption.getText();
    }
}
