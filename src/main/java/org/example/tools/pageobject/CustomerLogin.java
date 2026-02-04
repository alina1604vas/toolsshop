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

public class CustomerLogin implements LoginScreen {

    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(className = "btnSubmit")
    private WebElement buttonLogin;


    public CustomerLogin(WebDriver driver) {
        this.driver = driver;
    }

    public CustomerLogin openCustomerLogin() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }

    public boolean isCustomerLoginLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(emailInput));
            return true;
        } catch (Exception e) {
            return false;
        }
        //p[text()='Hello Jane Doe, you are already logged in. You can proceed to checkout.']")));
    }

    public SuccessCustomerLogin logIn(String email, String password) {
        emailInput.clear();
        emailInput.sendKeys(email);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        clickLoginBtn();
        return new SuccessCustomerLogin(driver);
    }

    public CustomerLogin clickLoginBtn() {
        buttonLogin.click();
        return this;
    }

}
