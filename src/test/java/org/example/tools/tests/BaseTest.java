package org.example.tools.tests;

import net.datafaker.Faker;
import org.example.tools.driver.DriverProvider;
import org.example.tools.network.ChromeResponseListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;

public abstract class BaseTest {

    protected WebDriver driver;
//    private DevTools devTools;

//    protected ChromeResponseListener responseListener;
    protected final Faker faker = new Faker();

    @BeforeEach
    void initDriver() {
        driver = DriverProvider.get();
//        devTools = ((HasDevTools) driver).getDevTools();
//        responseListener = new ChromeResponseListener(devTools);
    }

    @AfterEach
    void tearDownDriver() {
//        if (responseListener != null) {
//            responseListener.destroy();
//        }
        DriverProvider.remove();
        driver = null;
//        devTools = null;
//        responseListener = null;
    }

}
