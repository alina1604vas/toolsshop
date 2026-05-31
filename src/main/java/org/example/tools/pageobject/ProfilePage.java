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

public class ProfilePage {
    private final String url = SystemConfig.getBaseUrl() + "account/profile";
    private WebDriver driver;

    @FindBy(id = "first_name")
    private WebElement firstName;

    @FindBy(id = "last_name")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "phone")
    private WebElement phone;

    @FindBy(id = "street")
    private WebElement street;

    @FindBy(id = "postal_code")
    private WebElement postalCode;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(id = "country")
    private WebElement country;

    @FindBy(xpath = "//button[@data-test='update-profile-submit'")
    private WebElement updateProfileBtn;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public ProfilePage openProfilePage() {
        driver.get(url);
        return this;
    }

    public void waitUntilPageIsLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/account/profile"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[@data-test='page-title']")));
    }

    public void clickUpdateProfileBtn() {
        updateProfileBtn.click();
    }
}

