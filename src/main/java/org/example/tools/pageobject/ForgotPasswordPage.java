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

public class ForgotPasswordPage {
    private final String url = SystemConfig.getBaseUrl() + "auth/forgot-password";
    private WebDriver driver;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(className = "btnSubmit")
    private WebElement btnSubmit;

    private By emailErrorMsg = By.cssSelector("[data-test='email-error']");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    public ForgotPasswordPage open() {
        driver.get(url);
        waitUntilPageIsLoaded();
        return this;
    }

    public void waitUntilPageIsLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("auth/forgot-password"));
        wait.until(ExpectedConditions.visibilityOf(emailInput));
    }

    public ForgotPasswordPage clickSubmitBtn () {
        WebDriverWait wait = new WebDriverWait(driver,
                Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(btnSubmit));
        btnSubmit.click();
        return this;
    }

    public String getEmailErrorMsg() {
        return new WebDriverWait(
                driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(emailErrorMsg))
                .getText();
    }
}
