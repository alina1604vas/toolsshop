package org.example.tools.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
public class DriverProvider {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverProvider() {
    }

    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            driver = createDriver();
            DRIVER.set(driver);
        }
        return driver;
    }

    public static void remove() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

//    private static WebDriver createDriver() {
//        ChromeOptions options = new ChromeOptions();
//
//        boolean ci = isRunningInCi();
//        if (ci) {
//            options.addArguments("--headless=new");
//            options.addArguments("--window-size=1920,1080");
//            options.addArguments("--no-sandbox");
//            options.addArguments("--disable-dev-shm-usage");
//        }
//
//        WebDriver driver = new ChromeDriver(options);
//        if (!ci) {
//            driver.manage().window().maximize();
//        }
//        return driver;
//    }
private static WebDriver createDriver() {
    ChromeOptions options = new ChromeOptions();

    boolean ci = isRunningInCi();
    boolean headless = Boolean.parseBoolean(System.getProperty("headless"));

    if (ci || headless) {
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
    } else {
        options.addArguments("--start-maximized");
    }

    return new ChromeDriver(options);
}

    private static boolean isRunningInCi() {
        return Boolean.parseBoolean(System.getenv("CI"));
    }

}
