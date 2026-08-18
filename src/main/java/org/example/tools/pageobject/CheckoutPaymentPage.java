package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CheckoutPaymentPage {

    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    @FindBy(xpath = "//h3[text()=\"Payment\"]")
    private WebElement paymentHeader;

    @FindBy(id = "payment-method")
    private WebElement paymentMethodDropdown;

    @FindBy(id = "bank_name")
    private WebElement bankName;

    @FindBy(id = "account_name")
    private WebElement accountName;

    @FindBy(id = "account_number")
    private WebElement accountNumber;

    @FindBy(id = "credit_card_number")
    private WebElement creditCardNumber;

    @FindBy(id = "expiration_date")
    private WebElement expirationDate;

    @FindBy(id = "cvv")
    private WebElement cvv;

    @FindBy(id = "card_holder_name")
    private WebElement cardHolderName;

    @FindBy(id = "monthly_installments")
    private WebElement monthlyInstallments;

    @FindBy(id = "gift_card_number")
    private WebElement giftCardNumber;

    @FindBy(id = "validation_code")
    private WebElement validationCode;

    @FindBy(xpath = "//button[@data-test=\"finish\"]")
    private WebElement confirmButton;

    @FindBy(css = "[data-test='payment-success-message']")
    private WebElement paymentConfirmation;

    public CheckoutPaymentPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //do i need this method?
    public CheckoutPaymentPage open() {
        driver.get(url);
//        PageFactory.initElements(driver, this);
        return this;
    }

    public boolean isLoaded() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(paymentHeader))
                .isDisplayed();
    }

    public void setPaymentMethodDropdown(String method) {
        Select select = new Select(paymentMethodDropdown);
        select.selectByValue(paymentMethodValue(method));
    }

    private static String paymentMethodValue(String displayName) {
        switch (displayName) {
            case "Bank Transfer":
                return "bank-transfer";
            case "Cash on Delivery":
                return "cash-on-delivery";
            case "Credit Card":
                return "credit-card";
            case "Buy Now Pay Later":
                return "buy-now-pay-later";
            case "Gift Card":
                return "gift-card";
            default:
                throw new IllegalArgumentException("Unknown payment method: " + displayName);
        }
    }

    public void fillPaymentDetails(String method) {
        switch (method) {
            case "Bank Transfer":
                bankName.sendKeys("Test Bank");
                accountName.sendKeys(TestData.validAccountName());
                accountNumber.sendKeys(TestData.validAccountNumber());
                break;
            case "Cash on Delivery":
                break;
            case "Credit Card":
                creditCardNumber.sendKeys("4111-1111-1111-1111");
                expirationDate.sendKeys("12/2030");
                cvv.sendKeys("123");
                cardHolderName.sendKeys("John Doe");
                break;
            case "Buy Now Pay Later":
                new Select(monthlyInstallments).selectByValue("6");
                break;
            case "Gift Card":
                giftCardNumber.sendKeys("ABCD1234EFGH5678");
                validationCode.sendKeys("1234");
                break;
            default:
                throw new IllegalArgumentException("Unknown payment method: " + method);
        }
    }

    public void confirmPayment() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
    }

    public void waitConfirmationMesg() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(paymentConfirmation));
    }

    public boolean isConfirmationMsgPresent() {
        try {
            waitConfirmationMesg();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getPaymentConfirmationMsg() {
        waitConfirmationMesg();
        return paymentConfirmation.getText();
    }

    public String getPaymentErrorMessage() {
        List<WebElement> errors = driver.findElements(By.cssSelector("[data-test='payment-error-message']"));
        return errors.isEmpty() ? "" : errors.get(0).getText();
    }

    public OrderConfirmationPage confirmOrder() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
        return new OrderConfirmationPage(driver);
    }

}
