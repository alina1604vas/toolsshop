package org.example.mail;

import io.github.cdimascio.dotenv.Dotenv;
import net.datafaker.Faker;
import org.example.mail.driver.DriverSingleton;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {

    private static final Dotenv dotenv = Dotenv.load();
    protected static WebDriver driver = DriverSingleton.getInstance();

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
