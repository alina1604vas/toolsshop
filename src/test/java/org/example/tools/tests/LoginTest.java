package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.LoginPage;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledForSprint(4)
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    public void setUpLoginPage() {
        loginPage = new LoginPage(driver);
    }

    @AfterEach
    public void cleanUp() {
        loginPage = null;
    }

    @Test
    @Tag("sprint4")
    @DisplayName("Logging with valid credentials")
    public void testLoginWithValidCredentials() {
        loginPage
                .setEmail("email@gmail.com")
                .setPassword("12345678")
                .clickLogin();
    }

    @Test
    @Tag("sprint4")
    @DisplayName("Logging with invalid format of credentials")
    public void testLoginWithInvalidFormatOfCredentials() {
        String validEmail = faker.internet().emailAddress();
        int type = new Random().nextInt(5);

        String invalidEmail = switch (type) {
            case 0 -> validEmail.replace("@", "");
            case 1 -> validEmail.replace(".", "");
            case 2 -> validEmail + " ";
            case 3 -> "@" + faker.lorem().word() + ".com";
            case 4 -> faker.lorem().characters(300) + "@test.com";
            default -> "plainaddress";
        };
        String invalidPassword = faker.internet().password(1, 2);
        loginPage
                .setEmail(invalidEmail)
                .setPassword(invalidPassword)
                .clickLogin();

        List<String> expectedMessages = List.of(
                "Email format is invalid",
                "Password length is invalid"
        );
        String actualEmailErrorMessage = loginPage.getEmailError();
        String actualPasswordErrorMessage = loginPage.getPasswordError();
        assertTrue(expectedMessages.contains(actualEmailErrorMessage));
        assertTrue(expectedMessages.contains(actualPasswordErrorMessage));
    }

    @Test
    @Tag("sprint4")
    @DisplayName("Logging with empty credentials")
    public void testLoginWithEmptyCredentials() {
        loginPage
                .setEmail("")
                .setPassword("")
                .clickLogin();
        String expectedErrorMessageForEmptyEmail = "Email is required";
        String expectedErrorMessageForEmptyPassword = "Password is required";
        String actualErrorMessageForEmptyEmail = loginPage.getEmailError();
        String actualErrorMessageForEmptyPassword = loginPage.getPasswordError();
        assertEquals(expectedErrorMessageForEmptyEmail, actualErrorMessageForEmptyEmail);
        assertEquals(expectedErrorMessageForEmptyPassword, actualErrorMessageForEmptyPassword);
    }

}
