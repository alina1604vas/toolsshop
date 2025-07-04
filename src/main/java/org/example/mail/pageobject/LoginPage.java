package org.example.mail.pageobject;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.mail.driver.DriverSingleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class LoginPage {

    private final String url = Dotenv.load().get("BASE_URL") + "/auth/login";
    private WebDriver driver = DriverSingleton.getInstance();//це треба?

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(className = "btnSubmit")
    private WebElement buttonLogin;

    @FindBy(className = "help-block")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
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

    public String getErrorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));//драйвер був червоний
        wait.until(ExpectedConditions.visibilityOf(errorMessage));
        return errorMessage.getText();
    }
}
