package org.example.mail;

import org.example.mail.driver.Customer;
import org.example.mail.pageobject.LoginPage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @DisplayName("Logging with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        loginPage
                .setEmail("test604vas@gmail.com")
                .setPassword("dffrt")
                .clickLogin();
        String expectedErrorMessage = "Invalid email or password";
        String actualErrorMessage = loginPage.getErrorMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
}
