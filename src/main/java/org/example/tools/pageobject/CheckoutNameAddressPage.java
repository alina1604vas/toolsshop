package org.example.tools.pageobject;
import org.example.tools.SystemConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    @FindBy(xpath = "//h3[text()='Name']")
    private WebElement header;

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
        PageFactory.initElements(driver, this);
    }

    public void waitUntilPageIsLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));//was first name-> because of billing address PO
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

    public WebElement waitError(String fieldName) {
        WebElement field = getFieldByName(fieldName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
         return wait.until(ExpectedConditions.visibilityOf(field));
    }

    public By errorLocator(String fieldName) {
        return By.xpath("//input[@formcontrolname='" + fieldName + "']/parent::div/following-sibling::div[contains(@class,'alert-danger')]");
    }

    public String getErrorText(String fieldName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocator(fieldName)));
        return error.getText();
}

public void triggerValidation(String fieldName) {
    WebElement field = getFieldByName(fieldName);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    wait.until(ExpectedConditions.visibilityOf(field));

    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].focus();", field);
    js.executeScript("arguments[0].blur();", field);
}
    private WebElement getFieldByName(String fieldName) {
        switch (fieldName) {
            case "first_name": return firstName;
            case "last_name": return lastName;
            default: throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
    }

}
