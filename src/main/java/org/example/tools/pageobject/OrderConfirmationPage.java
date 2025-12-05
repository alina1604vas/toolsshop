package org.example.tools.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderConfirmationPage {
    private WebDriver driver;
    @FindBy(id = "order-confirmation")
    private WebElement orderConfirmationMsg;

    public OrderConfirmationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    public boolean isOrderConfirmationLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(orderConfirmationMsg));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
public String getOrderConfirmationMsg() {
        return orderConfirmationMsg.getText();
}
}
