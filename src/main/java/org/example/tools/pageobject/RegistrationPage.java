package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.utils.Customer;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RegistrationPage {

    private final WebDriver driver;
    private final String url = SystemConfig.getBaseUrl() + "auth/register";

    @FindBy(css = "input[data-test='first-name']")
    private WebElement firstName;

    @FindBy(css = "input[placeholder='Your last name *']")
    private WebElement lastName;

    @FindBy(id = "dob")
    private WebElement calendar;

    @FindBy(css = "input[placeholder='Your Street *']")
    private WebElement address;

    @FindBy(xpath = "//input[@placeholder='Your Postcode *']")
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

    @FindBy(xpath = "//button[@data-test='register-submit']")
    private WebElement buttonRegister;

    @FindBy(xpath = "//h1[@data-test='page-title']")
    private WebElement accountHeader;

    @FindBy(id = "house_number")
    private WebElement houseNumber;

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

    public RegistrationPage setHouseNumber(String value) {
        houseNumber.clear();
        houseNumber.sendKeys(value);
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
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(buttonRegister));
        buttonRegister.click();
        return this;
    }

    public void registerUser(Customer user) {
        this.setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setBirthDate(user.getBirthDate())
                .setAddress(user.getBillingAddress().getStreetAddress())
                .setPostCode(user.getBillingAddress().getPostCode())
                .setHouseNumber(user.getBillingAddress().getHouseNumber())
                .setCity(user.getBillingAddress().getCity())
                .setState(user.getBillingAddress().getState())
                .setPhone(user.getPhone())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .clickRegisterButton();
    }
    public boolean isRegistrationSuccessful() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.urlContains("/auth/login"));
            return true;
        } catch (TimeoutException e) {
            System.out.println("\n=== REGISTRATION FAILED ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());

            // 1. All visible validation alerts
            List<WebElement> errors = driver.findElements(
                    By.cssSelector("[role='alert'], .alert-danger, .invalid-feedback"));
            System.out.println("Validation messages on page:");
            for (WebElement err : errors) {
                String txt = err.getText().trim();
                if (!txt.isEmpty()) System.out.println("  - " + txt);
            }

            // 2. Which fields are currently invalid (Angular adds 'is-invalid' class)
            List<WebElement> invalidFields = driver.findElements(
                    By.cssSelector(".is-invalid"));
            System.out.println("Invalid fields (" + invalidFields.size() + "):");
            for (WebElement field : invalidFields) {
                String id = field.getDomAttribute("id");
                String value = field.getDomAttribute("value");
                System.out.println("  - id=" + id + ", value='" + value + "'");
            }

            // 3. Is the submit button even enabled?
            try {
                WebElement btn = driver.findElement(
                        By.cssSelector("[data-test='register-submit']"));
                System.out.println("Submit button enabled? " + btn.isEnabled());
            } catch (NoSuchElementException ignored) {}

            // 4. Screenshot for inspection
            try {
                File ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Path target = Paths.get("target/register-fail-"
                        + System.currentTimeMillis() + ".png");
                Files.createDirectories(target.getParent());
                Files.copy(ss.toPath(), target);
                System.out.println("Screenshot saved: " + target.toAbsolutePath());
            } catch (IOException ioe) {
                System.out.println("Failed to save screenshot: " + ioe.getMessage());
            }
            System.out.println("===========================\n");

            throw e;
        }
    }
//    public boolean isRegistrationSuccessful() {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        //TODO: move to constants
//        return wait.until(ExpectedConditions.urlContains("/auth/login"));
//    }
    public List<WebElement> getAvailableCountries() {
        return new Select(country).getOptions().stream()
                .filter(o -> {
                    String v = o.getDomAttribute("value");
                    return v != null && !v.isBlank();
                })
                .toList();
    }

    public String chooseRandomCountry(List<WebElement> dropdownOptions) {
        int idx = ThreadLocalRandom.current().nextInt(dropdownOptions.size());
        WebElement option = dropdownOptions.get(idx);

        String countryCode = option.getDomAttribute("value");
        new Select(country).selectByValue(countryCode);
        return countryCode;
    }
//    public List<WebElement> getAvailableCountries() {
//        Select select = new Select(country);
//        return select.getOptions()
//                .stream()
//                .filter(option -> !option.getText().equals("Your country *"))
//                .toList();
//    }
//
//    public String chooseRandomCountry(List<WebElement> dropdownOptions) {
//        Random random = new Random();
//        int randomIndex = random.nextInt(dropdownOptions.size());
//        WebElement selectedOption = dropdownOptions.get(randomIndex);
//        new Select(country).selectByVisibleText(selectedOption.getText());
//        return selectedOption.getText();
//    }

    public String getValidationErrorForField(String fieldKey) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocatorForField(fieldKey)));
        return error.getText().replaceAll("\\s+", " ").trim();
    }

    private By errorLocatorForField(String fieldKey) {
        return By.cssSelector("div[data-test='" + fieldKey + "-error']");
    }
    private By inputLocator(String fieldKey) {
        return By.cssSelector("input[data-test='" + fieldKey + "']");
    }

    public void clearInputField (String fieldKey) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocator(fieldKey)));
        field.clear();
    }
}
