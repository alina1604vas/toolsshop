package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountPage {

    private final String url = SystemConfig.getBaseUrl() + "account";
    private final WebDriver driver;

    @FindBy(xpath = "//h1[@data-test='page-title']")
    private WebElement accountHeader;

    public AccountPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public AccountPage openAccountPage() {
        driver.get(url);
        return this;
    }

    public boolean isPageLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("/account")); // wait for redirect
            return wait.until(ExpectedConditions.visibilityOf(accountHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
