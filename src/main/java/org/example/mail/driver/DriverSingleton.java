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

//System.out.println("➡ URL: " + response.getUrl());
//        System.out.println("   Status: " + response.getStatus());
//        System.out.println("   MIME Type: " + response.getMimeType());
//        System.out.println("-----------------------------------------");

//➡ URL: https://api-v2.practicesoftwaretesting.com/categories/tree
//Status: 200
//MIME Type: application/json
//-----------------------------------------
//        ➡ URL: https://api-v2.practicesoftwaretesting.com/brands
//Status: 200
//MIME Type: application/json
//-----------------------------------------
//        ➡ URL: https://api-v2.practicesoftwaretesting.com/products?page=1
//Status: 200
//MIME Type: application/json