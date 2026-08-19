package org.example.tools.tests;

import net.datafaker.Faker;
import org.example.tools.driver.DriverProvider;
import org.example.tools.network.api.ApiClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

    protected WebDriver driver;
    protected final Faker faker = new Faker();
    protected final ApiClient api = new ApiClient();

    @BeforeEach
    void initDriver() {
        driver = DriverProvider.get();
    }

    @AfterEach
    void tearDownDriver() {
        DriverProvider.remove();
        driver = null;
    }

}
