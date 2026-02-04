package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.utils.LoginScreen;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SuccessCustomerLogin implements LoginScreen {

    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    @FindBy (xpath = "//p[contains(normalize-space(.), 'You can proceed to checkout.')]")
    private WebElement successMsg;

    @FindBy (xpath = "//button[@data-test='proceed-2']")
    private WebElement proceedToCheckoutBtn;

    public SuccessCustomerLogin(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public SuccessCustomerLogin openCustomerLogin() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }

    //TODO: result is not used
    public boolean isSuccessCustomerLoginLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(successMsg));
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void clickProceedToCheckout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckoutBtn));
        button.click();
    }
}
