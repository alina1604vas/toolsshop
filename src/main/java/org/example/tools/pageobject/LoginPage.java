package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class LoginPage {

    private final String url = SystemConfig.getBaseUrl() + "/auth/login";
    private final WebDriver driver;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(className = "btnSubmit")
    private WebElement buttonLogin;

    @FindBy(className = "help-block")
    private WebElement helpBlockError;

    @FindBy(id = "email-error")
    private WebElement emailError;

    @FindBy(id = "password-error")
    private WebElement passwordError;

    public LoginPage(WebDriver driver) {
        this.driver = driver;

        driver.get(url);
        PageFactory.initElements(driver, this);
    }

    public LoginPage setPassword(String pass) {
        password.sendKeys(pass);
        return this;
    }

    public LoginPage setEmail(String mail) {
        email.sendKeys(mail);
        return this;
    }

    public LoginPage clickLogin() {
        buttonLogin.click();
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getHelpBlockError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(helpBlockError));
        return helpBlockError.getText();
    }
    //Invalid email or password

    public String getEmailError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(emailError));
        return emailError.getText();
    }
    //Email is required - є
    //Email format is invalid =є

    public String getPasswordError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(passwordError));
        return passwordError.getText();
    }
    //Password is required - є
    //Password length is invalid = є

}
