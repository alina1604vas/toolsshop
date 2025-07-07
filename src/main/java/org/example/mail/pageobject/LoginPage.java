package org.example.mail.pageobject;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class LoginPage {

    private final String url = Dotenv.load().get("BASE_URL") + "/auth/login";
    private final WebDriver driver;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(className = "btnSubmit")
    private WebElement buttonLogin;

    @FindBy(className = "help-block")
    private WebElement errorMessageForInvalidCredentials;

    @FindBy(id = "email-error")
    private WebElement errorMessageForEmptyEmail;

    @FindBy(id = "password-error")
    private WebElement errorMessageForEmptyPassword;

    public LoginPage(WebDriver driver) {
        this.driver = driver;

        driver.get(url);
        PageFactory.initElements(driver, this);
    }

    public LoginPage setPassword (String pass) {
        password.sendKeys(pass);
        return this;
    }

    public LoginPage setEmail (String mail) {
        email.sendKeys(mail);
        return this;
    }

    public LoginPage clickLogin () {
        buttonLogin.click();
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getErrorMessageForInvalidCredentials() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(errorMessageForInvalidCredentials));
        return errorMessageForInvalidCredentials.getText();
    }

    public String getErrorMessageForEmptyEmail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(errorMessageForEmptyEmail));
        return errorMessageForEmptyEmail.getText();
    }

    public String getErrorMessageForEmptyPassword() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(errorMessageForEmptyPassword));
        return errorMessageForEmptyPassword.getText();
    }

}
