package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.driver.DriverSingleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class LoginPage {

    private final String url = SystemConfig.getBaseUrl() + "auth/login";
    private final WebDriver driver;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(className = "btnSubmit")
    private WebElement buttonLogin;

    @FindBy(xpath = "//div[@data-test='email-error']")
    private WebElement emailError;

    @FindBy(xpath = "//div[@data-test='password-error']")
    private WebElement passwordError;

    public LoginPage() {
        this.driver = DriverSingleton.getDriver();
        PageFactory.initElements(driver, this);
    }

    public LoginPage openLogin() {
        driver.get(url);
        return this;
    }

    public AccountPage logIn(String email, String password) {
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        clickLogin();
        return new AccountPage();
    }

    public LoginPage setPasswordInput(String password) {
        if (password != null) {
            passwordInput.clear();
            passwordInput.sendKeys(password);
        }
        return this;
    }

    public LoginPage setEmailInput(String email) {
        if (email != null) {
            emailInput.clear();
            emailInput.sendKeys(email);
        }
        return this;
    }

    public LoginPage clickLogin() {
        buttonLogin.click();
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getEmailError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(emailError));
        return emailError.getText();
    }

    public String getPasswordError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(passwordError));
        return passwordError.getText();
    }
}
