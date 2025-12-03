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

public class CheckoutPaymentPage {
    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    @FindBy(xpath = "//h3[text()=\"Payment\"]")
    private WebElement paymentHeader;

    @FindBy(id = "payment-method")
    private WebElement paymentMethodDropdown;

    @FindBy(id = "account-name")
    private WebElement accountName;

    @FindBy(id = "account-number")
    private WebElement accountNumber;

    @FindBy(xpath = "//button[@data-test=\"finish\"]")
    private WebElement confirmButton;

    @FindBy(css = ".help-block")
    private WebElement paymentConfirmation;

    public CheckoutPaymentPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //do i need this method?
    public CheckoutPaymentPage open() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }

    public boolean isLoaded() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(paymentHeader))
                .isDisplayed();
    }

    public void setPaymentMethodDropdown(String method) {
        Select select = new Select(paymentMethodDropdown);
        select.selectByVisibleText(method);
    }

    public void enterValidAccountName() {
        accountName.sendKeys(TestData.validAccountName());
    }

    public void enterValidAccountNumber() {
        accountNumber.sendKeys(TestData.validAccountNumber());
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

    public OrderConfirmationPage confirmOrder() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
        return new OrderConfirmationPage(driver);
    }
}
