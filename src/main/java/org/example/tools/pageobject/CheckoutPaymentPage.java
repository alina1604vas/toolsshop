package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPaymentPage {
    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    @FindBy(xpath = "//h3[text()=\"Payment\"]")
    private WebElement paymentHeader;

    @FindBy(id = "payment-method")
    private WebElement paymentMethod;

    @FindBy(id = "account-name")
    private WebElement accountName;

    @FindBy(id = "account-number")
    private WebElement accountNumber;

    public CheckoutPaymentPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isLoaded() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(paymentHeader))
                .isDisplayed();
    }
}
