package org.example.mail;

import org.example.mail.driver.Customer;
import org.example.mail.pageobject.LoginPage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @DisplayName("Logging with valid credentials")
    public void testLoginWithValidCredentials() {
        Customer customer = new Customer();
        loginPage
                .setEmail(customer.getEmail())
                .setPassword(customer.getPassword())
                .clickLogin();
    }

    @Test
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
