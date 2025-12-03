package org.example.tools.pageobject;
import org.example.tools.SystemConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.example.tools.utils.TestData;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutNameAddressPage {
    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    @FindBy(id = "first_name")
    private WebElement firstName;

    @FindBy(id = "last_name")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "address")
    private WebElement address;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(id = "country")
    private WebElement country;

    @FindBy(id = "postcode")
    private WebElement postcode;

    @FindBy(xpath = "//button[@data-test=\"proceed-3\"]")
    private WebElement proceedToCheckoutBtn;

    public CheckoutNameAddressPage(WebDriver driver) {
        this.driver = driver;
    }

    //do i need this method?
    public CheckoutNameAddressPage open() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }

    public void waitUntilPageIsLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(firstName));
    }

    public void enterFirstName() {
        firstName.sendKeys(TestData.validFirstName());
    }

    public void enterLastName() {
        lastName.sendKeys(TestData.validLastName());
    }

    public void enterEmail() {
        email.sendKeys(TestData.validEmail());
    }

    public void enterAddress() {
        address.sendKeys(TestData.validAddress());
    }

    public void enterCity() {
        city.sendKeys(TestData.validCity());
    }

    public void enterState() {
        state.sendKeys(TestData.validState());
    }

    public void enterCountry() {
        country.sendKeys(TestData.validCountry());
    }

    public void enterPostCode() {
        postcode.sendKeys(TestData.validPostCode());
    }

    public CheckoutPaymentPage clickProceedToCheckoutButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckoutBtn)).click();
        return new CheckoutPaymentPage(driver);
    }

}
