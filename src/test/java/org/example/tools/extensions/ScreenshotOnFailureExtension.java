package org.example.tools.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

public class ScreenshotOnFailureExtension implements TestWatcher {

    private final Supplier<WebDriver> driverSupplier;

    public ScreenshotOnFailureExtension(Supplier<WebDriver> driverSupplier) {
        this.driverSupplier = driverSupplier;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        WebDriver driver = driverSupplier.get();
        if (driver != null) {
            AllureAttachments.takeScreenshot(driver);
        }
    }

}
