package org.example.tools.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;

public class ScreenshotOnFailureExtension implements TestWatcher {

    private final WebDriver driver;

    public ScreenshotOnFailureExtension(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        AllureAttachments.takeScreenshot(driver);
    }

}
