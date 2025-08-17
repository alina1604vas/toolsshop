package org.example.mail;

import net.datafaker.Faker;
import org.example.mail.driver.DriverSingleton;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public abstract class BaseTest {

    protected static WebDriver driver = DriverSingleton.getInstance();
    protected static DevTools devTools = ((HasDevTools) driver).getDevTools();

    protected final Faker faker = new Faker();
    Date birthdayDate = faker.date().birthday(18, 65);
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    String birthdayString = formatter.format(birthdayDate);

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
