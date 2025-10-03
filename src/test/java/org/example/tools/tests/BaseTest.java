package org.example.tools.tests;

import net.datafaker.Faker;
import org.example.tools.driver.DriverSingleton;
import org.example.tools.infra.SprintCondition;
import org.example.tools.network.ChromeResponseListener;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;

@ExtendWith(SprintCondition.class)
public abstract class BaseTest {

    protected static WebDriver driver = DriverSingleton.getDriver();
    private static final DevTools devTools = ((HasDevTools) driver).getDevTools();

    protected static final ChromeResponseListener responseListener = new ChromeResponseListener(devTools);
    protected final Faker faker = new Faker();

}
