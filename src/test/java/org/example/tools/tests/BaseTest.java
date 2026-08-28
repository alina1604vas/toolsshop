package org.example.tools.tests;

import net.datafaker.Faker;
import org.example.tools.driver.DriverProvider;
import org.example.tools.network.ChromeResponseListener;
import org.example.tools.network.NetworkDiagnosticsLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;

public abstract class BaseTest {

    protected WebDriver driver;
//    private DevTools devTools;

//    protected ChromeResponseListener responseListener;

    // TEMP diagnostic: logs the browser's real network traffic to the console. Remove when done.
    private NetworkDiagnosticsLogger networkLogger;

    protected final Faker faker = new Faker();

    @BeforeEach
    void initDriver() {
        driver = DriverProvider.get();
//        devTools = ((HasDevTools) driver).getDevTools();
//        responseListener = new ChromeResponseListener(devTools);

        DevTools devTools = ((HasDevTools) driver).getDevTools();
        networkLogger = new NetworkDiagnosticsLogger(devTools);
        networkLogger.start();
    }

    @AfterEach
    void tearDownDriver() {
//        if (responseListener != null) {
//            responseListener.destroy();
//        }
        if (networkLogger != null) {
            networkLogger.stop();
        }
        DriverProvider.remove();
        driver = null;
//        devTools = null;
//        responseListener = null;
        networkLogger = null;
    }

}
