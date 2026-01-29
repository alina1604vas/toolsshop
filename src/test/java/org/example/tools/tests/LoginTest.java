package org.example.tools.tests;

import org.example.tools.driver.DriverSingleton;
import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.AccountPage;
import org.example.tools.pageobject.LoginPage;
import org.example.tools.utils.Credentials;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

@EnabledForSprint(4)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    public void setUpLoginPage() {
        WebDriver driver = DriverSingleton.getDriver();
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
        loginPage = new LoginPage();
        loginPage.openLogin();
    }

    @AfterEach
    public void cleanUp() {
        loginPage = null;
    }

    @Test
    @Tag("sprint4")
    @DisplayName("Logging with registered user")
    public void testLoginWithRegisteredUser() {
        AccountPage accountPage = loginPage
                        .logIn(Credentials.email(), Credentials.password());
        assertTrue(accountPage.isPageLoaded(), "User cannot log in");
    }

    @ParameterizedTest(name = "[{index}] email={0}")
    @CsvFileSource(
            resources = "/login_invalid_email.csv",
            numLinesToSkip = 1,
            emptyValue = "''"
    )

    @Tag("sprint4")
    @DisplayName("Logging with invalid or empty email")
    public void testLoginWithInvalidEmail (String email, String expectedEmailError) {
        loginPage
                .setEmailInput(email)
                .clickLogin();
        String actualEmailError = loginPage.getEmailError();
        assertEquals(expectedEmailError, actualEmailError, "Incorrect email error message");
    }

    @ParameterizedTest(name = "[{index}] password={0}")
    @CsvFileSource(
            resources = "/login_invalid_password.csv",
            numLinesToSkip = 1,
            emptyValue = "''"
    )
    @Tag("sprint4")
    @DisplayName("Logging with invalid or empty password")
    public void testLoginWithInvalidPassword (String password, String expectedPasswordError) {
        loginPage
                .setPasswordInput(password)
                .clickLogin();
        String actualPasswordError = loginPage.getPasswordError();
        assertEquals(expectedPasswordError, actualPasswordError, "Incorrect password error message");
    }
}
