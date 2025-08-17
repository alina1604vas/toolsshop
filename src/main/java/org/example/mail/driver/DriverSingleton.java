package org.example.mail.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverSingleton {

    private static WebDriver driver;
    private static final Dotenv dotenv = Dotenv.load();

    private DriverSingleton() {
    }

    public static WebDriver getInstance() {
        if (driver == null) {
            String browser = dotenv.get("BROWSER", "chrome").toLowerCase();
            switch (browser) {
//                case "edge":
//                    WebDriverManager.edgedriver().setup();
//                    driver = new EdgeDriver();
//                    break;
//                case "firefox":
//                    WebDriverManager.firefoxdriver().setup();
//                    driver = new FirefoxDriver();
//                    break;
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    driver = new ChromeDriver(options);
                    break;
            }
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
