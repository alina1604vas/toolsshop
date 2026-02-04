package org.example.tools.pageobject.flow;

import org.example.tools.SystemConfig;
import org.example.tools.pageobject.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class SignOut {
    private final String url = SystemConfig.getBaseUrl() + "/auth/login";
    private WebDriver driver;



    public SignOut(WebDriver driver) {
        this.driver = driver;
    }

    public SignOut open() {
        driver.get(url);
        PageFactory.initElements(driver, this);
        return this;
    }
}
