package org.example.tools.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;
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
    boolean ci = isRunningInCi();
    boolean headless = Boolean.parseBoolean(System.getProperty("headless"));

    System.out.println("===== DRIVER DEBUG =====");
    System.out.println("CI = " + ci);
    System.out.println("headless property = " + System.getProperty("headless"));
    System.out.println("headless boolean = " + headless);
    System.out.println("=======================");

    ChromeOptions options = new ChromeOptions();

    if (ci || headless) {
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
    } else {
        options.addArguments("--start-maximized");
    }

    ChromeDriver driver = new ChromeDriver(options);
    logDriverVersions(driver);
    return driver;
}

    private static void logDriverVersions(ChromeDriver driver) {
        try {
            Capabilities caps = driver.getCapabilities();
            Object browserVersion = caps.getCapability("browserVersion");
            String chromeDriverVersion = "unknown";
            Object chrome = caps.getCapability("chrome");
            if (chrome instanceof Map) {
                Object cdv = ((Map<?, ?>) chrome).get("chromedriverVersion");
                if (cdv != null) {
                    chromeDriverVersion = cdv.toString();
                }
            }
            System.out.println("===== BROWSER VERSIONS =====");
            System.out.println("Chrome browserVersion = " + browserVersion);
            System.out.println("ChromeDriver version   = " + chromeDriverVersion);
            System.out.println("============================");
        } catch (Exception e) {
            System.out.println("[DriverProvider] could not read browser/driver versions: " + e);
        }
    }

    private static boolean isRunningInCi() {
        return Boolean.parseBoolean(System.getenv("CI"));
    }

}
