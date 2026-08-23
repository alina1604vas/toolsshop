package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.example.tools.utils.LoginScreen;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage implements LoginScreen {

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

    @FindBy(xpath = "//a[@data-test='forgot-password-link']")
    private WebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //TODO: remove duplication - PageFactory.initElements(driver, this);???
//    TODO: separate wait method
    public LoginPage openLogin() {
        driver.get(url);
        System.out.println("CURRENT URL: " + driver.getCurrentUrl());
        System.out.println("PAGE TITLE: " + driver.getTitle());
        System.out.println("PAGE SOURCE LENGTH: " + driver.getPageSource().length());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        PageFactory.initElements(driver, this);
        return this;
    }

    public boolean isLoginPageOpened() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.urlContains("auth/login"));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
    public AccountPage logIn(String email, String password) {
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        clickLogin();
        return new AccountPage(driver);
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

    public ForgotPasswordPage goToForgotPassword() {
        forgotPasswordLink.click();
        ForgotPasswordPage page = new ForgotPasswordPage(driver);
        page.waitUntilPageIsLoaded();
        return page;
    }
}
