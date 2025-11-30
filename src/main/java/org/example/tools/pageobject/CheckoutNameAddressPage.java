package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutNameAddressPage {
    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    @FindBy (id = "first_name")
    private WebElement firstName;

    @FindBy (id = "email")
    private WebElement email;

    @FindBy (id = "address")
    private WebElement address;

    @FindBy (id = "city")
    private WebElement city;

    @FindBy (id = "state")
    private WebElement state;

    @FindBy (id = "country")
    private WebElement country;

    @FindBy (id = "postcode")
    private WebElement postcode;

    public CheckoutNameAddressPage (WebDriver driver) {
        this.driver = driver;
    }

    public CheckoutNameAddressPage open() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }
}
