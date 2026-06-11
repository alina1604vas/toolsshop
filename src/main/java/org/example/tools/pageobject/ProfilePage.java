package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.utils.Customer;
import org.example.tools.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {
    private final String url = SystemConfig.getBaseUrl() + "account/profile";
    private WebDriver driver;

    @FindBy(id = "first_name")
    private WebElement firstName;

    @FindBy(id = "last_name")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "phone")
    private WebElement phone;

    @FindBy(id = "street")
    private WebElement street;

    @FindBy(id = "postal_code")
    private WebElement postalCode;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(id = "country")
    private WebElement country;

    @FindBy(xpath = "//button[@data-test='update-profile-submit']")
    private WebElement updateProfileBtn;
    @FindBy(id = "current-password")
    private WebElement currentPasswordField;

    @FindBy(id = "new-password")
    private WebElement newPasswordField;

    @FindBy(id = "new-password-confirm")
    private WebElement confirmNewPasswordField;

    @FindBy(xpath = "//button[@data-test='change-password-submit']")
    private WebElement changePasswordBtn;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public ProfilePage openProfilePage() {
        driver.get(url);
        return this;
    }

    public void waitUntilPageIsLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("account/profile"));
        wait.until(d -> !d.findElement(By.id("first_name"))
                .getDomProperty("value").isBlank());
    }

    public String getFirstName() {
        return firstName.getDomProperty("value");
    }

    public String getLastName() {
        return lastName.getDomProperty("value");
    }

    public String getEmail() {
        return email.getDomProperty("value");
    }

    public String getCountry() {
        return country.getDomProperty("value");
    }

    public String getCity() {
        return city.getDomProperty("value");
    }

    public String getStreet() {
        return street.getDomProperty("value");
    }

    public String getPhone() {
        return phone.getDomProperty("value");
    }

    public String getPostCode() {
        return postalCode.getDomProperty("value");
    }

    public String getState() {
        return state.getDomProperty("value");
    }

    private boolean isEditable(WebElement field) {
        String readonly = field.getDomAttribute("readonly");
        return field.isDisplayed()
                && field.isEnabled()
                && readonly == null;
    }

    private boolean isReadOnly(WebElement field) {
        return field.getDomAttribute("readonly") != null;
    }

    public boolean isFirstNameEditable() {
        return isEditable(firstName);
    }

    public boolean isLastNameEditable() {
        return isEditable(lastName);
    }

    public boolean isPhoneEditable() {
        return isEditable(phone);
    }

    public boolean isStreetEditable() {
        return isEditable(street);
    }

    public boolean isCityEditable() {
        return isEditable(city);
    }

    public boolean isCountryEditable() {
        return isEditable(country);
    }

    public boolean isEmailNotEditable() {
        return isReadOnly(email);
    }

    public String getSuccessMsg() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        return wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector(".alert-success, [role='alert'], .toast-success")))
                .getText();
    }

    private void replaceValue(WebElement field, String value) {
        field.clear();
        field.sendKeys(value);
    }

    public ProfilePage clickUpdateProfileBtn() {
        updateProfileBtn.click();
        return this;
    }

    public ProfilePage setFirstName(String value) {
        replaceValue(firstName, value);
        return this;
    }

    public ProfilePage setLastName(String value) {
        replaceValue(lastName, value);
        return this;
    }

    public ProfilePage setPhone(String value) {
        replaceValue(phone, value);
        return this;
    }

    public ProfilePage setStreet(String value) {
        replaceValue(street, value);
        return this;
    }

    public ProfilePage setPostCode(String value) {
        replaceValue(postalCode, value);
        return this;
    }

    public ProfilePage setCity(String value) {
        replaceValue(city, value);
        return this;
    }

    public ProfilePage setState(String value) {
        replaceValue(state, value);
        return this;
    }

    public ProfilePage setCountry(String value) {
        replaceValue(country, value);
        return this;
    }

    public ProfilePage updateProfile(Customer customer) {
        return setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName())
                .setPhone(customer.getPhone())
                .setStreet(customer.getBillingAddress().getStreetAddress())
                .setPostCode(customer.getBillingAddress().getPostCode())
                .setCity(customer.getBillingAddress().getCity())
                .setState(customer.getBillingAddress().getState())
                .setCountry(customer.getBillingAddress().getCountry());
    }

    public ProfilePage clickChangePasswordBtn() {
        changePasswordBtn.click();
        return this;
    }

    public ProfilePage setCurrentPassword(String currentPassword) {
        currentPasswordField.sendKeys(currentPassword);
        return this;
    }

    public ProfilePage setNewPassword(String newPassword) {
        newPasswordField.sendKeys(newPassword);
        return this;
    }

    public ProfilePage confirmNewPassword(String newPassword) {
        confirmNewPasswordField.sendKeys(newPassword);
        return this;
    }

    public ProfilePage changePassword(String currentPassword, String validNewPassword) {
        validNewPassword = TestData.validPassword();
        return setCurrentPassword(currentPassword)
                .setNewPassword(validNewPassword)
                .confirmNewPassword(validNewPassword)
                .clickChangePasswordBtn();
    }

}

