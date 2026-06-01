package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
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

    @FindBy(xpath = "//button[@data-test='update-profile-submit'")
    private WebElement updateProfileBtn;

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

    public void clickUpdateProfileBtn() {
        updateProfileBtn.click();
    }
}

