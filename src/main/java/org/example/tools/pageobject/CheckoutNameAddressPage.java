package org.example.tools.pageobject;

import org.example.tools.SystemConfig;
import org.openqa.selenium.WebDriver;

public class CheckoutNameAddressPage {
    private final String url = SystemConfig.getBaseUrl() + "checkout";
    private WebDriver driver;
}
