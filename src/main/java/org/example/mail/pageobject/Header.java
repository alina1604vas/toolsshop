package org.example.mail.pageobject;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Header {

    private final String url = Dotenv.load().get("BASE_URL");

    @FindBy(linkText = "Home")
    private WebElement homeNavigationItem;

    @FindBy(css = ".nav-item.dropdown")
    private WebElement categoriesNavigationItem;

    @FindBy(linkText = "Contact")
    private WebElement contactNavigationItem;

    public Header(WebDriver driver) {
        driver.get(url);
        PageFactory.initElements(driver, this);
    }

    public boolean isHomeVisible() {
        return homeNavigationItem.isDisplayed();
    }

    public boolean isCategoriesVisible() {
        return categoriesNavigationItem.isDisplayed();
    }

    public boolean isContactVisible() {
       return contactNavigationItem.isDisplayed();
    }

}
