package org.example.mail;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.mail.driver.DriverSingleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;

public class BaseTest {
    private static final Dotenv dotenv = Dotenv.load();
    protected static WebDriver driver = DriverSingleton.getInstance();

//    @BeforeAll
//    public static void setup() {
//        String envBaseURL = dotenv.get("BASE_URL");
//        driver.get(envBaseURL);
//    }

    @AfterAll
    public static void tearDown() {
        DriverSingleton.closeDriver();
    }
}
