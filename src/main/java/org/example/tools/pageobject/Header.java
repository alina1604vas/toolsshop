package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Header {

//    private final String url = SystemConfig.getBaseUrl();
private final WebDriver driver;

    @FindBy(linkText = "Home")
    private WebElement homeNavigationItem;

    @FindBy(css = ".nav-item.dropdown")
    private WebElement categoriesNavigationItem;

    @FindBy(linkText = "Contact")
    private WebElement contactNavigationItem;

//    public Header(WebDriver driver) {
//        driver.get(url);
//        PageFactory.initElements(driver, this);
//    }
public Header(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
}

//    public boolean isHomeVisible() {
//        return homeNavigationItem.isDisplayed();
//    }
public boolean isHomeVisible() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    return wait.until(ExpectedConditions.visibilityOf(homeNavigationItem)).isDisplayed();
}

    public boolean isCategoriesVisible() {
        return categoriesNavigationItem.isDisplayed();
    }

    public boolean isContactVisible() {
       return contactNavigationItem.isDisplayed();
    }

}
