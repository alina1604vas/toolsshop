package org.example.tools.pageobject;

import jdk.incubator.vector.VectorOperators;
import org.example.tools.SystemConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.example.tools.utils.TestData;

public class CheckoutNameAddressPage {
    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;

    @FindBy(id = "first_name")
    private WebElement firstName;

    @FindBy(id = "last_name")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "address")
    private WebElement address;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(id = "country")
    private WebElement country;

    @FindBy(id = "postcode")
    private WebElement postcode;

    public CheckoutNameAddressPage(WebDriver driver) {
        this.driver = driver;
    }

    public CheckoutNameAddressPage open() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }

//    public void waitUntilPageIsLoaded () {
//
//    }

    public void enterFirstName() {
        firstName.sendKeys(TestData.validFirstName());
    }

    public void enterLastName() {
        lastName.sendKeys(TestData.validLastName());
    }

    public void enterEmail() {
        email.sendKeys(TestData.validEmail());
    }

    public void enterAddress() {
        address.sendKeys(TestData.validAddress());
    }

    public void enterCity() {
        city.sendKeys(TestData.validCity());
    }

    public void enterState() {
        state.sendKeys(TestData.validState());
    }

    public void enterCountry() {
        country.sendKeys(TestData.validCountry());
    }

    public void enterPostCode() {
        postcode.sendKeys(TestData.validPostCode());
    }
}
